package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.ScriptDiagnostics;
import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.Block;
import com.ikalagaming.scripting.ast.Break;
import com.ikalagaming.scripting.ast.Call;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.ConstBool;
import com.ikalagaming.scripting.ast.ConstChar;
import com.ikalagaming.scripting.ast.ConstDouble;
import com.ikalagaming.scripting.ast.ConstInt;
import com.ikalagaming.scripting.ast.ConstString;
import com.ikalagaming.scripting.ast.Continue;
import com.ikalagaming.scripting.ast.DoWhile;
import com.ikalagaming.scripting.ast.ExprArithmetic;
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
import com.ikalagaming.scripting.ast.SourcePrinter;
import com.ikalagaming.scripting.ast.SwitchBlockGroup;
import com.ikalagaming.scripting.ast.SwitchLabel;
import com.ikalagaming.scripting.ast.SwitchStatement;
import com.ikalagaming.scripting.ast.Type;
import com.ikalagaming.scripting.ast.Type.Base;
import com.ikalagaming.scripting.ast.TypeNode;
import com.ikalagaming.scripting.ast.VarDeclaration;
import com.ikalagaming.scripting.ast.VarDeclarationList;
import com.ikalagaming.scripting.ast.While;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Perform validations on the tree. Things like basic type checking, or semantic analysis.
 *
 * @author Ches Burks
 */
@Slf4j
public class TreeValidator implements ASTVisitor {

    private static final String CONDITIONAL_NOT_BOOLEAN = "CONDITIONAL_NOT_BOOLEAN";
    private static final String INVALID_FIRST_CHILD = "INVALID_FIRST_CHILD";
    private static final String INVALID_SECOND_CHILD = "INVALID_SECOND_CHILD";
    private static final String INVALID_TYPE = "INVALID_TYPE";
    private static final String DOWNCASTING = "IMPLICIT_DOWNCASTING";
    private static final String INVALID_CAST = "INVALID_CAST_DECLARATION";

    private boolean valid;

    /** Positive if we are inside a loop, and should be allowed to have a break or continue. */
    private int loopDepth;

    /** Positive if we are inside a switch. */
    private int switchDepth;

    /**
     * The labels a script can be started at, after the last validation: labels with no variable
     * declarations in scope, so starting there skips nothing the script needs.
     */
    private Set<String> entryLabels = Set.of();

    /**
     * Recursively process the tree from the leaves up.
     *
     * @param node The node we are checking.
     */
    private void check(Node node) {
        for (Node child : node.getChildren()) {
            boolean inLoop =
                    node instanceof DoWhile || node instanceof ForLoop || node instanceof While;
            boolean inSwitch = node instanceof SwitchStatement;

            if (inLoop) {
                loopDepth++;
            }
            if (inSwitch) {
                switchDepth++;
            }
            check(child);
            if (inLoop) {
                loopDepth--;
            }
            if (inSwitch) {
                switchDepth--;
            }
        }
        node.process(this);
    }

    /**
     * Check for duplicate labels.
     *
     * @param node The node we are working on.
     * @param names The label names that we have encountered so far. When calling this, please pass
     *     in a fresh list.
     */
    private void checkLabels(Node node, List<String> names) {
        if (!valid) {
            return;
        }
        for (Node child : node.getChildren()) {
            checkLabels(child, names);
            if (!valid) {
                return;
            }
        }
        if (node instanceof Label label) {
            if (names.contains(label.getName())) {
                markInvalid(node, "DUPLICATE_LABEL");
            }
            names.add(label.getName());
        }
    }

    /**
     * Check types for assignment or declarations.
     *
     * @param node The root node.
     * @param firstChild The first child.
     * @param secondChild The second child.
     */
    private void checkMoveTypes(Node node, final Node firstChild, final Node secondChild) {
        if ((firstChild.getType().anyOf(Base.CHAR)
                        && secondChild.getType().anyOf(Base.INT, Base.DOUBLE))
                || (firstChild.getType().anyOf(Base.INT)
                        && secondChild.getType().anyOf(Base.DOUBLE))) {
            markInvalid(node, TreeValidator.DOWNCASTING);
            return;
        }
        if ((firstChild.getType().anyOf(Base.BOOLEAN)
                        && secondChild
                                .getType()
                                .anyOf(
                                        Base.CHAR,
                                        Base.DOUBLE,
                                        Base.INT,
                                        Base.STRING,
                                        Base.VOID,
                                        Base.IDENTIFIER))
                || (firstChild.getType().anyOf(Base.CHAR, Base.INT, Base.DOUBLE)
                        && secondChild
                                .getType()
                                .anyOf(Base.BOOLEAN, Base.STRING, Base.VOID, Base.IDENTIFIER))
                || (firstChild.getType().anyOf(Base.STRING)
                        && secondChild
                                .getType()
                                .anyOf(
                                        Base.BOOLEAN,
                                        Base.CHAR,
                                        Base.DOUBLE,
                                        Base.INT,
                                        Base.IDENTIFIER))) {
            markInvalid(node, TreeValidator.INVALID_CAST);
            return;
        }
        checkMoveTypesIdentifier(node, firstChild, secondChild);
    }

