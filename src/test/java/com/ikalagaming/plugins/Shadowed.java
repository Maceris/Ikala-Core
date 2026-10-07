package com.ikalagaming.plugins;

/**
 * A class on the test classpath that a test plugin also contains a different version of, to check
 * that a plugin's own jar takes precedence.
 *
 * @author Ches Burks
 */
public final class Shadowed {
    /**
     * Where this version of the class came from.
     *
     * @return Where the class came from.
     */
    public static String origin() {
        return "classpath";
    }

    /** Private constructor so this class is not instantiated. */
    private Shadowed() {}
}
