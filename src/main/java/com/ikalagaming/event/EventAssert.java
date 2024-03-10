package com.ikalagaming.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Tools for testing if events have been fired for use in test suites. This creates listeners as if
 * a normal event listener has been scheduled, so it tests if event listeners should have received
 * events, not just if the event manager tried to fire an event.
 *
 * @author Ches Burks
 */
public class EventAssert {
    private static Map<Class<? extends Event>, EventMonitor<?>> monitors = new HashMap<>();

    /**
     * Returns the number of times the given event class has been recorded.
     *
     * @param <T> The type of event.
     * @param eventClass The event class we want a count of.
     * @return The number of times since reset or initial listening the event has been received.
     * @see #resetFireCount(Class)
     */
    public static <T extends Event> int getFireCount(Class<T> eventClass) {
        if (!EventAssert.monitors.containsKey(eventClass)) {
            return 0;
        }
        return EventAssert.monitors.get(eventClass).getHitCount();
    }

    /**
     * Returns a list of events that have been recorded since the listening has started, or was last
     * reset. If no events were received, or we were not listening to the event, an empty list will
     * be returned. The returned list is a copy and can be freely modified.
     *
     * @param <T> The type of event.
     * @param eventClass The event class we want events from.
     * @return A copy of the list of event received since we started recording.
     * @see #resetFireCount(Class)
     * @see #getFireCount(Class)
     */
    public static <T extends Event> List<T> getRecordedEvents(Class<T> eventClass) {

        if (!EventAssert.monitors.containsKey(eventClass)) {
            return new ArrayList<>();
        }
        // we only store a monitor by key T if it's a monitor of type T
        @SuppressWarnings("unchecked")
        EventMonitor<T> monitor = (EventMonitor<T>) EventAssert.monitors.get(eventClass);
        return monitor.getRecordedEvents();
    }

    /**
     * Listen for a specific type of event.
     *
     * @param <T> The type of event we are trying to listen for.
     * @param eventClass The class of the event we want to listen for.
     */
    public static <T extends Event> void listenFor(Class<T> eventClass) {
        EventMonitor<T> monitor = new EventMonitor<>(eventClass);
        EventManager.getInstance().registerEventListeners(monitor);
        if (EventAssert.monitors.containsKey(eventClass)) {
            EventAssert.stopListeningFor(eventClass);
        }
        EventAssert.monitors.put(eventClass, monitor);
    }

    /**
     * Resets the number of times the given event class has been recorded, and also clears the list
     * of events that have been recorded for that event type.
     *
     * @param <T> The type of event.
     * @param eventClass The event class we are resetting.
     */
    public static <T extends Event> void resetFireCount(Class<T> eventClass) {
        if (!EventAssert.monitors.containsKey(eventClass)) {
            return;
        }
        EventAssert.monitors.get(eventClass).resetHitCount();
    }

    /**
     * Stop listening for a specific type of events.
     *
     * @param <T> The type of event we were listening for.
     * @param eventClass The event class we were listening for.
     */
    public static <T extends Event> void stopListeningFor(Class<T> eventClass) {
        if (!EventAssert.monitors.containsKey(eventClass)) {
            return;
        }
        EventManager.getInstance()
                .unregisterEventListeners(EventAssert.monitors.remove(eventClass));
    }

    /** Stop listening for all events and clean up resources. */
    public static void stopListeningForEverything() {
        for (Entry<Class<? extends Event>, EventMonitor<?>> entry :
                EventAssert.monitors.entrySet()) {
            EventManager.getInstance().unregisterEventListeners(entry.getValue());
            entry.getValue().resetHitCount();
        }
        EventAssert.monitors.clear();
    }

    /**
     * Checks if the given event class has been fired since we started listening for it, or since
     * the last time we reset the count.
     *
     * @param <T> The type of event we are looking for.
     * @param eventClass The event class we want to check.
     * @return True if the event was fired since we started listening or reset, False if we are not
     *     tracking it or it was not fired.\
     * @see #resetFireCount(Class)
     */
    public static <T extends Event> boolean wasFired(Class<T> eventClass) {
        if (!EventAssert.monitors.containsKey(eventClass)) {
            return false;
        }

        // we only store a monitor by key T if it's a monitor of type T
        @SuppressWarnings("unchecked")
        EventMonitor<T> monitor = (EventMonitor<T>) EventAssert.monitors.get(eventClass);
        return monitor.eventReceived();
    }

    /**
     * Checks if the given event class has been fired since we started listening for it, or since
     * the last time we reset the count. Causes the thread that calls this to sleep, and check every
     * second until the event was fired or we reach the given wait timeout. If the timeout is less
     * than a second, then that's how long it will wait. Defaults to a 10 ms poll time.
     *
     * @param <T> The type of event we are looking for.
     * @param eventClass The event class we want to check.
     * @param waitTimeout The longest time we will wait for the event to have been fired, in
     *     milliseconds.
     * @return True if the event was fired since we started listening or reset, False if we are not
     *     tracking it or it was not fired.
     * @see #wasFired(Class)
     * @see #resetFireCount(Class)
     * @see #wasFired(Class, long, long)
     */
    public static <T extends Event> boolean wasFired(Class<T> eventClass, long waitTimeout) {
        return EventAssert.wasFired(eventClass, waitTimeout, 10);
    }

    /**
     * Checks if the given event class has been fired since we started listening for it, or since
     * the last time we reset the count. Causes the thread that calls this to sleep, and check every
     * second until the event was fired or we reach the given wait timeout. If the timeout is less
     * than a second, then that's how long it will wait.
     *
     * @param <T> The type of event we are looking for.
     * @param eventClass The event class we want to check.
     * @param waitTimeout The longest time we will wait for the event to have been fired, in
     *     milliseconds.
     * @param pollTime How many milliseconds to wait between checking if the event has been fired.
     *     If less than 1, will be treated as 1.
     * @return True if the event was fired since we started listening or reset, False if we are not
     *     tracking it or it was not fired.
     * @see #wasFired(Class)
     * @see #resetFireCount(Class)
     * @see #wasFired(Class, long)
     */
    public static <T extends Event> boolean wasFired(
            Class<T> eventClass, long waitTimeout, long pollTime) {
        if (!EventAssert.monitors.containsKey(eventClass)) {
            return false;
        }
        if (waitTimeout < 0) {
            return EventAssert.wasFired(eventClass);
        }

        final long poll = pollTime < 1 ? 1 : pollTime;

        if (EventAssert.wasFired(eventClass)) {
            return true;
        }

        long nextWait = waitTimeout > poll ? poll : waitTimeout;
        long totalWait = 0;

        while (totalWait < waitTimeout) {
            try {
                Thread.sleep(nextWait);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (EventAssert.wasFired(eventClass)) {
                return true;
            }
            totalWait += nextWait;
            nextWait = waitTimeout > poll ? poll : waitTimeout;
        }
        return EventAssert.wasFired(eventClass);
    }

    /** Private constructor so this class is not initialized. */
    private EventAssert() {}
}
