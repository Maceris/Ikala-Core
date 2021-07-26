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
import com.ikalagaming.util.SafeResourceLoader;

import com.github.zafarkhaja.semver.Version;
import lombok.CustomLog;
import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * Handles loading, unloading and storage of plugins. This is considered a
 * plugin, but is always enabled and never loaded.
 *
 * @author Ches Burks
 *
 */
@CustomLog(topic = PluginManager.PLUGIN_NAME)
public class PluginManager {

	/**
	 * The name of the core system, since it is not technically a plugin.
	 */
	public static final String PLUGIN_NAME = "Ikala-Core";

	private static PluginManager instance;

	/**
	 * Compare the order of version strings, as if using
	 * first.compareTo(second). This should be string semantic versions and can
	 * handle build info and the like but build metadata is ignored.
	 *
	 * For example, compareVersions("1.0.0-rc.1+build.1",
	 * "1.3.7+build.2.b8f12d7") would return a number less than 0.
	 * compareVersions("2.1.3", "1.0.1") would return a number greater than 0.
	 * compareVersions("1.0.0+build.1", "1.0.0+build.1") would return 0 because
	 * they are equal builds.
	 *
	 * @param first The first version string
	 * @param second The second version string
	 * @return The value of comparing the two versions.
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
		PluginManager.instance.shutdown();
		PluginManager.instance = null;
	}

	/**
	 * Returns the static instance of the plugin manager. Since there should
	 * only be one of these, having a static instance is fine and any class can
	 * get the instance which all other classes should share. If there is no
	 * instance yet, one will be created.
	 *
	 * @return The static instance of the Plugin Manager.
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
	 * @return The static instance of the Plugin Manager.
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
	 * @return True if the first version is newer than the second, otherwise
	 *         false.
	 */
	public final static boolean isNewerVersion(final String toCheck,
		final String existing) {
		return Version.valueOf(toCheck).greaterThan(Version.valueOf(existing));
	}

	private EventManager eventManager;

	/**
	 * Maps strings to info about plugins loaded in memory
	 */
	private Map<String, PluginDetails> pluginDetails;

	/**
	 * Lock object for plugin related activities
	 */
	private Object pluginLock = new Object();

	/**
	 * Stores all the classes loaded by plugins. Keys are the unique class
	 * names.
	 */
	private final Map<String, Class<?>> pluginClassCache;

	/**
	 * The current resource bundle for the plugin manager.
	 * 
	 * @return The current resource bundle, which may be null.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private ResourceBundle resourceBundle;

	/**
	 * If plugins should be enabled by the plugin manager when they are loaded.
	 * If this is false then plugins must be enabled manually after they are
	 * loaded.
	 */
	private boolean enableOnLoad;

	/**
	 * A list of all of the commands registered. This list is sorted.
	 */
	private ArrayList<PluginCommand> commands;

	private Object commandLock = new Object();

	private MiscLoggingListener logListener;

	private final String PLUGIN_CONFIG_FILENAME = "plugin.yml";

	/**
	 * Constructs a new {@link PluginManager} and initializes variables.
	 *
	 * @param evtManager The event manager to use for the plugin system
	 */
	public PluginManager(EventManager evtManager) {
		this.enableOnLoad = false;
		this.eventManager = evtManager;
		this.pluginDetails = Collections.synchronizedMap(new HashMap<>());
		this.pluginClassCache = Collections.synchronizedMap(new HashMap<>());
		this.resourceBundle = ResourceBundle.getBundle(
			"com.ikalagaming.plugins.resources.PluginManager",
			Localization.getLocale());
		this.logListener = new MiscLoggingListener();

		this.commands = new ArrayList<>();

		Logging.create();

		this.registerCommands();
		this.eventManager.registerEventListeners(logListener);
	}

	private void alertMissingArgs() {
		String tmp = SafeResourceLoader.getString("COMMAND_ARG_MISSING",
			this.getResourceBundle());
		log.warning(tmp);
	}

	private void cbDisable(String[] args) {
		if (args.length < 1) {
			alertMissingArgs();
			return;
		}
		disable(args[0]);
	}

	private void cbEnable(String[] args) {
		if (args.length < 1) {
			alertMissingArgs();
			return;
		}
		enable(args[0]);
	}

	private void cbLoad(String[] args) {
		if (args.length < 1) {
			alertMissingArgs();
			return;
		}

		String tmp =
			SafeResourceLoader.getString("WIP_TEXT", this.getResourceBundle());
		log.warning(tmp);
		// TODO loading
		// loadPlugin(System.getProperty("user.dir"), args[0]);
	}

