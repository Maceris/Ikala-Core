
package com.ikalagaming.logging;

import java.util.LinkedList;
import java.util.NoSuchElementException;

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

	/**
	 * Creates and starts the thread. It will begin attempting to dispatch
	 * events immediately if there are any available.
	 * 
	 */
	public LogDispatcher() {
		setName("LogDispatcher");
		queue = new LinkedList<String>();
		this.hasLogs = false;
		this.running = true;
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
			queue.add(log);
			hasLogs = true;
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
		try {
			if (queue.isEmpty()) {
				hasLogs = false;
			}
			else if (queue.peek() != null) {
				currentStr = queue.remove();
				// log it to the system output stream
				System.out.println(currentStr);
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
		return hasLogs;
	}

	/**
	 * Stops the thread from executing its run method in preparation for
	 * shutting down the thread.
	 */
	public void terminate() {
		hasLogs = false;
		running = false;
	}
}
