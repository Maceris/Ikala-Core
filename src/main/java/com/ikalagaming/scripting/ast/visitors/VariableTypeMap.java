package com.ikalagaming.scripting.ast.visitors;

import com.ikalagaming.scripting.ScriptDiagnostics;
import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.scripting.ast.Node;
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
 */
@Slf4j
class VariableTypeMap {
    /** The default type to return if no mapping exists. */
    private static final Type DEFAULT = Type.voidType();

    /** The actual backing map. */
    private Map<String, Type> map;

    /** Create a new empty type map. */
    public VariableTypeMap() {
        map = new HashMap<>();
    }

    /**
     * Create a new map, copying all the values from an existing map.
     *
     * @param toCopy The map to clone.
     */
    public VariableTypeMap(VariableTypeMap toCopy) {
        map = new HashMap<>();
        map.putAll(toCopy.map);
    }

    /**
     * Checks if a variable exists.
     *
     * @param variable The variable.
     * @return True if it exists, false otherwise.
     */
    public boolean contains(@NonNull String variable) {
        return map.containsKey(variable);
    }

    /**
     * Fetch the type of a variable, will default to void if it does not exist.
     *
     * @param variable The variable to look up.
     * @return The type of the variable, or void.
     */
    public Type get(@NonNull String variable) {
        return map.getOrDefault(variable, VariableTypeMap.DEFAULT);
    }

    /**
     * Add a variable to the map. If it already exists, that's a semantic error, and we will
     * consider it void for this and all enclosing scopes.
     *
     * @param variable The name of the variable.
     * @param type The type of the variable.
     * @return True if the variable was added, false if it was already defined.
     */
    public boolean put(@NonNull String variable, @NonNull Type type) {
        return put(variable, type, -1);
    }

    /**
     * Add a variable to the map. If it already exists, that's a semantic error, and we will
     * consider it void for this and all enclosing scopes.
     *
     * @param variable The name of the variable.
     * @param type The type of the variable.
     * @param declaration Where the variable is declared, for error messages.
     * @return True if the variable was added, false if it was already defined.
     */
    public boolean put(@NonNull String variable, @NonNull Type type, @NonNull Node declaration) {
        return put(variable, type, declaration.getLine());
    }

    /**
     * Add a variable to the map. If it already exists, that's a semantic error, and we will
     * consider it void for this and all enclosing scopes.
     *
     * @param variable The name of the variable.
     * @param type The type of the variable.
     * @param line The line the variable is declared on, for error messages, or -1 if unknown.
     * @return True if the variable was added, false if it was already defined.
     */
    private boolean put(@NonNull String variable, @NonNull Type type, int line) {
        if (map.containsKey(variable)) {
            ScriptDiagnostics.warnAt(
                    log,
                    line,
                    -1,
                    SafeResourceLoader.getString(
                            "VARIABLE_ALREADY_DEFINED", ScriptManager.getResourceBundle()),
                    variable);
            map.put(variable, VariableTypeMap.DEFAULT);
            return false;
        }
        map.put(variable, type);
        return true;
    }

    /**
     * Remove a variable from the map.
     *
     * @param variable The variable in question.
     */
    public void remove(@NonNull String variable) {
        map.remove(variable);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
