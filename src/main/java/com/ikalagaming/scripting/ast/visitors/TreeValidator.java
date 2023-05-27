package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.Break;
import com.ikalagaming.scripting.ast.Call;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.ConstChar;
import com.ikalagaming.scripting.ast.ConstDouble;
import com.ikalagaming.scripting.ast.ConstInt;
import com.ikalagaming.scripting.ast.Continue;
import com.ikalagaming.scripting.ast.DoWhile;
import com.ikalagaming.scripting.ast.ExprArithmetic;
import com.ikalagaming.scripting.ast.ExprAssign;
import com.ikalagaming.scripting.ast.ExprLogic;
import com.ikalagaming.scripting.ast.ExprRelation;
import com.ikalagaming.scripting.ast.ExprTernary;
import com.ikalagaming.scripting.ast.ForLoop;
import com.ikalagaming.scripting.ast.Identifier;
import com.ikalagaming.scripting.ast.If;
import com.ikalagaming.scripting.ast.Label;
import com.ikalagaming.scripting.ast.Node;
import com.ikalagaming.scripting.ast.SwitchBlockGroup;
import com.ikalagaming.scripting.ast.SwitchLabel;
import com.ikalagaming.scripting.ast.SwitchStatement;
import com.ikalagaming.scripting.ast.Type;
import com.ikalagaming.scripting.ast.Type.Base;
import com.ikalagaming.scripting.ast.VarDeclaration;
import com.ikalagaming.scripting.ast.While;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Perform validations on the tree. Things like basic type checking, or semantic
 * analysis.
 *
 * @author Ches Burks
 *
 */
@Slf4j
public class TreeValidator implements ASTVisitor {

	private static final String CONDITIONAL_NOT_BOOLEAN =
		"CONDITIONAL_NOT_BOOLEAN";
	private static final String INVALID_FIRST_CHILD = "INVALID_FIRST_CHILD";
	private static final String INVALID_SECOND_CHILD = "INVALID_SECOND_CHILD";
	private static final String INVALID_TYPE = "INVALID_TYPE";
	private static final String DOWNCASTING = "IMPLICIT_DOWNCASTING";
	private static final String INVALID_CAST = "INVALID_CAST_DECLARATION";

	private boolean valid;

	/**
	 * Positive if we are inside a loop, and should be allowed to have a break
	 * or continue.
	 */
	private int loopDepth;

	/**
	 * Positive if we are inside a switch.
	 */
	private int switchDepth;

	/**
	 * Recursively process the tree from the leaves up.
	 *
	 * @param node The node we are checking.
	 */
	private void check(Node node) {
		for (Node child : node.getChildren()) {
			boolean inLoop = node instanceof DoWhile || node instanceof ForLoop
				|| node instanceof While;
			boolean inSwitch = node instanceof SwitchStatement;

			if (inLoop) {
				this.loopDepth++;
			}
			if (inSwitch) {
				this.switchDepth++;
			}
			this.check(child);
			if (inLoop) {
				this.loopDepth--;
			}
			if (inSwitch) {
				this.switchDepth--;
			}
		}
		node.process(this);
	}

