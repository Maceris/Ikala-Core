package com.ikalagaming.util;

/**
 * A binary tree node used in the {@link BinaryTree} class.
 *
 * @author Ches Burks
 * @param <T> the Type of object this node holds
 *
 */
public class BinaryTreeNode<T extends Comparable<T>> {
	/**
	 * The value stored in this node.
	 */
	protected T key;
	/**
	 * The height of the largest subtree that starts at this node.
	 */
	protected int height;
	/**
	 * A pointer to the left child. Null if there is no left child.
	 */
	protected BinaryTreeNode<T> left;
	/**
	 * A pointer to the right child. Null if there is no right child.
	 */
	protected BinaryTreeNode<T> right;
	/**
	 * A pointer to the parent node. The parent should be null if and only if it
	 * is the root of the tree.
	 */
	protected BinaryTreeNode<T> parent;

	/**
	 * Constructs a new BinaryTree node for the given object with null pointers
	 * for left, right, and parent.
	 *
	 * @param newKey the key to store
	 */
	public BinaryTreeNode(T newKey) {
		this(newKey, null, null, null);
	}

	/**
	 * Constructs a new BinaryTree node for the given object and
	 * children/parent. The other nodes may be null. Height defaults to 1.
	 *
	 * @param newKey the key to store
	 * @param leftNode the left node
	 * @param parentNode the parent node
	 * @param rightNode the right node
	 */
	public BinaryTreeNode(T newKey, BinaryTreeNode<T> leftNode,
			BinaryTreeNode<T> parentNode, BinaryTreeNode<T> rightNode) {
		this.key = newKey;
		this.height = 1;
		this.left = leftNode;
		this.parent = parentNode;
		this.right = rightNode;
	}

	/**
	 * Recursively calls delete on all children then removes all references to
	 * the children, parents and keys. Also zeroes the height.
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

	/**
	 * Dereferences all other objects this object has pointers to. This will
	 * cause any children to be cleaned up as well.
	 */
	@Override
	protected void finalize() throws Throwable {
		this.left = null;
		this.right = null;
		this.parent = null;
		this.key = null;
		this.height = 0;
		super.finalize();
	}

	/**
	 * Returns the height of the largest subtree starting at this node. Stored
	 * in the node for speed.
	 *
	 * @return this nodes height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Returns the key stored in this node.
	 *
	 * @return the key
	 */
	public T getKey() {
		return this.key;
	}

	/**
	 * Returns the left child of this node. This will be null if there is no
	 * left child.
	 *
	 * @return the left child, or null
	 */
	public BinaryTreeNode<T> getLeft() {
		return this.left;
	}

	/**
	 * Returns the parent of this node. This will be null if this node is the
	 * root of the tree.
	 *
	 * @return the parent, or null
	 */
	public BinaryTreeNode<T> getParent() {
		return this.parent;
	}

	/**
	 * Returns the right child of this node. This will be null if there is no
	 * right child.
	 *
	 * @return the right child, or null
	 */
	public BinaryTreeNode<T> getRight() {
		return this.right;
	}

	/**
	 * Sets the height of this node.
	 *
	 * @param newHeight the new height of this node
	 */
	public void setHeight(int newHeight) {
		this.height = newHeight;
	}

	/**
	 * Set the key value stored in this node.
	 *
	 * @param newKey the new key
	 */
	public void setKey(T newKey) {
		this.key = newKey;
	}

	/**
	 * Sets the left child of this node to the supplied node.
	 *
	 * @param newLeft the new left child
	 */
	public void setLeft(BinaryTreeNode<T> newLeft) {
		this.left = newLeft;
	}

	/**
	 * Sets the parent of this node to the supplied node.
	 *
	 * @param newParent the new parent
	 */
	public void setParent(BinaryTreeNode<T> newParent) {
		this.parent = newParent;
	}

	/**
	 * Sets the right child of this node to the supplied node.
	 *
	 * @param newRight the new right child
	 */
	public void setRight(BinaryTreeNode<T> newRight) {
		this.right = newRight;
	}

}
