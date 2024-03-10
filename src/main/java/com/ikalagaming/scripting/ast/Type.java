package com.ikalagaming.scripting.ast;

import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * A type so that we can have a rudimentary type system and interface with Java types.
 *
 * @author Ches Burks
 */
@Slf4j
@Getter
@EqualsAndHashCode
public class Type {

    /**
     * The base kind of type, ignoring array values or specific identifier values.
     *
     * @author Ches Burks
     */
    @Getter
    @AllArgsConstructor
    public enum Base {
        /** A node that does not evaluate to a type. */
        VOID(Void.class),
        /** A node that is treated as a boolean. */
        BOOLEAN(Boolean.class),
        /** A node that is treated as a character. */
        CHAR(Character.class),
        /** A node that is treated as a double. */
        DOUBLE(Double.class),
        /** A node that is treated as an integer. */
        INT(Integer.class),
        /** A node that is treated as an identifier, a class or interface type. */
        IDENTIFIER(Object.class),
        /** A node that is treated as a string. */
        STRING(String.class),
        /** A type that we can't determine until runtime. */
        UNKNOWN(Object.class),
        /** A node that is used as a label we can jump to. Mostly used to reserve identifiers. */
        LABEL(Void.class);

        /** The type that this best maps onto in Java. */
        private Class<?> correspondingClass;
    }

    /**
     * Checks that the given type is a primitive one, and throw an exception if not.
     *
     * @param base The base type.
     * @throws IllegalArgumentException If not a primitive type.
     */
    private static void checkForPrimitives(@NonNull Base base) {
        switch (base) {
            case BOOLEAN, CHAR, DOUBLE, INT, STRING:
                break;
            case IDENTIFIER, UNKNOWN, VOID:
            default:
                throw new IllegalArgumentException(
                        SafeResourceLoader.getStringFormatted(
                                "NOT_PRIMITIVE",
                                ScriptManager.getResourceBundle(),
                                base.toString()));
        }
    }

    /**
     * Create a type representing an identifier.
     *
     * @param value The literal name of the identifier, like "Player.Inventory", or "Object".
     * @return The newly created type.
     */
    public static Type identifier(@NonNull String value) {
        return new Type(Base.IDENTIFIER, value, 0);
    }

    /**
     * Create a type representing an array of identifiers.
     *
     * @param value The literal name of the identifier, like "Player.Inventory", or "Object".
     * @param dimensions The depth of the array.
     * @return The newly created type.
     */
    public static Type identifierArray(@NonNull String value, int dimensions) {
        return new Type(Base.IDENTIFIER, value, dimensions);
    }

    /**
     * Create a label type.
     *
     * @param value The actual label.
     * @return The newly created type.
     */
    public static Type label(@NonNull String value) {
        return new Type(Base.LABEL, value, 0);
    }

    /**
     * Create a type representing a primitive.
     *
     * @param base The base type.
     * @return The newly created type.
     * @throws IllegalArgumentException If not a primitive type.
     */
    public static Type primitive(@NonNull Base base) {
        Type.checkForPrimitives(base);
        return new Type(base, "", 0);
    }

    /**
     * Create a type representing an array of primitives.
     *
     * @param base The base type.
     * @param dimensions The depth of the array.
     * @return The newly created type.
     * @throws IllegalArgumentException If not a primitive type.
     */
    public static Type primitiveArray(@NonNull Base base, int dimensions) {
        Type.checkForPrimitives(base);
        return new Type(base, "", dimensions);
    }

    /**
     * Create an unknown type, that we can't determine until runtime.
     *
     * @return The newly created type.
     */
    public static Type unknownType() {
        return new Type(Base.UNKNOWN, "", 0);
    }

    /**
     * Create a Void type.
     *
     * @return The newly created type.
     */
    public static Type voidType() {
        return new Type(Base.VOID, "", 0);
    }

    /**
     * The base type, specifics of whether it's an array or specific identifier type aside.
     *
     * @return The type.
     */
    private final Base base;

    /**
     * The actual value of the type if it's an identifier.
     *
     * @return The value of the type, or an empty string if not an identifier.
     */
    private final String value;

    /**
     * The depth of the array, if this is an array type. A value of 0 would be used if it is not an
     * array, 1 would be for something like "int[]", and 2 would be for something like "int[][]".
     *
     * @return An integer greater than or equal to zero representing how deep of an array this
     *     represents.
     */
    private final int dimensions;

    /**
     * Construct a new Type.
     *
     * @param base The base type.
     * @param value The value of the identifier.
     * @param dimensions The dimensions.
     */
    private Type(@NonNull Base base, @NonNull String value, int dimensions) {
        this.base = base;
        this.value = value;
        if (dimensions < 0) {
            throw new IllegalArgumentException("Dimensions must be positive.");
        }
        this.dimensions = dimensions;
    }

    /**
     * Check if this type has any of the supplied bases.
     *
     * @param bases The bases to check for.
     * @return True if any match, false if none do.
     */
    public boolean anyOf(Base... bases) {
        for (Base b : bases) {
            if (this.base.equals(b)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Dereference once, representing the resulting type of an array access. If you dereference more
     * than the number of dimensions, a void type is returned.
     *
     * @return The type with fewer dimensions.
     */
    public Type dereference() {
        return this.dereference(1);
    }

    /**
     * Dereference a number of times, representing the resulting type of an array access. If you
     * dereference more than the number of dimensions, a void type is returned.
     *
     * @param count The number of array indices.
     * @return The type with fewer dimensions.
     */
    public Type dereference(int count) {
        if (this.base.equals(Base.UNKNOWN)) {
            return Type.unknownType();
        }
        if (count > this.dimensions) {
            Type.log.warn("Dereferencing a {} by {} is invalid", this.toString(), count);
            return Type.voidType();
        }

        return new Type(this.base, this.value, this.dimensions - count);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (this.base == Base.IDENTIFIER) {
            result.append(this.value);
        } else {
            result.append(this.base.toString());
        }
        if (this.dimensions > 0) {
            for (int i = 0; i < this.dimensions; ++i) {
                result.append("[]");
            }
        }
        return result.toString();
    }
}
