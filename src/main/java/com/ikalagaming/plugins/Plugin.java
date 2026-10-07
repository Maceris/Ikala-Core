package com.ikalagaming.plugins;

import com.ikalagaming.event.Listener;
import com.ikalagaming.plugins.config.ConfigManager;

import lombok.NonNull;

import java.util.Set;

/**
 * A distinct chunk of the program with a specific purpose and methods for managing its state and
 * interacting with the main program.
 *
 * @author Ches Burks
 */
public abstract class Plugin {

    /** The name from the plugin info, set by the plugin manager after creating the plugin. */
    private String name;

    /**
     * Returns a list of listeners for this plugin. These listeners will be used with the event
     * system. They are registered after {@link #onEnable()} succeeds, and unregistered before
     * {@link #onDisable()} is called, so the plugin only receives events while it is enabled.
     *
     * @return a list of listeners for the plugin.
     */
    public Set<Listener> getListeners() {
        return Set.of();
    }

    /**
     * Returns the name of the plugin, which is the name from its plugin.yml. This is set by the
     * plugin manager right after the plugin is created, so it is null inside the constructor.
     *
     * <p>Plugins may override this, for example to return a constant, but the plugin will fail to
     * load if it doesn't match the name in the plugin.yml.
     *
     * @return The unique name of the plugin.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the plugin, from its plugin info.
     *
     * @param name The name of the plugin.
     */
    void setName(@NonNull String name) {
        this.name = name;
    }

    /**
     * This method is called when the plugin is disabled, and gives the plugin the chance to shut
     * itself down and save any changes made in memory to disk if necessary.
     *
     * @return True if disabling was successful, false if there was a problem
     */
    public boolean onDisable() {
        return true;
    }

    /**
     * This method is called when the plugin is enabled. Initialization should be performed here,
     * and configuration and data should be loaded from disk if necessary. All of the plugin's
     * dependencies are enabled before this is called, unless they are part of a dependency cycle
     * with this plugin.
     *
     * @return True if enabling was successful, false if there was a problem
     */
    public boolean onEnable() {
        return true;
    }

    /**
     * Called when the plugin is loaded into memory. The plugin may or may not be enabled at this
     * time.
     *
     * @return True if loading was successful, false if there was a problem
     */
    public boolean onLoad() {
        return true;
    }

    /**
     * Called just before the plugin is unloaded from memory. If it is enabled, then the plugin
     * should disable itself now. Any memory that can reasonably be dereferenced by the plugin
     * itself, should be. Files may be saved to disk if needed.
     *
     * @return True if unloading was successful, false if there was a problem
     */
    public boolean onUnload() {
        return true;
    }

    /**
     * Called when a plugin has loaded with a version that is newer than previously seen. This is
     * the hook to do any activities for setting up or converting to a new version.
     *
     * @param lastVersion The last known version that was loaded.
     */
    public void onUpgrade(@NonNull String lastVersion) {}

    /**
     * Save the default configuration file to disk. If there are additional config files that need
     * to be saved, they can be added here.
     */
    public void saveDefaultConfig() {
        ConfigManager.saveDefaultConfig(this, ConfigManager.DEFAULT_NAME);
    }
}
