
package com.ikalagaming.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ikalagaming.localization.Localization;

/**
 * 
 * Adds methods for accessing resources.
 * 
 * @author Ches Burks
 * 
 */
public class SafeResourceLoader {

	/**
	 * Returns a string from the supplied bundle. Any errors are printed to
	 * console. If no string is loaded, returns the fallback.
	 * 
	 * @param name what to get from the bundle
	 * @param from the bundle to use
	 * @param fallback the string to use in the event of failure
	 * @return the string from the bundle or "ERROR"
	 */
	public static String getString(String name, ResourceBundle from,
			String fallback) {
		String toReturn = fallback;
		try {
			toReturn = from.getString(name);
		}
		catch (MissingResourceException missingResource) {
			missingResource.printStackTrace(System.err);
		}
		catch (ClassCastException classCast) {
			classCast.printStackTrace(System.err);
		}
		return toReturn;
	}

	/**
	 * Returns a string from the supplied bundle. Any errors are printed to
	 * console. If no string is loaded, returns the fallback.
	 * 
	 * @param name what to get from the bundle
	 * @param from the bundle to use
	 * @param fallback the string to use in the event of failure
	 * @return the string from the bundle or "ERROR"
	 */
	public static String getString(String name, String from, String fallback) {
		String toReturn = fallback;
		ResourceBundle bundle;
		try {
			bundle = ResourceBundle.getBundle(from, Localization.getLocale());
			toReturn = bundle.getString(name);
		}
		catch (MissingResourceException missingResource) {
			missingResource.printStackTrace(System.err);
		}
		catch (ClassCastException classCast) {
			classCast.printStackTrace(System.err);
		}
		return toReturn;
	}
}
