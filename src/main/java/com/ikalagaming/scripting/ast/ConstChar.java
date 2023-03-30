package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * A character literal.
 * 
 * @author Ches Burks
 *
 */
@Getter
@Setter
public class ConstChar extends Node {
	private char value;

	@Override
	public String toString() {
		return "'" + value + "'";
	}
}
