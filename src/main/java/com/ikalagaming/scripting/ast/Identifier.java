package com.ikalagaming.scripting.ast;

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
}
