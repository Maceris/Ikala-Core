package com.ikalagaming.plugins.config;

import com.ikalagaming.launcher.PluginFolder;
import com.ikalagaming.launcher.PluginFolder.ResourceType;
import com.ikalagaming.localization.Localization;
import com.ikalagaming.plugins.PluginManager;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Handles reading, writing, and caching configurations. This is thread safe, and every thread that
 * loads the same configuration gets the same {@link PluginConfig} object.
 *
 * <p>A configuration is layered: the defaults in the plugin's jar, then the config shipped in the
 * plugin's folder, then the user's changes in the Home. Saving writes only what differs from the
 * defaults, to the Home, so the install folder is never written to.
 *
 * @author Ches Burks
 */
@Slf4j
public class ConfigManager {
    /** The name of the default configuration file. */
    public static final String DEFAULT_NAME = "config.yml";

    /** What is added to a configuration's name for the reference copy of its defaults. */
    public static final String DEFAULTS_SUFFIX = ".defaults";

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
     * Read a configuration: the defaults in the plugin's jar, then the config shipped in its
     * folder, then the user's changes in the Home, each overriding the one before.
     *
     * @param pluginName The name of the plugin.
     * @param configName The name of the configuration file.
     * @return The configuration, empty if there is none.
     */
    private static PluginConfig readConfig(String pluginName, String configName) {
        Map<String, Object> settings = ConfigManager.readDefaults(pluginName, configName);
        File userFile = PluginFolder.getUserConfig(pluginName, configName);
        ConfigManager.mergeInto(settings, ConfigManager.readFile(userFile, pluginName, configName));
        ConfigManager.writeDefaultsReference(pluginName, configName);
        if (settings.isEmpty()) {
            log.debug(
                    SafeResourceLoader.getString(
                            "CONFIG_MISSING_FROM_DISK", ConfigManager.messages()),
                    configName,
                    pluginName);
        }
        return new PluginConfig(settings);
    }

    /**
     * Read the defaults for a configuration: the copy in the plugin's jar, overridden by the copy
     * shipped in the plugin's folder.
     *
     * @param pluginName The name of the plugin.
     * @param configName The name of the configuration file.
     * @return The default settings, empty if there are none.
     */
    private static Map<String, Object> readDefaults(String pluginName, String configName) {
        Map<String, Object> defaults = new LinkedHashMap<>();
        Optional<String> jarText = ConfigManager.readJarDefaults(pluginName, configName);
        if (jarText.isPresent()) {
            ConfigManager.mergeInto(
                    defaults,
                    ConfigManager.readStream(
                            new ByteArrayInputStream(
                                    jarText.get().getBytes(StandardCharsets.UTF_8)),
                            pluginName,
                            configName));
        }
        File shipped = PluginFolder.getResource(pluginName, ResourceType.CONFIG, configName);
        ConfigManager.mergeInto(defaults, ConfigManager.readFile(shipped, pluginName, configName));
        return defaults;
    }

    /**
     * Read a configuration's defaults from the plugin's jar, as written.
     *
     * @param pluginName The name of the plugin.
     * @param configName The name of the configuration file.
     * @return The file's text, or empty if the plugin isn't known or has no such file.
     */
    private static Optional<String> readJarDefaults(String pluginName, String configName) {
        return PluginManager.getInstance()
                .readJarEntry(pluginName, configName)
                .map(bytes -> new String(bytes, StandardCharsets.UTF_8));
    }

    /**
     * The name of the reference copy of a configuration's defaults, such as {@code
     * config.defaults.yml} for {@code config.yml}.
     *
     * @param configName The name of the configuration file.
     * @return The name of its defaults file.
     */
    public static String defaultsName(@NonNull String configName) {
        int dot = configName.lastIndexOf('.');
        return dot <= 0
                ? configName + ConfigManager.DEFAULTS_SUFFIX
                : configName.substring(0, dot)
                        + ConfigManager.DEFAULTS_SUFFIX
                        + configName.substring(dot);
    }

