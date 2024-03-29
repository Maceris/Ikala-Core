package com.ikalagaming.scripting.interpreter;

import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.ArgumentList;
import com.ikalagaming.scripting.ast.Break;
import com.ikalagaming.scripting.ast.Call;
import com.ikalagaming.scripting.ast.Cast;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.ConstBool;
import com.ikalagaming.scripting.ast.ConstChar;
import com.ikalagaming.scripting.ast.ConstDouble;
import com.ikalagaming.scripting.ast.ConstInt;
import com.ikalagaming.scripting.ast.ConstNull;
import com.ikalagaming.scripting.ast.ConstString;
import com.ikalagaming.scripting.ast.Continue;
import com.ikalagaming.scripting.ast.DoWhile;
import com.ikalagaming.scripting.ast.Exit;
import com.ikalagaming.scripting.ast.ExprArithmetic;
import com.ikalagaming.scripting.ast.ExprArithmetic.Operator;
import com.ikalagaming.scripting.ast.ExprAssign;
import com.ikalagaming.scripting.ast.ExprEquality;
import com.ikalagaming.scripting.ast.ExprLogic;
import com.ikalagaming.scripting.ast.ExprRelation;
import com.ikalagaming.scripting.ast.ExprTernary;
import com.ikalagaming.scripting.ast.ForLoop;
import com.ikalagaming.scripting.ast.Goto;
import com.ikalagaming.scripting.ast.Identifier;
import com.ikalagaming.scripting.ast.If;
import com.ikalagaming.scripting.ast.Label;
import com.ikalagaming.scripting.ast.Node;
import com.ikalagaming.scripting.ast.SwitchBlockGroup;
import com.ikalagaming.scripting.ast.SwitchLabel;
import com.ikalagaming.scripting.ast.SwitchStatement;
import com.ikalagaming.scripting.ast.Type.Base;
import com.ikalagaming.scripting.ast.VarDeclaration;
import com.ikalagaming.scripting.ast.While;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for transforming an abstract syntax tree into instructions.
 *
 * @author Ches Burks
 */
@Slf4j
public class InstructionGenerator implements ASTVisitor {

    /** Where we need to jump to in order to break out of the current, most proximal, loop. */
    private String breakLabel;

    /**
     * Where we need to jump to in order to continue with the next iteration of the current, most
     * proximal, loop.
     */
    private String continueLabel;

    /** Used for generating unique label names. */
    private int nextLabel = 0;

    /** Used for generating unique temp variable names. */
    private int nextVariable = 0;

    /**
     * A temporary list of instructions. We include a bunch of labels, including temporary ones used
     * for loops and conditionals.
     */
    private List<Instruction> tempInstructions;

    /**
     * Calculate and emit a jump based on the opposite expression provided. This does not emit the
     * expression itself, only calculates which jump expression is appropriate based on what we
     * predict the expression to emit.
     *
     * <p>We do this because comparison and jumps are separate instructions, emitted by different
     * visitors, but we need them to cooperate.
     *
     * <p>This specific method exists because if statements need to jump to the else clause or after
     * the if, but fall through to the if.
     *
     * @param expression Any kind of expression according to the grammar, but must evaluate to a
     *     boolean.
     * @param targetLabel Where we are jumping to <i>unless</i> the condition is true.
     * @see #calculateJump(Node, String)
     */
    private void calculateInvertedJump(@NonNull Node expression, @NonNull String targetLabel) {
        /*
         * These are arranged in descending order of how likely I think they are
         * to show up in practice, because I have to pick an order so I may as
         * well try to minimize the expected number of checks.
         */
        if (expression instanceof ExprRelation relation) {
            InstructionType type;
            switch (relation.getOperator()) {
                case GT:
                    type = InstructionType.JLE;
                    break;
                case GTE:
                    type = InstructionType.JLT;
                    break;
                case LT:
                    type = InstructionType.JGE;
                    break;
                case LTE:
                    type = InstructionType.JGT;
                    break;
                default:
                    type = InstructionType.NOP;
                    break;
            }
            emitJump(type, targetLabel);
            return;
        }
        if (expression instanceof ExprEquality equality) {
            InstructionType type;
            switch (equality.getOperator()) {
                case EQUAL:
                    type = InstructionType.JNE;
                    break;
                case NOT_EQUAL:
                    type = InstructionType.JEQ;
                    break;
                default:
                    type = InstructionType.NOP;
                    break;
            }
            emitJump(type, targetLabel);
            return;
        }
        if (expression instanceof ExprLogic
                || expression instanceof ExprAssign
                || expression instanceof ExprTernary
                || expression instanceof Identifier) {
            // Boolean values

            // Convert the resulting boolean value to a flag, clean the stack
            tempInstructions.add(
                    new Instruction(
                            InstructionType.CMP,
                            new MemLocation(MemArea.STACK, Boolean.class),
                            new MemLocation(MemArea.IMMEDIATE, Boolean.class, true),
                            null));

            emitJump(InstructionType.JNE, targetLabel);
            return;
        }
        log.warn(
                SafeResourceLoader.getString(
                        "UNEXPECTED_EXPRESSION", ScriptManager.getResourceBundle()),
                expression.toString());
    }

