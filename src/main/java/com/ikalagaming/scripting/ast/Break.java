package com.ikalagaming.scripting.ast;

/**
 * A break statement.
 *
 * @author Ches Burks
 */
public class Break extends Node {
    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
