package com.ikalagaming.logging;

import lombok.AllArgsConstructor;

/**
 * Logs messages for a specific plugin. What logs actually get recorded is
 * configured in {@link Logging}.
 *
 * @author Ches Burks
 *
 */
@AllArgsConstructor
public class Logger {

	/**
	 * The plugin that is logging messages.
	 *
	 * @param The origin of these messages.
	 */
	@SuppressWarnings("javadoc")
	private final String origin;

	/**
	 * Logs the given message at the {@link LogLevel#CONFIG config} log level.
	 *
	 * @param details What to log
	 * @see #severe(String)
	 * @see #warning(String)
	 * @see #info(String)
	 * @see #fine(String)
	 * @see #finer(String)
	 * @see #finest(String)
	 * @see #log(LogLevel, String)
	 */
	public void config(String details) {
		Logging.log(this.origin, LogLevel.CONFIG, details);
	}

	/**
	 * Logs the given message at the {@link LogLevel#FINE fine} log level.
	 *
	 * @param details What to log
	 * @see #severe(String)
	 * @see #warning(String)
	 * @see #info(String)
	 * @see #config(String)
	 * @see #finer(String)
	 * @see #finest(String)
	 * @see #log(LogLevel, String)
	 */
	public void fine(String details) {
		Logging.log(this.origin, LogLevel.FINE, details);
	}

	/**
	 * Logs the given message at the {@link LogLevel#FINER finer} log level.
	 *
	 * @param details What to log
	 * @see #severe(String)
	 * @see #warning(String)
	 * @see #info(String)
	 * @see #config(String)
	 * @see #fine(String)
	 * @see #finest(String)
	 * @see #log(LogLevel, String)
	 */
	public void finer(String details) {
		Logging.log(this.origin, LogLevel.FINER, details);
	}

	/**
	 * Logs the given message at the {@link LogLevel#FINEST finest} log level.
	 *
	 * @param details What to log
	 * @see #severe(String)
	 * @see #warning(String)
	 * @see #info(String)
	 * @see #config(String)
	 * @see #fine(String)
	 * @see #finer(String)
	 * @see #log(LogLevel, String)
	 */
	public void finest(String details) {
		Logging.log(this.origin, LogLevel.FINEST, details);
	}

	/**
	 * Logs the given message at the {@link LogLevel#INFO info} log level.
	 *
	 * @param details What to log
	 * @see #severe(String)
	 * @see #warning(String)
	 * @see #config(String)
	 * @see #fine(String)
	 * @see #finer(String)
	 * @see #finest(String)
	 * @see #log(LogLevel, String)
	 */
	public void info(String details) {
		Logging.log(this.origin, LogLevel.INFO, details);
	}

	/**
	 * Logs the provided information at the given log level.
	 *
	 * @param level The level of the requested log
	 * @param details What to log
	 * @see #severe(String)
	 * @see #warning(String)
	 * @see #info(String)
	 * @see #config(String)
	 * @see #fine(String)
	 * @see #finer(String)
	 * @see #finest(String)
	 */
	public void log(LogLevel level, String details) {
		Logging.log(this.origin, level, details);
	}

	/**
	 * Logs the given message at the {@link LogLevel#SEVERE severe} log level.
	 *
	 * @param details What to log
	 * @see #warning(String)
	 * @see #info(String)
	 * @see #config(String)
	 * @see #fine(String)
	 * @see #finer(String)
	 * @see #finest(String)
	 * @see #log(LogLevel, String)
	 */
	public void severe(String details) {
		Logging.log(this.origin, LogLevel.SEVERE, details);
	}

	/**
	 * Logs the given message at the {@link LogLevel#WARNING warning} log level.
	 *
	 * @param details What to log
	 * @see #severe(String)
	 * @see #info(String)
	 * @see #config(String)
	 * @see #fine(String)
	 * @see #finer(String)
	 * @see #finest(String)
	 * @see #log(LogLevel, String)
	 */
	public void warning(String details) {
		Logging.log(this.origin, LogLevel.WARNING, details);
	}
}
