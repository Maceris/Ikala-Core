package com.ikalagaming.scripting.ast;

import com.ikalagaming.scripting.VariableTypeMap;

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

	@Override
	protected void processType(VariableTypeMap variables) {
		Type actual = this.children.get(0).getType()
			.dereference(this.children.size() - 1);
		this.setType(actual);
	}
}
