package com.ikalagaming.scripting.ast;

import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Turns parts of a syntax tree back into something that looks like the source code, for error
 * messages. Expressions are printed as code, like {@code x += 1}, and anything else is described by
 * {@link SyntaxTreePrinter#describe(Node)}.
 *
 * @author Ches Burks
 */
public final class SourcePrinter {

    /** The longest we let printed code get before cutting it off, so messages stay readable. */
    private static final int MAX_LENGTH = 80;

    /**
     * Print a node as source code, if it's an expression, or describe it if it's not.
     *
     * @param node The node, which may be null.
     * @return The node as text, shortened if it is very long.
     */
    public static String toSource(Node node) {
        final String result = print(node);
        if (result.length() <= MAX_LENGTH) {
            return result;
        }
        return result.substring(0, MAX_LENGTH - 3) + "...";
    }

    /**
     * Print a node as source code.
     *
     * @param node The node, which may be null.
     * @return The node as text.
     */
    private static String print(Node node) {
        if (node == null) {
            return "<missing>";
        }
        final List<Node> children = node.getChildren();
        return switch (node) {
            case Identifier identifier -> identifier.getName();
            case Label label -> label.getName() + ":";
            case ConstBool constant -> Boolean.toString(constant.isValue());
            case ConstChar constant -> quote(Character.toString(constant.getValue()), '\'');
            case ConstDouble constant -> Double.toString(constant.getValue());
            case ConstInt constant -> Integer.toString(constant.getValue());
            case ConstString constant -> quote(constant.getValue(), '"');
            case ConstNull ignored -> "null";
            case ExprArithmetic arithmetic -> printArithmetic(arithmetic);
            case ExprAssign assign -> binary(children, assign.getOperator().getValue(), node);
            case ExprEquality equality -> binary(children, equality.getOperator().getValue(), node);
            case ExprRelation relation -> binary(children, relation.getOperator().getValue(), node);
            case ExprLogic logic ->
                    logic.getOperator() == ExprLogic.Operator.NOT
                            ? "!" + printChild(children, 0)
                            : binary(children, logic.getOperator().getValue(), node);
            case ExprTernary ignored ->
                    String.format(
                            "%s ? %s : %s",
                            printChild(children, 0),
                            printChild(children, 1),
                            printChild(children, 2));
            case Cast cast ->
                    "("
                            + SyntaxTreePrinter.typeName(cast.getType())
                            + ") "
                            + printChild(children, 0);
            case Call call -> printCall(call);
            case ArgumentList ignored -> printList(children);
            case VarDeclaration declaration ->
                    children.size() > 1
                            ? printChild(children, 0) + " = " + printChild(children, 1)
                            : printChild(children, 0);
            default -> SyntaxTreePrinter.describe(node);
        };
    }

    /**
     * Print an arithmetic expression, which might be unary.
     *
     * @param node The expression.
     * @return The expression as code.
     */
    private static String printArithmetic(@NonNull ExprArithmetic node) {
        final List<Node> children = node.getChildren();
        final String operator = node.getOperator().getReadable().replace("%%", "%");
        return switch (node.getOperator()) {
            case INC_PREFIX, DEC_PREFIX -> operator + printChild(children, 0);
            case INC_SUFFIX, DEC_SUFFIX -> printChild(children, 0) + operator;
            default ->
                    children.size() == 1
                            ? operator + printChild(children, 0)
                            : binary(children, operator, node);
        };
    }

    /**
     * Print a method call.
     *
     * @param call The call.
     * @return The call as code.
     */
    private static String printCall(@NonNull Call call) {
        final List<Node> children = call.getChildren();
        if (call.isPrimary()) {
            // Object, method name, then arguments
            final String arguments = children.size() > 2 ? print(children.get(2)) : "";
            return printChild(children, 0) + "." + printChild(children, 1) + "(" + arguments + ")";
        }
        // Method name, then arguments
        final String arguments = children.size() > 1 ? print(children.get(1)) : "";
        return printChild(children, 0) + "(" + arguments + ")";
    }

    /**
     * Print a binary expression.
     *
     * @param children The operands.
     * @param operator The operator.
     * @param node The expression, used if it doesn't have two operands.
     * @return The expression as code.
     */
    private static String binary(
            @NonNull List<Node> children, @NonNull String operator, @NonNull Node node) {
        if (children.size() != 2) {
            return SyntaxTreePrinter.describe(node);
        }
        return printOperand(children.get(0)) + " " + operator + " " + printOperand(children.get(1));
    }

    /**
     * Print an operand of a binary expression, with parentheses if it is also a binary expression
     * so the order is clear.
     *
     * @param node The operand.
     * @return The operand as code.
     */
    private static String printOperand(Node node) {
        final String result = print(node);
        final boolean compound =
                node != null
                        && node.getChildren().size() == 2
                        && (node instanceof ExprArithmetic
                                || node instanceof ExprEquality
                                || node instanceof ExprRelation
                                || node instanceof ExprLogic
                                || node instanceof ExprAssign);
        return compound ? "(" + result + ")" : result;
    }

    /**
     * Print a child, if it exists.
     *
     * @param children The children.
     * @param index The index of the child.
     * @return The child as code, or a placeholder if it is missing.
     */
    private static String printChild(@NonNull List<Node> children, int index) {
        return index < children.size() ? print(children.get(index)) : "<missing>";
    }

    /**
     * Print a comma separated list.
     *
     * @param children The items.
     * @return The items as code.
     */
    private static String printList(@NonNull List<Node> children) {
        return children.stream().map(SourcePrinter::print).collect(Collectors.joining(", "));
    }

    /**
     * Quote a value, escaping characters that would otherwise be hard to read.
     *
     * @param value The value.
     * @param quote The quote character.
     * @return The quoted and escaped value.
     */
    static String quote(String value, char quote) {
        if (value == null) {
            return "null";
        }
        StringBuilder result = new StringBuilder().append(quote);
        for (char c : value.toCharArray()) {
            switch (c) {
                case '\n' -> result.append("\\n");
                case '\r' -> result.append("\\r");
                case '\t' -> result.append("\\t");
                case '\\' -> result.append("\\\\");
                default -> {
                    if (c == quote) {
                        result.append('\\');
                    }
                    result.append(c);
                }
            }
        }
        return result.append(quote).toString();
    }

    /** Private constructor so that this class is not instantiated. */
    private SourcePrinter() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
