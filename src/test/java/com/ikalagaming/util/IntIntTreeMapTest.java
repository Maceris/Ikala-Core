package com.ikalagaming.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.TreeMap;

/** Tests for {@link IntIntTreeMap}, checked against a {@link TreeMap}. */
class IntIntTreeMapTest {

    /**
     * Check that a handle lookup matches a {@code TreeMap} entry.
     *
     * @param expected The tree map's entry, or null.
     * @param map The map.
     * @param entry The handle.
     */
    private static void assertSameEntry(
            Map.Entry<Integer, Integer> expected, IntIntTreeMap map, int entry) {
        if (expected == null) {
            assertEquals(IntIntTreeMap.NONE, entry);
        } else {
            assertTrue(entry != IntIntTreeMap.NONE);
            assertEquals(expected.getKey(), map.key(entry));
            assertEquals(expected.getValue(), map.value(entry));
        }
    }

    /** Random puts, removes and lookups agree with a TreeMap, and the tree stays balanced. */
    @Test
    void testMatchesTreeMap() {
        final Random random = new Random(1234);
        final IntIntTreeMap map = new IntIntTreeMap();
        final TreeMap<Integer, Integer> expected = new TreeMap<>();
        for (int step = 0; step < 200_000; ++step) {
            // A narrow key range, so removes and replaces often hit
            final int key = random.nextInt(4000) - 2000;
            final int operation = random.nextInt(10);
            if (operation < 5) {
                final int value = random.nextInt();
                assertEquals(expected.put(key, value) == null, map.put(key, value));
            } else if (operation < 8) {
                assertEquals(expected.remove(key) != null, map.remove(key));
            } else {
                assertEquals(expected.containsKey(key), map.containsKey(key));
                assertEquals(expected.getOrDefault(key, -7), map.get(key, -7));
                assertSameEntry(expected.floorEntry(key), map, map.floorEntry(key));
                assertSameEntry(expected.ceilingEntry(key), map, map.ceilingEntry(key));
                assertSameEntry(expected.lowerEntry(key), map, map.lowerEntry(key));
                assertSameEntry(expected.higherEntry(key), map, map.higherEntry(key));
                assertSameEntry(expected.firstEntry(), map, map.firstEntry());
                assertSameEntry(expected.lastEntry(), map, map.lastEntry());
            }
            assertEquals(expected.size(), map.size());
        }
        // AVL: at most about 1.44 log2(n + 2)
        final double bound = 1.45 * Math.log(map.size() + 2.0) / Math.log(2);
        assertTrue(map.height() <= bound, "Height " + map.height() + " over " + bound);

        final List<Integer> keys = new ArrayList<>();
        final List<Integer> values = new ArrayList<>();
        map.forEach(
                (key, value) -> {
                    keys.add(key);
                    values.add(value);
                });
        assertEquals(new ArrayList<>(expected.keySet()), keys);
        assertEquals(new ArrayList<>(expected.values()), values);
    }

    /** Ascending inserts, the worst case for an unbalanced tree, stay shallow. */
    @Test
    void testSortedInsertsStayBalanced() {
        final IntIntTreeMap map = new IntIntTreeMap();
        final int count = 1 << 16;
        for (int i = 0; i < count; ++i) {
            map.put(i, -i);
        }
        assertTrue(map.height() <= 17, "Height " + map.height());
        for (int i = 0; i < count; i += 2) {
            assertTrue(map.remove(i));
        }
        assertEquals(count / 2, map.size());
        assertEquals(1, map.firstKey());
        assertEquals(count - 1, map.lastKey());
        assertEquals(-5, map.get(5, 0));
    }

    /** Extreme keys sort as ints. */
    @Test
    void testExtremeKeys() {
        final IntIntTreeMap map = new IntIntTreeMap();
        map.put(Integer.MAX_VALUE, 1);
        map.put(Integer.MIN_VALUE, 2);
        map.put(0, 3);
        assertEquals(Integer.MIN_VALUE, map.firstKey());
        assertEquals(Integer.MAX_VALUE, map.lastKey());
        assertEquals(0, map.key(map.lowerEntry(Integer.MAX_VALUE)));
        assertEquals("{-2147483648=2, 0=3, 2147483647=1}", map.toString());
    }

    /** Values change in place, and replacing one isn't counted as new. */
    @Test
    void testValues() {
        final IntIntTreeMap map = new IntIntTreeMap(4);
        assertTrue(map.put(10, 1));
        assertFalse(map.put(10, 2));
        assertEquals(2, map.get(10, 0));
        map.setValue(map.entry(10), 5);
        assertEquals(5, map.get(10, 0));
        assertEquals(1, map.size());
    }

    /** Empty maps have no ends, and clearing empties. */
    @Test
    void testEmpty() {
        final IntIntTreeMap map = new IntIntTreeMap();
        assertTrue(map.isEmpty());
        assertEquals(IntIntTreeMap.NONE, map.firstEntry());
        assertThrows(NoSuchElementException.class, map::firstKey);
        assertThrows(NoSuchElementException.class, map::lastKey);
        assertFalse(map.remove(3));
        map.put(3, 4);
        map.clear();
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey(3));
        assertThrows(IllegalArgumentException.class, () -> new IntIntTreeMap(-1));
    }
}
