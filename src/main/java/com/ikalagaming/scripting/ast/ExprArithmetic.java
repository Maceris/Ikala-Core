package com.ikalagaming.scripting.ast;

import com.ikalagaming.scripting.VariableTypeMap;
import com.ikalagaming.scripting.ast.Type.Base;

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
	protected void processType(VariableTypeMap variables) {
		if (this.children.size() < 1) {
			ExprArithmetic.log.warn("Missing child for node {}",
				this.toString());
			return;
		}
		final Type firstType = this.children.get(0).type;

		Type secondType;
		if (this.children.size() == 2) {
			secondType = this.children.get(1).type;
		}
		else {
			secondType = Type.voidType();
		}

		switch (this.operator) {
			case DIV:
			case MUL:
				if (this.children.size() < 2) {
					ExprArithmetic.log.warn("Missing second child for node {}",
						this.toString());
					return;
				}
			case ADD:
			case SUB:
				if (this.children.size() < 2) {
					this.setType(firstType);
					break;
				}
				// invalid types
				if (firstType.anyOf(Base.BOOLEAN, Base.IDENTIFIER, Base.VOID)
					|| secondType.anyOf(Base.BOOLEAN, Base.IDENTIFIER,
						Base.VOID)) {
					ExprArithmetic.log.warn("Invalid types {} {} {}",
						firstType.toString(), this.operator.getReadable(),
						secondType.toString());
					return;
				}

				// Unknown
				if (firstType.anyOf(Base.UNKNOWN)
					|| secondType.anyOf(Base.UNKNOWN)) {
					this.setType(Type.unknownType());
					break;
				}

				// Same type
				if (firstType.equals(secondType)) {
					this.setType(firstType);
					break;
				}

				// different types

				if (firstType.anyOf(Base.STRING)
					|| secondType.anyOf(Base.STRING)) {
					// String concatenation
					this.setType(firstType);
					break;
				}
				if (firstType.anyOf(Base.DOUBLE)
					&& secondType.anyOf(Base.CHAR, Base.INT)) {
					this.setType(firstType);
					break;
				}
				if (secondType.anyOf(Base.DOUBLE)
					&& firstType.anyOf(Base.CHAR, Base.INT)) {
					this.setType(secondType);
					break;
				}
				if (firstType.anyOf(Base.INT) && secondType.anyOf(Base.CHAR)) {
					this.setType(firstType);
					break;
				}
				if (secondType.anyOf(Base.INT) && firstType.anyOf(Base.CHAR)) {
					this.setType(secondType);
					break;
				}
				ExprArithmetic.log.warn(
					"Cannot automatically cast types {} and {}",
					firstType.toString(), secondType.toString());
				break;

			case DEC_PREFIX:
			case DEC_SUFFIX:
			case INC_PREFIX:
			case INC_SUFFIX:
				if (firstType.anyOf(Base.INT, Base.CHAR, Base.DOUBLE,
					Base.UNKNOWN)) {
					this.setType(firstType);
				}
				else {
					ExprArithmetic.log.warn("Invalid type {}",
						firstType.toString());
				}
				break;
			case MOD:
				if (this.children.size() < 2) {
					ExprArithmetic.log.warn("Missing second child for node {}",
						this.toString());
					return;
				}
				if ((firstType.anyOf(Base.INT) && secondType.anyOf(Base.INT))
					|| (firstType.anyOf(Base.DOUBLE)
						&& secondType.anyOf(Base.INT, Base.DOUBLE))) {
					this.setType(firstType);
					break;
				}
				ExprArithmetic.log.warn(
					"Cannot automatically cast types {} and {}",
					firstType.toString(), secondType.toString());
				break;
			default:
				ExprArithmetic.log.warn("Unknown operator {}",
					this.operator.toString());
				break;
		}
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

		switch (this.operator) {
			case ADD:
			case MOD:
			case MUL:
			case SUB:
			case DIV:
				final String secondChild = this.children.get(1).toString();
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
			case DIV:
			case MUL:
			case SUB:
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
