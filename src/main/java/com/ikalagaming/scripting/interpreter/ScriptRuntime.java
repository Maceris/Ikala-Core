package com.ikalagaming.scripting.interpreter;

import com.ikalagaming.scripting.ast.Type;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * A runtime environment for a script, equivalent to a small VM or Turing
 * machine.
 *
 * @author Ches Burks
 *
 */
@Slf4j
public class ScriptRuntime {
	/**
	 * Where we are in the program.
	 */
	private int programCounter = 0;
	/**
	 * The actual program, a list of instructions.
	 */
	private List<Instruction> instructions = new ArrayList<>();
	/**
	 * The variables in the program, which retain type information, for my own
	 * sanity.
	 */
	private Map<String, MemoryItem> symbolTable = new HashMap<>();
	/**
	 * The stack.
	 */
	private ArrayDeque<MemoryItem> stack = new ArrayDeque<>();

	/**
	 * An equivalent to a register where the result of the last comparison is
	 * stored.
	 */
	private short lastComparison;

	/**
	 * If we should stop running the program.
	 */
	private boolean fatalError;

	/**
	 * Add chars.
	 *
	 * @param i The instruction we are executing.
	 */
	private void addChar(Instruction i) {
		this.charMath(i, (a, b) -> (char) (a + b));

	}

	/**
	 * Add doubles.
	 *
	 * @param i The instruction we are executing.
	 */
	private void addDouble(Instruction i) {
		this.doubleMath(i, (a, b) -> a + b);
	}

	/**
	 * Add integers.
	 *
	 * @param i The instruction we are executing.
	 */
	private void addInt(Instruction i) {
		this.intMath(i, (a, b) -> a + b);
	}

	/**
	 * Deal with any kind of math operation on two integers.
	 *
	 * @param i The instruction.
	 * @param operation The operation to perform on the two numbers.
	 */
	private void charMath(Instruction i,
		BiFunction<Character, Character, Character> operation) {

		final MemLocation firstLocation = i.firstLocation();
		final MemLocation secondLocation = i.secondLocation();

		final MemoryItem firstItem = this.loadValue(firstLocation);
		final MemoryItem secondItem = this.loadValue(secondLocation);

		if (this.fatalError) {
			return;
		}
		this.checkType(firstLocation, Type.Base.CHAR);
		this.checkType(secondLocation, Type.Base.CHAR);
		if (this.fatalError) {
			return;
		}

		char firstNumber;
		char secondNumber;

		if (firstLocation.isChar()) {
			final char unboxed = (Character) firstItem.value();
			firstNumber = unboxed;
		}
		else {
			// Can't happen because of type checks, but just to be thorough
			firstNumber = 0;
		}

		if (secondLocation.isChar()) {
			final char unboxed = (Character) secondItem.value();
			secondNumber = unboxed;
		}
		else {
			// Can't happen because of type checks, but just to be thorough
			secondNumber = 0;
		}

		MemoryItem result = new MemoryItem(Character.class,
			operation.apply(firstNumber, secondNumber));

		this.storeValue(result, i.targetLocation());
	}

	/**
	 * Check the memory is the expected type, halt the program if not.
	 *
	 * @param memory The memory to check.
	 * @param intended The type we are expecting to see or at least cast to.
	 */
	private void checkType(MemLocation memory, Type.Base intended) {
		switch (intended) {
			case BOOLEAN:
				if (!memory.isBoolean()) {
					ScriptRuntime.log.warn("Memory is not a boolean!");
					this.halt();
				}
				break;
			case CHAR:
				if (!memory.isChar()) {
					ScriptRuntime.log.warn("Memory is not a char!");
					this.halt();
				}
				break;
			case DOUBLE:
				if (!(memory.isChar() || memory.isInt() || memory.isDouble())) {
					ScriptRuntime.log.warn("Memory is not a double!");
					this.halt();
				}
				break;
			case INT:
				if (!(memory.isChar() || memory.isInt())) {
					ScriptRuntime.log.warn("Memory is not an int!");
					this.halt();
				}
				break;
			case STRING:
				// We can cast pretty much anything to string, so ignore this
				break;
			case LABEL:
			case IDENTIFIER:
			case UNKNOWN:
			case VOID:
			default:
				ScriptRuntime.log.warn("Invalid type {}", intended.toString());
				this.halt();
				break;
		}
	}

	/**
	 * Divide chars.
	 *
	 * @param i The instruction we are executing.
	 */
	private void divChar(Instruction i) {
		this.charMath(i, (a, b) -> (char) (a / b));

	}

	/**
	 * Divide doubles.
	 *
	 * @param i The instruction we are executing.
	 */
	private void divDouble(Instruction i) {
		this.doubleMath(i, (a, b) -> a / b);
	}

