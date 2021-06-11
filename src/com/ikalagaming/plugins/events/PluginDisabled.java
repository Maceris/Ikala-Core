package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.plugins.Plugin;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Fired when (after) a plugin is disabled.
 *
 * @author Ches Burks
 *
 */
@AllArgsConstructor
@Getter
public class PluginDisabled extends Event {

	/**
	 * The plugin which was just disabled.
	 * 
	 * @param The plugin that was disabled.
	 * @return The plugin that was disabled.
	 */
	private Plugin plugin;

}
