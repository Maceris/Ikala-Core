package com.ikalagaming.scripting;

import com.ikalagaming.scripting.interpreter.ScriptRuntime;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a a list of scripts and handles their execution.
 *
 * @author Ches Burks
 *
 */
@Slf4j
class ScriptRunner extends Thread {

	/**
	 * The number of milliseconds to wait before timing out and checking if
	 * there are more items again.
	 */
	private static final long WAIT_TIMEOUT = 1000;

	private List<ScriptRuntime> scripts;

	private boolean running;
	private boolean hasScripts;

	/**
	 * Used to handle synchronization and waiting for events
	 */
	private Object syncObject;

	/**
	 * Creates and starts the thread.
	 */
	public ScriptRunner() {
		this.setName("ScriptRunner");
		this.scripts = new ArrayList<>();
		this.hasScripts = false;
		this.running = true;
		this.syncObject = new Object();
	}

	/**
	 * Checks for events in the queue, and dispatches them if possible. Does not
	 * do anything if {@link #terminate()} has been called.
	 */
	@Override
	public void run() {
		while (this.running) {
			while (!this.hasScripts) {
				synchronized (this.syncObject) {
					try {
						// block this thread until an item is added
						this.syncObject.wait(ScriptRunner.WAIT_TIMEOUT);
					}
					catch (InterruptedException e) {
						String error =
							SafeResourceLoader.getString("THREAD_INTERRUPTED",
								ScriptManager.getResourceBundle());
						ScriptRunner.log.warn(error);
						// Re-interrupt as per SonarLint java:S2142
						Thread.currentThread().interrupt();
					}
				}
				// in case it was terminated while waiting
				if (!this.running) {
					break;
				}
			}
			if (this.hasScripts) {
				synchronized (this.scripts) {
					this.stepScripts();
				}
			}
		}
		// Done running
		this.scripts.clear();
	}

	/**
	 * Adds the script to the list of currently running scripts.
	 *
	 * @param script The script to run.
	 */
	public void runScript(@NonNull ScriptRuntime script) {
		synchronized (this.scripts) {
			this.scripts.add(script);
		}
		this.hasScripts = true;
		this.wakeUp();
	}

	/**
	 * Go through and execute one instruction for each script. Any fatal
	 * exceptions will result in the script being halted. Any scripts that is
	 * terminated, natrually or not, will be removed from the list.
	 */
	private void stepScripts() {
		for (ScriptRuntime script : this.scripts) {
			try {
				script.step();
			}
			catch (Exception e) {
				script.halt();
			}
		}
		this.scripts.removeIf(ScriptRuntime::hasTerminated);
		this.hasScripts = !this.scripts.isEmpty();
	}

	/**
	 * Stops the thread from executing its run method in preparation for
	 * shutting down the thread.
	 */
	public void terminate() {
		this.hasScripts = false;
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
