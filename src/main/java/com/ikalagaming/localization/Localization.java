package com.ikalagaming.localization;

import com.ikalagaming.localization.events.LocaleChanged;

import lombok.Getter;
import lombok.NonNull;

import java.util.Locale;

/**
 * Allows changing languages and locales in the program.
 *
 * @author Ches Burks
 */
public class Localization {

    /**
     * The current locale. Defaults to "en_US".
     *
     * @return The current locale
     */
    @SuppressWarnings("javadoc")
    @Getter
    private static Locale locale = new Locale("en", "US");

    /**
     * Update the current locale.
     *
     * @param newLocale The new locale to use
     */
    public static void setLocale(@NonNull Locale newLocale) {
        Localization.locale = newLocale;
        new LocaleChanged(newLocale).fire();
    }

    /** Private constructor so this class is not instantiated. */
    private Localization() {}
}
