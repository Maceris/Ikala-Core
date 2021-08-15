package com.ikalagaming.plugins;

import com.ikalagaming.event.Listener;

import java.util.HashSet;
import java.util.Set;

/**
 * A distinct chunk of the program with a specific purpose and methods for
 * managing its state and interacting with the main program.
 *
 * @author Ches Burks
 *
 */
public abstract class Plugin {

	/**
	 * Created so we have something to return in {@link #getListeners()} without
	 * creating a new set every time. Private since we want subclasses to handle
	 * the listeners themselves.
	 */
	private Set<Listener> emptyListenerSet = new HashSet<>();

	/**
	 * Returns a list of listeners for this plugin. These listeners will be used
	 * with the event system.
	 *
	 * @return a list of listeners for the plugin.
	 */
	public Set<Listener> getListeners() {
		return this.emptyListenerSet;
	}

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

	/**
	 * Called when a plugin has loaded with a version that is newer than
	 * previously seen. This is the hook to do any activities for setting up or
	 * converting to a new version.
	 *
	 * @param lastVersion The last known version that was loaded.
	 */
	public void onUpgrade(String lastVersion) {}

}
