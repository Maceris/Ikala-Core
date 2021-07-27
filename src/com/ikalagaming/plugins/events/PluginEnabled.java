package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;

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
	 * @param plugin The name of the plugin that was enabled.
	 * @return The name of the plugin that was enabled.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private String plugin;

}
