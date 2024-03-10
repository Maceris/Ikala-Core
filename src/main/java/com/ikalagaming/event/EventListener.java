package com.ikalagaming.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** A listener that handles events. Based off lahwran's fevents. */
@AllArgsConstructor
class EventListener {
    /**
     * The class that contains event handlers.
     *
     * @param listener The listener to be notified of events.
     * @return The listener to be called.
     */
    @Getter private final Listener listener;

    /**
     * The callback to execute for this listener.
     *
     * @param executor The executor for events.
     */
    private final EventExecutor executor;

    /**
     * The order that this listener will be called relative to others.
     *
     * @param order The order relative to other listeners.
     * @return The order of this listener.
     * @see Order
     */
    @Getter private final Order order;

    /**
     * Calls the event executor.
     *
     * @param event The event to execute
     * @throws EventException If an exception occurs during execution
     */
    public void callEvent(final Event event) throws EventException {
        this.executor.execute(this.listener, event);
    }
}
