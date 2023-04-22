package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.ConstChar;
import com.ikalagaming.scripting.ast.ConstDouble;
import com.ikalagaming.scripting.ast.ConstInt;
import com.ikalagaming.scripting.ast.ExprArithmetic;
import com.ikalagaming.scripting.ast.ExprLogic;
import com.ikalagaming.scripting.ast.ExprRelation;
import com.ikalagaming.scripting.ast.Identifier;
import com.ikalagaming.scripting.ast.Node;
import com.ikalagaming.scripting.ast.Type.Base;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Perform validations on the tree. Things like basic type checking, or semantic
 * analysis.
 *
 * @author Ches Burks
 *
 */
@Slf4j
public class TreeValidator implements ASTVisitor {

	private static final String INVALID_FIRST_CHILD = "INVALID_FIRST_CHILD";

	private static final String INVALID_SECOND_CHILD = "INVALID_SECOND_CHILD";
	private boolean valid;

	/**
	 * Recursively process the tree from the leaves up.
	 *
	 * @param node The node we are checking.
	 */
	private void check(Node node) {
		for (Node child : node.getChildren()) {
			this.check(child);
		}
		node.process(this);
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
		return this.valid;
	}

	@Override
	public void visit(ExprArithmetic node) {
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
			case ADD, SUB:
				break;
			case DIV, MUL, MOD:
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
	public void visit(ExprLogic node) {
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
	public void visit(Identifier node) {
		if (node.getType().anyOf(Base.VOID)) {
			TreeValidator.log
				.warn(SafeResourceLoader.getString("INVALID_VARIABLE_USE",
					ScriptManager.getResourceBundle()), node.getName());
			this.valid = false;
		}
	}

}
