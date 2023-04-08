package com.ikalagaming.scripting.ast;

import com.ikalagaming.scripting.VariableTypeMap;

import lombok.Getter;
import lombok.Setter;

/**
 * An identifier.
 *
 * @author Ches Burks
 *
 */
@Getter
@Setter
public class Identifier extends Node {
	private String name;

	@Override
	public void process(ASTVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected void processType(VariableTypeMap variables) {
		this.setType(variables.get(this.name));
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
		result.append(this.name);
		return result.toString();
	}
}
