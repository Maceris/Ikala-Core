package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * A parameter declaration. The children will be the type, then an variable
 * declaration.
 *
 * @author Ches Burks
 *
 */
@Getter
@Setter
public class Parameter extends Node {
	/**
	 * If the parameter is a variable argument.
	 */
	private boolean varArgs;

	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
