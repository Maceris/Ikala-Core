package com.ikalagaming.scripting.ast;

import com.ikalagaming.scripting.VariableTypeMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * An assignment expression. The children are the two sides of the expression.
 *
 * @author Ches Burks
 *
 */
@Getter
@Setter
public class ExprAssign extends Node {
	/**
	 * The operators that can be used for assignment.
	 *
	 * @author Ches Burks
	 *
	 */
	@AllArgsConstructor
	public enum Operator {
		/**
		 * "=", plain assignment.
		 */
		ASSIGN("="),
		/**
		 * "*=", only reasonable for numbers.
		 */
		MUL_ASSIGN("*="),
		/**
		 * "/=", only reasonable for numbers.
		 */
		DIV_ASSIGN("/="),
		/**
		 * "%=", only reasonable for numbers.
		 */
		MOD_ASSIGN("%="),
		/**
		 * "+=", only reasonable for numbers, or strings.
		 */
		ADD_ASSIGN("+="),
		/**
		 * "-=", only reasonable for numbers.
		 */
		SUB_ASSIGN("-=");

		/**
		 * Convert a tokens text to the corresponding operator.
		 *
		 * @param text The text version of a token, like "+=".
		 * @return The corresponding operator value.
		 * @throws IllegalArgumentException If no token matches.
		 */
		public static Operator fromText(@NonNull String text) {
			for (Operator o : Operator.values()) {
				if (o.value.equals(text)) {
					return o;
				}
			}
			throw new IllegalArgumentException("Unknown operator " + text);
		}

		private final String value;
	}

	private Operator operator;

	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected void processType(VariableTypeMap variables) {
		this.setType(this.children.get(0).type);
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