	private void cbReload(String[] args) {
		if (args.length < 1) {
			alertMissingArgs();
			return;
		}
		reload(args[0]);
	}

	private void cbUnload(String[] args) {
		if (args.length < 1) {
			alertMissingArgs();
			return;
		}
		unloadPlugin(args[0]);
	}

	/**
	 * Unregisters all commands
	 */
	@Synchronized("commandLock")
	public void clearCommands() {
		ArrayList<String> cmds = new ArrayList<>();
		for (PluginCommand s : this.commands) {
			cmds.add(s.getCommand());
		}
		for (String s : cmds) {
			this.unregisterCommand(s);
		}
		this.commands.clear();
	}

	/**
	 * Returns true if the array contains the given string.
	 *
	 * @param s The string to look for.
	 * @return True if the string exists.
	 */
	@Synchronized("commandLock")
	public boolean containsCommand(final String s) {
		int i;
		boolean res = false;

		for (i = 0; i < this.commands.size(); ++i) {
			if (this.commands.get(i).getCommand().equalsIgnoreCase(s)) {
				res = true;
				break;
			}
		}
		return res;
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
	@Synchronized("pluginLock")
	public boolean disable(final String target) {
		this.setPluginState(target, PluginState.DISABLING);

		logAlert("ALERT_DISABLING", target);

		PluginDetails details = this.pluginDetails.get(target);
		if (null == details) {
			logAlert("PLUGIN_DETAILS_MISSING", target);
			return false;
		}

		boolean success = details.getPlugin().onDisable();
		if (success) {
			this.setPluginState(target, PluginState.DISABLED);
			this.fireEvent(new PluginDisabled(target));
			logAlert("ALERT_DISABLED", target);
		}
		else {
			this.setPluginState(target, PluginState.CORRUPTED);
			logStateCorrupted(target);
			logAlert("PLUGIN_DISABLE_FAIL", target);
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
	@Synchronized("pluginLock")
	public boolean enable(final String target) {
		this.setPluginState(target, PluginState.ENABLING);

		logAlert("ALERT_ENABLING", target);

		PluginDetails details = this.pluginDetails.get(target);
		if (null == details) {
			logAlert("PLUGIN_DETAILS_MISSING", target);
			return false;
		}

		boolean success = details.getPlugin().onEnable();
		if (success) {
			this.setPluginState(target, PluginState.ENABLED);
			this.fireEvent(new PluginEnabled(target));
			logAlert("ALERT_ENABLED", target);
		}
		else {
			this.setPluginState(target, PluginState.CORRUPTED);
			logStateCorrupted(target);
			logAlert("PLUGIN_ENABLE_FAIL", target);
		}

		return success;
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
	public boolean getEnableOnLoad() {
		return this.enableOnLoad;
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
				log.warning(msg);
				jfile.close();
				return Optional.empty();
			}
		}
		catch (Exception e) {
			String msg = SafeResourceLoader
				.getString("PLUGIN_JAR_ERROR", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", fileName);
			log.warning(msg);
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
			log.warning(msg);
			try {
				jfile.close();
			}
			catch (IOException e) {
				String errorMsg = SafeResourceLoader
					.getString("PLUGIN_JAR_CLOSE_ERROR", this.resourceBundle)
					.replaceFirst("\\$PLUGIN", fileName);
				log.warning(errorMsg);
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
			log.warning(msg);
			log.warning(e1.getMessage());
			try {
				jfile.close();
			}
			catch (IOException e) {
				String errorMsg = SafeResourceLoader
					.getString("PLUGIN_JAR_CLOSE_ERROR", this.resourceBundle)
					.replaceFirst("\\$PLUGIN", fileName);
				log.warning(errorMsg);
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
			log.warning(errorMsg);
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
	private boolean fireEvent(@NonNull Event event) {
		if (this.eventManager == null) {
			String err = SafeResourceLoader
				.getString("PLUGIN_NOT_LOADED", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", PluginManager.PLUGIN_NAME);
			System.err.println(err);
			return false;
		}

		this.eventManager.fireEvent(event);

		return true;
	}

	/**
	 * Returns a clone of the commands list.
	 *
	 * @return a copy of the stored list
	 */
	@Synchronized("commandLock")
	public ArrayList<PluginCommand> getCommands() {
		ArrayList<PluginCommand> cmds = new ArrayList<>();

		for (PluginCommand pc : this.commands) {
			cmds.add(pc);
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
	@Synchronized("commandLock")
	private int getIndexOfCommand(String s) {
		int i;
		boolean found = false;

		for (i = 0; i < this.commands.size(); ++i) {
			if (this.commands.get(i).getCommand().equalsIgnoreCase(s)) {
				found = true;
				break;
			}
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
	 * @param target The name of the plugin to get info from.
	 * @return The info for the specified plugin.
	 */
	@Synchronized("pluginLock")
	public Optional<PluginInfo> getInfo(String target) {
		PluginDetails details = this.pluginDetails.get(target);
		if (details == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(details.getInfo());
	}

	/**
	 * Returns the map of plugin name to Plugin of the currently loaded plugins.
	 *
	 * @return the plugin map
	 */
	@Synchronized("pluginLock")
	public HashMap<String, Plugin> getLoadedPlugins() {
		HashMap<String, Plugin> ret = new HashMap<>();
		pluginDetails.forEach((key, value) -> {
			ret.put(key, value.getPlugin());
		});
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
	@Synchronized("pluginLock")
	public Optional<Plugin> getPlugin(final String type) {
		PluginDetails details = this.pluginDetails.get(type);
		if (details == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(details.getPlugin());
	}

	/**
	 * Returns the {@link PluginState current state} of the plugin.
	 *
	 * @param target The name of the plugin to fetch the state of
	 *
	 * @return a PluginState representing the status of this plugin
	 */
	@Synchronized("pluginLock")
	public PluginState getPluginState(String target) {
		if (null == target) {
			return PluginState.NOT_LOADED;
		}

		if (!this.pluginDetails.containsKey(target)) {
			return PluginState.NOT_LOADED;
		}
		return this.pluginDetails.get(target).getState();
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
	@Synchronized("pluginLock")
	public boolean isEnabled(final String target) {
		if (target == null) {
			return false;
		}
		if (!this.isLoaded(target)) {
			return false;
		}
		PluginState state = this.getPluginState(target);
		if (PluginState.ENABLED.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if a plugin exists with the given type (for example:
	 * "Graphics")'. Most existing states count as loaded, but the following do
	 * not:
	 * <ul>
	 * <li>{@link PluginState#UNLOADING}</li>
	 * <li>{@link PluginState#PENDING_REMOVAL}</li>
	 * <li>{@link PluginState#NOT_LOADED}</li>
	 * </ul>
	 *
	 * @param type The plugin name
	 * @return true If the plugin is loaded in memory, and not actively being
	 *         unloaded/removed
	 */
	@Synchronized("pluginLock")
	public boolean isLoaded(final String type) {
		if (type == null) {
			return false;
		}
		PluginDetails details = this.pluginDetails.get(type);
		if (details == null) {
			return false;
		}
		switch (details.getState()) {
			case CORRUPTED:
			case DEPS_CHECKING:
			case DEPS_MISSING:
			case DEPS_SATISFIED:
			case DISABLED:
			case DISABLING:
			case DISCOVERED:
			case ENABLED:
			case ENABLING:
			case LOADING:
				return true;
			default:
			case UNLOADING:
			case PENDING_REMOVAL:
			case NOT_LOADED:
				return false;
		}
	}

	/**
	 * Find the class by class name. Checks for cached versions, but will search
	 * through known plugin class loaders to find it. If it cannot be found,
	 * returns null.
	 * 
	 * @param name The name of the class to look for.
	 * @return The class by the given name, or null if not found.
	 */
	Class<?> getClassByName(final String name) {
		Class<?> cachedClass = pluginClassCache.get(name);

		if (cachedClass != null) {
			return cachedClass;
		}
		for (String pluginName : this.pluginDetails.keySet()) {
			PluginClassLoader loader =
				this.pluginDetails.get(pluginName).getClassLoader();
			try {
				cachedClass = loader.findClass(name, false);
			}
			catch (ClassNotFoundException e) {
			}
			if (cachedClass != null) {
				return cachedClass;
			}
		}
		return null;
	}

	/**
	 * Stores the class by name in the cached class list.
	 * 
	 * @param name The name of the class.
	 * @param clazz The corresponding class object.
	 */
	void setClass(final String name, final Class<?> clazz) {
		if (!pluginClassCache.containsKey(name)) {
			pluginClassCache.put(name, clazz);
		}
	}

	/**
	 * Removes the class by name in the cached class list.
	 * 
	 * @param name The name of the class.
	 */
	void removeClass(final String name) {
		pluginClassCache.remove(name);
	}

	/**
	 * Calculate the state of a plugin based on whether the dependencies have
	 * been satisfied. Does not calculate the state of the children, multiple
	 * passes may be required to determine the final state. This is used during
	 * the process of loading multiple plugins at once and determining if
	 * dependencies are satisfied.
	 * 
	 * @param pluginInfo The plugin information we are checking.
	 * @return The state of the plugin in context of loading multiple plugins.
	 */
	private PluginState calculatePluginState(PluginInfo pluginInfo) {
		if (null == pluginInfo) {
			return PluginState.NOT_LOADED;
		}
		/*
		 * If all dependencies are satisfied (DISCOVERED, loaded/enabled,
		 * DEPS_SATISFIED, generally existing in the system), or it has no
		 * dependencies, mark it as DEPS_SATISFIED as well. If a plugin has
		 * unsatisfied dependencies, such as something NOT_LOADED, mark the
		 * plugin as DEPS_MISSING. If a plugin does not have missing
		 * dependencies, but it has dependencies that are also DEPS_CHECKING,
		 * mark it as DEPS_CHECKING.
		 */
		boolean stillEvaluatingChildren = false;
		for (String dependencyName : pluginInfo.getDependencies()) {
			PluginState state = getPluginState(dependencyName);
			switch (state) {
				case DEPS_CHECKING:
					/*
					 * Not yet confirmed until children nodes are validated, so
					 * set the flag and keep checking children.
					 */
					stillEvaluatingChildren = true;
					break;
				case DEPS_SATISFIED:
				case DISABLED:
				case DISABLING:
				case DISCOVERED:
				case ENABLED:
				case ENABLING:
				case LOADING:
					// satisfied, we can keep going
					break;
				case DEPS_MISSING:
					// propagate the failure up
				case CORRUPTED:
				case NOT_LOADED:
				case PENDING_REMOVAL:
				case UNLOADING:
					/*
					 * Not satisfied or won't be, impossible to load so we just
					 * bail out of the method immediately
					 */
					return PluginState.DEPS_MISSING;
				default:
					break;
			}
		}
		/*
		 * At this point no child was missing dependencies or invalid, so if all
		 * were good we can continue, but if any were still needing to be
		 * checked themselves, we need to note that.
		 */
		if (stillEvaluatingChildren) {
			return PluginState.DEPS_CHECKING;
		}
		return PluginState.DEPS_SATISFIED;
	}

	/**
	 * Find all the names of plugins that have a given state.
	 * 
	 * @param state The state to look for.
	 * @return The names of plugins with that given state.
	 */
	private List<String> findPluginsByState(PluginState state) {
		return this.pluginDetails.keySet().stream().filter((name) -> {
			return state == this.pluginDetails.get(name).getState();
		}).collect(Collectors.toList());
	}

	/**
	 * Resolve the dependencies of all children using a breadth-first search.
	 * Returns true if everything is fine, but false if there is an unresolved
	 * dependency. This return value is used to propagate failures up the tree.
	 * This will set the state of any plugin it reaches which is still checking.
	 * 
	 * @param root The root node we are building a tree from.
	 */
	private void resolveDependencies(PluginDependencyNode root) {
		if (null == root) {
			return;
		}
		List<String> namesInTheTree = new ArrayList<>();
		namesInTheTree.add(root.getName());
		ArrayDeque<PluginDependencyNode> queue = new ArrayDeque<>();

		PluginDependencyNode currentNode;
		queue.add(root);

		while (!queue.isEmpty()) {
			currentNode = queue.pollFirst();

			PluginInfo info =
				this.pluginDetails.get(currentNode.getName()).getInfo();
			for (String dependencyName : info.getDependencies()) {
				PluginState state = getPluginState(dependencyName);
				switch (state) {
					case DEPS_CHECKING:
						if (!namesInTheTree.contains(dependencyName)) {
							namesInTheTree.add(dependencyName);
							PluginDependencyNode child =
								new PluginDependencyNode(dependencyName);
							child.setParent(root);
							currentNode.getChildren().add(child);
							queue.add(child);
						}
						break;
					case DEPS_SATISFIED:
					case DISABLED:
					case DISABLING:
					case DISCOVERED:
					case ENABLED:
					case ENABLING:
					case LOADING:
					default:
						// satisfied, we don't need to do anything here
						break;
					case DEPS_MISSING:
					case CORRUPTED:
					case NOT_LOADED:
					case PENDING_REMOVAL:
					case UNLOADING:
						// propagate failure up to the root and bail
						setPluginState(currentNode.getName(),
							PluginState.DEPS_MISSING);
						PluginDependencyNode parent = currentNode.getParent();
						while (null != parent) {
							setPluginState(parent.getName(),
								PluginState.DEPS_MISSING);
							parent = parent.getParent();
						}
						return;
				}
			}
		}

		/*
		 * We went through the whole tree and never failed. Therefore we can go
		 * mark everything as satisfied. Also we don't get to this point until
		 * the queue is empty.
		 */
		queue.add(root);
		while (!queue.isEmpty()) {
			currentNode = queue.pollFirst();
			setPluginState(currentNode.getName(), PluginState.DEPS_SATISFIED);
			// fine since we avoided cycles when setting up children earlier
			queue.addAll(currentNode.getChildren());
		}
	}

	/**
	 * Attempt to load all the provided jars as plugins.
	 * 
	 * @param jars The jars we want to load.
	 */
	private void plLoadPlugins(@NonNull List<File> jars) {
		Map<File, PluginInfo> jarInfoMap = new HashMap<>();

		// grab all the plugin info from them, discard invalid plugins
		for (File jarFile : jars) {
			Optional<PluginInfo> info = extractPluginInfo(jarFile);
			if (!info.isPresent()) {
				/*
				 * We don't have a valid plugin, the error was already logged
				 * when extracting plugin info
				 */
				continue;
			}
			jarInfoMap.put(jarFile, info.get());
		}
		// Unload/dereference any files that we don't care about anymore
		jars.removeIf((file) -> {
			return !jarInfoMap.containsKey(file);
		});

		// Plugins that were already had a newer version loaded
		List<File> skipped = new ArrayList<>();
		
		// load the plugins into memory with plugin info, DISCOVERED status
		for (File jarFile : jarInfoMap.keySet()) {
			PluginInfo info = jarInfoMap.get(jarFile);

			String pluginName = info.getName();
			if (this.isLoaded(pluginName)) {

				logAlert("ALERT_PLUGIN_ALREADY_LOADED", pluginName);

				boolean lowerVersion = isNewerVersion(info.getVersion(),
					this.pluginDetails.get(pluginName).getInfo().getVersion());

				if (lowerVersion) {
					this.unloadPlugin(pluginName);
					// unload the old plugin and continue loading the new one
				}
				else {
					logAlert("ALERT_PLUGIN_OUTDATED", pluginName);
					skipped.add(jarFile);
					continue;
				}
			}

			PluginClassLoader loader = null;
			try {
				loader = new PluginClassLoader(this,
					getClass().getClassLoader(), info, jarFile);
			}
			catch (MalformedURLException e) {
				logAlert("PLUGIN_URL_INVALID", jarFile.getName());
			}
			catch (InvalidPluginException e) {
				// The class loader already logs the problem
				continue;
			}
			if (loader == null) {
				jarInfoMap.remove(jarFile);
				jars.remove(jarFile);
				continue;
			}

			PluginDetails details = new PluginDetails(loader, info,
				loader.plugin, PluginState.DISCOVERED);

			this.pluginDetails.put(info.getName(), details);
			logAlert("ALERT_DISCOVERED", info.getName());
		}

		jars.removeAll(skipped);

		/*
		 * Resolve easy dependencies. Loop through all plugins once, if all
		 * dependencies are satisfied (DISCOVERED, loaded/enabled,
		 * DEPS_SATISFIED, generally existing in the system), or it has no
		 * dependencies, mark it as DEPS_SATISFIED as well. If a plugin has
		 * unsatisfied dependencies, such as something NOT_LOADED, mark the
		 * plugin as DEPS_MISSING. If a plugin does not have missing
		 * dependencies, but it has dependencies that are also DEPS_CHECKING,
		 * mark it as DEPS_CHECKING.
		 */
		for (String pluginName : this.pluginDetails.keySet()) {
			PluginDetails details = this.pluginDetails.get(pluginName);
			PluginState state = calculatePluginState(details.getInfo());
			details.setState(state);
		}

		/*
		 * Handle loops/clusters with BFS. Go through all nodes still
		 * DEPS_CHECKING and: if all dependencies are DEPS_SATISFIED, the node
		 * is DEPS_SATISFIED. If there are dependencies not found, or
		 * DEPS_MISSING, the node and all parent nodes are set to DEPS_MISSING
		 * and we move to a new node. If a child node is DEPS_CHECKING, and not
		 * already in the tree, we tack it on and keep navigating down. Once we
		 * completely exhaust reachable nodes, and everything is DEPS_SATISFIED
		 * or DEPS_CHECKING, we can mark the whole tree as DEPS_SATISFIED.
		 */

		List<String> stillChecking =
			findPluginsByState(PluginState.DEPS_CHECKING);
		while (stillChecking.size() != 0) {
			String name = stillChecking.get(0);
			resolveDependencies(new PluginDependencyNode(name));
			stillChecking = findPluginsByState(PluginState.DEPS_CHECKING);
		}

		// Report and unload all DEPS_MISSING plugins

		for (String pluginName : findPluginsByState(PluginState.DEPS_MISSING)) {
			logAlert("PLUGIN_DEPENDENCY_MISSING", pluginName);
			PluginDetails details = pluginDetails.remove(pluginName);
			details.dispose();
		}

		/*
		 * All plugins should now be DEPS_SATISFIED, so load them all. During
		 * the onLoad() method, plugins should deal with connecting to plugins
		 * that may be in a dependency loop.
		 */
		List<String> toLoad = findPluginsByState(PluginState.DEPS_SATISFIED);
		for (String pluginName : toLoad) {
			setPluginState(pluginName, PluginState.LOADING);
			logAlert("ALERT_LOADING", pluginName);

			PluginDetails details = this.pluginDetails.get(pluginName);
			Plugin plugin = details.getPlugin();

			if (!plugin.onLoad()) {
				logAlert("PLUGIN_LOAD_FAIL", pluginName);
				unloadPlugin(pluginName);
			}
			for (Listener l : plugin.getListeners()) {
				this.eventManager.registerEventListeners(l);
			}
			String msg = SafeResourceLoader
				.getString("ALERT_REG_EVENT_LISTENERS", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", pluginName);
			log.finer(msg);
			setPluginState(pluginName, PluginState.DISABLED);

			logAlert("ALERT_LOADED", pluginName);
			fireEvent(new PluginLoaded(pluginName));
		}

		/*
		 * After all plugins have been loaded, now they can be enabled (if that
		 * configuration is set). At this point problems with loops should have
		 * been resolved enough that the plugins can start in any order.
		 */
		if (enableOnLoad) {
			toLoad.forEach((plugin) -> {
				enable(plugin);
			});
		}
	}

	/**
	 * Load all the plugins from .jar files that are located in the given
	 * folder.
	 * 
	 * @param folder The folder that contains all the jar files we want to load.
	 */
	@Synchronized("pluginLock")
	public void loadAllPlugins(String folder) {

		File pluginFolder = null;
		Optional<File> folderMaybe = this.plGetFolder(folder);
		if (!folderMaybe.isPresent()) {
			String warning = SafeResourceLoader
				.getString("PLUGIN_FOLDER_NOT_FOUND", this.resourceBundle)
				.replaceFirst("\\$FOLDER", "folder");
			log.warning(warning);
			return;
		}
		pluginFolder = folderMaybe.get();

		// find all the jars
		ArrayList<File> jars = this.plGetAllJars(pluginFolder);

		plLoadPlugins(jars);
	}

	/**
	 * Loads a plugin by name from a folder.
	 *
	 * @param path the path to the folder containing the file
	 * @param name the filename to load from, without a file extension
	 * @return true on success, false if it failed
	 */
	@Deprecated
	@Synchronized("pluginLock")
	public boolean loadPlugin(String path, String name) {
		Optional<File> folderMaybe = this.plGetFolder(path);
		if (!folderMaybe.isPresent()) {
			return false;
		}
		File pluginFolder = folderMaybe.get();

		ArrayList<File> jars = this.plGetAllJars(pluginFolder);

		Optional<File> jarMaybe = this.plPickJarByName(jars, name);
		if (!jarMaybe.isPresent()) {
			return false;
		}
		File jarFile = jarMaybe.get();

		plLoadPlugins(Collections.singletonList(jarFile));
		return true;
	}

	/**
	 * Log an alert about a plugin lifecycle, where plugin name and version are
	 * automatically replaced.
	 * 
	 * @param whichAlert The string to read from the resource bundle
	 * @param pluginName The plugin that the alert is about
	 */
	void logAlert(String whichAlert, String pluginName) {
		PluginDetails details = this.pluginDetails.get(pluginName);
		String version;
		if (details == null || details.getInfo() == null) {
			version = "?";
		}
		else {
			version = details.getInfo().getVersion();
		}
		String message =
			SafeResourceLoader.getString(whichAlert, getResourceBundle())
				.replaceFirst("\\$PLUGIN", pluginName)
				.replaceFirst("\\$VERSION", version);
		log.fine(message);
	}

	/**
	 * Log the fact that a plugin had its state corrupted.
	 * 
	 * @param plugin The plugin that was corrupted.
	 */
	void logStateCorrupted(String plugin) {
		String msgCorrupted = SafeResourceLoader
			.getString("PLUGIN_STATE_CORRUPTED", this.resourceBundle)
			.replaceFirst("\\$PLUIGN", plugin);
		log.warning(msgCorrupted);
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
			log.warning(msg);
			return jars;
		}
		if (files.length == 0) {
			String msg = SafeResourceLoader
				.getString("PLUGIN_FOLDER_EMPTY", this.resourceBundle)
				.replaceFirst("\\$FOLDER", folder.getAbsolutePath());
			log.warning(msg);
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
				log.fine(msg);
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
			log.warning(msg);
			return Optional.empty();
		}
		try {
			if (!pluginFolder.exists()) {
				String msg = SafeResourceLoader
					.getString("PLUGIN_FOLDER_NOT_FOUND", this.resourceBundle)
					.replaceFirst("\\$FOLDER", pluginFolder.getAbsolutePath());
				log.warning(msg);
				return Optional.empty();
			}
			if (!pluginFolder.isDirectory()) {
				String msg = SafeResourceLoader
					.getString("PLUGIN_FOLDER_NOT_FOLDER", this.resourceBundle)
					.replaceFirst("\\$FOLDER", pluginFolder.getAbsolutePath());
				log.warning(msg);
				return Optional.empty();
			}
			if (!pluginFolder.canRead()) {
				String msg = SafeResourceLoader
					.getString("PLUGIN_FOLDER_UNREADABLE", this.resourceBundle)
					.replaceFirst("\\$FOLDER", pluginFolder.getAbsolutePath());
				log.warning(msg);
				return Optional.empty();
			}
		}
		catch (SecurityException securExcep) {
			String msg = SafeResourceLoader
				.getString("PLUGIN_FOLDER_ACCESS_DENIED", this.resourceBundle)
				.replaceFirst("\\$FOLDER", path);
			log.warning(msg);
			return Optional.empty();
		}
		return Optional.of(pluginFolder);
	}

	/**
	 * Returns an optional that contains the jar file with the specified name,
	 * or an empty optional if none was found in the list of files. The file
	 * name is without an extension so "example.jar" should be passed as
	 * "example", and "test-1.0.2.jar" should be "test-1.0.2".
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
				log.fine(msg);
				// Maybe one or more files can't be read
				continue;
			}
		}

		// It hasn't returned by now which means it couldn't find a match
		String msg = SafeResourceLoader
			.getString("PLUGIN_NOT_FOUND", this.resourceBundle)
			.replaceFirst("\\$PLUGIN", name);
		log.warning(msg);
		return Optional.empty();
	}

	/**
	 * Attempts to register the command for the given class. If the command
	 * already exists, an error is logged and the method returns false.
	 * 
	 * @param command the command to register
	 * @param callback The function to call when executing a command. Argument
	 *            list is passed in.
	 * @param owner The owner of the plugin
	 * @return true if the command registered successfully
	 * 
	 */
	@Synchronized("commandLock")
	public boolean registerCommand(final String command,
		Consumer<String[]> callback, String owner) {
		if (this.containsCommand(command)) {
			String msg = SafeResourceLoader
				.getString("COMMAND_ALREADY_REGISTERED",
					this.getResourceBundle())
				.replaceFirst("\\$COMMAND", command);
			log.warning(msg);
			return false;
		}

		PluginCommand cmd = new PluginCommand(command, callback, owner);
		this.commands.add(cmd);

		String msg = SafeResourceLoader
			.getString("REGISTERED_COMMAND", this.getResourceBundle())
			.replaceFirst("\\$COMMAND", command);
		log.finest(msg);

		java.util.Collections.sort(this.commands);

		return true;
	}

	/**
	 * Registers commands with the registry that the plugin manager uses
	 */
	@Synchronized("commandLock")
	private void registerCommands() {
		this.registerCommand(
			SafeResourceLoader.getString("COMMAND_ENABLE", this.resourceBundle),
			this::cbEnable, PluginManager.PLUGIN_NAME);
		this.registerCommand(
			SafeResourceLoader.getString("COMMAND_DISABLE",
				this.resourceBundle),
			this::cbDisable, PluginManager.PLUGIN_NAME);
		this.registerCommand(
			SafeResourceLoader.getString("COMMAND_LOAD", this.resourceBundle),
			this::cbLoad, PluginManager.PLUGIN_NAME);
		this.registerCommand(
			SafeResourceLoader.getString("COMMAND_UNLOAD", this.resourceBundle),
			this::cbUnload, PluginManager.PLUGIN_NAME);
		this.registerCommand(
			SafeResourceLoader.getString("COMMAND_RELOAD", this.resourceBundle),
			this::cbReload, PluginManager.PLUGIN_NAME);
	}

	/**
	 * This is essentially restarting the plugins. The plugin is disabled if it
	 * is enabled, unloaded, then loaded.
	 *
	 * @param target The name of the plugin to reload
	 *
	 * @return true if the plugin reloaded successfully, false otherwise
	 */
	@Synchronized("pluginLock")
	public boolean reload(String target) {
		Optional<PluginInfo> p = getInfo(target);

		if (!p.isPresent()) {
			logAlert("PLUGIN_INFO_MISSING", target);
			return false;
		}
		PluginInfo pInfo = p.get();
		if (!this.isLoaded(target)) {
			String tmp = SafeResourceLoader
				.getString("PLUGIN_NOT_LOADED", this.getResourceBundle())
				.replaceFirst("\\$PLUGIN", pInfo.getName());
			log.warning(tmp);
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
		// if (!this.loadPlugin(target, pInfo)) {
		// this.setPluginState(target, PluginState.CORRUPTED);
		// return false;
		// }
		// TODO load plugin

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
		this.enableOnLoad = newValue;
	}

	@Synchronized("pluginLock")
	private boolean setPluginState(String target, PluginState newState) {

		if (!this.pluginDetails.containsKey(target)) {
			return false;
		}

		// replaces the old state
		PluginState oldState = this.pluginDetails.get(target).getState();
		if (PluginState.PENDING_REMOVAL.equals(oldState)) {
			// can't change plugins pending removal
			return false;
		}
		this.pluginDetails.get(target).setState(newState);
		return true;

	}

	private void shutdown() {
		this.eventManager.unregisterEventListeners(logListener);

		synchronized (pluginLock) {
			List<String> toUnload = new ArrayList<>(pluginDetails.keySet());
			for (String s : toUnload) {
				unloadPlugin(s);
			}
		}
		synchronized (commandLock) {
			clearCommands();
			commands = null;
		}
		eventManager = null;
	}

	/**
	 * Attempts to unload the plugin from memory. If no plugin exists with the
	 * given name ({@link #isLoaded(String)}), returns false and does nothing.
	 *
	 * @param toUnload The type of plugin to unload
	 * @return true if the plugin was unloaded properly
	 */
	@Synchronized("pluginLock")
	public boolean unloadPlugin(final String toUnload) {

		logAlert("ALERT_UNLOADING", toUnload);

		if (!this.isLoaded(toUnload)) {
			logAlert("PLUGIN_NOT_LOADED", toUnload);
			PluginDetails removedDetails = this.pluginDetails.remove(toUnload);
			if (null != removedDetails) {
				removedDetails.dispose();
			}
			return false;
		}

		PluginDetails details = this.pluginDetails.get(toUnload);

		if (null == details) {
			String notLoaded = SafeResourceLoader
				.getString("PLUGIN_LOADED_BUT_NULL", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", toUnload);
			log.warning(notLoaded);
			return false;
		}

		Plugin plugin = details.getPlugin();

		if (null == plugin) {
			String notLoaded = SafeResourceLoader
				.getString("PLUGIN_LOADED_BUT_NULL", this.resourceBundle)
				.replaceFirst("\\$PLUGIN", toUnload);
			log.warning(notLoaded);
			details.dispose();
			this.pluginDetails.remove(toUnload);
			return false;
		}

		// It has to be disabled before unloading.
		if (this.isEnabled(toUnload)) {
			this.disable(toUnload);
		}
		this.setPluginState(toUnload, PluginState.UNLOADING);

		// TODO handle failure
		plugin.onUnload();
		PluginUnloaded packUnloaded = new PluginUnloaded(toUnload);
		this.fireEvent(packUnloaded);

		for (Listener l : plugin.getListeners()) {
			this.eventManager.unregisterEventListeners(l);
		}
		String unreg = SafeResourceLoader.getString("", this.resourceBundle)
			.replaceFirst("\\$PLUGIN", PluginManager.PLUGIN_NAME);
		log.finer(unreg);

		details = this.pluginDetails.remove(toUnload);
		details.dispose();

		logAlert("ALERT_UNLOADED", toUnload);
		return true;
	}

	/**
	 * Unregisters the given command.
	 *
	 * @param command the command to remove
	 * @return true if the command was removed
	 */
	@Synchronized("commandLock")
	public boolean unregisterCommand(final String command) {
		boolean found = false;

		if (this.containsCommand(command)) {

			while (this.containsCommand(command)) {
				// just in case there are multiple
				int index = this.getIndexOfCommand(command);
				this.commands.remove(index);
			}
			String msg = SafeResourceLoader.getString("UNREGISTERED_COMMAND",
				this.getResourceBundle());
			msg = msg.replaceFirst("\\$COMMAND", command);
			log.finest(msg);
			found = true;
		}
		return found;
	}

	/**
	 * Removes all commands that the given plugin registered.
	 *
	 * @param owner the plugin which is having commands removed
	 */
	@Synchronized("commandLock")
	public void unregisterPluginCommands(@NonNull String owner) {
		this.commands.stream().filter((command) -> {
			return owner.equalsIgnoreCase(command.getOwner());
		}).forEach((command) -> {
			this.unregisterCommand(command.getCommand());
		});
	}

}
