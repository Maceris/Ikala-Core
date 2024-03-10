package com.ikalagaming.scripting.interpreter;

/**
 * The types of locations we can reference.
 *
 * @author Ches Burks
 */
public enum MemArea {
    /** An item stored on the stack. */
    STACK,
    /** An immediate value. */
    IMMEDIATE,
    /** An item stored in a variable. */
    VARIABLE;
}
