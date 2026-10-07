package com.ikalagaming.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.event.Listener;

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
import java.nio.file.Path;

/**
 * Tests for how the Plugin Manager handles dependencies between plugins while loading, unloading,
 * enabling, disabling, and reloading.
 *
 * @author Ches Burks
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestPluginDependencies {

    private static final String TARGET = "Target";
    private static final String MIDDLE = "Middle";
    private static final String TOP = "Top";

    @Mock private EventManager eventManager;

    @TempDir Path folder;

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
     * Assert that the callback happened for each of the plugins, in the order they are listed.
     *
     * @param callback The lifecycle method, like "onLoad".
     * @param plugins The plugins in the order we expect.
     */
    private void assertCalledInOrder(String callback, String... plugins) {
        int previous = -1;
        for (String plugin : plugins) {
            int index = RecordingPlugin.indexOf(callback, plugin);
            assertTrue(index >= 0, callback + " was never called for " + plugin);
            assertTrue(
                    index > previous,
                    callback
                            + " for "
                            + plugin
                            + " happened out of order: "
                            + RecordingPlugin.CALLS);
            previous = index;
        }
    }

    /**
     * Top depends on Middle and Target, Middle depends on Target. Reversing a breadth-first search
     * from Target can put Middle before Top, which is wrong since Top depends on Middle.
     *
     * @throws IOException If the jars can't be written.
     */
    private void writeDiamond() throws IOException {
        PluginJars.write(folder, TARGET, RecordingPlugin.class);
        PluginJars.write(folder, MIDDLE, RecordingPlugin.class, TARGET);
        PluginJars.write(folder, TOP, RecordingPlugin.class, TARGET, MIDDLE);
    }

    @Test
    void testDisableDisablesDependentsFirst() throws IOException {
        writeDiamond();
        pluginManager.setEnableOnLoad(true);
        pluginManager.loadAllPlugins(folder.toString());
        assertTrue(pluginManager.isEnabled(TARGET));
        assertTrue(pluginManager.isEnabled(MIDDLE));
        assertTrue(pluginManager.isEnabled(TOP));

        assertTrue(pluginManager.disable(TARGET));

        assertCalledInOrder("onDisable", TOP, MIDDLE, TARGET);
        for (String plugin : new String[] {TARGET, MIDDLE, TOP}) {
            assertTrue(pluginManager.isLoaded(plugin));
            assertFalse(pluginManager.isEnabled(plugin));
        }
    }

    @Test
    void testEnableOnLoadEnablesDependenciesFirst() throws IOException {
        writeDiamond();
        pluginManager.setEnableOnLoad(true);
        pluginManager.loadAllPlugins(folder.toString());

        assertCalledInOrder("onEnable", TARGET, MIDDLE, TOP);
    }

    @Test
    void testFailedLoadRemovesDependents() throws IOException {
        PluginJars.write(folder, "Broken", FailingLoadPlugin.class);
        PluginJars.write(folder, "NeedsBroken", RecordingPlugin.class, "Broken");
        PluginJars.write(folder, "Standalone", RecordingPlugin.class);
        pluginManager.setEnableOnLoad(true);

        pluginManager.loadAllPlugins(folder.toString());

        assertFalse(pluginManager.isLoaded("Broken"));
        assertFalse(pluginManager.isLoaded("NeedsBroken"));
        assertTrue(pluginManager.isEnabled("Standalone"));

        assertTrue(RecordingPlugin.indexOf("onLoad", "Broken") >= 0);
        // Never loaded, so it should not be told to load, enable, or unload
        assertEquals(-1, RecordingPlugin.indexOf("onLoad", "NeedsBroken"));
        assertEquals(-1, RecordingPlugin.indexOf("onEnable", "NeedsBroken"));
        assertEquals(-1, RecordingPlugin.indexOf("onUnload", "NeedsBroken"));
        assertEquals(-1, RecordingPlugin.indexOf("onEnable", "Broken"));

        verify(eventManager, never()).registerEventListeners(FailingLoadPlugin.LISTENER);
    }

    @Test
    void testMissingTransitiveDependency() throws IOException {
        /*
         * Whether a dependent is checked before its dependency depends on map
         * iteration order, so use several chains to cover both orders.
         */
        final int chains = 6;
        for (int i = 0; i < chains; ++i) {
            PluginJars.write(folder, "Alpha" + i, RecordingPlugin.class, "Omega" + i);
            PluginJars.write(folder, "Omega" + i, RecordingPlugin.class, "Missing");
        }
        PluginJars.write(folder, "Standalone", RecordingPlugin.class);

        pluginManager.loadAllPlugins(folder.toString());

        for (int i = 0; i < chains; ++i) {
            assertFalse(
                    pluginManager.isLoaded("Omega" + i), "Omega" + i + " is missing a dependency");
            assertFalse(
                    pluginManager.isLoaded("Alpha" + i),
                    "Alpha" + i + " depends on Omega" + i + ", which is missing a dependency");
        }
        assertTrue(pluginManager.isLoaded("Standalone"));
    }

    @Test
    void testReloadNotLoaded() {
        assertFalse(pluginManager.reload("NotAPlugin"));
    }

    @Test
    void testReloadRestoresEnabledPlugins() throws IOException {
        PluginJars.write(folder, TARGET, RecordingPlugin.class);
        PluginJars.write(folder, MIDDLE, RecordingPlugin.class, TARGET);
        pluginManager.setEnableOnLoad(true);
        pluginManager.loadAllPlugins(folder.toString());
        RecordingPlugin.CALLS.clear();

        assertTrue(pluginManager.reload(TARGET));

        assertTrue(pluginManager.isEnabled(TARGET));
        assertTrue(pluginManager.isEnabled(MIDDLE));
        assertCalledInOrder("onDisable", MIDDLE, TARGET);
        assertCalledInOrder("onUnload", MIDDLE, TARGET);
        assertCalledInOrder("onEnable", TARGET, MIDDLE);
        assertTrue(
                RecordingPlugin.indexOf("onLoad", TARGET)
                        > RecordingPlugin.indexOf("onUnload", TARGET));
    }

    @Test
    void testReloadRestoresPreviousState() throws IOException {
        PluginJars.write(folder, TARGET, RecordingPlugin.class);
        PluginJars.write(folder, MIDDLE, RecordingPlugin.class, TARGET);
        pluginManager.loadAllPlugins(folder.toString());
        pluginManager.enable(TARGET);
        RecordingPlugin.CALLS.clear();

        assertTrue(pluginManager.reload(TARGET));

        // The target was enabled and the dependent was not, so that should still be true
        assertTrue(pluginManager.isEnabled(TARGET));
        assertTrue(pluginManager.isLoaded(MIDDLE));
        assertFalse(pluginManager.isEnabled(MIDDLE));
        assertCalledInOrder("onUnload", MIDDLE, TARGET);
        assertCalledInOrder("onLoad", TARGET, MIDDLE);
        assertEquals(-1, RecordingPlugin.indexOf("onEnable", MIDDLE));
    }

    @Test
    void testUnloadUnloadsDependentsFirst() throws IOException {
        writeDiamond();
        pluginManager.loadAllPlugins(folder.toString());
        assertTrue(pluginManager.isLoaded(TARGET));
        assertTrue(pluginManager.isLoaded(MIDDLE));
        assertTrue(pluginManager.isLoaded(TOP));

        assertTrue(pluginManager.unloadPlugin(TARGET));

        assertCalledInOrder("onUnload", TOP, MIDDLE, TARGET);
        assertFalse(pluginManager.isLoaded(TARGET));
        assertFalse(pluginManager.isLoaded(MIDDLE));
        assertFalse(pluginManager.isLoaded(TOP));
    }

    @Test
    void testEnableEnablesDependenciesFirst() throws IOException {
        writeDiamond();
        pluginManager.loadAllPlugins(folder.toString());

        assertTrue(pluginManager.enable(TOP));

        assertCalledInOrder("onEnable", TARGET, MIDDLE, TOP);
        assertTrue(pluginManager.isEnabled(TARGET));
        assertTrue(pluginManager.isEnabled(MIDDLE));
        assertTrue(pluginManager.isEnabled(TOP));
    }

    @Test
    void testEnableHardDependencyCycle() throws IOException {
        PluginJars.write(folder, "CycleA", RecordingPlugin.class, "CycleB");
        PluginJars.write(folder, "CycleB", RecordingPlugin.class, "CycleA");
        pluginManager.loadAllPlugins(folder.toString());

        assertTrue(pluginManager.enable("CycleA"));

        assertTrue(pluginManager.isEnabled("CycleA"));
        assertTrue(pluginManager.isEnabled("CycleB"));
    }

    @Test
    void testEnableStopsWhenDependencyFails() throws IOException {
        PluginJars.write(folder, TARGET, FailingEnablePlugin.class);
        PluginJars.write(folder, MIDDLE, ListeningPlugin.class, TARGET);
        pluginManager.loadAllPlugins(folder.toString());
        ListeningPlugin target = (ListeningPlugin) pluginManager.getPlugin(TARGET).orElseThrow();
        ListeningPlugin middle = (ListeningPlugin) pluginManager.getPlugin(MIDDLE).orElseThrow();

        assertFalse(pluginManager.enable(MIDDLE));

        assertEquals(PluginState.CORRUPTED, pluginManager.getPluginState(TARGET));
        assertFalse(pluginManager.isEnabled(MIDDLE));
        assertEquals(-1, RecordingPlugin.indexOf("onEnable", MIDDLE));

        // The dependency is now corrupted, so it should not even be attempted again
        RecordingPlugin.CALLS.clear();
        assertFalse(pluginManager.enable(MIDDLE));
        assertTrue(RecordingPlugin.CALLS.isEmpty(), RecordingPlugin.CALLS.toString());

        verify(eventManager, never()).registerEventListeners(target.getListener());
        verify(eventManager, never()).registerEventListeners(middle.getListener());
    }

    @Test
    void testListenersOnlyRegisteredWhileEnabled() throws IOException {
        PluginJars.write(folder, "Listening", ListeningPlugin.class);
        pluginManager.loadAllPlugins(folder.toString());
        Listener listener =
                ((ListeningPlugin) pluginManager.getPlugin("Listening").orElseThrow())
                        .getListener();

        // Loaded but disabled, so it should not receive events
        verify(eventManager, never()).registerEventListeners(listener);

        assertTrue(pluginManager.enable("Listening"));
        verify(eventManager, times(1)).registerEventListeners(listener);
        verify(eventManager, never()).unregisterEventListeners(listener);

        assertTrue(pluginManager.disable("Listening"));
        verify(eventManager, times(1)).unregisterEventListeners(listener);

        assertTrue(pluginManager.enable("Listening"));
        verify(eventManager, times(2)).registerEventListeners(listener);

        // Unloading an enabled plugin disables it, which unregisters the listener
        assertTrue(pluginManager.unloadPlugin("Listening"));
        verify(eventManager, times(2)).unregisterEventListeners(listener);
        verify(eventManager, times(2)).registerEventListeners(listener);
    }
}
