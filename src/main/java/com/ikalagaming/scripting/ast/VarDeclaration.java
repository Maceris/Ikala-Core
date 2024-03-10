package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * A variable declaration. The children will be the identifier, then an expression used to
 * initialize the variable if such an expression exists.
 *
 * @author Ches Burks
 */
@Getter
@Setter
public class VarDeclaration extends Node {
    /** The number of dimensions, which might be 0. */
    private int dimensions;

    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