    /**
     * Calculate and emit a jump based on the opposite expression provided. This does not emit the
     * expression itself, only calculates which jump expression is appropriate based on what we
     * predict the expression to emit.
     *
     * <p>We do this because comparison and jumps are separate instructions, emitted by different
     * visitors, but we need them to cooperate.
     *
     * @param expression Any kind of expression according to the grammar, but must evaluate to a
     *     boolean.
     * @param targetLabel Where we are jumping to.
     * @see #calculateInvertedJump(Node, String)
     */
    private void calculateJump(@NonNull Node expression, @NonNull String targetLabel) {
        /*
         * These are arranged in descending order of how likely I think they are
         * to show up in practice, because I have to pick an order so I may as
         * well try to minimize the expected number of checks.
         */
        if (expression instanceof ExprRelation relation) {
            InstructionType type;
            switch (relation.getOperator()) {
                case GT:
                    type = InstructionType.JGT;
                    break;
                case GTE:
                    type = InstructionType.JGE;
                    break;
                case LT:
                    type = InstructionType.JLT;
                    break;
                case LTE:
                    type = InstructionType.JLE;
                    break;
                default:
                    type = InstructionType.JMP;
                    break;
            }
            emitJump(type, targetLabel);
            return;
        }
        if (expression instanceof ExprEquality equality) {
            InstructionType type;
            switch (equality.getOperator()) {
                case EQUAL:
                    type = InstructionType.JEQ;
                    break;
                case NOT_EQUAL:
                    type = InstructionType.JNE;
                    break;
                default:
                    type = InstructionType.JMP;
                    break;
            }
            emitJump(type, targetLabel);
            return;
        }
        if (expression instanceof ExprLogic
                || expression instanceof ExprAssign
                || expression instanceof ExprTernary) {
            // Boolean values

            // Convert the resulting boolean value to a flag, clean the stack
            tempInstructions.add(
                    new Instruction(
                            InstructionType.CMP,
                            new MemLocation(MemArea.STACK, Boolean.class),
                            new MemLocation(MemArea.IMMEDIATE, Boolean.class, true),
                            null));

            emitJump(InstructionType.JEQ, targetLabel);
            return;
        }
        log.warn(
                SafeResourceLoader.getString(
                        "UNEXPECTED_EXPRESSION", ScriptManager.getResourceBundle()),
                expression.toString());
    }

    /**
     * Calculate where the value will be found for the given node, as part of an expression.
     *
     * @param node The node we are interested in.
     * @param clazz The class of the memory location.
     * @return The location where the contents would be found.
     */
    private MemLocation calculateLocation(@NonNull Node node, Class<?> clazz) {
        if (node instanceof ExprArithmetic) {
            return new MemLocation(MemArea.STACK, clazz);
        }
        if (node instanceof ConstBool boolNode) {
            return new MemLocation(MemArea.IMMEDIATE, clazz, boolNode.isValue());
        }
        if (node instanceof ConstChar charNode) {
            return new MemLocation(MemArea.IMMEDIATE, clazz, charNode.getValue());
        }
        if (node instanceof ConstDouble doubleNode) {
            return new MemLocation(MemArea.IMMEDIATE, clazz, doubleNode.getValue());
        }
        if (node instanceof ConstInt intNode) {
            return new MemLocation(MemArea.IMMEDIATE, clazz, intNode.getValue());
        }
        if (node instanceof ConstString stringNode) {
            return new MemLocation(MemArea.IMMEDIATE, clazz, stringNode.getValue());
        }
        if (node instanceof Identifier identifier) {
            return new MemLocation(MemArea.VARIABLE, clazz, identifier.getName());
        }
        log.warn(
                SafeResourceLoader.getString(
                        "UNHANDLED_EXPRESSION_MEMBER", ScriptManager.getResourceBundle()),
                node.toString());
        return new MemLocation(MemArea.STACK, clazz);
    }

