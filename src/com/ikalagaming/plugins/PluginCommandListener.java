package com.ikalagaming.plugins;

import com.ikalagaming.event.EventHandler;
import com.ikalagaming.event.Listener;
import com.ikalagaming.plugins.events.PluginCommandSent;

import java.util.List;

/**
 * The event listener for the plugin command system.
 *
 * @author Ches Burks
 *
 */
class PluginCommandListener implements Listener {

	/**
	 * Handles processing of Plugin Commands.
	 *
	 * @param event The plugin command that was sent.
	 */
	@EventHandler
	public void onPluginCommand(PluginCommandSent event) {
		List<PluginCommand> commands =
			PluginManager.getInstance().getCommands();
		if (commands.stream()
			.noneMatch(cmd -> event.getCommand().equals(cmd.getCommand()))) {
			PluginManager.getInstance().cbHelp(null);
		}

		commands.stream()
			.filter(cmd -> event.getCommand().equals(cmd.getCommand()))
			.forEach(cmd -> cmd.getCallback().accept(event.getArguments()));
	}

}
