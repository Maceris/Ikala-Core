package com.ikalagaming.plugins;

import com.ikalagaming.permissions.DefaultPermissionValue;
import com.ikalagaming.permissions.Permission;

import com.github.zafarkhaja.semver.ParseException;
import com.github.zafarkhaja.semver.Version;
import lombok.CustomLog;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Contains data about a particular plugin. This info can be loaded from code or
 * from a file.
 *
 * @author Ches Burks
 *
 */
@CustomLog(topic = PluginManager.PLUGIN_NAME)
public class PluginInfo {

	private static List<String> makePluginNameList(final Map<?, ?> map,
		final String key) throws InvalidDescriptionException {
		final Object value = map.get(key);
		if (value == null) {
			return new ArrayList<>();
		}
		final ArrayList<String> pluginNameList = new ArrayList<>();
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
	private List<String> authors = null;

	/**
	 * A map from strings to commands.
	 * 
	 * @return The command map.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private Map<String, Map<String, Object>> commands = null;
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
	private String description = null;

	private Map<?, ?> lazyPermissions = null;

	/**
	 * Plugins to load before this plugin.
	 * 
	 * @return The list of plugins to load first before this one.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private List<String> loadBefore = new ArrayList<>();

	/**
	 * The fully qualified name of the class that extends {@link Plugin} for
	 * this plugin. The format should follow the
	 * {@link ClassLoader#loadClass(String)} syntax.
	 * 
	 * @return The absolute path to the main plugin class.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private String main = null;

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
	 * The default permission value for this plugin.
	 * 
	 * @return The default permission value.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private DefaultPermissionValue permissionDefault =
		DefaultPermissionValue.FALSE;
	private List<Permission> permissions = null;

	/**
	 * The prefix to use in logging. This will appear before logs sent by this
	 * plugin.
	 * 
	 * @return The logging prefix.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private String prefix = null;

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
	 * Returns a plugin description loaded by the given inputstream.
	 *
	 * @param stream the steam to load info from
	 * @throws InvalidDescriptionException if the description is not valid
	 */
	public PluginInfo(final InputStream stream)
		throws InvalidDescriptionException {
		// TODO finish javadoc
		// TODO provide examples
		// TODO list yaml tags
		if (stream == null) {
			log.fine("Attempting to get plugin info from a null stream");
		}

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
	 * Returns the full name of the plugin. This is a string that describes the
	 * plugin, such as "Graphics" or "AI", with version info appended.
	 *
	 * @return the full name
	 */
	public String getFullName() {
		return this.name + "-" + this.version;
	}

	/**
	 * Returns a list of permissions for this plugin
	 *
	 * @return this plugins permissions
	 */
	public List<Permission> getPermissions() {
		if (this.permissions == null) {
			if (this.lazyPermissions == null) {
				this.permissions = new ArrayList<>();
			}
			else {
				this.permissions =
					Permission.loadPermissions(this.lazyPermissions,
						"Permission node '%s' in plugin description file for "
							+ this.getFullName() + " is invalid",
						this.permissionDefault.value());

				this.lazyPermissions = null;
			}
		}
		return this.permissions;
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
			this.main = map.get("main").toString();
		}
		catch (NullPointerException ex) {
			throw new InvalidDescriptionException("main class is not defined",
				ex);
		}
		catch (ClassCastException ex) {
			throw new InvalidDescriptionException("main is of the wrong type",
				ex);
		}
		if (map.get("commands") != null) {
			HashMap<String, Map<String, Object>> commandsMap = new HashMap<>();
			try {
				for (Map.Entry<?, ?> command : ((Map<?, ?>) map.get("commands"))
					.entrySet()) {
					HashMap<String, Object> commandMap = new HashMap<>();
					if (command.getValue() != null) {
						for (Map.Entry<?, ?> commandEntry : ((Map<?, ?>) command
							.getValue()).entrySet()) {
							if (commandEntry.getValue() instanceof Iterable) {
								HashSet<Object> commandSubList =
									new HashSet<>();
								for (Object commandSubListItem : (Iterable<?>) commandEntry
									.getValue()) {
									if (commandSubListItem != null) {
										commandSubList.add(commandSubListItem);
									}
								}
								commandMap.put(commandEntry.getKey().toString(),
									commandSubList);

							}
							else if (commandEntry.getValue() != null) {
								commandMap.put(commandEntry.getKey().toString(),
									commandEntry.getValue());
							}
						}
					}
					commandsMap.put(command.getKey().toString(), commandMap);
				}
			}
			catch (ClassCastException ex) {
				throw new InvalidDescriptionException(
					"commands are of the wrong type", ex);
			}
			this.commands = commandsMap;
		}
		this.dependencies = PluginInfo.makePluginNameList(map, "depend");
		this.softDependencies =
			PluginInfo.makePluginNameList(map, "soft-depend");
		this.loadBefore = PluginInfo.makePluginNameList(map, "load-before");
		if (map.get("description") != null) {
			this.description = map.get("description").toString();
		}
		if (map.get("authors") != null) {
			ArrayList<String> authorsList = new ArrayList<>();
			try {
				for (Object o : (Iterable<?>) map.get("authors")) {
					authorsList.add(o.toString());
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
			this.authors = authorsList;
		}
		else {
			this.authors = new ArrayList<>();
		}
		if (map.get("default-permission") != null) {
			try {
				this.permissionDefault = DefaultPermissionValue
					.getByName(map.get("default-permission").toString());
			}
			catch (ClassCastException ex) {
				throw new InvalidDescriptionException(
					"default-permission is of the wrong type", ex);
			}
			catch (IllegalArgumentException ex) {
				throw new InvalidDescriptionException(
					"default-permission is not a valid choice", ex);
			}
		}
		try {
			this.lazyPermissions = (Map<?, ?>) map.get("permissions");
			this.permissions = Permission.loadPermissions(this.lazyPermissions,
				"Permission node '%s' in plugin description file for "
					+ this.getFullName() + " is invalid",
				this.permissionDefault.value());
		}
		catch (ClassCastException ex) {
			throw new InvalidDescriptionException(
				"permissions are of the wrong type", ex);
		}
		if (map.get("prefix") != null) {
			this.prefix = map.get("prefix").toString();
		}
	}

}
