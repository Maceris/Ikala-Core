package com.ikalagaming.scripting.ast;

/**
 * A null literal value, with a void type.
 *
 * @author Ches Burks
 */
public class ConstNull extends Node {
    /** Create a null constant. */
    public ConstNull() {
        setType(Type.voidType());
    }

    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "null";
    }
}
