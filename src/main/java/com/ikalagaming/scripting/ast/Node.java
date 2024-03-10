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
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class Node {
    /** The children of this node. */
    protected List<Node> children = new ArrayList<>();

    /** The type of the node. */
    protected Type type;

    /**
     * Add a child to the list of children.
     *
     * @param child The child to add.
     */
    public void addChild(Node child) {
        children.add(child);
    }

    /**
     * Allow the visitor pattern for the tree.
     *
     * @param visitor The visitor.
     */
    public abstract void process(ASTVisitor visitor);

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (type != null) {
            result.append(type.toString());
            result.append(" ");
        } else {
            result.append("____ ");
        }
        result.append(this.getClass().getSimpleName());
        if (!children.isEmpty()) {
            result.append(" { ");
            result.append(
                    children.stream()
                            .map(node -> node == null ? "null" : node.toString())
                            .collect(Collectors.joining(", ")));
            result.append(" } ");
        }
        return result.toString();
    }
}
