package com.ikalagaming.scripting;

import com.ikalagaming.scripting.interpreter.ScriptRuntime;

import lombok.NonNull;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Tests the parts of scripts that work with the host, stepped on the test thread: the standard
 * classes, globals, {@code await} and starting at labels.
 */
class TestHostIntegration {

    /** The tags scripts yielded with, in order. */
    private final List<String> yields = new ArrayList<>();

    /** Register the debug methods. */
    @BeforeEach
    void beforeEach() {
        ScriptManager.registerClass(DebugMethods.class);
        DebugMethods.reset();
        yields.clear();
    }

    /** Clear recorded output. */
    @AfterEach
    void afterEach() {
        DebugMethods.reset();
    }

    /**
     * Compile a script with the standard globals, ready to run on this thread. Yields are recorded
     * instead of going to the script manager.
     *
     * @param program The script.
     * @return The runtime.
     */
    private ScriptRuntime prepare(@NonNull String program) {
        final Map<String, Object> globals = ScriptManager.globalsFor(null);
        final Optional<ScriptRuntime> compiled =
                IkalaScriptCompiler.compile(CharStreams.fromString(program), globals.keySet())
                        .runtime();
        Assertions.assertTrue(compiled.isPresent(), () -> "Program should compile: " + program);
        final ScriptRuntime runtime = compiled.get();
        globals.forEach(runtime::setGlobal);
        runtime.setYieldHandler((script, tag) -> yields.add(tag));
        return runtime;
    }

    /**
     * Step a script until it terminates or yields.
     *
     * @param runtime The script.
     */
    private void runUntilStopped(@NonNull ScriptRuntime runtime) {
        final int yieldsBefore = yields.size();
        int instructions = 0;
        while (!runtime.hasTerminated()
                && yields.size() == yieldsBefore
                && instructions < ScriptTestHelper.MAX_INSTRUCTIONS) {
            runtime.step();
            ++instructions;
        }
    }

    /**
     * Run a script to the end, and check it finished cleanly.
     *
     * @param runtime The script.
     * @return What it printed.
     */
    private List<String> finish(@NonNull ScriptRuntime runtime) {
        runUntilStopped(runtime);
        Assertions.assertTrue(runtime.hasTerminated(), "The script should finish");
        Assertions.assertFalse(runtime.isFatalError(), "The script should not halt with an error");
        Assertions.assertEquals(
                0, runtime.getStack().size(), "Nothing should be left on the stack");
        return List.copyOf(DebugMethods.getOutput());
    }

    /**
     * Scripts calling the standard classes, and what they print.
     *
     * @return The programs and their output.
     */
    static Stream<Arguments> standardLibrary() {
        return Stream.of(
                ScriptTestHelper.output("TEST_printInt(Math.min(3, 7));", "3"),
                ScriptTestHelper.output("TEST_printDouble(Math.max(2.5, 1));", "2.5"),
                ScriptTestHelper.output("TEST_printInt(Math.abs(-4));", "4"),
                // Math.round returns a long, which scripts get as an int
                ScriptTestHelper.output("int r = Math.round(2.6); TEST_printInt(r + 1);", "4"),
                ScriptTestHelper.output("TEST_printDouble(Math.sqrt(16));", "4.0"),
                ScriptTestHelper.output(
                        "Object items = List.of(\"a\", \"b\", \"c\");"
                                + " TEST_printString(items.get(0)); TEST_printInt(items.size());",
                        "a",
                        "3"),
                // More than List.of has fixed versions for, so it takes the variable arguments
                ScriptTestHelper.output(
                        "TEST_printInt(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).size());",
                        "12"),
                ScriptTestHelper.output(
                        "TEST_printString(String.format(\"%d-%s\", 5, \"x\"));", "5-x"),
                ScriptTestHelper.output("TEST_printInt(Integer.parseInt(\"42\") + 1);", "43"),
                ScriptTestHelper.output("TEST_printInt(Map.of(\"k\", 1).get(\"k\"));", "1"),
                ScriptTestHelper.output("TEST_printBool(Set.of(\"x\").contains(\"x\"));", "true"),
                ScriptTestHelper.output("TEST_printBool(Objects.equals(\"a\", \"a\"));", "true"),
                ScriptTestHelper.output("TEST_printBool(Character.isDigit('7'));", "true"),
                ScriptTestHelper.output("TEST_printBool(Boolean.parseBoolean(\"true\"));", "true"),
                ScriptTestHelper.output("TEST_printDouble(Double.parseDouble(\"1.5\"));", "1.5"),
                ScriptTestHelper.output("TEST_printInt(Collections.max(List.of(4, 9, 2)));", "9"));
    }

