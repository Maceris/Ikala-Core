package com.ikalagaming.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Stores handlers per event. Based on lahwran's fevents.
 */
public class HandlerList {
	/*
	 * Handler list. This changes when register() and unregister() are called.
	 * This is a HashSet for speed.
	 */
	private HashSet<EventListener> handlerslots;
	private EventListener[] bakedList;

	/**
	 * Create a new handler list and initialize using an EventPriority. The
	 * HandlerList is then added to meta-list for use in bakeAll().
	 */
	public HandlerList() {
		this.handlerslots = new HashSet<>();
		this.bakedList = new EventListener[0];
	}

	/**
	 * Creates an array of listeners that can be returned. This is done whenever
	 * the hashset changes and saves on memory.
	 */
	private synchronized void bake() {
		this.bakedList = new EventListener[this.handlerslots.size()];
		this.bakedList = this.handlerslots.toArray(this.bakedList);
	}

	/**
	 * Get the baked registered listeners associated with this handler list
	 *
	 * @return The listeners registered
	 */
	public EventListener[] getRegisteredListeners() {
		return this.bakedList;
	}

	/**
	 * Register a new listener in this handler list.
	 *
	 * @param listener The listener to register
	 * @throws IllegalStateException if the listener is already registered
	 */
	public synchronized void register(EventListener listener) {
		if (this.handlerslots.contains(listener)) {
			IllegalStateException excep =
					new IllegalStateException(
							"This listener is already registered");
			throw excep;
		}
		this.handlerslots.add(listener);
		this.bake();
	}

	/**
	 * Register a collection of new listeners in this handler list.
	 *
	 * @param listeners The collection to register
	 */
	public void registerAll(Collection<EventListener> listeners) {
		for (EventListener listener : listeners) {
			this.register(listener);
		}
		this.bake();
	}

	/**
	 * Remove a listener from a specific order slot.
	 *
	 * @param listener The listener to unregister
	 */
	public synchronized void unregister(EventListener listener) {
		this.handlerslots.remove(listener);
		this.bake();
	}

	/**
	 * Remove a specific listener from this handler
	 *
	 * @param listener the Listener to unregister
	 */
	public synchronized void unregister(Listener listener) {

		// loop through and unregister a listener from the list if it
		// matches the param
		for (Iterator<EventListener> i = this.handlerslots.iterator(); i
				.hasNext();) {
			if (i.next().getListener().equals(listener)) {
				i.remove();
			}
		}
		this.bake();
	}

	/**
	 * Unregisters all handlers.
	 */
	public void unregisterAll() {
		synchronized (this.handlerslots) {
			this.handlerslots.clear();
			this.bake();
		}
	}

	/**
	 * Unregister a specific listener from the handler list.
	 *
	 * @param listener The listener to unregister
	 */
	public void unregisterAll(Listener listener) {
		synchronized (this.handlerslots) {
			this.handlerslots.remove(listener);
			this.bake();
		}
	}

}
