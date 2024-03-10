package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * An identifier.
 *
 * @author Ches Burks
 */
@Getter
@Setter
public class Identifier extends Node {
    private String name;

    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (type != null) {
            result.append(type.toString());
            result.append(" ");
        } else {
            result.append("____ ");
        }
        result.append(name);
        return result.toString();
    }
}
