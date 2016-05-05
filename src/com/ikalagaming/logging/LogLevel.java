package com.ikalagaming.logging;

import java.util.Locale;
import java.util.logging.Level;

import com.ikalagaming.localization.Localization;
import com.ikalagaming.util.SafeResourceLoader;

/**
 * A set of levels for logging. This is the standard java logging list in an
 * enum form, with the values changed.
 *
 * @see Level
 * @author Ches Burks
 *
 */
public enum LogLevel {
	/**
	 * OFF is a special level that can be used to turn off logging. This should
	 * not be used as a value when constructing a message for logging. This
	 * level is initialized to <CODE>1000</CODE>.
	 */
	OFF("OFF", 1000),
	/**
	 * SEVERE is a message level indicating a serious failure.
	 * <p>
	 * In general SEVERE messages should describe events that are of
	 * considerable importance and which will prevent normal program execution.
	 * They should be reasonably intelligible to end users and to system
	 * administrators. This level is initialized to <CODE>70</CODE>.
	 */
	SEVERE("SEVERE", 70),
	/**
	 * WARNING is a message level indicating a potential problem.
	 * <p>
	 * In general WARNING messages should describe events that will be of
	 * interest to end users or system managers, or which indicate potential
	 * problems. This level is initialized to <CODE>60</CODE>.
	 */
	WARNING("WARNING", 60),
	/**
	 * INFO is a message level for informational messages.
	 * <p>
	 * Typically INFO messages will be written to the console or its equivalent.
	 * So the INFO level should only be used for reasonably significant messages
	 * that will make sense to end users and system administrators. This level
	 * is initialized to <CODE>50</CODE>.
	 */
	INFO("INFO", 50),
	/**
	 * CONFIG is a message level for static configuration messages.
	 * <p>
	 * CONFIG messages are intended to provide a variety of static configuration
	 * information, to assist in debugging problems that may be associated with
	 * particular configurations. For example, CONFIG message might include the
	 * OS architecture, the screen size, the GUI look-and-feel, etc. This level
	 * is initialized to <CODE>40</CODE>.
	 */
	CONFIG("CONFIG", 40),
	/**
	 * FINE is a message level providing tracing information.
	 * <p>
	 * All of FINE, FINER, and FINEST are intended for relatively detailed
	 * tracing. The exact meaning of the three levels will vary between
	 * subsystems, but in general, FINEST should be used for the most detailed
	 * output, FINER for somewhat less detailed output, and FINE for the lowest
	 * volume (and most important) messages.
	 * <p>
	 * In general the FINE level should be used for information that will be
	 * broadly interesting to developers who do not have a specialized interest
	 * in the specific subsystem.
	 * <p>
	 * FINE messages might include things like minor (recoverable) failures.
	 * Issues indicating potential performance problems are also worth logging
	 * as FINE. This level is initialized to <CODE>30</CODE>.
	 */
	FINE("FINE", 30),
	/**
	 * FINER indicates a fairly detailed tracing message. By default logging
	 * calls for entering, returning, or throwing an exception are traced at
	 * this level. This level is initialized to <CODE>20</CODE>.
	 */
	FINER("FINER", 20),
	/**
	 * FINEST indicates a highly detailed tracing message. This level is
	 * initialized to <CODE>10</CODE>.
	 */
	FINEST("FINEST", 10),
	/**
	 * ALL indicates that all messages should be logged. This should not be used
	 * as a value when constructing a message for logging. This level is
	 * initialized to <CODE>-10</CODE>.
	 */
	ALL("ALL", -10);

	private final String levelName;
	private final int levelValue;

	// for caching the names
	private transient String localizedLevelName;
	private transient Locale cachedLocale;

	/**
	 * Constructs a named logging level with the given integer value.
	 *
	 * @param name the name of the level
	 * @param value an integer value for the level
	 */
	private LogLevel(String name, int value) {
		this.levelName = name;
		this.levelValue = value;
	}

	// Avoid looking up the localizedLevelName twice if we already have it
	/**
	 * Returns the cached localized name for the given Level. If it has changed,
	 * it updates it and then returns it.
	 *
	 * @see Level
	 * @return The cached name for this level
	 */
	private final String getCachedLocalizedLevelName() {

		if (this.localizedLevelName != null) {
			if (this.cachedLocale != null) {
				if (this.cachedLocale.equals(Localization.getLocale())) {
					// OK: our cached value was looked up with the same
					// locale. We can use it.
					return this.localizedLevelName;
				}
			}
		}
		// We need to compute the localized name.
		// Either because it's the first time, or because our cached
		// value is for a different locale.
		this.localizedLevelName =
				SafeResourceLoader.getString(this.levelName,
						"com.ikalagaming.logging.resources.LoggingLevel",
						this.levelName);
		this.cachedLocale = Localization.getLocale();

		return this.localizedLevelName;
	}

	/**
	 * Attempts to find the localized name for the logging level. If none
	 * exists, the default level will be returned.
	 *
	 * @return the localized name of the logging level
	 */
	public String getLocalizedName() {
		return this.getCachedLocalizedLevelName();
	}

	/**
	 * Returns the (non-localized) name of the logging level.
	 *
	 * @return the non-localized name of the logging level
	 */
	public String getName() {
		return this.levelName;
	}

	/**
	 * Returns the integer value assigned to the level.
	 *
	 * @return the integer value for this level
	 */
	public int intValue() {
		return this.levelValue;
	}
}
