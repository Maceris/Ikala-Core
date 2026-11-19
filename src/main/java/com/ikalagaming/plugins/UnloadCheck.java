package com.ikalagaming.plugins;

import com.sun.management.HotSpotDiagnosticMXBean;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A debugging check that an unloaded plugin really left memory. Turned on with {@code
 * -Dikala.unloadCheck=true}, it waits a moment after each unload, asks for garbage collection, and
 * then reports whether the plugin's class loader was freed. If it wasn't, it lists threads still
 * running the plugin's code; anything else holding it (a static field in another plugin, a listener
 * list, a cache) needs a heap dump to find.
 *
 * <p>Garbage collection doesn't clear soft references unless memory runs low, and the JDK keeps
 * some to a plugin's classes, such as the method handles behind its records' {@code equals} and
 * {@code hashCode}, so a plugin can linger until then without anything holding it for good. Run
 * with {@code -XX:SoftRefLRUPolicyMSPerMB=0}, which clears them at every collection, to rule that
 * out; the warning says when the check ran without it.
 *
 * <p>It runs on its own thread so unloading isn't slowed down, and asking for garbage collection
 * causes a pause, so it is off by default.
 *
 * @author Ches Burks
 */
@Slf4j
final class UnloadCheck {
    /** The system property that turns the check on. */
    static final String PROPERTY = "ikala.unloadCheck";

    /** How long to wait before the first garbage collection, for threads to wind down. */
    private static final long FIRST_WAIT_MILLIS = 2000;

    /** How many times to collect garbage before deciding the plugin is stuck in memory. */
    private static final int ATTEMPTS = 5;

    /** How long to wait between attempts. */
    private static final long ATTEMPT_WAIT_MILLIS = 500;

    /** The JVM option that makes every collection clear soft references when it is 0. */
    private static final String SOFT_POLICY = "SoftRefLRUPolicyMSPerMB";

    /** What the warning adds when soft references may be what still holds the plugin. */
    private static final String SOFT_HINT =
            " It may only be held by soft references, like the JDK's method handle caches, which"
                    + " are cleared when memory runs low; run with -XX:"
                    + SOFT_POLICY
                    + "=0 to rule that out.";

    /**
     * Whether the check is turned on.
     *
     * @return True if the property is set to true.
     */
    static boolean enabled() {
        return Boolean.getBoolean(PROPERTY);
    }

    /**
     * Check, in the background, that a plugin's class loader is freed, if the check is on.
     *
     * @param plugin The plugin that unloaded.
     * @param loader Its class loader, which must not be referenced from here on by the caller.
     */
    static void watch(@NonNull String plugin, @NonNull ClassLoader loader) {
        if (!enabled()) {
            return;
        }
        final WeakReference<ClassLoader> reference = new WeakReference<>(loader);
        final String loaderName = loader.getName();
        Thread checker =
                new Thread(() -> check(plugin, reference, loaderName), "Unload check " + plugin);
        checker.setDaemon(true);
        // Not the plugin's loader, which this thread would otherwise keep alive
        checker.setContextClassLoader(ClassLoader.getPlatformClassLoader());
        checker.start();
    }

    /**
     * Wait for the class loader to be collected, and report the result.
     *
     * @param plugin The plugin.
     * @param reference The class loader.
     * @param loaderName The class loader's name, which stack frames from it carry.
     */
    private static void check(
            String plugin, WeakReference<ClassLoader> reference, String loaderName) {
        try {
            Thread.sleep(FIRST_WAIT_MILLIS);
            for (int i = 0; i < ATTEMPTS && reference.get() != null; ++i) {
                System.gc(); // NOSONAR: the point of this check is to see what collection frees
                Thread.sleep(ATTEMPT_WAIT_MILLIS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        if (reference.get() == null) {
            log.info("Unload check: {} was freed from memory", plugin);
            return;
        }
        List<String> threads = threadsRunning(loaderName, reference.get());
        final String hint = softReferencesKept() ? SOFT_HINT : "";
        if (threads.isEmpty()) {
            log.warn(
                    "Unload check: {} is still in memory after unloading, but no thread is running"
                            + " its code. Something else holds it, like a static field, a"
                            + " listener or a cache; a heap dump will show what.{}",
                    plugin,
                    hint);
        } else {
            log.warn(
                    "Unload check: {} is still in memory after unloading. Threads still using"
                            + " it: {}.{}",
                    plugin,
                    threads,
                    hint);
        }
    }

    /**
     * Whether garbage collection may keep softly reachable objects, as it does unless the JVM was
     * started with {@code -XX:SoftRefLRUPolicyMSPerMB=0}.
     *
     * @return True unless every collection clears soft references, or if that can't be told.
     */
    private static boolean softReferencesKept() {
        try {
            final HotSpotDiagnosticMXBean diagnostics =
                    ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
            return diagnostics == null
                    || !"0".equals(diagnostics.getVMOption(SOFT_POLICY).getValue());
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    /**
     * Find threads that are running code from a class loader, or use it as their context class
     * loader.
     *
     * @param loaderName The class loader's name.
     * @param loader The class loader, or null if it was freed meanwhile.
     * @return Descriptions of the threads.
     */
    static List<String> threadsRunning(String loaderName, ClassLoader loader) {
        List<String> found = new ArrayList<>();
        for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            Thread thread = entry.getKey();
            if (loader != null && thread.getContextClassLoader() == loader) {
                found.add(thread.getName() + " (context class loader)");
                continue;
            }
            for (StackTraceElement frame : entry.getValue()) {
                if (loaderName != null && loaderName.equals(frame.getClassLoaderName())) {
                    found.add(thread.getName() + " (at " + frame + ")");
                    break;
                }
            }
        }
        return found;
    }

    /** Static helpers only. */
    private UnloadCheck() {}
}
