package com.ikalagaming.logging;

import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import com.ikalagaming.event.EventHandler;
import com.ikalagaming.event.EventManager;
import com.ikalagaming.event.Listener;
import com.ikalagaming.localization.Localization;
import com.ikalagaming.logging.events.Log;
import com.ikalagaming.logging.events.LogError;
import com.ikalagaming.packages.Package;
import com.ikalagaming.packages.PackageState;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * Handles reporting and logging errors.
 *
 * @author Ches Burks
 *
 */
public class LoggingPackage implements Package, Listener {

	private ResourceBundle resourceBundle;
	private PackageState state = PackageState.DISABLED;
	private final double version = 0.2;
	private String packageName = "logging";
	private LogDispatcher dispatcher;
	private String newLog = "";
	private Set<Listener> listeners;
	private EventManager manager;
	/**
	 * Only logs events that are of this level or higher
	 */
	public static LoggingLevel threshold = LoggingLevel.ALL;

	/**
	 * Creates a logging package for the given event manager
	 *
	 * @param eventManager the event manager to use in firing events
	 */
	public LoggingPackage(EventManager eventManager) {
		this.manager = eventManager;
	}

	@Override
	public boolean disable() {
		this.setPackageState(PackageState.DISABLING);
		try {
			this.onDisable();
		}
		catch (Exception e) {
			this.logError(this.packageName, SafeResourceLoader.getString(
					"package_disable_fail",
					"com.ikalagaming.packages.resources.PackageManager",
					"Package failed to disable"), LoggingLevel.SEVERE,
					"LoggingPackage.enable()");
			this.setPackageState(PackageState.CORRUPTED);
			return false;
		}
		return true;
	}

	@Override
	public boolean enable() {
		this.setPackageState(PackageState.ENABLING);
		try {
			this.onEnable();
		}
		catch (Exception e) {
			this.logError(this.packageName, SafeResourceLoader.getString(
					"package_enable_fail",
					"com.ikalagaming.packages.resources.PackageManager",
					"Package failed to enable"), LoggingLevel.SEVERE,
					"LoggingPackage.enable()");
			// better safe than sorry (probably did not initialize correctly)
			this.setPackageState(PackageState.CORRUPTED);
			return false;
		}
		return true;
	}

	@Override
	public Set<Listener> getListeners() {
		if (this.listeners == null) {
			this.listeners = new HashSet<>();
			this.listeners.add(this);
		}
		return this.listeners;
	}

	@Override
	public String getName() {
		return this.packageName;
	}

	@Override
	public PackageState getPackageState() {
		synchronized (this.state) {
			return this.state;
		}
	}

	@Override
	public double getVersion() {
		return this.version;
	}

	@Override
	public boolean isEnabled() {
		if (this.getPackageState() == PackageState.ENABLED) {
			return true;
		}
		return false;
	}

	/**
	 * Logs the provided information. Attempts to use localized names for the
	 * logging level. This only logs information that is above or equal to the
	 * logging threshold. <br>
	 * If the package is not enabled, simply logs straight to System.out
	 *
	 * @param origin The package that is logging the info
	 * @param level what level is the requested log
	 * @param details what to log
	 */
	private void log(String origin, LoggingLevel level, String details) {
		this.newLog = "";
		if (!this.isEnabled()) {
			System.out.println(level.getName() + " " + details);
			return;
		}
		if (level.intValue() < LoggingPackage.threshold.intValue()) {
			return;
		}
		try {
			this.newLog =
					SafeResourceLoader.getString("level_prefix",
							this.resourceBundle, "[")
							+ level.getLocalizedName()
							+ SafeResourceLoader.getString("level_postfix",
									this.resourceBundle, "]")
							+ " "
							+ SafeResourceLoader.getString("name_prefix",
									this.resourceBundle, "<")
							+ origin
							+ SafeResourceLoader.getString("name_postfix",
									this.resourceBundle, ">") + " " + details;
		}
		catch (Exception e) {
			System.err.println(level.getName());
			System.err.println(details);
			e.printStackTrace(System.err);// we need to know what broke the log
		}
		this.dispatcher.log(this.newLog);
	}

