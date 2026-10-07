package com.ikalagaming.plugins;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.launcher.PluginFolder;
import com.ikalagaming.launcher.PluginFolder.ResourceType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tests that the Plugin Manager copes with plugins that misbehave, and with more than one version
 * of a plugin.
 *
 * @author Ches Burks
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestPluginRobustness {

    private static final String STANDALONE = "Standalone";
    private static final String TARGET = "Target";
    private static final String DEPENDENT = "Dependent";

    @Mock private EventManager eventManager;

    @TempDir Path folder;

    @TempDir Path upgradeFolder;

    private PluginManager pluginManager;

    @AfterEach
    void afterTest() {
        PluginManager.destroyInstance();
    }

    @BeforeEach
    void beforeTest() {
        RecordingPlugin.CALLS.clear();
        pluginManager = PluginManager.getInstance(eventManager);
        pluginManager.setEnableOnLoad(false);
    }

    /**
     * Delete the data folder for a plugin, and the main plugin folder if that leaves it empty.
     *
     * @param plugin The plugin whose folder we want to delete.
     */
    private void deletePluginFolder(String plugin) {
        PluginFolder.deleteFolder(plugin);
        Path pluginsFolder =
                PluginFolder.getResource(plugin, ResourceType.CONFIG, "")
                        .toPath()
                        .getParent()
                        .getParent();
        try {
            Files.deleteIfExists(pluginsFolder);
        } catch (IOException ignored) {
            // Not empty, so it was there before the tests
        }
    }

    @Test
    void testDisableExceptionIsContained() throws IOException {
        PluginJars.write(folder, "Disabler", ThrowingPlugins.InOnDisable.class);
        pluginManager.setEnableOnLoad(true);
        pluginManager.loadAllPlugins(folder.toString());
        assertTrue(pluginManager.isEnabled("Disabler"));

        assertFalse(assertDoesNotThrow(() -> pluginManager.disable("Disabler")));

        assertEquals(PluginState.CORRUPTED, pluginManager.getPluginState("Disabler"));
    }

    @Test
    void testDuplicateJarsLoadNewest() throws IOException {
        PluginJars.writeVersion(folder, "Dup-1.jar", "Dup", "1.0.0", RecordingPlugin.class);
        PluginJars.writeVersion(folder, "Dup-2.jar", "Dup", "2.0.0", RecordingPlugin.class);
        PluginJars.writeVersion(folder, "Dup-3.jar", "Dup", "1.5.0", RecordingPlugin.class);

        pluginManager.loadAllPlugins(folder.toString());

        assertEquals("2.0.0", pluginManager.getInfo("Dup").orElseThrow().getVersion());
        assertEquals(1, RecordingPlugin.CALLS.stream().filter("onLoad:Dup"::equals).count());
        // On Windows, the temp folder can't be deleted if the other jars were left open
    }

    @Test
    void testEnableExceptionIsContained() throws IOException {
        PluginJars.write(folder, TARGET, ThrowingPlugins.InOnEnable.class);
        PluginJars.write(folder, DEPENDENT, RecordingPlugin.class, TARGET);
        pluginManager.loadAllPlugins(folder.toString());

        assertFalse(assertDoesNotThrow(() -> pluginManager.enable(DEPENDENT)));

        assertEquals(PluginState.CORRUPTED, pluginManager.getPluginState(TARGET));
        assertEquals(PluginState.DISABLED, pluginManager.getPluginState(DEPENDENT));
        assertEquals(-1, RecordingPlugin.indexOf("onEnable", DEPENDENT));
    }

    @Test
    void testGetListenersExceptionIsContained() throws IOException {
        PluginJars.write(folder, "Listening", ThrowingPlugins.InGetListeners.class);
        pluginManager.loadAllPlugins(folder.toString());

        assertFalse(assertDoesNotThrow(() -> pluginManager.enable("Listening")));

        assertEquals(PluginState.CORRUPTED, pluginManager.getPluginState("Listening"));
    }

    @Test
    void testLoadExceptionsAreContained() throws IOException {
        PluginJars.write(folder, "Constructor", ThrowingPlugins.InConstructor.class);
        PluginJars.write(folder, "Static", ThrowingPlugins.InStaticInitializer.class);
        PluginJars.write(folder, "Loader", ThrowingPlugins.InOnLoad.class);
        PluginJars.write(folder, "Named", ThrowingPlugins.WrongName.class);
        PluginJars.write(folder, "NeedsLoader", RecordingPlugin.class, "Loader");
        PluginJars.write(folder, STANDALONE, RecordingPlugin.class);
        pluginManager.setEnableOnLoad(true);

        assertDoesNotThrow(() -> pluginManager.loadAllPlugins(folder.toString()));

        for (String plugin :
                new String[] {"Constructor", "Static", "Loader", "Named", "NeedsLoader"}) {
            assertFalse(pluginManager.isLoaded(plugin), plugin + " should not have loaded");
        }
        assertTrue(RecordingPlugin.indexOf("onLoad", "Loader") >= 0);
        assertTrue(pluginManager.isEnabled(STANDALONE));
    }

    @Test
    void testOutdatedVersionSkipped() throws IOException {
        PluginJars.writeVersion(folder, "Target-2.jar", TARGET, "2.0.0", RecordingPlugin.class);
        PluginJars.writeVersion(
                upgradeFolder, "Target-1.jar", TARGET, "1.0.0", RecordingPlugin.class);
        pluginManager.loadAllPlugins(folder.toString());
        Plugin original = pluginManager.getPlugin(TARGET).orElseThrow();

        pluginManager.loadPlugin(upgradeFolder.toString(), TARGET);

        assertEquals("2.0.0", pluginManager.getInfo(TARGET).orElseThrow().getVersion());
        assertSame(original, pluginManager.getPlugin(TARGET).orElseThrow());
    }

    @Test
    void testUnloadExceptionIsContained() throws IOException {
        PluginJars.write(folder, "Unloader", ThrowingPlugins.InOnUnload.class);
        pluginManager.loadAllPlugins(folder.toString());

        assertFalse(assertDoesNotThrow(() -> pluginManager.unloadPlugin("Unloader")));

        assertEquals(PluginState.CORRUPTED, pluginManager.getPluginState("Unloader"));
    }

    @Test
    void testUpgradeFailureIsRetried() throws IOException {
        PluginJars.write(folder, "Upgrader", ThrowingPlugins.InOnUpgrade.class);
        PluginJars.write(folder, "Upgradable", RecordingPlugin.class);
        pluginManager.setCommandLine(true);
        try {
            pluginManager.loadAllPlugins(folder.toString());

            assertFalse(pluginManager.isLoaded("Upgrader"));
            // Not recorded, so the upgrade is attempted again next time
            assertEquals("0.0.0", PluginFolder.getLastVersionUsed("Upgrader"));

            assertTrue(pluginManager.isLoaded("Upgradable"));
            assertEquals("0.0.1", PluginFolder.getLastVersionUsed("Upgradable"));
        } finally {
            PluginManager.destroyInstance();
            deletePluginFolder("Upgrader");
            deletePluginFolder("Upgradable");
        }
    }

    @Test
    void testUpgradeReloadsDependents() throws IOException {
        PluginJars.writeVersion(folder, "Target-1.jar", TARGET, "1.0.0", RecordingPlugin.class);
        PluginJars.write(folder, DEPENDENT, RecordingPlugin.class, TARGET);
        PluginJars.writeVersion(
                upgradeFolder, "Target-2.jar", TARGET, "2.0.0", RecordingPlugin.class);
        pluginManager.loadAllPlugins(folder.toString());
        assertTrue(pluginManager.enable(DEPENDENT));
        RecordingPlugin.CALLS.clear();

        assertTrue(pluginManager.loadPlugin(upgradeFolder.toString(), TARGET));

        assertEquals("2.0.0", pluginManager.getInfo(TARGET).orElseThrow().getVersion());
        // The dependent was unloaded first, came back, and was enabled again like before
        assertTrue(
                RecordingPlugin.indexOf("onUnload", DEPENDENT)
                        < RecordingPlugin.indexOf("onUnload", TARGET));
        assertTrue(RecordingPlugin.indexOf("onLoad", DEPENDENT) >= 0);
        assertTrue(pluginManager.isEnabled(TARGET));
        assertTrue(pluginManager.isEnabled(DEPENDENT));
    }
}
