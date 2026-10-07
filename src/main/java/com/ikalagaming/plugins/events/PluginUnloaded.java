package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Fired when (after) a plugin is unloaded.
 *
 * @author Ches Burks
 */
@AllArgsConstructor
@Getter
public class PluginUnloaded extends Event {

    /**
     * The plugin that was just unloaded.
     *
     * @return The name of the plugin that was unloaded.
     */
    @SuppressWarnings("javadoc")
    @NonNull private final String plugin;
}
