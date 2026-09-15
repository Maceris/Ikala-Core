package com.ikalagaming.plugins;

import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A custom Class that can handle loading classes from Jar files.
 *
 * @author Ches Burks
 */
@Slf4j
public class PluginClassLoader extends URLClassLoader {

    private final SharedClassLoader parent;

    /**
     * Classes known by this class loader, keyed by the class name. Used for quick lookup but also
     * so that we can easily dump all these classes at once when the plugin is being unloaded.
     */
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();

    private final PluginManager manager;

    /**
     * Create a new plugin class loader.
     *
     * @param manager The PluginManager handling this plugin loader.
     * @param parent The parent classloader to use.
     * @param file The file where the plugin is located.
     * @throws MalformedURLException If the file URL cannot be parsed.
     */
    public PluginClassLoader(
            @NonNull final PluginManager manager, final SharedClassLoader parent, final File file)
            throws MalformedURLException {

        super(new URL[] {file.toURI().toURL()}, parent);
        this.manager = manager;
        this.parent = parent;
        if (!registerAsParallelCapable()) {
            log.warn("Plugin class loader failed to register as parallel capable");
        }
    }

    /**
     * Unregisters all it's classes from the plugin manager class cache, and cleans up references.
     * Also closes the files.
     */
    void dispose() {
        getClasses().forEach(manager::removeClass);
        classes.clear();
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
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return loadClassInternal(name, resolve, false);
    }

    /**
     * Recreates the logic from {@link ClassLoader#loadClass(String, boolean)}, but slightly more
     * convoluted to avoid nested updates.
     *
     * @param name The binary name of the class.
     * @param resolve If true then resolve the class.
     * @param calledByGetClassByName If this was called from {@link
     *     PluginManager#getClassByName(String)}.
     * @return The resulting Class object.
     * @throws ClassNotFoundException If the class could not be found.
     */
    Class<?> loadClassInternal(String name, boolean resolve, boolean calledByGetClassByName)
            throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                try {
                    c = parent.loadClassInternal(name, resolve, calledByGetClassByName);
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found from the non-null parent
                    // class loader
                }

                if (c == null) {
                    // If still not found, then invoke findClass in order to find the class.
                    c = findClass(name);
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }

    /**
     * Finds and loads the class with the specified name from the URL search path. Any URLs
     * referring to JAR files are loaded and opened as needed until the class is found.
     *
     * @param name The name of the class.
     * @return The class that was found,
     * @throws ClassNotFoundException If the class was not found.
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // See if this plugin has already cached the value.
        Class<?> result = classes.get(name);

        if (result != null) {
            return result;
        }

        // Check the jar for files
        result = super.findClass(name);

        if (result == null) {
            throw new ClassNotFoundException(name);
        }

        // we did find it, cache for next time
        classes.put(name, result);

        return result;
    }

    /**
     * Returns the classes that have been loaded for this class so far.
     *
     * @return The classes this classloader has found.
     */
    Set<String> getClasses() {
        return classes.keySet();
    }
}
