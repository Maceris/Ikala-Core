package com.ikalagaming.event;

/**
 * An abstract event to be extended. These should always be passive
 * observations, e.g. "X happened" not "do X". Based off lahwran's fevents.
 */
public abstract class Event {

	/**
	 * Fires this event using the static instance of the {@link EventManager}.
	 *
	 * For example: <code>new CustomEvent("That was easy").fire();</code>
	 */
	public void fire() {
		EventManager.getInstance().fireEvent(this);
	}

}
