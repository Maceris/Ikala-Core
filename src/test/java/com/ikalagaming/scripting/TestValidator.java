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
