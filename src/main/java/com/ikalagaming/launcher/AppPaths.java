package com.ikalagaming.launcher;

import com.ikalagaming.util.SystemProperties;

import lombok.NonNull;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Where the application's files are. There are three kinds of folder:
 *
 * <ul>
 *   <li><b>Install</b>: the folder holding the Ikala-Core jar, with its plugins and libraries. It
 *       is never written to while the application runs.
 *   <li><b>Home</b>: one per user per machine, for settings, logs, saves and anything else the
 *       application writes. The cache is a separate folder, as it can be large and deleted at any
 *       time.
 *   <li><b>Instance</b>: an optional folder given on the command line, such as the save a server
 *       hosts. A server with an instance keeps its logs there.
 * </ul>
 *
 * <p>The application's name comes from {@value #APP_FILE} in the install folder, so the engine
 * stays independent of the game. Each folder can be overridden with a system property, and the
 * launcher also accepts them as command line options. A file named {@value #PORTABLE_FILE} in the
 * install folder keeps the Home and cache inside the install, in {@value #PORTABLE_HOME}.
 *
 * <p>This class must not log: it decides where the log files go, so it runs before logging is set
 * up.
 */
public final class AppPaths {

    /** The file in the install folder that names the application. */
    public static final String APP_FILE = "app.yml";

    /** The key for the application name in {@value #APP_FILE}. */
    public static final String APP_NAME_KEY = "name";

    /** The name used when there is no {@value #APP_FILE}. */
    public static final String DEFAULT_APP_NAME = "Ikala";

    /** The system property that overrides the install folder. */
    public static final String INSTALL_PROPERTY = "ikala.install";

    /** The system property that overrides the Home folder. */
    public static final String HOME_PROPERTY = "ikala.home";

    /** The system property that overrides the cache folder. */
    public static final String CACHE_PROPERTY = "ikala.cache";

    /** The system property that names the instance folder. */
    public static final String INSTANCE_PROPERTY = "ikala.instance";

    /** The system property the log configuration reads the log folder from. */
    public static final String LOG_DIR_PROPERTY = "ikala.logDir";

    /** A file in the install folder that keeps the Home and cache inside the install. */
    public static final String PORTABLE_FILE = "portable";

    /** The folder in the install that a portable install uses as its Home. */
    public static final String PORTABLE_HOME = "home";

    /** The folder for user overrides of shipped settings, in the Home. */
    public static final String SETTINGS_FOLDER = "settings";

    /** The folder for state the application keeps between runs, in the Home. */
    public static final String STATE_FOLDER = "state";

    /** The folder for logs, in the Home or an instance. */
    public static final String LOGS_FOLDER = "logs";

    /** The folder for crash reports, in the Home. */
    public static final String CRASH_REPORTS_FOLDER = "crash-reports";

    /** The folder for benchmark results, dumps and other generated reports, in the Home. */
    public static final String REPORTS_FOLDER = "reports";

    /** The folder for screenshots and frame captures, in the Home. */
    public static final String SCREENSHOTS_FOLDER = "screenshots";

    /** The cache folder's name inside a Home, when the Home holds the cache. */
    public static final String CACHE_FOLDER = "cache";

    /** The folder for plugins, in the install. */
    public static final String PLUGINS_FOLDER = "plugins";

    /** The folder for shared libraries, in the install. */
    public static final String LIBRARIES_FOLDER = "libs";

    /** The resolved folders, worked out on first use. */
    private static Resolved resolved;

    /** The lock for {@link #resolved}. */
    private static final ReentrantLock LOCK = new ReentrantLock();

    /**
     * The folders, once worked out.
     *
     * @param install The install folder.
     * @param appName The application name.
     * @param home The Home folder.
     * @param cache The cache folder.
     * @param instance The instance folder, if there is one.
     */
    private record Resolved(
            @NonNull Path install,
            @NonNull String appName,
            @NonNull Path home,
            @NonNull Path cache,
            Path instance) {}

    /**
     * The application's name, from {@value #APP_FILE} in the install folder.
     *
     * @return The name.
     */
    public static String appName() {
        return AppPaths.get().appName();
    }

    /**
     * The cache folder. Everything in it can be deleted at any time.
     *
     * @return The cache folder.
     */
    public static Path cache() {
        return AppPaths.get().cache();
    }

    /**
     * Resolve a path against a base folder, leaving absolute paths as they are.
     *
     * @param base The folder a relative path is in.
     * @param path The path.
     * @return The path, absolute if the base is.
     */
    public static Path resolve(@NonNull Path base, @NonNull String path) {
        Path given = Path.of(path);
        return given.isAbsolute() ? given : base.resolve(given);
    }

    /**
     * The folder for crash reports.
     *
     * @return The crash reports folder, in the Home.
     */
    public static Path crashReports() {
        return AppPaths.home().resolve(AppPaths.CRASH_REPORTS_FOLDER);
    }

    /**
     * Find the folders, if that has not happened yet.
     *
     * @return The folders.
     */
    private static Resolved get() {
        AppPaths.LOCK.lock();
        try {
            if (AppPaths.resolved == null) {
                AppPaths.resolved = AppPaths.resolveAll();
            }
            return AppPaths.resolved;
        } finally {
            AppPaths.LOCK.unlock();
        }
    }

    /**
     * The Home folder, for everything the application writes except the cache.
     *
     * @return The Home folder.
     */
    public static Path home() {
        return AppPaths.get().home();
    }

    /**
     * The folder the application is installed in, which holds the Ikala-Core jar.
     *
     * @return The install folder.
     */
    public static Path install() {
        return AppPaths.get().install();
    }

    /**
     * The instance folder given on the command line, such as the save a server hosts.
     *
     * @return The instance folder, or empty if there isn't one.
     */
    public static Optional<Path> instance() {
        return Optional.ofNullable(AppPaths.get().instance());
    }

    /**
     * The folder for shared libraries.
     *
     * @return The library folder, in the install.
     */
    public static Path libraries() {
        return AppPaths.install().resolve(AppPaths.LIBRARIES_FOLDER);
    }

    /**
     * The folder log files go in: the instance's if there is one, otherwise the Home's.
     *
     * @return The log folder.
     */
    public static Path logs() {
        return AppPaths.instance().orElseGet(AppPaths::home).resolve(AppPaths.LOGS_FOLDER);
    }

    /**
     * The folder for plugins that ship with the application.
     *
     * @return The plugin folder, in the install.
     */
    public static Path plugins() {
        return AppPaths.install().resolve(AppPaths.PLUGINS_FOLDER);
    }

    /**
     * The folder for generated reports, such as benchmark results and dumps.
     *
     * @return The reports folder, in the Home.
     */
    public static Path reports() {
        return AppPaths.home().resolve(AppPaths.REPORTS_FOLDER);
    }

    /**
     * Forget the folders, so they are worked out again from the current system properties on next
     * use. For tests and for the launcher after it reads the command line.
     */
    public static void reset() {
        AppPaths.LOCK.lock();
        try {
            AppPaths.resolved = null;
        } finally {
            AppPaths.LOCK.unlock();
        }
    }

    /**
     * The folder for screenshots and frame captures.
     *
     * @return The screenshots folder, in the Home.
     */
    public static Path screenshots() {
        return AppPaths.home().resolve(AppPaths.SCREENSHOTS_FOLDER);
    }

    /**
     * The folder for user overrides of settings that ship with the application.
     *
     * @return The settings folder, in the Home.
     */
    public static Path settings() {
        return AppPaths.home().resolve(AppPaths.SETTINGS_FOLDER);
    }

    /**
     * The folder for state kept between runs, such as the last version of each plugin.
     *
     * @return The state folder, in the Home.
     */
    public static Path state() {
        return AppPaths.home().resolve(AppPaths.STATE_FOLDER);
    }

    /**
     * Work out every folder from the system properties, the install folder and the OS.
     *
     * @return The folders.
     */
    private static Resolved resolveAll() {
        final Path install =
                AppPaths.property(AppPaths.INSTALL_PROPERTY).orElseGet(AppPaths::findInstall);
        final String appName = AppPaths.readAppName(install);
        final boolean portable = Files.exists(install.resolve(AppPaths.PORTABLE_FILE));

        final Optional<Path> homeOverride = AppPaths.property(AppPaths.HOME_PROPERTY);
        final Path home;
        if (homeOverride.isPresent()) {
            home = homeOverride.get();
        } else if (portable) {
            home = install.resolve(AppPaths.PORTABLE_HOME);
        } else {
            home = AppPaths.defaultHome(appName);
        }

        final Optional<Path> cacheOverride = AppPaths.property(AppPaths.CACHE_PROPERTY);
        final Path cache;
        if (cacheOverride.isPresent()) {
            cache = cacheOverride.get();
        } else if (homeOverride.isPresent() || portable) {
            // A moved Home keeps its cache with it
            cache = home.resolve(AppPaths.CACHE_FOLDER);
        } else {
            cache = AppPaths.defaultCache(appName);
        }

        final Path instance = AppPaths.property(AppPaths.INSTANCE_PROPERTY).orElse(null);
        return new Resolved(install, appName, home, cache, instance);
    }

    /**
     * The OS's usual folder for an application's data.
     *
     * @param appName The application name.
     * @return The default Home.
     */
    static Path defaultHome(@NonNull String appName) {
        final String userHome = System.getProperty("user.home");
        return switch (SystemProperties.getOS()) {
            case WINDOWS ->
                    AppPaths.envFolder("APPDATA")
                            .orElseGet(() -> Path.of(userHome, "AppData", "Roaming"))
                            .resolve(appName);
            case MAC -> Path.of(userHome, "Library", "Application Support", appName);
            case LINUX ->
                    AppPaths.envFolder("XDG_DATA_HOME")
                            .orElseGet(() -> Path.of(userHome, ".local", "share"))
                            .resolve(appName.toLowerCase(Locale.ROOT));
            case UNKNOWN -> Path.of(userHome, "." + appName.toLowerCase(Locale.ROOT));
        };
    }

    /**
     * The OS's usual folder for an application's cache.
     *
     * @param appName The application name.
     * @return The default cache.
     */
    static Path defaultCache(@NonNull String appName) {
        final String userHome = System.getProperty("user.home");
        return switch (SystemProperties.getOS()) {
            case WINDOWS ->
                    AppPaths.envFolder("LOCALAPPDATA")
                            .orElseGet(() -> Path.of(userHome, "AppData", "Local"))
                            .resolve(appName)
                            .resolve(AppPaths.CACHE_FOLDER);
            case MAC -> Path.of(userHome, "Library", "Caches", appName);
            case LINUX ->
                    AppPaths.envFolder("XDG_CACHE_HOME")
                            .orElseGet(() -> Path.of(userHome, ".cache"))
                            .resolve(appName.toLowerCase(Locale.ROOT));
            case UNKNOWN -> AppPaths.defaultHome(appName).resolve(AppPaths.CACHE_FOLDER);
        };
    }

    /**
     * A folder named by an environment variable, if it is set.
     *
     * @param variable The variable.
     * @return The folder, or empty if it isn't set.
     */
    private static Optional<Path> envFolder(@NonNull String variable) {
        final String value = System.getenv(variable);
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(Path.of(value));
    }

    /**
     * The folder holding the jar this class was loaded from. Run from a folder of classes, such as
     * in tests or an IDE, it is the working directory instead.
     *
     * @return The install folder.
     */
    private static Path findInstall() {
        final Path workingDirectory = Path.of(System.getProperty("user.dir"));
        try {
            final CodeSource source = AppPaths.class.getProtectionDomain().getCodeSource();
            if (source == null || source.getLocation() == null) {
                return workingDirectory;
            }
            final Path location = Path.of(source.getLocation().toURI());
            if (Files.isRegularFile(location)) {
                return location.toAbsolutePath().getParent();
            }
        } catch (URISyntaxException | InvalidPathException | SecurityException e) {
            // Fall back to the working directory below
        }
        return workingDirectory;
    }

    /**
     * A folder named by a system property, if it is set.
     *
     * @param name The property.
     * @return The folder, made absolute, or empty if the property isn't set.
     */
    private static Optional<Path> property(@NonNull String name) {
        final String value = System.getProperty(name);
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(Path.of(value).toAbsolutePath().normalize());
    }

    /**
     * Read the application name from {@value #APP_FILE}.
     *
     * @param install The install folder.
     * @return The name, or {@value #DEFAULT_APP_NAME} if there is no file or no name in it.
     */
    private static String readAppName(@NonNull Path install) {
        final Path file = install.resolve(AppPaths.APP_FILE);
        if (!Files.isRegularFile(file)) {
            return AppPaths.DEFAULT_APP_NAME;
        }
        try (InputStream stream = Files.newInputStream(file)) {
            final Object contents = new Yaml().load(stream);
            if (contents instanceof Map<?, ?> map
                    && map.get(AppPaths.APP_NAME_KEY) instanceof String name
                    && AppPaths.isValidName(name)) {
                return name.strip();
            }
        } catch (IOException | RuntimeException e) {
            // A broken file falls back to the default name; logging isn't set up yet
            System.err.println( // NOSONAR
                    "Could not read the application name from " + file + ": " + e);
        }
        return AppPaths.DEFAULT_APP_NAME;
    }

    /**
     * Whether a name can be used as a folder name on every OS.
     *
     * @param name The name.
     * @return Whether it is usable.
     */
    static boolean isValidName(@NonNull String name) {
        return name.strip().matches("[0-9A-Za-z][0-9A-Za-z _.-]*");
    }

    /** Private constructor so this class is not instantiated. */
    private AppPaths() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
