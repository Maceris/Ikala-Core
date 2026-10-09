package com.ikalagaming.scripting;

import com.ikalagaming.scripting.interpreter.ScriptRuntime;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Holds a list of scripts and handles their execution.
 *
 * @author Ches Burks
 */
@Slf4j
class ScriptRunner extends Thread {

    /**
     * The number of milliseconds to wait before timing out and checking if there are more items
     * again.
     */
    private static final long WAIT_TIMEOUT = 1000;

    /** The tag to use when not specified for halted scripts. */
    private static final String DEFAULT_TAG = "";

    private List<ScriptRuntime> scripts;

    /** Tracks requests to halt scripts. */
    private Map<ScriptRuntime, String> yieldRequests;

    /** Tracks requests to resume every script that yielded with a tag. */
    private List<Resume> resumeRequests;

    /** Tracks requests to resume one particular script. */
    private List<Delivery> deliveries;

    /** The actual scripts that are halted. */
    private Map<ScriptRuntime, String> haltedScripts;

    private volatile boolean running;

    /** Used to handle synchronization and waiting for events */
    private Object syncObject;

    /**
     * A request to resume every script waiting on a tag.
     *
     * @param tag The tag.
     * @param value The value awaits return.
     */
    private record Resume(@NonNull String tag, Object value) {}

    /**
     * A request to resume one script waiting on a tag.
     *
     * @param runtime The script.
     * @param tag The tag.
     * @param value The value its await returns.
     */
    private record Delivery(@NonNull ScriptRuntime runtime, @NonNull String tag, Object value) {}

    /** Creates and starts the thread. */
    public ScriptRunner() {
        setName("ScriptRunner");
        scripts = new ArrayList<>();
        yieldRequests = Collections.synchronizedMap(new HashMap<>());
        resumeRequests = Collections.synchronizedList(new ArrayList<>());
        deliveries = new ArrayList<>();
        haltedScripts = new HashMap<>();
        running = true;
        syncObject = new Object();
    }

    /**
     * Fetch the scripts that are currently running, not including any that are about to yield.
     *
     * @return A copy of the list of running scripts.
     */
    @Synchronized
    public List<ScriptRuntime> getRunningScripts() {
        final List<ScriptRuntime> running = new ArrayList<>(scripts);
        synchronized (yieldRequests) {
            running.removeAll(yieldRequests.keySet());
        }
        return running;
    }

    /**
     * Fetch the scripts that have yielded, including any that are about to yield.
     *
     * @return A copy of the yielded scripts, mapped to the tag they yielded with. The tag is an
     *     empty string if they yielded without a tag.
     */
    @Synchronized
    public Map<ScriptRuntime, String> getYieldedScripts() {
        final Map<ScriptRuntime, String> yielded = new LinkedHashMap<>(haltedScripts);
        synchronized (yieldRequests) {
            yielded.putAll(yieldRequests);
        }
        return yielded;
    }

    /**
     * Stop a script, whether it is running or yielded. It will not be resumed.
     *
     * @param runtime The script to stop.
     * @return True if the script was running or yielded, false if we didn't know about it.
     */
    @Synchronized
    public boolean terminate(@NonNull ScriptRuntime runtime) {
        boolean found = scripts.remove(runtime);
        found |= haltedScripts.remove(runtime) != null;
        found |= yieldRequests.remove(runtime) != null;
        runtime.halt();
        return found;
    }

    /**
     * Stop every script a plugin owns, and drop everything they hold so they don't keep the
     * plugin's objects alive.
     *
     * @param owner The plugin.
     * @return The number of scripts that were stopped.
     */
    @Synchronized
    public int terminateAllOwnedBy(@NonNull String owner) {
        final Set<ScriptRuntime> owned = Collections.newSetFromMap(new IdentityHashMap<>());
        for (ScriptRuntime runtime : scripts) {
            if (owner.equals(runtime.getOwner())) {
                owned.add(runtime);
            }
        }
        for (ScriptRuntime runtime : haltedScripts.keySet()) {
            if (owner.equals(runtime.getOwner())) {
                owned.add(runtime);
            }
        }
        // Every use of the yield requests holds this object's lock
        for (ScriptRuntime runtime : yieldRequests.keySet()) {
            if (owner.equals(runtime.getOwner())) {
                owned.add(runtime);
            }
        }
        for (ScriptRuntime runtime : owned) {
            scripts.remove(runtime);
            haltedScripts.remove(runtime);
            yieldRequests.remove(runtime);
            runtime.release();
        }
        deliveries.removeIf(delivery -> owned.contains(delivery.runtime()));
        return owned.size();
    }

    /** Halt any scripts as required. */
    @Synchronized
    private void haltScripts() {
        for (var entry : yieldRequests.entrySet()) {
            this.haltedScripts.put(entry.getKey(), entry.getValue());
            this.scripts.remove(entry.getKey());
        }
        this.yieldRequests.clear();
    }

    /** Request that we resume any scripts halted without a tag. */
    @Synchronized
    public void requestResume() {
        this.resumeRequests.add(new Resume(ScriptRunner.DEFAULT_TAG, null));
        this.wakeUp();
    }