    /**
     * Check types for assignment or declarations, relating to identifiers.
     *
     * @param node The root node.
     * @param firstChild The first child.
     * @param secondChild The second child.
     */
    private void checkMoveTypesIdentifier(
            Node node, final Node firstChild, final Node secondChild) {
        if (firstChild.getType().anyOf(Base.IDENTIFIER)) {
            if (secondChild.getType().anyOf(Base.IDENTIFIER)
                    && !firstChild.getType().getValue().equals(secondChild.getType().getValue())) {
                markInvalid(node, TreeValidator.INVALID_CAST);
                return;
            }
            if (secondChild
                    .getType()
                    .anyOf(Base.BOOLEAN, Base.CHAR, Base.DOUBLE, Base.INT, Base.STRING)) {
                markInvalid(node, TreeValidator.INVALID_CAST);
            }
        }
    }

    /**
     * Fetch the list of labels from a switch body. This is not recursive, we are not interested in
     * any nested statements.
     *
     * @param switchBody The body of the switch statement.
     * @return The labels in the body.
     */
    private List<SwitchLabel> getSwitchLabels(Node switchBody) {
        List<SwitchLabel> result = new ArrayList<>();

        for (Node child : switchBody.getChildren()) {
            if (child instanceof SwitchBlockGroup) {
                for (Node subchild : child.getChildren()) {
                    if (subchild instanceof SwitchLabel label) {
                        result.add(label);
                    }
                }
            } else if (child instanceof SwitchLabel label) {
                result.add(label);
            }
        }
        return result;
    }

    /**
     * Check that a node has at least one child, warns and marks the tree invalid if it does not.
     *
     * @param node The node to check.
     * @return Whether we had at least one child.
     */
    private boolean hasAtLeastOneChild(Node node) {
        if (node.getChildren().isEmpty()) {
            markInvalid(node, "MISSING_FIRST_CHILD");
            return false;
        }
        return true;
    }

    /**
     * Check that a node has at least two children, warns and marks the tree invalid if it does not.
     *
     * @param node The node to check.
     * @return Whether we had at least two children.
     */
    private boolean hasAtLeastTwoChildren(Node node) {
        if (node.getChildren().size() < 2) {
            markInvalid(node, "MISSING_SECOND_CHILD");
            return false;
        }
        return true;
    }

    /**
     * Mark the tree as invalid, printing out an error message including the text of the node that
     * was invalid.
     *
     * @param node The node to include in the error message.
     * @param errorMessage The key to look up the localize error message.
     */
    private void markInvalid(@NonNull Node node, @NonNull String errorMessage) {
        ScriptDiagnostics.warnAt(
                log,
                node,
                SafeResourceLoader.getString(errorMessage, ScriptManager.getResourceBundle()),
                SourcePrinter.toSource(node));
        valid = false;
    }

    /**
     * Validates the tree and returns a result indicating if it is okay or had issues.
     *
     * @param ast The tree to validate.
     * @return True if the tree is valid, false if anything was not.
     */
    public boolean validate(CompilationUnit ast) {
        return validate(ast, Set.of());
    }

    /**
     * Validates the tree and returns a result indicating if it is okay or had issues.
     *
     * @param ast The tree to validate.
     * @param globals The names of globals the host provides, which the script can't assign to.
     * @return True if the tree is valid, false if anything was not.
     */
    public boolean validate(CompilationUnit ast, @NonNull Set<String> globals) {
        valid = true;
        entryLabels = Set.of();
        check(ast);
        checkLabels(ast, new ArrayList<>());
        if (valid) {
            checkGotoScopes(ast);
        }
        // Globals behave like final variables declared before the script
        checkFinals(ast, new HashSet<>(globals));
        return valid;
    }

