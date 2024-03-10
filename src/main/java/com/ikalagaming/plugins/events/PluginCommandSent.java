package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

/**
 * A command was sent.
 *
 * @author Ches Burks
 *
 */
@Getter
@AllArgsConstructor
public class PluginCommandSent extends Event {

	/**
	 * The command that was sent.
	 *
	 * @return The command that was sent.
	 */
	@SuppressWarnings("javadoc")
	@NonNull
	private final String command;

	/**
	 * The parameters supplied for the command.
	 *
	 * @return The commands arguments.
	 */
	@SuppressWarnings("javadoc")
	@NonNull
	private final List<String> arguments;

	/**
	 * Creates a new {@link PluginCommandSent} with no arguments.
	 *
	 * @param command The command that was sent.
	 */
	public PluginCommandSent(@NonNull String command) {
		this(command, List.of());
	}

	/**
	 * Returns true if there are any arguments.
	 *
	 * @return Whether there are arguments.
	 */
	public boolean hasArguments() {
		return !arguments.isEmpty();
	}

}