	/**
	 * Logs the provided error. Attempts to use localized names for the error
	 * code and logging level. This only logs errors that are above or equal to
	 * the threshold. The package name is listed before the info. <br>
	 * If the package is not enabled, simply logs straight to System.err
	 *
	 * @param origin The package that is logging the error
	 * @param error The error that occurred
	 * @param level what level is the requested log
	 * @param details additional information about the error
	 */
	private void logError(String origin, String error, LoggingLevel level,
			String details) {
		this.newLog = "";
		if (!this.isEnabled()) {
			System.err.println(level.getName() + " " + error + " " + details);
			return;
		}
		if (level.intValue() < LoggingPackage.threshold.intValue()) {
			return;
		}

		this.newLog =
				SafeResourceLoader.getString("level_prefix",
						this.resourceBundle, "[")
						+ level.getLocalizedName()
						+ SafeResourceLoader.getString("level_postfix",
								this.resourceBundle, "]")
						+ " "
						+ SafeResourceLoader.getString("name_prefix",
								this.resourceBundle, "<")
						+ origin
						+ SafeResourceLoader.getString("name_postfix",
								this.resourceBundle, ">")
						+ " "
						+ error
						+ " "
						+ details;

		this.dispatcher.log(this.newLog);

	}

	@Override
	public void onDisable() {
		this.setPackageState(PackageState.DISABLED);
	}

	@Override
	public void onEnable() {
		this.setPackageState(PackageState.ENABLED);
	}

	@Override
	public void onLoad() {
		this.setPackageState(PackageState.LOADING);
		try {
			this.resourceBundle =
					ResourceBundle.getBundle(
							"com.ikalagaming.logging.resources.LoggingPackage",
							Localization.getLocale());
		}
		catch (MissingResourceException missingResource) {
			// TODO Attempt to localize this somehow
			this.logError(this.packageName, "locale not found",
					LoggingLevel.SEVERE, "LoggingPackage.onLoad()");
		}
		this.dispatcher = new LogDispatcher(this.manager);
		this.dispatcher.start();
		this.setPackageState(PackageState.DISABLED);
	}

	/**
	 * Logs the provided error. Attempts to use localized names for the error
	 * code and logging level. This only logs errors that are above or equal to
	 * the threshold. The package name is listed before the info. <br>
	 * If the package is not enabled, simply logs straight to System.err
	 *
	 * @param event the Event to record
	 */
	@EventHandler
	public void onLogErrorEvent(LogError event) {
		this.logError(event.getSender(), event.getError(), event.getLevel(),
				event.getDetails());
	}

	/**
	 * Logs the provided information. Attempts to use localized names for the
	 * logging level. This only logs information that is above or equal to the
	 * logging threshold. <br>
	 * If the package is not enabled, simply logs straight to System.out
	 *
	 * @param event the Event to record
	 */
	@EventHandler
	public void onLogEvent(Log event) {
		this.log(event.getSender(), event.getLevel(), event.getDetails());
	}

	@Override
	public void onUnload() {
		this.setPackageState(PackageState.UNLOADING);
		this.resourceBundle = null;
		this.setPackageState(PackageState.PENDING_REMOVAL);
	}

	@Override
	public boolean reload() {
		this.setPackageState(PackageState.UNLOADING);
		if (this.getPackageState() == PackageState.ENABLED) {
			this.disable();
		}
		this.resourceBundle = null;
		this.setPackageState(PackageState.LOADING);
		this.onLoad();
		this.enable();// it will start up enabled
		return true;
	}

	@Override
	public void setPackageState(PackageState newState) {
		synchronized (this.state) {
			this.state = newState;
		}
	}

}
