package com.ikalagaming.scripting.ast;

/**
 * A type name, which is a list of identifiers.
 *
 * @author Ches Burks
 *
 */
public class TypeName extends Node {
	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
