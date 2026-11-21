package com.ikalagaming.launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ikalagaming.launcher.PluginFolder.ResourceType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tests for plugin data folders.
 *
 * @author Ches Burks
 */
class TestPluginFolder {

    private static final String PLUGIN = "TestPluginFolderPlugin";

    @AfterEach
    void afterTest() {
        PluginFolder.deleteFolder(PLUGIN);
        PluginFolder.forgetLastVersionUsed(PLUGIN);
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

    /**
     * Write the test plugin's last version into the versions file directly.
     *
     * @param version The version, as written in the file.
     * @throws IOException If the file can't be written.
     */
    private void writeVersionFile(String version) throws IOException {
        Path file = PluginFolder.versionsFile();
        Files.createDirectories(file.getParent());
        Files.writeString(file, PLUGIN + ": '" + version + "'\n");
    }

    @Test
    void testInvalidLastVersionIgnored() throws IOException {
        writeVersionFile("not a version");
        assertEquals("0.0.0", PluginFolder.getLastVersionUsed(PLUGIN));
    }

    @Test
    void testLastVersionWhitespaceIgnored() throws IOException {
        writeVersionFile("  1.2.3 ");
        assertEquals("1.2.3", PluginFolder.getLastVersionUsed(PLUGIN));
    }

    @Test
    void testMissingLastVersion() {
        assertEquals("0.0.0", PluginFolder.getLastVersionUsed(PLUGIN));
    }

    @Test
    void testForgetLastVersion() {
        assertTrue(PluginFolder.setLastVersionUsed(PLUGIN, "1.0.0"));
        assertTrue(PluginFolder.forgetLastVersionUsed(PLUGIN));
        assertEquals("0.0.0", PluginFolder.getLastVersionUsed(PLUGIN));
    }

    @Test
    void testResourcesNextToRegisteredJar(@TempDir Path jarFolder) {
        PluginFolder.setParentFolder(PLUGIN, jarFolder);
        assertEquals(
                jarFolder.resolve(PLUGIN).resolve("data").resolve("a.txt").toFile(),
                PluginFolder.getResource(PLUGIN, ResourceType.DATA, "a.txt"));
        PluginFolder.setParentFolder(PLUGIN, AppPaths.plugins());
    }

    @Test
    void testUserConfigInHome() {
        assertEquals(
                AppPaths.settings()
                        .resolve("plugins")
                        .resolve(PLUGIN)
                        .resolve("config.yml")
                        .toFile(),
                PluginFolder.getUserConfig(PLUGIN, "config.yml"));
    }

    @Test
    void testSetLastVersion() {
        assertTrue(PluginFolder.setLastVersionUsed(PLUGIN, "1.0.0"));
        assertTrue(PluginFolder.setLastVersionUsed(PLUGIN, "2.0.0-beta.1"));
        assertEquals("2.0.0-beta.1", PluginFolder.getLastVersionUsed(PLUGIN));
    }
}
