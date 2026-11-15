package com.ikalagaming.util;

import java.util.Arrays;

/**
 * The AVL tree behind {@link IntIntTreeMap} and {@link LongTreeSet}: nodes live in parallel arrays
 * and are found by index, so adding and removing never boxes or allocates once the arrays are big
 * enough. Removed nodes are reused before the arrays grow.
 *
 * <p>Keys are longs; the int map stores its keys widened. A node's index is the handle the public
 * classes hand out, valid until the next change to the tree, because removing a node with two
 * children moves its successor's key into it.
 *
 * <p>Not thread safe.
 */
final class AvlNodes {
    /** No node: an empty subtree, or nothing found. */
    static final int NIL = -1;

    /** The fewest nodes there is room for once anything is added. */
    private static final int MIN_CAPACITY = 16;

    /** Each node's key. */
    long[] keys;

    /** Each node's value, or null for a set. */
    int[] values;

    /** Each node's left child, or the next free node for a free one. */
    private int[] left;

    /** Each node's right child. */
    private int[] right;

    /** Each node's height, 1 for a leaf. An AVL tree of 2^31 nodes is under 50 high. */
    private byte[] heights;

    /** The root, or {@link #NIL} if empty. */
    private int root = NIL;

    /** How many keys are in the tree. */
    private int size;

    /** How many nodes have ever been made, free ones included: the rest of the arrays is unused. */
    private int made;

    /** The first of the free nodes, chained through {@link #left}. */
    private int freeHead = NIL;

    /** Whether the last insert found the key already there, or the last delete found it. */
    private boolean found;

    /** The node the last insert made or found. */
    private int touched;

