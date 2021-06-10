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
public class PluginDisabled extends Event {

	/**
	 * The plugin which was just disabled.
	 */
	@Getter
	private Plugin plugin;

}
