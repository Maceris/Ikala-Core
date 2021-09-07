package com.ikalagaming.logging;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
