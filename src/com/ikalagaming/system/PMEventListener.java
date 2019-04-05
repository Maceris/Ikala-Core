package com.ikalagaming.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.ikalagaming.event.EventHandler;
import com.ikalagaming.event.Listener;
import com.ikalagaming.gui.console.events.ConsoleCommandEntered;
import com.ikalagaming.gui.console.events.ConsoleMessage;
import com.ikalagaming.gui.console.events.ReportUnknownCommand;
import com.ikalagaming.logging.Logging;
import com.ikalagaming.plugins.Plugin;
import com.ikalagaming.plugins.PluginCommand;
import com.ikalagaming.plugins.PluginManager;
import com.ikalagaming.plugins.events.PluginCommandSent;
import com.ikalagaming.plugins.events.PluginDisabled;
import com.ikalagaming.plugins.events.PluginEnabled;
import com.ikalagaming.plugins.events.PluginLoaded;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * The event listener for the plugin management system.
 *
 * @author Ches Burks
 *
 */
class PMEventListener implements Listener {

	private String cmd_help;
	private String cmd_plugins;
	private String cmd_enable;
	private String cmd_disable;
	private String cmd_load;
	private String cmd_unload;
	private String cmd_reload;
	private String cmd_version;

	private PluginManager manager;
	private SystemPlugin sysPlugin;

	/**
	 * Constructs a listener for the default plugin manager.
	 *
	 * @param parent the system plugin to handle events for
	 * @param packManager The plugin management system this listener is for
	 */
	public PMEventListener(SystemPlugin parent, PluginManager packManager) {
		this.manager = packManager;
		this.sysPlugin = parent;

		this.cmd_help = SafeResourceLoader.getString("COMMAND_HELP",
			this.manager.getResourceBundle(), "help");
		this.cmd_plugins = SafeResourceLoader.getString("COMMAND_LIST_PLUGINS",
			this.manager.getResourceBundle(), "plugins");
		this.cmd_enable = SafeResourceLoader.getString("COMMAND_ENABLE",
			this.manager.getResourceBundle(), "enable");
		this.cmd_disable = SafeResourceLoader.getString("COMMAND_DISABLE",
			this.manager.getResourceBundle(), "disable");
		this.cmd_load = SafeResourceLoader.getString("COMMAND_LOAD",
			this.manager.getResourceBundle(), "load");
		this.cmd_unload = SafeResourceLoader.getString("COMMAND_UNLOAD",
			this.manager.getResourceBundle(), "unload");
		this.cmd_reload = SafeResourceLoader.getString("COMMAND_RELOAD",
			this.manager.getResourceBundle(), "reload");
		this.cmd_version = SafeResourceLoader.getString("COMMAND_VERSION",
			this.manager.getResourceBundle(), "version");

	}

	/**
	 * Disables the specified plugin. If no plugin exists, alerts the user to
	 * this fact.
	 *
	 * @param pluginName The plugin to find a version for
	 */
	private void disable(String pluginName) {
		ConsoleMessage message;
		Plugin pack = this.manager.getPlugin(pluginName);

		if (pack == null) {
			message = new ConsoleMessage(SafeResourceLoader
				.getString("PLUGIN_NOT_LOADED",
					this.manager.getResourceBundle())
				.replaceFirst("\\$PLUGIN", pluginName));
			this.manager.fireEvent(message);
			// stop right here. It does not exist
			return;
		}
		if (!this.manager.isEnabled(pack)) {
			message = new ConsoleMessage(SafeResourceLoader.getString(
				"PLUGIN_DISABLE_FAIL", this.manager.getResourceBundle()));
			this.manager.fireEvent(message);
			return;
		}
		this.manager.disable(pack);
	}

