package com.ikalagaming.scripting;

import com.ikalagaming.scripting.IkalaScriptParser.CompilationUnitContext;
import com.ikalagaming.scripting.ast.AbstractSyntaxTree;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.visitors.TreeValidator;
import com.ikalagaming.scripting.ast.visitors.TypePreprocessor;

import lombok.NonNull;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.TokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests the functionality of the validator. This is testing syntactically
 * valid, but semantically invalid programs.
 *
 * @author Ches Burks
 *
 */
class TestValidator {
	private static IkalaScriptLexer lexer;
	private static IkalaScriptParser parser;
	private static ParserErrorListener errorListener;
	private static TypePreprocessor processor = new TypePreprocessor();
	private static TreeValidator validator = new TreeValidator();

	/**
	 * Sets up the lexer and parser with a dummy string.
	 */
	@BeforeAll
	static void beforeAll() {
		CharStream stream = CharStreams.fromString("");
		TestValidator.errorListener = new ParserErrorListener();

		TestValidator.lexer = new IkalaScriptLexer(stream);
		TestValidator.lexer.removeErrorListeners();
		TestValidator.lexer.addErrorListener(TestValidator.errorListener);
		TokenStream tokenStream = new BufferedTokenStream(TestValidator.lexer);
		TestValidator.parser = new IkalaScriptParser(tokenStream);
		TestValidator.parser.removeErrorListeners();
		TestValidator.parser.addErrorListener(TestValidator.errorListener);
	}

	/**
	 * Validates that we can place blocks arbitrarily.
	 */
	@Test
	void testBlocks() {
		final String blockMess = """
			{}
			{{{{{}}}}}
			{
			  {}
			  {{{}{}}{{{}}}}
			}
			""";
		Assertions.assertTrue(this.validateProgram(blockMess),
			"We should be able to have loose blocks");
	}

	/**
	 * Check boolean assignments.
	 */
	@Test
	void testBooleanAssignment() {
		final String trueCase = """
			boolean x;
			x = true;
			""";
		Assertions.assertTrue(this.validateProgram(trueCase),
			"Assignment to boolean using true should work");

		final String falseCase = """
			boolean x;
			x = false;
			""";
		Assertions.assertTrue(this.validateProgram(falseCase),
			"Assignment to boolean using false should work");

		final String otherBoolean = """
			boolean x = true;
			boolean y;
			y = x;
			""";
		Assertions.assertTrue(this.validateProgram(otherBoolean),
			"Assignment to boolean using another boolean should work");

		final String implicitString = """
			boolean x;
			x = "true";
			""";
		Assertions.assertFalse(this.validateProgram(implicitString),
			"Assignment to boolean using string should not work");

		final String implicitChar = """
			boolean x;
			x = 'f';
			""";
		Assertions.assertFalse(this.validateProgram(implicitChar),
			"Assignment to boolean using char should not work");

		final String implicitInt = """
			boolean x;
			x = 0;
			""";
		Assertions.assertFalse(this.validateProgram(implicitInt),
			"Assignment to boolean using int should not work");

		final String implicitDouble = """
			boolean x;
			x = 1.0;
			""";
		Assertions.assertFalse(this.validateProgram(implicitDouble),
			"Assignment to boolean using double should not work");

		final String implicitObject = """
			Random obj;
			boolean x;
			x = obj;
			""";
		Assertions.assertFalse(this.validateProgram(implicitObject),
			"Assignment to boolean using object should not work");

		final String implicitNull = """
			boolean x;
			x = null;
			""";
		Assertions.assertFalse(this.validateProgram(implicitNull),
			"Assignment to boolean using null should not work");
	}

	/**
	 * Check boolean declarations.
	 */
	@Test
	void testBooleanDeclaration() {
		final String minimum = "boolean x;";
		Assertions.assertTrue(this.validateProgram(minimum),
			"Construction of plain boolean declaration should work");

		final String trueCase = "boolean x = true;";
		Assertions.assertTrue(this.validateProgram(trueCase),
			"Construction of boolean using true should work");

		final String falseCase = "boolean x = false;";
		Assertions.assertTrue(this.validateProgram(falseCase),
			"Construction of boolean using false should work");

		final String otherBoolean = """
			boolean x = false;
			boolean y = x;
			""";
		Assertions.assertTrue(this.validateProgram(otherBoolean),
			"Construction of boolean using another boolean should work");

		final String implicitString = "boolean x = \"test\";";
		Assertions.assertFalse(this.validateProgram(implicitString),
			"Construction of boolean using string should not work");

		final String implicitChar = "boolean x = 'a';";
		Assertions.assertFalse(this.validateProgram(implicitChar),
			"Construction of boolean using char should not work");

		final String implicitInt = "boolean x = 1;";
		Assertions.assertFalse(this.validateProgram(implicitInt),
			"Construction of boolean using int should not work");

		final String implicitDouble = "boolean x = 1.0;";
		Assertions.assertFalse(this.validateProgram(implicitDouble),
			"Construction of boolean using double should not work");

		final String implicitNull = "boolean x = null;";
		Assertions.assertFalse(this.validateProgram(implicitNull),
			"Construction of boolean using null should not work");

		final String implicitObject = """
			Random obj;
			boolean x = obj;
			""";
		Assertions.assertFalse(this.validateProgram(implicitObject),
			"Construction of boolean using object should not work");

		final String recursive = "boolean x = x;";
		Assertions.assertFalse(this.validateProgram(recursive),
			"Initialization by self-reference should not work");
	}

