package com.ikalagaming.logging;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.logging.events.DisplayLog;

/**
 * Holds an internal queue and dispatches the events in order when possible.
 *
 * @author Ches Burks
 *
 */
public class LogDispatcher extends Thread {

	private LinkedList<String> queue;
	private String currentStr;
	private boolean running;
	private boolean hasLogs;
	private EventManager manager;

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
		this.queue = new LinkedList<>();
		this.hasLogs = false;
		this.running = true;
	}

	private void handleEvent() {
		try {
			if (this.queue.isEmpty()) {
				this.hasLogs = false;
			}
			else if (this.queue.peek() != null) {
				this.currentStr = this.queue.remove();
				// log it to the system output stream
				DisplayLog log = new DisplayLog(this.currentStr);
				this.manager.fireEvent(log);
				System.out.println(this.currentStr);
			}
			else {
				return;
			}
		}
		catch (NoSuchElementException noElement) {
			// the queue is empty
			// hasEvents = false;
			return;
		}
		catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	private boolean hasEvents() {
		return this.hasLogs;
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
		}
		catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * Checks for Strings in the queue, and logs them if possible. Does not do
	 * anything if {@link #terminate()} has been called.
	 *
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
		this.hasLogs = false;
		this.running = false;
	}
}