	private void enable(String pluginName) {
		ConsoleMessage message;
		Plugin pack = this.manager.getPlugin(pluginName);

		if (pack == null) {
			message = new ConsoleMessage(SafeResourceLoader
				.getString("PLUGIN_NOT_LOADED",
					this.manager.getResourceBundle())
				.replaceFirst("\\$PLUGIN", pluginName));
			this.manager.fireEvent(message);
			// stop right here. It does not exist
			return;
		}
		if (this.manager.isEnabled(pack)) {
			message = new ConsoleMessage(SafeResourceLoader.getString(
				"PLUGIN_ENABLE_FAIL", this.manager.getResourceBundle()));
			this.manager.fireEvent(message);
			return;
		}
		this.manager.enable(pack);
	}

	/**
	 * Called when a command event is sent.
	 *
	 * @param event the command sent
	 */
	@EventHandler
	public void onCommand(PluginCommandSent event) {
		if (event.getCommand().equalsIgnoreCase(this.cmd_help)) {
			this.printHelp();
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_plugins)) {
			this.printPlugins();
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_enable)) {
			String name = "";
			if (event.getArgs().length >= 1) {
				name = event.getArgs()[0];
			}
			else {
				// TODO error
			}
			this.enable(name);
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_disable)) {
			String name = "";
			if (event.getArgs().length >= 1) {
				name = event.getArgs()[0];
			}
			else {
				// TODO error
			}
			this.disable(name);
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_load)) {
			this.printWIP();
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_unload)) {
			String name = "";
			if (event.getArgs().length >= 1) {
				name = event.getArgs()[0];
			}
			else {
				// TODO error
			}
			this.unload(name);
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_reload)) {
			String name = "";
			if (event.getArgs().length >= 1) {
				name = event.getArgs()[0];
			}
			else {
				// TODO error
			}
			this.reload(name);
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_version)) {
			String name = "";
			if (event.getArgs().length >= 1) {
				name = event.getArgs()[0];
			}
			this.printVersion(name);
		}

	}

	/**
	 * Check to see if the command is registered, and handle it or report it as
	 * unregistered.
	 *
	 * @param event the command sent by the console
	 */
	@EventHandler
	public void onConsoleCommandEntered(ConsoleCommandEntered event) {

		String firstWord = event.getCommand().trim().split("\\s+")[0];
		if (!this.manager.containsCommand(firstWord)) {
			ReportUnknownCommand report = new ReportUnknownCommand(firstWord);
			this.manager.fireEvent(report);
		}

		Plugin pack = this.manager.getCommandParent(firstWord);
		if (pack != null) {
			this.manager.fireEvent(new PluginCommandSent(pack.getName(),
				event.getCommand(), "console"));
		}

	}

	/**
	 * Logs the plugin being disabled.
	 *
	 * @param event the event that was received
	 */
	@EventHandler
	public void onPluginDisabled(PluginDisabled event) {
		Plugin target = event.getPlugin();
		String msgDisabled = SafeResourceLoader
			.getString("ALERT_ENABLED", this.manager.getResourceBundle())
			.replaceFirst("\\$PLUGIN", target.getName())
			.replaceFirst("\\$VERSION", target.getVersion() + "");
		Logging.fine(this.sysPlugin.getName(), msgDisabled);
	}

	/**
	 * Logs the plugin being enabled.
	 *
	 * @param event the event that was received
	 */
	@EventHandler
	public void onPluginEnabled(PluginEnabled event) {
		Plugin target = event.getPlugin();
		String msgEnabled = SafeResourceLoader
			.getString("ALERT_ENABLED", this.manager.getResourceBundle())
			.replaceFirst("\\$PLUGIN", target.getName())
			.replaceFirst("\\$VERSION", target.getVersion() + "");
		Logging.fine(this.sysPlugin.getName(), msgEnabled);
	}

	// logging plugin events
	/**
	 * If plugins should be enabled on load, this enables the newly loaded
	 * plugin. Also logs the plugin being loaded.
	 *
	 * @param event the event that was received
	 */
	@EventHandler
	public void onPluginLoaded(PluginLoaded event) {
		if (this.manager.enableOnLoad()) {
			this.manager.enable(event.getPlugin());
		}
		String loaded = SafeResourceLoader.getString("ALERT_LOADED",
			this.manager.getResourceBundle());
		loaded = loaded.replaceFirst("\\$PLUGIN", event.getPlugin().getName());
		loaded = loaded.replaceFirst("\\$VERSION",
			"" + event.getPlugin().getVersion());
		Logging.fine(this.sysPlugin.getName(), loaded);
	}

	private void printHelp() {
		ArrayList<PluginCommand> commands;
		commands = this.manager.getCommands();
		String tmp;
		ConsoleMessage message;
		for (PluginCommand cmd : commands) {
			tmp = cmd.getCommand();

			message = new ConsoleMessage(tmp);
			this.manager.fireEvent(message);
		}
	}

	private void printPlugins() {
		String tmp;
		ConsoleMessage message;
		HashMap<String, Plugin> loadedPlugins = this.manager.getLoadedPlugins();
		ArrayList<String> names = new ArrayList<>();
		names.addAll(loadedPlugins.keySet());
		Collections.sort(names);

		for (String name : names) {
			tmp = "";
			tmp += loadedPlugins.get(name).getName();
			tmp += " "
				+ SafeResourceLoader.getString("PLUGIN_VERSION",
					this.manager.getResourceBundle())
				+ loadedPlugins.get(name).getVersion();
			if (this.manager.isEnabled(loadedPlugins.get(name))) {
				tmp +=
					" " + "(" + SafeResourceLoader.getString("ENABLED_STATUS",
						this.manager.getResourceBundle()) + ")";
			}
			else {
				tmp +=
					" " + "(" + SafeResourceLoader.getString("DISABLED_STATUS",
						this.manager.getResourceBundle()) + ")";
			}
			message = new ConsoleMessage(tmp);
			this.manager.fireEvent(message);
		}
	}

	/**
	 * Prints the version of the plugin specified, if it exists. If no plugin
	 * exists, alerts the user to this fact.
	 *
	 * @param pluginName The plugin to find a version for
	 */
	private void printVersion(String pluginName) {
		String tmp = "";
		ConsoleMessage message;

		Plugin pack = this.manager.getPlugin(pluginName);

		if (pack != null) {
			tmp = SafeResourceLoader.getString("PLUGIN_VERSION",
				this.manager.getResourceBundle()) + pack.getVersion();
		}
		else {
			tmp = SafeResourceLoader
				.getString("PLUGIN_NOT_LOADED",
					this.manager.getResourceBundle())
				.replaceFirst("\\$PLUGIN", pluginName);
		}
		message = new ConsoleMessage(tmp);
		this.manager.fireEvent(message);
	}

	/**
	 * Alerts the user that the given feature is not yet completed or working
	 * correctly.
	 */
	private void printWIP() {
		ConsoleMessage message = new ConsoleMessage(SafeResourceLoader
			.getString("WIP_TEXT", this.manager.getResourceBundle()));
		this.manager.fireEvent(message);
	}

	private void reload(String pluginName) {
		ConsoleMessage message;
		Plugin pack = this.manager.getPlugin(pluginName);

		if (pack == null) {
			message = new ConsoleMessage(SafeResourceLoader
				.getString("PLUGIN_NOT_LOADED",
					this.manager.getResourceBundle())
				.replaceFirst("\\$PLUGIN", pluginName));
			this.manager.fireEvent(message);
			// stop right here. It does not exist
			return;
		}
		this.manager.reload(pack);
	}

	private void unload(String pluginName) {
		ConsoleMessage message;
		Plugin pack = this.manager.getPlugin(pluginName);

		if (pack == null) {
			message = new ConsoleMessage(SafeResourceLoader
				.getString("PLUGIN_NOT_LOADED",
					this.manager.getResourceBundle())
				.replaceFirst("\\$PLUGIN", pluginName));
			this.manager.fireEvent(message);
			// stop right here. It does not exist
			return;
		}
		if (this.manager.isEnabled(pack)) {
			this.manager.disable(pack);
		}
		this.manager.unloadPlugin(pack);
	}

}
