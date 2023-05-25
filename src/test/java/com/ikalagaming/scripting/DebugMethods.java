package com.ikalagaming.scripting;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides methods used to test the scripting system.
 *
 * @author Ches Burks
 *
 */
public class DebugMethods {

	/**
	 * The output from the program.
	 */
	@Getter
	private static List<String> output = new ArrayList<>();

	/**
	 * Reset for the next test. Intended to be used by the test suite.
	 */
	public static void reset() {
		DebugMethods.output.clear();
	}

	/**
	 * Records a string as if printing. To be called by scripts to validate they
	 * function as expected.
	 *
	 * @param string The string to record.
	 */
	public static void TEST_printString(String string) {
		DebugMethods.output.add(string);
	}

	/**
	 * Private constructor so that this class is not instantiated.
	 */
	private DebugMethods() {
		throw new UnsupportedOperationException(
			"This utility class should not be instantiated");
	}
}
