package com.ikalagaming.scripting;

import com.ikalagaming.scripting.interpreter.ScriptRuntime;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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

	/**
	 * The tag to use when not specified for halted scripts.
	 */
	private static final String DEFAULT_TAG = "";

	private List<ScriptRuntime> scripts;

	/**
	 * Tracks requests to halt scripts.
	 */
	private Map<ScriptRuntime, String> yieldRequests;

	/**
	 * Tracks requests to resume scripts.
	 */
	private List<String> resumeRequests;

	/**
	 * The actual scripts that are halted.
	 */
	private Map<ScriptRuntime, String> haltedScripts;

	private boolean running;

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
		this.yieldRequests = Collections.synchronizedMap(new HashMap<>());
		this.resumeRequests = Collections.synchronizedList(new ArrayList<>());
		this.haltedScripts = new HashMap<>();
		this.running = true;
		this.syncObject = new Object();
	}

	/**
	 * Halt any scripts as required.
	 */
	@Synchronized
	private void haltScripts() {
		for (var entry : yieldRequests.entrySet()) {
			this.haltedScripts.put(entry.getKey(), entry.getValue());
			this.scripts.remove(entry.getKey());
		}
		this.yieldRequests.clear();
	}

	/**
	 * Request that we resume any scripts halted without a tag.
	 */
	@Synchronized
	public void requestResume() {
		this.resumeRequests.add(ScriptRunner.DEFAULT_TAG);
		this.wakeUp();
	}

	/**
	 * Request that we resume any scripts halted using the given tag.
	 *
	 * @param tag The tag to resume.
	 */
	@Synchronized
	public void requestResume(@NonNull String tag) {
		this.resumeRequests.add(tag);
		this.wakeUp();
	}

	/**
	 * Request that we halt the given script without a tag. These will be
	 * resumed by {@link #requestResume()}.
	 *
	 * @param runtime The runtime to halt.
	 * @see #requestYield(ScriptRuntime, String)
	 * @see #requestResume()
	 */
	@Synchronized
	public void requestYield(@NonNull ScriptRuntime runtime) {
		this.yieldRequests.put(runtime, ScriptRunner.DEFAULT_TAG);
	}

	/**
	 * Request that we halt the given script, we resume using the same tag.
	 *
	 * @param runtime The runtime to halt.
	 * @param tag The tag that will be used to resume.
	 * @see #requestYield(ScriptRuntime)
	 * @see #requestResume(String)
	 */
	@Synchronized
	public void requestYield(@NonNull ScriptRuntime runtime,
		@NonNull String tag) {
		this.yieldRequests.put(runtime, tag);
	}

	/**
	 * Resume any scripts as required.
	 */
	@Synchronized
	private void resumeScripts() {
		for (String tag : resumeRequests) {
			List<ScriptRuntime> toResume = this.haltedScripts.entrySet()
				.stream().filter(entry -> entry.getValue().equals(tag))
				.map(Entry::getKey)
				.collect(Collectors.toCollection(ArrayList::new));
			this.scripts.addAll(toResume);
			toResume.forEach(this.haltedScripts::remove);
		}
		this.resumeRequests.clear();
	}

	/**
	 * Checks for events in the queue, and dispatches them if possible. Does not
	 * do anything if {@link #terminate()} has been called.
	 */
	@Override
	public void run() {
		while (this.running) {
			while (this.scripts.isEmpty()) {
				synchronized (this.syncObject) {
					try {
						// block this thread until an item is added
						this.syncObject.wait(ScriptRunner.WAIT_TIMEOUT);
					}
					catch (InterruptedException e) {
						ScriptRunner.log.warn(
							SafeResourceLoader.getString("THREAD_INTERRUPTED",
								ScriptManager.getResourceBundle()));
						// Re-interrupt as per SonarLint java:S2142
						Thread.currentThread().interrupt();
					}
				}
				// in case it was terminated while waiting
				if (!this.running) {
					break;
				}
				this.resumeScripts();
			}
			if (!this.scripts.isEmpty()) {
				this.stepScripts();
			}
			this.haltScripts();
			this.resumeScripts();
		}
		// Done running
		this.scripts.clear();
	}

	/**
	 * Adds the script to the list of currently running scripts.
	 *
	 * @param script The script to run.
	 */
	@Synchronized
	public void runScript(@NonNull ScriptRuntime script) {
		this.scripts.add(script);
		this.wakeUp();
	}

	/**
	 * Go through and execute one instruction for each script. Any fatal
	 * exceptions will result in the script being halted. Any scripts that is
	 * terminated, naturally or not, will be removed from the list.
	 */
	@Synchronized
	private void stepScripts() {
		for (ScriptRuntime script : this.scripts) {
			try {
				script.step();
			}
			catch (Exception e) {
				script.halt();
				ScriptRunner.log
					.warn(SafeResourceLoader.getString("EXCEPTION_IN_RUNTIME",
						ScriptManager.getResourceBundle()), e);
			}
		}
		this.scripts.removeIf(ScriptRuntime::hasTerminated);
	}

	/**
	 * Stops the thread from executing its run method in preparation for
	 * shutting down the thread.
	 */
	public void terminate() {
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
