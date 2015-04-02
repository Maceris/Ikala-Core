
package event;

import com.ikalagaming.event.Event;

/**
 * An event for testing purposes.
 * 
 * @author Ches Burks
 * 
 */
public class TestEvent extends Event {

	private final int i;

	/**
	 * An event for testing
	 * 
	 * @param i a value for ensuring proper ordering of events
	 * 
	 */
	public TestEvent(int i) {
		this.i = i;
	}

	/**
	 * Returns the value this event is assigned
	 * 
	 * @return the value stored in this event
	 */
	public int getValue() {
		return i;
	}

}
