package com.ikalagaming.util;

/** A collision was found when inserting an object into a BinaryTree. */
public class DuplicateEntry extends Exception {

    private static final long serialVersionUID = -3959290844034459793L;

    private final Throwable theCause;

    /** Constructs a new {@link DuplicateEntry} with no cause. */
    public DuplicateEntry() {
        theCause = null;
    }

    /**
     * Constructs a new {@link DuplicateEntry} with no cause and the supplied detail message.
     *
     * @param message The detail message
     */
    public DuplicateEntry(String message) {
        super(message);
        theCause = null;
    }

    /**
     * Constructs a new {@link DuplicateEntry} with the given {@link Throwable}
     *
     * @param throwable The throwable that was thrown
     */
    public DuplicateEntry(Throwable throwable) {
        theCause = throwable;
    }

    /**
     * Constructs a new {@link DuplicateEntry} with the given {@link Throwable} and message.
     *
     * @param cause The throwable that was thrown
     * @param message The detail message
     */
    public DuplicateEntry(Throwable cause, String message) {
        super(message);
        theCause = cause;
    }

    /**
     * If applicable, returns the Exception that triggered this Exception.
     *
     * @return Inner exception, or null if one does not exist
     */
    @Override
    public synchronized Throwable getCause() {
        return theCause;
    }
}
