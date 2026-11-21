package com.ikalagaming.launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Tests for working out the application's folders. */
class TestAppPaths {

    /** The properties the tests change, so they can be put back. */
    private static final List<String> PROPERTIES =
            List.of(
                    AppPaths.INSTALL_PROPERTY,
                    AppPaths.HOME_PROPERTY,
                    AppPaths.CACHE_PROPERTY,
                    AppPaths.INSTANCE_PROPERTY);

    /** The property values before each test. */
    private final Map<String, String> saved = new TreeMap<>();

    @TempDir Path install;

    @AfterEach
    void afterTest() {
        for (String property : PROPERTIES) {
            String value = saved.get(property);
            if (value == null) {
                System.clearProperty(property);
            } else {
                System.setProperty(property, value);
            }
        }
        AppPaths.reset();
    }

    @BeforeEach
    void beforeTest() {
        for (String property : PROPERTIES) {
            saved.put(property, System.getProperty(property));
        }
        System.setProperty(AppPaths.INSTALL_PROPERTY, install.toString());
        System.clearProperty(AppPaths.HOME_PROPERTY);
        System.clearProperty(AppPaths.CACHE_PROPERTY);
        System.clearProperty(AppPaths.INSTANCE_PROPERTY);
        AppPaths.reset();
    }

    @Test
    void testAppNameFromFile() throws IOException {
        Files.writeString(install.resolve(AppPaths.APP_FILE), "name: Lotomation\n");
        assertEquals("Lotomation", AppPaths.appName());
        assertEquals(AppPaths.defaultHome("Lotomation"), AppPaths.home());
        assertEquals(AppPaths.defaultCache("Lotomation"), AppPaths.cache());
    }

    @Test
    void testBadAppNameFallsBack() throws IOException {
        Files.writeString(install.resolve(AppPaths.APP_FILE), "name: ../escape\n");
        assertEquals(AppPaths.DEFAULT_APP_NAME, AppPaths.appName());
    }

    @Test
    void testFolderOptions() {
        List<String> rest =
                Bootstrap.applyFolderOptions(
                        new String[] {"-l", "en", "--home", "h", "--instance", "server"});
        assertEquals(List.of("-l", "en"), rest);
        assertEquals(Path.of("h").toAbsolutePath(), AppPaths.home());
        assertEquals(Path.of("server").toAbsolutePath().resolve("logs"), AppPaths.logs());
    }

    @Test
    void testFolderOptionNeedsValue() {
        assertNull(Bootstrap.applyFolderOptions(new String[] {"--home"}));
    }

    @Test
    void testHomeOverrideKeepsCache(@TempDir Path home) {
        System.setProperty(AppPaths.HOME_PROPERTY, home.toString());
        AppPaths.reset();
        assertEquals(home, AppPaths.home());
        assertEquals(home.resolve("cache"), AppPaths.cache());
        assertEquals(home.resolve("logs"), AppPaths.logs());
        assertTrue(AppPaths.instance().isEmpty());
    }

    @Test
    void testInstallFolders() {
        assertEquals(install, AppPaths.install());
        assertEquals(install.resolve("plugins"), AppPaths.plugins());
        assertEquals(install.resolve("libs"), AppPaths.libraries());
    }

    @Test
    void testPortable() throws IOException {
        Files.writeString(install.resolve(AppPaths.PORTABLE_FILE), "");
        assertEquals(install.resolve("home"), AppPaths.home());
        assertEquals(install.resolve("home").resolve("cache"), AppPaths.cache());
    }

    @Test
    void testResolve() {
        Path base = install.resolve("reports");
        assertEquals(base.resolve("a.txt"), AppPaths.resolve(base, "a.txt"));
        Path absolute = install.resolve("elsewhere.txt");
        assertEquals(absolute, AppPaths.resolve(base, absolute.toString()));
    }

    @Test
    void testValidNames() {
        assertTrue(AppPaths.isValidName("Ikala Asset Editor"));
        assertFalse(AppPaths.isValidName("a/b"));
        assertFalse(AppPaths.isValidName(".hidden"));
    }
}
