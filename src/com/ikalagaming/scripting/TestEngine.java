package com.ikalagaming.scripting;

import javax.script.ScriptEngineFactory;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the functionality of the scripting engine class.
 *
 * @author Ches Burks
 *
 */
public class TestEngine {

	/**
	 * Displays the engine name and version and the language name and version.
	 * Fails if the lua engines ScriptEngineFactory is null
	 */
	@Test
	public void showLuaEngineInfo() {
		ScriptEngineFactory f = Engine.getLuaEngine().getFactory();
		Assert.assertNotNull(f);
		System.out.println("Engine name: " + f.getEngineName());
		System.out.println("Engine Version: " + f.getEngineVersion());
		System.out.println("LanguageName: " + f.getLanguageName());
		System.out.println("Language Version: " + f.getLanguageVersion());
	}

}
