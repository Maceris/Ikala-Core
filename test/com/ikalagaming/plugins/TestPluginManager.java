package com.ikalagaming.plugins;

import com.ikalagaming.launcher.Launcher;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

/**
 * Tests for the Plugin Manager.
 *
 * @author Ches Burks
 *
 */
public class TestPluginManager {

	/**
	 * Tear down after the tests, shutting down the framework.
	 */
	@AfterClass
	public static void afterClass() {
		Launcher.shutdown();
	}

	/**
	 * Set up before the tests, initializing the framework.
	 */
	@BeforeClass
	public static void beforeClass() {
		Launcher.initialize();
	}

	/**
	 * The resources folder where the test jars are located.
	 */
	private final String TEST_JAR_FOLDER =
		System.getProperty("user.dir") + File.separatorChar + "test"
			+ File.separatorChar + "com" + File.separatorChar + "ikalagaming"
			+ File.separatorChar + "plugins" + File.separatorChar + "resources";

	/**
	 * The names of plugins that should be loaded successfully.
	 */
	private final String[] VALID_PLUGINS = {"TestComplexChainA",
		"TestComplexChainB", "TestComplexChainC", "TestComplexChainD",
		"TestCycleA", "TestCycleB", "TestDependA", "TestDependB", "TestDependC",
		"TestDependD", "TestSoftDependA", "TestSoftDependB", "TestSoftDependC",
		"TestSoftDependD", "TestSoftDependCycleA", "TestSoftDependCycleB",
		"TestSoftDependCycleC", "TestStandalone"};

	/**
	 * The names of plugins that have errors and should not load successfully.
	 */
	private final String[] INVALID_PLUGINS = {"TestError.InvalidName",
		"TestErrorInvalidVersion", "TestErrorMainClassType",
		"TestErrorNoMainClass", "TestErrorNoMainClassFile", "TestErrorNoName",
		"TestErrorNoVersion", "TestErrorNoYml"};

	/**
	 * The basic suite that tries to load all the test plugins and sees if the
	 * loaded or were skipped correctly.
	 */
	@Test
	public void testLoadingPlugins() {
		PluginManager manager = PluginManager.getInstance();
		manager.loadAllPlugins(this.TEST_JAR_FOLDER);

		for (String name : this.VALID_PLUGINS) {
			String message =
				String.format("Plugin '%s' should have been loaded.", name);
			Assert.assertTrue(message, manager.isLoaded(name));
		}

		for (String name : this.INVALID_PLUGINS) {
			String message =
				String.format("Plugin '%s' should not have been loaded.", name);
			Assert.assertFalse(message, manager.isLoaded(name));
		}

	}
}