    /**
     * The labels the script can be started at, found by the last validation. A label can be an
     * entry point if no variable declarations are in scope at it, the same rule as a goto from the
     * start of the script.
     *
     * @return The names of the labels.
     */
    public Set<String> getEntryLabels() {
        return entryLabels;
    }

    /**
     * Check that final variables are never modified after being declared. Every declaration gives
     * the variable a value, even if it's just the default, so there are no blank finals that can be
     * assigned later like in Java.
     *
     * @param node The node we are currently checking.
     * @param finals The names of final variables that are in scope. Names are unique among
     *     variables in scope, since shadowing is not allowed.
     */
    private void checkFinals(Node node, Set<String> finals) {
        if (node instanceof VarDeclarationList list) {
            final boolean isFinal = ((TypeNode) list.getChildren().get(0)).isFinal();
            for (int i = 1; i < list.getChildren().size(); ++i) {
                VarDeclaration declaration = (VarDeclaration) list.getChildren().get(i);
                // Initializers can't refer to the variable itself, so check them first
                for (int j = 1; j < declaration.getChildren().size(); ++j) {
                    checkFinals(declaration.getChildren().get(j), finals);
                }
                if (isFinal) {
                    finals.add(((Identifier) declaration.getChildren().get(0)).getName());
                }
            }
            return;
        }

        if (node instanceof ExprAssign assign
                && assign.getChildren().get(0) instanceof Identifier id
                && finals.contains(id.getName())) {
            markInvalid(node, "ASSIGN_TO_FINAL");
        }
        if (node instanceof ExprArithmetic arithmetic
                && !arithmetic.getChildren().isEmpty()
                && arithmetic.getChildren().get(0) instanceof Identifier id
                && finals.contains(id.getName())) {
            switch (arithmetic.getOperator()) {
                case DEC_PREFIX, DEC_SUFFIX, INC_PREFIX, INC_SUFFIX:
                    markInvalid(node, "ASSIGN_TO_FINAL");
                    break;
                default:
                    break;
            }
        }

        // Same scoping rules as the type preprocessor
        final boolean newScope = node instanceof Block || node instanceof ForLoop;
        Set<String> scopeFinals = newScope ? new HashSet<>(finals) : finals;
        for (Node child : node.getChildren()) {
            checkFinals(child, scopeFinals);
        }
    }

    /**
     * Fetch the value of a constant case label, so we can check for duplicates. Numbers are all
     * converted to doubles, since a character and integer with the same value would match the same
     * thing.
     *
     * @param node The expression for the case label.
     * @return The constant value, or null if it's not a simple constant.
     */
    private Object constantValue(Node node) {
        if (node instanceof ConstInt constant) {
            return (double) constant.getValue();
        }
        if (node instanceof ConstChar constant) {
            return (double) constant.getValue();
        }
        if (node instanceof ConstDouble constant) {
            return constant.getValue();
        }
        if (node instanceof ConstString constant) {
            return constant.getValue();
        }
        if (node instanceof ConstBool constant) {
            return constant.isValue();
        }
        if (node instanceof ExprArithmetic arithmetic
                && arithmetic.getOperator() == ExprArithmetic.Operator.SUB
                && arithmetic.getChildren().size() == 1
                && constantValue(arithmetic.getChildren().get(0)) instanceof Double value) {
            // Negative numbers
            return -value;
        }
        return null;
    }

    /**
     * Check if a type is a reference type, which can be compared with other references or null.
     *
     * @param type The type to check.
     * @return True if the type is a string, object, array, or null.
     */
    private boolean isReference(Type type) {
        return type.getDimensions() > 0 || type.anyOf(Base.STRING, Base.IDENTIFIER, Base.VOID);
    }

    /**
     * Check that no goto jumps into the scope of a variable while skipping over its declaration,
     * which is the same rule C++ has. Unlike C++, every declaration here initializes the variable
     * (to a default value if nothing else), so there are no declarations that are safe to skip.
     *
     * <p>Jumping into a block is fine as long as no declarations are skipped, and so is jumping out
     * of the scope of a variable.
     *
     * @param ast The tree to check, which should already have valid labels.
     */
    private void checkGotoScopes(CompilationUnit ast) {
        Map<String, List<VarDeclaration>> labelScopes = new HashMap<>();
        Map<Goto, List<VarDeclaration>> gotoScopes = new LinkedHashMap<>();
        findScopes(ast, new ArrayList<>(), labelScopes, gotoScopes);

        Set<String> entries = new HashSet<>();
        labelScopes.forEach(
                (label, scope) -> {
                    if (scope.isEmpty()) {
                        entries.add(label);
                    }
                });
        entryLabels = Set.copyOf(entries);

        for (var entry : gotoScopes.entrySet()) {
            final String target = ((Identifier) entry.getKey().getChildren().get(0)).getName();
            final List<VarDeclaration> labelScope = labelScopes.get(target);
            if (labelScope == null) {
                // Missing labels are reported elsewhere
                continue;
            }
            // Compare by identity, different scopes can reuse a variable name
            Set<VarDeclaration> gotoScope = Collections.newSetFromMap(new IdentityHashMap<>());
            gotoScope.addAll(entry.getValue());
            for (VarDeclaration declaration : labelScope) {
                if (!gotoScope.contains(declaration)) {
                    markInvalid(declaration, "GOTO_SKIPS_DECLARATION");
                    break;
                }
            }
        }
    }

