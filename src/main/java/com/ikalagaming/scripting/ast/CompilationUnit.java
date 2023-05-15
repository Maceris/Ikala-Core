package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

import java.util.stream.Collectors;

/**
 * A full script.
 *
 * @author Ches Burks
 *
 */
@Getter
@Setter
public class CompilationUnit extends Node {
	/**
	 * If we know immediately that this is not valid because we found unknown
	 * block statements on the initial build of the tree.
	 */
	private boolean invalid = false;

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
		if (!this.children.isEmpty()) {
			result.append(" {\n");
			result.append(this.children.stream()
				.map(node -> node == null ? "null" : node.toString())
				.collect(Collectors.joining(",\n\t")));
			result.append("\n} ");
		}
		return result.toString();
	}
}
