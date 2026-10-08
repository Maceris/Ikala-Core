package com.ikalagaming.scripting.interpreter;

import lombok.NonNull;

/**
 * A single unit of execution.
 *
 * @author Ches Burks
 * @param type The type of instruction.
 * @param firstLocation The first location to read. Null if we don't have any operands.
 * @param secondLocation The second location to read. Null if we have zero or one operand.
 * @param targetLocation The location to write results to. Null if we don't have output. In the case
 *     of variable storage, the value will be the name of the variable as a string.
 * @param line The line in the source the instruction came from, starting at 1, or -1 if unknown.
 */
public record Instruction(
        @NonNull InstructionType type,
        MemLocation firstLocation,
        MemLocation secondLocation,
        MemLocation targetLocation,
        int line) {

    /**
     * Create an instruction without a source line.
     *
     * @param type The type of instruction.
     * @param firstLocation The first location to read. Null if we don't have any operands.
     * @param secondLocation The second location to read. Null if we have zero or one operand.
     * @param targetLocation The location to write results to. Null if we don't have output.
     */
    public Instruction(
            @NonNull InstructionType type,
            MemLocation firstLocation,
            MemLocation secondLocation,
            MemLocation targetLocation) {
        this(type, firstLocation, secondLocation, targetLocation, -1);
    }

    /**
     * Create a copy of this instruction with a different source line.
     *
     * @param newLine The line in the source, starting at 1, or -1 if unknown.
     * @return The new instruction.
     */
    public Instruction withLine(int newLine) {
        return new Instruction(type, firstLocation, secondLocation, targetLocation, newLine);
    }
}
