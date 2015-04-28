package com.ikalagaming.packages.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.packages.Package;

/**
 * Fired when (after) a package is loaded.
 *
 * @author Ches Burks
 *
 */
public class PackageLoaded extends Event {

	/**
	 * The package that was just loaded.
	 */
	private Package thePackage;

	/**
	 * Creates a new {@link PackageLoaded} for the given package.
	 * 
	 * @param loaded the package that has been loaded
	 *
	 */
	public PackageLoaded(Package loaded) {
		this.thePackage = loaded;
	}

	/**
	 * Returns a reference to the package was loaded.
	 *
	 * @return the name of the package
	 */
	public Package getPackage() {
		return this.thePackage;
	}

}
