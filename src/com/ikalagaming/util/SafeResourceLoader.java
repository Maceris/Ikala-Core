package com.ikalagaming.util;

import com.ikalagaming.localization.Localization;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
	 * console. If no string is loaded, it attempts to load from the root
	 * resource bundle. If it fails again, the name is returned.
	 *
	 * @param name what to get from the bundle
	 * @param from the bundle to use
	 * @return the string from the bundle or name
	 */
	public static String getString(String name, ResourceBundle from) {
		String toReturn = name;
		boolean failed = false;
		try {
			toReturn = from.getString(name);
		}
		catch (MissingResourceException missingResource) {
			missingResource.printStackTrace(System.err);
			failed = true;
		}
		catch (ClassCastException classCast) {
			classCast.printStackTrace(System.err);
		}
		if (!failed) {
			return toReturn;
		}
		ResourceBundle rootOnly = ResourceBundle
			.getBundle(from.getBaseBundleName(), new ResourceBundle.Control() {
				@Override
				public List<Locale> getCandidateLocales(String n,
					Locale locale) {
					return Collections.singletonList(Locale.ROOT);
				}
			});
		try {
			toReturn = rootOnly.getString(name);
		}
		catch (MissingResourceException missingResource) {
			missingResource.printStackTrace(System.err);
			failed = true;
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
	 * @return the string from the bundle or the fallback
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
	 * console. If no string is loaded, it attempts to load from the root
	 * resource bundle. If it fails again, the name is returned.
	 *
	 * @param name what to get from the bundle
	 * @param from the bundle to use
	 * @return the string from the bundle or the fallback
	 */
	public static String getString(String name, String from) {
		String toReturn = name;
		ResourceBundle bundle;
		boolean failed = false;

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
		if (!failed) {
			return toReturn;
		}

		ResourceBundle rootOnly =
			ResourceBundle.getBundle(from, new ResourceBundle.Control() {
				@Override
				public List<Locale> getCandidateLocales(String n,
					Locale locale) {
					return Collections.singletonList(Locale.ROOT);
				}
			});

		try {
			toReturn = rootOnly.getString(name);
		}
		catch (MissingResourceException missingResource) {
			missingResource.printStackTrace(System.err);
			failed = true;
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
	 * @return the string from the bundle or the fallback
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
