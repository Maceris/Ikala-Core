package com.ikalagaming.plugins;

import com.ikalagaming.event.Event;
import com.ikalagaming.event.EventManager;
import com.ikalagaming.event.Listener;
import com.ikalagaming.localization.Localization;
import com.ikalagaming.logging.Logging;
import com.ikalagaming.plugins.events.PluginDisabled;
import com.ikalagaming.plugins.events.PluginEnabled;
import com.ikalagaming.plugins.events.PluginLoaded;
import com.ikalagaming.plugins.events.PluginUnloaded;
import com.ikalagaming.system.SystemPlugin;
import com.ikalagaming.util.SafeResourceLoader;

import com.github.zafarkhaja.semver.Version;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

/**
 * Handles loading, unloading and storage of plugins. This is considered a
 * plugin, but is always enabled and never loaded.
 *
 * @author Ches Burks
 *
 */
public class PluginManager {

	private static PluginManager instance;

	/**
	 * Compare the order of version strings, as if using
	 * first.compareTo(second). This should be string semantic versions and can
	 * handle build info and the like but build metadata is ignored.
	 *
	 * For example, compareVersions("1.0.0-rc.1+build.1",
	 * "1.3.7+build.2.b8f12d7") would return a number < 0.
	 * compareVersions("2.1.3", "1.0.1") would return >0.
	 * compareVersions("1.0.0+build.1", "1.0.0+build.1") would return 0 because
	 * they are equal builds.
	 *
	 * @param first The first version string
	 * @param second The second version string
	 * @return the value of comparing the two versions.
	 */
	public final static int compareVersions(final String first,
		final String second) {
		return Version.valueOf(first).compareTo(Version.valueOf(second));
	}

	/**
	 * Shuts down the static instance (unregisters commands and unloads plugins)
	 * if it exists, and then nullifies the reference to it. This exists in case
	 * you wish to use your own instances of the Plugin Manager and not use the
	 * single static instance provided. If the instance does not exist, nothing
	 * happens. Note that a new static instance may be created if the instance
	 * is requested later.
	 *
	 * @see #getInstance()
	 * @see #getInstance(EventManager)
	 */
	public static void destoryInstance() {
		if (PluginManager.instance == null) {
			return;
		}
		PluginManager.instance.pluginLock.lock();
		try {
			for (String s : PluginManager.instance.loadedPlugins.keySet()) {
				PluginManager.instance.unloadPlugin(s);
			}
		}
		finally {
			PluginManager.instance.pluginLock.unlock();
		}

		PluginManager.instance.clearCommands();
		PluginManager.instance.eventManager = null;
		PluginManager.instance.commands = null;

		PluginManager.instance = null;
	}

	/**
	 * Returns the static instance of the plugin manager. Since there should
	 * only be one of these, having a static instance is fine and any class can
	 * get the instance which all other classes should share. If there is no
	 * instance yet, one will be created.
	 *
	 * @return the static instance of the Plugin Manager
	 * @see #getInstance(EventManager)
	 * @see PluginManager#destoryInstance()
	 */
	public static PluginManager getInstance() {
		if (PluginManager.instance == null) {
			PluginManager.instance =
				new PluginManager(EventManager.getInstance());
		}
		return PluginManager.instance;
	}

	/**
	 * Returns the static instance of the plugin manager. Since there should
	 * only be one of these, having a static instance is fine and any class can
	 * get the instance which all other classes should share. If there is no
	 * instance yet, one will be created.
	 *
	 * @param manager the event manager to use for the static instance
	 *
	 * @return the static instance of the Plugin Manager
	 * @see #getInstance()
	 * @see #destoryInstance()
	 */
	public static PluginManager getInstance(EventManager manager) {
		if (PluginManager.instance == null) {
			PluginManager.instance = new PluginManager(manager);
		}
		return PluginManager.instance;
	}

	/**
	 * Returns true if the first string is a strictly newer version than the
	 * second, where both are semantic version strings.
	 *
	 * For example, isNewerVersion("1.2.3", "0.0.1") returns true.
	 * isNewerVersion("1.0.0+build.1", "1.0.2") returns false.
	 * isNewerVersion("1.0.0+build.1", "1.0.0") returns false because it is the
	 * same.
	 *
	 * @param toCheck The string that is unknown relative to the known version.
	 * @param existing The existing version that is known but could be outdated.
	 * @return true if the first version is newer than the second, otherwise
	 *         false.
	 */
	public final static boolean isNewerVersion(final String toCheck,
		final String existing) {
		return Version.valueOf(toCheck).greaterThan(Version.valueOf(existing));
	}

	private EventManager eventManager;

	/** Maps strings to plugins loaded in memory */
	private HashMap<String, Plugin> loadedPlugins;

	/**
	 * Lock for plugin related maps
	 */
	private ReentrantLock pluginLock;

	private Map<Plugin, PluginState> pluginStates;

	private Map<Plugin, PluginInfo> pluginInfo;

	private ResourceBundle resourceBundle;

	/**
	 * If plugins should be enabled by the plugin manager when they are loaded.
	 * If this is false then plugins must be enabled manually after they are
	 * loaded.
	 */
	private boolean enableOnLoad;

	/**
	 * Lock for enableOnLoad
	 */
	private ReentrantLock enableLock;

	/**
	 * A list of all of the commands registered. This list is sorted.
	 */
	private ArrayList<PluginCommand> commands;

	private ReentrantLock commandLock;

	private SystemPlugin sysPlugin;

