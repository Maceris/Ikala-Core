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
public final class PluginJars {

    /**
     * Compile Java sources against the engine classes.
     *
     * @param workFolder A folder we can create temporary files in.
     * @param sources Source code, keyed by fully qualified class name.
     * @return The compiled class files, keyed by their path in a jar, like "a/b/C.class".
     * @throws IOException If there is a problem reading or writing files.
     */
    public static Map<String, byte[]> compile(Path workFolder, Map<String, String> sources)
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
    public static void write(
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
    public static void write(
            Path folder,
            String name,
            String mainClass,
            List<String> dependencies,
            List<String> softDependencies,
            Map<String, byte[]> entries)
            throws IOException {
        write(folder, name, mainClass, dependencies, softDependencies, List.of(), entries);
    }

    /**
     * Write a plugin jar to the given folder, which requires shared libraries.
     *
     * @param folder The folder to write the jar into.
     * @param name The name of the plugin.
     * @param mainClass The fully qualified name of the main class of the plugin.
     * @param dependencies The hard dependencies of the plugin.
     * @param softDependencies The soft dependencies of the plugin.
     * @param libraries The libraries the plugin requires.
     * @param entries Other files to put in the jar, keyed by their path in the jar.
     * @throws IOException If the jar could not be written.
     */
    public static void write(
            Path folder,
            String name,
            String mainClass,
            List<String> dependencies,
            List<String> softDependencies,
            List<String> libraries,
            Map<String, byte[]> entries)
            throws IOException {
        writeJar(
                folder.resolve(name + ".jar"),
                name,
                "0.0.1",
                mainClass,
                dependencies,
                softDependencies,
                libraries,
                entries);
    }

    /**
     * Write a plain jar, like a library, with no plugin.yml.
     *
     * @param jarFile The jar file to write.
     * @param entries The files to put in the jar, keyed by their path in the jar.
     * @throws IOException If the jar could not be written.
     */
    public static void writeLibrary(Path jarFile, Map<String, byte[]> entries) throws IOException {
        try (OutputStream file = Files.newOutputStream(jarFile);
                JarOutputStream jar = new JarOutputStream(file)) {
            writeEntries(jar, entries);
        }
    }

    /**
     * Write a specific version of a plugin jar to the given folder, containing only a plugin.yml.
     * The main class must be on the test classpath.
     *
     * @param folder The folder to write the jar into.
     * @param fileName The file name of the jar, so that several versions can be side by side.
     * @param name The name of the plugin.
     * @param version The version of the plugin.
     * @param mainClass The main class of the plugin.
     * @param dependencies The hard dependencies of the plugin.
     * @throws IOException If the jar could not be written.
     */
    public static void writeVersion(
            Path folder,
            String fileName,
            String name,
            String version,
            Class<? extends Plugin> mainClass,
            String... dependencies)
            throws IOException {
        writeJar(
                folder.resolve(fileName),
                name,
                version,
                mainClass.getName(),
                List.of(dependencies),
                List.of(),
                List.of(),
                Map.of());
    }

    /**
     * Write a plugin jar.
     *
     * @param jarFile The jar file to write.
     * @param name The name of the plugin.
     * @param version The version of the plugin.
     * @param mainClass The fully qualified name of the main class of the plugin.
     * @param dependencies The hard dependencies of the plugin.
     * @param softDependencies The soft dependencies of the plugin.
     * @param libraries The libraries the plugin requires, left out of the plugin.yml if empty.
     * @param entries Other files to put in the jar, keyed by their path in the jar.
     * @throws IOException If the jar could not be written.
     */
    private static void writeJar(
            Path jarFile,
            String name,
            String version,
            String mainClass,
            List<String> dependencies,
            List<String> softDependencies,
            List<String> libraries,
            Map<String, byte[]> entries)
            throws IOException {
        String info =
                "name: %s\nversion: %s\nmain-class: %s\ndependencies: [%s]\nsoft-dependencies: [%s]\n"
                        .formatted(
                                name,
                                version,
                                mainClass,
                                String.join(", ", dependencies),
                                String.join(", ", softDependencies));
        if (!libraries.isEmpty()) {
            info += "libraries: [%s]\n".formatted(String.join(", ", libraries));
        }

        try (OutputStream file = Files.newOutputStream(jarFile);
                JarOutputStream jar = new JarOutputStream(file)) {
            jar.putNextEntry(new JarEntry("plugin.yml"));
            jar.write(info.getBytes(StandardCharsets.UTF_8));
            jar.closeEntry();
            writeEntries(jar, entries);
        }
    }

    /**
     * Write files into a jar.
     *
     * @param jar The jar being written.
     * @param entries The files to write, keyed by their path in the jar.
     * @throws IOException If the files could not be written.
     */
    private static void writeEntries(JarOutputStream jar, Map<String, byte[]> entries)
            throws IOException {
        for (Map.Entry<String, byte[]> entry : entries.entrySet()) {
            jar.putNextEntry(new JarEntry(entry.getKey()));
            jar.write(entry.getValue());
            jar.closeEntry();
        }
    }

    /** Private constructor so this class is not instantiated. */
    private PluginJars() {}
}
