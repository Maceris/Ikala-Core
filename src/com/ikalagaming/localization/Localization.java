package com.ikalagaming.localization;

import java.util.Locale;

/**
 * Allows changing languages and locales in the program.
 *
 * @author Ches Burks
 *
 */
public class Localization {

	/**
	 * Returns the static instance. Creates one if it does not exist.
	 *
	 * @return The current instance of the class
	 */
	private static Localization getInstance() {
		if (Localization.instance == null) {
			Localization.instance = new Localization();
		}
		return Localization.instance;
	}

	/**
	 * Returns the current locale. If no locale was set, defaults to "en_US".
	 *
	 * @return The current locale
	 */
	public static Locale getLocale() {
		return Localization.getInstance().locale;
	}

	/**
	 * Sets the current locale to the given locale.
	 *
	 * @param locale The new locale to use
	 */
	public static void setLocale(Locale locale) {
		Localization.getInstance().locale = locale;
	}

	private static Localization instance;

	private Locale locale = new Locale("en", "US");
}
