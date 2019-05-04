package com.ikalagaming.plugins;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A command that has been registered with the system. This contains a string
 * representing the command and a reference to the owner of the command.
 *
 * @author Ches Burks
 *
 */
public class PluginCommand implements Comparable<PluginCommand> {
	private final String command;
	private final Consumer<String[]> callback;
	private final Plugin owningPlugin;

	/**
	 * Constructs a new registered command with the given command and plugin
	 *
	 * @param cmd The command registered
	 * @param fnCallback The callback function for the command.
	 */
	public PluginCommand(final String cmd,
		final Consumer<String[]> fnCallback) {
		this(cmd, fnCallback, null);
	}

	/**
	 * Constructs a new registered command with the given command and plugin
	 *
	 * @param cmd The command registered
	 * @param fnCallback The callback function for the command.
	 * @param owner The owner of the plugin, or null if none exists.
	 */
	public PluginCommand(final String cmd, final Consumer<String[]> fnCallback,
		Plugin owner) {
		this.command = cmd;
		this.callback = fnCallback;
		this.owningPlugin = owner;
	}

	@Override
	public int compareTo(PluginCommand o) {
		return this.getCommand().toLowerCase()
			.compareTo(o.getCommand().toLowerCase());
	}

	/**
	 * Returns the callback for this command
	 *
	 * @return The callback function
	 */
	public Consumer<String[]> getCallback() {
		return this.callback;

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
	 * Returns the owner of this command, which might not exist.
	 *
	 * @return The owning plugin, or an empty optional
	 */
	public Optional<Plugin> getOwner() {
		return Optional.ofNullable(this.owningPlugin);
	}

	/**
	 * Checks if there is an owner for this command.
	 *
	 * @return True if this command has an owner, false otherwise
	 */
	public boolean hasOwner() {
		return this.owningPlugin == null;
	}

}
