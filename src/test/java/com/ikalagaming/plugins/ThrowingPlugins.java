package com.ikalagaming.plugins;

import com.ikalagaming.event.Listener;

import java.util.Set;

/**
 * Plugins that throw exceptions from different places, to check that one broken plugin can't take
 * down the plugin manager.
 *
 * @author Ches Burks
 */
public final class ThrowingPlugins {

    /** Throws from its constructor. */
    public static class InConstructor extends RecordingPlugin {
        /** Throws an exception. */
        public InConstructor() {
            throw new IllegalStateException("Broken constructor");
        }
    }

    /** Throws from its static initializer. */
    public static class InStaticInitializer extends RecordingPlugin {
        static {
            if (Boolean.TRUE) {
                throw new IllegalStateException("Broken static initializer");
            }
        }
    }

    /** Throws from {@link #getListeners()}. */
    public static class InGetListeners extends RecordingPlugin {
        @Override
        public Set<Listener> getListeners() {
            throw new IllegalStateException("Broken getListeners");
        }
    }

    /** Throws from {@link #onDisable()}. */
    public static class InOnDisable extends RecordingPlugin {
        @Override
        public boolean onDisable() {
            record("onDisable");
            throw new IllegalStateException("Broken onDisable");
        }
    }

    /** Throws from {@link #onEnable()}. */
    public static class InOnEnable extends RecordingPlugin {
        @Override
        public boolean onEnable() {
            record("onEnable");
            throw new IllegalStateException("Broken onEnable");
        }
    }

    /** Throws from {@link #onLoad()}. */
    public static class InOnLoad extends RecordingPlugin {
        @Override
        public boolean onLoad() {
            record("onLoad");
            throw new IllegalStateException("Broken onLoad");
        }
    }

    /** Throws from {@link #onUnload()}. */
    public static class InOnUnload extends RecordingPlugin {
        @Override
        public boolean onUnload() {
            record("onUnload");
            throw new IllegalStateException("Broken onUnload");
        }
    }

    /** Throws from {@link #onUpgrade(String)}. */
    public static class InOnUpgrade extends RecordingPlugin {
        @Override
        public void onUpgrade(String lastVersion) {
            record("onUpgrade");
            throw new IllegalStateException("Broken onUpgrade");
        }
    }

    /** Overrides {@link #getName()} with a name that doesn't match its plugin.yml. */
    public static class WrongName extends RecordingPlugin {
        @Override
        public String getName() {
            return "SomethingElse";
        }
    }

    /** Private constructor so this class is not instantiated. */
    private ThrowingPlugins() {}
}
