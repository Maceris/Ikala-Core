package com.ikalagaming.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * A binary AVL tree for storing (comparable) objects. This is not synchronized,
 * so synchronization must be done externally.
 *
 * @author Ches Burks
 *
 * @param <K> The type of keys used. Must extend {@link Comparable}
 * @param <V> The values to store.
 */
public class AVLTreeMap<K extends Comparable<K>, V> {

	/**
	 * A binary tree node used in the {@link BinaryTree} class.
	 *
	 * @author Ches Burks
	 * @param <K> The key type for indexing.
	 * @param <V> The value stored.
	 */
	@Getter
	@Setter
	@SuppressWarnings("javadoc")
	static class BinaryTreeNode<K extends Comparable<K>, V> {
		/**
		 * The key for this node.
		 *
		 * @return The key.
		 * @param key The new key.
		 */
		private K key;

		/**
		 * The value for this node.
		 *
		 * @return The value.
		 * @param value The new value.
		 */
		private V value;

		/**
		 * Returns the height of the largest subtree starting at this node.
		 * Stored in the node for speed.
		 *
		 * @return This nodes height.
		 * @param height The new height of the node.
		 */
		private int height;

		/**
		 * A pointer to the left child. Null if there is no left child.
		 *
		 * @return The left node, or null.
		 * @param left The new left node.
		 */
		private BinaryTreeNode<K, V> left;

		/**
		 * A pointer to the right child. Null if there is no right child.
		 *
		 * @return The right node or null.
		 * @param right The new right node.
		 */
		private BinaryTreeNode<K, V> right;

		/**
		 * A pointer to the parent node. The parent should be null if and only
		 * if it is the root of the tree.
		 *
		 * @return The parent node, or null if it's the root of the tree.
		 * @param parent The new parent.
		 */
		private BinaryTreeNode<K, V> parent;

		/**
		 * Constructs a new BinaryTree node for the given object with null
		 * pointers for left, right, and parent.
		 *
		 * @param newKey The key to store.
		 * @param newValue The value to store.
		 */
		public BinaryTreeNode(K newKey, V newValue) {
			this(newKey, newValue, null, null, null);
		}

		/**
		 * Constructs a new BinaryTree node for the given object and
		 * children/parent. The other nodes may be null. Height defaults to 1.
		 *
		 * @param newKey The key to store.
		 * @param newValue The value to store.
		 * @param leftNode The left node.
		 * @param parentNode The parent node.
		 * @param rightNode The right node.
		 */
		public BinaryTreeNode(K newKey, V newValue,
			BinaryTreeNode<K, V> leftNode, BinaryTreeNode<K, V> parentNode,
			BinaryTreeNode<K, V> rightNode) {
			this.key = newKey;
			this.value = newValue;
			this.height = 1;
			this.left = leftNode;
			this.parent = parentNode;
			this.right = rightNode;
		}

		/**
		 * Recursively calls delete on all children then removes all references
		 * to the children, parents and keys. Also zeroes the height.
		 */
		public void delete() {
			if (this.left != null) {
				this.left.delete();
			}
			this.left = null;
			if (this.right != null) {
				this.right.delete();
			}
			this.right = null;
			this.parent = null;
			this.key = null;
			this.height = 0;
		}
	}

	/**
	 * The base of the tree. This is null if the tree is empty.
	 *
	 * @return The root node of the tree.
	 */
	@Getter(value = AccessLevel.PACKAGE)
	private BinaryTreeNode<K, V> treeRoot;

	/**
	 * How many elements are stored in the tree.
	 *
	 * @return The number of elements in the tree.
	 */
	@SuppressWarnings("javadoc")
	@Getter
	private int size;

	/**
	 * Removes all objects from the tree.
	 */
	public void clear() {
		if (this.treeRoot != null) {
			this.treeRoot.delete();
		}
		this.treeRoot = null;
		this.size = 0;
	}

