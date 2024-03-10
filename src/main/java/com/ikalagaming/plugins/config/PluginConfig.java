package com.ikalagaming.plugins.config;

import com.ikalagaming.plugins.PluginManager;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Plugin configuration. This class is a wrapper around a nested map structure that allows us to
 * more easily interface with it and convert keys to appropriate types.
 *
 * @author Ches Burks
 */
@Slf4j
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PluginConfig {

    /** The regular expression used to split paths. */
    private static final String PATH_SEPARATOR = "\\.";

    /**
     * The actual contents of the configuration, a nested map structure.
     *
     * @return The current contents of the config.
     */
    @Getter(value = AccessLevel.PACKAGE)
    private final Map<String, Object> contents;

    /**
     * Handle the casting to an appropriate type, logging a warning if it fails.
     *
     * @param <T> The type to cast to, assumed from context.
     * @param object The object to cast.
     * @return The object, cast to the appropriate type, or null if there was an exception.
     */
    @SuppressWarnings("unchecked")
    private <T> T cast(@NonNull Object object) {
        try {
            return (T) object;
        } catch (ClassCastException e) {
            log.warn(
                    SafeResourceLoader.getString(
                            "CONFIG_INVALID_TYPE", PluginManager.getInstance().getResourceBundle()),
                    e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Access a generic type from the config. If the key cannot be found, or the key is not of the
     * generic type, null is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param <T> The resulting type.
     * @param key The path to the key.
     * @return The key, or null if the key is missing.
     */
    public <T> T get(@NonNull String key) {
        return this.navigateKeys(key, null);
    }

    /**
     * Access a boolean from the config. If the key cannot be found, false is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or false if the key is missing.
     */
    public boolean getBoolean(@NonNull String key) {
        return this.navigateKeys(key, Boolean.FALSE);
    }

    /**
     * Access a list of booleans from the config. If the key cannot be found, an empty unmodifiable
     * list is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    public List<Boolean> getBooleanList(@NonNull String key) {
        return this.navigateKeys(key, List.of());
    }

    /**
     * Access a list of bytes from the config. If the key cannot be found, an empty unmodifiable
     * list is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    public List<Byte> getByteList(@NonNull String key) {
        return this.navigateKeys(key, List.of());
    }

    /**
     * Access a list of characters from the config. If the key cannot be found, an empty
     * unmodifiable list is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    public List<Character> getCharacterList(@NonNull String key) {
        return this.navigateKeys(key, List.of());
    }

    /**
     * Access a double from the config. If the key cannot be found, 0 is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or 0 if the key is missing.
     */
    public double getDouble(@NonNull String key) {
        return this.navigateKeys(key, 0d);
    }

    /**
     * Access a list of doubles from the config. If the key cannot be found, an empty unmodifiable
     * list is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    public List<Double> getDoubleList(@NonNull String key) {
        return this.navigateKeys(key, List.of());
    }

    /**
     * Access an integer from the config. If the key cannot be found, 0 is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or 0 if the key is missing.
     */
    public int getInt(@NonNull String key) {
        return this.navigateKeys(key, 0);
    }

    /**
     * Access a list of integers from the config. If the key cannot be found, an empty unmodifiable
     * list is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    public List<Integer> getIntList(@NonNull String key) {
        return this.navigateKeys(key, List.of());
    }

    /**
     * Access a generic list from the config. If the key cannot be found, an empty unmodifiable list
     * is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    public List<?> getList(@NonNull String key) {
        return this.navigateKeys(key, List.of());
    }

    /**
     * Access a long from the config. If the key cannot be found, 0 is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or 0 if the key is missing.
     */
    public long getLong(@NonNull String key) {
        return this.navigateKeys(key, 0L);
    }

    /**
     * Access a list of longs from the config. If the key cannot be found, an empty unmodifiable
     * list is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    public List<Long> getLongList(@NonNull String key) {
        return this.navigateKeys(key, List.of());
    }

    /**
     * Access a generic type from the config. If the key cannot be found, or the key is not of the
     * generic type, the default value is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param <T> The resulting type.
     * @param key The path to the key.
     * @param defaultValue The value to return if the key is not found. May be null.
     * @return The key, or the default value if the key is missing.
     */
    public <T> T getOrDefault(@NonNull String key, T defaultValue) {
        return this.navigateKeys(key, defaultValue);
    }

    /**
     * Access a list of shorts from the config. If the key cannot be found, an empty unmodifiable
     * list is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    public List<Short> getShortList(@NonNull String key) {
        return this.navigateKeys(key, List.of());
    }

    /**
     * Access a string from the config. If the key cannot be found, an empty string is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or an empty string if the key is missing.
     */
    public String getString(@NonNull String key) {
        return this.navigateKeys(key, "");
    }

    /**
     * Access a list of strings from the config. If the key cannot be found, an empty unmodifiable
     * list is returned.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    public List<String> getStringList(@NonNull String key) {
        return this.navigateKeys(key, List.of());
    }

    /**
     * Checks if the given key is a boolean. If it does not exist, or is not a boolean, this will
     * return false. If it exists and can be assigned to a boolean, it will return true.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return True if the given key is a boolean, false if it is not a boolean or does not exist.
     */
    public boolean isBoolean(@NonNull String key) {
        return isType(key, Boolean.class);
    }

    /**
     * Checks if the given key is a double. If it does not exist, or is not a double, this will
     * return false. If it exists and can be assigned to a double, it will return true.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return True if the given key is a double, false if it is not a double or does not exist.
     */
    public boolean isDouble(@NonNull String key) {
        return isType(key, Double.class);
    }

    /**
     * Checks if the given key is an integer. If it does not exist, or is not an integer, this will
     * return false. If it exists and can be assigned to an integer, it will return true.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return True if the given key is an integer, false if it is not an integer or does not exist.
     */
    public boolean isInt(@NonNull String key) {
        return isType(key, Integer.class);
    }

    /**
     * Checks if the given key is a list. If it does not exist, or is not a list, this will return
     * false. If it exists and can be assigned to a string, it will return true.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return True if the given key is a list, false if it is not a list or does not exist.
     */
    public boolean isList(@NonNull String key) {
        return isType(key, List.class);
    }

    /**
     * Checks if the given key is a long. If it does not exist, or is not a long, this will return
     * false. If it exists and can be assigned to a long, it will return true.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return True if the given key is a long, false if it is not a long or does not exist.
     */
    public boolean isLong(@NonNull String key) {
        return isType(key, Long.class);
    }

    /**
     * Checks if the config has a value set at the given path.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param path The path to the key.
     * @return True if that entry exists, false if it does not.
     */
    public boolean isPresent(@NonNull String path) {
        String[] parts = path.split(PluginConfig.PATH_SEPARATOR);
        Map<String, Object> currentMap = contents;
        for (int i = 0; i < parts.length; ++i) {
            final String currentPart = parts[i];
            if (i == parts.length - 1) {
                return currentMap.containsKey(currentPart);
            }
            if (currentMap.get(currentPart) == null) {
                return false;
            }
            currentMap = this.cast(currentMap.get(currentPart));
        }
        return false;
    }

    /**
     * Checks if the given key is a string. If it does not exist, or is not a string, this will
     * return false. If it exists and can be assigned to a string, it will return true.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param key The path to the key.
     * @return True if the given key is a string, false if it is not a string or does not exist.
     */
    public boolean isString(@NonNull String key) {
        return isType(key, String.class);
    }

    /**
     * Checks if the config has a value set at the given path.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * @param path The path to the key.
     * @param target The class we expect the key to be assignable to.
     * @return True if that entry exists, false if it does not.
     */
    private boolean isType(@NonNull String path, @NonNull Class<?> target) {
        String[] parts = path.split(PluginConfig.PATH_SEPARATOR);
        Map<String, Object> currentMap = contents;
        for (int i = 0; i < parts.length; ++i) {
            final String currentPart = parts[i];
            if (i == parts.length - 1) {
                Object value = currentMap.get(currentPart);
                if (value == null) {
                    // Key does not exist
                    return false;
                }
                return target.isAssignableFrom(value.getClass());
            }
            if (currentMap.get(currentPart) == null) {
                // Path to the key does not exist
                return false;
            }
            currentMap = this.cast(currentMap.get(currentPart));
        }
        // Should not reach here
        return false;
    }

    /**
     * Dig through the config to find the specified value.
     *
     * @param <T> The type to return.
     * @param path The key, as a path like "test", or "test.other.longer-name".
     * @param defaultValue The default value to return if an entry is not found.
     * @return The resulting value, or a default one if it is not found.
     */
    private <T> T navigateKeys(@NonNull String path, T defaultValue) {
        String[] parts = path.split(PluginConfig.PATH_SEPARATOR);
        Map<String, Object> currentMap = contents;
        for (int i = 0; i < parts.length; ++i) {
            final String currentPart = parts[i];
            if (i == parts.length - 1) {
                return this.cast(currentMap.getOrDefault(currentPart, defaultValue));
            }
            if (currentMap.get(currentPart) == null) {
                StringBuilder invalidName = new StringBuilder();
                for (int j = 0; j <= i; ++j) {
                    invalidName.append(parts[j]);
                }
                log.warn(
                        SafeResourceLoader.getString(
                                "CONFIG_INVALID_KEY",
                                PluginManager.getInstance().getResourceBundle()),
                        invalidName.toString());
                return defaultValue;
            }
            currentMap = this.cast(currentMap.get(currentPart));
        }
        return defaultValue;
    }

    /**
     * Sets the value in the config to the specified object.
     *
     * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with
     * dot separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are
     * valid keys.
     *
     * <p>If any keys in the path do not exist, they will be created. So if we set {@code
     * "more.complex.nested-key"} on an empty config, {@code "more"}, {@code "more.complex"}, and
     * {@code "more.complex.nested-key"} will all be created.
     *
     * @param path The path to the key.
     * @param value The value to store in the specified location.
     */
    public void set(@NonNull String path, Object value) {
        String[] parts = path.split(PluginConfig.PATH_SEPARATOR);
        Map<String, Object> currentMap = contents;
        for (int i = 0; i < parts.length; ++i) {
            final String currentPart = parts[i];
            if (i == parts.length - 1) {
                currentMap.put(currentPart, value);
                // Exit the loop/method so we don't overwrite the value
                return;
            }
            if (currentMap.get(currentPart) == null) {
                // Create the intermediate values
                currentMap.put(currentPart, new HashMap<>());
            }
            currentMap = this.cast(currentMap.get(parts[i]));
        }
    }
}
