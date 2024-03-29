package com.ikalagaming.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents where in the order of receiving events a particular handler is. Based off lahwran's
 * fevents.
 */
@AllArgsConstructor
public enum Order {

    /**
     * Event is handled very early, before any other order level, to allow other handlers to modify
     * the outcome later.
     */
    EARLIEST(0),
    /** Event handling is of low importance. */
    EARLY(1),
    /** Events are handled at the default time. */
    DEFAULT(2),
    /** Event handling is of high importance. */
    LATE(3),
    /**
     * Event is handled very late, this is the last chance to modify events before they are listened
     * to purely for monitoring or logging.
     */
    LATEST(4),
    /**
     * Event is listened to purely for monitoring the outcome of an event, after any other order
     * level has processed the event.
     *
     * <p>No changes to the event should be made in handlers with this priority.
     */
    MONITOR(5);

    /**
     * Returns the numerical index of this priority, with higher numbers being later.
     *
     * @return The numerical order of this priority
     */
    @SuppressWarnings("javadoc")
    @Getter
    private final int index;
}
