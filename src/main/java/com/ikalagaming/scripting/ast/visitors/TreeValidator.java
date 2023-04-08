package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.ExprArithmetic;
import com.ikalagaming.scripting.ast.Node;

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
		if (node.getChildren().size() < 1) {
			TreeValidator.log.warn("Missing first child for node {}",
				this.toString());
			this.valid = false;
		}
		switch (node.getOperator()) {
			case ADD:
			case SUB:
				break;
			case DIV:
			case MUL:
			case MOD:
				if (node.getChildren().size() < 2) {
					TreeValidator.log.warn("Missing second child for node {}",
						this.toString());
					this.valid = false;
				}
				break;
			case DEC_PREFIX:
			case DEC_SUFFIX:
			case INC_PREFIX:
			case INC_SUFFIX:
				break;
			default:
				TreeValidator.log.warn("Unknown operator {}",
					node.getOperator().toString());
				this.valid = false;
		}
	}

}
