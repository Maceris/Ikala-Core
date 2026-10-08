package com.ikalagaming.scripting.interpreter;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Formats instructions as text, similar to assembly, for debugging tools. For example:
 *
 * <pre>
 * DECLARE 0 -> x:int
 * ADD_INT x:int, stack:int -> x:int
 * JLT -> 0006
 * CALL print(1 arg)
 * CALL stack.getName(0 args) -> stack
 * </pre>
 *
 * <p>Variables are shown by name, values on the stack as "stack", and immediate values as literals.
 * Types are shown after a colon, except for literals where they are obvious.
 *
 * @author Ches Burks
 */
public final class InstructionFormatter {

    /**
     * Format an instruction.
     *
     * @param instruction The instruction.
     * @return The instruction as text.
     */
    public static String format(@NonNull Instruction instruction) {
        final InstructionType type = instruction.type();
        final String name = type.toString();
        return switch (type) {
            case JMP, JEQ, JNE, JGE, JGT, JLE, JLT ->
                    name + " -> " + formatJumpTarget(instruction.firstLocation());
            case CALL -> formatCall(instruction);
            default -> {
                final List<String> inputs = new ArrayList<>();
                if (instruction.firstLocation() != null) {
                    inputs.add(formatLocation(instruction.firstLocation()));
                }
                if (instruction.secondLocation() != null) {
                    inputs.add(formatLocation(instruction.secondLocation()));
                }
                StringBuilder result = new StringBuilder(name);
                if (!inputs.isEmpty()) {
                    result.append(' ').append(String.join(", ", inputs));
                }
                if (instruction.targetLocation() != null) {
                    result.append(" -> ").append(formatLocation(instruction.targetLocation()));
                }
                yield result.toString();
            }
        };
    }

    /**
     * Format a method call. The first location is the method name, which is called on the object on
     * the stack if it is a stack location. The second is how many arguments there are.
     *
     * @param instruction The call instruction.
     * @return The call as text.
     */
    private static String formatCall(@NonNull Instruction instruction) {
        final MemLocation method = instruction.firstLocation();
        final MemLocation argumentCount = instruction.secondLocation();
        StringBuilder result = new StringBuilder("CALL ");
        if (method != null && method.area() == MemArea.STACK) {
            result.append("stack.");
        }
        result.append(method == null ? "?" : String.valueOf(method.value()));
        final Object count = argumentCount == null ? null : argumentCount.value();
        result.append('(');
        if (count instanceof Integer number) {
            result.append(number).append(number == 1 ? " arg" : " args");
        } else {
            result.append('?');
        }
        result.append(')');
        if (instruction.targetLocation() != null) {
            result.append(" -> stack");
        }
        return result.toString();
    }

    /**
     * Format where a jump goes.
     *
     * @param location The location holding the instruction index.
     * @return The instruction index.
     */
    private static String formatJumpTarget(MemLocation location) {
        if (location == null || !(location.value() instanceof Integer index)) {
            return "?";
        }
        return String.format("%04d", index);
    }

    /**
     * Format a memory location.
     *
     * @param location The location.
     * @return The location as text.
     */
    public static String formatLocation(@NonNull MemLocation location) {
        return switch (location.area()) {
            case STACK -> "stack" + formatType(location);
            case VARIABLE -> location.value() + formatType(location);
            case IMMEDIATE -> formatLiteral(location.value());
        };
    }

    /**
     * Format a literal value.
     *
     * @param value The value.
     * @return The value as it would appear in a script.
     */
    private static String formatLiteral(Object value) {
        if (value instanceof String string) {
            return '"' + escape(string) + '"';
        }
        if (value instanceof Character character) {
            return "'" + escape(character.toString()) + "'";
        }
        return String.valueOf(value);
    }

    /**
     * Format the type of a location, if it has one worth showing.
     *
     * @param location The location.
     * @return The type with a leading colon, or an empty string.
     */
    private static String formatType(@NonNull MemLocation location) {
        if (location.isBoolean()) {
            return ":boolean";
        }
        if (location.isChar()) {
            return ":char";
        }
        if (location.isDouble()) {
            return ":double";
        }
        if (location.isInt()) {
            return ":int";
        }
        if (location.isString()) {
            return ":string";
        }
        return "";
    }

    /**
     * Escape characters that would make a literal hard to read.
     *
     * @param value The text.
     * @return The escaped text.
     */
    private static String escape(@NonNull String value) {
        return value.replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /** Private constructor so that this class is not instantiated. */
    private InstructionFormatter() {
        throw new UnsupportedOperationException("This utility class should not be instantiated");
    }
}
