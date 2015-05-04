package com.ikalagaming.packages.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.packages.Package;

/**
 * Fired when (after) a package is disabled.
 *
 * @author Ches Burks
 *
 */
public class PackageDisabled extends Event {

	/**
	 * The package which was just disabled.
	 */
	private Package thePackage;

	/**
	 * Creates a new {@link PackageDisabled} for the given package.
	 *
	 * @param disabled the package that has been disabled.
	 *
	 */
	public PackageDisabled(Package disabled) {
		this.thePackage = disabled;
	}

	/**
	 * Returns a reference to the package was disabled.
	 *
	 * @return the name of the package
	 */
	public Package getPackage() {
		return this.thePackage;
	}

}