	/**
	 * Returns true if the tree contains the given object
	 *
	 * @param toFind the entry to search for
	 * @return true if the object exists or false if it is not in the tree
	 */
	public boolean contains(@NonNull K toFind) {
		/*
		 * true if the find method returns something that is not null (that is,
		 * it exists in the tree)
		 */
		return (this.find(toFind, this.treeRoot) != null);
	}

	/**
	 * Recursively searches for the entry and returns the node that contains it
	 * or null if it is not found.
	 *
	 * @param toFind the entry to search for
	 * @param root where to start looking
	 * @return null or the node that has toFind as its key
	 */
	protected BinaryTreeNode<K, V> find(@NonNull K toFind,
		BinaryTreeNode<K, V> root) {
		if (root == null || root.getKey() == null) {
			return null;
		}
		int compareVal = toFind.compareTo(root.getKey());
		if (compareVal == 0) {
			// they match
			return root;
		}
		else if (compareVal < 0) {
			// less than the roots value, so in the left subtree
			return this.find(toFind, root.getLeft());
		}
		else if (compareVal > 0) {
			// greater than the roots value, so in the right subtree
			return this.find(toFind, root.getRight());
		}
		// won't reach here but just in case.
		return null;
	}

	/**
	 * Returns the AVL balance factor of the node. That is, 0 if the node is
	 * null, or the height of the left subtree minus the height of the right
	 * subtree. This should be -1, 0, or 1 unless the node needs to be
	 * re-balanced.
	 *
	 * @see #getHeight(BinaryTreeNode)
	 * @param node the node to find the balance of
	 * @return the nodes AVL balance factor
	 */
	int getBalance(BinaryTreeNode<K, V> node) {
		if (node == null) {
			return 0;
		}
		return this.getHeight(node.getLeft()) - this.getHeight(node.getRight());
	}

	/**
	 * Returns the height of the node, or -1 if it is null.
	 *
	 * @param node The node to get the height of.
	 * @return The tree's (with node as the root) maximum height.
	 */
	protected int getHeight(BinaryTreeNode<K, V> node) {
		if (node == null) {
			return 0;
		}
		return node.getHeight();
	}

	/**
	 * Returns the smallest child node of the tree that has root as its parent.
	 * If root is null it just returns null. If root has no children, it returns
	 * itself.
	 *
	 * @param root The root of the tree to search.
	 * @return The smallest node in that subtree.
	 */
	protected BinaryTreeNode<K, V>
		getSmallestSubnode(BinaryTreeNode<K, V> root) {
		if (root == null) {
			return null;
		}
		BinaryTreeNode<K, V> node = root;
		while (node.getLeft() != null) {
			node = node.getLeft();
		}
		return node;
	}

