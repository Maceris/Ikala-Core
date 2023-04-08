package com.ikalagaming.scripting.ast;

import com.ikalagaming.scripting.VariableTypeMap;

/**
 * A list of variable declarations. The first node is the type, then there are
 * variable declarations.
 *
 * @author Ches Burks
 *
 */
public class VarDeclarationList extends Node {
	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected void processType(VariableTypeMap variables) {
		Type declaredType = this.children.get(0).getType();

		for (int i = 1; i < this.children.size(); ++i) {
			VarDeclaration decl = (VarDeclaration) this.children.get(i);
			decl.setType(declaredType);
			Identifier id = (Identifier) decl.getChildren().get(0);
			id.setType(declaredType);
			variables.put(id.getName(), declaredType);
		}
	}
}
