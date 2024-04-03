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

    @Mock private PluginManager manager;
    @Mock private PluginCommand intendedCommand;
    @Mock private Consumer<List<String>> intendedConsumer;

    @Mock private PluginCommand otherCommand;
    @Mock private Consumer<List<String>> otherConsumer;

    private PluginCommandListener listener;
    private String intendedCommandName;
    private String otherCommandName;

    /** Sets up before each test.. */
    @BeforeEach
    void setUp() {
        var owner = "UnitTests";

        intendedCommandName = "Expected";
        otherCommandName = "Unexpected";

        given(manager.getCommands()).willReturn(List.of(otherCommand, intendedCommand));

        given(otherCommand.getCommand()).willReturn(otherCommandName);
        given(otherCommand.getCallback()).willReturn(otherConsumer);
        given(otherCommand.getOwner()).willReturn(owner);
        given(intendedCommand.getCallback()).willReturn(intendedConsumer);
        given(intendedCommand.getCommand()).willReturn(intendedCommandName);
        given(intendedCommand.getOwner()).willReturn(owner);

        listener = new PluginCommandListener(manager);
    }

    /** Tests that we properly forward commands with arguments. */
    @Test
    void testPluginCommandSentWithArguments() {
        var argument = "arg1";
        var event = new PluginCommandSent(intendedCommandName, List.of(argument));

        listener.onPluginCommand(event);

        verify(intendedConsumer).accept(List.of(argument));
        verify(otherConsumer, never()).accept(List.of(argument));
    }

    /** Tests that we properly forward commands without arguments. */
    @Test
    void testPluginCommandSentWithoutArguments() {
        var event = new PluginCommandSent(intendedCommandName);

        listener.onPluginCommand(event);

        verify(intendedConsumer).accept(List.of());
        verify(otherConsumer, never()).accept(List.of());
    }
}