	/**
	 * Inserts an object into the tree using recursive calls, with a root at
	 * theRoot. Returns the new root of the tree. The root of the tree should be
	 * passed as theRoot and then set to the return value.
	 *
	 * If the key is already in the tree, updates the value.
	 *
	 * @param root The root of the tree to insert into.
	 * @param key The key to insert.
	 * @param value The value to insert.
	 * @return The new root of the tree (where root was before).
	 */
	protected BinaryTreeNode<K, V> insert(BinaryTreeNode<K, V> root, K key,
		V value) {
		if (this.treeRoot == null) {
			this.treeRoot = new BinaryTreeNode<>(key, value);
			return this.treeRoot;
		}

		if (root == null || root.getKey() == null) {
			return new BinaryTreeNode<>(key, value);
		}

		// insert node, looking for it recursively
		if (key.compareTo(root.getKey()) == 0) {
			root.setValue(value);
			--this.size;// We did not add a node, but will later think we did
			return root;
		}
		else if (key.compareTo(root.getKey()) < 1) {
			// inserting on the left of this node
			root.setLeft(this.insert(root.getLeft(), key, value));
			root.getLeft().setParent(root);
		}
		else {
			// inserting on the right of this node
			root.setRight(this.insert(root.getRight(), key, value));
			root.getRight().setParent(root);
		}

		// update the height
		/*
		 * recalculate the height of the left node. this will be called on each
		 * parent until the root because this is a recursive function and this
		 * occurs after the recursive function call, so it will update the
		 * height appropriately
		 */
		root.setHeight(this.max(this.getHeight(root.getLeft()),
			this.getHeight(root.getRight())) + 1);

		long balance = this.getBalance(root);

		// if it is unbalanced, handle the 4 special cases

		/*
		 * single right, because its larger on the left and inserting on
		 * left.left
		 */
		if (root.getLeft() != null && root.getLeft().getKey() != null) {
			if (balance > 1 && key.compareTo(root.getLeft().getKey()) < 0) {
				BinaryTreeNode<K, V> newRoot = this.rightRotate(root);
				if (newRoot.getParent() == null) {
					this.treeRoot = newRoot;
				}
				return newRoot;
			}
			/*
			 * double right, because its larger on the left and inserting on
			 * left.right
			 */
			if (balance > 1 && key.compareTo(root.getLeft().getKey()) > 0) {
				root.setLeft(this.leftRotate(root.getLeft()));
				BinaryTreeNode<K, V> newRoot = this.rightRotate(root);
				if (newRoot.getParent() == null) {
					this.treeRoot = newRoot;
				}
				return newRoot;
			}
		}
		/*
		 * single left, because its larger on the right and its inserting on
		 * right.right
		 */
		if (root.getRight() != null && root.getRight().getKey() != null) {
			if (balance < -1 && key.compareTo(root.getRight().getKey()) > 0) {
				BinaryTreeNode<K, V> newRoot = this.leftRotate(root);
				if (newRoot.getParent() == null) {
					this.treeRoot = newRoot;
				}
				return newRoot;
			}
			// double left
			if (balance < -1 && key.compareTo(root.getRight().getKey()) > 0) {
				root.setRight(this.rightRotate(root.getRight()));
				BinaryTreeNode<K, V> newRoot = this.leftRotate(root);
				if (newRoot.getParent() == null) {
					this.treeRoot = newRoot;
				}
				return newRoot;
			}
		}
		return root;// No rebalancing needed
	}

	/**
	 * Inserts the given value into the tree. If the key is already in the tree,
	 * updates the value.
	 *
	 * @param key The key to store.
	 * @param value The value to store.
	 *
	 */
	public void insert(K key, V value) {
		this.treeRoot = this.insert(this.treeRoot, key, value);
		++this.size;
	}

	/**
	 * Rotates the given node left and returns the new root that is where root
	 * used to be. Updates heights of nodes and parents appropriately. If the
	 * root's right child is null then it just returns the root because it can't
	 * rotate.
	 *
	 * @param root the root of the three nodes to rotate
	 * @return the new root
	 *
	 */
	protected BinaryTreeNode<K, V> leftRotate(BinaryTreeNode<K, V> root) {
		BinaryTreeNode<K, V> rightChild = root.getRight();
		if (rightChild == null) {
			return root;
		}
		BinaryTreeNode<K, V> rightsLeftChild = rightChild.getLeft();

		// Change root.parents reference to root to root.left
		if (root.getParent() != null) {
			if (root.getParent().getLeft() == root) {
				root.getParent().setLeft(rightChild);
			}
			else if (root.equals(root.getParent().getRight())) {
				root.getParent().setRight(rightChild);
			}
		}

		// rotate
		rightChild.setLeft(root);
		rightChild.setParent(root.getParent());
		root.setParent(rightChild);
		root.setRight(rightsLeftChild);
		if (rightsLeftChild != null) {
			rightsLeftChild.setParent(root);
		}

		// update height, starting with the smallest changed child
		if (rightsLeftChild != null) {
			this.updateHeight(rightsLeftChild);
		}
		else {
			this.updateHeight(root);
		}

		return rightChild;
	}

	/**
	 * Returns the larger of the two numbers.
	 *
	 * @param a the first number
	 * @param b the second number
	 * @return whichever number is largest
	 */
	protected int max(int a, int b) {
		/*
		 * If a is less than b, return b. otherwise, return a.
		 */
		return (a < b) ? b : a;
	}

