package com.ikalagaming.system;

import java.util.HashSet;
import java.util.Set;

import com.ikalagaming.event.Listener;
import com.ikalagaming.packages.Package;
import com.ikalagaming.packages.PackageManager;

/**
 * Allows for interfacing with the core system using events.
 *
 * @author Ches Burks
 *
 */
public class SystemPackage implements Package {

	/**
	 * The name that is returned by {@link #getName()}. This is readable by
	 * everyone to simplify logging by the system.
	 */
	public static final String PACKAGE_NAME = "System";

	private static final double PACKAGE_VERSION = 1.1;

	/*
	 * Made final so that if anyone tries to modify it they will fail, as a
	 * security feature.
	 */
	private final Set<Listener> listeners;

	/**
	 * Constructs a new System package
	 *
	 * @param manager The packaging system to handle events for
	 */
	public SystemPackage(PackageManager manager) {
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
		return SystemPackage.PACKAGE_NAME;
	}

	@Override
	public double getVersion() {
		return SystemPackage.PACKAGE_VERSION;
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
