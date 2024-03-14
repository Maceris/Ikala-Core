package com.ikalagaming.util;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * A binary AVL tree for storing (comparable) objects. This is not synchronized, so synchronization
 * must be done externally.
 *
 * @author Ches Burks
 * @param <T> the type of object to store. Must extend {@link Comparable}
 */
public class BinaryTree<T extends Comparable<T>> {
    /**
     * A binary tree node used in the {@link BinaryTree} class.
     *
     * @author Ches Burks
     * @param <R> the Type of object this node holds
     */
    @Getter
    @Setter
    protected static class BinaryTreeNode<R extends Comparable<R>> {
        /** The value stored in this node. */
        protected R key;

        /** The height of the largest subtree that starts at this node. */
        protected int height;

        /** A pointer to the left child. Null if there is no left child. */
        protected BinaryTreeNode<R> left;

        /** A pointer to the right child. Null if there is no right child. */
        protected BinaryTreeNode<R> right;

        /**
         * A pointer to the parent node. The parent should be null if and only if it is the root of
         * the tree.
         */
        protected BinaryTreeNode<R> parent;

        /**
         * Constructs a new BinaryTree node for the given object and children/parent. The other
         * nodes may be null. Height defaults to 1.
         *
         * @param newKey the key to store
         * @param leftNode the left node
         * @param parentNode the parent node
         * @param rightNode the right node
         */
        public BinaryTreeNode(
                R newKey,
                BinaryTreeNode<R> leftNode,
                BinaryTreeNode<R> parentNode,
                BinaryTreeNode<R> rightNode) {
            key = newKey;
            height = 1;
            left = leftNode;
            parent = parentNode;
            right = rightNode;
        }
    }

    /** The base of the tree. This is null if the tree is empty. */
    protected BinaryTreeNode<T> treeRoot;

    /** How many elements are stored in the tree. */
    protected int size;

    /** Removes all objects from the tree. */
    public void clear() {
        treeRoot = null;
        size = 0;
    }

    /**
     * Returns true if the tree contains the given object
     *
     * @param toFind the entry to search for
     * @return true if the object exists or false if it is not in the tree
     */
    public boolean contains(T toFind) {
        return this.find(toFind, treeRoot) != null;
    }

    /**
     * Recursively searches for the entry and returns the node that contains it or null if it is not
     * found.
     *
     * @param toFind the entry to search for
     * @param root where to start looking
     * @return null or the node that has toFind as its key
     */
    protected BinaryTreeNode<T> find(T toFind, BinaryTreeNode<T> root) {
        if (root == null || root.key == null) {
            return null;
        }
        int compareVal = toFind.compareTo(root.key);
        if (compareVal == 0) {
            // they match
            return root;
        }
        if (compareVal < 0) {
            // less than the roots value, so in the left subtree
            return this.find(toFind, root.left);
        }
        return this.find(toFind, root.right);
    }

    /**
     * Returns the AVL balance factor of the node. That is, 0 if the node is null, or the height of
     * the left subtree minus the height of the right subtree. This should be -1, 0, or 1 unless the
     * node needs to be rebalanced.
     *
     * @see #getHeight(BinaryTreeNode)
     * @param node the node to find the balance of
     * @return the nodes AVL balance factor
     */
    protected int getBalance(BinaryTreeNode<T> node) {
        if (node == null) {
            return 0;
        }
        return this.getHeight(node.left) - this.getHeight(node.right);
    }

    /**
     * Returns the height of the node, or -1 if it is null.
     *
     * @param node the node to get the height of
     * @return the tree's (with node as the root) maximum height
     */
    protected int getHeight(BinaryTreeNode<T> node) {
        if (node == null) {
            return -1;
        }
        return node.height;
    }

