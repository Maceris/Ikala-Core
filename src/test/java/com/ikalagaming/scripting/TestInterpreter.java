package com.ikalagaming.scripting;

import com.ikalagaming.scripting.interpreter.ScriptRuntime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Tests that compiled scripts actually behave the way they should when executed, end to end through
 * the compiler and runtime. Every program must finish without errors and leave nothing on the
 * stack, in addition to printing the expected output.
 *
 * @author Ches Burks
 */
class TestInterpreter {

    /** A program that prints different things depending on the value of x, for if statements. */
    private static final String IF_ELSE =
            """
            int x = %d;
            if (x < 0) {
                TEST_printString("negative");
            } else if (x == 0) {
                TEST_printString("zero");
            } else {
                TEST_printString("positive");
            }
            """;

    /** A program with every kind of switch label, which switches on x. */
    private static final String SWITCH =
            """
            int x = %d;
            switch (x) {
                case 1:
                    TEST_printString("one");
                    break;
                case 2:
                case 3:
                    TEST_printString("two or three");
                case 4:
                    TEST_printString("four");
                    break;
                default:
                    TEST_printString("default");
            }
            TEST_printString("end");
            """;

    /** Clear out recorded output. */
    @AfterEach
    void afterEach() {
        DebugMethods.reset();
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void arithmetic(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> arithmetic() {
        return Stream.of(
                ScriptTestHelper.output("TEST_printInt(2 + 3 * 4 - 6 / 2);", "11"),
                ScriptTestHelper.output("TEST_printInt(10 - 3 - 2);", "5"),
                ScriptTestHelper.output("int a = 10; TEST_printInt(a - 3 - 2);", "5"),
                ScriptTestHelper.output("int a = 10; TEST_printInt(a / 3);", "3"),
                ScriptTestHelper.output("int a = -7; TEST_printInt(a % 3);", "-1"),
                ScriptTestHelper.output("int a = 5; TEST_printInt(-a);", "-5"),
                ScriptTestHelper.output("int a = 5; TEST_printInt(-(-a));", "5"),
                ScriptTestHelper.output("TEST_printInt((2 + 3) * 4);", "20"),
                ScriptTestHelper.output(
                        "int i = 3; double d = i / 2.0; TEST_printDouble(d);", "1.5"),
                ScriptTestHelper.output("double d = 7 / 2; TEST_printDouble(d);", "3.0"),
                ScriptTestHelper.output("double d = 1.5; TEST_printDouble(-d);", "-1.5"),
                ScriptTestHelper.output(
                        "int i = 2; double d = 0.5; TEST_printDouble(i * d);", "1.0"),
                ScriptTestHelper.output("char c = 'a'; int i = c + 1; TEST_printInt(i);", "98"),
                ScriptTestHelper.output(
                        "char c = 'a'; char d = c + 'b'; TEST_printInt((int) d);", "195"));
    }

    /** Math involving method calls, whose types we can't know until runtime. */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void arithmeticWithUnknownTypes(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> arithmeticWithUnknownTypes() {
        return Stream.of(
                ScriptTestHelper.output("TEST_printInt(TEST_getInt() + 1);", "-4566"),
                ScriptTestHelper.output("TEST_printInt(1 - TEST_getInt());", "4568"),
                ScriptTestHelper.output("TEST_printDouble(TEST_getDouble() * 2);", "241.8428"),
                ScriptTestHelper.output("TEST_printInt(-TEST_getInt());", "4567"),
                ScriptTestHelper.output("TEST_printDouble(-TEST_getDouble());", "-120.9214"),
                ScriptTestHelper.output("TEST_printString(\"x\" + TEST_getInt());", "x-4567"),
                // + works out whether to add or concatenate at runtime
                ScriptTestHelper.output(
                        "TEST_printString(TEST_getString() + TEST_getString());",
                        "Sample string!Sample string!"),
                ScriptTestHelper.output("TEST_printInt(TEST_getInt() + TEST_getInt());", "-9134"),
                ScriptTestHelper.output(
                        "TEST_printDouble(TEST_getDouble() + TEST_getInt());",
                        String.valueOf(120.9214 + -4567)),
                ScriptTestHelper.output("TEST_printInt(TEST_getChar() + TEST_getChar());", "194"),
                ScriptTestHelper.output(
                        "TEST_printString(TEST_getString() + TEST_getInt());",
                        "Sample string!-4567"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void assignment(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> assignment() {
        return Stream.of(
                ScriptTestHelper.output("int x = 10; x = 3; TEST_printInt(x);", "3"),
                ScriptTestHelper.output("int x = 10; x += 3; TEST_printInt(x);", "13"),
                ScriptTestHelper.output("int x = 10; x -= 3; TEST_printInt(x);", "7"),
                ScriptTestHelper.output("int x = 10; x *= 3; TEST_printInt(x);", "30"),
                ScriptTestHelper.output("int x = 10; x /= 3; TEST_printInt(x);", "3"),
                ScriptTestHelper.output("int x = 10; x %= 3; TEST_printInt(x);", "1"),
                ScriptTestHelper.output("double x = 10; x -= 2.5; TEST_printDouble(x);", "7.5"),
                ScriptTestHelper.output("double x = 10; x /= 4; TEST_printDouble(x);", "2.5"),
                ScriptTestHelper.output("double x = 1.5; x += 1; TEST_printDouble(x);", "2.5"),
                ScriptTestHelper.output("char c = 'b'; c -= 'a'; TEST_printInt((int) c);", "1"),
                ScriptTestHelper.output(
                        "string s = \"a\"; s += \"b\"; s += 1; s += 'c'; TEST_printString(s);",
                        "ab1c"),
                // assignments are expressions, evaluating to the assigned value
                ScriptTestHelper.output(
                        "int a; int b; a = b = 3; TEST_printInt(a); TEST_printInt(b);", "3", "3"),
                ScriptTestHelper.output("int a; int b = (a = 4) + 1; TEST_printInt(b);", "5"),
                ScriptTestHelper.output("int a = 1; TEST_printInt(a += 2);", "3"));
    }

    /** Breaking and continuing should only affect the innermost loop or switch. */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void breakAndContinue(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> breakAndContinue() {
        return Stream.of(
                ScriptTestHelper.output(
                        """
                        int n = 0;
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                if (j == 1) {
                                    break;
                                }
                                n++;
                            }
                            if (i == 1) {
                                break;
                            }
                        }
                        TEST_printInt(n);
                        """,
                        "2"),
                ScriptTestHelper.output(
                        """
                        int n = 0;
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 2; j++) {
                                n++;
                            }
                            if (i == 0) {
                                continue;
                            }
                            n += 10;
                        }
                        TEST_printInt(n);
                        """,
                        "26"),
                ScriptTestHelper.output(
                        """
                        int i = 0;
                        int n = 0;
                        while (i < 3) {
                            i++;
                            int j = 0;
                            do {
                                j++;
                                if (j == 2) {
                                    continue;
                                }
                                n++;
                            } while (j < 3);
                            if (i == 2) {
                                continue;
                            }
                            n += 100;
                        }
                        TEST_printInt(n);
                        """,
                        "206"),
                ScriptTestHelper.output(
                        """
                        int n = 0;
                        for (int i = 0; i < 3; i++) {
                            switch (i) {
                                case 1:
                                    n += 10;
                                    break;
                                default:
                                    n++;
                            }
                        }
                        TEST_printInt(n);
                        """,
                        "12"),
                ScriptTestHelper.output(
                        """
                        int i = 0;
                        while (i < 10) {
                            switch (i) {
                                case 0:
                                    break;
                            }
                            i++;
                            if (i == 2) {
                                break;
                            }
                        }
                        TEST_printInt(i);
                        """,
                        "2"),
                ScriptTestHelper.output(
                        """
                        int n = 0;
                        for (int i = 0; i < 4; i++) {
                            switch (i) {
                                case 1:
                                    continue;
                            }
                            n++;
                        }
                        TEST_printInt(n);
                        """,
                        "3"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void casts(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> casts() {
        return Stream.of(
                ScriptTestHelper.output("TEST_printInt((int) 3.9);", "3"),
                ScriptTestHelper.output("TEST_printInt((int) -3.9);", "-3"),
                ScriptTestHelper.output("TEST_printInt((int) 'a');", "97"),
                ScriptTestHelper.output("TEST_printChar((char) 98);", "b"),
                ScriptTestHelper.output("TEST_printDouble((double) 2);", "2.0"),
                ScriptTestHelper.output("double d = 2.5; TEST_printInt((int) d);", "2"),
                ScriptTestHelper.output("string s = (string) 5; TEST_printString(s);", "5"),
                ScriptTestHelper.output("TEST_printBool((boolean) 1);", "true"),
                ScriptTestHelper.output("TEST_printBool((boolean) 0);", "false"),
                ScriptTestHelper.output("TEST_printBool((boolean) \"x\");", "true"),
                ScriptTestHelper.output("TEST_printBool((boolean) \"\");", "false"),
                // reference casts can't be checked, but should pass the value through
                ScriptTestHelper.output(
                        """
                        Object o = TEST_getObject();
                        TestObject t = (TestObject) o;
                        t.setInteger(4);
                        TEST_printInt(t.getInteger());
                        """,
                        "4"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void comparisons(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> comparisons() {
        return Stream.of(
                ScriptTestHelper.output(
                        "int a = 1; int b = 2; TEST_printBool(a < b); TEST_printBool(b < a);",
                        "true",
                        "false"),
                ScriptTestHelper.output(
                        "int a = 1; boolean b = a >= 1; TEST_printBool(b); TEST_printBool(a != 1);",
                        "true",
                        "false"),
                ScriptTestHelper.output(
                        "int i = 1; if (i < 2.5) { TEST_printString(\"lt\"); }", "lt"),
                ScriptTestHelper.output(
                        "double d = 2.5; if (d > 2) { TEST_printString(\"gt\"); }", "gt"),
                ScriptTestHelper.output("char c = 'b'; TEST_printBool(c > 'a');", "true"),
                ScriptTestHelper.output("char c = 'a'; TEST_printBool(c == 97);", "true"),
                ScriptTestHelper.output(
                        "if (TEST_getInt() < 0) { TEST_printString(\"neg\"); }", "neg"),
                ScriptTestHelper.output("int x = 3; TEST_printBool(x >= x);", "true"),
                ScriptTestHelper.output("int x = 3; TEST_printBool(x > x);", "false"),
                // comparisons are exact, like Java
                ScriptTestHelper.output(
                        "double d = 0.000001; TEST_printBool(d == 0); TEST_printBool(d > 0);",
                        "false",
                        "true"),
                ScriptTestHelper.output(
                        "double a = 0.1; double b = 0.2; TEST_printBool(a + b == 0.3);",
                        String.valueOf(0.1 + 0.2 == 0.3)),
                ScriptTestHelper.output(
                        """
                        double n = 0.0 / 0.0;
                        TEST_printBool(n == 1);
                        TEST_printBool(n != 1);
                        TEST_printBool(n < 1);
                        TEST_printBool(n >= 1);
                        if (n > 1 || n <= 1) TEST_printString("ordered");
                        """,
                        "false",
                        "true",
                        "false",
                        "false"),
                // equality works on values, not references
                ScriptTestHelper.output(
                        "string a = \"x\"; if (a == \"x\") { TEST_printString(\"eq\"); }", "eq"),
                ScriptTestHelper.output(
                        "string a = \"x\"; if (a != \"y\") { TEST_printString(\"ne\"); }", "ne"),
                ScriptTestHelper.output(
                        "boolean a = true; if (a == true) { TEST_printString(\"t\"); }", "t"),
                ScriptTestHelper.output(
                        "Object o = TEST_getNull(); if (o == null) { TEST_printString(\"null\"); }",
                        "null"),
                ScriptTestHelper.output(
                        "Object o = TEST_getNull(); if (null != o) { TEST_printString(\"x\"); }"),
                ScriptTestHelper.output(
                        "Object o = TEST_getObject(); if (o != null) { TEST_printString(\"obj\"); }",
                        "obj"));
    }

    /** Conditions in loops and if statements can be any boolean expression. */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void conditions(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> conditions() {
        return Stream.of(
                ScriptTestHelper.output(
                        "if (true) TEST_printString(\"a\"); if (false) TEST_printString(\"b\");",
                        "a"),
                ScriptTestHelper.output(
                        "if (3 > 2) TEST_printString(\"y\"); else TEST_printString(\"n\");", "y"),
                ScriptTestHelper.output(
                        "if (2 > 3) TEST_printString(\"y\"); else TEST_printString(\"n\");", "n"),
                ScriptTestHelper.output(
                        "boolean b = false; if (b) TEST_printString(\"y\"); else TEST_printString(\"n\");",
                        "n"),
                ScriptTestHelper.output(
                        "if (TEST_getBoolean()) TEST_printString(\"call\");", "call"),
                ScriptTestHelper.output(
                        "boolean b = false; if (!b) TEST_printString(\"not\");", "not"),
                ScriptTestHelper.output(
                        "int i = 0; while (true) { i++; if (i == 3) break; } TEST_printInt(i);",
                        "3"),
                ScriptTestHelper.output(
                        "boolean go = true; int i = 0; while (go) { i++; go = i < 3; } TEST_printInt(i);",
                        "3"),
                ScriptTestHelper.output(
                        "boolean go = true; int i = 0; do { i++; go = i < 3; } while (go); TEST_printInt(i);",
                        "3"),
                ScriptTestHelper.output(
                        "int i = 0; for (; TEST_getBoolean(); ) { i++; if (i > 2) break; } TEST_printInt(i);",
                        "3"),
                ScriptTestHelper.output(
                        "int i = 0; for (;;) { if (++i == 4) break; } TEST_printInt(i);", "4"),
                ScriptTestHelper.output(
                        "int i = 0; boolean go = true; while (go ? i < 3 : false) i++; TEST_printInt(i);",
                        "3"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void declarations(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> declarations() {
        return Stream.of(
                ScriptTestHelper.output(
                        """
                        int i; double d; char c; boolean b; string s;
                        TEST_printInt(i);
                        TEST_printDouble(d);
                        TEST_printInt((int) c);
                        TEST_printBool(b);
                        TEST_printString(s);
                        """,
                        "0",
                        "0.0",
                        "0",
                        "false",
                        ""),
                ScriptTestHelper.output("int x = 5; int y = x; TEST_printInt(y);", "5"),
                ScriptTestHelper.output(
                        "int a = 1, b = a + 1, c; TEST_printInt(a); TEST_printInt(b); TEST_printInt(c);",
                        "1",
                        "2",
                        "0"),
                ScriptTestHelper.output("int x = 2; double d = x; TEST_printDouble(d);", "2.0"),
                ScriptTestHelper.output("double d; d = 3; TEST_printDouble(d / 2);", "1.5"),
                ScriptTestHelper.output("double d; d = d + 1.5; TEST_printDouble(d);", "1.5"),
                ScriptTestHelper.output("boolean b = 1 < 2; TEST_printBool(b);", "true"),
                // variables in different scopes can reuse names with different types
                ScriptTestHelper.output(
                        "{ int x = 1; } { string x = \"a\"; TEST_printString(x); }", "a"),
                ScriptTestHelper.output(
                        "for (int i = 0; i < 2; i++) { string s = \"\" + i; TEST_printString(s); }",
                        "0",
                        "1"),
                // values are converted to the declared type where Java would do so
                ScriptTestHelper.output("int x = TEST_getChar(); TEST_printInt(x);", "97"),
                ScriptTestHelper.output(
                        "double d = TEST_getInt(); TEST_printDouble(d);", "-4567.0"),
                // objects can hold anything, including null
                ScriptTestHelper.output(
                        "Object o = TEST_getObject(); o = TEST_getNull(); o = TEST_getString(); TEST_printObject(o);",
                        "Sample string!"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void escapeSequences(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> escapeSequences() {
        return Stream.of(
                ScriptTestHelper.output("TEST_printString(\"a\\tb\");", "a\tb"),
                ScriptTestHelper.output("TEST_printString(\"line\\nline\");", "line\nline"),
                ScriptTestHelper.output("TEST_printString(\"\\\"quoted\\\"\");", "\"quoted\""),
                ScriptTestHelper.output("TEST_printString(\"back\\\\slash\");", "back\\slash"),
                ScriptTestHelper.output("TEST_printString(\"\\b\\f\\r\\s\");", "\b\f\r "),
                ScriptTestHelper.output("TEST_printString(\"\\u0041\\uuu0042\");", "AB"),
                ScriptTestHelper.output("TEST_printString(\"it's\");", "it's"),
                ScriptTestHelper.output("TEST_printChar('\\'');", "'"),
                ScriptTestHelper.output("TEST_printChar('\\\\');", "\\"),
                ScriptTestHelper.output("TEST_printChar('\"');", "\""),
                ScriptTestHelper.output("TEST_printInt((int) '\\n');", "10"),
                ScriptTestHelper.output("TEST_printChar('\\u00e9');", "é"),
                ScriptTestHelper.output("TEST_printInt(\"\\t\\\\\".length());", "2"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void exit(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> exit() {
        return Stream.of(
                ScriptTestHelper.output(
                        "TEST_printString(\"a\"); exit; TEST_printString(\"b\");", "a"),
                ScriptTestHelper.output(
                        "for (int i = 0; i < 5; i++) { if (i == 2) exit; TEST_printInt(i); }",
                        "0",
                        "1"));
    }

    /** Tries to execute the fizzbuzz program, and compares it with Java. */
    @Test
    void fizzBuzz() {
        final String program =
                """
                for (int i = 1; i <= 100; ++i) {
                    string result;
                    if (i % 3 == 0 && i % 5 == 0) {
                        result = "FizzBuzz";
                    } else if (i % 3 == 0) {
                        result = "Fizz";
                    } else if (i % 5 == 0) {
                        result = "Buzz";
                    } else {
                        result = "" + i;
                    }
                    TEST_printString(result);
                }
                """;
        List<String> expected =
                IntStream.rangeClosed(1, 100)
                        .mapToObj(
                                i -> {
                                    if (i % 15 == 0) {
                                        return "FizzBuzz";
                                    }
                                    if (i % 3 == 0) {
                                        return "Fizz";
                                    }
                                    if (i % 5 == 0) {
                                        return "Buzz";
                                    }
                                    return String.valueOf(i);
                                })
                        .toList();
        ScriptTestHelper.assertOutput(program, expected);
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void gotoJumps(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> gotoJumps() {
        return Stream.of(
                ScriptTestHelper.output(
                        "int i = 0; top: i++; if (i < 3) goto top; TEST_printInt(i);", "3"),
                ScriptTestHelper.output(
                        "goto end; TEST_printString(\"skipped\"); end: TEST_printString(\"end\");",
                        "end"),
                ScriptTestHelper.output(
                        """
                        for (int i = 0; i < 10; i++) {
                            for (int j = 0; j < 10; j++) {
                                if (i == 1 && j == 1) {
                                    goto out;
                                }
                            }
                            TEST_printInt(i);
                        }
                        out:
                        TEST_printString("out");
                        """,
                        "0",
                        "out"),
                ScriptTestHelper.output("goto end; end:"),
                // jumping into a block is fine if it doesn't skip any declarations
                ScriptTestHelper.output(
                        "int i = 5; goto in; while (i < 3) { in: TEST_printInt(i); i++; }", "5"),
                // jumping backwards before a declaration runs it again
                ScriptTestHelper.output(
                        "int n = 0; top: int x = n; n++; if (n < 3) goto top; TEST_printInt(x);",
                        "2"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @ValueSource(ints = {-1, 0, 1})
    void ifElse(int x) {
        final String expected;
        if (x < 0) {
            expected = "negative";
        } else if (x == 0) {
            expected = "zero";
        } else {
            expected = "positive";
        }
        ScriptTestHelper.assertOutput(String.format(TestInterpreter.IF_ELSE, x), List.of(expected));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void incrementDecrement(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> incrementDecrement() {
        return Stream.of(
                ScriptTestHelper.output(
                        "int x = 1; int y = x++; TEST_printInt(x); TEST_printInt(y);", "2", "1"),
                ScriptTestHelper.output(
                        "int x = 1; int y = ++x; TEST_printInt(x); TEST_printInt(y);", "2", "2"),
                ScriptTestHelper.output(
                        "int x = 1; int y = x--; TEST_printInt(x); TEST_printInt(y);", "0", "1"),
                ScriptTestHelper.output(
                        "int x = 1; int y = --x; TEST_printInt(x); TEST_printInt(y);", "0", "0"),
                ScriptTestHelper.output("double d = 1.5; d++; TEST_printDouble(d);", "2.5"),
                ScriptTestHelper.output("double d = 1.5; --d; TEST_printDouble(d);", "0.5"),
                ScriptTestHelper.output("char c = 'a'; c++; TEST_printChar(c);", "b"),
                ScriptTestHelper.output(
                        "char c = 'b'; char d = c--; TEST_printChar(c); TEST_printChar(d);",
                        "a",
                        "b"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void logic(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> logic() {
        List<Arguments> cases = new ArrayList<>();
        for (boolean a : new boolean[] {true, false}) {
            for (boolean b : new boolean[] {true, false}) {
                cases.add(
                        ScriptTestHelper.output(
                                "boolean a = "
                                        + a
                                        + "; boolean b = "
                                        + b
                                        + "; TEST_printBool(a && b); TEST_printBool(a || b);"
                                        + " TEST_printBool(!a);",
                                String.valueOf(a && b),
                                String.valueOf(a || b),
                                String.valueOf(!a)));
            }
        }
        cases.addAll(
                List.of(
                        ScriptTestHelper.output("TEST_printBool(!TEST_getBoolean());", "false"),
                        ScriptTestHelper.output("int a = 1; TEST_printBool(!(a < 2));", "false"),
                        ScriptTestHelper.output(
                                "int a = 5; if (a < 0 || a > 3) TEST_printString(\"y\");", "y"),
                        ScriptTestHelper.output(
                                "int a = 2; if (a > 0 && a < 3 && a != 1) TEST_printString(\"y\");",
                                "y"),
                        ScriptTestHelper.output("TEST_printBool(!!true);", "true"),
                        // the right side is only evaluated when it matters
                        ScriptTestHelper.output(
                                "boolean f = false; boolean r = f && TEST_sideEffect();"),
                        ScriptTestHelper.output(
                                "boolean t = true; boolean r = t || TEST_sideEffect();"),
                        ScriptTestHelper.output(
                                "boolean t = true; boolean r = t && TEST_sideEffect();",
                                "side effect"),
                        ScriptTestHelper.output(
                                "boolean f = false; boolean r = f || TEST_sideEffect();",
                                "side effect"),
                        ScriptTestHelper.output(
                                "Object o = TEST_getNull(); if (o != null && o.equals(o)) TEST_printString(\"x\");"),
                        // constant folding must not drop calls that would have been evaluated
                        ScriptTestHelper.output("boolean r = false && TEST_sideEffect();"),
                        ScriptTestHelper.output("boolean r = true || TEST_sideEffect();"),
                        ScriptTestHelper.output(
                                "boolean r = TEST_sideEffect() && false;", "side effect"),
                        ScriptTestHelper.output(
                                "boolean r = TEST_sideEffect() || true;", "side effect")));
        return cases.stream();
    }

    /** Loops, including empty bodies. */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void loops(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> loops() {
        return Stream.of(
                ScriptTestHelper.output(
                        "int i = 0; while (i < 3) { TEST_printInt(i); i++; }", "0", "1", "2"),
                ScriptTestHelper.output("int i = 10; while (i < 3) { TEST_printInt(i); }"),
                ScriptTestHelper.output(
                        "int i = 0; while (i < 5) { i++; if (i == 2) continue; if (i == 4) break; TEST_printInt(i); }",
                        "1",
                        "3"),
                ScriptTestHelper.output(
                        "int i = 10; do { TEST_printInt(i); } while (i < 5);", "10"),
                ScriptTestHelper.output(
                        "int i = 0; do { i++; if (i < 3) { continue; } } while (i < 5); TEST_printInt(i);",
                        "5"),
                ScriptTestHelper.output("int i = 0; do i++; while (i < 3); TEST_printInt(i);", "3"),
                ScriptTestHelper.output(
                        "for (int i = 0; i < 3; i++) { TEST_printInt(i); }", "0", "1", "2"),
                ScriptTestHelper.output(
                        "for (int i = 3; i > 0; --i) TEST_printInt(i);", "3", "2", "1"),
                ScriptTestHelper.output(
                        "int n = 0; for (int i = 0; i < 5; i++) { if (i % 2 == 0) continue; n += i; } TEST_printInt(n);",
                        "4"),
                ScriptTestHelper.output(
                        "int i; int j; for (i = 0, j = 10; i < j; i++, j--) { } TEST_printInt(i);",
                        "5"),
                ScriptTestHelper.output(
                        "for (int i = 0; i < 2; i++) { } for (int i = 5; i < 6; i++) { TEST_printInt(i); }",
                        "5"),
                // empty statements as bodies
                ScriptTestHelper.output(
                        "boolean b = true; if (b) ; else TEST_printString(\"else\"); TEST_printString(\"end\");",
                        "end"),
                ScriptTestHelper.output(
                        "boolean b = false; if (b) ; else TEST_printString(\"else\");", "else"),
                ScriptTestHelper.output("int i = 0; while (i++ < 3); TEST_printInt(i);", "4"),
                ScriptTestHelper.output("int i = 0; for (; i < 3; i++); TEST_printInt(i);", "3"),
                ScriptTestHelper.output("int i = 0; do ; while (i++ < 3); TEST_printInt(i);", "4"),
                ScriptTestHelper.output(";;; TEST_printString(\"ok\");", "ok"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void methodCalls(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> methodCalls() {
        return Stream.of(
                ScriptTestHelper.output(
                        """
                        TestObject o = TEST_getObject();
                        o.setString("abc");
                        TEST_printString(o.getString());
                        TEST_printInt(o.getString().length());
                        """,
                        "abc",
                        "3"),
                ScriptTestHelper.output(
                        "TEST_printString(TEST_getString().toUpperCase().substring(1, 3));", "AM"),
                ScriptTestHelper.output("TEST_printChar(TEST_getChar());", "a"),
                // strings are objects, so methods can be called on them like in Java
                ScriptTestHelper.output("TEST_printInt(\"abc\".length());", "3"),
                ScriptTestHelper.output("string s = \"abc\"; TEST_printInt(s.length());", "3"),
                ScriptTestHelper.output(
                        "TEST_printString(\"a\".concat(\"b\").toUpperCase());", "AB"),
                ScriptTestHelper.output("TEST_printInt((\"a\" + 12).length());", "3"),
                ScriptTestHelper.output("TEST_printBool(\"abc\".startsWith(\"ab\"));", "true"),
                ScriptTestHelper.output("TEST_printInt(\"abc\".indexOf('c'));", "2"),
                ScriptTestHelper.output(
                        "string s = \"x\"; if (s.equals(\"x\")) TEST_printString(\"eq\");", "eq"),
                ScriptTestHelper.output("\"ignored\".length(); TEST_printString(\"ok\");", "ok"),
                // parameters are passed in the correct order
                ScriptTestHelper.output("TEST_printString(\"abcdef\".substring(1, 4));", "bcd"),
                // widening primitive conversions are allowed for parameters
                ScriptTestHelper.output("TEST_printDouble(1);", "1.0"),
                ScriptTestHelper.output("TEST_printInt('a');", "97"),
                ScriptTestHelper.output(
                        "int a = 1; TEST_printBool(a < 2); TEST_printBool(a == 2);",
                        "true",
                        "false"),
                // null values can be returned and passed around
                ScriptTestHelper.output("Object o = TEST_getNull(); TEST_printObject(o);", "null"),
                ScriptTestHelper.output("TEST_printObject(TEST_getNull());", "null"),
                ScriptTestHelper.output("TEST_printObject(null);", "null"),
                ScriptTestHelper.output("string s = null; TEST_printObject(s);", "null"));
    }

    /** The values set by the method call test should all have made it through. */
    @Test
    void methodCallsPassValues() {
        ScriptTestHelper.runCleanly(
                """
                double d;
                String s;
                int i = TEST_getInt();
                s = TEST_getString();
                d = TEST_getDouble();
                TestObject o = TEST_getObject();
                o.setInteger(i);
                o.setString(s);
                o.setDoub(d);
                TEST_checkValues(s, i, d, o);
                """);
        Assertions.assertTrue(DebugMethods.isCheckValuesMatched());
    }

    /** The most specific overload is chosen, the same way Java does. */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void overloads(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> overloads() {
        return Stream.of(
                ScriptTestHelper.output("TEST_printString(TEST_overload(\"a\"));", "String"),
                ScriptTestHelper.output("TEST_printString(TEST_overload(1));", "int"),
                ScriptTestHelper.output("TEST_printString(TEST_overload('c'));", "int"),
                ScriptTestHelper.output("TEST_printString(TEST_overload(1.5));", "double"),
                ScriptTestHelper.output("TEST_printString(TEST_overload(null));", "String"),
                ScriptTestHelper.output("TEST_printString(TEST_overload(true));", "Object"),
                ScriptTestHelper.output(
                        "TEST_printString(TEST_overload(TEST_getObject()));", "Object"));
    }

    /**
     * Runtime errors should halt the script cleanly, and the script should report terminated.
     *
     * @param program The program, which should fail at runtime.
     */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @ValueSource(
            strings = {
                "int z = 0; int x = 1 / z;",
                "int z = 0; int x = 1 % z;",
                "int x = 1 / 0;",
                "Object o = TEST_getNull(); o.toString();",
                "string s = null; s.length();",
                "TEST_doesNotExist();",
                "TEST_printInt(\"wrong type\");",
                // Only static methods should be callable without an object
                "TEST_printInt(hashCode());",
                // Variables only hold values of the type they were declared with
                "int x = TEST_getDouble();",
                "int x; x = TEST_getString();",
                "string s = TEST_getObject();",
                "boolean b = TEST_getInt();",
            })
    void runtimeErrorsHalt(String program) {
        ScriptRuntime runtime = ScriptTestHelper.run(program + " TEST_printString(\"after\");");
        Assertions.assertTrue(runtime.isFatalError(), "Should halt");
        Assertions.assertTrue(runtime.hasTerminated(), "Should terminate");
        Assertions.assertTrue(
                DebugMethods.getOutput().isEmpty(), "Should not continue after halting");
    }

    /** Discarded results should not build up on the stack, even in long running loops. */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void stackDoesNotLeak(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> stackDoesNotLeak() {
        return Stream.of(
                ScriptTestHelper.output("for (int i = 0; i < 5; i++) { TEST_getInt(); }"),
                ScriptTestHelper.output(
                        "int x = 0; for (int i = 0; i < 5; i++) if (true) x++; TEST_printInt(x);",
                        "5"),
                ScriptTestHelper.output(
                        "int x = 0; while (x < 5) x++; do x--; while (x > 2); TEST_printInt(x);",
                        "2"),
                ScriptTestHelper.output(
                        "int x = 0; top: x++; if (x < 3) goto top; else x = x + 1; TEST_printInt(x);",
                        "4"),
                ScriptTestHelper.output(
                        "int x; for (x = 0; x < 3; x = x + 1) TEST_getString(); TEST_printInt(x);",
                        "3"),
                ScriptTestHelper.output(
                        "int x = 1; switch (x) { case 1: x++; TEST_getInt(); x += 2; } TEST_printInt(x);",
                        "4"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void strings(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> strings() {
        return Stream.of(
                ScriptTestHelper.output("TEST_printString(\"a\" + 1 + 2);", "a12"),
                ScriptTestHelper.output("TEST_printString(1 + 2 + \"a\");", "3a"),
                ScriptTestHelper.output("int i = 3; TEST_printString(i + \"x\");", "3x"),
                ScriptTestHelper.output("TEST_printString(\"x\" + 1.5 + 'c' + true);", "x1.5ctrue"),
                ScriptTestHelper.output("string s = \"\"; TEST_printString(s + s);", ""));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void switches(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> switches() {
        return Stream.of(
                ScriptTestHelper.output(String.format(TestInterpreter.SWITCH, 1), "one", "end"),
                ScriptTestHelper.output(
                        String.format(TestInterpreter.SWITCH, 2), "two or three", "four", "end"),
                ScriptTestHelper.output(
                        String.format(TestInterpreter.SWITCH, 3), "two or three", "four", "end"),
                ScriptTestHelper.output(String.format(TestInterpreter.SWITCH, 4), "four", "end"),
                ScriptTestHelper.output(String.format(TestInterpreter.SWITCH, 5), "default", "end"),
                // no match and no default runs nothing
                ScriptTestHelper.output(
                        "int x = 5; switch (x) { case 1: TEST_printString(\"one\"); break; case 2: TEST_printString(\"two\"); } TEST_printString(\"end\");",
                        "end"),
                // labels at the end with no statements
                ScriptTestHelper.output(
                        "int x = 5; switch (x) { case 1: TEST_printString(\"one\"); break; default: } TEST_printString(\"end\");",
                        "end"),
                ScriptTestHelper.output(
                        "int x = 2; switch (x) { default: TEST_printString(\"default\"); break; case 2: } TEST_printString(\"end\");",
                        "end"),
                // default does not need to be last
                ScriptTestHelper.output(
                        "int x = 9; switch (x) { default: TEST_printString(\"default\"); case 1: TEST_printString(\"one\"); }",
                        "default",
                        "one"),
                ScriptTestHelper.output(
                        "int x = 1; switch (x) { } TEST_printString(\"end\");", "end"),
                // other types
                ScriptTestHelper.output(
                        "string s = \"b\"; switch (s) { case \"a\": TEST_printString(\"A\"); break; case \"b\": TEST_printString(\"B\"); }",
                        "B"),
                ScriptTestHelper.output(
                        "char c = 'b'; switch (c) { case 'a': TEST_printString(\"A\"); break; case 'b': TEST_printString(\"B\"); }",
                        "B"),
                ScriptTestHelper.output(
                        "switch (TEST_getInt()) { case -4567: TEST_printString(\"match\"); }",
                        "match"),
                // nested switches each have their own break
                ScriptTestHelper.output(
                        """
                        int a = 1;
                        int b = 2;
                        switch (a) {
                            case 1:
                                switch (b) {
                                    case 2:
                                        TEST_printString("inner");
                                        break;
                                }
                                TEST_printString("after inner");
                                break;
                            case 2:
                                TEST_printString("wrong");
                        }
                        """,
                        "inner",
                        "after inner"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void ternary(String program, List<String> expected) {
        ScriptTestHelper.assertOutput(program, expected);
    }

    static Stream<Arguments> ternary() {
        return Stream.of(
                ScriptTestHelper.output(
                        "int a = 1; int b = 2; boolean c = false; int r = c ? a : b; TEST_printInt(r);",
                        "2"),
                ScriptTestHelper.output("int r = 3 > 2 ? 10 : 20; TEST_printInt(r);", "10"),
                ScriptTestHelper.output(
                        "int a = 5; TEST_printString(a > 3 ? \"big\" : \"small\");", "big"),
                ScriptTestHelper.output(
                        "int a = 1; double d = a > 3 ? 1.5 : a; TEST_printDouble(d);", "1.0"),
                ScriptTestHelper.output(
                        "int a = 1; boolean b = a > 0 ? a < 5 : false; TEST_printBool(b);", "true"),
                ScriptTestHelper.output(
                        "int a = 7; TEST_printString(a < 5 ? \"low\" : a < 10 ? \"mid\" : \"high\");",
                        "mid"));
    }

    /** Methods with the same signature in different classes are kept separate. */
    @Test
    void duplicateMethodsAcrossClasses() {
        final String program = "TEST_printString(TEST_duplicate());";
        ScriptManager.registerClass(DebugMethods.class);
        ScriptManager.registerClass(DuplicateMethods.class);
        try {
            ScriptRuntime runtime = ScriptTestHelper.run(program);
            Assertions.assertTrue(
                    runtime.isFatalError(), "Calls should be ambiguous with both registered");
        } finally {
            ScriptManager.unregisterClass(DuplicateMethods.class);
        }
        // Unregistering one class should not remove the other's method
        ScriptTestHelper.assertOutput(program, List.of("DebugMethods"));
    }
}