	/**
	 * Test break statements.
	 */
	@Test
	void testBreak() {
		final String outsideLoop = "break;";
		Assertions.assertFalse(this.validateProgram(outsideLoop),
			"We should not be able to break outside a loop");

		final String loop = """
			for(;;) {
				break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(loop),
			"We should be able to break from a loop");

		final String loopNested = """
			while (true) {
				for(;;) {
					break;
				}
				break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(loopNested),
			"We should be able to break in a nested loop");

		final String loopNestedInSwitch = """
			int x = 55;
			switch (x) {
				case 45:
					while (true) {
						for(;;) {
							break;
						}
						break;
					}
					break;
				default:
					break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(loopNestedInSwitch),
			"We should be able to break in a loop in a switch");

		final String switchNormal = """
			int i = 1;
			switch (i) {
				case 1:
					break;
				case 2:
				default:
					i = 100;
					break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(switchNormal),
			"We should be able to break in a switch");

		final String switchInLoop = """
			for (int i = 0; i <= 10; ++i) {
				switch (i) {
					case 1:
						break;
					case 2:
					default:
						i = 100;
						break;
				}
			}
			""";
		Assertions.assertTrue(this.validateProgram(switchInLoop),
			"We should be able to break in a switch in a loop");
	}

	/**
	 * Check character assignments.
	 */
	@Test
	void testCharAssignment() {
		final String letter = """
			char x;
			x = 'a';
			""";
		Assertions.assertTrue(this.validateProgram(letter),
			"Assignment to char using char should work");

		final String otherChar = """
			char x = 'x';
			char y;
			y = x;
			""";
		Assertions.assertTrue(this.validateProgram(otherChar),
			"Assignment to char using char using another char should work");

		final String implicitString = """
			char x;
			x = "test";
			""";
		Assertions.assertFalse(this.validateProgram(implicitString),
			"Assignment to char using string should not work");

		final String implicitInt = """
			char x;
			x = 5;
			""";
		Assertions.assertFalse(this.validateProgram(implicitInt),
			"Assignment to char using integer should not work");

		final String implicitDouble = """
			char x;
			x = 3.14;
			""";
		Assertions.assertFalse(this.validateProgram(implicitDouble),
			"Assignment to char using double should not work");

		final String implicitNull = """
			char x;
			x = null;
			""";
		Assertions.assertFalse(this.validateProgram(implicitNull),
			"Assignment to char using null should not work");

		final String implicitObject = """
			Random obj;
			char x;
			x = obj;
			""";
		Assertions.assertFalse(this.validateProgram(implicitObject),
			"Assignment to char using object should not work");
	}

	/**
	 * Check character declarations.
	 */
	@Test
	void testCharDeclaration() {
		final String minimum = "char x;";
		Assertions.assertTrue(this.validateProgram(minimum),
			"Construction of plain char declaration should work");

		final String letter = "char x = 'a';";
		Assertions.assertTrue(this.validateProgram(letter),
			"Construction of char using char should work");

		final String implicitString = "char x = \"test\";";
		Assertions.assertFalse(this.validateProgram(implicitString),
			"Construction of char using string should not work");

		final String implicitInt = "char x = 1;";
		Assertions.assertFalse(this.validateProgram(implicitInt),
			"Construction of char using integer should not work");

		final String implicitDouble = "char x = 3.0;";
		Assertions.assertFalse(this.validateProgram(implicitDouble),
			"Construction of char using double should not work");

		final String implicitNull = "char x = null;";
		Assertions.assertFalse(this.validateProgram(implicitNull),
			"Construction of char using null should not work");

		final String implicitObject = """
			Random obj;
			char x = obj;
			""";
		Assertions.assertFalse(this.validateProgram(implicitObject),
			"Construction of char using object should not work");

		final String recursive = "char x = x;";
		Assertions.assertFalse(this.validateProgram(recursive),
			"Initialization by self-reference should not work");
	}

