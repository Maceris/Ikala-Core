package com.ikalagaming.util;

import java.io.File;

/**
 * Contains methods for fetching various system information such as the Java version or the
 * directory in which applications store data.
 *
 * @author Ches Burks
 */
public class SystemProperties {

    /** A classification of operating systems. */
    public enum OperatingSystem {
        WINDOWS,
        MAC,
        LINUX,
        UNKNOWN
    }

    /** The type of operating system the engine is running on */
    private static OperatingSystem operatingSystem;

    /** Equivalent to the current systems appdata folder */
    private static String homeDirectory;

    /**
     * Returns the home directory for the program. This is the application data folder and
     * <b>NOT</b> the games root folder. The games folder is contained in this directory. For
     * example, the game 'TextGame's folder would look like <code>getHomeDir() + "/TextGame/"</code>
     * on a Windows computer.
     *
     * <table>
     * <tr>
     * <th>Operating System</th>
     * <th>Directory</th>
     * </tr>
     * <tr>
     * <td>WINDOWS</td>
     * <td><code>System.getenv("APPDATA")</code></td>
     * </tr>
     * <tr>
     * <td><code>MAC</code></td>
     * <td>Users home directory +
     * <code>"/Library/Application Support"</code></td>
     * </tr>
     * <tr>
     * <td><code>LINUX</code></td>
     * <td>Users home directory</td>
     * </tr>
     * <tr>
     * <td><code>UNKNOWN(anything else)</code></td>
     * <td>The user's current working directory</td>
     * </tr>
     * </table>
     *
     * <p>The users home directory is defined as <code>System.getProperty("user.home")</code>
     *
     * @return the programs home directory
     */
    public static String getHomeDir() {
        if (homeDirectory == null) {
            obtainHomeDir();
        }
        return homeDirectory;
    }

    /**
     * Returns the type of operating system that is running.
     *
     * @return The type of operating system.
     */
    public static OperatingSystem getOS() {
        if (operatingSystem == null) {
            obtainOS();
        }
        return operatingSystem;
    }

    /**
     * Determines the application data folder based on the OS type. Sets the home dir string to the
     * folder path.
     */
    private static void obtainHomeDir() {
        if (operatingSystem == null) {
            obtainOS();
        }
        homeDirectory =
                switch (operatingSystem) {
                    case WINDOWS -> System.getenv("APPDATA");
                    case MAC ->
                            System.getProperty("user.home")
                                    + File.separator
                                    + "Library"
                                    + File.separator
                                    + "Application Support";
                    case LINUX -> System.getProperty("user.home");
                    case UNKNOWN -> System.getProperty("user.dir");
                };
    }

    /** Determines what operating system is running and sets the OS string. */
    private static void obtainOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            operatingSystem = OperatingSystem.WINDOWS;
        } else if (osName.contains("mac")) {
            operatingSystem = OperatingSystem.MAC;
        } else if (osName.contains("linux") || osName.contains("unix")) {
            operatingSystem = OperatingSystem.LINUX;
        } else {
            operatingSystem = OperatingSystem.UNKNOWN;
        }
    }

    /** Private constructor so that this class is not instantiated. */
    private SystemProperties() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