    /**
     * The standard classes work through ordinary method calls.
     *
     * @param program The script.
     * @param expected What it prints.
     */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource("standardLibrary")
    void testStandardLibrary(String program, List<String> expected) {
        Assertions.assertEquals(expected, finish(prepare(program)));
    }

    /** Classes that reach the system aren't globals, so scripts can't name them. */
    @Test
    void testSystemIsNotAvailable() {
        Assertions.assertFalse(
                IkalaScriptCompiler.compile(
                                CharStreams.fromString("System.exit(0);"),
                                ScriptManager.globalsFor(null).keySet())
                        .succeeded());
    }

    /**
     * Ways a script could reach reflection.
     *
     * @return The programs.
     */
    static Stream<String> reflectionPaths() {
        return Stream.of(
                "Object c = \"text\".getClass();",
                "Object c = List.of(1).getClass();",
                // A class reached some other way still can't be used
                "Object c = TEST_getClassObject(); Object m = c.getName();");
    }

    /**
     * Scripts can't reach reflection, so they can't get at classes the host didn't give them.
     *
     * @param program The script.
     */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource("reflectionPaths")
    void testReflectionIsBlocked(String program) {
        final ScriptRuntime runtime = prepare(program);
        runUntilStopped(runtime);
        Assertions.assertTrue(runtime.isFatalError(), "The call should halt the script");
    }

    /** Static methods of the standard classes that read system properties are hidden. */
    @Test
    void testSystemPropertyReadersAreHidden() {
        final ScriptRuntime runtime = prepare("Object x = Integer.getInteger(\"user.dir\");");
        runUntilStopped(runtime);
        Assertions.assertTrue(runtime.isFatalError());
    }

    /**
     * Programs that use a global in a way that isn't allowed.
     *
     * @return The programs.
     */
    static Stream<String> invalidGlobalUses() {
        return Stream.of(
                "Math = null;",
                "int Math = 1;",
                "Object List = null;",
                "Math: TEST_printInt(Math.abs(1));");
    }

    /**
     * Globals can't be assigned to or redeclared.
     *
     * @param program The script.
     */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource("invalidGlobalUses")
    void testGlobalsAreReadOnly(String program) {
        Assertions.assertFalse(
                IkalaScriptCompiler.compile(
                                CharStreams.fromString(program),
                                ScriptManager.globalsFor(null).keySet())
                        .succeeded());
    }

    /** A host object given as a global can be used by name. */
    @Test
    void testHostGlobal() {
        final Optional<ScriptRuntime> compiled =
                IkalaScriptCompiler.compile(
                                CharStreams.fromString("TEST_printInt(names.size());"),
                                Set.of("names"))
                        .runtime();
        Assertions.assertTrue(compiled.isPresent());
        compiled.get().setGlobal("names", List.of("a", "b"));
        Assertions.assertEquals(List.of("2"), finish(compiled.get()));
    }

    /** Records which script called it, for {@link #testCurrentScript()}. */
    public static final class CallerProbe {
        /** The script that called {@link #record()}, if any. */
        private Optional<ScriptRuntime> caller = Optional.empty();

        /** Note the script that is calling. */
        public void record() {
            caller = ScriptRuntime.current();
        }
    }

    /** Host methods can tell which script called them, and nothing is current outside a step. */
    @Test
    void testCurrentScript() {
        final Optional<ScriptRuntime> compiled =
                IkalaScriptCompiler.compile(
                                CharStreams.fromString("probe.record();"), Set.of("probe"))
                        .runtime();
        Assertions.assertTrue(compiled.isPresent());
        final CallerProbe probe = new CallerProbe();
        compiled.get().setGlobal("probe", probe);
        finish(compiled.get());
        Assertions.assertSame(compiled.get(), probe.caller.orElse(null));
        Assertions.assertTrue(ScriptRuntime.current().isEmpty());
    }

    /** Without the global, the name is unknown, the same as before globals existed. */
    @Test
    void testUnknownNameStillFails() {
        Assertions.assertFalse(ScriptTestHelper.compiles("TEST_printInt(names.size());"));
    }

    /** Await parks the script, and returns the value it is resumed with. */
    @Test
    void testAwaitReturnsValue() {
        final ScriptRuntime runtime =
                prepare("int choice = await(\"pick\"); TEST_printInt(choice + 1);");
        runUntilStopped(runtime);
        Assertions.assertEquals(List.of("pick"), yields);
        Assertions.assertEquals("pick", runtime.getAwaitTag());

        runtime.resumeWith(41);
        Assertions.assertNull(runtime.getAwaitTag());
        Assertions.assertEquals(List.of("42"), finish(runtime));
    }

