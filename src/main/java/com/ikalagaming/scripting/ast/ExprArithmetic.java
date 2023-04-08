package com.ikalagaming.scripting.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * An arithmetic expression, acting on numeric types. This can have one or two
 * child nodes, depending on if it's a unary or binary operator.
 *
 * @author Ches Burks
 *
 */
@Getter
@Setter
@Slf4j
public class ExprArithmetic extends Node {
	/**
	 * The operators that can be used.
	 *
	 * @author Ches Burks
	 *
	 */
	@AllArgsConstructor
	@Getter
	public enum Operator {
		/**
		 * Add the first and second number.
		 */
		ADD("+"),
		/**
		 * Subtract the second number from the first.
		 */
		SUB("-"),
		/**
		 * Multiply the first and second number.
		 */
		MUL("*"),
		/**
		 * Divide the first number by the second.
		 */
		DIV("/"),
		/**
		 * Modulus, or remainder, of dividing the first number by the second.
		 */
		MOD("%"),
		/**
		 * A unary prefix increment.
		 */
		INC_PREFIX("++"),
		/**
		 * A unary suffix increment.
		 */
		INC_SUFFIX("++"),
		/**
		 * A unary prefix decrement.
		 */
		DEC_PREFIX("--"),
		/**
		 * A unary suffix decrement.
		 */
		DEC_SUFFIX("--");

		private String readable;
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

		final String firstChild = this.children.get(0).toString();

		final String secondChild =
			this.children.size() > 1 ? this.children.get(1).toString() : "";

		switch (this.operator) {
			case ADD:
			case SUB:
				if (this.children.size() == 1) {
					result.append(this.operator.getReadable());
					result.append(firstChild);
					break;
				}
			case MOD:
			case MUL:
			case DIV:
				result.append(firstChild);
				result.append(' ');
				result.append(this.operator.getReadable());
				result.append(' ');
				result.append(secondChild);
				break;
			case DEC_PREFIX:
			case INC_PREFIX:
				result.append(this.operator.getReadable());
				result.append(' ');
				result.append(firstChild);
				break;
			case DEC_SUFFIX:
			case INC_SUFFIX:
				result.append(firstChild);
				result.append(' ');
				result.append(this.operator.getReadable());
				break;
			default:
				result.append(this.operator.toString());
				break;
		}

		if (this.children.size() > 2) {
			result.append("Extra nodes ( ");
			for (int i = 2; i < this.children.size(); ++i) {
				result.append(this.children.get(i).toString());
				if (i < this.children.size() - 1) {
					result.append(", ");
				}
			}
			result.append(" ) ");
		}
		result.append(" } ");
		return result.toString();
	}

	@Override
	protected boolean validate() {
		if (this.children.size() < 1) {
			ExprArithmetic.log.warn("Missing first child for node {}",
				this.toString());
			return false;
		}
		switch (this.operator) {
			case ADD:
			case SUB:
				break;
			case DIV:
			case MUL:
			case MOD:
				if (this.children.size() < 2) {
					ExprArithmetic.log.warn("Missing second child for node {}",
						this.toString());
					return false;
				}
				break;
			case DEC_PREFIX:
			case DEC_SUFFIX:
			case INC_PREFIX:
			case INC_SUFFIX:
				break;
			default:
				ExprArithmetic.log.warn("Unknown operator {}",
					this.operator.toString());
				return false;
		}
		return true;
	}
}
