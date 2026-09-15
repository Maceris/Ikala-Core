package com.ikalagaming.plugins;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A custom ClassLoader that can defer to plugin classloaders if we don't find a class.
 *
 * @author Ches Burks
 */
@Slf4j
public class SharedClassLoader extends ClassLoader {

    /** Classes known by this class loader, keyed by the class name. */
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();

    private final ClassLoader parent;
    private final PluginManager manager;

    /**
     * Create a new plugin class loader.
     *
     * @param manager The PluginManager handling this plugin loader.
     * @param parent The parent classloader to use.
     */
    public SharedClassLoader(@NonNull final PluginManager manager, final ClassLoader parent) {
        super(parent);
        this.manager = manager;
        this.parent = parent;
        if (!registerAsParallelCapable()) {
            log.warn("Shared class loader failed to register as parallel capable");
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
     * @param name The binary name of the class
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
                    if (parent != null) {
                        c = parent.loadClass(name);
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found from the non-null parent
                    // class loader
                }

                if (c == null) {
                    // If still not found, then invoke findClass in order to find the class.
                    c = findClassInternal(name, calledByGetClassByName);
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
     * @return The resulting Class object.
     * @throws ClassNotFoundException If the class was not found.
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClassInternal(name, false);
    }

    /**
     * Looks up the class in the cache, or plugins if we aren't already doing that.
     *
     * @param name The name of the class.
     * @param calledByGetClassByName If this was called from {@link
     *     PluginManager#getClassByName(String)}.
     * @return The resulting Class object.
     * @throws ClassNotFoundException If the class was not found, or calledByGetClassByName is true.
     */
    private Class<?> findClassInternal(String name, boolean calledByGetClassByName)
            throws ClassNotFoundException {
        if (calledByGetClassByName) {
            throw new ClassNotFoundException(name);
        }

        Class<?> result = classes.get(name);
        if (result != null) {
            return result;
        }

        // Check the plugins for it
        result = manager.getClassByName(name);

        if (result == null) {
            throw new ClassNotFoundException(name);
        }
        classes.put(name, result);

        return result;
    }
}