	/**
	 * Divide integers.
	 *
	 * @param i The instruction we are executing.
	 */
	private void divInt(Instruction i) {
		this.intMath(i, (a, b) -> a / b);
	}

	/**
	 * Deal with any kind of math operation on two doubles.
	 *
	 * @param i The instruction.
	 * @param operation The operation to perform on the two numbers.
	 */
	private void doubleMath(Instruction i,
		BiFunction<Double, Double, Double> operation) {

		final MemLocation firstLocation = i.firstLocation();
		final MemLocation secondLocation = i.secondLocation();

		final MemoryItem firstItem = this.loadValue(firstLocation);
		final MemoryItem secondItem = this.loadValue(secondLocation);

		if (this.fatalError) {
			return;
		}
		this.checkType(firstLocation, Type.Base.DOUBLE);
		this.checkType(secondLocation, Type.Base.DOUBLE);
		if (this.fatalError) {
			return;
		}

		double firstNumber;
		double secondNumber;

		if (firstLocation.isDouble()) {
			firstNumber = (Double) firstItem.value();
		}
		else if (firstLocation.isInt()) {
			final int unboxed = (Integer) firstItem.value();
			firstNumber = unboxed;
		}
		else if (firstLocation.isChar()) {
			final char unboxed = (Character) firstItem.value();
			firstNumber = unboxed;
		}
		else {
			// Can't happen because of type checks, but just to be thorough
			firstNumber = 0;
		}

		if (secondLocation.isDouble()) {
			secondNumber = (Double) secondItem.value();
		}
		else if (secondLocation.isInt()) {
			final int unboxed = (Integer) secondItem.value();
			secondNumber = unboxed;
		}
		else if (secondLocation.isChar()) {
			final char unboxed = (Character) secondItem.value();
			secondNumber = unboxed;
		}

		else {
			// Can't happen because of type checks, but just to be thorough
			secondNumber = 0;
		}

		MemoryItem result = new MemoryItem(Double.class,
			operation.apply(firstNumber, secondNumber));

		this.storeValue(result, i.targetLocation());
	}

	private void execute(Instruction i) {
		switch (i.type()) {
			case ADD_CHAR:
				this.addChar(i);
				break;
			case ADD_DOUBLE:
				this.addDouble(i);
				break;
			case ADD_INT:
				this.addInt(i);
				break;
			case AND:
				break;
			case ARRAY_ACCESS:
				break;
			case CALL:
				break;
			case CMP:
				break;
			case CONCAT_STRING:
				break;
			case DIV_CHAR:
				this.divChar(i);
				break;
			case DIV_DOUBLE:
				this.divDouble(i);
				break;
			case DIV_INT:
				this.divInt(i);
				break;
			case FIELD_ACCESS:
				break;
			case HALT:
				halt();
				break;
			case JEQ:
				break;
			case JGE:
				break;
			case JGT:
				break;
			case JLE:
				break;
			case JLT:
				break;
			case JMP:
				break;
			case JNE:
				break;
			case MOD_CHAR:
				this.modChar(i);
				break;
			case MOD_DOUBLE:
				this.modDouble(i);
				break;
			case MOD_INT:
				this.modInt(i);
				break;
			case MOV:
				break;
			case MUL_CHAR:
				this.mulChar(i);
				break;
			case MUL_DOUBLE:
				this.mulDouble(i);
				break;
			case MUL_INT:
				this.mulInt(i);
				break;
			case NOP:
				// No operation
				break;
			case NOT:
				break;
			case OR:
				break;
			case SUB_CHAR:
				this.subChar(i);
				break;
			case SUB_DOUBLE:
				this.subDouble(i);
				break;
			case SUB_INT:
				this.subInt(i);
				break;
			default:
				ScriptRuntime.log.warn("Unknown instruction {}",
					i.type().toString());
				break;
		}
	}

	/**
	 * Stop running the program.
	 */
	private void halt() {
		this.fatalError = true;
		this.programCounter = this.instructions.size();
	}

	/**
	 * Deal with any kind of math operation on two integers.
	 *
	 * @param i The instruction.
	 * @param operation The operation to perform on the two numbers.
	 */
	private void intMath(Instruction i,
		BiFunction<Integer, Integer, Integer> operation) {

		final MemLocation firstLocation = i.firstLocation();
		final MemLocation secondLocation = i.secondLocation();

		final MemoryItem firstItem = this.loadValue(firstLocation);
		final MemoryItem secondItem = this.loadValue(secondLocation);

		if (this.fatalError) {
			return;
		}
		this.checkType(firstLocation, Type.Base.INT);
		this.checkType(secondLocation, Type.Base.INT);
		if (this.fatalError) {
			return;
		}

		int firstNumber;
		int secondNumber;

		if (firstLocation.isInt()) {
			firstNumber = (Integer) firstItem.value();
		}
		else if (firstLocation.isChar()) {
			final char unboxed = (Character) firstItem.value();
			firstNumber = unboxed;
		}
		else {
			// Can't happen because of type checks, but just to be thorough
			firstNumber = 0;
		}

		if (secondLocation.isInt()) {
			secondNumber = (Integer) secondItem.value();
		}
		else if (secondLocation.isChar()) {
			final char unboxed = (Character) secondItem.value();
			secondNumber = unboxed;
		}
		else {
			// Can't happen because of type checks, but just to be thorough
			secondNumber = 0;
		}

		MemoryItem result = new MemoryItem(Integer.class,
			operation.apply(firstNumber, secondNumber));

		this.storeValue(result, i.targetLocation());
	}

