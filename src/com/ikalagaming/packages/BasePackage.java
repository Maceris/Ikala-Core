package com.ikalagaming.packages;

import java.util.HashSet;
import java.util.Set;

import com.ikalagaming.event.Listener;

/**
 * A basic implementation of the {@link Package} interface. This allows you to
 * only implement some of the methods yourself.
 *
 * @author Ches
 *
 */
public class BasePackage implements Package {

	private String packageName = "package-manager";
	private final double version = 0.1;
	private PackageState state = PackageState.DISABLED;
	private Set<Listener> listeners;

	@Override
	public boolean disable() {
		this.setPackageState(PackageState.DISABLING);
		this.onDisable();
		return true;
	}

	@Override
	public boolean enable() {
		this.setPackageState(PackageState.ENABLING);
		this.onEnable();
		return true;
	}

	@Override
	public Set<Listener> getListeners() {
		if (this.listeners == null) {
			this.listeners = new HashSet<>();
		}
		return this.listeners;
	}

	@Override
	public String getName() {
		return this.packageName;
	}

	@Override
	public PackageState getPackageState() {
		synchronized (this.state) {
			return this.state;
		}
	}

	@Override
	public double getVersion() {
		return this.version;
	}

	@Override
	public boolean isEnabled() {
		if (this.getPackageState() == PackageState.ENABLED) {
			return true;
		}
		return false;
	}

	@Override
	public void onDisable() {
		this.setPackageState(PackageState.DISABLED);
	}

	@Override
	public void onEnable() {
		this.setPackageState(PackageState.ENABLED);
	}

	@Override
	public void onLoad() {
		this.setPackageState(PackageState.LOADING);
		this.setPackageState(PackageState.DISABLED);
	}

	@Override
	public void onUnload() {
		this.setPackageState(PackageState.UNLOADING);
		if (this.getPackageState() == PackageState.ENABLED) {
			this.disable();
			this.setPackageState(PackageState.UNLOADING);
		}
		this.setPackageState(PackageState.PENDING_REMOVAL);
	}

	@Override
	public boolean reload() {
		this.setPackageState(PackageState.UNLOADING);
		if (this.getPackageState() == PackageState.ENABLED) {
			this.disable();
			this.setPackageState(PackageState.UNLOADING);
		}
		this.onLoad();
		return false;
	}

	@Override
	public void setPackageState(PackageState newState) {
		synchronized (this.state) {
			this.state = newState;
		}
	}

}
