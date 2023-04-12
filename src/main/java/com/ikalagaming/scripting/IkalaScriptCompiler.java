package com.ikalagaming.scripting;

import com.ikalagaming.scripting.IkalaScriptParser.CompilationUnitContext;
import com.ikalagaming.scripting.ast.AbstractSyntaxTree;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.visitors.OptimizationPass;
import com.ikalagaming.scripting.ast.visitors.TreeValidator;
import com.ikalagaming.scripting.ast.visitors.TypePreprocessor;
import com.ikalagaming.scripting.interpreter.Instruction;
import com.ikalagaming.scripting.interpreter.InstructionGenerator;
import com.ikalagaming.scripting.interpreter.ScriptRuntime;

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;

import java.util.List;
import java.util.Optional;

/**
 * Handle transforming a script into something that can execute.
 * 
 * @author Ches Burks
 *
 */
public class IkalaScriptCompiler {

	/**
	 * Handle the parsing of a character stream.
	 * 
	 * @param input The input stream.
	 * @return The corresponding runtime.
	 */
	public Optional<ScriptRuntime> parse(CharStream input) {
		// Generate parse tree
		IkalaScriptLexer lexer = new IkalaScriptLexer(input);
		TokenStream tokenStream = new BufferedTokenStream(lexer);
		IkalaScriptParser parser = new IkalaScriptParser(tokenStream);
		CompilationUnitContext context = parser.compilationUnit();

		// Convert parse tree to an Abstract Syntax Tree
		CompilationUnit ast = AbstractSyntaxTree.process(context);

		// Clean up types
		TypePreprocessor processor = new TypePreprocessor();
		processor.processTreeTypes(ast);

		// Validate the tree
		TreeValidator validator = new TreeValidator();
		if (!validator.validate(ast)) {
			return Optional.empty();
		}

		// Optimize the tree
		OptimizationPass optimizer = new OptimizationPass();
		optimizer.optimize(ast);

		// Generate instructions
		InstructionGenerator gen = new InstructionGenerator();
		List<Instruction> instructions = gen.process(ast);

		// Convert to a runtime
		ScriptRuntime runtime = new ScriptRuntime(instructions);

		return Optional.of(runtime);
	}
}
