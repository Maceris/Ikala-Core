package com.ikalagaming.packages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.ikalagaming.event.EventHandler;
import com.ikalagaming.event.Listener;
import com.ikalagaming.gui.console.events.ConsoleCommandEntered;
import com.ikalagaming.gui.console.events.ConsoleMessage;
import com.ikalagaming.gui.console.events.ReportUnknownCommand;
import com.ikalagaming.logging.LoggingLevel;
import com.ikalagaming.logging.events.Log;
import com.ikalagaming.packages.events.PackageCommandSent;
import com.ikalagaming.packages.events.PackageDisabled;
import com.ikalagaming.packages.events.PackageEnabled;
import com.ikalagaming.packages.events.PackageLoaded;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * The event listener for the package management system.
 *
 * @author Ches Burks
 *
 */
class PMEventListener implements Listener {

	private String cmd_help;
	private String cmd_packages;
	private String cmd_enable;
	private String cmd_disable;
	private String cmd_load;
	private String cmd_unload;
	private String cmd_reload;
	private String cmd_version;

	private PackageManager manager;

	/**
	 * Constructs a listener for the given package manager.
	 *
	 * @param parent the manager to handle events for
	 */
	public PMEventListener(PackageManager parent) {
		this.manager = parent;

		this.cmd_help =
				SafeResourceLoader.getString("COMMAND_HELP",
						this.manager.getResourceBundle(), "help");
		this.cmd_packages =
				SafeResourceLoader.getString("COMMAND_LIST_PACKAGES",
						this.manager.getResourceBundle(), "packages");
		this.cmd_enable =
				SafeResourceLoader.getString("COMMAND_ENABLE",
						this.manager.getResourceBundle(), "enable");
		this.cmd_disable =
				SafeResourceLoader.getString("COMMAND_DISABLE",
						this.manager.getResourceBundle(), "disable");
		this.cmd_load =
				SafeResourceLoader.getString("COMMAND_LOAD",
						this.manager.getResourceBundle(), "load");
		this.cmd_unload =
				SafeResourceLoader.getString("COMMAND_UNLOAD",
						this.manager.getResourceBundle(), "unload");
		this.cmd_reload =
				SafeResourceLoader.getString("COMMAND_RELOAD",
						this.manager.getResourceBundle(), "reload");
		this.cmd_version =
				SafeResourceLoader.getString("COMMAND_VERSION",
						this.manager.getResourceBundle(), "version");

	}

	/**
	 * Disables the specified package. If no package exists, alerts the user to
	 * this fact.
	 *
	 * @param packageName The package to find a version for
	 */
	private void disable(String packageName) {
		ConsoleMessage message;
		Package pack = this.manager.getPackage(packageName);

		if (pack == null) {
			message =
					new ConsoleMessage(SafeResourceLoader.getString(
							"PACKAGE_NOT_LOADED",
							this.manager.getResourceBundle(),
							"Package $PACKAGE not loaded").replaceFirst(
							"\\$PACKAGE", packageName));
			this.manager.fireEvent(message);
			// stop right here. It does not exist
			return;
		}
		if (!this.manager.isEnabled(pack)) {
			message =
					new ConsoleMessage(SafeResourceLoader.getString(
							"package_disable_fail",
							this.manager.getResourceBundle(),
							"Package failed to disable"));
			this.manager.fireEvent(message);
			return;
		}
		this.manager.disable(pack);
	}

	private void reload(String packageName) {
		ConsoleMessage message;
		Package pack = this.manager.getPackage(packageName);

		if (pack == null) {
			message =
					new ConsoleMessage(SafeResourceLoader.getString(
							"PACKAGE_NOT_LOADED",
							this.manager.getResourceBundle(),
							"Package $PACKAGE not loaded").replaceFirst(
							"\\$PACKAGE", packageName));
			this.manager.fireEvent(message);
			// stop right here. It does not exist
			return;
		}
		this.manager.reload(pack);
	}

