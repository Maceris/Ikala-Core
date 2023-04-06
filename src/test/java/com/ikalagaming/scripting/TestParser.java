package com.ikalagaming.scripting;

import com.ikalagaming.scripting.IkalaScriptParser.CompilationUnitContext;
import com.ikalagaming.scripting.ast.AbstractSyntaxTree;
import com.ikalagaming.scripting.ast.CompilationUnit;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.TokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * Tests the functionality of the scripting engine class.
 *
 * @author Ches Burks
 *
 */
class TestParser {

	/**
	 * The resources folder where the test scripts are located.
	 */
	private final String TEST_SCRIPT_FOLDER =
		System.getProperty("user.dir") + File.separatorChar + "build"
			+ File.separatorChar + "resources" + File.separatorChar + "test"
			+ File.separatorChar + "com" + File.separatorChar + "ikalagaming"
			+ File.separatorChar + "scripting" + File.separatorChar;

	/**
	 * Tries to evaluate some lua code that prints text.
	 */
	@Test
	public void testPrinting() {
		CharStream stream;
		try {
			stream = CharStreams
				.fromFileName(this.TEST_SCRIPT_FOLDER + "ParserSmoke.iks");
		}
		catch (IOException e) {
			Assertions.fail(e);
			return;
		}

		IkalaScriptLexer lexer = new IkalaScriptLexer(stream);
		TokenStream tokenStream = new BufferedTokenStream(lexer);
		IkalaScriptParser parser = new IkalaScriptParser(tokenStream);

		CompilationUnitContext context = parser.compilationUnit();

		Assertions.assertEquals(0, parser.getNumberOfSyntaxErrors(),
			"Parser should have no syntax errors");

		CompilationUnit ast = AbstractSyntaxTree.process(context);
		ast.processTreeTypes();
		Assertions.assertTrue(ast.validateTree(), "Validation should pass");
	}

}
