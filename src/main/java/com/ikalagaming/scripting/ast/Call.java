package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * A method call. The first child will be the object or method name, then the second may be an
 * method name, and there may be an argument list.
 *
 * @author Ches Burks
 */
@Getter
@Setter
public class Call extends Node {
    /**
     * Whether we have a primary expression to evaluate to an object. If this is false, the first
     * child is an identifier, and if true the first child is the expression.
     *
     * @param primary Whether we have a primary expression.
     * @return Whether we have a primary expression.
     */
    private boolean primary = false;

    /**
     * Used to signify that the return value of this call is not used for anything, and does not
     * need to be stored on the stack.
     */
    private boolean ignoreResult;

    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