    /**
     * Write every default setting of a configuration, with its comments, next to the user's file in
     * the Home, so they can see what can be changed. The game never reads this file, and replaces
     * it whenever the defaults change, so it never goes out of date.
     *
     * @param pluginName The name of the plugin.
     * @param configName The name of the configuration file.
     */
    static void writeDefaultsReference(String pluginName, String configName) {
        Optional<String> jarText = ConfigManager.readJarDefaults(pluginName, configName);
        File shipped = PluginFolder.getResource(pluginName, ResourceType.CONFIG, configName);
        if (jarText.isEmpty() && !shipped.isFile()) {
            return;
        }
        StringBuilder text = new StringBuilder();
        text.append("# Every default setting of the ")
                .append(pluginName)
                .append(" plugin, for reference. This file is\n")
                .append("# replaced whenever the defaults change, and the game never reads it.")
                .append(" To change a\n")
                .append("# setting, put it in ")
                .append(configName)
                .append(" next to this file; only the settings you change need to be there.\n\n");
        jarText.ifPresent(jar -> text.append(jar.strip()).append('\n'));
        if (shipped.isFile()) {
            try {
                String override = Files.readString(shipped.toPath(), StandardCharsets.UTF_8);
                text.append("\n# This install also ships these settings, which replace the ones")
                        .append(" above:\n");
                for (String line : override.strip().split("\\R")) {
                    text.append("# ").append(line).append('\n');
                }
            } catch (IOException e) {
                log.warn(
                        SafeResourceLoader.getString(
                                "CONFIG_FILE_VANISHED", ConfigManager.messages()),
                        e);
            }
        }
        Path target =
                PluginFolder.getUserConfig(pluginName, ConfigManager.defaultsName(configName))
                        .toPath();
        String contents = text.toString();
        try {
            if (Files.isRegularFile(target)
                    && contents.equals(Files.readString(target, StandardCharsets.UTF_8))) {
                return;
            }
            Files.createDirectories(target.getParent());
            // Written whole and moved into place, so a crash never leaves half a file
            Path temporary = target.resolveSibling(target.getFileName() + ".tmp");
            Files.writeString(temporary, contents, StandardCharsets.UTF_8);
            Files.move(
                    temporary,
                    target,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            log.warn(
                    SafeResourceLoader.getString("CONFIG_WRITING_FAILED", ConfigManager.messages()),
                    ConfigManager.defaultsName(configName),
                    pluginName,
                    e.getLocalizedMessage());
        }
    }

    /**
     * Read the settings from a YAML file, if it exists.
     *
     * @param file The file.
     * @param pluginName The name of the plugin, for messages.
     * @param configName The name of the configuration file, for messages.
     * @return The settings, empty if the file is missing or isn't a map of settings.
     */
    private static Map<String, Object> readFile(File file, String pluginName, String configName) {
        if (!file.isFile()) {
            return new LinkedHashMap<>();
        }
        try (InputStream stream = Files.newInputStream(file.toPath())) {
            return ConfigManager.readStream(stream, pluginName, configName);
        } catch (IOException e) {
            log.warn(
                    SafeResourceLoader.getString("CONFIG_FILE_VANISHED", ConfigManager.messages()),
                    e);
        }
        return new LinkedHashMap<>();
    }

    /**
     * Read the settings from a YAML stream.
     *
     * @param stream The stream.
     * @param pluginName The name of the plugin, for messages.
     * @param configName The name of the configuration file, for messages.
     * @return The settings, empty if the stream isn't a map of settings.
     */
    private static Map<String, Object> readStream(
            InputStream stream, String pluginName, String configName) {
        Object contents = new Yaml().load(stream);
        if (contents instanceof Map<?, ?> map) {
            Map<String, Object> settings = new LinkedHashMap<>();
            map.forEach((key, value) -> settings.put(String.valueOf(key), value));
            return settings;
        }
        if (contents != null) {
            // An empty file is fine, but anything else isn't a valid config
            log.warn(
                    SafeResourceLoader.getString("CONFIG_NOT_A_MAP", ConfigManager.messages()),
                    configName,
                    pluginName);
        }
        return new LinkedHashMap<>();
    }

    /**
     * Copy settings over others. Sections present in both are merged, so an override only has to
     * hold the values it changes.
     *
     * @param target The settings to change.
     * @param overrides The settings that win.
     */
    @SuppressWarnings("unchecked")
    static void mergeInto(Map<String, Object> target, Map<String, Object> overrides) {
        for (Map.Entry<String, Object> entry : overrides.entrySet()) {
            Object existing = target.get(entry.getKey());
            if (existing instanceof Map<?, ?> existingSection
                    && entry.getValue() instanceof Map<?, ?> overrideSection) {
                Map<String, Object> merged =
                        new LinkedHashMap<>((Map<String, Object>) existingSection);
                ConfigManager.mergeInto(merged, (Map<String, Object>) overrideSection);
                target.put(entry.getKey(), merged);
            } else {
                target.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * The settings that differ from the defaults, so only the user's changes are saved.
     *
     * @param settings The current settings.
     * @param defaults The defaults.
     * @return The settings that are missing from the defaults or have another value.
     */
    @SuppressWarnings("unchecked")
    static Map<String, Object> differences(
            Map<String, Object> settings, Map<String, Object> defaults) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : settings.entrySet()) {
            Object fallback = defaults.get(entry.getKey());
            if (entry.getValue() instanceof Map<?, ?> section
                    && fallback instanceof Map<?, ?> defaultSection) {
                Map<String, Object> changed =
                        ConfigManager.differences(
                                (Map<String, Object>) section,
                                (Map<String, Object>) defaultSection);
                if (!changed.isEmpty()) {
                    result.put(entry.getKey(), changed);
                }
            } else if (!Objects.equals(entry.getValue(), fallback)
                    || !defaults.containsKey(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
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
        Path configPath = PluginFolder.getUserConfig(pluginName, configName).toPath();
        Map<String, Object> defaults = ConfigManager.readDefaults(pluginName, configName);
        // One save at a time per config, so two saves can't mix in the file. The snapshot is
        // taken inside the lock too, so a later save can't be overwritten by an earlier one.
        ReentrantLock saveLock = config.getSaveLock();
        saveLock.lock();
        try {
            Map<String, Object> changed = ConfigManager.differences(config.snapshot(), defaults);
            try {
                if (changed.isEmpty()) {
                    // Nothing differs from the defaults, so there is nothing to keep
                    Files.deleteIfExists(configPath);
                    return;
                }
                Files.createDirectories(configPath.getParent());
                // Creates the file if missing, and truncates it if it already exists
                Files.writeString(configPath, new Yaml(options).dump(changed));
            } catch (IOException e) {
                log.warn(
                        SafeResourceLoader.getString(
                                "CONFIG_WRITING_FAILED", ConfigManager.messages()),
                        configName,
                        pluginName,
                        e.getLocalizedMessage());
            }
        } finally {
            saveLock.unlock();
        }
    }

    /** Private constructor so that this class is not instantiated. */
    private ConfigManager() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
