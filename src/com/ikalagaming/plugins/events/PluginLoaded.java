package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.plugins.Plugin;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Fired when (after) a plugin is loaded.
 *
 * @author Ches Burks
 *
 */
@AllArgsConstructor
@Getter
public class PluginLoaded extends Event {

	/**
	 * The plugin that was just loaded.
	 * 
	 * @param The plugin that was loaded.
	 * @return The plugin that was loaded.
	 */
	private Plugin plugin;

}
