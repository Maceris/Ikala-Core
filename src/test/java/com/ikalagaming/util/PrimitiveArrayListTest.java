package com.ikalagaming.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * Tests for {@link IntArrayList} and {@link FloatArrayList}, run against an {@link ArrayList}
 * doing the same operations.
 */
class PrimitiveArrayListTest {

    private static final int STEPS = 20_000;
    private static final int VALUE_RANGE = 8;

    /**
     * Run random operations on the list under test and an {@code ArrayList}, comparing them after
     * each step.
     *
     * @param <T> The boxed element type.
     * @param factory Makes an empty list under test.
     * @param value Turns a small int into an element.
     * @param seed The random seed.
     */
    private static <T extends Comparable<T>> void compareWithArrayList(
            Supplier<List<T>> factory, IntFunction<T> value, long seed) {
        final Random random = new Random(seed);
        final List<T> tested = factory.get();
        final List<T> expected = new ArrayList<>();
        for (int step = 0; step < STEPS; ++step) {
            final int size = expected.size();
            final int operation = random.nextInt(16);
            final String what = "step " + step + ", operation " + operation;
            switch (operation) {
                case 0, 1, 2 -> {
                    T item = value.apply(random.nextInt(VALUE_RANGE));
                    tested.add(item);
                    expected.add(item);
                }
                case 3 -> {
                    int index = random.nextInt(size + 1);
                    T item = value.apply(random.nextInt(VALUE_RANGE));
                    tested.add(index, item);
                    expected.add(index, item);
                }
                case 4 -> {
                    List<T> items = randomItems(random, value);
                    assertEquals(expected.addAll(items), tested.addAll(items), what);
                }
                case 5 -> {
                    int index = random.nextInt(size + 1);
                    List<T> items = randomItems(random, value);
                    assertEquals(
                            expected.addAll(index, items), tested.addAll(index, items), what);
                }
                case 6 -> {
                    if (size > 0) {
                        int index = random.nextInt(size);
                        assertEquals(expected.remove(index), tested.remove(index), what);
                    }
                }
                case 7 -> {
                    T item = value.apply(random.nextInt(VALUE_RANGE));
                    assertEquals(expected.remove(item), tested.remove(item), what);
                }
                case 8 -> {
                    List<T> items = randomItems(random, value);
                    assertEquals(expected.removeAll(items), tested.removeAll(items), what);
                }
                case 9 -> {
                    List<T> items = randomItems(random, value);
                    assertEquals(expected.retainAll(items), tested.retainAll(items), what);
                }
                case 10 -> {
                    if (size > 0) {
                        int index = random.nextInt(size);
                        T item = value.apply(random.nextInt(VALUE_RANGE));
                        assertEquals(expected.set(index, item), tested.set(index, item), what);
                    }
                }
                case 11 -> {
                    if (random.nextInt(8) == 0) {
                        tested.clear();
                        expected.clear();
                    }
                }
                case 12 -> {
                    tested.sort(null);
                    expected.sort(null);
                }
                case 13 -> subListStep(random, value, tested, expected, what);
                case 14 -> listIteratorStep(random, value, tested, expected, what);
                default -> {
                    if (size > 0) {
                        int from = random.nextInt(size);
                        int to = from + random.nextInt(size - from + 1);
                        tested.subList(from, to).clear();
                        expected.subList(from, to).clear();
                    }
                }
            }
            assertEquals(expected, new ArrayList<>(tested), what);
            assertEquals(expected.size(), tested.size(), what);
            assertEquals(expected.hashCode(), tested.hashCode(), what);
        }
    }

    private static <T> List<T> randomItems(Random random, IntFunction<T> value) {
        final int count = random.nextInt(4);
        final List<T> items = new ArrayList<>(count);
        for (int i = 0; i < count; ++i) {
            items.add(value.apply(random.nextInt(VALUE_RANGE)));
        }
        return items;
    }

