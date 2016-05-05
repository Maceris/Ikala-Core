package com.ikalagaming.packages;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.ikalagaming.event.Event;
import com.ikalagaming.event.EventManager;
import com.ikalagaming.event.Listener;
import com.ikalagaming.localization.Localization;
import com.ikalagaming.logging.Logging;
import com.ikalagaming.packages.events.PackageDisabled;
import com.ikalagaming.packages.events.PackageEnabled;
import com.ikalagaming.packages.events.PackageLoaded;
import com.ikalagaming.packages.events.PackageUnloaded;
import com.ikalagaming.system.SystemPackage;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * Handles loading, unloading and storage of Packages. This is considered a
 * package, but is always enabled and never loaded.
 *
 * @author Ches Burks
 *
 */
public class PackageManager {

	private static PackageManager instance;

	/**
	 * Shuts down the static instance (unregisters commands and unloads
	 * packages)if it exists, and then nullifies the reference to it. This
	 * exists in case you wish to use your own instances of the Package Manager
	 * and not use the single static instance provided. If the instance does not
	 * exist, nothing happens. Note that a new static instance may be created if
	 * the instance is requested later.
	 *
	 * @see #getInstance()
	 * @see #getInstance(EventManager)
	 */
	public static void destoryInstance() {
		if (PackageManager.instance == null) {
			return;
		}
		for (String s : PackageManager.instance.loadedPackages.keySet()) {
			PackageManager.instance.unloadPackage(s);
		}
		PackageManager.instance.clearCommands();
		PackageManager.instance.eventManager = null;
		PackageManager.instance.commands = null;

		PackageManager.instance = null;
	}

	/**
	 * Returns the static instance of the package manager. Since there should
	 * only be one of these, having a static instance is fine and any class can
	 * get the instance which all other classes should share. If there is no
	 * instance yet, one will be created.
	 *
	 * @return the static instance of the Package Manager
	 * @see #getInstance(EventManager)
	 * @see PackageManager#destoryInstance()
	 */
	public static PackageManager getInstance() {
		if (PackageManager.instance == null) {
			PackageManager.instance =
					new PackageManager(EventManager.getInstance());
		}
		return PackageManager.instance;
	}

	/**
	 * Returns the static instance of the package manager. Since there should
	 * only be one of these, having a static instance is fine and any class can
	 * get the instance which all other classes should share. If there is no
	 * instance yet, one will be created.
	 *
	 * @param manager the event manager to use for the static instance
	 *
	 * @return the static instance of the Package Manager
	 * @see #getInstance()
	 * @see #destoryInstance()
	 */
	public static PackageManager getInstance(EventManager manager) {
		if (PackageManager.instance == null) {
			PackageManager.instance = new PackageManager(manager);
		}
		return PackageManager.instance;
	}

	private EventManager eventManager;

	/** maps strings to packages loaded in memory */
	private HashMap<String, Package> loadedPackages;

	private Map<Package, PackageState> packageStates;

	private ResourceBundle resourceBundle;

	/**
	 * If packages should be enabled by the package manager when they are
	 * loaded. If this is false then packages must be enabled manually after
	 * they are loaded.
	 */
	private boolean enableOnLoad;

	/**
	 * A list of all of the commands registered. This list is sorted.
	 */
	private ArrayList<PackageCommand> commands;

	private SystemPackage sysPackage;

	/**
	 * Constructs a new {@link PackageManager} and initializes variables.
	 *
	 * @param evtManager The event manager to use for the package system
	 */
	public PackageManager(EventManager evtManager) {
		this.enableOnLoad = false;
		this.eventManager = evtManager;
		this.loadedPackages = new HashMap<>();
		// Ensure package states are synchronized
		this.packageStates = Collections.synchronizedMap(new HashMap<>());
		this.resourceBundle =
				ResourceBundle.getBundle(
						"com.ikalagaming.packages.resources.PackageManager",
						Localization.getLocale());

		this.commands = new ArrayList<>();

		Logging.create();
		this.sysPackage = new SystemPackage();
		this.loadPackage(this.sysPackage);

		this.registerCommands();
	}

	/**
	 * Unregisters all commands
	 */
	public void clearCommands() {
		for (PackageCommand s : this.commands) {
			this.unregisterCommand(s.getCommand());
		}
		this.commands.clear();
	}

