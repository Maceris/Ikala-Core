package com.ikalagaming.scripting.ast;

/**
 * A full script.
 *
 * @author Ches Burks
 *
 */
public class CompilationUnit extends Node {
	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}
}
