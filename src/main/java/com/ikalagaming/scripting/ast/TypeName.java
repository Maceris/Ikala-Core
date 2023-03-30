package com.ikalagaming.scripting.ast;

import com.ikalagaming.scripting.VariableTypeMap;

import lombok.Getter;

/**
 * A type name, which is a list of identifiers.
 *
 * @author Ches Burks
 *
 */
@Getter
public class TypeName extends Node {
	@Override
	protected void processType(VariableTypeMap variables) {
		if (this.children.size() == 1) {
			Identifier id = (Identifier) this.children.get(0);
			if (variables.contains(id.getName())) {
				final Type idType = variables.get(id.getName());
				this.setType(idType);
				id.setType(idType);
				return;
			}
		}
		for (Node node : this.children) {
			node.setType(Type.unknownType());
		}
		this.setType(Type.unknownType());
	}
}
