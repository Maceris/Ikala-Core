package com.ikalagaming.system;

import java.util.HashSet;
import java.util.Set;

import com.ikalagaming.event.Listener;
import com.ikalagaming.plugins.Plugin;
import com.ikalagaming.plugins.PluginManager;

/**
 * Allows for interfacing with the core system using events.
 *
 * @deprecated This will be extracted to the console plugin.
 * @author Ches Burks
 *
 */
@Deprecated
public class SystemPlugin implements Plugin {

	/**
	 * The name. This is readable by everyone to simplify logging by the system.
	 * 
	 * @deprecated This is going to go away
	 */
	@Deprecated
	public static final String PLUGIN_NAME = "System";

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
		Listener sysListener = new SystemEventListener();
		this.listeners = new HashSet<>();
		this.listeners.add(sysListener);
	}

	@Override
	public Set<Listener> getListeners() {
		return this.listeners;
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