	/**
	 * Test continue statements.
	 */
	@Test
	void testContinue() {
		final String outsideLoop = "continue;";
		Assertions.assertFalse(this.validateProgram(outsideLoop),
			"We should not be able to continue outside a loop");

		final String loop = """
			for(;false;) {
				continue;
			}
			""";
		Assertions.assertTrue(this.validateProgram(loop),
			"We should be able to continue from a loop");

		final String loopNested = """
			while (false) {
				for(;false;) {
					continue;
				}
				continue;
			}
			""";
		Assertions.assertTrue(this.validateProgram(loopNested),
			"We should be able to continue in a nested loop");

		final String loopNestedInSwitch = """
			int x = 55;
			switch (x) {
				case 45:
					while (false) {
						for(;false;) {
							continue;
						}
						continue;
					}
					break;
				default:
					break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(loopNestedInSwitch),
			"We should be able to continue in a loop in a switch");

		final String switchNormal = """
			int i = 1;
			switch (i) {
				case 1:
					continue;
				case 2:
				default:
					i = 100;
					break;
			}
			""";
		Assertions.assertFalse(this.validateProgram(switchNormal),
			"We should not be able to continue in a switch");

		final String switchInLoop = """
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
			""";
		Assertions.assertTrue(this.validateProgram(switchInLoop),
			"We should be able to continue in a switch in a loop");
	}

	/**
	 * Check double assignments.
	 */
	@Test
	void testDoubleAssignment() {
		final String implicitChar = """
			double x;
			x = 'b';
			""";
		Assertions.assertTrue(this.validateProgram(implicitChar),
			"Assignment to double using char should work");

		final String implicitInt = """
			double x;
			x = 4;
			""";
		Assertions.assertTrue(this.validateProgram(implicitInt),
			"Assignment to double using int should work");

		final String number = """
			double x;
			x = 7.340;
			""";
		Assertions.assertTrue(this.validateProgram(number),
			"Assignment to double using double should work");

		final String implicitString = """
			double x;
			x = "4.21";
			""";
		Assertions.assertFalse(this.validateProgram(implicitString),
			"Assignment to double using string should not work");

		final String implicitNull = """
			double x;
			x = null;
			""";
		Assertions.assertFalse(this.validateProgram(implicitNull),
			"Assignment to double using null should not work");

		final String implicitObject = """
			Random obj;
			double x;
			x = obj;
			""";
		Assertions.assertFalse(this.validateProgram(implicitObject),
			"Assignment to double using object should not work");
	}

	/**
	 * Check double declarations.
	 */
	@Test
	void testDoubleDeclaration() {
		final String minimum = "double x;";
		Assertions.assertTrue(this.validateProgram(minimum),
			"Construction of plain double declaration should work");

		final String implicitChar = "double x = 'a';";
		Assertions.assertTrue(this.validateProgram(implicitChar),
			"Construction of double using char should work");

		final String implicitInt = "double x = 1;";
		Assertions.assertTrue(this.validateProgram(implicitInt),
			"Construction of double using int should work");

		final String number = "double x = 1.34;";
		Assertions.assertTrue(this.validateProgram(number),
			"Construction of double using double should work");

		final String implicitString = "double x = \"test\";";
		Assertions.assertFalse(this.validateProgram(implicitString),
			"Construction of double using string should not work");

		final String implicitNull = "double x = null;";
		Assertions.assertFalse(this.validateProgram(implicitNull),
			"Construction of double using null should not work");

		final String implicitObject = """
			Random obj;
			double x = obj;
			""";
		Assertions.assertFalse(this.validateProgram(implicitObject),
			"Construction of double using object should not work");

		final String recursive = "double x = x;";
		Assertions.assertFalse(this.validateProgram(recursive),
			"Initialization by self-reference should not work");
	}

	/**
	 * Test the while statement for equality expressions.
	 */
	@Test
	void testDoWhileEquality() {
		final String equalityExpression1 = """
			do {
				break;
			}
			while(1 == 2);
			""";
		Assertions.assertTrue(this.validateProgram(equalityExpression1),
			"Do while should work with an equality expression");

		final String equalityExpression2 = """
			int x = 1;
			do {
				break;
			}
			while(1 == x);
			""";
		Assertions.assertTrue(this.validateProgram(equalityExpression2),
			"Do while should work with an equality expression");

		final String equalityExpression3 = """
			int x = 1;
			do {
				break;
			}
			while(x != 1);
			""";
		Assertions.assertTrue(this.validateProgram(equalityExpression3),
			"Do while should work with an equality expression");

		final String equalityExpression4 = """
			int x = 1;
			do {
				break;
			}
			while(x != x);
			""";
		Assertions.assertTrue(this.validateProgram(equalityExpression4),
			"Do while should work with an equality expression");
	}

	/**
	 * Test the while statement with logical expressions.
	 */
	@Test
	void testDoWhileLogical() {
		final String logicalExpression1 = """
			int x = 1;
			do {
				break;
			}
			while(x <= 3 || 3 > x);
			""";
		Assertions.assertTrue(this.validateProgram(logicalExpression1),
			"Do while should work with a logical expression");

		final String logicalExpression2 = """
			boolean x = true;
			do {
				break;
			}
			while(x && 1 > 3);
			""";
		Assertions.assertTrue(this.validateProgram(logicalExpression2),
			"Do while should work with a logical expression");

		final String logicalExpression3 = """
			int x = 1;
			do {
				break;
			}
			while(!(x <= x || x > x));
			""";
		Assertions.assertTrue(this.validateProgram(logicalExpression3),
			"Do while should work with a logical expression");
	}

	/**
	 * Test the while statement with negative cases.
	 */
	@Test
	void testDoWhileNegative() {
		final String integer = """
			do {
				break;
			}
			while(1);
			""";
		Assertions.assertFalse(this.validateProgram(integer),
			"Do while should not work with an integer");

		final String character = """
			do {
				break;
			}
			while('y');
			""";
		Assertions.assertFalse(this.validateProgram(character),
			"Do while should not work with a character");

		final String string = """
			do {
				break;
			}
			while("true");
			""";
		Assertions.assertFalse(this.validateProgram(string),
			"Do while should not work with a string");

		final String nullValue = """
			do {
				break;
			}
			while(null);
			""";
		Assertions.assertFalse(this.validateProgram(nullValue),
			"Do while should not work with a null");

		final String numericExpression = """
			do {
				break;
			}
			while(1 + 2);
			""";
		Assertions.assertFalse(this.validateProgram(numericExpression),
			"Do while should not work with a numeric expression");
	}

	/**
	 * Test the while statement with relational expressions.
	 */
	@Test
	void testDoWhileRelation() {
		final String relationExpression1 = """
			do {
				break;
			}
			while(3 < 2);
			""";
		Assertions.assertTrue(this.validateProgram(relationExpression1),
			"Do while should work with an relational expression");

		final String relationExpression2 = """
			int x = 1;
			do {
				break;
			}
			while(x > 1);
			""";
		Assertions.assertTrue(this.validateProgram(relationExpression2),
			"Do while should work with an relational expression");

		final String relationExpression3 = """
			int x = 1;
			do {
				break;
			}
			while(3 <= x);
			""";
		Assertions.assertTrue(this.validateProgram(relationExpression3),
			"Do while should work with an relational expression");

		final String relationExpression4 = """
			int x = 1;
			do {
				break;
			}
			while(x <= x);
			""";
		Assertions.assertTrue(this.validateProgram(relationExpression4),
			"Do while should work with an relational expression");
	}

	/**
	 * Check for empty statements.
	 */
	@Test
	void testEmptyStatement() {
		final String single = ";";
		Assertions.assertTrue(this.validateProgram(single),
			"We should allow empty statements");

		final String several = """
			;{
			  ;
			}
			;
			{
			  ;;;
			  {;}
			  ;;;
			;}
			""";
		Assertions.assertTrue(this.validateProgram(several),
			"We should allow empty statements in arbitrary places");
	}

	/**
	 * Test the for statement only works with boolean conditionals.
	 */
	@Test
	void testForExpressions() {
		final String[] negativeCases =
			{"1", "'c'", "\"true\"", "4.2", "null", "43 % 1"};

		for (String negativeCase : negativeCases) {
			final String equalityExpression1 =
				String.format("for (;%s;){break;}", negativeCase);
			Assertions.assertFalse(this.validateProgram(equalityExpression1),
				String.format(
					"For conditional should only accept a boolean, but accepted %s",
					negativeCase));
		}
		final String[] positiveCases =
			{"true", "false", "1  >= 3", "true || x < 3 && x <= 5", ""};

		for (String positiveCase : positiveCases) {
			final String equalityExpression1 =
				String.format("int x = 4; for (;%s;){break;}", positiveCase);
			Assertions.assertTrue(this.validateProgram(equalityExpression1),
				String.format(
					"For conditional should accept a boolean, but did not accept %s",
					positiveCase));
		}
	}

	/**
	 * Test the goto label logic.
	 */
	@Test
	void testGoto() {
		final String endLabel = """
			goto END;
			END:
			""";
		Assertions.assertTrue(this.validateProgram(endLabel),
			"We should be able to jump to a label at the end of the program");

		final String labeledStatement = """
			goto before;
			int x;
			before:
			x++;
			""";
		Assertions.assertTrue(this.validateProgram(labeledStatement),
			"We should be able to have a label before a statement");

		final String moreComplex = """
			// Vary up case a bit
			goto afterFor;

			for (;;) {
			  inside_for:
			  goto End;
			}

			afterFor:
			goto inside_for;

			End:
			""";
		Assertions.assertTrue(this.validateProgram(moreComplex),
			"We should allow more complex jumps");

		final String nonexistent = """
			goto nonexistent;
			""";
		Assertions.assertFalse(this.validateProgram(nonexistent),
			"We should not be able to goto a label that does not exist");
	}

	/**
	 * Test if statements.
	 */
	@Test
	void testIfStatement() {
		final String standard = """
			int x = 4;
			if (x >= 4) {

			}
			""";
		Assertions.assertTrue(this.validateProgram(standard),
			"If statements should work");

		final String oneElse = """
			int x = 4;
			if (x >= 4) {

			} else {

			}
			""";
		Assertions.assertTrue(this.validateProgram(oneElse),
			"If statements with one else should work");

		final String elseIf = """
			int x = 4;
			if (x == 4) {

			} else if (x < 4) {

			}
			""";
		Assertions.assertTrue(this.validateProgram(elseIf),
			"If statements with an else if should work");

		final String elseIfElse = """
			int x = 4;
			if (x >= 4) {

			} else if (x < 4) {

			} else {

			}
			""";
		Assertions.assertTrue(this.validateProgram(elseIfElse),
			"If statements with an else if and else should work");

		final String withBooleanVar = """
			boolean x = true;
			if (x) {

			} else {

			}
			""";
		Assertions.assertTrue(this.validateProgram(withBooleanVar),
			"If statements with with a boolean variable should work");

		final String notBoolean = """
			int x = 4;
			if (x) {

			} else if (x < 4) {

			} else {

			}
			""";
		Assertions.assertFalse(this.validateProgram(notBoolean),
			"If statements with without a boolean should not work");

		// extraneous else clauses is caught by the parser itself
	}

	/**
	 * Check integer assignments.
	 */
	@Test
	void testIntAssignment() {
		final String implicitChar = """
			int x;
			x = 'f';
			""";
		Assertions.assertTrue(this.validateProgram(implicitChar),
			"Assignment to int using char should work");

		final String number = """
			int x;
			x = 4;
			""";
		Assertions.assertTrue(this.validateProgram(number),
			"Assignment to int using int should work");

		final String implicitDouble = """
			int x;
			x = 6.6;
			""";
		Assertions.assertFalse(this.validateProgram(implicitDouble),
			"Assignment to int using double should not work");

		final String implicitString = """
			int x;
			x = "456";
			""";
		Assertions.assertFalse(this.validateProgram(implicitString),
			"Assignment to int using string should not work");

		final String implicitNull = """
			int x;
			x = null;
			""";
		Assertions.assertFalse(this.validateProgram(implicitNull),
			"Assignment to int using null should not work");

		final String implicitObject = """
			Random obj;
			int x;
			x = obj;
			""";
		Assertions.assertFalse(this.validateProgram(implicitObject),
			"Assignment to int using object should not work");
	}

	/**
	 * Check integer declarations.
	 */
	@Test
	void testIntDeclaration() {
		final String minimum = "int x;";
		Assertions.assertTrue(this.validateProgram(minimum),
			"Construction of plain int declaration should work");

		final String implicitChar = "int x = 'a';";
		Assertions.assertTrue(this.validateProgram(implicitChar),
			"Construction of int using char should work");

		final String number = "int x = 1;";
		Assertions.assertTrue(this.validateProgram(number),
			"Construction of int using int should work");

		final String implicitDouble = "int x = 1.0;";
		Assertions.assertFalse(this.validateProgram(implicitDouble),
			"Construction of int using double should not work");

		final String implicitString = "int x = \"test\";";
		Assertions.assertFalse(this.validateProgram(implicitString),
			"Construction of int using string should not work");

		final String implicitNull = "int x = null;";
		Assertions.assertFalse(this.validateProgram(implicitNull),
			"Construction of int using null should not work");

		final String implicitObject = """
			Random obj;
			int x = obj;
			""";
		Assertions.assertFalse(this.validateProgram(implicitObject),
			"Construction of int using object should not work");

		final String recursive = "int x = x;";
		Assertions.assertFalse(this.validateProgram(recursive),
			"Initialization by self-reference should not work");
	}

	/**
	 * Test the label logic.
	 */
	@Test
	void testLabels() {
		final String endLabel = """
			END:
			""";
		Assertions.assertTrue(this.validateProgram(endLabel),
			"We should be able to have a label at the end of the program");

		final String labeledStatement = """
			int x;
			before:
			x++;
			""";
		Assertions.assertTrue(this.validateProgram(labeledStatement),
			"We should be able to have a label before a statement");

		final String duplicated = """
			duplicated:
			int x;
			duplicated:
			x++;
			""";
		Assertions.assertFalse(this.validateProgram(duplicated),
			"We should not be able to have duplicate labels");
	}

	/**
	 * Validates method calls.
	 */
	@Test
	void testMethods() {
		ScriptManager.registerClass(DebugMethods.class);

		final String methodChain = """
			TestObject object = TEST_getObject();
			object.getSelf().getSelf().getSelf();
			""";
		Assertions.assertTrue(this.validateProgram(methodChain),
			"We should be able to call static and instance methods");

		final String ternaryCall = """
			Object x = TEST_getObject(), y = TEST_getObject();
			int z = 3;
			(z > 2 ? x : y).getSelf();
			""";
		Assertions.assertTrue(this.validateProgram(ternaryCall),
			"We should be able to call methods on objects from a ternary");

		final String charCall = """
			char x = 'e';
			x.toString();
			""";
		Assertions.assertFalse(this.validateProgram(charCall),
			"We should not be able to call methods on characters");

		final String intCall = """
			int x = 1;
			x.toString();
			""";
		Assertions.assertFalse(this.validateProgram(intCall),
			"We should not be able to call methods on integers");

		final String doubleCall = """
			double x = 3.12;
			x.toString();
			""";
		Assertions.assertFalse(this.validateProgram(doubleCall),
			"We should not be able to call methods on doubles");

		final String booleanCall = """
			boolean x = true;
			x.toString();
			""";
		Assertions.assertFalse(this.validateProgram(booleanCall),
			"We should not be able to call methods on booleans");

		final String stringCall = """
			string x = "test";
			x.toString();
			""";
		Assertions.assertFalse(this.validateProgram(stringCall),
			"We should not be able to call methods on strings");
	}

	/**
	 * Check object assignment.
	 */
	@Test
	void testObjectAssignment() {
		final String sameType = """
			Random obj1;
			Random obj2;
			obj2 = obj1;
			""";
		Assertions.assertTrue(this.validateProgram(sameType),
			"Assignment to object using another of the same type should work");

		final String differentType = """
			Random obj1;
			Another obj2;
			obj2 = obj1;
			""";
		Assertions.assertFalse(this.validateProgram(differentType),
			"Assignment to object using another of different type should not work");

		final String implicitChar = """
			Example object;
			object = 'a';
			""";
		Assertions.assertFalse(this.validateProgram(implicitChar),
			"Assignment to object using char should not work");

		final String implicitInt = """
			Example object;
			object = 1;
			""";
		Assertions.assertFalse(this.validateProgram(implicitInt),
			"Assignment to object using int should not work");

		final String implicitDouble = """
			Example object;
			object = 5.21;
			""";
		Assertions.assertFalse(this.validateProgram(implicitDouble),
			"Assignment to object using double should not work");

		final String implicitBoolean = """
			Example object;
			object = false;
			""";
		Assertions.assertFalse(this.validateProgram(implicitBoolean),
			"Assignment to object using boolean should not work");

		final String implicitString = """
			Example object;
			object = "test";
			""";
		Assertions.assertFalse(this.validateProgram(implicitString),
			"Assignment to object using string should not work");
	}

	/**
	 * Check object declaration.
	 */
	@Test
	void testObjectDeclaration() {
		final String minimum = "Random obj;";
		Assertions.assertTrue(this.validateProgram(minimum),
			"Declaration of object should work");

		final String sameType = """
			Random obj1 = null;
			Random obj2 = obj1;
			""";
		Assertions.assertTrue(this.validateProgram(sameType),
			"Construction of object using another of the same type should work");

		final String differentType = """
			Random obj1;
			Another obj2 = obj1;
			""";
		Assertions.assertFalse(this.validateProgram(differentType),
			"Construction of object using another of different type should not work");

		final String implicitChar = """
			Example object = 'd';
			""";
		Assertions.assertFalse(this.validateProgram(implicitChar),
			"Construction of object using char should not work");

		final String implicitInt = """
			Example object = 1;
			""";
		Assertions.assertFalse(this.validateProgram(implicitInt),
			"Construction of object using int should not work");

		final String implicitDouble = """
			Example object = 5.21;
			""";
		Assertions.assertFalse(this.validateProgram(implicitDouble),
			"Construction of object using double should not work");

		final String implicitBoolean = """
			Example object = false;
			""";
		Assertions.assertFalse(this.validateProgram(implicitBoolean),
			"Construction of object using boolean should not work");

		final String implicitString = """
			Example object = "test";
			""";
		Assertions.assertFalse(this.validateProgram(implicitString),
			"Construction of object using string should not work");
	}

	/**
	 * Check string assignments.
	 */
	@Test
	void testStringAssignment() {
		final String string = """
			string x;
			x = "test";
			""";
		Assertions.assertTrue(this.validateProgram(string),
			"Construction of string using string should work");

		final String implicitNull = """
			string x;
			x = null;
			""";
		Assertions.assertTrue(this.validateProgram(implicitNull),
			"Construction of string using null should work");

		final String implicitChar = """
			string x;
			x = 'y';
			""";
		Assertions.assertFalse(this.validateProgram(implicitChar),
			"Construction of string using char should not work");

		final String implicitInt = """
			string x;
			x = 8;
			""";
		Assertions.assertFalse(this.validateProgram(implicitInt),
			"Construction of string using int should not work");

		final String implicitDouble = """
			string x;
			x = 4.1;
			""";
		Assertions.assertFalse(this.validateProgram(implicitDouble),
			"Construction of string using double should not work");

		final String implicitObject = """
			Random obj;
			string x;
			x = obj;
			""";
		Assertions.assertFalse(this.validateProgram(implicitObject),
			"Construction of string using object should not work");
	}

	/**
	 * Check string declarations.
	 */
	@Test
	void testStringDeclaration() {
		final String minimum = "string x;";
		Assertions.assertTrue(this.validateProgram(minimum),
			"Construction of plain string declaration should work");

		final String string = "string x = \"test\";";
		Assertions.assertTrue(this.validateProgram(string),
			"Construction of string using string should work");

		final String implicitNull = "string x = null;";
		Assertions.assertTrue(this.validateProgram(implicitNull),
			"Construction of string using null should work");

		final String implicitChar = "string x = 'a';";
		Assertions.assertFalse(this.validateProgram(implicitChar),
			"Construction of string using char should not work");

		final String implicitInt = "string x = 1;";
		Assertions.assertFalse(this.validateProgram(implicitInt),
			"Construction of string using int should not work");

		final String implicitDouble = "string x = 1.0;";
		Assertions.assertFalse(this.validateProgram(implicitDouble),
			"Construction of string using double should not work");

		final String implicitObject = """
			Random obj;
			string x = obj;
			""";
		Assertions.assertFalse(this.validateProgram(implicitObject),
			"Construction of string using object should not work");

		final String recursive = "string x = x;";
		Assertions.assertFalse(this.validateProgram(recursive),
			"Initialization by self-reference should not work");
	}

	/**
	 * Validates that we can only have 1 default label in a switch statement.
	 */
	@Test
	void testSwitchDefault() {
		final String zeroDefaults = """
			int x = 4;
			switch (x) {
			  case 1:
			    break;
			  case 2:
			  case 3:
			    break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(zeroDefaults),
			"Zero defaults should pass");

		final String oneDefault = """
			int x = 4;
			switch (x) {
			  case 1:
			    break;
			  case 2:
			  case 3:
			    break;
			  default:
			    break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(oneDefault),
			"One default should pass");

		final String twoDefaults = """
			int x = 4;
			switch (x) {
			  case 1:
			  default:
			    break;
			  case 2:
			  case 3:
			    break;
			  default:
			    break;
			}
			""";
		Assertions.assertFalse(this.validateProgram(twoDefaults),
			"Two defaults should fail");
	}

	/**
	 * Test the ternary operator.
	 */
	@Test
	void testTernary() {
		final String usingVariable = """
			boolean cond = true;
			int x = cond ? 1 : 4;
			""";
		Assertions.assertTrue(this.validateProgram(usingVariable),
			"Ternary operators should work with boolean variables");
		final String hardcoded = """
			double x = true ? 1.0 : 4.3;
			""";
		Assertions.assertTrue(this.validateProgram(hardcoded),
			"Ternary operators should work with boolean constants");

		final String relationals = """
			double x = 4.45;
			// It's testing types, don't @ me
			boolean y = x <= 500 ? true : false;
			""";
		Assertions.assertTrue(this.validateProgram(relationals),
			"Ternary operators should work with relational operators");

		final String equality = """
			int x = 45;
			double y = x != 46 ? 4.1 : 2.1;
			""";
		Assertions.assertTrue(this.validateProgram(equality),
			"Ternary operators should work with equality operators");

		final String differentValidTypes1 = """
			int x = 45;
			double y = x != 46 ? 4.1 : 2;
			""";
		Assertions.assertTrue(this.validateProgram(differentValidTypes1),
			"Ternary operators should work with reasonable casting");

		final String differentValidTypes2 = """
			int x = 45;
			double y = x != 46 ? 4 : 0.2;
			""";
		Assertions.assertTrue(this.validateProgram(differentValidTypes2),
			"Ternary operators should work with reasonable casting");

		final String differentInvalidTypes1 = """
			int x = 45;
			int y = x != 46 ? 4 : true;
			""";
		Assertions.assertFalse(this.validateProgram(differentInvalidTypes1),
			"Ternary operators should not work with unreasonable types");

		final String differentInvalidTypes2 = """
			int x = 45;
			int y = x != 46 ? false : 4.667;
			""";
		Assertions.assertFalse(this.validateProgram(differentInvalidTypes2),
			"Ternary operators should not work with unreasonable types");
	}

	/**
	 * Validates ternary conditionals.
	 */
	@Test
	void testTernaryConditional() {
		final String plainBool = """
			int x = true ? 3 : 5;
			""";
		Assertions.assertTrue(this.validateProgram(plainBool),
			"We should be able to use a boolean for ternary operators condition");

		final String boolVar = """
			boolean b = false;
			int x = b ? 5 : 6;
			""";
		Assertions.assertTrue(this.validateProgram(boolVar),
			"We should be able to use a boolean variable for ternary operators condition");

		final String nestedTernary = """
			int x = 5;
			int y = (x < 4 ? false : true) ? 5 : 6;
			""";
		Assertions.assertTrue(this.validateProgram(nestedTernary),
			"We should be able to use a ternary in ternary operators condition");

		final String methodCall = """
			int y = TEST_getBoolean() ? 5 : 6;
			""";
		Assertions.assertTrue(this.validateProgram(methodCall),
			"We should be able to use a method call in ternary operators condition");

		final String[] invalidTypes = {"'t'", "5", "5.2", "\"true\"", "null"};

		for (String type : invalidTypes) {
			final String invalid = String.format("int x = %s ? 2 : 1;", type);
			Assertions.assertFalse(this.validateProgram(invalid),
				"We should not be able to use a constant " + type
					+ " in ternary operators condition");
		}

		final String[] invalidObjects = {"char x = 'c';", "int x = 346;",
			"double x = 0.01;", "string x = \"true\";", "TestObj x = null;"};

		for (String type : invalidObjects) {
			final String invalid = String.format("%s int x = x ? 0 : 3;", type);
			Assertions.assertFalse(this.validateProgram(invalid),
				"We should not be able to use a constant " + type
					+ " in ternary operators condition");
		}
	}

	/**
	 * Test the while statement for equality expressions.
	 */
	@Test
	void testWhileEquality() {
		final String equalityExpression1 = """
			while(1 == 2) {
				break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(equalityExpression1),
			"While should work with an equality expression");

		final String equalityExpression2 = """
			int x = 1;
			while(1 == x) {
				break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(equalityExpression2),
			"While should work with an equality expression");

		final String equalityExpression3 = """
			int x = 1;
			while(x != 1) {
				break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(equalityExpression3),
			"While should work with an equality expression");

		final String equalityExpression4 = """
			int x = 1;
			while(x != x) {
				break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(equalityExpression4),
			"While should work with an equality expression");
	}

	/**
	 * Test the while statement with logical expressions.
	 */
	@Test
	void testWhileLogical() {
		final String logicalExpression1 = """
			int x = 1;
			while(x <= 3 || 3 > x) {
				break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(logicalExpression1),
			"While should work with a logical expression");

		final String logicalExpression2 = """
			boolean x = true;
			while(x && 1 > 3) {
				break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(logicalExpression2),
			"While should work with a logical expression");

		final String logicalExpression3 = """
			int x = 1;
			while(!(x <= x || x > x)) {
				break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(logicalExpression3),
			"While should work with a logical expression");

	}

