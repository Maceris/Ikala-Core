package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.Block;
import com.ikalagaming.scripting.ast.Call;
import com.ikalagaming.scripting.ast.Cast;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.ConstBool;
import com.ikalagaming.scripting.ast.ConstChar;
import com.ikalagaming.scripting.ast.ConstDouble;
import com.ikalagaming.scripting.ast.ConstInt;
import com.ikalagaming.scripting.ast.EmptyStatement;
import com.ikalagaming.scripting.ast.ExprArithmetic;
import com.ikalagaming.scripting.ast.ExprAssign;
import com.ikalagaming.scripting.ast.ExprLogic;
import com.ikalagaming.scripting.ast.ExprRelation;
import com.ikalagaming.scripting.ast.ExprTernary;
import com.ikalagaming.scripting.ast.Identifier;
import com.ikalagaming.scripting.ast.Node;
import com.ikalagaming.scripting.ast.StatementList;
import com.ikalagaming.scripting.ast.SwitchBlockGroup;
import com.ikalagaming.scripting.ast.Type;
import com.ikalagaming.scripting.ast.Type.Base;
import com.ikalagaming.scripting.ast.VarDeclaration;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.extern.slf4j.Slf4j;

/**
 * Optimize some of the tree.
 *
 * @author Ches Burks
 */
@Slf4j
public class OptimizationPass implements ASTVisitor {

    /** Used for localization. */
    private static final String INVALID_CONSTANT = "INVALID_CONSTANT";

    /** Used for localization. */
    private static final String INVALID_OPERATOR = "INVALID_OPERATOR";

    /**
     * Return a constant boolean based on the value.
     *
     * @param value The value of the boolean.
     * @return A node representing said value.
     */
    private ConstBool getBool(boolean value) {
        ConstBool result = new ConstBool();
        result.setValue(value);
        result.setType(Type.primitive(Base.BOOLEAN));
        return result;
    }

    /**
     * Check if a node is an integer or character division or modulo by a constant zero.
     *
     * @param node The arithmetic node.
     * @param divisor The second child of the node.
     * @return True if folding the node would throw an exception.
     */
    private boolean isIntegerDivisionByZero(ExprArithmetic node, Node divisor) {
        if (!node.getType().anyOf(Base.INT, Base.CHAR)
                || !(ExprArithmetic.Operator.DIV.equals(node.getOperator())
                        || ExprArithmetic.Operator.MOD.equals(node.getOperator()))) {
            return false;
        }
        return (divisor instanceof ConstInt constInt && constInt.getValue() == 0)
                || (divisor instanceof ConstChar constChar && constChar.getValue() == 0)
                || (divisor instanceof ConstDouble constDouble
                        && (int) constDouble.getValue() == 0);
    }

