package com.ikalagaming.plugins;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Contains all details about plugins that are loaded in memory.
 * 
 * @author Ches Burks
 *
 */
@Getter
@Setter
@AllArgsConstructor
class PluginDetails {
	/**
	 * The class loader that allows for loading from the plugin jar.
	 * 
	 * @param classLoader The new classloader.
	 * @return The plugins class loader.
	 */
	@SuppressWarnings("javadoc")
	private PluginClassLoader classLoader;
	/**
	 * The plugin info, read from the jar file.
	 * 
	 * @param info The plugin information.
	 * @return The plugin info.
	 */
	@SuppressWarnings("javadoc")
	private PluginInfo info;
	/**
	 * The plugin object itself.
	 * 
	 * @param plugin The plugin to store.
	 * @return The plugin object.
	 */
	@SuppressWarnings("javadoc")
	private Plugin plugin;

	/**
	 * The current state of the plugin.
	 * 
	 * @param state The new state of the plugin.
	 * @return The current plugin state.
	 */
	@SuppressWarnings("javadoc")
	private PluginState state;

	/**
	 * Dereference and clean up all information, pending removal of the
	 * associated plugin. 
	 */
	void dispose() {
		this.classLoader.dispose();
		this.info = null;
		this.plugin = null;
		this.state = PluginState.PENDING_REMOVAL;
	}

}
