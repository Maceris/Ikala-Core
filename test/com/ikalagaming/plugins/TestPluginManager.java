package com.ikalagaming.plugins;

import com.ikalagaming.event.EventAssert;
import com.ikalagaming.launcher.Launcher;
import com.ikalagaming.logging.LogLevel;
import com.ikalagaming.logging.Logging;
import com.ikalagaming.plugins.events.PluginCommandSent;
import com.ikalagaming.util.SafeResourceLoader;

import org.awaitility.Awaitility;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Tests for the Plugin Manager.
 *
 * @author Ches Burks
 *
 */
public class TestPluginManager {

	/**
	 * Tracks the callback method being hit.
	 */
	private boolean callbackExecuted = false;
	private Object callbackSync = new Object();

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
		Logging.setLogLevel(LogLevel.WARNING);
	}

	/**
	 * A method that records itself being called.
	 *
	 * @param args Ignored.
	 */
	public void commandCallback(String args[]) {
		synchronized (this.callbackSync) {
			this.callbackExecuted = true;
		}
	}

	/**
	 * Used to check if {@link #commandCallback(String[])} was executed.
	 *
	 * @return True if the callback was run, false if not.
	 */
	private boolean isCallbackExecuted() {
		synchronized (this.callbackSync) {
			return this.callbackExecuted;
		}
	}

	/**
	 * Clears all plugin commands and checks that they're gone.
	 */
	@Test
	public void testCommandClearing() {
		PluginManager manager = PluginManager.getInstance();

		List<String> commands = Arrays
			.asList("COMMAND_ENABLE", "COMMAND_DISABLE", "COMMAND_DISABLE",
				"COMMAND_LOAD", "COMMAND_UNLOAD", "COMMAND_RELOAD",
				"COMMAND_LIST_PLUGINS", "COMMAND_HELP")
			.stream().map(cmd -> SafeResourceLoader.getString(cmd,
				manager.getResourceBundle()))
			.collect(Collectors.toList());

		Assert.assertTrue(commands.stream().map(manager::isCommandRegistered)
			.allMatch(value -> true == value));

		manager.clearCommands();

		Assert.assertTrue(commands.stream().map(manager::isCommandRegistered)
			.allMatch(value -> false == value));
	}

	/**
	 * Test that command registration and unregistration works.
	 */
	@Test
	public void testCommandRegistration() {
		PluginManager manager = PluginManager.getInstance();
		final String COMMAND = "TestCommand";

		manager.registerCommand(COMMAND, this::commandCallback, "UnitTests");
		Assert.assertTrue(manager.isCommandRegistered(COMMAND));

		new PluginCommandSent(COMMAND).fire();

		Awaitility.await().atMost(1000, TimeUnit.MILLISECONDS)
			.until(this::isCallbackExecuted);
		manager.unregisterCommand(COMMAND);
		synchronized (this.callbackSync) {
			this.callbackExecuted = false;
		}

		EventAssert.listenFor(PluginCommandSent.class);
		new PluginCommandSent(COMMAND).fire();

		Awaitility.await().atMost(1000, TimeUnit.MILLISECONDS)
			.until(() -> EventAssert.wasFired(PluginCommandSent.class));

		Assert.assertFalse(this.callbackExecuted);
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
		Assert.assertFalse(manager.isEnableOnLoad());

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

	/**
	 * Tests the lifecycle of enabling, disabling, and unloading a plugin using
	 * the plugin commands. Load and reload are skipped as it expects a standard
	 * location that unit tests don't support.
	 */
	@Test
	public void testLifecycleCommands() {
		PluginManager manager = PluginManager.getInstance();
		final String pluginName = "TestStandalone";

		manager.setEnableOnLoad(false);

		String enable = SafeResourceLoader.getString("COMMAND_ENABLE",
			manager.getResourceBundle());
		String disable = SafeResourceLoader.getString("COMMAND_DISABLE",
			manager.getResourceBundle());
		String unload = SafeResourceLoader.getString("COMMAND_UNLOAD",
			manager.getResourceBundle());

		final String[] args = {pluginName};
		Assert.assertTrue(manager.loadPlugin(this.TEST_JAR_FOLDER, pluginName));

		EventAssert.listenFor(PluginCommandSent.class);

		String loadedMessage =
			String.format("Plugin '%s' should be loaded.", pluginName);
		Assert.assertTrue(loadedMessage, manager.isLoaded(pluginName));

		EventAssert.resetFireCount(PluginCommandSent.class);
		new PluginCommandSent(enable, args).fire();
		Awaitility.await().atMost(1000, TimeUnit.MILLISECONDS)
			.until(() -> EventAssert.wasFired(PluginCommandSent.class));

		String enabledMessage =
			String.format("Plugin '%s' should be enabled", pluginName);
		Assert.assertTrue(loadedMessage, manager.isLoaded(pluginName));
		Assert.assertTrue(enabledMessage, manager.isEnabled(pluginName));

		EventAssert.resetFireCount(PluginCommandSent.class);
		new PluginCommandSent(disable, args).fire();
		Awaitility.await().atMost(1000, TimeUnit.MILLISECONDS)
			.until(() -> EventAssert.wasFired(PluginCommandSent.class));

		String disabledMessage =
			String.format("Plugin '%s' should not be enabled", pluginName);
		Assert.assertTrue(loadedMessage, manager.isLoaded(pluginName));
		Assert.assertFalse(disabledMessage, manager.isEnabled(pluginName));

		EventAssert.resetFireCount(PluginCommandSent.class);
		new PluginCommandSent(unload, args).fire();
		Awaitility.await().atMost(1000, TimeUnit.MILLISECONDS)
			.until(() -> EventAssert.wasFired(PluginCommandSent.class));

		String unloadedMessage =
			String.format("Plugin '%s' should not be loaded.", pluginName);
		Assert.assertFalse(unloadedMessage, manager.isLoaded(pluginName));
		Assert.assertFalse(disabledMessage, manager.isEnabled(pluginName));
	}

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
	 * Test the semantic version comparison.
	 */
	@Test
	public void testVersionComparison() {
		// lower versions
		int compareResult = PluginManager.compareVersions("1.0.0-rc.1+build.1",
			"1.3.7+build.2.b8f12d7");
		boolean newer = PluginManager.isNewerVersion("1.0.0-rc.1+build.1",
			"1.3.7+build.2.b8f12d7");
		Assert.assertTrue(compareResult < 0);
		Assert.assertFalse(newer);

		compareResult = PluginManager.compareVersions("2.3.4-rc.1", "2.3.4");
		newer = PluginManager.isNewerVersion("2.3.4-rc.1", "2.3.4");
		Assert.assertTrue(compareResult < 0);
		Assert.assertFalse(newer);

		// equal versions
		compareResult = PluginManager.compareVersions("3.0.0", "3.0.0");
		newer = PluginManager.isNewerVersion("3.0.0", "3.0.0");
		Assert.assertEquals(0, compareResult);
		Assert.assertFalse(newer);
		compareResult =
			PluginManager.compareVersions("1.0.0-beta", "1.0.0-beta");
		newer = PluginManager.isNewerVersion("1.0.0-beta", "1.0.0-beta");
		Assert.assertEquals(0, compareResult);
		Assert.assertFalse(newer);
		compareResult = PluginManager.compareVersions("1.0.0-rc.1+build.1",
			"1.0.0-rc.1+build.2");
		newer = PluginManager.isNewerVersion("1.0.0-rc.1+build.1",
			"1.0.0-rc.1+build.2");
		Assert.assertEquals(0, compareResult);
		Assert.assertFalse(newer);

		// greater versions
		compareResult = PluginManager.compareVersions("3.0.0", "2.5.3");
		newer = PluginManager.isNewerVersion("3.0.0", "2.5.3");
		Assert.assertTrue(compareResult > 0);
		Assert.assertTrue(newer);
		compareResult = PluginManager.compareVersions("1.3.7+build.2.b8f12d7",
			"1.0.0-rc.1+build.1");
		newer = PluginManager.isNewerVersion("1.3.7+build.2.b8f12d7",
			"1.0.0-rc.1+build.1");
		Assert.assertTrue(compareResult > 0);
		Assert.assertTrue(newer);
	}
}
