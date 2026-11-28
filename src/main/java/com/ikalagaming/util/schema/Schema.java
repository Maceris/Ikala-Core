package com.ikalagaming.util.schema;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        final Map<String, Object> unknown = new LinkedHashMap<>();
        final List<Problem> problems = new ArrayList<>();
        final Map<String, Object> given = readObject(this, raw, "", problems, unknown, null);
        return new Values(this, given, unknown, problems);
    }

    /**
     * Read an object's values through a schema, nested objects and lists too.
     *
     * @param schema The object's fields.
     * @param raw Its keys and values, as read.
     * @param prefix Its path, with a trailing period, or empty for a file's top.
     * @param problems Gets what is wrong, by path.
     * @param unknown Gets keys the schema doesn't know, or null to only warn about them.
     * @param skip A key that is not a field, like a typed object's type, or null.
     * @return The known values, converted, in field order.
     */
    private static Map<String, Object> readObject(
            Schema schema,
            Map<?, ?> raw,
            String prefix,
            List<Problem> problems,
            @Nullable Map<String, Object> unknown,
            @Nullable String skip) {
        final Map<String, Object> given = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : raw.entrySet()) {
            final String key = String.valueOf(entry.getKey());
            if (key.equals(skip)) {
                continue;
            }
            final Field field = schema.field(key);
            if (field == null) {
                if (unknown != null) {
                    unknown.put(key, entry.getValue());
                }
                problems.add(
                        new Problem(
                                prefix + key,
                                false,
                                schema.name() + " has no field " + key + "; it is ignored"));
                continue;
            }
            final Object value = value(field, entry.getValue(), prefix + key, problems);
            if (value != null) {
                given.put(key, value);
            }
        }
        final Map<String, Object> ordered = new LinkedHashMap<>();
        for (Field field : schema.fields()) {
            if (given.containsKey(field.name())) {
                ordered.put(field.name(), given.get(field.name()));
            } else if (field.required() && !raw.containsKey(field.name())) {
                problems.add(
                        new Problem(prefix + field.name(), true, field.name() + " is missing"));
            }
        }
        return ordered;
    }

    /**
     * Read one value through its field.
     *
     * @param field The field.
     * @param raw The value as read.
     * @param path Where it is, for problems.
     * @param problems Gets what is wrong.
     * @return The value, converted, or null if it is wrong.
     */
    private static @Nullable Object value(
            Field field, Object raw, String path, List<Problem> problems) {
        final Field.Shape shape = field.shape();
        switch (field.kind()) {
            case OBJECT -> {
                final Schema schema = shape == null ? null : shape.objectSchema();
                if (raw instanceof Map<?, ?> map && schema != null) {
                    return readObject(schema, map, path + ".", problems, null, null);
                }
                problems.add(new Problem(path, true, field.name() + " must be an object"));
                return null;
            }
            case LIST -> {
                if (!(raw instanceof List<?> list) || shape == null || shape.element() == null) {
                    problems.add(new Problem(path, true, field.name() + " must be a list"));
                    return null;
                }
                final List<Object> out = new ArrayList<>();
                for (int i = 0; i < list.size(); ++i) {
                    final Object element =
                            value(
                                    shape.element().named(field.name()),
                                    list.get(i),
                                    path + "[" + i + "]",
                                    problems);
                    if (element != null) {
                        out.add(element);
                    }
                }
                return List.copyOf(out);
            }
            case TYPED -> {
                return typed(field, raw, path, problems);
            }
            case MAP -> {
                if (!(raw instanceof Map<?, ?> map) || shape == null || shape.element() == null) {
                    problems.add(new Problem(path, true, field.name() + " must be an object"));
                    return null;
                }
                final Map<String, Object> out = new LinkedHashMap<>();
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    final String key = String.valueOf(entry.getKey());
                    final Object value =
                            value(
                                    shape.element().named(key),
                                    entry.getValue(),
                                    path + "." + key,
                                    problems);
                    if (value != null) {
                        out.put(key, value);
                    }
                }
                return out;
            }
            case UNION -> {
                if (shape != null) {
                    for (Field alternative : shape.alternatives()) {
                        if (fits(alternative, raw)) {
                            return value(alternative.named(field.name()), raw, path, problems);
                        }
                    }
                }
                problems.add(new Problem(path, true, field.name() + " must be " + describe(field)));
                return null;
            }
            default -> {
                try {
                    return field.convert(raw);
                } catch (IllegalArgumentException e) {
                    problems.add(new Problem(path, true, e.getMessage()));
                    return null;
                }
            }
        }
    }

    /**
     * Read a typed object: its type, then the fields that type has.
     *
     * @param field The field.
     * @param raw The value as read.
     * @param path Where it is.
     * @param problems Gets what is wrong.
     * @return The object, its type first, or null if it is wrong.
     */
    private static @Nullable Object typed(
            Field field, Object raw, String path, List<Problem> problems) {
        final Variants variants = field.shape() == null ? null : field.shape().variants();
        if (!(raw instanceof Map<?, ?> map) || variants == null) {
            problems.add(new Problem(path, true, field.name() + " must be an object"));
            return null;
        }
        final Object type = map.get(variants.key());
        if (!(type instanceof String name) || !variants.names().contains(name)) {
            problems.add(
                    new Problem(
                            path + "." + variants.key(),
                            true,
                            (type == null
                                            ? "a " + variants.name() + " needs a type"
                                            : "no " + variants.name() + " type " + type)
                                    + ": one of "
                                    + String.join(", ", variants.names())));
            return null;
        }
        final Map<String, Object> out = new LinkedHashMap<>();
        out.put(variants.key(), name);
        final Schema schema = variants.schemaOf(name).orElse(null);
        if (schema == null) {
            // A type without a schema takes anything
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                out.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            return out;
        }
        out.putAll(readObject(schema, map, path + ".", problems, null, variants.key()));
        return out;
    }

    /**
     * Whether a value has the shape a union's alternative takes.
     *
     * @param alternative The alternative.
     * @param raw The value.
     * @return True if it fits.
     */
    private static boolean fits(Field alternative, Object raw) {
        return switch (alternative.kind()) {
            case NUMBER, INTEGER -> raw instanceof Number;
            case FLAG -> raw instanceof Boolean;
            case TEXT, CHOICE, REFERENCE -> raw instanceof String;
            case TEXT_LIST, NUMBER_LIST, LIST -> raw instanceof List<?>;
            case OBJECT, TYPED, MAP -> raw instanceof Map<?, ?>;
            case UNION -> false;
        };
    }

    /**
     * What a union may be, as words for messages.
     *
     * @param field The union.
     * @return The words.
     */
    private static String describe(Field field) {
        final List<String> kinds = new ArrayList<>();
        if (field.shape() != null) {
            for (Field alternative : field.shape().alternatives()) {
                kinds.add(
                        switch (alternative.kind()) {
                            case NUMBER, INTEGER -> "a number";
                            case FLAG -> "true or false";
                            case REFERENCE ->
                                    "the ID of a "
                                            + Objects.requireNonNullElse(
                                                    alternative.refersTo(), "file");
                            case TEXT, CHOICE -> "text";
                            case TEXT_LIST, NUMBER_LIST, LIST -> "a list";
                            case OBJECT, MAP -> "an object";
                            case TYPED ->
                                    "a "
                                            + (alternative.shape() == null
                                                            || alternative.shape().variants()
                                                                    == null
                                                    ? "typed object"
                                                    : alternative.shape().variants().name());
                            case UNION -> "one of several";
                        });
            }
        }
        return String.join(", or ", kinds);
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
