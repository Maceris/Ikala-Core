package com.ikalagaming.plugins.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Plugin configuration. This class is a wrapper around a nested map structure
 * that allows us to more easily interface with it and convert keys to
 * appropriate types.
 *
 * @author Ches Burks
 *
 */
@Slf4j
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PluginConfig {

	/**
	 * The actual contents of the configuration, a nested map structure.
	 */
	private final Map<String, Object> contents;

	/**
	 * Handle the casting to an appropriate type, logging a warning if it fails.
	 *
	 * @param <T> The type to cast to, assumed from context.
	 * @param object The object to cast.
	 * @return The object, cast to the appropriate type, or null if there was an
	 *         exception.
	 */
	@SuppressWarnings("unchecked")
	private <T> T cast(@NonNull Object object) {
		try {
			return (T) object;
		}
		catch (ClassCastException e) {
			PluginConfig.log.warn("Invalid type {}", e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * Access a generic type from the config. If the key cannot be found, or the
	 * key is not of the generic type, null is returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param <T> The resulting type.
	 * @param key The path to the key.
	 * @return The key, or null if the key is missing.
	 */
	public <T> T get(@NonNull String key) {
		return this.navigateKeys(key, null);
	}

	/**
	 * Access a boolean from the config. If the key cannot be found, false is
	 * returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param key The path to the key.
	 * @return The key, or false if the key is missing.
	 */
	public boolean getBoolean(@NonNull String key) {
		return this.navigateKeys(key, Boolean.FALSE);
	}

	/**
	 * Access a list of booleans from the config. If the key cannot be found, an
	 * empty unmodifiable list is returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param key The path to the key.
	 * @return The key, or an empty list if the key is missing.
	 */
	public List<Boolean> getBooleanList(@NonNull String key) {
		return this.navigateKeys(key, List.of());
	}

	/**
	 * Access a list of bytes from the config. If the key cannot be found, an
	 * empty unmodifiable list is returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param key The path to the key.
	 * @return The key, or an empty list if the key is missing.
	 */
	public List<Byte> getByteList(@NonNull String key) {
		return this.navigateKeys(key, List.of());
	}

	/**
	 * Access a list of characters from the config. If the key cannot be found,
	 * an empty unmodifiable list is returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param key The path to the key.
	 * @return The key, or an empty list if the key is missing.
	 */
	public List<Character> getCharacterList(@NonNull String key) {
		return this.navigateKeys(key, List.of());
	}

	/**
	 * Access a double from the config. If the key cannot be found, 0 is
	 * returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param key The path to the key.
	 * @return The key, or 0 if the key is missing.
	 */
	public double getDouble(@NonNull String key) {
		return this.navigateKeys(key, 0d);
	}

	/**
	 * Access a list of doubles from the config. If the key cannot be found, an
	 * empty unmodifiable list is returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param key The path to the key.
	 * @return The key, or an empty list if the key is missing.
	 */
	public List<Double> getDoubleList(@NonNull String key) {
		return this.navigateKeys(key, List.of());
	}

	/**
	 * Access an integer from the config. If the key cannot be found, 0 is
	 * returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param key The path to the key.
	 * @return The key, or 0 if the key is missing.
	 */
	public int getInt(@NonNull String key) {
		return this.navigateKeys(key, 0);
	}

	/**
	 * Access a list of integers from the config. If the key cannot be found, an
	 * empty unmodifiable list is returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param key The path to the key.
	 * @return The key, or an empty list if the key is missing.
	 */
	public List<Integer> getIntList(@NonNull String key) {
		return this.navigateKeys(key, List.of());
	}

	/**
	 * Access a generic list from the config. If the key cannot be found, an
	 * empty unmodifiable list is returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
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
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param key The path to the key.
	 * @return The key, or 0 if the key is missing.
	 */
	public long getLong(@NonNull String key) {
		return this.navigateKeys(key, 0L);
	}

	/**
	 * Access a list of longs from the config. If the key cannot be found, an
	 * empty unmodifiable list is returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param key The path to the key.
	 * @return The key, or an empty list if the key is missing.
	 */
	public List<Long> getLongList(@NonNull String key) {
		return this.navigateKeys(key, List.of());
	}

	/**
	 * Access a generic type from the config. If the key cannot be found, or the
	 * key is not of the generic type, the default value is returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param <T> The resulting type.
	 * @param key The path to the key.
	 * @param defaultValue The value to return if the key is not found. May be
	 *            null.
	 * @return The key, or the default value if the key is missing.
	 */
	public <T> T getOrDefault(@NonNull String key, T defaultValue) {
		return this.navigateKeys(key, defaultValue);
	}

	/**
	 * Access a list of shorts from the config. If the key cannot be found, an
	 * empty unmodifiable list is returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param key The path to the key.
	 * @return The key, or an empty list if the key is missing.
	 */
	public List<Short> getShortList(@NonNull String key) {
		return this.navigateKeys(key, List.of());
	}

	/**
	 * Access a string from the config. If the key cannot be found, an empty
	 * string is returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param key The path to the key.
	 * @return The key, or an empty string if the key is missing.
	 */
	public String getString(@NonNull String key) {
		return this.navigateKeys(key, "");
	}

	/**
	 * Access a list of strings from the config. If the key cannot be found, an
	 * empty unmodifiable list is returned.
	 *
	 * Keys are accessed by name, and nested keys are accessed by the full path
	 * to the key with dot separators. For example, {@code "simple-key"} and
	 * {@code "more.complex.nested-key"} are valid keys.
	 *
	 * @param key The path to the key.
	 * @return The key, or an empty list if the key is missing.
	 */
	public List<String> getStringList(@NonNull String key) {
		return this.navigateKeys(key, List.of());
	}

	/**
	 * Dig through the config to find the specified value.
	 *
	 * @param <T> The type to return.
	 * @param keyPath The key, as a path like "test", or
	 *            "test.other.longer-name".
	 * @param defaultValue The default value to return if an entry is not found.
	 * @return The resulting value, or a default one if it is not found.
	 */
	private <T> T navigateKeys(@NonNull String keyPath, T defaultValue) {
		String[] parts = keyPath.split("\\.");
		Map<String, Object> currentMap = this.contents;
		for (int i = 0; i < parts.length; ++i) {
			if (i == parts.length - 1) {
				return this
					.cast(currentMap.getOrDefault(parts[i], defaultValue));
			}
			if (currentMap.get(parts[i]) == null) {
				StringBuilder invalidName = new StringBuilder();
				for (int j = 0; j <= i; ++j) {
					invalidName.append(parts[j]);
				}
				PluginConfig.log.warn("Invalid key {}", invalidName.toString());
				return defaultValue;
			}
			currentMap = this.cast(currentMap.get(parts[i]));
		}
		return defaultValue;
	}

}
