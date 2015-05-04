package com.ikalagaming.event;

/**
 * An event for testing purposes.
 *
 * @author Ches Burks
 *
 */
class TestEvent extends Event {

	private final int value;

	/**
	 * An event for testing
	 *
	 * @param val a value for ensuring proper ordering of events
	 *
	 */
	public TestEvent(int val) {
		this.value = val;
	}

	/**
	 * Returns the value this event is assigned
	 *
	 * @return the value stored in this event
	 */
	public int getValue() {
		return this.value;
	}

}
