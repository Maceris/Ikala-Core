package com.ikalagaming.scripting;

import com.ikalagaming.scripting.interpreter.Instruction;
import com.ikalagaming.scripting.interpreter.InstructionType;
import com.ikalagaming.scripting.interpreter.ScriptRuntime;

import org.antlr.v4.runtime.CharStreams;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

/**
 * Tests source line numbers, breakpoints, and terminating scripts.
 *
 * @author Ches Burks
 */
class TestBreakpoints {

    /** How long to wait for the runner to do something. */
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    /** Shut down the runner thread. */
    @AfterAll
    static void afterAll() {
        ScriptManager.shutdown();
    }

    /** Register the debug methods. */
    @BeforeAll
    static void beforeAll() {
        ScriptManager.registerClass(DebugMethods.class);
    }

    /** Clear out recorded output. */
    @AfterEach
    void afterEach() {
        DebugMethods.reset();
    }

    private static ScriptRuntime compile(String program) {
        IkalaScriptCompiler.CompileResult result =
                IkalaScriptCompiler.compile(CharStreams.fromString(program));
        Assertions.assertTrue(result.succeeded(), result.errors().toString());
        return result.runtime().get();
    }

    /**
     * Run a script until it terminates, with a limit so a broken test can't hang.
     *
     * @param runtime The script.
     */
    private static void runToEnd(ScriptRuntime runtime) {
        for (int i = 0; i < 10_000 && !runtime.hasTerminated(); ++i) {
            runtime.step();
        }
        Assertions.assertTrue(runtime.hasTerminated());
    }

    /** Instructions remember which line of source they came from. */
    @Test
    void lineNumbers() {
        ScriptRuntime runtime =
                compile(
                        """
                        TEST_printString("one");

                        TEST_printString("three");
                        if (true) {
                            TEST_printString("five");
                        }
                        """);
        List<Integer> lines = new ArrayList<>();
        for (Instruction instruction : runtime.getInstructions()) {
            lines.add(instruction.line());
            Assertions.assertTrue(instruction.line() > 0, "Missing line for " + instruction);
        }
        Assertions.assertTrue(lines.contains(1));
        Assertions.assertFalse(lines.contains(2));
        Assertions.assertTrue(lines.contains(3));
        Assertions.assertTrue(lines.contains(5));

        Assertions.assertEquals(OptionalInt.of(0), runtime.getFirstInstructionOnLine(1));
        Assertions.assertTrue(runtime.getFirstInstructionOnLine(2).isEmpty());
    }

    /**
     * A patched breakpoint calls the handler without running the instruction, then runs it when the
     * script continues.
     */
    @Test
    void patchedBreakpoint() {
        ScriptRuntime runtime =
                compile(
                        """
                        TEST_printString("one");
                        TEST_printString("two");
                        """);
        final int lineTwo = runtime.getFirstInstructionOnLine(2).getAsInt();
        final Instruction original = runtime.getInstructions().get(lineTwo);
        Assertions.assertTrue(runtime.setBreakpoint(lineTwo));
        Assertions.assertTrue(runtime.hasBreakpoint(lineTwo));
        Assertions.assertEquals(
                InstructionType.BREAKPOINT, runtime.getInstructions().get(lineTwo).type());
        Assertions.assertEquals(original, runtime.getOriginalInstruction(lineTwo));

        List<Integer> hits = new ArrayList<>();
        runtime.setBreakpointHandler((script, index) -> hits.add(index));

        // Run until the breakpoint
        while (hits.isEmpty() && !runtime.hasTerminated()) {
            runtime.step();
        }
        Assertions.assertEquals(List.of(lineTwo), hits);
        Assertions.assertEquals(lineTwo, runtime.getProgramCounter());
        Assertions.assertEquals(List.of("one"), DebugMethods.getOutput());

        // Continuing runs the original instruction, without hitting the breakpoint again
        runToEnd(runtime);
        Assertions.assertEquals(List.of(lineTwo), hits);
        Assertions.assertEquals(List.of("one", "two"), DebugMethods.getOutput());
        Assertions.assertFalse(runtime.isFatalError());
    }

