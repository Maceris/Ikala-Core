package com.ikalagaming.plugins;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads third party libraries that plugins share, like LWJGL. It sits between the engine and the
 * plugins, so every plugin can see the libraries, and the libraries can see the engine.
 *
 * <p>Libraries can be added at any time, but are never removed. Many libraries load native code,
 * and the JVM only lets one class loader hold a native library, releasing it once that class loader
 * is garbage collected. If those libraries were loaded by plugin class loaders, reloading the
 * plugin would try to load the native library again from a new class loader and fail. Since this
 * class loader lives as long as the plugin manager, plugins that use libraries can be reloaded.
 *
 * <p>Libraries are identified by name only, which is the jar file name without its version, like
 * {@code lwjgl-glfw} for {@code lwjgl-glfw-3.3.6.jar}. Only one version of each library can be
 * added, since everything shares it.
 *
 * @author Ches Burks
 */
@Slf4j
public class LibraryClassLoader extends URLClassLoader {

    static {
        if (!ClassLoader.registerAsParallelCapable()) {
            log.warn("Library class loader failed to register as parallel capable");
        }
    }

    /** What happened when trying to add a library jar. */
    public enum AddResult {
        /** The jar was added. */
        ADDED,
        /** The same version of the library was already added, so it was skipped. */
        ALREADY_ADDED,
        /** A different version of the library was already added, so it was skipped. */
        CONFLICTING_VERSION,
        /** The file is not a jar that can be added. */
        INVALID
    }

    /**
     * Details about a library jar, parsed from a file name like {@code
     * lwjgl-3.3.6-natives-windows.jar}.
     *
     * @param name The name of the library, like {@code lwjgl}.
     * @param version The version of the library, like {@code 3.3.6}. Empty if there isn't one.
     * @param classifier The native classifier, like {@code natives-windows}. Empty for jars that
     *     aren't natives.
     * @param file The jar file.
     */
    public record LibraryJar(String name, String version, String classifier, File file) {

        private static final String JAR_EXTENSION = ".jar";

        private static final String NATIVES = "-natives-";

        /**
         * Parse the library details from a jar file name. The name is everything before the first
         * hyphen that is followed by a digit, and the rest is the version. Natives jars have their
         * classifier split off of the version, so they don't conflict with the main jar.
         *
         * @param file The jar file.
         * @return The details for the library jar.
         */
        public static LibraryJar of(@NonNull File file) {
            String baseName = file.getName();
            if (baseName.endsWith(JAR_EXTENSION)) {
                baseName = baseName.substring(0, baseName.length() - JAR_EXTENSION.length());
            }

            int versionStart = -1;
            for (int i = 1; i < baseName.length(); ++i) {
                if (baseName.charAt(i - 1) == '-' && Character.isDigit(baseName.charAt(i))) {
                    versionStart = i;
                    break;
                }
            }
            if (versionStart < 0) {
                return new LibraryJar(baseName, "", "", file);
            }

            String name = baseName.substring(0, versionStart - 1);
            String rest = baseName.substring(versionStart);
            int natives = rest.indexOf(NATIVES);
            if (natives < 0) {
                return new LibraryJar(name, rest, "", file);
            }
            return new LibraryJar(
                    name, rest.substring(0, natives), rest.substring(natives + 1), file);
        }

        /**
         * The key that identifies this jar, ignoring the version. A library and its natives have
         * different keys.
         *
         * @return The name, plus the classifier if there is one.
         */
        String key() {
            return classifier.isEmpty() ? name : name + ":" + classifier;
        }
    }

    /** The jars that have been added, keyed by {@link LibraryJar#key()}. */
    private final Map<String, LibraryJar> jars = new ConcurrentHashMap<>();

    /**
     * Create a new library class loader, with no libraries yet.
     *
     * @param parent The parent class loader, which should be able to load engine classes.
     */
    public LibraryClassLoader(final ClassLoader parent) {
        super("ikala-libraries", new URL[0], parent);
    }

    /**
     * Add a library jar, so that its classes and resources can be loaded. If a version of the same
     * library was already added, the jar is skipped.
     *
     * @param file The library jar.
     * @return What happened.
     */
    public synchronized AddResult addLibrary(@NonNull File file) {
        if (!file.isFile() || !file.getName().endsWith(LibraryJar.JAR_EXTENSION)) {
            return AddResult.INVALID;
        }
        LibraryJar jar = LibraryJar.of(file);
        LibraryJar existing = jars.get(jar.key());
        if (existing != null) {
            return existing.version().equals(jar.version())
                    ? AddResult.ALREADY_ADDED
                    : AddResult.CONFLICTING_VERSION;
        }
        try {
            addURL(file.toURI().toURL());
        } catch (MalformedURLException e) {
            return AddResult.INVALID;
        }
        jars.put(jar.key(), jar);
        return AddResult.ADDED;
    }

    /**
     * Find the jar that was added for a library.
     *
     * @param key The library name, plus {@code :classifier} for natives.
     * @return The jar, or null if none was added for that library.
     */
    public LibraryJar getJar(@NonNull String key) {
        return jars.get(key);
    }

    /**
     * The names of all the libraries that have been added.
     *
     * @return The library names, sorted.
     */
    public Set<String> getLibraryNames() {
        Set<String> names = new TreeSet<>();
        jars.values().forEach(jar -> names.add(jar.name()));
        return names;
    }

    /**
     * Check if a library has been added.
     *
     * @param name The name of the library, without a version.
     * @return True if some version of the library has been added.
     */
    public boolean hasLibrary(@NonNull String name) {
        return jars.values().stream().anyMatch(jar -> jar.name().equals(name));
    }

    /**
     * Check if a class was defined from one of the library jars, as opposed to the engine.
     *
     * @param clazz The class to check.
     * @return True if the class came from a library.
     */
    boolean isLibraryClass(@NonNull Class<?> clazz) {
        return clazz.getClassLoader() == this;
    }
}
