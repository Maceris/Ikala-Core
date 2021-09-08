package com.ikalagaming.logging;

/**
 * Handles printing logs to some source.
 *
 * @author Ches Burks
 *
 */
interface LogAppender {
	/**
	 * Append a log to some output source.
	 *
	 * @param log The log to print.
	 */
	void append(String log);

	/**
	 * Quit logging, clean up.
	 */
	void stop();
}
