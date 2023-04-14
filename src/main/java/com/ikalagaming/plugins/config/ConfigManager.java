package com.ikalagaming.plugins.config;

import com.ikalagaming.launcher.PluginFolder;
import com.ikalagaming.launcher.PluginFolder.ResourceType;
import com.ikalagaming.plugins.Plugin;

import lombok.NonNull;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;

/**
 * Handles reading, writing, and caching configurations.
 * 
 * @author Ches Burks
 *
 */
public class ConfigManager {

	/**
	 * The name of the default configuration file.
	 */
	public static final String DEFAULT_NAME = "config.yml";

	private PluginConfig emptyConfig() {
		return null;
	}

	private static PluginConfig getDefaultConfig(@NonNull String pluginName) {

		return null;
	}

	/**
	 * Save a copy of the default configuration file if it is missing. This will
	 * not not overwrite an existing configuration.
	 * 
	 * @param owner The plugin that owns the configuration.
	 * @param configName The name of the configuration file.
	 */
	public static void saveDefaultConfig(@NonNull Plugin owner,
		@NonNull String configName) {
		try (
			InputStream in = owner.getClass().getResourceAsStream(configName)) {
			if (in == null) {
				return;
			}

			File target = PluginFolder.getResource(owner.getName(),
				ResourceType.CONFIG, configName);
			if (target.exists()) {
				return;
			}

			Files.copy(in, target.toPath());
		}
		catch (IOException ignored) {
			// No default config exists
		}
	}

	public static PluginConfig getConfig(@NonNull String pluginName) {
		return getConfig(pluginName, DEFAULT_NAME);
	}

	public static PluginConfig getConfig(@NonNull String pluginName,
		@NonNull String configName) {
		
		File configFile = PluginFolder.getResource(pluginName,
			ResourceType.CONFIG, configName);
		if (!configFile.exists()) {
			return getDefaultConfig(pluginName);
		}

		try {
			InputStream stream = new FileInputStream(configFile);
			Yaml yaml = new Yaml();
			Map<String, Object> contents = yaml.load(stream);

		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
