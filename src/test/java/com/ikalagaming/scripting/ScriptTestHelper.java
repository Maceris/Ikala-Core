package com.ikalagaming.scripting;

import com.ikalagaming.scripting.IkalaScriptParser.CompilationUnitContext;
import com.ikalagaming.scripting.ast.AbstractSyntaxTree;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.visitors.TreeValidator;
import com.ikalagaming.scripting.ast.visitors.TypePreprocessor;
import com.ikalagaming.scripting.interpreter.ScriptRuntime;

import lombok.NonNull;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utilities for compiling and running scripts in tests.
 *
 * @author Ches Burks
 */
final class ScriptTestHelper {

    /**
     * Whether a program is expected to pass validation. Used instead of a boolean so test names are
     * readable.
     *
     * @author Ches Burks
     */
    enum Validity {
        /** The program should be accepted. */
        VALID,
        /** The program should be rejected. */
        INVALID
    }

    /** The display name for parameterized tests, which just shows the arguments. */
    static final String NAME = "[{index}] {arguments}";

    /** The maximum number of instructions to execute, so broken loops don't hang the build. */
    static final int MAX_INSTRUCTIONS = 100_000;

    /**
     * Check that a program compiles, runs cleanly, and prints exactly the expected lines.
     *
     * @param program The script to run.
     * @param expected The lines we expect to be printed, in order.
     */
    static void assertOutput(@NonNull String program, @NonNull List<String> expected) {
        Assertions.assertEquals(
                expected,
                ScriptTestHelper.runCleanly(program),
                () -> "Unexpected output for: " + program);
    }

    /**
     * Check that a program passes or fails validation, as expected.
     *
     * @param program The script to validate.
     * @param validity Whether it should pass.
     */
    static void assertValidity(@NonNull String program, @NonNull Validity validity) {
        Assertions.assertEquals(
                validity == Validity.VALID,
                ScriptTestHelper.validates(program),
                () -> "Expected " + validity + ": " + program);
    }

    /**
     * Generate test cases for every combination of operands with a binary operator. A combination
     * is valid only if both operands are valid.
     *
     * @param prefix The start of the program, up to the first operand.
     * @param operator The operator that goes between operands.
     * @param valid Operands that are allowed.
     * @param invalid Operands that are not allowed.
     * @return Arguments of the program and its expected validity.
     */
    static Stream<Arguments> binaryOperatorCases(
            @NonNull String prefix,
            @NonNull String operator,
            @NonNull List<String> valid,
            @NonNull List<String> invalid) {
        List<String> all = new ArrayList<>(valid);
        all.addAll(invalid);
        List<Arguments> cases = new ArrayList<>();
        for (String left : all) {
            for (String right : all) {
                final boolean bothValid = valid.contains(left) && valid.contains(right);
                cases.add(
                        Arguments.of(
                                prefix + left + " " + operator + " " + right + ";",
                                bothValid ? Validity.VALID : Validity.INVALID));
            }
        }
        return cases.stream();
    }

    /**
     * Compile a program and run it until it terminates, however that happens. Fails if the program
     * does not compile or does not terminate.
     *
     * @param program The script to run.
     * @return The runtime, after it has terminated.
     */
    static ScriptRuntime run(@NonNull String program) {
        ScriptManager.registerClass(DebugMethods.class);
        DebugMethods.reset();

        Optional<ScriptRuntime> maybeRuntime =
                IkalaScriptCompiler.parse(CharStreams.fromString(program));
        Assertions.assertTrue(maybeRuntime.isPresent(), () -> "Program should compile: " + program);
        ScriptRuntime runtime = maybeRuntime.get();

        int instructions = 0;
        while (!runtime.hasTerminated() && instructions < ScriptTestHelper.MAX_INSTRUCTIONS) {
            runtime.step();
            ++instructions;
        }
        Assertions.assertTrue(
                runtime.hasTerminated(), () -> "Program should terminate: " + program);
        return runtime;
    }

    /**
     * Compile a program, then run it to completion and check that it finished cleanly. That means
     * it did not halt due to an error, and did not leave anything behind on the stack.
     *
     * @param program The script to run.
     * @return Everything the program printed using the debug methods.
     */
    static List<String> runCleanly(@NonNull String program) {
        ScriptRuntime runtime = ScriptTestHelper.run(program);
        Assertions.assertFalse(
                runtime.isFatalError(), () -> "Program should not halt with an error: " + program);
        Assertions.assertEquals(
                0,
                runtime.getStack().size(),
                () -> "Program should not leave values on the stack: " + program);
        return new ArrayList<>(DebugMethods.getOutput());
    }

    /**
     * Check whether a program compiles all the way through, without running it.
     *
     * @param program The script to compile.
     * @return True if it compiled successfully.
     */
    static boolean compiles(@NonNull String program) {
        return IkalaScriptCompiler.parse(CharStreams.fromString(program)).isPresent();
    }

    /**
     * Create test arguments for a program and the output it should produce.
     *
     * @param program The script to run.
     * @param expected The lines we expect it to print.
     * @return The arguments for a parameterized test.
     */
    static Arguments output(@NonNull String program, String... expected) {
        return Arguments.of(program, List.of(expected));
    }

    /**
     * Run a program through the parser, type processing, and validator. Unlike {@link
     * #compiles(String)}, this fails the test if the program has syntax errors, so that we know
     * we're testing the validator and not the parser.
     *
     * @param program The script to validate.
     * @return True if the validator accepted the program.
     */
    static boolean validates(@NonNull String program) {
        ParserErrorListener errorListener = new ParserErrorListener();
        IkalaScriptLexer lexer = new IkalaScriptLexer(CharStreams.fromString(program));
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);
        IkalaScriptParser parser = new IkalaScriptParser(new BufferedTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        CompilationUnitContext context = parser.compilationUnit();
        Assertions.assertEquals(
                0, errorListener.getErrorCount(), () -> "Should parse without errors: " + program);

        CompilationUnit ast = AbstractSyntaxTree.process(context);
        Assertions.assertFalse(ast.isInvalid(), () -> "Should build a syntax tree: " + program);

        new TypePreprocessor().processTreeTypes(ast);
        return new TreeValidator().validate(ast);
    }

    /** Private constructor so that this class is not instantiated. */
    private ScriptTestHelper() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
