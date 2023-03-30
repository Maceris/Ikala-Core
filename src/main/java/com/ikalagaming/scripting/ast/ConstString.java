package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * A string literal.
 * 
 * @author Ches Burks
 *
 */
@Getter
@Setter
public class ConstString extends Node {
	private String value;

	@Override
	public String toString() {
		return value;
	}
}
