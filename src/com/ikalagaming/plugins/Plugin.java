package com.ikalagaming.plugins;

import java.util.Set;

import com.ikalagaming.event.Listener;

/**
 * A distinct chunk of the program with a specific purpose and methods for
 * managing its state and interacting with the main program.
 *
 * @author Ches Burks
 *
 */
public abstract class Plugin {

	/**
	 * Returns a list of listeners for this plugin. These listeners will be used
	 * with the event system.
	 *
	 * @return a list of listeners for the plugin.
	 */
	public abstract Set<Listener> getListeners();

	/**
	 * This method is called when the plugin is disabled, and gives the plugin
	 * the chance to shut itself down and save any changes made in memory to
	 * disk if necessary.
	 *
	 * @return True if disabling was successful, false if there was a problem
	 */
	public boolean onDisable() {
		return true;
	}

	/**
	 * This method is called when the plugin is enabled. Initialization should
	 * be performed here, and configuration and data should be loaded from disk
	 * if necessary.
	 *
	 * @return True if enabling was successful, false if there was a problem
	 */
	public boolean onEnable() {
		return true;
	}

	/**
	 * Called when the plugin is loaded into memory. The plugin may or may not
	 * be enabled at this time.
	 *
	 * @return True if loading was successful, false if there was a problem
	 */
	public boolean onLoad() {
		return true;
	}

	/**
	 * Called just before the plugin is unloaded from memory. If it is enabled,
	 * then the plugin should disable itself now. Any memory that can reasonably
	 * be dereferenced by the plugin itself, should be. Files may be saved to
	 * disk if needed.
	 *
	 * @return True if unloading was successful, false if there was a problem
	 */
	public boolean onUnload() {
		return true;
	}

}
