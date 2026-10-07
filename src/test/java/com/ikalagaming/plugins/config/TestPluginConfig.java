package com.ikalagaming.plugins.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.plugins.PluginManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

/**
 * Tests for reading and changing values in a plugin configuration. The configuration is loaded from
 * YAML, so the values have the same types they would have when read from a file.
 *
 * @author Ches Burks
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestPluginConfig {

    private static final String YAML =
            """
            small: 5
            big: 10000000000
            decimal: 2.5
            text: hello
            number-text: '42'
            flag: true
            empty:
            section:
              nested: inner
            numbers: [1, 2, 3]
            mixed: [1, 300, x, 2]
            letters: [a, bc, d]
            scalars: [1, a, true]
            """;

    @Mock private EventManager eventManager;

    private PluginConfig config;

    @AfterEach
    void afterTest() {
        PluginManager.destroyInstance();
    }

    @BeforeEach
    void beforeTest() {
        // Warnings look up messages through the plugin manager
        PluginManager.getInstance(eventManager);
        Map<String, Object> contents = new Yaml().load(YAML);
        config = new PluginConfig(contents);
    }

    @Test
    void testGenericAccess() {
        assertEquals("hello", config.<String>get("text"));
        assertNull(config.get("missing"));
        assertEquals("fallback", config.getOrDefault("empty", "fallback"));
        assertEquals("inner", config.getOrDefault("section.nested", "fallback"));
        // Not converted, so the wrong type fails where it's used
        assertThrows(
                ClassCastException.class,
                () -> {
                    String value = config.get("small");
                    value.length();
                });
    }

    @Test
    void testListConversion() {
        assertEquals(List.of(1L, 2L, 3L), config.getLongList("numbers"));
        assertEquals(List.of(1.0, 2.0, 3.0), config.getDoubleList("numbers"));
        assertEquals(List.of((short) 1, (short) 2, (short) 3), config.getShortList("numbers"));
        // 300 doesn't fit in a byte, and x isn't a number, so both are left out
        assertEquals(List.of((byte) 1, (byte) 2), config.getByteList("mixed"));
        assertEquals(List.of('a', 'd'), config.getCharacterList("letters"));
        assertEquals(List.of("1", "a", "true"), config.getStringList("scalars"));
        assertEquals(List.of(), config.getIntList("text"));
        assertEquals(List.of(), config.getIntList("missing"));
    }

    @Test
    void testListsAreCopies() {
        List<?> numbers = config.getList("numbers");
        assertThrows(UnsupportedOperationException.class, () -> numbers.remove(0));
        assertEquals(3, config.getList("numbers").size());
    }

    @Test
    void testNullAndMissingUseDefaults() {
        assertEquals(0, config.getInt("empty"));
        assertFalse(config.getBoolean("empty"));
        assertEquals("", config.getString("empty"));
        assertEquals(0L, config.getLong("missing"));
        assertEquals(0, config.getDouble("section.missing.deeper"));
    }

    @Test
    void testNumberConversion() {
        // YAML loads small numbers as integers
        assertEquals(5L, config.getLong("small"));
        assertEquals(5.0, config.getDouble("small"));
        assertEquals(10000000000L, config.getLong("big"));
        assertEquals(2.5, config.getDouble("decimal"));
        assertEquals("5", config.getString("small"));
        assertEquals("true", config.getString("flag"));
    }

    @Test
    void testPresenceAndTypeChecks() {
        assertTrue(config.isPresent("empty"));
        assertFalse(config.isPresent("missing"));
        assertTrue(config.isLong("small"));
        assertTrue(config.isInt("small"));
        assertFalse(config.isInt("big"));
        assertTrue(config.isLong("big"));
        assertTrue(config.isDouble("small"));
        assertFalse(config.isInt("decimal"));
        assertTrue(config.isString("number-text"));
        assertFalse(config.isString("small"));
        assertTrue(config.isList("numbers"));
        assertTrue(config.isBoolean("flag"));
        assertFalse(config.isBoolean("empty"));
    }

    @Test
    void testSetCreatesAndReplacesSections() {
        config.set("new.nested.key", 1);
        assertEquals(1, config.getInt("new.nested.key"));

        // "small" holds a number, so it has to become a section
        config.set("small.child", "value");
        assertEquals("value", config.getString("small.child"));
        assertFalse(config.isInt("small"));
    }

    @Test
    void testWrongTypesUseDefaults() {
        assertEquals(0, config.getInt("text"));
        assertEquals(0, config.getInt("big"));
        assertEquals(0, config.getInt("decimal"));
        assertFalse(config.getBoolean("text"));
        assertEquals("", config.getString("numbers"));
        // A value where a section was expected
        assertEquals("", config.getString("text.child"));
        assertFalse(config.isPresent("text.child"));
    }
}
