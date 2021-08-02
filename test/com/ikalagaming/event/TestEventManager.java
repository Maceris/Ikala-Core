package com.ikalagaming.event;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the {@link EventManager} class.
 *
 * @author Ches Burks
 *
 */
public class TestEventManager {
	/**
	 * An event used for testing.
	 *
	 * @author Ches Burks
	 *
	 */
	public class TestEvent extends Event {}

	/**
	 * Tear down after the tests, destroying the event manager.
	 */
	@AfterClass
	public static void afterClass() {
		EventManager.destoryInstance();
		EventAssert.stopListeningForEverything();
	}

	/**
	 * Set up before the tests, creating the event manager.
	 */
	@BeforeClass
	public static void beforeClass() {
		EventManager.getInstance();
	}

	/**
	 * Test that events get sent to event listeners.
	 */
	@Test
	public void testEventFiring() {
		EventAssert.listenFor(TestEvent.class);
		TestEvent e = new TestEvent();
		EventManager.getInstance().fireEvent(e);
		Assert.assertTrue("Class was not fired",
			EventAssert.wasFired(TestEvent.class, 1000));
	}

}
