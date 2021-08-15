package com.ikalagaming.logging;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.localization.Localization;
import com.ikalagaming.plugins.PluginManager;
import com.ikalagaming.util.SafeResourceLoader;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Handles reporting and logging errors. Actual loggers get created in
 * {@link LoggerFactory}.
 *
 * @author Ches Burks
 *
 */
public class Logging {

	private static final LogLevel DEFAULT_THRESHOLD = LogLevel.ALL;
	/**
	 * Only logs events that are of this level or higher
	 */
	private static LogLevel threshold = Logging.DEFAULT_THRESHOLD;
	private static ResourceBundle resourceBundle;
	private static LogDispatcher dispatcher;
	private static EventManager eventManager;

	private static boolean initialized = false;

	private static ReentrantLock initLock = new ReentrantLock();
	private static ReentrantLock thresholdLock = new ReentrantLock();

	/**
	 * Sets up a static logger using the default event manager if one does not
	 * exist already.
	 *
	 * @see Logging#destory()
	 */
	public static void create() {
		Logging.initLock.lock();
		try {
			if (Logging.initialized) {
				return;
			}
			Logging.eventManager = EventManager.getInstance();
			try {
				Logging.resourceBundle = ResourceBundle.getBundle(
					"com.ikalagaming.logging.resources.LoggingPlugin",
					Localization.getLocale());
			}
			catch (MissingResourceException missingResource) {
				Logging.log(PluginManager.PLUGIN_NAME, LogLevel.SEVERE,
					"locale not found in LoggingPlugin.onLoad()");
			}
			Logging.dispatcher = new LogDispatcher(Logging.eventManager);
			Logging.dispatcher.start();

			Logging.initialized = true;
		}
		finally {
			Logging.initLock.unlock();
		}
	}

	/**
	 * Shuts down the logger and clears out static variables. Note that if logs
	 * are requested after this, or the threshold is requested or changed, it
	 * will recreate the logger and start up again.
	 *
	 * @see #create()
	 */
	public static void destory() {
		Logging.initLock.lock();
		try {
			if (!Logging.initialized) {
				return;
			}
			Logging.dispatcher.terminate();
			Logging.dispatcher = null;
			Logging.resourceBundle = null;
			Logging.eventManager = null;
			Logging.initialized = false;
			Logging.thresholdLock.lock();
			try {
				Logging.threshold = Logging.DEFAULT_THRESHOLD;
			}
			finally {
				Logging.thresholdLock.unlock();
			}
		}
		finally {
			Logging.initLock.unlock();
		}
	}

	/**
	 * Returns the current logging level threshold. Logs below this are ignored,
	 * logs that are equal to or above the threshold are actually logged. If the
	 * logger is not initialized, it just returns the default threshold value.
	 *
	 * @return the threshold value for the logger
	 * @see #setLogLevel(LogLevel)
	 */
	public static LogLevel getLogLevel() {
		Logging.initLock.lock();
		LogLevel ret;
		try {
			if (!Logging.initialized) {
				ret = Logging.DEFAULT_THRESHOLD;
			}
			else {
				Logging.thresholdLock.lock();
				try {
					ret = Logging.threshold;
				}
				finally {
					Logging.thresholdLock.unlock();
				}
			}
		}
		finally {
			Logging.initLock.unlock();
		}
		return ret;
	}

	/**
	 * Returns true if the logger has been created, false if it is in an
	 * uninitialized state.
	 *
	 * @return true if the logger has been created, false otherwise
	 */
	public static boolean isInitialized() {
		Logging.initLock.lock();
		try {
			return Logging.initialized;
		}
		finally {
			Logging.initLock.unlock();
		}
	}

	/**
	 * Logs the provided information. Attempts to use localized names for the
	 * logging level. This only logs information that is above or equal to the
	 * logging threshold. If the logger is not initialized, it will be created.
	 *
	 * @param origin The plugin that is logging the info
	 * @param level The level of the requested log
	 * @param details What to log
	 * @see #create()
	 */
	static void log(String origin, LogLevel level, String details) {
		Logging.thresholdLock.lock();
		try {
			if (level.intValue() < Logging.threshold.intValue()) {
				/*
				 * We don't care about the log as it's not higher importance
				 * than the threshold, so we bail out of the method. For some
				 * reason SonarLint rule java:S3626 picks this up as redundant,
				 * despite skipping the whole rest of the method.
				 */
				return;// NOSONAR
			}
		}
		finally {
			Logging.thresholdLock.unlock();
		}

		Logging.initLock.lock();
		try {
			if (!Logging.initialized) {
				Logging.create();
			}
		}
		finally {
			Logging.initLock.unlock();
		}

		String newLog = "";
		try {
			newLog = SafeResourceLoader.getString("level_prefix",
				Logging.resourceBundle, "[")
				+ level.getLocalizedName()
				+ SafeResourceLoader
					.getString("level_postfix", Logging.resourceBundle, "]")
				+ " "
				+ SafeResourceLoader.getString("name_prefix",
					Logging.resourceBundle, "<")
				+ origin + SafeResourceLoader.getString("name_postfix",
					Logging.resourceBundle, ">")
				+ " " + details;
		}
		catch (Exception e) {
			/*
			 * This is the logger, and it's broken, so as a last resort we are
			 * printing to System.err
			 */
			System.err.println(level.getName());// NOSONAR
			System.err.println(details);// NOSONAR
			e.printStackTrace();// we need to know what broke the log
		}
		Logging.dispatcher.log(newLog);
	}

	/**
	 * Sets the logging threshold to the given value. Logs below this are
	 * ignored, logs that are equal to or above the threshold are actually
	 * logged. If a null value is passed for some reason, the default level is
	 * used. <br>
	 * If the logger is not initialized, a new one is created.
	 *
	 * @param newLevel the new level to use
	 * @see #getLogLevel()
	 * @see #create()
	 */
	public static void setLogLevel(LogLevel newLevel) {
		Logging.initLock.lock();
		try {
			if (!Logging.initialized) {
				Logging.create();
			}
		}
		finally {
			Logging.initLock.unlock();
		}
		Logging.thresholdLock.lock();
		try {
			if (newLevel == null) {
				Logging.threshold = Logging.DEFAULT_THRESHOLD;
			}
			Logging.threshold = newLevel;
		}
		finally {
			Logging.thresholdLock.unlock();
		}
	}

	/**
	 * Private constructor so this class is not instantiated.
	 */
	private Logging() {}
}
