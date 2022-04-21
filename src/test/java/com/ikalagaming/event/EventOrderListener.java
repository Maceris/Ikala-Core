package com.ikalagaming.event;


import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Listens for events in different orders to see how they get called.
 *
 * @author Ches Burks
 *
 */
public class EventOrderListener implements Listener {

	/**
	 * Does not do much.
	 *
	 * @author Ches Burks
	 *
	 */
	public static class SimpleEvent extends Event {}

	/**
	 * Whether a listener of each order has been called or not.
	 */
	private Map<Order, Boolean> called;

	/**
	 * Set up for handing listeners.
	 */
	public EventOrderListener() {
		this.called = new HashMap<>();
		for (Order o : Order.values()) {
			this.called.put(o, false);
		}
	}

	/**
	 * Listen at the default order.
	 *
	 * @param event The event.
	 */
	@EventHandler(order = Order.DEFAULT)
	private void defaultOrder(SimpleEvent event) {
		Assertions.assertTrue(this.called.get(Order.EARLIEST));
		Assertions.assertTrue(this.called.get(Order.EARLY));
		Assertions.assertFalse(this.called.get(Order.LATE));
		Assertions.assertFalse(this.called.get(Order.LATEST));
		Assertions.assertFalse(this.called.get(Order.MONITOR));
		this.called.put(Order.DEFAULT, true);
	}

	/**
	 * Listen at the default order, without specifying order.
	 *
	 * @param event The event.
	 */
	@EventHandler
	private void defaultOrderAndArgs(SimpleEvent event) {
		Assertions.assertTrue(this.called.get(Order.EARLIEST));
		Assertions.assertTrue(this.called.get(Order.EARLY));
		Assertions.assertFalse(this.called.get(Order.LATE));
		Assertions.assertFalse(this.called.get(Order.LATEST));
		Assertions.assertFalse(this.called.get(Order.MONITOR));
		this.called.put(Order.DEFAULT, true);
	}

	/**
	 * Listen at the earliest order.
	 *
	 * @param event The event.
	 */
	@EventHandler(order = Order.EARLIEST)
	private void earliest(SimpleEvent event) {
		Assertions.assertFalse(this.called.get(Order.EARLIEST));
		Assertions.assertFalse(this.called.get(Order.EARLY));
		Assertions.assertFalse(this.called.get(Order.DEFAULT));
		Assertions.assertFalse(this.called.get(Order.LATE));
		Assertions.assertFalse(this.called.get(Order.LATEST));
		Assertions.assertFalse(this.called.get(Order.MONITOR));
		this.called.put(Order.EARLIEST, true);
	}

	/**
	 * Listen at the early order.
	 *
	 * @param event The event.
	 */
	@EventHandler(order = Order.EARLY)
	private void early(SimpleEvent event) {
		Assertions.assertTrue(this.called.get(Order.EARLIEST));
		Assertions.assertFalse(this.called.get(Order.EARLY));
		Assertions.assertFalse(this.called.get(Order.DEFAULT));
		Assertions.assertFalse(this.called.get(Order.LATE));
		Assertions.assertFalse(this.called.get(Order.LATEST));
		Assertions.assertFalse(this.called.get(Order.MONITOR));
		this.called.put(Order.EARLY, true);
	}

	/**
	 * Checks if every listener has been called.
	 *
	 * @return If every order valued listener got called.
	 */
	public boolean isEverythingCalled() {
		// check all values are true
		return this.called.entrySet().stream().allMatch(Entry::getValue);
	}

	/**
	 * Listen at the late order.
	 *
	 * @param event The event.
	 */
	@EventHandler(order = Order.LATE)
	private void late(SimpleEvent event) {
		Assertions.assertTrue(this.called.get(Order.EARLIEST));
		Assertions.assertTrue(this.called.get(Order.EARLY));
		Assertions.assertTrue(this.called.get(Order.DEFAULT));
		Assertions.assertFalse(this.called.get(Order.LATE));
		Assertions.assertFalse(this.called.get(Order.LATEST));
		Assertions.assertFalse(this.called.get(Order.MONITOR));
		this.called.put(Order.LATE, true);
	}

	/**
	 * Listen at the latest order.
	 *
	 * @param event The event.
	 */
	@EventHandler(order = Order.LATEST)
	private void latest(SimpleEvent event) {
		Assertions.assertTrue(this.called.get(Order.EARLIEST));
		Assertions.assertTrue(this.called.get(Order.EARLY));
		Assertions.assertTrue(this.called.get(Order.DEFAULT));
		Assertions.assertTrue(this.called.get(Order.LATE));
		Assertions.assertFalse(this.called.get(Order.LATEST));
		Assertions.assertFalse(this.called.get(Order.MONITOR));
		this.called.put(Order.LATEST, true);
	}

	/**
	 * Listen at the monitor order.
	 *
	 * @param event The event.
	 */
	@EventHandler(order = Order.MONITOR)
	private void monitor(SimpleEvent event) {
		this.called.put(Order.MONITOR, true);

	}

}
