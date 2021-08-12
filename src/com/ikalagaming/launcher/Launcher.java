package com.ikalagaming.launcher;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.localization.Localization;
import com.ikalagaming.logging.Logging;
import com.ikalagaming.plugins.PluginManager;

import java.util.Arrays;
import java.util.Locale;

/**
 * Contains functionality for controling the lifecycle of the framework, and the
 * main method for running from command line.
 *
 * @author Ches Burks
 *
 */
public class Launcher {

	/**
	 * Set up the main systems.
	 */
	public static void initialize() {
		// creates all the static instances
		EventManager.getInstance();
		Logging.create();
		PluginManager.getInstance();
	}

	private static boolean isFlag(final String argument) {
		return argument.startsWith("-");
	}

	/**
	 * Processes the command line arguments and launches the framework.
	 *
	 * @param args Arguments to the program from command line.
	 */
	public static void main(String[] args) {
		Launcher.processArguments(args);
		Launcher.initialize();
	}

	private static void printHelp() {
		System.out.println("Usage:");
		System.out.println("\tjava -jar Ikala-Core.jar [OPTIONS...]");

		System.out.println("Examples:");
		System.out.println("\tjava -jar Ikala-Core.jar --help");
		System.out.println("\tjava -jar Ikala-Core.jar -l fr -c CA");

		System.out.println("Options:");
		System.out.println("\t-h --help \t\t\tShows the help text");
		System.out.println(
			"\t-l, --language <language>\tSets the language to use for localization");
		System.out.println(
			"\t-c, --country <country>\t\tSets the country to use for localization (please also provide language)");
	}

	private static void processArguments(final String[] args) {
		String language = null;
		String country = null;

		for (int i = 0; i < args.length; ++i) {
			final String arg = args[i];
			if (!Launcher.isFlag(arg)) {
				Launcher.printHelp();
				return;
			}
			if ("-l".equalsIgnoreCase(arg)
				|| "--language".equalsIgnoreCase(arg)) {
				if (i >= args.length - 1 || Launcher.isFlag(args[i + 1])) {
					System.out.println("Please enter a language.");
					System.out.println();
					Launcher.printHelp();
					return;
				}
				language = args[i + 1];
				// skip the next argument, we consumed it
				++i;
				continue;
			}
			if ("-c".equalsIgnoreCase(arg)
				|| "--country".equalsIgnoreCase(arg)) {
				if (i >= args.length - 1 || Launcher.isFlag(args[i + 1])) {
					System.out.println("Please enter a country.");
					System.out.println();
					Launcher.printHelp();
					return;
				}
				country = args[i + 1];
				// skip the next argument, we consumed it
				++i;
				continue;
			}
		}
		if (null != language) {
			Launcher.setLocale(language, country);
		}
	}

	/**
	 * Sets the language/country. If language is null, or either language or
	 * country are not valid ISO values, this will fail. Returns the status of
	 * whether or not it should have worked.
	 *
	 * @param language The language to use in ISO format ("fr", "en").
	 * @param country The country, in ISO format ("CN", "US")
	 * @return True if we tried to set the locale, false if input was not valid.
	 */
	private static boolean setLocale(final String language,
		final String country) {
		if (null == language) {
			Launcher.printHelp();
			return false;
		}

		// check that the language is valid
		int index = Arrays.binarySearch(Locale.getISOLanguages(), language);
		if (index < 0) {
			System.out
				.println("'" + language + "' is not a valid ISO language.");
			Launcher.printHelp();
			return false;
		}

		if (null == country) {
			Locale locale = new Locale(language);
			if (Arrays.stream(Locale.getAvailableLocales())
				.noneMatch(locale::equals)) {
				System.out
					.println("'" + locale + "' is not a supported locale.");
				Launcher.printHelp();
				return false;
			}
			Localization.setLocale(locale);
			return true;
		}

		// check that the country is valid if provided
		index = Arrays.binarySearch(Locale.getISOCountries(), country);
		if (index < 0) {
			System.out.println("'" + country + "' is not a valid ISO country.");
			Launcher.printHelp();
			return false;
		}
		Locale locale = new Locale(language, country);
		if (Arrays.stream(Locale.getAvailableLocales())
			.noneMatch(locale::equals)) {
			System.out.println("'" + locale + "' is not a supported locale.");
			Launcher.printHelp();
			return false;
		}
		Localization.setLocale(locale);
		return true;
	}

	/**
	 * Shut down all the main systems, unloads everything.
	 */
	public static void shutdown() {
		PluginManager.destoryInstance();
		Logging.destory();
		EventManager.destoryInstance();
	}

	/**
	 * Private constructor so this class is not instantiated.
	 */
	private Launcher() {}
}
