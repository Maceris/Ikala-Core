package com.ikalagaming.event;

import com.ikalagaming.util.SafeResourceLoader;

import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Holds an EventQueue and dispatches the events in order when possible.
 *
 * @author Ches Burks
 */
@Slf4j
class EventDispatcher extends Thread {

    /**
     * The number of milliseconds to wait before timing out and checking if there are more items
     * again.
     */
    private static final long WAIT_TIMEOUT = 1000;

    private final ConcurrentLinkedDeque<Event> queue;

    private EventManager eventManager;

    private boolean running;
    private boolean hasEvents;

    /** Used to handle synchronization and waiting for events */
    private final Object syncObject;

    /**
     * Creates and starts the thread. It will begin attempting to dispatch events immediately if
     * there are any available.
     *
     * @param manager the event manager that this dispatcher belongs to
     */
    public EventDispatcher(EventManager manager) {
        setName("EventDispatcher");
        queue = new ConcurrentLinkedDeque<>();
        eventManager = manager;
        hasEvents = false;
        running = true;
        syncObject = new Object();
    }

    private void dispatch(Event event) {
        if (event == null) {
            return;
        }
        if (eventManager == null) {
            log.error("There is no event manager!");
            return;
        }
        HandlerList handlers = eventManager.getHandlers(event);
        if (handlers == null) {
            return;
        }
        EventListener[] listeners = handlers.getRegisteredListeners();
        for (EventListener registration : listeners) {
            try {
                registration.callEvent(event);
            } catch (EventException e) {
                String error =
                        SafeResourceLoader.getString(
                                "DISPATCH_ERROR", EventManager.getResourceBundle());
                log.warn(error, e);
            }
        }
    }

    /**
     * Adds the {@link Event event} to the queue pending dispatch.
     *
     * @param event The event to send out
     * @throws IllegalStateException if the element cannot be added at this time due to capacity
     *     restrictions
     */
    public void dispatchEvent(Event event) throws IllegalStateException {
        if (event == null) {
            return;
        }
        try {
            synchronized (queue) {
                queue.add(event);
            }
            hasEvents = true;
        } catch (IllegalStateException illegalState) {
            throw illegalState;
        } catch (Exception e) {
            String error =
                    SafeResourceLoader.getString(
                            "DISPATCH_ERROR", EventManager.getResourceBundle());
            log.warn(error, e);
            return; // don't wake up thread
        }
        wakeUp();
    }

    private void handleEvent() {
        synchronized (queue) {
            if (queue.isEmpty()) {
                hasEvents = false;
                return;
            }
        }
        Event event;
        try {
            event = queue.remove();
        } catch (NoSuchElementException noElement) {
            // the queue is empty
            hasEvents = false;
            String error =
                    SafeResourceLoader.getString(
                            "EVT_QUEUE_EMPTY", EventManager.getResourceBundle());
            log.warn(error);
            return;
        }
        dispatch(event);
    }

    /**
     * Checks for events in the queue, and dispatches them if possible. Does not do anything if
     * {@link #terminate()} has been called.
     */
    @Override
    public void run() {
        while (running) {
            while (!hasEvents) {
                synchronized (syncObject) {
                    try {
                        // block this thread until an item is added
                        syncObject.wait(WAIT_TIMEOUT);
                    } catch (InterruptedException e) {
                        String error =
                                SafeResourceLoader.getString(
                                        "THREAD_INTERRUPTED", EventManager.getResourceBundle());
                        log.warn(error);
                        // Re-interrupt as per SonarLint java:S2142
                        Thread.currentThread().interrupt();
                    }
                }
                // in case it was terminated while waiting
                if (!running) {
                    break;
                }
            }
            if (hasEvents) {
                handleEvent();
            }
        }
        // Done running
        queue.clear();
    }

    /**
     * Stops the thread from executing its run method in preparation for shutting down the thread.
     */
    public void terminate() {
        hasEvents = false;
        running = false;
        eventManager = null;
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
