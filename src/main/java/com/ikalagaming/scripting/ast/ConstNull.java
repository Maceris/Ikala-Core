package com.ikalagaming.scripting.ast;

/**
 * A null literal value, with a void type.
 * 
 * @author Ches Burks
 *
 */
public class ConstNull extends Node {
	/**
	 * Create a null constant.
	 */
	public ConstNull() {
		this.setType(Type.voidType());
	}
}