	/**
	 * Rebalances the tree starting at the given the lowest possibly unbalanced
	 * node and working up the path to the root.
	 *
	 * @param lowest the lowest node in the tree that might be unbalance or was
	 *            changed
	 */
	protected void rebalance(BinaryTreeNode<K, V> lowest) {
		if (lowest == null) {
			return;
		}
		long balance = this.getBalance(lowest);
		// single right, because its larger on the left and inserting on
		// left.left
		BinaryTreeNode<K, V> newLowest = lowest;
		if (balance > 1) {
			// left is too big
			newLowest = this.rightRotate(lowest);
		}
		else if (balance < -1) {
			// right is too big
			newLowest = this.leftRotate(lowest);
		}
		this.rebalance(newLowest.getParent());
	}

	/**
	 * Removes the given node from the tree.
	 *
	 * @param toRemove the node to remove
	 */
	protected void remove(BinaryTreeNode<K, V> toRemove) {
		if (toRemove == null) {
			return;// just ignore it
		}

		// If and only if only one child is not null. (a logical exclusive or)
		// Basically, there is a child but only one child.
		if ((toRemove.getLeft() == null) != (toRemove.getRight() == null)) {
			this.removeWithSingleChild(toRemove);
		}
		/*
		 * No children, just delete this node. this works because the null
		 * status of both children is the same, so only checking one is fine
		 * (and faster)
		 */
		else if (toRemove.getLeft() == null) {
			this.removeWithNoChildren(toRemove);
		}
		// there are two children
		// Because the null status of both children is the same
		// and the children cant be null because of the previous else in the
		// if-else chain
		else {
			this.removeWithTwoChildren(toRemove);
		}
		// ensure the element to remove is gone
		toRemove.delete();
	}

	/**
	 * Removes the object form the tree if it exists.
	 *
	 * @param toRemove the object to remove
	 */
	public void remove(@NonNull K toRemove) {
		BinaryTreeNode<K, V> nodeToRemove = this.find(toRemove, this.treeRoot);
		if (nodeToRemove != null) {
			this.remove(nodeToRemove);
		}
		--this.size;
	}

	/**
	 * Used to remove a node that has two children, where the node to remove is
	 * a left child.
	 *
	 * @param toRemove The node to remove.
	 * @param smallestRight The smallest subnode that is larger than the one we
	 *            are removing.
	 * @see #removeWithTwoChildren(BinaryTreeNode)
	 */
	private void removeAsLeftChild(BinaryTreeNode<K, V> toRemove,
		BinaryTreeNode<K, V> smallestRight) {
		if (smallestRight.equals(smallestRight.getParent().getLeft())) {
			smallestRight.getParent().setLeft(null);
		}
		else if (smallestRight.equals(smallestRight.getParent().getRight())) {
			smallestRight.getParent().setRight(null);
		}
		if (toRemove.getLeft() != null
			&& !toRemove.getLeft().equals(smallestRight)) {
			toRemove.getLeft().setParent(smallestRight);
		}
		if (toRemove.getRight() != null
			&& !toRemove.getRight().equals(smallestRight)) {
			toRemove.getRight().setParent(smallestRight);
		}

		toRemove.getParent().setLeft(smallestRight);
		if (!smallestRight.equals(toRemove.getLeft())) {
			smallestRight.setLeft(toRemove.getLeft());
		}
		if (!smallestRight.equals(toRemove.getRight())) {
			smallestRight.setRight(toRemove.getRight());
		}
		smallestRight.setParent(toRemove.getParent());
		toRemove.setParent(null);
		toRemove.setLeft(null);
		toRemove.setRight(null);
	}

