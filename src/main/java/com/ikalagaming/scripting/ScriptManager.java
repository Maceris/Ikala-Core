package com.ikalagaming.scripting;

import com.ikalagaming.localization.Localization;
import com.ikalagaming.scripting.interpreter.ScriptRuntime;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Handles scripting.
 *
 * @author Ches Burks
 */
@Slf4j
public class ScriptManager {

    /**
     * The current resource bundle for the script manager.
     *
     * @return The current resource bundle.
     */
    @SuppressWarnings("javadoc")
    @Getter
    private static ResourceBundle resourceBundle =
            ResourceBundle.getBundle(
                    "com.ikalagaming.scripting.Scripting", Localization.getLocale());

    /** List of classes that are registered. */
    private static List<Class<?>> registeredClasses =
            Collections.synchronizedList(new ArrayList<>());

    /** Maps from class to registrations. */
    private static Map<Class<?>, List<FunctionRegistration>> classMethods =
            Collections.synchronizedMap(new HashMap<>());

    /** A list of all methods that are registered for easy searching. */
    private static Map<FunctionRegistration, Method> registeredMethods =
            Collections.synchronizedMap(new HashMap<>());

    /** Runs scripts on a different thread. */
    private static ScriptRunner runner;

    /**
     * Classes every script can call static methods on, by their simple name. Only classes that
     * can't reach the system, like files, threads or reflection, are included.
     */
    private static final Map<String, HostClass> STANDARD_CLASSES =
            Map.ofEntries(
                    Map.entry("Math", HostClass.of(Math.class)),
                    Map.entry("Integer", HostClass.of(Integer.class)),
                    Map.entry("Double", HostClass.of(Double.class)),
                    Map.entry("Boolean", HostClass.of(Boolean.class)),
                    Map.entry("Character", HostClass.of(Character.class)),
                    Map.entry("String", HostClass.of(String.class)),
                    Map.entry("Objects", HostClass.of(Objects.class)),
                    Map.entry("List", HostClass.of(List.class)),
                    Map.entry("Map", HostClass.of(Map.class)),
                    Map.entry("Set", HostClass.of(Set.class)),
                    Map.entry("Collections", HostClass.of(Collections.class)));

    /**
     * Gives each script its value of a global.
     *
     * @param owner The plugin that registered the global, which may be null.
     * @param provider Makes the value for a script, given the plugin that owns the script, which
     *     may be null.
     */
    private record GlobalProvider(String owner, @NonNull Function<String, Object> provider) {}

    /** Globals that plugins provide to every script, by name. */
    private static final Map<String, GlobalProvider> globalProviders = new ConcurrentHashMap<>();

    /**
     * A compiled script file.
     *
     * @param path The file.
     * @param globals The globals it was compiled with.
     */
    private record CompiledKey(@NonNull Path path, @NonNull Set<String> globals) {}

    /**
     * A compiled script file, kept until the file changes.
     *
     * @param modified When the file was last modified when it was compiled.
     * @param program The compiled program, which is copied for each run and never run itself.
     * @param owner The plugin that last ran it, so it is dropped when the plugin unloads.
     */
    private record Compiled(
            @NonNull FileTime modified, @NonNull ScriptRuntime program, String owner) {}

    /** Compiled script files. */
    private static final Map<CompiledKey, Compiled> compiledFiles = new ConcurrentHashMap<>();

    /**
     * Fetch a list of registered methods with the given name that can take the parameter count,
     * either exactly or through variable arguments.
     *
     * @param name The name of the method.
     * @param parameterCount The number of parameters.
     * @return A list containing all matching registered methods, which may be empty.
     */
    public static List<Method> getMethods(@NonNull String name, int parameterCount) {
        // The same method might be registered through multiple classes if it's inherited
        Set<Method> methods = new LinkedHashSet<>();
        synchronized (ScriptManager.registeredMethods) {
            for (var entry : ScriptManager.registeredMethods.entrySet()) {
                final int declared = entry.getKey().parameterTypes().size();
                final boolean countFits =
                        parameterCount == declared
                                || (entry.getValue().isVarArgs() && parameterCount >= declared - 1);
                if (!name.equals(entry.getKey().name()) || !countFits) {
                    continue;
                }
                methods.add(entry.getValue());
            }
        }
        return new ArrayList<>(methods);
    }

