package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * An arithmetic expression, acting on numeric types. This can have one or two
 * child nodes, depending on if it's a unary or binary operator.
 *
 * @author Ches Burks
 *
 */
@Getter
@Setter
public class ExprArithmetic extends Node {
	/**
	 * The operators that can be used.
	 *
	 * @author Ches Burks
	 *
	 */
	public enum Operator {
		/**
		 * Add the first and second number.
		 */
		ADD,
		/**
		 * Subtract the second number from the first.
		 */
		SUB,
		/**
		 * Multiply the first and second number.
		 */
		MUL,
		/**
		 * Divide the first number by the second.
		 */
		DIV,
		/**
		 * Modulus, or remainder, of dividing the first number by the second.
		 */
		MOD,
		/**
		 * A unary prefix increment.
		 */
		INC_PREFIX,
		/**
		 * A unary suffix increment.
		 */
		INC_SUFFIX,
		/**
		 * A unary prefix decrement.
		 */
		DEC_PREFIX,
		/**
		 * A unary suffix decrement.
		 */
		DEC_SUFFIX;
	}

	/**
	 * The associated operator.
	 */
	private Operator operator;

	/**
	 * The number of times the unary operator appears, since the grammar allows
	 * us to stack them.
	 */
	private int unaryCount;
}
