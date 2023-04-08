package com.ikalagaming.scripting.ast;

/**
 * A list of statements.
 *
 * @author Ches Burks
 *
 */
public class StatementList extends Node {
	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