    /**
     * Check if a loop contains a break without a label name. We will need a label to break to after
     * the loop in that case.
     *
     * @param loop The loop we are interested in.
     * @return Whether there is a generic break in that loop.
     */
    private boolean containsBreak(@NonNull Node loop) {
        for (Node child : loop.getChildren()) {
            if (child instanceof Break br) {
                return br.getChildren().isEmpty();
            }
            // recurse
            if (containsBreak(child)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Emit a temporary jump instruction. The type should be one of the jump types.
     *
     * @param type The instruction type, one of the jumps.
     * @param target The name of the target label.
     */
    private void emitJump(@NonNull final InstructionType type, @NonNull final String target) {
        tempInstructions.add(
                new Instruction(
                        type,
                        new MemLocation(MemArea.IMMEDIATE, String.class, target),
                        null,
                        null));
    }

    /**
     * Emit a label, in a standardized form.
     *
     * @param label The name of the label.
     * @see #getNextLabelName()
     */
    private void emitLabel(@NonNull final String label) {
        tempInstructions.add(
                new Instruction(
                        InstructionType.NOP,
                        new MemLocation(MemArea.IMMEDIATE, String.class, label),
                        null,
                        null));
    }

    /**
     * Generates and emits the jump table for a switch statement. It returns the label that
     * corresponds to the default case, if it exists.
     *
     * @param body The body of the switch statement.
     * @param targetTable Where expressions and pseudo-label nodes will be stored. This will later
     *     be used to generate actual labels and expressions to jump to.
     * @param expressionResult The name of the variable that contains the results of the switch
     *     expression.
     * @return The name of the label that the default case jumps to, will be null if there is no
     *     default.
     */
    private String generateSwitchJumpTable(
            @NonNull Node body, @NonNull List<Node> targetTable, @NonNull String expressionResult) {
        String defaultLabel = null;
        // statement groups are first, then loose switch labels
        for (Node child : body.getChildren()) {
            if (!(child instanceof SwitchBlockGroup)) {
                continue;
            }
            final String sharedLabel = getNextLabelName();

            // Just used to store the label name in the target table
            Label fakeLabel = new Label();
            fakeLabel.setName(sharedLabel);
            targetTable.add(fakeLabel);

            for (Node subChild : child.getChildren()) {
                if (subChild instanceof SwitchLabel label) {
                    if (label.isDefault()) {
                        defaultLabel = sharedLabel;
                    } else {
                        Node labelExpression = label.getChildren().get(0);
                        processTree(labelExpression);
                        tempInstructions.add(
                                new Instruction(
                                        InstructionType.CMP,
                                        new MemLocation(
                                                MemArea.STACK,
                                                labelExpression
                                                        .getType()
                                                        .getBase()
                                                        .getCorrespondingClass()),
                                        new MemLocation(
                                                MemArea.VARIABLE, String.class, expressionResult),
                                        null));
                        emitJump(InstructionType.JEQ, sharedLabel);
                    }
                } else {
                    // block statement
                    targetTable.add(subChild);
                }
            }
        }
        return defaultLabel;
    }

    /**
     * Return the next label name, and update the value for the next call. These are not valid
     * labels according to the grammar, so there should be no conflicts.
     *
     * @return The name for the next valid label.
     */
    private String getNextLabelName() {
        return String.format(".L%d", nextLabel++);
    }

    /**
     * Return the next temporary variable name, and update the value for the next call. These are
     * not valid variables according to the grammar, so there should be no conflicts.
     *
     * @return The name for the next valid variable name.
     */
    private String getNextVariableName() {
        return String.format("$%d", nextVariable++);
    }

    /**
     * Calculate which numeric class best represents the math that will be done involving a node,
     * based on the base of the type of that node.
     *
     * @param node The nod ewe are interested in.
     * @return The class that best represents that node's base type.
     */
    private Class<?> getNumericBase(@NonNull Node node) {
        switch (node.getType().getBase()) {
            case CHAR, DOUBLE, INT, STRING:
                return node.getType().getBase().getCorrespondingClass();
            case UNKNOWN:
                // We assume it's an integer, as that's most common
                return Integer.class;
            case BOOLEAN, IDENTIFIER, LABEL, VOID:
            default:
                // Fallback, but the tree verification should(tm) prevent this
                log.warn(
                        SafeResourceLoader.getString(
                                "INVALID_ARITHMETIC_TYPE", ScriptManager.getResourceBundle()),
                        node.getType().toString());
                return Integer.class;
        }
    }

    /**
     * Calculate the instruction type based on the operator.
     *
     * @param node The node we are interested in.
     * @return The type of instruction we are generating.
     */
    private InstructionType instructionType(@NonNull ExprArithmetic node) {
        if (node.getType().anyOf(Base.INT)) {
            return this.instructionTypeInt(node);
        }
        if (node.getType().anyOf(Base.DOUBLE)) {
            return this.instructionTypeDouble(node);
        }
        if (node.getType().anyOf(Base.CHAR)) {
            return this.instructionTypeChar(node);
        }

        // defaults
        switch (node.getOperator()) {
            case ADD:
                return InstructionType.CONCAT_STRING;
            case DEC_PREFIX, DEC_SUFFIX, DIV, INC_PREFIX, INC_SUFFIX, MOD, MUL, SUB:
            default:
                return InstructionType.NOP;
        }
    }

    /**
     * Calculates the type of instruction to use for assignment, since we can have shortcuts like
     * "+=", we can use different instructions for efficiency.
     *
     * @param operator The operator we want to use.
     * @param numericType The numerical type, assuming this is not a standard assignment. Might be
     *     null for normal assigns.
     * @return The instruction type to use.
     */
    private InstructionType instructionType(
            @NonNull ExprAssign.Operator operator, Class<?> numericType) {
        if (operator == ExprAssign.Operator.ASSIGN) {
            return InstructionType.MOV;
        }
        if (numericType == Integer.class) {
            return this.instructionTypeInt(operator);
        }
        if (numericType == Double.class) {
            return this.instructionTypeDouble(operator);
        }
        if (numericType == Character.class) {
            return this.instructionTypeChar(operator);
        }
        log.warn(
                SafeResourceLoader.getString(
                        "UNKNONW_ASSIGN_OPERATOR", ScriptManager.getResourceBundle()),
                operator.toString());
        return InstructionType.NOP;
    }

    /**
     * Calculate the instruction type based on the operator, with the knowledge that it deals with
     * characters.
     *
     * @param node The node we are interested in.
     * @return The type of instruction we are generating.
     */
    private InstructionType instructionTypeChar(ExprArithmetic node) {
        switch (node.getOperator()) {
            case ADD:
                return InstructionType.ADD_CHAR;
            case DEC_PREFIX, DEC_SUFFIX:
                return InstructionType.SUB_CHAR;
            case DIV:
                return InstructionType.DIV_CHAR;
            case INC_PREFIX, INC_SUFFIX:
                return InstructionType.ADD_CHAR;
            case MOD:
                return InstructionType.MOD_CHAR;
            case MUL:
                return InstructionType.MUL_CHAR;
            case SUB:
                if (node.getChildren().size() == 1) {
                    return InstructionType.NEG_CHAR;
                }
                return InstructionType.SUB_CHAR;
            default:
                return InstructionType.NOP;
        }
    }

    /**
     * Calculate the instruction type based on the operator, with the knowledge that it deals with
     * characters.
     *
     * @param operator The operator we want to use.
     * @return The type of instruction we are generating.
     */
    private InstructionType instructionTypeChar(ExprAssign.Operator operator) {
        switch (operator) {
            case ADD_ASSIGN:
                return InstructionType.ADD_CHAR;
            case DIV_ASSIGN:
                return InstructionType.DIV_CHAR;
            case MOD_ASSIGN:
                return InstructionType.MOD_CHAR;
            case MUL_ASSIGN:
                return InstructionType.MUL_CHAR;
            case SUB_ASSIGN:
                return InstructionType.SUB_CHAR;
            case ASSIGN:
            default:
                return InstructionType.NOP;
        }
    }

    /**
     * Calculate the instruction type based on the operator, with the knowledge that it deals with
     * doubles.
     *
     * @param node The node we are interested in.
     * @return The type of instruction we are generating.
     */
    private InstructionType instructionTypeDouble(ExprArithmetic node) {
        switch (node.getOperator()) {
            case ADD:
                return InstructionType.ADD_DOUBLE;
            case DEC_PREFIX, DEC_SUFFIX:
                return InstructionType.SUB_DOUBLE;
            case DIV:
                return InstructionType.DIV_DOUBLE;
            case INC_PREFIX, INC_SUFFIX:
                return InstructionType.ADD_DOUBLE;
            case MOD:
                return InstructionType.MOD_DOUBLE;
            case MUL:
                return InstructionType.MUL_DOUBLE;
            case SUB:
                if (node.getChildren().size() == 1) {
                    return InstructionType.NEG_DOUBLE;
                }
                return InstructionType.SUB_DOUBLE;
            default:
                return InstructionType.NOP;
        }
    }

    /**
     * Calculate the instruction type based on the operator, with the knowledge that it deals with
     * doubles.
     *
     * @param operator The operator we want to use.
     * @return The type of instruction we are generating.
     */
    private InstructionType instructionTypeDouble(ExprAssign.Operator operator) {
        switch (operator) {
            case ADD_ASSIGN:
                return InstructionType.ADD_DOUBLE;
            case DIV_ASSIGN:
                return InstructionType.DIV_DOUBLE;
            case MOD_ASSIGN:
                return InstructionType.MOD_DOUBLE;
            case MUL_ASSIGN:
                return InstructionType.MUL_DOUBLE;
            case SUB_ASSIGN:
                return InstructionType.SUB_DOUBLE;
            case ASSIGN:
            default:
                return InstructionType.NOP;
        }
    }

    /**
     * Calculate the instruction type based on the operator, with the knowledge that it deals with
     * integers.
     *
     * @param node The node we are interested in.
     * @return The type of instruction we are generating.
     */
    private InstructionType instructionTypeInt(ExprArithmetic node) {
        switch (node.getOperator()) {
            case ADD:
                return InstructionType.ADD_INT;
            case DEC_PREFIX, DEC_SUFFIX:
                return InstructionType.SUB_INT;
            case DIV:
                return InstructionType.DIV_INT;
            case INC_PREFIX, INC_SUFFIX:
                return InstructionType.ADD_INT;
            case MOD:
                return InstructionType.MOD_INT;
            case MUL:
                return InstructionType.MUL_INT;
            case SUB:
                if (node.getChildren().size() == 1) {
                    return InstructionType.NEG_INT;
                }
                return InstructionType.SUB_INT;
            default:
                return InstructionType.NOP;
        }
    }

    /**
     * Calculate the instruction type based on the operator, with the knowledge that it deals with
     * integers.
     *
     * @param operator The operator we want to use.
     * @return The type of instruction we are generating.
     */
    private InstructionType instructionTypeInt(ExprAssign.Operator operator) {
        switch (operator) {
            case ADD_ASSIGN:
                return InstructionType.ADD_INT;
            case DIV_ASSIGN:
                return InstructionType.DIV_INT;
            case MOD_ASSIGN:
                return InstructionType.MOD_INT;
            case MUL_ASSIGN:
                return InstructionType.MUL_INT;
            case SUB_ASSIGN:
                return InstructionType.SUB_INT;
            case ASSIGN:
            default:
                return InstructionType.NOP;
        }
    }

    /**
     * Handles {@link ExprEquality} and {@link ExprRelation}, since they use the exact same logic
     * due to a common {@link InstructionType#CMP} instruction.
     *
     * @param node The ExprEquality or ExprRelation node.
     */
    private void outputBooleanComparison(@NonNull Node node) {
        /*
         * We reverse the order of child parsing so the order here makes sense.
         */
        Node left = node.getChildren().get(0);
        Node right = node.getChildren().get(1);

        processBoolExpression(right);

        processBoolExpression(left);

        /*
         * We reverse the order of child parsing so the order here makes sense.
         */
        MemLocation first;
        if (left instanceof Identifier leftID) {
            first =
                    new MemLocation(
                            MemArea.VARIABLE,
                            left.getType().getBase().getCorrespondingClass(),
                            leftID.getName());
        } else {
            first =
                    new MemLocation(
                            MemArea.STACK, left.getType().getBase().getCorrespondingClass());
        }

        MemLocation second;
        if (right instanceof Identifier rightID) {
            second =
                    new MemLocation(
                            MemArea.VARIABLE,
                            left.getType().getBase().getCorrespondingClass(),
                            rightID.getName());
        } else {
            second =
                    new MemLocation(
                            MemArea.STACK, left.getType().getBase().getCorrespondingClass());
        }

        tempInstructions.add(new Instruction(InstructionType.CMP, first, second, null));
    }

    /**
     * Translate an abstract syntax tree to instructions.
     *
     * @param ast The tree to process.
     * @return The list of instructions corresponding to the tree.
     */
    public List<Instruction> process(@NonNull CompilationUnit ast) {
        tempInstructions = new LinkedList<>();
        processTree(ast);

        // generate temporary instructions

        return processJumps();
    }

    /**
     * Process an argument list's children in reverse order, pushing identifiers to the stack so
     * they can be passed to methods.
     *
     * @param node The argument list.
     */
    private void processArgumentList(Node node) {
        for (int i = node.getChildren().size() - 1; i >= 0; --i) {
            Node child = node.getChildren().get(i);
            if (child instanceof Identifier id) {
                // more variable special handling
                pushVarToStack(id);
            }
            processTree(child);
        }
    }

    /**
     * Process a boolean expression node, and push the results to the stack if it does not do that
     * by default.
     *
     * @param node The node to process.
     */
    private void processBoolExpression(@NonNull Node node) {
        processTree(node);
        if (node instanceof ExprRelation relation) {
            this.pushResultToStack(relation);
        } else if (node instanceof ExprEquality equality) {
            this.pushResultToStack(equality);
        }
    }

    /**
     * Process an arithmetic expression's children in reverse order, skipping identifiers and
     * constants since the handler deals with direct variable access.
     *
     * @param node The arithmetic node.
     */
    private void processExprArithmetic(Node node) {
        for (int i = node.getChildren().size() - 1; i >= 0; --i) {
            Node child = node.getChildren().get(i);
            if (child instanceof Identifier
                    || child instanceof ConstChar
                    || child instanceof ConstInt
                    || child instanceof ConstDouble
                    || child instanceof ConstString) {
                // Arithmetic expressions handle direct access
                continue;
            }
            processTree(child);
        }
    }

    private List<Instruction> processJumps() {
        List<Instruction> result = new ArrayList<>();

        // calculate jump locations for labels, conditionals, etc

        /** The number of the instruction each label actually refers to. */
        Map<String, Integer> labelLocations = new HashMap<>();
        /**
         * Stores labels we have passed, but not yet assigned a jump location to. We might have
         * multiple labels in a row between instructions.
         */
        List<String> currentLabels = new ArrayList<>();
        /** The actual instruction number we are on, ignoring labels. */
        int instructionCount = 0;
        for (Instruction i : tempInstructions) {
            if (i.type() == InstructionType.NOP) {
                // Label
                currentLabels.add((String) i.firstLocation().value());
                // Strip out labels by just not copying them over
                continue;
            }
            if (!currentLabels.isEmpty()) {
                for (String label : currentLabels) {
                    labelLocations.put(label, instructionCount);
                }
                currentLabels.clear();
            }
            result.add(i);
            ++instructionCount;
        }

        // Handle labels at the end of the program
        if (!currentLabels.isEmpty()) {
            for (String label : currentLabels) {
                labelLocations.put(label, instructionCount);
            }
            currentLabels.clear();
        }

        // Replace jumps label field with relative locations
        for (int i = 0; i < result.size(); ++i) {
            Instruction current = result.get(i);
            final InstructionType type = current.type();

            if (type == InstructionType.JEQ
                    || type == InstructionType.JNE
                    || type == InstructionType.JGE
                    || type == InstructionType.JGT
                    || type == InstructionType.JLE
                    || type == InstructionType.JLT
                    || type == InstructionType.JMP) {
                // Look up the jump target
                Instruction replacement =
                        new Instruction(
                                current.type(),
                                new MemLocation(
                                        MemArea.IMMEDIATE,
                                        Integer.class,
                                        labelLocations.get(current.firstLocation().value())),
                                null,
                                null);
                result.set(i, replacement);
            }
        }
        return result;
    }

    /**
     * Process a tree recursively, with special handling for some cases such as skipping children or
     * processing in reverse order.
     *
     * @param node The node to start processing from.
     */
    private void processTree(Node node) {
        if (shouldSkipChildren(node)) {
            /*
             * Skip processing children because the visitor handles processing
             * them, or they should be skipped.
             */
        } else if (node instanceof ExprArithmetic) {
            processExprArithmetic(node);
        } else if (node instanceof ArgumentList) {
            processArgumentList(node);
        } else {
            // Forward order
            for (Node child : node.getChildren()) {
                processTree(child);
            }
        }
        node.process(this);
    }

    /**
     * Calculate the SET instruction to use based on the type of equality expression, since the node
     * itself only outputs a CMP. This is used when we need to use the result in another expression.
     *
     * @param node The node that had a comparison.
     */
    private void pushResultToStack(ExprEquality node) {
        InstructionType type;
        switch (node.getOperator()) {
            case EQUAL:
                type = InstructionType.SET_EQ;
                break;
            case NOT_EQUAL:
                type = InstructionType.SET_NE;
                break;
            default:
                type = InstructionType.NOP;
                log.warn(
                        SafeResourceLoader.getString(
                                "UNKNOWN_EQUALITY_OPERATOR", ScriptManager.getResourceBundle()),
                        node.getOperator().toString());
                break;
        }
        tempInstructions.add(
                new Instruction(type, null, null, new MemLocation(MemArea.STACK, Boolean.class)));
    }

    /**
     * Calculate the SET instruction to use based on the type of relation, since the node itself
     * only outputs a CMP. This is used when we need to use the result in another expression.
     *
     * @param node The node that had a comparison.
     */
    private void pushResultToStack(ExprRelation node) {
        InstructionType type;
        switch (node.getOperator()) {
            case GT:
                type = InstructionType.SET_GT;
                break;
            case GTE:
                type = InstructionType.SET_GE;
                break;
            case LT:
                type = InstructionType.SET_LT;
                break;
            case LTE:
                type = InstructionType.SET_LE;
                break;
            default:
                type = InstructionType.NOP;
                log.warn(
                        SafeResourceLoader.getString(
                                "UNKNOWN_RELATIONAL_OPERATOR", ScriptManager.getResourceBundle()),
                        node.getOperator().toString());
                break;
        }
        tempInstructions.add(
                new Instruction(type, null, null, new MemLocation(MemArea.STACK, Boolean.class)));
    }

    /**
     * Push a variable to the stack based on the identifier node representing the variable.
     *
     * @param node The node that contains the name of the variable we need on the stack.
     */
    private void pushVarToStack(Identifier node) {
        tempInstructions.add(
                new Instruction(
                        InstructionType.MOV,
                        new MemLocation(MemArea.VARIABLE, String.class, node.getName()),
                        null,
                        new MemLocation(
                                MemArea.STACK, node.getType().getBase().getCorrespondingClass())));
    }

    /**
     * Check if we should skip processing of children while processing the tree, based on the type
     * of node.
     *
     * @param candidate The node we are looking at.
     * @return Whether we should process the children of this node.
     */
    private boolean shouldSkipChildren(Node candidate) {
        return candidate instanceof Call
                || candidate instanceof DoWhile
                || candidate instanceof ExprAssign
                || candidate instanceof ExprEquality
                || candidate instanceof ExprLogic
                || candidate instanceof ExprRelation
                || candidate instanceof ExprTernary
                || candidate instanceof ForLoop
                || candidate instanceof Goto
                || candidate instanceof If
                || candidate instanceof SwitchStatement
                || candidate instanceof VarDeclaration
                || candidate instanceof While;
    }

    /**
     * Handle creating the jump table and calculating targets for the jump table, for switch
     * statements.
     *
     * @param body The block that contains the statements.
     * @param targetTable Where we store the labels and expressions to emit later.
     * @param expressionResult The name of the variable where the expression result is stored.
     */
    private void switchBody(Node body, List<Node> targetTable, String expressionResult) {
        String defaultLabel = generateSwitchJumpTable(body, targetTable, expressionResult);
        /** Used for all trailing labels. */
        final String endLabel = getNextLabelName();
        for (Node child : body.getChildren()) {
            if (child instanceof SwitchLabel label) {
                if (label.isDefault()) {
                    defaultLabel = endLabel;
                } else {
                    Node labelExpression = label.getChildren().get(0);
                    processTree(labelExpression);
                    emitJump(InstructionType.JEQ, endLabel);
                }
            }
        }
        if (defaultLabel != null) {
            emitJump(InstructionType.JMP, defaultLabel);
        }
    }

    @Override
    public void visit(Break node) {
        emitJump(InstructionType.JMP, breakLabel);
    }

    @Override
    public void visit(Call node) {
        Node targetObject = node.getChildren().get(0);

        MemLocation object;
        MemLocation paramCount;
        Node params;

        if (node.isPrimary()) {
            Identifier name = (Identifier) node.getChildren().get(1);
            String methodName = name.getName();
            if (targetObject instanceof Identifier id) {
                pushVarToStack(id);
            } else {
                processTree(targetObject);
            }
            object = new MemLocation(MemArea.STACK, String.class, methodName);

            if (node.getChildren().size() > 2) {
                params = node.getChildren().get(2);
            } else {
                params = null;
            }
        } else {
            String methodName = ((Identifier) targetObject).getName();
            object = new MemLocation(MemArea.IMMEDIATE, String.class, methodName);

            if (node.getChildren().size() > 1) {
                params = node.getChildren().get(1);
            } else {
                params = null;
            }
        }

        if (params != null) {
            processTree(params);
        }

        paramCount =
                new MemLocation(
                        MemArea.IMMEDIATE,
                        Integer.class,
                        params == null ? 0 : params.getChildren().size());

        tempInstructions.add(
                new Instruction(
                        InstructionType.CALL,
                        object,
                        paramCount,
                        new MemLocation(MemArea.STACK, Void.class)));
    }

    @Override
    public void visit(Cast node) {
        Node expression = node.getChildren().get(0);

        MemArea area;
        Object value = null;
        if (expression instanceof Identifier id) {
            area = MemArea.VARIABLE;
            value = id.getName();
        } else {
            area = MemArea.STACK;
        }

        tempInstructions.add(
                new Instruction(
                        InstructionType.CAST,
                        new MemLocation(
                                area,
                                expression.getType().getBase().getCorrespondingClass(),
                                value),
                        null,
                        new MemLocation(
                                MemArea.STACK, node.getType().getBase().getCorrespondingClass())));
    }

    @Override
    public void visit(ConstBool node) {
        tempInstructions.add(
                new Instruction(
                        InstructionType.MOV,
                        new MemLocation(MemArea.IMMEDIATE, Boolean.class, node.isValue()),
                        null,
                        new MemLocation(MemArea.STACK, Boolean.class)));
    }

    @Override
    public void visit(ConstChar node) {
        tempInstructions.add(
                new Instruction(
                        InstructionType.MOV,
                        new MemLocation(MemArea.IMMEDIATE, Character.class, node.getValue()),
                        null,
                        new MemLocation(MemArea.STACK, Character.class)));
    }

    @Override
    public void visit(ConstDouble node) {
        tempInstructions.add(
                new Instruction(
                        InstructionType.MOV,
                        new MemLocation(MemArea.IMMEDIATE, Double.class, node.getValue()),
                        null,
                        new MemLocation(MemArea.STACK, Double.class)));
    }

    @Override
    public void visit(ConstInt node) {
        tempInstructions.add(
                new Instruction(
                        InstructionType.MOV,
                        new MemLocation(MemArea.IMMEDIATE, Integer.class, node.getValue()),
                        null,
                        new MemLocation(MemArea.STACK, Integer.class)));
    }

    @Override
    public void visit(ConstNull node) {
        tempInstructions.add(
                new Instruction(
                        InstructionType.MOV,
                        new MemLocation(MemArea.IMMEDIATE, Object.class, null),
                        null,
                        new MemLocation(MemArea.STACK, Object.class)));
    }

    @Override
    public void visit(ConstString node) {
        tempInstructions.add(
                new Instruction(
                        InstructionType.MOV,
                        new MemLocation(MemArea.IMMEDIATE, String.class, node.getValue()),
                        null,
                        new MemLocation(MemArea.STACK, String.class)));
    }

    @Override
    public void visit(Continue node) {
        emitJump(InstructionType.JMP, continueLabel);
    }

    @Override
    public void visit(DoWhile node) {
        Node body = node.getChildren().get(0);
        Node conditional = node.getChildren().get(1);

        final String topOfLoopLabel = getNextLabelName();
        final String conditionLabel = getNextLabelName();
        continueLabel = conditionLabel;

        final boolean containsBreak = containsBreak(body);
        if (containsBreak) {
            breakLabel = getNextLabelName();
        }

        emitLabel(topOfLoopLabel);

        processTree(body);

        processTree(conditional);
        calculateJump(conditional, topOfLoopLabel);

        if (containsBreak) {
            emitLabel(breakLabel);
        }
    }

    @Override
    public void visit(Exit node) {
        tempInstructions.add(new Instruction(InstructionType.HALT, null, null, null));
    }

    @Override
    public void visit(ExprArithmetic node) {
        Class<?> clazz = getNumericBase(node);

        InstructionType type = this.instructionType(node);

        MemLocation first = calculateLocation(node.getChildren().get(0), clazz);

        MemLocation second = null;
        switch (node.getOperator()) {
            case SUB:
                if (node.getChildren().size() == 1) {
                    // unary minus
                    break;
                }
                // fall through
            case ADD, DIV, MOD, MUL:
                second = calculateLocation(node.getChildren().get(1), clazz);
                break;
            case DEC_PREFIX, DEC_SUFFIX, INC_PREFIX, INC_SUFFIX:
                second = new MemLocation(MemArea.IMMEDIATE, clazz, node.getUnaryCount());
                break;
            default:
                break;
        }

        if (Operator.DEC_PREFIX.equals(node.getOperator())
                || Operator.INC_PREFIX.equals(node.getOperator())) {
            tempInstructions.add(new Instruction(type, first, second, first));
            if (!node.isIgnoreResult()) {
                // Copy the post-increment value onto the stack
                tempInstructions.add(
                        new Instruction(
                                InstructionType.MOV,
                                first,
                                null,
                                new MemLocation(MemArea.STACK, clazz)));
            }
        } else if (Operator.DEC_SUFFIX.equals(node.getOperator())
                || Operator.INC_SUFFIX.equals(node.getOperator())) {
            if (!node.isIgnoreResult()) {
                // Push the pre-increment value onto the stack
                tempInstructions.add(
                        new Instruction(
                                InstructionType.MOV,
                                first,
                                null,
                                new MemLocation(MemArea.STACK, clazz)));
            }
            // Update the value by however much, in place
            tempInstructions.add(new Instruction(type, first, second, first));
        } else {
            // Normal binary operator
            tempInstructions.add(
                    new Instruction(type, first, second, new MemLocation(MemArea.STACK, clazz)));
        }
    }

    @Override
    public void visit(ExprAssign node) {
        final Node leftSide = node.getChildren().get(0);
        final Node rightSide = node.getChildren().get(1);
        processBoolExpression(rightSide);

        Class<?> numericType = null;
        if (node.getOperator() != ExprAssign.Operator.ASSIGN) {
            numericType = getNumericBase(leftSide);
        }

        InstructionType instruction = this.instructionType(node.getOperator(), numericType);

        MemLocation first;
        if (rightSide instanceof Identifier identifier) {
            pushVarToStack(identifier);
            first =
                    new MemLocation(
                            MemArea.STACK, rightSide.getType().getBase().getCorrespondingClass());
        } else {
            first =
                    new MemLocation(
                            MemArea.STACK, rightSide.getType().getBase().getCorrespondingClass());
        }

        Class<?> clazz = node.getType().getBase().getCorrespondingClass();

        MemLocation target = null;
        // Determine if the left side is a variable, field access, array access
        if (leftSide instanceof Identifier identifier) {
            target = new MemLocation(MemArea.VARIABLE, clazz, identifier.getName());
        } else {
            // Impossible due to grammar.
            log.warn(
                    SafeResourceLoader.getString(
                            "UNKNOWN_ASSIGN_LEFT_SIDE", ScriptManager.getResourceBundle()),
                    leftSide.toString());
            target = new MemLocation(MemArea.IMMEDIATE, Void.class);
        }

        MemLocation second = null;
        if (node.getOperator() != ExprAssign.Operator.ASSIGN) {
            // Handle somewhat in-place modification
            // We want to do target _= right -> target for whatever operator _
            second = new MemLocation(target.area(), target.type(), target.value());
        }

        tempInstructions.add(new Instruction(instruction, first, second, target));
    }

    @Override
    public void visit(ExprEquality node) {
        outputBooleanComparison(node);
    }

    @Override
    public void visit(ExprLogic node) {
        Node left = node.getChildren().get(0);
        MemLocation target = new MemLocation(MemArea.STACK, Boolean.class);

        MemLocation first;
        if (left instanceof Identifier leftID) {
            first = new MemLocation(MemArea.VARIABLE, Boolean.class, leftID.getName());
        } else {
            first = new MemLocation(MemArea.STACK, Boolean.class);
        }

        if (node.getOperator() == ExprLogic.Operator.NOT) {
            processBoolExpression(left);

            tempInstructions.add(new Instruction(InstructionType.NOT, first, null, target));
            return;
        }
        Node right = node.getChildren().get(1);

        // We want to pop the left side first, so we have to push it last
        processBoolExpression(right);

        processBoolExpression(left);

        InstructionType type = InstructionType.NOP;
        switch (node.getOperator()) {
            case AND:
                type = InstructionType.AND;
                break;
            case OR:
                type = InstructionType.OR;
                break;
            case NOT:
            default:
                // Already handled
                break;
        }
        tempInstructions.add(
                new Instruction(
                        type, first, new MemLocation(MemArea.STACK, Boolean.class), target));
    }

    @Override
    public void visit(ExprRelation node) {
        outputBooleanComparison(node);
    }

    @Override
    public void visit(ExprTernary node) {
        Node conditional = node.getChildren().get(0);
        Node ifTrue = node.getChildren().get(1);
        Node ifFalse = node.getChildren().get(2);

        final String end = getNextLabelName();

        if (conditional instanceof Identifier id) {
            pushVarToStack(id);
        } else {
            processTree(conditional);
        }

        String falseLabel = getNextLabelName();
        // If not condition, goto else, otherwise we fall through to if
        calculateInvertedJump(conditional, falseLabel);
        processTree(ifTrue);

        if (ifTrue instanceof ExprRelation relation) {
            this.pushResultToStack(relation);
        } else if (ifTrue instanceof ExprEquality equality) {
            this.pushResultToStack(equality);
        }

        emitJump(InstructionType.JMP, end);
        emitLabel(falseLabel);
        processTree(ifFalse);
        if (ifFalse instanceof ExprRelation relation) {
            this.pushResultToStack(relation);
        } else if (ifFalse instanceof ExprEquality equality) {
            this.pushResultToStack(equality);
        }

        emitLabel(end);
    }

    @Override
    public void visit(ForLoop node) {
        int position = 0;

        Node init = null;
        Node condition = null;
        Node update = null;
        if (node.isInitializer()) {
            init = node.getChildren().get(position);
            ++position;
        }
        if (node.isCondition()) {
            condition = node.getChildren().get(position);
            ++position;
        }
        if (node.isUpdate()) {
            update = node.getChildren().get(position);
            ++position;
            if (update instanceof ExprArithmetic expr) {
                // Handle post/pre-fix expressions being thrown away
                expr.setIgnoreResult(true);
            }
        }
        Node body = node.getChildren().get(position);

        final String topOfLoopLabel = getNextLabelName();
        final String conditionLabel = getNextLabelName();

        final boolean containsBreak = containsBreak(body);
        if (containsBreak) {
            breakLabel = getNextLabelName();
        }

        continueLabel = conditionLabel;

        if (init != null) {
            processTree(init);
        }
        emitJump(InstructionType.JMP, conditionLabel);
        emitLabel(topOfLoopLabel);
        processTree(body);
        if (update != null) {
            processTree(update);
        }
        emitLabel(conditionLabel);
        if (condition != null) {
            processTree(condition);
            calculateJump(condition, topOfLoopLabel);
        } else {
            emitJump(InstructionType.JMP, topOfLoopLabel);
        }
        if (containsBreak) {
            emitLabel(breakLabel);
        }
    }

    @Override
    public void visit(Goto node) {
        Identifier target = (Identifier) node.getChildren().get(0);
        emitJump(InstructionType.JMP, target.getName());
    }

    @Override
    public void visit(If node) {
        Node conditional = node.getChildren().get(0);
        Node ifPart = node.getChildren().get(1);
        final boolean containsElse = node.getChildren().size() > 2;

        final String end = getNextLabelName();

        if (conditional instanceof Identifier id) {
            pushVarToStack(id);
        } else {
            processTree(conditional);
        }
        if (containsElse) {
            Node elsePart = node.getChildren().get(2);
            String elseLabel = getNextLabelName();
            // If not condition, goto else, otherwise we fall through to if
            calculateInvertedJump(conditional, elseLabel);
            processTree(ifPart);
            emitJump(InstructionType.JMP, end);
            emitLabel(elseLabel);
            processTree(elsePart);
        } else {
            calculateInvertedJump(conditional, end);
            processTree(ifPart);
        }
        emitLabel(end);
    }

    @Override
    public void visit(Label node) {
        tempInstructions.add(
                new Instruction(
                        InstructionType.NOP,
                        new MemLocation(MemArea.IMMEDIATE, String.class, node.getName()),
                        null,
                        null));
    }

    @Override
    public void visit(SwitchStatement node) {
        Node expression = node.getChildren().get(0);
        Node body = node.getChildren().get(1);

        final String expressionResult = getNextVariableName();

        final boolean containsBreak = containsBreak(body);
        if (containsBreak) {
            breakLabel = getNextLabelName();
        }

        /*
         * Emit the expression and store the result in a temporary variable. We
         * use a variable since we have many comparisons, and don't want to spam
         * the stack.
         */
        if (expression instanceof Identifier id) {
            pushVarToStack(id);
        } else {
            processTree(expression);
        }
        tempInstructions.add(
                new Instruction(
                        InstructionType.MOV,
                        new MemLocation(
                                MemArea.STACK,
                                expression.getType().getBase().getCorrespondingClass()),
                        null,
                        new MemLocation(MemArea.VARIABLE, String.class, expressionResult)));

        // emit jump table

        /**
         * A list of pseudo-labels and expressions to be evaluated as the target of the jump table.
         */
        List<Node> targetTable = new ArrayList<>();

        switchBody(body, targetTable, expressionResult);

        // Then emit targets and contents

        for (Node child : targetTable) {
            if (child instanceof Label label) {
                emitLabel(label.getName());
            } else {
                processTree(child);
            }
        }

        if (containsBreak) {
            emitLabel(breakLabel);
        }
    }

    @Override
    public void visit(VarDeclaration node) {
        final int dims = node.getDimensions();

        Identifier id = (Identifier) node.getChildren().get(0);
        final String varName = id.getName();

        Class<?> clazz;
        switch (node.getType().getBase()) {
            case BOOLEAN:
                clazz = Boolean.class;
                break;
            case CHAR:
                clazz = Character.class;
                break;
            case DOUBLE:
                clazz = Double.class;
                break;
            case INT:
                clazz = Integer.class;
                break;
            case STRING:
                clazz = String.class;
                break;
            case UNKNOWN, IDENTIFIER:
                clazz = Object.class;
                break;
            case LABEL, VOID:
            default:
                log.warn(
                        SafeResourceLoader.getString(
                                "INVALID_MEMORY_TYPE", ScriptManager.getResourceBundle()),
                        node.getType().getBase());
                clazz = Object.class;
        }

        MemLocation target = new MemLocation(MemArea.VARIABLE, clazz, varName);
        MemLocation defaultValue;

        if (node.getChildren().size() > 1) {
            Node rightSide = node.getChildren().get(1);
            processTree(rightSide);
            defaultValue = new MemLocation(MemArea.STACK, clazz);
            if (rightSide instanceof ExprRelation relation) {
                this.pushResultToStack(relation);
            } else if (rightSide instanceof ExprEquality equality) {
                this.pushResultToStack(equality);
            }
        } else if (dims == 0) {
            // Check for primitives, store defaults

            if (node.getType().anyOf(Base.CHAR, Base.INT, Base.DOUBLE)) {
                defaultValue = new MemLocation(MemArea.IMMEDIATE, clazz, 0);
            } else if (node.getType().anyOf(Base.BOOLEAN)) {
                defaultValue = new MemLocation(MemArea.IMMEDIATE, clazz, false);
            } else if (node.getType().anyOf(Base.STRING)) {
                defaultValue = new MemLocation(MemArea.IMMEDIATE, clazz, "");
            } else {
                defaultValue = new MemLocation(MemArea.IMMEDIATE, clazz, null);
            }
        } else {
            defaultValue = new MemLocation(MemArea.IMMEDIATE, clazz, null);
        }
        tempInstructions.add(new Instruction(InstructionType.MOV, defaultValue, null, target));
    }

    @Override
    public void visit(While node) {
        Node conditional = node.getChildren().get(0);
        Node body = node.getChildren().get(1);

        final String topOfLoopLabel = getNextLabelName();
        final String conditionLabel = getNextLabelName();
        continueLabel = conditionLabel;

        final boolean containsBreak = containsBreak(body);
        if (containsBreak) {
            breakLabel = getNextLabelName();
        }

        emitJump(InstructionType.JMP, conditionLabel);
        emitLabel(topOfLoopLabel);

        processTree(body);

        emitLabel(conditionLabel);
        processTree(conditional);
        calculateJump(conditional, topOfLoopLabel);

        if (containsBreak) {
            emitLabel(breakLabel);
        }
    }
}
