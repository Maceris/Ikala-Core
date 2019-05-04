package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;

/**
 * A command was sent.
 *
 * @author Ches Burks
 *
 */
public class PluginCommandSent extends Event {

	/**
	 * The command.
	 */
	private final String cmd;

	/**
	 * The parameters for the command.
	 */
	private final String[] arguments;

	/**
	 * Creates a new {@link PluginCommandSent} with the supplied parameters.
	 *
	 * @param command the command
	 */
	public PluginCommandSent(String command) {
		this(command, null);
	}

	/**
	 * Creates a new {@link PluginCommandSent} with the supplied parameters. If
	 * the arguments list is null, it will be created from the command, if the
	 * command has multiple parts.
	 *
	 * @param command the command
	 * @param args arguments for the command
	 */
	public PluginCommandSent(String command, String[] args) {
		String[] parts = command.trim().split("\\s+");
		this.cmd = parts[0];
		String[] argumentArray = args;
		if (argumentArray == null) {
			if (parts.length > 1) {
				argumentArray = new String[parts.length - 1];
				System.arraycopy(parts, 1, argumentArray, 0, parts.length - 1);
			}
			else {
				// so it is not null
				argumentArray = new String[0];
			}
		}
		this.arguments = argumentArray;
	}

	/**
	 * Return the list of arguments, {@link #hasArgs() if there are any}.
	 *
	 * @return the arguments for the command.
	 */
	public String[] getArgs() {
		return this.arguments;
	}

	/**
	 * Returns the command transmitted.
	 *
	 * @return the command
	 */
	public String getCommand() {
		return this.cmd;
	}

	/**
	 * Returns true if arguments exist, or false if the arguments list is null
	 * or empty.
	 *
	 * @return true if there are arguments
	 */
	public boolean hasArgs() {
		if (this.arguments == null) {
			return false;
		}
		else if (this.arguments.length > 0) {
			return true;
		}
		return false;
	}

}