	private void enable(String packageName) {
		ConsoleMessage message;
		Package pack = this.manager.getPackage(packageName);

		if (pack == null) {
			message =
					new ConsoleMessage(SafeResourceLoader.getString(
							"PACKAGE_NOT_LOADED",
							this.manager.getResourceBundle(),
							"Package $PACKAGE not loaded").replaceFirst(
							"\\$PACKAGE", packageName));
			this.manager.fireEvent(message);
			// stop right here. It does not exist
			return;
		}
		if (this.manager.isEnabled(pack)) {
			message =
					new ConsoleMessage(SafeResourceLoader.getString(
							"package_enable_fail",
							this.manager.getResourceBundle(),
							"Package failed to enable"));
			this.manager.fireEvent(message);
			return;
		}
		this.manager.enable(pack);
	}

	private void unload(String packageName) {
		ConsoleMessage message;
		Package pack = this.manager.getPackage(packageName);

		if (pack == null) {
			message =
					new ConsoleMessage(SafeResourceLoader.getString(
							"PACKAGE_NOT_LOADED",
							this.manager.getResourceBundle(),
							"Package $PACKAGE not loaded").replaceFirst(
							"\\$PACKAGE", packageName));
			this.manager.fireEvent(message);
			// stop right here. It does not exist
			return;
		}
		if (this.manager.isEnabled(pack)) {
			this.manager.disable(pack);
		}
		this.manager.unloadPackage(pack);
	}

