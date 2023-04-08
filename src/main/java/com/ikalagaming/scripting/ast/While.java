package com.ikalagaming.scripting.ast;

/**
 * A while loop. The children will be the conditional expression, then the
 * statement.
 *
 * @author Ches Burks
 *
 */
public class While extends Node {
	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
