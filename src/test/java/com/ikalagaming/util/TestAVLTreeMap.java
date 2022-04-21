package com.ikalagaming.util;

import com.ikalagaming.util.AVLTreeMap.BinaryTreeNode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the AVL tree map.
 *
 * @author Ches Burks
 *
 */
public class TestAVLTreeMap {

	private int caseNum = 1;

	private void checkBalance(AVLTreeMap<Long, String> map,
		BinaryTreeNode<Long, String> root) {
		if (map == null || root == null) {
			return;
		}
		Assertions.assertTrue(Math.abs(map.getBalance(root)) <= 1);
		if (root.getLeft() != null) {
			this.checkBalance(map, root.getLeft());
		}
		if (root.getRight() != null) {
			this.checkBalance(map, root.getRight());
		}
	}

	private <K extends Comparable<K>, V> void
		checkOrder(BinaryTreeNode<K, V> root) {
		if (root == null) {
			return;
		}
		if (root.getLeft() != null) {
			Assertions.assertTrue(
				root.getLeft().getKey().compareTo(root.getKey()) < 0);
			this.checkOrder(root.getLeft());
		}
		if (root.getRight() != null) {
			Assertions.assertTrue(
				root.getRight().getKey().compareTo(root.getKey()) > 0);
			this.checkOrder(root.getRight());
		}
	}

	private void insertCase(long key, AVLTreeMap<Long, String> testMap) {
		testMap.insert(key, "test" + this.caseNum++);
	}

	/**
	 * Test that inserting to the tree ends up with a balanced tree.
	 */
	@Test
	public void testTreeBalance() {
		AVLTreeMap<Long, String> testMap = new AVLTreeMap<>();

		List<Long> cases = Arrays.asList(8L, 1L, 6L, 2L, 5L, 4L, 45L, 23L, 7L,
			13L, 1112L, 3L, 123L, 145L, 345L, 564L);
		cases.forEach(l -> this.insertCase(l, testMap));

		this.checkBalance(testMap, testMap.getTreeRoot());
		Assertions.assertEquals(cases.size(), testMap.getSize());
	}

	/**
	 * Test that inserting to the tree ends up with a balanced tree.
	 */
	@Test
	public void testTreeOrdering() {
		AVLTreeMap<Long, String> testMap = new AVLTreeMap<>();

		List<Long> cases = Arrays.asList(8L, 1L, 6L, 2L, 5L, 4L, 45L, 23L, 7L,
			13L, 1112L, 3L, 123L, 145L, 345L, 564L);
		cases.forEach(l -> this.insertCase(l, testMap));

		AVLTreeMap<String, String> testMap2 = new AVLTreeMap<>();

		List<String> strings = Arrays.asList("Test", "walrus", "xyzzy",
			"AaaaAAa", "aaaaaaa", "bbasedf", "static", "null?");
		strings.forEach(s -> testMap2.insert(s, "ignored"));
		this.checkOrder(testMap2.getTreeRoot());
	}

}
