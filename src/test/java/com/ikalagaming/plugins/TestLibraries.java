package com.ikalagaming.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ikalagaming.event.EventManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Tests for shared libraries, which plugins use but are loaded outside of any plugin. The classes
 * used here are compiled at test time, so they can't be found on the test classpath.
 *
 * @author Ches Burks
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestLibraries {

    private static final String COUNTER_CLASS = "lib.counted.Counter";

    /** Counts how many times the library class has been initialized, like loading a native. */
    private static final String INIT_COUNT_PROPERTY = "ikala.test.libraries.initCount";

    /** The compiled library and plugin classes, keyed by their path in a jar. */
    private static Map<String, byte[]> classes;

    /** A different version of the library class, keyed by their path in a jar. */
    private static Map<String, byte[]> otherVersion;

    @TempDir static Path workFolder;

    @BeforeAll
    static void compileClasses() throws IOException {
        Map<String, String> sources = new HashMap<>();
        sources.put(COUNTER_CLASS, counterSource("library"));
        for (String plugin : List.of("UserA", "UserB")) {
            sources.put(
                    "libuser.%s.Main".formatted(plugin.toLowerCase()),
                            """
                    package libuser.%s;
                    public class Main extends com.ikalagaming.plugins.Plugin {
                        private String greeting;
                        public String getGreeting() { return greeting; }
                        @Override public String getName() { return "%s"; }
                        @Override public boolean onEnable() {
                            greeting = lib.counted.Counter.hello();
                            return true;
                        }
                    }
                    """
                            .formatted(plugin.toLowerCase(), plugin));
        }
        classes = PluginJars.compile(workFolder, sources);
        otherVersion =
                PluginJars.compile(workFolder, Map.of(COUNTER_CLASS, counterSource("other")));
    }

    /**
     * The source for the library class, which counts how many times it is initialized.
     *
     * @param greeting What the library returns from hello().
     * @return The source code.
     */
    private static String counterSource(String greeting) {
        return
                """
        package lib.counted;
        public class Counter {
            static {
                int count = Integer.getInteger("%s", 0);
                System.setProperty("%s", Integer.toString(count + 1));
            }
            public static String hello() { return "%s"; }
        }
        """
                .formatted(INIT_COUNT_PROPERTY, INIT_COUNT_PROPERTY, greeting);
    }

    @Mock private EventManager eventManager;

    @TempDir Path pluginFolder;

    @TempDir Path libraryFolder;

    private PluginManager pluginManager;

    @AfterEach
    void afterTest() {
        PluginManager.destroyInstance();
        System.clearProperty(INIT_COUNT_PROPERTY);
    }

    @BeforeEach
    void beforeTest() {
        System.clearProperty(INIT_COUNT_PROPERTY);
        pluginManager = PluginManager.getInstance(eventManager);
        pluginManager.setEnableOnLoad(true);
    }

    /**
     * The class files in a given package.
     *
     * @param packageName The package, like "lib.counted".
     * @return The class files from {@link #classes} in that package.
     */
    private Map<String, byte[]> classesIn(String packageName) {
        String prefix = packageName.replace('.', '/') + "/";
        return classes.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * The greeting a user plugin got from the library when it was enabled.
     *
     * @param plugin The plugin name.
     * @return The greeting.
     * @throws Exception If the greeting can't be read.
     */
    private String greetingFor(String plugin) throws Exception {
        Plugin user = pluginManager.getPlugin(plugin).orElseThrow();
        return (String) user.getClass().getMethod("getGreeting").invoke(user);
    }

    /**
     * The library class, as seen by a plugin.
     *
     * @param plugin The plugin name.
     * @return The class the plugin's class loader finds.
     * @throws ClassNotFoundException If the plugin can't see it.
     */
    private Class<?> counterFor(String plugin) throws ClassNotFoundException {
        return pluginManager
                .getPlugin(plugin)
                .orElseThrow()
                .getClass()
                .getClassLoader()
                .loadClass(COUNTER_CLASS);
    }

    /**
     * Write the library jar into the library folder.
     *
     * @param fileName The jar file name.
     * @param entries The classes to put in it.
     * @throws IOException If the jar can't be written.
     */
    private void writeLibrary(String fileName, Map<String, byte[]> entries) throws IOException {
        PluginJars.writeLibrary(libraryFolder.resolve(fileName), entries);
    }

    /**
     * Write a plugin that uses the library.
     *
     * @param name The plugin name, either UserA or UserB.
     * @param dependencies The hard dependencies of the plugin.
     * @param libraries The libraries the plugin declares.
     * @param extra Other files to put in the jar.
     * @throws IOException If the jar can't be written.
     */
    private void writeUser(
            String name,
            List<String> dependencies,
            List<String> libraries,
            Map<String, byte[]> extra)
            throws IOException {
        String packageName = "libuser." + name.toLowerCase();
        Map<String, byte[]> entries = new HashMap<>(classesIn(packageName));
        entries.putAll(extra);
        PluginJars.write(
                pluginFolder,
                name,
                packageName + ".Main",
                dependencies,
                List.of(),
                libraries,
                entries);
    }

    @Test
    void testConflictingVersionSkipped() throws Exception {
        writeLibrary("counted-1.0.0.jar", classesIn("lib.counted"));
        writeLibrary("counted-2.0.0.jar", otherVersion);
        writeUser("UserA", List.of(), List.of("counted"), Map.of());

        pluginManager.loadAllLibraries(libraryFolder.toString());
        pluginManager.loadAllPlugins(pluginFolder.toString());

        assertEquals("library", greetingFor("UserA"));
        assertFalse(pluginManager.loadLibrary(libraryFolder.resolve("counted-2.0.0.jar").toFile()));
        assertTrue(pluginManager.loadLibrary(libraryFolder.resolve("counted-1.0.0.jar").toFile()));
    }

    @Test
    void testLibraryAddedAtRuntime() throws Exception {
        writeUser("UserA", List.of(), List.of("counted"), Map.of());
        pluginManager.loadAllLibraries(libraryFolder.toString());

        pluginManager.loadAllPlugins(pluginFolder.toString());
        assertFalse(pluginManager.isLoaded("UserA"));

        // Library folders are checked again when a plugin needs something we don't have
        writeLibrary("counted-1.0.0.jar", classesIn("lib.counted"));
        pluginManager.loadAllPlugins(pluginFolder.toString());

        assertTrue(pluginManager.isEnabled("UserA"));
        assertEquals("library", greetingFor("UserA"));
        assertEquals(Set.of("counted"), pluginManager.getLibraryClassLoader().getLibraryNames());
    }

    @Test
    void testLibraryJarNames() {
        LibraryClassLoader.LibraryJar plain =
                LibraryClassLoader.LibraryJar.of(new File("lwjgl-glfw-3.3.6.jar"));
        assertEquals("lwjgl-glfw", plain.name());
        assertEquals("3.3.6", plain.version());
        assertEquals("", plain.classifier());

        LibraryClassLoader.LibraryJar natives =
                LibraryClassLoader.LibraryJar.of(new File("lwjgl-3.3.6-natives-windows.jar"));
        assertEquals("lwjgl", natives.name());
        assertEquals("3.3.6", natives.version());
        assertEquals("natives-windows", natives.classifier());

        LibraryClassLoader.LibraryJar snapshot =
                LibraryClassLoader.LibraryJar.of(
                        new File("imgui-java-binding-1.86.11-SNAPSHOT.jar"));
        assertEquals("imgui-java-binding", snapshot.name());
        assertEquals("1.86.11-SNAPSHOT", snapshot.version());

        LibraryClassLoader.LibraryJar unversioned =
                LibraryClassLoader.LibraryJar.of(new File("something.jar"));
        assertEquals("something", unversioned.name());
        assertEquals("", unversioned.version());
    }

    @Test
    void testLibraryNativesDontConflict() throws Exception {
        writeLibrary("counted-1.0.0.jar", classesIn("lib.counted"));
        writeLibrary("counted-1.0.0-natives-windows.jar", Map.of());

        pluginManager.loadAllLibraries(libraryFolder.toString());

        LibraryClassLoader libraries = pluginManager.getLibraryClassLoader();
        assertEquals(2, libraries.getURLs().length);
        assertEquals(Set.of("counted"), libraries.getLibraryNames());
    }

    @Test
    void testLibrarySharedBetweenPlugins() throws Exception {
        writeLibrary("counted-1.0.0.jar", classesIn("lib.counted"));
        writeUser("UserA", List.of(), List.of("counted"), Map.of());
        // Libraries are visible to every plugin, declaring them is just a check
        writeUser("UserB", List.of(), List.of(), Map.of());

        pluginManager.loadAllLibraries(libraryFolder.toString());
        pluginManager.loadAllPlugins(pluginFolder.toString());

        Class<?> counter = counterFor("UserA");
        assertSame(pluginManager.getLibraryClassLoader(), counter.getClassLoader());
        assertSame(counter, counterFor("UserB"));
        assertSame(counter, pluginManager.getSharedClassLoader().loadClass(COUNTER_CLASS));
        assertEquals("library", greetingFor("UserA"));
        assertEquals("library", greetingFor("UserB"));
        assertEquals(1, Integer.getInteger(INIT_COUNT_PROPERTY));
    }

    @Test
    void testLibraryTakesPrecedenceOverBundledCopy() throws Exception {
        writeLibrary("counted-1.0.0.jar", classesIn("lib.counted"));
        writeUser("UserA", List.of(), List.of("counted"), otherVersion);

        pluginManager.loadAllLibraries(libraryFolder.toString());
        pluginManager.loadAllPlugins(pluginFolder.toString());

        assertSame(pluginManager.getLibraryClassLoader(), counterFor("UserA").getClassLoader());
        assertEquals("library", greetingFor("UserA"));
    }

    @Test
    void testMissingLibraryNotLoaded() throws Exception {
        writeUser("UserA", List.of(), List.of("counted"), Map.of());
        writeUser("UserB", List.of("UserA"), List.of(), Map.of());

        pluginManager.loadAllLibraries(libraryFolder.toString());
        pluginManager.loadAllPlugins(pluginFolder.toString());

        assertFalse(pluginManager.isLoaded("UserA"));
        assertFalse(pluginManager.isLoaded("UserB"));
    }

    @Test
    void testInvalidLibraryName() {
        String info =
                "name: Bad\nversion: 1.0.0\nmain-class: a.B\nlibraries: [ok-name, 'not ok']\n";
        assertThrows(
                InvalidDescriptionException.class,
                () ->
                        new PluginInfo(
                                new ByteArrayInputStream(info.getBytes(StandardCharsets.UTF_8))));
    }

    @Test
    void testReloadKeepsLibrary() throws Exception {
        writeLibrary("counted-1.0.0.jar", classesIn("lib.counted"));
        writeUser("UserA", List.of(), List.of("counted"), Map.of());

        pluginManager.loadAllLibraries(libraryFolder.toString());
        pluginManager.loadAllPlugins(pluginFolder.toString());
        Class<?> before = counterFor("UserA");
        ClassLoader pluginLoaderBefore =
                pluginManager.getPlugin("UserA").orElseThrow().getClass().getClassLoader();

        assertTrue(pluginManager.reload("UserA"));

        Plugin reloaded = pluginManager.getPlugin("UserA").orElseThrow();
        assertNotSame(pluginLoaderBefore, reloaded.getClass().getClassLoader());
        assertSame(before, counterFor("UserA"));
        assertEquals("library", greetingFor("UserA"));
        // Like a native library, the library's static initializer only ever runs once
        assertEquals(1, Integer.getInteger(INIT_COUNT_PROPERTY));
    }

    @Test
    void testMissingLibraryFolder() {
        Path missing = libraryFolder.resolve("missing");
        pluginManager.loadAllLibraries(missing.toString());
        assertTrue(Files.notExists(missing));
        assertTrue(pluginManager.getLibraryClassLoader().getLibraryNames().isEmpty());
    }
}
