package com.ikalagaming.util.schema;

import lombok.NonNull;

import java.util.Optional;
import java.util.SortedSet;

/**
 * The types a typed object may be, each with its own schema, chosen by the object's type field:
 * like world generation's density nodes, whose {@code type} says which fields apply. Usually a
 * registry, so types registered later have their schemas too.
 */
public interface Variants {
    /**
     * The field naming an object's type.
     *
     * @return The key, like {@code type}.
     */
    default String key() {
        return "type";
    }

    /**
     * What the types are, for messages and pickers.
     *
     * @return A name for the set, like {@code density}.
     */
    String name();

    /**
     * Every type's name.
     *
     * @return The names, sorted.
     */
    SortedSet<String> names();

    /**
     * A type's schema: its fields besides the type field.
     *
     * @param type The type's name.
     * @return The schema, or empty if there is no such type or it declares none.
     */
    Optional<Schema> schemaOf(@NonNull String type);
}
