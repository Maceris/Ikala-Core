package com.ikalagaming.scripting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Tests that a script using every part of the grammar makes it through the compiler.
 *
 * @author Ches Burks
 */
class TestParser {

    /** Compiles the smoke test script, which should be valid. */
    @Test
    void smokeTest() throws IOException {
        final String program;
        try (InputStream stream = TestParser.class.getResourceAsStream("ParserSmoke.iks")) {
            Assertions.assertNotNull(stream, "The smoke test script should exist");
            program = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
        Assertions.assertTrue(ScriptTestHelper.validates(program), "Validation should pass");
        Assertions.assertTrue(ScriptTestHelper.compiles(program), "Compilation should pass");
    }
}
