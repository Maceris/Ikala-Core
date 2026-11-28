package com.ikalagaming.util.schema;

/** What a field holds, and so how it is read, checked and edited. */
public enum FieldKind {
    /** Free text, a {@link String}. */
    TEXT,
    /** A number, a {@link Double}, maybe within a range. */
    NUMBER,
    /** A whole number, a {@link Long}, maybe within a range. */
    INTEGER,
    /** True or false, a {@link Boolean}. */
    FLAG,
    /** One of a fixed set of words, a {@link String}. */
    CHOICE,
    /** The ID of something of another kind, a {@link String}. */
    REFERENCE,
    /**
     * A list of text, or of IDs when the field refers to a kind, a {@link java.util.List} of {@link
     * String}.
     */
    TEXT_LIST,
    /** A list of numbers, a {@link java.util.List} of {@link Double}, maybe of a set length. */
    NUMBER_LIST
}
