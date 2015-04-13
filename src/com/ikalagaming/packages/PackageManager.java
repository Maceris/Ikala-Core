package com.ikalagaming.packages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import com.ikalagaming.event.Event;
import com.ikalagaming.event.EventManager;
import com.ikalagaming.event.Listener;
import com.ikalagaming.localization.Localization;
import com.ikalagaming.logging.LoggingLevel;
import com.ikalagaming.logging.LoggingPackage;
import com.ikalagaming.logging.events.Log;
import com.ikalagaming.packages.events.PackageEvent;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * Handles loading, unloading and storage of Packages. This is considered a
 * package, but is always enabled and never loaded.
 *
 * @author Ches Burks
 *
 */
public class PackageManager implements Package {

	private CommandRegistry cmdRegistry;

	private EventManager eventManager;
	private PMEventListener listener;
	private HashSet<Listener> listeners;
	/** maps strings to packages loaded in memory */
	private HashMap<String, Package> loadedPackages;
	private String packageName = "package-manager";
	private ResourceBundle resourceBundle;
	private PackageState state = PackageState.ENABLED;
	private final double version = 0.2;

	/**
	 * Constructs a new {@link PackageManager} and initializes variables.
	 *
	 * @param evtManager The event manager to use for the package system
	 */
	public PackageManager(EventManager evtManager) {
		this.eventManager = evtManager;
		this.loadedPackages = new HashMap<>();
		this.resourceBundle =
				ResourceBundle.getBundle(
						"com.ikalagaming.packages.resources.PackageManager",
						Localization.getLocale());
		this.listeners = new HashSet<>();
		this.cmdRegistry = new CommandRegistry(this, evtManager);

		this.listener = new PMEventListener(this);
		this.listeners.add(this.listener);

		this.loadCorePackages();
		this.registerCommands();
	}

	private void callDirectly(Package recipient, String operation) {
		if (operation == "load") {
			recipient.onLoad();
		}
		else if (operation == "enable") {
			recipient.enable();
		}
		else if (operation == "disable") {
			recipient.disable();
		}
		else if (operation == "unload") {
			recipient.onUnload();
		}
	}

	private boolean callUsingEvents(Package recipient, String message) {

		/*
		 * Tries to send the event. If the return value is false, it failed and
		 * therefore we must load manually
		 */
		if (!this.firePackageEvent(recipient.getName(), message)) {
			String failMsg =
					SafeResourceLoader.getString("ALERT_CALL_EVENT_FAILED",
							this.resourceBundle,
							"Event failed. Calling method directly");
			this.eventManager.fireEvent(new Log(failMsg, LoggingLevel.FINER,
					this));
			return false;
		}
		return true;
	}

	/**
	 * Change the state of a given package. The operations allowed are
	 * <ul>
	 * <li>load</li>
	 * <li>enable</li>
	 * <li>disable</li>
	 * <li>unload</li>
	 * </ul>
	 * If events fail and should be used, reverts to direct calling of methods.
	 *
	 * @param toChange the package to change
	 * @param operation what you want to do to the package
	 * @param usingEvents true if you want to use events, otherwise false
	 */
	private void changeState(Package toChange, String operation,
			boolean usingEvents) {
		String toSend = "";
		String localMethodName = "";
		String backupMethodName = "";
		boolean callDirectly = usingEvents;
		if (operation == "load") {
			localMethodName = "ARG_ON_LOAD";
			backupMethodName = "onLoad";
		}
		else if (operation == "enable") {
			localMethodName = "ARG_ENABLE";
			backupMethodName = "enable";
		}
		else if (operation == "disable") {
			localMethodName = "ARG_DISABLE";
			backupMethodName = "disable";
		}
		else if (operation == "onUnload") {
			localMethodName = "ARG_ON_UNLOAD";
			backupMethodName = "onUnload";
		}

		else {
			localMethodName = "";
			backupMethodName = "";
		}

		toSend =
				SafeResourceLoader.getString("CMD_CALL", this.resourceBundle,
						"call")
						+ " "
						+ SafeResourceLoader.getString(localMethodName,
								this.resourceBundle, backupMethodName);

		this.logMethodCall(backupMethodName, toChange.getName(), true);
		if (!callDirectly) {
			callDirectly = !this.callUsingEvents(toChange, toSend);
		}
		if (callDirectly) {
			this.logMethodCall(backupMethodName, toChange.getName(), false);
			this.callDirectly(toChange, operation);
		}
	}

