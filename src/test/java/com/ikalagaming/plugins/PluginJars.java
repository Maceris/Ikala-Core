package com.ikalagaming.plugins;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Stream;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Builds plugin jars for tests.
 *
 * @author Ches Burks
 */
final class PluginJars {

    /**
     * Compile Java sources against the engine classes.
     *
     * @param workFolder A folder we can create temporary files in.
     * @param sources Source code, keyed by fully qualified class name.
     * @return The compiled class files, keyed by their path in a jar, like "a/b/C.class".
     * @throws IOException If there is a problem reading or writing files.
     */
    static Map<String, byte[]> compile(Path workFolder, Map<String, String> sources)
            throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        assumeTrue(compiler != null, "Compiling test plugins requires a JDK");

        Path sourceFolder = Files.createTempDirectory(workFolder, "src");
        Path outputFolder = Files.createTempDirectory(workFolder, "out");
        List<Path> files = new ArrayList<>();
        for (Map.Entry<String, String> source : sources.entrySet()) {
            Path file = sourceFolder.resolve(source.getKey().replace('.', '/') + ".java");
            Files.createDirectories(file.getParent());
            Files.writeString(file, source.getValue());
            files.add(file);
        }

        String engineClasses;
        try {
            engineClasses =
                    Path.of(
                                    Plugin.class
                                            .getProtectionDomain()
                                            .getCodeSource()
                                            .getLocation()
                                            .toURI())
                            .toString();
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }

        try (StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8)) {
            List<String> options =
                    List.of(
                            "-d",
                            outputFolder.toString(),
                            "-classpath",
                            engineClasses,
                            "-proc:none");
            boolean compiled =
                    compiler.getTask(
                                    null,
                                    fileManager,
                                    null,
                                    options,
                                    null,
                                    fileManager.getJavaFileObjectsFromPaths(files))
                            .call();
            assertTrue(compiled, "Test plugin sources failed to compile");
        }

        Map<String, byte[]> classes = new HashMap<>();
        try (Stream<Path> outputs = Files.walk(outputFolder)) {
            for (Path file : outputs.filter(Files::isRegularFile).toList()) {
                String entry = outputFolder.relativize(file).toString().replace('\\', '/');
                classes.put(entry, Files.readAllBytes(file));
            }
        }
        return classes;
    }

    /**
     * Write a plugin jar to the given folder, containing only a plugin.yml. The main class must be
     * on the test classpath.
     *
     * @param folder The folder to write the jar into.
     * @param name The name of the plugin.
     * @param mainClass The main class of the plugin.
     * @param dependencies The hard dependencies of the plugin.
     * @throws IOException If the jar could not be written.
     */
    static void write(
            Path folder, String name, Class<? extends Plugin> mainClass, String... dependencies)
            throws IOException {
        write(folder, name, mainClass.getName(), List.of(dependencies), List.of(), Map.of());
    }

    /**
     * Write a plugin jar to the given folder.
     *
     * @param folder The folder to write the jar into.
     * @param name The name of the plugin.
     * @param mainClass The fully qualified name of the main class of the plugin.
     * @param dependencies The hard dependencies of the plugin.
     * @param softDependencies The soft dependencies of the plugin.
     * @param entries Other files to put in the jar, keyed by their path in the jar.
     * @throws IOException If the jar could not be written.
     */
    static void write(
            Path folder,
            String name,
            String mainClass,
            List<String> dependencies,
            List<String> softDependencies,
            Map<String, byte[]> entries)
            throws IOException {
        String info =
                "name: %s\nversion: 0.0.1\nmain-class: %s\ndependencies: [%s]\nsoft-dependencies: [%s]\n"
                        .formatted(
                                name,
                                mainClass,
                                String.join(", ", dependencies),
                                String.join(", ", softDependencies));

        try (OutputStream file = Files.newOutputStream(folder.resolve(name + ".jar"));
                JarOutputStream jar = new JarOutputStream(file)) {
            jar.putNextEntry(new JarEntry("plugin.yml"));
            jar.write(info.getBytes(StandardCharsets.UTF_8));
            jar.closeEntry();
            for (Map.Entry<String, byte[]> entry : entries.entrySet()) {
                jar.putNextEntry(new JarEntry(entry.getKey()));
                jar.write(entry.getValue());
                jar.closeEntry();
            }
        }
    }

    /** Private constructor so this class is not instantiated. */
    private PluginJars() {}
}
