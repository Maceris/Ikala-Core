package com.ikalagaming.plugins;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * Builds plugin jars for tests. The jars only contain a plugin.yml, so the main class must be on
 * the test classpath.
 *
 * @author Ches Burks
 */
final class PluginJars {

    /**
     * Write a plugin jar to the given folder.
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
        String info =
                "name: %s\nversion: 0.0.1\nmain-class: %s\ndependencies: [%s]\n"
                        .formatted(name, mainClass.getName(), String.join(", ", dependencies));

        try (OutputStream file = Files.newOutputStream(folder.resolve(name + ".jar"));
                JarOutputStream jar = new JarOutputStream(file)) {
            jar.putNextEntry(new JarEntry("plugin.yml"));
            jar.write(info.getBytes(StandardCharsets.UTF_8));
            jar.closeEntry();
        }
    }

    /** Private constructor so this class is not instantiated. */
    private PluginJars() {}
}
