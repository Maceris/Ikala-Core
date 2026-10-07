package com.ikalagaming.plugins;

import com.ikalagaming.event.Listener;

import lombok.Getter;

import java.util.Set;

/**
 * A {@link RecordingPlugin} with its own event listener.
 *
 * @author Ches Burks
 */
public class ListeningPlugin extends RecordingPlugin {
    /**
     * The listener for this plugin.
     *
     * @return The listener for this plugin.
     */
    @SuppressWarnings("javadoc")
    @Getter
    private final Listener listener = new Listener() {};

    private final Set<Listener> listeners = Set.of(listener);

    @Override
    public Set<Listener> getListeners() {
        return listeners;
    }
}
