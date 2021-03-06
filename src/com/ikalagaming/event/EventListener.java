package com.ikalagaming.event;

/**
 * A listener that handles events. Based off lahwran's fevents.
 */
class EventListener {
	private final Listener theListener;
	private final EventExecutor theExecutor;
	private final Order thePriority;

	/**
	 * Constructs a new {@link EventListener}.
	 *
	 * @param listener The listener to call
	 * @param executor The executor for the events
	 * @param priority The priority of the listener
	 */
	public EventListener(final Listener listener, final EventExecutor executor,
			final Order priority) {
		this.theListener = listener;
		this.theExecutor = executor;
		this.thePriority = priority;
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

	/**
	 * Returns the priority for this listener
	 *
	 * @return The registered Priority
	 */
	public Order getPriority() {
		return this.thePriority;
	}

}
