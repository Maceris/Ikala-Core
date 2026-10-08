package com.ikalagaming.scripting;

import com.ikalagaming.scripting.IkalaScriptCompiler.CompileResult;
import com.ikalagaming.scripting.ast.SyntaxTreePrinter;
import com.ikalagaming.scripting.interpreter.Instruction;
import com.ikalagaming.scripting.interpreter.InstructionFormatter;
import com.ikalagaming.scripting.interpreter.ScriptRuntime;

import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Tests for printing syntax trees and instructions as text.
 *
 * @author Ches Burks
 */
class TestPrinters {

    private static CompileResult compile(String program) {
        CompileResult result = IkalaScriptCompiler.compile(CharStreams.fromString(program));
        Assertions.assertTrue(result.succeeded(), result.errors().toString());
        return result;
    }

    /** Trees are printed one node per line, indented by depth, with lines and types. */
    @Test
    void syntaxTree() {
        CompileResult result = compile("int x;\nx += 1;");
        final String printed = SyntaxTreePrinter.print(result.syntaxTree());
        final List<String> lines = printed.lines().toList();

        Assertions.assertEquals("CompilationUnit {", lines.getFirst(), printed);
        Assertions.assertEquals("}", lines.getLast(), printed);
        Assertions.assertTrue(lines.size() > 4, printed);
        Assertions.assertTrue(
                lines.contains("  ExprAssign += : int (line 2) {"),
                "Missing assignment\n" + printed);
        Assertions.assertTrue(
                lines.contains("    Identifier x : int (line 2)"),
                "Missing identifier\n" + printed);
        // Brackets are balanced
        Assertions.assertEquals(
                printed.chars().filter(c -> c == '{').count(),
                printed.chars().filter(c -> c == '}').count());
    }

    /** Strings are quoted and escaped so they stay on one line. */
    @Test
    void syntaxTreeStrings() {
        CompileResult result = compile("string s = \"a\\tb\";");
        final String printed = SyntaxTreePrinter.print(result.syntaxTree());
        Assertions.assertTrue(printed.contains("ConstString \"a\\tb\""), printed);
    }

    /** Instructions look like assembly, with variables by name and jumps by index. */
    @Test
    void instructions() {
        ScriptRuntime runtime =
                compile(
                                """
                                int x;
                                for (int i = 0; i < 3; ++i) {
                                    x += i;
                                }
                                TEST_printString("done");
                                """)
                        .runtime()
                        .get();
        final List<String> formatted =
                runtime.getInstructions().stream().map(InstructionFormatter::format).toList();

        Assertions.assertTrue(formatted.contains("DECLARE 0 -> x:int"), formatted.toString());
        Assertions.assertTrue(
                formatted.contains("ADD_INT x:int, stack:int -> x:int"), formatted.toString());
        Assertions.assertTrue(
                formatted.contains("MOV i:int -> stack:int"),
                "Variables should have their real type\n" + formatted);
        Assertions.assertTrue(
                formatted.stream().anyMatch(text -> text.matches("JLT -> \\d{4}")),
                formatted.toString());
        Assertions.assertTrue(
                formatted.contains("CALL TEST_printString(1 arg)"), formatted.toString());
        Assertions.assertTrue(
                formatted.contains("MOV \"done\" -> stack:string"), formatted.toString());

        // Every instruction formats without problems
        for (Instruction instruction : runtime.getInstructions()) {
            Assertions.assertFalse(InstructionFormatter.format(instruction).contains("?"));
        }
    }
}
