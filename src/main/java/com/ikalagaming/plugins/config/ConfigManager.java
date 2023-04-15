package com.ikalagaming.plugins.config;

import com.ikalagaming.launcher.PluginFolder;
import com.ikalagaming.launcher.PluginFolder.ResourceType;
import com.ikalagaming.plugins.Plugin;
import com.ikalagaming.plugins.PluginManager;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Handles reading, writing, and caching configurations.
 *
 * @author Ches Burks
 *
 */
@Slf4j
public class ConfigManager {

	/**
	 * The name of the default configuration file.
	 */
	public static final String DEFAULT_NAME = "config.yml";

	private static Map<String, PluginConfig> configCache = new HashMap<>();

	/**
	 * Create an empty configuration to use if none is present.
	 *
	 * @param configName The name of the configuration, since a plugin might
	 *            have multiple.
	 * @return The configuration.
	 */
	private static PluginConfig emptyConfig(@NonNull String configName) {
		return new PluginConfig(new HashMap<>());
	}

	private static String getCacheName(@NonNull String pluginName,
		@NonNull String configName) {
		// $ is not a valid character in plugin names
		return String.format("%s$%s", pluginName, configName);
	}

	/**
	 * Load the default configuration for a plugin. If no configuration exists
	 * on disk, a blank one will be returned.
	 *
	 * @param pluginName The plugin to load configuration for.
	 * @return The default configuration for the given plugin.
	 */
	public static PluginConfig loadConfig(@NonNull String pluginName) {
		return ConfigManager.loadConfig(pluginName, ConfigManager.DEFAULT_NAME);
	}

	/**
	 * Load an arbitrary configuration for a plugin. If no configuration exists
	 * on disk, a blank one will be returned.
	 *
	 * @param pluginName The plugin to load configuration for.
	 * @param configName The configuration file to load. Should be of the format
	 *            like "example.yml".
	 * @return The associated configuration.
	 */
	public static PluginConfig loadConfig(@NonNull String pluginName,
		@NonNull String configName) {

		final String cacheName =
			ConfigManager.getCacheName(pluginName, configName);
		if (ConfigManager.configCache.containsKey(cacheName)) {
			return ConfigManager.configCache.get(cacheName);
		}
		File configFile = PluginFolder.getResource(pluginName,
			ResourceType.CONFIG, configName);
		if (!configFile.exists()
			&& PluginManager.getInstance().isLoaded(pluginName)) {

			// Try and copy in the case we are still enabling the plugin
			Optional<Plugin> maybePlugin =
				PluginManager.getInstance().getPlugin(pluginName);
			if (maybePlugin.isPresent()
				&& !PluginManager.getInstance().isEnabled(pluginName)) {
				ConfigManager.log.debug(
					"We are still loading {}, but a missing config was requested, attempting to create the missing {} config",
					pluginName, configName);
				ConfigManager.saveDefaultConfig(maybePlugin.get(), configName);
			}
		}
		if (!configFile.exists()) {
			ConfigManager.log.debug(
				"Can't find config {} for the {} plugin, using a blank default",
				configName, pluginName);
			PluginConfig cached = ConfigManager.emptyConfig(configName);
			ConfigManager.configCache.put(cacheName, cached);
			return cached;
		}

		try {
			InputStream stream = new FileInputStream(configFile);
			Yaml yaml = new Yaml();
			Map<String, Object> contents = yaml.load(stream);

			PluginConfig cached = new PluginConfig(contents);
			ConfigManager.configCache.put(cacheName, cached);
			return cached;
		}
		catch (FileNotFoundException e) {
			ConfigManager.log
				.warn("File does not exist after verifying it exists", e);
		}
		PluginConfig cached = ConfigManager.emptyConfig(configName);
		ConfigManager.configCache.put(cacheName, cached);
		return cached;
	}

	/**
	 * Save the default configuration file to disk, including changes that have
	 * been made in memory.
	 *
	 * @param pluginName The plugin to load configuration for.
	 */
	public static void saveConfigToDisk(@NonNull String pluginName) {
		ConfigManager.saveConfigToDisk(pluginName, ConfigManager.DEFAULT_NAME);
	}

	/**
	 * Save a configuration file to disk, including changes that have been made
	 * in memory.
	 *
	 * @param pluginName The plugin to load configuration for.
	 * @param configName The configuration file to load. Should be of the format
	 *            like "example.yml".
	 */
	public static void saveConfigToDisk(@NonNull String pluginName,
		@NonNull String configName) {

		final String cacheName =
			ConfigManager.getCacheName(pluginName, configName);

		PluginConfig config;
		if (ConfigManager.configCache.containsKey(cacheName)) {
			config = ConfigManager.configCache.get(cacheName);
		}
		else {
			ConfigManager.log.warn("We don't have a {} config in memory for {}",
				configName, pluginName);
			return;
		}

		File configFile = PluginFolder.getResource(pluginName,
			ResourceType.CONFIG, configName);
		try {
			DumperOptions options = new DumperOptions();
			options.setIndent(2);
			options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			options.setPrettyFlow(true);
			Yaml yaml = new Yaml(options);
			Files.writeString(configFile.toPath(),
				yaml.dump(config.getContents()), StandardOpenOption.WRITE);
		}
		catch (IOException e) {
			ConfigManager.log.warn("Error writing {} config for {} to disk: {}",
				configName, pluginName, e.getLocalizedMessage());
		}
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

		try (InputStream in =
			owner.getClass().getClassLoader().getResourceAsStream(configName)) {
			if (in == null) {
				ConfigManager.log.debug(
					"Can't find config {} in the jar for the {} plugin",
					configName, owner.getName());
				return;
			}

			File target = PluginFolder.getResource(owner.getName(),
				ResourceType.CONFIG, configName);
			if (target.exists()) {
				ConfigManager.log.debug(
					"Config {} already exists for the {} plugin", configName,
					owner.getName());
				return;
			}

			Files.copy(in, target.toPath());
		}
		catch (IOException ignored) {
			// No default config exists
			ConfigManager.log.debug(
				"Exception when adding config {} for the {} plugin", configName,
				owner.getName());
		}
	}
}
