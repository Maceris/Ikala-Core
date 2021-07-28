package com.ikalagaming.plugins;

import com.github.zafarkhaja.semver.ParseException;
import com.github.zafarkhaja.semver.Version;
import lombok.Getter;
import lombok.NonNull;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contains data about a particular plugin. This info can be loaded from code or
 * from a file.
 *
 * @author Ches Burks
 *
 */
public class PluginInfo {

	private static List<String> makePluginNameList(final Map<?, ?> map,
		final String key) throws InvalidDescriptionException {
		final Object value = map.get(key);
		final ArrayList<String> pluginNameList = new ArrayList<>();
		if (value == null) {
			return pluginNameList;
		}

		try {
			for (final Object entry : (Iterable<?>) value) {
				pluginNameList.add(entry.toString().replace(' ', '_'));
			}
		}
		catch (ClassCastException ex) {
			throw new InvalidDescriptionException(key + " is of the wrong type",
				ex);
		}
		catch (NullPointerException ex) {
			throw new InvalidDescriptionException("invalid " + key + " format",
				ex);
		}
		return pluginNameList;
	}

	/**
	 * The list of authors for the plugin. This is used to give credit to
	 * developers.
	 *
	 * @return The list of plugin authors.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private List<String> authors = new ArrayList<>();

	/**
	 * Returns a list of plugins this plugin requires in order to run. Use the
	 * value of {@link #getName()} for the target plugin to specify it in the
	 * dependencies. If any plugin in this list is not found, this plugin will
	 * fail to load at startup.
	 *
	 * @return The list of plugin dependencies.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private List<String> dependencies = new ArrayList<>();

	/**
	 * This is a short human-friendly description of what the plugin does. It
	 * may be multiple lines.
	 *
	 * @return The brief description of this plugin.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private String description = "";

	/**
	 * The fully qualified name of the class that extends {@link Plugin} for
	 * this plugin. The format should follow the
	 * {@link ClassLoader#loadClass(String)} syntax.
	 *
	 * @return The absolute path to the main plugin class.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private String mainClass = null;

	/**
	 * The name of the plugin. Names are unique for each plugin. The name can
	 * contain the following characters:
	 * <ul>
	 * <li>a-z</li>
	 * <li>A-Z</li>
	 * <li>0-9</li>
	 * <li>period</li>
	 * <li>hyphen</li>
	 * <li>underscore</li>
	 * </ul>
	 *
	 * @return The name of the plugin.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private String name = null;

	/**
	 * Returns a list of dependencies that are desired but not needed to run
	 *
	 * @return Soft dependencies for this plugin.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private List<String> softDependencies = new ArrayList<>();

	private Version version = Version.valueOf("0.0.0");

	/**
	 * Returns a plugin description loaded by the given InputStream, from a Yaml
	 * file. The tags that are required or possible are listed on the wiki.
	 *
	 * @param stream the steam to load info from
	 * @throws InvalidDescriptionException if the description is not valid
	 */
	public PluginInfo(@NonNull final InputStream stream)
		throws InvalidDescriptionException {

		Yaml yaml = new Yaml();
		this.loadMap(this.asMap(yaml.load(stream)));
	}

	private Map<?, ?> asMap(Object object) throws InvalidDescriptionException {
		if (object instanceof Map) {
			return (Map<?, ?>) object;
		}
		throw new InvalidDescriptionException(
			object + " is not properly structured.");
	}

	/**
	 * Extract the required fields as part of {@link #loadMap(Map)}.
	 *
	 * @param map The map we are loading from.
	 * @throws InvalidDescriptionException If the description is invalid.
	 */
	private void extractRequiredFields(Map<?, ?> map)
		throws InvalidDescriptionException {
		try {
			this.name = map.get("name").toString().toLowerCase();
			if (!this.name.matches("^[a-zA-Z0-9 _.-]+$")) {
				throw new InvalidDescriptionException(
					"name '" + this.name + "' contains invalid characters.");
			}
			this.name = this.name.replace(' ', '_');
		}
		catch (NullPointerException ex) {
			throw new InvalidDescriptionException("name is not defined", ex);
		}
		catch (ClassCastException ex) {
			throw new InvalidDescriptionException("name is of wrong type", ex);
		}

		try {
			this.version = Version.valueOf((String) map.get("version"));
		}
		catch (NullPointerException ex) {
			throw new InvalidDescriptionException("version is not defined", ex);
		}
		catch (ClassCastException ex) {
			throw new InvalidDescriptionException("version is of wrong type",
				ex);
		}
		catch (IllegalArgumentException ex) {
			throw new InvalidDescriptionException("version is not there", ex);
		}
		catch (ParseException ex) {
			throw new InvalidDescriptionException(
				"version is in an invalid format", ex);
		}

		try {
			this.mainClass = map.get("main-class").toString();
		}
		catch (NullPointerException ex) {
			throw new InvalidDescriptionException("main class is not defined",
				ex);
		}
		catch (ClassCastException ex) {
			throw new InvalidDescriptionException("main is of the wrong type",
				ex);
		}
	}

	/**
	 * Returns the full name of the plugin. This is a string that describes the
	 * plugin, such as "Graphics" or "AI", with version info appended.
	 *
	 * @return the full name
	 */
	public String getFullName() {
		return this.name + "-" + this.version;
	}

	/**
	 * The version of the plugin. This value is a string that follows the
	 * MajorVersion.MinorVersion.PatchVersion format. It should be increased
	 * when new features are added or bugs are fixed.
	 *
	 * @return the version of the plugin
	 */
	public String getVersion() {
		return this.version.getNormalVersion();
	}

	private void loadMap(Map<?, ?> map) throws InvalidDescriptionException {
		this.extractRequiredFields(map);

		this.dependencies = PluginInfo.makePluginNameList(map, "dependencies");
		this.softDependencies =
			PluginInfo.makePluginNameList(map, "soft-dependencies");

		if (map.get("description") != null) {
			this.description = map.get("description").toString();
		}

		if (map.get("authors") != null) {
			try {
				for (Object o : (Iterable<?>) map.get("authors")) {
					this.authors.add(o.toString());
				}
			}
			catch (ClassCastException ex) {
				throw new InvalidDescriptionException(
					"authors are of the wrong type", ex);
			}
			catch (NullPointerException ex) {
				throw new InvalidDescriptionException(
					"authors are not defined properly", ex);
			}
		}
	}

}
