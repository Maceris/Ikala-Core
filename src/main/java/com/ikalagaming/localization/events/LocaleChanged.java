package com.ikalagaming.localization.events;

import com.ikalagaming.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

/**
 * Fired off when the locale is changed, so that plugins can update their
 * resource bundles.
 *
 * @author Ches Burks
 *
 */
@Getter
@AllArgsConstructor
public class LocaleChanged extends Event {
	/**
	 * The locale that we have now changed to.
	 *
	 * @return The new locale.
	 */
	private final Locale locale;
}
