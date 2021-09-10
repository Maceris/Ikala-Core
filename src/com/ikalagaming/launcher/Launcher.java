package com.ikalagaming.launcher;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.localization.Localization;
import com.ikalagaming.plugins.Plugin;
import com.ikalagaming.plugins.PluginManager;
import com.ikalagaming.plugins.events.PluginCommandSent;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntSupplier;

/**
 * Contains functionality for controling the lifecycle of the framework, and the
 * main method for running from command line.
 *
 * @author Ches Burks
 *
 */
@Slf4j
public class Launcher {

	private static ResourceBundle bundle;

	private static List<IntSupplier> mainThreadStages;

	/**
	 * The status for a main thread stage indicating everything is fine.
	 */
	public static final int STATUS_OK = 0;

	private static Scanner commandLine;

	private static AtomicBoolean shouldShutdown = new AtomicBoolean(false);

	/**
	 * Used to exit the program.
	 */
	private static String stopCommand;

	/**
	 * Add a stage to the main thread loop. The main loop will go through and
	 * run each stage in order on the main thread. This returns a status value
	 * indicating whether or not everything is fine.
	 * 
	 * @param stage The method to execute each loop.
	 */
	public static void addMainThreadStage(IntSupplier stage) {
		synchronized (Launcher.mainThreadStages) {
			Launcher.mainThreadStages.add(stage);
		}

	}

	/**
	 * Run all stages until told to stop.
	 */
	private static void mainLoop() {
		while (true) {
			if (shouldShutdown.get()) {
				break;
			}
			synchronized (Launcher.mainThreadStages) {
				for (IntSupplier stage : Launcher.mainThreadStages) {
					int status = stage.getAsInt();
					if (STATUS_OK != status) {
						// TODO log issue
					}
				}
			}
		}
	}

	/**
	 * Set up the main systems.
	 */
	public static void initialize() {
		Launcher.mainThreadStages = new ArrayList<>();
		Launcher.bundle = ResourceBundle.getBundle(
			"com.ikalagaming.launcher.resources.Launcher",
			Localization.getLocale());
		Launcher.log
			.info("=====================================================");
		Launcher.log.info(
			SafeResourceLoader.getString("STARTING_MESSAGE", Launcher.bundle));
		Launcher.log
			.info("=====================================================");
		// creates all the static instances
		EventManager.getInstance();
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
		if (!Launcher.processArguments(args)) {
			return;
		}
		Launcher.setupMainFolders();
		Launcher.initialize();
		PluginManager.getInstance().setCommandLine(true);
		PluginManager.getInstance().loadAllPlugins(
			System.getProperty("user.dir") + Constants.PLUGIN_FOLDER_PATH);
		Launcher.setupPluginFolders();

		stopCommand = SafeResourceLoader.getString("STOP_COMMAND",
			Launcher.bundle, "stop");
		String stopMessage = SafeResourceLoader.getString("STOP_MESSAGE",
			Launcher.bundle, "Type '%s' to exit the program\n");
		System.out.printf(stopMessage, stopCommand);
		commandLine = new Scanner(System.in);
		new Thread(Launcher::readInput, "CmdInput").start();
		mainLoop();
		commandLine.close();
		Launcher.shutdown();
	}

	private static void printHelp() {
		System.out.println("Usage:");
		System.out.println("\tjava -jar Ikala-Core.jar [OPTIONS...]");
		System.out.println();

		System.out.println("Examples:");
		System.out.println("\tjava -jar Ikala-Core.jar --help");
		System.out.println("\tjava -jar Ikala-Core.jar -l fr -c CA");
		System.out.println();

		System.out.println("Options:");
		System.out.println("\t-h --help \t\t\tShows the help text");
		System.out.println(
			"\t-l, --language <language>\tSets the language to use for localization");
		System.out.println(
			"\t-c, --country <country>\t\tSets the country to use for localization (please also provide language)");
	}

	/**
	 * Process all the arguments, setting things up as appropriate.
	 *
	 * @param args The command line arguments.
	 * @return True if we should continue with the program, false if there was a
	 *         problem and the program should exit.
	 */
	private static boolean processArguments(final String[] args) {
		String language = null;
		String country = null;

		for (int i = 0; i < args.length; ++i) {
			final String arg = args[i];
			if (!Launcher.isFlag(arg)) {
				System.out.println("Unrecognized argument '" + arg + "'");
				Launcher.printHelp();
				return false;
			}
			if ("-h".equalsIgnoreCase(arg) || "--help".equalsIgnoreCase(arg)) {
				Launcher.printHelp();
				return false;
			}
			if ("-l".equalsIgnoreCase(arg)
				|| "--language".equalsIgnoreCase(arg)) {
				if (i >= args.length - 1 || Launcher.isFlag(args[i + 1])) {
					System.out.println("Please enter a language.");
					System.out.println();
					Launcher.printHelp();
					return false;
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
					return false;
				}
				country = args[i + 1];
				// skip the next argument, we consumed it
				++i;
				continue;
			}
			// default case
			System.out.println("Unrecognized flag '" + arg + "'.");
			Launcher.printHelp();
			return false;
		}
		if (null == language) {
			language = "en";
			country = "US";
		}

		return Launcher.setLocale(language, country);
	}

	/**
	 * Reading in input line as package management commands.
	 * 
	 * @return The status.
	 */
	private static int readInput() {
		while (commandLine.hasNextLine()) {
			String line = commandLine.nextLine();

			if (stopCommand.equalsIgnoreCase(line.trim())) {
				shouldShutdown.set(true);
				;
				return -1;
			}

			PluginCommandSent event;
			String[] parts = line.split("\\s+");
			if (1 == parts.length) {
				event = new PluginCommandSent(parts[0]);
			}
			else {
				String[] cmdArgs = new String[parts.length - 1];
				System.arraycopy(parts, 1, cmdArgs, 0, cmdArgs.length);
				event = new PluginCommandSent(parts[0], cmdArgs);
			}
			event.fire();
		}
		return STATUS_OK;

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
	 * Sets up the folders used by the system if they don't exist.
	 */
	private static void setupMainFolders() {
		File pluginFolder = new File(
			System.getProperty("user.dir") + Constants.PLUGIN_FOLDER_PATH);
		try {
			if (!pluginFolder.exists()) {
				pluginFolder.mkdirs();
			}
		}
		catch (SecurityException e) {
			Launcher.log.warn(SafeResourceLoader
				.getString("ERROR_CREATE_PLUGIN_FOLDER", Launcher.bundle));
		}
	}

	/**
	 * Set up the resource folders for all plugins.
	 */
	private static void setupPluginFolders() {
		Map<String, Plugin> plugins =
			PluginManager.getInstance().getLoadedPlugins();
		for (String plugin : plugins.keySet()) {
			PluginFolder.createFolder(plugin);
			for (PluginFolder.ResourceType resourceType : PluginFolder.ResourceType
				.values()) {
				PluginFolder.createResourceFolder(plugin, resourceType);
			}
		}
	}

	/**
	 * Shut down all the main systems, unloads everything.
	 */
	public static void shutdown() {
		PluginManager.destoryInstance();
		EventManager.destoryInstance();
		Launcher.bundle = null;
	}

	/**
	 * Private constructor so this class is not instantiated.
	 */
	private Launcher() {}
}
