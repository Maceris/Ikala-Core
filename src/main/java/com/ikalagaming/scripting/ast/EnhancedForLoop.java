package com.ikalagaming.scripting.ast;

/**
 * An enhanced for statement.
 *
 * Children will be the type, variable declaration, the expression we are
 * iterating over, then the statement that is the loop body.
 *
 * @author Ches Burks
 *
 */
public class EnhancedForLoop extends Node {
	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
