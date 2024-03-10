package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.Block;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.ExprArithmetic;
import com.ikalagaming.scripting.ast.Node;
import com.ikalagaming.scripting.ast.StatementList;
import com.ikalagaming.scripting.ast.SwitchBlockGroup;

/**
 * Add some annotations to node as required for the instruction generation.
 *
 * @author Ches Burks
 */
public class NodeAnnotationPass implements ASTVisitor {

    /**
     * Optimize the syntax tree.
     *
     * @param ast The tree to validate.
     */
    public void annotate(CompilationUnit ast) {
        processTree(ast);
    }

    /**
     * Go through all immediate children nodes, and ignore expression results that are not going to
     * be stored anywhere.
     *
     * @param node The node that should contain statements.
     */
    private void ignoreExpressionResults(Node node) {
        for (Node child : node.getChildren()) {
            if (child instanceof ExprArithmetic arithmetic) {
                arithmetic.setIgnoreResult(true);
            }
        }
    }

    /**
     * Process the tree recursively.
     *
     * @param node The node we are processing.
     */
    private void processTree(Node node) {
        for (Node child : node.getChildren()) {
            processTree(child);
        }
        node.process(this);
    }

    @Override
    public void visit(Block node) {
        ignoreExpressionResults(node);
    }

    @Override
    public void visit(CompilationUnit node) {
        ignoreExpressionResults(node);
    }

    @Override
    public void visit(StatementList node) {
        ignoreExpressionResults(node);
    }

    @Override
    public void visit(SwitchBlockGroup node) {
        ignoreExpressionResults(node);
    }
}