    /**
     * Request that we resume any scripts halted using the given tag.
     *
     * @param tag The tag to resume.
     */
    @Synchronized
    public void requestResume(@NonNull String tag) {
        requestResume(tag, null);
    }

    /**
     * Request that we resume any scripts halted using the given tag, with a value for any of them
     * that are awaiting.
     *
     * @param tag The tag to resume.
     * @param value The value their awaits return.
     */
    @Synchronized
    public void requestResume(@NonNull String tag, Object value) {
        this.resumeRequests.add(new Resume(tag, value));
        this.wakeUp();
    }

    /**
     * Request that we resume one script waiting on the tag, with a value for its await. If it isn't
     * waiting on the tag yet, the value is kept for it, and its await on the tag returns straight
     * away.
     *
     * <p>Scripts only yield while they are being stepped, which holds the same lock as this, so a
     * script is either already waiting or will find the kept value.
     *
     * @param runtime The script.
     * @param tag The tag.
     * @param value The value its await returns.
     */
    @Synchronized
    public void requestResume(@NonNull ScriptRuntime runtime, @NonNull String tag, Object value) {
        if (tag.equals(waitingTag(runtime))) {
            this.deliveries.add(new Delivery(runtime, tag, value));
            this.wakeUp();
        } else {
            runtime.post(tag, value);
        }
    }

    /**
     * The tag a script is waiting on, including if it is about to yield.
     *
     * @param runtime The script.
     * @return The tag, or null if it isn't waiting.
     */
    private String waitingTag(@NonNull ScriptRuntime runtime) {
        final String halted = haltedScripts.get(runtime);
        return halted != null ? halted : yieldRequests.get(runtime);
    }

    /**
     * Request that we halt the given script without a tag. These will be resumed by {@link
     * #requestResume()}.
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
    public void requestYield(@NonNull ScriptRuntime runtime, @NonNull String tag) {
        this.yieldRequests.put(runtime, tag);
    }

    /** Resume any scripts as required. */
    @Synchronized
    private void resumeScripts() {
        // Every use of the resume requests holds this object's lock
        for (Resume request : resumeRequests) {
            List<ScriptRuntime> toResume =
                    this.haltedScripts.entrySet().stream()
                            .filter(entry -> entry.getValue().equals(request.tag()))
                            .map(Entry::getKey)
                            .collect(Collectors.toCollection(ArrayList::new));
            for (ScriptRuntime runtime : toResume) {
                this.haltedScripts.remove(runtime);
                runtime.resumeWith(request.value());
                this.scripts.add(runtime);
            }
        }
        this.resumeRequests.clear();
        for (Delivery delivery : deliveries) {
            final ScriptRuntime runtime = delivery.runtime();
            if (delivery.tag().equals(haltedScripts.get(runtime))) {
                this.haltedScripts.remove(runtime);
                runtime.resumeWith(delivery.value());
                this.scripts.add(runtime);
            } else {
                // Resumed some other way first, so keep the value for its next await on the tag
                runtime.post(delivery.tag(), delivery.value());
            }
        }
        this.deliveries.clear();
    }

    /**
     * Checks for events in the queue, and dispatches them if possible. Does not do anything if
     * {@link #terminate()} has been called.
     */
    @Override
    public void run() {
        while (running) {
            while (scripts.isEmpty()) {
                synchronized (syncObject) {
                    try {
                        // block this thread until an item is added
                        syncObject.wait(ScriptRunner.WAIT_TIMEOUT);
                    } catch (InterruptedException e) {
                        log.warn(
                                SafeResourceLoader.getString(
                                        "THREAD_INTERRUPTED", ScriptManager.getResourceBundle()));
                        // Re-interrupt as per SonarLint java:S2142
                        Thread.currentThread().interrupt();
                    }
                }
                // in case it was terminated while waiting
                if (!running) {
                    break;
                }
                resumeScripts();
            }
            if (!scripts.isEmpty()) {
                stepScripts();
            }
            haltScripts();
            resumeScripts();
        }
        // Done running
        scripts.clear();
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
     * Go through and execute one instruction for each script. Any fatal exceptions will result in
     * the script being halted. Any scripts that is terminated, naturally or not, will be removed
     * from the list.
     */
    @Synchronized
    private void stepScripts() {
        for (ScriptRuntime script : this.scripts) {
            try {
                script.step();
            } catch (Exception e) {
                script.halt();
                log.warn(
                        SafeResourceLoader.getString(
                                "EXCEPTION_IN_RUNTIME", ScriptManager.getResourceBundle()),
                        e);
            }
        }
        this.scripts.removeIf(ScriptRuntime::hasTerminated);
    }

    /**
     * Stops the thread from executing its run method in preparation for shutting down the thread.
     */
    public void terminate() {
        running = false;
        wakeUp();
    }

    /** Wakes this thread up when it is sleeping */
    private void wakeUp() {
        synchronized (syncObject) {
            // Wake the thread up as there is now an event
            syncObject.notifyAll();
        }
    }
}
