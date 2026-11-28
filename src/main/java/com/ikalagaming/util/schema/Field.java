package com.ikalagaming.util.schema;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
        @Nullable String refersTo) {

    /** Copy the choices so they can't change. */
    public Field {
        choices = List.copyOf(choices);
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
        return new Field(name, kind, null, true, description, min, max, choices, refersTo);
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
