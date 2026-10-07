package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Fired when (after) a plugin is enabled.
 *
 * @author Ches Burks
 */
@AllArgsConstructor
@Getter
public class PluginEnabled extends Event {

    /**
     * The plugin that was just enabled.
     *
     * @return The name of the plugin that was enabled.
     */
    @SuppressWarnings("javadoc")
    @NonNull private final String plugin;
}
