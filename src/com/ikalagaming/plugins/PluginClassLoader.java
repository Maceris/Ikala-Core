package com.ikalagaming.plugins;

import com.ikalagaming.util.SafeResourceLoader;

import lombok.CustomLog;
import lombok.NonNull;

import java.io.File;
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
 *
 */
@CustomLog(topic = PluginManager.PLUGIN_NAME)
public class PluginClassLoader extends URLClassLoader {

	private final Map<String, Class<?>> classes = new HashMap<>();
	private final PluginManager manager;
	/**
	 * The plugin this class loader loads classes for.
	 */
	final Plugin plugin;

	/**
	 * Create a new plugin class loader.
	 *
	 * @param manager The PluginManager handling this plugin loader.
	 * @param parent The parent classloader to use.
	 * @param pluginInfo Info about the plugin.
	 * @param file The file where the plugin is located.
	 *
	 * @throws InvalidPluginException If the plugin cannot be loaded properly.
	 * @throws MalformedURLException If the file URL cannot be parsed.
	 */
	public PluginClassLoader(@NonNull final PluginManager manager,
		final ClassLoader parent, final PluginInfo pluginInfo, final File file)
		throws InvalidPluginException, MalformedURLException {
		super(new URL[] {file.toURI().toURL()}, parent);

		this.manager = manager;

		Class<?> clazz;
		try {
			clazz = Class.forName(pluginInfo.getMain(), true, this);
		}
		catch (ClassNotFoundException e) {
			String err = SafeResourceLoader
				.getString("PLUGIN_MAIN_METHOD_MISSING",
					manager.getResourceBundle())
				.replaceFirst("\\$PLUGIN", pluginInfo.getName());
			PluginClassLoader.log.warning(err);
			throw new InvalidPluginException(err);
		}

		Class<? extends Plugin> pluginClass;
		try {
			pluginClass = clazz.asSubclass(Plugin.class);
			this.plugin = pluginClass.newInstance();
		}
		catch (ClassCastException ex) {
			String err = SafeResourceLoader
				.getString("PLUGIN_MAIN_NOT_A_PLUGIN",
					manager.getResourceBundle())
				.replaceFirst("\\$PLUGIN", pluginInfo.getName());
			PluginClassLoader.log.warning(err);
			throw new InvalidPluginException(err);
		}
		catch (InstantiationException e) {
			String err = SafeResourceLoader
				.getString("PLUGIN_CANT_INSTANTIATE_MAIN",
					manager.getResourceBundle())
				.replaceFirst("\\$PLUGIN", pluginInfo.getName());
			PluginClassLoader.log.warning(err);
			throw new InvalidPluginException(err);
		}
		catch (IllegalAccessException e) {
			String err = SafeResourceLoader
				.getString("PLUGIN_MAIN_ILLEGAL_ACCESS",
					manager.getResourceBundle())
				.replaceFirst("\\$PLUGIN", pluginInfo.getName());
			PluginClassLoader.log.warning(err);
			throw new InvalidPluginException(err);
		}
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return this.findClass(name, true);
	}

	/**
	 * Finds and loads the class with the specified name from the URL search
	 * path. Any URLs referring to JAR files are loaded and opened as needed
	 * until the class is found.
	 *
	 * @param name The name of the class.
	 * @param checkGlobal If we want to check all the classes across plugins.
	 *            False to only check this plugins classes.
	 * @return The class that was found,
	 * @throws ClassNotFoundException If the class was not found.
	 */
	Class<?> findClass(String name, boolean checkGlobal)
		throws ClassNotFoundException {

		Class<?> result = this.classes.get(name);

		if (result != null) {
			return result;
		}

		if (checkGlobal) {
			result = this.manager.getClassByName(name);
		}

		if (result == null) {
			result = super.findClass(name);
		}

		if (result == null) {
			throw new ClassNotFoundException(name);
		}

		// we did find it in the parent
		this.manager.setClass(name, result);
		this.classes.put(name, result);

		return result;
	}

	/**
	 * Returns the classes that have been loaded for this class so far.
	 *
	 * @return The classes this classloader has found.
	 */
	Set<String> getClasses() {
		return this.classes.keySet();
	}

}
