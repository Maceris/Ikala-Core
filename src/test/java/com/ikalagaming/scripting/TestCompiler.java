package com.ikalagaming.scripting;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.stream.Stream;

/**
 * Tests the compiler as a whole, rather than just the validator. Things like literals, and making
 * sure optimizations don't change the meaning of programs.
 *
 * @author Ches Burks
 */
class TestCompiler {

    /** Integer operands used to check constant folding, including negatives. */
    private static final int[][] INT_PAIRS = {{7, 3}, {-7, 3}, {7, -3}, {3, 7}, {5, 5}, {0, 4}};

    /** Double operands used to check constant folding. */
    private static final double[][] DOUBLE_PAIRS = {
        {7.5, 2.5}, {-1.25, 0.5}, {2.5, 7.5}, {3.0, 3.0}
    };

    /** The arithmetic operators. */
    private static final String[] ARITHMETIC = {"+", "-", "*", "/", "%"};

    /** Clear out recorded output. */
    @AfterEach
    void afterEach() {
        DebugMethods.reset();
    }

    /**
     * Folding constants should give the same results as calculating them at runtime, and as Java.
     * Each case is run with constants that get folded, and variables that don't.
     *
     * @param program The program to run.
     * @param expected The expected output.
     */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void constantFolding(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> constantFolding() {
        List<Arguments> cases = new ArrayList<>();

        final IntBinaryOperator[] intFunctions = {
            (a, b) -> a + b, (a, b) -> a - b, (a, b) -> a * b, (a, b) -> a / b, (a, b) -> a % b,
        };
        final DoubleBinaryOperator[] doubleFunctions = {
            (a, b) -> a + b, (a, b) -> a - b, (a, b) -> a * b, (a, b) -> a / b, (a, b) -> a % b,
        };
        for (int op = 0; op < TestCompiler.ARITHMETIC.length; ++op) {
            final String operator = TestCompiler.ARITHMETIC[op];
            for (int[] pair : TestCompiler.INT_PAIRS) {
                final String expected =
                        String.valueOf(intFunctions[op].applyAsInt(pair[0], pair[1]));
                // Parentheses so negative values are parsed as unary minus
                cases.add(
                        ScriptTestHelper.output(
                                "TEST_printInt(("
                                        + pair[0]
                                        + ") "
                                        + operator
                                        + " ("
                                        + pair[1]
                                        + "));",
                                expected));
                cases.add(
                        ScriptTestHelper.output(
                                "int a = "
                                        + pair[0]
                                        + "; int b = "
                                        + pair[1]
                                        + "; TEST_printInt(a "
                                        + operator
                                        + " b);",
                                expected));
            }
            for (double[] pair : TestCompiler.DOUBLE_PAIRS) {
                final String expected =
                        String.valueOf(doubleFunctions[op].applyAsDouble(pair[0], pair[1]));
                cases.add(
                        ScriptTestHelper.output(
                                "TEST_printDouble(("
                                        + pair[0]
                                        + ") "
                                        + operator
                                        + " ("
                                        + pair[1]
                                        + "));",
                                expected));
                cases.add(
                        ScriptTestHelper.output(
                                "double a = "
                                        + pair[0]
                                        + "; double b = "
                                        + pair[1]
                                        + "; TEST_printDouble(a "
                                        + operator
                                        + " b);",
                                expected));
            }
        }

        final String[] relations = {"<", "<=", ">", ">=", "==", "!="};
        for (int[] pair : TestCompiler.INT_PAIRS) {
            final int a = pair[0];
            final int b = pair[1];
            final boolean[] results = {a < b, a <= b, a > b, a >= b, a == b, a != b};
            for (int op = 0; op < relations.length; ++op) {
                final String operator = relations[op];
                final String expected = String.valueOf(results[op]);
                cases.add(
                        ScriptTestHelper.output(
                                "TEST_printBool((" + a + ") " + operator + " (" + b + "));",
                                expected));
                cases.add(
                        ScriptTestHelper.output(
                                "int a = "
                                        + a
                                        + "; int b = "
                                        + b
                                        + "; TEST_printBool(a "
                                        + operator
                                        + " b);",
                                expected));
                cases.add(
                        ScriptTestHelper.output(
                                "if (("
                                        + a
                                        + ") "
                                        + operator
                                        + " ("
                                        + b
                                        + ")) TEST_printBool(true); else TEST_printBool(false);",
                                expected));
            }
        }
        for (String operator : relations) {
            final boolean sameIsTrue =
                    "<=".equals(operator) || ">=".equals(operator) || "==".equals(operator);
            cases.add(
                    ScriptTestHelper.output(
                            "int x = 4; TEST_printBool(x " + operator + " x);",
                            String.valueOf(sameIsTrue)));
            // NaN is not equal to anything, including itself
            cases.add(
                    ScriptTestHelper.output(
                            "double x = 0.0 / 0.0; TEST_printBool(x " + operator + " x);",
                            String.valueOf("!=".equals(operator))));
        }

        final boolean[] booleans = {true, false};
        for (boolean a : booleans) {
            for (boolean b : booleans) {
                cases.add(
                        ScriptTestHelper.output(
                                "TEST_printBool(" + a + " && " + b + ");", String.valueOf(a && b)));
                cases.add(
                        ScriptTestHelper.output(
                                "TEST_printBool(" + a + " || " + b + ");", String.valueOf(a || b)));
                cases.add(
                        ScriptTestHelper.output(
                                "boolean v = " + a + "; TEST_printBool(v && " + b + ");",
                                String.valueOf(a && b)));
                cases.add(
                        ScriptTestHelper.output(
                                "boolean v = " + a + "; TEST_printBool(" + b + " || v);",
                                String.valueOf(b || a)));
            }
            cases.add(ScriptTestHelper.output("TEST_printBool(!" + a + ");", String.valueOf(!a)));
        }
        return cases.stream();
    }

    /**
     * The compiler should never throw, even when evaluating constants would.
     *
     * @param program The program to compile.
     */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @ValueSource(
            strings = {
                "int x = 1 / 0;",
                "int x = 1 % 0;",
                "char c = 'a' / 0;",
                "int x = 10 / (5 - 5);",
                "double d = 1.0 / 0;",
                "int x = 99999999999;",
                "double d = 1.0e999999;",
            })
    void compilerDoesNotThrow(String program) {
        Assertions.assertDoesNotThrow(() -> ScriptTestHelper.compiles(program));
    }

    /**
     * Integer literals that don't fit should be errors, not silently become zero.
     *
     * @param program The program to compile.
     */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @ValueSource(
            strings = {
                "int x = 2147483648;",
                "int x = 99999999999;",
                "int x = -2147483649;",
                // Only valid as the direct operand of a minus sign, like Java
                "int x = -(2147483648);",
                "int x = 2147483648 * -1;"
            })
    void literalsTooLarge(String program) {
        Assertions.assertFalse(ScriptTestHelper.compiles(program));
    }

    /**
     * Literals at the limits of what fits.
     *
     * @param program The program to run.
     * @param expected The expected output.
     */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void literalLimits(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> literalLimits() {
        return Stream.of(
                ScriptTestHelper.output("TEST_printInt(2147483647);", "2147483647"),
                ScriptTestHelper.output("TEST_printInt(-2147483648);", "-2147483648"),
                ScriptTestHelper.output("TEST_printInt(- 2147483648);", "-2147483648"),
                ScriptTestHelper.output("int x = -2147483648; TEST_printInt(x - 1);", "2147483647"),
                ScriptTestHelper.output("TEST_printInt(2147483647 + 1);", "-2147483648"));
    }
}
