package com.ikalagaming.util;

/**
 * A binary tree node used in the {@link BinaryTree} class.
 * 
 * @author Ches Burks
 * @param <T> the Type of object this node holds
 *
 */
public class BinaryTreeNode<T extends Comparable<T>> {
	protected T key;
	protected int height;
	protected BinaryTreeNode<T> left;
	protected BinaryTreeNode<T> right;
	protected BinaryTreeNode<T> parent;

	/**
	 * Constructs a new BinaryTree node for the given object and
	 * children/parent. The other nodes may be null. Height defaults to 1.
	 * 
	 * @param key the key to store
	 * @param left the left node
	 * @param parent the parent node
	 * @param right the right node
	 */
	public BinaryTreeNode(T key, BinaryTreeNode<T> left,
			BinaryTreeNode<T> parent, BinaryTreeNode<T> right) {
		this.key = key;
		this.height = 1;
		this.left = left;
		this.parent = parent;
		this.right = right;
	}

	/**
	 * Dereferences all other objects this object has pointers to. This will
	 * cause any children to be cleaned up as well.
	 */
	@Override
	protected void finalize() throws Throwable {
		left = null;
		right = null;
		parent = null;
		key = null;
		height = 0;
		super.finalize();
	}

	/**
	 * Recursively calls delete on all children then removes all references to
	 * the children, parents and keys. Also zeroes the height.
	 */
	public void delete() {
		if (left != null) {
			left.delete();
		}
		left = null;
		if (right != null) {
			right.delete();
		}
		right = null;
		parent = null;
		key = null;
		height = 0;
	}

	/**
	 * Constructs a new BinaryTree node for the given object with null pointers
	 * for left, right, and parent.
	 * 
	 * @param key the key to store
	 */
	public BinaryTreeNode(T key) {
		this(key, null, null, null);
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
	 * Sets the height of this node.
	 * 
	 * @param newHeight the new height of this node
	 */
	public void setHeight(int newHeight) {
		this.height = newHeight;
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
	 * Set the key value stored in this node.
	 * 
	 * @param newKey the new key
	 */
	public void setKey(T newKey) {
		this.key = newKey;
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
	 * Sets the left child of this node to the supplied node.
	 * 
	 * @param newLeft the new left child
	 */
	public void setLeft(BinaryTreeNode<T> newLeft) {
		this.left = newLeft;
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
	 * Sets the right child of this node to the supplied node.
	 * 
	 * @param newRight the new right child
	 */
	public void setRight(BinaryTreeNode<T> newRight) {
		this.right = newRight;
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
	 * Sets the parent of this node to the supplied node.
	 * 
	 * @param newParent the new parent
	 */
	public void setParent(BinaryTreeNode<T> newParent) {
		this.parent = newParent;
	}

}
