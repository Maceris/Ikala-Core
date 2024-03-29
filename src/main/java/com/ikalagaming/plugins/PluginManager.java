package com.ikalagaming.plugins;

import com.ikalagaming.event.EventManager;
import com.ikalagaming.event.Listener;
import com.ikalagaming.launcher.Constants;
import com.ikalagaming.launcher.PluginFolder;
import com.ikalagaming.localization.Localization;
import com.ikalagaming.plugins.events.PluginDisabled;
import com.ikalagaming.plugins.events.PluginEnabled;
import com.ikalagaming.plugins.events.PluginLoaded;
import com.ikalagaming.plugins.events.PluginUnloaded;
import com.ikalagaming.util.SafeResourceLoader;

import com.github.zafarkhaja.semver.Version;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * Handles loading, unloading and storage of plugins. This is considered a plugin, but is always
 * enabled and never loaded.
 *
 * @author Ches Burks
 */
@Slf4j
public class PluginManager {

    private static PluginManager instance;

    private static final String PLUGIN_CONFIG_FILENAME = "plugin.yml";

    /** The name of the core system, since it is not technically a plugin. */
    public static final String PLUGIN_NAME = "Ikala-Core";

    /**
     * Compare the order of version strings, as if using first.compareTo(second). This should be
     * string semantic versions and can handle build info and the like but build metadata is
     * ignored.
     *
     * <p>For example, compareVersions("1.0.0-rc.1+build.1", "1.3.7+build.2.b8f12d7") would return a
     * number less than 0. compareVersions("2.1.3", "1.0.1") would return a number greater than 0.
     * compareVersions("1.0.0+build.1", "1.0.0+build.1") would return 0 because they are equal
     * builds.
     *
     * @param first The first version string
     * @param second The second version string
     * @return The value of comparing the two versions.
     */
    public static final int compareVersions(final String first, final String second) {
        return Version.parse(first).compareToIgnoreBuildMetadata(Version.parse(second));
    }

    /**
     * Shuts down the static instance (unregisters commands and unloads plugins) if it exists, and
     * then nullifies the reference to it. This exists in case you wish to use your own instances of
     * the Plugin Manager and not use the single static instance provided. If the instance does not
     * exist, nothing happens. Note that a new static instance may be created if the instance is
     * requested later.
     *
     * @see #getInstance()
     */
    @Synchronized
    public static void destroyInstance() {
        if (PluginManager.instance == null) {
            return;
        }
        PluginManager.instance.shutdown();
        PluginManager.instance = null;
    }

    /**
     * Returns the static instance of the plugin manager. Since there should only be one of these,
     * having a static instance is fine and any class can get the instance which all other classes
     * should share. If there is no instance yet, one will be created.
     *
     * @return The static instance of the Plugin Manager.
     * @see PluginManager#destroyInstance()
     */
    @Synchronized
    public static PluginManager getInstance() {
        if (PluginManager.instance == null) {
            PluginManager.instance = new PluginManager(EventManager.getInstance());
        }
        return PluginManager.instance;
    }

    /**
     * Returns the static instance of the plugin manager. Since there should only be one of these,
     * having a static instance is fine and any class can get the instance which all other classes
     * should share. If there is no instance yet, one will be created.
     *
     * @param eventManager The event manager to use.
     * @return The static instance of the Plugin Manager.
     * @see PluginManager#destroyInstance()
     */
    @Synchronized
    public static PluginManager getInstance(EventManager eventManager) {
        if (PluginManager.instance == null) {
            PluginManager.instance = new PluginManager(eventManager);
        }
        return PluginManager.instance;
    }

    /**
     * Returns true if the first string is a strictly newer version than the second, where both are
     * semantic version strings.
     *
     * <p>For example, isNewerVersion("1.2.3", "0.0.1") returns true.
     * isNewerVersion("1.0.0+build.1", "1.0.2") returns false. isNewerVersion("1.0.0+build.1",
     * "1.0.0") returns false because it is the same.
     *
     * @param toCheck The string that is unknown relative to the known version.
     * @param existing The existing version that is known but could be outdated.
     * @return True if the first version is newer than the second, otherwise false.
     */
    public static final boolean isNewerVersion(final String toCheck, final String existing) {
        return Version.parse(toCheck).isHigherThan(Version.parse(existing));
    }

    private Object commandLock = new Object();

    /** A list of all of the commands registered. This list is sorted. */
    private List<PluginCommand> commands;

    /**
     * If the jar is run from command line. If true we do things like storing version numbers for
     * plugins on the file system, if false it keeps things clean as we are probably running tests
     * or doing things from another entry point.
     *
     * @param commandLine If we are running from command line or not.
     * @return true if the framework was started from command line, false otherwise.
     */
    @SuppressWarnings("javadoc")
    @Getter
    @Setter
    private boolean commandLine;

    /**
     * The flag for automatically enabling plugins when they are loaded. If they are not enabled on
     * load, they must be enabled manually after being loaded.
     *
     * <p>This allows some customization of how the plugin system works.
     *
     * @param enableOnLoad true if the plugins should be automatically enabled after loading, false
     *     if they should be manually enabled
     * @return true if the plugins enable after loading, false if they must be manually enabled
     */
    @SuppressWarnings("javadoc")
    @Getter
    @Setter
    private boolean enableOnLoad;

    private PluginCommandListener commandListener;

    /** Stores all the classes loaded by plugins. Keys are the unique class names. */
    private final Map<String, Class<?>> pluginClassCache;

    /**
     * The class loader that replaces the threads class loader.
     *
     * @return The common class loader for plugins.
     */
    @Getter private SharedClassLoader sharedClassLoader;

    /** Maps strings to info about plugins loaded in memory */
    private Map<String, PluginDetails> pluginDetails;

    /** Lock object for plugin related activities */
    private Object pluginLock = new Object();

    /**
     * The current resource bundle for the plugin manager.
     *
     * @return The current resource bundle, which may be null.
     */
    @SuppressWarnings("javadoc")
    @Getter
    private ResourceBundle resourceBundle;

    private EventManager eventManager;

    /**
     * Constructs a new {@link PluginManager} and initializes variables.
     *
     * @param eventManager The event manager to use for the plugin system
     */
    public PluginManager(@NonNull EventManager eventManager) {
        enableOnLoad = true;
        commandLine = false;
        this.eventManager = eventManager;
        pluginDetails = Collections.synchronizedMap(new HashMap<>());
        pluginClassCache = Collections.synchronizedMap(new HashMap<>());
        resourceBundle =
                ResourceBundle.getBundle(
                        "com.ikalagaming.plugins.PluginManager", Localization.getLocale());
        commandListener = new PluginCommandListener(this);

        commands = new ArrayList<>();

        registerCommands();
        this.eventManager.registerEventListeners(commandListener);
    }

    private void alertMissingArgs() {
        String tmp = SafeResourceLoader.getString("COMMAND_ARG_MISSING", getResourceBundle());
        log.warn(tmp);
    }

