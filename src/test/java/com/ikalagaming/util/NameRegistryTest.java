package com.ikalagaming.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests for {@link NameRegistry}. */
class NameRegistryTest {

    @Test
    void testFreedIdsAreReused() {
        NameRegistry registry = new NameRegistry();
        assertEquals("House-0", registry.registerName("House"));
        assertEquals("House-1", registry.registerName("House"));
        assertEquals("House-2", registry.registerName("House"));
        assertTrue(registry.unregisterName("House-1"));
        assertFalse(registry.contains("House-1"));
        assertTrue(registry.contains("House-2"));
        assertEquals(1, registry.getNextID("House"));
        assertEquals("House-1", registry.registerName("House"));
    }

    @Test
    void testUnknownNames() {
        NameRegistry registry = new NameRegistry();
        assertFalse(registry.contains("Car-0"));
        assertFalse(registry.unregisterName("Car-0"));
        assertFalse(registry.contains("-0"));
        assertFalse(registry.contains("Car"));
        assertFalse(registry.contains("Car-x"));
        assertEquals(0, registry.getNextID("Car"));
    }

    @Test
    void testBaseNamesWithDashes() {
        NameRegistry registry = new NameRegistry();
        assertEquals("a-b-0", registry.registerName("a-b"));
        assertTrue(registry.contains("a-b-0"));
        assertTrue(registry.unregisterName("a-b-0"));
    }

    @Test
    void testClearEverything() {
        NameRegistry registry = new NameRegistry();
        registry.registerName("House");
        registry.registerName("Car");
        registry.registerName("Tree");
        registry.clear();
        assertFalse(registry.contains("House-0"));
        assertEquals("Car-0", registry.registerName("Car"));
    }
}
