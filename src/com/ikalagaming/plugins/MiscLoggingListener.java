package com.ikalagaming.plugins;

import com.ikalagaming.event.EventHandler;
import com.ikalagaming.event.Listener;
import com.ikalagaming.logging.events.Log;

/**
 * The event listener for the plugin management system.
 *
 * @author Ches Burks
 *
 */
class MiscLoggingListener implements Listener {

	/**
	 * Prints out logs to the standard output stream.
	 *
	 * @param log the Event to record
	 */
	@EventHandler
	public void onLogEvent(Log log) {
		/**
		 * This is the logger implementation for now.
		 */
		System.out.println(log.getMessage());// NOSONAR
	}

}
