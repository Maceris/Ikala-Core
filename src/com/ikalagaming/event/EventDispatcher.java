
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
public class EventDispatcher extends Thread {

	private IkEventQueue<Event> queue;

	private HandlerList handlers;
	private EventListener[] listeners;
	private EventManager manager;

	private boolean running;
	private boolean hasEvents;

	/**
	 * Creates and starts the thread. It will begin attempting to dispatch
	 * events immediately if there are any available.
	 * 
	 * @param manager the event manager that this dispatcher belongs to
	 */
	public EventDispatcher(EventManager manager) {
		setName("EventDispatcher");
		queue = new IkEventQueue<Event>();
		this.manager = manager;
		this.hasEvents = false;
		this.running = true;
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
			queue.add(event);
			hasEvents = true;
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

	/**
	 * Checks for events in the queue, and dispatches them if possible. Does not
	 * do anything if {@link #terminate()} has been called.
	 */
	@Override
	public void run() {
		if (!running) {
			return;
		}
		while (running) {
			try {
				sleep(5);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (hasEvents()) {
				handleEvent();
			}
		}
	}

	private void handleEvent() {

		if (queue.isEmpty()) {
			hasEvents = false;
		}
		else if (queue.peek() != null) {
			Event event;
			try {
				event = queue.remove();
			}
			catch (NoSuchElementException noElement) {
				// the queue is empty
				hasEvents = false;
				System.err.println(noElement.toString());
				return;
			}
			dispatch(event);
		}
		else {
			return;
		}

	}

	private void dispatch(Event event) {
		handlers = manager.getHandlers(event);
		if (handlers != null) {
			listeners = handlers.getRegisteredListeners();
			for (EventListener registration : listeners) {
				try {
					registration.callEvent(event);
				}
				catch (EventException e) {
					String error =
							SafeResourceLoader.getString("DISPATCH_ERROR",
									"com.ikalagaming.event.resources.strings",
									"There was a problem sending an event");
					manager.fireEvent(new LogError(error, LoggingLevel.WARNING,
							"event-manager"));
					System.err.println(e.toString());
					e.printStackTrace(System.err);
				}
			}
		}
	}

	private boolean hasEvents() {
		return hasEvents;
	}

	/**
	 * Stops the thread from executing its run method in preparation for
	 * shutting down the thread.
	 */
	public void terminate() {
		hasEvents = false;
		running = false;
		manager = null;
	}
}
