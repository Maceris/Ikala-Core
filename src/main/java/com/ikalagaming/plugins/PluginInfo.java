package com.ikalagaming.plugins;

import com.github.zafarkhaja.semver.ParseException;
import com.github.zafarkhaja.semver.Version;
import lombok.Getter;
import lombok.NonNull;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Contains data about a particular plugin. This info can be loaded from code or from a file.
 *
 * @author Ches Burks
 */
public class PluginInfo {

    /** The regular expression describing what a valid plugin name looks like. */
    private static final String NAME_REGEX = "^[a-zA-Z0-9_-]+$";

    /**
     * Pull a list of dependencies from the plugin info and return it.
     *
     * @param map The map loaded from the plugin info yaml file.
     * @param key The entry in the configuration we are interested in.
     * @return The contents of that entry, as a list.
     * @throws InvalidDescriptionException If the entry is not present, not a list, or has invalid
     *     dependency names.
     */
    private static List<String> makePluginNameList(final Map<?, ?> map, final String key)
            throws InvalidDescriptionException {
        final Object value = map.get(key);
        final ArrayList<String> pluginNameList = new ArrayList<>();
        if (value == null) {
            return pluginNameList;
        }

        try {
            for (final Object entry : (Iterable<?>) value) {
                String dependency = entry.toString();
                if (!dependency.matches(PluginInfo.NAME_REGEX)) {
                    throw new InvalidDescriptionException(
                            "Dependency '" + dependency + "' contains invalid characters.");
                }

                pluginNameList.add(dependency);
            }
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException(key + " is of the wrong type", ex);
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException("invalid " + key + " format", ex);
        }
        return pluginNameList;
    }

    /**
     * The list of authors for the plugin. This is used to give credit to developers.
     *
     * @return The list of plugin authors.
     */
    @SuppressWarnings("javadoc")
    @Getter
    private List<String> authors = new ArrayList<>();

    /**
     * Returns a list of plugins this plugin requires in order to run. Use the value of {@link
     * #getName()} for the target plugin to specify it in the dependencies. If any plugin in this
     * list is not found, this plugin will fail to load at startup.
     *
     * @return The list of plugin dependencies.
     */
    @SuppressWarnings("javadoc")
    @Getter
    private List<String> dependencies = new ArrayList<>();

    /**
     * This is a short human-friendly description of what the plugin does. It may be multiple lines.
     *
     * @return The brief description of this plugin.
     */
    @SuppressWarnings("javadoc")
    @Getter
    private String description = "";

    /**
     * The fully qualified name of the class that extends {@link Plugin} for this plugin. The format
     * should follow the {@link ClassLoader#loadClass(String)} syntax.
     *
     * @return The absolute path to the main plugin class.
     */
    @SuppressWarnings("javadoc")
    @Getter
    private String mainClass = null;

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

    private Version version = Version.parse("0.0.0");

    /**
     * Returns a plugin description loaded by the given InputStream, from a Yaml file. The tags that
     * are required or possible are listed on the wiki.
     *
     * @param stream the steam to load info from
     * @throws InvalidDescriptionException if the description is not valid
     */
    public PluginInfo(@NonNull final InputStream stream) throws InvalidDescriptionException {

        Yaml yaml = new Yaml();
        loadMap(asMap(yaml.load(stream)));
    }

    /**
     * Cast to a map, throw a custom exception if it is not.
     *
     * @param object The object to cast to a map.
     * @return The resulting map object.
     * @throws InvalidDescriptionException If the object is not a map.
     */
    private Map<?, ?> asMap(Object object) throws InvalidDescriptionException {
        if (object instanceof Map) {
            return (Map<?, ?>) object;
        }
        throw new InvalidDescriptionException(object + " is not properly structured.");
    }

    /**
     * Extract the required fields as part of {@link #loadMap(Map)}.
     *
     * @param map The map we are loading from.
     * @throws InvalidDescriptionException If the description is invalid.
     */
    private void extractRequiredFields(Map<?, ?> map) throws InvalidDescriptionException {
        try {
            name = map.get("name").toString();
            if (!name.matches(PluginInfo.NAME_REGEX)) {
                throw new InvalidDescriptionException(
                        "name '" + name + "' contains invalid characters.");
            }
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException("name is not defined", ex);
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException("name is of wrong type", ex);
        }

        try {
            version = Version.parse((String) map.get("version"));
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException("version is not defined", ex);
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException("version is of wrong type", ex);
        } catch (IllegalArgumentException ex) {
            throw new InvalidDescriptionException("version is not there", ex);
        } catch (ParseException ex) {
            throw new InvalidDescriptionException("version is in an invalid format", ex);
        }

        try {
            mainClass = map.get("main-class").toString();
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException("main class is not defined", ex);
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException("main is of the wrong type", ex);
        }
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

    /**
     * The version of the plugin. This value is a string that follows the
     * MajorVersion.MinorVersion.PatchVersion format. It should be increased when new features are
     * added or bugs are fixed.
     *
     * @return the version of the plugin
     */
    public String getVersion() {
        return String.format(
                Locale.ROOT,
                "%d.%d.%d",
                version.majorVersion(),
                version.minorVersion(),
                version.patchVersion());
    }

    /**
     * Load fields from a map, and throw an exception if the contents of the map are not valid.
     *
     * @param map The map generated by loading the plugin yaml file.
     * @throws InvalidDescriptionException If the plugin info is not valid.
     */
    private void loadMap(Map<?, ?> map) throws InvalidDescriptionException {
        extractRequiredFields(map);

        dependencies = PluginInfo.makePluginNameList(map, "dependencies");
        softDependencies = PluginInfo.makePluginNameList(map, "soft-dependencies");

        if (map.get("description") != null) {
            description = map.get("description").toString();
        }

        if (map.get("authors") != null) {
            try {
                for (Object o : (Iterable<?>) map.get("authors")) {
                    authors.add(o.toString());
                }
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException("authors are of the wrong type", ex);
            } catch (NullPointerException ex) {
                throw new InvalidDescriptionException("authors are not defined properly", ex);
            }
        }
    }
}
