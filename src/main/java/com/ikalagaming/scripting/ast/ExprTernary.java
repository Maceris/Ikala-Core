package com.ikalagaming.scripting.ast;

import com.ikalagaming.scripting.VariableTypeMap;
import com.ikalagaming.scripting.ast.Type.Base;

import lombok.extern.slf4j.Slf4j;

/**
 * A ternary expression. The children are the conditional, value if true, then
 * value if false.
 *
 * @author Ches Burks
 *
 */
@Slf4j
public class ExprTernary extends Node {
	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected void processType(VariableTypeMap variables) {
		Type ifTrue = this.children.get(1).getType();
		Type ifFalse = this.children.get(2).getType();

		if (ifTrue.equals(ifFalse)) {
			this.setType(ifTrue);
			return;
		}

		// Both numeric
		if (ifTrue.anyOf(Base.CHAR, Base.INT, Base.DOUBLE, Base.UNKNOWN)
			&& ifFalse.anyOf(Base.CHAR, Base.INT, Base.DOUBLE, Base.UNKNOWN)) {
			// go with the broadest type
			if (ifTrue.anyOf(Base.DOUBLE)) {
				this.setType(ifTrue);
				return;
			}
			if (ifFalse.anyOf(Base.DOUBLE)) {
				this.setType(ifFalse);
				return;
			}
			if (ifTrue.anyOf(Base.INT)) {
				this.setType(ifTrue);
				return;
			}
			if (ifFalse.anyOf(Base.INT)) {
				this.setType(ifFalse);
				return;
			}
			if (ifTrue.anyOf(Base.CHAR)) {
				this.setType(ifTrue);
				return;
			}
			if (ifFalse.anyOf(Base.CHAR)) {
				this.setType(ifFalse);
				return;
			}
		}

		ExprTernary.log.warn("Non-matching types {} and {}", ifTrue, ifFalse);
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
		result.append(" ? ");
		result.append(this.children.get(1).toString());
		result.append(" : ");
		result.append(this.children.get(2).toString());
		result.append(" } ");

		return result.toString();
	}
}
