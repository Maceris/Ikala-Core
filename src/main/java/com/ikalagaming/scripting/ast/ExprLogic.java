package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * A logical expression. May have one or two children, depending on if it's a
 * unary or binary operator.
 * 
 * @author Ches Burks
 *
 */
@Getter
@Setter
public class ExprLogic extends Node {
	/**
	 * The operator that is used.
	 * 
	 * @author Ches Burks
	 *
	 */
	public static enum Operator {
		/**
		 * And logic.
		 */
		AND,
		/**
		 * Or logic.
		 */
		OR,
		/**
		 * Not, or inverting, logic.
		 */
		NOT;
	}

	private Operator operator;
}
