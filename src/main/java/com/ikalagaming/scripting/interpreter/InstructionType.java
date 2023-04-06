package com.ikalagaming.scripting.interpreter;

/**
 * The types of instructions that we can execute.
 * 
 * @author Ches Burks
 *
 */
enum InstructionType {
	/**
	 * Add one character to another.
	 */
	ADD_CHAR,
	/**
	 * Add one double to another.
	 */
	ADD_DOUBLE,
	/**
	 * Add one integer to another.
	 */
	ADD_INT,
	/**
	 * Logical and.
	 */
	AND,
	/**
	 * Call a method.
	 */
	CALL,
	/**
	 * Compare two values.
	 */
	CMP,
	/**
	 * Add one string to another.
	 */
	CONCAT_STRING,
	/**
	 * Divide one character by another.
	 */
	DIV_CHAR,
	/**
	 * Divide one double by another.
	 */
	DIV_DOUBLE,
	/**
	 * Divide one integer by another.
	 */
	DIV_INT,
	/**
	 * Jump if two values are equal.
	 */
	JEQ,
	/**
	 * Jump if the first value is greater than or equal to the second.
	 */
	JGE,
	/**
	 * Jump if the first value is strictly greater than the second.
	 */
	JGT,
	/**
	 * Jump if the first value is less than or equal to the second.
	 */
	JLE,
	/**
	 * Jump if the first value is strictly less than the second.
	 */
	JLT,
	/**
	 * Jump to another location.
	 */
	JMP,
	/**
	 * Jump if two values are not equal.
	 */
	JNE,
	/**
	 * Calculate the remainder of dividing one character by another.
	 */
	MOD_CHAR,
	/**
	 * Calculate the remainder of dividing one double by another.
	 */
	MOD_DOUBLE,
	/**
	 * Calculate the remainder of dividing one integer by another.
	 */
	MOD_INT,
	/**
	 * Copy one value to another location.
	 */
	MOV,
	/**
	 * Multiply one character by another.
	 */
	MUL_CHAR,
	/**
	 * Multiply one double by another.
	 */
	MUL_DOUBLE,
	/**
	 * Multiply one integer by another.
	 */
	MUL_INT,
	/**
	 * Does nothing.
	 */
	NOP,
	/**
	 * Logical negation.
	 */
	NOT,
	/**
	 * Logical or.
	 */
	OR,
	/**
	 * Subtract one character from another.
	 */
	SUB_CHAR,
	/**
	 * Subtract one double from another.
	 */
	SUB_DOUBLE,
	/**
	 * Subtract one integer from another.
	 */
	SUB_INT;
}