	/**
	 * Constructs a new {@link PluginManager} and initializes variables.
	 *
	 * @param evtManager The event manager to use for the plugin system
	 */
	public PluginManager(EventManager evtManager) {
		this.pluginLock = new ReentrantLock();
		this.commandLock = new ReentrantLock();
		this.enableLock = new ReentrantLock();
		this.enableOnLoad = false;
		this.eventManager = evtManager;
		this.loadedPlugins = new HashMap<>();
		// Ensure plugin states are synchronized
		this.pluginStates = Collections.synchronizedMap(new HashMap<>());
		this.pluginInfo = Collections.synchronizedMap(new HashMap<>());
		this.resourceBundle = ResourceBundle.getBundle(
			"com.ikalagaming.plugins.resources.PluginManager",
			Localization.getLocale());

		this.commands = new ArrayList<>();

		Logging.create();
		this.sysPlugin = new SystemPlugin(this);
		this.loadPlugin(this.sysPlugin);
		this.enable(this.sysPlugin);

		this.registerCommands();
	}

	/**
	 * Unregisters all commands
	 */
	public void clearCommands() {
		this.commandLock.lock();
		try {
			ArrayList<String> cmds = new ArrayList<>();
			for (PluginCommand s : this.commands) {
				cmds.add(s.getCommand());
			}
			for (String s : cmds) {
				this.unregisterCommand(s);
			}
			this.commands.clear();
		}
		finally {
			this.commandLock.unlock();
		}
	}

	/**
	 * Returns true if the array contains the given string.
	 *
	 * @param s the string to look for
	 * @return true if the string exists
	 */
	public boolean containsCommand(final String s) {
		int i;
		boolean res = false;
		this.commandLock.lock();
		try {
			for (i = 0; i < this.commands.size(); ++i) {
				if (this.commands.get(i).getCommand().equalsIgnoreCase(s)) {
					res = true;
					break;
				}
			}
		}
		finally {
			this.commandLock.unlock();
		}
		return res;
	}

