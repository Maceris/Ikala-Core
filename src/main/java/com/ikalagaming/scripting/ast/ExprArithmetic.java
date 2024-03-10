package com.ikalagaming.scripting.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * An arithmetic expression, acting on numeric types. This can have one or two child nodes,
 * depending on if it's a unary or binary operator.
 *
 * @author Ches Burks
 */
@Getter
@Setter
public class ExprArithmetic extends Node {
    /**
     * The operators that can be used.
     *
     * @author Ches Burks
     */
    @AllArgsConstructor
    @Getter
    public enum Operator {
        /** Add the first and second number. */
        ADD("+"),
        /** Subtract the second number from the first. */
        SUB("-"),
        /** Multiply the first and second number. */
        MUL("*"),
        /** Divide the first number by the second. */
        DIV("/"),
        /** Modulus, or remainder, of dividing the first number by the second. */
        MOD("%%"),
        /** A unary prefix increment. */
        INC_PREFIX("++"),
        /** A unary suffix increment. */
        INC_SUFFIX("++"),
        /** A unary prefix decrement. */
        DEC_PREFIX("--"),
        /** A unary suffix decrement. */
        DEC_SUFFIX("--");

        private String readable;
    }

    /** The associated operator. */
    private Operator operator;

    /**
     * The number of times the unary operator appears, since the grammar allows us to stack them.
     */
    private int unaryCount;

    /**
     * Used to signify that the result of this expression is not used for anything, and does not
     * need to be stored on the stack. Mostly required because things like ++x and x-- are valid
     * statements.
     */
    private boolean ignoreResult;

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

        if (ignoreResult) {
            result.append(" result_ignored ");
        } else {
            result.append(" result_kept ");
        }
        result.append(" { ");

        final String firstChild = children.get(0).toString();

        final String secondChild = children.size() > 1 ? children.get(1).toString() : "";

        switch (operator) {
            case ADD, SUB:
                if (children.size() == 1) {
                    result.append(operator.getReadable());
                    result.append(firstChild);
                    break;
                }
                // fallthrough
            case MOD, MUL, DIV:
                result.append(firstChild);
                result.append(' ');
                result.append(operator.getReadable());
                result.append(' ');
                result.append(secondChild);
                break;
            case DEC_PREFIX, INC_PREFIX:
                result.append(operator.getReadable());
                result.append('x');
                result.append(unaryCount);
                result.append(' ');
                result.append(firstChild);
                break;
            case DEC_SUFFIX, INC_SUFFIX:
                result.append(firstChild);
                result.append(' ');
                result.append(operator.getReadable());
                result.append('x');
                result.append(unaryCount);
                break;
            default:
                result.append(operator.toString());
                break;
        }

        if (children.size() > 2) {
            result.append("Extra nodes ( ");
            for (int i = 2; i < children.size(); ++i) {
                result.append(children.get(i).toString());
                if (i < children.size() - 1) {
                    result.append(", ");
                }
            }
            result.append(" ) ");
        }
        result.append(" } ");
        return result.toString();
    }
}
