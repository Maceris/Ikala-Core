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
	private Map<String, Type> map = new HashMap<>();

	/**
	 * Clones the map and returns the result.
	 */
	@Override
	public VariableTypeMap clone() {
		VariableTypeMap result = new VariableTypeMap();
		result.map.putAll(this.map);
		return result;
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
		if (this.map.containsKey(variable)) {
			VariableTypeMap.log
				.warn(SafeResourceLoader.getString("VARIABLE_ALREADY_DEFINED",
					ScriptManager.getResourceBundle()), variable);
			this.map.put(variable, VariableTypeMap.DEFAULT);
		}
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
