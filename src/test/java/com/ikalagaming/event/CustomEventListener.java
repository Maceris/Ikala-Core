package com.ikalagaming.event;

import lombok.Getter;

/**
 * An event listener for unit testing events being fired.
 *
 * @author Ches Burks
 *
 */
public class CustomEventListener implements Listener {

	/**
	 * Checks if the event was received.
	 *
	 * @return If the event listener got called.
	 */
	@Getter
	private boolean eventReceived = false;

	/**
	 * Data that was pulled from the test event.
	 *
	 * @return The recorded data from the event.
	 */
	@Getter
	private String recordedData;

	/**
	 * Resets as if nothing has been recorded.
	 */
	public void clear() {
		this.eventReceived = false;
		this.recordedData = "";
	}

	@EventHandler
	private void listener(CustomEvent event) {
		this.eventReceived = true;
		this.recordedData = event.getTestString();
	}
}
