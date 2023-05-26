package com.ikalagaming.scripting;

import lombok.Getter;
import lombok.Setter;

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
	 * Used for testing method calls, object typing.
	 *
	 * @author Ches Burks
	 *
	 */
	@Getter
	@Setter
	public static class TestObject {
		/**
		 * An integer value.
		 */
		int integer;
		/**
		 * A double value.
		 */
		double doub;
		/**
		 * A string value.
		 */
		String string;
	}

	/**
	 * The output from the program.
	 */
	@Getter
	private static List<String> output = new ArrayList<>();
	/**
	 * Checks if we have successfully validated the values in
	 * {@link #TEST_checkValues(String, int, double, TestObject)}.
	 */
	@Getter
	private static boolean checkValuesMatched = false;

	/**
	 * Reset for the next test. Intended to be used by the test suite.
	 */
	public static void reset() {
		DebugMethods.output.clear();
		DebugMethods.checkValuesMatched = false;
	}

	/**
	 * Checks that the values in the parameters equal to the same ones returned
	 * by the getters in this class.
	 *
	 * @param string The string that should match the value given by
	 *            {@link #TEST_getString()}.
	 * @param integer The integer that should match the value given by
	 *            {@link #TEST_getInt()}.
	 * @param doub The string that should match the value given by
	 *            {@link #TEST_getDouble()}.
	 * @param o The object that should exist and contain values that match the
	 *            aforementioned ones.
	 */
	public static void TEST_checkValues(String string, int integer, double doub,
		TestObject o) {
		if (string == null || !string.equals(DebugMethods.TEST_getString())
			|| (integer != DebugMethods.TEST_getInt())
			|| (doub != DebugMethods.TEST_getDouble())) {
			DebugMethods.checkValuesMatched = false;
			return;
		}
		if ((o == null) || o.getString() == null
			|| !o.getString().equals(DebugMethods.TEST_getString())
			|| (o.getInteger() != DebugMethods.TEST_getInt())
			|| (o.getDoub() != DebugMethods.TEST_getDouble())) {
			DebugMethods.checkValuesMatched = false;
			return;
		}
		DebugMethods.checkValuesMatched = true;
	}

	/**
	 * Fetches a static double.
	 *
	 * @return The double.
	 */
	public static double TEST_getDouble() {
		return 120.9214;
	}

	/**
	 * Fetches a static integer.
	 *
	 * @return The integer.
	 */
	public static int TEST_getInt() {
		return -4567;
	}

	/**
	 * Returns a test object.
	 *
	 * @return The new object.
	 */
	public static TestObject TEST_getObject() {
		return new TestObject();
	}

	/**
	 * Fetches a static string.
	 *
	 * @return The string.
	 */
	public static String TEST_getString() {
		return "Sample string!";
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
