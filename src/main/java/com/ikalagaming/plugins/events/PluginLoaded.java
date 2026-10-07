package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Fired when (after) a plugin is loaded.
 *
 * @author Ches Burks
 */
@AllArgsConstructor
@Getter
public class PluginLoaded extends Event {

    /**
     * The plugin that was just loaded.
     *
     * @return The name of the plugin that was loaded.
     */
    @SuppressWarnings("javadoc")
    @NonNull private final String plugin;
}