	/**
	 * Deactivates the plugin and halts all of its operations. The plugin is
	 * still loaded in memory but not active. Calls {@link Plugin#onDisable()}.
	 * This changes the plugin state to {@link PluginState#DISABLING DISABLING}.
	 * The plugin state is changed to {@link PluginState#DISABLED DISABLED}
	 * after completion. If {@link Plugin#onDisable()} returns false (failed),
	 * the plugin state is set to {@link PluginState#CORRUPTED CORRUPTED}.
	 *
	 * @param target the plugin to disable
	 *
	 * @return true if the plugin has been successfully disabled, false if there
	 *         was a problem
	 */
	public boolean disable(Plugin target) {
		this.setPluginState(target, PluginState.DISABLING);
		String msgDisabling =
			SafeResourceLoader.getString("ALERT_DISABLING", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", target.getName());
		Logging.fine(SystemPlugin.PLUGIN_NAME, msgDisabling);

		boolean success = target.onDisable();
		if (success) {
			this.setPluginState(target, PluginState.DISABLED);
			PluginDisabled packDisabled = new PluginDisabled(target);
			this.fireEvent(packDisabled);
		}
		else {
			this.setPluginState(target, PluginState.CORRUPTED);
			String msgCorrupted = SafeResourceLoader
				.getString("PLUGIN_STATE_CORRUPTED", this.resourceBundle)
				.replaceFirst("\\$PLUIGN", target.getName());
			Logging.warning(SystemPlugin.PLUGIN_NAME, msgCorrupted);
		}

		return success;
	}

	/**
	 * Deactivates the plugin and halts all of its operations. The plugin is
	 * still loaded in memory but not active. Calls {@link Plugin#onDisable()}.
	 * This changes the plugins state to {@link PluginState#DISABLING
	 * DISABLING}. The plugin state is changed to {@link PluginState#DISABLED
	 * DISABLED} after completion. If {@link Plugin#onDisable()} returns false
	 * (failed), the plugin state is set to {@link PluginState#CORRUPTED
	 * CORRUPTED}.
	 *
	 * @param target the name of the plugin to disable
	 *
	 * @return true if the plugin has been successfully disabled, false if there
	 *         was a problem
	 */
	public boolean disable(final String target) {
		Optional<Plugin> p;
		this.pluginLock.lock();
		try {
			p = this.getPlugin(target);
		}
		finally {
			this.pluginLock.unlock();
		}

		if (!p.isPresent()) {
			return false;
		}
		Plugin plugin = p.get();

		boolean disabled = this.disable(plugin);

		if (!disabled) {
			this.setPluginState(plugin, PluginState.CORRUPTED);
		}
		return disabled;
	}

	/**
	 * Activates the plugin and enables it to perform its normal functions.
	 * Calls {@link Plugin#onEnable()}. This changes the plugin state to
	 * {@link PluginState#ENABLING ENABLING}. The plugin state is changed to
	 * {@link PluginState#ENABLED ENABLED} after completion. If
	 * {@link Plugin#onEnable()} returns false (failed), the plugin state is set
	 * to {@link PluginState#CORRUPTED CORRUPTED}.
	 *
	 * @param target The plugin to enable
	 *
	 * @return true if the plugin was successfully enabled, false if there was a
	 *         problem
	 */
	public boolean enable(Plugin target) {
		this.setPluginState(target, PluginState.ENABLING);
		String msgEnable =
			SafeResourceLoader.getString("ALERT_ENABLING", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", target.getName());
		Logging.fine(SystemPlugin.PLUGIN_NAME, msgEnable);

		boolean success = target.onEnable();
		if (success) {
			this.setPluginState(target, PluginState.ENABLED);
			PluginEnabled plugEnabled = new PluginEnabled(target);
			this.fireEvent(plugEnabled);
		}
		else {
			this.setPluginState(target, PluginState.CORRUPTED);
			String msgCorrupted = SafeResourceLoader
				.getString("PLUGIN_STATE_CORRUPTED", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", target.getName());
			Logging.warning(SystemPlugin.PLUGIN_NAME, msgCorrupted);
		}

		return success;
	}

	/**
	 * Activates the plugin and enables it to perform its normal functions.
	 * Calls {@link Plugin#onEnable()}. This changes the plugin state to
	 * {@link PluginState#ENABLING ENABLING}. The plugin state is changed to
	 * {@link PluginState#ENABLED ENABLED} after completion. If
	 * {@link Plugin#onEnable()} returns false (failed), the plugin state is set
	 * to {@link PluginState#CORRUPTED CORRUPTED}.
	 *
	 * @param target The name of the plugin to enable
	 *
	 * @return true if the plugin was successfully enabled, false if there was a
	 *         problem
	 */
	public boolean enable(final String target) {
		Optional<Plugin> p;
		this.pluginLock.lock();
		try {
			p = this.getPlugin(target);
		}
		finally {
			this.pluginLock.unlock();
		}

		if (!p.isPresent()) {
			return false;
		}

		Plugin plugin = p.get();

		boolean enabled = this.enable(plugin);

		if (!enabled) {
			this.setPluginState(plugin, PluginState.CORRUPTED);
		}

		return enabled;
	}

	/**
	 * Returns true if this plugin manager automatically enables plugins when
	 * they are loaded. If they are not enabled on load, they must be enabled
	 * manually after being loaded.
	 *
	 * @return true if the plugins enable after loading, false if they must be
	 *         manually enabled
	 * @see #setEnableOnLoad(boolean)
	 */
	public boolean enableOnLoad() {
		boolean res = false;
		this.enableLock.lock();
		try {
			res = this.enableOnLoad;
		}
		finally {
			this.enableLock.unlock();
		}
		return res;
	}

	/**
	 * Load up the plugin info from the given jar file and return it. If there
	 * is some error like the file not actually being a jar or plugin info
	 * missing, then the returned optional is empty.
	 *
	 * @param jar the jar to load info from.
	 * @return an optional containing the plugin info, or an empty optional on
	 *         failure.
	 */
	public Optional<PluginInfo> extractPluginInfo(final File jar) {
		JarFile jfile;
		ZipEntry config;

		final String PLUGIN_CONFIG_FILENAME = "plugin.yml";
		final String fileName = jar.getName();

		/*
		 * Check for being a jar file check for plugin info file load and check
		 * for valid info load the file if necessary
		 */
		try {
			jfile = new JarFile(jar);
			config = jfile.getEntry(PLUGIN_CONFIG_FILENAME);
			if (config == null) {
				String msg = SafeResourceLoader
					.getString("PLUGIN_CONFIG_MISSING", this.resourceBundle)
					.replaceFirst("\\$FILE", PLUGIN_CONFIG_FILENAME);
				Logging.warning(SystemPlugin.PLUGIN_NAME, msg);
				jfile.close();
				return Optional.empty();
			}
		}
		catch (Exception e) {
			String msg = SafeResourceLoader
				.getString("PLUGIN_JAR_ERROR", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", fileName);
			Logging.warning(SystemPlugin.PLUGIN_NAME, msg);
			return Optional.empty();
		}

		// grab an input stream for the configuration file
		InputStream configIStream;
		try {
			configIStream = jfile.getInputStream(config);
		}
		catch (IOException e1) {
			String msg = SafeResourceLoader
				.getString("PLUGIN_CONFIG_READ_ERROR", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", fileName);
			Logging.warning(SystemPlugin.PLUGIN_NAME, msg);
			try {
				jfile.close();
			}
			catch (IOException e) {
				String errorMsg = SafeResourceLoader
					.getString("PLUGIN_JAR_CLOSE_ERROR", this.resourceBundle)
					.replaceFirst("\\$PLUGIN", fileName);
				Logging.warning(SystemPlugin.PLUGIN_NAME, errorMsg);
				// wow we really must have messed up
			}
			return Optional.empty();
		}

		// Load in the plugin info from the config file
		PluginInfo info = null;
		try {
			info = new PluginInfo(configIStream);
		}
		catch (InvalidDescriptionException e1) {
			String msg = SafeResourceLoader
				.getString("PLUGIN_INVALID_DESCRIPTION", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", fileName);
			Logging.warning(SystemPlugin.PLUGIN_NAME, msg);
			Logging.warning(SystemPlugin.PLUGIN_NAME, e1.getMessage());
			try {
				jfile.close();
			}
			catch (IOException e) {
				String errorMsg = SafeResourceLoader
					.getString("PLUGIN_JAR_CLOSE_ERROR", this.resourceBundle)
					.replaceFirst("\\$PLUGIN", fileName);
				Logging.warning(SystemPlugin.PLUGIN_NAME, errorMsg);
			}
			return Optional.empty();
		}

		try {
			jfile.close();// We already loaded in the config info
		}
		catch (IOException e1) {
			String errorMsg = SafeResourceLoader
				.getString("PLUGIN_JAR_CLOSE_ERROR", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", fileName);
			Logging.warning(SystemPlugin.PLUGIN_NAME, errorMsg);
		}
		return Optional.ofNullable(info);
	}

	/**
	 * Fires an event with a message to a plugin type from the plugin manager.
	 * If an error occurs, this will return false. The event should not have
	 * been sent if false was returned.
	 *
	 * @param event the event to fire
	 *
	 * @return true if the event was fired correctly
	 */
	public boolean fireEvent(Event event) {
		if (this.eventManager == null) {
			String err = SafeResourceLoader
				.getString("PLUGIN_NOT_LOADED", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", SystemPlugin.PLUGIN_NAME);
			System.err.println(err);
			return false;
		}

		if (event != null) {// just in case the assignment failed
			this.eventManager.fireEvent(event);
		}

		return true;
	}

	/**
	 * Returns the plugin that registered the given string, or null if it cannot
	 * be found.
	 *
	 * @param s the string to look for
	 * @return the owner of the command
	 */
	public Plugin getCommandParent(final String s) {
		int i;
		Plugin ret = null;
		this.commandLock.lock();
		try {
			for (i = 0; i < this.commands.size(); ++i) {
				if (this.commands.get(i).getCommand().equalsIgnoreCase(s)) {
					ret = this.commands.get(i).getOwner();
				}
			}
		}
		finally {
			this.commandLock.unlock();
		}
		return ret;
	}

	/**
	 * Returns a clone of the commands list.
	 *
	 * @return a copy of the stored list
	 */
	public ArrayList<PluginCommand> getCommands() {
		ArrayList<PluginCommand> cmds = new ArrayList<>();
		this.commandLock.lock();
		try {
			for (PluginCommand pc : this.commands) {
				cmds.add(pc);
			}
		}
		finally {
			this.commandLock.unlock();
		}
		return cmds;
	}

	/**
	 * Returns the index of the given string if it exists. If it is not in the
	 * array, returns -1.
	 *
	 * @param s the string to look for
	 * @return the index of the string
	 */
	private int getIndexOfCommand(String s) {
		int i;
		boolean found = false;
		this.commandLock.lock();
		try {
			for (i = 0; i < this.commands.size(); ++i) {
				if (this.commands.get(i).getCommand().equalsIgnoreCase(s)) {
					found = true;
					break;
				}
			}
		}
		finally {
			this.commandLock.unlock();
		}
		if (!found) {
			i = -1;
		}
		return i;
	}

	/**
	 * Get the plugin info for a given plugin. If no info exists for the
	 * specified plugin, an empty optinal is returned.
	 *
	 * @param target The plugin to get info from.
	 * @return The info for the specified plugin.
	 */
	public Optional<PluginInfo> getInfo(Plugin target) {
		Optional<PluginInfo> ret;
		this.pluginLock.lock();
		try {
			ret = Optional.ofNullable(this.pluginInfo.get(target));
		}
		finally {
			this.pluginLock.unlock();
		}
		return ret;
	}

	/**
	 * Returns the map of plugin name to Plugin of the currently loaded plugins.
	 *
	 * @return the plugin map
	 */
	public HashMap<String, Plugin> getLoadedPlugins() {
		HashMap<String, Plugin> ret = new HashMap<>();
		this.pluginLock.lock();
		try {
			ret.putAll(this.loadedPlugins);
		}
		finally {
			this.pluginLock.unlock();
		}
		return ret;
	}

	/**
	 * If a plugin of the given type exists ({@link #isLoaded(String)}), then
	 * the plugin that is of that type is returned. Otherwise, an empty optional
	 * is returned.
	 *
	 * @param type The plugin type
	 * @return The Plugin with the given type
	 */
	public Optional<Plugin> getPlugin(final String type) {
		Optional<Plugin> ret = null;
		this.pluginLock.lock();
		try {
			ret = Optional.ofNullable(this.loadedPlugins.get(type));
		}
		finally {
			this.pluginLock.unlock();
		}
		return ret;
	}

	/**
	 * Returns the {@link PluginState current state} of the plugin.
	 *
	 * @param target The plugin to fetch the state of
	 *
	 * @return a PluginState representing the status of this plugin
	 */
	public PluginState getPluginState(Plugin target) {
		PluginState ret = PluginState.NOT_LOADED;
		this.pluginLock.lock();
		try {
			if (!this.pluginStates.containsKey(target)) {
				ret = PluginState.NOT_LOADED;
			}
			else {
				ret = this.pluginStates.get(target);
			}
		}
		finally {
			this.pluginLock.unlock();
		}

		return ret;
	}

	/**
	 * Returns the resource bundle for the plugin manager. This is not safe and
	 * could be null.
	 *
	 * @return the current resource bundle
	 */
	public ResourceBundle getResourceBundle() {
		return this.resourceBundle;
	}

	/**
	 * Returns true if the plugin is enabled, and false otherwise. A state of
	 * {@link PluginState#ENABLED ENABLED} returns true, any other states will
	 * return false. Plugins that are not loaded will return false.
	 *
	 * @param target The plugin to check for enabled status
	 *
	 * @return true if the plugin is fully ready to operate
	 */
	public boolean isEnabled(Plugin target) {
		boolean ret = true;
		this.pluginLock.lock();
		try {
			if (!this.isLoaded(target)) {
				ret = false;
			}
		}
		finally {
			this.pluginLock.unlock();
		}
		if (!ret) {// ret starts as true, if set to false (!loaded), return f.
			return ret;
		}

		ret = false;
		this.pluginLock.lock();
		try {
			PluginState state = this.getPluginState(target);
			if (state.equals(PluginState.ENABLED)) {
				ret = true;
			}
		}
		finally {
			this.pluginLock.unlock();
		}

		return ret;
	}

	/**
	 * Returns true if the plugin is enabled, and false otherwise. A state of
	 * {@link PluginState#ENABLED ENABLED} returns true, any other states will
	 * return false. Plugins that are not loaded will return false.
	 *
	 * @param target The name of plugin to check for enabled status
	 *
	 * @return true if the plugin is fully ready to operate
	 */
	public boolean isEnabled(final String target) {
		boolean ret = false;
		this.pluginLock.lock();
		try {
			Optional<Plugin> p = this.getPlugin(target);
			if (p.isPresent()) {
				ret = this.isEnabled(p.get());
			}
		}
		finally {
			this.pluginLock.unlock();
		}
		return ret;
	}

	/**
	 * Returns true if a plugin exists that has the same type as the provided
	 * plugin (for example: "Graphics"). This is the same as calling
	 * <code>{@link #isLoaded(String) isLoaded}({@link Plugin#getName()})</code>
	 *
	 * @param type the plugin type
	 * @return true if the plugin is loaded in memory, false if it does not
	 *         exist
	 */
	public boolean isLoaded(Plugin type) {
		boolean ret;
		this.pluginLock.lock();
		try {
			ret = this.loadedPlugins.containsKey(type.getName());
		}
		finally {
			this.pluginLock.unlock();
		}
		return ret;
	}

	/**
	 * Returns true if a plugin exists with the given type (for example:
	 * "Graphics")'
	 *
	 * @param type the plugin type
	 * @return true if the plugin is loaded in memory, false if it does not
	 *         exist
	 */
	public boolean isLoaded(final String type) {
		boolean ret;
		this.pluginLock.lock();
		try {
			ret = this.loadedPlugins.containsKey(type);
		}
		finally {
			this.pluginLock.unlock();
		}
		return ret;
	}

	/**
	 * <p>
	 * Loads the given plugin into memory, stores it by type, and enables it if
	 * plugins are enabled on load by default.
	 * </p>
	 * <p>
	 * If the type of plugin already exists in the manager, and the new plugin
	 * has a higher version number, then the old plugin is unloaded and the new
	 * one is loaded in its place. If the versions are equal, or the new plugin
	 * is older, then it does not load the new version and returns false.
	 * </p>
	 *
	 * @see PluginManager#enableOnLoad()
	 *
	 * @param toLoad the plugin to load
	 * @return true if the plugin was loaded properly, false otherwise
	 */
	public boolean loadPlugin(Plugin toLoad) {
		final String pluginName = toLoad.getName();
		String loading =
			SafeResourceLoader.getString("ALERT_LOADING", this.resourceBundle);
		loading = loading.replaceFirst("\\$PLUGIN", pluginName);
		loading = loading.replaceFirst("\\$VERSION", "" + toLoad.getVersion());
		Logging.fine(SystemPlugin.PLUGIN_NAME, loading);
		// if the plugin exists and is older than toLoad, unload
		if (this.isLoaded(toLoad)) {
			String alreadyLoaded = SafeResourceLoader
				.getString("ALERT_PLUGIN_ALREADY_LOADED", this.resourceBundle);
			alreadyLoaded = alreadyLoaded.replaceFirst("\\$PLUGIN", pluginName);
			alreadyLoaded = alreadyLoaded.replaceFirst("\\$VERSION",
				"" + toLoad.getVersion());
			Logging.fine(SystemPlugin.PLUGIN_NAME, alreadyLoaded);

			boolean lowerVersion = false;
			this.pluginLock.lock();
			try {
				lowerVersion = this.loadedPlugins.get(pluginName)
					.getVersion() < toLoad.getVersion();
			}
			finally {
				this.pluginLock.unlock();
			}
			if (lowerVersion) {
				this.unloadPlugin(pluginName);
				// unload the old plugin and continue loading the new one
			}
			else {
				String outdated = SafeResourceLoader
					.getString("ALERT_PLUGIN_OUTDATED", this.resourceBundle);
				outdated = outdated.replaceFirst("\\$PLUGIN", pluginName);
				outdated = outdated.replaceFirst("\\$VERSION",
					"" + toLoad.getVersion());
				Logging.fine(SystemPlugin.PLUGIN_NAME, outdated);
				return false;
			}
		}
		this.setPluginState(toLoad, PluginState.LOADING);

		this.pluginLock.lock();
		try {
			// store the new plugin
			this.loadedPlugins.put(pluginName, toLoad);
		}
		finally {
			this.pluginLock.unlock();
		}

		for (Listener l : toLoad.getListeners()) {
			this.eventManager.registerEventListeners(l);
		}
		String msg = SafeResourceLoader
			.getString("ALERT_REG_EVENT_LISTENERS", this.resourceBundle)
			.replaceFirst("\\$PLUGIN", pluginName);
		Logging.finer(SystemPlugin.PLUGIN_NAME, msg);

		// load it
		toLoad.onLoad();

		this.setPluginState(toLoad, PluginState.DISABLED);
		PluginLoaded packLoaded = new PluginLoaded(toLoad);
		this.fireEvent(packLoaded);

		return true;
	}

	/**
	 * Loads a plugin by name from a folder.
	 *
	 * @param path the path to the folder containing the file
	 * @param name the filename to load from, without a file extension
	 * @return true on success, false if it failed
	 */
	public boolean loadPlugin(String path, String name) {

		File pluginFolder = null;
		{// keeps scope name space clean
			Optional<File> folderMaybe = this.plGetFolder(path);
			if (!folderMaybe.isPresent()) {
				return false;
			}
			pluginFolder = folderMaybe.get();
		}

		ArrayList<File> jars = this.plGetAllJars(pluginFolder);

		File jarFile = null;

		{// keeps scope name space clean
			Optional<File> jarMaybe = this.plPickJarByName(jars, name);
			if (!jarMaybe.isPresent()) {
				return false;
			}
			jarFile = jarMaybe.get();
		}

		PluginInfo info = null;

		{// keeps scope name space clean
			Optional<PluginInfo> infoMaybe = this.extractPluginInfo(jarFile);
			if (!infoMaybe.isPresent()) {
				return false;
			}
			info = infoMaybe.get();
		}

		ClassLoader loader;
		try {
			loader =
				URLClassLoader.newInstance(new URL[] {jarFile.toURI().toURL()},
					this.getClass().getClassLoader());
		}
		catch (MalformedURLException e) {
			String err = SafeResourceLoader
				.getString("PLUGIN_URL_INVALID", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", name);
			Logging.warning(SystemPlugin.PLUGIN_NAME, err);
			return false;
		}

		Class<?> clazz;
		try {
			clazz = Class.forName(info.getMain(), true, loader);
		}
		catch (ClassNotFoundException e) {
			String err = SafeResourceLoader
				.getString("PLUGIN_MAIN_METHOD_MISSING", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", name);
			Logging.warning(SystemPlugin.PLUGIN_NAME, err);
			return false;
		}

		Object classInstance;

		try {
			classInstance = clazz.newInstance();
		}
		catch (InstantiationException e) {
			String err = SafeResourceLoader
				.getString("PLUGIN_CANT_INSTANTIATE_MAIN", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", name);
			Logging.warning(SystemPlugin.PLUGIN_NAME, err);
			return false;
		}
		catch (IllegalAccessException e) {
			String err = SafeResourceLoader
				.getString("PLUGIN_MAIN_ILLEGAL_ACCESS", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", name);
			Logging.warning(SystemPlugin.PLUGIN_NAME, err);
			return false;
		}
		if (!(classInstance instanceof Plugin)) {
			String err = SafeResourceLoader
				.getString("PLUGIN_MAIN_NOT_A_PLUGIN", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", name);
			Logging.warning(SystemPlugin.PLUGIN_NAME, err);
			return false;
		}

		Plugin p = (Plugin) classInstance;

		final String pluginName = info.getName();
		String loading =
			SafeResourceLoader.getString("ALERT_LOADING", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", pluginName)
				.replaceFirst("\\$VERSION", info.getVersion());
		Logging.fine(SystemPlugin.PLUGIN_NAME, loading);

		// if the plugin exists and is older than toLoad, unload
		if (this.isLoaded(pluginName)) {
			String alreadyLoaded = SafeResourceLoader
				.getString("ALERT_PLUGIN_ALREADY_LOADED", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", pluginName)
				.replaceFirst("\\$VERSION", info.getVersion());
			Logging.fine(SystemPlugin.PLUGIN_NAME, alreadyLoaded);

			boolean existingPluginOld = false;
			this.pluginLock.lock();
			try {
				// TODO real version comparison
				existingPluginOld =
					PluginManager.isNewerVersion(info.getVersion(), "0.0.0");
			}
			finally {
				this.pluginLock.unlock();
			}
			if (existingPluginOld) {
				this.unloadPlugin(pluginName);
				// unload the old plugin and continue loading the new one
			}
			else {
				String outdated = SafeResourceLoader
					.getString("ALERT_PLUGIN_OUTDATED", this.resourceBundle);
				outdated = outdated.replaceFirst("\\$PLUGIN", pluginName);
				outdated =
					outdated.replaceFirst("\\$VERSION", "" + info.getVersion());
				Logging.fine(SystemPlugin.PLUGIN_NAME, outdated);
				return false;
			}
		}

		this.setPluginState(p, PluginState.LOADING);

		this.pluginLock.lock();
		try {
			// store the new plugin
			this.loadedPlugins.put(pluginName, p);
			this.pluginInfo.put(p, info);
		}
		finally {
			this.pluginLock.unlock();
		}

		for (Listener l : p.getListeners()) {
			this.eventManager.registerEventListeners(l);
		}
		String msg = SafeResourceLoader
			.getString("ALERT_REG_EVENT_LISTENERS", this.resourceBundle)
			.replaceFirst("\\$PLUGIN", pluginName);
		Logging.finer(SystemPlugin.PLUGIN_NAME, msg);

		// load it
		p.onLoad();

		this.setPluginState(p, PluginState.DISABLED);
		PluginLoaded packLoaded = new PluginLoaded(p);
		this.fireEvent(packLoaded);

		return true;

		// TODO finish this?
	}

	/**
	 * Return all jar files in the specified folder. If none are found, an empty
	 * list is returned.
	 *
	 * @param folder a valid folder
	 * @return a list of all readable jars in the folder
	 */
	private ArrayList<File> plGetAllJars(final File folder) {
		File[] files;
		files = folder.listFiles();

		ArrayList<File> jars = new ArrayList<>();
		if (files == null) {
			String msg = SafeResourceLoader
				.getString("PLUGIN_FILES_NULL", this.resourceBundle)
				.replaceFirst("\\$FOLDER", folder.getAbsolutePath());
			Logging.warning(SystemPlugin.PLUGIN_NAME, msg);
			return jars;
		}
		if (files.length == 0) {
			String msg = SafeResourceLoader
				.getString("PLUGIN_FOLDER_EMPTY", this.resourceBundle)
				.replaceFirst("\\$FOLDER", folder.getAbsolutePath());
			Logging.warning(SystemPlugin.PLUGIN_NAME, msg);
			return jars;
		}

		for (File file : files) {
			try {
				if (!file.exists()) {
					continue;
				}
				if (!file.canRead()) {
					continue;
				}
				if (file.isDirectory()) {
					continue;
				}
				if (!file.getName().endsWith(".jar")) {
					continue;
				}
			}
			catch (SecurityException secExcep) {
				String msg = SafeResourceLoader
					.getString("PLUGIN_FILE_SECURITY_ERR", this.resourceBundle)
					.replaceFirst("\\$FILE", file.getName());
				Logging.fine(SystemPlugin.PLUGIN_NAME, msg);
				// Maybe one or more files can't be read
				continue;
			}
			jars.add(file);
		}

		return jars;
	}

	/**
	 * For plugin loading. The return value is an empty optional if there was a
	 * problem accessing the folder from path. If there is a file in the
	 * optional it should be an existing folder that can be read.
	 *
	 * @param path the path of the folder to return as a File
	 * @return an optional containing the folder or empty if there was an error
	 */
	private Optional<File> plGetFolder(final String path) {
		File pluginFolder;
		try {
			pluginFolder = new File(path);
		}
		catch (NullPointerException nullExcept) {
			String msg = SafeResourceLoader.getString("PLUGIN_PATH_NULL",
				this.resourceBundle);
			Logging.warning(SystemPlugin.PLUGIN_NAME, msg);
			return Optional.empty();
		}
		try {
			if (!pluginFolder.exists()) {
				String msg = SafeResourceLoader
					.getString("PLUGIN_FOLDER_NOT_FOUND", this.resourceBundle)
					.replaceFirst("\\$FOLDER", pluginFolder.getAbsolutePath());
				Logging.warning(SystemPlugin.PLUGIN_NAME, msg);
				return Optional.empty();
			}
			if (!pluginFolder.isDirectory()) {
				String msg = SafeResourceLoader
					.getString("PLUGIN_FOLDER_NOT_FOLDER", this.resourceBundle)
					.replaceFirst("\\$FOLDER", pluginFolder.getAbsolutePath());
				Logging.warning(SystemPlugin.PLUGIN_NAME, msg);
				return Optional.empty();
			}
			if (!pluginFolder.canRead()) {
				String msg = SafeResourceLoader
					.getString("PLUGIN_FOLDER_UNREADABLE", this.resourceBundle)
					.replaceFirst("\\$FOLDER", pluginFolder.getAbsolutePath());
				Logging.warning(SystemPlugin.PLUGIN_NAME, msg);
				return Optional.empty();
			}
		}
		catch (SecurityException securExcep) {
			String msg = SafeResourceLoader
				.getString("PLUGIN_FOLDER_ACCESS_DENIED", this.resourceBundle)
				.replaceFirst("\\$FOLDER", path);
			Logging.warning(SystemPlugin.PLUGIN_NAME, msg);
			return Optional.empty();
		}
		return Optional.of(pluginFolder);
	}

	/**
	 * Returns an optional that contains the jar file with the specified name,
	 * or an empty optional if none was found in the list of files. The file
	 * name is without an extension so "example.jar" should be passed as
	 * "example", and "test.1.0.2.jar" should be "test.1.0.2".
	 *
	 * @param files The list of files to look through, must all be valid
	 *            readable files.
	 * @param name the name of the jar, without a jar extension.
	 * @return an optional containing the matching jar, or an empty optional if
	 *         it was not found
	 */
	private Optional<File> plPickJarByName(final List<File> files,
		final String name) {
		Pattern fileNamePattern = Pattern.compile(name + "\\.jar\\z");
		Matcher fileNameMatcher = fileNamePattern.matcher("");

		for (File file : files) {
			try {
				if (fileNameMatcher.reset(file.getName()).matches()) {
					return Optional.of(file);
				}
			}
			catch (SecurityException secExcep) {
				String msg = SafeResourceLoader
					.getString("PLUGIN_FILE_SECURITY_ERR", this.resourceBundle)
					.replaceFirst("\\$FILE", file.getName());
				Logging.fine(SystemPlugin.PLUGIN_NAME, msg);
				// Maybe one or more files can't be read
				continue;
			}
		}

		// It hasn't returned by now which means it couldn't find a match
		String msg = SafeResourceLoader
			.getString("PLUGIN_NOT_FOUND", this.resourceBundle)
			.replaceFirst("\\$PLUGIN", name);
		Logging.warning(SystemPlugin.PLUGIN_NAME, msg);
		return Optional.empty();
	}

	/**
	 * Attempts to register the command for the given class. If the command
	 * already exists, an error is logged and the method returns false.
	 *
	 * @param command the command to register
	 * @param owner what plugin is registering the command
	 * @return true if the command registered successfully
	 */
	public boolean registerCommand(final String command, Plugin owner) {
		if (this.containsCommand(command)) {
			int index = this.getIndexOfCommand(command);
			String msg = SafeResourceLoader.getString(
				"COMMAND_ALREADY_REGISTERED", this.getResourceBundle());
			msg = msg.replaceFirst("\\$COMMAND", command);
			msg = msg.replaceFirst("\\$PLUGIN",
				this.commands.get(index).getOwner().getName());
			Logging.warning(SystemPlugin.PLUGIN_NAME, msg);
			return false;
		}
		PluginCommand cmd = new PluginCommand(command, owner);
		this.commandLock.lock();
		try {
			this.commands.add(cmd);
		}
		finally {
			this.commandLock.unlock();
		}

		String msg = SafeResourceLoader.getString("REGISTERED_COMMAND",
			this.getResourceBundle());
		msg = msg.replaceFirst("\\$COMMAND", command);
		msg = msg.replaceFirst("\\$PLUGIN", owner.getName());
		Logging.finest(SystemPlugin.PLUGIN_NAME, msg);
		this.commandLock.lock();
		try {
			java.util.Collections.sort(this.commands);
		}
		finally {
			this.commandLock.unlock();
		}

		return true;
	}

	/**
	 * Registers commands with the registry that the plugin manager uses
	 */
	private void registerCommands() {
		ArrayList<String> cmds = new ArrayList<>();
		cmds.add("COMMAND_ENABLE");
		cmds.add("COMMAND_DISABLE");
		cmds.add("COMMAND_LOAD");
		cmds.add("COMMAND_UNLOAD");
		cmds.add("COMMAND_LIST_PLUGINS");
		cmds.add("COMMAND_RELOAD");
		cmds.add("COMMAND_HELP");
		cmds.add("COMMAND_VERSION");

		String tmp = "";

		for (String s : cmds) {
			tmp = SafeResourceLoader.getString(s, this.resourceBundle);
			this.registerCommand(tmp, this.sysPlugin);
		}

	}

	/**
	 * This is essentially restarting the plugins. The plugin is disabled if it
	 * is enabled, unloaded, then loaded.
	 *
	 * @param target The plugin to reload
	 *
	 * @return true if the plugin reloaded successfully, false otherwise
	 */
	public boolean reload(Plugin target) {
		if (!this.isLoaded(target)) {
			String tmp = SafeResourceLoader
				.getString("PLUGIN_NOT_LOADED", this.getResourceBundle())
				.replaceFirst("\\$PLUGIN", target.getName());
			Logging.warning(SystemPlugin.PLUGIN_NAME, tmp);
			return false;
		}
		if (this.isEnabled(target)) {
			if (!this.disable(target)) {
				// disable failed
				this.setPluginState(target, PluginState.CORRUPTED);

				return false;
			}
		}
		this.setPluginState(target, PluginState.UNLOADING);
		this.unloadPlugin(target);
		this.setPluginState(target, PluginState.LOADING);
		if (!this.loadPlugin(target)) {
			this.setPluginState(target, PluginState.CORRUPTED);
			return false;
		}
		return true;
	}

	/**
	 * Sets the flag for automatically enabling plugins when they are loaded. If
	 * they are not enabled on load, they must be enabled manually after being
	 * loaded.
	 * <p>
	 * This allows some customization of how the plugin system works.
	 *
	 * @param newValue true if the plugins should be automatically enabled after
	 *            loading, false if they should be manually enabled
	 */
	public void setEnableOnLoad(final boolean newValue) {
		this.enableLock.lock();
		try {
			this.enableOnLoad = newValue;
		}
		finally {
			this.enableLock.unlock();
		}
	}

	private void setPluginState(Plugin target, PluginState newState) {
		boolean added = false;

		this.pluginLock.lock();
		try {
			if (!this.pluginStates.containsKey(target)) {
				this.pluginStates.put(target, newState);
				added = true;
			}
		}
		finally {
			this.pluginLock.unlock();
		}
		if (added) {
			return;
		}

		this.pluginLock.lock();
		try {
			// replaces the old state
			PluginState oldState = this.pluginStates.get(target);
			if (!oldState.equals(PluginState.PENDING_REMOVAL)) {
				// can't change plugins pending removal
				this.pluginStates.put(target, newState);
			}
		}
		finally {
			this.pluginLock.unlock();
		}
	}

	/**
	 * Attempts to unload the plugin from memory. Does nothing if the plugin is
	 * not loaded. Plugins are disabled before unloading. This calls
	 * {@link #unloadPlugin(String)} using the plugin type.
	 *
	 * @param toUnload The type of plugin to unload
	 */
	public void unloadPlugin(Plugin toUnload) {
		/*
		 * Using a string the plugins type to ensure the plugin that is stored
		 * in this class is modified and not just the plugin passed to the
		 * method.
		 */
		String type = toUnload.getName();
		if (!this.isLoaded(type)) {
			String notLoaded = SafeResourceLoader
				.getString("PLUGIN_NOT_LOADED", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", type);
			Logging.fine(SystemPlugin.PLUGIN_NAME, notLoaded);
			return;
		}
		this.unloadPlugin(type);
	}

	/**
	 * Attempts to unload the plugin from memory. If no plugin exists with the
	 * given name ({@link #isLoaded(String)}), returns false and does nothing.
	 *
	 * @param toUnload The type of plugin to unload
	 * @return true if the plugin was unloaded properly
	 */
	public boolean unloadPlugin(final String toUnload) {
		String unloading = SafeResourceLoader.getString("ALERT_UNLOADING",
			this.resourceBundle);
		unloading = unloading.replaceFirst("\\$PLUGIN", toUnload);
		Logging.fine(SystemPlugin.PLUGIN_NAME, unloading);
		if (!this.isLoaded(toUnload)) {
			String notLoaded = SafeResourceLoader
				.getString("PLUGIN_NOT_LOADED", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", toUnload);
			Logging.fine(SystemPlugin.PLUGIN_NAME, notLoaded);
			return false;
		}

		Plugin pack;
		this.pluginLock.lock();
		try {
			pack = this.loadedPlugins.get(toUnload);
		}
		finally {
			this.pluginLock.unlock();
		}
		if (pack == null) {
			return false;// TODO log this problem
		}

		// It has to be disabled before unloading.
		if (this.isEnabled(pack)) {
			this.disable(pack);
		}
		this.setPluginState(pack, PluginState.UNLOADING);

		pack.onUnload();
		// TODO will this not become an invalid reference during event sending?
		PluginUnloaded packUnloaded = new PluginUnloaded(pack);
		this.fireEvent(packUnloaded);

		for (Listener l : pack.getListeners()) {
			this.eventManager.unregisterEventListeners(l);
		}
		String unreg = SafeResourceLoader
			.getString("ALERT_UNREG_EVENT_LISTENERS", this.resourceBundle)
			.replaceFirst("\\$PLUGIN", SystemPlugin.PLUGIN_NAME);
		Logging.finer(SystemPlugin.PLUGIN_NAME, unreg);
		this.pluginLock.lock();
		try {
			this.loadedPlugins.remove(toUnload);
			this.pluginStates.remove(pack);
		}
		finally {
			this.pluginLock.unlock();
		}

		String unloaded =
			SafeResourceLoader.getString("ALERT_UNLOADED", this.resourceBundle);
		unloaded = unloaded.replaceFirst("\\$PLUGIN", toUnload);
		Logging.fine(SystemPlugin.PLUGIN_NAME, unloaded);
		return true;
	}

	/**
	 * Unregisters the given command.
	 *
	 * @param command the command to remove
	 * @return true if the command was removed
	 */
	public boolean unregisterCommand(final String command) {
		boolean found = false;

		this.commandLock.lock();
		try {
			if (this.containsCommand(command)) {

				while (this.containsCommand(command)) {// just in case there are
														// multiple
					int index = this.getIndexOfCommand(command);
					this.commands.remove(index);
				}
				String msg = SafeResourceLoader.getString(
					"UNREGISTERED_COMMAND", this.getResourceBundle());
				msg = msg.replaceFirst("\\$COMMAND", command);
				Logging.finest(SystemPlugin.PLUGIN_NAME, msg);
				found = true;
			}
		}
		finally {
			this.commandLock.unlock();
		}
		return found;
	}

	/**
	 * Removes all commands that the given plugin registered.
	 *
	 * @param owner the plugin which is having commands removed
	 */
	public void unregisterPluginCommands(Plugin owner) {
		ArrayList<String> pluginCommands = new ArrayList<>();
		this.commandLock.lock();
		try {
			// find all the commands registered to the plugin
			for (PluginCommand c : this.commands) {
				if (c.getOwner().getName().equalsIgnoreCase(owner.getName())) {
					pluginCommands.add(c.getCommand());
				}
			}
			// unload the commands
			for (String s : pluginCommands) {
				this.unregisterCommand(s);
			}
		}
		finally {
			this.commandLock.unlock();
		}

	}

}