	/**
	 * Test the while statement with negative cases.
	 */
	@Test
	void testWhileNegative() {
		final String integer = """
			while(1) {
				break;
			}
			""";
		Assertions.assertFalse(this.validateProgram(integer),
			"While should not work with an integer");

		final String character = """
			while('y') {
				break;
			}
			""";
		Assertions.assertFalse(this.validateProgram(character),
			"While should not work with a character");

		final String string = """
			while("true") {
				break;
			}
			""";
		Assertions.assertFalse(this.validateProgram(string),
			"While should not work with a string");

		final String nullValue = """
			while(null) {
				break;
			}
			""";
		Assertions.assertFalse(this.validateProgram(nullValue),
			"While should not work with a null");

		final String numericExpression = """
			while(1 + 2) {
				break;
			}
			""";
		Assertions.assertFalse(this.validateProgram(numericExpression),
			"While should not work with a numeric expression");
	}

	/**
	 * Test the while statement with relational expressions.
	 */
	@Test
	void testWhileRelation() {
		final String relationExpression1 = """
			while(3 < 2) {
				break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(relationExpression1),
			"While should work with an relational expression");

		final String relationExpression2 = """
			int x = 1;
			while(x > 1) {
				break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(relationExpression2),
			"While should work with an relational expression");

		final String relationExpression3 = """
			int x = 1;
			while(3 <= x) {
				break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(relationExpression3),
			"While should work with an relational expression");

		final String relationExpression4 = """
			int x = 1;
			while(x <= x) {
				break;
			}
			""";
		Assertions.assertTrue(this.validateProgram(relationExpression4),
			"While should work with an relational expression");
	}

	/**
	 * Validate a program and return what the results were.
	 *
	 * @param program The program to validate.
	 * @return The results from the tree validator.
	 */
	private boolean validateProgram(@NonNull String program) {
		TestValidator.errorListener.resetErrorCount();

		CharStream stream = CharStreams.fromString(program);
		TestValidator.lexer.setInputStream(stream);
		TokenStream tokenStream = new BufferedTokenStream(TestValidator.lexer);
		TestValidator.parser.setTokenStream(tokenStream);
		CompilationUnitContext context = TestValidator.parser.compilationUnit();
		if (TestValidator.errorListener.getErrorCount() > 0) {
			Assertions.fail("There should be no errors parsing");
		}

		Assertions.assertEquals(0,
			TestValidator.parser.getNumberOfSyntaxErrors(),
			"Parser should have no syntax errors");

		CompilationUnit ast = AbstractSyntaxTree.process(context);
		if (ast == null || ast.isInvalid()) {
			Assertions.fail("There should be a valid syntax tree");
		}

		TestValidator.processor.processTreeTypes(ast);

		return TestValidator.validator.validate(ast);
	}

}
