package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * A relational expression, comparing numeric types.
 * 
 * @author Ches Burks
 *
 */
@Getter
@Setter
public class ExprRelation extends Node {
	/**
	 * The available operators.
	 * 
	 * @author Ches Burks
	 *
	 */
	public static enum Operator {
		/**
		 * Less than.
		 */
		LT,
		/**
		 * Less than or equal to.
		 */
		LTE,
		/**
		 * Greater than.
		 */
		GT,
		/**
		 * Greater than or equal to.
		 */
		GTE;
	}

	/**
	 * The operator that is being used.
	 */
	private Operator operator;
}
