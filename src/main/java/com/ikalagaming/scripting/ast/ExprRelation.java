package com.ikalagaming.scripting.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A relational expression, comparing numeric types.
 *
 * @author Ches Burks
 */
@Getter
@Setter
public class ExprRelation extends Node {
    /**
     * The available operators.
     *
     * @author Ches Burks
     */
    @AllArgsConstructor
    public enum Operator {
        /** Less than. */
        LT("<"),
        /** Less than or equal to. */
        LTE("<="),
        /** Greater than. */
        GT(">"),
        /** Greater than or equal to. */
        GTE(">=");

        private final String value;
    }

    /** The operator that is being used. */
    private Operator operator;

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
        result.append(' ');
        result.append(operator.value);
        result.append(' ');
        result.append(children.get(1).toString());
        result.append(" } ");

        return result.toString();
    }
}
