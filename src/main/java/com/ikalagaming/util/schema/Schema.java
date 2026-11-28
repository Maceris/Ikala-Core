package com.ikalagaming.util.schema;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * The fields a kind of file has, in order, declared once beside the code that reads the files, so
 * readers check files and editors make forms from the same declarations. Knows nothing of file
 * formats: readers hand it each key's value as plain Java objects (text, numbers, true or false,
 * lists) and write what it gives back.
 *
 * @param name What the files are, like {@code block}.
 * @param fields The fields, in the order shown and written.
 */
public record Schema(@NonNull String name, @NonNull List<Field> fields) {

    /** Copy the fields so they can't change, and check their names are distinct. */
    public Schema {
        fields = List.copyOf(fields);
        final List<String> seen = new ArrayList<>();
        for (Field field : fields) {
            if (seen.contains(field.name())) {
                throw new IllegalArgumentException(name + " has two fields called " + field.name());
            }
            seen.add(field.name());
        }
    }

    /**
     * Declare a schema.
     *
     * @param name What the files are.
     * @param fields The fields, in order.
     * @return The schema.
     */
    public static Schema of(@NonNull String name, @NonNull Field... fields) {
        return new Schema(name, List.of(fields));
    }

    /**
     * A field by name.
     *
     * @param key The name.
     * @return The field, or null if there is none.
     */
    public @Nullable Field field(@NonNull String key) {
        for (Field field : fields) {
            if (field.name().equals(key)) {
                return field;
            }
        }
        return null;
    }

    /**
     * Something wrong with a file.
     *
     * @param field The field at fault.
     * @param error Whether the field's value can't be used (an error), or is only odd, like an
     *     unknown field (a warning).
     * @param message What is wrong.
     */
    public record Problem(@NonNull String field, boolean error, @NonNull String message) {}

    /**
     * A file's values, as read through a schema.
     *
     * @param schema The schema.
     * @param given The values the file gives for known fields, converted, by name in field order.
     * @param unknown The file's other keys and their values as read, which writers keep.
     * @param problems What is wrong, errors and warnings.
     */
    public record Values(
            @NonNull Schema schema,
            @NonNull Map<String, Object> given,
            @NonNull Map<String, Object> unknown,
            @NonNull List<Problem> problems) {
        /** Copy the maps so they can't change. */
        public Values {
            given = Collections.unmodifiableMap(new LinkedHashMap<>(given));
            unknown = Collections.unmodifiableMap(new LinkedHashMap<>(unknown));
            problems = List.copyOf(problems);
        }

        /**
         * A field's value: the file's, or the default.
         *
         * @param key The field.
         * @return The value, or null if the file gives none and there is no default.
         * @throws IllegalArgumentException If the schema has no such field.
         */
        public @Nullable Object get(@NonNull String key) {
            final Field field = schema.field(key);
            if (field == null) {
                throw new IllegalArgumentException(schema.name() + " has no field " + key);
            }
            return given.containsKey(key) ? given.get(key) : field.defaultValue();
        }

        /**
         * A text, choice or reference field's value.
         *
         * @param key The field.
         * @return The text, or null.
         */
        public @Nullable String text(@NonNull String key) {
            return (String) get(key);
        }

        /**
         * A number field's value.
         *
         * @param key The field.
         * @return The number.
         * @throws NullPointerException If it has neither a value nor a default.
         */
        public double number(@NonNull String key) {
            return ((Number) get(key)).doubleValue();
        }

        /**
         * A flag's value.
         *
         * @param key The field.
         * @return The flag.
         * @throws NullPointerException If it has neither a value nor a default.
         */
        public boolean flag(@NonNull String key) {
            return (Boolean) get(key);
        }

        /**
         * A text list's value, empty if missing.
         *
         * @param key The field.
         * @return A list that can be changed.
         */
        @SuppressWarnings("unchecked")
        public List<String> texts(@NonNull String key) {
            final Object value = get(key);
            return value == null ? new ArrayList<>() : new ArrayList<>((List<String>) value);
        }

        /**
         * The errors among the problems.
         *
         * @return The errors.
         */
        public List<Problem> errors() {
            return problems.stream().filter(Problem::error).toList();
        }
    }

    /**
     * Read a file's values: each known key's value converted and checked, missing required fields
     * and unknown keys noted.
     *
     * @param raw The file's keys and values, as read, in the file's order.
     * @return The values.
     */
    public Values read(@NonNull Map<String, Object> raw) {
        final Map<String, Object> given = new LinkedHashMap<>();
        final Map<String, Object> unknown = new LinkedHashMap<>();
        final List<Problem> problems = new ArrayList<>();
        for (Map.Entry<String, Object> entry : raw.entrySet()) {
            final Field field = field(entry.getKey());
            if (field == null) {
                unknown.put(entry.getKey(), entry.getValue());
                problems.add(
                        new Problem(
                                entry.getKey(),
                                false,
                                name + " has no field " + entry.getKey() + "; it is ignored"));
                continue;
            }
            try {
                given.put(field.name(), field.convert(entry.getValue()));
            } catch (IllegalArgumentException e) {
                problems.add(new Problem(field.name(), true, e.getMessage()));
            }
        }
        for (Field field : fields) {
            if (field.required() && !raw.containsKey(field.name())) {
                problems.add(new Problem(field.name(), true, field.name() + " is missing"));
            }
        }
        // In field order, as editors show them
        final Map<String, Object> ordered = new LinkedHashMap<>();
        for (Field field : fields) {
            if (given.containsKey(field.name())) {
                ordered.put(field.name(), given.get(field.name()));
            }
        }
        return new Values(this, ordered, unknown, problems);
    }

    /**
     * What to write for some values: each field whose value isn't its default, in field order, so
     * files list only what differs. A required field is always written.
     *
     * @param values The values by field name; a missing or null one is left out.
     * @return The values to write, by name.
     */
    public Map<String, Object> differing(@NonNull Map<String, Object> values) {
        final Map<String, Object> out = new LinkedHashMap<>();
        for (Field field : fields) {
            final Object value = values.get(field.name());
            if (value != null && (field.required() || !field.isDefault(value))) {
                out.put(field.name(), value);
            }
        }
        return out;
    }
}
