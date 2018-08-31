package com.ikalagaming.plugins;

/**
 * The plugin state is a simple way of determining what a plugin is doing and
 * whether or not it can be interacted with or perform tasks. When each value
 * should be used is described in the appropriate {@link Plugin} methods.
 *
 * <br>
 * These values do not strictly have to be set as suggested, but it is optimal
 * if they are as changing the flow of how a plugin is operated will likely
 * cause problems.
 *
 * @author Ches Burks
 *
 */
public enum PluginState {
	/**
	 * The plugin is being loaded and is not yet in memory fully. It is not
	 * able to be usable yet. Related files are likely locked and some basic
	 * data is being loaded into memory.
	 */
	LOADING,
	/**
	 * The plugin is being unloaded from memory but is still partially loaded.
	 * It is not able to be used, but related files are likely still locked and
	 * the plugins data likely still exists in memory.
	 */
	UNLOADING,
	/**
	 * The plugin is being enabled but has not completed the process. It is not
	 * guaranteed to be usable yet.
	 */
	ENABLING,
	/**
	 * The plugin is disabling itself but has not completed the process. It is
	 * not likely to still be usable.
	 */
	DISABLING,
	/**
	 * The plugin is fully enabled and running. It can be used at this point.
	 */
	ENABLED,
	/**
	 * The plugin is fully disabled but still loaded in memory. It is not able
	 * to be used.
	 */
	DISABLED,
	/**
	 * The plugin has unloaded itself as much as reasonable. This is a value
	 * used to indicate that it is no longer in use and further removal is to be
	 * done outside of the plugin.
	 */
	PENDING_REMOVAL,
	/**
	 * There was a problem and the plugin is now in an unstable state. It may
	 * be partially loaded/enabled or unloaded/disabled. The system will attempt
	 * to fix the problem, but if it cannot then the plugin will be removed.
	 */
	CORRUPTED,
	/**
	 * The plugin does not exist as far as the manager is concerned. If the
	 * plugin state of a non-existent plugin is requested, this is the value
	 * that is returned.
	 */
	NOT_LOADED,
	/**
	 * The plugin has been discovered, which means the system found it in a
	 * file, has loaded data about it, but has yet to check dependencies or
	 * start loading it up.
	 */
	DISCOVERED,
	/**
	 * The system is checking dependencies for the plugin, but has not
	 * determined whether or not dependencies are satisfied.
	 */
	DEPS_CHECKING,
	/**
	 * The system has checked for dependencies, and confirmed that all
	 * dependencies are satisfied. A plugin with no dependencies has all
	 * dependencies satisfied. A plugin with all dependencies either being
	 * loaded simultaneously or having been loaded previously also has its
	 * dependencies satisfied.
	 */
	DEPS_SATISFIED,
	/**
	 * The plugin has dependencies which no currently loaded plugin, or
	 * plugin loaded simultaneously, satisfies. This will be reported and
	 * unloaded.
	 */
	DEPS_MISSING;

}
