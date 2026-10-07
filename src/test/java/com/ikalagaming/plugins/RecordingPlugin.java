package com.ikalagaming.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A plugin that records every lifecycle method called on it, so tests can check the order things
 * happen in. Jars made with {@link PluginJars} don't contain this class, so it is loaded from the
 * test classpath and every instance shares the same record.
 *
 * @author Ches Burks
 */
public class RecordingPlugin extends Plugin {
    /** Lifecycle calls in the order they happened, formatted like "onLoad:PluginName". */
    public static final List<String> CALLS = Collections.synchronizedList(new ArrayList<>());

    /**
     * The index of a call in {@link #CALLS}.
     *
     * @param callback The lifecycle method, like "onLoad".
     * @param plugin The plugin name.
     * @return The index of the call, or -1 if it never happened.
     */
    public static int indexOf(String callback, String plugin) {
        return CALLS.indexOf(callback + ":" + plugin);
    }

    /**
     * Record a call to a lifecycle method.
     *
     * @param callback The lifecycle method, like "onLoad".
     */
    protected void record(String callback) {
        CALLS.add(callback + ":" + getName());
    }

    /**
     * Every instance shares this class, so look up the name the plugin was loaded under.
     *
     * @return The name of the plugin from its plugin.yml.
     */
    @Override
    public String getName() {
        return PluginManager.getInstance().getLoadedPlugins().entrySet().stream()
                .filter(entry -> entry.getValue() == this)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("?");
    }

    @Override
    public boolean onDisable() {
        record("onDisable");
        return true;
    }

    @Override
    public boolean onEnable() {
        record("onEnable");
        return true;
    }

    @Override
    public boolean onLoad() {
        record("onLoad");
        return true;
    }

    @Override
    public boolean onUnload() {
        record("onUnload");
        return true;
    }
}
