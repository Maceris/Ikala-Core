package com.ikalagaming.scripting;

/**
 * Methods with the same signatures as some in {@link DebugMethods}, used to test registering
 * classes that would conflict.
 *
 * @author Ches Burks
 */
public class DuplicateMethods {

    /**
     * Has the same signature as {@link DebugMethods#TEST_duplicate()}.
     *
     * @return The name of this class.
     */
    public static String TEST_duplicate() {
        return "DuplicateMethods";
    }

    /** Private constructor so that this class is not instantiated. */
    private DuplicateMethods() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
