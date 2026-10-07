package com.ikalagaming.plugins;

import static org.mockito.BDDMockito.*;

import com.ikalagaming.plugins.events.PluginCommandSent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.function.Consumer;

/**
 * Tests the plugin command listener class.
 *
 * @author Ches Burks
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestPluginCommandListener {

    private static final String INTENDED_COMMAND = "Expected";
    private static final String OTHER_COMMAND = "Unexpected";

    @Mock private PluginManager manager;
    @Mock private Consumer<List<String>> intendedConsumer;
    @Mock private Consumer<List<String>> otherConsumer;

    private PluginCommandListener listener;

    /** Sets up before each test. */
    @BeforeEach
    void setUp() {
        var owner = "UnitTests";
        given(manager.getCommands())
                .willReturn(
                        List.of(
                                new PluginCommand(OTHER_COMMAND, owner, otherConsumer),
                                new PluginCommand(INTENDED_COMMAND, owner, intendedConsumer)));

        listener = new PluginCommandListener(manager);
    }

    /** Tests that command names are matched ignoring case, like they are registered. */
    @Test
    void testPluginCommandIgnoresCase() {
        listener.onPluginCommand(new PluginCommandSent(INTENDED_COMMAND.toUpperCase()));

        verify(intendedConsumer).accept(List.of());
        verify(manager, never()).callbackHelp(any());
    }

    /** Tests that we properly forward commands with arguments. */
    @Test
    void testPluginCommandSentWithArguments() {
        var argument = "arg1";
        var event = new PluginCommandSent(INTENDED_COMMAND, List.of(argument));

        listener.onPluginCommand(event);

        verify(intendedConsumer).accept(List.of(argument));
        verify(otherConsumer, never()).accept(any());
    }

    /** Tests that we properly forward commands without arguments. */
    @Test
    void testPluginCommandSentWithoutArguments() {
        var event = new PluginCommandSent(INTENDED_COMMAND);

        listener.onPluginCommand(event);

        verify(intendedConsumer).accept(List.of());
        verify(otherConsumer, never()).accept(any());
    }

    /** Tests that an unknown command prints help instead of calling anything. */
    @Test
    void testUnknownCommandPrintsHelp() {
        listener.onPluginCommand(new PluginCommandSent("NotACommand"));

        verify(manager).callbackHelp(List.of());
        verify(intendedConsumer, never()).accept(any());
        verify(otherConsumer, never()).accept(any());
    }
}
