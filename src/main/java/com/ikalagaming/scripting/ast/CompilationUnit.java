package com.ikalagaming.scripting.ast;

import java.util.stream.Collectors;

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
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		if (this.type != null) {
			result.append(this.type.toString());
			result.append(" ");
		}
		else {
			result.append("____ ");
		}
		result.append(this.getClass().getSimpleName());
		if (this.children.size() > 0) {
			result.append(" {\n");
			result.append(this.children.stream()
				.map(node -> node == null ? "null" : node.toString())
				.collect(Collectors.joining(",\n\t")));
			result.append("\n} ");
		}
		return result.toString();
	}
}
