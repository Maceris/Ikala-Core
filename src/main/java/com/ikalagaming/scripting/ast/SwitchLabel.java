package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * A switch label. Contains an expression if not the default.
 *
 * @author Ches Burks
 */
@Getter
@Setter
public class SwitchLabel extends Node {
    /** Whether the label is a default label. */
    private boolean isDefault;

    @Override
    public void process(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
