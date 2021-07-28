package com.ikalagaming.logging;

import lombok.NonNull;
import lombok.Synchronized;

import java.util.HashMap;
import java.util.Map;

/**
 * Generates {@link Logger loggers} for a specific plugin. What logs actually
 * get recorded is configured in {@link Logging}.
 *
 * @author Ches Burks
 *
 */
public class LoggerFactory {

	private static Map<String, Logger> loggers = new HashMap<>();

	/**
	 * Return a logger for the given plugin. If one already exists, then we will
	 * give a cached version.
	 *
	 * @param pluginName The name of the plugin logging, what you want to show
	 *            in the logs.
	 * @return The logger for that plugin.
	 */
	@Synchronized
	public static Logger getLogger(@NonNull String pluginName) {
		if (loggers.containsKey(pluginName)) {
			return loggers.get(pluginName);
		}
		Logger log = new Logger(pluginName);
		loggers.put(pluginName, log);
		return log;
	}

	/**
	 * Private constructor so this class is not instantiated.
	 */
	private LoggerFactory() {}
}
