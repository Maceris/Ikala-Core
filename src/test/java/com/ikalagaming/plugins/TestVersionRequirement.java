package com.ikalagaming.plugins;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.List;

/** Tests for the versions a plugin accepts for its dependencies. */
class TestVersionRequirement {

    @Test
    void testAny() {
        VersionRequirement any = VersionRequirement.parse(List.of());
        assertTrue(any.isAny());
        assertTrue(any.accepts("0.0.1-alpha"));
    }

    @Test
    void testExact() {
        VersionRequirement exact = VersionRequirement.parse(List.of("1.2.3"));
        assertTrue(exact.accepts("1.2.3"));
        // Build metadata doesn't change precedence
        assertTrue(exact.accepts("1.2.3+build.7"));
        assertFalse(exact.accepts("1.2.4"));
    }

    @Test
    void testInvalidBounds() {
        assertThrows(
                IllegalArgumentException.class, () -> VersionRequirement.parse(List.of(">=1.0")));
        assertThrows(
                IllegalArgumentException.class, () -> VersionRequirement.parse(List.of("~1.0.0")));
    }

    @Test
    void testPreReleaseComesFirst() {
        VersionRequirement atLeast = VersionRequirement.parse(List.of(">=1.0.0"));
        assertFalse(atLeast.accepts("1.0.0-alpha"));
        assertTrue(VersionRequirement.parse(List.of(">=1.0.0-alpha")).accepts("1.0.0-alpha"));
    }

    @Test
    void testRange() {
        VersionRequirement range = VersionRequirement.parse(List.of(">=1.2.0", "<2.0.0"));
        assertEquals(">=1.2.0 <2.0.0", range.toString());
        assertFalse(range.accepts("1.1.9"));
        assertTrue(range.accepts("1.2.0"));
        assertTrue(range.accepts("1.9.9"));
        assertFalse(range.accepts("2.0.0"));
        assertFalse(range.accepts("not a version"));
    }

    @Test
    void testStrictBounds() {
        VersionRequirement newer = VersionRequirement.parse(List.of(">1.0.0", "<=1.5.0"));
        assertFalse(newer.accepts("1.0.0"));
        assertTrue(newer.accepts("1.0.1"));
        assertTrue(newer.accepts("1.5.0"));
        assertFalse(newer.accepts("1.5.1"));
    }
}
