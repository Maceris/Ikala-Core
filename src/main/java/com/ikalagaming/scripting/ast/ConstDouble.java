package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * A floating point literal.
 *
 * @author Ches Burks
 */
@Getter
@Setter
public class ConstDouble extends Node {
    private double value;

    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
