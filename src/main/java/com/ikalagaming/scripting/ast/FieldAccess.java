package com.ikalagaming.scripting.ast;

/**
 * Access to a field, which has two nodes, the primary expression and the
 * identifier we are trying to access.
 *
 * @author Ches Burks
 *
 */
public class FieldAccess extends Node {
	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