	/**
	 * Used to remove a node that has two children, where the node to remove is
	 * a right node.
	 *
	 * @param toRemove The node to remove.
	 * @param smallestRight The smallest subnode that is larger than the one we
	 *            are removing.
	 * @see #removeWithTwoChildren(BinaryTreeNode)
	 */
	private void removeAsRightNode(BinaryTreeNode<K, V> toRemove,
		BinaryTreeNode<K, V> smallestRight) {
		if (toRemove.getLeft() != null) {
			toRemove.getLeft().setParent(smallestRight);
		}
		if (toRemove.getRight() != null) {
			toRemove.getRight().setParent(smallestRight);
		}

		if (smallestRight.equals(smallestRight.getParent().getLeft())) {
			smallestRight.getParent().setLeft(null);
		}
		else if (smallestRight.equals(smallestRight.getParent().getRight())) {
			smallestRight.getParent().setRight(null);
		}

		toRemove.getParent().setRight(smallestRight);

		if (!smallestRight.equals(toRemove.getLeft())) {
			smallestRight.setLeft(toRemove.getLeft());
		}
		if (!smallestRight.equals(toRemove.getRight())) {
			smallestRight.setRight(toRemove.getRight());
		}
		smallestRight.setParent(toRemove.getParent());
		toRemove.setParent(null);
		toRemove.setLeft(null);
		toRemove.setRight(null);
	}

	/**
	 * Used to remove a node that has two children, where the node to remove is
	 * the root node.
	 *
	 * @param toRemove The node to remove.
	 * @param smallestRight The smallest subnode that is larger than the one we
	 *            are removing.
	 * @see #removeWithTwoChildren(BinaryTreeNode)
	 */
	private void removeAsRootNode(BinaryTreeNode<K, V> toRemove,
		BinaryTreeNode<K, V> smallestRight) {
		if (smallestRight.equals(smallestRight.getParent().getLeft())) {
			smallestRight.getParent().setLeft(null);
		}
		else if (smallestRight.equals(smallestRight.getParent().getRight())) {
			smallestRight.getParent().setRight(null);
		}
		if (toRemove.getLeft() != null
			&& !toRemove.getLeft().equals(smallestRight)) {
			toRemove.getLeft().setParent(smallestRight);
		}
		if (toRemove.getRight() != null
			&& !toRemove.getRight().equals(smallestRight)) {
			toRemove.getRight().setParent(smallestRight);
		}
		if (!smallestRight.equals(toRemove.getLeft())) {
			smallestRight.setLeft(toRemove.getLeft());
		}
		if (!smallestRight.equals(toRemove.getRight())) {
			smallestRight.setRight(toRemove.getRight());
		}
		smallestRight.setParent(null);

		toRemove.setParent(null);
		toRemove.setLeft(null);
		toRemove.setRight(null);

		this.treeRoot = smallestRight;
	}

	/**
	 * Used to remove a node that has no children.
	 *
	 * @param toRemove The node to remove.
	 * @see #remove(BinaryTreeNode)
	 */
	private void removeWithNoChildren(BinaryTreeNode<K, V> toRemove) {
		if (toRemove.getParent() == null) {
			// this is the root node, and the only node in the tree
			this.treeRoot = null;
		}
		else if (toRemove.equals(toRemove.getParent().getLeft())) {
			// its a left child
			// remove parents reference to this node
			toRemove.getParent().setLeft(null);
			this.updateHeight(toRemove.getParent());
			BinaryTreeNode<K, V> parent = toRemove.getParent();
			toRemove.setParent(null);
			this.rebalance(parent);

		}
		else if (toRemove.equals(toRemove.getParent().getRight())) {
			// remove parents reference to this node
			toRemove.getParent().setRight(null);
			this.updateHeight(toRemove.getParent());
			BinaryTreeNode<K, V> parent = toRemove.getParent();
			toRemove.setParent(null);
			this.rebalance(parent);
		}
	}

