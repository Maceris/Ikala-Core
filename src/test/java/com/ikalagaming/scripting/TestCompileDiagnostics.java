package com.ikalagaming.scripting;

import com.ikalagaming.scripting.IkalaScriptCompiler.CompileResult;
import com.ikalagaming.scripting.interpreter.ScriptRuntime;

import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the information tools like debuggers use, like compile errors and yield handlers.
 *
 * @author Ches Burks
 */
class TestCompileDiagnostics {

    private static CompileResult compile(String program) {
        return IkalaScriptCompiler.compile(CharStreams.fromString(program));
    }

    /** A valid program has a runtime, a tree, and no errors. */
    @Test
    void validProgram() {
        CompileResult result = compile("int x;\nx += 1;");

        Assertions.assertTrue(result.succeeded());
        Assertions.assertNotNull(result.syntaxTree());
        Assertions.assertTrue(result.errors().isEmpty(), result.errors().toString());
        Assertions.assertFalse(result.runtime().get().getInstructions().isEmpty());
    }

    /** Syntax errors are collected, including the line they were on. */
    @Test
    void syntaxError() {
        CompileResult result = compile("int x;\nx += ;");

        Assertions.assertFalse(result.succeeded());
        Assertions.assertFalse(result.errors().isEmpty());
        final ScriptDiagnostics.Diagnostic error = result.errors().getFirst();
        Assertions.assertEquals(2, error.line(), error.toString());
        Assertions.assertEquals(6, error.column(), error.toString());
        Assertions.assertTrue(error.toString().startsWith("Line 2, column 6: "), error.toString());
    }

    /**
     * A missing semicolon is reported right after the end of the statement it's missing from,
     * rather than at the start of the next statement.
     *
     * @param program The program, which is missing a semicolon on the first line.
     */
    @ParameterizedTest
    @ValueSource(
            strings = {
                "int y = 1\nint z = 2;",
                // Comments and whitespace are skipped
                "int y = 1   // a comment\n\n\tint z = 2;",
                "int y = 1 /* a\ncomment */ int z = 2;",
                // At the end of the script
                "int y = 1",
                "int y = 1\n",
            })
    void missingSemicolon(String program) {
        CompileResult result = compile(program);

        Assertions.assertFalse(result.succeeded());
        final ScriptDiagnostics.Diagnostic error = result.errors().getFirst();
        Assertions.assertEquals(1, error.line(), error.toString());
        Assertions.assertEquals(10, error.column(), error.toString());
        Assertions.assertEquals("Syntax error, missing ';'", error.message(), error.toString());
    }

    /** A missing semicolon after a method call goes right after the closing parenthesis. */
    @Test
    void missingSemicolonAfterCall() {
        CompileResult result = compile("TEST_printString(\"a\")\nint x;");

        Assertions.assertFalse(result.succeeded());
        final ScriptDiagnostics.Diagnostic error = result.errors().getFirst();
        Assertions.assertEquals(1, error.line(), error.toString());
        Assertions.assertEquals(22, error.column(), error.toString());
    }

    /** Long lists of expected tokens are shortened. */
    @Test
    void syntaxErrorsAreShortened() {
        Assertions.assertEquals(
                "mismatched input ';' expecting one of 'a', 'b', 'c', 'd', 'e', 'f', or 2 others",
                ParserErrorListener.shortenMessage(
                        "mismatched input ';' expecting {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'}"));
        Assertions.assertEquals(
                "missing ';' at 'x'", ParserErrorListener.shortenMessage("missing ';' at 'x'"));
    }

    /**
     * Errors found when validating the tree are collected with the line, show the code rather than
     * the syntax tree, and there is still a tree.
     */
    @Test
    void validationError() {
        CompileResult result = compile("int x;\n\nx = \"text\";");

        Assertions.assertFalse(result.succeeded());
        Assertions.assertNotNull(result.syntaxTree());
        Assertions.assertFalse(result.errors().isEmpty());
        final ScriptDiagnostics.Diagnostic error = result.errors().getFirst();
        Assertions.assertEquals(3, error.line(), error.toString());
        Assertions.assertTrue(error.message().contains("x = \"text\""), error.toString());
    }

    /** Collected messages only go to the innermost collector, and collection stops afterward. */
    @Test
    void nestedCollection() {
        List<ScriptDiagnostics.Diagnostic> outer = new ArrayList<>();
        List<ScriptDiagnostics.Diagnostic> inner = new ArrayList<>();
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TestCompileDiagnostics.class);

        ScriptDiagnostics.collect(
                outer,
                () -> {
                    ScriptDiagnostics.warn(log, "outer {}", 1);
                    ScriptDiagnostics.collect(inner, () -> ScriptDiagnostics.warn(log, "inner"));
                    ScriptDiagnostics.warn(log, "outer {}", 2);
                });
        ScriptDiagnostics.warn(log, "not collected");

        Assertions.assertEquals(
                List.of("outer 1", "outer 2"),
                outer.stream().map(ScriptDiagnostics.Diagnostic::message).toList());
        Assertions.assertEquals(
                List.of("inner"),
                inner.stream().map(ScriptDiagnostics.Diagnostic::message).toList());
        Assertions.assertEquals(-1, inner.getFirst().line());
        Assertions.assertEquals("inner", inner.getFirst().toString());
    }

    /** A yield handler gets yields instead of the script manager, with the tag if any. */
    @Test
    void yieldHandler() {
        CompileResult result = compile("yield(\"Dialogue\");\nyield();");
        Assertions.assertTrue(result.succeeded(), result.errors().toString());
        ScriptRuntime runtime = result.runtime().get();

        List<String> tags = new ArrayList<>();
        runtime.setYieldHandler((script, tag) -> tags.add(tag));
        while (!runtime.hasTerminated()) {
            runtime.step();
        }

        Assertions.assertEquals(2, tags.size());
        Assertions.assertEquals("Dialogue", tags.get(0));
        Assertions.assertNull(tags.get(1));
    }

    /** Runtimes have unique IDs, and display their names when they have one. */
    @Test
    void displayNames() {
        ScriptRuntime first = compile("int x;").runtime().get();
        ScriptRuntime second = compile("int x;").runtime().get();

        Assertions.assertNotEquals(first.getId(), second.getId());
        Assertions.assertEquals("Script #" + first.getId(), first.getDisplayName());

        second.setName("test.ika");
        Assertions.assertEquals("test.ika (#" + second.getId() + ")", second.getDisplayName());
    }
}
