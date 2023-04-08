package com.ikalagaming.scripting.ast;

/**
 * Represents an access to a value in an array, contains the array reference,
 * then all the index expressions.
 *
 * @author Ches Burks
 *
 */
public class ArrayAccess extends Node {
	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}

}