    /**
     * Record which variable declarations are in scope at every label and goto.
     *
     * @param node The node we are currently looking at.
     * @param inScope The declarations currently in scope, in the order they were declared. This is
     *     modified as we go, but restored when leaving a scope.
     * @param labelScopes Where to store the declarations in scope for each label name.
     * @param gotoScopes Where to store the declarations in scope for each goto.
     */
    private void findScopes(
            Node node,
            List<VarDeclaration> inScope,
            Map<String, List<VarDeclaration>> labelScopes,
            Map<Goto, List<VarDeclaration>> gotoScopes) {
        if (node instanceof Label label) {
            labelScopes.put(label.getName(), List.copyOf(inScope));
            return;
        }
        if (node instanceof Goto gotoNode) {
            gotoScopes.put(gotoNode, List.copyOf(inScope));
            return;
        }

        // Same scoping rules as the type preprocessor
        final boolean newScope = node instanceof Block || node instanceof ForLoop;
        final int outerSize = inScope.size();

        for (Node child : node.getChildren()) {
            findScopes(child, inScope, labelScopes, gotoScopes);
        }
        if (node instanceof VarDeclaration declaration) {
            // In scope from here until the end of the enclosing block
            inScope.add(declaration);
        }

        if (newScope) {
            inScope.subList(outerSize, inScope.size()).clear();
        }
    }

    @Override
    public void visit(Break node) {
        if (loopDepth == 0 && switchDepth == 0) {
            markInvalid(node, "BREAK_OUTSIDE_LOOP");
        }
    }

    @Override
    public void visit(Call node) {
        if (node.isPrimary()) {
            Node expression = node.getChildren().get(0);
            // Strings are objects in Java, so "abc".length() is fine, unlike other primitives
            if (!expression.getType().anyOf(Base.IDENTIFIER, Base.STRING, Base.UNKNOWN)) {
                markInvalid(expression, "INVALID_METHOD_CALL");
            }
        }
    }

    @Override
    public void visit(Continue node) {
        if (loopDepth == 0) {
            markInvalid(node, "CONTINUE_OUTSIDE_LOOP");
        }
    }

    @Override
    public void visit(DoWhile node) {
        final Node expression = node.getChildren().get(1);
        if (!expression.getType().anyOf(Base.BOOLEAN, Base.UNKNOWN)) {
            markInvalid(expression, TreeValidator.CONDITIONAL_NOT_BOOLEAN);
        }
    }

    @Override
    public void visit(ExprArithmetic node) {
        if (node.getType().anyOf(Base.VOID)) {
            markInvalid(node, TreeValidator.INVALID_TYPE);
            return;
        }
        if (!hasAtLeastOneChild(node)) {
            return;
        }
        final Node firstChild = node.getChildren().get(0);
        // String concatenation can include booleans, like "a" + true
        final boolean concatenation =
                node.getOperator() == ExprArithmetic.Operator.ADD
                        && node.getType().anyOf(Base.STRING);
        final Base[] invalidBases =
                concatenation ? new Base[] {Base.VOID} : new Base[] {Base.VOID, Base.BOOLEAN};
        if (firstChild.getType().anyOf(invalidBases)) {
            markInvalid(firstChild, TreeValidator.INVALID_FIRST_CHILD);
        }
        if (node.getChildren().size() == 2) {
            final Node secondChild = node.getChildren().get(1);
            if (secondChild.getType().anyOf(invalidBases)) {
                markInvalid(secondChild, TreeValidator.INVALID_SECOND_CHILD);
            }
        }
        switch (node.getOperator()) {
            case SUB:
                break;
            case ADD, DIV, MUL, MOD:
                // Sets the flag if not
                hasAtLeastTwoChildren(node);
                break;
            case DEC_PREFIX, DEC_SUFFIX, INC_PREFIX, INC_SUFFIX:
                if (firstChild instanceof Identifier id
                        && id.getType().anyOf(Base.CHAR, Base.INT, Base.DOUBLE)) {
                    // Fine
                } else {
                    // We can only modify variables, not values like the result of x++
                    ScriptDiagnostics.warnAt(
                            log,
                            node,
                            SafeResourceLoader.getString(
                                    "INVALID_OPERATOR", ScriptManager.getResourceBundle()),
                            node.getOperator().toString(),
                            firstChild.getClass().getSimpleName());
                    valid = false;
                }
                break;
            default:
                ScriptDiagnostics.warnAt(
                        log,
                        node,
                        SafeResourceLoader.getString(
                                "UNKNOWN_OPERATOR", ScriptManager.getResourceBundle()),
                        node.getOperator().toString());
                valid = false;
        }
    }

