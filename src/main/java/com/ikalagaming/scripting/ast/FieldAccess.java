package com.ikalagaming.scripting.ast;

import com.ikalagaming.scripting.VariableTypeMap;

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

	@Override
	protected void processType(VariableTypeMap variables) {
		// Reset the field's type, in case we have a variable with that name
		this.children.get(1).setType(Type.unknownType());
	}
}
