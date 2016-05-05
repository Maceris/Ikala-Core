package com.ikalagaming.system;

import com.ikalagaming.event.EventHandler;
import com.ikalagaming.event.Listener;
import com.ikalagaming.logging.events.Log;

/**
 * The event listener for the package management system.
 *
 * @author Ches Burks
 *
 */
class SystemEventListener implements Listener {

	/**
	 * Logs the provided information. Attempts to use localized names for the
	 * logging level. This only logs information that is above or equal to the
	 * logging threshold. <br>
	 * If the package is not enabled, simply logs straight to System.out
	 *
	 * @param log the Event to record
	 */
	@EventHandler
	public void onLogEvent(Log log) {
		System.out.println(log.getMessage());
	}

}
