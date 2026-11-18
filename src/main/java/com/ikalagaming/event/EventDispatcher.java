package com.ikalagaming.event;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Holds the queue of events and commands, and dispatches them in the order they were fired.
 *
 * @author Ches Burks
 */
@Slf4j
class EventDispatcher extends Thread {

    private static final String EVENT_EXCEPTION = "Exception while dispatching event";
    private static final String EVENT_MANAGER_MISSING = "There is no event manager!";

    /**
     * Something waiting to be dispatched.
     *
     * @param event The event or command, or null for {@link #STOP}.
     * @param command Whether it was fired as a command, so its command handler runs. A command
     *     fired as an event only reaches event listeners.
     */
    private record Pending(Event event, boolean command) {}

    /** Put on the queue to wake the thread when it should stop. */
    private static final Pending STOP = new Pending(null, false);

    /** Events and commands waiting to be dispatched, and possibly {@link #STOP}. */
    private final BlockingQueue<Pending> queue = new LinkedBlockingQueue<>();

    /** The manager that knows the listeners, or null once terminated. */
    private volatile EventManager eventManager;

    /** Whether to keep dispatching. */
    private volatile boolean running;

    /**
     * Creates the thread. Once started, it dispatches events as they are fired.
     *
     * @param manager the event manager that this dispatcher belongs to
     */
    public EventDispatcher(EventManager manager) {
        setName("EventDispatcher");
        eventManager = manager;
        running = true;
    }

    private void actuallyDispatchCommand(Command command) {
        final EventManager manager = eventManager;
        if (manager == null) {
            log.error(EVENT_MANAGER_MISSING);
            return;
        }
        HandlerList handlers = manager.getEventHandlers(command);
        final boolean hasEventHandlers = handlers != null;
        EventListener[] listeners = null;

        // Call any listeners below monitor
        if (hasEventHandlers) {
            listeners = handlers.getRegisteredListeners();
            for (EventListener registration : listeners) {
                if (registration.getOrder().getIndex() < Order.MONITOR.getIndex()) {
                    try {
                        registration.callEvent(command);
                    } catch (EventException e) {
                        log.error(EVENT_EXCEPTION, e);
                    }
                }
            }
        }

        // Handle the command
        CommandListener commandHandler = manager.getCommandHandler(command);
        if (commandHandler == null) {
            log.warn("No command handler registered for {}", command.getClass());
        } else {
            try {
                commandHandler.callHandler(command);
            } catch (EventException e) {
                log.error("Exception while dispatching command", e);
            }
        }

        // Now call monitor listeners
        if (hasEventHandlers) {
            for (EventListener registration : listeners) {
                if (registration.getOrder().getIndex() == Order.MONITOR.getIndex()) {
                    try {
                        registration.callEvent(command);
                    } catch (EventException e) {
                        log.error(EVENT_EXCEPTION, e);
                    }
                }
            }
        }
    }

    private void actuallyDispatchEvent(Event event) {
        final EventManager manager = eventManager;
        if (manager == null) {
            log.error(EVENT_MANAGER_MISSING);
            return;
        }
        HandlerList handlers = manager.getEventHandlers(event);
        if (handlers == null) {
            return;
        }
        EventListener[] listeners = handlers.getRegisteredListeners();
        for (EventListener registration : listeners) {
            try {
                registration.callEvent(event);
            } catch (EventException e) {
                log.error("There was a problem dispatching events", e);
            }
        }
    }

    /**
     * Adds the {@link Command command} to the queue pending dispatch.
     *
     * @param command The command to send out
     * @throws IllegalStateException if the element cannot be added at this time due to capacity
     *     restrictions
     */
    public void dispatchCommand(Command command) throws IllegalStateException {
        if (command != null && running) {
            queue.add(new Pending(command, true));
        }
    }

    /**
     * Adds the {@link Event event} to the queue pending dispatch. Should not be used for {@link
     * Command commands}, as they'll be dispatched to any relevant event handlers anyway and this
     * would not call the command handler.
     *
     * @param event The event to send out
     * @throws IllegalStateException if the element cannot be added at this time due to capacity
     *     restrictions
     */
    public void dispatchEvent(Event event) throws IllegalStateException {
        if (event != null && running) {
            queue.add(new Pending(event, false));
        }
    }

    /**
     * Dispatch one item from the queue.
     *
     * @param item A command or an event.
     */
    private void dispatch(Pending item) {
        try {
            if (item.command()) {
                actuallyDispatchCommand((Command) item.event());
            } else {
                actuallyDispatchEvent(item.event());
            }
        } catch (RuntimeException e) {
            // A listener list or handler lookup failing must not stop every later event
            log.error(EVENT_EXCEPTION, e);
        }
    }

    /**
     * Dispatches events and commands as they arrive, until {@link #terminate()} is called.
     * Interrupts don't stop it; only terminating does.
     */
    @Override
    public void run() {
        try {
            while (running) {
                final Pending item;
                try {
                    item = queue.take();
                } catch (InterruptedException e) {
                    // Only terminate() stops dispatching; carry on unless it was called
                    continue;
                }
                if (item == STOP || !running) {
                    break;
                }
                dispatch(item);
            }
        } catch (Error e) {
            log.error("The event dispatcher stopped", e);
            throw e;
        } finally {
            queue.clear();
        }
    }

    /**
     * Stops the thread from executing its run method in preparation for shutting down the thread.
     * Events still in the queue are dropped.
     */
    public void terminate() {
        running = false;
        eventManager = null;
        queue.add(STOP);
    }
}
