package com.ikalagaming.packages;

import java.util.Set;

import com.ikalagaming.event.Listener;

/**
 * A distinct chunk of the program with a specific purpose and methods for
 * managing its state and interacting with the main program.
 *
 * @author Ches Burks
 *
 */
public interface Package {

	/**
	 * Returns a list of listeners for this package. These listeners will be
	 * used with the event system.
	 *
	 * @return a list of listeners for the package.
	 */
	public Set<Listener> getListeners();

	/**
	 * Returns the type of package this is. This is a string that describes the
	 * package, such as "Graphics" or "AI".
	 *
	 * @return a string descriptor of the package
	 */
	public String getName();

	/**
	 * Returns this classes version number. This changes periodically for each
	 * package subclass as they are changed and updated.
	 *
	 * @return the version
	 */
	public double getVersion();

	/**
	 * This method is called when the package is disabled, and gives the package
	 * the chance to shut itself down and save any changes made in memory to
	 * disk if necessary.
	 *
	 * @return True if disabling was successful, false if there was a problem
	 */
	public boolean onDisable();

	/**
	 * This method is called when the package is enabled. Initialization should
	 * be performed here, and configuration and data should be loaded from disk
	 * if necessary.
	 *
	 * @return True if enabling was successful, false if there was a problem
	 */
	public boolean onEnable();

	/**
	 * Called when the package is loaded into memory. The package may or may not
	 * be enabled at this time.
	 *
	 * @return True if loading was successful, false if there was a problem
	 */
	public boolean onLoad();

	/**
	 * Called just before the package is unloaded from memory. If it is enabled,
	 * then the package should disable itself now. Any memory that can
	 * reasonably be dereferenced by the package itself, should be. Files may be
	 * saved to disk if needed.
	 *
	 * @return True if unloading was successful, false if there was a problem
	 */
	public boolean onUnload();

}
