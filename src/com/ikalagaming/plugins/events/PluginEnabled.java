package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.plugins.Plugin;

/**
 * Fired when (after) a plugin is enabled.
 *
 * @author Ches Burks
 *
 */
public class PluginEnabled extends Event {

	/**
	 * The plugin that was just enabled.
	 */
	private Plugin thePlugin;

	/**
	 * Creates a new {@link PluginEnabled} for the given plugin.
	 *
	 * @param enabled the plugin that has been enabled
	 *
	 */
	public PluginEnabled(Plugin enabled) {
		this.thePlugin = enabled;
	}

	/**
	 * Returns a reference to the plugin was enabled.
	 *
	 * @return the name of the plugin
	 */
	public Plugin getPlugin() {
		return this.thePlugin;
	}

}
