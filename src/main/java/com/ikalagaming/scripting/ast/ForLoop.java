package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * A regular for statement. Since this has 3 optional fields, we store booleans indicating whether
 * each section of the loop is filled out, so that the list of children can be understood.
 *
 * <p>Children will be the initializer (if it exists), condition (if it exists), update (if it
 * exists), and then statement in that order.
 *
 * @author Ches Burks
 */
@Getter
@Setter
public class ForLoop extends Node {
    /** Whether we have an initializer, the first of three parts of the loop. */
    private boolean initializer;

    /**
     * Whether we have the check for if we should continue, the second of three parts of the loop.
     */
    private boolean condition;

    /**
     * Whether we have the part to make updates after each loop, the third of three parts of the
     * loop.
     */
    private boolean update;

    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
