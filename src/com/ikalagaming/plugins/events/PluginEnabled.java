package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.plugins.Plugin;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Fired when (after) a plugin is enabled.
 *
 * @author Ches Burks
 *
 */
@AllArgsConstructor
public class PluginEnabled extends Event {

	/**
	 * The plugin that was just enabled.
	 * 
	 * @return The plugin that was enabled.
	 */
	@Getter
	private Plugin plugin;

}
