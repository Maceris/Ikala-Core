package com.ikalagaming.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.TreeSet;

/** Tests for {@link LongTreeSet}, checked against a {@link TreeSet}. */
class LongTreeSetTest {

    /**
     * Check that a handle lookup matches a {@code TreeSet} result.
     *
     * @param expected The tree set's value, or null.
     * @param set The set.
     * @param entry The handle.
     */
    private static void assertSameValue(Long expected, LongTreeSet set, int entry) {
        if (expected == null) {
            assertEquals(LongTreeSet.NONE, entry);
        } else {
            assertTrue(entry != LongTreeSet.NONE);
            assertEquals(expected, set.value(entry));
        }
    }

    /** Random adds, removes and lookups agree with a TreeSet, and the tree stays balanced. */
    @Test
    void testMatchesTreeSet() {
        final Random random = new Random(4321);
        final LongTreeSet set = new LongTreeSet();
        final TreeSet<Long> expected = new TreeSet<>();
        for (int step = 0; step < 200_000; ++step) {
            // Pairs from a narrow range, so removes often hit
            final long value = LongTreeSet.pack(random.nextInt(60), random.nextInt(60) - 30);
            final int operation = random.nextInt(10);
            if (operation < 5) {
                assertEquals(expected.add(value), set.add(value));
            } else if (operation < 8) {
                assertEquals(expected.remove(value), set.remove(value));
            } else {
                assertEquals(expected.contains(value), set.contains(value));
                assertSameValue(expected.floor(value), set, set.floorEntry(value));
                assertSameValue(expected.ceiling(value), set, set.ceilingEntry(value));
                assertSameValue(expected.lower(value), set, set.lowerEntry(value));
                assertSameValue(expected.higher(value), set, set.higherEntry(value));
                assertSameValue(
                        expected.isEmpty() ? null : expected.first(), set, set.firstEntry());
                assertSameValue(expected.isEmpty() ? null : expected.last(), set, set.lastEntry());
            }
            assertEquals(expected.size(), set.size());
        }
        final double bound = 1.45 * Math.log(set.size() + 2.0) / Math.log(2);
        assertTrue(set.height() <= bound, "Height " + set.height() + " over " + bound);

        final List<Long> values = new ArrayList<>();
        set.forEach(values::add);
        assertEquals(new ArrayList<>(expected), values);
    }

    /** Packed pairs sort by the first int, then the second as unsigned, and unpack. */
    @Test
    void testPacking() {
        final long packed = LongTreeSet.pack(-5, -1);
        assertEquals(-5, LongTreeSet.high(packed));
        assertEquals(-1, LongTreeSet.low(packed));
        assertTrue(LongTreeSet.pack(3, 0) < LongTreeSet.pack(3, 1));
        assertTrue(LongTreeSet.pack(3, Integer.MAX_VALUE) < LongTreeSet.pack(4, 0));
        assertTrue(LongTreeSet.pack(-1, 7) < LongTreeSet.pack(0, 0));

        final LongTreeSet set = new LongTreeSet();
        set.add(LongTreeSet.pack(8, 100));
        set.add(LongTreeSet.pack(8, 20));
        set.add(LongTreeSet.pack(9, 0));
        // The smallest pair with a first of at least 8
        final long fit = set.value(set.ceilingEntry(LongTreeSet.pack(8, 0)));
        assertEquals(8, LongTreeSet.high(fit));
        assertEquals(20, LongTreeSet.low(fit));
    }

    /** Empty sets have no ends, and clearing empties. */
    @Test
    void testEmpty() {
        final LongTreeSet set = new LongTreeSet(2);
        assertTrue(set.isEmpty());
        assertEquals(LongTreeSet.NONE, set.lastEntry());
        assertThrows(NoSuchElementException.class, set::first);
        assertThrows(NoSuchElementException.class, set::last);
        assertTrue(set.add(Long.MIN_VALUE));
        assertTrue(set.add(Long.MAX_VALUE));
        assertFalse(set.add(Long.MAX_VALUE));
        assertEquals(Long.MIN_VALUE, set.first());
        assertEquals(Long.MAX_VALUE, set.last());
        assertEquals("[" + Long.MIN_VALUE + ", " + Long.MAX_VALUE + "]", set.toString());
        set.clear();
        assertTrue(set.isEmpty());
        assertFalse(set.contains(Long.MAX_VALUE));
    }
}
