package com.ikalagaming.logging;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantLock;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.localization.Localization;
import com.ikalagaming.plugins.PluginManager;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * Handles reporting and logging errors.
 *
 * @author Ches Burks
 *
 */
public class Logging {

	private static final LogLevel DEFAULT_THRESHOLD = LogLevel.ALL;
	/**
	 * Only logs events that are of this level or higher
	 */
	private static LogLevel threshold;
	private static ResourceBundle resourceBundle;
	private static LogDispatcher dispatcher;
	private static EventManager eventManager;

	private static boolean initialized = false;

	private static ReentrantLock initLock = new ReentrantLock();
	private static ReentrantLock thresholdLock = new ReentrantLock();

	/**
	 * Logs the given message at the {@link LogLevel#CONFIG config} log level.
	 *
	 * @param origin The plugin that is logging the info
	 * @param details What to log
	 * @see #severe(String, String)
	 * @see #warning(String, String)
	 * @see #info(String, String)
	 * @see #fine(String, String)
	 * @see #finer(String, String)
	 * @see #finest(String, String)
	 * @see #log(String, LogLevel, String)
	 */
	public static void config(String origin, String details) {
		Logging.log(origin, LogLevel.CONFIG, details);
	}

	/**
	 * Sets up a static logger using the default event manager if one does not
	 * exist already.
	 *
	 * @see Logging#destory()
	 */
	public static void create() {
		initLock.lock();
		try {
			if (Logging.initialized) {
				return;
			}
			thresholdLock.lock();
			try {
				Logging.threshold = Logging.DEFAULT_THRESHOLD;
			}
			finally {
				thresholdLock.unlock();
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
			initLock.unlock();
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
		initLock.lock();
		try {
			if (!Logging.initialized) {
				return;
			}
			Logging.dispatcher.terminate();
			Logging.dispatcher = null;
			Logging.resourceBundle = null;
			Logging.eventManager = null;
			Logging.initialized = false;
			thresholdLock.lock();
			try {
				Logging.threshold = null;
			}
			finally {
				thresholdLock.unlock();
			}
		}
		finally {
			initLock.unlock();
		}
	}

	/**
	 * Logs the given message at the {@link LogLevel#FINE fine} log level.
	 *
	 * @param origin The plugin that is logging the info
	 * @param details What to log
	 * @see #severe(String, String)
	 * @see #warning(String, String)
	 * @see #info(String, String)
	 * @see #config(String, String)
	 * @see #finer(String, String)
	 * @see #finest(String, String)
	 * @see #log(String, LogLevel, String)
	 */
	public static void fine(String origin, String details) {
		Logging.log(origin, LogLevel.FINE, details);
	}

	/**
	 * Logs the given message at the {@link LogLevel#FINER finer} log level.
	 *
	 * @param origin The plugin that is logging the info
	 * @param details What to log
	 * @see #severe(String, String)
	 * @see #warning(String, String)
	 * @see #info(String, String)
	 * @see #config(String, String)
	 * @see #fine(String, String)
	 * @see #finest(String, String)
	 * @see #log(String, LogLevel, String)
	 */
	public static void finer(String origin, String details) {
		Logging.log(origin, LogLevel.FINER, details);
	}

	/**
	 * Logs the given message at the {@link LogLevel#FINEST finest} log level.
	 *
	 * @param origin The plugin that is logging the info
	 * @param details What to log
	 * @see #severe(String, String)
	 * @see #warning(String, String)
	 * @see #info(String, String)
	 * @see #config(String, String)
	 * @see #fine(String, String)
	 * @see #finer(String, String)
	 * @see #log(String, LogLevel, String)
	 */
	public static void finest(String origin, String details) {
		Logging.log(origin, LogLevel.FINEST, details);
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
		initLock.lock();
		LogLevel ret;
		try {
			if (!Logging.initialized) {
				ret = Logging.DEFAULT_THRESHOLD;
			}
			else {
				thresholdLock.lock();
				try {
					ret = Logging.threshold;
				}
				finally {
					thresholdLock.unlock();
				}
			}
		}
		finally {
			initLock.unlock();
		}
		return ret;
	}

	/**
	 * Logs the given message at the {@link LogLevel#INFO info} log level.
	 *
	 * @param origin The plugin that is logging the info
	 * @param details What to log
	 * @see #severe(String, String)
	 * @see #warning(String, String)
	 * @see #config(String, String)
	 * @see #fine(String, String)
	 * @see #finer(String, String)
	 * @see #finest(String, String)
	 * @see #log(String, LogLevel, String)
	 */
	public static void info(String origin, String details) {
		Logging.log(origin, LogLevel.INFO, details);
	}

	/**
	 * Returns true if the logger has been created, false if it is in an
	 * uninitialized state.
	 *
	 * @return true if the logger has been created, false otherwise
	 */
	public static boolean isInitialized() {
		initLock.lock();
		try {
			return Logging.initialized;
		}
		finally {
			initLock.unlock();
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
	 * @see #severe(String, String)
	 * @see #warning(String, String)
	 * @see #info(String, String)
	 * @see #config(String, String)
	 * @see #fine(String, String)
	 * @see #finer(String, String)
	 * @see #finest(String, String)
	 */
	public static void log(String origin, LogLevel level, String details) {
		initLock.lock();
		try {
			if (!Logging.initialized) {
				Logging.create();
			}
		}
		finally {
			initLock.unlock();
		}
		thresholdLock.lock();
		try {
			if (level.intValue() < Logging.threshold.intValue()) {
				return;
			}
		}
		finally {
			thresholdLock.unlock();
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
			System.err.println(level.getName());
			System.err.println(details);
			e.printStackTrace(System.err);// we need to know what broke the log
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
		initLock.lock();
		try {
			if (!Logging.initialized) {
				Logging.create();
			}
		}
		finally {
			initLock.unlock();
		}
		thresholdLock.lock();
		try {
			if (newLevel == null) {
				Logging.threshold = Logging.DEFAULT_THRESHOLD;
			}
			Logging.threshold = newLevel;
		}
		finally {
			thresholdLock.unlock();
		}
	}

	/**
	 * Logs the given message at the {@link LogLevel#SEVERE severe} log level.
	 *
	 * @param origin The plugin that is logging the info
	 * @param details What to log
	 * @see #warning(String, String)
	 * @see #info(String, String)
	 * @see #config(String, String)
	 * @see #fine(String, String)
	 * @see #finer(String, String)
	 * @see #finest(String, String)
	 * @see #log(String, LogLevel, String)
	 */
	public static void severe(String origin, String details) {
		Logging.log(origin, LogLevel.SEVERE, details);
	}

	/**
	 * Logs the given message at the {@link LogLevel#WARNING warning} log level.
	 *
	 * @param origin The plugin that is logging the info
	 * @param details What to log
	 * @see #severe(String, String)
	 * @see #info(String, String)
	 * @see #config(String, String)
	 * @see #fine(String, String)
	 * @see #finer(String, String)
	 * @see #finest(String, String)
	 * @see #log(String, LogLevel, String)
	 */
	public static void warning(String origin, String details) {
		Logging.log(origin, LogLevel.WARNING, details);
	}
}