	@Override
	public boolean disable() {
		return false;
	}

	@Override
	public boolean enable() {
		return true;
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
	 * Fires an event with a message to a package type from the package manager.
	 * If an error occurs, this will return false. The event should not have
	 * been sent if false was returned.
	 *
	 * @param to the package to send the message to
	 * @param content the message to transfer
	 * @return true if the event was fired correctly
	 */
	private boolean firePackageEvent(String to, String content) {

		PackageEvent tmpEvent;

		tmpEvent = new PackageEvent(this.packageName, to, content);
		this.eventManager.fireEvent(tmpEvent);

		return true;
	}

	/**
	 * Returns the {@link CommandRegistry} for this package manager.
	 *
	 * @return the command registry
	 */
	public CommandRegistry getCommandRegistry() {
		if (this.cmdRegistry == null) {
			this.cmdRegistry = new CommandRegistry(this, this.eventManager);
		}
		return this.cmdRegistry;
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

	@Override
	public PackageState getPackageState() {
		synchronized (this.state) {
			return this.state;
		}
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
		return this.version;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	/**
	 * Returns true if a package exists that has the same type as the provided
	 * package (for example: "Graphics"). This is the same as calling
	 * <code>{@link #isLoaded(String) isLoaded}(Package.getType())</code>
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
		LoggingPackage loggingPack = new LoggingPackage(this.eventManager);

		this.loadedPackages.put(loggingPack.getName(), loggingPack);

		loggingPack.onLoad();
		loggingPack.enable();

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
						.getString("ALERT_PACKAGE_LOADING",
								this.resourceBundle,
								"Loading package $PACKAGE (v$VERSION)...")
						.replaceFirst("\\$PACKAGE", loggingPack.getName())
						.replaceFirst("\\$VERSION",
								"" + loggingPack.getVersion());

		String loadedL =
				SafeResourceLoader
						.getString("ALERT_PACKAGE_LOADED", this.resourceBundle,
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
	 * packages are {@link PackageSettings#ENABLE_ON_LOAD enabled on load} by
	 * default.
	 * </p>
	 * <p>
	 * If the type of package already exists in the manager, and the new package
	 * has a higher version number, then the old package is unloaded and the new
	 * one is loaded in its place. If the versions are equal, or the new package
	 * is older, then it does not load the new version and returns false.
	 * </p>
	 *
	 * @param toLoad the package to load
	 * @return true if the package was loaded properly, false otherwise
	 */
	@SuppressWarnings("unused")
	public boolean loadPackage(Package toLoad) {
		String loading =
				SafeResourceLoader.getString("ALERT_PACKAGE_LOADING",
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

		// store the new package
		this.loadedPackages.put(toLoad.getName(), toLoad);

		if (PackageSettings.USE_EVENTS_FOR_ACCESS) {
			for (Listener l : toLoad.getListeners()) {
				this.eventManager.registerEventListeners(l);
			}
			String msg =
					SafeResourceLoader.getString("ALERT_REG_EVENT_LISTENERS",
							this.resourceBundle,
							"Registered event listeners for $PACKAGE")
							.replaceFirst("\\$PACKAGE", toLoad.getName());
			this.eventManager.fireEvent(new Log(msg, LoggingLevel.FINER, this));
		}

		// load it
		if (PackageSettings.USE_EVENTS_FOR_ACCESS
				&& PackageSettings.USE_EVENTS_FOR_ON_LOAD) {
			this.changeState(toLoad, "load", true);
		}
		else {
			this.changeState(toLoad, "load", false);
		}

		// enable the package
		if (PackageSettings.ENABLE_ON_LOAD) {
			if (PackageSettings.USE_EVENTS_FOR_ACCESS
					&& PackageSettings.USE_EVENTS_FOR_ENABLE) {
				this.changeState(toLoad, "enable", true);
			}
			else {
				this.changeState(toLoad, "enable", false);
			}
		}

		String loaded =
				SafeResourceLoader.getString("ALERT_PACKAGE_LOADED",
						this.resourceBundle,
						"Package $PACKAGE (v$VERSION) loaded!");
		loaded = loaded.replaceFirst("\\$PACKAGE", toLoad.getName());
		loaded = loaded.replaceFirst("\\$VERSION", "" + toLoad.getVersion());
		this.eventManager.fireEvent(new Log(loaded, LoggingLevel.FINE, this));
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

	/**
	 * Logs a call to the given method of the given package. Uses the
	 * ALERT_CALL_METHOD_EVENT or ALERT_CALL_METHOD_DIRECT depending on whether
	 * usingEvents is true or false.
	 *
	 * @param method the method being called
	 * @param pack the package the method belongs to
	 * @param usingEvents true if it is using the event system, false if direct
	 */
	private void logMethodCall(String method, String pack, boolean usingEvents) {
		String call;
		if (usingEvents) {
			call =
					SafeResourceLoader.getString("ALERT_CALL_METHOD_EVENT",
							this.resourceBundle,
							"Calling $METHOD of $PACKAGE using event system");
		}
		else {
			call =
					SafeResourceLoader.getString("ALERT_CALL_METHOD_DIRECT",
							this.resourceBundle,
							"Calling $METHOD of $PACKAGE directly");
		}
		call = call.replaceFirst("\\$METHOD", method);
		call = call.replaceFirst("\\$PACKAGE", pack);

		this.eventManager.fireEvent(new Log(call, LoggingLevel.FINER, this));
	}

	@Override
	public void onDisable() {}

	@Override
	public void onEnable() {}

	@Override
	public void onLoad() {}

	@Override
	public void onUnload() {}

	/**
	 * Registers commands with the registry that the package manager uses
	 */
	private void registerCommands() {
		ArrayList<String> commands = new ArrayList<>();
		commands.add("COMMAND_ENABLE");
		commands.add("COMMAND_DISABLE");
		commands.add("COMMAND_LOAD");
		commands.add("COMMAND_UNLOAD");
		commands.add("COMMAND_LIST_PACKAGES");
		commands.add("COMMAND_RELOAD");
		commands.add("COMMAND_HELP");
		commands.add("COMMAND_VERSION");

		String tmp = "";

		for (String s : commands) {

			tmp = SafeResourceLoader.getString(s, this.resourceBundle, s);
			this.cmdRegistry.registerCommand(tmp, this);
		}

	}

	@Override
	public boolean reload() {
		return false;
	}

	@Override
	public void setPackageState(PackageState newState) {
		synchronized (this.state) {
			this.state = newState;
		}
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
	@SuppressWarnings("unused")
	public boolean unloadPackage(String toUnload) {
		String unloading =
				SafeResourceLoader.getString("ALERT_PACKAGE_UNLOADING",
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

		// It has to be disabled before unloading.
		if (this.loadedPackages.get(toUnload).isEnabled()) {
			if (PackageSettings.USE_EVENTS_FOR_ACCESS
					&& PackageSettings.USE_EVENTS_FOR_DISABLE) {
				this.changeState(this.loadedPackages.get(toUnload), "disable",
						true);
			}
			else {
				this.changeState(this.loadedPackages.get(toUnload), "disable",
						false);
			}
		}

		if (PackageSettings.USE_EVENTS_FOR_ACCESS
				&& PackageSettings.USE_EVENTS_FOR_ON_UNLOAD) {
			this.changeState(this.loadedPackages.get(toUnload), "unload", true);
		}
		else {
			this.changeState(this.loadedPackages.get(toUnload), "unload", false);
		}

		for (Listener l : this.loadedPackages.get(toUnload).getListeners()) {
			this.eventManager.unregisterEventListeners(l);
		}
		String unreg =
				SafeResourceLoader.getString("ALERT_UNREG_EVENT_LISTENERS",
						this.resourceBundle,
						"Unregistered event listeners for $PACKAGE")
						.replaceFirst("\\$PACKAGE", this.getName());
		this.eventManager.fireEvent(new Log(unreg, LoggingLevel.FINER, this));

		this.loadedPackages.remove(toUnload);

		String unloaded =
				SafeResourceLoader.getString("ALERT_PACKAGE_UNLOADED",
						this.resourceBundle, "Package $PACKAGE unloaded!");
		unloaded = unloaded.replaceFirst("\\$PACKAGE", toUnload);

		this.eventManager.fireEvent(new Log(unloaded, LoggingLevel.FINE, this));

		return true;
	}
}
