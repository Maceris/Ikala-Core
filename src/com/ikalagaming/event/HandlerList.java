package com.ikalagaming.event;

import com.ikalagaming.util.SafeResourceLoader;

import lombok.Synchronized;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.EnumMap;

/**
 * Stores handlers per event. Based on lahwran's fevents.
 */
class HandlerList {

	private final EnumMap<Order, ArrayDeque<EventListener>> handlerSlots;

	private EventListener[] bakedList;

	/**
	 * Create a new handler list and initialize using an EventPriority. The
	 * HandlerList is then added to meta-list for use in bakeAll().
	 */
	public HandlerList() {
		this.handlerSlots = new EnumMap<>(Order.class);
		for (Order o : Order.values()) {
			this.handlerSlots.put(o, new ArrayDeque<EventListener>());
		}
		this.bakedList = null;
	}

	/**
	 * Creates an array of listeners that can be returned. This is done whenever
	 * the hashset changes and saves on memory.
	 */
	@Synchronized
	private void bake() {
		if (this.bakedList != null) {
			return;// The list is still valid, so do not bake it again
		}
		// A temporary list of entries
		ArrayDeque<EventListener> entries = new ArrayDeque<>();

		// add all of the listeners, in priority order, to the entries list
		this.handlerSlots.entrySet()
			.forEach((entry) -> entries.addAll(entry.getValue()));
		// bake the list into an array
		this.bakedList = entries.toArray(new EventListener[entries.size()]);
	}

	/**
	 * Get the baked registered listeners associated with this handler list
	 *
	 * @return The listeners registered
	 */
	public EventListener[] getRegisteredListeners() {
		EventListener[] handlers;
		while ((handlers = this.bakedList) == null) {
			this.bake(); // This prevents fringe cases of returning null
		}
		return handlers;
	}

	/**
	 * Register a new listener in this handler list.
	 *
	 * @param listener The listener to register
	 * @throws IllegalStateException if the listener is already registered
	 */
	@Synchronized
	public void register(EventListener listener) {
		if (this.handlerSlots.get(listener.getPriority()).contains(listener)) {
			throw new IllegalStateException(
				SafeResourceLoader.getString("LISTENER_ALREADY_REGISTERED",
					EventManager.resourceBundle).replaceFirst("\\$PRIORITY",
						listener.getPriority().toString()));
		}
		this.bakedList = null;
		this.handlerSlots.get(listener.getPriority()).add(listener);
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
	}

	/**
	 * Remove a listener from a specific order slot.
	 *
	 * @param listener The listener to unregister
	 */
	@Synchronized
	public void unregister(EventListener listener) {
		if (this.handlerSlots.get(listener.getPriority()).remove(listener)) {
			this.bakedList = null;
		}
	}

	/**
	 * Remove a specific listener from this handler
	 *
	 * @param listener listener to remove
	 */
	@Synchronized
	public void unregister(Listener listener) {
		// go through each priority
		this.handlerSlots.values().forEach(
			(list) -> list.removeIf((li) -> li.getListener().equals(listener)));

		this.bakedList = null;
	}

	/**
	 * Unregisters all handlers.
	 */
	@Synchronized
	public void unregisterAll() {
		this.handlerSlots.values().forEach((list) -> list.clear());
		this.bakedList = null;
	}

}
