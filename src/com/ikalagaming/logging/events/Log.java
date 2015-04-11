package com.ikalagaming.logging.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.logging.LoggingLevel;
import com.ikalagaming.packages.Package;

/**
 * A message needs to be logged.
 *
 * @author Ches Burks
 *
 */
public class Log extends Event {

	private final LoggingLevel theLevel;

	private final String theDetails;

	private final String theSender;

	/**
	 * Creates a Log event for the given information.
	 *
	 * @param details the info to record or display
	 * @param level the level of importance
	 * @param sender the package that is logging the information
	 */
	public Log(String details, LoggingLevel level, Package sender) {
		this(details, level, sender.getName());
	}

	/**
	 * Creates a Log event for the given information.
	 *
	 * @param details the info to record or display
	 * @param level the level of importance
	 * @param sender the name of the package logging the information
	 */
	public Log(String details, LoggingLevel level, String sender) {
		this.theDetails = details;
		this.theLevel = level;
		this.theSender = sender;
	}

	/**
	 * Returns a string containing the information to be recorded or displayed.
	 *
	 * @return the details
	 */
	public String getDetails() {
		return this.theDetails;
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
