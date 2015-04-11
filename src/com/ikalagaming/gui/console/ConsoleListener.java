package com.ikalagaming.gui.console;

import com.ikalagaming.event.EventHandler;
import com.ikalagaming.event.Listener;
import com.ikalagaming.gui.console.events.ConsoleMessage;
import com.ikalagaming.gui.console.events.ReportUnknownCommand;
import com.ikalagaming.logging.events.DisplayLog;
import com.ikalagaming.packages.events.PackageCommandSent;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * The listener for the console gui. This handles events for the console.
 *
 * @author Ches Burks
 *
 */
public class ConsoleListener implements Listener {
	private Console parent;

	/**
	 * Constructs a listener for the given console.
	 *
	 * @param console the console to listen for
	 */
	public ConsoleListener(Console console) {
		this.parent = console;
	}

	/**
	 * Called when a command event is sent.
	 *
	 * @param event the command sent
	 */
	@EventHandler
	public void onCommand(PackageCommandSent event) {
		if (!event.getTo().equalsIgnoreCase(this.parent.getName())) {
			return;
		}

	}

	/**
	 * When a console message is sent, append it to the console.
	 *
	 * @param event the event that was received
	 */
	@EventHandler
	public void onConsoleMessage(ConsoleMessage event) {
		this.parent.appendMessage(event.getMessage());
	}

	/**
	 * Displays messages created by the logger.
	 *
	 * @param event logs and errors received
	 */
	@EventHandler
	public void onDisplayLog(DisplayLog event) {
		this.parent.appendMessage(event.getMessage());
	}

	/**
	 * Appends a message stating the last command was incorrect and a help
	 * message informing the user of the help command.
	 *
	 * @param event the command that was reported as unknown
	 */
	@EventHandler
	public void onReportUnknownCommand(ReportUnknownCommand event) {
		this.parent.appendMessage(SafeResourceLoader.getString(
				"unknown_command", this.parent.getResourceBundle(),
				"Unknown command")
				+ " '"
				+ event.getCommand()
				+ "'. "
				+ SafeResourceLoader.getString("try_cmd",
						this.parent.getResourceBundle(),
						"For a list of available commands, type")
				+ " '"
				+ SafeResourceLoader.getString("COMMAND_HELP",
						"com.ikalagaming.packages.resources.PackageManager",
						"help") + "'");
	}
}
