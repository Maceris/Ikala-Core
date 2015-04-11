package com.ikalagaming.logging.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.logging.LoggingLevel;
import com.ikalagaming.packages.Package;

/**
 * An error message needs to be logged.
 *
 * @author Ches Burks
 *
 */
public class LogError extends Event {

	private final String theError;

	private final LoggingLevel theLevel;

	private final String theDetails;

	private final String theSender;

	/**
	 * Creates a LogError event with the given information.
	 *
	 * @param error What went wrong
	 * @param level the level of importance
	 * @param sender who is logging the error
	 */
	public LogError(String error, LoggingLevel level, Package sender) {
		this(error, "", level, sender.getName());
	}

	/**
	 * Creates a LogError event with the given information.
	 *
	 * @param error What went wrong
	 * @param level the level of importance
	 * @param sender who is logging the error
	 */
	public LogError(String error, LoggingLevel level, String sender) {
		this(error, "", level, sender);
	}

	/**
	 * Creates a LogError event with the given information.
	 *
	 * @param error What went wrong
	 * @param details extra information about the error
	 * @param level the level of importance
	 * @param sender who is logging the error
	 */
	public LogError(String error, String details, LoggingLevel level,
			Package sender) {
		this(error, details, level, sender.getName());
	}

	/**
	 * Creates a LogError event with the given information.
	 *
	 * @param error What went wrong
	 * @param details extra information about the error
	 * @param level the level of importance
	 * @param sender who is logging the error
	 */
	public LogError(String error, String details, LoggingLevel level,
			String sender) {
		this.theError = error;
		this.theDetails = details;
		this.theLevel = level;
		this.theSender = sender;
	}

	/**
	 * Returns a string containing extra information to be recorded or
	 * displayed.
	 *
	 * @return the details
	 */
	public String getDetails() {
		return this.theDetails;
	}

	/**
	 * Returns a string containing the information to be recorded or displayed.
	 *
	 * @return the error
	 */
	public String getError() {
		return this.theError;
	}

	/**
	 * Returns the logging level assigned to this log. This determines how
	 * important the information is and whether or not it will be displayed.
	 *
	 * @return the logging level
	 */
	public LoggingLevel getLevel() {
		return this.theLevel;
	}

	/**
	 * Returns the name of the package that sent the information to be logged.
	 *
	 * @return the senders name
	 */
	public String getSender() {
		return this.theSender;
	}

}
