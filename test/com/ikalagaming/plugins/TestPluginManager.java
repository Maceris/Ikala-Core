package com.ikalagaming.plugins;

import com.ikalagaming.launcher.Launcher;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the Plugin Manager.
 *
 * @author Ches Burks
 *
 */
public class TestPluginManager {

	/**
	 * Tear down after each test, shutting down the framework.
	 */
	@After
	public void afterTest() {
		Launcher.shutdown();
	}

	/**
	 * Set up before each test, initializing the framework.
	 */
	@Before
	public void beforeTest() {
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

	/**
	 * Tests the ability to load a set of plugins by name.
	 */
	@Test
	public void testLoadingPluginSubset() {
		PluginManager manager = PluginManager.getInstance();
		List<String> toLoad = Arrays.asList("TestComplexChainA",
			"TestComplexChainB", "TestComplexChainC", "TestComplexChainD");

		manager.loadPlugins(this.TEST_JAR_FOLDER, toLoad);

		for (String name : toLoad) {
			String message =
				String.format("Plugin '%s' should have been loaded.", name);
			Assert.assertTrue(message, manager.isLoaded(name));
		}
	}

	/**
	 * Tests that loading a single plugin works.
	 */
	@Test
	public void testLoadPlugin() {
		PluginManager manager = PluginManager.getInstance();
		final String pluginName = "TestStandalone";

		manager.loadPlugin(this.TEST_JAR_FOLDER, pluginName);

		String message =
			String.format("Plugin '%s' should have been loaded.", pluginName);
		Assert.assertTrue(message, manager.isLoaded(pluginName));
	}

	/**
	 * Tests the lifecycle of loading, enabling, disabling, and unloading a
	 * plugin.
	 */
	@Test
	public void testLifecycle() {
		PluginManager manager = PluginManager.getInstance();
		final String pluginName = "TestStandalone";

		manager.setEnableOnLoad(false);

		Assert.assertTrue(manager.loadPlugin(this.TEST_JAR_FOLDER, pluginName));

		String loadedMessage =
			String.format("Plugin '%s' should be loaded.", pluginName);
		Assert.assertTrue(loadedMessage, manager.isLoaded(pluginName));

		Assert.assertTrue(manager.enable(pluginName));

		String enabledMessage =
			String.format("Plugin '%s' should be enabled", pluginName);
		Assert.assertTrue(loadedMessage, manager.isLoaded(pluginName));
		Assert.assertTrue(enabledMessage, manager.isEnabled(pluginName));

		Assert.assertTrue(manager.disable(pluginName));

		String disabledMessage =
			String.format("Plugin '%s' should not be enabled", pluginName);
		Assert.assertTrue(loadedMessage, manager.isLoaded(pluginName));
		Assert.assertFalse(disabledMessage, manager.isEnabled(pluginName));

		String unloadedMessage =
			String.format("Plugin '%s' should not be loaded.", pluginName);
		Assert.assertTrue(manager.unloadPlugin(pluginName));
		Assert.assertFalse(unloadedMessage, manager.isLoaded(pluginName));
		Assert.assertFalse(disabledMessage, manager.isEnabled(pluginName));

	}
}
