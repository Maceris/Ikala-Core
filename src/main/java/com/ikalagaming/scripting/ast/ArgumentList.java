package com.ikalagaming.scripting.ast;

/**
 * Represents a list of arguments to a method call.
 *
 * @author Ches Burks
 */
public class ArgumentList extends Node {
    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
