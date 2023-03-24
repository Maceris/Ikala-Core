package com.ikalagaming.scripting.ast;

import lombok.Getter;
import lombok.Setter;

/**
 * A floating point literal.
 * 
 * @author Ches Burks
 *
 */
@Getter
@Setter
public class ConstDouble extends Node {
	private double value;
}
