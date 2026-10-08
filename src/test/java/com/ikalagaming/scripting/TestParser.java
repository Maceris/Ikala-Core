package com.ikalagaming.scripting;

import com.ikalagaming.scripting.IkalaScriptCompiler.CompileResult;
import com.ikalagaming.scripting.ScriptDiagnostics.Diagnostic;

import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tests that scripts using every part of the grammar, including unusual syntax, make it through the
 * compiler with the right meaning, and that invalid scripts fail cleanly.
 *
 * @author Ches Burks
 */
class TestParser {

    /** Matches the comments in ParserSemantics.iks that say what a line should print. */
    private static final Pattern EXPECT = Pattern.compile("//\\s*expect:\\s?(.*)$");

    /** Clear out recorded output. */
    @AfterEach
    void afterEach() {
        DebugMethods.reset();
    }

    /**
     * Load a script from the test resources.
     *
     * @param name The file name.
     * @return The contents of the script.
     */
    private static String load(String name) throws IOException {
        try (InputStream stream = TestParser.class.getResourceAsStream(name)) {
            Assertions.assertNotNull(stream, () -> "The script " + name + " should exist");
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Compiles scripts which should be valid.
     *
     * @param name The file name of the script.
     */
    @ParameterizedTest
    @ValueSource(strings = {"ParserSmoke.iks", "ParserEdgeCases.iks"})
    void smokeTest(String name) throws IOException {
        final String program = load(name);
        final CompileResult result = IkalaScriptCompiler.compile(CharStreams.fromString(program));
        Assertions.assertTrue(result.errors().isEmpty(), () -> "Problems: " + result.errors());
        Assertions.assertTrue(ScriptTestHelper.validates(program), "Validation should pass");
        Assertions.assertTrue(ScriptTestHelper.compiles(program), "Compilation should pass");
    }

    /** Valid scripts should compile the same with Windows line endings. */
    @Test
    void windowsLineEndings() throws IOException {
        for (String name : List.of("ParserSmoke.iks", "ParserEdgeCases.iks")) {
            final String program = load(name).replace("\r\n", "\n").replace("\n", "\r\n");
            final CompileResult result =
                    IkalaScriptCompiler.compile(CharStreams.fromString(program));
            Assertions.assertTrue(
                    result.errors().isEmpty(), () -> name + " problems: " + result.errors());
        }
    }

    /**
     * Runs a script full of code that is easy to parse with the wrong meaning, like dangling elses
     * and packed together operators, and checks it prints what the "expect:" comments say.
     */
    @Test
    void semantics() throws IOException {
        final String program = load("ParserSemantics.iks");
        final List<String> expected = new ArrayList<>();
        for (String line : program.split("\\R")) {
            Matcher matcher = EXPECT.matcher(line);
            if (matcher.find()) {
                expected.add(matcher.group(1).stripTrailing());
            }
        }
        Assertions.assertFalse(expected.isEmpty(), "The script should have expectations");

        final List<String> output = ScriptTestHelper.runCleanly(program);

        // Compare line by line first, for a more useful message than two long lists
        for (int i = 0; i < Math.min(expected.size(), output.size()); ++i) {
            final int index = i;
            Assertions.assertEquals(
                    expected.get(i),
                    output.get(i),
                    () -> "Output " + index + " was wrong, expected " + expected);
        }
        Assertions.assertEquals(expected, output);
    }

    /**
     * Scripts that are not valid fail to compile, without throwing exceptions, and say where the
     * problem is.
     *
     * @param program The invalid script.
     */
    @ParameterizedTest
    @ValueSource(
            strings = {
                // Keywords as names
                "int if = 1;",
                "int x; for: x++;",
                // Missing pieces
                "int x; x = ;",
                "int x = 1 +;",
                "int x = 1 int y = 2;",
                "int x; if (x > 0 { x++; }",
                "int x; while x > 0 { }",
                "int x; x = true ? : 2;",
                "goto;",
                "switch (1) { case: break; }",
                "TEST_printString(\"a\",);",
                // Unbalanced brackets
                "int x = ((1);",
                "int x = (1));",
                "{ int x = 1;",
                "int x = 1; }",
                // Things in the wrong place
                "else { }",
                "case 1: break;",
                "int x; x++ ++;",
                "int x; x = 5 5;",
                "for (int i = 0, double j = 0;;) {}",
                // Bad literals
                "int x = 2147483648;",
                "string s = \"unterminated;",
                "char c = 'ab';",
                "char c = '';",
                "double d = 1.2.3;",
                // An exponent needs digits
                "double d = 2E;",
                "double d = 2E+;",
                "double d = 1.5e;",
                "int 1x = 2;",
                // Characters that aren't part of the language
                "int x = 1; @",
                "int x = 1;\n#\nint y;",
                "/* an unterminated comment",
            })
    void invalidScripts(String program) {
        final CompileResult result =
                Assertions.assertDoesNotThrow(
                        () -> IkalaScriptCompiler.compile(CharStreams.fromString(program)));

        Assertions.assertFalse(result.succeeded(), "Should not compile");
        Assertions.assertFalse(result.errors().isEmpty(), "Should explain why");
        final Diagnostic first = result.errors().getFirst();
        Assertions.assertTrue(
                first.line() > 0, () -> "The first problem should have a line: " + first);
    }
}
