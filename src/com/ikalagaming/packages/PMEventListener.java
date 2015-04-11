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
import com.ikalagaming.logging.events.LogError;
import com.ikalagaming.packages.events.PackageCommandSent;
import com.ikalagaming.packages.events.PackageEvent;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * The event listener for the package management system.
 *
 * @author Ches Burks
 *
 */
public class PMEventListener implements Listener {

	private final String callMethod;
	private final String onLoad;
	private final String onUnload;
	private final String enable;
	private final String disable;

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
		this.callMethod =
				SafeResourceLoader.getString("CMD_CALL",
						this.manager.getResourceBundle(), "call");
		this.onLoad =
				SafeResourceLoader.getString("ARG_ON_LOAD",
						this.manager.getResourceBundle(), "onLoad");
		this.onUnload =
				SafeResourceLoader.getString("ARG_ON_UNLOAD",
						this.manager.getResourceBundle(), "onUnload");
		this.enable =
				SafeResourceLoader.getString("ARG_ENABLE",
						this.manager.getResourceBundle(), "enable");
		this.disable =
				SafeResourceLoader.getString("ARG_DISABLE",
						this.manager.getResourceBundle(), "disable");

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
		String tmp = "";
		ConsoleMessage message;

		Package pack = this.manager.getPackage(packageName);

		if (pack == null) {
			tmp =
					SafeResourceLoader.getString("PACKAGE_NOT_LOADED",
							this.manager.getResourceBundle(),
							"Package $PACKAGE not loaded").replaceFirst(
							"\\$PACKAGE", packageName);
			message = new ConsoleMessage(tmp);
			this.manager.fireEvent(message);
			// stop right here. It does not exist
			return;
		}
		if (!pack.isEnabled()) {
			tmp =
					SafeResourceLoader.getString("package_disable_fail",
							this.manager.getResourceBundle(),
							"Package failed to disable");
			message = new ConsoleMessage(tmp);
			this.manager.fireEvent(message);
			return;
		}
		// unload the package
		this.manager.fireEvent(new PackageEvent("package-manager", packageName,
				this.callMethod + " " + this.disable));
	}

	/**
	 * Called when a command event is sent.
	 *
	 * @param event the command sent
	 */
	@EventHandler
	public void onCommand(PackageCommandSent event) {
		// TODO handle these with scripting?
		if (event.getCommand().equalsIgnoreCase(this.cmd_help)) {
			this.printHelp();
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_packages)) {
			this.printPackages();
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_enable)) {
			this.printWIP();
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_disable)) {
			String name = "";
			if (event.getArgs().length >= 1) {
				name = event.getArgs()[0];
			}
			this.disable(name);
			this.printWIP();
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_load)) {
			this.printWIP();
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_unload)) {
			this.printWIP();
		}
		else if (event.getCommand().equalsIgnoreCase(this.cmd_reload)) {
			this.printWIP();
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
		if (!this.manager.getCommandRegistry().contains(firstWord)) {
			ReportUnknownCommand report = new ReportUnknownCommand(firstWord);
			this.manager.fireEvent(report);
		}

		Package pack = this.manager.getCommandRegistry().getParent(firstWord);
		if (pack != null) {
			this.manager.fireEvent(new PackageCommandSent(pack.getName(), event
					.getCommand(), "console"));
		}

	}

	/**
	 * Called when a package event is sent out by the event system.
	 *
	 * @param event the event that was fired
	 */
	@EventHandler
	public void onPackageEvent(PackageEvent event) {

		if (!this.manager.isLoaded(event.getTo())) {
			String err =
					SafeResourceLoader.getString("PACKAGE_NOT_LOADED",
							this.manager.getResourceBundle(),
							"Package $PACKAGE not loaded").replaceFirst(
							"\\$PACKAGE", event.getTo());
			this.manager.fireEvent(new LogError(err, LoggingLevel.INFO,
					"package-manager"));
			return;
		}
		Package pack = this.manager.getPackage(event.getTo());
		if (event.getMessage().startsWith(this.callMethod)) {

			String trimmed =
					event.getMessage().replaceFirst(this.callMethod, "");
			trimmed = trimmed.replaceFirst(" ", "");
			if (trimmed.startsWith(this.onLoad)) {
				pack.onLoad();
			}
			else if (trimmed.startsWith(this.onUnload)) {
				pack.onUnload();
			}
			else if (trimmed.startsWith(this.enable)) {
				pack.enable();
			}
			else if (trimmed.startsWith(this.disable)) {
				pack.disable();
				String details =
						SafeResourceLoader.getString("ALERT_DISABLED",
								this.manager.getResourceBundle(),
								"Package $PACKAGE ($VERSION) disabled!");
				details = details.replaceFirst("\\$PACKAGE", pack.getName());
				details =
						details.replaceFirst("\\$VERSION",
								"" + pack.getVersion());
				this.manager.fireEvent(new Log(details, LoggingLevel.FINE,
						"package-manager"));
			}
		}
	}

	private void printHelp() {
		ArrayList<PackageCommand> commands;
		commands = this.manager.getCommandRegistry().getCommands();
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
			if (loadedPackages.get(name).isEnabled()) {
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
