package com.ikalagaming.packages.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.packages.Package;

/**
 * Fired when (after) a package is enabled.
 *
 * @author Ches Burks
 *
 */
public class PackageEnabled extends Event {

	/**
	 * The package that was just enabled.
	 */
	private Package thePackage;

	/**
	 * Creates a new {@link PackageEnabled} for the given package.
	 * 
	 * @param enabled the package that has been enabled
	 *
	 */
	public PackageEnabled(Package enabled) {
		this.thePackage = enabled;
	}

	/**
	 * Returns a reference to the package was enabled.
	 *
	 * @return the name of the package
	 */
	public Package getPackage() {
		return this.thePackage;
	}

}
