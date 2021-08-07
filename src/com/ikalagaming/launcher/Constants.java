package com.ikalagaming.launcher;

import java.io.File;

/**
 * Constants that are used for the framework.
 *
 * @author Ches Burks
 *
 */
public class Constants {

	/**
	 * The path to the configuration folder for each plugin. This should be set
	 * up with path separators so that you can do something like
	 * {@code "ExamplePlugin" + CONFIG_PATH + "example.yml"} and get a valid
	 * path to a file.
	 */
	public static final String CONFIG_PATH =
		File.separator + "config" + File.separator;

	/**
	 * The path to the data folder for each plugin. This should be set up with
	 * path separators so that you can do something like
	 * {@code "ExamplePlugin" + DATA_PATH + "example.yml"} and get a valid path
	 * to a file.
	 */
	public static final String DATA_PATH =
		File.separator + "data" + File.separator;

	/**
	 * The path to the plugins from the current folder. This should be set up
	 * with path separators so that you can do something like
	 * {@code System.getProperty("user.dir") + PLUGIN_FOLDER_PATH + "Example.jar"}
	 * and get a valid path to a plugin.
	 */
	public static final String PLUGIN_FOLDER_PATH =
		File.separator + "plugins" + File.separator;

	/**
	 * The path to the scripts folder for each plugin. This should be set up
	 * with path separators so that you can do something like
	 * {@code "ExamplePlugin" + SCRIPTS_PATH + "example.yml"} and get a valid
	 * path to a file.
	 */
	public static final String SCRIPTS_PATH =
		File.separator + "scripts" + File.separator;

	/**
	 * The name of the file that records the last loaded version of a plugin.
	 */
	static final String PLUGIN_VERSION_FILE = "last-version.txt";

	/**
	 * Private constructor so this class is not initialized.
	 */
	private Constants() {}
}
