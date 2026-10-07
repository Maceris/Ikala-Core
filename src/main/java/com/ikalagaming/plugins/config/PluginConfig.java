package com.ikalagaming.plugins.config;

import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Plugin configuration. This class is a wrapper around a nested map structure that allows us to
 * more easily interface with it and convert keys to appropriate types.
 *
 * <p>Keys are accessed by name, and nested keys are accessed by the full path to the key with dot
 * separators. For example, {@code "simple-key"} and {@code "more.complex.nested-key"} are valid
 * keys.
 *
 * <p>The typed getters convert between compatible types, so for example {@link #getLong(String)}
 * works for a number that YAML loaded as an integer, and {@link #getString(String)} works for a
 * number. If a value is missing, null, or can't be converted, the default is returned, and a
 * warning is logged if it was the wrong type. Lists returned by the getters are copies, so use
 * {@link #set(String, Object)} to make changes.
 *
 * <p>This is thread safe, and every method sees a consistent view of the configuration. To make
 * sure nothing can change it without going through these methods, sections and lists returned by
 * {@link #get(String)} and {@link #getOrDefault(String, Object)} are read-only copies, and sections
 * and lists passed to {@link #set(String, Object)} are copied. Other values should be simple YAML
 * values like strings, numbers, and booleans.
 *
 * @author Ches Burks
 */
@Slf4j
public class PluginConfig {

    /** The regular expression used to split paths. */
    private static final String PATH_SEPARATOR = "\\.";

    /** Returned by {@link #find(String)} when there is no value at a path. */
    private static final Object MISSING = new Object();

    /**
     * Convert a value to a boolean.
     *
     * @param value The value to convert.
     * @return The boolean, or null if it isn't one.
     */
    private static Boolean asBoolean(Object value) {
        return value instanceof Boolean bool ? bool : null;
    }

    /**
     * Convert a value to a byte.
     *
     * @param value The value to convert.
     * @return The byte, or null if it isn't a whole number in range.
     */
    private static Byte asByte(Object value) {
        Long number = asLong(value);
        if (number == null || number < Byte.MIN_VALUE || number > Byte.MAX_VALUE) {
            return null;
        }
        return number.byteValue();
    }

    /**
     * Convert a value to a character.
     *
     * @param value The value to convert.
     * @return The character, or null if it isn't a character or a string of length 1.
     */
    private static Character asCharacter(Object value) {
        if (value instanceof Character character) {
            return character;
        }
        if (value instanceof String string && string.length() == 1) {
            return string.charAt(0);
        }
        return null;
    }

    /**
     * Convert a value to a double.
     *
     * @param value The value to convert.
     * @return The double, or null if it isn't a number.
     */
    private static Double asDouble(Object value) {
        return value instanceof Number number ? number.doubleValue() : null;
    }

    /**
     * Convert a value to an integer.
     *
     * @param value The value to convert.
     * @return The integer, or null if it isn't a whole number in range.
     */
    private static Integer asInt(Object value) {
        Long number = asLong(value);
        if (number == null || number < Integer.MIN_VALUE || number > Integer.MAX_VALUE) {
            return null;
        }
        return number.intValue();
    }

    /**
     * Convert a value to a long.
     *
     * @param value The value to convert.
     * @return The long, or null if it isn't a whole number in range.
     */
    private static Long asLong(Object value) {
        return switch (value) {
            case Long number -> number;
            case Integer number -> number.longValue();
            case Short number -> number.longValue();
            case Byte number -> number.longValue();
            case BigInteger number when number.bitLength() < Long.SIZE -> number.longValue();
            default -> null;
        };
    }

    /**
     * Convert a value to a short.
     *
     * @param value The value to convert.
     * @return The short, or null if it isn't a whole number in range.
     */
    private static Short asShort(Object value) {
        Long number = asLong(value);
        if (number == null || number < Short.MIN_VALUE || number > Short.MAX_VALUE) {
            return null;
        }
        return number.shortValue();
    }

    /**
     * Convert a value to a string. Single values like numbers are converted to strings, but lists
     * and sections are not.
     *
     * @param value The value to convert.
     * @return The string, or null if it isn't a single value.
     */
    private static String asString(Object value) {
        return switch (value) {
            case String string -> string;
            case Number number -> number.toString();
            case Boolean bool -> bool.toString();
            case Character character -> character.toString();
            default -> null;
        };
    }

    /**
     * Copy sections and lists, so that the copy can be handed out without letting anyone change the
     * configuration through it.
     *
     * @param value The value to copy.
     * @return A read-only copy of sections and lists, or the value itself for anything else.
     */
    private static Object copyForReading(Object value) {
        return switch (value) {
            case null -> null;
            case Map<?, ?> map -> {
                Map<Object, Object> copy = new LinkedHashMap<>();
                map.forEach((key, child) -> copy.put(key, PluginConfig.copyForReading(child)));
                yield Collections.unmodifiableMap(copy);
            }
            case List<?> list -> list.stream().map(PluginConfig::copyForReading).toList();
            default -> value;
        };
    }

    /**
     * Copy sections and lists, so that the configuration has its own modifiable copy that nobody
     * else can change.
     *
     * @param value The value to copy.
     * @return A modifiable copy of sections and lists, or the value itself for anything else.
     */
    private static Object copyForStoring(Object value) {
        return switch (value) {
            case null -> null;
            case Map<?, ?> map -> {
                Map<Object, Object> copy = new LinkedHashMap<>();
                map.forEach((key, child) -> copy.put(key, PluginConfig.copyForStoring(child)));
                yield copy;
            }
            case List<?> list -> {
                List<Object> copy = new ArrayList<>(list.size());
                list.forEach(child -> copy.add(PluginConfig.copyForStoring(child)));
                yield copy;
            }
            default -> value;
        };
    }

    /** The actual contents of the configuration, a nested map structure. */
    private final Map<String, Object> contents;

    /** Held while saving this configuration to disk, so only one save happens at a time. */
    private final Object saveLock = new Object();

    /**
     * Create a configuration. It takes ownership of the contents, so nothing else should keep using
     * that map.
     *
     * @param contents The contents of the configuration, a nested map structure.
     */
    PluginConfig(@NonNull Map<String, Object> contents) {
        this.contents = contents;
    }

    /**
     * Find the value at a path.
     *
     * @param path The path to the key.
     * @return The value, which may be null, or {@link #MISSING} if there is no value there.
     */
    private Object find(String path) {
        String[] parts = path.split(PluginConfig.PATH_SEPARATOR);
        Object current = contents;
        for (int i = 0; i < parts.length; ++i) {
            if (!(current instanceof Map<?, ?> map)) {
                warnNotASection(parts, i, path);
                return MISSING;
            }
            if (!map.containsKey(parts[i])) {
                return MISSING;
            }
            current = map.get(parts[i]);
        }
        return current;
    }

    /**
     * Access a generic type from the config. If the key cannot be found, null is returned. The
     * value is not converted, so if it is not the type you expect, a {@link ClassCastException}
     * will be thrown where you use it. Sections and lists are returned as read-only copies.
     *
     * @param <T> The resulting type.
     * @param key The path to the key.
     * @return The key, or null if the key is missing.
     */
    @Synchronized
    public <T> T get(@NonNull String key) {
        return this.getOrDefault(key, null);
    }

    /**
     * Access a boolean from the config. If the key cannot be found, false is returned.
     *
     * @param key The path to the key.
     * @return The key, or false if the key is missing.
     */
    @Synchronized
    public boolean getBoolean(@NonNull String key) {
        return this.getConverted(key, Boolean.FALSE, PluginConfig::asBoolean);
    }

    /**
     * Access a list of booleans from the config. If the key cannot be found, an empty list is
     * returned.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    @Synchronized
    public List<Boolean> getBooleanList(@NonNull String key) {
        return this.getConvertedList(key, PluginConfig::asBoolean);
    }

    /**
     * Access a list of bytes from the config. If the key cannot be found, an empty list is
     * returned.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    @Synchronized
    public List<Byte> getByteList(@NonNull String key) {
        return this.getConvertedList(key, PluginConfig::asByte);
    }

    /**
     * Access a list of characters from the config. If the key cannot be found, an empty list is
     * returned.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    @Synchronized
    public List<Character> getCharacterList(@NonNull String key) {
        return this.getConvertedList(key, PluginConfig::asCharacter);
    }

    /**
     * Get a value, converted to the expected type.
     *
     * @param <T> The expected type.
     * @param key The path to the key.
     * @param defaultValue The value to use if it is missing, null, or can't be converted.
     * @param converter Converts the value, returning null if it can't.
     * @return The converted value, or the default.
     */
    private <T> T getConverted(String key, T defaultValue, Function<Object, T> converter) {
        Object value = this.find(key);
        if (value == MISSING || value == null) {
            return defaultValue;
        }
        T result = converter.apply(value);
        if (result == null) {
            this.warnWrongType(key, value);
            return defaultValue;
        }
        return result;
    }

    /**
     * Get a list of values, each converted to the expected type. Elements that can't be converted
     * are left out.
     *
     * @param <T> The expected type.
     * @param key The path to the key.
     * @param converter Converts each value, returning null if it can't.
     * @return The converted values, or an empty list if it is missing or not a list.
     */
    private <T> List<T> getConvertedList(String key, Function<Object, T> converter) {
        Object value = this.find(key);
        if (value == MISSING || value == null) {
            return List.of();
        }
        if (!(value instanceof List<?> list)) {
            this.warnWrongType(key, value);
            return List.of();
        }
        List<T> result = new ArrayList<>();
        for (Object element : list) {
            T converted = element == null ? null : converter.apply(element);
            if (converted == null) {
                this.warnWrongType(key, element);
                continue;
            }
            result.add(converted);
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Access a double from the config. If the key cannot be found, 0 is returned.
     *
     * @param key The path to the key.
     * @return The key, or 0 if the key is missing.
     */
    @Synchronized
    public double getDouble(@NonNull String key) {
        return this.getConverted(key, 0d, PluginConfig::asDouble);
    }

    /**
     * Access a list of doubles from the config. If the key cannot be found, an empty list is
     * returned.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    @Synchronized
    public List<Double> getDoubleList(@NonNull String key) {
        return this.getConvertedList(key, PluginConfig::asDouble);
    }

    /**
     * Access an integer from the config. If the key cannot be found, 0 is returned.
     *
     * @param key The path to the key.
     * @return The key, or 0 if the key is missing.
     */
    @Synchronized
    public int getInt(@NonNull String key) {
        return this.getConverted(key, 0, PluginConfig::asInt);
    }

    /**
     * Access a list of integers from the config. If the key cannot be found, an empty list is
     * returned.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    @Synchronized
    public List<Integer> getIntList(@NonNull String key) {
        return this.getConvertedList(key, PluginConfig::asInt);
    }

    /**
     * Access a generic list from the config. If the key cannot be found, an empty list is returned.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    @Synchronized
    public List<?> getList(@NonNull String key) {
        return this.getConvertedList(key, Function.identity());
    }

    /**
     * Access a long from the config. If the key cannot be found, 0 is returned.
     *
     * @param key The path to the key.
     * @return The key, or 0 if the key is missing.
     */
    @Synchronized
    public long getLong(@NonNull String key) {
        return this.getConverted(key, 0L, PluginConfig::asLong);
    }

    /**
     * Access a list of longs from the config. If the key cannot be found, an empty list is
     * returned.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    @Synchronized
    public List<Long> getLongList(@NonNull String key) {
        return this.getConvertedList(key, PluginConfig::asLong);
    }

    /**
     * Access a generic type from the config. If the key cannot be found or is null, the default
     * value is returned. The value is not converted, so if it is not the type you expect, a {@link
     * ClassCastException} will be thrown where you use it.
     *
     * @param <T> The resulting type.
     * @param key The path to the key.
     * @param defaultValue The value to return if the key is not found. May be null.
     * @return The key, or the default value if the key is missing.
     */
    @SuppressWarnings("unchecked")
    @Synchronized
    public <T> T getOrDefault(@NonNull String key, T defaultValue) {
        Object value = this.find(key);
        if (value == MISSING || value == null) {
            return defaultValue;
        }
        return (T) PluginConfig.copyForReading(value);
    }

    /**
     * Access a list of shorts from the config. If the key cannot be found, an empty list is
     * returned.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    @Synchronized
    public List<Short> getShortList(@NonNull String key) {
        return this.getConvertedList(key, PluginConfig::asShort);
    }

    /**
     * Access a string from the config. If the key cannot be found, an empty string is returned.
     * Numbers and booleans are converted to strings.
     *
     * @param key The path to the key.
     * @return The key, or an empty string if the key is missing.
     */
    @Synchronized
    public String getString(@NonNull String key) {
        return this.getConverted(key, "", PluginConfig::asString);
    }

    /**
     * Access a list of strings from the config. If the key cannot be found, an empty list is
     * returned. Numbers and booleans in the list are converted to strings.
     *
     * @param key The path to the key.
     * @return The key, or an empty list if the key is missing.
     */
    @Synchronized
    public List<String> getStringList(@NonNull String key) {
        return this.getConvertedList(key, PluginConfig::asString);
    }

    /**
     * Checks if the given key is a boolean.
     *
     * @param key The path to the key.
     * @return True if the given key is a boolean, false if it is not a boolean or does not exist.
     */
    @Synchronized
    public boolean isBoolean(@NonNull String key) {
        return this.isConvertible(key, PluginConfig::asBoolean);
    }

    /**
     * Whether there is a value at the given key that can be converted.
     *
     * @param key The path to the key.
     * @param converter Converts the value, returning null if it can't.
     * @return True if the value exists and can be converted.
     */
    private boolean isConvertible(String key, Function<Object, ?> converter) {
        Object value = this.find(key);
        return value != MISSING && value != null && converter.apply(value) != null;
    }

    /**
     * Checks if the given key is a number, which can be read as a double.
     *
     * @param key The path to the key.
     * @return True if the given key is a number, false if it is not a number or does not exist.
     */
    @Synchronized
    public boolean isDouble(@NonNull String key) {
        return this.isConvertible(key, PluginConfig::asDouble);
    }

    /**
     * Checks if the given key is a whole number that fits in an integer.
     *
     * @param key The path to the key.
     * @return True if the given key is an integer, false if it is not an integer or does not exist.
     */
    @Synchronized
    public boolean isInt(@NonNull String key) {
        return this.isConvertible(key, PluginConfig::asInt);
    }

    /**
     * Checks if the given key is a list.
     *
     * @param key The path to the key.
     * @return True if the given key is a list, false if it is not a list or does not exist.
     */
    @Synchronized
    public boolean isList(@NonNull String key) {
        return this.find(key) instanceof List;
    }

    /**
     * Checks if the given key is a whole number that fits in a long.
     *
     * @param key The path to the key.
     * @return True if the given key is a long, false if it is not a long or does not exist.
     */
    @Synchronized
    public boolean isLong(@NonNull String key) {
        return this.isConvertible(key, PluginConfig::asLong);
    }

    /**
     * Checks if the config has a value set at the given path, even if that value is null.
     *
     * @param path The path to the key.
     * @return True if that entry exists, false if it does not.
     */
    @Synchronized
    public boolean isPresent(@NonNull String path) {
        return this.find(path) != MISSING;
    }

    /**
     * Checks if the given key is a string. Numbers are not considered strings here, even though
     * {@link #getString(String)} will convert them.
     *
     * @param key The path to the key.
     * @return True if the given key is a string, false if it is not a string or does not exist.
     */
    @Synchronized
    public boolean isString(@NonNull String key) {
        return this.find(key) instanceof String;
    }

    /**
     * Sets the value in the config to the specified object.
     *
     * <p>If any sections in the path do not exist, they will be created. So if we set {@code
     * "more.complex.nested-key"} on an empty config, {@code "more"}, {@code "more.complex"}, and
     * {@code "more.complex.nested-key"} will all be created. If part of the path holds a value
     * instead of a section, it is replaced with a section.
     *
     * @param path The path to the key.
     * @param value The value to store in the specified location.
     */
    @SuppressWarnings("unchecked")
    @Synchronized
    public void set(@NonNull String path, Object value) {
        String[] parts = path.split(PluginConfig.PATH_SEPARATOR);
        Map<String, Object> currentMap = contents;
        for (int i = 0; i < parts.length - 1; ++i) {
            Object next = currentMap.get(parts[i]);
            if (!(next instanceof Map)) {
                if (next != null) {
                    log.warn(
                            SafeResourceLoader.getString(
                                    "CONFIG_REPLACED_VALUE", ConfigManager.messages()),
                            String.join(".", List.of(parts).subList(0, i + 1)),
                            path);
                }
                next = new HashMap<String, Object>();
                currentMap.put(parts[i], next);
            }
            currentMap = (Map<String, Object>) next;
        }
        currentMap.put(parts[parts.length - 1], PluginConfig.copyForStoring(value));
    }

    /**
     * A copy of the whole configuration, for saving it while other threads keep using it.
     *
     * @return A copy of the contents of the configuration.
     */
    @Synchronized
    @SuppressWarnings("unchecked")
    Map<String, Object> snapshot() {
        return (Map<String, Object>) PluginConfig.copyForStoring(contents);
    }

    /**
     * The lock held while saving this configuration to disk.
     *
     * @return The lock for saving.
     */
    Object getSaveLock() {
        return saveLock;
    }

    /**
     * Log that part of a path holds a value instead of a section, so the rest of the path can't
     * exist.
     *
     * @param parts The parts of the path.
     * @param index The index of the first part that can't be looked up.
     * @param path The full path.
     */
    private void warnNotASection(String[] parts, int index, String path) {
        log.warn(
                SafeResourceLoader.getString("CONFIG_INVALID_KEY", ConfigManager.messages()),
                String.join(".", List.of(parts).subList(0, index)),
                path);
    }

    /**
     * Log that a value isn't the type that was asked for.
     *
     * @param key The path to the key.
     * @param value The value that has the wrong type.
     */
    private void warnWrongType(String key, Object value) {
        log.warn(
                SafeResourceLoader.getString("CONFIG_INVALID_TYPE", ConfigManager.messages()),
                key,
                value,
                value.getClass().getSimpleName());
    }
}
