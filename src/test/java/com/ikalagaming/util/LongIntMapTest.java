package com.ikalagaming.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Tests for the long to int map. */
class LongIntMapTest {

    /** Pairs whose halves XOR to the same value, which collide in a boxed Long set, all stay. */
    @Test
    void testCollidingPairsAllKept() {
        LongIntMap map = new LongIntMap(0);
        final int count = 5000;
        for (int a = 0; a < count; ++a) {
            // a ^ (a ^ 7) is always 7
            assertEquals(LongIntMap.MISSING, map.putIfAbsent(LongIntMap.pair(a, a ^ 7), a));
        }
        assertEquals(count, map.size());
        for (int a = 0; a < count; ++a) {
            assertEquals(a, map.get(LongIntMap.pair(a, a ^ 7)));
        }
    }

    /** A key already there keeps its value, and a missing key reads as missing. */
    @Test
    void testPutIfAbsentAndMissing() {
        LongIntMap map = new LongIntMap(4);
        assertEquals(LongIntMap.MISSING, map.putIfAbsent(-1L, 3));
        assertEquals(3, map.putIfAbsent(-1L, 9));
        assertEquals(3, map.get(-1L));
        assertEquals(LongIntMap.MISSING, map.get(0L));
        assertEquals(LongIntMap.MISSING, map.putIfAbsent(LongIntMap.pair(-5, -6), 1));
        assertEquals(1, map.get(LongIntMap.pair(-5, -6)));
        assertEquals(2, map.size());
    }

    /**
     * Removing keys keeps every other key findable, even in long runs of collisions, and against a
     * boxed map doing the same.
     */
    @Test
    void testRemove() {
        final LongIntMap map = new LongIntMap(0);
        final java.util.Map<Long, Integer> expected = new java.util.HashMap<>();
        final java.util.SplittableRandom random = new java.util.SplittableRandom(7);
        for (int step = 0; step < 20_000; ++step) {
            // Few distinct keys, so runs are long and removals are common
            final long key = LongIntMap.pair(random.nextInt(64), random.nextInt(4));
            if (random.nextInt(3) == 0) {
                final Integer was = expected.remove(key);
                assertEquals(was == null ? LongIntMap.MISSING : was, map.remove(key));
            } else {
                final int value = random.nextInt(1000);
                expected.put(key, value);
                map.put(key, value);
            }
            assertEquals(expected.size(), map.size());
        }
        for (int a = 0; a < 64; ++a) {
            for (int b = 0; b < 4; ++b) {
                final Integer value = expected.get(LongIntMap.pair(a, b));
                assertEquals(
                        value == null ? LongIntMap.MISSING : value, map.get(LongIntMap.pair(a, b)));
            }
        }
        assertEquals(LongIntMap.MISSING, map.remove(LongIntMap.pair(-1, -1)));
    }
}