    /**
     * Check if evaluating a node definitely has no side effects, so it is safe to remove.
     *
     * @param node The node to check.
     * @return True if we know there are no side effects, false if there might be.
     */
    private boolean isFreeOfSideEffects(Node node) {
        if (node instanceof Call || node instanceof ExprAssign) {
            return false;
        }
        if (node instanceof ExprArithmetic arithmetic) {
            switch (arithmetic.getOperator()) {
                case DEC_PREFIX, DEC_SUFFIX, INC_PREFIX, INC_SUFFIX:
                    return false;
                default:
                    break;
            }
        }
        for (Node child : node.getChildren()) {
            if (!isFreeOfSideEffects(child)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Optimize the syntax tree.
     *
     * @param ast The tree to validate.
     */
    public void optimize(CompilationUnit ast) {
        processTree(ast);
    }

    /**
     * Process the tree recursively.
     *
     * @param node The node we are processing.
     */
    private void processTree(Node node) {
        if (node instanceof Block
                || node instanceof CompilationUnit
                || node instanceof SwitchBlockGroup) {
            /*
             * Only remove empty statements from lists of statements. Things like if statements and
             * loops expect a body in a specific position, even if it's empty.
             */
            node.getChildren().removeIf(EmptyStatement.class::isInstance);
        }
        for (int i = 0; i < node.getChildren().size(); ++i) {
            Node child = node.getChildren().get(i);
            processTree(child);
            if (child instanceof ExprArithmetic expr) {
                /*
                 * We might need to replace this node with a constant after
                 * processing the children. This will recursively calculate
                 * constant expressions and allows final variable replacements.
                 */
                // replace the node
                node.getChildren().set(i, this.simplify(expr));
            } else if (child instanceof StatementList && (child.getChildren().size() == 1)) {
                /*
                 * If a statement list has only 1 child, we replace the list
                 * with its child. Essentially removing pointless nesting.
                 */
                node.getChildren().set(i, child.getChildren().get(0));
            } else if (child instanceof ExprLogic expr) {
                node.getChildren().set(i, this.simplify(expr));
            } else if (child instanceof ExprRelation expr) {
                node.getChildren().set(i, this.simplify(expr));
            }
        }
        node.process(this);
    }

    /**
     * Simplify an arithmetic expression, into a constant if its children are also constants.
     *
     * @param node The node we are simplifying.
     * @return The resulting node, which might be the same node if it's not a constant expression.
     */
    private Node simplify(ExprArithmetic node) {
        if (node.getChildren().size() == 1) {
            return node;
        }

        final Node firstChild = node.getChildren().get(0);
        final Node secondChild = node.getChildren().get(1);

        if (node.getType().anyOf(Base.UNKNOWN)) {
            // We can't cast to a type we don't know yet, the runtime will figure it out
            return node;
        }

        // Implicit casts
        boolean addedCast = false;
        if (!firstChild.getType().getBase().equals(node.getType().getBase())) {
            Cast cast = new Cast();
            cast.setType(node.getType());
            cast.addChild(firstChild);
            node.getChildren().set(0, cast);
            addedCast = true;
        }
        if (!secondChild.getType().getBase().equals(node.getType().getBase())) {
            Cast cast = new Cast();
            cast.setType(node.getType());
            cast.addChild(secondChild);
            node.getChildren().set(1, cast);
            addedCast = true;
        }
        if (addedCast) {
            return node;
        }

        // Constant optimizations
        if (firstChild.getType().anyOf(Base.UNKNOWN)
                || secondChild.getType().anyOf(Base.UNKNOWN)
                || !((firstChild instanceof ConstInt
                                || firstChild instanceof ConstDouble
                                || firstChild instanceof ConstChar)
                        && (secondChild instanceof ConstInt
                                || secondChild instanceof ConstDouble
                                || secondChild instanceof ConstChar))) {
            // Bail if it's not the case that both children are constants
            return node;
        }

        if (isIntegerDivisionByZero(node, secondChild)) {
            // Leave it for the runtime to report, rather than crashing the compiler
            return node;
        }

        if (node.getType().anyOf(Base.INT)) {
            return simplifyInt(node);
        }
        if (node.getType().anyOf(Base.DOUBLE)) {
            return simplifyDouble(node);
        }
        if (node.getType().anyOf(Base.CHAR)) {
            return simplifyChar(node);
        }

        return node;
    }

    /**
     * Simplify a logical expression if we can.
     *
     * @param node The node we are processing.
     * @return The resulting node, which might be the same node.
     */
    private Node simplify(ExprLogic node) {
        if (ExprLogic.Operator.NOT.equals(node.getOperator())) {
            if (node.getChildren().get(0) instanceof ConstBool bool) {
                return getBool(!bool.isValue());
            }
            return node;
        }
        if (node.getChildren().size() < 2) {
            return node;
        }

        final Node left = node.getChildren().get(0);
        final Node right = node.getChildren().get(1);

        if (ExprLogic.Operator.AND.equals(node.getOperator())) {
            return simplifyAnd(node, left, right);
        } else if (ExprLogic.Operator.OR.equals(node.getOperator())) {
            return simplifyOr(node, left, right);
        }
        return node;
    }

    /**
     * Simplify a relation expression if we can. Comparing a variable with itself, and comparing
     * constants of the same type.
     *
     * @param node The node we are processing.
     * @return The resulting node, which might be the same node.
     */
    private Node simplify(ExprRelation node) {

        final Node firstChild = node.getChildren().get(0);
        final Node secondChild = node.getChildren().get(1);

        if (firstChild instanceof Identifier firstID
                && secondChild instanceof Identifier secondID) {
            // Doubles can be NaN, which is not equal to itself
            if (firstID.getName().equals(secondID.getName())
                    && firstID.getType().anyOf(Base.CHAR, Base.INT)) {
                switch (node.getOperator()) {
                    case GT, LT:
                        return getBool(false);
                    case GTE, LTE:
                        return getBool(true);
                    default:
                        return node;
                }
            }
            return node;
        }

        double comparison = Double.NaN;

        if (firstChild instanceof ConstChar firstChar
                && secondChild instanceof ConstChar secondChar) {
            comparison = ((double) firstChar.getValue()) - secondChar.getValue();
        } else if (firstChild instanceof ConstInt firstInt
                && secondChild instanceof ConstInt secondInt) {
            comparison = ((double) firstInt.getValue()) - secondInt.getValue();
        } else if (firstChild instanceof ConstDouble firstDouble
                && secondChild instanceof ConstDouble secondDouble) {
            comparison = firstDouble.getValue() - secondDouble.getValue();
        }

        if (Double.isNaN(comparison)) {
            return node;
        }

        switch (node.getOperator()) {
            case GT:
                return getBool(comparison > 0);
            case GTE:
                return getBool(comparison >= 0);
            case LT:
                return getBool(comparison < 0);
            case LTE:
                return getBool(comparison <= 0);
        }
        return node;
    }

    /**
     * Simplify an and expression. Removes pointless logic, and simplifies to a constant if
     * possible.
     *
     * @param node The logic node we are working on.
     * @param leftChild The left child.
     * @param rightChild The right child.
     * @return The node that should be in the place of the node parameter, which may just be the
     *     node unaltered.
     */
    private Node simplifyAnd(ExprLogic node, Node leftChild, Node rightChild) {
        if (leftChild instanceof ConstBool left && (!left.isValue())
                || rightChild instanceof ConstBool right
                        && (!right.isValue())
                        && isFreeOfSideEffects(leftChild)) {
            /*
             * If either side of an && is false, the result is always false. The left side is
             * always evaluated though, so we can only drop it if doing so changes nothing.
             */
            return getBool(false);
        }
        if (leftChild instanceof ConstBool bool && (bool.isValue())) {
            // true && ____
            return rightChild;
        }
        if (rightChild instanceof ConstBool bool && (bool.isValue())) {
            // ____ && true
            return leftChild;
        }
        return node;
    }

    /**
     * Simplify a char expression.
     *
     * @param node The node we are simplifying.
     * @return The resulting value.
     */
    private Node simplifyChar(ExprArithmetic node) {
        final Node firstChild = node.getChildren().get(0);
        final Node secondChild = node.getChildren().get(1);

        char firstValue;
        char secondValue;

        if (firstChild instanceof ConstInt convertedFirst) {
            int value = convertedFirst.getValue();
            // Good luck
            firstValue = (char) value;
        } else if (firstChild instanceof ConstDouble convertedFirst) {
            double value = convertedFirst.getValue();
            // Good luck
            firstValue = (char) value;
        } else if (firstChild instanceof ConstChar convertedFirst) {
            char value = convertedFirst.getValue();
            // Already a char
            firstValue = value;
        } else {
            log.warn(
                    SafeResourceLoader.getString(
                            OptimizationPass.INVALID_CONSTANT, ScriptManager.getResourceBundle()),
                    firstChild.toString());
            return node;
        }

        if (secondChild instanceof ConstInt convertedSecond) {
            int value = convertedSecond.getValue();
            // Good luck
            secondValue = (char) value;
        } else if (secondChild instanceof ConstDouble convertedSecond) {
            double value = convertedSecond.getValue();
            // Good luck
            secondValue = (char) value;
        } else if (secondChild instanceof ConstChar convertedSecond) {
            char value = convertedSecond.getValue();
            // Already a char
            secondValue = value;
        } else {
            log.warn(
                    SafeResourceLoader.getString(
                            OptimizationPass.INVALID_CONSTANT, ScriptManager.getResourceBundle()),
                    secondChild.toString());
            return node;
        }

        ConstChar result = new ConstChar();
        result.setType(Type.primitive(Base.CHAR));

        switch (node.getOperator()) {
            case ADD:
                result.setValue((char) (firstValue + secondValue));
                break;
            case DIV:
                result.setValue((char) (firstValue / secondValue));
                break;
            case MOD:
                result.setValue((char) (firstValue % secondValue));
                break;
            case MUL:
                result.setValue((char) (firstValue * secondValue));
                break;
            case SUB:
                result.setValue((char) (firstValue - secondValue));
                break;
            case DEC_PREFIX, DEC_SUFFIX, INC_PREFIX, INC_SUFFIX:
            default:
                // Can't happen, but let's cover it anyway
                log.warn(
                        SafeResourceLoader.getString(
                                OptimizationPass.INVALID_OPERATOR,
                                ScriptManager.getResourceBundle()),
                        node.getOperator().toString(),
                        firstChild.getClass().getSimpleName());
                return node;
        }

        return result;
    }

    /**
     * Simplify a double expression.
     *
     * @param node The node we are simplifying.
     * @return The resulting value.
     */
    private Node simplifyDouble(ExprArithmetic node) {
        final Node firstChild = node.getChildren().get(0);
        final Node secondChild = node.getChildren().get(1);

        double firstValue;
        double secondValue;

        if (firstChild instanceof ConstInt convertedFirst) {
            int value = convertedFirst.getValue();
            // Fits fine
            firstValue = value;
        } else if (firstChild instanceof ConstDouble convertedFirst) {
            double value = convertedFirst.getValue();
            // Already a double
            firstValue = value;
        } else if (firstChild instanceof ConstChar convertedFirst) {
            char value = convertedFirst.getValue();
            // Fits fine
            firstValue = value;
        } else {
            log.warn(
                    SafeResourceLoader.getString(
                            OptimizationPass.INVALID_CONSTANT, ScriptManager.getResourceBundle()),
                    firstChild.toString());
            return node;
        }

        if (secondChild instanceof ConstInt convertedSecond) {
            int value = convertedSecond.getValue();
            // Fits fine
            secondValue = value;
        } else if (secondChild instanceof ConstDouble convertedSecond) {
            double value = convertedSecond.getValue();
            // Already a double
            secondValue = value;
        } else if (secondChild instanceof ConstChar convertedSecond) {
            char value = convertedSecond.getValue();
            // Fits fine
            secondValue = value;
        } else {
            log.warn(
                    SafeResourceLoader.getString(
                            OptimizationPass.INVALID_CONSTANT, ScriptManager.getResourceBundle()),
                    secondChild.toString());
            return node;
        }

        ConstDouble result = new ConstDouble();
        result.setType(Type.primitive(Base.DOUBLE));

        switch (node.getOperator()) {
            case ADD:
                result.setValue(firstValue + secondValue);
                break;
            case DIV:
                result.setValue(firstValue / secondValue);
                break;
            case MOD:
                result.setValue(firstValue % secondValue);
                break;
            case MUL:
                result.setValue(firstValue * secondValue);
                break;
            case SUB:
                result.setValue(firstValue - secondValue);
                break;
            case DEC_PREFIX, DEC_SUFFIX, INC_PREFIX, INC_SUFFIX:
            default:
                // Can't happen, but let's cover it anyway
                log.warn(
                        SafeResourceLoader.getString(
                                OptimizationPass.INVALID_OPERATOR,
                                ScriptManager.getResourceBundle()),
                        node.getOperator().toString(),
                        firstChild.getClass().getSimpleName());
                return node;
        }

        return result;
    }

    /**
     * Simplify an integer expression.
     *
     * @param node The node we are simplifying.
     * @return The resulting value.
     */
    private Node simplifyInt(ExprArithmetic node) {
        final Node firstChild = node.getChildren().get(0);
        final Node secondChild = node.getChildren().get(1);

        int firstValue;
        int secondValue;

        if (firstChild instanceof ConstInt convertedFirst) {
            int value = convertedFirst.getValue();
            // Already an int
            firstValue = value;
        } else if (firstChild instanceof ConstDouble convertedFirst) {
            double value = convertedFirst.getValue();
            // Truncate
            firstValue = (int) value;
        } else if (firstChild instanceof ConstChar convertedFirst) {
            char value = convertedFirst.getValue();
            // Fits fine
            firstValue = value;
        } else {
            log.warn(
                    SafeResourceLoader.getString(
                            OptimizationPass.INVALID_CONSTANT, ScriptManager.getResourceBundle()),
                    firstChild.toString());
            return node;
        }

        if (secondChild instanceof ConstInt convertedSecond) {
            int value = convertedSecond.getValue();
            // Already an int
            secondValue = value;
        } else if (secondChild instanceof ConstDouble convertedSecond) {
            double value = convertedSecond.getValue();
            // Truncate
            secondValue = (int) value;
        } else if (secondChild instanceof ConstChar convertedSecond) {
            char value = convertedSecond.getValue();
            // Fits fine
            secondValue = value;
        } else {
            log.warn(
                    SafeResourceLoader.getString(
                            OptimizationPass.INVALID_CONSTANT, ScriptManager.getResourceBundle()),
                    secondChild.toString());
            return node;
        }

        ConstInt result = new ConstInt();
        result.setType(Type.primitive(Base.INT));

        switch (node.getOperator()) {
            case ADD:
                result.setValue(firstValue + secondValue);
                break;
            case DIV:
                result.setValue(firstValue / secondValue);
                break;
            case MOD:
                result.setValue(firstValue % secondValue);
                break;
            case MUL:
                result.setValue(firstValue * secondValue);
                break;
            case SUB:
                result.setValue(firstValue - secondValue);
                break;
            case DEC_PREFIX, DEC_SUFFIX, INC_PREFIX, INC_SUFFIX:
            default:
                // Can't happen, but let's cover it anyway
                log.warn(
                        SafeResourceLoader.getString(
                                OptimizationPass.INVALID_OPERATOR,
                                ScriptManager.getResourceBundle()),
                        node.getOperator().toString(),
                        firstChild.getClass().getSimpleName());
                return node;
        }

        return result;
    }

    /**
     * Simplify an or expression. Removes pointless logic, and simplifies to a constant if possible.
     *
     * @param node The logic node we are working on.
     * @param leftChild The left child.
     * @param rightChild The right child.
     * @return The node that should be in the place of the node parameter, which may just be the
     *     node unaltered.
     */
    private Node simplifyOr(ExprLogic node, Node leftChild, Node rightChild) {
        if (leftChild instanceof ConstBool left && (left.isValue())
                || rightChild instanceof ConstBool right
                        && (right.isValue())
                        && isFreeOfSideEffects(leftChild)) {
            /*
             * If either side of an || is true, the result is always true. The left side is always
             * evaluated though, so we can only drop it if doing so changes nothing.
             */
            return getBool(true);
        }
        if (leftChild instanceof ConstBool bool && (!bool.isValue())) {
            // false || ____
            return rightChild;
        }
        if (rightChild instanceof ConstBool bool && (!bool.isValue())) {
            // ____ || false
            return leftChild;
        }
        return node;
    }

    @Override
    public void visit(ExprTernary node) {
        if (!node.getType().anyOf(Base.CHAR, Base.DOUBLE, Base.INT)) {
            return;
        }

        if (node.getType().anyOf(Base.CHAR, Base.INT, Base.DOUBLE)) {
            final Node firstChild = node.getChildren().get(1);
            final Node secondChild = node.getChildren().get(2);

            if (!firstChild.getType().getBase().equals(node.getType().getBase())) {

                Cast cast = new Cast();
                cast.setType(node.getType());
                cast.addChild(firstChild);
                node.getChildren().set(1, cast);
            } else if (!secondChild.getType().getBase().equals(node.getType().getBase())) {

                Cast cast = new Cast();
                cast.setType(node.getType());
                cast.addChild(secondChild);
                node.getChildren().set(2, cast);
            }
            // All the same type
        }
    }

    /** Add in automatic casts to larger numerical types. */
    @Override
    public void visit(ExprAssign node) {
        if (node.getOperator() != ExprAssign.Operator.ASSIGN) {
            // The math instructions handle mixed types
            return;
        }
        widenIfNeeded(node);
    }

    /** Add in automatic casts to larger numerical types. */
    @Override
    public void visit(VarDeclaration node) {
        widenIfNeeded(node);
    }

    /**
     * Add in automatic casts to larger numerical types, for a node where the first child is the
     * variable and the second is the value being stored in it.
     *
     * @param node The declaration or assignment.
     */
    private void widenIfNeeded(Node node) {
        if (node.getChildren().size() < 2) {
            return;
        }
        final Node firstChild = node.getChildren().get(0);
        final Node secondChild = node.getChildren().get(1);

        if (firstChild.getType().getDimensions() > 0 || secondChild.getType().getDimensions() > 0) {
            return;
        }

        if ((firstChild.getType().anyOf(Base.INT) && secondChild.getType().anyOf(Base.CHAR))
                || (firstChild.getType().anyOf(Base.DOUBLE)
                        && secondChild.getType().anyOf(Base.CHAR, Base.INT))) {
            Cast cast = new Cast();
            cast.setType(firstChild.getType());
            cast.addChild(secondChild);
            node.getChildren().set(1, cast);
        }
    }
}
