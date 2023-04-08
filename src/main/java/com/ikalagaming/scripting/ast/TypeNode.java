package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * A type in the code. Since all nodes have a type, we just use that instead of
 * storing anything extra here.
 *
 * @author Ches Burks
 *
 */
@Getter
@Setter
public class TypeNode extends Node {
	/**
	 * Whether the type is for a final variable.
	 */
	private boolean isFinal;

	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
