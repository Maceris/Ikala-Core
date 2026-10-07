package com.ikalagaming.plugins;

import lombok.NonNull;

import java.util.List;
import java.util.function.Consumer;

/**
 * A command that has been registered with the system. Command names are case-insensitive, so they
 * sort ignoring case, and only one command can be registered for a name regardless of case.
 *
 * @param command The command registered.
 * @param owner The name of the plugin that registered the command.
 * @param callback The function to call when the command is run, which is given the arguments.
 * @author Ches Burks
 */
public record PluginCommand(
        @NonNull String command, @NonNull String owner, @NonNull Consumer<List<String>> callback)
        implements Comparable<PluginCommand> {

    @Override
    public int compareTo(PluginCommand other) {
        return String.CASE_INSENSITIVE_ORDER.compare(command, other.command);
    }
}
