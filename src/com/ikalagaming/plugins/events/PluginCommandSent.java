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
	 * The name of the plugin that the command was registered to.
	 */
	private final String pluginTypeTo;

	/**
	 * The command.
	 */
	private final String cmd;

	/**
	 * The parameters for the command.
	 */
	private final String[] arguments;

	private final String pluginTypeFrom;

	/**
	 * Creates a new {@link PluginCommandSent} with the supplied parameters.
	 * There is no guarantee that only the intended plugin will receive the
	 * message.
	 *
	 * @param to the Plugin type that owns the command
	 * @param command the command
	 * @param from the Plugin type that sent the command
	 */
	public PluginCommandSent(String to, String command, String from) {
		this(to, command, null, from);
	}

	/**
	 * Creates a new {@link PluginCommandSent} with the supplied parameters.
	 * There is no guarantee that only the intended plugin will receive the
	 * message (yet?). If the arguments list is null, it will be created from the
	 * command, if the command has multiple parts.
	 *
	 * @param to the Plugin type that owns the command
	 * @param command the command
	 * @param args arguments for the command
	 * @param from the plugin type that sent the command
	 */
	public PluginCommandSent(String to, String command, String[] args,
			String from) {
		this.pluginTypeTo = to;
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
		this.pluginTypeFrom = from;
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
	 * Returns the type of the plugin that sent the command.
	 *
	 * @return the name of the sending plugin
	 */
	public String getFrom() {
		return this.pluginTypeFrom;
	}

	/**
	 * Returns the name of the plugin that the command was registered to.
	 *
	 * @return the name of the receiving plugin
	 */
	public String getTo() {
		return this.pluginTypeTo;
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
