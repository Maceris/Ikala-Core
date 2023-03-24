package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A node in the abstract syntax tree.
 *
 * @author Ches Burks
 *
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class Node {
	/**
	 * The children of this node.
	 */
	List<Node> children = new ArrayList<>();

	/**
	 * The type of the node.
	 */
	Type type;

	/**
	 * Add a child to the list of children.
	 *
	 * @param child The child to add.
	 */
	public void addChild(Node child) {
		this.children.add(child);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.getClass().getSimpleName());
		if (this.type != null) {
			result.append("(");
			result.append(this.type.toString());
			result.append(")");
		}
		if (this.children.size() > 0) {
			result.append(" { ");
			result.append(this.children.stream()
				.map(node -> node == null ? "null" : node.toString())
				.collect(Collectors.joining(", ")));
			result.append(" } ");
		}
		return result.toString();
	}
}