	/**
	 * Called when a command event is sent.
	 *
	 * @param event the command sent
	 */
	@EventHandler
	public void onCommand(PackageCommandSent event) {
		if (event.getCommand().equalsIgnoreCase(this.cmd_help)) {
			this.printHelp();
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_packages)) {
			this.printPackages();
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_enable)) {
			String name = "";
			if (event.getArgs().length >= 1) {
				name = event.getArgs()[0];
			}
			else {
				// TODO error
			}
			this.enable(name);
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_disable)) {
			String name = "";
			if (event.getArgs().length >= 1) {
				name = event.getArgs()[0];
			}
			else {
				// TODO error
			}
			this.disable(name);
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_load)) {
			this.printWIP();
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_unload)) {
			String name = "";
			if (event.getArgs().length >= 1) {
				name = event.getArgs()[0];
			}
			else {
				// TODO error
			}
			this.unload(name);
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_reload)) {
			String name = "";
			if (event.getArgs().length >= 1) {
				name = event.getArgs()[0];
			}
			else {
				// TODO error
			}
			this.reload(name);
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_version)) {
			String name = "";
			if (event.getArgs().length >= 1) {
				name = event.getArgs()[0];
			}
			this.printVersion(name);
		}

	}

	/**
	 * Check to see if the command is registered, and handle it or report it as
	 * unregistered.
	 *
	 * @param event the command sent by the console
	 */
	@EventHandler
	public void onConsoleCommandEntered(ConsoleCommandEntered event) {

		String firstWord = event.getCommand().trim().split("\\s+")[0];
		if (!this.manager.containsCommand(firstWord)) {
			ReportUnknownCommand report = new ReportUnknownCommand(firstWord);
			this.manager.fireEvent(report);
		}

		Package pack = this.manager.getCommandParent(firstWord);
		if (pack != null) {
			this.manager.fireEvent(new PackageCommandSent(pack.getName(), event
					.getCommand(), "console"));
		}

	}

	/**
	 * Logs the package being disabled.
	 *
	 * @param event the event that was received
	 */
	@EventHandler
	public void onPackageDisabled(PackageDisabled event) {
		Package target = event.getPackage();
		String msgDisabled =
				SafeResourceLoader
						.getString("ALERT_ENABLED",
								this.manager.getResourceBundle(),
								"Package $PACKAGE (v$VERSION) enabled!")
						.replaceFirst("\\$PACKAGE", target.getName())
						.replaceFirst("\\$VERSION", target.getVersion() + "");
		Log logDisabled =
				new Log(msgDisabled, LoggingLevel.FINE, this.manager.getName());
		this.manager.fireEvent(logDisabled);
	}

	/**
	 * Logs the package being enabled.
	 *
	 * @param event the event that was received
	 */
	@EventHandler
	public void onPackageEnabled(PackageEnabled event) {
		Package target = event.getPackage();
		String msgEnabled =
				SafeResourceLoader
						.getString("ALERT_ENABLED",
								this.manager.getResourceBundle(),
								"Package $PACKAGE (v$VERSION) enabled!")
						.replaceFirst("\\$PACKAGE", target.getName())
						.replaceFirst("\\$VERSION", target.getVersion() + "");
		Log logEnabled =
				new Log(msgEnabled, LoggingLevel.FINE, this.manager.getName());
		this.manager.fireEvent(logEnabled);
	}

	// logging package events
	/**
	 * If packages should be enabled on load, this enables the newly loaded
	 * package. Also logs the package being loaded.
	 *
	 * @param event the event that was received
	 */
	@EventHandler
	public void onPackageLoaded(PackageLoaded event) {
		if (this.manager.enableOnLoad()) {
			this.manager.enable(event.getPackage());
		}
		String loaded =
				SafeResourceLoader.getString("ALERT_LOADED",
						this.manager.getResourceBundle(),
						"Package $PACKAGE (v$VERSION) loaded!");
		loaded =
				loaded.replaceFirst("\\$PACKAGE", event.getPackage().getName());
		loaded =
				loaded.replaceFirst("\\$VERSION", ""
						+ event.getPackage().getVersion());
		this.manager
				.fireEvent(new Log(loaded, LoggingLevel.FINE, this.manager));
	}

	private void printHelp() {
		ArrayList<PackageCommand> commands;
		commands = this.manager.getCommands();
		String tmp;
		ConsoleMessage message;
		for (PackageCommand cmd : commands) {
			tmp = cmd.getCommand();

			message = new ConsoleMessage(tmp);
			this.manager.fireEvent(message);
		}
	}

	private void printPackages() {
		String tmp;
		ConsoleMessage message;
		HashMap<String, Package> loadedPackages =
				this.manager.getLoadedPackages();
		ArrayList<String> names = new ArrayList<>();
		names.addAll(loadedPackages.keySet());
		Collections.sort(names);

		for (String name : names) {
			tmp = "";
			tmp += loadedPackages.get(name).getName();
			tmp +=
					" "
							+ SafeResourceLoader.getString("PACKAGE_VERSION",
									this.manager.getResourceBundle(), "v")
							+ loadedPackages.get(name).getVersion();
			if (this.manager.isEnabled(loadedPackages.get(name))) {
				tmp +=
						" "
								+ "("
								+ SafeResourceLoader.getString(
										"ENABLED_STATUS",
										this.manager.getResourceBundle(),
										"Enabled") + ")";
			}
			else {
				tmp +=
						" "
								+ "("
								+ SafeResourceLoader.getString(
										"DISABLED_STATUS",
										this.manager.getResourceBundle(),
										"Disabled") + ")";
			}
			message = new ConsoleMessage(tmp);
			this.manager.fireEvent(message);
		}
	}

	/**
	 * Prints the version of the package specified, if it exists. If no package
	 * exists, alerts the user to this fact.
	 *
	 * @param packageName The package to find a version for
	 */
	private void printVersion(String packageName) {
		String tmp = "";
		ConsoleMessage message;

		Package pack = this.manager.getPackage(packageName);

		if (pack != null) {
			tmp =
					SafeResourceLoader.getString("PACKAGE_VERSION",
							this.manager.getResourceBundle(), "v")
							+ pack.getVersion();
		}
		else {
			tmp =
					SafeResourceLoader.getString("PACKAGE_NOT_LOADED",
							this.manager.getResourceBundle(),
							"Package $PACKAGE not loaded").replaceFirst(
							"\\$PACKAGE", packageName);
		}
		message = new ConsoleMessage(tmp);
		this.manager.fireEvent(message);
	}

	/**
	 * Alerts the user that the given feature is not yet completed or working
	 * correctly.
	 */
	private void printWIP() {
		ConsoleMessage message =
				new ConsoleMessage(SafeResourceLoader.getString("WIP_TEXT",
						this.manager.getResourceBundle(),
						"WIP. This may not function correctly."));
		this.manager.fireEvent(message);
	}

}
