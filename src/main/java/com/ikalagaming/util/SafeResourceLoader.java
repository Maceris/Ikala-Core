package com.ikalagaming.util;

import com.ikalagaming.localization.Localization;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Adds methods for accessing resources.
 *
 * @author Ches Burks
 */
@Slf4j
public class SafeResourceLoader {

    /**
     * The arguments are used to replace any "{}" in the string.
     *
     * <p>Intended for when we need to log a string, and also throw an exception with the same
     * message.
     *
     * @param message The message to format.
     * @param args The arguments to insert into the string.
     * @return The formatted string.
     * @see #getStringFormatted(String, ResourceBundle, String...)
     */
    public static String format(@NonNull String message, String... args) {
        return MessageFormatter.arrayFormat(message, args).getMessage();
    }

    /**
     * Returns a string from the supplied bundle. Any errors are printed to console. If no string is
     * loaded, the name is returned.
     *
     * @param name what to get from the bundle
     * @param from the bundle to use
     * @return the string from the bundle or name
     */
    public static String getString(@NonNull String name, @NonNull ResourceBundle from) {
        try {
            return from.getString(name);
        } catch (MissingResourceException missingResource) {
            SafeResourceLoader.logMissingResource(name, from.getBaseBundleName());
        } catch (ClassCastException classCast) {
            SafeResourceLoader.logClassCastException(name, from.getBaseBundleName());
        }

        return name;
    }

    /**
     * Returns a string from the supplied bundle. Any errors are logged. If no string is loaded,
     * returns the fallback.
     *
     * @param name what to get from the bundle
     * @param from the bundle to use
     * @param fallback the string to use in the event of failure
     * @return the string from the bundle or the fallback
     */
    public static String getString(
            @NonNull String name, @NonNull ResourceBundle from, String fallback) {
        try {
            return from.getString(name);
        } catch (MissingResourceException missingResource) {
            SafeResourceLoader.logMissingResource(name, from.getBaseBundleName());
        } catch (ClassCastException classCast) {
            SafeResourceLoader.logClassCastException(name, from.getBaseBundleName());
        }
        return fallback;
    }

    /**
     * Returns a string from the supplied bundle. Any errors are logged. If no string is loaded, it
     * attempts to load from the root resource bundle. If it fails again, the name is returned.
     *
     * @param name what to get from the bundle
     * @param from the bundle to use
     * @return the string from the bundle or the fallback
     */
    public static String getString(@NonNull String name, @NonNull String from) {
        ResourceBundle bundle;

        try {
            bundle = ResourceBundle.getBundle(from, Localization.getLocale());
            return bundle.getString(name);
        } catch (MissingResourceException missingResource) {
            SafeResourceLoader.logMissingResource(name, from);
        } catch (ClassCastException classCast) {
            SafeResourceLoader.logClassCastException(name, from);
        }

        return name;
    }

    /**
     * Returns a string from the supplied bundle. Any errors are logged. If no string is loaded,
     * returns the fallback.
     *
     * @param name what to get from the bundle
     * @param from the bundle to use
     * @param fallback the string to use in the event of failure
     * @return the string from the bundle or the fallback
     */
    public static String getString(@NonNull String name, @NonNull String from, String fallback) {
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle(from, Localization.getLocale());
            return bundle.getString(name);
        } catch (MissingResourceException missingResource) {
            SafeResourceLoader.logMissingResource(name, from);
        } catch (ClassCastException classCast) {
            SafeResourceLoader.logClassCastException(name, from);
        }
        return fallback;
    }

    /**
     * Returns a string from the supplied bundle. Any errors are logged. If no string is loaded, it
     * attempts to load from the root resource bundle. If it fails again, the name is returned.
     *
     * <p>The arguments are used to replace any "{}" in the string, intended only for use in
     * creating messages for exceptions. Strings for logging should not use this method, as that is
     * going through extra steps.
     *
     * @param name what to get from the bundle
     * @param from the bundle to use
     * @param args The arguments to insert into the string.
     * @return The string from the bundle or name, after formatting.
     * @see #format(String, String...)
     */
    public static String getStringFormatted(
            @NonNull String name, @NonNull ResourceBundle from, String... args) {
        return SafeResourceLoader.format(SafeResourceLoader.getString(name, from), args);
    }

    private static void logClassCastException(String name, String bundle) {
        log.warn("The {} key from the {} bundle is not a string", name, bundle);
    }

    private static void logMissingResource(String name, String bundle) {
        log.warn("Missing the {} key from the {} bundle", name, bundle);
    }

    /** Private constructor so that this class is not instantiated. */
    private SafeResourceLoader() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
