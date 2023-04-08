package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.VariableTypeMap;
import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.Label;
import com.ikalagaming.scripting.ast.Node;
import com.ikalagaming.scripting.ast.Type;

import lombok.AllArgsConstructor;

/**
 * Finds all the labels, notes them down in the type map.
 *
 * @author Ches Burks
 *
 */
@AllArgsConstructor
public class LabelPass implements ASTVisitor {
	private final VariableTypeMap typeMap;

	/**
	 * Process the tree and update the type map (provided when this class was
	 * constructed) with all the labels.
	 *
	 * @param root The root of the tree, should be a {@link CompilationUnit}
	 *            when called externally, but we use the Node type for easy tree
	 *            traversal.
	 */
	public void process(Node root) {
		if (root.getChildren().size() > 0) {
			root.getChildren().forEach(child -> child.process(this));
		}
	}

	@Override
	public void visit(Label node) {
		// Note down the name of the label as being a label.
		Type childType = node.getType();
		this.typeMap.put(childType.getValue(), childType);
	}
}
