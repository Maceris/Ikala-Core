package com.ikalagaming.scripting.interpreter;

import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.scripting.ast.Type;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;

/**
 * A runtime environment for a script, equivalent to a small VM or Turing
 * machine.
 *
 * @author Ches Burks
 *
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class ScriptRuntime {

	/**
	 * Used instead of null memory.
	 */
	private static final MemoryItem VOID_MEMORY =
		new MemoryItem(Void.class, "void");

	/**
	 * If we should stop running the program.
	 */
	private boolean fatalError;
	/**
	 * The actual program, a list of instructions.
	 */
	private final List<Instruction> instructions;
	/**
	 * An equivalent to a register where the result of the last comparison is
	 * stored.
	 *
	 * When comparing numbers: If the first number is less than the second, this
	 * will be -1. If they are equal, it will be 0. If the first number is
	 * greater than the second, this will be 1.
	 *
	 * When comparing equality: If two items are equal, this will be zero. If
	 * they are not equal, this will be 1.
	 */
	private int lastComparison;
	/**
	 * Where we are in the program.
	 */
	private int programCounter = 0;

	/**
	 * The stack.
	 */
	private ArrayDeque<MemoryItem> stack = new ArrayDeque<>();

	/**
	 * The variables in the program, which retain type information, for my own
	 * sanity.
	 */
	private Map<String, MemoryItem> symbolTable = new HashMap<>();

	/**
	 * Deal with logical operations on two booleans.
	 *
	 * @param i The instruction.
	 * @param operation The operation to perform on the two booleans.
	 */
	private void boolLogic(Instruction i, BinaryOperator<Boolean> operation) {

		final MemLocation firstLocation = i.firstLocation();
		final MemLocation secondLocation = i.secondLocation();

		final MemoryItem firstItem = this.loadValue(firstLocation);
		final MemoryItem secondItem = this.loadValue(secondLocation);

		if (this.fatalError) {
			return;
		}
		this.checkType(firstLocation, Type.Base.BOOLEAN);
		this.checkType(secondLocation, Type.Base.BOOLEAN);
		if (this.fatalError) {
			return;
		}

		boolean first = (Boolean) firstItem.value();
		boolean second = (Boolean) secondItem.value();

		MemoryItem result =
			new MemoryItem(Boolean.class, operation.apply(first, second));

		this.storeValue(result, i.targetLocation());
	}

	/**
	 * Deal with any kind of math operation on two integers.
	 *
	 * @param i The instruction.
	 * @param operation The operation to perform on the two numbers.
	 */
	private void charMath(Instruction i, BinaryOperator<Character> operation) {

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
			firstNumber = (Character) firstItem.value();
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
		final String MEMORY_MISMATCH = "MEMORY_TYPE_MISMATCH";
		switch (intended) {
			case BOOLEAN:
				if (!memory.isBoolean()) {
					ScriptRuntime.log.warn(
						SafeResourceLoader.getString(MEMORY_MISMATCH,
							ScriptManager.getResourceBundle()),
						intended.toString());
					this.halt();
				}
				break;
			case CHAR:
				if (!memory.isChar()) {
					ScriptRuntime.log.warn(
						SafeResourceLoader.getString(MEMORY_MISMATCH,
							ScriptManager.getResourceBundle()),
						intended.toString());
					this.halt();
				}
				break;
			case DOUBLE:
				if (!(memory.isChar() || memory.isInt() || memory.isDouble())) {
					ScriptRuntime.log.warn(
						SafeResourceLoader.getString(MEMORY_MISMATCH,
							ScriptManager.getResourceBundle()),
						intended.toString());
					this.halt();
				}
				break;
			case INT:
				if (!(memory.isChar() || memory.isInt())) {
					ScriptRuntime.log.warn(
						SafeResourceLoader.getString(MEMORY_MISMATCH,
							ScriptManager.getResourceBundle()),
						intended.toString());
					this.halt();
				}
				break;
			case STRING:
				// We can cast pretty much anything to string, so ignore this
				break;
			case LABEL, IDENTIFIER, UNKNOWN, VOID:
			default:
				ScriptRuntime.log.warn(
					SafeResourceLoader.getString("INVALID_MEMORY_TYPE",
						ScriptManager.getResourceBundle()),
					intended.toString());
				this.halt();
				break;
		}
	}

	/**
	 * Compares two items, either for equality or numerically.
	 *
	 * @param i The instruction to execute.
	 * @see #lastComparison
	 */
	private void compare(Instruction i) {
		final MemLocation firstLocation = i.firstLocation();
		final MemLocation secondLocation = i.secondLocation();

		final MemoryItem firstItem = this.loadValue(firstLocation);
		final MemoryItem secondItem = this.loadValue(secondLocation);

		if (this.fatalError) {
			return;
		}

		if ((firstLocation.isChar() || firstLocation.isDouble()
			|| firstLocation.isInt())
			&& (secondLocation.isChar() || secondLocation.isDouble()
				|| secondLocation.isInt())) {

			this.doubleComparison(i, firstItem, secondItem, (a, b) -> {
				final double TOLERANCE = 0.00001;
				if (Math.abs(a - b) < TOLERANCE) {
					this.lastComparison = 0;
				}
				else if (a < b) {
					this.lastComparison = -1;
				}
				else if (a > b) {
					this.lastComparison = 1;
				}
			});
			return;
		}

		Object first = firstItem.value();
		Object second = secondItem.value();
		if (first.equals(second)) {
			this.lastComparison = 0;
		}
		else {
			// Different types
			this.lastComparison = 1;
		}
	}

	/**
	 * Concatenate strings together. Automatically converts whatever is in the
	 * arguments to strings.
	 *
	 * @param i The instruction we are executing.
	 */
	private void concatStrings(Instruction i) {
		final MemLocation firstLocation = i.firstLocation();
		final MemLocation secondLocation = i.secondLocation();

		final MemoryItem firstItem = this.loadValue(firstLocation);
		final MemoryItem secondItem = this.loadValue(secondLocation);

		if (this.fatalError) {
			return;
		}

		String first = firstItem.value().toString();
		String second = secondItem.value().toString();

		MemoryItem result = new MemoryItem(String.class, first + second);

		this.storeValue(result, i.targetLocation());
	}

	/**
	 * Compares the values of the given instruction as doubles using the
	 * provided function.
	 *
	 * @param i The instruction we are doing comparisons on.
	 * @param firstItem The first memory item.
	 * @param secondItem The second memory item.
	 * @param comparisonFunction The function to use for comparisons.
	 */
	private void doubleComparison(Instruction i, MemoryItem firstItem,
		MemoryItem secondItem, BiConsumer<Double, Double> comparisonFunction) {
		final MemLocation firstLocation = i.firstLocation();
		final MemLocation secondLocation = i.secondLocation();

		if (this.fatalError) {
			return;
		}

		// Just cast all numbers to doubles.
		double first;
		double second;

		if (firstLocation.isInt()) {
			first = (int) firstItem.value();
		}
		else if (firstLocation.isChar()) {
			first = (char) firstItem.value();
		}
		else if (firstLocation.isDouble()) {
			first = (double) firstItem.value();
		}
		else {
			// Should not happen
			this.halt();
			return;
		}

		if (secondLocation.isInt()) {
			second = (int) secondItem.value();
		}
		else if (secondLocation.isChar()) {
			second = (char) secondItem.value();
		}
		else if (secondLocation.isDouble()) {
			second = (double) secondItem.value();
		}
		else {
			// Should not happen
			this.halt();
			return;
		}

		comparisonFunction.accept(first, second);
	}

	/**
	 * Deal with any kind of math operation on two doubles.
	 *
	 * @param i The instruction.
	 * @param operation The operation to perform on the two numbers.
	 */
	private void doubleMath(Instruction i, DoubleBinaryOperator operation) {

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
			operation.applyAsDouble(firstNumber, secondNumber));

		this.storeValue(result, i.targetLocation());
	}

	private void call(Instruction i) {
		// TODO implement

		final MemArea objectLocation = i.firstLocation().area();
		final String methodName = i.firstLocation().value().toString();
		final int numParams = (Integer) i.secondLocation().value();
	}

	private void execute(Instruction i) {
		switch (i.type()) {
			case ADD_CHAR:
				this.charMath(i, (a, b) -> (char) (a + b));
				this.programCounter++;
				break;
			case ADD_DOUBLE:
				this.doubleMath(i, (a, b) -> a + b);
				this.programCounter++;
				break;
			case ADD_INT:
				this.intMath(i, (a, b) -> a + b);
				this.programCounter++;
				break;
			case AND:
				this.boolLogic(i, (a, b) -> a && b);
				this.programCounter++;
				break;
			case ARRAY_ACCESS:
				// TODO implement
				this.programCounter++;
				break;
			case CAST:
				// TODO implement
				this.programCounter++;
				break;
			case CALL:
				call(i);
				this.programCounter++;
				break;
			case CMP:
				this.compare(i);
				this.programCounter++;
				break;
			case CONCAT_STRING:
				this.concatStrings(i);
				this.programCounter++;
				break;
			case DIV_CHAR:
				this.charMath(i, (a, b) -> (char) (a / b));
				this.programCounter++;
				break;
			case DIV_DOUBLE:
				this.doubleMath(i, (a, b) -> a / b);
				this.programCounter++;
				break;
			case DIV_INT:
				this.intMath(i, (a, b) -> a / b);
				this.programCounter++;
				break;
			case HALT:
				this.halt();
				break;
			case JEQ:
				this.jump(i, comp -> comp == 0);
				break;
			case JGE:
				this.jump(i, comp -> comp >= 0);
				break;
			case JGT:
				this.jump(i, comp -> comp > 0);
				break;
			case JLE:
				this.jump(i, comp -> comp <= 0);
				break;
			case JLT:
				this.jump(i, comp -> comp < 0);
				break;
			case JMP:
				this.jump(i, comp -> true);
				break;
			case JNE:
				this.jump(i, comp -> comp != 0);
				break;
			case MOD_CHAR:
				this.charMath(i, (a, b) -> (char) (a % b));
				this.programCounter++;
				break;
			case MOD_DOUBLE:
				this.doubleMath(i, (a, b) -> a % b);
				this.programCounter++;
				break;
			case MOD_INT:
				this.intMath(i, (a, b) -> a % b);
				this.programCounter++;
				break;
			case MOV:
				this.move(i);
				this.programCounter++;
				break;
			case MUL_CHAR:
				this.charMath(i, (a, b) -> (char) (a * b));
				this.programCounter++;
				break;
			case MUL_DOUBLE:
				this.doubleMath(i, (a, b) -> a * b);
				this.programCounter++;
				break;
			case MUL_INT:
				this.intMath(i, (a, b) -> a * b);
				this.programCounter++;
				break;
			case NEG_CHAR:
				this.negateChar(i);
				this.programCounter++;
				break;
			case NEG_DOUBLE:
				this.negateDouble(i);
				this.programCounter++;
				break;
			case NEG_INT:
				this.negateInt(i);
				this.programCounter++;
				break;
			case NOP:
				// No operation
				this.programCounter++;
				break;
			case NOT:
				// TODO implement
				this.programCounter++;
				break;
			case OR:
				this.boolLogic(i, (a, b) -> a || b);
				this.programCounter++;
				break;
			case SUB_CHAR:
				this.charMath(i, (a, b) -> (char) (a - b));
				this.programCounter++;
				break;
			case SUB_DOUBLE:
				this.doubleMath(i, (a, b) -> a - b);
				this.programCounter++;
				break;
			case SUB_INT:
				this.intMath(i, (a, b) -> a - b);
				this.programCounter++;
				break;
			case SET_EQ:
				this.set(i, cmp -> cmp == 0);
				this.programCounter++;
				break;
			case SET_GE:
				this.set(i, cmp -> cmp >= 0);
				this.programCounter++;
				break;
			case SET_GT:
				this.set(i, cmp -> cmp > 0);
				this.programCounter++;
				break;
			case SET_LE:
				this.set(i, cmp -> cmp <= 0);
				this.programCounter++;
				break;
			case SET_LT:
				this.set(i, cmp -> cmp < 0);
				this.programCounter++;
				break;
			case SET_NE:
				this.set(i, cmp -> cmp != 0);
				this.programCounter++;
				break;
			default:
				ScriptRuntime.log.warn(
					SafeResourceLoader.getString("UNKNOWN_INSTRUCTION",
						ScriptManager.getResourceBundle()),
					i.type().toString());
				this.halt();
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
	 * Check if the program has terminated.
	 *
	 * @return Whether we have terminated the program.
	 */
	public boolean hasTerminated() {
		return this.programCounter == this.instructions.size();
	}

	/**
	 * Deal with any kind of math operation on two integers.
	 *
	 * @param i The instruction.
	 * @param operation The operation to perform on the two numbers.
	 */
	private void intMath(Instruction i, IntBinaryOperator operation) {

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
			operation.applyAsInt(firstNumber, secondNumber));

		this.storeValue(result, i.targetLocation());
	}

	/**
	 * A conditional jump. We jump to the given location if the given function
	 * returns true when passed the last comparison value. If we don't jump, we
	 * just move to the next instruction.
	 *
	 * @param instruction The relevant jump instruction.
	 * @param operator The function that determines if we should jump.
	 */
	private void jump(Instruction instruction, IntPredicate operator) {
		final int location = (Integer) instruction.firstLocation().value();
		if (location < 0 || location > this.instructions.size()) {
			// instructions.size is for when we want to bail on the program.
			ScriptRuntime.log
				.warn(SafeResourceLoader.getString("INVALID_JUMP_LOCATION",
					ScriptManager.getResourceBundle()), location);
			this.halt();
			return;
		}
		if (operator.test(this.lastComparison)) {
			this.programCounter = location;
		}
		else {
			this.programCounter++;
		}
	}

	/**
	 * Read the value from the memory location. You should check if the program
	 * halted after using this.
	 *
	 * @param from The location we are reading from.
	 * @return The appropriate value, will be void memory if the location is
	 *         invalid.
	 */
	private MemoryItem loadValue(MemLocation from) {
		switch (from.area()) {
			case IMMEDIATE:
				return new MemoryItem(from.type(), from.value());
			case STACK:
				if (this.stack.isEmpty()) {
					ScriptRuntime.log.warn(SafeResourceLoader.getString(
						"POPPING_TOO_FAR", ScriptManager.getResourceBundle()));
					this.halt();
					return ScriptRuntime.VOID_MEMORY;
				}
				return this.stack.pop();
			case VARIABLE:
				if (!this.symbolTable.containsKey(from.value())) {
					ScriptRuntime.log
						.warn(
							SafeResourceLoader.getString("UNKNOWN_VARIABLE",
								ScriptManager.getResourceBundle()),
							from.value());
					this.halt();
					return ScriptRuntime.VOID_MEMORY;
				}
				return this.symbolTable.get(from.value());
			default:
				ScriptRuntime.log.warn(
					SafeResourceLoader.getString("UNKNOWN_MEMORY_AREA",
						ScriptManager.getResourceBundle()),
					from.area().toString());
				this.halt();
				return ScriptRuntime.VOID_MEMORY;
		}
	}

	/**
	 * Execute a move instruction.
	 *
	 * @param i The instruction to execute.
	 */
	private void move(@NonNull Instruction i) {
		MemoryItem memory = this.loadValue(i.firstLocation());
		this.storeValue(memory, i.targetLocation());
	}

	/**
	 * Negate the sign of a character.
	 *
	 * @param i The instruction.
	 */
	private void negateChar(Instruction i) {
		final MemLocation firstLocation = i.firstLocation();
		final MemoryItem firstItem = this.loadValue(firstLocation);

		if (this.fatalError) {
			return;
		}
		this.checkType(firstLocation, Type.Base.CHAR);
		if (this.fatalError) {
			return;
		}

		char firstNumber;

		if (firstLocation.isChar()) {
			firstNumber = (Character) firstItem.value();
		}
		else {
			// Can't happen because of type checks, but just to be thorough
			firstNumber = 0;
		}

		MemoryItem result = new MemoryItem(Character.class, -firstNumber);

		this.storeValue(result, i.targetLocation());
	}

	/**
	 * Negate the sign of a double.
	 *
	 * @param i The instruction.
	 */
	private void negateDouble(Instruction i) {
		final MemLocation firstLocation = i.firstLocation();
		final MemoryItem firstItem = this.loadValue(firstLocation);

		if (this.fatalError) {
			return;
		}
		this.checkType(firstLocation, Type.Base.DOUBLE);
		if (this.fatalError) {
			return;
		}

		double firstNumber;

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

		MemoryItem result = new MemoryItem(Double.class, -firstNumber);

		this.storeValue(result, i.targetLocation());
	}

	/**
	 * Negate the sign of an integer.
	 *
	 * @param i The instruction.
	 */
	private void negateInt(Instruction i) {
		final MemLocation firstLocation = i.firstLocation();
		final MemoryItem firstItem = this.loadValue(firstLocation);

		if (this.fatalError) {
			return;
		}
		this.checkType(firstLocation, Type.Base.INT);
		if (this.fatalError) {
			return;
		}

		int firstNumber;

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

		MemoryItem result = new MemoryItem(Integer.class, -firstNumber);

		this.storeValue(result, i.targetLocation());
	}

	/**
	 * Perform a set operation.
	 *
	 * @param instruction The instruction to extract a target location from.
	 * @param operation The operation that takes the last comparison and outputs
	 *            a boolean result.
	 */
	private void set(Instruction instruction, IntPredicate operation) {
		MemoryItem result =
			new MemoryItem(Boolean.class, operation.test(this.lastComparison));
		this.storeValue(result, instruction.targetLocation());
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
				if (this.symbolTable.containsKey(variable)) {
					MemoryItem existingValue = this.symbolTable.get(variable);
					if (!existingValue.getClass().equals(item.getClass())) {
						ScriptRuntime.log.warn(
							SafeResourceLoader.getString(
								"VARIABLE_TYPE_MISMATCH",
								ScriptManager.getResourceBundle()),
							item.getClass().getSimpleName(), variable,
							existingValue.getClass().getSimpleName());
						this.halt();
						break;
					}
				}
				this.symbolTable.put(variable, item);
				break;
			case IMMEDIATE:
			default:
				ScriptRuntime.log
					.warn(
						SafeResourceLoader.getString("INVALID_MEMORY_LOCATION",
							ScriptManager.getResourceBundle()),
						location.area());
				this.halt();
				break;
		}
	}

}
