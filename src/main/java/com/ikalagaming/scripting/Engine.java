package com.ikalagaming.scripting;

import com.ikalagaming.util.SafeResourceLoader;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * The scripting engine for Ikala projects. This engine uses the LuaJ implementation of the lua
 * scripting language.
 *
 * @author Ches Burks
 */
@Slf4j
public class Engine {

    /** The name of the lua engine. */
    private static final String LUA_ENGINE_NAME = "luaj";

    private static ScriptEngineManager engineManager = null;
    private static ScriptEngine luaEngine = null;

    private static List<Class<?>> registeredClasses =
            Collections.synchronizedList(new ArrayList<>());

    private static ScriptEngineManager getEngineManager() {
        if (Engine.engineManager == null) {
            Engine.engineManager = new ScriptEngineManager();
        }
        return Engine.engineManager;
    }

    /**
     * Returns the Engine's lua engine.
     *
     * @return The ScriptEngine for luaj
     */
    public static ScriptEngine getLuaEngine() {
        if (Engine.luaEngine == null) {
            Engine.luaEngine = Engine.getEngineManager().getEngineByName(Engine.LUA_ENGINE_NAME);
        }
        return Engine.luaEngine;
    }

    /**
     * Register a class with the script engine, making all of its methods available for use. If we
     * registered the class <code>
     * public class Example{ public static void test() {...} }
     * </code> we could call it in lua like {@code Example:test()}. <br>
     * If the class is already registered, this will not do anything.
     *
     * @param clazz The class we are registering.
     */
    public static void registerClass(Class<?> clazz) {
        if (Engine.registeredClasses.contains(clazz)) {
            return;
        }
        Engine.registeredClasses.add(clazz);
        ScriptEngine engine = Engine.getLuaEngine();
        engine.put(clazz.getSimpleName(), clazz);
        log.debug(
                SafeResourceLoader.getString("REGISTERED_CLASS", ScriptManager.getResourceBundle()),
                clazz.getSimpleName());
    }

    /**
     * Unregister a class from the script engine. <br>
     * If the class is not registered, this will not do anything.
     *
     * @param clazz The class we are unregistering.
     */
    public static void unregisterClass(Class<?> clazz) {
        if (!Engine.registeredClasses.contains(clazz)) {
            return;
        }
        ScriptEngine engine = Engine.getLuaEngine();
        engine.put(clazz.getSimpleName(), null);
        log.debug(
                SafeResourceLoader.getString(
                        "UNREGISTERED_CLASS", ScriptManager.getResourceBundle()),
                clazz.getSimpleName());
    }

    /** Private method so this class is not instantiated. */
    private Engine() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
