package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * Compares equality of two things.
 * 
 * @author Ches Burks
 *
 */
@Getter
@Setter
public class ExprEquality extends Node {
	/**
	 * The available operators for comparison.
	 * 
	 * @author Ches Burks
	 *
	 */
	public static enum Operator {
		/**
		 * If they are equal.
		 */
		EQUAL,
		/**
		 * If they are not equal.
		 */
		NOT_EQUAL;
	}

	private Operator operator;
}
