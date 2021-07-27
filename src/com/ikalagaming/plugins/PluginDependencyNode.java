package com.ikalagaming.plugins;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to create a dependency graph to resolve dependency cycles as part of
 * loading plugins.
 *
 * @author Ches Burks
 *
 */
@Getter
@Setter
class PluginDependencyNode {
	/**
	 * Plugins that this one depends on.
	 *
	 * @param children The list of dependencies.
	 * @return The list of dependencies.
	 */
	@SuppressWarnings("javadoc")
	private List<PluginDependencyNode> children;

	/**
	 * The name of this plugin.
	 *
	 * @param name The name of the plugin.
	 * @return The plugins name.
	 */
	@SuppressWarnings("javadoc")
	private String name;

	/**
	 * The parent node, might be null.
	 *
	 * @param parent The parent dependency, null if there is none.
	 * @return The parent dependency, null if there is none.
	 */
	@SuppressWarnings("javadoc")
	private PluginDependencyNode parent;

	/**
	 * Create a new node given the name.
	 *
	 * @param name The name of the plugin.
	 */
	public PluginDependencyNode(String name) {
		this.name = name;
		this.children = new ArrayList<>();
	}

	/**
	 * Add a child node to the list of plugins that this plugin depends on,
	 * records this node as its parent.
	 *
	 * @param childName The name of the plugin this plugin depends on.
	 */
	public void addChild(String childName) {
		PluginDependencyNode child = new PluginDependencyNode(childName);
		this.children.add(child);
		child.setParent(this);
	}
}
