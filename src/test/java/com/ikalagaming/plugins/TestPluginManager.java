package com.ikalagaming.plugins;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ikalagaming.event.EventAssert;
import com.ikalagaming.event.EventManager;
import com.ikalagaming.plugins.events.PluginCommandSent;
import com.ikalagaming.util.SafeResourceLoader;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tests for the Plugin Manager.
 *
 * @author Ches Burks
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestPluginManager {

    private static enum VersionComparison {
        NEWER,
        EQUAL,
        OLDER;
    }

    private static Stream<Arguments> versionProvider() {
        return Stream.of(
                Arguments.of(
                        "1.0.0-rc.1+build.1", "1.3.7+build.2.b8f12d7", VersionComparison.OLDER),
                Arguments.of("2.3.4-rc.1", "2.3.4", VersionComparison.OLDER),
                Arguments.of("3.0.0", "3.0.0", VersionComparison.EQUAL),
                Arguments.of("1.0.0-beta", "1.0.0-beta", VersionComparison.EQUAL),
                Arguments.of("1.0.0-rc.1+build.1", "1.0.0-rc.1+build.2", VersionComparison.EQUAL),
                Arguments.of("3.0.0", "2.5.3", VersionComparison.NEWER),
                Arguments.of(
                        "1.3.7+build.2.b8f12d7", "1.0.0-rc.1+build.1", VersionComparison.NEWER));
    }

    @Mock private EventManager eventManager;

    private PluginManager pluginManager;

    /** The resources folder where the test jars are located. */
    private final String TEST_JAR_FOLDER =
            System.getProperty("user.dir")
                    + File.separatorChar
                    + "build"
                    + File.separatorChar
                    + "resources"
                    + File.separatorChar
                    + "test"
                    + File.separatorChar
                    + "com"
                    + File.separatorChar
                    + "ikalagaming"
                    + File.separatorChar
                    + "plugins";

    /** The names of plugins that should be loaded successfully. */
    private final String[] VALID_PLUGINS = {
        "TestComplexChainA",
        "TestComplexChainB",
        "TestComplexChainC",
        "TestComplexChainD",
        "TestCycleA",
        "TestCycleB",
        "TestDependA",
        "TestDependB",
        "TestDependC",
        "TestDependD",
        "TestSoftDependA",
        "TestSoftDependB",
        "TestSoftDependC",
        "TestSoftDependD",
        "TestSoftDependCycleA",
        "TestSoftDependCycleB",
        "TestSoftDependCycleC",
        "TestStandalone"
    };

    /** The names of plugins that have errors and should not load successfully. */
    private final String[] INVALID_PLUGINS = {
        "TestError.InvalidName",
        "TestErrorInvalidVersion",
        "TestErrorMainClassType",
        "TestErrorNoMainClass",
        "TestErrorNoMainClassFile",
        "TestErrorNoName",
        "TestErrorNoVersion",
        "TestErrorNoYml"
    };

    /** Tear down after each test, shutting down the framework. */
    @AfterEach
    void afterTest() {
        PluginManager.destroyInstance();
    }

    /** Set up before each test, initializing the framework. */
    @BeforeEach
    void beforeTest() {
        pluginManager = PluginManager.getInstance(eventManager);
    }

    /** Clears all plugin commands and checks that they're gone. */
    @Test
    void testCommandClearing() {
        List<String> commands =
                Stream.of(
                                "COMMAND_ENABLE",
                                "COMMAND_DISABLE",
                                "COMMAND_DISABLE",
                                "COMMAND_LOAD",
                                "COMMAND_UNLOAD",
                                "COMMAND_RELOAD",
                                "COMMAND_LIST_PLUGINS",
                                "COMMAND_HELP")
                        .map(
                                cmd ->
                                        SafeResourceLoader.getString(
                                                cmd, pluginManager.getResourceBundle()))
                        .collect(Collectors.toList());

        assertTrue(
                commands.stream()
                        .map(pluginManager::isCommandRegistered)
                        .allMatch(value -> true == value));

        pluginManager.clearCommands();

        assertTrue(
                commands.stream()
                        .map(pluginManager::isCommandRegistered)
                        .allMatch(value -> false == value));
    }

    /** Test that command registration and unregistration works. */
    @Test
    void testCommandRegistration() {
        final String commandName = "TestCommand";

        Consumer<List<String>> callback = foo -> {};

        pluginManager.registerCommand(commandName, callback, "UnitTests");

        assertTrue(pluginManager.isCommandRegistered(commandName));

        pluginManager.unregisterCommand(commandName);

        assertFalse(pluginManager.isCommandRegistered(commandName));
    }

    /** Tests the lifecycle of loading, enabling, disabling, and unloading a plugin. */
    @Test
    void testLifecycle() {
        final String pluginName = "TestStandalone";

        pluginManager.setEnableOnLoad(false);
        assertFalse(pluginManager.isEnableOnLoad());

        assertTrue(pluginManager.loadPlugin(this.TEST_JAR_FOLDER, pluginName));

        String loadedMessage = String.format("Plugin '%s' should be loaded.", pluginName);
        assertTrue(pluginManager.isLoaded(pluginName), loadedMessage);

        assertTrue(pluginManager.enable(pluginName));

        String enabledMessage = String.format("Plugin '%s' should be enabled", pluginName);
        assertTrue(pluginManager.isLoaded(pluginName), loadedMessage);
        assertTrue(pluginManager.isEnabled(pluginName), enabledMessage);

        assertTrue(pluginManager.disable(pluginName));

        String disabledMessage = String.format("Plugin '%s' should not be enabled", pluginName);
        assertTrue(pluginManager.isLoaded(pluginName), loadedMessage);
        assertFalse(pluginManager.isEnabled(pluginName), disabledMessage);

        String unloadedMessage = String.format("Plugin '%s' should not be loaded.", pluginName);
        assertTrue(pluginManager.unloadPlugin(pluginName));
        assertFalse(pluginManager.isLoaded(pluginName), unloadedMessage);
        assertFalse(pluginManager.isEnabled(pluginName), disabledMessage);
    }

    /**
     * Tests the lifecycle of enabling, disabling, and unloading a plugin using the plugin commands.
     * Load and reload are skipped as it expects a standard location that unit tests don't support.
     */
    @Test
    void testLifecycleCommands() {
        final String pluginName = "TestStandalone";

        var realManager = new PluginManager(EventManager.getInstance());

        realManager.setEnableOnLoad(false);

        String enable =
                SafeResourceLoader.getString("COMMAND_ENABLE", realManager.getResourceBundle());
        String disable =
                SafeResourceLoader.getString("COMMAND_DISABLE", realManager.getResourceBundle());
        String unload =
                SafeResourceLoader.getString("COMMAND_UNLOAD", realManager.getResourceBundle());

        final List<String> args = List.of(pluginName);
        assertTrue(realManager.loadPlugin(this.TEST_JAR_FOLDER, pluginName));

        EventAssert.listenFor(PluginCommandSent.class);

        String loadedMessage = String.format("Plugin '%s' should be loaded.", pluginName);
        assertTrue(realManager.isLoaded(pluginName), loadedMessage);

        EventAssert.resetFireCount(PluginCommandSent.class);
        new PluginCommandSent(enable, args).fire();
        Awaitility.await()
                .atMost(1000, TimeUnit.MILLISECONDS)
                .until(() -> EventAssert.wasFired(PluginCommandSent.class));

        String enabledMessage = String.format("Plugin '%s' should be enabled", pluginName);
        assertTrue(realManager.isLoaded(pluginName), loadedMessage);
        assertTrue(realManager.isEnabled(pluginName), enabledMessage);

        EventAssert.resetFireCount(PluginCommandSent.class);
        new PluginCommandSent(disable, args).fire();
        Awaitility.await()
                .atMost(1000, TimeUnit.MILLISECONDS)
                .until(() -> EventAssert.wasFired(PluginCommandSent.class));

        String disabledMessage = String.format("Plugin '%s' should not be enabled", pluginName);
        assertTrue(realManager.isLoaded(pluginName), loadedMessage);
        assertFalse(realManager.isEnabled(pluginName), disabledMessage);

        EventAssert.resetFireCount(PluginCommandSent.class);
        new PluginCommandSent(unload, args).fire();
        Awaitility.await()
                .atMost(1000, TimeUnit.MILLISECONDS)
                .until(() -> EventAssert.wasFired(PluginCommandSent.class));

        String unloadedMessage = String.format("Plugin '%s' should not be loaded.", pluginName);
        assertFalse(realManager.isLoaded(pluginName), unloadedMessage);
        assertFalse(realManager.isEnabled(pluginName), disabledMessage);
        EventManager.destroyInstance();
    }

    /**
     * The basic suite that tries to load all the test plugins and sees if the loaded or were
     * skipped correctly.
     */
    @Test
    void testLoadingPlugins() {
        pluginManager.loadAllPlugins(this.TEST_JAR_FOLDER);

        for (String name : this.VALID_PLUGINS) {
            String message = String.format("Plugin '%s' should have been loaded.", name);
            assertTrue(pluginManager.isLoaded(name), message);
        }

        for (String name : this.INVALID_PLUGINS) {
            String message = String.format("Plugin '%s' should not have been loaded.", name);
            assertFalse(pluginManager.isLoaded(name), message);
        }
    }

    /** Tests the ability to load a set of plugins by name. */
    @Test
    void testLoadingPluginSubset() {
        List<String> toLoad =
                Arrays.asList(
                        "TestComplexChainA",
                        "TestComplexChainB",
                        "TestComplexChainC",
                        "TestComplexChainD");

        pluginManager.loadPlugins(this.TEST_JAR_FOLDER, toLoad);

        for (String name : toLoad) {
            String message = String.format("Plugin '%s' should have been loaded.", name);
            assertTrue(pluginManager.isLoaded(name), message);
        }
    }

    /** Tests that loading a single plugin works. */
    @Test
    void testLoadPlugin() {
        final String pluginName = "TestStandalone";

        pluginManager.loadPlugin(this.TEST_JAR_FOLDER, pluginName);

        String message = String.format("Plugin '%s' should have been loaded.", pluginName);
        assertTrue(pluginManager.isLoaded(pluginName), message);
    }

    /**
     * Test the semantic version comparison.
     *
     * @param firstVersion The first version to use in comparison.
     * @param secondVersion The second version to use in comparison.
     * @param expectedResult How the first version should compare to the second.
     */
    @ParameterizedTest
    @MethodSource("versionProvider")
    void testVersionComparison(
            final String firstVersion,
            final String secondVersion,
            final VersionComparison expectedResult) {

        // lower versions
        int comparison = PluginManager.compareVersions(firstVersion, secondVersion);
        boolean newer = PluginManager.isNewerVersion(firstVersion, secondVersion);

        var resultMatches =
                switch (expectedResult) {
                    case EQUAL -> comparison == 0;
                    case NEWER -> comparison > 0;
                    case OLDER -> comparison < 0;
                };

        var newerMatches =
                switch (expectedResult) {
                    case NEWER -> newer;
                    case EQUAL, OLDER -> !newer;
                };

        assertTrue(resultMatches);
        assertTrue(newerMatches);
    }
}
