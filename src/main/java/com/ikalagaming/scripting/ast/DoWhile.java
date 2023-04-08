package com.ikalagaming.scripting.ast;

/**
 * A do while loop. The children will be the statement, then the conditional
 * expression.
 *
 * @author Ches Burks
 *
 */
public class DoWhile extends Node {
	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
