package com.ikalagaming.scripting;

import com.ikalagaming.localization.Localization;
import com.ikalagaming.scripting.interpreter.ScriptRuntime;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

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
     * Fetch a list of registered methods with the given name and parameter count.
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
                if (!name.equals(entry.getKey().name())
                        || (parameterCount != entry.getKey().parameterTypes().size())) {
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
     * Actually run the script. Will start up a new thread if one does not exist.
     *
     * @param stream The stream to pass to the lexer.
     * @param name The name of the script, which may be null.
     * @return Whether we actually got back a program.
     */
    @Synchronized
    private static boolean runScript(@NonNull CharStream stream, String name) {
        if (ScriptManager.runner == null) {
            ScriptManager.runner = new ScriptRunner();
            ScriptManager.runner.start();
        }
        Optional<ScriptRuntime> maybeScript = IkalaScriptCompiler.parse(stream);
        if (maybeScript.isEmpty()) {
            return false;
        }
        maybeScript.get().setName(name);
        ScriptManager.runner.runScript(maybeScript.get());
        return true;
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

        CharStream stream;
        try {
            stream = CharStreams.fromPath(script.toPath());
        } catch (IOException e) {
            log.warn(
                    SafeResourceLoader.getString(
                            "FILE_READ_ERROR", ScriptManager.getResourceBundle()),
                    script.getAbsolutePath());
            return false;
        }
        return ScriptManager.runScript(stream, script.getName());
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
        CharStream stream = CharStreams.fromString(script);
        return ScriptManager.runScript(stream, name);
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
