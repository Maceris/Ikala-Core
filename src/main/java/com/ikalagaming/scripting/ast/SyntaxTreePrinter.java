package com.ikalagaming.scripting.ast;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Prints an abstract syntax tree as indented text, one node per line, which is much easier to read
 * than the single line {@link Node#toString()} for anything but small trees. For example:
 *
 * <pre>
 * CompilationUnit {
 *   VarDeclarationList (line 1) {
 *     TypeNode : int (line 1)
 *     VarDeclaration : int (line 1) {
 *       Identifier x : int (line 1)
 *     }
 *   }
 *   ExprAssign += : int (line 2) {
 *     Identifier x : int (line 2)
 *     ConstInt 1 : int (line 2)
 *   }
 * }
 * </pre>
 *
 * @author Ches Burks
 */
public final class SyntaxTreePrinter {

    /** The indentation for each level of the tree. */
    private static final String INDENT = "  ";

    /**
     * Print a tree.
     *
     * @param root The root of the tree.
     * @return The tree as indented text, with a line for each node.
     */
    public static String print(@NonNull Node root) {
        StringBuilder result = new StringBuilder();
        print(root, 0, result);
        return result.toString();
    }

    /**
     * Print a node and its children.
     *
     * @param node The node, which may be null if the tree is malformed.
     * @param depth How deep in the tree the node is.
     * @param result Where to put the text.
     */
    private static void print(Node node, int depth, @NonNull StringBuilder result) {
        result.append(INDENT.repeat(depth));
        if (node == null) {
            result.append("null\n");
            return;
        }
        result.append(describe(node));
        final List<Node> children = node.getChildren();
        if (children.isEmpty()) {
            result.append('\n');
            return;
        }
        result.append(" {\n");
        for (Node child : children) {
            print(child, depth + 1, result);
        }
        result.append(INDENT.repeat(depth)).append("}\n");
    }

    /**
     * Describe a single node, without its children.
     *
     * @param node The node.
     * @return The node type, details like names or values, its type, and line.
     */
    public static String describe(@NonNull Node node) {
        StringBuilder result = new StringBuilder(node.getClass().getSimpleName());
        final String details = details(node);
        if (!details.isEmpty()) {
            result.append(' ').append(details);
        }
        // Void is the type of statements, which isn't interesting to show
        if (node.getType() != null
                && node.getType().getBase() != Type.Base.VOID
                && node.getType().getBase() != Type.Base.UNKNOWN) {
            result.append(" : ").append(typeName(node.getType()));
        }
        if (node.getLine() > 0) {
            result.append(" (line ").append(node.getLine()).append(')');
        }
        return result.toString();
    }

    /**
     * The name of a type as it would be written in a script, like {@code int} or {@code string[]}.
     *
     * @param type The type.
     * @return The name of the type.
     */
    public static String typeName(@NonNull Type type) {
        StringBuilder result = new StringBuilder();
        if (type.getBase() == Type.Base.IDENTIFIER) {
            result.append(type.getValue());
        } else {
            result.append(type.getBase().name().toLowerCase(java.util.Locale.ROOT));
        }
        result.append("[]".repeat(Math.max(0, type.getDimensions())));
        return result.toString();
    }

    /**
     * Details specific to the kind of node, like the name of an identifier.
     *
     * @param node The node.
     * @return The details, or an empty string if there are none.
     */
    private static String details(@NonNull Node node) {
        return switch (node) {
            case Identifier identifier -> identifier.getName();
            case Label label -> label.getName();
            case ConstBool constant -> Boolean.toString(constant.isValue());
            case ConstChar constant ->
                    SourcePrinter.quote(Character.toString(constant.getValue()), '\'');
            case ConstDouble constant -> Double.toString(constant.getValue());
            case ConstInt constant -> Integer.toString(constant.getValue());
            case ConstString constant -> SourcePrinter.quote(constant.getValue(), '"');
            case ConstNull ignored -> "null";
            case ExprArithmetic arithmetic -> {
                String operator = arithmetic.getOperator().getReadable().replace("%%", "%");
                if (arithmetic.getUnaryCount() > 1) {
                    operator += " x" + arithmetic.getUnaryCount();
                }
                yield switch (arithmetic.getOperator()) {
                    case INC_PREFIX, DEC_PREFIX -> operator + " (prefix)";
                    case INC_SUFFIX, DEC_SUFFIX -> operator + " (suffix)";
                    default -> operator;
                };
            }
            case ExprAssign assign -> assign.getOperator().getValue();
            case ExprEquality equality -> equality.getOperator().getValue();
            case ExprLogic logic -> logic.getOperator().getValue();
            case ExprRelation relation -> relation.getOperator().getValue();
            case Call call -> call.isPrimary() ? "(method on an object)" : "";
            case ForLoop loop -> forLoopParts(loop);
            case SwitchLabel label -> label.isDefault() ? "default" : "";
            case TypeNode typeNode -> typeNode.isFinal() ? "final" : "";
            default -> "";
        };
    }

    /**
     * Describe which optional parts a for loop has, since its children don't say.
     *
     * @param loop The for loop.
     * @return The parts that are present.
     */
    private static String forLoopParts(@NonNull ForLoop loop) {
        List<String> parts = new ArrayList<>();
        if (loop.isInitializer()) {
            parts.add("initializer");
        }
        if (loop.isCondition()) {
            parts.add("condition");
        }
        if (loop.isUpdate()) {
            parts.add("update");
        }
        return parts.isEmpty() ? "(empty)" : "(" + String.join(", ", parts) + ")";
    }

    /** Private constructor so that this class is not instantiated. */
    private SyntaxTreePrinter() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
