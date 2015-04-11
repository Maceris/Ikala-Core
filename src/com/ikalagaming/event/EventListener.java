package com.ikalagaming.event;

/**
 * A listener that handles events.
 */
public class EventListener {
	private final Listener theListener;
	private final EventExecutor theExecutor;

	/**
	 * Constructs a new {@link EventListener}.
	 *
	 * @param listener The listener to call
	 * @param executor The executor for the events
	 */
	public EventListener(final Listener listener, final EventExecutor executor) {
		this.theListener = listener;
		this.theExecutor = executor;
	}

	/**
	 * Calls the event executor.
	 *
	 * @param event The event to execute
	 * @throws EventException If an exception occurs during execution
	 */
	public void callEvent(final Event event) throws EventException {
		this.theExecutor.execute(this.theListener, event);
	}

	/**
	 * Returns the {@link Listener listener}.
	 *
	 * @return The listener
	 */
	public Listener getListener() {
		return this.theListener;
	}

}
