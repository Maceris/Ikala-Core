package com.ikalagaming.plugins;

import com.github.zafarkhaja.semver.Version;
import lombok.Getter;
import lombok.NonNull;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contains data about a particular plugin, loaded from the plugin.yml file in its jar. The tags
 * that are required or possible are listed on the wiki.
 *
 * @author Ches Burks
 */
@Getter
public class PluginInfo {

    /** The regular expression describing what a valid plugin name looks like. */
    private static final String NAME_REGEX = "^[a-zA-Z0-9_-]+$";

    /**
     * Cast to a map, throw a custom exception if it is not.
     *
     * @param object The object to cast to a map.
     * @return The resulting map object.
     * @throws InvalidDescriptionException If the object is not a map.
     */
    private static Map<?, ?> asMap(Object object) throws InvalidDescriptionException {
        if (object instanceof Map<?, ?> map) {
            return map;
        }
        throw new InvalidDescriptionException(object + " is not properly structured.");
    }

    /**
     * Pull a list of strings from the plugin info.
     *
     * @param map The map loaded from the plugin info yaml file.
     * @param key The entry in the configuration we are interested in.
     * @return The contents of that entry, as an unmodifiable list. Empty if it is not present.
     * @throws InvalidDescriptionException If the entry is not a list.
     */
    private static List<String> makeList(final Map<?, ?> map, final String key)
            throws InvalidDescriptionException {
        final Object value = map.get(key);
        if (value == null) {
            return List.of();
        }
        if (!(value instanceof List<?> list)) {
            throw new InvalidDescriptionException(key + " is of the wrong type");
        }
        List<String> result = new ArrayList<>();
        for (Object entry : list) {
            if (entry == null) {
                throw new InvalidDescriptionException("invalid " + key + " format");
            }
            result.add(entry.toString());
        }
        return List.copyOf(result);
    }

    /**
     * Pull a list of plugin names from the plugin info.
     *
     * @param map The map loaded from the plugin info yaml file.
     * @param key The entry in the configuration we are interested in.
     * @return The contents of that entry, as an unmodifiable list. Empty if it is not present.
     * @throws InvalidDescriptionException If the entry is not a list, or has invalid names.
     */
    private static List<String> makePluginNameList(final Map<?, ?> map, final String key)
            throws InvalidDescriptionException {
        List<String> names = PluginInfo.makeList(map, key);
        for (String name : names) {
            if (!name.matches(PluginInfo.NAME_REGEX)) {
                throw new InvalidDescriptionException(
                        "Dependency '" + name + "' contains invalid characters.");
            }
        }
        return names;
    }

    /**
     * Get a required value from the plugin info.
     *
     * @param map The map loaded from the plugin info yaml file.
     * @param key The entry in the configuration we are interested in.
     * @param description What the value is, for error messages.
     * @return The value, as a string.
     * @throws InvalidDescriptionException If the value is missing, or not a single value.
     */
    private static String required(final Map<?, ?> map, final String key, String description)
            throws InvalidDescriptionException {
        return switch (map.get(key)) {
            case null -> throw new InvalidDescriptionException(description + " is not defined");
            case Map<?, ?> ignored ->
                    throw new InvalidDescriptionException(description + " is of the wrong type");
            case List<?> ignored ->
                    throw new InvalidDescriptionException(description + " is of the wrong type");
            case Object value -> value.toString();
        };
    }

    /**
     * The list of authors for the plugin. This is used to give credit to developers.
     *
     * @return The list of plugin authors.
     */
    @SuppressWarnings("javadoc")
    private final List<String> authors;

    /**
     * Returns a list of plugins this plugin requires in order to run. Use the value of {@link
     * #getName()} for the target plugin to specify it in the dependencies. If any plugin in this
     * list is not found, this plugin will fail to load at startup.
     *
     * @return The list of plugin dependencies.
     */
    @SuppressWarnings("javadoc")
    private final List<String> dependencies;

    /**
     * This is a short human-friendly description of what the plugin does. It may be multiple lines.
     *
     * @return The brief description of this plugin.
     */
    @SuppressWarnings("javadoc")
    private final String description;

    /**
     * The fully qualified name of the class that extends {@link Plugin} for this plugin. The format
     * should follow the {@link ClassLoader#loadClass(String)} syntax.
     *
     * @return The absolute path to the main plugin class.
     */
    @SuppressWarnings("javadoc")
    private final String mainClass;

    /**
     * The name of the plugin. Names are unique for each plugin. The name can contain the following
     * characters:
     *
     * <ul>
     *   <li>a-z
     *   <li>A-Z
     *   <li>0-9
     *   <li>hyphen
     *   <li>underscore
     * </ul>
     *
     * @return The name of the plugin.
     */
    @SuppressWarnings("javadoc")
    private final String name;

    /**
     * Returns a list of dependencies that are desired but not needed to run
     *
     * @return Soft dependencies for this plugin.
     */
    @SuppressWarnings("javadoc")
    private final List<String> softDependencies;

    /**
     * The version of the plugin, as a semantic version string like {@code 1.2.3} or {@code
     * 1.0.0-beta.1+build.5}, including any pre-release and build information.
     *
     * @return The version of the plugin.
     */
    @SuppressWarnings("javadoc")
    private final String version;

    /**
     * Returns a plugin description loaded by the given InputStream, from a Yaml file. The tags that
     * are required or possible are listed on the wiki.
     *
     * @param stream the steam to load info from
     * @throws InvalidDescriptionException if the description is not valid
     */
    public PluginInfo(@NonNull final InputStream stream) throws InvalidDescriptionException {
        Map<?, ?> map = PluginInfo.asMap(new Yaml().load(stream));

        name = PluginInfo.required(map, "name", "name");
        if (!name.matches(PluginInfo.NAME_REGEX)) {
            throw new InvalidDescriptionException(
                    "name '" + name + "' contains invalid characters.");
        }

        if (!(map.get("version") instanceof String versionString)) {
            throw new InvalidDescriptionException(
                    map.get("version") == null
                            ? "version is not defined"
                            : "version is of wrong type, it may need quotes");
        }
        if (!Version.isValid(versionString)) {
            throw new InvalidDescriptionException("version is in an invalid format");
        }
        // Normalized, so equivalent versions are written the same way
        version = Version.parse(versionString).toString();

        mainClass = PluginInfo.required(map, "main-class", "main class");
        dependencies = PluginInfo.makePluginNameList(map, "dependencies");
        softDependencies = PluginInfo.makePluginNameList(map, "soft-dependencies");
        authors = PluginInfo.makeList(map, "authors");
        description = map.get("description") == null ? "" : map.get("description").toString();
    }

    /**
     * Returns the full name of the plugin. This is a string that describes the plugin, such as
     * "Graphics" or "AI", with version info appended.
     *
     * @return the full name
     */
    public String getFullName() {
        return name + "-" + version;
    }
}
