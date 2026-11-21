package com.ikalagaming.plugins.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.launcher.PluginFolder;
import com.ikalagaming.launcher.PluginFolder.ResourceType;
import com.ikalagaming.plugins.PluginJars;
import com.ikalagaming.plugins.PluginManager;
import com.ikalagaming.plugins.RecordingPlugin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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

    @TempDir Path jarFolder;

    @AfterEach
    void afterTest() throws IOException {
        PluginFolder.deleteFolder(PLUGIN);
        deleteUserConfigs();
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
    void beforeTest() throws IOException {
        PluginManager.getInstance(eventManager);
        PluginFolder.deleteFolder(PLUGIN);
        deleteUserConfigs();
    }

    /**
     * Delete the test plugin's config overrides from the Home.
     *
     * @throws IOException If they can't be deleted.
     */
    private void deleteUserConfigs() throws IOException {
        Path folder = PluginFolder.getUserConfig(PLUGIN, "x").toPath().getParent();
        if (!Files.isDirectory(folder)) {
            return;
        }
        try (var files = Files.list(folder)) {
            for (Path file : files.toList()) {
                Files.delete(file);
            }
        }
        Files.delete(folder);
    }

    /**
     * Write a user override for the test plugin's config, in the Home.
     *
     * @param configName The name of the config file.
     * @param contents The contents of the file.
     * @return The path to the file.
     * @throws IOException If the file could not be written.
     */
    private Path writeUserConfig(String configName, String contents) throws IOException {
        Path file = PluginFolder.getUserConfig(PLUGIN, configName).toPath();
        Files.createDirectories(file.getParent());
        Files.writeString(file, contents);
        return file;
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
    void testCacheClearedWhenPluginUnloads() throws IOException {
        PluginJars.write(jarFolder, PLUGIN, RecordingPlugin.class);
        PluginManager pluginManager = PluginManager.getInstance(eventManager);
        pluginManager.setEnableOnLoad(false);
        pluginManager.loadAllPlugins(jarFolder.toString());
        ConfigManager.loadConfig(PLUGIN, "unload.yml").set("value", 1);

        assertTrue(pluginManager.unloadPlugin(PLUGIN));

        assertFalse(ConfigManager.loadConfig(PLUGIN, "unload.yml").isPresent("value"));
    }

    @Test
    void testClearCache() {
        PluginConfig config = ConfigManager.reloadConfig(PLUGIN, "cache.yml");
        config.set("value", 1);
        assertSame(config, ConfigManager.loadConfig(PLUGIN, "cache.yml"));

        ConfigManager.clearCache(PLUGIN);

        assertFalse(ConfigManager.loadConfig(PLUGIN, "cache.yml").isPresent("value"));
    }

    @Test
    void testConcurrentLoadsGetSameConfig() throws Exception {
        final String configName = "concurrent.yml";
        writeConfig(configName, "value: 1\n");
        final int threads = 8;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        try {
            List<Future<PluginConfig>> results = new ArrayList<>();
            for (int i = 0; i < threads; ++i) {
                results.add(
                        executor.submit(
                                () -> {
                                    start.await();
                                    return ConfigManager.loadConfig(PLUGIN, configName);
                                }));
            }
            start.countDown();

            PluginConfig first = results.get(0).get(30, TimeUnit.SECONDS);
            for (Future<PluginConfig> result : results) {
                assertSame(first, result.get(30, TimeUnit.SECONDS));
            }
            assertSame(first, ConfigManager.loadConfig(PLUGIN, configName));
        } finally {
            executor.shutdownNow();
            ConfigManager.clearCache(PLUGIN);
        }
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
        Path file = PluginFolder.getUserConfig(PLUGIN, configName).toPath();

        PluginConfig config = ConfigManager.reloadConfig(PLUGIN, configName);
        config.set("value", "created");
        ConfigManager.saveConfigToDisk(PLUGIN, configName);

        assertTrue(Files.exists(file));
        assertEquals("created", ConfigManager.reloadConfig(PLUGIN, configName).getString("value"));
    }

    @Test
    void testUserConfigOverridesShipped() throws IOException {
        final String configName = "layered.yml";
        writeConfig(configName, "kept: 1\nchanged: 2\nsection:\n  a: 1\n  b: 2\n");
        writeUserConfig(configName, "changed: 3\nsection:\n  b: 4\n");

        PluginConfig config = ConfigManager.reloadConfig(PLUGIN, configName);

        assertEquals(1, config.getInt("kept"));
        assertEquals(3, config.getInt("changed"));
        assertEquals(1, config.getInt("section.a"));
        assertEquals(4, config.getInt("section.b"));
    }

    @Test
    void testSaveKeepsOnlyChanges() throws IOException {
        final String configName = "changes.yml";
        Path shipped = writeConfig(configName, "kept: 1\nchanged: 2\n");
        String shippedContents = Files.readString(shipped);

        PluginConfig config = ConfigManager.reloadConfig(PLUGIN, configName);
        config.set("changed", 5);
        ConfigManager.saveConfigToDisk(PLUGIN, configName);

        // The shipped file is never written to
        assertEquals(shippedContents, Files.readString(shipped));
        Path user = PluginFolder.getUserConfig(PLUGIN, configName).toPath();
        assertEquals("changed: 5", Files.readString(user).strip());

        config.set("changed", 2);
        ConfigManager.saveConfigToDisk(PLUGIN, configName);
        assertFalse(Files.exists(user));
    }

    @Test
    void testSaveTruncatesExistingFile() throws IOException {
        final String configName = "truncate.yml";
        Path file =
                writeUserConfig(
                        configName,
                        "value: a long string that takes up much more room than the new one\n");

        PluginConfig config = ConfigManager.reloadConfig(PLUGIN, configName);
        config.set("value", "short");
        ConfigManager.saveConfigToDisk(PLUGIN, configName);

        assertEquals("value: short", Files.readString(file).strip());
        assertEquals("short", ConfigManager.reloadConfig(PLUGIN, configName).getString("value"));
    }

    @Test
    void testDefaultsReferenceWritten() throws IOException {
        PluginJars.write(
                jarFolder,
                PLUGIN,
                RecordingPlugin.class.getName(),
                List.of(),
                List.of(),
                Map.of("config.yml", "# How loud\nvolume: 5\n".getBytes(StandardCharsets.UTF_8)));
        PluginManager pluginManager = PluginManager.getInstance(eventManager);
        pluginManager.setEnableOnLoad(false);
        pluginManager.loadAllPlugins(jarFolder.toString());

        PluginConfig config = ConfigManager.reloadConfig(PLUGIN, ConfigManager.DEFAULT_NAME);

        assertEquals(5, config.getInt("volume"));
        Path reference = PluginFolder.getUserConfig(PLUGIN, "config.defaults.yml").toPath();
        String text = Files.readString(reference);
        assertTrue(text.contains("# How loud\nvolume: 5\n"));
        // Only the reference is written; the user's own file appears once they change something
        assertFalse(
                Files.exists(
                        PluginFolder.getUserConfig(PLUGIN, ConfigManager.DEFAULT_NAME).toPath()));
        assertTrue(pluginManager.unloadPlugin(PLUGIN));
    }

    @Test
    void testDefaultsName() {
        assertEquals("config.defaults.yml", ConfigManager.defaultsName("config.yml"));
        assertEquals("keys.defaults", ConfigManager.defaultsName("keys"));
    }
}
