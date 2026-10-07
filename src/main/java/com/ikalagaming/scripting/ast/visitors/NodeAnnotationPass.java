package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.Block;
import com.ikalagaming.scripting.ast.Call;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.DoWhile;
import com.ikalagaming.scripting.ast.ExprArithmetic;
import com.ikalagaming.scripting.ast.ExprAssign;
import com.ikalagaming.scripting.ast.ForLoop;
import com.ikalagaming.scripting.ast.If;
import com.ikalagaming.scripting.ast.LabeledStatement;
import com.ikalagaming.scripting.ast.Node;
import com.ikalagaming.scripting.ast.StatementList;
import com.ikalagaming.scripting.ast.SwitchBlockGroup;
import com.ikalagaming.scripting.ast.While;

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
            ignoreResult(child);
        }
    }

    /**
     * Mark a node that is used as a statement, if it's an expression, as not needing to keep its
     * result on the stack.
     *
     * @param node The node that is used as a statement.
     */
    private void ignoreResult(Node node) {
        if (node instanceof ExprArithmetic arithmetic) {
            arithmetic.setIgnoreResult(true);
        } else if (node instanceof Call call) {
            call.setIgnoreResult(true);
        } else if (node instanceof ExprAssign assign) {
            assign.setIgnoreResult(true);
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
    public void visit(DoWhile node) {
        // The body
        ignoreResult(node.getChildren().get(0));
    }

    @Override
    public void visit(ForLoop node) {
        // Everything except the condition is a statement
        final int conditionIndex = node.isCondition() ? (node.isInitializer() ? 1 : 0) : -1;
        for (int i = 0; i < node.getChildren().size(); ++i) {
            if (i != conditionIndex) {
                ignoreResult(node.getChildren().get(i));
            }
        }
    }

    @Override
    public void visit(If node) {
        // The if and else bodies, but not the condition
        for (int i = 1; i < node.getChildren().size(); ++i) {
            ignoreResult(node.getChildren().get(i));
        }
    }

    @Override
    public void visit(LabeledStatement node) {
        // The label is first, then the statement
        for (int i = 1; i < node.getChildren().size(); ++i) {
            ignoreResult(node.getChildren().get(i));
        }
    }

    @Override
    public void visit(StatementList node) {
        ignoreExpressionResults(node);
    }

    @Override
    public void visit(SwitchBlockGroup node) {
        ignoreExpressionResults(node);
    }

    @Override
    public void visit(While node) {
        // The body
        ignoreResult(node.getChildren().get(1));
    }
}
