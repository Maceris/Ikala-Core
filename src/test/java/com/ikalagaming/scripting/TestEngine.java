package com.ikalagaming.scripting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngineFactory;

/**
 * Tests the legacy Lua scripting engine.
 *
 * @author Ches Burks
 */
class TestEngine {

    /** The Lua engine should be available, and be the one we expect. */
    @Test
    void luaEngineInfo() {
        ScriptEngineFactory factory = Engine.getLuaEngine().getFactory();
        Assertions.assertAll(
                () -> Assertions.assertEquals("Luaj", factory.getEngineName()),
                () -> Assertions.assertNotNull(factory.getEngineVersion()),
                () -> Assertions.assertEquals("lua", factory.getLanguageName()),
                () -> Assertions.assertNotNull(factory.getLanguageVersion()));
    }

    /** The Lua engine should be able to run a script. */
    @Test
    void evaluate() {
        Object result =
                Assertions.assertDoesNotThrow(() -> Engine.getLuaEngine().eval("return 1 + 2"));
        Assertions.assertEquals(3, ((Number) result).intValue());
    }
}