    /**
     * Values from Java are converted to types scripts can use.
     *
     * @return The values, and what the script prints for them.
     */
    static Stream<Arguments> convertedValues() {
        return Stream.of(
                Arguments.of(41L, "int", "TEST_printInt", "41"),
                Arguments.of((short) 3, "int", "TEST_printInt", "3"),
                Arguments.of(2.5f, "double", "TEST_printDouble", "2.5"),
                Arguments.of(3_000_000_000L, "double", "TEST_printDouble", "3.0E9"),
                Arguments.of("text", "string", "TEST_printString", "text"));
    }

    /**
     * Resumed values are normalized.
     *
     * @param value The value from Java.
     * @param type The type the script stores it in.
     * @param printer The debug method that prints that type.
     * @param expected What it prints.
     */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource("convertedValues")
    void testAwaitConvertsValues(Object value, String type, String printer, String expected) {
        final ScriptRuntime runtime = prepare(type + " v = await(\"value\"); " + printer + "(v);");
        runUntilStopped(runtime);
        runtime.resumeWith(value);
        Assertions.assertEquals(List.of(expected), finish(runtime));
    }

    /** A value posted before the script reaches the await is returned straight away. */
    @Test
    void testPostedValueIsUsedImmediately() {
        final ScriptRuntime runtime =
                prepare("int choice = await(\"pick\"); TEST_printInt(choice);");
        runtime.post("pick", 7);
        Assertions.assertEquals(List.of("7"), finish(runtime));
        Assertions.assertTrue(yields.isEmpty(), "It should never have yielded");
    }

    /** Posted values for a tag are delivered oldest first, one per await. */
    @Test
    void testPostedValuesQueue() {
        final ScriptRuntime runtime =
                prepare("TEST_printInt(await(\"n\")); TEST_printInt(await(\"n\"));");
        runtime.post("n", 1);
        runtime.post("n", 2);
        Assertions.assertEquals(List.of("1", "2"), finish(runtime));
    }

    /** An await whose result isn't used leaves nothing on the stack. */
    @Test
    void testAwaitAsStatement() {
        final ScriptRuntime runtime = prepare("await(\"go\"); TEST_printString(\"done\");");
        runUntilStopped(runtime);
        runtime.resumeWith(3);
        Assertions.assertEquals(List.of("done"), finish(runtime));
    }

    /** Await needs a string tag. */
    @Test
    void testAwaitNeedsStringTag() {
        final ScriptRuntime runtime = prepare("int x = await(5);");
        runUntilStopped(runtime);
        Assertions.assertTrue(runtime.isFatalError());
    }

    /** A script can start at a label, skipping what comes before it. */
    @Test
    void testStartAtLabel() {
        final ScriptRuntime runtime =
                prepare("TEST_printString(\"a\"); start: TEST_printString(\"b\");");
        Assertions.assertTrue(runtime.startAt("start"));
        Assertions.assertEquals(List.of("b"), finish(runtime));
    }

    /** A copy of the program has its labels too, and none of the original's state. */
    @Test
    void testCopyProgramKeepsLabels() {
        final ScriptRuntime original =
                prepare("TEST_printString(\"a\"); start: TEST_printString(\"b\");");
        final ScriptRuntime copy = original.copyProgram();
        copy.setYieldHandler((script, tag) -> yields.add(tag));
        Assertions.assertTrue(copy.startAt("start"));
        Assertions.assertEquals(List.of("b"), finish(copy));
    }

    /**
     * Labels that can't be started at: variables are declared before them in their scope, or they
     * don't exist.
     *
     * @return The programs and labels.
     */
    static Stream<Arguments> invalidEntryLabels() {
        return Stream.of(
                Arguments.of("int x = 1; later: TEST_printInt(x);", "later"),
                Arguments.of("TEST_printString(\"a\");", "missing"),
                // Generated labels for loops aren't entry points either
                Arguments.of("while (false) {}", ".L0"));
    }

    /**
     * Starting at an invalid label changes nothing.
     *
     * @param program The script.
     * @param label The label.
     */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource("invalidEntryLabels")
    void testInvalidEntryLabel(String program, String label) {
        final ScriptRuntime runtime = prepare(program);
        Assertions.assertFalse(runtime.startAt(label));
        Assertions.assertEquals(0, runtime.getProgramCounter());
    }

    /** Releasing a script drops its globals and variables. */
    @Test
    void testReleaseDropsEverything() {
        final ScriptRuntime runtime = prepare("int x = 1; int y = await(\"t\");");
        runUntilStopped(runtime);
        runtime.post("other", new Object());
        runtime.release();
        Assertions.assertTrue(runtime.hasTerminated());
        Assertions.assertTrue(runtime.getSymbolTable().isEmpty());
        Assertions.assertNull(runtime.getAwaitTag());
    }
}
