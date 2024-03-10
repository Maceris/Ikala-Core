package com.ikalagaming.scripting.ast;

/**
 * A ternary expression. The children are the conditional, value if true, then value if false.
 *
 * @author Ches Burks
 */
public class ExprTernary extends Node {
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

        result.append(this.getClass().getSimpleName());

        result.append(" { ");
        result.append(children.get(0).toString());
        result.append(" ? ");
        result.append(children.get(1).toString());
        result.append(" : ");
        result.append(children.get(2).toString());
        result.append(" } ");

        return result.toString();
    }
}
