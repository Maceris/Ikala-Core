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
import com.ikalagaming.packages.PackageManager;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * Handles reporting and logging errors.
 *
 * @author Ches Burks
 *
 */
public class LoggingPackage implements Package, Listener {

	private ResourceBundle resourceBundle;
	private final double version = 0.2;
	private String packageName = "logging";
	private LogDispatcher dispatcher;
	private String newLog = "";
	private Set<Listener> listeners;
	private EventManager eventManager;
	private PackageManager packageManager;
	/**
	 * Only logs events that are of this level or higher
	 */
	public static LoggingLevel threshold = LoggingLevel.ALL;

	/**
	 * Creates a logging package using the default static event manager.
	 */
	public LoggingPackage() {
		this.eventManager = EventManager.getInstance();
		this.packageManager = PackageManager.getInstance();
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
	public double getVersion() {
		return this.version;
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
		if (!packageManager.isEnabled(this)) {
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
		if (!packageManager.isEnabled(this)) {
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
	public boolean onDisable() {
		return true;
	}

	@Override
	public boolean onEnable() {
		return true;
	}

	@Override
	public boolean onLoad() {
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
			return false;
		}
		this.dispatcher = new LogDispatcher(this.eventManager);
		this.dispatcher.start();
		return true;
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
	public boolean onUnload() {
		this.resourceBundle = null;
		this.packageManager = null;
		this.eventManager = null;
		return true;
	}

}
