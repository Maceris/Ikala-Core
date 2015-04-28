package com.ikalagaming.packages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.ikalagaming.event.Event;
import com.ikalagaming.event.EventManager;
import com.ikalagaming.event.Listener;
import com.ikalagaming.localization.Localization;
import com.ikalagaming.logging.LoggingLevel;
import com.ikalagaming.logging.LoggingPackage;
import com.ikalagaming.logging.events.Log;
import com.ikalagaming.logging.events.LogError;
import com.ikalagaming.packages.events.PackageDisabled;
import com.ikalagaming.packages.events.PackageEnabled;
import com.ikalagaming.packages.events.PackageLoaded;
import com.ikalagaming.packages.events.PackageUnloaded;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * Handles loading, unloading and storage of Packages. This is considered a
 * package, but is always enabled and never loaded.
 *
 * @author Ches Burks
 *
 */
public class PackageManager implements Package {

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
		for (Listener l : PackageManager.instance.listeners) {
			PackageManager.instance.eventManager.unregisterEventListeners(l);
		}
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
	private PMEventListener listener;
	private HashSet<Listener> listeners;
	/** maps strings to packages loaded in memory */
	private HashMap<String, Package> loadedPackages;

	private Map<Package, PackageState> packageStates;

	private String packageName = "package-manager";

	private ResourceBundle resourceBundle;

	private static PackageManager instance;

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
		this.listeners = new HashSet<>();

		this.listener = new PMEventListener(this);
		this.listeners.add(this.listener);
		this.commands = new ArrayList<>();

