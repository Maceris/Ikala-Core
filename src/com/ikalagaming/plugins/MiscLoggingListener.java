package com.ikalagaming.plugins;

import com.ikalagaming.event.EventHandler;
import com.ikalagaming.event.Listener;
import com.ikalagaming.logging.events.Log;
import com.ikalagaming.plugins.events.PluginDisabled;
import com.ikalagaming.plugins.events.PluginEnabled;
import com.ikalagaming.plugins.events.PluginLoaded;

/**
 * The event listener for the plugin management system.
 *
 * @author Ches Burks
 *
 */
class MiscLoggingListener implements Listener {

	/**
	 * Logs the provided information. Attempts to use localized names for the
	 * logging level. This only logs information that is above or equal to the
	 * logging threshold. <br>
	 * If the plugin is not enabled, simply logs straight to System.out
	 *
	 * @param log the Event to record
	 */
	@EventHandler
	public void onLogEvent(Log log) {
		System.out.println(log.getMessage());
	}

	/**
	 * If plugins should be enabled on load, this enables the newly loaded
	 * plugin. Also logs the plugin being loaded.
	 *
	 * @param event the event that was received
	 */
	@EventHandler
	public void onPluginLoaded(PluginLoaded event) {
		PluginManager manager = PluginManager.getInstance();
		//TODO remove this, handle in plugin loading stage
		if (manager.getEnableOnLoad()) {
			manager.enable(event.getPlugin());
		}
		manager.logAlert("ALERT_LOADED", event.getPlugin());
	}

	/**
	 * Logs the plugin being disabled.
	 *
	 * @param event the event that was received
	 */
	@EventHandler
	public void onPluginDisabled(PluginDisabled event) {
		PluginManager.getInstance().logAlert("ALERT_DISABLED",
			event.getPlugin());
	}

	/**
	 * Logs the plugin being enabled.
	 *
	 * @param event the event that was received
	 */
	@EventHandler
	public void onPluginEnabled(PluginEnabled event) {
		PluginManager.getInstance().logAlert("ALERT_ENABLED",
			event.getPlugin());
	}

}