    @Override
    public void visit(ExprAssign node) {
        if (node.getChildren().size() < 2) {
            return;
        }

        final Node firstChild = node.getChildren().get(0);
        final Node secondChild = node.getChildren().get(1);

        if (firstChild.getType().anyOf(Base.LABEL, Base.VOID)) {
            markInvalid(firstChild, TreeValidator.INVALID_FIRST_CHILD);
            return;
        }

        if (node.getOperator() == ExprAssign.Operator.ADD_ASSIGN
                && firstChild.getType().anyOf(Base.STRING)
                && !secondChild.getType().anyOf(Base.IDENTIFIER, Base.LABEL, Base.VOID)) {
            // String concatenation, same rules as the + operator
            return;
        }

        if (node.getOperator() != ExprAssign.Operator.ASSIGN
                && (!firstChild.getType().anyOf(Base.CHAR, Base.INT, Base.DOUBLE)
                        || !secondChild
                                .getType()
                                .anyOf(Base.CHAR, Base.INT, Base.DOUBLE, Base.UNKNOWN))) {
            // Things like -= only work on numbers
            markInvalid(node, TreeValidator.INVALID_TYPE);
            return;
        }

        checkMoveTypes(node, firstChild, secondChild);
    }

    @Override
    public void visit(ExprEquality node) {
        if (!hasAtLeastTwoChildren(node)) {
            return;
        }
        final Type first = node.getChildren().get(0).getType();
        final Type second = node.getChildren().get(1).getType();

        if (first.anyOf(Base.LABEL) || second.anyOf(Base.LABEL)) {
            markInvalid(node, "INCOMPARABLE_TYPES");
            return;
        }

        if (first.anyOf(Base.UNKNOWN)
                || second.anyOf(Base.UNKNOWN)
                || (first.getDimensions() == 0 && first.anyOf(Base.IDENTIFIER))
                || (second.getDimensions() == 0 && second.anyOf(Base.IDENTIFIER))) {
            /*
             * We don't know the type until runtime, or it's an object that might be something
             * like an Integer, so we can't rule anything out.
             */
            return;
        }

        final boolean bothNumeric =
                first.getDimensions() == 0
                        && second.getDimensions() == 0
                        && first.anyOf(Base.CHAR, Base.INT, Base.DOUBLE)
                        && second.anyOf(Base.CHAR, Base.INT, Base.DOUBLE);
        final boolean bothBoolean =
                first.getDimensions() == 0
                        && second.getDimensions() == 0
                        && first.anyOf(Base.BOOLEAN)
                        && second.anyOf(Base.BOOLEAN);
        final boolean bothReferences = isReference(first) && isReference(second);

        if (!(bothNumeric || bothBoolean || bothReferences)) {
            // Same rules as Java, like no comparing numbers to strings or booleans
            markInvalid(node, "INCOMPARABLE_TYPES");
        }
    }

    @Override
    public void visit(ExprLogic node) {
        if (node.getType().anyOf(Base.VOID)) {
            markInvalid(node, TreeValidator.INVALID_TYPE);
            return;
        }
        if (!hasAtLeastOneChild(node)) {
            return;
        }
        final Node firstChild = node.getChildren().get(0);
        if (!firstChild.getType().anyOf(Base.BOOLEAN, Base.UNKNOWN)) {
            markInvalid(firstChild, TreeValidator.INVALID_FIRST_CHILD);
            return;
        }

        if (node.getOperator() == ExprLogic.Operator.NOT) {
            // Unary
            return;
        }

        if (!hasAtLeastTwoChildren(node)) {
            return;
        }
        final Node secondChild = node.getChildren().get(1);
        if (!secondChild.getType().anyOf(Base.BOOLEAN, Base.UNKNOWN)) {
            markInvalid(secondChild, TreeValidator.INVALID_SECOND_CHILD);
        }
    }