    private static <T> void subListStep(
            Random random, IntFunction<T> value, List<T> tested, List<T> expected, String what) {
        final int size = expected.size();
        final int from = random.nextInt(size + 1);
        final int to = from + random.nextInt(size - from + 1);
        final List<T> testedSub = tested.subList(from, to);
        final List<T> expectedSub = expected.subList(from, to);
        assertEquals(expectedSub, new ArrayList<>(testedSub), what);
        switch (random.nextInt(5)) {
            case 0 -> {
                T item = value.apply(random.nextInt(VALUE_RANGE));
                testedSub.add(item);
                expectedSub.add(item);
            }
            case 1 -> {
                List<T> items = randomItems(random, value);
                assertEquals(expectedSub.removeAll(items), testedSub.removeAll(items), what);
            }
            case 2 -> {
                List<T> items = randomItems(random, value);
                assertEquals(expectedSub.retainAll(items), testedSub.retainAll(items), what);
            }
            case 3 -> {
                List<T> items = randomItems(random, value);
                assertEquals(expectedSub.addAll(items), testedSub.addAll(items), what);
            }
            default -> {
                List<T> seen = new ArrayList<>();
                for (T item : testedSub) {
                    seen.add(item);
                }
                assertEquals(expectedSub, seen, what);
            }
        }
        assertEquals(expectedSub.size(), testedSub.size(), what);
        assertEquals(expectedSub, new ArrayList<>(testedSub), what);
    }

    private static <T> void listIteratorStep(
            Random random, IntFunction<T> value, List<T> tested, List<T> expected, String what) {
        final int start = random.nextInt(expected.size() + 1);
        final ListIterator<T> testedIterator = tested.listIterator(start);
        final ListIterator<T> expectedIterator = expected.listIterator(start);
        final int moves = random.nextInt(6);
        for (int i = 0; i < moves; ++i) {
            if (random.nextBoolean() && expectedIterator.hasNext()) {
                assertEquals(expectedIterator.next(), testedIterator.next(), what);
            } else if (expectedIterator.hasPrevious()) {
                assertEquals(expectedIterator.previous(), testedIterator.previous(), what);
            }
            assertEquals(expectedIterator.nextIndex(), testedIterator.nextIndex(), what);
        }
        T item = value.apply(random.nextInt(VALUE_RANGE));
        testedIterator.add(item);
        expectedIterator.add(item);
    }

    @Test
    void testIntListMatchesArrayList() {
        for (long seed = 1; seed <= 5; ++seed) {
            compareWithArrayList(IntArrayList::new, i -> i, seed);
        }
    }

    @Test
    void testFloatListMatchesArrayList() {
        for (long seed = 1; seed <= 5; ++seed) {
            compareWithArrayList(FloatArrayList::new, i -> (float) i, seed);
        }
    }

    @Test
    void testAddAllKeepsEveryItem() {
        IntArrayList list = new IntArrayList();
        list.addAll(List.of(1, 2, 3));
        assertEquals(List.of(1, 2, 3), new ArrayList<>(list));
    }

    @Test
    void testInsertIntoEmptyAndAtEnd() {
        IntArrayList list = new IntArrayList();
        list.addInt(0, 5);
        list.addInt(1, 7);
        list.add(1, 6);
        assertEquals(List.of(5, 6, 7), new ArrayList<>(list));
        assertThrows(IndexOutOfBoundsException.class, () -> list.addInt(4, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.addInt(-1, 0));
        assertTrue(new IntArrayList().listIterator(0) != null);
    }

    @Test
    void testEqualsWithDifferentHistories() {
        IntArrayList first = new IntArrayList();
        first.addAll(List.of(1, 2, 3));
        IntArrayList second = new IntArrayList();
        for (int i = 0; i <= 3; ++i) {
            second.addInt(i);
        }
        second.removeInt(0);
        assertEquals(first, second);
        assertEquals(second, first);
    }

    @Test
    void testRemoveAllRemovesOnlyMatches() {
        IntArrayList list = new IntArrayList();
        list.addAll(List.of(1, 2, 3, 2, 4));
        list.removeAll(List.of(2));
        assertEquals(List.of(1, 3, 4), new ArrayList<>(list));
        assertEquals(3, list.size());
    }

    @Test
    void testSortIgnoresSpareCapacity() {
        IntArrayList list = new IntArrayList();
        list.addAll(List.of(1, 9, 9));
        list.clear();
        list.addAll(List.of(5, 4));
        list.sort(null);
        assertEquals(List.of(4, 5), new ArrayList<>(list));
    }

    @Test
    void testConvertsOtherNumbers() {
        Collection<Number> numbers = List.of(1L, 2.5, 3);
        assertEquals(List.of(1, 2, 3), new ArrayList<>(new IntArrayList(numbers)));
        assertEquals(List.of(1f, 2.5f, 3f), new ArrayList<>(new FloatArrayList(numbers)));
    }
}
