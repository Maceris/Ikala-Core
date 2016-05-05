package com.ikalagaming.logging;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.logging.events.Log;

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
	private String currentStr;
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
			this.currentStr = this.queue.remove();
			// log it to the system output stream
			Log log = new Log(this.currentStr);
			this.manager.fireEvent(log);
		}
		catch (NoSuchElementException noElement) {
			// the queue is empty
			this.hasLogs = false;
			return;
		}
		catch (Exception e) {
			System.err.println(e.toString());
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
			;// do nothing since its a null event
			return;// don't wake up thread
		}
		catch (Exception e) {
			System.err.println(e.toString());
			return;// don't wake up thread
		}
		wakeUp();
	}

	/**
	 * Wakes this thread up when it is sleeping
	 */
	private void wakeUp() {
		synchronized (this.syncObject) {
			// Wake the thread up as there is now an event
			this.syncObject.notify();
		}
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
						this.wait(LogDispatcher.WAIT_TIMEOUT);
					}
					catch (InterruptedException e) {
						// TODO log this
						e.printStackTrace(System.err);
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
		wakeUp();
	}
}
