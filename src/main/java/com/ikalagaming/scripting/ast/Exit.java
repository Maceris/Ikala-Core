package com.ikalagaming.scripting.ast;

/**
 * Used to stop running the program.
 *
 * @author Ches Burks
 */
public class Exit extends Node {
    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
