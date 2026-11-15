package com.ikalagaming.util;

import lombok.NonNull;

import java.util.NoSuchElementException;
import java.util.function.LongConsumer;

/**
 * A sorted set of longs, like a {@code TreeSet<Long>} without the boxing: values live in arrays,
 * and once those are big enough adding and removing allocate nothing. Two ints can be packed into a
 * long to sort pairs, the high one first.
 *
 * <p>Neighbor lookups ({@link #floorEntry}, {@link #ceilingEntry} and so on) return an entry
 * handle, an int read with {@link #value(int)}, or {@link #NONE} if there is no such value, rather
 * than a boxed value or null. A handle is only good until the set next changes.
 *
 * <p>Not thread safe.
 */
public final class LongTreeSet {
    /** The entry handle for no entry. */
    public static final int NONE = AvlNodes.NIL;

    /** The values. */
    private final AvlNodes tree;

    /** Make an empty set. */
    public LongTreeSet() {
        this(0);
    }

    /**
     * Make an empty set with room for some values.
     *
     * @param initialCapacity How many values to make room for.
     * @throws IllegalArgumentException If the capacity is negative.
     */
    public LongTreeSet(int initialCapacity) {
        tree = new AvlNodes(initialCapacity, false);
    }

    /**
     * How many values there are.
     *
     * @return The size.
     */
    public int size() {
        return tree.size();
    }

    /**
     * Whether there are no values.
     *
     * @return True if empty.
     */
    public boolean isEmpty() {
        return tree.size() == 0;
    }

    /** Remove every value, keeping the room they took. */
    public void clear() {
        tree.clear();
    }

    /**
     * Whether a value is in the set.
     *
     * @param value The value.
     * @return True if it is.
     */
    public boolean contains(long value) {
        return tree.find(value) != NONE;
    }

    /**
     * Add a value.
     *
     * @param value The value.
     * @return True if it wasn't already there.
     */
    public boolean add(long value) {
        tree.insert(value);
        return tree.inserted();
    }

    /**
     * Remove a value.
     *
     * @param value The value.
     * @return Whether it was there.
     */
    public boolean remove(long value) {
        return tree.delete(value);
    }

    /**
     * The entry with the greatest value at or below one.
     *
     * @param value The value.
     * @return The entry, or {@link #NONE}.
     */
    public int floorEntry(long value) {
        return tree.floor(value);
    }

    /**
     * The entry with the least value at or above one.
     *
     * @param value The value.
     * @return The entry, or {@link #NONE}.
     */
    public int ceilingEntry(long value) {
        return tree.ceiling(value);
    }

    /**
     * The entry with the greatest value below one.
     *
     * @param value The value.
     * @return The entry, or {@link #NONE}.
     */
    public int lowerEntry(long value) {
        return tree.lower(value);
    }

    /**
     * The entry with the least value above one.
     *
     * @param value The value.
     * @return The entry, or {@link #NONE}.
     */
    public int higherEntry(long value) {
        return tree.higher(value);
    }

    /**
     * The entry with the least value.
     *
     * @return The entry, or {@link #NONE} if empty.
     */
    public int firstEntry() {
        return tree.first();
    }

    /**
     * The entry with the greatest value.
     *
     * @return The entry, or {@link #NONE} if empty.
     */
    public int lastEntry() {
        return tree.last();
    }

    /**
     * The least value.
     *
     * @return The value.
     * @throws NoSuchElementException If empty.
     */
    public long first() {
        return value(nonEmpty(tree.first()));
    }

    /**
     * The greatest value.
     *
     * @return The value.
     * @throws NoSuchElementException If empty.
     */
    public long last() {
        return value(nonEmpty(tree.last()));
    }

    /**
     * An entry's value.
     *
     * @param entry The entry, from a lookup since the set last changed.
     * @return Its value.
     */
    public long value(int entry) {
        return tree.keys[entry];
    }

    /**
     * Visit every value in order. The set must not change meanwhile.
     *
     * @param action What to call for each.
     */
    public void forEach(@NonNull LongConsumer action) {
        tree.forEach(node -> action.accept(tree.keys[node]));
    }

    /**
     * Pack two ints into a long that sorts by the first, then by the second as an unsigned int.
     *
     * @param high The first, which decides the order.
     * @param low The second, which breaks ties.
     * @return The packed pair.
     */
    public static long pack(int high, int low) {
        return ((long) high << Integer.SIZE) | Integer.toUnsignedLong(low);
    }

    /**
     * The first of a packed pair.
     *
     * @param packed The pair, see {@link #pack(int, int)}.
     * @return The first int.
     */
    public static int high(long packed) {
        return (int) (packed >> Integer.SIZE);
    }

    /**
     * The second of a packed pair.
     *
     * @param packed The pair, see {@link #pack(int, int)}.
     * @return The second int.
     */
    public static int low(long packed) {
        return (int) packed;
    }

    /**
     * Check that a lookup found something.
     *
     * @param entry The entry.
     * @return The entry.
     * @throws NoSuchElementException If it is {@link #NONE}.
     */
    private static int nonEmpty(int entry) {
        if (entry == NONE) {
            throw new NoSuchElementException("The set is empty");
        }
        return entry;
    }

    /**
     * The tree's height, for tests.
     *
     * @return The height.
     */
    int height() {
        return tree.height();
    }

    @Override
    public String toString() {
        final StringBuilder text = new StringBuilder("[");
        forEach(
                value -> {
                    if (text.length() > 1) {
                        text.append(", ");
                    }
                    text.append(value);
                });
        return text.append(']').toString();
    }
}
