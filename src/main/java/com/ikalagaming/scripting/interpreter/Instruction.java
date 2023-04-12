package com.ikalagaming.scripting.interpreter;

import lombok.NonNull;

/**
 * A single unit of execution.
 *
 * @author Ches Burks
 * @param type The type of instruction.
 * @param firstLocation The first location to read. Null if we don't have any
 *            operands.
 * @param secondLocation The second location to read. Null if we have zero or
 *            one operand.
 * @param targetLocation The location to write results to. Null if we don't have
 *            output. In the case of variable storage, the value will be the
 *            name of the variable as a string.
 *
 */
public record Instruction(@NonNull InstructionType type,
	MemLocation firstLocation, MemLocation secondLocation,
	MemLocation targetLocation) {}
