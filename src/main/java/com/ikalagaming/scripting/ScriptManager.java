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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

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

    /** Maps from class name to registrations. */
    private static Map<String, List<FunctionRegistration>> classMethods =
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
        List<Method> methods = new ArrayList<>();
        for (var entry : ScriptManager.registeredMethods.entrySet()) {
            if (!name.equals(entry.getKey().name())
                    || (parameterCount != entry.getKey().parameterTypes().size())) {
                continue;
            }
            methods.add(entry.getValue());
        }
        return methods;
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
            if (!(Modifier.isStatic(modifiers) || Modifier.isPublic(modifiers))
                    || Modifier.isAbstract(modifiers)
                    || Modifier.isInterface(modifiers)) {
                continue;
            }
            FunctionRegistration registration = FunctionRegistration.fromMethod(method);
            funcs.add(registration);
            ScriptManager.registeredMethods.put(registration, method);
        }
        ScriptManager.classMethods.put(clazz.getSimpleName(), List.copyOf(funcs));
    }

    /**
     * Resume any scripts that were halted without a specific tag.
     *
     * @see #yieldScript(ScriptRuntime)
     */
    public static void resume() {
        ScriptManager.runner.requestResume();
    }

    /**
     * Resume any scripts that were halted using the supplied tag.
     *
     * @param tag The tag to resume.
     * @see #yieldScript(ScriptRuntime, String)
     */
    public static void resume(@NonNull String tag) {
        ScriptManager.runner.requestResume(tag);
    }

    /**
     * Actually run the script. Will start up a new thread if one does not exist.
     *
     * @param stream The stream to pass to the lexer.
     * @return Whether we actually got back a program.
     */
    @Synchronized
    private static boolean runScript(@NonNull CharStream stream) {
        if (ScriptManager.runner == null) {
            ScriptManager.runner = new ScriptRunner();
            ScriptManager.runner.start();
        }
        Optional<ScriptRuntime> maybeScript = IkalaScriptCompiler.parse(stream);
        if (maybeScript.isEmpty()) {
            return false;
        }
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
        return ScriptManager.runScript(stream);
    }

    /**
     * Execute a script as as string.
     *
     * @param script The script to execute.
     * @return Whether we successfully parsed and started to run the script.
     */
    public static boolean runScript(@NonNull String script) {
        CharStream stream = CharStreams.fromString(script);
        return ScriptManager.runScript(stream);
    }

    /**
     * Stop executing scripts, shut down the runner thread. This should be called while the program
     * is shutting down.
     */
    public static void shutdown() {
        if (ScriptManager.runner != null) {
            ScriptManager.runner.terminate();
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
        ScriptManager.classMethods
                .get(clazz.getSimpleName())
                .forEach(ScriptManager.registeredMethods::remove);
        ScriptManager.classMethods.remove(clazz.getSimpleName());
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
        ScriptManager.runner.requestYield(runtime);
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
        ScriptManager.runner.requestYield(runtime, tag);
    }

    /** Private constructor so that this class is not instantiated. */
    private ScriptManager() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
