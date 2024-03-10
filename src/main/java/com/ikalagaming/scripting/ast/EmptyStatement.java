package com.ikalagaming.scripting.ast;

/**
 * Does nothing.
 *
 * @author Ches Burks
 */
public class EmptyStatement extends Node {
    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