    /**
     * Calculate the state of a plugin based on whether the dependencies have been satisfied. Does
     * not calculate the state of the children, multiple passes may be required to determine the
     * final state. This is used during the process of loading multiple plugins at once and
     * determining if dependencies are satisfied.
     *
     * @param pluginInfo The plugin information we are checking.
     * @return The state of the plugin in context of loading multiple plugins.
     */
    private PluginState calculatePluginState(PluginInfo pluginInfo) {
        if (null == pluginInfo) {
            return PluginState.NOT_LOADED;
        }
        /*
         * If all dependencies are satisfied (DISCOVERED, loaded/enabled,
         * DEPS_SATISFIED, generally existing in the system), or it has no
         * dependencies, mark it as DEPS_SATISFIED as well. If a plugin has
         * unsatisfied dependencies, such as something NOT_LOADED, mark the
         * plugin as DEPS_MISSING. If a plugin does not have missing
         * dependencies, but it has dependencies that are also DEPS_CHECKING,
         * mark it as DEPS_CHECKING.
         */
        boolean stillEvaluatingChildren = false;
        for (String dependencyName : pluginInfo.getDependencies()) {
            PluginState state = getPluginState(dependencyName);
            switch (state) {
                case DEPS_CHECKING:
                    /*
                     * Not yet confirmed until children nodes are validated, so
                     * set the flag and keep checking children.
                     */
                    stillEvaluatingChildren = true;
                    break;
                case DEPS_SATISFIED, DISABLED, DISABLING, DISCOVERED, ENABLED, ENABLING, LOADING:
                    // satisfied, we can keep going
                    break;
                case DEPS_MISSING, CORRUPTED, NOT_LOADED, PENDING_REMOVAL, UNLOADING:
                    // propagate the failure up
                    /*
                     * Not satisfied or won't be, impossible to load so we just
                     * bail out of the method immediately
                     */
                    return PluginState.DEPS_MISSING;
                default:
                    break;
            }
        }
        /*
         * At this point no child was missing dependencies or invalid, so if all
         * were good we can continue, but if any were still needing to be
         * checked themselves, we need to note that.
         */
        if (stillEvaluatingChildren) {
            return PluginState.DEPS_CHECKING;
        }
        return PluginState.DEPS_SATISFIED;
    }

    private void callbackDisable(@NonNull List<String> args) {
        if (args.isEmpty()) {
            alertMissingArgs();
            return;
        }
        disable(args.get(0));
    }

    private void callbackEnable(@NonNull List<String> args) {
        if (args.isEmpty()) {
            alertMissingArgs();
            return;
        }
        enable(args.get(0));
    }

    /**
     * The callback for printing out all possible commands.
     *
     * @param args Ignored.
     */
    void callbackHelp(List<String> args) {
        log.info(SafeResourceLoader.getString("HELP_TEXT", resourceBundle));

        int longestCmdLength =
                commands.stream()
                        .map(PluginCommand::getCommand)
                        .map(String::length)
                        .mapToInt(i -> i)
                        .max()
                        .orElse(0);

        for (PluginCommand cmd : commands) {
            StringBuilder sb = new StringBuilder();
            final int padding = longestCmdLength - cmd.getCommand().length();
            sb.append(cmd.getCommand());
            for (int i = 0; i < padding; ++i) {
                sb.append(' ');
            }
            sb.append(" : ");
            sb.append(cmd.getOwner());
            /*
             * This should show on the command line, as logs might be redirected
             * to console and we want command line interaction.
             */
            System.out.println(sb.toString()); // NOSONAR
        }
    }

    private void callbackLoad(@NonNull List<String> args) {
        if (args.isEmpty()) {
            alertMissingArgs();
            return;
        }
        loadPlugins(System.getProperty("user.dir") + Constants.PLUGIN_FOLDER_PATH, args);
    }

    private void callbackPrintPlugins(@SuppressWarnings("unused") List<String> args) {
        Map<String, Plugin> loadedPlugins = getLoadedPlugins();

        ArrayList<String> names = new ArrayList<>(loadedPlugins.keySet());
        Collections.sort(names);

        loadedPlugins.keySet().stream()
                .sorted()
                .forEach(
                        name -> {
                            StringBuilder sb = new StringBuilder();
                            sb.append(name);
                            sb.append(" (");
                            sb.append(getPluginState(name));
                            sb.append(")");
                            /*
                             * This should show on the command line, as logs might be
                             * redirected to console and we want command line
                             * interaction.
                             */
                            System.out.println(sb.toString()); // NOSONAR
                        });
    }

    private void callbackReload(@NonNull List<String> args) {
        if (args.isEmpty()) {
            alertMissingArgs();
            return;
        }
        reload(args.get(0));
    }

    private void callbackUnload(@NonNull List<String> args) {
        if (args.isEmpty()) {
            alertMissingArgs();
            return;
        }
        unloadPlugin(args.get(0));
    }

    /** Unregisters all commands */
    @Synchronized("commandLock")
    public void clearCommands() {
        ArrayList<String> cmds = new ArrayList<>();
        for (PluginCommand s : this.commands) {
            cmds.add(s.getCommand());
        }
        for (String s : cmds) {
            this.unregisterCommand(s);
        }
        this.commands.clear();
    }

    /**
     * Deactivates the plugin and halts all of its operations. The plugin is still loaded in memory
     * but not active. Calls {@link Plugin#onDisable()}. This changes the plugins state to {@link
     * PluginState#DISABLING DISABLING}. The plugin state is changed to {@link PluginState#DISABLED
     * DISABLED} after completion. If {@link Plugin#onDisable()} returns false (failed), the plugin
     * state is set to {@link PluginState#CORRUPTED CORRUPTED}. Any plugin that depends on the
     * provided plugin will also be disabled.
     *
     * @param target the name of the plugin to disable
     * @return true if the plugin has been successfully disabled, false if there was a problem
     */
    @Synchronized("pluginLock")
    public boolean disable(@NonNull final String target) {
        if (!this.isLoaded(target)) {
            logNotLoaded(target);
            return false;
        }
        if (!this.isEnabled(target)) {
            logAlert("ALERT_PLUGIN_ALREADY_DISABLED", target);
            return false;
        }

        ArrayDeque<String> needsDisable = new ArrayDeque<>();
        ArrayDeque<String> processingQueue = new ArrayDeque<>();
        processingQueue.add(target);

        /*
         * Add all plugins to a queue in order as if doing a Breadth-First
         * Search.
         */
        while (!processingQueue.isEmpty()) {
            String next = processingQueue.poll();
            List<String> dependents =
                    pluginDetails.entrySet().stream()
                            .filter(
                                    entry ->
                                            entry.getValue()
                                                    .getInfo()
                                                    .getDependencies()
                                                    .contains(next))
                            .filter(
                                    entry ->
                                            PluginState.ENABLED.equals(entry.getValue().getState()))
                            .map(Entry::getKey)
                            .collect(Collectors.toCollection(ArrayList::new));

            for (String dependent : dependents) {
                if (processingQueue.contains(dependent) || needsDisable.contains(dependent)) {
                    continue;
                }
                processingQueue.add(dependent);
            }
            if (!needsDisable.contains(next)) {
                needsDisable.add(next);
            }
        }

        boolean success = true;
        /*
         * We unload from deepest dependency first, starting with the last thing
         * added to the list and working our way back to the original plugin.
         */
        while (!needsDisable.isEmpty()) {
            /*
             * Keep the unload operation first so we don't short circuit and
             * skip unloading if something fails.
             */
            success = disableSingle(needsDisable.pop()) && success;
        }

        return success;
    }

