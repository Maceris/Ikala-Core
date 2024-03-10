package com.ikalagaming.plugins;

import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A custom Class that can handle loading classes from Jar files.
 *
 * @author Ches Burks
 */
@Slf4j
public class PluginClassLoader extends URLClassLoader {

    /** Classes known by this class loader, keyed by the class name. */
    private final Map<String, Class<?>> classes = new HashMap<>();

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
            @NonNull final PluginManager manager, final ClassLoader parent, final File file)
            throws MalformedURLException {

        super(new URL[] {file.toURI().toURL()}, parent);
        this.manager = manager;
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
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return this.findClass(name, true);
    }

    /**
     * Finds and loads the class with the specified name from the URL search path. Any URLs
     * referring to JAR files are loaded and opened as needed until the class is found.
     *
     * @param name The name of the class.
     * @param checkGlobal If we want to check all the classes across plugins. False to only check
     *     this plugins classes.
     * @return The class that was found,
     * @throws ClassNotFoundException If the class was not found.
     */
    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {

        Class<?> result = classes.get(name);

        if (result != null) {
            return result;
        }

        if (checkGlobal) {
            result = manager.getClassByName(name);
        }

        if (result == null) {
            result = super.findClass(name);
        }

        if (result == null) {
            throw new ClassNotFoundException(name);
        }

        // we did find it in the parent
        manager.setClass(name, result);
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
