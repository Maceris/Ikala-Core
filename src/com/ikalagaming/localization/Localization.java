package com.ikalagaming.localization;

import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

/**
 * Allows changing languages and locales in the program.
 *
 * @author Ches Burks
 *
 */
public class Localization {

	/**
	 * The current locale. Defaults to "en_US".
	 * 
	 * @return The current locale
	 * @param locale The new locale to use
	 */
	@SuppressWarnings("javadoc")
	@Getter
	@Setter
	private static Locale locale = new Locale("en", "US");

	/**
	 * Private constructor so this class is not instantiated.
	 */
	private Localization() {}

}
