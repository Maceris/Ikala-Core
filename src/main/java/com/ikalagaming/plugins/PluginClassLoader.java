package com.ikalagaming.plugins;

import com.ikalagaming.util.SafeResourceLoader;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads classes and resources for a single plugin. Lookups go, in order, to:
 *
 * <ol>
 *   <li>The Java platform, so that plugins can't replace JDK classes
 *   <li>The plugin's own jar, which takes precedence over everything else
 *   <li>The parent class loader, which has the engine and its libraries
 *   <li>The jars of plugins this plugin declares as dependencies or soft dependencies, including
 *       any libraries bundled in those jars
 * </ol>
 *
 * <p>Plugins can't see classes from plugins they don't declare as dependencies.
 *
 * @author Ches Burks
 */
@Slf4j
public class PluginClassLoader extends URLClassLoader {

    static {
        if (!ClassLoader.registerAsParallelCapable()) {
            log.warn("Plugin class loader failed to register as parallel capable");
        }
    }

    /** The names of classes defined from this plugin's jar, so we can clean them up later. */
    private final Set<String> definedClasses = ConcurrentHashMap.newKeySet();

    private final PluginManager manager;

    /**
     * The name of the plugin this loads classes for.
     *
     * @return The name of the plugin.
     */
    @SuppressWarnings("javadoc")
    @Getter
    private final String pluginName;

    /**
     * Create a new plugin class loader.
     *
     * @param manager The PluginManager handling this plugin loader.
     * @param pluginName The name of the plugin this loads classes for.
     * @param parent The parent class loader, which should be able to load engine classes.
     * @param file The file where the plugin is located.
     * @throws MalformedURLException If the file URL cannot be parsed.
     */
    public PluginClassLoader(
            @NonNull final PluginManager manager,
            @NonNull final String pluginName,
            final ClassLoader parent,
            @NonNull final File file)
            throws MalformedURLException {
        super(new URL[] {file.toURI().toURL()}, parent);
        this.manager = manager;
        this.pluginName = pluginName;
    }

    /**
     * Unregisters all its classes from the plugin manager class cache, and cleans up references.
     * Also closes the files.
     */
    void dispose() {
        definedClasses.forEach(manager::removeClass);
        definedClasses.clear();
        try {
            close();
        } catch (IOException e) {
            String name;
            URL[] urls = getURLs();
            if (urls == null || urls.length == 0) {
                name = "?";
            } else {
                name = urls[0].getFile();
            }
            String err =
                    SafeResourceLoader.getString(
                            "PLUGIN_JAR_CLOSE_ERROR", manager.getResourceBundle());
            log.warn(err, name);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> result = super.findClass(name);
        definedClasses.add(name);
        return result;
    }

    /**
     * Look for a class in this plugin's own jar, without checking anywhere else. Used for dependent
     * plugins and the shared class loader. This only ever locks this class loader, so plugins that
     * depend on each other can't deadlock.
     *
     * @param name The binary name of the class.
     * @return The class if it is defined in this plugin's jar, or null if it isn't.
     */
    Class<?> findOwnClass(@NonNull String name) {
        synchronized (getClassLoadingLock(name)) {
            Class<?> loaded = findLoadedClass(name);
            if (loaded != null) {
                // We might have just been the loader that asked for it, not the one defining it
                return loaded.getClassLoader() == this ? loaded : null;
            }
            try {
                return findClass(name);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
    }

    /**
     * Look for a resource in this plugin's own jar, without checking anywhere else.
     *
     * @param name The resource name.
     * @return The resource URL, or null if it isn't in this plugin's jar.
     */
    URL findOwnResource(@NonNull String name) {
        return findResource(name);
    }

    @Override
    public URL getResource(String name) {
        URL result = findResource(name);
        if (result == null && getParent() != null) {
            result = getParent().getResource(name);
        }
        if (result == null) {
            for (PluginClassLoader dependency : manager.getDependencyClassLoaders(pluginName)) {
                result = dependency.findOwnResource(name);
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        List<URL> results = new ArrayList<>(Collections.list(findResources(name)));
        if (getParent() != null) {
            results.addAll(Collections.list(getParent().getResources(name)));
        }
        for (PluginClassLoader dependency : manager.getDependencyClassLoaders(pluginName)) {
            results.addAll(Collections.list(dependency.findResources(name)));
        }
        return Collections.enumeration(results);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> result = findLoadedClass(name);
        if (result == null) {
            result = loadFromPlatform(name);
        }
        if (result == null) {
            result = findOwnClass(name);
        }
        if (result == null && getParent() != null) {
            try {
                result = getParent().loadClass(name);
            } catch (ClassNotFoundException e) {
                // Not an engine class, try dependencies next
            }
        }
        if (result == null) {
            for (PluginClassLoader dependency : manager.getDependencyClassLoaders(pluginName)) {
                result = dependency.findOwnClass(name);
                if (result != null) {
                    break;
                }
            }
        }
        if (result == null) {
            throw new ClassNotFoundException(name);
        }
        if (resolve) {
            resolveClass(result);
        }
        return result;
    }

    /**
     * Load a class from the Java platform (JDK modules), if it is one.
     *
     * @param name The binary name of the class.
     * @return The class, or null if it isn't part of the platform.
     */
    private static Class<?> loadFromPlatform(String name) {
        try {
            return ClassLoader.getPlatformClassLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