    /**
     * Returns the smallest child node of the tree that has root as its parent. If root is null it
     * just returns null. If root has no children, it returns itself.
     *
     * @param root the root of the tree to search
     * @return the smallest node in that subtree
     */
    protected BinaryTreeNode<T> getSmallestSubnode(BinaryTreeNode<T> root) {
        if (root == null) {
            return null;
        }
        var node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Inserts an object into the tree using recursive calls, with a root at theRoot. Returns the
     * new root of the tree. The root of the tree should be passed as theRoot and then set to the
     * return value.
     *
     * @param theRoot the root of the tree to insert into
     * @param ins the value to insert
     * @return the new root of the tree (where theRoot was before)
     * @throws DuplicateEntry if the entry already exists in the tree
     */
    protected BinaryTreeNode<T> insert(BinaryTreeNode<T> theRoot, T ins) throws DuplicateEntry {
        if (treeRoot == null) {
            treeRoot = new BinaryTreeNode<>(ins, null, null, null);
            return treeRoot;
        }

        if (theRoot == null || theRoot.key == null) {
            return new BinaryTreeNode<>(ins, null, null, null);
        }

        // insert node, looking for it recursively
        if (ins.compareTo(theRoot.key) == 0) {
            throw new DuplicateEntry(theRoot.key.toString());
        } else if (ins.compareTo(theRoot.key) < 1) {
            // inserting on the left of this node
            theRoot.left = this.insert(theRoot.left, ins);
            theRoot.left.parent = theRoot;
        } else {
            // inserting on the right of this node
            theRoot.right = this.insert(theRoot.right, ins);
            theRoot.right.parent = theRoot;
        }

        theRoot.height = Math.max(this.getHeight(theRoot.left), this.getHeight(theRoot.right)) + 1;

        int balance = this.getBalance(theRoot);

        // if it is unbalanced, handle the 4 special cases

        // single right
        if (theRoot.left != null && theRoot.left.key != null) {
            if (balance > 1 && ins.compareTo(theRoot.left.key) < 0) {
                BinaryTreeNode<T> newRoot = this.rightRotate(theRoot);
                if (newRoot.parent == null) {
                    treeRoot = newRoot;
                }
                return newRoot;
            }
            // double right
            if (balance > 1 && ins.compareTo(theRoot.left.key) > 0) {
                theRoot.left = this.leftRotate(theRoot.left);
                BinaryTreeNode<T> newRoot = this.rightRotate(theRoot);
                if (newRoot.parent == null) {
                    treeRoot = newRoot;
                }
                return newRoot;
            }
        }
        // single left
        if (theRoot.right != null && theRoot.right.key != null) {
            if (balance < -1 && ins.compareTo(theRoot.right.key) > 0) {
                BinaryTreeNode<T> newRoot = this.leftRotate(theRoot);
                if (newRoot.parent == null) {
                    treeRoot = newRoot;
                }
                return newRoot;
            }
            // double left
            if (balance < -1 && ins.compareTo(theRoot.right.key) < 0) {
                theRoot.right = this.rightRotate(theRoot.right);
                BinaryTreeNode<T> newRoot = this.leftRotate(theRoot);
                if (newRoot.parent == null) {
                    treeRoot = newRoot;
                }
                return newRoot;
            }
        }

        return theRoot; // No rebalancing needed
    }

    /**
     * Inserts the given value into the tree.
     *
     * @param toInsert the object to store
     * @throws DuplicateEntry if the entry already exists in the tree
     */
    public void insert(T toInsert) throws DuplicateEntry {
        treeRoot = this.insert(treeRoot, toInsert);
        ++size;
    }

    /**
     * Rotates the given node left and returns the new root that is where root used to be. Updates
     * heights of nodes and parents appropriately. If the root's right child is null then it just
     * returns the root because it can't rotate.
     *
     * @param root the root of the three nodes to rotate
     * @return the new root
     */
    protected BinaryTreeNode<T> leftRotate(BinaryTreeNode<T> root) {
        var rightChild = root.right;
        if (rightChild == null) {
            return root;
        }
        var rightsLeftChild = rightChild.left;
        var parent = root.parent;

        // Change root.parents reference to root, to root.left
        if (parent != null) {
            if (parent.left == root) {
                parent.left = rightChild;
            } else if (parent.right == root) {
                parent.right = rightChild;
            }
        }

        // rotate
        rightChild.left = root;
        rightChild.parent = parent;
        root.parent = rightChild;
        root.right = rightsLeftChild;
        if (rightsLeftChild != null) {
            rightsLeftChild.parent = root;
        }

        this.updateHeight(Objects.requireNonNullElse(rightsLeftChild, root));

        return rightChild;
    }

    /**
     * Rebalances the tree starting at the given the lowest possibly unbalanced node and working up
     * the path to the root.
     *
     * @param lowest the lowest node in the tree that might be unbalanced or was changed
     */
    protected void rebalance(BinaryTreeNode<T> lowest) {
        if (lowest == null) {
            return;
        }
        int balance = this.getBalance(lowest);
        var newLowest = lowest;
        if (balance > 1) {
            // left is too big
            newLowest = this.rightRotate(lowest);
        } else if (balance < -1) {
            // right is too big
            newLowest = this.leftRotate(lowest);
        }
        this.rebalance(newLowest.parent);
    }

    /**
     * Removes the given node from the tree. This will nullify the pointer to toRemove.
     *
     * @param toRemove the node to remove
     */
    protected void remove(BinaryTreeNode<T> toRemove) {
        if (toRemove == null) {
            return;
        }

        var parent = toRemove.parent;

        // There is a child, but only one child.
        if ((toRemove.left == null) != (toRemove.right == null)) {
            BinaryTreeNode<T> child =
                    Objects.requireNonNullElseGet(toRemove.left, () -> toRemove.right);
            child.parent = toRemove.parent;

            if (parent == null) {
                // this is the root node
                treeRoot = child;
                this.updateHeight(treeRoot);
            } else if (toRemove == parent.left) {
                parent.left = child;
                this.updateHeight(child);
            } else if (toRemove == parent.right) {
                parent.right = child;
                this.updateHeight(child);
            }
            this.rebalance(child);
            return;
        }
        /*
         * No children, just delete this node.
         */
        if (toRemove.left == null) {
            if (parent == null) {
                // this is the root node, and the only node in the tree
                treeRoot = null;
            } else if (toRemove == parent.left) {
                // it's a left child
                parent.left = null;
                this.updateHeight(parent);
                this.rebalance(parent);
            } else if (parent.right == toRemove) {
                parent.right = null;
                this.updateHeight(parent);
                parent = null;
                this.rebalance(parent);
            }
            return;
        }
        /*
         * There are two children Because the null status of both children is
         * the same and the children can't be null because of the previous else
         * in the if-else chain
         */
        var smallestRight = this.getSmallestSubnode(toRemove.right);
        var newParent = smallestRight.parent;
        if (parent == null) {
            // this is the root node
            if (newParent.left == smallestRight) {
                newParent.left = null;
            } else if (newParent.right == smallestRight) {
                newParent.right = null;
            }
            if (toRemove.left != null && toRemove.left != smallestRight) {
                toRemove.left.parent = smallestRight;
            }
            if (toRemove.right != null && toRemove.right != smallestRight) {
                toRemove.right.parent = smallestRight;
            }
            if (toRemove.left != smallestRight) {
                smallestRight.left = toRemove.left;
            }
            if (toRemove.right != smallestRight) {
                smallestRight.right = toRemove.right;
            }

            treeRoot = smallestRight;
        } else if (parent.left == toRemove) {
            // this is a left child
            if (newParent.left == smallestRight) {
                newParent.left = null;
            } else if (newParent.right == smallestRight) {
                newParent.right = null;
            }
            if (toRemove.left != null && toRemove.left != smallestRight) {
                toRemove.left.parent = smallestRight;
            }
            if (toRemove.right != null && toRemove.right != smallestRight) {
                toRemove.right.parent = smallestRight;
            }

            parent.left = smallestRight;
            if (toRemove.left != smallestRight) {
                smallestRight.left = toRemove.left;
            }
            if (toRemove.right != smallestRight) {
                smallestRight.right = toRemove.right;
            }
        } else if (parent.right == toRemove) {
            // this is a right child
            if (toRemove.left != null) {
                toRemove.left.parent = smallestRight;
            }
            if (toRemove.right != null) {
                toRemove.right.parent = smallestRight;
            }

            if (newParent.left == smallestRight) {
                newParent.left = null;
            } else if (newParent.right == smallestRight) {
                newParent.right = null;
            }

            parent.right = smallestRight;

            if (toRemove.left != smallestRight) {
                smallestRight.left = toRemove.left;
            }
            if (toRemove.right != smallestRight) {
                smallestRight.right = toRemove.right;
            }
        }

        // update heights and balance
        if (parent != null) {
            this.updateHeight(parent);
            this.rebalance(parent);
        } else {
            this.updateHeight(smallestRight);
            this.rebalance(smallestRight);
        }
    }

    /**
     * Removes the object form the tree if it exists.
     *
     * @param toRemove the object to remove
     */
    public void remove(T toRemove) {
        var nodeToRemove = this.find(toRemove, treeRoot);
        if (nodeToRemove != null) {
            this.remove(nodeToRemove);
        }
        --size;
    }

    /**
     * Rotates the given node right and returns the new root that is where root used to be. Updates
     * heights of nodes and parents appropriately. If the root's left child is null, it just returns
     * the root because it can't rotate.
     *
     * @param root the root of the three nodes to rotate
     * @return the new root
     */
    protected BinaryTreeNode<T> rightRotate(BinaryTreeNode<T> root) {
        var leftChild = root.left;
        if (leftChild == null) {
            return root;
        }
        var leftsRightChild = leftChild.right;

        var parent = root.parent;

        // Change root.parents reference to root to the root.left
        if (parent != null) {
            if (parent.left == root) {
                parent.left = leftChild;
            } else if (parent.right == root) {
                parent.right = leftChild;
            }
        }

        // rotate
        leftChild.right = root;
        leftChild.parent = parent;
        root.parent = leftChild;
        root.left = leftsRightChild;
        if (leftsRightChild != null) {
            leftsRightChild.parent = root;
        }

        this.updateHeight(Objects.requireNonNullElse(leftsRightChild, root));

        return leftChild;
    }

    /**
     * Recursively updates the heights of the parents of this node
     *
     * @param changed the lowest node that needs to be updated
     */
    protected void updateHeight(BinaryTreeNode<T> changed) {
        if (changed == null) {
            return;
        }
        changed.height = Math.max(this.getHeight(changed.left), this.getHeight(changed.right)) + 1;
        if (changed.parent != null) {
            this.updateHeight(changed.parent);
        }
    }
}
