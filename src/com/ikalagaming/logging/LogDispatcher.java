package com.ikalagaming.logging;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.logging.events.Log;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

/**
 * Holds an internal queue and dispatches the events in order when possible.
 *
 * @author Ches Burks
 *
 */
class LogDispatcher extends Thread {

	/**
	 * The number of milliseconds to wait before timing out and checking if
	 * there are more items again.
	 */
	private static final long WAIT_TIMEOUT = 10000;

	private ArrayDeque<String> queue;
	private boolean running;
	private boolean hasLogs;
	private EventManager manager;

	/**
	 * Used to handle synchronization and waiting for events
	 */
	private Object syncObject;

	/**
	 * Creates and starts the thread. It will begin attempting to dispatch
	 * events immediately if there are any available.
	 *
	 * @param eventManager the event manager to use in dispatching events
	 *
	 */
	public LogDispatcher(EventManager eventManager) {
		this.setName("LogDispatcher");
		this.manager = eventManager;
		this.queue = new ArrayDeque<>();
		this.hasLogs = false;
		this.running = true;
		this.syncObject = new Object();
	}

	private void handleEvent() {
		try {
			if (this.queue.isEmpty()) {
				this.hasLogs = false;
				return;
			}
			// log it to the system output stream
			Log log = new Log(this.queue.remove());
			this.manager.fireEvent(log);
		}
		catch (NoSuchElementException noElement) {
			// the queue is empty
			this.hasLogs = false;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the String to the queue pending logging.
	 *
	 * @param log The log message to record
	 * @throws IllegalStateException if the element cannot be added at this time
	 *             due to capacity restrictions
	 */
	protected void log(String log) throws IllegalStateException {
		try {
			this.queue.add(log);
			this.hasLogs = true;
		}
		catch (IllegalStateException illegalState) {
			throw illegalState;
		}
		catch (NullPointerException nullPointer) {
			// do nothing since its a null event
			return;// don't wake up thread
		}
		catch (Exception e) {
			e.printStackTrace();
			return;// don't wake up thread
		}
		this.wakeUp();
	}

	/**
	 * Checks for Strings in the queue, and logs them if possible. Does not do
	 * anything if {@link #terminate()} has been called.
	 *
	 */
	@Override
	public void run() {
		while (this.running) {
			while (!this.hasLogs) {
				synchronized (this.syncObject) {
					try {
						// block this thread until an item is added
						this.syncObject.wait(LogDispatcher.WAIT_TIMEOUT);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
						// Re-interrupt as per SonarLint java:S2142
						Thread.currentThread().interrupt();
					}
				}
				// in case it was terminated while waiting
				if (!this.running) {
					break;
				}
			}
			if (this.hasLogs) {
				this.handleEvent();
			}
		}
		this.queue.clear();
	}

	/**
	 * Stops the thread from executing its run method in preparation for
	 * shutting down the thread.
	 */
	public synchronized void terminate() {
		this.hasLogs = false;
		this.running = false;
		this.wakeUp();
	}

	/**
	 * Wakes this thread up when it is sleeping
	 */
	private void wakeUp() {
		synchronized (this.syncObject) {
			// Wake the thread up as there is now an event
			this.syncObject.notifyAll();
		}
	}
}