	/**
	 * Used to remove a node that has a single child.
	 *
	 * @param toRemove The node to remove.
	 * @see #remove(BinaryTreeNode)
	 */
	private void removeWithSingleChild(BinaryTreeNode<K, V> toRemove) {
		// set this nodes child's parent pointer to this nodes parent
		// if its null then those are the new root.
		BinaryTreeNode<K, V> child;
		if (toRemove.getLeft() != null) {
			// its the left child
			child = toRemove.getLeft();
			toRemove.getLeft().setParent(toRemove.getParent());
		}
		else {
			// its the right child
			child = toRemove.getRight();
			toRemove.getRight().setParent(toRemove.getParent());
		}

		if (toRemove.getParent() == null) {
			// this is the root node
			this.treeRoot = child;
			this.updateHeight(this.treeRoot);
		}
		else if (toRemove.equals(toRemove.getParent().getLeft())) {
			// this is a left child
			toRemove.getParent().setLeft(child);
			child.setParent(toRemove.getParent());
			this.updateHeight(child);
		}
		else if (toRemove.equals(toRemove.getParent().getRight())) {
			// this is a right child
			toRemove.getParent().setRight(child);
			child.setParent(toRemove.getParent());
			this.updateHeight(child);
		}
		this.rebalance(child);
	}

	/**
	 * Used to remove a node that has two children.
	 *
	 * @param toRemove The node to remove.
	 * @see #remove(BinaryTreeNode)
	 */
	private void removeWithTwoChildren(BinaryTreeNode<K, V> toRemove) {
		BinaryTreeNode<K, V> smallestRight =
			this.getSmallestSubnode(toRemove.getRight());
		BinaryTreeNode<K, V> parent = smallestRight.getParent();
		if (toRemove.getParent() == null) {
			// this is the root node
			this.removeAsRootNode(toRemove, smallestRight);
		}
		else if (toRemove.equals(toRemove.getParent().getLeft())) {
			// this is a left child
			this.removeAsLeftChild(toRemove, smallestRight);
		}
		else if (toRemove.equals(toRemove.getParent().getRight())) {
			// this is a right child
			this.removeAsRightNode(toRemove, smallestRight);
		}

		// update heights and balance
		if (parent != null) {
			this.updateHeight(parent);
			this.rebalance(parent);
		}
		else {
			this.updateHeight(smallestRight);
			this.rebalance(smallestRight);
		}
	}

	/**
	 * Rotates the given node right and returns the new root that is where root
	 * used to be. Updates heights of nodes and parents appropriately. If the
	 * root's left child is null, it just returns the root because it can't
	 * rotate.
	 *
	 * @param root the root of the three nodes to rotate
	 * @return the new root
	 */
	protected BinaryTreeNode<K, V> rightRotate(BinaryTreeNode<K, V> root) {
		BinaryTreeNode<K, V> leftChild = root.getLeft();
		if (leftChild == null) {
			return root;
		}
		BinaryTreeNode<K, V> leftsRightChild = leftChild.getRight();

		// Change root.parents reference to root to the root.left
		if (root.getParent() != null) {
			if (root.equals(root.getParent().getLeft())) {
				root.getParent().setLeft(leftChild);
			}
			else if (root.equals(root.getParent().getRight())) {
				root.getParent().setRight(leftChild);
			}
		}

		// rotate
		leftChild.setRight(root);
		leftChild.setParent(root.getParent());
		root.setParent(leftChild);
		root.setLeft(leftsRightChild);
		if (leftsRightChild != null) {
			leftsRightChild.setParent(root);
		}

		// update height, starting with the smallest changed child
		if (leftsRightChild != null) {
			this.updateHeight(leftsRightChild);
		}
		else {
			this.updateHeight(root);
		}

		return leftChild;
	}

	/**
	 * Recursively updates the heights of the parents of this node
	 *
	 * @param changed the lowest node that needs to be updated
	 */
	protected void updateHeight(BinaryTreeNode<K, V> changed) {
		if (changed == null) {
			return;
		}
		int height = this.max(this.getHeight(changed.getLeft()),
			this.getHeight(changed.getRight())) + 1;
		changed.setHeight(height);
		if (changed.getParent() != null) {
			this.updateHeight(changed.getParent());
		}
	}
}
