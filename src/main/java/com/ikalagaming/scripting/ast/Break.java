package com.ikalagaming.scripting.ast;

/**
 * A break statement, which might contain an identifier for which labeled
 * statement to break out of.
 *
 * @author Ches Burks
 *
 */
public class Break extends Node {
	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