    /**
     * Disable a single plugin.
     *
     * @param target The plugin to disable.
     * @return True on success, false on failure.
     */
    private boolean disableSingle(final String target) {
        if (!isLoaded(target)) {
            logNotLoaded(target);
            return false;
        }
        if (!isEnabled(target)) {
            logAlert("ALERT_PLUGIN_ALREADY_DISABLED", target);
            return false;
        }
        setPluginState(target, PluginState.DISABLING);

        logAlert("ALERT_DISABLING", target);

        PluginDetails details = pluginDetails.get(target);
        if (null == details) {
            logAlert("PLUGIN_DETAILS_MISSING", target);
            return false;
        }

        boolean success = details.getPlugin().onDisable();
        if (success) {
            setPluginState(target, PluginState.DISABLED);
            new PluginDisabled(target).fire();
            logAlert("ALERT_DISABLED", target);
        } else {
            setPluginState(target, PluginState.CORRUPTED);
            logStateCorrupted(target);
            logAlert("PLUGIN_DISABLE_FAIL", target);
        }
        return success;
    }

    /**
     * Activates the plugin and enables it to perform its normal functions. Calls {@link
     * Plugin#onEnable()}. This changes the plugin state to {@link PluginState#ENABLING ENABLING}.
     * The plugin state is changed to {@link PluginState#ENABLED ENABLED} after completion. If
     * {@link Plugin#onEnable()} returns false (failed), the plugin state is set to {@link
     * PluginState#CORRUPTED CORRUPTED}.
     *
     * @param target The name of the plugin to enable
     * @return true if the plugin was successfully enabled, false if there was a problem
     */
    @Synchronized("pluginLock")
    public boolean enable(@NonNull final String target) {
        if (!this.isLoaded(target)) {
            logNotLoaded(target);
            return false;
        }

        if (this.isEnabled(target)) {
            logAlert("ALERT_PLUGIN_ALREADY_ENABLED", target);
            return false;
        }

        this.setPluginState(target, PluginState.ENABLING);

        logAlert("ALERT_ENABLING", target);

        PluginDetails details = this.pluginDetails.get(target);
        if (null == details) {
            logAlert("PLUGIN_DETAILS_MISSING", target);
            return false;
        }

        boolean success = details.getPlugin().onEnable();
        if (success) {
            this.setPluginState(target, PluginState.ENABLED);
            new PluginEnabled(target).fire();
            logAlert("ALERT_ENABLED", target);
        } else {
            this.setPluginState(target, PluginState.CORRUPTED);
            logStateCorrupted(target);
            logAlert("PLUGIN_ENABLE_FAIL", target);
        }

        return success;
    }

