package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.plugins.Plugin;

/**
 * Fired when (after) a plugin is disabled.
 *
 * @author Ches Burks
 *
 */
public class PluginDisabled extends Event {

	/**
	 * The plugin which was just disabled.
	 */
	private Plugin thePlugin;

	/**
	 * Creates a new {@link PluginDisabled} for the given plugin.
	 *
	 * @param disabled the plugin that has been disabled.
	 *
	 */
	public PluginDisabled(Plugin disabled) {
		this.thePlugin = disabled;
	}

	/**
	 * Returns a reference to the plugin was disabled.
	 *
	 * @return the name of the plugin
	 */
	public Plugin getPlugin() {
		return this.thePlugin;
	}

}