	/**
	 * Returns true if the array contains the given string.
	 *
	 * @param s the string to look for
	 * @return true if the string exists
	 */
	public boolean containsCommand(String s) {
		int i;
		for (i = 0; i < this.commands.size(); ++i) {
			if (this.commands.get(i).getCommand().equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Deactivates the package and halts all of its operations. The package is
	 * still loaded in memory but not active. Calls {@link Package#onDisable()}.
	 * This changes the packages state to {@link PackageState#DISABLING
	 * DISABLING}. The package state is changed to {@link PackageState#DISABLED
	 * DISABLED} after completion. If {@link Package#onDisable()} returns false
	 * (failed), the package state is set to {@link PackageState#CORRUPTED
	 * CORRUPTED}.
	 *
	 * @param target the package to disable
	 *
	 * @return true if the package has been successfully disabled, false if
	 *         there was a problem
	 */
	public boolean disable(Package target) {
		this.setPackageState(target, PackageState.DISABLING);
		String msgDisabling =
				SafeResourceLoader.getString("ALERT_DISABLING",
						this.resourceBundle, "Disabling package $PACKAGE...")
						.replaceFirst("\\$PACKAGE", target.getName());
		Logging.fine(SystemPackage.PACKAGE_NAME, msgDisabling);

		boolean success = target.onDisable();
		if (success) {
			this.setPackageState(target, PackageState.DISABLED);
			PackageDisabled packDisabled = new PackageDisabled(target);
			this.fireEvent(packDisabled);
		}
		else {
			this.setPackageState(target, PackageState.CORRUPTED);
			// TODO note the error
		}
		return success;
	}

	/**
	 * Deactivates the package and halts all of its operations. The package is
	 * still loaded in memory but not active. Calls {@link Package#onDisable()}.
	 * This changes the packages state to {@link PackageState#DISABLING
	 * DISABLING}. The package state is changed to {@link PackageState#DISABLED
	 * DISABLED} after completion. If {@link Package#onDisable()} returns false
	 * (failed), the package state is set to {@link PackageState#CORRUPTED
	 * CORRUPTED}.
	 *
	 * @param target the name of the package to disable
	 *
	 * @return true if the package has been successfully disabled, false if
	 *         there was a problem
	 */
	public boolean disable(String target) {
		Package p = this.getPackage(target);
		if (p == null) {
			return true;
		}
		return this.disable(p);
	}

	/**
	 * Activates the package and enables it to perform its normal functions.
	 * Calls {@link Package#onEnable()}. This changes the package state to
	 * {@link PackageState#ENABLING ENABLING}. The package state is changed to
	 * {@link PackageState#ENABLED ENABLED} after completion. If
	 * {@link Package#onEnable()} returns false (failed), the package state is
	 * set to {@link PackageState#CORRUPTED CORRUPTED}.
	 *
	 * @param target The package to enable
	 *
	 * @return true if the package was successfully enabled, false if there was
	 *         a problem
	 */
	public boolean enable(Package target) {
		this.setPackageState(target, PackageState.ENABLING);
		String msgEnable =
				SafeResourceLoader.getString("ALERT_ENABLING",
						this.resourceBundle, "Enabling package $PACKAGE...")
						.replaceFirst("\\$PACKAGE", target.getName());
		Logging.fine(SystemPackage.PACKAGE_NAME, msgEnable);

		boolean success = target.onEnable();
		if (success) {
			this.setPackageState(target, PackageState.ENABLED);
			PackageEnabled packEnabled = new PackageEnabled(target);
			this.fireEvent(packEnabled);
		}
		else {
			this.setPackageState(target, PackageState.CORRUPTED);
			// TODO note the error
		}

		return success;
	}

	/**
	 * Activates the package and enables it to perform its normal functions.
	 * Calls {@link Package#onEnable()}. This changes the package state to
	 * {@link PackageState#ENABLING ENABLING}. The package state is changed to
	 * {@link PackageState#ENABLED ENABLED} after completion. If
	 * {@link Package#onEnable()} returns false (failed), the package state is
	 * set to {@link PackageState#CORRUPTED CORRUPTED}.
	 *
	 * @param target The name of the package to enable
	 *
	 * @return true if the package was successfully enabled, false if there was
	 *         a problem
	 */
	public boolean enable(String target) {
		Package p = this.getPackage(target);
		if (p == null) {
			return true;
		}
		return this.enable(p);
	}

	/**
	 * Returns true if this package manager automatically enables packages when
	 * they are loaded. If they are not enabled on load, they must be enabled
	 * manually after being loaded.
	 *
	 * @return true if the packages enable after loading, false if they must be
	 *         manually enabled
	 * @see #setEnableOnLoad(boolean)
	 */
	public synchronized boolean enableOnLoad() {
		return this.enableOnLoad;
	}

	/**
	 * Fires an event with a message to a package type from the package manager.
	 * If an error occurs, this will return false. The event should not have
	 * been sent if false was returned.
	 *
	 * @param event the event to fire
	 *
	 * @return true if the event was fired correctly
	 */
	public boolean fireEvent(Event event) {
		if (this.eventManager == null) {
			String err =
					SafeResourceLoader.getString("PACKAGE_NOT_LOADED",
							this.resourceBundle, "Package $PACKAGE not loaded")
							.replaceFirst("\\$PACKAGE", "event-manager");
			System.err.println(err);
			return false;
		}

		if (event != null) {// just in case the assignment failed
			this.eventManager.fireEvent(event);

		}

		return true;
	}

	/**
	 * Returns the package that registered the given string, or null if it
	 * cannot be found.
	 *
	 * @param s the string to look for
	 * @return the owner of the command
	 */
	public Package getCommandParent(String s) {
		int i;
		for (i = 0; i < this.commands.size(); ++i) {
			if (this.commands.get(i).getCommand().equalsIgnoreCase(s)) {
				return this.commands.get(i).getOwner();
			}
		}
		return null;
	}

	/**
	 * Returns a clone of the commands list.
	 *
	 * @return a copy of the stored list
	 */
	public ArrayList<PackageCommand> getCommands() {
		ArrayList<PackageCommand> cmds = new ArrayList<>(this.commands);
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
		for (i = 0; i < this.commands.size(); ++i) {
			if (this.commands.get(i).getCommand().equalsIgnoreCase(s)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the map of package name to package of the currently loaded
	 * packages.
	 *
	 * @return the package map
	 */
	public HashMap<String, Package> getLoadedPackages() {
		return this.loadedPackages;
	}

	/**
	 * If a package of type exists ({@link #isLoaded(String)}), then the package
	 * that is of that type is returned. If no package exists of that type, null
	 * is returned.
	 *
	 * @param type The package type
	 * @return the Package with the given type or null if none exists
	 */
	public Package getPackage(String type) {
		if (this.isLoaded(type)) {
			return this.loadedPackages.get(type);
		}
		return null;
	}

	/**
	 * Returns the {@link PackageState current state} of the package.
	 *
	 * @param target The package to fetch the state of
	 *
	 * @return a PackageState representing the status of this package
	 */
	public PackageState getPackageState(Package target) {
		if (!this.packageStates.containsKey(target)) {
			return PackageState.NOT_LOADED;
		}
		return this.packageStates.get(target);
	}

	/**
	 * Returns the resource bundle for the package manager. This is not safe and
	 * could be null.
	 *
	 * @return the current resource bundle
	 */
	public ResourceBundle getResourceBundle() {
		return this.resourceBundle;
	}

	/**
	 * Returns true if the package is enabled, and false otherwise. A state of
	 * {@link PackageState#ENABLED ENABLED} returns true, any other states will
	 * return false. Packages that are not loaded will return false.
	 *
	 * @param target The package to check for enabled status
	 *
	 * @return true if the package is fully ready to operate
	 */
	public boolean isEnabled(Package target) {
		if (!this.isLoaded(target)) {
			return false;
		}
		PackageState state = this.getPackageState(target);
		if (state.equals(PackageState.ENABLED)) {
			return true;
		}
		return false;

	}

	/**
	 * Returns true if the package is enabled, and false otherwise. A state of
	 * {@link PackageState#ENABLED ENABLED} returns true, any other states will
	 * return false. Packages that are not loaded will return false.
	 *
	 * @param target The name of package to check for enabled status
	 *
	 * @return true if the package is fully ready to operate
	 */
	public boolean isEnabled(String target) {
		Package p = this.getPackage(target);
		if (p == null) {
			return false;
		}
		return this.isEnabled(p);
	}

	/**
	 * Returns true if a package exists that has the same type as the provided
	 * package (for example: "Graphics"). This is the same as calling
	 * <code>{@link #isLoaded(String) isLoaded}({@link Package#getName()})</code>
	 *
	 * @param type the package type
	 * @return true if the package is loaded in memory, false if it does not
	 *         exist
	 */
	public boolean isLoaded(Package type) {
		return this.loadedPackages.containsKey(type.getName());
	}

	/**
	 * Returns true if a package exists with the given type (for example:
	 * "Graphics")'
	 *
	 * @param type the package type
	 * @return true if the package is loaded in memory, false if it does not
	 *         exist
	 */
	public boolean isLoaded(String type) {
		return this.loadedPackages.containsKey(type);
	}

	/**
	 * <p>
	 * Loads the given package into memory, stores it by type, and enables it if
	 * packages are enabled on load by default.
	 * </p>
	 * <p>
	 * If the type of package already exists in the manager, and the new package
	 * has a higher version number, then the old package is unloaded and the new
	 * one is loaded in its place. If the versions are equal, or the new package
	 * is older, then it does not load the new version and returns false.
	 * </p>
	 *
	 * @see PackageManager#enableOnLoad()
	 *
	 * @param toLoad the package to load
	 * @return true if the package was loaded properly, false otherwise
	 */
	public boolean loadPackage(Package toLoad) {
		String loading =
				SafeResourceLoader.getString("ALERT_LOADING",
						this.resourceBundle,
						"Loading package $PACKAGE (v$VERSION)...");
		loading = loading.replaceFirst("\\$PACKAGE", toLoad.getName());
		loading = loading.replaceFirst("\\$VERSION", "" + toLoad.getVersion());
		Logging.fine(SystemPackage.PACKAGE_NAME, loading);
		// if the package exists and is older than toLoad, unload
		if (this.isLoaded(toLoad)) {
			String alreadyLoaded =
					SafeResourceLoader.getString(
							"ALERT_PACKAGE_ALREADY_LOADED",
							this.resourceBundle,
							"Package $PACKAGE is already loaded. (v$VERSION)");
			alreadyLoaded =
					alreadyLoaded.replaceFirst("\\$PACKAGE", toLoad.getName());
			alreadyLoaded =
					alreadyLoaded.replaceFirst("\\$VERSION",
							"" + toLoad.getVersion());
			Logging.fine(SystemPackage.PACKAGE_NAME, alreadyLoaded);
			if (this.loadedPackages.get(toLoad.getName()).getVersion() < toLoad
					.getVersion()) {
				this.unloadPackage(this.loadedPackages.get(toLoad.getName()));
				// unload the old package and continue loading the new one
			}
			else {
				String outdated =
						SafeResourceLoader.getString("ALERT_PACKAGE_OUTDATED",
								this.resourceBundle,
								"Package $PACKAGE (v$VERSION) "
										+ "was outdated. Aborting.");
				outdated =
						outdated.replaceFirst("\\$PACKAGE", toLoad.getName());
				outdated =
						outdated.replaceFirst("\\$VERSION",
								"" + toLoad.getVersion());
				Logging.fine(SystemPackage.PACKAGE_NAME, outdated);
				return false;
			}
		}
		this.setPackageState(toLoad, PackageState.LOADING);

		// store the new package
		this.loadedPackages.put(toLoad.getName(), toLoad);

		for (Listener l : toLoad.getListeners()) {
			this.eventManager.registerEventListeners(l);
		}
		String msg =
				SafeResourceLoader.getString("ALERT_REG_EVENT_LISTENERS",
						this.resourceBundle,
						"Registered event listeners for $PACKAGE")
						.replaceFirst("\\$PACKAGE", toLoad.getName());
		Logging.finer(SystemPackage.PACKAGE_NAME, msg);

		// load it
		toLoad.onLoad();

		this.setPackageState(toLoad, PackageState.DISABLED);
		PackageLoaded packLoaded = new PackageLoaded(toLoad);
		this.fireEvent(packLoaded);

		return true;
	}

	public boolean loadPlugin(PackageInfo info) {
		return false;
	}

	/**
	 * Loads a plugin from a name
	 * 
	 * @param path the path to the folder containing the file
	 * @param name the filename to load from
	 * @return true on success, false if it failed
	 */
	public boolean loadPlugin(String path, String name) {
		// TODO javadoc
		// TODO load a package from file

		// The folder containing the plugin
		File pluginFolder;
		try {
			pluginFolder = new File(path);
		}
		catch (NullPointerException nullExcept) {
			// TODO localize error
			String msg = "Null plugin folder path";
			Logging.warning(SystemPackage.PACKAGE_NAME, msg);
			return false;
		}
		try {
			if (!pluginFolder.exists()) { // TODO localize error
				String msg = "Plugin folder does not exist";
				Logging.warning(SystemPackage.PACKAGE_NAME, msg);
				return false;
			}
			if (!pluginFolder.isDirectory()) { // TODO localize error
				String msg = "Plugin folder is not a folder";
				Logging.warning(SystemPackage.PACKAGE_NAME, msg);
				return false;
			}
			if (!pluginFolder.canRead()) {
				// TODO localize error
				String msg = "Cannot read plugin folder";
				Logging.warning(SystemPackage.PACKAGE_NAME, msg);
				return false;
			}

		}
		catch (SecurityException securExcep) {
			// TODO localize error
			String msg = "System security denied read access to plugin folder";
			Logging.warning(SystemPackage.PACKAGE_NAME, msg);
			return false;
		}

		// directory should be good by here

		File[] files;
		files = pluginFolder.listFiles();
		if (files == null) { // TODO log error
			String msg = "Error reading file names in plugin folder";
			Logging.warning(SystemPackage.PACKAGE_NAME, msg);
			return false;
		}
		if (files.length == 0) {
			// empty
			// TODO localize error
			String msg = "Plugin folder is empty";
			Logging.warning(SystemPackage.PACKAGE_NAME, msg);
			return false;
		}

		File jarFile = null;

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
				if (file.getName().replaceAll("\\.jar\\z", "").equals(name)) {
					jarFile = file;
					break;
				}
			}
			catch (SecurityException secExcep) {
				// Maybe one or more files can't be read
				continue;
			}
		}
		files = null;
		if (jarFile == null) {
			// TODO localize error
			String msg = "Cannot find specified plugin";
			Logging.warning(SystemPackage.PACKAGE_NAME, msg);
			return false;
		}

		// jar file should be valid and correct by here

		JarFile jfile;
		ZipEntry config;
		/*
		 * Check for being a jar file check for package info file load and check
		 * for valid info load the file if necessary
		 */
		try {
			jfile = new JarFile(jarFile);
			config = jfile.getEntry("plugin.yaml");
			if (config == null) {
				// TODO log error
				// invalid plugin
				jfile.close();
				return false;
			}
		}
		catch (IOException e) {
			// TODO log error
			e.printStackTrace();
			return false;
		}
		catch (Exception e) {
			// TODO log error
			e.printStackTrace();
			return false;
		}
		// grab an input stream for the configuration file
		InputStream configIStream;
		try {
			configIStream = jfile.getInputStream(config);
		}
		catch (IOException e1) {
			// TODO log error
			e1.printStackTrace();
			try {
				jfile.close();
			}
			catch (IOException e) {
				// TODO log error
				// wow we really must have messed up
				e.printStackTrace();
			}
			return false;
		}

		// Load in the package info from the config file
		PackageInfo info;
		try {
			info = new PackageInfo(configIStream);
		}
		catch (InvalidDescriptionException e1) {
			// TODO log error
			e1.printStackTrace();
			try {
				jfile.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}

		try {
			jfile.close();// We already loaded in the config info
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}

		ClassLoader loader;
		try {
			loader =
					URLClassLoader.newInstance(new URL[] {jarFile.toURI()
							.toURL()}, this.getClass().getClassLoader());
		}
		catch (MalformedURLException e) {
			// TODO log error
			e.printStackTrace();
			return false;
		}
		Class<?> clazz;
		try {
			clazz = Class.forName(info.getMain(), true, loader);
		}
		catch (ClassNotFoundException e) {
			// TODO log error
			e.printStackTrace();
			return false;
		}

		// TODO finish this
		return false;
	}

	/**
	 * Attempts to register the command for the given class. If the command
	 * already exists, an error is logged and the method returns false.
	 *
	 * @param command the command to register
	 * @param owner what package is registering the command
	 * @return true if the command registered successfully
	 */
	public boolean registerCommand(String command, Package owner) {
		if (this.containsCommand(command)) {
			int index = this.getIndexOfCommand(command);
			String msg =
					SafeResourceLoader
							.getString("COMMAND_ALREADY_REGISTERED",
									this.getResourceBundle(),
									"Command $COMMAND is already registered to $PACKAGE");
			msg = msg.replaceFirst("\\$COMMAND", command);
			msg =
					msg.replaceFirst("\\$PACKAGE", this.commands.get(index)
							.getOwner().getName());
			Logging.warning(SystemPackage.PACKAGE_NAME, msg);
			return false;
		}
		PackageCommand cmd = new PackageCommand(command, owner);
		this.commands.add(cmd);
		String msg =
				SafeResourceLoader.getString("REGISTERED_COMMAND",
						this.getResourceBundle(),
						"Registered command $COMMAND to $PACKAGE");
		msg = msg.replaceFirst("\\$COMMAND", command);
		msg = msg.replaceFirst("\\$PACKAGE", owner.getName());
		Logging.finest(SystemPackage.PACKAGE_NAME, msg);
		java.util.Collections.sort(this.commands);
		return true;
	}

	/**
	 * Registers commands with the registry that the package manager uses
	 */
	private void registerCommands() {
		ArrayList<String> cmds = new ArrayList<>();
		cmds.add("COMMAND_ENABLE");
		cmds.add("COMMAND_DISABLE");
		cmds.add("COMMAND_LOAD");
		cmds.add("COMMAND_UNLOAD");
		cmds.add("COMMAND_LIST_PACKAGES");
		cmds.add("COMMAND_RELOAD");
		cmds.add("COMMAND_HELP");
		cmds.add("COMMAND_VERSION");

		String tmp = "";

		for (String s : cmds) {
			tmp = SafeResourceLoader.getString(s, this.resourceBundle, s);
			this.registerCommand(tmp, this.sysPackage);
		}

	}

	/**
	 * This is essentially restarting the package. The package is disabled if it
	 * is enabled, unloaded, then loaded.
	 *
	 * @param target The package to reload
	 *
	 * @return true if the package reloaded successfully, false otherwise
	 */
	public boolean reload(Package target) {

		if (!this.isLoaded(target)) {
			String tmp =
					SafeResourceLoader.getString("PACKAGE_NOT_LOADED",
							this.getResourceBundle(),
							"Package $PACKAGE not loaded").replaceFirst(
							"\\$PACKAGE", target.getName());
			Logging.warning(SystemPackage.PACKAGE_NAME, tmp);
			return false;
		}
		if (this.isEnabled(target)) {
			if (!this.disable(target)) {
				// disable failed
				this.setPackageState(target, PackageState.CORRUPTED);

				return false;
			}
		}
		this.setPackageState(target, PackageState.UNLOADING);
		this.unloadPackage(target);
		this.setPackageState(target, PackageState.LOADING);
		if (!this.loadPackage(target)) {
			this.setPackageState(target, PackageState.CORRUPTED);
			return false;
		}
		return true;
	}

	/**
	 * Sets the flag for automatically enabling packages when they are loaded.
	 * If they are not enabled on load, they must be enabled manually after
	 * being loaded.
	 * <p>
	 * This allows some customization of how the package system works.
	 *
	 * @param newValue true if the packages should be automatically enabled
	 *            after loading, false if they should be manually enabled
	 */
	public synchronized void setEnableOnLoad(boolean newValue) {
		this.enableOnLoad = newValue;
	}

	private void setPackageState(Package target, PackageState newState) {
		if (!this.packageStates.containsKey(target)) {
			this.packageStates.put(target, newState);
			return;
		}
		// replaces the old state
		PackageState oldState = this.packageStates.get(target);
		if (oldState.equals(PackageState.PENDING_REMOVAL)) {
			return;// You can't change the state at this point
		}
		this.packageStates.put(target, newState);
	}

	/**
	 * Attempts to unload the package from memory. Does nothing if the package
	 * is not loaded. Packages are disabled before unloading. This calls
	 * {@link #unloadPackage(String)} using the package type.
	 *
	 * @param toUnload The type of package to unload
	 */
	public void unloadPackage(Package toUnload) {
		/*
		 * using a string the packages type to ensure the package that is stored
		 * in this class is modified and not just the package passed to the
		 * method.
		 */
		String type = toUnload.getName();
		if (!this.isLoaded(type)) {
			String notLoaded =
					SafeResourceLoader.getString("PACKAGE_NOT_LOADED",
							this.resourceBundle, "Package $PACKAGE not loaded")
							.replaceFirst("\\$PACKAGE", type);
			Logging.fine(SystemPackage.PACKAGE_NAME, notLoaded);
			return;
		}
		this.unloadPackage(type);
	}

	/**
	 * Attempts to unload the package from memory. If no package exists with the
	 * given name ({@link #isLoaded(String)}), returns false and does nothing.
	 *
	 * @param toUnload The type of package to unload
	 * @return true if the package was unloaded properly
	 */
	public boolean unloadPackage(String toUnload) {
		String unloading =
				SafeResourceLoader.getString("ALERT_UNLOADING",
						this.resourceBundle, "Unloading package $PACKAGE...");
		unloading = unloading.replaceFirst("\\$PACKAGE", toUnload);
		Logging.fine(SystemPackage.PACKAGE_NAME, unloading);
		if (!this.isLoaded(toUnload)) {
			String notLoaded =
					SafeResourceLoader.getString("PACKAGE_NOT_LOADED",
							this.resourceBundle, "Package $PACKAGE not loaded")
							.replaceFirst("\\$PACKAGE", toUnload);
			Logging.fine(SystemPackage.PACKAGE_NAME, notLoaded);
			return false;
		}

		Package pack = this.loadedPackages.get(toUnload);

		// It has to be disabled before unloading.
		if (this.isEnabled(pack)) {
			this.disable(pack);
		}
		this.setPackageState(pack, PackageState.UNLOADING);

		pack.onUnload();
		// TODO will this not become an invalid reference during event sending?
		PackageUnloaded packUnloaded = new PackageUnloaded(pack);
		this.fireEvent(packUnloaded);

		for (Listener l : pack.getListeners()) {
			this.eventManager.unregisterEventListeners(l);
		}
		String unreg =
				SafeResourceLoader.getString("ALERT_UNREG_EVENT_LISTENERS",
						this.resourceBundle,
						"Unregistered event listeners for $PACKAGE")
						.replaceFirst("\\$PACKAGE", SystemPackage.PACKAGE_NAME);
		Logging.finer(SystemPackage.PACKAGE_NAME, unreg);
		this.loadedPackages.remove(toUnload);
		this.packageStates.remove(pack);

		String unloaded =
				SafeResourceLoader.getString("ALERT_UNLOADED",
						this.resourceBundle, "Package $PACKAGE unloaded!");
		unloaded = unloaded.replaceFirst("\\$PACKAGE", toUnload);
		Logging.fine(SystemPackage.PACKAGE_NAME, unloaded);
		return true;
	}

	/**
	 * Unregisters the given command.
	 *
	 * @param command the command to remove
	 * @return true if the command was removed
	 */
	public boolean unregisterCommand(String command) {
		if (this.containsCommand(command)) {
			while (this.containsCommand(command)) {// just in case there are
													// multiple
				int index = this.getIndexOfCommand(command);
				this.commands.remove(index);
			}
			String msg =
					SafeResourceLoader.getString("UNREGISTERED_COMMAND",
							this.getResourceBundle(),
							"Unregistered command $COMMAND");
			msg = msg.replaceFirst("\\$COMMAND", command);
			Logging.finest(SystemPackage.PACKAGE_NAME, msg);
			return true;
		}
		return false;
	}

	/**
	 * Removes all commands that the given package registered.
	 *
	 * @param owner the package which is having commands removed
	 */
	public void unregisterPackageCommands(Package owner) {
		ArrayList<String> packageCommands = new ArrayList<>();
		// find all the commands registered to the package
		for (PackageCommand c : this.commands) {
			if (c.getOwner().getName().equalsIgnoreCase(owner.getName())) {
				packageCommands.add(c.getCommand());
			}
		}
		// unload the commands
		for (String s : packageCommands) {
			this.unregisterCommand(s);
		}

	}

}
