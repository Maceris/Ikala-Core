package com.ikalagaming.scripting.ast;

/**
 * A switch statement. This has 2 children, the expression and the block.
 *
 * @author Ches Burks
 */
public class SwitchStatement extends Node {
    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