	/**
	 * Check for duplicate labels.
	 *
	 * @param node The node we are working on.
	 * @param names The label names that we have encountered so far. When
	 *            calling this, please pass in a fresh list.
	 */
	private void checkLabels(Node node, List<String> names) {
		if (!this.valid) {
			return;
		}
		for (Node child : node.getChildren()) {
			this.checkLabels(child, names);
			if (!this.valid) {
				return;
			}
		}
		if (node instanceof Label label) {
			if (names.contains(label.getName())) {
				this.markInvalid(node, "DUPLICATE_LABEL");
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
	private void checkMoveTypes(Node node, final Node firstChild,
		final Node secondChild) {
		if ((firstChild.getType().anyOf(Base.CHAR)
			&& secondChild.getType().anyOf(Base.INT, Base.DOUBLE))
			|| (firstChild.getType().anyOf(Base.INT)
				&& secondChild.getType().anyOf(Base.DOUBLE))) {
			this.markInvalid(node, TreeValidator.DOWNCASTING);
			return;
		}
		if ((firstChild.getType().anyOf(Base.BOOLEAN)
			&& secondChild.getType().anyOf(Base.CHAR, Base.DOUBLE, Base.INT,
				Base.STRING, Base.VOID, Base.IDENTIFIER))
			|| (firstChild.getType().anyOf(Base.CHAR, Base.INT, Base.DOUBLE)
				&& secondChild.getType().anyOf(Base.BOOLEAN, Base.STRING,
					Base.VOID, Base.IDENTIFIER))
			|| (firstChild.getType().anyOf(Base.STRING)
				&& secondChild.getType().anyOf(Base.BOOLEAN, Base.CHAR,
					Base.DOUBLE, Base.INT, Base.IDENTIFIER))) {
			this.markInvalid(node, TreeValidator.INVALID_CAST);
			return;
		}
		this.checkMoveTypesIdentifier(node, firstChild, secondChild);
	}

	/**
	 * Check types for assignment or declarations, relating to identifiers.
	 *
	 * @param node The root node.
	 * @param firstChild The first child.
	 * @param secondChild The second child.
	 */
	private void checkMoveTypesIdentifier(Node node, final Node firstChild,
		final Node secondChild) {
		if (firstChild.getType().anyOf(Base.IDENTIFIER)) {
			if (secondChild.getType().anyOf(Base.IDENTIFIER)
				&& !firstChild.getType().getValue()
					.equals(secondChild.getType().getValue())) {
				this.markInvalid(node, TreeValidator.INVALID_CAST);
				return;
			}
			if (secondChild.getType().anyOf(Base.BOOLEAN, Base.CHAR,
				Base.DOUBLE, Base.INT, Base.STRING)) {
				this.markInvalid(node, TreeValidator.INVALID_CAST);
			}
		}
	}

	/**
	 * Fetch the list of labels from a switch body. This is not recursive, we
	 * are not interested in any nested statements.
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
			}
			else if (child instanceof SwitchLabel label) {
				result.add(label);
			}
		}
		return result;
	}

	/**
	 * Check that a node has at least one child, warns and marks the tree
	 * invalid if it does not.
	 *
	 * @param node The node to check.
	 * @return Whether we had at least one child.
	 */
	private boolean hasAtLeastOneChild(Node node) {
		if (node.getChildren().isEmpty()) {
			this.markInvalid(node, "MISSING_FIRST_CHILD");
			return false;
		}
		return true;
	}

	/**
	 * Check that a node has at least two children, warns and marks the tree
	 * invalid if it does not.
	 *
	 * @param node The node to check.
	 * @return Whether we had at least two children.
	 */
	private boolean hasAtLeastTwoChildren(Node node) {
		if (node.getChildren().size() < 2) {
			this.markInvalid(node, "MISSING_SECOND_CHILD");
			return false;
		}
		return true;
	}

	/**
	 * Mark the tree as invalid, printing out an error message including the
	 * text of the node that was invalid.
	 *
	 * @param node The node to include in the error message.
	 * @param errorMessage The key to look up the localize error message.
	 */
	private void markInvalid(@NonNull Node node, @NonNull String errorMessage) {
		TreeValidator.log.warn(SafeResourceLoader.getString(errorMessage,
			ScriptManager.getResourceBundle()), node.toString());
		this.valid = false;
	}

	/**
	 * Validates the tree and returns a result indicating if it is okay or had
	 * issues.
	 *
	 * @param ast The tree to validate.
	 *
	 * @return True if the tree is valid, false if anything was not.
	 */
	public boolean validate(CompilationUnit ast) {
		this.valid = true;
		this.check(ast);
		this.checkLabels(ast, new ArrayList<>());
		return this.valid;
	}

	@Override
	public void visit(Break node) {
		if (this.loopDepth == 0 && this.switchDepth == 0) {
			this.markInvalid(node, "BREAK_OUTSIDE_LOOP");
		}
	}

	@Override
	public void visit(Call node) {
		if (node.isPrimary()) {
			Node expression = node.getChildren().get(0);
			if (!expression.getType().anyOf(Base.IDENTIFIER, Base.UNKNOWN)) {
				this.markInvalid(expression, "INVALID_METHOD_CALL");
			}
		}
	}

	@Override
	public void visit(Continue node) {
		if (this.loopDepth == 0) {
			this.markInvalid(node, "CONTINUE_OUTSIDE_LOOP");
		}
	}

	@Override
	public void visit(DoWhile node) {
		final Node expression = node.getChildren().get(1);
		if (!expression.getType().anyOf(Base.BOOLEAN, Base.UNKNOWN)) {
			this.markInvalid(expression, TreeValidator.CONDITIONAL_NOT_BOOLEAN);
		}
	}

	@Override
	public void visit(ExprArithmetic node) {
		if (node.getType().anyOf(Base.VOID)) {
			this.markInvalid(node, TreeValidator.INVALID_TYPE);
			return;
		}
		if (!this.hasAtLeastOneChild(node)) {
			return;
		}
		final Node firstChild = node.getChildren().get(0);
		if (firstChild.getType().anyOf(Base.VOID, Base.BOOLEAN)) {
			this.markInvalid(firstChild, TreeValidator.INVALID_FIRST_CHILD);
		}
		if (node.getChildren().size() == 2) {
			final Node secondChild = node.getChildren().get(1);
			if (secondChild.getType().anyOf(Base.VOID, Base.BOOLEAN)) {
				this.markInvalid(secondChild,
					TreeValidator.INVALID_SECOND_CHILD);
			}
		}
		switch (node.getOperator()) {
			case SUB:
				break;
			case ADD, DIV, MUL, MOD:
				// Sets the flag if not
				this.hasAtLeastTwoChildren(node);
				break;
			case DEC_PREFIX, DEC_SUFFIX, INC_PREFIX, INC_SUFFIX:
				if (firstChild instanceof ConstChar
					|| firstChild instanceof ConstDouble
					|| firstChild instanceof ConstInt) {
					TreeValidator.log.warn(
						SafeResourceLoader.getString("INVALID_OPERATOR",
							ScriptManager.getResourceBundle()),
						node.getOperator().toString(),
						firstChild.getClass().getSimpleName());
					this.valid = false;
				}
				break;
			default:
				TreeValidator.log.warn(
					SafeResourceLoader.getString("UNKNOWN_OPERATOR",
						ScriptManager.getResourceBundle()),
					node.getOperator().toString());
				this.valid = false;
		}
	}

	@Override
	public void visit(ExprAssign node) {
		if (node.getChildren().size() < 2) {
			return;
		}

		final Node firstChild = node.getChildren().get(0);
		final Node secondChild = node.getChildren().get(1);

		this.checkMoveTypes(node, firstChild, secondChild);
	}

	@Override
	public void visit(ExprLogic node) {
		if (node.getType().anyOf(Base.VOID)) {
			this.markInvalid(node, TreeValidator.INVALID_TYPE);
			return;
		}
		if (!this.hasAtLeastOneChild(node)) {
			return;
		}
		final Node firstChild = node.getChildren().get(0);
		if (!firstChild.getType().anyOf(Base.BOOLEAN, Base.UNKNOWN)) {
			this.markInvalid(firstChild, TreeValidator.INVALID_FIRST_CHILD);
			return;
		}

		if (!this.hasAtLeastTwoChildren(node)) {
			return;
		}
		final Node secondChild = node.getChildren().get(1);
		if (!secondChild.getType().anyOf(Base.BOOLEAN, Base.UNKNOWN)) {
			this.markInvalid(secondChild, TreeValidator.INVALID_SECOND_CHILD);
		}
	}

	@Override
	public void visit(ExprRelation node) {
		if (!this.hasAtLeastOneChild(node)) {
			return;
		}
		final Node firstChild = node.getChildren().get(0);
		if (firstChild.getType().anyOf(Base.BOOLEAN, Base.IDENTIFIER,
			Base.LABEL, Base.STRING, Base.VOID)) {
			this.markInvalid(firstChild, TreeValidator.INVALID_FIRST_CHILD);
			return;
		}

		if (!this.hasAtLeastTwoChildren(node)) {
			return;
		}
		final Node secondChild = node.getChildren().get(1);
		if (secondChild.getType().anyOf(Base.BOOLEAN, Base.IDENTIFIER,
			Base.LABEL, Base.STRING, Base.VOID)) {
			this.markInvalid(secondChild, TreeValidator.INVALID_SECOND_CHILD);
		}
	}

	@Override
	public void visit(ExprTernary node) {
		final Node expression = node.getChildren().get(0);

		if (!expression.getType().anyOf(Base.BOOLEAN)) {
			this.markInvalid(expression, TreeValidator.INVALID_FIRST_CHILD);
			return;
		}
		if (node.getType().anyOf(Base.VOID)) {
			this.markInvalid(node, TreeValidator.INVALID_TYPE);
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
				this.markInvalid(expression,
					TreeValidator.CONDITIONAL_NOT_BOOLEAN);
			}
		}
	}

	@Override
	public void visit(Identifier node) {
		if (node.getType().anyOf(Base.VOID)) {
			TreeValidator.log
				.warn(SafeResourceLoader.getString("INVALID_VARIABLE_USE",
					ScriptManager.getResourceBundle()), node.getName());
			this.valid = false;
		}
	}

	@Override
	public void visit(If node) {
		final Node expression = node.getChildren().get(0);
		if (!expression.getType().anyOf(Base.BOOLEAN, Base.UNKNOWN)) {
			this.markInvalid(expression, TreeValidator.CONDITIONAL_NOT_BOOLEAN);
		}
	}

	@Override
	public void visit(SwitchStatement node) {
		Type expressionType = node.getChildren().get(0).getType();
		Node block = node.getChildren().get(1);
		int defaultCount = 0;

		List<SwitchLabel> labels = this.getSwitchLabels(block);

		for (SwitchLabel label : labels) {
			if (label.isDefault()) {
				++defaultCount;
			}
			if (!label.isDefault() && !label.getChildren().get(0).getType()
				.equals(expressionType)) {
				this.markInvalid(label, "SWITCH_TYPE_MISMATCH");
			}
		}

		if (defaultCount > 1) {
			this.markInvalid(node, "MULTIPLE_DEFAULTS");
		}

	}

	@Override
	public void visit(VarDeclaration node) {
		if (node.getChildren().size() < 2) {
			return;
		}
		final Node firstChild = node.getChildren().get(0);
		final Node secondChild = node.getChildren().get(1);

		this.checkMoveTypes(node, firstChild, secondChild);
	}

	@Override
	public void visit(While node) {
		final Node expression = node.getChildren().get(0);
		if (!expression.getType().anyOf(Base.BOOLEAN, Base.UNKNOWN)) {
			this.markInvalid(expression, TreeValidator.CONDITIONAL_NOT_BOOLEAN);
		}
	}
}
