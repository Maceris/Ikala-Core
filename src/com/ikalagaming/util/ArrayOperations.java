
package com.ikalagaming.util;

/**
 * Methods useful for Arrays.
 * 
 * @author Ches Burks
 * 
 */
public class ArrayOperations {

	/**
	 * Converts the given array into a String that can be output. The String
	 * will start with '[', end with ']' and have values separated by ','. Empty
	 * arrays will output "[]". The values are determined by the objects
	 * {@code toString()} method.
	 * 
	 * @param array the array to convert
	 * @return the formatted string
	 */
	public static String convertToString(Object[] array) {
		String output = "";

		if (array == null) {
			return "[" + "]";
		}
		if (array.length <= 0) {
			return "[" + "]";
		}
		output = output.concat("[");
		for (int i = 0; i <= array.length - 2; ++i) {
			output = output.concat(array[i].toString());
			output = output.concat(",");
		}
		output = output.concat(array[array.length - 1].toString());
		output = output.concat("]");

		return output;
	}
}
