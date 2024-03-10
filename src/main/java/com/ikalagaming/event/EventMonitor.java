package com.ikalagaming.event;

import lombok.Getter;
import lombok.Synchronized;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to listen for events of a specific type, to see if those events were fired.
 *
 * @author Ches Burks
 * @param <T> The type of event we are listening for.
 */
class EventMonitor<T extends Event> implements Listener {
    private List<T> events;

    /**
     * The type of event we are tracking.
     *
     * @return The event class we are listening for.
     */
    @SuppressWarnings("javadoc")
    @Getter
    private Class<T> eventType;

    private int hitCount = 0;

    /**
     * Create an event type for the given class.
     *
     * @param eventType The event class we are listening for.
     */
    public EventMonitor(Class<T> eventType) {
        this.eventType = eventType;
        events = new ArrayList<>();
    }

    /**
     * Returns a boolean for if the event has been received or not.
     *
     * @return True if the event was received, false if it never was.
     */
    @Synchronized
    public boolean eventReceived() {
        return hitCount > 0;
    }

    /**
     * Return the number of times the event was received.
     *
     * @return The total number of times the event was received.
     */
    @Synchronized
    public int getHitCount() {
        return this.hitCount;
    }

    /**
     * Returns all the events that have been recorded since we started listening or last reset.
     *
     * @return A copy of the list of events.
     */
    public List<T> getRecordedEvents() {
        return new ArrayList<>(events);
    }

    /**
     * Track the fact that the event was received.
     *
     * @param event The event that is fired.
     */
    @Synchronized
    public void onEvent(T event) {
        ++hitCount;
        this.events.add(event);
    }

    /** Reset the hit count to 0, as if the event was never received. */
    @Synchronized
    public void resetHitCount() {
        this.hitCount = 0;
        this.events.clear();
    }
}
