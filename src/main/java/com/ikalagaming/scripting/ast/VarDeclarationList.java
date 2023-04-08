package com.ikalagaming.scripting.ast;

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

}
