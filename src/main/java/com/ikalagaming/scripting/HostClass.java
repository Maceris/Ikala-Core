package com.ikalagaming.scripting;

import lombok.NonNull;

/**
 * A class that a script can call static methods on, like {@code Math.min(a, b)}. Scripts get one
 * through a global, so the class is reached by name without the script being able to load classes.
 *
 * @param type The class whose public static methods are callable.
 */
public record HostClass(@NonNull Class<?> type) {
    /**
     * Wrap a class so scripts can call its static methods.
     *
     * @param type The class.
     * @return The wrapped class.
     */
    public static HostClass of(@NonNull Class<?> type) {
        return new HostClass(type);
    }

    @Override
    public String toString() {
        return type.getSimpleName();
    }
}
