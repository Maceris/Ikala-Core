package com.ikalagaming.util;

/**
 * A {@link BinaryTree} subclass for storing integers. Allows the finding of the smallest int not
 * stored in the tree. This is not synchronized so synchronization must be done externally.
 *
 * @author Ches Burks
 */
public class IntegerTree extends BinaryTree<Integer> {

    /**
     * Returns the smallest integer not stored in the tree.
     *
     * @return the smallest available int not in the tree.
     */
    public int getSmallestUnusedInt() {
        int smallest = 0;
        // Each value is in the tree once, so this stops after at most size + 1 lookups
        while (contains(smallest)) {
            ++smallest;
        }
        return smallest;
    }
}
