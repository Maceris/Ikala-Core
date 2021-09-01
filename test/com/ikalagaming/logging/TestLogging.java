package com.ikalagaming.logging;

import com.ikalagaming.event.EventAssert;
import com.ikalagaming.logging.events.Log;

import org.awaitility.Awaitility;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Tests for the logging system.
 *
 * @author Ches Burks
 *
 */
public class TestLogging {

	/**
	 * Setup the logger before each test.
	 */
	@Before
	public void setup() {
		Logging.create();
	}

	/**
	 * Tear down the logger after each test.
	 */
	@After
	public void teardown() {
		Logging.destory();
	}

	/**
	 * Tests that logs get recorded.
	 */
	@Test
	public void testLog() {
		EventAssert.listenFor(Log.class);
		Logging.setLogLevel(LogLevel.ALL);
		String message = "This is a test.";
		Logging.log("Unit tests", LogLevel.INFO, message);
		Awaitility.await().atMost(1000, TimeUnit.MILLISECONDS)
			.until(() -> EventAssert.wasFired(Log.class));

		Log recorded = EventAssert.getRecordedEvents(Log.class).get(0);
		Assert.assertTrue(
			recorded.getMessage().contains(LogLevel.INFO.getLocalizedName()));
		Assert.assertTrue(recorded.getMessage().contains(message));
		EventAssert.stopListeningForEverything();
	}

	/**
	 * Test that log events only get sent when they should according to the log
	 * level.
	 */
	@Test
	public void testLogLevelFiltering() {
		EventAssert.listenFor(Log.class);
		Logging.setLogLevel(LogLevel.OFF);

		for (LogLevel setLevel : LogLevel.values()) {
			Logging.setLogLevel(setLevel);

			for (LogLevel sendLevel : LogLevel.values()) {
				if (LogLevel.ALL.equals(sendLevel)
					|| LogLevel.OFF.equals(sendLevel)) {
					// skip special values
					continue;
				}
				EventAssert.resetFireCount(Log.class);
				Logging.log("Unit tests", sendLevel, "test");

				boolean shouldBeLogged =
					sendLevel.intValue() >= setLevel.intValue();

				String error = "Logging " + sendLevel.getLocalizedName()
					+ " with min log level set to "
					+ setLevel.getLocalizedName();
				Assert.assertEquals(error, shouldBeLogged,
					EventAssert.wasFired(Log.class, 100));
			}
		}
		Logging.setLogLevel(LogLevel.ALL);

		EventAssert.resetFireCount(Log.class);
		Logging.log("Unit tests", LogLevel.ALL, "skip");
		Assert.assertFalse(EventAssert.wasFired(Log.class, 100));

		EventAssert.resetFireCount(Log.class);
		Logging.log("Unit tests", LogLevel.OFF, "skip");
		Assert.assertFalse(EventAssert.wasFired(Log.class, 100));

		EventAssert.stopListeningForEverything();
	}

	/**
	 * Test that the log level getter and setter work.
	 */
	@Test
	public void testSettingLogLevel() {
		for (LogLevel level : LogLevel.values()) {
			Logging.setLogLevel(level);
			Assert.assertEquals(level, Logging.getLogLevel());
		}
	}

}
