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

		final String recursive = "boolean x = x;";
		Assertions.assertFalse(this.validateProgram(recursive),
			"Initialization by self-reference should not work");
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

		final String recursive = "char x = x;";
		Assertions.assertFalse(this.validateProgram(recursive),
			"Initialization by self-reference should not work");
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

		final String recursive = "double x = x;";
		Assertions.assertFalse(this.validateProgram(recursive),
			"Initialization by self-reference should not work");
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
