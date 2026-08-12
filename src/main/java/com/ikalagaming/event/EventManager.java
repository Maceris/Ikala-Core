package com.ikalagaming.event;

import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/** Manages events and listeners. Based off lahwran's fevents. */
@Slf4j
public class EventManager {

    private static EventManager instance;

    /**
     * Shuts down the static instance if it exists, and then nullifies the reference to it. This
     * exists in case you wish to use your own instances of the Event Manager and not use the single
     * static instance provided. If the instance does not exist, nothing happens. Note that a new
     * static instance may be created if the instance is requested later.
     *
     * @see EventManager#getInstance()
     */
    @Synchronized
    public static void destroyInstance() {
        if (EventManager.instance == null) {
            return;
        }
        EventManager.instance.shutdown();
        EventManager.instance = null;
    }

    /**
     * Returns the static instance of the event manager. Since there should only be one of these,
     * having a static instance is fine and any class can get the instance which all other classes
     * should share. If there is no instance yet, one will be created.
     *
     * @return the static instance of the Event Manager
     * @see EventManager#destroyInstance()
     */
    @Synchronized
    public static EventManager getInstance() {
        if (EventManager.instance == null) {
            EventManager.instance = new EventManager();
        }
        return EventManager.instance;
    }

    private final EventDispatcher dispatcher;

    private final Map<Class<? extends Event>, HandlerList> eventHandlers;
    private final Map<Class<? extends Command>, CommandListener> commandListeners;

    /**
     * Sets up the event managers handlers and event dispatching and starts the dispatching thread
     */
    public EventManager() {
        dispatcher = new EventDispatcher(this);
        eventHandlers = new HashMap<>();
        commandListeners = new HashMap<>();
        dispatcher.start();
    }

    /**
     * Creates and registers {@link EventListener EventListeners} for a given {@link Listener
     * listener}.
     *
     * @param listener Listener to create EventListeners for.
     */
    private void createEventListeners(@NonNull Listener listener) {
        // Search the methods for listeners
        for (final Method method : listener.getClass().getDeclaredMethods()) {
            final EventHandler handlerAnnotation = method.getAnnotation(EventHandler.class);
            if (handlerAnnotation == null) {
                continue;
            }
            final Class<?> checkClass = method.getParameterTypes()[0];
            if (method.getParameterTypes().length != 1
                    || !Event.class.isAssignableFrom(checkClass)) {
                continue;
            }
            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            /*
             * We need the method to be publicly visible so that it can be
             * called and passed events. SonarLint java:S3011 complains about
             * this, but we don't have much better options.
             */
            method.setAccessible(true); // NOSONAR

            // creates a class to execute the listener for the event
            EventExecutor executor =
                    (listener1, event) -> {
                        try {
                            if (!eventClass.isAssignableFrom(event.getClass())) {
                                return;
                            }
                            method.invoke(listener1, event);
                        } catch (Exception t) {
                            throw new EventException(t);
                        }
                    };

            eventHandlers
                    .computeIfAbsent(eventClass, ignored -> new HandlerList())
                    .register(new EventListener(listener, executor, handlerAnnotation.order()));
        }
    }

    /**
     * Creates and registers {@link CommandHandler CommandHandlers} for a given {@link Listener
     * listener}.
     *
     * @param listener Listener to create CommandHandlers for.
     */
    private void createCommandHandlers(@NonNull Listener listener) {
        // Search the methods for listeners
        for (final Method method : listener.getClass().getDeclaredMethods()) {
            final CommandHandler handlerAnnotation = method.getAnnotation(CommandHandler.class);
            if (handlerAnnotation == null) {
                continue;
            }
            final Class<?> checkClass = method.getParameterTypes()[0];
            if (method.getParameterTypes().length != 1
                    || !Command.class.isAssignableFrom(checkClass)) {
                continue;
            }
            final Class<? extends Command> eventClass = checkClass.asSubclass(Command.class);

            if (commandListeners.containsKey(eventClass)) {
                String message =
                        SafeResourceLoader.format(
                                "There is already a command handler associated with {}, registered by {}",
                                eventClass,
                                commandListeners
                                        .get(eventClass)
                                        .getListener()
                                        .getClass()
                                        .toString());
                log.error(message);
                throw new IllegalStateException(message);
            }

            /*
             * We need the method to be publicly visible so that it can be
             * called and passed events. SonarLint java:S3011 complains about
             * this, but we don't have much better options.
             */
            method.setAccessible(true); // NOSONAR

            // creates a class to execute the listener for the event
            EventExecutor executor =
                    (listener1, event) -> {
                        try {
                            if (!eventClass.isAssignableFrom(event.getClass())) {
                                return;
                            }
                            method.invoke(listener1, event);
                        } catch (Exception t) {
                            throw new EventException(t);
                        }
                    };
            commandListeners.put(eventClass, new CommandListener(listener, executor));
        }
    }

