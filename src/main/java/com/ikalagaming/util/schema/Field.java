package com.ikalagaming.util.schema;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import javax.annotation.Nullable;

/**
 * One field of a {@link Schema}: its name, what it holds, its default and its limits, enough for a
 * reader to check a file and an editor to make a control for it.
 *
 * @param name The key in files.
 * @param kind What it holds.
 * @param defaultValue Its value when a file leaves it out, of the kind's Java type, or null when
 *     there is none (the field is optional with no value, or required).
 * @param required Whether a file must give it.
 * @param description What it means, for tooltips and documentation.
 * @param min The least number, or the least list length for a list, inclusive.
 * @param max The greatest number, or list length, inclusive.
 * @param choices The words a {@link FieldKind#CHOICE} may be, in order; empty otherwise.
 * @param refersTo The kind of thing a reference or list of references names, like {@code
 *     materials}, or null.
 * @param shape What an object, list, typed object or union holds, or null for other kinds.
 */
public record Field(
        @NonNull String name,
        @NonNull FieldKind kind,
        @Nullable Object defaultValue,
        boolean required,
        @NonNull String description,
        double min,
        double max,
        @NonNull List<String> choices,
        @Nullable String refersTo,
        @Nullable Shape shape) {

    /**
     * What a field made of other fields holds.
     *
     * @param schema An object's fields, or null.
     * @param element A list's element, described as a field whose name is unused, or null.
     * @param variants A typed object's types, or null.
     * @param alternatives A union's kinds, tried by the value's shape, in order; empty otherwise.
     * @param later An object's fields when they can't be given yet, as a schema that holds itself
     *     needs, or null.
     */
    public record Shape(
            @Nullable Schema schema,
            @Nullable Field element,
            @Nullable Variants variants,
            @NonNull List<Field> alternatives,
            @Nullable Supplier<Schema> later) {
        /** Copy the alternatives so they can't change. */
        public Shape {
            alternatives = List.copyOf(alternatives);
        }

        /**
         * A shape without fields given later.
         *
         * @param schema An object's fields, or null.
         * @param element A list's or map's element, or null.
         * @param variants A typed object's types, or null.
         * @param alternatives A union's kinds.
         */
        public Shape(
                @Nullable Schema schema,
                @Nullable Field element,
                @Nullable Variants variants,
                @NonNull List<Field> alternatives) {
            this(schema, element, variants, alternatives, null);
        }

        /**
         * An object's fields, given now or later.
         *
         * @return The schema, or null if this isn't an object's shape.
         */
        public @Nullable Schema objectSchema() {
            return schema != null ? schema : later == null ? null : later.get();
        }
    }

    /** Copy the choices so they can't change. */
    public Field {
        choices = List.copyOf(choices);
    }

    /**
     * A field of a plain kind, holding no other fields.
     *
     * @param name The key in files.
     * @param kind What it holds.
     * @param defaultValue Its default, or null.
     * @param required Whether a file must give it.
     * @param description What it means.
     * @param min The least number or list length.
     * @param max The greatest number or list length.
     * @param choices The words a choice may be.
     * @param refersTo The kind a reference names, or null.
     */
    public Field(
            @NonNull String name,
            @NonNull FieldKind kind,
            @Nullable Object defaultValue,
            boolean required,
            @NonNull String description,
            double min,
            double max,
            @NonNull List<String> choices,
            @Nullable String refersTo) {
        this(name, kind, defaultValue, required, description, min, max, choices, refersTo, null);
    }

    /**
     * A nested object with fields of its own.
     *
     * @param name The key.
     * @param schema Its fields.
     * @param description What it means.
     * @return The field, optional with no default.
     */
    public static Field object(
            @NonNull String name, @NonNull Schema schema, @NonNull String description) {
        return shaped(
                name, FieldKind.OBJECT, description, new Shape(schema, null, null, List.of()));
    }

    /**
     * A nested object whose fields are given later, for a schema that holds objects of itself, like
     * rules holding rules.
     *
     * @param name The key.
     * @param schema Gives its fields, when first read.
     * @param description What it means.
     * @return The field, optional with no default.
     */
    public static Field object(
            @NonNull String name, @NonNull Supplier<Schema> schema, @NonNull String description) {
        return shaped(
                name,
                FieldKind.OBJECT,
                description,
                new Shape(null, null, null, List.of(), schema));
    }

    /**
     * An object whose keys are free, like names, each value described by an element field.
     *
     * @param name The key.
     * @param element What each value is; its name is unused.
     * @param description What it means.
     * @return The field, optional with no default.
     */
    public static Field map(
            @NonNull String name, @NonNull Field element, @NonNull String description) {
        return shaped(name, FieldKind.MAP, description, new Shape(null, element, null, List.of()));
    }

    /**
     * A list of values, each described by an element field, whose name is unused.
     *
     * @param name The key.
     * @param element What each entry is.
     * @param description What it means.
     * @return The field, empty by default.
     */
    public static Field list(
            @NonNull String name, @NonNull Field element, @NonNull String description) {
        return new Field(
                name,
                FieldKind.LIST,
                List.of(),
                false,
                description,
                0,
                Double.POSITIVE_INFINITY,
                List.of(),
                null,
                new Shape(null, element, null, List.of()));
    }

    /**
     * An object whose type field chooses its fields from a set of types.
     *
     * @param name The key.
     * @param variants The types.
     * @param description What it means.
     * @return The field, optional with no default.
     */
    public static Field typed(
            @NonNull String name, @NonNull Variants variants, @NonNull String description) {
        return shaped(
                name, FieldKind.TYPED, description, new Shape(null, null, variants, List.of()));
    }

    /**
     * A value that may be one of several kinds, tried in order by the value's shape: a number, a
     * text, a list or an object. The alternatives' names are unused.
     *
     * @param name The key.
     * @param description What it means.
     * @param alternatives The kinds it may be.
     * @return The field, optional with no default.
     */
    public static Field union(
            @NonNull String name, @NonNull String description, @NonNull Field... alternatives) {
        return shaped(
                name,
                FieldKind.UNION,
                description,
                new Shape(null, null, null, List.of(alternatives)));
    }

    /**
     * A list of a set number of numbers with no default, like a point or a range.
     *
     * @param name The key.
     * @param count How many numbers.
     * @param description What it means.
     * @return The field, optional with no default.
     */
    public static Field numbers(@NonNull String name, int count, @NonNull String description) {
        return new Field(
                name,
                FieldKind.NUMBER_LIST,
                null,
                false,
                description,
                count,
                count,
                List.of(),
                null);
    }

    /**
     * A field made of other fields, optional with no default.
     *
     * @param name The key.
     * @param kind Its kind.
     * @param description What it means.
     * @param shape What it holds.
     * @return The field.
     */
    private static Field shaped(String name, FieldKind kind, String description, Shape shape) {
        return new Field(
                name,
                kind,
                null,
                false,
                description,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                List.of(),
                null,
                shape);
    }

    /**
     * The same field with another name, as an element or alternative is used for a key.
     *
     * @param key The name.
     * @return The renamed field.
     */
    public Field named(@NonNull String key) {
        return new Field(
                key, kind, defaultValue, required, description, min, max, choices, refersTo, shape);
    }

    /**
     * The same field with another default.
     *
     * @param value The default, of the kind's Java type.
     * @return The field.
     */
    public Field withDefault(@Nullable Object value) {
        return new Field(name, kind, value, false, description, min, max, choices, refersTo, shape);
    }

    /**
     * Free text.
     *
     * @param name The key.
     * @param defaultValue The default, or null for none.
     * @param description What it means.
     * @return The field.
     */
    public static Field text(
            @NonNull String name, @Nullable String defaultValue, @NonNull String description) {
        return new Field(
                name,
                FieldKind.TEXT,
                defaultValue,
                false,
                description,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                List.of(),
                null);
    }

    /**
     * A number within a range.
     *
     * @param name The key.
     * @param defaultValue The default.
     * @param min The least it may be, inclusive.
     * @param max The greatest it may be, inclusive.
     * @param description What it means.
     * @return The field.
     */
    public static Field number(
            @NonNull String name,
            double defaultValue,
            double min,
            double max,
            @NonNull String description) {
        return new Field(
                name,
                FieldKind.NUMBER,
                defaultValue,
                false,
                description,
                min,
                max,
                List.of(),
                null);
    }

    /**
     * A whole number within a range.
     *
     * @param name The key.
     * @param defaultValue The default.
     * @param min The least it may be, inclusive.
     * @param max The greatest it may be, inclusive.
     * @param description What it means.
     * @return The field.
     */
    public static Field integer(
            @NonNull String name,
            long defaultValue,
            long min,
            long max,
            @NonNull String description) {
        return new Field(
                name,
                FieldKind.INTEGER,
                defaultValue,
                false,
                description,
                min,
                max,
                List.of(),
                null);
    }

    /**
     * True or false.
     *
     * @param name The key.
     * @param defaultValue The default.
     * @param description What it means.
     * @return The field.
     */
    public static Field flag(
            @NonNull String name, boolean defaultValue, @NonNull String description) {
        return new Field(
                name,
                FieldKind.FLAG,
                defaultValue,
                false,
                description,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                List.of(),
                null);
    }

    /**
     * One of a set of words, read without regard to case or surrounding space.
     *
     * @param name The key.
     * @param defaultValue The default, one of the choices.
     * @param description What it means.
     * @param choices The words, in the order offered.
     * @return The field.
     */
    public static Field choice(
            @NonNull String name,
            @NonNull String defaultValue,
            @NonNull String description,
            @NonNull String... choices) {
        return new Field(
                name,
                FieldKind.CHOICE,
                defaultValue,
                false,
                description,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                List.of(choices),
                null);
    }

    /**
     * The ID of something of another kind.
     *
     * @param name The key.
     * @param refersTo The kind, like {@code materials}.
     * @param defaultValue The default, or null for none.
     * @param description What it means.
     * @return The field.
     */
    public static Field reference(
            @NonNull String name,
            @NonNull String refersTo,
            @Nullable String defaultValue,
            @NonNull String description) {
        return new Field(
                name,
                FieldKind.REFERENCE,
                defaultValue,
                false,
                description,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                List.of(),
                refersTo);
    }

    /**
     * A list of text, empty by default; of IDs if it refers to a kind.
     *
     * @param name The key.
     * @param refersTo The kind its entries name, or null for free text.
     * @param description What it means.
     * @return The field.
     */
    public static Field textList(
            @NonNull String name, @Nullable String refersTo, @NonNull String description) {
        return new Field(
                name,
                FieldKind.TEXT_LIST,
                List.of(),
                false,
                description,
                0,
                Double.POSITIVE_INFINITY,
                List.of(),
                refersTo);
    }

    /**
     * A list of a set number of numbers, like a color.
     *
     * @param name The key.
     * @param defaultValue The default, as many numbers as the list holds.
     * @param description What it means.
     * @return The field.
     */
    public static Field numbers(
            @NonNull String name, @NonNull List<Double> defaultValue, @NonNull String description) {
        return new Field(
                name,
                FieldKind.NUMBER_LIST,
                List.copyOf(defaultValue),
                false,
                description,
                defaultValue.size(),
                defaultValue.size(),
                List.of(),
                null);
    }

    /**
     * The same field, but one a file must give, with no default.
     *
     * @return The required field.
     */
    public Field asRequired() {
        return new Field(name, kind, null, true, description, min, max, choices, refersTo, shape);
    }

    /**
     * A value from a file as this field's Java type: numbers widened, words of a choice matched
     * without regard to case.
     *
     * @param raw The value as read, like an {@link Integer} or a {@link List}.
     * @return The value.
     * @throws IllegalArgumentException If it isn't of this field's kind, or out of its limits; the
     *     message says what was expected.
     */
    public Object convert(@NonNull Object raw) {
        return switch (kind) {
            case OBJECT, LIST, TYPED, UNION, MAP ->
                    throw new IllegalStateException(
                            name + " holds other fields; read it through its schema");
            case TEXT, REFERENCE -> {
                if (raw instanceof String text) {
                    yield text;
                }
                throw new IllegalArgumentException(name + " must be text");
            }
            case CHOICE -> {
                if (raw instanceof String text) {
                    final String word = text.trim().toLowerCase(Locale.ROOT);
                    for (String choice : choices) {
                        if (choice.toLowerCase(Locale.ROOT).equals(word)) {
                            yield choice;
                        }
                    }
                }
                throw new IllegalArgumentException(
                        name + " must be one of " + String.join(", ", choices));
            }
            case FLAG -> {
                if (raw instanceof Boolean flag) {
                    yield flag;
                }
                throw new IllegalArgumentException(name + " must be true or false");
            }
            case NUMBER -> {
                if (raw instanceof Number number && Double.isFinite(number.doubleValue())) {
                    yield inRange(number.doubleValue());
                }
                throw new IllegalArgumentException(name + " must be a number");
            }
            case INTEGER -> {
                if (raw instanceof Number number && number.doubleValue() == number.longValue()) {
                    inRange(number.longValue());
                    yield number.longValue();
                }
                throw new IllegalArgumentException(name + " must be a whole number");
            }
            case TEXT_LIST -> {
                if (raw instanceof List<?> list
                        && list.stream().allMatch(String.class::isInstance)) {
                    yield List.copyOf(list.stream().map(String.class::cast).toList());
                }
                throw new IllegalArgumentException(name + " must be a list of text");
            }
            case NUMBER_LIST -> {
                if (raw instanceof List<?> list
                        && list.stream().allMatch(Number.class::isInstance)) {
                    if (list.size() < min || list.size() > max) {
                        throw new IllegalArgumentException(
                                name + " must have " + lengthText() + " numbers");
                    }
                    final List<Double> numbers = new ArrayList<>();
                    for (Object item : list) {
                        numbers.add(((Number) item).doubleValue());
                    }
                    yield List.copyOf(numbers);
                }
                throw new IllegalArgumentException(name + " must be a list of numbers");
            }
        };
    }

    /**
     * A number, if it is within the field's range.
     *
     * @param value The number.
     * @return The number.
     * @throws IllegalArgumentException If it is out of range.
     */
    private double inRange(double value) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    name + " must be from " + plain(min) + " to " + plain(max));
        }
        return value;
    }

    /**
     * How many numbers a list holds, as text.
     *
     * @return The length or range of lengths.
     */
    private String lengthText() {
        return min == max ? plain(min) : plain(min) + " to " + plain(max);
    }

    /**
     * A number as short text: whole numbers without a point.
     *
     * @param value The number.
     * @return The text.
     */
    private static String plain(double value) {
        return value == Math.rint(value) && Math.abs(value) < 1e15
                ? Long.toString((long) value)
                : Double.toString(value);
    }

    /**
     * Whether a value is this field's default, so a file can leave it out.
     *
     * @param value The value, of the field's Java type, or null for none.
     * @return True if it equals the default.
     */
    public boolean isDefault(@Nullable Object value) {
        if (value == null || defaultValue == null) {
            return value == null && defaultValue == null
                    || value instanceof List<?> list && list.isEmpty() && defaultValue == null;
        }
        if (value instanceof Number number && defaultValue instanceof Number fallback) {
            return number.doubleValue() == fallback.doubleValue();
        }
        return value.equals(defaultValue);
    }
}