    /**
     * Load up the plugin info from the given jar file and return it. If there is some error like
     * the file not actually being a jar or plugin info missing, then the returned optional is
     * empty.
     *
     * @param jar the jar to load info from.
     * @return an optional containing the plugin info, or an empty optional on failure.
     */
    protected Optional<PluginInfo> extractPluginInfo(@NonNull final File jar) {
        ZipEntry config;

        final String fileName = jar.getName();

        /*
         * Check for being a jar file check for plugin info file load and check
         * for valid info load the file if necessary
         */
        try (JarFile jfile = new JarFile(jar)) {
            config = jfile.getEntry(PluginManager.PLUGIN_CONFIG_FILENAME);
            if (config == null) {
                String msg = SafeResourceLoader.getString("PLUGIN_CONFIG_MISSING", resourceBundle);
                log.warn(msg, PluginManager.PLUGIN_CONFIG_FILENAME);
                return Optional.empty();
            }

            InputStream configIStream = jfile.getInputStream(config);
            PluginInfo info = new PluginInfo(configIStream);
            return Optional.ofNullable(info);
        } catch (IOException e1) {
            String msg = SafeResourceLoader.getString("PLUGIN_CONFIG_READ_ERROR", resourceBundle);
            log.warn(msg, fileName);
            return Optional.empty();
        } catch (InvalidDescriptionException e1) {
            String msg = SafeResourceLoader.getString("PLUGIN_INVALID_DESCRIPTION", resourceBundle);
            log.warn(msg, fileName);
            log.warn(e1.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            String msg = SafeResourceLoader.getString("PLUGIN_JAR_ERROR", resourceBundle);
            log.warn(msg, fileName);
            return Optional.empty();
        }
    }

    /**
     * Find all the names of plugins that have a given state.
     *
     * @param state The state to look for.
     * @return The names of plugins with that given state.
     */
    private List<String> findPluginsByState(@NonNull PluginState state) {
        return pluginDetails.keySet().stream()
                .filter(name -> state.equals(pluginDetails.get(name).getState()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Find the class by class name. Checks for cached versions, but will search through known
     * plugin class loaders to find it. If it cannot be found, returns null.
     *
     * @param name The name of the class to look for.
     * @return The class by the given name, or null if not found.
     */
    Class<?> getClassByName(@NonNull final String name) {
        Class<?> cachedClass = pluginClassCache.get(name);

        if (cachedClass != null) {
            return cachedClass;
        }
        for (Map.Entry<String, PluginDetails> entry : pluginDetails.entrySet()) {
            PluginClassLoader loader = entry.getValue().getClassLoader();
            try {
                cachedClass = loader.findClass(name, false);
            } catch (ClassNotFoundException e) {
                // We couldn't find it here, try another
                continue;
            }
            if (cachedClass != null) {
                return cachedClass;
            }
        }
        return null;
    }

    /**
     * Returns a clone of the commands list.
     *
     * @return a copy of the stored list
     */
    @Synchronized("commandLock")
    public List<PluginCommand> getCommands() {
        ArrayList<PluginCommand> cmds = new ArrayList<>();

        for (PluginCommand pc : this.commands) {
            cmds.add(pc);
        }
        return cmds;
    }

    /**
     * Get the plugin info for a given plugin. If no info exists for the specified plugin, an empty
     * optinal is returned.
     *
     * @param target The name of the plugin to get info from.
     * @return The info for the specified plugin.
     */
    @Synchronized("pluginLock")
    public Optional<PluginInfo> getInfo(@NonNull String target) {
        PluginDetails details = this.pluginDetails.get(target);
        if (details == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(details.getInfo());
    }

    /**
     * Returns the map of plugin name to Plugin of the currently loaded plugins.
     *
     * @return the plugin map
     */
    @Synchronized("pluginLock")
    public Map<String, Plugin> getLoadedPlugins() {
        HashMap<String, Plugin> ret = new HashMap<>();
        pluginDetails.forEach((key, value) -> ret.put(key, value.getPlugin()));
        return ret;
    }

    /**
     * If a plugin of the given name exists ({@link #isLoaded(String)}), then that plugin is
     * returned. Otherwise, an empty optional is returned.
     *
     * @param name The name of the plugin.
     * @return The Plugin with the given name
     */
    @Synchronized("pluginLock")
    public Optional<Plugin> getPlugin(@NonNull final String name) {
        PluginDetails details = this.pluginDetails.get(name);
        if (details == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(details.getPlugin());
    }

    /**
     * Returns the {@link PluginState current state} of the plugin.
     *
     * @param target The name of the plugin to fetch the state of
     * @return a PluginState representing the status of this plugin
     */
    @Synchronized("pluginLock")
    public PluginState getPluginState(String target) {
        if (null == target) {
            return PluginState.NOT_LOADED;
        }

        if (!this.pluginDetails.containsKey(target)) {
            return PluginState.NOT_LOADED;
        }
        return this.pluginDetails.get(target).getState();
    }

    /**
     * Checks if a plugin command has already been registered.
     *
     * @param command The command to check for.
     * @return True if the command has been registered.
     */
    @Synchronized("commandLock")
    public boolean isCommandRegistered(@NonNull final String command) {
        return this.commands.stream().anyMatch(cmd -> command.equalsIgnoreCase(cmd.getCommand()));
    }

    /**
     * Returns true if the plugin is enabled, and false otherwise. A state of {@link
     * PluginState#ENABLED ENABLED} returns true, any other states will return false. Plugins that
     * are not loaded will return false.
     *
     * @param target The name of plugin to check for enabled status
     * @return true if the plugin is fully ready to operate
     */
    @Synchronized("pluginLock")
    public boolean isEnabled(final String target) {
        if (target == null) {
            return false;
        }
        if (!this.isLoaded(target)) {
            return false;
        }
        PluginState state = this.getPluginState(target);
        return PluginState.ENABLED.equals(state);
    }

    /**
     * Returns true if a plugin exists with the given type (for example: "Graphics")'. Most existing
     * states count as loaded, but the following do not:
     *
     * <ul>
     *   <li>{@link PluginState#UNLOADING}
     *   <li>{@link PluginState#PENDING_REMOVAL}
     *   <li>{@link PluginState#NOT_LOADED}
     * </ul>
     *
     * @param type The plugin name
     * @return true If the plugin is loaded in memory, and not actively being unloaded/removed
     */
    @Synchronized("pluginLock")
    public boolean isLoaded(final String type) {
        if (type == null) {
            return false;
        }
        PluginDetails details = this.pluginDetails.get(type);
        if (details == null) {
            return false;
        }
        switch (details.getState()) {
            case CORRUPTED,
                    DEPS_CHECKING,
                    DEPS_MISSING,
                    DEPS_SATISFIED,
                    DISABLED,
                    DISABLING,
                    DISCOVERED,
                    ENABLED,
                    ENABLING,
                    LOADING:
                return true;
            case UNLOADING, PENDING_REMOVAL, NOT_LOADED:
            default:
                return false;
        }
    }

    /**
     * Load all the plugins from .jar files that are located in the given folder.
     *
     * @param folder The folder that contains all the jar files we want to load.
     */
    @Synchronized("pluginLock")
    public void loadAllPlugins(@NonNull String folder) {
        File pluginFolder = null;
        Optional<File> folderMaybe = this.plGetFolder(folder);
        if (folderMaybe.isEmpty()) {
            String warning =
                    SafeResourceLoader.getString("PLUGIN_FOLDER_NOT_FOUND", this.resourceBundle);
            log.warn(warning, folder);
            return;
        }
        pluginFolder = folderMaybe.get();

        // find all the jars
        ArrayList<File> jars = this.plGetAllJars(pluginFolder);

        plLoadPlugins(jars);
    }

    /**
     * Loads a plugin by name from a folder.
     *
     * @param path The path to the folder containing the file.
     * @param pluginName The name of the plugin to load.
     * @return true on success, false if it failed
     */
    @Synchronized("pluginLock")
    public boolean loadPlugin(@NonNull String path, @NonNull String pluginName) {
        Optional<File> folderMaybe = this.plGetFolder(path);
        if (folderMaybe.isEmpty()) {
            return false;
        }
        File pluginFolder = folderMaybe.get();

        ArrayList<File> jars = this.plGetAllJars(pluginFolder);

        File jar = null;
        for (File jarFile : jars) {
            Optional<PluginInfo> info = this.extractPluginInfo(jarFile);
            if (!info.isPresent()) {
                /*
                 * We don't have a valid plugin, the error was already logged
                 * when extracting plugin info
                 */
                continue;
            }
            if (pluginName.equals(info.get().getName())) {
                jar = jarFile;
                break;
            }
        }
        if (null == jar) {
            return false;
        }

        plLoadPlugins(Collections.singletonList(jar));
        return true;
    }

    /**
     * Load a list of plugins from a folder.
     *
     * @param path The path to the folder containing the file.
     * @param pluginNames The names of the plugins we want to load.
     */
    @Synchronized("pluginLock")
    public void loadPlugins(@NonNull String path, @NonNull List<String> pluginNames) {
        Optional<File> folderMaybe = this.plGetFolder(path);
        if (folderMaybe.isEmpty()) {
            return;
        }
        if (pluginNames.isEmpty()) {
            return;
        }
        File pluginFolder = folderMaybe.get();

        ArrayList<File> jars = this.plGetAllJars(pluginFolder);

        Map<File, PluginInfo> jarInfoMap = new HashMap<>();
        for (File jarFile : jars) {
            Optional<PluginInfo> info = this.extractPluginInfo(jarFile);
            if (!info.isPresent()) {
                /*
                 * We don't have a valid plugin, the error was already logged
                 * when extracting plugin info
                 */
                continue;
            }
            jarInfoMap.put(jarFile, info.get());
        }

        jarInfoMap.entrySet().removeIf(entry -> !pluginNames.contains(entry.getValue().getName()));
        jars.removeIf(file -> !jarInfoMap.keySet().contains(file));
        plLoadPlugins(jars);
    }

    /**
     * Log an alert about a plugin lifecycle, where plugin name and version are automatically
     * replaced.
     *
     * @param whichAlert The string to read from the resource bundle
     * @param pluginName The plugin that the alert is about
     */
    void logAlert(String whichAlert, String pluginName) {
        PluginDetails details = pluginDetails.get(pluginName);
        String version;
        if (details == null || details.getInfo() == null) {
            version = "?";
        } else {
            version = details.getInfo().getVersion();
        }
        String message = SafeResourceLoader.getString(whichAlert, getResourceBundle());
        log.info(message, pluginName, version);
    }

    /**
     * Log the fact that a plugin was not loaded, despite trying to take actions on it as if it were
     * loaded.
     *
     * @param plugin The plugin to log the alert about.
     */
    private void logNotLoaded(String plugin) {
        String tmp = SafeResourceLoader.getString("PLUGIN_NOT_LOADED", getResourceBundle());
        log.warn(tmp, plugin);
    }

    /**
     * Log the fact that a plugin had its state corrupted.
     *
     * @param plugin The plugin that was corrupted.
     */
    void logStateCorrupted(String plugin) {
        String msgCorrupted =
                SafeResourceLoader.getString("PLUGIN_STATE_CORRUPTED", resourceBundle);
        log.warn(msgCorrupted, plugin);
    }

    /**
     * Go through the list list of plugins, unload any older versions that may be loaded, and create
     * classloaders for each file.
     *
     * @param jarInfoMap The mapping of file to PluginInfo for the plugin represented by that jar.
     * @param skipped The list of files we have skipped over in this method, so we can ignore them
     *     later.
     * @param loaders The map of loaders for each file that should be populated by this method.
     */
    private void plCreateClassloaders(
            Map<File, PluginInfo> jarInfoMap,
            List<File> skipped,
            Map<File, PluginClassLoader> loaders) {
        sharedClassLoader = new SharedClassLoader(this, this.getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(sharedClassLoader);
        eventManager.setThreadClassloader(sharedClassLoader);

        for (Map.Entry<File, PluginInfo> entry : jarInfoMap.entrySet()) {
            PluginInfo info = entry.getValue();

            String pluginName = info.getName();
            if (isLoaded(pluginName)) {
                logAlert("ALERT_PLUGIN_ALREADY_LOADED", pluginName);

                boolean lowerVersion =
                        PluginManager.isNewerVersion(
                                info.getVersion(),
                                pluginDetails.get(pluginName).getInfo().getVersion());

                if (lowerVersion) {
                    unloadPlugin(pluginName);
                    // unload the old plugin and continue loading the new one
                } else {
                    logAlert("ALERT_PLUGIN_OUTDATED", pluginName);
                    skipped.add(entry.getKey());
                    continue;
                }
            }

            PluginClassLoader loader = null;
            try {
                loader = new PluginClassLoader(this, sharedClassLoader, entry.getKey());
            } catch (MalformedURLException e) {
                logAlert("PLUGIN_URL_INVALID", entry.getKey().getName());
            }

            if (loader == null) {
                continue;
            }
            loaders.put(entry.getKey(), loader);
        }
    }

    /**
     * Calculate the state of plugins that are being loaded based on their dependencies, and unload
     * the ones that are missing dependencies.
     */
    private void plDependencyResolutionStage() {
        /*
         * Resolve easy dependencies. Loop through all plugins once, if all
         * dependencies are satisfied (DISCOVERED, loaded/enabled,
         * DEPS_SATISFIED, generally existing in the system), or it has no
         * dependencies, mark it as DEPS_SATISFIED as well. If a plugin has
         * unsatisfied dependencies, such as something NOT_LOADED, mark the
         * plugin as DEPS_MISSING. If a plugin does not have missing
         * dependencies, but it has dependencies that are also DEPS_CHECKING,
         * mark it as DEPS_CHECKING.
         */
        for (Map.Entry<String, PluginDetails> entry : pluginDetails.entrySet()) {
            PluginDetails details = entry.getValue();
            if (!PluginState.DISCOVERED.equals(details.getState())) {
                continue;
            }
            PluginState state = calculatePluginState(details.getInfo());
            details.setState(state);
        }

        /*
         * Handle loops/clusters with BFS. Go through all nodes still
         * DEPS_CHECKING and: if all dependencies are DEPS_SATISFIED, the node
         * is DEPS_SATISFIED. If there are dependencies not found, or
         * DEPS_MISSING, the node and all parent nodes are set to DEPS_MISSING
         * and we move to a new node. If a child node is DEPS_CHECKING, and not
         * already in the tree, we tack it on and keep navigating down. Once we
         * completely exhaust reachable nodes, and everything is DEPS_SATISFIED
         * or DEPS_CHECKING, we can mark the whole tree as DEPS_SATISFIED.
         */

        List<String> stillChecking = findPluginsByState(PluginState.DEPS_CHECKING);
        while (!stillChecking.isEmpty()) {
            String name = stillChecking.get(0);
            plResolveDependencies(new PluginDependencyNode(name));
            stillChecking = findPluginsByState(PluginState.DEPS_CHECKING);
        }

        // Report and unload all DEPS_MISSING plugins

        for (String pluginName : findPluginsByState(PluginState.DEPS_MISSING)) {
            logAlert("PLUGIN_DEPENDENCY_MISSING", pluginName);
            PluginDetails details = pluginDetails.remove(pluginName);
            details.dispose();
        }
    }

    /**
     * Go through the list of jars and remove the ones that don't have valid info. Populates the
     * info map with info if found.
     *
     * @param jars The jars we are looking at. This list will be modified.
     * @param jarInfoMap The map of info for each jar. This list will be modified.
     */
    private void plDiscardInvalidPlugins(List<File> jars, Map<File, PluginInfo> jarInfoMap) {
        // grab all the plugin info from them, discard invalid plugins
        for (File jarFile : jars) {
            Optional<PluginInfo> info = extractPluginInfo(jarFile);
            if (!info.isPresent()) {
                /*
                 * We don't have a valid plugin, the error was already logged
                 * when extracting plugin info
                 */
                continue;
            }
            jarInfoMap.put(jarFile, info.get());
        }
    }

    /**
     * Return all jar files in the specified folder. If none are found, an empty list is returned.
     *
     * @param folder a valid folder
     * @return a list of all readable jars in the folder
     */
    private ArrayList<File> plGetAllJars(final File folder) {
        File[] files;
        files = folder.listFiles();

        ArrayList<File> jars = new ArrayList<>();
        if (files == null) {
            String msg = SafeResourceLoader.getString("PLUGIN_FILES_NULL", resourceBundle);
            log.warn(msg, folder.getAbsolutePath());
            return jars;
        }
        if (files.length == 0) {
            String msg = SafeResourceLoader.getString("PLUGIN_FOLDER_EMPTY", resourceBundle);
            log.warn(msg, folder.getAbsolutePath());
            return jars;
        }

        for (File file : files) {
            try {
                if (!file.exists()
                        || !file.canRead()
                        || file.isDirectory()
                        || !file.getName().endsWith(".jar")) {
                    continue;
                }
            } catch (SecurityException secExcep) {
                String msg =
                        SafeResourceLoader.getString("PLUGIN_FILE_SECURITY_ERR", resourceBundle);
                log.debug(msg, file.getName());
                // Maybe one or more files can't be read
                continue;
            }
            jars.add(file);
        }

        return jars;
    }

    /**
     * For plugin loading. The return value is an empty optional if there was a problem accessing
     * the folder from path. If there is a file in the optional it should be an existing folder that
     * can be read.
     *
     * @param path the path of the folder to return as a File
     * @return an optional containing the folder or empty if there was an error
     */
    private Optional<File> plGetFolder(final String path) {
        File pluginFolder;
        try {
            pluginFolder = new File(path);
        } catch (NullPointerException nullExcept) {
            String msg = SafeResourceLoader.getString("PLUGIN_PATH_NULL", resourceBundle);
            log.warn(msg);
            return Optional.empty();
        }
        try {
            if (!pluginFolder.exists()) {
                String msg =
                        SafeResourceLoader.getString("PLUGIN_FOLDER_NOT_FOUND", resourceBundle);
                log.warn(msg, pluginFolder.getAbsolutePath());
                return Optional.empty();
            }
            if (!pluginFolder.isDirectory()) {
                String msg =
                        SafeResourceLoader.getString("PLUGIN_FOLDER_NOT_FOLDER", resourceBundle);
                log.warn(msg, pluginFolder.getAbsolutePath());
                return Optional.empty();
            }
            if (!pluginFolder.canRead()) {
                String msg =
                        SafeResourceLoader.getString("PLUGIN_FOLDER_UNREADABLE", resourceBundle);
                log.warn(msg, pluginFolder.getAbsolutePath());
                return Optional.empty();
            }
        } catch (SecurityException securExcep) {
            String msg =
                    SafeResourceLoader.getString("PLUGIN_FOLDER_ACCESS_DENIED", resourceBundle);
            log.warn(msg, path);
            return Optional.empty();
        }
        return Optional.of(pluginFolder);
    }

    /**
     * Attempt to instantiate a plugins main class using the classloader for the jar that contains
     * it.
     *
     * @param pluginInfo The plugin info for the plugin.
     * @param classLoader The classloader that can find classes in the plugin jar.
     * @return The newly created Plugin object.
     * @throws InvalidPluginException If there was an issue creating the plugin.
     */
    private Plugin plInstantiatePluginClass(
            final PluginInfo pluginInfo, PluginClassLoader classLoader)
            throws InvalidPluginException {

        Class<?> clazz;
        try {
            clazz = Class.forName(pluginInfo.getMainClass(), true, classLoader);
        } catch (ClassNotFoundException e) {
            String err = SafeResourceLoader.getString("PLUGIN_MAIN_METHOD_MISSING", resourceBundle);
            log.warn(err, pluginInfo.getName());
            throw new InvalidPluginException(err);
        }

        Class<? extends Plugin> pluginClass;
        try {
            pluginClass = clazz.asSubclass(Plugin.class);
            return pluginClass.getDeclaredConstructor().newInstance();
        } catch (ClassCastException ex) {
            String err = SafeResourceLoader.getString("PLUGIN_MAIN_NOT_A_PLUGIN", resourceBundle);
            log.warn(err, pluginInfo.getName());
            throw new InvalidPluginException(err);
        } catch (InstantiationException e) {
            String err =
                    SafeResourceLoader.getString("PLUGIN_CANT_INSTANTIATE_MAIN", resourceBundle);
            log.warn(err, pluginInfo.getName());
            throw new InvalidPluginException(err);
        } catch (IllegalAccessException e) {
            String err = SafeResourceLoader.getString("PLUGIN_MAIN_ILLEGAL_ACCESS", resourceBundle);
            log.warn(err, pluginInfo.getName());
            throw new InvalidPluginException(err);
        } catch (IllegalArgumentException e) {
            String err =
                    SafeResourceLoader.getString("PLUGIN_MAIN_ILLEGAL_ARGUMENT", resourceBundle);
            log.warn(err, pluginInfo.getName());
            throw new InvalidPluginException(err);
        } catch (InvocationTargetException e) {
            String err =
                    SafeResourceLoader.getString("PLUGIN_MAIN_INVOCATION_TARGET", resourceBundle);
            log.warn(err, pluginInfo.getName());
            throw new InvalidPluginException(err);
        } catch (NoSuchMethodException e) {
            String err = SafeResourceLoader.getString("PLUGIN_MAIN_METHOD_MISSING", resourceBundle);
            log.warn(err, pluginInfo.getName());
            throw new InvalidPluginException(err);
        } catch (SecurityException e) {
            String err = SafeResourceLoader.getString("PLUGIN_MAIN_SECURITY", resourceBundle);
            log.warn(err, pluginInfo.getName());
            throw new InvalidPluginException(err);
        }
    }

    /**
     * Attempt to load all the provided jars as plugins.
     *
     * @param jars The jars we want to load.
     */
    private void plLoadPlugins(@NonNull List<File> jars) {
        Map<File, PluginInfo> jarInfoMap = new HashMap<>();

        plDiscardInvalidPlugins(jars, jarInfoMap);

        // Plugins that were already had a newer version loaded
        List<File> skipped = new ArrayList<>();

        Map<File, PluginClassLoader> loaders = new HashMap<>();

        plCreateClassloaders(jarInfoMap, skipped, loaders);

        for (Map.Entry<File, PluginInfo> entry : jarInfoMap.entrySet()) {
            if (skipped.contains(entry.getKey())) {
                continue;
            }
            PluginInfo info = entry.getValue();
            PluginClassLoader loader = loaders.get(entry.getKey());
            PluginDetails details = new PluginDetails(loader, info, null, PluginState.DISCOVERED);

            pluginDetails.put(info.getName(), details);
        }

        for (Map.Entry<File, PluginInfo> entry : jarInfoMap.entrySet()) {
            if (skipped.contains(entry.getKey())) {
                continue;
            }
            PluginInfo info = entry.getValue();

            PluginDetails details = pluginDetails.get(info.getName());

            Plugin plugin;
            try {
                plugin = plInstantiatePluginClass(entry.getValue(), details.getClassLoader());
                details.setPlugin(plugin);
            } catch (InvalidPluginException e) {
                // The method already logs the problem
                loaders.remove(entry.getKey());
                details.getClassLoader().dispose();
                pluginDetails.remove(info.getName());
                continue;
            }

            logAlert("ALERT_DISCOVERED", info.getName());
        }

        plDependencyResolutionStage();
        plLoadSatisfiedDependencies();
    }

    /** Load the satisfied dependencies, and enable them if configured to do so on load. */
    private void plLoadSatisfiedDependencies() {
        /*
         * All plugins should now be DEPS_SATISFIED, so load them all. During
         * the onLoad() method, plugins should deal with connecting to plugins
         * that may be in a dependency loop.
         */
        List<String> toLoad = findPluginsByState(PluginState.DEPS_SATISFIED);

        // we don't want people making assumptions about load order
        Collections.shuffle(toLoad);

        // we want to be able to quickly remove arbitrary elements
        LinkedList<String> unchecked = new LinkedList<>(toLoad);

        ArrayDeque<String> loadQueue = new ArrayDeque<>();

        while (!unchecked.isEmpty()) {
            String current = unchecked.poll();
            plTraverseDependencies(unchecked, current, loadQueue);
        }

        loadQueue.forEach(this::plLoadSinglePlugin);

        /*
         * After all plugins have been loaded, now they can be enabled (if that
         * configuration is set). At this point problems with loops should have
         * been resolved enough that the plugins can start in any order.
         */
        if (enableOnLoad) {
            toLoad.forEach(this::enable);
        }
    }

    /**
     * Load a single plugin, given the name. Used in {@link #plLoadSatisfiedDependencies()} to load
     * in dependency order.
     *
     * @param pluginName The name of the plugin to load.
     */
    private void plLoadSinglePlugin(String pluginName) {
        setPluginState(pluginName, PluginState.LOADING);
        logAlert("ALERT_LOADING", pluginName);

        PluginDetails details = pluginDetails.get(pluginName);
        Plugin plugin = details.getPlugin();

        if (isCommandLine()) {
            String lastVersion = PluginFolder.getLastVersionUsed(pluginName);
            String newVersion = details.getInfo().getVersion();

            if (PluginManager.isNewerVersion(newVersion, lastVersion)) {
                plugin.onUpgrade(lastVersion);
                PluginFolder.setLastVersionUsed(pluginName, newVersion);
            }
        }

        if (!plugin.onLoad()) {
            logAlert("PLUGIN_LOAD_FAIL", pluginName);
            unloadPlugin(pluginName);
        }
        for (Listener l : plugin.getListeners()) {
            eventManager.registerEventListeners(l);
        }
        String msg = SafeResourceLoader.getString("ALERT_REG_EVENT_LISTENERS", resourceBundle);
        log.debug(msg, pluginName);
        setPluginState(pluginName, PluginState.DISABLED);

        logAlert("ALERT_LOADED", pluginName);
        new PluginLoaded(pluginName).fire();
    }

    /**
     * Go through and mark everything in the tree starting at the given root to have it's
     * dependencies satisfied.
     *
     * @param root The root node to start marking satisfied from.
     */
    private void plMarkSatisfied(PluginDependencyNode root) {
        ArrayDeque<PluginDependencyNode> queue = new ArrayDeque<>();
        PluginDependencyNode currentNode;
        /*
         * We went through the whole tree and never failed. Therefore we can go
         * mark everything as satisfied. Also we don't get to this point until
         * the queue is empty.
         */
        queue.add(root);
        while (!queue.isEmpty()) {
            currentNode = queue.pollFirst();
            setPluginState(currentNode.getName(), PluginState.DEPS_SATISFIED);
            // fine since we avoided cycles when setting up children earlier
            queue.addAll(currentNode.getChildren());
        }
    }

    /**
     * Resolve the dependencies of all children using a breadth-first search. Returns true if
     * everything is fine, but false if there is an unresolved dependency. This return value is used
     * to propagate failures up the tree. This will set the state of any plugin it reaches which is
     * still checking.
     *
     * @param root The root node we are building a tree from.
     */
    private void plResolveDependencies(PluginDependencyNode root) {
        if (null == root) {
            return;
        }
        List<String> namesInTheTree = new ArrayList<>();
        namesInTheTree.add(root.getName());
        ArrayDeque<PluginDependencyNode> queue = new ArrayDeque<>();

        PluginDependencyNode currentNode;
        queue.add(root);

        while (!queue.isEmpty()) {
            currentNode = queue.pollFirst();

            PluginInfo info = pluginDetails.get(currentNode.getName()).getInfo();
            for (String dependencyName : info.getDependencies()) {
                PluginState state = getPluginState(dependencyName);
                switch (state) {
                    case DEPS_CHECKING:
                        if (!namesInTheTree.contains(dependencyName)) {
                            namesInTheTree.add(dependencyName);
                            PluginDependencyNode child = new PluginDependencyNode(dependencyName);
                            child.setParent(root);
                            currentNode.getChildren().add(child);
                            queue.add(child);
                        }
                        break;
                    case DEPS_MISSING, CORRUPTED, NOT_LOADED, PENDING_REMOVAL, UNLOADING:
                        // propagate failure up to the root and bail
                        setPluginState(currentNode.getName(), PluginState.DEPS_MISSING);
                        PluginDependencyNode parent = currentNode.getParent();
                        while (null != parent) {
                            setPluginState(parent.getName(), PluginState.DEPS_MISSING);
                            parent = parent.getParent();
                        }
                        return;
                    case DEPS_SATISFIED,
                            DISABLED,
                            DISABLING,
                            DISCOVERED,
                            ENABLED,
                            ENABLING,
                            LOADING:
                    default:
                        // satisfied, we don't need to do anything here
                        break;
                }
            }
        }

        plMarkSatisfied(root);
    }

    /**
     * Append root and dependencies to the load queue so that dependencies load of a plugin load
     * before it does.
     *
     * @param unchecked All plugins that have not been visited or added to the load queue yet. This
     *     will be modified.
     * @param root The root of the dependency tree.
     * @param loadQueue The full list of plugins, in the order they should be loaded in. This will
     *     be modified.
     */
    private void plTraverseDependencies(
            List<String> unchecked, String root, ArrayDeque<String> loadQueue) {

        // we immediately remove the nodes we have seen before
        unchecked.remove(root);
        List<String> dependencies = new ArrayList<>();

        PluginInfo info = pluginDetails.get(root).getInfo();
        if (null == info) {
            // wasn't a real plugin.
            return;
        }

        dependencies.addAll(info.getDependencies());
        dependencies.addAll(info.getSoftDependencies());

        for (String dependency : dependencies) {
            if (!unchecked.contains(dependency)) {
                /*
                 * Skip already visited plugins, soft dependencies that don't
                 * exist.
                 */
                continue;
            }
            plTraverseDependencies(unchecked, dependency, loadQueue);
        }
        loadQueue.add(root);
    }

    /**
     * Attempts to register the command for the given class. If the command already exists, an error
     * is logged and the method returns false.
     *
     * @param command the command to register
     * @param callback The function to call when executing a command. Argument list is passed in.
     * @param owner The owner of the plugin
     * @return true if the command registered successfully
     */
    @Synchronized("commandLock")
    public boolean registerCommand(
            @NonNull final String command,
            @NonNull Consumer<List<String>> callback,
            @NonNull String owner) {
        if (this.isCommandRegistered(command)) {
            String msg =
                    SafeResourceLoader.getString(
                            "COMMAND_ALREADY_REGISTERED", this.getResourceBundle());
            log.warn(msg, command);
            return false;
        }

        PluginCommand cmd = new PluginCommand(callback, command, owner);
        this.commands.add(cmd);

        String msg = SafeResourceLoader.getString("REGISTERED_COMMAND", this.getResourceBundle());
        log.debug(msg, command);

        java.util.Collections.sort(this.commands);

        return true;
    }

    /** Registers commands with the registry that the plugin manager uses */
    @Synchronized("commandLock")
    private void registerCommands() {
        this.registerCommand(
                SafeResourceLoader.getString("COMMAND_ENABLE", this.resourceBundle),
                this::callbackEnable,
                PluginManager.PLUGIN_NAME);
        this.registerCommand(
                SafeResourceLoader.getString("COMMAND_DISABLE", this.resourceBundle),
                this::callbackDisable,
                PluginManager.PLUGIN_NAME);
        this.registerCommand(
                SafeResourceLoader.getString("COMMAND_LOAD", this.resourceBundle),
                this::callbackLoad,
                PluginManager.PLUGIN_NAME);
        this.registerCommand(
                SafeResourceLoader.getString("COMMAND_UNLOAD", this.resourceBundle),
                this::callbackUnload,
                PluginManager.PLUGIN_NAME);
        this.registerCommand(
                SafeResourceLoader.getString("COMMAND_RELOAD", this.resourceBundle),
                this::callbackReload,
                PluginManager.PLUGIN_NAME);
        this.registerCommand(
                SafeResourceLoader.getString("COMMAND_LIST_PLUGINS", this.resourceBundle),
                this::callbackPrintPlugins,
                PluginManager.PLUGIN_NAME);
        this.registerCommand(
                SafeResourceLoader.getString("COMMAND_HELP", this.resourceBundle),
                this::callbackHelp,
                PluginManager.PLUGIN_NAME);
    }

    /**
     * This is essentially restarting the plugins. The plugin is disabled if it is enabled,
     * unloaded, then loaded.
     *
     * @param target The name of the plugin to reload
     * @return true if the plugin reloaded successfully, false otherwise
     */
    @Synchronized("pluginLock")
    public boolean reload(@NonNull String target) {
        if (!this.isLoaded(target)) {
            logNotLoaded(target);
            return false;
        }

        if (this.isEnabled(target) && !this.disable(target)) {
            // disable failed
            this.setPluginState(target, PluginState.CORRUPTED);
            return false;
        }
        this.setPluginState(target, PluginState.UNLOADING);
        this.unloadPlugin(target);
        this.setPluginState(target, PluginState.LOADING);
        this.loadPlugin(System.getProperty("user.dir") + Constants.PLUGIN_FOLDER_PATH, target);
        return true;
    }

    /**
     * Removes the class by name in the cached class list.
     *
     * @param name The name of the class.
     */
    void removeClass(final String name) {
        pluginClassCache.remove(name);
    }

    /**
     * Stores the class by name in the cached class list.
     *
     * @param name The name of the class.
     * @param clazz The corresponding class object.
     */
    void setClass(@NonNull final String name, @NonNull final Class<?> clazz) {
        pluginClassCache.computeIfAbsent(name, ignored -> clazz);
    }

    @Synchronized("pluginLock")
    private boolean setPluginState(String target, PluginState newState) {

        if (!this.pluginDetails.containsKey(target)) {
            return false;
        }

        // replaces the old state
        PluginState oldState = this.pluginDetails.get(target).getState();
        if (PluginState.PENDING_REMOVAL.equals(oldState)) {
            // can't change plugins pending removal
            return false;
        }
        this.pluginDetails.get(target).setState(newState);
        return true;
    }

    private void shutdown() {
        eventManager.unregisterEventListeners(commandListener);

        synchronized (pluginLock) {
            List<String> toUnload = new ArrayList<>(pluginDetails.keySet());
            for (String s : toUnload) {
                unloadPlugin(s);
            }
        }
        synchronized (commandLock) {
            clearCommands();
            commands = null;
        }
    }

    /**
     * Attempts to unload the plugin from memory. If no plugin exists with the given name ({@link
     * #isLoaded(String)}), returns false and does nothing. Any plugins that depend on the specified
     * plugin are also unloaded.
     *
     * @param toUnload The name of the plugin to unload
     * @return true if the plugin was unloaded properly
     */
    @Synchronized("pluginLock")
    public boolean unloadPlugin(@NonNull final String toUnload) {
        if (!this.isLoaded(toUnload)) {
            logAlert("PLUGIN_NOT_LOADED", toUnload);
            PluginDetails removedDetails = this.pluginDetails.remove(toUnload);
            if (null != removedDetails) {
                removedDetails.dispose();
            }
            return false;
        }

        ArrayDeque<String> needsUnload = new ArrayDeque<>();
        ArrayDeque<String> processingQueue = new ArrayDeque<>();
        processingQueue.add(toUnload);

        /*
         * Add all plugins to a queue in order as if doing a Breadth-First
         * Search.
         */
        while (!processingQueue.isEmpty()) {
            String next = processingQueue.poll();
            List<String> dependents =
                    pluginDetails.entrySet().stream()
                            .filter(
                                    entry ->
                                            entry.getValue()
                                                    .getInfo()
                                                    .getDependencies()
                                                    .contains(next))
                            .map(Entry::getKey)
                            .collect(Collectors.toCollection(ArrayList::new));

            for (String dependent : dependents) {
                if (processingQueue.contains(dependent) || needsUnload.contains(dependent)) {
                    continue;
                }
                processingQueue.add(dependent);
            }
            if (!needsUnload.contains(next)) {
                needsUnload.add(next);
            }
        }

        boolean success = true;
        /*
         * We unload from deepest dependency first, starting with the last thing
         * added to the list and working our way back to the original plugin.
         */
        while (!needsUnload.isEmpty()) {
            /*
             * Keep the unload operation first so we don't short circuit and
             * skip unloading if something fails.
             */
            success = unloadSingle(needsUnload.pop()) && success;
        }

        return success;
    }

    /**
     * The logic for unloading a single plugin.
     *
     * @param toUnload The name of the plugin to unload.
     * @return true if the plugin was unloaded properly.
     * @see #unloadPlugin(String)
     */
    private boolean unloadSingle(@NonNull final String toUnload) {
        logAlert("ALERT_UNLOADING", toUnload);

        PluginDetails details = pluginDetails.get(toUnload);

        if (null == details) {
            String notLoaded =
                    SafeResourceLoader.getString("PLUGIN_LOADED_BUT_NULL", resourceBundle);
            log.warn(notLoaded, toUnload);
            return false;
        }

        Plugin plugin = details.getPlugin();

        if (null == plugin) {
            String notLoaded =
                    SafeResourceLoader.getString("PLUGIN_LOADED_BUT_NULL", resourceBundle);
            log.warn(notLoaded, toUnload);
            details.dispose();
            pluginDetails.remove(toUnload);
            return false;
        }

        // It has to be disabled before unloading.
        if (isEnabled(toUnload)) {
            disable(toUnload);
        }
        setPluginState(toUnload, PluginState.UNLOADING);

        if (!plugin.onUnload()) {
            String notLoaded = SafeResourceLoader.getString("PLUGIN_UNLOAD_FAIL", resourceBundle);
            log.warn(notLoaded, toUnload);
            setPluginState(toUnload, PluginState.CORRUPTED);
            return false;
        }

        new PluginUnloaded(toUnload).fire();

        for (Listener l : plugin.getListeners()) {
            eventManager.unregisterEventListeners(l);
        }
        String unreg = SafeResourceLoader.getString("ALERT_UNREG_EVENT_LISTENERS", resourceBundle);
        log.debug(unreg, toUnload);

        details = pluginDetails.remove(toUnload);
        details.dispose();

        logAlert("ALERT_UNLOADED", toUnload);
        return true;
    }

    /**
     * Unregisters the given command.
     *
     * @param command the command to remove
     * @return true if the command was removed
     */
    @Synchronized("commandLock")
    public boolean unregisterCommand(@NonNull final String command) {
        boolean found = false;

        if (this.isCommandRegistered(command)) {
            this.commands.removeIf(cmd -> command.equalsIgnoreCase(cmd.getCommand()));

            String msg =
                    SafeResourceLoader.getString("UNREGISTERED_COMMAND", this.getResourceBundle());
            log.debug(msg, command);
            found = true;
        }
        return found;
    }

    /**
     * Removes all commands that the given plugin registered.
     *
     * @param owner the plugin which is having commands removed
     */
    @Synchronized("commandLock")
    public void unregisterPluginCommands(@NonNull String owner) {
        this.commands.stream()
                .filter(command -> owner.equalsIgnoreCase(command.getOwner()))
                .forEach(command -> this.unregisterCommand(command.getCommand()));
    }
}
