package com.ikalagaming.scripting.ast;

/**
 * A switch statement.
 *
 * @author Ches Burks
 *
 */
public class SwitchStatement extends Node {
	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
