package com.ikalagaming.scripting.ast;

/**
 * A block that might contain statements, switch block groups, or switch labels.
 *
 * @author Ches Burks
 */
public class Block extends Node {
    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
