package com.ikalagaming.scripting;

import com.ikalagaming.scripting.IkalaScriptParser.CompilationUnitContext;
import com.ikalagaming.scripting.ast.AbstractSyntaxTree;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.visitors.TreeValidator;
import com.ikalagaming.scripting.ast.visitors.TypePreprocessor;

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
 */
class TestParser {

    /** The resources folder where the test scripts are located. */
    private final String TEST_SCRIPT_FOLDER =
            System.getProperty("user.dir")
                    + File.separatorChar
                    + "build"
                    + File.separatorChar
                    + "resources"
                    + File.separatorChar
                    + "test"
                    + File.separatorChar
                    + "com"
                    + File.separatorChar
                    + "ikalagaming"
                    + File.separatorChar
                    + "scripting"
                    + File.separatorChar;

    /** Loads the smoke suite and checks for validation errors. */
    @Test
    void testValidator() {
        CharStream stream;
        try {
            stream = CharStreams.fromFileName(this.TEST_SCRIPT_FOLDER + "ParserSmoke.iks");
        } catch (IOException e) {
            Assertions.fail(e);
            return;
        }

        ParserErrorListener errorListener = new ParserErrorListener();

        IkalaScriptLexer lexer = new IkalaScriptLexer(stream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);
        TokenStream tokenStream = new BufferedTokenStream(lexer);
        IkalaScriptParser parser = new IkalaScriptParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        CompilationUnitContext context = parser.compilationUnit();
        if (errorListener.getErrorCount() > 0) {
            Assertions.fail("There should be no errors parsing");
        }

        Assertions.assertEquals(
                0, parser.getNumberOfSyntaxErrors(), "Parser should have no syntax errors");

        CompilationUnit ast = AbstractSyntaxTree.process(context);
        if (ast == null || ast.isInvalid()) {
            Assertions.fail("There should be a valid syntax tree");
        }

        TypePreprocessor processor = new TypePreprocessor();
        processor.processTreeTypes(ast);

        TreeValidator validator = new TreeValidator();
        Assertions.assertTrue(validator.validate(ast), "Validation should pass");
    }
}
