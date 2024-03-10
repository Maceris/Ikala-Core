package com.ikalagaming.scripting.interpreter;

import lombok.NonNull;

/**
 * A location in memory, optionally with an immediate value.
 *
 * @author Ches Burks
 * @param area The area of memory to look at.
 * @param type The type of the contents of this memory.
 * @param value The literal value, null if this is not an immediate value.
 */
public record MemLocation(@NonNull MemArea area, @NonNull Class<?> type, Object value) {
    /**
     * Construct a new memory location without an immediate value.
     *
     * @param area The area of memory to look at.
     * @param type The literal value, null if this is not an immediate value.
     */
    public MemLocation(@NonNull MemArea area, @NonNull Class<?> type) {
        this(area, type, null);
    }

    /**
     * If this is an boolean, for convenience.
     *
     * @return True if the type was specified as an boolean, otherwise false.
     */
    public boolean isBoolean() {
        return Boolean.class.isAssignableFrom(this.type);
    }

    /**
     * If this is an character, for convenience.
     *
     * @return True if the type was specified as an character, otherwise false.
     */
    public boolean isChar() {
        return Character.class.isAssignableFrom(this.type);
    }

    /**
     * If this is an double, for convenience.
     *
     * @return True if the type was specified as an double, otherwise false.
     */
    public boolean isDouble() {
        return Double.class.isAssignableFrom(this.type);
    }

    /**
     * If this is an integer, for convenience.
     *
     * @return True if the type was specified as an integer, otherwise false.
     */
    public boolean isInt() {
        return Integer.class.isAssignableFrom(this.type);
    }

    /**
     * If this is an string, for convenience.
     *
     * @return True if the type was specified as an string, otherwise false.
     */
    public boolean isString() {
        return String.class.isAssignableFrom(this.type);
    }
}
