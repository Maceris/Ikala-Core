package com.ikalagaming.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tests for which classes and resources plugins can see. The plugin classes used here are compiled
 * at test time and only exist inside the plugin jars, so they can't be found on the test classpath.
 *
 * @author Ches Burks
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestClassVisibility {

    private static final String LIBRARY_CLASS = "vis.library.Shared";
    private static final String THING_CLASS = "vis.dup.Thing";

    /** The compiled main class and library classes, keyed by their path in a jar. */
    private static Map<String, byte[]> classes;

    /** One version of {@link #THING_CLASS} per plugin that contains it. */
    private static Map<String, Map<String, byte[]>> things;

    /** A version of {@link Shadowed} that is different from the one on the test classpath. */
    private static Map<String, byte[]> shadowed;

    @TempDir static Path workFolder;

    @BeforeAll
    static void compilePlugins() throws IOException {
        Map<String, String> sources = new HashMap<>();
        for (String plugin :
                List.of("Library", "Stranger", "SoftUser", "Top", "Shadowing", "DupA", "DupB")) {
            sources.put("vis.%s.Main".formatted(plugin.toLowerCase()), mainSource(plugin));
        }
        sources.put(
                LIBRARY_CLASS,
                """
                package vis.library;
                public class Shared {
                    public static String hello() { return "hello from the library"; }
                }
                """);
        // Links directly against the library, so it can only be enabled if it can see it
        sources.put(
                "vis.uses.Main",
                """
                package vis.uses;
                public class Main extends com.ikalagaming.plugins.Plugin {
                    private String greeting;
                    public String getGreeting() { return greeting; }
                    @Override public String getName() { return "UsesLibrary"; }
                    @Override public boolean onEnable() {
                        greeting = vis.library.Shared.hello();
                        return true;
                    }
                }
                """);
        classes = PluginJars.compile(workFolder, sources);

        things = new HashMap<>();
        for (String plugin : List.of("DupA", "DupB")) {
            things.put(
                    plugin,
                    PluginJars.compile(
                            workFolder,
                            Map.of(
                                    THING_CLASS,
                                            """
                                    package vis.dup;
                                    public class Thing {
                                        public static String origin() { return "%s"; }
                                    }
                                    """
                                            .formatted(plugin))));
        }

        shadowed =
                PluginJars.compile(
                        workFolder,
                        Map.of(
                                Shadowed.class.getName(),
                                """
                                package com.ikalagaming.plugins;
                                public final class Shadowed {
                                    public static String origin() { return "plugin"; }
                                }
                                """));
    }

    /**
     * The source for a plugin main class that does nothing.
     *
     * @param plugin The plugin name.
     * @return The source code.
     */
    private static String mainSource(String plugin) {
        return
                """
                package vis.%s;
                public class Main extends com.ikalagaming.plugins.Plugin {
                    @Override public String getName() { return "%s"; }
                }
                """
                .formatted(plugin.toLowerCase(), plugin);
    }

    @Mock private EventManager eventManager;

    @TempDir Path folder;

    private PluginManager pluginManager;

    @AfterEach
    void afterTest() {
        PluginManager.destroyInstance();
    }

    @BeforeEach
    void beforeTest() {
        pluginManager = PluginManager.getInstance(eventManager);
        pluginManager.setEnableOnLoad(false);
    }

    /**
     * The class files in a given package.
     *
     * @param packageName The package, like "vis.library".
     * @return The class files from {@link #classes} in that package.
     */
    private Map<String, byte[]> classesIn(String packageName) {
        String prefix = packageName.replace('.', '/') + "/";
        return classes.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * The class loader for a loaded plugin.
     *
     * @param plugin The plugin name.
     * @return The class loader that loaded its main class.
     */
    private ClassLoader loaderFor(String plugin) {
        return pluginManager.getPlugin(plugin).orElseThrow().getClass().getClassLoader();
    }

    /**
     * Write a plugin jar with its compiled main class.
     *
     * @param name The plugin name.
     * @param dependencies The hard dependencies.
     * @param softDependencies The soft dependencies.
     * @param extra Other files to include in the jar.
     * @throws IOException If the jar can't be written.
     */
    private void writePlugin(
            String name,
            List<String> dependencies,
            List<String> softDependencies,
            Map<String, byte[]> extra)
            throws IOException {
        String packageName = "vis." + name.toLowerCase();
        Map<String, byte[]> entries = new HashMap<>(classesIn(packageName));
        entries.putAll(extra);
        PluginJars.write(
                folder, name, packageName + ".Main", dependencies, softDependencies, entries);
    }

    /**
     * Write the library plugin, and a plugin that uses it.
     *
     * @throws IOException If the jars can't be written.
     */
    private void writeLibraryAndUser() throws IOException {
        writePlugin("Library", List.of(), List.of(), classesIn("vis.library"));
        PluginJars.write(
                folder,
                "UsesLibrary",
                "vis.uses.Main",
                List.of("Library"),
                List.of(),
                classesIn("vis.uses"));
    }

    @Test
    void testDependencyClassesVisible() throws Exception {
        writeLibraryAndUser();
        pluginManager.loadAllPlugins(folder.toString());

        Class<?> library = loaderFor("Library").loadClass(LIBRARY_CLASS);
        assertSame(library, loaderFor("UsesLibrary").loadClass(LIBRARY_CLASS));

        // Linking against the dependency's class works too
        assertTrue(pluginManager.enable("UsesLibrary"));
        Plugin user = pluginManager.getPlugin("UsesLibrary").orElseThrow();
        assertEquals(
                "hello from the library", user.getClass().getMethod("getGreeting").invoke(user));
    }

    @Test
    void testDuplicateClassesAreSeparate() throws Exception {
        writePlugin("DupA", List.of(), List.of(), things.get("DupA"));
        writePlugin("DupB", List.of(), List.of(), things.get("DupB"));
        pluginManager.loadAllPlugins(folder.toString());

        Class<?> thingA = loaderFor("DupA").loadClass(THING_CLASS);
        Class<?> thingB = loaderFor("DupB").loadClass(THING_CLASS);

        assertNotSame(thingA, thingB);
        assertEquals("DupA", thingA.getMethod("origin").invoke(null));
        assertEquals("DupB", thingB.getMethod("origin").invoke(null));
    }

    @Test
    void testEngineAndPlatformClassesShared() throws Exception {
        writePlugin("Stranger", List.of(), List.of(), Map.of());
        pluginManager.loadAllPlugins(folder.toString());
        ClassLoader loader = loaderFor("Stranger");

        assertSame(Plugin.class, loader.loadClass(Plugin.class.getName()));
        assertSame(String.class, loader.loadClass(String.class.getName()));
    }

    @Test
    void testOwnJarTakesPrecedence() throws Exception {
        Map<String, byte[]> extra = new HashMap<>(shadowed);
        // Also on the test classpath, and the parent would find it first if it were checked first
        extra.put("log4j2-test.xml", "plugin resource".getBytes(StandardCharsets.UTF_8));
        writePlugin("Shadowing", List.of(), List.of(), extra);
        pluginManager.loadAllPlugins(folder.toString());
        ClassLoader loader = loaderFor("Shadowing");

        Class<?> pluginVersion = loader.loadClass(Shadowed.class.getName());
        assertSame(loader, pluginVersion.getClassLoader());
        assertEquals("plugin", pluginVersion.getMethod("origin").invoke(null));
        assertEquals("classpath", Shadowed.origin());

        try (InputStream resource = loader.getResourceAsStream("log4j2-test.xml")) {
            assertEquals(
                    "plugin resource", new String(resource.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    @Test
    void testSharedClassLoaderForgetsUnloadedPlugins() throws Exception {
        writePlugin("Library", List.of(), List.of(), classesIn("vis.library"));
        pluginManager.loadAllPlugins(folder.toString());

        Class<?> library = loaderFor("Library").loadClass(LIBRARY_CLASS);
        assertSame(library, pluginManager.getSharedClassLoader().loadClass(LIBRARY_CLASS));

        pluginManager.unloadPlugin("Library");

        ClassLoader shared = pluginManager.getSharedClassLoader();
        assertThrows(ClassNotFoundException.class, () -> shared.loadClass(LIBRARY_CLASS));
        assertSame(shared, Thread.currentThread().getContextClassLoader());
    }

    @Test
    void testSoftDependencyClassesVisible() throws Exception {
        writePlugin("Library", List.of(), List.of(), classesIn("vis.library"));
        writePlugin("SoftUser", List.of(), List.of("Library"), Map.of());
        pluginManager.loadAllPlugins(folder.toString());

        assertSame(
                loaderFor("Library").loadClass(LIBRARY_CLASS),
                loaderFor("SoftUser").loadClass(LIBRARY_CLASS));
    }

    @Test
    void testTransitiveDependencyClassesNotVisible() throws Exception {
        writeLibraryAndUser();
        writePlugin("Top", List.of("UsesLibrary"), List.of(), Map.of());
        pluginManager.loadAllPlugins(folder.toString());
        ClassLoader loader = loaderFor("Top");

        assertSame(
                loaderFor("UsesLibrary").loadClass("vis.uses.Main"),
                loader.loadClass("vis.uses.Main"));
        assertThrows(ClassNotFoundException.class, () -> loader.loadClass(LIBRARY_CLASS));
    }

    @Test
    void testUndeclaredPluginClassesNotVisible() throws Exception {
        writePlugin("Library", List.of(), List.of(), classesIn("vis.library"));
        writePlugin("Stranger", List.of(), List.of(), Map.of());
        pluginManager.loadAllPlugins(folder.toString());

        assertThrows(
                ClassNotFoundException.class, () -> loaderFor("Stranger").loadClass(LIBRARY_CLASS));
    }
}