    @Override
    public void visit(ExprRelation node) {
        if (!hasAtLeastOneChild(node)) {
            return;
        }
        final Node firstChild = node.getChildren().get(0);
        if (firstChild
                .getType()
                .anyOf(Base.BOOLEAN, Base.IDENTIFIER, Base.LABEL, Base.STRING, Base.VOID)) {
            markInvalid(firstChild, TreeValidator.INVALID_FIRST_CHILD);
            return;
        }

        if (!hasAtLeastTwoChildren(node)) {
            return;
        }
        final Node secondChild = node.getChildren().get(1);
        if (secondChild
                .getType()
                .anyOf(Base.BOOLEAN, Base.IDENTIFIER, Base.LABEL, Base.STRING, Base.VOID)) {
            markInvalid(secondChild, TreeValidator.INVALID_SECOND_CHILD);
        }
    }

    @Override
    public void visit(ExprTernary node) {
        final Node expression = node.getChildren().get(0);

        if (!expression.getType().anyOf(Base.BOOLEAN, Base.UNKNOWN)) {
            markInvalid(expression, TreeValidator.INVALID_FIRST_CHILD);
            return;
        }

        if (node.getType().anyOf(Base.VOID)) {
            markInvalid(node, TreeValidator.INVALID_TYPE);
        }
    }

    @Override
    public void visit(ForLoop node) {
        if (node.isCondition()) {
            int conditionalIndex = 0;
            if (node.isInitializer()) {
                ++conditionalIndex;
            }
            final Node expression = node.getChildren().get(conditionalIndex);
            if (!expression.getType().anyOf(Base.BOOLEAN, Base.UNKNOWN)) {
                markInvalid(expression, TreeValidator.CONDITIONAL_NOT_BOOLEAN);
            }
        }
    }

    @Override
    public void visit(Identifier node) {
        if (node.getType().anyOf(Base.VOID)) {
            ScriptDiagnostics.warnAt(
                    log,
                    node,
                    SafeResourceLoader.getString(
                            "INVALID_VARIABLE_USE", ScriptManager.getResourceBundle()),
                    node.getName());
            valid = false;
        }
    }

    @Override
    public void visit(Goto node) {
        final Node target = node.getChildren().get(0);
        if (!target.getType().anyOf(Base.LABEL)) {
            markInvalid(target, "GOTO_NOT_LABEL");
        }
    }

    @Override
    public void visit(If node) {
        final Node expression = node.getChildren().get(0);
        if (!expression.getType().anyOf(Base.BOOLEAN, Base.UNKNOWN)) {
            markInvalid(expression, TreeValidator.CONDITIONAL_NOT_BOOLEAN);
        }
    }

    @Override
    public void visit(SwitchStatement node) {
        Type expressionType = node.getChildren().get(0).getType();
        Node block = node.getChildren().get(1);
        int defaultCount = 0;

        List<SwitchLabel> labels = getSwitchLabels(block);
        Set<Object> caseValues = new HashSet<>();

        for (SwitchLabel label : labels) {
            if (label.isDefault()) {
                ++defaultCount;
            } else {
                final Object value = constantValue(label.getChildren().get(0));
                if (value != null && !caseValues.add(value)) {
                    markInvalid(label, "DUPLICATE_CASE");
                }
            }
            if (!label.isDefault()
                    && !expressionType.anyOf(Base.UNKNOWN)
                    && !label.getChildren().get(0).getType().equals(expressionType)) {
                markInvalid(label, "SWITCH_TYPE_MISMATCH");
            }
        }

        if (defaultCount > 1) {
            markInvalid(node, "MULTIPLE_DEFAULTS");
        }
    }

    @Override
    public void visit(VarDeclaration node) {
        if (node.getChildren().size() < 2) {
            return;
        }
        final Node firstChild = node.getChildren().get(0);
        final Node secondChild = node.getChildren().get(1);

        checkMoveTypes(node, firstChild, secondChild);
    }

    @Override
    public void visit(While node) {
        final Node expression = node.getChildren().get(0);
        if (!expression.getType().anyOf(Base.BOOLEAN, Base.UNKNOWN)) {
            markInvalid(expression, TreeValidator.CONDITIONAL_NOT_BOOLEAN);
        }
    }
}