	/**
	 * Read the value from the memory location.
	 *
	 * @param from The location we are reading from.
	 * @return The appropriate value, will be null if the location is invalid.
	 */
	private MemoryItem loadValue(MemLocation from) {
		switch (from.area()) {
			case IMMEDIATE:
				return new MemoryItem(from.getClass(), from.value());
			case STACK:
				if (this.stack.size() <= 0) {
					ScriptRuntime.log
						.warn("Trying to pop more than is on the stack!");
					this.halt();
					return null;
				}
				return this.stack.pop();
			case VARIABLE:
				if (!this.symbolTable.containsKey(from.value())) {
					ScriptRuntime.log.warn("Unkown variable {}!", from.value());
					this.halt();
					return null;
				}
				return this.symbolTable.get(from.value());
			default:
				ScriptRuntime.log.warn("Unknown memory area {}",
					from.area().toString());
				this.halt();
				return null;
		}
	}

	/**
	 * Modulus for chars.
	 *
	 * @param i The instruction we are executing.
	 */
	private void modChar(Instruction i) {
		this.charMath(i, (a, b) -> (char) (a % b));

	}

	/**
	 * Modulus for doubles.
	 *
	 * @param i The instruction we are executing.
	 */
	private void modDouble(Instruction i) {
		this.doubleMath(i, (a, b) -> a % b);
	}

	/**
	 * Modulus for integers.
	 *
	 * @param i The instruction we are executing.
	 */
	private void modInt(Instruction i) {
		this.intMath(i, (a, b) -> a % b);
	}

	/**
	 * Multiply chars.
	 *
	 * @param i The instruction we are executing.
	 */
	private void mulChar(Instruction i) {
		this.charMath(i, (a, b) -> (char) (a * b));

	}

	/**
	 * Multiply doubles.
	 *
	 * @param i The instruction we are executing.
	 */
	private void mulDouble(Instruction i) {
		this.doubleMath(i, (a, b) -> a * b);
	}

	/**
	 * Multiply integers.
	 *
	 * @param i The instruction we are executing.
	 */
	private void mulInt(Instruction i) {
		this.intMath(i, (a, b) -> a * b);
	}

	/**
	 * Execute one instruction.
	 */
	public void step() {
		if (this.fatalError || (this.programCounter < 0)
			|| (this.programCounter >= this.instructions.size())) {
			// Stop executing
			return;
		}
		this.execute(this.instructions.get(this.programCounter));
	}

	/**
	 * Store a value in the specified memory location. May halt the program if
	 * something goes wrong.
	 *
	 * @param item The item to store.
	 * @param location The location to store the item in.
	 */
	private void storeValue(MemoryItem item, MemLocation location) {
		switch (location.area()) {
			case STACK:
				this.stack.push(item);
				break;
			case VARIABLE:
				final String variable = (String) location.value();
				if (!this.symbolTable.containsKey(variable)) {
					ScriptRuntime.log.warn("Invalid variable {}", variable);
					this.halt();
					break;
				}
				MemoryItem existingValue = this.symbolTable.get(variable);
				if (!existingValue.getClass().equals(item.getClass())) {
					ScriptRuntime.log.warn(
						"Trying to store the type {} in the variable {}, but it's currently {}",
						item.getClass().getSimpleName(), variable,
						existingValue.getClass().getSimpleName());
					this.halt();
					break;
				}
				this.symbolTable.put(variable, item);
				break;
			case IMMEDIATE:
			default:
				ScriptRuntime.log.warn("Invalid memory location {}",
					location.area());
				this.halt();
				break;
		}
	}

	/**
	 * Subtract chars.
	 *
	 * @param i The instruction we are executing.
	 */
	private void subChar(Instruction i) {
		this.charMath(i, (a, b) -> (char) (a - b));

	}

	/**
	 * Subtract doubles.
	 *
	 * @param i The instruction we are executing.
	 */
	private void subDouble(Instruction i) {
		this.doubleMath(i, (a, b) -> a - b);
	}

	/**
	 * Subtract integers.
	 *
	 * @param i The instruction we are executing.
	 */
	private void subInt(Instruction i) {
		this.intMath(i, (a, b) -> a - b);
	}
}
