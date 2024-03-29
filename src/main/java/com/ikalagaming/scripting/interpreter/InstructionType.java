package com.ikalagaming.scripting.interpreter;

/**
 * The types of instructions that we can execute.
 *
 * @author Ches Burks
 */
public enum InstructionType {
    /**
     * Add one character to another. <br>
     * <br>
     * <b>Input 1:</b> A character value.<br>
     * <b>Input 2:</b> A character value.<br>
     * <b>Output:</b> The resulting character.
     */
    ADD_CHAR,
    /**
     * Add one double to another. <br>
     * <br>
     * <b>Input 1:</b> A double value.<br>
     * <b>Input 2:</b> A double value.<br>
     * <b>Output:</b> The resulting double.
     */
    ADD_DOUBLE,
    /**
     * Add one integer to another. <br>
     * <br>
     * <b>Input 1:</b> An integer value.<br>
     * <b>Input 2:</b> An integer value.<br>
     * <b>Output:</b> The resulting integer.
     */
    ADD_INT,
    /**
     * Logical and. <br>
     * <br>
     * <b>Input 1:</b> A boolean value.<br>
     * <b>Input 2:</b> A boolean value.<br>
     * <b>Output:</b> The resulting boolean.
     */
    AND,
    /**
     * Call a method. Parameters are stored on the stack. If this is a static method, we use a
     * memory location of IMMEDIATE. Otherwise the location will reflect where we find the object to
     * call a method on, whether that be the stack or a variable. <br>
     * <br>
     * <b>Input 1:</b> The location of the object and name of the method.<br>
     * <b>Input 2:</b> The number of parameters as an integer.<br>
     * <b>Output:</b> The result of the call.
     */
    CALL,
    /**
     * Cast one primitive to another and place it onto the stack. <br>
     * <br>
     * <b>Input 1:</b> The location of the result to cast.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> The type to cast to.
     */
    CAST,
    /**
     * Compare two values of similar types.
     *
     * <p>Numbers are compared as double values, with a result of less than, equal to, or greater
     * than. Anything else is compared strictly in terms of equality. <br>
     * <br>
     * <b>Input 1:</b> Any value.<br>
     * <b>Input 2:</b> Any value.<br>
     * <b>Output:</b> None, but does set the comparison register.
     */
    CMP,
    /**
     * Add one string to another. <br>
     * <br>
     * <b>Input 1:</b> A string value.<br>
     * <b>Input 2:</b> A string value.<br>
     * <b>Output:</b> The resulting string.
     */
    CONCAT_STRING,
    /**
     * Divide one character by another. <br>
     * <br>
     * <b>Input 1:</b> A character value.<br>
     * <b>Input 2:</b> A character value.<br>
     * <b>Output:</b> The resulting character.
     */
    DIV_CHAR,
    /**
     * Divide one double by another. <br>
     * <br>
     * <b>Input 1:</b> A double value.<br>
     * <b>Input 2:</b> A double value.<br>
     * <b>Output:</b> The resulting double.
     */
    DIV_DOUBLE,
    /**
     * Divide one integer by another. <br>
     * <br>
     * <b>Input 1:</b> An integer value.<br>
     * <b>Input 2:</b> An integer value.<br>
     * <b>Output:</b> The resulting integer.
     */
    DIV_INT,
    /**
     * Stops the program. <br>
     * <br>
     * <b>Input 1:</b> Ignored.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> None.
     */
    HALT,
    /**
     * Jump if two values are equal. <br>
     * <br>
     * <b>Input 1:</b> Ignored, but does read the comparison register.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> None.
     */
    JEQ,
    /**
     * Jump if the first value is greater than or equal to the second. <br>
     * <br>
     * <b>Input 1:</b> Ignored, but does read the comparison register.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> None.
     */
    JGE,
    /**
     * Jump if the first value is strictly greater than the second. <br>
     * <br>
     * <b>Input 1:</b> Ignored, but does read the comparison register.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> None.
     */
    JGT,
    /**
     * Jump if the first value is less than or equal to the second. <br>
     * <br>
     * <b>Input 1:</b> Ignored, but does read the comparison register.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> None.
     */
    JLE,
    /**
     * Jump if the first value is strictly less than the second. <br>
     * <br>
     * <b>Input 1:</b> Ignored, but does read the comparison register.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> None.
     */
    JLT,
    /**
     * Jump to another location. <br>
     * <br>
     * <b>Input 1:</b> Ignored.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> None.
     */
    JMP,
    /**
     * Jump if two values are not equal. <br>
     * <br>
     * <b>Input 1:</b> Ignored, but does read the comparison register.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> None.
     */
    JNE,
    /**
     * Calculate the remainder of dividing one character by another. <br>
     * <br>
     * <b>Input 1:</b> A character value.<br>
     * <b>Input 2:</b> A character value.<br>
     * <b>Output:</b> The resulting character.
     */
    MOD_CHAR,
    /**
     * Calculate the remainder of dividing one double by another. <br>
     * <br>
     * <b>Input 1:</b> A double value.<br>
     * <b>Input 2:</b> A double value.<br>
     * <b>Output:</b> The resulting double.
     */
    MOD_DOUBLE,
    /**
     * Calculate the remainder of dividing one integer by another. <br>
     * <br>
     * <b>Input 1:</b> An integer value.<br>
     * <b>Input 2:</b> An integer value.<br>
     * <b>Output:</b> The resulting integer.
     */
    MOD_INT,
    /**
     * Copy one value to another location. <br>
     * <br>
     * <b>Input 1:</b> The value to copy.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> The destination to copy to.
     */
    MOV,
    /**
     * Multiply one character by another. <br>
     * <br>
     * <b>Input 1:</b> A character value.<br>
     * <b>Input 2:</b> A character value.<br>
     * <b>Output:</b> The resulting character.
     */
    MUL_CHAR,
    /**
     * Multiply one double by another. <br>
     * <br>
     * <b>Input 1:</b> A double value.<br>
     * <b>Input 2:</b> A double value.<br>
     * <b>Output:</b> The resulting double.
     */
    MUL_DOUBLE,
    /**
     * Multiply one integer by another. <br>
     * <br>
     * <b>Input 1:</b> An integer value.<br>
     * <b>Input 2:</b> An integer value.<br>
     * <b>Output:</b> The resulting integer.
     */
    MUL_INT,
    /**
     * Negate the sign of a character. <br>
     * <br>
     * <b>Input 1:</b> An character value.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> The resulting character.
     */
    NEG_CHAR,
    /**
     * Negate the sign of an integer. <br>
     * <br>
     * <b>Input 1:</b> An integer value.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> The resulting integer.
     */
    NEG_INT,
    /**
     * Negate the sign of a double. <br>
     * <br>
     * <b>Input 1:</b> A double value.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> The resulting double.
     */
    NEG_DOUBLE,
    /**
     * Does nothing. <br>
     * <br>
     * <b>Input 1:</b> Ignored.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> None.
     */
    NOP,
    /**
     * Logical negation. <br>
     * <br>
     * <b>Input 1:</b> A boolean value.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> The resulting boolean.
     */
    NOT,
    /**
     * Logical or. <br>
     * <br>
     * <b>Input 1:</b> A boolean value.<br>
     * <b>Input 2:</b> A boolean value.<br>
     * <b>Output:</b> The resulting boolean.
     */
    OR,
    /**
     * Outputs a boolean based on the results of the last comparison. True if equal. <br>
     * <br>
     * <b>Input 1:</b> Ignored, but does read the comparison register.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> The boolean value.
     */
    SET_EQ,
    /**
     * Outputs a boolean based on the results of the last comparison. True if greater than or equal.
     * <br>
     * <br>
     * <b>Input 1:</b> Ignored, but does read the comparison register.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> The boolean value.
     */
    SET_GE,
    /**
     * Outputs a boolean based on the results of the last comparison. True if strictly greater than.
     * <br>
     * <br>
     * <b>Input 1:</b> Ignored, but does read the comparison register.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> The boolean value.
     */
    SET_GT,
    /**
     * Outputs a boolean based on the results of the last comparison. True if less than or equal.
     * <br>
     * <br>
     * <b>Input 1:</b> Ignored, but does read the comparison register.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> The boolean value.
     */
    SET_LE,
    /**
     * Outputs a boolean based on the results of the last comparison. True if strictly less than.
     * <br>
     * <br>
     * <b>Input 1:</b> Ignored, but does read the comparison register.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> The boolean value.
     */
    SET_LT,
    /**
     * Outputs a boolean based on the results of the last comparison. True if not equal. <br>
     * <br>
     * <b>Input 1:</b> Ignored, but does read the comparison register.<br>
     * <b>Input 2:</b> Ignored.<br>
     * <b>Output:</b> The boolean value.
     */
    SET_NE,
    /**
     * Subtract one character from another. <br>
     * <br>
     * <b>Input 1:</b> A character value.<br>
     * <b>Input 2:</b> A character value.<br>
     * <b>Output:</b> The resulting character.
     */
    SUB_CHAR,
    /**
     * Subtract one double from another. <br>
     * <br>
     * <b>Input 1:</b> A double value.<br>
     * <b>Input 2:</b> A double value.<br>
     * <b>Output:</b> The resulting double.
     */
    SUB_DOUBLE,
    /**
     * Subtract one integer from another. <br>
     * <br>
     * <b>Input 1:</b> An integer value.<br>
     * <b>Input 2:</b> An integer value.<br>
     * <b>Output:</b> The resulting integer.
     */
    SUB_INT;
}
