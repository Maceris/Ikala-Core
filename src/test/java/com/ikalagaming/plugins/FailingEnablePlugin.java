package com.ikalagaming.plugins;

/**
 * A {@link ListeningPlugin} that always fails to enable.
 *
 * @author Ches Burks
 */
public class FailingEnablePlugin extends ListeningPlugin {
    @Override
    public boolean onEnable() {
        record("onEnable");
        return false;
    }
}
