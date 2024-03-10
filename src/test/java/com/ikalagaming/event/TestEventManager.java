package com.ikalagaming.event;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * Tests for the {@link EventManager} class.
 *
 * @author Ches Burks
 */
public class TestEventManager {
    /**
     * An event used for testing.
     *
     * @author Ches Burks
     */
    public static class TestEvent extends Event {}

    /** Tear down after the tests, destroying the event manager. */
    @AfterAll
    public static void afterClass() {
        EventManager.destoryInstance();
        EventAssert.stopListeningForEverything();
    }

    /** Set up before the tests, creating the event manager. */
    @BeforeAll
    public static void beforeClass() {
        EventManager.getInstance();
    }

    /** Test that events get sent to event listeners. */
    @Test
    public void testEventFiring() {
        EventAssert.listenFor(TestEvent.class);
        TestEvent e = new TestEvent();
        EventManager.getInstance().fireEvent(e);
        Assertions.assertTrue(EventAssert.wasFired(TestEvent.class, 1000), "Class was not fired");
    }

    /** Tests the order that event listeners get called in, and that all get called. */
    @Test
    public void testEventOrder() {
        EventManager manager = EventManager.getInstance();
        EventOrderListener listener = new EventOrderListener();

        manager.registerEventListeners(listener);

        EventAssert.listenFor(EventOrderListener.SimpleEvent.class);

        new EventOrderListener.SimpleEvent().fire();

        Awaitility.await()
                .atMost(1000, TimeUnit.MILLISECONDS)
                .until(() -> EventAssert.wasFired(EventOrderListener.SimpleEvent.class));
        EventAssert.stopListeningForEverything();
        Assertions.assertTrue(listener.isEverythingCalled());
    }

    /**
     * Register a normal event listener, see if it handles events, then unregister and make sure it
     * stops getting events.
     */
    @Test
    public void testNormalListenerFlow() {
        EventManager manager = EventManager.getInstance();
        CustomEventListener listener = new CustomEventListener();

        final String message = "Test123 message.";

        manager.registerEventListeners(listener);
        CustomEvent event = new CustomEvent(message);
        manager.fireEvent(event);

        Awaitility.await().atMost(1000, TimeUnit.MILLISECONDS).until(listener::isEventReceived);

        Assertions.assertTrue(listener.isEventReceived());
        Assertions.assertEquals(message, listener.getRecordedData());

        listener.clear();

        manager.unregisterEventListeners(listener);

        EventAssert.listenFor(CustomEvent.class);

        manager.fireEvent(event);

        Awaitility.await()
                .atMost(1000, TimeUnit.MILLISECONDS)
                .until(() -> EventAssert.wasFired(CustomEvent.class));

        Assertions.assertFalse(listener.isEventReceived());
        EventAssert.stopListeningForEverything();
    }
}
