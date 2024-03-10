package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * A string literal.
 *
 * @author Ches Burks
 */
@Getter
@Setter
public class ConstString extends Node {
    private String value;

    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return value;
    }
}
