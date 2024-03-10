package com.ikalagaming.scripting.ast;

/**
 * A statement with a label. The children are the label, and the statement.
 *
 * @author Ches Burks
 */
public class LabeledStatement extends Node {
    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
