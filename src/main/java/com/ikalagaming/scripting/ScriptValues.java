package com.ikalagaming.scripting;

/**
 * Converts values coming from Java into the types scripts can compute with. Scripts only have int,
 * double, char, boolean and string, so other numbers would be values they can store but not use.
 */
public final class ScriptValues {
    /**
     * Convert a value from Java into a type scripts can compute with. Longs, shorts and bytes
     * become ints, and floats become doubles. A long too big for an int becomes a double, which
     * keeps its size at the cost of precision. Everything else, including null, is kept as it is.
     *
     * @param value The value from Java.
     * @return The value to give the script.
     */
    public static Object normalize(Object value) {
        if (value instanceof Long number) {
            final long whole = number;
            if (whole >= Integer.MIN_VALUE && whole <= Integer.MAX_VALUE) {
                return (int) whole;
            }
            return (double) whole;
        }
        if (value instanceof Short || value instanceof Byte) {
            return ((Number) value).intValue();
        }
        if (value instanceof Float number) {
            return number.doubleValue();
        }
        return value;
    }

    /** Static methods only. */
    private ScriptValues() {}
}
