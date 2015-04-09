package com.ikalagaming.util;

/**
 * A collision was found when inserting an object into a BinaryTree.
 */
public class DuplicateEntry extends Exception {

	private static final long serialVersionUID = -3959290844034459793L;

	private final Throwable cause;

	/**
	 * Constructs a new {@link DuplicateEntry} with the given {@link Throwable}
	 * 
	 * @param throwable The throwable that was thrown
	 */
	public DuplicateEntry(Throwable throwable) {
		cause = throwable;
	}

	/**
	 * Constructs a new {@link DuplicateEntry} with no cause.
	 */
	public DuplicateEntry() {
		cause = null;
	}

	/**
	 * Constructs a new {@link DuplicateEntry} with the given {@link Throwable}
	 * and message.
	 * 
	 * @param cause The throwable that was thrown
	 * @param message The detail message
	 */
	public DuplicateEntry(Throwable cause, String message) {
		super(message);
		this.cause = cause;
	}

	/**
	 * Constructs a new {@link DuplicateEntry} with no cause and the supplied
	 * detail message.
	 * 
	 * @param message The detail message
	 */
	public DuplicateEntry(String message) {
		super(message);
		cause = null;
	}

	/**
	 * If applicable, returns the Exception that triggered this Exception.
	 * 
	 * @return Inner exception, or null if one does not exist
	 */
	@Override
	public Throwable getCause() {
		return cause;
	}
}