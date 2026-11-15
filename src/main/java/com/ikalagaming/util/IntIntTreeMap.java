package com.ikalagaming.util;

import lombok.NonNull;

import java.util.NoSuchElementException;

/**
 * A sorted map from int keys to int values, like a {@code TreeMap<Integer, Integer>} without the
 * boxing: entries live in arrays, and once those are big enough adding and removing allocate
 * nothing.
 *
 * <p>Lookups that would return a {@code Map.Entry} in a {@code TreeMap} return an entry handle here
 * instead, an int read with {@link #key(int)} and {@link #value(int)}, or {@link #NONE} if there is
 * no such entry. A handle is only good until the map next changes.
 *
 * <p>Not thread safe.
 */
public final class IntIntTreeMap {
    /** The entry handle for no entry. */
    public static final int NONE = AvlNodes.NIL;

    /** The entries. */
    private final AvlNodes tree;

    /** Visits entries in key order. */
    @FunctionalInterface
    public interface EntryConsumer {
        /**
         * Visit an entry.
         *
         * @param key Its key.
         * @param value Its value.
         */
        void accept(int key, int value);
    }

    /** Make an empty map. */
    public IntIntTreeMap() {
        this(0);
    }

    /**
     * Make an empty map with room for some entries.
     *
     * @param initialCapacity How many entries to make room for.
     * @throws IllegalArgumentException If the capacity is negative.
     */
    public IntIntTreeMap(int initialCapacity) {
        tree = new AvlNodes(initialCapacity, true);
    }

    /**
     * How many entries there are.
     *
     * @return The size.
     */
    public int size() {
        return tree.size();
    }

    /**
     * Whether there are no entries.
     *
     * @return True if empty.
     */
    public boolean isEmpty() {
        return tree.size() == 0;
    }

    /** Remove every entry, keeping the room they took. */
    public void clear() {
        tree.clear();
    }

    /**
     * Whether a key has an entry.
     *
     * @param key The key.
     * @return True if it does.
     */
    public boolean containsKey(int key) {
        return tree.find(key) != NONE;
    }

    /**
     * A key's value.
     *
     * @param key The key.
     * @param ifAbsent What to return if the key has no entry.
     * @return The value, or {@code ifAbsent}.
     */
    public int get(int key, int ifAbsent) {
        final int entry = tree.find(key);
        return entry == NONE ? ifAbsent : tree.values[entry];
    }

    /**
     * Set a key's value, adding an entry if it has none.
     *
     * @param key The key.
     * @param value The value.
     * @return True if the entry is new, false if an existing value was replaced.
     */
    public boolean put(int key, int value) {
        final int entry = tree.insert(key);
        tree.values[entry] = value;
        return tree.inserted();
    }

    /**
     * Remove a key's entry.
     *
     * @param key The key.
     * @return Whether it had one.
     */
    public boolean remove(int key) {
        return tree.delete(key);
    }

    /**
     * The entry for a key.
     *
     * @param key The key.
     * @return The entry, or {@link #NONE}.
     */
    public int entry(int key) {
        return tree.find(key);
    }

    /**
     * The entry with the greatest key at or below a key.
     *
     * @param key The key.
     * @return The entry, or {@link #NONE}.
     */
    public int floorEntry(int key) {
        return tree.floor(key);
    }

    /**
     * The entry with the least key at or above a key.
     *
     * @param key The key.
     * @return The entry, or {@link #NONE}.
     */
    public int ceilingEntry(int key) {
        return tree.ceiling(key);
    }

    /**
     * The entry with the greatest key below a key.
     *
     * @param key The key.
     * @return The entry, or {@link #NONE}.
     */
    public int lowerEntry(int key) {
        return tree.lower(key);
    }

    /**
     * The entry with the least key above a key.
     *
     * @param key The key.
     * @return The entry, or {@link #NONE}.
     */
    public int higherEntry(int key) {
        return tree.higher(key);
    }

    /**
     * The entry with the least key.
     *
     * @return The entry, or {@link #NONE} if empty.
     */
    public int firstEntry() {
        return tree.first();
    }

    /**
     * The entry with the greatest key.
     *
     * @return The entry, or {@link #NONE} if empty.
     */
    public int lastEntry() {
        return tree.last();
    }

    /**
     * The least key.
     *
     * @return The key.
     * @throws NoSuchElementException If empty.
     */
    public int firstKey() {
        return key(nonEmpty(tree.first()));
    }

    /**
     * The greatest key.
     *
     * @return The key.
     * @throws NoSuchElementException If empty.
     */
    public int lastKey() {
        return key(nonEmpty(tree.last()));
    }

    /**
     * An entry's key.
     *
     * @param entry The entry, from a lookup since the map last changed.
     * @return Its key.
     */
    public int key(int entry) {
        return (int) tree.keys[entry];
    }

    /**
     * An entry's value.
     *
     * @param entry The entry, from a lookup since the map last changed.
     * @return Its value.
     */
    public int value(int entry) {
        return tree.values[entry];
    }

    /**
     * Change an entry's value. This doesn't change the map's shape, so handles stay good.
     *
     * @param entry The entry, from a lookup since the map last changed.
     * @param value The new value.
     */
    public void setValue(int entry, int value) {
        tree.values[entry] = value;
    }

    /**
     * Visit every entry in key order. The map must not change meanwhile.
     *
     * @param action What to call for each.
     */
    public void forEach(@NonNull EntryConsumer action) {
        tree.forEach(node -> action.accept((int) tree.keys[node], tree.values[node]));
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
            throw new NoSuchElementException("The map is empty");
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
        final StringBuilder text = new StringBuilder("{");
        forEach(
                (key, value) -> {
                    if (text.length() > 1) {
                        text.append(", ");
                    }
                    text.append(key).append('=').append(value);
                });
        return text.append('}').toString();
    }
}
