package com.ikalagaming.scripting.ast;

/**
 * A goto statement, which contains an identifier to jump to.
 *
 * @author Ches Burks
 */
public class Goto extends Node {
    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
