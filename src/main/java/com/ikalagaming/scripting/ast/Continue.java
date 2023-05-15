package com.ikalagaming.scripting.ast;

/**
 * A continue statement.
 *
 * @author Ches Burks
 *
 */
public class Continue extends Node {
	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
