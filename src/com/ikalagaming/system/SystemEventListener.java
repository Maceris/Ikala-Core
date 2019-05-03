package com.ikalagaming.system;

import com.ikalagaming.event.EventHandler;
import com.ikalagaming.event.Listener;
import com.ikalagaming.logging.Logging;
import com.ikalagaming.logging.events.Log;
import com.ikalagaming.plugins.PluginInfo;
import com.ikalagaming.plugins.PluginManager;
import com.ikalagaming.plugins.events.PluginLoaded;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * The event listener for the plugin management system.
 *
 * @author Ches Burks
 *
 */
class SystemEventListener implements Listener {

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
		if (manager.enableOnLoad()) {
			manager.enable(event.getPlugin());
		}
		PluginInfo pInfo = manager.getInfo(event.getPlugin()).get();

		String message = SafeResourceLoader
			.getString("ALERT_LOADED", manager.getResourceBundle())
			.replaceFirst("\\$PLUGIN", pInfo.getName())
			.replaceFirst("\\$VERSION", pInfo.getVersion());
		Logging.fine(SystemPlugin.PLUGIN_NAME, message);
	}

}
