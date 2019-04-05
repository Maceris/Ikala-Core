package com.ikalagaming.localization;

import java.util.Locale;

/**
 * Allows changing languages and locales in the program.
 *
 * @author Ches Burks
 *
 */
public class Localization {
	private static Locale loc = new Locale("en", "US");

	/**
	 * Returns the current locale. If no locale was set, defaults to "en_US".
	 *
	 * @return The current locale
	 */
	public static Locale getLocale() {
		return Localization.loc;
	}

	/**
	 * Sets the current locale to the given locale.
	 *
	 * @param locale The new locale to use
	 */
	public static void setLocale(Locale locale) {
		Localization.loc = locale;
	}

}
