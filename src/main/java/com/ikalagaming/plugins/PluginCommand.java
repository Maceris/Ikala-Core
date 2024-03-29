package com.ikalagaming.plugins;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Consumer;

/**
 * A command that has been registered with the system. This contains a string representing the
 * command and a reference to the owner of the command.
 *
 * @author Ches Burks
 */
@AllArgsConstructor
@Getter
public class PluginCommand implements Comparable<PluginCommand> {
    /**
     * The callback functional to call when the command is run.
     *
     * @param callback The callback function for the command.
     * @return The callback function for the command.
     */
    @SuppressWarnings("javadoc")
    private final Consumer<List<String>> callback;

    /**
     * The command registered.
     *
     * @param command The command registered.
     * @return The command registered.
     */
    @SuppressWarnings("javadoc")
    private final String command;

    /**
     * The plugin that this command was registered to.
     *
     * @param owner The name of the owner of the plugin.
     * @return The name of the owner of the plugin.
     */
    @SuppressWarnings("javadoc")
    private final String owner;

    @Override
    public int compareTo(PluginCommand o) {
        return getCommand().toLowerCase().compareTo(o.getCommand().toLowerCase());
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (!(obj instanceof PluginCommand)) {
            return super.equals(obj);
        }

        String otherCommand = ((PluginCommand) obj).getCommand();
        if (null == command) {
            return null == otherCommand;
        }

        return command.equalsIgnoreCase(otherCommand);
    }

    @Override
    public int hashCode() {
        if (null == command) {
            return super.hashCode();
        }
        return command.hashCode();
    }
}
