package com.ikalagaming.scripting;

import com.ikalagaming.scripting.ScriptTestHelper.Validity;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Tests the validator, which rejects programs that are syntactically valid, but semantically
 * invalid. Each test is a list of programs and whether they should pass validation.
 *
 * @author Ches Burks
 */
class TestValidator {

    /** Operands that are never valid for arithmetic. */
    private static final List<String> NOT_NUMBERS = List.of("\"test\"", "null", "true");

    /**
     * Create test cases that should all pass validation.
     *
     * @param programs The programs.
     * @return The test arguments.
     */
    private static Stream<Arguments> valid(String... programs) {
        return Arrays.stream(programs).map(program -> Arguments.of(program, Validity.VALID));
    }

    /**
     * Create test cases that should all fail validation.
     *
     * @param programs The programs.
     * @return The test arguments.
     */
    private static Stream<Arguments> invalid(String... programs) {
        return Arrays.stream(programs).map(program -> Arguments.of(program, Validity.INVALID));
    }

    /**
     * Combine several streams of test cases into one.
     *
     * @param groups The test cases.
     * @return A stream of all of them.
     */
    @SafeVarargs
    private static Stream<Arguments> concat(Stream<Arguments>... groups) {
        return Arrays.stream(groups).flatMap(group -> group);
    }

