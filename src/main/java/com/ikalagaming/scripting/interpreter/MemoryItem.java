package com.ikalagaming.scripting.interpreter;

import lombok.NonNull;

/**
 *
 * An entry in the stack or an immediate. Retains type information to make our
 * lives easier.
 *
 * @author Ches Burks
 *
 * @param <T> The type of the stack item.
 * @param type The type of the object, since it gets erased.
 * @param value The actual object.
 */
public record MemoryItem(@NonNull Class<?> type, @NonNull Object value) {
	/**
	 * Construct without specifying the type.
	 *
	 * @param value The actual object.
	 */
	public MemoryItem(@NonNull Object value) {
		this(value.getClass(), value);
	}
}
