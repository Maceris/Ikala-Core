package com.ikalagaming.plugins.config;

import com.ikalagaming.launcher.PluginFolder;
import com.ikalagaming.launcher.PluginFolder.ResourceType;
import com.ikalagaming.plugins.Plugin;
import com.ikalagaming.plugins.PluginManager;
import com.ikalagaming.util.SafeResourceLoader;

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
 */
@Slf4j
public class ConfigManager {
    /** The name of the default configuration file. */
    public static final String DEFAULT_NAME = "config.yml";

    private static Map<String, PluginConfig> configCache = new HashMap<>();

    /**
     * Create an empty configuration to use if none is present.
     *
     * @return The configuration.
     */
    private static PluginConfig emptyConfig() {
        return new PluginConfig(new HashMap<>());
    }

    private static String getCacheName(@NonNull String pluginName, @NonNull String configName) {
        // $ is not a valid character in plugin names
        return String.format("%s$%s", pluginName, configName);
    }

    /**
     * Load the default configuration for a plugin. If no configuration exists on disk, a blank one
     * will be returned.
     *
     * @param pluginName The plugin to load configuration for.
     * @return The default configuration for the given plugin.
     */
    public static PluginConfig loadConfig(@NonNull String pluginName) {
        return ConfigManager.loadConfig(pluginName, ConfigManager.DEFAULT_NAME);
    }

    /**
     * Load an arbitrary configuration for a plugin. If no configuration exists on disk, a blank one
     * will be returned.
     *
     * @param pluginName The plugin to load configuration for.
     * @param configName The configuration file to load. Should be of the format like "example.yml".
     * @return The associated configuration.
     */
    public static PluginConfig loadConfig(@NonNull String pluginName, @NonNull String configName) {

        final String cacheName = ConfigManager.getCacheName(pluginName, configName);
        if (ConfigManager.configCache.containsKey(cacheName)) {
            return ConfigManager.configCache.get(cacheName);
        }
        File configFile = PluginFolder.getResource(pluginName, ResourceType.CONFIG, configName);
        if (!configFile.exists() && PluginManager.getInstance().isLoaded(pluginName)) {

            // Try and copy in the case we are still enabling the plugin
            Optional<Plugin> maybePlugin = PluginManager.getInstance().getPlugin(pluginName);
            if (maybePlugin.isPresent() && !PluginManager.getInstance().isEnabled(pluginName)) {
                log.debug(
                        SafeResourceLoader.getString(
                                "CONFIG_REQUESTED_BEFORE_ENABLE",
                                PluginManager.getInstance().getResourceBundle()),
                        pluginName,
                        configName);
                ConfigManager.saveDefaultConfig(maybePlugin.get(), configName);
            }
        }
        if (!configFile.exists()) {
            log.debug(
                    SafeResourceLoader.getString(
                            "CONFIG_MISSING_FROM_DISK",
                            PluginManager.getInstance().getResourceBundle()),
                    configName,
                    pluginName);
            PluginConfig cached = ConfigManager.emptyConfig();
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
        } catch (FileNotFoundException e) {
            log.warn(
                    SafeResourceLoader.getString(
                            "CONFIG_FILE_VANISHED",
                            PluginManager.getInstance().getResourceBundle()),
                    e);
        }
        PluginConfig cached = ConfigManager.emptyConfig();
        ConfigManager.configCache.put(cacheName, cached);
        return cached;
    }

    /**
     * Forcibly reload a configuration from disk, discarding any in-memory changes.
     *
     * @param pluginName The plugin to load configuration for.
     * @return The default configuration for the given plugin.
     */
    public static PluginConfig reloadConfig(@NonNull String pluginName) {
        return ConfigManager.reloadConfig(pluginName, ConfigManager.DEFAULT_NAME);
    }

    /**
     * Forcibly reload a configuration from disk, discarding any in-memory changes.
     *
     * @param pluginName The plugin to load configuration for.
     * @param configName The configuration file to load. Should be of the format like "example.yml".
     * @return The associated configuration.
     */
    public static PluginConfig reloadConfig(
            @NonNull String pluginName, @NonNull String configName) {

        final String cacheName = ConfigManager.getCacheName(pluginName, configName);
        ConfigManager.configCache.remove(cacheName);

        return ConfigManager.loadConfig(pluginName, configName);
    }

    /**
     * Save the default configuration file to disk, including changes that have been made in memory.
     *
     * @param pluginName The plugin to load configuration for.
     */
    public static void saveConfigToDisk(@NonNull String pluginName) {
        ConfigManager.saveConfigToDisk(pluginName, ConfigManager.DEFAULT_NAME);
    }

    /**
     * Save a configuration file to disk, including changes that have been made in memory.
     *
     * @param pluginName The plugin to load configuration for.
     * @param configName The configuration file to load. Should be of the format like "example.yml".
     */
    public static void saveConfigToDisk(@NonNull String pluginName, @NonNull String configName) {

        final String cacheName = ConfigManager.getCacheName(pluginName, configName);

        PluginConfig config;
        if (ConfigManager.configCache.containsKey(cacheName)) {
            config = ConfigManager.configCache.get(cacheName);
        } else {
            log.warn(
                    SafeResourceLoader.getString(
                            "CONFIG_MISSING_FROM_MEMORY",
                            PluginManager.getInstance().getResourceBundle()),
                    configName,
                    pluginName);
            return;
        }

        File configFile = PluginFolder.getResource(pluginName, ResourceType.CONFIG, configName);
        try {
            DumperOptions options = new DumperOptions();
            options.setIndent(2);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yaml = new Yaml(options);
            Files.writeString(
                    configFile.toPath(), yaml.dump(config.getContents()), StandardOpenOption.WRITE);
        } catch (IOException e) {
            log.warn(
                    SafeResourceLoader.getString(
                            "CONFIG_WRITING_FAILED",
                            PluginManager.getInstance().getResourceBundle()),
                    configName,
                    pluginName,
                    e.getLocalizedMessage());
        }
    }

    /**
     * Save a copy of the default configuration file if it is missing. This will not not overwrite
     * an existing configuration.
     *
     * @param owner The plugin that owns the configuration.
     * @param configName The name of the configuration file.
     */
    public static void saveDefaultConfig(@NonNull Plugin owner, @NonNull String configName) {

        try (InputStream in = owner.getClass().getClassLoader().getResourceAsStream(configName)) {
            if (in == null) {
                log.debug(
                        SafeResourceLoader.getString(
                                "CONFIG_MISSING_FROM_JAR",
                                PluginManager.getInstance().getResourceBundle()),
                        configName,
                        owner.getName());
                return;
            }

            File target =
                    PluginFolder.getResource(owner.getName(), ResourceType.CONFIG, configName);
            if (target.exists()) {
                log.debug(
                        SafeResourceLoader.getString(
                                "CONFIG_ALREADY_EXISTS",
                                PluginManager.getInstance().getResourceBundle()),
                        configName,
                        owner.getName());
                return;
            }

            Files.copy(in, target.toPath());
        } catch (IOException e) {
            // No default config exists
            log.debug(
                    SafeResourceLoader.getString(
                            "CONFIG_WRITING_FAILED",
                            PluginManager.getInstance().getResourceBundle()),
                    configName,
                    owner.getName(),
                    e.getLocalizedMessage());
        }
    }

    /** Private constructor so that this class is not instantiated. */
    private ConfigManager() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
