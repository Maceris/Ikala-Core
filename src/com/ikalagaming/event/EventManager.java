package com.ikalagaming.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ikalagaming.logging.LoggingLevel;
import com.ikalagaming.logging.events.Log;
import com.ikalagaming.logging.events.LogError;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * Manages events and listeners.
 */
public class EventManager {

	private EventDispatcher dispatcher;
	private HashMap<Class<? extends Event>, HandlerList> handlerMap;

	/**
	 * Sets up the event managers handlers and event dispatching and starts the
	 * dispatching thread
	 */
	public EventManager() {
		this.dispatcher = new EventDispatcher(this);
		this.handlerMap = new HashMap<>();
		this.dispatcher.start();
	}

	/**
	 * Creates {@link EventListener EventListeners} for a given {@link Listener
	 * listener}.
	 *
	 * @param listener The listener to create EventListenrs for
	 * @return A map of events to a set of EventListeners belonging to it
	 */
	private Map<Class<? extends Event>, Set<EventListener>> createRegisteredListeners(
			Listener listener) {

		Map<Class<? extends Event>, Set<EventListener>> toReturn =
				new HashMap<>();
		Set<Method> methods;
		try {
			Method[] publicMethods = listener.getClass().getMethods();
			methods = new HashSet<>(publicMethods.length, Float.MAX_VALUE);
			for (Method method : publicMethods) {
				methods.add(method);
			}
			for (Method method : listener.getClass().getDeclaredMethods()) {
				methods.add(method);
			}
		}
		catch (NoClassDefFoundError e) {
			return toReturn;
		}

		// search the methods for listeners
		for (final Method method : methods) {
			final EventHandler handlerAnnotation =
					method.getAnnotation(EventHandler.class);
			if (handlerAnnotation == null) {
				continue;
			}
			final Class<?> checkClass;
			if (method.getParameterTypes().length != 1
					|| !Event.class.isAssignableFrom(checkClass =
							method.getParameterTypes()[0])) {
				continue;
			}
			final Class<? extends Event> eventClass =
					checkClass.asSubclass(Event.class);
			method.setAccessible(true);
			Set<EventListener> eventSet = toReturn.get(eventClass);
			if (eventSet == null) {
				eventSet = new HashSet<>();
				// add the listener methods to the list of events
				toReturn.put(eventClass, eventSet);
			}

			// creates a class to execute the listener for the event
			EventExecutor executor = new EventExecutor() {
				@Override
				public void execute(Listener listener1, Event event)
						throws EventException {
					try {
						if (!eventClass.isAssignableFrom(event.getClass())) {
							return;
						}
						method.invoke(listener1, event);
					}
					catch (Throwable t) {
						EventException evtExcept = new EventException(t);
						throw evtExcept;
					}
				}
			};

			eventSet.add(new EventListener(listener, executor));

		}
		return toReturn;
	}

	/**
	 * Sends the {@link Event event} to all of its listeners.
	 *
	 * @param event The event to fire
	 * @throws IllegalStateException if the element cannot be added at this time
	 *             due to capacity restrictions
	 */
	public void fireEvent(Event event) throws IllegalStateException {
		try {
			this.dispatcher.dispatchEvent(event);
		}
		catch (IllegalStateException illegalState) {
			throw illegalState;
		}
		catch (Exception e) {
			if (event instanceof Log || event instanceof LogError) {
				e.printStackTrace(System.err);
			}
			else {
				LogError err =
						new LogError(SafeResourceLoader.getString(
								"EVT_QUEUE_FULL",
								"com.ikalagaming.event.strings",
								"Event queue full"),
								"EventManager.fireEvent(Event)",
								LoggingLevel.WARNING, "event-manager");
				this.dispatcher.dispatchEvent(err);
			}

		}
	}

	/**
	 * Returns a {@link HandlerList} for a give event type. Creates one if none
	 * exist.
	 *
	 * @param type the type of event to find handlers for
	 * @return the map of handlers for the given type
	 */
	private HandlerList getEventListeners(Class<? extends Event> type) {
		if (!this.handlerMap.containsKey(type)) {
			this.handlerMap.put(type, new HandlerList());
		}
		return this.handlerMap.get(type);
	}

	/**
	 * Returns the handlerlist for the given event.
	 *
	 * @param event the class to find handlers for
	 * @return the handlerlist for that class
	 */
	public HandlerList getHandlers(Event event) {
		return this.getEventListeners(event.getClass());
	}

	/**
	 * Registers event listeners in the supplied listener.
	 *
	 * @param listener The listener to register
	 */
	public void registerEventListeners(Listener listener) {
		for (Map.Entry<Class<? extends Event>, Set<EventListener>> entry : this
				.createRegisteredListeners(listener).entrySet()) {
			this.getEventListeners(entry.getKey())
					.registerAll(entry.getValue());
		}
	}

	/**
	 * Clears up the handlers and stops the dispatching thread. Acts like an
	 * onUnload method.
	 */
	public void shutdown() {
		// TODO make sure this is called
		for (HandlerList l : this.handlerMap.values()) {
			l.unregisterAll();
		}
		this.handlerMap.clear();

		this.dispatcher.terminate();
		try {
			this.dispatcher.join();
		}
		catch (InterruptedException e) {
			LogError err =
					new LogError(SafeResourceLoader.getString(
							"THREAD_INTERRUPTED",
							"com.ikalagaming.event.resources.strings",
							"Thread interrupted"), "EventManager.onDisable()",
							LoggingLevel.SEVERE, "event-manager");
			this.dispatcher.dispatchEvent(err);
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Unregisters event listeners in the supplied listener.
	 *
	 * @param listener The listener to unregister
	 */
	public void unregisterEventListeners(Listener listener) {
		for (HandlerList list : this.handlerMap.values()) {
			list.unregisterAll(listener);
		}
	}
}
