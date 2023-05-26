package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.scripting.ast.Type;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps variables to their corresponding type.
 *
 * @author Ches Burks
 *
 */
@Slf4j
class VariableTypeMap {
	/**
	 * The default type to return if no mapping exists.
	 */
	private static final Type DEFAULT = Type.voidType();
	/**
	 * The actual backing map.
	 */
	private Map<String, Type> map;

	/**
	 * Create a new empty type map.
	 */
	public VariableTypeMap() {
		this.map = new HashMap<>();
	}

	/**
	 * Create a new map, copying all the values from an existing map.
	 *
	 * @param toCopy The map to clone.
	 */
	public VariableTypeMap(VariableTypeMap toCopy) {
		this.map = new HashMap<>();
		this.map.putAll(toCopy.map);
	}

	/**
	 * Checks if a variable exists.
	 *
	 * @param variable The variable.
	 * @return True if it exists, false otherwise.
	 */
	public boolean contains(@NonNull String variable) {
		return this.map.containsKey(variable);
	}

	/**
	 * Fetch the type of a variable, will default to void if it does not exist.
	 *
	 * @param variable The variable to look up.
	 * @return The type of the variable, or void.
	 */
	public Type get(@NonNull String variable) {
		return this.map.getOrDefault(variable, VariableTypeMap.DEFAULT);
	}

	/**
	 * Add a variable to the map. If it already exists, that's a semantic error,
	 * and we will consider it void for this and all enclosing scopes.
	 *
	 * @param variable The name of the variable.
	 * @param type The type of the variable.
	 */
	public void put(@NonNull String variable, @NonNull Type type) {
		this.map.computeIfPresent(variable, (key, current) -> {
			VariableTypeMap.log
				.warn(SafeResourceLoader.getString("VARIABLE_ALREADY_DEFINED",
					ScriptManager.getResourceBundle()), variable);
			return VariableTypeMap.DEFAULT;
		});
		this.map.put(variable, type);
	}

	/**
	 * Remove a variable from the map.
	 *
	 * @param variable The variable in question.
	 */
	public void remove(@NonNull String variable) {
		this.map.remove(variable);
	}

	@Override
	public String toString() {
		return this.map.toString();
	}
}
