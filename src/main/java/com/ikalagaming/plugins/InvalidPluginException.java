package com.ikalagaming.plugins;

/**
 * A plugin could not be loaded correctly.
 *
 * @author Ches Burks
 */
public class InvalidPluginException extends Exception {

    /** The serial version ID. */
    private static final long serialVersionUID = -2146772321497139914L;

    /**
     * Constructs a new exception with {@code null} as its detail message. The cause is not
     * initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public InvalidPluginException() {}

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized,
     * and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the
     *     {@link #getMessage()} method.
     */
    public InvalidPluginException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
     * incorporated in this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link
     *     #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()}
     *     method). (A <code>null</code> value is permitted, and indicates that the cause is
     *     nonexistent or unknown.)
     * @since 1.4
     */
    public InvalidPluginException(String message, Throwable cause) {
        super(message, cause);
    }
}
