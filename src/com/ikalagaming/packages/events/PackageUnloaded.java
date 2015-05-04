package com.ikalagaming.packages.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.packages.Package;

/**
 * Fired when (after) a package is unloaded.
 *
 * @author Ches Burks
 *
 */
public class PackageUnloaded extends Event {

	/**
	 * The package that was just unloaded.
	 */
	private Package thePackage;

	/**
	 * Creates a new {@link PackageUnloaded} for the given package.
	 *
	 * @param unloaded the package that has been unloaded
	 *
	 */
	public PackageUnloaded(Package unloaded) {
		this.thePackage = unloaded;
	}

	/**
	 * Returns a reference to the package was unloaded.
	 *
	 * @return the name of the package
	 */
	public Package getPackage() {
		return this.thePackage;
	}

}
