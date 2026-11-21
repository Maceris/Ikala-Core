package com.ikalagaming.launcher;

import com.ikalagaming.util.FileUtils;

import com.github.zafarkhaja.semver.Version;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Contains utilities for dealing with the folders of Plugins.
 *
 * <p>Each plugin has a folder next to its jar, named after the plugin, holding the config, data and
 * scripts it ships with. That folder is in the install, or wherever the plugin was loaded from, and
 * is only read while the application runs. What the user changes goes in the Home instead: config
 * overrides in {@link #getUserConfig(String, String)}, and the last version of each plugin in
 * {@value #VERSIONS_FILE}.
 *
 * @author Ches Burks
 */
@Slf4j
public class PluginFolder {

    /**
     * The type of resources we can have for a plugin.
     *
     * @author Ches Burks
     */
    public enum ResourceType {
        /** The configuration the plugin ships with, as defaults. */
        CONFIG,
        /** Data files the plugin ships with. */
        DATA,
        /** Scripts the plugin ships with. */
        SCRIPTS;
    }

    /** The file in the Home's state folder that records the last loaded version of each plugin. */
    public static final String VERSIONS_FILE = "plugin-versions.yml";

    /** The folder in the Home's settings folder that holds plugin config overrides. */
    public static final String PLUGIN_SETTINGS_FOLDER = "plugins";

    /** The version reported for a plugin that was never loaded before. */
    static final String NO_VERSION = "0.0.0";

    /** The folder each plugin was loaded from, by plugin name. */
    private static final Map<String, Path> PARENT_FOLDERS = new ConcurrentHashMap<>();

    /** Guards reading and writing {@value #VERSIONS_FILE}. */
    private static final ReentrantLock VERSIONS_LOCK = new ReentrantLock();

    /**
     * Create the plugin folder for the given plugin if it does not already exist.
     *
     * @param pluginName The plugin we want a data folder for.
     */
    public static void createFolder(@NonNull final String pluginName) {
        if (PluginFolder.folderExists(pluginName)) {
            return;
        }
        PluginFolder.getFolderPath(pluginName).toFile().mkdirs();
    }

    /**
     * Creates a resource folder for the specified type of resource. Will create the main folder for
     * the plugin if it does not exist.
     *
     * @param pluginName The name of the plugin we are creating for.
     * @param type The type of resource we want a folder for.
     * @return Whether we succeeded in creating the folder.
     */
    public static boolean createResourceFolder(
            @NonNull final String pluginName, @NonNull ResourceType type) {
        PluginFolder.createFolder(pluginName);
        File folder = PluginFolder.getResource(pluginName, type, "");
        return folder.isDirectory() || folder.mkdirs();
    }

    /**
     * Delete the plugin folder for the given plugin, if it exists. This does not touch anything in
     * the Home.
     *
     * @param pluginName The plugin we want to delete the folder of.
     */
    public static void deleteFolder(@NonNull final String pluginName) {
        Optional<File> file = FileUtils.getFile(PluginFolder.getFolderPath(pluginName).toString());
        file.ifPresent(PluginFolder::deleteRecursively);
    }

    /**
     * Delete a file or folder and everything in it.
     *
     * @param toDelete The file or folder.
     * @return Whether it was deleted.
     */
    private static boolean deleteRecursively(File toDelete) {
        if (!toDelete.exists()) {
            return false;
        }
        File[] contents = toDelete.listFiles();
        if (contents != null) {
            for (File file : contents) {
                PluginFolder.deleteRecursively(file);
            }
        }
        try {
            Files.delete(toDelete.toPath());
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Check if the plugin folder exists for the given plugin.
     *
     * @param pluginName The plugin we are checking for.
     * @return True if the folder exists, false if it does not.
     */
    public static boolean folderExists(@NonNull final String pluginName) {
        return Files.isDirectory(PluginFolder.getFolderPath(pluginName));
    }

    /**
     * Remember which folder a plugin was loaded from, so its resources are found next to its jar.
     * Plugins that were never registered are looked for in the install's plugin folder.
     *
     * @param pluginName The plugin.
     * @param parentFolder The folder holding the plugin's jar.
     */
    public static void setParentFolder(@NonNull String pluginName, @NonNull Path parentFolder) {
        PluginFolder.PARENT_FOLDERS.put(pluginName, parentFolder.toAbsolutePath());
    }

    /**
     * The folder for a plugin's shipped files, next to its jar.
     *
     * @param pluginName The plugin.
     * @return The folder, which might not exist.
     */
    public static Path getFolderPath(@NonNull final String pluginName) {
        return PluginFolder.PARENT_FOLDERS
                .getOrDefault(pluginName, AppPaths.plugins())
                .resolve(pluginName);
    }

    /**
     * Fetch the last version of the plugin that we loaded, which is useful for upgrading.
     *
     * @param pluginName The name of the plugin to check.
     * @return The last version of the plugin that was loaded, or "0.0.0" if we can't find any
     *     information on it.
     */
    public static String getLastVersionUsed(@NonNull final String pluginName) {
        PluginFolder.VERSIONS_LOCK.lock();
        try {
            Object lastVersion = PluginFolder.readVersions().get(pluginName);
            if (lastVersion == null) {
                return PluginFolder.NO_VERSION;
            }
            String version = lastVersion.toString().strip();
            if (!Version.isValid(version)) {
                log.warn(
                        "Ignoring invalid last used version '{}' for plugin {}",
                        version,
                        pluginName);
                return PluginFolder.NO_VERSION;
            }
            return version;
        } finally {
            PluginFolder.VERSIONS_LOCK.unlock();
        }
    }

    /**
     * Returns a file representing the resource of the given name and type for a plugin. These are
     * the files the plugin ships with, next to its jar, so they are only read. For config files,
     * these are the defaults; the user's changes are in {@link #getUserConfig(String, String)}.
     *
     * @param pluginName The name of the plugin.
     * @param type The type of resource.
     * @param path The path to the file, relative to the resource folder.
     * @return The file, which might not exist.
     */
    public static File getResource(
            @NonNull final String pluginName,
            @NonNull ResourceType type,
            @NonNull final String path) {
        String folderName =
                switch (type) {
                    case CONFIG -> Constants.CONFIG_FOLDER;
                    case DATA -> Constants.DATA_FOLDER;
                    case SCRIPTS -> Constants.SCRIPTS_FOLDER;
                };
        Path folder = PluginFolder.getFolderPath(pluginName).resolve(folderName);
        return path.isEmpty() ? folder.toFile() : folder.resolve(path).toFile();
    }

    /**
     * The file holding the user's changes to one of a plugin's config files, in the Home.
     *
     * @param pluginName The name of the plugin.
     * @param configName The name of the config file, such as config.yml.
     * @return The file, which might not exist.
     */
    public static File getUserConfig(
            @NonNull final String pluginName, @NonNull final String configName) {
        return AppPaths.settings()
                .resolve(PluginFolder.PLUGIN_SETTINGS_FOLDER)
                .resolve(pluginName)
                .resolve(configName)
                .toFile();
    }

    /**
     * Read the last version of every plugin. Must be called with the lock held.
     *
     * @return The versions by plugin name, empty if the file is missing or broken.
     */
    private static Map<String, Object> readVersions() {
        Path file = PluginFolder.versionsFile();
        if (!Files.isRegularFile(file)) {
            return new TreeMap<>();
        }
        try (InputStream stream = Files.newInputStream(file)) {
            Object contents = new Yaml().load(stream);
            if (contents instanceof Map<?, ?> map) {
                Map<String, Object> versions = new TreeMap<>();
                map.forEach((key, value) -> versions.put(String.valueOf(key), value));
                return versions;
            }
        } catch (IOException | RuntimeException e) {
            log.warn("Could not read the last plugin versions from {}", file, e);
        }
        return new TreeMap<>();
    }

    /**
     * Forget the last version of a plugin, as if it was never loaded.
     *
     * @param pluginName The name of the plugin.
     * @return Whether the change was saved.
     */
    public static boolean forgetLastVersionUsed(@NonNull final String pluginName) {
        PluginFolder.VERSIONS_LOCK.lock();
        try {
            Map<String, Object> versions = PluginFolder.readVersions();
            if (versions.remove(pluginName) == null) {
                return true;
            }
            return PluginFolder.writeVersions(versions);
        } finally {
            PluginFolder.VERSIONS_LOCK.unlock();
        }
    }

    /**
     * Record the last version of a plugin that was loaded, so it can tell when it is upgraded.
     *
     * @param pluginName The name of the plugin.
     * @param version The version that was loaded.
     * @return Whether the version was saved.
     */
    public static boolean setLastVersionUsed(
            @NonNull final String pluginName, @NonNull final String version) {
        PluginFolder.VERSIONS_LOCK.lock();
        try {
            Map<String, Object> versions = PluginFolder.readVersions();
            versions.put(pluginName, version);
            return PluginFolder.writeVersions(versions);
        } finally {
            PluginFolder.VERSIONS_LOCK.unlock();
        }
    }

    /**
     * The file that records the last version of each plugin.
     *
     * @return The file, in the Home's state folder.
     */
    static Path versionsFile() {
        return AppPaths.state().resolve(PluginFolder.VERSIONS_FILE);
    }

    /**
     * Write the last version of every plugin. Must be called with the lock held.
     *
     * @param versions The versions by plugin name.
     * @return Whether they were written.
     */
    private static boolean writeVersions(@NonNull Map<String, Object> versions) {
        Path file = PluginFolder.versionsFile();
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        try {
            Files.createDirectories(file.getParent());
            // Written whole and moved into place, so a crash never leaves half a file
            Path temporary = file.resolveSibling(PluginFolder.VERSIONS_FILE + ".tmp");
            Files.writeString(temporary, new Yaml(options).dump(new TreeMap<>(versions)));
            Files.move(
                    temporary,
                    file,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            log.warn("Could not save the last plugin versions to {}", file, e);
            return false;
        }
        return true;
    }

    /** Private constructor so this class is not initialized. */
    private PluginFolder() {}
}