    /**
     * Generate test cases for a unary operator applied to each operand.
     *
     * @param before The program up to the operand.
     * @param after The program after the operand.
     * @param valid Operands that are allowed.
     * @param invalid Operands that are not allowed.
     * @return The test arguments.
     */
    private static Stream<Arguments> unaryCases(
            String before, String after, List<String> valid, List<String> invalid) {
        return Stream.concat(
                valid.stream().map(v -> Arguments.of(before + v + after, Validity.VALID)),
                invalid.stream().map(v -> Arguments.of(before + v + after, Validity.INVALID)));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void arithmetic(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> arithmetic() {
        // The variable declarations, then the operands that are valid for that type
        final Map<String, List<String>> targets =
                Map.of(
                        "char a; char x = ",
                        List.of("'a'", "'x'", "TEST_getChar()", "a"),
                        "char a; int b; int x = ",
                        List.of("1", "-2", "'a'", "1 + 2", "TEST_getInt()", "a", "b"),
                        "char a; int b; double c; double x = ",
                        List.of(
                                "1",
                                "-2",
                                "'a'",
                                "4.1",
                                "-4.1",
                                "1 + 2",
                                "(9 % 5)",
                                "TEST_getDouble()",
                                "a",
                                "b",
                                "c"));

        List<Stream<Arguments>> cases = new ArrayList<>();
        for (var target : targets.entrySet()) {
            for (String operator : List.of("+", "-", "*", "/", "%")) {
                cases.add(
                        ScriptTestHelper.binaryOperatorCases(
                                target.getKey(),
                                operator,
                                target.getValue(),
                                TestValidator.NOT_NUMBERS));
            }
            for (String operator : List.of("-", "+")) {
                cases.add(
                        TestValidator.unaryCases(
                                target.getKey() + operator + "(",
                                ");",
                                target.getValue(),
                                TestValidator.NOT_NUMBERS));
            }
        }
        cases.add(
                TestValidator.concat(
                        invalid(
                                "string s = \"a\" * 2;",
                                "string s = \"a\" / 2;",
                                "string s = 2 * \"a\";",
                                "string s = \"a\" - \"b\";",
                                "string s = \"a\" % 2;",
                                "string s = -\"abc\";",
                                "int x = -true;",
                                "int x = true + 1;"),
                        valid(
                                "string s = \"a\" + 1;",
                                "string s = 1 + \"a\";",
                                "string s = \"a\" + true;",
                                "string s = \"a\" + 'c' + 1.5;")));
        return cases.stream().flatMap(stream -> stream);
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void assignmentTypes(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> assignmentTypes() {
        final List<String> values =
                List.of("true", "'c'", "1", "1.5", "\"s\"", "null", "obj", "other");
        // Which values each type accepts, like Java
        final Map<String, List<String>> accepted =
                Map.of(
                        "boolean", List.of("true"),
                        "char", List.of("'c'"),
                        "int", List.of("'c'", "1"),
                        "double", List.of("'c'", "1", "1.5"),
                        "string", List.of("\"s\"", "null"),
                        "Random", List.of("null", "obj"));
        final String objects = "Random obj; Another other; ";

        List<Arguments> cases = new ArrayList<>();
        for (var type : accepted.entrySet()) {
            for (String value : values) {
                final Validity validity =
                        type.getValue().contains(value) ? Validity.VALID : Validity.INVALID;
                final String name = type.getKey();
                cases.add(Arguments.of(objects + name + " x = " + value + ";", validity));
                cases.add(Arguments.of(objects + name + " x; x = " + value + ";", validity));
            }
            cases.add(Arguments.of(type.getKey() + " x;", Validity.VALID));
            cases.add(Arguments.of(type.getKey() + " x = x;", Validity.INVALID));
        }
        return cases.stream();
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void breakAndContinue(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> breakAndContinue() {
        return TestValidator.concat(
                invalid("break;", "continue;", "int i = 1; switch (i) { case 1: continue; }"),
                valid(
                        "for (;;) { break; }",
                        "for (; false;) { continue; }",
                        "while (true) { for (;;) { break; } break; }",
                        "while (false) { for (; false;) { continue; } continue; }",
                        """
                        int x = 55;
                        switch (x) {
                            case 45:
                                while (true) {
                                    for (;;) {
                                        break;
                                    }
                                    continue;
                                }
                                break;
                            default:
                                break;
                        }
                        """,
                        "int i = 1; switch (i) { case 1: break; case 2: default: i = 100; break; }",
                        """
                        for (int i = 0; i <= 10; ++i) {
                            switch (i) {
                                case 1:
                                    continue;
                                case 2:
                                default:
                                    i = 100;
                                    break;
                            }
                        }
                        """));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void compoundAssignment(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> compoundAssignment() {
        return TestValidator.concat(
                valid(
                        "int x = 1; x += 2; x -= 'a'; x *= TEST_getInt(); x /= 2; x %= 3;",
                        "double d = 1; d += 1.5; d -= 1; d *= 'c';",
                        "string s = \"a\"; s += 1; s += true; s += 'c'; s += 1.5; s += \"b\";"),
                invalid(
                        "string s = \"a\"; s -= \"b\";",
                        "string s = \"a\"; s *= 2;",
                        "boolean b = true; b += true;",
                        "Random r; r += 1;",
                        "int x = 1; x += \"a\";",
                        "int x = 1; x += 1.5;"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void conditions(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> conditions() {
        final String variables = "int x = 1; boolean b = true; string s = \"true\"; Random obj; ";
        final List<String> statements =
                List.of(
                        "if (COND) { }",
                        "if (COND) { } else if (COND) { } else { }",
                        "while (COND) { break; }",
                        "do { break; } while (COND);",
                        "for (; COND;) { break; }",
                        "int y = COND ? 1 : 2;");
        final List<String> booleans =
                List.of(
                        "true",
                        "false",
                        "b",
                        "TEST_getBoolean()",
                        "1 == 2",
                        "1 == x",
                        "x != x",
                        "3 < 2",
                        "x > 1",
                        "3 <= x",
                        "x <= x",
                        "x <= 3 || 3 > x",
                        "b && 1 > 3",
                        "!(x <= x || x > x)",
                        "true || x < 3 && x <= 5",
                        "(x < 4 ? false : true)");
        final List<String> notBooleans =
                List.of("1", "'y'", "\"true\"", "null", "1 + 2", "4.2", "43 % 1", "x", "s", "obj");

        List<Arguments> cases = new ArrayList<>();
        for (String statement : statements) {
            for (String condition : booleans) {
                cases.add(
                        Arguments.of(
                                variables + statement.replace("COND", condition), Validity.VALID));
            }
            for (String condition : notBooleans) {
                cases.add(
                        Arguments.of(
                                variables + statement.replace("COND", condition),
                                Validity.INVALID));
            }
        }
        // For loops are the only place a condition is optional
        cases.add(Arguments.of("for (;;) { break; }", Validity.VALID));
        return cases.stream();
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void equality(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> equality() {
        // Values can only be compared with values of a compatible type, like Java
        final List<List<String>> compatibleGroups =
                List.of(
                        List.of(
                                "true",
                                "(3 >= 6)",
                                "(4 == 4)",
                                "(3 < 1 || 3 >= 1)",
                                "(!(3 < 1) && 3 >= 1)"),
                        List.of("'c'", "4", "4.1", "-2"),
                        List.of("\"test\"", "null"));
        // We don't know the type until runtime, so these can be compared to anything
        final List<String> unknown = List.of("TEST_getBoolean()", "obj");

        List<Arguments> cases = new ArrayList<>();
        for (String operator : List.of("==", "!=")) {
            final String prefix = "Random obj; boolean x = ";
            for (int group = 0; group < compatibleGroups.size(); ++group) {
                for (String first : compatibleGroups.get(group)) {
                    for (String other : unknown) {
                        cases.add(
                                Arguments.of(
                                        prefix + first + " " + operator + " " + other + ";",
                                        Validity.VALID));
                    }
                    for (int otherGroup = 0; otherGroup < compatibleGroups.size(); ++otherGroup) {
                        for (String second : compatibleGroups.get(otherGroup)) {
                            cases.add(
                                    Arguments.of(
                                            prefix + first + " " + operator + " " + second + ";",
                                            group == otherGroup
                                                    ? Validity.VALID
                                                    : Validity.INVALID));
                        }
                    }
                }
            }
        }
        return cases.stream();
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void finalVariables(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> finalVariables() {
        return TestValidator.concat(
                valid(
                        "final int x = 1; int y = x + 1;",
                        "final int x = 1; int y = 2; y = 3;",
                        "{ final int x = 1; } { int x = 2; x = 3; }",
                        "final string s = \"a\"; string t = s + \"b\";",
                        "for (final int i = 0; i < 3;) { break; }"),
                invalid(
                        "final int x = 1; x = 2;",
                        "final int x = 1; x++;",
                        "final int x = 1; --x;",
                        "final int x = 1; x += 1;",
                        // Every declaration initializes, so there are no blank finals
                        "final int x; x = 1;",
                        "final int a = 1, b = 2; b = 3;",
                        "final int x = 1; { x = 2; }",
                        "for (final int i = 0; i < 3; i++) { }"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void gotoAndLabels(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> gotoAndLabels() {
        return TestValidator.concat(
                valid(
                        "END:",
                        "goto END; END:",
                        "int x; before: x++;",
                        "int x; goto before; before: x++;",
                        """
                        goto afterFor;
                        for (;;) {
                            inside_for:
                            goto End;
                        }
                        afterFor:
                        goto inside_for;
                        End:
                        """),
                invalid(
                        "goto nonexistent;",
                        "duplicated: int x; duplicated: x++;",
                        "int x = 1; goto x;",
                        // Labels and variables share names
                        "x: int x = 1;",
                        "int x = 1; x: ;",
                        "x: ; x = 1;"));
    }

    /**
     * A goto can't jump past a declaration into the scope of that variable, the same rule as C++.
     * Every declaration initializes its variable, even if just to a default, so unlike C++ there
     * are no declarations that are safe to skip.
     */
    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void gotoScopes(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> gotoScopes() {
        return TestValidator.concat(
                invalid(
                        "goto L; int x = 1; L: ;",
                        "goto L; int x; L: ;",
                        "goto L; int a = 1, b = 2; L: ;",
                        "goto in; for (int i = 0; i < 3; i++) { in: ; }",
                        "goto in; { int x = 1; in: ; }",
                        "goto in; { int a = 1; { in: ; } }",
                        "{ int x = 1; in: ; } goto in;",
                        "for (int i = 0; i < 1; i++) { goto L; int y = 1; L: ; }",
                        "int z = 1; switch (z) { case 1: goto L; int y = 1; L: ; }",
                        "goto L; int x = 1; L: x++;"),
                valid(
                        "int x = 1; goto L; L: x++;",
                        "goto L; L: int x = 1;",
                        "top: int x = 1; goto top;",
                        "goto in; while (false) { in: ; }",
                        "goto in; for (;;) { in: break; }",
                        "for (int i = 0; i < 3; i++) { if (i == 1) goto out; } out: ;",
                        "{ int x = 1; } goto L; { L: ; }",
                        "int x = 1; { int y = 2; goto L; } L: x++;",
                        "for (int i = 0; i < 3; i++) { int y = i; goto next; next: ; }",
                        "goto L; { int x = 1; } L: ;",
                        "int x = 1; switch (x) { case 1: int y = 2; goto L; case 2: L: ; }"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void incrementDecrement(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> incrementDecrement() {
        final List<String> notVariables =
                List.of(
                        "1",
                        "-2",
                        "'a'",
                        "4.1",
                        "1 + 2",
                        "TEST_getInt()",
                        "\"test\"",
                        "null",
                        "true",
                        "x++",
                        "++x");
        final Map<String, List<String>> targets =
                Map.of(
                        "char a; char x = 'a'; char y = ", List.of("a"),
                        "char a; int b; int x = 1; int y = ", List.of("a", "b"),
                        "char a; int b; double c; int x = 1; double y = ", List.of("a", "b", "c"));

        List<Stream<Arguments>> cases = new ArrayList<>();
        for (var target : targets.entrySet()) {
            for (String operator : List.of("--", "++")) {
                cases.add(
                        TestValidator.unaryCases(
                                target.getKey() + operator + "(",
                                ");",
                                target.getValue(),
                                notVariables));
                cases.add(
                        TestValidator.unaryCases(
                                target.getKey() + "(",
                                ")" + operator + ";",
                                target.getValue(),
                                notVariables));
            }
        }
        cases.add(invalid("boolean b = true; b++;", "string s = \"a\"; s--;"));
        return cases.stream().flatMap(stream -> stream);
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void logic(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> logic() {
        final List<String> booleans =
                List.of(
                        "true",
                        "false",
                        "1 < 2",
                        "(3 >= 6)",
                        "0 != 3",
                        "4 == 4",
                        "(3 < 1 || 3 >= 1)",
                        "(!(3 < 1) && 3 >= 1)",
                        "TEST_getBoolean()");
        final List<String> notBooleans = List.of("'c'", "4", "4.1", "\"test\"", "null");
        return TestValidator.concat(
                ScriptTestHelper.binaryOperatorCases("boolean x = ", "||", booleans, notBooleans),
                ScriptTestHelper.binaryOperatorCases("boolean x = ", "&&", booleans, notBooleans),
                // Parentheses are required, since ! binds tighter than relational operators
                TestValidator.unaryCases("boolean x = !(", ");", booleans, notBooleans),
                invalid("boolean x = !1 < 2;", "int x = 1; boolean b = !x;"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void methodCalls(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> methodCalls() {
        return TestValidator.concat(
                valid(
                        "TestObject object = TEST_getObject(); object.getSelf().getSelf();",
                        """
                        Object x = TEST_getObject(), y = TEST_getObject();
                        int z = 3;
                        (z > 2 ? x : y).getSelf();
                        """,
                        // Strings are objects in Java, unlike other primitives
                        "string x = \"test\"; x.toString();",
                        "int x = \"test\".length();",
                        "int i = \"a\".concat(\"b\").length();",
                        "int i = (\"a\" + 1).length();"),
                invalid(
                        "char x = 'e'; x.toString();",
                        "int x = 1; x.toString();",
                        "double x = 3.12; x.toString();",
                        "boolean x = true; x.toString();",
                        "true.toString();",
                        "'c'.toString();",
                        "int y = ++TEST_getInt();"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void relational(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> relational() {
        final List<String> numbers =
                List.of("1", "-2", "'a'", "4.1", "-4.1", "1 + 2", "(9 % 5)", "TEST_getInt()");
        return Stream.of("<", "<=", ">", ">=")
                .flatMap(
                        operator ->
                                ScriptTestHelper.binaryOperatorCases(
                                        "boolean x = ",
                                        operator,
                                        numbers,
                                        TestValidator.NOT_NUMBERS));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void statements(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> statements() {
        return valid(
                ";",
                "{} {{{{{}}}}} { {} {{{}{}}{{{}}}} }",
                ";{ ; } ; { ;;; {;} ;;; ;}",
                "if (true) ; else ;",
                "while (false);",
                "for (;;);",
                "do ; while (false);",
                "label: ;",
                "int x = 4; if (x >= 4) { }",
                "int x = 4; if (x >= 4) { } else { }",
                "int x = 4; if (x == 4) { } else if (x < 4) { }",
                "int x = 4; if (x >= 4) { } else if (x < 4) { } else { }");
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void switches(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> switches() {
        return TestValidator.concat(
                valid(
                        "int x = 4; switch (x) { case 1: break; case 2: case 3: break; }",
                        "int x = 4; switch (x) { case 1: break; case 2: default: break; }",
                        "int x = 4; switch (x) { }",
                        "int x = 4; switch (x) { case 1: case -1: case 2: }",
                        "switch (TEST_getInt()) { case 1: break; }",
                        "string s = \"a\"; switch (s) { case \"a\": case \"b\": }"),
                invalid(
                        "int x = 4; switch (x) { case 1: default: break; case 2: default: }",
                        "int x = 4; switch (x) { case 1: break; case 1: break; }",
                        "int x = 4; switch (x) { case -1: case 2: case -1: }",
                        "char c = 'a'; switch (c) { case 'a': case 'a': }",
                        "string s = \"a\"; switch (s) { case \"a\": case \"a\": }",
                        "int x = 4; switch (x) { case \"a\": }"));
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void ternaryTypes(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> ternaryTypes() {
        // The type, values that work for it, and values that don't
        final List<List<List<String>>> types =
                List.of(
                        List.of(
                                List.of("boolean"),
                                List.of("true", "TEST_getBoolean()"),
                                List.of("'c'", "4", "4.1", "\"test\"", "null")),
                        List.of(
                                List.of("char"),
                                List.of("'c'", "TEST_getChar()"),
                                List.of("true", "4", "4.1", "\"test\"", "null")),
                        List.of(
                                List.of("int"),
                                List.of("'b'", "1", "TEST_getInt()"),
                                List.of("true", "4.1", "\"test\"", "null")),
                        List.of(
                                List.of("double"),
                                List.of("'b'", "1", "3.0", "TEST_getDouble()"),
                                List.of("true", "\"test\"", "null")),
                        List.of(
                                List.of("string"),
                                List.of("\"test\"", "null", "TEST_getString()"),
                                List.of("true", "'b'", "1", "3.0")));

        List<Arguments> cases = new ArrayList<>();
        for (List<List<String>> type : types) {
            final String prefix = type.get(0).get(0) + " x = true ? ";
            List<String> all = new ArrayList<>(type.get(1));
            all.addAll(type.get(2));
            for (String left : all) {
                for (String right : all) {
                    final boolean nullAndUnknown =
                            ("null".equals(left) && right.endsWith("()"))
                                    || (left.endsWith("()") && "null".equals(right));
                    if (nullAndUnknown || ("null".equals(left) && "null".equals(right))) {
                        // Can't tell what type these are, so they're not useful to check
                        continue;
                    }
                    final boolean bothValid =
                            type.get(1).contains(left) && type.get(1).contains(right);
                    cases.add(
                            Arguments.of(
                                    prefix + left + " : " + right + ";",
                                    bothValid ? Validity.VALID : Validity.INVALID));
                }
            }
        }
        cases.addAll(
                TestValidator.concat(
                                valid(
                                        "boolean cond = true; int x = cond ? 1 : 4;",
                                        "double x = 4.45; boolean y = x <= 500 ? true : false;",
                                        "int x = 45; double y = x != 46 ? 4.1 : 2.1;",
                                        "int x = 5; int y = (x < 4 ? false : true) ? 5 : 6;"),
                                invalid("int x = true ? 1 : \"a\";"))
                        .toList());
        return cases.stream();
    }

    @ParameterizedTest(name = ScriptTestHelper.NAME)
    @MethodSource
    void variableNames(String program, Validity validity) {
        ScriptTestHelper.assertValidity(program, validity);
    }

    static Stream<Arguments> variableNames() {
        return TestValidator.concat(
                valid(
                        "int a = 1, b = a;",
                        "{ int x = 1; } { int x = 2; }",
                        "for (int i = 0; i < 1; i++) { } for (int i = 0; i < 1; i++) { }",
                        "Random obj1 = null; Random obj2 = obj1;"),
                invalid(
                        "int x = 1; int x = 2;",
                        "int x = 1; string x = \"a\";",
                        "int x, x;",
                        "int x = 1; { int x = 2; }",
                        "for (int i = 0; i < 1; i++) { int i = 2; }",
                        "x = 1; int x;",
                        "int y = x; int x = 1;",
                        "{ int x = 1; } x = 2;",
                        "Random obj1; Another obj2 = obj1;"));
    }
}