    /**
     * Register a class with the script engine, making all of its methods available for use. If the
     * class is already registered, this will not do anything. <br>
     * Only public static methods, which are not abstract or interfaces, will be registered.
     *
     * @param clazz The class we are registering.
     */
    public static void registerClass(Class<?> clazz) {
        if (ScriptManager.registeredClasses.contains(clazz)) {
            return;
        }
        ScriptManager.registeredClasses.add(clazz);
        List<FunctionRegistration> funcs = new ArrayList<>();

        for (Method method : clazz.getMethods()) {
            final int modifiers = method.getModifiers();
            if (!(Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
                    || Modifier.isAbstract(modifiers)) {
                continue;
            }
            FunctionRegistration registration = FunctionRegistration.fromMethod(clazz, method);
            funcs.add(registration);
            ScriptManager.registeredMethods.put(registration, method);
        }
        ScriptManager.classMethods.put(clazz, List.copyOf(funcs));
    }

    /**
     * Provide a global to every script started from now on, like {@code ui}. Each script gets the
     * value the provider makes for the plugin that owns the script, so plugins don't share objects.
     * The global is removed when its owner unloads.
     *
     * @param name The name scripts use.
     * @param owner The plugin providing it, which may be null if it is never removed.
     * @param provider Makes the value for a script, given the plugin that owns the script, which
     *     may be null.
     * @throws IllegalArgumentException If the name is already a global.
     */
    public static void registerGlobal(
            @NonNull String name, String owner, @NonNull Function<String, Object> provider) {
        if (STANDARD_CLASSES.containsKey(name)
                || globalProviders.putIfAbsent(name, new GlobalProvider(owner, provider)) != null) {
            throw new IllegalArgumentException(
                    SafeResourceLoader.getString(
                                    "GLOBAL_ALREADY_REGISTERED", ScriptManager.getResourceBundle())
                            .replace("{}", name));
        }
    }

    /**
     * Stop providing a global. Scripts that already have it keep it.
     *
     * @param name The name of the global.
     */
    public static void unregisterGlobal(@NonNull String name) {
        globalProviders.remove(name);
    }

    /**
     * The globals a script owned by a plugin gets: the standard classes, then provided globals.
     *
     * @param owner The plugin that owns the script, which may be null.
     * @return The globals by name, in a new map the caller can change.
     */
    public static Map<String, Object> globalsFor(String owner) {
        final Map<String, Object> globals = new LinkedHashMap<>(STANDARD_CLASSES);
        globalProviders.forEach(
                (name, global) -> globals.put(name, global.provider().apply(owner)));
        return globals;
    }

    /**
     * Compile and start a script. It runs on the script thread, which also runs everything it
     * calls.
     *
     * @param launch What to run.
     * @return The running script, or empty if it failed to compile or the entry label isn't valid.
     *     Problems are logged.
     */
    @Synchronized
    public static Optional<ScriptRuntime> start(@NonNull ScriptLaunch launch) {
        final Map<String, Object> globals = ScriptManager.globalsFor(launch.getOwner());
        globals.putAll(launch.getGlobals());

        final Optional<ScriptRuntime> program = ScriptManager.compile(launch, globals.keySet());
        if (program.isEmpty()) {
            return Optional.empty();
        }
        final ScriptRuntime runtime = program.get().copyProgram();
        runtime.setName(launch.getName());
        runtime.setOwner(launch.getOwner());
        globals.forEach(runtime::setGlobal);
        final String label = launch.getEntryLabel();
        if (label != null && !runtime.startAt(label)) {
            log.warn(
                    SafeResourceLoader.getString(
                            "UNKNOWN_ENTRY_LABEL", ScriptManager.getResourceBundle()),
                    label,
                    runtime.getDisplayName());
            return Optional.empty();
        }

        if (ScriptManager.runner == null) {
            ScriptManager.runner = new ScriptRunner();
            ScriptManager.runner.start();
        }
        ScriptManager.runner.runScript(runtime);
        return Optional.of(runtime);
    }

    /**
     * Compile a launch's script, using the cache for files.
     *
     * @param launch What to compile.
     * @param globals The names of the globals.
     * @return The compiled program, or empty if it failed.
     */
    private static Optional<ScriptRuntime> compile(
            @NonNull ScriptLaunch launch, @NonNull Set<String> globals) {
        if (launch.getFile() == null) {
            return IkalaScriptCompiler.compile(CharStreams.fromString(launch.getSource()), globals)
                    .runtime();
        }
        final Path path = launch.getFile().toAbsolutePath().normalize();
        try {
            final FileTime modified = Files.getLastModifiedTime(path);
            final CompiledKey key = new CompiledKey(path, Set.copyOf(globals));
            final Compiled cached = compiledFiles.get(key);
            if (cached != null && cached.modified().equals(modified)) {
                return Optional.of(cached.program());
            }
            final Optional<ScriptRuntime> program =
                    IkalaScriptCompiler.compile(CharStreams.fromPath(path), globals).runtime();
            program.ifPresent(
                    compiled ->
                            compiledFiles.put(
                                    key, new Compiled(modified, compiled, launch.getOwner())));
            return program;
        } catch (IOException e) {
            log.warn(
                    SafeResourceLoader.getString(
                            "FILE_READ_ERROR", ScriptManager.getResourceBundle()),
                    path);
            return Optional.empty();
        }
    }

    /**
     * Stop every script a plugin owns, drop what they hold, and remove the globals it provides.
     * Called when the plugin unloads.
     *
     * @param owner The plugin.
     */
    public static void terminateAllOwnedBy(@NonNull String owner) {
        globalProviders.values().removeIf(global -> owner.equals(global.owner()));
        compiledFiles.values().removeIf(compiled -> owner.equals(compiled.owner()));
        final ScriptRunner currentRunner = ScriptManager.runner;
        if (currentRunner != null) {
            final int stopped = currentRunner.terminateAllOwnedBy(owner);
            if (stopped > 0) {
                log.debug(
                        SafeResourceLoader.getString(
                                "TERMINATED_OWNED_SCRIPTS", ScriptManager.getResourceBundle()),
                        stopped,
                        owner);
            }
        }
    }

    /**
     * Resume one script waiting on a tag, with the value its await returns. If it hasn't reached
     * the await yet, the value is kept and the await returns it straight away, so a resume can't be
     * missed by arriving early.
     *
     * @param runtime The script.
     * @param tag The tag it awaits.
     * @param value The value the await returns.
     */
    public static void resume(@NonNull ScriptRuntime runtime, @NonNull String tag, Object value) {
        final ScriptRunner currentRunner = ScriptManager.runner;
        if (currentRunner != null) {
            currentRunner.requestResume(runtime, tag, value);
        } else {
            runtime.post(tag, value);
        }
    }

    /**
     * Resume any scripts that were halted using the supplied tag, with the value their awaits
     * return. Scripts that aren't waiting yet miss it.
     *
     * @param tag The tag to resume.
     * @param value The value awaits on the tag return.
     */
    public static void resume(@NonNull String tag, Object value) {
        final ScriptRunner currentRunner = ScriptManager.runner;
        if (currentRunner != null) {
            currentRunner.requestResume(tag, value);
        }
    }

    /**
     * Resume any scripts that were halted without a specific tag.
     *
     * @see #yieldScript(ScriptRuntime)
     */
    public static void resume() {
        final ScriptRunner currentRunner = ScriptManager.runner;
        if (currentRunner != null) {
            currentRunner.requestResume();
        }
    }

    /**
     * Resume any scripts that were halted using the supplied tag.
     *
     * @param tag The tag to resume.
     * @see #yieldScript(ScriptRuntime, String)
     */
    public static void resume(@NonNull String tag) {
        final ScriptRunner currentRunner = ScriptManager.runner;
        if (currentRunner != null) {
            currentRunner.requestResume(tag);
        }
    }

    /**
     * Stop a script that the script manager is running or has yielded. It will not be resumed.
     *
     * @param runtime The script to stop.
     * @return True if the script was running or yielded, false if the script manager didn't know
     *     about it.
     */
    public static boolean terminate(@NonNull ScriptRuntime runtime) {
        final ScriptRunner currentRunner = ScriptManager.runner;
        return currentRunner != null && currentRunner.terminate(runtime);
    }

    /**
     * Fetch the scripts that are currently running, not including yielded scripts.
     *
     * @return A snapshot of the running scripts.
     */
    public static List<ScriptRuntime> getRunningScripts() {
        final ScriptRunner currentRunner = ScriptManager.runner;
        return currentRunner == null ? List.of() : currentRunner.getRunningScripts();
    }

    /**
     * Fetch the scripts that have yielded and are waiting to be resumed.
     *
     * @return A snapshot of the yielded scripts, mapped to the tag they yielded with. The tag is an
     *     empty string for scripts that yielded without a tag, which are resumed by {@link
     *     #resume()}.
     */
    public static Map<ScriptRuntime, String> getYieldedScripts() {
        final ScriptRunner currentRunner = ScriptManager.runner;
        return currentRunner == null ? Map.of() : currentRunner.getYieldedScripts();
    }

    /**
     * Execute a script as as string.
     *
     * @param script The file containing the script.
     * @return Whether we successfully parsed and started to run the script.
     */
    @Synchronized
    public static boolean runScript(@NonNull File script) {
        if (!script.exists() || !script.canRead()) {
            return false;
        }
        return ScriptManager.start(ScriptLaunch.file(script.toPath())).isPresent();
    }

    /**
     * Execute a script as as string.
     *
     * @param script The script to execute.
     * @return Whether we successfully parsed and started to run the script.
     */
    public static boolean runScript(@NonNull String script) {
        return ScriptManager.runScript(script, null);
    }

    /**
     * Execute a script as as string, with a name to identify it while debugging.
     *
     * @param script The script to execute.
     * @param name The name of the script, which may be null.
     * @return Whether we successfully parsed and started to run the script.
     */
    public static boolean runScript(@NonNull String script, String name) {
        return ScriptManager.start(ScriptLaunch.source(script).name(name)).isPresent();
    }

    /**
     * Stop executing scripts, shut down the runner thread. This should be called while the program
     * is shutting down.
     */
    @Synchronized
    public static void shutdown() {
        if (ScriptManager.runner != null) {
            ScriptManager.runner.terminate();
            // Otherwise we would keep adding scripts to a thread that is no longer running
            ScriptManager.runner = null;
        }
    }

    /**
     * Unregister a class from the script engine. <br>
     * If the class is not registered, this will not do anything.
     *
     * @param clazz The class we are unregistering.
     */
    public static void unregisterClass(Class<?> clazz) {
        if (!ScriptManager.registeredClasses.contains(clazz)) {
            return;
        }
        ScriptManager.registeredClasses.remove(clazz);
        ScriptManager.classMethods.get(clazz).forEach(ScriptManager.registeredMethods::remove);
        ScriptManager.classMethods.remove(clazz);
    }

    /**
     * Yield the given script execution without a specific tag.
     *
     * <p>This is intended to be called internally by the script runtime itself.
     *
     * @param runtime The runtime that is supposed to halt.
     * @see #resume()
     */
    public static void yieldScript(@NonNull ScriptRuntime runtime) {
        final ScriptRunner currentRunner = ScriptManager.runner;
        if (currentRunner != null) {
            currentRunner.requestYield(runtime);
        }
    }

    /**
     * Yield the given script execution using the supplied tag.
     *
     * <p>This is intended to be called internally by the script runtime itself.
     *
     * @param runtime The runtime that is supposed to halt.
     * @param tag The tag that will be used to resume the script.
     * @see #resume(String)
     */
    public static void yieldScript(@NonNull ScriptRuntime runtime, @NonNull String tag) {
        final ScriptRunner currentRunner = ScriptManager.runner;
        if (currentRunner != null) {
            currentRunner.requestYield(runtime, tag);
        }
    }

    /** Private constructor so that this class is not instantiated. */
    private ScriptManager() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
