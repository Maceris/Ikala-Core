package com.ikalagaming.scripting;

import com.ikalagaming.scripting.IkalaScriptParser.CompilationUnitContext;
import com.ikalagaming.scripting.ast.AbstractSyntaxTree;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.visitors.NodeAnnotationPass;
import com.ikalagaming.scripting.ast.visitors.OptimizationPass;
import com.ikalagaming.scripting.ast.visitors.TreeValidator;
import com.ikalagaming.scripting.ast.visitors.TypePreprocessor;
import com.ikalagaming.scripting.interpreter.Instruction;
import com.ikalagaming.scripting.interpreter.InstructionGenerator;
import com.ikalagaming.scripting.interpreter.ScriptRuntime;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Handle transforming a script into something that can execute.
 *
 * @author Ches Burks
 */
public class IkalaScriptCompiler {

    /**
     * The results of compiling a script.
     *
     * @param runtime The runtime for the script, if it compiled successfully.
     * @param syntaxTree The abstract syntax tree, which may be null if parsing failed. If later
     *     stages failed, this is the tree as of the stage that failed.
     * @param errors Problems found while compiling, which is empty if it succeeded.
     */
    public record CompileResult(
            Optional<ScriptRuntime> runtime,
            CompilationUnit syntaxTree,
            List<ScriptDiagnostics.Diagnostic> errors) {

        /**
         * Whether the script compiled successfully.
         *
         * @return True if there is a runtime.
         */
        public boolean succeeded() {
            return runtime.isPresent();
        }
    }

    /**
     * Compile a script, collecting the syntax tree and any problems, for tools like debuggers.
     * Problems are still logged.
     *
     * @param input The input stream.
     * @return The results of compiling.
     */
    public static CompileResult compile(@NonNull CharStream input) {
        return compile(input, Set.of());
    }

    /**
     * Compile a script that uses globals the host provides, collecting the syntax tree and any
     * problems. Problems are still logged. The runtime needs a value for each global before it
     * runs, see {@link ScriptRuntime#setGlobal(String, Object)}.
     *
     * @param input The input stream.
     * @param globals The names of the globals, like {@code ui} or {@code Math}.
     * @return The results of compiling.
     */
    public static CompileResult compile(@NonNull CharStream input, @NonNull Set<String> globals) {
        final List<ScriptDiagnostics.Diagnostic> errors = new ArrayList<>();
        final CompilationUnit[] tree = new CompilationUnit[1];
        final Optional<ScriptRuntime> runtime =
                ScriptDiagnostics.collect(errors, () -> compile(input, globals, tree, errors));
        return new CompileResult(runtime, tree[0], List.copyOf(errors));
    }

    /**
     * Compile a script, storing the syntax tree as soon as it's created.
     *
     * @param input The input stream.
     * @param globals The names of the globals the host provides.
     * @param tree A single element array to store the tree in.
     * @param errors Errors reported so far, used to explain failures that didn't report anything.
     * @return The runtime if compiling succeeded.
     */
    private static Optional<ScriptRuntime> compile(
            @NonNull CharStream input,
            @NonNull Set<String> globals,
            CompilationUnit @NonNull [] tree,
            List<ScriptDiagnostics.Diagnostic> errors) {
        // Generate parse tree
        ParserErrorListener errorListener = new ParserErrorListener();

        IkalaScriptLexer lexer = new IkalaScriptLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);
        TokenStream tokenStream = new BufferedTokenStream(lexer);
        IkalaScriptParser parser = new IkalaScriptParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        CompilationUnitContext context = parser.compilationUnit();
        if (errorListener.getErrorCount() > 0) {
            return failed(errors, "COMPILE_FAILED_SYNTAX");
        }

        // Convert parse tree to an Abstract Syntax Tree
        CompilationUnit ast;
        try {
            ast = AbstractSyntaxTree.process(context);
        } catch (IllegalArgumentException e) {
            // Invalid types or literals, which have already been reported
            return failed(errors, "COMPILE_FAILED_TREE");
        }
        tree[0] = ast;
        if (ast.isInvalid()) {
            return failed(errors, "COMPILE_FAILED_TREE");
        }

        // Clean up types
        TypePreprocessor processor = new TypePreprocessor();
        processor.processTreeTypes(ast, globals);

        // Validate the tree
        TreeValidator validator = new TreeValidator();
        if (!validator.validate(ast, globals)) {
            return failed(errors, "COMPILE_FAILED_VALIDATION");
        }

        // Optimize the tree
        OptimizationPass optimizer = new OptimizationPass();
        optimizer.optimize(ast);

        // Annotate the tree
        NodeAnnotationPass annotator = new NodeAnnotationPass();
        annotator.annotate(ast);

        // Generate instructions
        InstructionGenerator gen = new InstructionGenerator();
        List<Instruction> instructions = gen.process(ast);

        // Convert to a runtime, which can start at labels that skip no declarations
        Map<String, Integer> entryPoints = new HashMap<>(gen.getScriptLabels());
        entryPoints.keySet().retainAll(validator.getEntryLabels());
        return Optional.of(new ScriptRuntime(instructions, entryPoints));
    }

    /**
     * Note that a compilation stage failed, if nothing more specific was reported.
     *
     * @param errors The errors reported so far.
     * @param messageKey The key for the message to report if no errors were reported.
     * @return An empty optional.
     */
    private static Optional<ScriptRuntime> failed(
            @NonNull List<ScriptDiagnostics.Diagnostic> errors, @NonNull String messageKey) {
        if (errors.isEmpty()) {
            errors.add(
                    new ScriptDiagnostics.Diagnostic(
                            -1,
                            -1,
                            SafeResourceLoader.getString(
                                    messageKey, ScriptManager.getResourceBundle())));
        }
        return Optional.empty();
    }

    /**
     * Handle the parsing of a character stream.
     *
     * @param input The input stream.
     * @return The corresponding runtime.
     */
    public static Optional<ScriptRuntime> parse(CharStream input) {
        return compile(input).runtime();
    }

    /** Private constructor so that this class is not instantiated. */
    private IkalaScriptCompiler() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
