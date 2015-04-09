package com.ikalagaming.util;

/**
 * A collision was found when inserting an object into a BinaryTree.
 */
public class InvalidOperation extends Exception {

	private static final long serialVersionUID = -3534598283367014116L;
	private final Throwable cause;

	/**
	 * Constructs a new {@link InvalidOperation} with the given
	 * {@link Throwable}
	 * 
	 * @param throwable The throwable that was thrown
	 */
	public InvalidOperation(Throwable throwable) {
		cause = throwable;
	}

	/**
	 * Constructs a new {@link InvalidOperation} with no cause.
	 */
	public InvalidOperation() {
		cause = null;
	}

	/**
	 * Constructs a new {@link InvalidOperation} with the given
	 * {@link Throwable} and message.
	 * 
	 * @param cause The throwable that was thrown
	 * @param message The detail message
	 */
	public InvalidOperation(Throwable cause, String message) {
		super(message);
		this.cause = cause;
	}

	/**
	 * Constructs a new {@link InvalidOperation} with no cause and the supplied
	 * detail message.
	 * 
	 * @param message The detail message
	 */
	public InvalidOperation(String message) {
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