package com.ikalagaming.scripting.ast;

/**
 * A list of switch labels and block statements. They should be in that order.
 *
 * @author Ches Burks
 */
public class SwitchBlockGroup extends Node {
    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
