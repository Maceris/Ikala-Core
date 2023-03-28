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

	/**
	 * Process the types for the tree.
	 */
	public void processTreeTypes() {
		if (this.children.size() > 0) {
			this.children.forEach(Node::processType);
		}
		this.processType();
	}

	/**
	 * Process the types for the node, updating them if we can determine what
	 * they are based on it's children.
	 */
	protected void processType() {}

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

	/**
	 * Perform node-specific validation. Things like basic type checking, or
	 * semantic analysis.
	 *
	 * @return True if the node is valid given it's contents and children, false
	 *         if is not valid.
	 */
	protected boolean validate() {
		// Default
		return true;
	}

	/**
	 * Validates the tree.
	 *
	 * @return True if the tree is valid, false if anything was not.
	 */
	public boolean validateTree() {
		boolean valid = true;
		if (this.children.size() > 0) {
			valid = this.children.stream().map(Node::validateTree).collect(
				Collectors.reducing(Boolean.TRUE, Boolean::logicalAnd));
		}
		// Don't short circuit
		return valid & this.validate();
	}
}
