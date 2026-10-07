package com.ikalagaming.plugins;

import com.ikalagaming.event.EventHandler;
import com.ikalagaming.event.Listener;
import com.ikalagaming.plugins.events.PluginCommandSent;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * The event listener for the plugin command system.
 *
 * @author Ches Burks
 */
@RequiredArgsConstructor
class PluginCommandListener implements Listener {

    private final PluginManager manager;

    /**
     * Handles processing of Plugin Commands. Command names are case-insensitive, matching how they
     * are registered. If nobody registered the command, the help text is printed.
     *
     * @param event The plugin command that was sent.
     */
    @EventHandler
    public void onPluginCommand(PluginCommandSent event) {
        Optional<PluginCommand> command =
                manager.getCommands().stream()
                        .filter(cmd -> cmd.command().equalsIgnoreCase(event.getCommand()))
                        .findFirst();
        if (command.isEmpty()) {
            manager.callbackHelp(List.of());
            return;
        }
        command.get().callback().accept(event.getArguments());
    }
}
