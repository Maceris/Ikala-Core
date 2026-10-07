package com.ikalagaming.plugins;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.inOrder;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.plugins.events.PluginDisabled;
import com.ikalagaming.plugins.events.PluginEnabled;
import com.ikalagaming.plugins.events.PluginLoaded;
import com.ikalagaming.plugins.events.PluginUnloaded;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Tests for plugin commands, and the events the plugin manager fires.
 *
 * @author Ches Burks
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestCommandsAndEvents {

    private static final String OWNER = "CommandOwner";

    @Mock private EventManager eventManager;

    @TempDir Path folder;

    private PluginManager pluginManager;

    @AfterEach
    void afterTest() {
        PluginManager.destroyInstance();
    }

    @BeforeEach
    void beforeTest() {
        pluginManager = PluginManager.getInstance(eventManager);
        pluginManager.setEnableOnLoad(false);
    }

    @Test
    void testCommandNamesIgnoreCase() {
        assertTrue(pluginManager.registerCommand("DoThing", args -> {}, OWNER));

        assertFalse(pluginManager.registerCommand("dothing", args -> {}, "Other"));
        assertTrue(pluginManager.isCommandRegistered("DOTHING"));
        assertTrue(pluginManager.unregisterCommand("doTHING"));
        assertFalse(pluginManager.isCommandRegistered("DoThing"));
    }

    @Test
    void testLifecycleEventsUseInjectedEventManager() throws IOException {
        PluginJars.write(folder, OWNER, RecordingPlugin.class);

        pluginManager.loadAllPlugins(folder.toString());
        pluginManager.enable(OWNER);
        pluginManager.disable(OWNER);
        pluginManager.unloadPlugin(OWNER);

        InOrder order = inOrder(eventManager);
        order.verify(eventManager).fireEvent(isA(PluginLoaded.class));
        order.verify(eventManager).fireEvent(isA(PluginEnabled.class));
        order.verify(eventManager).fireEvent(isA(PluginDisabled.class));
        order.verify(eventManager).fireEvent(isA(PluginUnloaded.class));
    }

    @Test
    void testPluginCommandsRemovedOnUnload() throws IOException {
        PluginJars.write(folder, OWNER, RecordingPlugin.class);
        pluginManager.loadAllPlugins(folder.toString());
        pluginManager.registerCommand("first", args -> {}, OWNER);
        pluginManager.registerCommand("second", args -> {}, OWNER);
        pluginManager.registerCommand("unrelated", args -> {}, "Other");

        assertTrue(pluginManager.unloadPlugin(OWNER));

        assertFalse(pluginManager.isCommandRegistered("first"));
        assertFalse(pluginManager.isCommandRegistered("second"));
        assertTrue(pluginManager.isCommandRegistered("unrelated"));
    }

    @Test
    void testUnregisterPluginCommands() {
        pluginManager.registerCommand("first", args -> {}, OWNER);
        pluginManager.registerCommand("second", args -> {}, OWNER);
        pluginManager.registerCommand("unrelated", args -> {}, "Other");

        // This used to modify the list while streaming over it
        assertDoesNotThrow(() -> pluginManager.unregisterPluginCommands(OWNER));

        assertFalse(pluginManager.isCommandRegistered("first"));
        assertFalse(pluginManager.isCommandRegistered("second"));
        assertTrue(pluginManager.isCommandRegistered("unrelated"));
    }
}
