package com.ikalagaming.launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ikalagaming.launcher.PluginFolder.ResourceType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

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
     * Write the last version file directly.
     *
     * @param contents The contents of the file.
     * @throws IOException If the file can't be written.
     */
    private void writeVersionFile(String contents) throws IOException {
        PluginFolder.createFolder(PLUGIN);
        Path file =
                Path.of(
                        System.getProperty("user.dir") + Constants.PLUGIN_FOLDER_PATH + PLUGIN,
                        Constants.PLUGIN_VERSION_FILE);
        Files.writeString(file, contents);
    }

    @Test
    void testInvalidLastVersionIgnored() throws IOException {
        writeVersionFile("not a version");
        assertEquals("0.0.0", PluginFolder.getLastVersionUsed(PLUGIN));
    }

    @Test
    void testLastVersionWhitespaceIgnored() throws IOException {
        writeVersionFile("  1.2.3\r\n");
        assertEquals("1.2.3", PluginFolder.getLastVersionUsed(PLUGIN));
    }

    @Test
    void testMissingLastVersion() {
        assertEquals("0.0.0", PluginFolder.getLastVersionUsed(PLUGIN));
    }

    @Test
    void testSetLastVersion() {
        assertTrue(PluginFolder.setLastVersionUsed(PLUGIN, "1.0.0"));
        assertTrue(PluginFolder.setLastVersionUsed(PLUGIN, "2.0.0-beta.1"));
        assertEquals("2.0.0-beta.1", PluginFolder.getLastVersionUsed(PLUGIN));
    }
}
