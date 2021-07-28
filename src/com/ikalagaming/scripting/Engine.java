package com.ikalagaming.scripting;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * The scripting engine for Ikala projects. This engine uses the LuaJ
 * implementation of the lua scripting language.
 *
 * @author Ches Burks
 *
 */
public class Engine {

	/**
	 * The name of the lua engine.
	 */
	private static final String LUA_ENGINE_NAME = "luaj";

	private static ScriptEngineManager engineManager = null;
	private static ScriptEngine luaEngine = null;

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
			Engine.luaEngine = Engine.getEngineManager()
				.getEngineByName(Engine.LUA_ENGINE_NAME);
		}

		return Engine.luaEngine;
	}

	/**
	 * Private method so this class is not instantiated.
	 */
	private Engine() {}

}
