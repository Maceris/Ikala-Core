package com.ikalagaming.plugins.config;

import com.ikalagaming.launcher.PluginFolder;
import com.ikalagaming.launcher.PluginFolder.ResourceType;
import com.ikalagaming.localization.Localization;
import com.ikalagaming.plugins.Plugin;
import com.ikalagaming.plugins.PluginManager;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles reading, writing, and caching configurations. This is thread safe, and every thread that
 * loads the same configuration gets the same {@link PluginConfig} object.
 *
 * @author Ches Burks
 */
@Slf4j
public class ConfigManager {
    /** The name of the default configuration file. */
    public static final String DEFAULT_NAME = "config.yml";

    private static final Map<String, PluginConfig> configCache = new ConcurrentHashMap<>();

    /**
     * Forget all the cached configurations for a plugin, so they are read from disk the next time
     * they are loaded. The plugin manager does this when a plugin is unloaded, so a reloaded or
     * upgraded plugin doesn't get stale settings. Changes that were not saved are lost.
     *
     * @param pluginName The plugin to forget configurations for.
     */
    public static void clearCache(@NonNull String pluginName) {
        final String prefix = ConfigManager.getCacheName(pluginName, "");
        ConfigManager.configCache.keySet().removeIf(cacheName -> cacheName.startsWith(prefix));
    }

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
        PluginConfig cached = ConfigManager.configCache.get(cacheName);
        if (cached != null) {
            return cached;
        }
        /*
         * No locks are held while reading, since that calls into the plugin
         * manager. If two threads load at once, they both read the file, but
         * only the first one is kept, and both get that one.
         */
        PluginConfig loaded = ConfigManager.readConfig(pluginName, configName);
        PluginConfig existing = ConfigManager.configCache.putIfAbsent(cacheName, loaded);
        return existing == null ? loaded : existing;
    }

    /**
     * The resource bundle for configuration messages. This is looked up directly instead of through
     * the plugin manager, so that logging never needs the plugin manager's locks.
     *
     * @return The resource bundle for messages.
     */
    static ResourceBundle messages() {
        return ResourceBundle.getBundle(
                "com.ikalagaming.plugins.PluginManager", Localization.getLocale());
    }

    /**
     * Read a configuration from disk, copying the default from the plugin jar first if it is
     * missing and the plugin is still loading.
     *
     * @param pluginName The plugin to load configuration for.
     * @param configName The configuration file to load.
     * @return The configuration, which is blank if it doesn't exist or can't be read.
     */
    private static PluginConfig readConfig(String pluginName, String configName) {
        File configFile = PluginFolder.getResource(pluginName, ResourceType.CONFIG, configName);
        if (!configFile.exists()) {
            // Try and copy in the case we are still enabling the plugin
            PluginManager manager = PluginManager.getInstance();
            Optional<Plugin> maybePlugin = manager.getPlugin(pluginName);
            if (maybePlugin.isPresent()
                    && manager.isLoaded(pluginName)
                    && !manager.isEnabled(pluginName)) {
                log.debug(
                        SafeResourceLoader.getString(
                                "CONFIG_REQUESTED_BEFORE_ENABLE", ConfigManager.messages()),
                        pluginName,
                        configName);
                ConfigManager.saveDefaultConfig(maybePlugin.get(), configName);
            }
        }
        if (!configFile.exists()) {
            log.debug(
                    SafeResourceLoader.getString(
                            "CONFIG_MISSING_FROM_DISK", ConfigManager.messages()),
                    configName,
                    pluginName);
            return ConfigManager.emptyConfig();
        }

        try (InputStream stream = Files.newInputStream(configFile.toPath())) {
            Object contents = new Yaml().load(stream);
            if (contents instanceof Map<?, ?> map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> settings = (Map<String, Object>) map;
                return new PluginConfig(settings);
            }
            if (contents != null) {
                // An empty file is fine, but anything else isn't a valid config
                log.warn(
                        SafeResourceLoader.getString("CONFIG_NOT_A_MAP", ConfigManager.messages()),
                        configName,
                        pluginName);
            }
        } catch (IOException e) {
            log.warn(
                    SafeResourceLoader.getString("CONFIG_FILE_VANISHED", ConfigManager.messages()),
                    e);
        }
        return ConfigManager.emptyConfig();
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
     * Forcibly reload a configuration from disk, discarding any in-memory changes. Anything still
     * holding the old configuration object keeps seeing the old values.
     *
     * @param pluginName The plugin to load configuration for.
     * @param configName The configuration file to load. Should be of the format like "example.yml".
     * @return The associated configuration.
     */
    public static PluginConfig reloadConfig(
            @NonNull String pluginName, @NonNull String configName) {
        PluginConfig loaded = ConfigManager.readConfig(pluginName, configName);
        ConfigManager.configCache.put(ConfigManager.getCacheName(pluginName, configName), loaded);
        return loaded;
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
     * Save a configuration file to disk, including changes that have been made in memory. What is
     * saved is a snapshot of the configuration, so other threads can keep using it while it saves.
     *
     * @param pluginName The plugin to load configuration for.
     * @param configName The configuration file to load. Should be of the format like "example.yml".
     */
    public static void saveConfigToDisk(@NonNull String pluginName, @NonNull String configName) {
        PluginConfig config =
                ConfigManager.configCache.get(ConfigManager.getCacheName(pluginName, configName));
        if (config == null) {
            log.warn(
                    SafeResourceLoader.getString(
                            "CONFIG_MISSING_FROM_MEMORY", ConfigManager.messages()),
                    configName,
                    pluginName);
            return;
        }

        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        String contents = new Yaml(options).dump(config.snapshot());

        Path configPath =
                PluginFolder.getResource(pluginName, ResourceType.CONFIG, configName).toPath();
        // One save at a time per config, so two saves can't mix in the file
        synchronized (config.getSaveLock()) {
            try {
                Files.createDirectories(configPath.getParent());
                // Creates the file if missing, and truncates it if it already exists
                Files.writeString(configPath, contents);
            } catch (IOException e) {
                log.warn(
                        SafeResourceLoader.getString(
                                "CONFIG_WRITING_FAILED", ConfigManager.messages()),
                        configName,
                        pluginName,
                        e.getLocalizedMessage());
            }
        }
    }

    /**
     * Save a copy of the default configuration file if it is missing. This will not overwrite an
     * existing configuration.
     *
     * @param owner The plugin that owns the configuration.
     * @param configName The name of the configuration file.
     */
    public static void saveDefaultConfig(@NonNull Plugin owner, @NonNull String configName) {

        try (InputStream in = owner.getClass().getClassLoader().getResourceAsStream(configName)) {
            if (in == null) {
                log.debug(
                        SafeResourceLoader.getString(
                                "CONFIG_MISSING_FROM_JAR", ConfigManager.messages()),
                        configName,
                        owner.getName());
                return;
            }

            File target =
                    PluginFolder.getResource(owner.getName(), ResourceType.CONFIG, configName);
            if (target.exists()) {
                log.debug(
                        SafeResourceLoader.getString(
                                "CONFIG_ALREADY_EXISTS", ConfigManager.messages()),
                        configName,
                        owner.getName());
                return;
            }

            Files.createDirectories(target.toPath().getParent());
            // Fails rather than overwriting, if another thread just created it
            Files.copy(in, target.toPath());
        } catch (IOException e) {
            log.debug(
                    SafeResourceLoader.getString("CONFIG_WRITING_FAILED", ConfigManager.messages()),
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
