package com.ikalagaming.scripting.ast;

import lombok.AllArgsConstructor;
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
	@AllArgsConstructor
	public enum Operator {
		/**
		 * And logic.
		 */
		AND("&&"),
		/**
		 * Or logic.
		 */
		OR("||"),
		/**
		 * Not, or inverting, logic.
		 */
		NOT("!");

		private final String value;
	}

	private Operator operator;

	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		if (this.type != null) {
			result.append(this.type.toString());
			result.append(" ");
		}
		else {
			result.append("____ ");
		}

		result.append(this.getClass().getSimpleName());

		result.append(" { ");
		switch (this.operator) {
			case AND:
			case OR:
				result.append(this.children.get(0).toString());
				result.append(' ');
				result.append(this.operator.value);
				result.append(' ');
				result.append(this.children.get(1).toString());
				break;
			case NOT:
				result.append(this.operator.value);
				result.append(' ');
				result.append(this.children.get(0).toString());
				break;
			default:
				break;
		}
		result.append(" } ");

		return result.toString();
	}
}
