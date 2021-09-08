package com.ikalagaming.logging;

/**
 * Prints the logs to console via standard out.
 *
 * @author Ches Burks
 *
 */
public class ConsoleAppender implements LogAppender {

	@Override
	public void append(String log) {
		System.out.println(log);// NOSONAR
	}

	@Override
	public void stop() {
		// Do nothing, not relevant here.
	}

}