		this.loadCorePackages();
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
	 * still loaded in memory but not active. Calls {@link #onDisable()}. This
	 * should change the packages state to {@link PackageState#DISABLING
	 * DISABLING}. If the disabling were done here and not in the
	 * {@link #onDisable()} method, the package state should be changed to
	 * {@link PackageState#DISABLED DISABLED} after completion. Otherwise the
	 * change to DISABLED is handled in the onDisable method.
	 *
	 * @param target the package to disable
	 *
	 * @return true if the package has been successfully disabled
	 */
	public boolean disable(Package target) {
		this.setPackageState(target, PackageState.DISABLING);
		String msgDisabling =
				SafeResourceLoader.getString("ALERT_DISABLING",
						this.resourceBundle, "Disabling package $PACKAGE...")
						.replaceFirst("\\$PACKAGE", target.getName());
		Log logDisabling =
				new Log(msgDisabling, LoggingLevel.FINE, this.getName());
		this.fireEvent(logDisabling);
		target.onDisable();// TODO can return false
		this.setPackageState(target, PackageState.DISABLED);
		PackageDisabled packDisabled = new PackageDisabled(target);
		this.fireEvent(packDisabled);
		return true;
	}

	/**
	 * Deactivates the package and halts all of its operations. The package is
	 * still loaded in memory but not active. Calls {@link #onDisable()}. This
	 * should change the packages state to {@link PackageState#DISABLING
	 * DISABLING}. If the disabling were done here and not in the
	 * {@link #onDisable()} method, the package state should be changed to
	 * {@link PackageState#DISABLED DISABLED} after completion. Otherwise the
	 * change to DISABLED is handled in the onDisable method.
	 *
	 * @param target the name of the package to disable
	 *
	 * @return true if the package has been successfully disabled
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
	 * Calls {@link #onEnable()}. This should change the package state to
	 * {@link PackageState#ENABLING ENABLING}. If the enabling were done here
	 * and not in the {@link #onEnable()} method, the package state should be
	 * changed to {@link PackageState#ENABLED ENABLED} after completion.
	 * Otherwise the change to ENABLED is handled in the onEnable method.
	 *
	 * @param target The package to enable
	 *
	 * @return true if the package was successfully enabled
	 */
	public boolean enable(Package target) {
		this.setPackageState(target, PackageState.ENABLING);
		String msgEnable =
				SafeResourceLoader.getString("ALERT_ENABLING",
						this.resourceBundle, "Enabling package $PACKAGE...")
						.replaceFirst("\\$PACKAGE", target.getName());
		Log logEnabling = new Log(msgEnable, LoggingLevel.FINE, this.getName());
		this.fireEvent(logEnabling);
		target.onEnable();// TODO can return false
		this.setPackageState(target, PackageState.ENABLED);
		PackageEnabled packEnabled = new PackageEnabled(target);
		this.fireEvent(packEnabled);
		return true;
	}

	/**
	 * Activates the package and enables it to perform its normal functions.
	 * Calls {@link #onEnable()}. This should change the package state to
	 * {@link PackageState#ENABLING ENABLING}. If the enabling were done here
	 * and not in the {@link #onEnable()} method, the package state should be
	 * changed to {@link PackageState#ENABLED ENABLED} after completion.
	 * Otherwise the change to ENABLED is handled in the onEnable method.
	 *
	 * @param target The name of the package to enable
	 *
	 * @return true if the package was successfully enabled
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
	@SuppressWarnings("unchecked")
	public ArrayList<PackageCommand> getCommands() {
		return (ArrayList<PackageCommand>) this.commands.clone();
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

	@Override
	public Set<Listener> getListeners() {
		return this.listeners;
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

	@Override
	public String getName() {
		return this.packageName;
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

	@Override
	public double getVersion() {
		return 0.3f;
	}

	/**
	 * Returns true if the package is enabled, and false otherwise. A state of
	 * {@link PackageState#ENABLED ENABLED} returns true, any other states will
	 * return false.
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
	 * return false.
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
	 * Loads the event manager and logging packages. This is done on
	 * initialization.
	 */
	private void loadCorePackages() {
		LoggingPackage loggingPack = new LoggingPackage(this);
		// TODO handle all this on creation
		this.loadedPackages.put(loggingPack.getName(), loggingPack);

		loggingPack.onLoad();
		this.enable(loggingPack);// TODO loaded and enabled events

		/*
		 * ensures the event manager can have its listeners registered after
		 * being loaded.
		 */
		for (Listener l : this.getListeners()) {
			this.eventManager.registerEventListeners(l);
		}

		for (Listener l : loggingPack.getListeners()) {
			this.eventManager.registerEventListeners(l);
		}

		String regListenersP =
				SafeResourceLoader.getString("ALERT_REG_EVENT_LISTENERS",
						this.resourceBundle,
						"Registered event listeners for $PACKAGE")
						.replaceFirst("\\$PACKAGE", this.getName());
		String regListenersL =
				SafeResourceLoader.getString("ALERT_REG_EVENT_LISTENERS",
						this.resourceBundle,
						"Registered event listeners for $PACKAGE")
						.replaceFirst("\\$PACKAGE", loggingPack.getName());
		String loadingL =
				SafeResourceLoader
						.getString("ALERT_LOADING", this.resourceBundle,
								"Loading package $PACKAGE (v$VERSION)...")
						.replaceFirst("\\$PACKAGE", loggingPack.getName())
						.replaceFirst("\\$VERSION",
								"" + loggingPack.getVersion());

		String loadedL =
				SafeResourceLoader
						.getString("ALERT_LOADED", this.resourceBundle,
								"Package $PACKAGE (v$VERSION) loaded!")
						.replaceFirst("\\$PACKAGE", loggingPack.getName())
						.replaceFirst("\\$VERSION",
								"" + loggingPack.getVersion());

		this.eventManager.fireEvent(new Log(loadingL, LoggingLevel.FINE, this));
		this.eventManager.fireEvent(new Log(regListenersP, LoggingLevel.FINER,
				this));
		this.eventManager.fireEvent(new Log(regListenersL, LoggingLevel.FINER,
				this));
		this.eventManager.fireEvent(new Log(loadedL, LoggingLevel.FINER, this));

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
		this.eventManager.fireEvent(new Log(loading, LoggingLevel.FINE, this));
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
			this.eventManager.fireEvent(new Log(alreadyLoaded,
					LoggingLevel.FINE, this));
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
				this.eventManager.fireEvent(new Log(outdated,
						LoggingLevel.FINE, this));
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
		this.eventManager.fireEvent(new Log(msg, LoggingLevel.FINER, this));

		// load it
		toLoad.onLoad();

		this.setPackageState(toLoad, PackageState.DISABLED);
		PackageLoaded packLoaded = new PackageLoaded(toLoad);
		this.fireEvent(packLoaded);

		return true;
	}

	/**
	 * Loads a plugin from a name
	 *
	 * @param name the filename to load from
	 * @return true on success, false if it failed
	 */
	public boolean loadPlugin(String name) {
		// TODO javadoc
		// TODO load a package from file
		/*
		 * Check for being a jar file check for package info file load and check
		 * for valid info load the file if necessary
		 */
		/*
		 * File pluginFolder = Game.getPluginFolder(); if
		 * (!pluginFolder.exists()) { // TOD O log error return false; } if
		 * (!pluginFolder.isDirectory()) { // TO DO log error return false; }
		 * String[] filenames; filenames = pluginFolder.list(); if (filenames ==
		 * null) { // TOD O log error return false; } if (filenames.length == 0)
		 * { // empty // TO DO log error return false; } filenames = null;
		 * 
		 * ArrayList<File> files = new ArrayList<File>();
		 * 
		 * // adds valid jar files to the list of files for (File f :
		 * pluginFolder.listFiles()) { if (f.isDirectory()) { continue;// its a
		 * folder } if (!f.getName().toLowerCase().endsWith(".jar")) {
		 * continue;// its not a jar file } files.add(f); }
		 * 
		 * if (files.size() == 0) { // TOD O log error return false; }
		 */
		/*
		 * ListIterator<File> iterator = files.listIterator(); File tmp; while
		 * (iterator.hasNext()) { tmp = iterator.next(); }
		 */
		return false;
	}

	@Override
	public boolean onDisable() {
		return true;
	}

	@Override
	public boolean onEnable() {
		return true;
	}

	@Override
	public boolean onLoad() {
		return true;
	}

	@Override
	public boolean onUnload() {
		return true;
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
			LogError err =
					new LogError(msg, LoggingLevel.WARNING, this.getName());
			this.fireEvent(err);
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
		Log log = new Log(msg, LoggingLevel.FINEST, this.getName());
		this.fireEvent(log);
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
			this.registerCommand(tmp, this);
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
			Log message = new Log(tmp, LoggingLevel.WARNING, this.getName());
			this.fireEvent(message);
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
			this.eventManager.fireEvent(new Log(notLoaded, LoggingLevel.FINE,
					this));
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
		this.eventManager
				.fireEvent(new Log(unloading, LoggingLevel.FINE, this));
		if (!this.isLoaded(toUnload)) {
			String notLoaded =
					SafeResourceLoader.getString("PACKAGE_NOT_LOADED",
							this.resourceBundle, "Package $PACKAGE not loaded")
							.replaceFirst("\\$PACKAGE", toUnload);
			this.eventManager.fireEvent(new Log(notLoaded, LoggingLevel.FINE,
					this));

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
						.replaceFirst("\\$PACKAGE", this.getName());
		this.eventManager.fireEvent(new Log(unreg, LoggingLevel.FINER, this));

		this.loadedPackages.remove(toUnload);
		this.packageStates.remove(pack);

		String unloaded =
				SafeResourceLoader.getString("ALERT_UNLOADED",
						this.resourceBundle, "Package $PACKAGE unloaded!");
		unloaded = unloaded.replaceFirst("\\$PACKAGE", toUnload);

		this.eventManager.fireEvent(new Log(unloaded, LoggingLevel.FINE, this));

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
			Log log = new Log(msg, LoggingLevel.FINEST, this.getName());
			this.fireEvent(log);
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
