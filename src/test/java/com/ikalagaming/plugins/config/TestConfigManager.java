package com.ikalagaming.plugins.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.launcher.PluginFolder;
import com.ikalagaming.launcher.PluginFolder.ResourceType;
import com.ikalagaming.plugins.PluginManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tests for reading and writing configuration files.
 *
 * @author Ches Burks
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestConfigManager {

    private static final String PLUGIN = "TestConfigManagerPlugin";

    @Mock private EventManager eventManager;

    @AfterEach
    void afterTest() {
        PluginFolder.deleteFolder(PLUGIN);
        PluginManager.destroyInstance();
        // Don't leave behind an empty plugin folder that the tests created
        Path pluginsFolder =
                PluginFolder.getResource(PLUGIN, ResourceType.CONFIG, "")
                        .toPath()
                        .getParent()
                        .getParent();
        try {
            Files.deleteIfExists(pluginsFolder);
        } catch (IOException ignored) {
            // Not empty, so it was there before the tests
        }
    }

    @BeforeEach
    void beforeTest() {
        PluginManager.getInstance(eventManager);
        PluginFolder.deleteFolder(PLUGIN);
    }

    /**
     * Write a config file for the test plugin.
     *
     * @param configName The name of the config file.
     * @param contents The contents of the file.
     * @return The path to the file.
     * @throws IOException If the file could not be written.
     */
    private Path writeConfig(String configName, String contents) throws IOException {
        Path file = PluginFolder.getResource(PLUGIN, ResourceType.CONFIG, configName).toPath();
        Files.createDirectories(file.getParent());
        Files.writeString(file, contents);
        return file;
    }

    @Test
    void testEmptyConfigFile() throws IOException {
        final String configName = "empty.yml";
        writeConfig(configName, "");

        PluginConfig config = ConfigManager.reloadConfig(PLUGIN, configName);

        assertFalse(config.isPresent("value"));
        assertEquals(0, config.getInt("value"));
        config.set("value", 1);
        assertEquals(1, config.getInt("value"));
    }

    @Test
    void testLoadingDoesNotKeepFileOpen() throws IOException {
        final String configName = "open.yml";
        Path file = writeConfig(configName, "value: 1\n");

        ConfigManager.reloadConfig(PLUGIN, configName);

        // Fails on Windows if the file is still open
        assertDoesNotThrow(() -> Files.delete(file));
    }

    @Test
    void testSaveCreatesMissingFile() throws IOException {
        final String configName = "missing.yml";
        Path file = PluginFolder.getResource(PLUGIN, ResourceType.CONFIG, configName).toPath();

        PluginConfig config = ConfigManager.reloadConfig(PLUGIN, configName);
        config.set("value", "created");
        ConfigManager.saveConfigToDisk(PLUGIN, configName);

        assertTrue(Files.exists(file));
        assertEquals("created", ConfigManager.reloadConfig(PLUGIN, configName).getString("value"));
    }

    @Test
    void testSaveTruncatesExistingFile() throws IOException {
        final String configName = "truncate.yml";
        Path file =
                writeConfig(
                        configName,
                        "value: a long string that takes up much more room than the new one\n");

        PluginConfig config = ConfigManager.reloadConfig(PLUGIN, configName);
        config.set("value", "short");
        ConfigManager.saveConfigToDisk(PLUGIN, configName);

        assertEquals("value: short", Files.readString(file).strip());
        assertEquals("short", ConfigManager.reloadConfig(PLUGIN, configName).getString("value"));
    }
}
