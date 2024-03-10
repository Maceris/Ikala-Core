package com.ikalagaming.scripting.ast;

/**
 * A cast to a different type. The child is the expression to cast, and the type is the type we are
 * trying to cast to.
 *
 * @author Ches Burks
 */
public class Cast extends Node {
    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