    /**
     * Make an empty tree.
     *
     * @param initialCapacity How many nodes to make room for.
     * @param withValues Whether each node carries an int value.
     * @throws IllegalArgumentException If the capacity is negative.
     */
    AvlNodes(int initialCapacity, boolean withValues) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity must not be negative");
        }
        keys = new long[initialCapacity];
        values = withValues ? new int[initialCapacity] : null;
        left = new int[initialCapacity];
        right = new int[initialCapacity];
        heights = new byte[initialCapacity];
    }

    /**
     * How many keys are in the tree.
     *
     * @return The size.
     */
    int size() {
        return size;
    }

    /** Remove every key, keeping the arrays. */
    void clear() {
        root = NIL;
        size = 0;
        made = 0;
        freeHead = NIL;
    }

    /**
     * The tree's height, for tests.
     *
     * @return The root's height, 0 if empty.
     */
    int height() {
        return height(root);
    }

    /**
     * Add a key, or find it if it is already there.
     *
     * @param key The key.
     * @return Its node.
     */
    int insert(long key) {
        // Before descending: an assignment like left[n] = insert(...) stores into the array it
        // read first, so the arrays must not be replaced partway down
        if (freeHead == NIL && made == keys.length) {
            grow();
        }
        root = insert(root, key);
        return touched;
    }

    /**
     * Whether the last {@link #insert(long)} added a new key.
     *
     * @return False if the key was already there.
     */
    boolean inserted() {
        return !found;
    }

    /**
     * Remove a key.
     *
     * @param key The key.
     * @return Whether it was there.
     */
    boolean delete(long key) {
        found = false;
        root = delete(root, key);
        if (found) {
            --size;
        }
        return found;
    }

    /**
     * The node holding a key.
     *
     * @param key The key.
     * @return Its node, or {@link #NIL}.
     */
    int find(long key) {
        int node = root;
        while (node != NIL) {
            final int order = Long.compare(key, keys[node]);
            if (order == 0) {
                return node;
            }
            node = order < 0 ? left[node] : right[node];
        }
        return NIL;
    }

    /**
     * The node with the greatest key at or below one.
     *
     * @param key The key.
     * @return The node, or {@link #NIL}.
     */
    int floor(long key) {
        int node = root;
        int best = NIL;
        while (node != NIL) {
            final int order = Long.compare(key, keys[node]);
            if (order == 0) {
                return node;
            }
            if (order > 0) {
                best = node;
                node = right[node];
            } else {
                node = left[node];
            }
        }
        return best;
    }

    /**
     * The node with the least key at or above one.
     *
     * @param key The key.
     * @return The node, or {@link #NIL}.
     */
    int ceiling(long key) {
        int node = root;
        int best = NIL;
        while (node != NIL) {
            final int order = Long.compare(key, keys[node]);
            if (order == 0) {
                return node;
            }
            if (order < 0) {
                best = node;
                node = left[node];
            } else {
                node = right[node];
            }
        }
        return best;
    }

    /**
     * The node with the greatest key below one.
     *
     * @param key The key.
     * @return The node, or {@link #NIL}.
     */
    int lower(long key) {
        int node = root;
        int best = NIL;
        while (node != NIL) {
            if (keys[node] < key) {
                best = node;
                node = right[node];
            } else {
                node = left[node];
            }
        }
        return best;
    }

    /**
     * The node with the least key above one.
     *
     * @param key The key.
     * @return The node, or {@link #NIL}.
     */
    int higher(long key) {
        int node = root;
        int best = NIL;
        while (node != NIL) {
            if (keys[node] > key) {
                best = node;
                node = left[node];
            } else {
                node = right[node];
            }
        }
        return best;
    }

    /**
     * The node with the least key.
     *
     * @return The node, or {@link #NIL} if empty.
     */
    int first() {
        int node = root;
        if (node == NIL) {
            return NIL;
        }
        while (left[node] != NIL) {
            node = left[node];
        }
        return node;
    }

    /**
     * The node with the greatest key.
     *
     * @return The node, or {@link #NIL} if empty.
     */
    int last() {
        int node = root;
        if (node == NIL) {
            return NIL;
        }
        while (right[node] != NIL) {
            node = right[node];
        }
        return node;
    }

    /** Visits nodes in key order. */
    @FunctionalInterface
    interface NodeVisitor {
        /**
         * Visit a node.
         *
         * @param node The node.
         */
        void visit(int node);
    }

    /**
     * Visit every node in key order. The tree must not change meanwhile.
     *
     * @param visitor What to call for each.
     */
    void forEach(NodeVisitor visitor) {
        forEach(root, visitor);
    }

    /**
     * Visit a subtree in key order.
     *
     * @param node The subtree's root.
     * @param visitor What to call for each.
     */
    private void forEach(int node, NodeVisitor visitor) {
        if (node == NIL) {
            return;
        }
        forEach(left[node], visitor);
        visitor.visit(node);
        forEach(right[node], visitor);
    }

    /**
     * Add a key to a subtree, or find it.
     *
     * @param node The subtree's root.
     * @param key The key.
     * @return The subtree's new root.
     */
    private int insert(int node, long key) {
        if (node == NIL) {
            touched = newNode(key);
            found = false;
            ++size;
            return touched;
        }
        final int order = Long.compare(key, keys[node]);
        if (order == 0) {
            touched = node;
            found = true;
            return node;
        }
        if (order < 0) {
            left[node] = insert(left[node], key);
        } else {
            right[node] = insert(right[node], key);
        }
        return found ? node : balance(node);
    }

    /**
     * Remove a key from a subtree.
     *
     * @param node The subtree's root.
     * @param key The key.
     * @return The subtree's new root.
     */
    private int delete(int node, long key) {
        if (node == NIL) {
            return NIL;
        }
        final int order = Long.compare(key, keys[node]);
        if (order < 0) {
            left[node] = delete(left[node], key);
        } else if (order > 0) {
            right[node] = delete(right[node], key);
        } else {
            found = true;
            if (left[node] == NIL || right[node] == NIL) {
                final int child = left[node] != NIL ? left[node] : right[node];
                freeNode(node);
                return child;
            }
            // Two children: the successor's key moves up, and its node is the one removed
            int successor = right[node];
            while (left[successor] != NIL) {
                successor = left[successor];
            }
            keys[node] = keys[successor];
            if (values != null) {
                values[node] = values[successor];
            }
            right[node] = deleteFirst(right[node]);
        }
        return found ? balance(node) : node;
    }

    /**
     * Remove a subtree's least key.
     *
     * @param node The subtree's root.
     * @return The subtree's new root.
     */
    private int deleteFirst(int node) {
        if (left[node] == NIL) {
            final int child = right[node];
            freeNode(node);
            return child;
        }
        left[node] = deleteFirst(left[node]);
        return balance(node);
    }

    /**
     * Take a node for a key, a free one if there is one. There must be room.
     *
     * @param key The key.
     * @return The node, a leaf.
     */
    private int newNode(long key) {
        final int node;
        if (freeHead != NIL) {
            node = freeHead;
            freeHead = left[node];
        } else {
            node = made++;
        }
        keys[node] = key;
        left[node] = NIL;
        right[node] = NIL;
        heights[node] = 1;
        return node;
    }

    /**
     * Put a node on the free list.
     *
     * @param node The node.
     */
    private void freeNode(int node) {
        left[node] = freeHead;
        freeHead = node;
    }

    /** Make room for more nodes, by half again. */
    private void grow() {
        final int length = keys.length;
        if (length == Integer.MAX_VALUE) {
            throw new OutOfMemoryError("Tree is full");
        }
        final int grown =
                (int) Math.min(Integer.MAX_VALUE, Math.max(MIN_CAPACITY, length + (length >> 1)));
        keys = Arrays.copyOf(keys, grown);
        if (values != null) {
            values = Arrays.copyOf(values, grown);
        }
        left = Arrays.copyOf(left, grown);
        right = Arrays.copyOf(right, grown);
        heights = Arrays.copyOf(heights, grown);
    }

    /**
     * A subtree's height.
     *
     * @param node The subtree's root, or {@link #NIL}.
     * @return Its height, 0 if empty.
     */
    private int height(int node) {
        return node == NIL ? 0 : heights[node];
    }

    /**
     * Work out a node's height from its children's.
     *
     * @param node The node.
     */
    private void updateHeight(int node) {
        heights[node] = (byte) (1 + Math.max(height(left[node]), height(right[node])));
    }

    /**
     * Rotate a subtree right, its left child becoming its root.
     *
     * @param node The subtree's root.
     * @return The new root.
     */
    private int rotateRight(int node) {
        final int child = left[node];
        left[node] = right[child];
        right[child] = node;
        updateHeight(node);
        updateHeight(child);
        return child;
    }

    /**
     * Rotate a subtree left, its right child becoming its root.
     *
     * @param node The subtree's root.
     * @return The new root.
     */
    private int rotateLeft(int node) {
        final int child = right[node];
        right[node] = left[child];
        left[child] = node;
        updateHeight(node);
        updateHeight(child);
        return child;
    }

    /**
     * Restore a subtree's balance after one side changed height by one.
     *
     * @param node The subtree's root.
     * @return The new root.
     */
    private int balance(int node) {
        updateHeight(node);
        final int tilt = height(left[node]) - height(right[node]);
        if (tilt > 1) {
            if (height(left[left[node]]) < height(right[left[node]])) {
                left[node] = rotateLeft(left[node]);
            }
            return rotateRight(node);
        }
        if (tilt < -1) {
            if (height(right[right[node]]) < height(left[right[node]])) {
                right[node] = rotateRight(right[node]);
            }
            return rotateLeft(node);
        }
        return node;
    }
}
