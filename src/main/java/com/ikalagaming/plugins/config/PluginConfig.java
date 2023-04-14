package com.ikalagaming.plugins.config;

import lombok.NonNull;

/**
 * Plugin configuration.
 * 
 * @author Ches Burks
 *
 */
public class PluginConfig {

	public boolean getBoolean(@NonNull String key) {
		return false;
	}

	public int getInt(@NonNull String key) {
		return 0;
	}

	public String getString(@NonNull String key) {
		return "";
	}
}
