package com.ikalagaming.launcher;

import com.ikalagaming.util.FileUtils;

import lombok.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * Contains utilities for dealing with data folders for Plugins.
 *
 * @author Ches Burks
 */
public class PluginFolder {

    /**
     * The type of resources we can have for a plugin.
     *
     * @author Ches Burks
     */
    public enum ResourceType {
        /** Configuration files. */
        CONFIG,
        /** Raw resources like images, audio, etc. */
        DATA,
        /** Scripts related to the plugin. */
        SCRIPTS;
    }

    private static final String RUNTIME_DIR = System.getProperty("user.dir");

    /**
     * Create the plugin folder for the given plugin if it does not already exist.
     *
     * @param pluginName The plugin we want a data folder for.
     */
    public static void createFolder(@NonNull final String pluginName) {
        if (PluginFolder.folderExists(pluginName)) {
            return;
        }
        FileUtils.createFolder(PluginFolder.RUNTIME_DIR + Constants.PLUGIN_FOLDER_PATH, pluginName);
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
        // should start with File.separator
        String folderName = "";

        switch (type) {
            case CONFIG:
                folderName = Constants.CONFIG_PATH;
                break;
            case DATA:
                folderName = Constants.DATA_PATH;
                break;
            case SCRIPTS:
                folderName = Constants.SCRIPTS_PATH;
                break;
            default:
                break;
        }

        return FileUtils.createFolder(
                PluginFolder.RUNTIME_DIR + Constants.PLUGIN_FOLDER_PATH, pluginName + folderName);
    }

    /**
     * Deletes the folder for a plugin if it exists, including everything in the folder.
     *
     * @param pluginName The plugin to delete data for.
     */
    public static void deleteFolder(@NonNull final String pluginName) {
        Optional<File> file = FileUtils.getFile(PluginFolder.getFolderForPlugin(pluginName));
        file.ifPresent(PluginFolder::deleteRecursively);
    }

    /**
     * Delete a directory and all files and subdirectories.
     *
     * @param toDelete The folder to delete.
     * @return True on success, false on a failure.
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
     * Checks if there is already a data folder for the given plugin.
     *
     * @param pluginName The name of the plugin.
     * @return True if the folder exists, false if it does not.
     */
    public static boolean folderExists(@NonNull final String pluginName) {
        return FileUtils.fileExists(PluginFolder.getFolderForPlugin(pluginName));
    }

    /**
     * Calculate the path to the plugin folder based on the name.
     *
     * @param pluginName The name of the plugin.
     * @return The path to the folder for that plugin.
     */
    private static String getFolderForPlugin(@NonNull final String pluginName) {
        return PluginFolder.RUNTIME_DIR + Constants.PLUGIN_FOLDER_PATH + pluginName;
    }

    /**
     * Returns the last version that was used for the given plugin, which will be 0.0.0 if we can't
     * find a last version.
     *
     * @param pluginName The name of the plugin to look for.
     * @return The last version that was recorded.
     */
    public static String getLastVersionUsed(@NonNull final String pluginName) {
        String version = "0.0.0";
        if (!PluginFolder.folderExists(pluginName)) {
            return version;
        }

        Optional<File> file =
                FileUtils.getFile(
                        PluginFolder.getFolderForPlugin(pluginName)
                                + File.separator
                                + Constants.PLUGIN_VERSION_FILE);
        if (file.isEmpty() || !file.get().exists()) {
            return version;
        }

        try {
            return new String(Files.readAllBytes(file.get().toPath()));
        } catch (IOException e) {
            return version;
        }
    }

    /**
     * Returns a File that points to the requested resource. This file very well might not exist,
     * but should not be a null File object. This could be used as a File reference to create a
     * resource, or to read from disk if the resource already exists.
     *
     * @param pluginName The name of the plugin we are looking for resources for.
     * @param type The type of resource to fetch.
     * @param path The path from the resource directory to the resource. For example, {@code
     *     image.png} or {@code audio/world/rain.wav}.
     * @return The file object for the resource if it existed.
     * @see File#exists()
     */
    public static File getResource(
            @NonNull final String pluginName,
            @NonNull ResourceType type,
            @NonNull final String path) {
        String folderName = File.separator;

        switch (type) {
            case CONFIG:
                folderName = Constants.CONFIG_PATH;
                break;
            case DATA:
                folderName = Constants.DATA_PATH;
                break;
            case SCRIPTS:
                folderName = Constants.SCRIPTS_PATH;
                break;
            default:
                break;
        }

        return new File(
                PluginFolder.RUNTIME_DIR + Constants.PLUGIN_FOLDER_PATH,
                pluginName + folderName + path);
    }

    /**
     * Stores the last version used in a text file for later reference.
     *
     * @param pluginName The plugin we are recording a version for.
     * @param version The version we are using.
     * @return True if we have stored the version, false if there was a problem.
     */
    public static boolean setLastVersionUsed(
            @NonNull final String pluginName, @NonNull final String version) {
        PluginFolder.createFolder(pluginName);
        String pathToVersionFile =
                PluginFolder.getFolderForPlugin(pluginName)
                        + File.separator
                        + Constants.PLUGIN_VERSION_FILE;
        File versionFile = new File(pathToVersionFile);
        if (!versionFile.exists()) {
            try {
                boolean created = versionFile.createNewFile();
                if (!created) {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }

        try (FileWriter writer = new FileWriter(versionFile)) {
            writer.write(version);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /** Private constructor so this class is not initialized. */
    private PluginFolder() {}
}
