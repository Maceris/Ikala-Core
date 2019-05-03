package com.ikalagaming.plugins;

/**
 * A command that has been registered with the system. This contains a string
 * representing the command and a reference to the owner of the command.
 *
 * @author Ches Burks
 *
 */
public class PluginCommand implements Comparable<PluginCommand> {
	private final String command;
	private final String owner;

	/**
	 * Constructs a new registered command with the given command and plugin
	 *
	 * @param cmd the command registered
	 * @param theOwner what plugin registered the command
	 */
	public PluginCommand(final String cmd, final String theOwner) {
		this.command = cmd;
		this.owner = theOwner;
	}

	@Override
	public int compareTo(PluginCommand o) {
		return this.getCommand().toLowerCase()
				.compareTo(o.getCommand().toLowerCase());
	}

	/**
	 * Returns the command
	 *
	 * @return the command
	 */
	public String getCommand() {
		return this.command;
	}

	/**
	 * Returns the plugin that registered this command
	 *
	 * @return the owner
	 */
	public String getOwner() {
		return this.owner;
	}

}
