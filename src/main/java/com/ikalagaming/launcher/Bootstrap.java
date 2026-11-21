package com.ikalagaming.launcher;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The entry point of the jar. It reads the folder options from the command line and decides where
 * the log files go, before anything starts logging, then hands over to the {@link Launcher}.
 *
 * <p>This class must not log: logging is configured from the {@value AppPaths#LOG_DIR_PROPERTY}
 * property it sets, the first time anything logs.
 */
public final class Bootstrap {

    /** Command line options that set a folder, and the system property each one sets. */
    static final Map<String, String> FOLDER_OPTIONS =
            Map.of(
                    "--home", AppPaths.HOME_PROPERTY,
                    "--cache", AppPaths.CACHE_PROPERTY,
                    "--instance", AppPaths.INSTANCE_PROPERTY);

    /**
     * Start the application.
     *
     * @param args Arguments from the command line.
     */
    public static void main(String[] args) {
        final List<String> remaining = Bootstrap.applyFolderOptions(args);
        if (remaining == null) {
            return;
        }
        Bootstrap.setLogFolder();
        Launcher.main(remaining.toArray(String[]::new));
    }

    /**
     * Turn the folder options into system properties, so {@link AppPaths} sees them.
     *
     * @param args Arguments from the command line.
     * @return The other arguments, or null if a folder option is missing its value.
     */
    static List<String> applyFolderOptions(@NonNull String[] args) {
        final List<String> remaining = new ArrayList<>();
        for (int i = 0; i < args.length; ++i) {
            final String property = Bootstrap.FOLDER_OPTIONS.get(args[i]);
            if (property == null) {
                remaining.add(args[i]);
                continue;
            }
            if (i + 1 >= args.length || args[i + 1].startsWith("-")) {
                System.out.println("Please give a folder after " + args[i]); // NOSONAR
                return null;
            }
            System.setProperty(property, args[i + 1]);
            ++i;
        }
        AppPaths.reset();
        return remaining;
    }

    /** Point the log configuration at the log folder, unless it was set by hand. */
    static void setLogFolder() {
        final String current = System.getProperty(AppPaths.LOG_DIR_PROPERTY);
        if (current == null || current.isBlank()) {
            System.setProperty(AppPaths.LOG_DIR_PROPERTY, AppPaths.logs().toString());
        }
    }

    /** Private constructor so this class is not instantiated. */
    private Bootstrap() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
