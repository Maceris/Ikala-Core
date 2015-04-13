package com.ikalagaming.event;

import java.util.NoSuchElementException;

import com.ikalagaming.logging.LoggingLevel;
import com.ikalagaming.logging.events.LogError;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * Holds an EventQueue and dispatches the events in order when possible.
 *
 * @author Ches Burks
 *
 */
class EventDispatcher extends Thread {

	private IkEventQueue<Event> queue;

	private HandlerList handlers;
	private EventListener[] listeners;
	private EventManager eventManager;

	private boolean running;
	private boolean hasEvents;

	/**
	 * Creates and starts the thread. It will begin attempting to dispatch
	 * events immediately if there are any available.
	 *
	 * @param manager the event manager that this dispatcher belongs to
	 */
	public EventDispatcher(EventManager manager) {
		this.setName("EventDispatcher");
		this.queue = new IkEventQueue<>();
		this.eventManager = manager;
		this.hasEvents = false;
		this.running = true;
	}

	private void dispatch(Event event) {
		this.handlers = this.eventManager.getHandlers(event);
		if (this.handlers != null) {
			this.listeners = this.handlers.getRegisteredListeners();
			for (EventListener registration : this.listeners) {
				try {
					registration.callEvent(event);
				}
				catch (EventException e) {
					String error =
							SafeResourceLoader.getString("DISPATCH_ERROR",
									"com.ikalagaming.event.resources.strings",
									"There was a problem sending an event");
					this.eventManager.fireEvent(new LogError(error,
							LoggingLevel.WARNING, "event-manager"));
					System.err.println(e.toString());
					e.printStackTrace(System.err);
				}
			}
		}
	}

	/**
	 * Adds the {@link Event event} to the queue pending dispatch.
	 *
	 * @param event The event to send out
	 * @throws IllegalStateException if the element cannot be added at this time
	 *             due to capacity restrictions
	 */
	public void dispatchEvent(Event event) throws IllegalStateException {
		try {
			this.queue.add(event);
			this.hasEvents = true;
		}
		catch (IllegalStateException illegalState) {
			throw illegalState;
		}
		catch (NullPointerException nullPointer) {
			;// do nothing since its a null event
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private void handleEvent() {

		if (this.queue.isEmpty()) {
			this.hasEvents = false;
		}
		else if (this.queue.peek() != null) {
			Event event;
			try {
				event = this.queue.remove();
			}
			catch (NoSuchElementException noElement) {
				// the queue is empty
				this.hasEvents = false;
				System.err.println(noElement.toString());
				return;
			}
			this.dispatch(event);
		}
		else {
			return;
		}

	}

	private boolean hasEvents() {
		return this.hasEvents;
	}

	/**
	 * Checks for events in the queue, and dispatches them if possible. Does not
	 * do anything if {@link #terminate()} has been called.
	 */
	@Override
	public void run() {
		if (!this.running) {
			return;
		}
		while (this.running) {
			try {
				Thread.sleep(5);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (this.hasEvents()) {
				this.handleEvent();
			}
		}
	}

	/**
	 * Stops the thread from executing its run method in preparation for
	 * shutting down the thread.
	 */
	public void terminate() {
		this.hasEvents = false;
		this.running = false;
		this.eventManager = null;
	}
}
