package com.ikalagaming.scripting.interpreter;

import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.scripting.ast.Type;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;

/**
 * A runtime environment for a script, equivalent to a small VM or Turing machine.
 *
 * @author Ches Burks
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class ScriptRuntime {

    /** Used instead of null memory. */
    private static final MemoryItem VOID_MEMORY = new MemoryItem(Void.class, "void");

    /** If we should stop running the program. */
    private boolean fatalError;

    /** The actual program, a list of instructions. */
    private final List<Instruction> instructions;

    /**
     * An equivalent to a register where the result of the last comparison is stored.
     *
     * <p>When comparing numbers: If the first number is less than the second, this will be -1. If
     * they are equal, it will be 0. If the first number is greater than the second, this will be 1.
     *
     * <p>When comparing equality: If two items are equal, this will be zero. If they are not equal,
     * this will be 1.
     */
    private int lastComparison;

    /** Where we are in the program. */
    private int programCounter = 0;

    /** The stack. */
    private ArrayDeque<MemoryItem> stack = new ArrayDeque<>();

    /** The variables in the program, which retain type information, for my own sanity. */
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

        boolean first;
        boolean second;

        try {
            first = (Boolean) firstItem.value();
            second = (Boolean) secondItem.value();
        } catch (ClassCastException e) {
            ScriptRuntime.log.warn(
                    SafeResourceLoader.getString("CAST_FAILED", ScriptManager.getResourceBundle()),
                    firstItem.getClass(),
                    Boolean.class);
            this.halt();
            return;
        }

        MemoryItem result = new MemoryItem(Boolean.class, operation.apply(first, second));

        this.storeValue(result, i.targetLocation());
    }

    /**
     * Handle method calls to java, both static and on objects.
     *
     * @param i The instruction we are executing.
     */
    private void call(Instruction i) {
        final MemArea objectLocation = i.firstLocation().area();
        final String methodName = i.firstLocation().value().toString();
        final int numParams = (Integer) i.secondLocation().value();

        // All parameters come from the stack, might as well reuse this object
        final MemLocation stackLocation = new MemLocation(MemArea.STACK, Object.class);

        /** The object to invoke a method on. */
        Object object = null;
        /** Methods that match the name and number of parameters. */
        List<Method> options;
        List<MemoryItem> parameters = new ArrayList<>();
        for (int paramIndex = 0; paramIndex < numParams; ++paramIndex) {
            parameters.add(this.loadValue(stackLocation));
            if (this.fatalError) {
                return;
            }
        }

        if (this.reservedMethods(objectLocation, methodName, numParams, parameters)) {
            return;
        }

        if (objectLocation != MemArea.IMMEDIATE) {
            final MemoryItem first = this.loadValue(i.firstLocation());

            object = first.value();

            Method[] methods = object.getClass().getMethods();

            options = new ArrayList<>();

            for (Method m : methods) {
                if (m.getName().equals(methodName) && m.getParameterCount() == numParams) {
                    options.add(m);
                }
            }
            if (options.isEmpty()) {
                ScriptRuntime.log.warn(
                        SafeResourceLoader.getString(
                                "UNKNOWN_METHOD", ScriptManager.getResourceBundle()),
                        methodName);
                this.halt();
                return;
            }
        } else {
            options = ScriptManager.getMethods(methodName, numParams);
        }

        if (!this.call(options, parameters, object)) {
            ScriptRuntime.log.warn(
                    SafeResourceLoader.getString(
                            "UNKNOWN_METHOD", ScriptManager.getResourceBundle()),
                    methodName);
            this.halt();
        }
    }

    /**
     * Actually try to execute the call.
     *
     * @param options The potential options we have for method calls.
     * @param parameters The actual parameters we are trying to match.
     * @param target The object to invoke the method on, may be null for static methods.
     * @return Whether we successfully called a method.
     */
    private boolean call(
            @NonNull List<Method> options, @NonNull List<MemoryItem> parameters, Object target) {
        if (options.isEmpty()) {
            return false;
        }
        for (Method option : options) {
            Class<?>[] params = option.getParameterTypes();
            boolean viable = true;
            for (int i = 0; i < params.length; ++i) {
                Class<?> expectedType = params[i];
                Class<?> actualType = parameters.get(i).type();
                viable = this.canAssign(expectedType, actualType);
                if (!viable) {
                    break;
                }
            }
            if (!viable) {
                continue;
            }
            Object[] actualParams = new Object[parameters.size()];
            for (int i = 0; i < parameters.size(); ++i) {
                actualParams[i] = parameters.get(i).value();
            }
            try {
                Object result = option.invoke(target, actualParams);
                if (result != null) {
                    this.stack.push(new MemoryItem(result));
                }
                return true;
            } catch (IllegalAccessException
                    | IllegalArgumentException
                    | InvocationTargetException e) {
                ScriptRuntime.log.warn(
                        SafeResourceLoader.getString(
                                "METHOD_CALL_FAILED", ScriptManager.getResourceBundle()),
                        option.getName());
                return false;
            }
        }
        return false;
    }

    /**
     * Check if the expected parameter lines up with what we have for it. This includes checks for
     * primitives.
     *
     * @param expected The expected type.
     * @param actual The actual type we have.
     * @return Whether this is a reasonable match.
     */
    private boolean canAssign(Class<?> expected, Class<?> actual) {
        if (expected.isPrimitive()) {
            return ((expected == int.class && actual == Integer.class)
                    || (expected == double.class && actual == Double.class)
                    || (expected == boolean.class && actual == Boolean.class)
                    || (expected == char.class && actual == Character.class));
        } else if (!expected.isAssignableFrom(actual)) {
            return false;
        }
        return true;
    }

    /**
     * Handle a cast instruction.
     *
     * @param i The instruction to process.
     */
    private void cast(Instruction i) {
        final MemLocation firstLocation = i.firstLocation();

        final MemoryItem firstItem = this.loadValue(firstLocation);

        if (this.fatalError) {
            return;
        }

        final Class<?> targetClass = i.targetLocation().type();

        MemoryItem target = null;

        if (targetClass == Integer.class) {
            target = this.castToInt(firstItem.value());
        } else if (targetClass == Double.class) {
            target = this.castToDouble(firstItem.value());
        } else if (targetClass == Character.class) {
            target = this.castToChar(firstItem.value());
        } else if (targetClass == Boolean.class) {
            target = this.castToBoolean(firstItem.value());
        } else if (targetClass == String.class) {
            target = new MemoryItem(Character.class, firstItem.value().toString());
        }

        if (target == null) {
            ScriptRuntime.log.warn(
                    SafeResourceLoader.getString(
                            "INVALID_CAST_TYPE", ScriptManager.getResourceBundle()),
                    targetClass);
            this.halt();
            return;
        }

        MemLocation targetLocation = new MemLocation(i.targetLocation().area(), targetClass);
        this.storeValue(target, targetLocation);
    }

    /**
     * Handle casting to a boolean.
     *
     * @param o The object we are converting.
     * @return The memory item after a cast, or null if we could not handle it.
     */
    private MemoryItem castToBoolean(Object o) {
        boolean value;

        if (o instanceof Integer integer) {
            value = integer == 0;
        } else if (o instanceof Double doub) {
            value = doub == 0;
        } else if (o instanceof Character character) {
            value = character == 0;
        } else if (o instanceof Boolean bool) {
            value = bool;
        } else if (o instanceof String str) {
            value = str.isEmpty();
        } else {
            return null;
        }
        return new MemoryItem(Boolean.class, value);
    }

    /**
     * Handle casting to a character.
     *
     * @param o The object we are converting.
     * @return The memory item after a cast, or null if we could not handle it.
     */
    private MemoryItem castToChar(Object o) {
        char value;

        if (o instanceof Integer integer) {
            int val = integer;
            value = (char) val;
        } else if (o instanceof Double doub) {
            double val = doub;
            value = (char) val;
        } else if (o instanceof Character character) {
            value = character;
        } else if (o instanceof Boolean bool) {
            value = Boolean.TRUE.equals(bool) ? (char) 1 : (char) 0;
        } else if (o instanceof String str) {
            try {
                value = (char) Integer.parseInt(str);
            } catch (NumberFormatException ignored) {
                value = 0;
            }
        } else {
            return null;
        }
        return new MemoryItem(Character.class, value);
    }

    /**
     * Handle casting to a double.
     *
     * @param o The object we are converting.
     * @return The memory item after a cast, or null if we could not handle it.
     */
    private MemoryItem castToDouble(Object o) {
        double value;

        if (o instanceof Integer integer) {
            value = integer;
        } else if (o instanceof Double doub) {
            value = doub;
        } else if (o instanceof Character character) {
            value = character;
        } else if (o instanceof Boolean bool) {
            value = Boolean.TRUE.equals(bool) ? 1.0 : 0.0;
        } else if (o instanceof String str) {
            try {
                value = Double.parseDouble(str);
            } catch (NumberFormatException ignored) {
                value = 0;
            }
        } else {
            return null;
        }
        return new MemoryItem(Double.class, value);
    }

    /**
     * Handle casting to a integer.
     *
     * @param o The object we are converting.
     * @return The memory item after a cast, or null if we could not handle it.
     */
    private MemoryItem castToInt(Object o) {
        int value;

        if (o instanceof Integer integer) {
            value = integer;
        } else if (o instanceof Double doub) {
            double val = doub;
            value = (int) val;
        } else if (o instanceof Character character) {
            value = character;
        } else if (o instanceof Boolean bool) {
            value = Boolean.TRUE.equals(bool) ? 1 : 0;
        } else if (o instanceof String str) {
            try {
                value = Integer.parseInt(str);
            } catch (NumberFormatException ignored) {
                value = 0;
            }
        } else {
            return null;
        }
        return new MemoryItem(Integer.class, value);
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
        } else {
            // Can't happen because of type checks, but just to be thorough
            firstNumber = 0;
        }

        if (secondLocation.isChar()) {
            final char unboxed = (Character) secondItem.value();
            secondNumber = unboxed;
        } else {
            // Can't happen because of type checks, but just to be thorough
            secondNumber = 0;
        }

        MemoryItem result =
                new MemoryItem(Character.class, operation.apply(firstNumber, secondNumber));

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
                            SafeResourceLoader.getString(
                                    MEMORY_MISMATCH, ScriptManager.getResourceBundle()),
                            intended.toString());
                    this.halt();
                }
                break;
            case CHAR:
                if (!memory.isChar()) {
                    ScriptRuntime.log.warn(
                            SafeResourceLoader.getString(
                                    MEMORY_MISMATCH, ScriptManager.getResourceBundle()),
                            intended.toString());
                    this.halt();
                }
                break;
            case DOUBLE:
                if (!(memory.isChar() || memory.isInt() || memory.isDouble())) {
                    ScriptRuntime.log.warn(
                            SafeResourceLoader.getString(
                                    MEMORY_MISMATCH, ScriptManager.getResourceBundle()),
                            intended.toString());
                    this.halt();
                }
                break;
            case INT:
                if (!(memory.isChar() || memory.isInt())) {
                    ScriptRuntime.log.warn(
                            SafeResourceLoader.getString(
                                    MEMORY_MISMATCH, ScriptManager.getResourceBundle()),
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
                        SafeResourceLoader.getString(
                                "INVALID_MEMORY_TYPE", ScriptManager.getResourceBundle()),
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

        if ((firstLocation.isChar() || firstLocation.isDouble() || firstLocation.isInt())
                && (secondLocation.isChar()
                        || secondLocation.isDouble()
                        || secondLocation.isInt())) {

            this.doubleComparison(
                    i,
                    firstItem,
                    secondItem,
                    (a, b) -> {
                        final double TOLERANCE = 0.00001;
                        if (Math.abs(a - b) < TOLERANCE) {
                            this.lastComparison = 0;
                        } else if (a < b) {
                            this.lastComparison = -1;
                        } else if (a > b) {
                            this.lastComparison = 1;
                        }
                    });
            return;
        }

        Object first = firstItem.value();
        Object second = secondItem.value();
        if (first.equals(second)) {
            this.lastComparison = 0;
        } else {
            // Different types
            this.lastComparison = 1;
        }
    }

    /**
     * Concatenate strings together. Automatically converts whatever is in the arguments to strings.
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
     * Compares the values of the given instruction as doubles using the provided function.
     *
     * @param i The instruction we are doing comparisons on.
     * @param firstItem The first memory item.
     * @param secondItem The second memory item.
     * @param comparisonFunction The function to use for comparisons.
     */
    private void doubleComparison(
            Instruction i,
            MemoryItem firstItem,
            MemoryItem secondItem,
            BiConsumer<Double, Double> comparisonFunction) {
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
        } else if (firstLocation.isChar()) {
            first = (char) firstItem.value();
        } else if (firstLocation.isDouble()) {
            first = (double) firstItem.value();
        } else {
            // Should not happen
            this.halt();
            return;
        }

        if (secondLocation.isInt()) {
            second = (int) secondItem.value();
        } else if (secondLocation.isChar()) {
            second = (char) secondItem.value();
        } else if (secondLocation.isDouble()) {
            second = (double) secondItem.value();
        } else {
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
        } else if (firstLocation.isInt()) {
            final int unboxed = (Integer) firstItem.value();
            firstNumber = unboxed;
        } else if (firstLocation.isChar()) {
            final char unboxed = (Character) firstItem.value();
            firstNumber = unboxed;
        } else {
            // Can't happen because of type checks, but just to be thorough
            firstNumber = 0;
        }

        if (secondLocation.isDouble()) {
            secondNumber = (Double) secondItem.value();
        } else if (secondLocation.isInt()) {
            final int unboxed = (Integer) secondItem.value();
            secondNumber = unboxed;
        } else if (secondLocation.isChar()) {
            final char unboxed = (Character) secondItem.value();
            secondNumber = unboxed;
        } else {
            // Can't happen because of type checks, but just to be thorough
            secondNumber = 0;
        }

        MemoryItem result =
                new MemoryItem(Double.class, operation.applyAsDouble(firstNumber, secondNumber));

        this.storeValue(result, i.targetLocation());
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
            case CAST:
                this.cast(i);
                this.programCounter++;
                break;
            case CALL:
                this.call(i);
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
                this.not(i);
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
                        SafeResourceLoader.getString(
                                "UNKNOWN_INSTRUCTION", ScriptManager.getResourceBundle()),
                        i.type().toString());
                this.halt();
                break;
        }
    }

    /** Stop running the program. Should only be called internally and by the script runner. */
    public void halt() {
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
        } else if (firstLocation.isChar()) {
            final char unboxed = (Character) firstItem.value();
            firstNumber = unboxed;
        } else {
            // Can't happen because of type checks, but just to be thorough
            firstNumber = 0;
        }

        if (secondLocation.isInt()) {
            secondNumber = (Integer) secondItem.value();
        } else if (secondLocation.isChar()) {
            final char unboxed = (Character) secondItem.value();
            secondNumber = unboxed;
        } else {
            // Can't happen because of type checks, but just to be thorough
            secondNumber = 0;
        }

        MemoryItem result =
                new MemoryItem(Integer.class, operation.applyAsInt(firstNumber, secondNumber));

        this.storeValue(result, i.targetLocation());
    }

    /**
     * A conditional jump. We jump to the given location if the given function returns true when
     * passed the last comparison value. If we don't jump, we just move to the next instruction.
     *
     * @param instruction The relevant jump instruction.
     * @param operator The function that determines if we should jump.
     */
    private void jump(Instruction instruction, IntPredicate operator) {
        final int location = (Integer) instruction.firstLocation().value();
        if (location < 0 || location > this.instructions.size()) {
            // instructions.size is for when we want to bail on the program.
            ScriptRuntime.log.warn(
                    SafeResourceLoader.getString(
                            "INVALID_JUMP_LOCATION", ScriptManager.getResourceBundle()),
                    location);
            this.halt();
            return;
        }
        if (operator.test(this.lastComparison)) {
            this.programCounter = location;
        } else {
            this.programCounter++;
        }
    }

    /**
     * Read the value from the memory location. You should check if the program halted after using
     * this.
     *
     * @param from The location we are reading from.
     * @return The appropriate value, will be void memory if the location is invalid.
     */
    private MemoryItem loadValue(MemLocation from) {
        switch (from.area()) {
            case IMMEDIATE:
                return new MemoryItem(from.type(), from.value());
            case STACK:
                if (this.stack.isEmpty()) {
                    ScriptRuntime.log.warn(
                            SafeResourceLoader.getString(
                                    "POPPING_TOO_FAR", ScriptManager.getResourceBundle()));
                    this.halt();
                    return ScriptRuntime.VOID_MEMORY;
                }
                return this.stack.pop();
            case VARIABLE:
                if (!this.symbolTable.containsKey(from.value())) {
                    ScriptRuntime.log.warn(
                            SafeResourceLoader.getString(
                                    "UNKNOWN_VARIABLE", ScriptManager.getResourceBundle()),
                            from.value());
                    this.halt();
                    return ScriptRuntime.VOID_MEMORY;
                }
                return this.symbolTable.get(from.value());
            default:
                ScriptRuntime.log.warn(
                        SafeResourceLoader.getString(
                                "UNKNOWN_MEMORY_AREA", ScriptManager.getResourceBundle()),
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
        } else {
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
        } else if (firstLocation.isInt()) {
            final int unboxed = (Integer) firstItem.value();
            firstNumber = unboxed;
        } else if (firstLocation.isChar()) {
            final char unboxed = (Character) firstItem.value();
            firstNumber = unboxed;
        } else {
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
        } else if (firstLocation.isChar()) {
            final char unboxed = (Character) firstItem.value();
            firstNumber = unboxed;
        } else {
            // Can't happen because of type checks, but just to be thorough
            firstNumber = 0;
        }

        MemoryItem result = new MemoryItem(Integer.class, -firstNumber);

        this.storeValue(result, i.targetLocation());
    }

    /**
     * A logical not.
     *
     * @param i The instruction we are executing.
     */
    private void not(Instruction i) {
        final MemLocation firstLocation = i.firstLocation();
        final MemoryItem firstItem = this.loadValue(firstLocation);

        if (this.fatalError) {
            return;
        }
        this.checkType(firstLocation, Type.Base.BOOLEAN);
        if (this.fatalError) {
            return;
        }

        boolean value = (Boolean) firstItem.value();

        MemoryItem result = new MemoryItem(Boolean.class, !value);

        this.storeValue(result, i.targetLocation());
    }

    /**
     * Check for reserved methods that require special handling.
     *
     * @param objectLocation The memory area that the object we might be calling methods on is
     *     located.
     * @param methodName The name of the method.
     * @param numParams The number of parameters.
     * @param parameters The actual list of parameters to be passed.
     * @return Whether we should stop executing methods. False if we didn't match a reserved method
     *     and should keep looking.
     */
    private boolean reservedMethods(
            final MemArea objectLocation,
            final String methodName,
            final int numParams,
            List<MemoryItem> parameters) {
        if (objectLocation == MemArea.IMMEDIATE && methodName.equals("yield") && numParams < 2) {
            // Reserved method name
            if (numParams == 1) {
                Object tag = parameters.get(0).value();
                if (!(tag instanceof String)) {
                    ScriptRuntime.log.warn(
                            SafeResourceLoader.getString(
                                    "YIELD_PARAMETER", ScriptManager.getResourceBundle()),
                            methodName);
                    this.halt();
                    return true;
                }
                ScriptManager.yieldScript(this, (String) tag);
            } else {
                ScriptManager.yieldScript(this);
            }
            return true;
        }
        return false;
    }

    /**
     * Perform a set operation.
     *
     * @param instruction The instruction to extract a target location from.
     * @param operation The operation that takes the last comparison and outputs a boolean result.
     */
    private void set(Instruction instruction, IntPredicate operation) {
        MemoryItem result = new MemoryItem(Boolean.class, operation.test(this.lastComparison));
        this.storeValue(result, instruction.targetLocation());
    }

    /** Execute one instruction. */
    public void step() {
        if (this.fatalError
                || (this.programCounter < 0)
                || (this.programCounter >= this.instructions.size())) {
            // Stop executing
            return;
        }
        this.execute(this.instructions.get(this.programCounter));
    }

    /**
     * Store a value in the specified memory location. May halt the program if something goes wrong.
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
                                item.getClass().getSimpleName(),
                                variable,
                                existingValue.getClass().getSimpleName());
                        this.halt();
                        break;
                    }
                }
                this.symbolTable.put(variable, item);
                break;
            case IMMEDIATE:
            default:
                ScriptRuntime.log.warn(
                        SafeResourceLoader.getString(
                                "INVALID_MEMORY_LOCATION", ScriptManager.getResourceBundle()),
                        location.area());
                this.halt();
                break;
        }
    }
}
