package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.ast.ASTVisitor;
import com.ikalagaming.scripting.ast.CompilationUnit;
import com.ikalagaming.scripting.ast.Label;
import com.ikalagaming.scripting.ast.Node;
import com.ikalagaming.scripting.ast.Type;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Finds all the labels, notes them down in the type map.
 *
 * @author Ches Burks
 */
@AllArgsConstructor
public class LabelPass implements ASTVisitor {
    private final VariableTypeMap typeMap;

    /**
     * Process the tree and update the type map (provided when this class was constructed) with all
     * the labels.
     *
     * @param root The root of the tree.
     */
    public void processLabels(@NonNull CompilationUnit root) {
        processTree(root);
    }

    /**
     * Process the tree and update the type map (provided when this class was constructed) with all
     * the labels.
     *
     * @param root The node we are currently processing.
     */
    private void processTree(Node root) {
        if (!root.getChildren().isEmpty()) {
            root.getChildren().forEach(this::processTree);
        }
        root.process(this);
    }

    @Override
    public void visit(Label node) {
        // Note down the name of the label as being a label.
        Type childType = node.getType();
        typeMap.put(childType.getValue(), childType);
    }
}
