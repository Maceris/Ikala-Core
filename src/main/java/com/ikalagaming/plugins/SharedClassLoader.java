package com.ikalagaming.plugins;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * A class loader that can see the engine and every loaded plugin, used as the thread context class
 * loader so that engine code can look up plugin classes by name. Plugins themselves do not use
 * this, since they can only see their own dependencies.
 *
 * <p>The JVM remembers classes that were looked up through a class loader, so the plugin manager
 * replaces this whenever plugins are loaded or unloaded. That way unloaded plugin classes aren't
 * kept alive by it.
 *
 * @author Ches Burks
 */
@Slf4j
public class SharedClassLoader extends ClassLoader {

    static {
        if (!ClassLoader.registerAsParallelCapable()) {
            log.warn("Shared class loader failed to register as parallel capable");
        }
    }

    private final PluginManager manager;

    /**
     * Create a new shared class loader.
     *
     * @param manager The PluginManager to look up plugin classes from.
     * @param parent The parent classloader to use.
     */
    public SharedClassLoader(@NonNull final PluginManager manager, final ClassLoader parent) {
        super(parent);
        this.manager = manager;
    }

    /**
     * Looks up the class in the loaded plugins, since the parent couldn't find it.
     *
     * @param name The name of the class.
     * @return The resulting Class object.
     * @throws ClassNotFoundException If no plugin has the class.
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> result = manager.getClassByName(name);
        if (result == null) {
            throw new ClassNotFoundException(name);
        }
        return result;
    }
}
