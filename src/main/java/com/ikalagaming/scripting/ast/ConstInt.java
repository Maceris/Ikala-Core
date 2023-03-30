package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * An integer literal.
 * 
 * @author Ches Burks
 *
 */
@Getter
@Setter
public class ConstInt extends Node {
	private int value;

	@Override
	public String toString() {
		return Integer.toString(value);
	}
}
