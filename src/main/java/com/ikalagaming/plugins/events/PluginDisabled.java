package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Fired when (after) a plugin is disabled.
 *
 * @author Ches Burks
 */
@AllArgsConstructor
@Getter
public class PluginDisabled extends Event {

    /**
     * The plugin which was just disabled.
     *
     * @param The name of the plugin that was disabled.
     * @return The name of the plugin that was disabled.
     */
    @SuppressWarnings("javadoc")
    private String plugin;
}
