package com.ikalagaming.plugins.events;

import com.ikalagaming.event.Event;
import com.ikalagaming.plugins.Plugin;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Fired when (after) a plugin is unloaded.
 *
 * @author Ches Burks
 *
 */
@AllArgsConstructor
public class PluginUnloaded extends Event {

	/**
	 * The plugin that was just unloaded.
	 */
	@Getter
	private Plugin thePlugin;

}
