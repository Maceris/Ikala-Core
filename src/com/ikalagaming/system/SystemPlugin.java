package com.ikalagaming.system;

import java.util.HashSet;
import java.util.Set;

import com.ikalagaming.event.Listener;
import com.ikalagaming.plugins.Plugin;
import com.ikalagaming.plugins.PluginManager;

/**
 * Allows for interfacing with the core system using events.
 *
 * @author Ches Burks
 *
 */
public class SystemPlugin implements Plugin {

	/**
	 * The name that is returned by {@link #getName()}. This is readable by
	 * everyone to simplify logging by the system.
	 */
	public static final String PLUGIN_NAME = "System";

	private static final double PLUGIN_VERSION = 1.1;

	/*
	 * Made final so that if anyone tries to modify it they will fail, as a
	 * security feature.
	 */
	private final Set<Listener> listeners;

	/**
	 * Constructs a new System plugin
	 *
	 * @param manager The packaging system to handle events for
	 */
	public SystemPlugin(PluginManager manager) {
		Listener pmListener = new PMEventListener(this, manager);
		Listener sysListener = new SystemEventListener();
		this.listeners = new HashSet<>();
		this.listeners.add(pmListener);
		this.listeners.add(sysListener);
	}

	@Override
	public Set<Listener> getListeners() {
		return this.listeners;
	}

	@Override
	public String getName() {
		return SystemPlugin.PLUGIN_NAME;
	}

	@Override
	public double getVersion() {
		return SystemPlugin.PLUGIN_VERSION;
	}

	@Override
	public boolean onDisable() {
		return true;
	}

	@Override
	public boolean onEnable() {
		return true;
	}

	@Override
	public boolean onLoad() {
		return true;
	}

	@Override
	public boolean onUnload() {
		return true;
	}

}
