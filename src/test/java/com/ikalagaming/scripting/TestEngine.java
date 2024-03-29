package com.ikalagaming.scripting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

/**
 * Tests the functionality of the scripting engine class.
 *
 * @author Ches Burks
 */
class TestEngine {

    /**
     * Displays the engine name and version and the language name and version. Fails if the lua
     * engines ScriptEngineFactory is null
     */
    @Test
    public void showLuaEngineInfo() {
        ScriptEngineFactory f = Engine.getLuaEngine().getFactory();
        Assertions.assertNotNull(f);
        Assertions.assertEquals("Luaj", f.getEngineName());
        Assertions.assertNotNull(f.getEngineVersion());
        Assertions.assertEquals("lua", f.getLanguageName());
        Assertions.assertNotNull(f.getLanguageVersion());

        System.out.println("Engine name: " + f.getEngineName());
        System.out.println("Engine Version: " + f.getEngineVersion());
        System.out.println("LanguageName: " + f.getLanguageName());
        System.out.println("Language Version: " + f.getLanguageVersion());
    }

    /** Tries to evaluate some lua code that prints text. */
    @Test
    public void testPrinting() {
        ScriptEngine eng = Engine.getLuaEngine();
        Assertions.assertNotNull(eng);
        try {
            eng.eval("print('This should show in stdout')");
        } catch (ScriptException e) {
            Assertions.fail("Exception in print string");
        }
    }
}