    /**
     * Sends the {@link Command command} to all of its listeners.
     *
     * @param command The command to fire.
     * @throws IllegalStateException if the element cannot be added at this time due to capacity
     *     restrictions
     */
    public void fireCommand(Command command) throws IllegalStateException {
        try {
            dispatcher.dispatchCommand(command);
        } catch (IllegalStateException illegalState) {
            throw illegalState;
        } catch (Exception e) {
            log.warn("Exception while dispatching command", e);
        }
    }

    /**
     * Sends the {@link Event event} to all of its listeners. Should not be used for {@link Command
     * commands}, * as they'll be dispatched to any relevant event handlers anyway and this would
     * not call the command handler.
     *
     * @param event The event to fire
     * @throws IllegalStateException if the element cannot be added at this time due to capacity
     *     restrictions
     */
    public void fireEvent(Event event) throws IllegalStateException {
        try {
            dispatcher.dispatchEvent(event);
        } catch (IllegalStateException illegalState) {
            throw illegalState;
        } catch (Exception e) {
            log.warn("Exception while dispatching event", e);
        }
    }

    /**
     * Returns a {@link HandlerList} for a give event type. Creates one if none exist.
     *
     * @param type the type of event to find handlers for
     * @return the map of handlers for the given type
     */
    private HandlerList getEventListeners(@NonNull Class<? extends Event> type) {
        synchronized (eventHandlers) {
            eventHandlers.computeIfAbsent(type, ignored -> new HandlerList());
            return eventHandlers.get(type);
        }
    }

    /**
     * Returns the handler list for the given event.
     *
     * @param event the class to find handlers for
     * @return The event handlers list for the given event.
     */
    HandlerList getEventHandlers(@NonNull Event event) {
        return getEventListeners(event.getClass());
    }

    /**
     * Fetch the command handler for the given command. Might be null if there isn't one.
     *
     * @param command The command.
     * @return The command handler, or possibly null.
     */
    CommandListener getCommandHandler(@NonNull Command command) {
        synchronized (commandListeners) {
            return commandListeners.get(command.getClass());
        }
    }

    /**
     * Registers event listeners for the given event monitor.
     *
     * @param <T> The type of event we are recording a listener for.
     * @param monitor The listener to register.
     */
    <T extends Event> void registerEventListeners(@NonNull EventMonitor<T> monitor) {
        @SuppressWarnings("unchecked")
        EventExecutor executor =
                (listener, event) -> {
                    try {
                        /*
                         * This executor only runs for the given monitor instance,
                         * so we can cast it to the type that it is.
                         */
                        ((EventMonitor<T>) listener).onEvent((T) event);
                    } catch (Exception t) {
                        throw new EventException(t);
                    }
                };

        HandlerList handlers = getEventListeners(monitor.getEventType());
        EventListener listener = new EventListener(monitor, executor, Order.MONITOR);
        handlers.register(listener);
    }

    /**
     * Registers event listeners in the supplied listener.
     *
     * @param listener The listener to register
     */
    public void registerEventListeners(@NonNull Listener listener) {
        synchronized (eventHandlers) {
            createEventListeners(listener);
        }
        synchronized (commandListeners) {
            createCommandHandlers(listener);
        }
    }

    /**
     * Set the event dispatcher's class loader.
     *
     * @param loader The new loader to use.
     */
    public void setThreadClassloader(@NonNull ClassLoader loader) {
        dispatcher.setContextClassLoader(loader);
    }

    /** Clears up the handlers and stops the dispatching thread. Acts like an onUnload method. */
    public void shutdown() {
        synchronized (eventHandlers) {
            eventHandlers.values().forEach(HandlerList::unregisterAll);
            eventHandlers.clear();
        }
        synchronized (commandListeners) {
            commandListeners.clear();
        }

        dispatcher.terminate();
        try {
            dispatcher.join();
        } catch (InterruptedException e) {
            log.warn("Interrupted while shutting down", e);
            // Re-interrupt as per SonarLint java:S2142
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Unregisters event listeners in the supplied listener.
     *
     * @param listener The listener to unregister
     */
    public void unregisterEventListeners(@NonNull Listener listener) {
        synchronized (eventHandlers) {
            eventHandlers.values().forEach(list -> list.unregister(listener));
        }
        synchronized (commandListeners) {
            this.commandListeners
                    .entrySet()
                    .removeIf(entry -> entry.getValue().getListener().equals(listener));
        }
    }
}
