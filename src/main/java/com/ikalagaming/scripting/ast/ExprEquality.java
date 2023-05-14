package com.ikalagaming.scripting.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Compares equality of two things. There are two children, the nodes being
 * compared.
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
	@AllArgsConstructor
	public enum Operator {
		/**
		 * If they are equal.
		 */
		EQUAL("=="),
		/**
		 * If they are not equal.
		 */
		NOT_EQUAL("!=");

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
		result.append(this.children.get(0).toString());
		result.append(' ');
		result.append(this.operator.value);
		result.append(' ');
		result.append(this.children.get(1).toString());
		result.append(" } ");

		return result.toString();
	}
}
