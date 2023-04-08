package com.ikalagaming.scripting.ast;

import com.ikalagaming.scripting.VariableTypeMap;

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
	protected List<Node> children = new ArrayList<>();

	/**
	 * The type of the node.
	 */
	protected Type type;

	/**
	 * Add a child to the list of children.
	 *
	 * @param child The child to add.
	 */
	public void addChild(Node child) {
		this.children.add(child);
	}

	/**
	 * Find all of the labels, and add them to the type map.
	 *
	 * @param variables The variables that are valid for this node.
	 */
	private final void populateLabels(VariableTypeMap variables) {
		if (this.children.size() > 0) {
			for (Node child : this.children) {
				if (child instanceof Label) {
					Type childType = child.getType();
					variables.put(childType.getValue(), childType);
				}
				else {
					child.populateLabels(variables);
				}
			}
		}
	}

	/**
	 * Allow the visitor pattern for the tree.
	 *
	 * @param visitor The visitor.
	 */
	public abstract void process(ASTVisitor visitor);

	/**
	 * Process the types for the tree. Intended for use only on the root node.
	 */
	public final void processTreeTypes() {
		VariableTypeMap variables = new VariableTypeMap();
		this.populateLabels(variables);
		this.processTreeTypes(variables);
	}

	/**
	 * Process the types for the tree. Intended for use only on the root node.
	 *
	 * @param variables The variables that are valid for this node.
	 */
	private final void processTreeTypes(VariableTypeMap variables) {
		if (this.children.size() > 0) {
			for (Node child : this.children) {
				if (child instanceof Block || child instanceof ForLoop) {
					child.processTreeTypes(variables.clone());
				}
				else {
					child.processTreeTypes(variables);
				}
			}
		}
		this.processType(variables);
	}

	/**
	 * Process the types for the node, updating them if we can determine what
	 * they are based on it's children.
	 *
	 * @param variables The variables that are valid for this node.
	 */
	protected void processType(VariableTypeMap variables) {}

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
	 * Validates the tree. Intended for use only on the root node.
	 *
	 * @return True if the tree is valid, false if anything was not.
	 */
	public final boolean validateTree() {
		boolean valid = true;
		for (Node child : this.children) {
			valid &= child.validateTree();
		}
		// Don't short circuit
		return valid & this.validate();
	}
}
