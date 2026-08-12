package com.ikalagaming.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** A listener that handles commands. */
@AllArgsConstructor
class CommandListener {
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
     * Calls the command handler.
     *
     * @param command The command to execute.
     * @throws EventException If an exception occurs during execution.
     */
    public void callHandler(final Command command) throws EventException {
        if (command.isCanceled()) {
            return;
        }
        executor.execute(listener, command);
    }
}