    /** Breakpoints in loops are hit every time around, and can be removed. */
    @Test
    void breakpointInLoop() {
        ScriptRuntime runtime =
                compile(
                        """
                        for (int i = 0; i < 3; ++i) {
                            TEST_printInt(i);
                        }
                        """);
        final int body = runtime.getFirstInstructionOnLine(2).getAsInt();
        runtime.setBreakpoint(body);
        List<Integer> hits = new ArrayList<>();
        runtime.setBreakpointHandler(
                (script, index) -> {
                    hits.add(index);
                    if (hits.size() == 2) {
                        script.clearBreakpoint(index);
                    }
                });

        runToEnd(runtime);
        Assertions.assertEquals(2, hits.size());
        Assertions.assertEquals(List.of("0", "1", "2"), DebugMethods.getOutput());
        Assertions.assertTrue(runtime.getBreakpoints().isEmpty());
    }

    /** Clearing a breakpoint puts the original instruction back. */
    @Test
    void clearBreakpoints() {
        ScriptRuntime runtime = compile("TEST_printString(\"one\");");
        final List<Instruction> before = List.copyOf(runtime.getInstructions());

        Assertions.assertFalse(runtime.setBreakpoint(-1));
        Assertions.assertFalse(runtime.setBreakpoint(before.size()));
        runtime.setBreakpoint(0);
        runtime.setBreakpoint(1);
        Assertions.assertEquals(2, runtime.getBreakpoints().size());
        runtime.clearBreakpoints();

        Assertions.assertEquals(before, runtime.getInstructions());
        Assertions.assertFalse(runtime.clearBreakpoint(0));
    }

    /** A breakpoint written in the script calls the handler and keeps going. */
    @Test
    void inlineBreakpoint() {
        ScriptRuntime runtime =
                compile(
                        """
                        TEST_printString("one");
                        breakpoint();
                        TEST_printString("two");
                        """);
        List<Integer> hits = new ArrayList<>();
        runtime.setBreakpointHandler((script, index) -> hits.add(index));

        runToEnd(runtime);
        Assertions.assertEquals(1, hits.size());
        Assertions.assertEquals(List.of("one", "two"), DebugMethods.getOutput());
    }

    /** Without a breakpoint handler, breakpoints yield with the breakpoint tag. */
    @Test
    void breakpointWithoutHandlerYields() {
        ScriptRuntime runtime = compile("breakpoint();");
        List<String> tags = new ArrayList<>();
        runtime.setYieldHandler((script, tag) -> tags.add(tag));

        runToEnd(runtime);
        Assertions.assertEquals(List.of(ScriptRuntime.BREAKPOINT_TAG), tags);
    }

    /** Scripts in the script manager can be terminated, whether running or yielded. */
    @Test
    void terminate() {
        Assertions.assertTrue(ScriptManager.runScript("while (true) {}", "Forever"));
        Assertions.assertTrue(ScriptManager.runScript("yield(\"never\");", "Waiting"));

        Awaitility.await()
                .atMost(TIMEOUT)
                .until(
                        () ->
                                ScriptManager.getRunningScripts().stream()
                                                .anyMatch(s -> "Forever".equals(s.getName()))
                                        && ScriptManager.getYieldedScripts()
                                                .containsValue("never"));

        ScriptRuntime forever =
                ScriptManager.getRunningScripts().stream()
                        .filter(s -> "Forever".equals(s.getName()))
                        .findFirst()
                        .orElseThrow();
        ScriptRuntime waiting = ScriptManager.getYieldedScripts().keySet().iterator().next();

        Assertions.assertTrue(ScriptManager.terminate(forever));
        Assertions.assertTrue(ScriptManager.terminate(waiting));
        Assertions.assertTrue(forever.hasTerminated());
        Assertions.assertFalse(ScriptManager.getRunningScripts().contains(forever));
        Assertions.assertTrue(ScriptManager.getYieldedScripts().isEmpty());
        Assertions.assertFalse(ScriptManager.terminate(forever));
    }

    /**
     * A for loop's line runs in two places, the initializer before the loop and the update after
     * each iteration, so it has two starts.
     */
    @Test
    void lineStartsForLoop() {
        ScriptRuntime runtime =
                compile(
                        """
                        int x = 0;
                        for (int i = 0; i < 3; ++i) {
                            x += i;
                        }
                        """);
        List<Integer> starts = runtime.getLineStartInstructions(2);
        Assertions.assertEquals(2, starts.size());
        Assertions.assertEquals(runtime.getFirstInstructionOnLine(2).getAsInt(), starts.get(0));
        for (int start : starts) {
            Assertions.assertEquals(2, runtime.getOriginalInstruction(start).line());
            Assertions.assertNotEquals(2, runtime.getOriginalInstruction(start - 1).line());
        }
        Assertions.assertEquals(1, runtime.getLineStartInstructions(3).size());
        Assertions.assertTrue(runtime.getLineStartInstructions(5).isEmpty());
    }
}
