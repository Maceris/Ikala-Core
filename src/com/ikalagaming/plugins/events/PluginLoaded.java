package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.plugins.Plugin;

/**
 * Fired when (after) a plugin is loaded.
 *
 * @author Ches Burks
 *
 */
public class PluginLoaded extends Event {

	/**
	 * The plugin that was just loaded.
	 */
	private Plugin thePlugin;

	/**
	 * Creates a new {@link PluginLoaded} for the given plugin.
	 *
	 * @param loaded the plugin that has been loaded
	 *
	 */
	public PluginLoaded(Plugin loaded) {
		this.thePlugin = loaded;
	}

	/**
	 * Returns a reference to the plugin was loaded.
	 *
	 * @return the name of the plugin
	 */
	public Plugin getPlugin() {
		return this.thePlugin;
	}

}
