package com.ikalagaming.plugins;

import com.ikalagaming.event.Listener;

import java.util.Set;

/**
 * A {@link RecordingPlugin} that always fails to load.
 *
 * @author Ches Burks
 */
public class FailingLoadPlugin extends RecordingPlugin {
    /** A listener that should never be registered, since the plugin never loads. */
    public static final Listener LISTENER = new Listener() {};

    private final Set<Listener> listeners = Set.of(FailingLoadPlugin.LISTENER);

    @Override
    public Set<Listener> getListeners() {
        return listeners;
    }

    @Override
    public boolean onLoad() {
        record("onLoad");
        return false;
    }
}
