package com.ikalagaming.scripting;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * What to run and how, for {@link ScriptManager#start(ScriptLaunch)}. Made with {@link #file(Path)}
 * or {@link #source(String)}, then filled in with the other methods.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScriptLaunch {
    /** The file to run, or null if running source text. */
    private final Path file;

    /** The source text to run, or null if running a file. */
    private final String source;

    /** A name to identify the script while debugging, which may be null. */
    private String name;

    /** The plugin that owns the script, or null if it isn't owned. */
    private String owner;

    /** The label to start at, or null to start at the beginning. */
    private String entryLabel;

    /** Globals for this script only, on top of the ones the script manager provides. */
    @Getter(AccessLevel.NONE)
    private final Map<String, Object> globals = new LinkedHashMap<>();

    /**
     * Run a script file. Compiled files are cached until the file changes.
     *
     * @param file The file.
     * @return The launch, named after the file.
     */
    public static ScriptLaunch file(@NonNull Path file) {
        final ScriptLaunch launch = new ScriptLaunch(file, null);
        final Path fileName = file.getFileName();
        launch.name = fileName == null ? file.toString() : fileName.toString();
        return launch;
    }

    /**
     * Run script source text.
     *
     * @param source The script.
     * @return The launch.
     */
    public static ScriptLaunch source(@NonNull String source) {
        return new ScriptLaunch(null, source);
    }

    /**
     * Set the name, to identify the script while debugging.
     *
     * @param newName The name, which may be null.
     * @return This launch.
     */
    public ScriptLaunch name(String newName) {
        name = newName;
        return this;
    }

    /**
     * Set the plugin that owns the script. Owned scripts are terminated when the plugin unloads,
     * and get that plugin's version of provided globals like {@code ui}.
     *
     * @param plugin The plugin name, which may be null for no owner.
     * @return This launch.
     */
    public ScriptLaunch owner(String plugin) {
        owner = plugin;
        return this;
    }

    /**
     * Start at a label instead of the beginning. The label can't have variable declarations in
     * scope, the same as for a goto from the start of the script.
     *
     * @param label The label, which may be null to start at the beginning.
     * @return This launch.
     */
    public ScriptLaunch at(String label) {
        entryLabel = label;
        return this;
    }

    /**
     * Give this script a global, like the event that started it. It replaces a provided global with
     * the same name.
     *
     * @param globalName The name the script uses.
     * @param value The value.
     * @return This launch.
     */
    public ScriptLaunch global(@NonNull String globalName, Object value) {
        globals.put(globalName, value);
        return this;
    }

    /**
     * The globals for this script only.
     *
     * @return The globals by name, which can't be modified.
     */
    public Map<String, Object> getGlobals() {
        return Collections.unmodifiableMap(globals);
    }
}
