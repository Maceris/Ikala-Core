
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

	private static ScriptEngineManager engineManager =
			new ScriptEngineManager();
	private static ScriptEngine luaEngine = engineManager
			.getEngineByName("luaj");

	/**
	 * Returns the Engine's lua engine.
	 * 
	 * @return The ScriptEngine for luaj
	 */
	public static ScriptEngine getLuaEngine() {
		return luaEngine;
	}

}
