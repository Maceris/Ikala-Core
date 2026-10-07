package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Fired when (after) a plugin is disabled.
 *
 * @author Ches Burks
 */
@AllArgsConstructor
@Getter
public class PluginDisabled extends Event {

    /**
     * The plugin that was just disabled.
     *
     * @return The name of the plugin that was disabled.
     */
    @SuppressWarnings("javadoc")
    @NonNull private final String plugin;
}
