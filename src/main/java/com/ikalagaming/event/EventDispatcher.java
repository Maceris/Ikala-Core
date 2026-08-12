package com.ikalagaming.event;

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

    private static final String EVENT_EXCEPTION = "Exception while dispatching event";
    private static final String EVENT_MANAGER_MISSING = "There is no event manager!";

    /**
     * The number of milliseconds to wait before timing out and checking if there are more items
     * again.
     */
    private static final long WAIT_TIMEOUT = 1000;

    private final ConcurrentLinkedDeque<Command> commandQueue;
    private final ConcurrentLinkedDeque<Event> eventQueue;

    private EventManager eventManager;

    private boolean running;
    private boolean hasCommands;
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
        commandQueue = new ConcurrentLinkedDeque<>();
        eventQueue = new ConcurrentLinkedDeque<>();
        eventManager = manager;
        hasCommands = false;
        hasEvents = false;
        running = true;
        syncObject = new Object();
    }

    private void actuallyDispatchCommand(Command command) {
        if (command == null) {
            return;
        }
        if (eventManager == null) {
            log.error(EVENT_MANAGER_MISSING);
            return;
        }
        HandlerList handlers = eventManager.getEventHandlers(command);
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
        CommandListener commandHandler = eventManager.getCommandHandler(command);
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
        if (event == null) {
            return;
        }
        if (eventManager == null) {
            log.error(EVENT_MANAGER_MISSING);
            return;
        }
        HandlerList handlers = eventManager.getEventHandlers(event);
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
     * Adds the {@link Command event} to the queue pending dispatch.
     *
     * @param command The command to send out
     * @throws IllegalStateException if the element cannot be added at this time due to capacity
     *     restrictions
     */
    public void dispatchCommand(Command command) throws IllegalStateException {
        if (command == null) {
            return;
        }
        try {
            synchronized (commandQueue) {
                commandQueue.add(command);
            }
            hasCommands = true;
        } catch (IllegalStateException illegalState) {
            throw illegalState;
        } catch (Exception e) {
            log.error(EVENT_EXCEPTION, e);
            return; // don't wake up thread
        }
        wakeUp();
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
        if (event == null) {
            return;
        }
        try {
            synchronized (eventQueue) {
                eventQueue.add(event);
            }
            hasEvents = true;
        } catch (IllegalStateException illegalState) {
            throw illegalState;
        } catch (Exception e) {
            log.error(EVENT_EXCEPTION, e);
            return; // don't wake up thread
        }
        wakeUp();
    }

    private void handleCommand() {
        synchronized (commandQueue) {
            if (commandQueue.isEmpty()) {
                hasCommands = false;
                return;
            }
        }
        Command command;
        try {
            command = commandQueue.remove();
        } catch (NoSuchElementException noElement) {
            // the queue is empty
            hasCommands = false;
            log.warn("The command queue was empty due to threading issues");
            return;
        }
        actuallyDispatchCommand(command);
    }

    private void handleEvent() {
        synchronized (eventQueue) {
            if (eventQueue.isEmpty()) {
                hasEvents = false;
                return;
            }
        }
        Event event;
        try {
            event = eventQueue.remove();
        } catch (NoSuchElementException noElement) {
            // the queue is empty
            hasEvents = false;
            log.warn("The event queue was empty due to threading issues");
            return;
        }
        actuallyDispatchEvent(event);
    }

    /**
     * Checks for events in the queue, and dispatches them if possible. Does not do anything if
     * {@link #terminate()} has been called.
     */
    @Override
    public void run() {
        while (running) {
            while (!hasEvents && !hasCommands) {
                synchronized (syncObject) {
                    try {
                        // block this thread until an item is added
                        syncObject.wait(WAIT_TIMEOUT);
                    } catch (InterruptedException e) {
                        log.warn("Thread interrupted while waiting for events");
                        // Re-interrupt as per SonarLint java:S2142
                        Thread.currentThread().interrupt();
                    }
                }
                // in case it was terminated while waiting
                if (!running) {
                    break;
                }
            }
            if (hasCommands) {
                handleCommand();
            }
            if (hasEvents) {
                handleEvent();
            }
        }
        // Done running
        eventQueue.clear();
        commandQueue.clear();
    }

    /**
     * Stops the thread from executing its run method in preparation for shutting down the thread.
     */
    public void terminate() {
        hasCommands = false;
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
