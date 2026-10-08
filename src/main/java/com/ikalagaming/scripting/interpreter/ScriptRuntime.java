package com.ikalagaming.scripting.interpreter;

import com.ikalagaming.scripting.ScriptDiagnostics;
import com.ikalagaming.scripting.ScriptManager;
import com.ikalagaming.scripting.ast.Type;
import com.ikalagaming.util.SafeResourceLoader;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
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
public class ScriptRuntime {

    /**
     * Handles a script yielding, instead of the script manager.
     *
     * @see ScriptRuntime#setYieldHandler(YieldHandler)
     */
    @FunctionalInterface
    public interface YieldHandler {
        /**
         * Called when a script yields.
         *
         * @param runtime The script that yielded.
         * @param tag The tag the script yielded with, or null if it did not use a tag.
         */
        void onYield(@NonNull ScriptRuntime runtime, String tag);
    }

    /**
     * Handles a script hitting a breakpoint, like an interrupt handler.
     *
     * @see ScriptRuntime#setBreakpointHandler(BreakpointHandler)
     */
    @FunctionalInterface
    public interface BreakpointHandler {
        /**
         * Called when a script hits a breakpoint. The script continues from the breakpoint the next
         * time it steps, so a handler that wants to pause the script needs to stop stepping it.
         *
         * @param runtime The script that hit a breakpoint.
         * @param instructionIndex The index of the instruction the breakpoint is on.
         */
        void onBreakpoint(@NonNull ScriptRuntime runtime, int instructionIndex);
    }

    /**
     * The tag scripts yield with when they hit a breakpoint without a breakpoint handler, so they
     * can be resumed with {@link ScriptManager#resume(String)}.
     */
    public static final String BREAKPOINT_TAG = "breakpoint";

    /** Used to give each runtime a unique ID. */
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    /** A unique ID for this runtime, so it can be identified while debugging. */
    private final int id = NEXT_ID.getAndIncrement();

    /**
     * A name for the script, like the file it came from, to make it easier to identify. May be
     * null.
     *
     * @param name The new name.
     * @return The name, which may be null.
     */
    @Setter private volatile String name;

    /**
     * Handles yields instead of the script manager, for running scripts outside of the script
     * manager like in a debugger. If null, yields go to the script manager.
     *
     * @param yieldHandler The new yield handler, or null to use the script manager.
     * @return The yield handler, which may be null.
     */
    @Setter private volatile YieldHandler yieldHandler;

    /**
     * Handles breakpoints. If null, scripts yield with {@link #BREAKPOINT_TAG} when they hit a
     * breakpoint.
     *
     * @param breakpointHandler The new breakpoint handler, or null to yield.
     * @return The breakpoint handler, which may be null.
     */
    @Setter private volatile BreakpointHandler breakpointHandler;

    /** The original instructions that breakpoints were patched over, by instruction index. */
    @Getter(AccessLevel.NONE)
    private final Map<Integer, Instruction> patchedInstructions = new HashMap<>();

    /**
     * Whether we stopped at a breakpoint, so the next step should run the original instruction
     * instead of hitting the breakpoint again.
     */
    @Getter(AccessLevel.NONE)
    private boolean resumingFromBreakpoint;

    /** Used instead of null memory. */
    private static final MemoryItem VOID_MEMORY = new MemoryItem(Void.class, "void");

    /** If we should stop running the program. */
    private boolean fatalError;

    /**
     * The actual program, a list of instructions. Breakpoints are patched into this list, see
     * {@link #getOriginalInstruction(int)} for the instructions without breakpoints.
     */
    private final List<Instruction> instructions;

    /**
     * Create a runtime for a program.
     *
     * @param instructions The program, which is copied so breakpoints can be patched in.
     */
    public ScriptRuntime(@NonNull List<Instruction> instructions) {
        this.instructions = new ArrayList<>(instructions);
    }

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

    /**
     * The value of {@link #lastComparison} when two values can't be ordered, like when comparing
     * against NaN or values that are not equal and not numbers. Only != is true for these, which
     * matches how Java handles NaN.
     */
    private static final int UNORDERED = 2;

    /** The types that variables were declared with, so we can check what is stored in them. */
    private Map<String, Class<?>> declaredTypes = new HashMap<>();

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

        final MemoryItem firstItem = loadValue(firstLocation);
        final MemoryItem secondItem = loadValue(secondLocation);

        if (fatalError) {
            return;
        }
        checkType(firstLocation, Type.Base.BOOLEAN);
        checkType(secondLocation, Type.Base.BOOLEAN);
        if (fatalError) {
            return;
        }

        boolean first;
        boolean second;

        try {
            first = (Boolean) firstItem.value();
            second = (Boolean) secondItem.value();
        } catch (ClassCastException e) {
            ScriptDiagnostics.warnAt(
                    log,
                    getCurrentLine(),
                    -1,
                    SafeResourceLoader.getString("CAST_FAILED", ScriptManager.getResourceBundle()),
                    firstItem.getClass(),
                    Boolean.class);
            halt();
            return;
        }

        MemoryItem result = new MemoryItem(Boolean.class, operation.apply(first, second));

        storeValue(result, i.targetLocation());
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
            parameters.add(loadValue(stackLocation));
            if (fatalError) {
                return;
            }
        }

        if (reservedMethods(objectLocation, methodName, numParams, parameters)) {
            return;
        }

        if (objectLocation != MemArea.IMMEDIATE) {
            final MemoryItem first = loadValue(i.firstLocation());
            if (fatalError) {
                return;
            }

            object = first.value();
            if (object == null) {
                ScriptDiagnostics.warnAt(
                        log,
                        getCurrentLine(),
                        -1,
                        SafeResourceLoader.getString(
                                "METHOD_CALL_ON_NULL", ScriptManager.getResourceBundle()),
                        methodName);
                halt();
                return;
            }

            Method[] methods = object.getClass().getMethods();

            options = new ArrayList<>();

            for (Method m : methods) {
                if (m.getName().equals(methodName) && m.getParameterCount() == numParams) {
                    options.add(m);
                }
            }
            if (options.isEmpty()) {
                ScriptDiagnostics.warnAt(
                        log,
                        getCurrentLine(),
                        -1,
                        SafeResourceLoader.getString(
                                "UNKNOWN_METHOD", ScriptManager.getResourceBundle()),
                        methodName);
                halt();
                return;
            }
        } else {
            options = ScriptManager.getMethods(methodName, numParams);
        }

        // There is no target location if the result is not used
        final boolean keepResult = i.targetLocation() != null;

        if (!this.call(options, parameters, object, keepResult)) {
            ScriptDiagnostics.warnAt(
                    log,
                    getCurrentLine(),
                    -1,
                    SafeResourceLoader.getString(
                            "UNKNOWN_METHOD", ScriptManager.getResourceBundle()),
                    methodName);
            halt();
        }
    }

    /**
     * Actually try to execute the call.
     *
     * @param options The potential options we have for method calls.
     * @param parameters The actual parameters we are trying to match.
     * @param target The object to invoke the method on, may be null for static methods.
     * @param keepResult Whether the return value should be pushed onto the stack. Ignored for void
     *     methods, which never push anything.
     * @return Whether we successfully called a method.
     */
    private boolean call(
            @NonNull List<Method> options,
            @NonNull List<MemoryItem> parameters,
            Object target,
            boolean keepResult) {
        if (options.isEmpty()) {
            return false;
        }
        List<Method> viableOptions = new ArrayList<>();
        for (Method option : options) {
            if (option.isBridge() || option.isSynthetic()) {
                // Compiler generated duplicates of real methods
                continue;
            }
            Class<?>[] params = option.getParameterTypes();
            boolean viable = true;
            for (int i = 0; i < params.length; ++i) {
                viable = canAssign(params[i], parameters.get(i));
                if (!viable) {
                    break;
                }
            }
            if (viable) {
                viableOptions.add(option);
            }
        }

        final Method mostSpecific = ScriptRuntime.mostSpecific(viableOptions);
        if (mostSpecific == null) {
            if (viableOptions.size() > 1) {
                ScriptDiagnostics.warnAt(
                        log,
                        getCurrentLine(),
                        -1,
                        SafeResourceLoader.getString(
                                "AMBIGUOUS_METHOD", ScriptManager.getResourceBundle()),
                        viableOptions.get(0).getName(),
                        viableOptions.toString());
            }
            return false;
        }

        final Method option = mostSpecific;
        Object[] actualParams = new Object[parameters.size()];
        for (int i = 0; i < parameters.size(); ++i) {
            actualParams[i] = parameters.get(i).value();
        }
        try {
            Object result = option.invoke(target, actualParams);
            final Class<?> returnType = option.getReturnType();
            if (keepResult && returnType != void.class) {
                /*
                 * Null results still need to take up space on the stack, otherwise whatever
                 * uses the result will pop something else.
                 */
                if (result == null) {
                    stack.push(new MemoryItem(returnType, null));
                } else {
                    stack.push(new MemoryItem(result));
                }
            }
            return true;
        } catch (IllegalAccessException
                | IllegalArgumentException
                | NullPointerException
                | InvocationTargetException e) {
            ScriptDiagnostics.warnAt(
                    log,
                    getCurrentLine(),
                    -1,
                    SafeResourceLoader.getString(
                            "METHOD_CALL_FAILED", ScriptManager.getResourceBundle()),
                    option.getName());
            return false;
        }
    }

    /**
     * Pick the most specific method out of the options, the same way Java chooses between
     * overloads. A method is more specific than another if each of its parameters could be passed
     * to the other method.
     *
     * @param options The methods we could call.
     * @return The most specific method, or null if there are no options or it's ambiguous.
     */
    private static Method mostSpecific(@NonNull List<Method> options) {
        for (Method candidate : options) {
            boolean best = true;
            for (Method other : options) {
                if (other != candidate && !ScriptRuntime.isAtLeastAsSpecific(candidate, other)) {
                    best = false;
                    break;
                }
            }
            if (best) {
                // Identical signatures are only possible across classes, which is ambiguous
                for (Method other : options) {
                    if (other != candidate
                            && ScriptRuntime.isAtLeastAsSpecific(other, candidate)
                            && !other.getDeclaringClass()
                                    .isAssignableFrom(candidate.getDeclaringClass())) {
                        return null;
                    }
                }
                return candidate;
            }
        }
        return null;
    }

    /**
     * Convert a primitive class to its boxed equivalent.
     *
     * @param primitive The primitive class.
     * @return The boxed class, or the original class if it is not one we handle.
     */
    private static Class<?> box(Class<?> primitive) {
        if (primitive == int.class) {
            return Integer.class;
        }
        if (primitive == double.class) {
            return Double.class;
        }
        if (primitive == char.class) {
            return Character.class;
        }
        if (primitive == boolean.class) {
            return Boolean.class;
        }
        return primitive;
    }

    /**
     * Check if every parameter of the first method could be passed to the second method.
     *
     * @param first The method we think might be more specific.
     * @param second The method to compare against.
     * @return True if the first is at least as specific as the second.
     */
    private static boolean isAtLeastAsSpecific(Method first, Method second) {
        Class<?>[] firstParams = first.getParameterTypes();
        Class<?>[] secondParams = second.getParameterTypes();
        for (int i = 0; i < firstParams.length; ++i) {
            final Class<?> from = firstParams[i];
            final Class<?> to = secondParams[i];
            final boolean widens =
                    (to == double.class && (from == int.class || from == char.class))
                            || (to == int.class && from == char.class);
            // A primitive can be boxed to pass it to a reference, like int to Object
            final boolean boxes =
                    from.isPrimitive() && to.isAssignableFrom(ScriptRuntime.box(from));
            if (!to.isAssignableFrom(from) && !widens && !boxes) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the expected parameter lines up with what we have for it. This includes checks for
     * primitives.
     *
     * @param expected The expected type.
     * @param actual The actual parameter we have.
     * @return Whether this is a reasonable match.
     */
    private boolean canAssign(Class<?> expected, MemoryItem actual) {
        final Object value = actual.value();
        if (value == null) {
            // Null can be passed to anything but primitives
            return !expected.isPrimitive();
        }
        // The value is more accurate than the type stored in memory
        final Class<?> actualType = value.getClass();
        if (expected.isPrimitive()) {
            // Includes widening primitive conversions, which reflection handles for us
            return ((expected == int.class
                            && (actualType == Integer.class || actualType == Character.class))
                    || (expected == double.class
                            && (actualType == Double.class
                                    || actualType == Integer.class
                                    || actualType == Character.class))
                    || (expected == boolean.class && actualType == Boolean.class)
                    || (expected == char.class && actualType == Character.class));
        }
        return expected.isAssignableFrom(actualType);
    }

    /**
     * Handle a cast instruction.
     *
     * @param i The instruction to process.
     */
    private void cast(Instruction i) {
        final MemLocation firstLocation = i.firstLocation();

        final MemoryItem firstItem = loadValue(firstLocation);

        if (fatalError) {
            return;
        }

        final Class<?> targetClass = i.targetLocation().type();

        MemoryItem target = null;

        if (targetClass == Integer.class) {
            target = castToInt(firstItem.value());
        } else if (targetClass == Double.class) {
            target = castToDouble(firstItem.value());
        } else if (targetClass == Character.class) {
            target = castToChar(firstItem.value());
        } else if (targetClass == Boolean.class) {
            target = castToBoolean(firstItem.value());
        } else if (targetClass == String.class) {
            target = new MemoryItem(String.class, String.valueOf(firstItem.value()));
        } else {
            /*
             * Reference types, and types we won't know until runtime. We have no way of checking
             * or converting these, so pass them through and let method calls sort it out.
             */
            target = firstItem;
        }

        if (target == null) {
            ScriptDiagnostics.warnAt(
                    log,
                    getCurrentLine(),
                    -1,
                    SafeResourceLoader.getString(
                            "INVALID_CAST_TYPE", ScriptManager.getResourceBundle()),
                    targetClass);
            halt();
            return;
        }

        storeValue(target, i.targetLocation());
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
            value = integer != 0;
        } else if (o instanceof Double doub) {
            value = doub != 0;
        } else if (o instanceof Character character) {
            value = character != 0;
        } else if (o instanceof Boolean bool) {
            value = bool;
        } else if (o instanceof String str) {
            value = !str.isEmpty();
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

        final MemoryItem firstItem = loadValue(firstLocation);
        final MemoryItem secondItem = loadValue(secondLocation);

        if (fatalError) {
            return;
        }
        checkType(firstLocation, Type.Base.CHAR);
        checkType(secondLocation, Type.Base.CHAR);
        if (fatalError) {
            return;
        }

        if (!ScriptRuntime.isIntegral(firstItem.value())
                || !ScriptRuntime.isIntegral(secondItem.value())) {
            valueTypeMismatch(Type.Base.CHAR);
            return;
        }

        /*
         * The values are checked rather than the locations, since the location type is what we
         * expected at compile time, and immediate values like increments may be boxed integers.
         */
        final char firstNumber = (char) ScriptRuntime.toInt(firstItem.value());
        final char secondNumber = (char) ScriptRuntime.toInt(secondItem.value());

        MemoryItem result;
        try {
            result = new MemoryItem(Character.class, operation.apply(firstNumber, secondNumber));
        } catch (ArithmeticException e) {
            ScriptDiagnostics.warnAt(
                    log,
                    getCurrentLine(),
                    -1,
                    SafeResourceLoader.getString(
                            "ARITHMETIC_ERROR", ScriptManager.getResourceBundle()),
                    e.getMessage());
            halt();
            return;
        }

        storeValue(result, i.targetLocation());
    }

    /**
     * Check if a value is a whole number we can do integer math on.
     *
     * @param value The value to check.
     * @return True if the value is an integer or character.
     */
    private static boolean isIntegral(Object value) {
        return value instanceof Integer || value instanceof Character;
    }

    /**
     * Check if a value is any kind of number we can do math on.
     *
     * @param value The value to check.
     * @return True if the value is an integer, character, or double.
     */
    private static boolean isNumeric(Object value) {
        return ScriptRuntime.isIntegral(value) || value instanceof Double;
    }

    /**
     * Convert a numeric value to a double. The value must be numeric.
     *
     * @param value The value to convert.
     * @return The value as a double.
     * @see #isNumeric(Object)
     */
    private static double toDouble(Object value) {
        if (value instanceof Double doub) {
            return doub;
        }
        return ScriptRuntime.toInt(value);
    }

    /**
     * Convert an integral value to an integer. The value must be integral.
     *
     * @param value The value to convert.
     * @return The value as an integer.
     * @see #isIntegral(Object)
     */
    private static int toInt(Object value) {
        if (value instanceof Character character) {
            return character;
        }
        return (Integer) value;
    }

    /**
     * Log that the actual value in memory did not match what we expected, and halt.
     *
     * @param intended The type we were expecting.
     */
    private void valueTypeMismatch(Type.Base intended) {
        ScriptDiagnostics.warnAt(
                log,
                getCurrentLine(),
                -1,
                SafeResourceLoader.getString(
                        "MEMORY_TYPE_MISMATCH", ScriptManager.getResourceBundle()),
                intended.toString());
        halt();
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
                    ScriptDiagnostics.warnAt(
                            log,
                            getCurrentLine(),
                            -1,
                            SafeResourceLoader.getString(
                                    MEMORY_MISMATCH, ScriptManager.getResourceBundle()),
                            intended.toString());
                    halt();
                }
                break;
            case CHAR:
                if (!memory.isChar()) {
                    ScriptDiagnostics.warnAt(
                            log,
                            getCurrentLine(),
                            -1,
                            SafeResourceLoader.getString(
                                    MEMORY_MISMATCH, ScriptManager.getResourceBundle()),
                            intended.toString());
                    halt();
                }
                break;
            case DOUBLE:
                if (!(memory.isChar() || memory.isInt() || memory.isDouble())) {
                    ScriptDiagnostics.warnAt(
                            log,
                            getCurrentLine(),
                            -1,
                            SafeResourceLoader.getString(
                                    MEMORY_MISMATCH, ScriptManager.getResourceBundle()),
                            intended.toString());
                    halt();
                }
                break;
            case INT:
                if (!(memory.isChar() || memory.isInt())) {
                    ScriptDiagnostics.warnAt(
                            log,
                            getCurrentLine(),
                            -1,
                            SafeResourceLoader.getString(
                                    MEMORY_MISMATCH, ScriptManager.getResourceBundle()),
                            intended.toString());
                    halt();
                }
                break;
            case STRING:
                // We can cast pretty much anything to string, so ignore this
                break;
            case LABEL, IDENTIFIER, UNKNOWN, VOID:
            default:
                ScriptDiagnostics.warnAt(
                        log,
                        getCurrentLine(),
                        -1,
                        SafeResourceLoader.getString(
                                "INVALID_MEMORY_TYPE", ScriptManager.getResourceBundle()),
                        intended.toString());
                halt();
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

        final MemoryItem firstItem = loadValue(firstLocation);
        final MemoryItem secondItem = loadValue(secondLocation);

        if (fatalError) {
            return;
        }

        Object first = firstItem.value();
        Object second = secondItem.value();

        /*
         * We check the actual values instead of the locations, since things like method calls
         * have types we don't know until runtime.
         */
        if (ScriptRuntime.isNumeric(first) && ScriptRuntime.isNumeric(second)) {
            final double a = ScriptRuntime.toDouble(first);
            final double b = ScriptRuntime.toDouble(second);
            // Exact comparisons, the same as Java
            if (a < b) {
                lastComparison = -1;
            } else if (a > b) {
                lastComparison = 1;
            } else if (a == b) {
                lastComparison = 0;
            } else {
                // NaN
                lastComparison = ScriptRuntime.UNORDERED;
            }
            return;
        }

        if (Objects.equals(first, second)) {
            lastComparison = 0;
        } else {
            // Different values that we can't put in order
            lastComparison = ScriptRuntime.UNORDERED;
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

        final MemoryItem firstItem = loadValue(firstLocation);
        final MemoryItem secondItem = loadValue(secondLocation);

        if (fatalError) {
            return;
        }

        String first = String.valueOf(firstItem.value());
        String second = String.valueOf(secondItem.value());

        MemoryItem result = new MemoryItem(String.class, first + second);

        storeValue(result, i.targetLocation());
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

        final MemoryItem firstItem = loadValue(firstLocation);
        final MemoryItem secondItem = loadValue(secondLocation);

        if (fatalError) {
            return;
        }
        checkType(firstLocation, Type.Base.DOUBLE);
        checkType(secondLocation, Type.Base.DOUBLE);
        if (fatalError) {
            return;
        }

        if (!ScriptRuntime.isNumeric(firstItem.value())
                || !ScriptRuntime.isNumeric(secondItem.value())) {
            valueTypeMismatch(Type.Base.DOUBLE);
            return;
        }

        final double firstNumber = ScriptRuntime.toDouble(firstItem.value());
        final double secondNumber = ScriptRuntime.toDouble(secondItem.value());

        MemoryItem result =
                new MemoryItem(Double.class, operation.applyAsDouble(firstNumber, secondNumber));

        storeValue(result, i.targetLocation());
    }

    /**
     * Declare a variable, which records its type and stores the initial value. Declaring a variable
     * that already exists replaces it, since it's a new variable that happens to reuse a name, like
     * a loop variable or a variable in a different block.
     *
     * @param i The instruction to execute.
     */
    private void declare(@NonNull Instruction i) {
        MemoryItem memory = loadValue(i.firstLocation());
        if (fatalError) {
            return;
        }
        final String variable = (String) i.targetLocation().value();
        declaredTypes.put(variable, i.targetLocation().type());
        storeValue(memory, i.targetLocation());
    }

    /**
     * Add two values whose types we did not know at compile time.
     *
     * @param i The instruction to execute.
     * @see InstructionType#ADD_DYNAMIC
     */
    private void dynamicAdd(Instruction i) {
        final MemoryItem firstItem = loadValue(i.firstLocation());
        final MemoryItem secondItem = loadValue(i.secondLocation());
        if (fatalError) {
            return;
        }
        final Object first = firstItem.value();
        final Object second = secondItem.value();

        if (first instanceof String || second instanceof String) {
            storeValue(
                    new MemoryItem(String.class, String.valueOf(first) + String.valueOf(second)),
                    i.targetLocation());
            return;
        }
        if (!ScriptRuntime.isNumeric(first) || !ScriptRuntime.isNumeric(second)) {
            valueTypeMismatch(Type.Base.UNKNOWN);
            return;
        }
        if (first instanceof Double || second instanceof Double) {
            storeValue(
                    new MemoryItem(
                            Double.class,
                            ScriptRuntime.toDouble(first) + ScriptRuntime.toDouble(second)),
                    i.targetLocation());
            return;
        }
        // Like Java, adding characters results in an integer
        storeValue(
                new MemoryItem(
                        Integer.class, ScriptRuntime.toInt(first) + ScriptRuntime.toInt(second)),
                i.targetLocation());
    }

    /**
     * Apply widening primitive conversions, like storing an integer in a double variable, the same
     * as Java does for assignments.
     *
     * @param declaredType The type of the variable.
     * @param item The value we want to store.
     * @return The converted value, or the original if no conversion applies.
     */
    private static MemoryItem widen(@NonNull Class<?> declaredType, @NonNull MemoryItem item) {
        final Object value = item.value();
        if (declaredType == Double.class
                && (value instanceof Integer || value instanceof Character)) {
            return new MemoryItem(Double.class, ScriptRuntime.toDouble(value));
        }
        if (declaredType == Integer.class && value instanceof Character character) {
            return new MemoryItem(Integer.class, (int) character);
        }
        return item;
    }

    /**
     * Check if a value can be stored in a variable of the given type.
     *
     * @param declaredType The type the variable was declared with.
     * @param value The value we want to store.
     * @return True if the value is allowed.
     */
    private static boolean fitsType(@NonNull Class<?> declaredType, Object value) {
        if (value == null) {
            // Only references can be null, and Object covers identifiers and arrays
            return declaredType == Object.class || declaredType == String.class;
        }
        return declaredType.isInstance(value);
    }

    private void execute(Instruction i) {
        switch (i.type()) {
            case ADD_CHAR:
                charMath(i, (a, b) -> (char) (a + b));
                programCounter++;
                break;
            case ADD_DYNAMIC:
                dynamicAdd(i);
                programCounter++;
                break;
            case ADD_DOUBLE:
                doubleMath(i, (a, b) -> a + b);
                programCounter++;
                break;
            case ADD_INT:
                intMath(i, (a, b) -> a + b, (a, b) -> a + b);
                programCounter++;
                break;
            case AND:
                boolLogic(i, (a, b) -> a && b);
                programCounter++;
                break;
            case CAST:
                cast(i);
                programCounter++;
                break;
            case CALL:
                this.call(i);
                programCounter++;
                break;
            case CMP:
                compare(i);
                programCounter++;
                break;
            case CONCAT_STRING:
                concatStrings(i);
                programCounter++;
                break;
            case DECLARE:
                declare(i);
                programCounter++;
                break;
            case DIV_CHAR:
                charMath(i, (a, b) -> (char) (a / b));
                programCounter++;
                break;
            case DIV_DOUBLE:
                doubleMath(i, (a, b) -> a / b);
                programCounter++;
                break;
            case DIV_INT:
                intMath(i, (a, b) -> a / b, (a, b) -> a / b);
                programCounter++;
                break;
            case HALT:
                // A normal exit, not an error
                programCounter = instructions.size();
                break;
            case JEQ:
                jump(i, comp -> comp == 0);
                break;
            case JGE:
                jump(i, comp -> comp == 0 || comp == 1);
                break;
            case JGT:
                jump(i, comp -> comp == 1);
                break;
            case JLE:
                jump(i, comp -> comp == 0 || comp == -1);
                break;
            case JLT:
                jump(i, comp -> comp == -1);
                break;
            case JMP:
                jump(i, comp -> true);
                break;
            case JNE:
                jump(i, comp -> comp != 0);
                break;
            case MOD_CHAR:
                charMath(i, (a, b) -> (char) (a % b));
                programCounter++;
                break;
            case MOD_DOUBLE:
                doubleMath(i, (a, b) -> a % b);
                programCounter++;
                break;
            case MOD_INT:
                intMath(i, (a, b) -> a % b, (a, b) -> a % b);
                programCounter++;
                break;
            case MOV:
                move(i);
                programCounter++;
                break;
            case MUL_CHAR:
                charMath(i, (a, b) -> (char) (a * b));
                programCounter++;
                break;
            case MUL_DOUBLE:
                doubleMath(i, (a, b) -> a * b);
                programCounter++;
                break;
            case MUL_INT:
                intMath(i, (a, b) -> a * b, (a, b) -> a * b);
                programCounter++;
                break;
            case NEG_CHAR:
                negateChar(i);
                programCounter++;
                break;
            case NEG_DOUBLE:
                negateDouble(i);
                programCounter++;
                break;
            case NEG_INT:
                negateInt(i);
                programCounter++;
                break;
            case NOP:
                // No operation
                programCounter++;
                break;
            case NOT:
                not(i);
                programCounter++;
                break;
            case OR:
                boolLogic(i, (a, b) -> a || b);
                programCounter++;
                break;
            case SUB_CHAR:
                charMath(i, (a, b) -> (char) (a - b));
                programCounter++;
                break;
            case SUB_DOUBLE:
                doubleMath(i, (a, b) -> a - b);
                programCounter++;
                break;
            case SUB_INT:
                intMath(i, (a, b) -> a - b, (a, b) -> a - b);
                programCounter++;
                break;
            case SET_EQ:
                set(i, cmp -> cmp == 0);
                programCounter++;
                break;
            case SET_GE:
                set(i, cmp -> cmp == 0 || cmp == 1);
                programCounter++;
                break;
            case SET_GT:
                set(i, cmp -> cmp == 1);
                programCounter++;
                break;
            case SET_LE:
                set(i, cmp -> cmp == 0 || cmp == -1);
                programCounter++;
                break;
            case SET_LT:
                set(i, cmp -> cmp == -1);
                programCounter++;
                break;
            case SET_NE:
                set(i, cmp -> cmp != 0);
                programCounter++;
                break;
            case BREAKPOINT:
                // A breakpoint that is part of the program, rather than patched in
                programCounter++;
                hitBreakpoint(programCounter - 1);
                break;
            default:
                ScriptDiagnostics.warnAt(
                        log,
                        getCurrentLine(),
                        -1,
                        SafeResourceLoader.getString(
                                "UNKNOWN_INSTRUCTION", ScriptManager.getResourceBundle()),
                        i.type().toString());
                halt();
                break;
        }
    }

    /**
     * A name to show for the script, using the name if there is one.
     *
     * @return The name to display.
     */
    public String getDisplayName() {
        final String currentName = name;
        return currentName == null ? "Script #" + id : currentName + " (#" + id + ")";
    }

    /**
     * Patch a breakpoint in over an instruction. When the script reaches it, the breakpoint handler
     * is called, and the original instruction runs when the script continues.
     *
     * @param index The index of the instruction.
     * @return True if there is now a breakpoint there, false if the index is out of range.
     */
    public synchronized boolean setBreakpoint(int index) {
        if (index < 0 || index >= instructions.size()) {
            return false;
        }
        if (patchedInstructions.containsKey(index)) {
            return true;
        }
        final Instruction original = instructions.get(index);
        patchedInstructions.put(index, original);
        instructions.set(
                index,
                new Instruction(InstructionType.BREAKPOINT, null, null, null, original.line()));
        return true;
    }

    /**
     * Patch the original instruction back in, removing a breakpoint.
     *
     * @param index The index of the instruction.
     * @return True if there was a breakpoint to remove.
     */
    public synchronized boolean clearBreakpoint(int index) {
        final Instruction original = patchedInstructions.remove(index);
        if (original == null) {
            return false;
        }
        instructions.set(index, original);
        return true;
    }

    /** Remove all the breakpoints. */
    public synchronized void clearBreakpoints() {
        for (var entry : patchedInstructions.entrySet()) {
            instructions.set(entry.getKey(), entry.getValue());
        }
        patchedInstructions.clear();
    }

    /**
     * Check if there is a breakpoint on an instruction.
     *
     * @param index The index of the instruction.
     * @return True if a breakpoint is patched in there.
     */
    public synchronized boolean hasBreakpoint(int index) {
        return patchedInstructions.containsKey(index);
    }

    /**
     * Fetch the instructions that have breakpoints.
     *
     * @return The sorted indices of instructions with breakpoints.
     */
    public synchronized Set<Integer> getBreakpoints() {
        return new TreeSet<>(patchedInstructions.keySet());
    }

    /**
     * Fetch an instruction as it was compiled, ignoring any breakpoint patched over it.
     *
     * @param index The index of the instruction.
     * @return The original instruction.
     */
    public synchronized Instruction getOriginalInstruction(int index) {
        final Instruction original = patchedInstructions.get(index);
        return original != null ? original : instructions.get(index);
    }

    /**
     * Find the first instruction generated from a line of source, which is where a breakpoint on
     * that line goes.
     *
     * @param line The line in the source, starting at 1.
     * @return The index of the first instruction for the line, or empty if no instructions came
     *     from that line.
     */
    public synchronized OptionalInt getFirstInstructionOnLine(int line) {
        for (int i = 0; i < instructions.size(); ++i) {
            if (getOriginalInstruction(i).line() == line) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    /**
     * Find where execution enters a line of source, which is where breakpoints on the line go. A
     * line can generate more than one group of instructions, like a for loop which has the
     * initializer before the loop and the update at the end of each iteration, so this is the first
     * instruction of each group of consecutive instructions from that line.
     *
     * @param line The line in the source, starting at 1.
     * @return The indices of the first instruction in each group, in order, which is empty if no
     *     instructions came from that line.
     */
    public synchronized List<Integer> getLineStartInstructions(int line) {
        final List<Integer> starts = new ArrayList<>();
        boolean previousOnLine = false;
        for (int i = 0; i < instructions.size(); ++i) {
            final boolean onLine = getOriginalInstruction(i).line() == line;
            if (onLine && !previousOnLine) {
                starts.add(i);
            }
            previousOnLine = onLine;
        }
        return starts;
    }

    /**
     * Fetch the line of source the current instruction came from.
     *
     * @return The line, starting at 1, or -1 if unknown or the script has finished.
     */
    public synchronized int getCurrentLine() {
        if (programCounter < 0 || programCounter >= instructions.size()) {
            return -1;
        }
        return getOriginalInstruction(programCounter).line();
    }

    /**
     * Call the breakpoint handler, or yield if there is none.
     *
     * @param index The index of the instruction with the breakpoint.
     */
    private void hitBreakpoint(int index) {
        final BreakpointHandler handler = breakpointHandler;
        if (handler != null) {
            handler.onBreakpoint(this, index);
            return;
        }
        final YieldHandler yielder = yieldHandler;
        if (yielder != null) {
            yielder.onYield(this, BREAKPOINT_TAG);
        } else {
            ScriptManager.yieldScript(this, BREAKPOINT_TAG);
        }
    }

    /** Stop running the program. Should only be called internally and by the script runner. */
    public synchronized void halt() {
        fatalError = true;
        programCounter = instructions.size();
    }

    /**
     * Check if the program has terminated.
     *
     * @return Whether we have terminated the program.
     */
    public boolean hasTerminated() {
        /*
         * Instructions increment the program counter after executing, so a halt in the middle of
         * an instruction can push it past the end of the program.
         */
        return fatalError || programCounter >= instructions.size();
    }

    /**
     * Deal with any kind of math operation on two integers.
     *
     * @param i The instruction.
     * @param operation The operation to perform on the two numbers.
     */
    private void intMath(
            Instruction i, IntBinaryOperator operation, DoubleBinaryOperator doubleOperation) {

        final MemLocation firstLocation = i.firstLocation();
        final MemLocation secondLocation = i.secondLocation();

        final MemoryItem firstItem = loadValue(firstLocation);
        final MemoryItem secondItem = loadValue(secondLocation);

        if (fatalError) {
            return;
        }
        checkType(firstLocation, Type.Base.INT);
        checkType(secondLocation, Type.Base.INT);
        if (fatalError) {
            return;
        }

        final Object firstValue = firstItem.value();
        final Object secondValue = secondItem.value();

        if (!ScriptRuntime.isNumeric(firstValue) || !ScriptRuntime.isNumeric(secondValue)) {
            valueTypeMismatch(Type.Base.INT);
            return;
        }

        if (firstValue instanceof Double || secondValue instanceof Double) {
            /*
             * Integer math is assumed for expressions involving types we don't know until
             * runtime, like method calls. If they turn out to be doubles, promote the operation.
             */
            MemoryItem result =
                    new MemoryItem(
                            Double.class,
                            doubleOperation.applyAsDouble(
                                    ScriptRuntime.toDouble(firstValue),
                                    ScriptRuntime.toDouble(secondValue)));
            storeValue(result, i.targetLocation());
            return;
        }

        MemoryItem result;
        try {
            result =
                    new MemoryItem(
                            Integer.class,
                            operation.applyAsInt(
                                    ScriptRuntime.toInt(firstValue),
                                    ScriptRuntime.toInt(secondValue)));
        } catch (ArithmeticException e) {
            ScriptDiagnostics.warnAt(
                    log,
                    getCurrentLine(),
                    -1,
                    SafeResourceLoader.getString(
                            "ARITHMETIC_ERROR", ScriptManager.getResourceBundle()),
                    e.getMessage());
            halt();
            return;
        }

        storeValue(result, i.targetLocation());
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
        if (location < 0 || location > instructions.size()) {
            // instructions.size is for when we want to bail on the program.
            ScriptDiagnostics.warnAt(
                    log,
                    getCurrentLine(),
                    -1,
                    SafeResourceLoader.getString(
                            "INVALID_JUMP_LOCATION", ScriptManager.getResourceBundle()),
                    location);
            halt();
            return;
        }
        if (operator.test(lastComparison)) {
            programCounter = location;
        } else {
            programCounter++;
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
                if (stack.isEmpty()) {
                    ScriptDiagnostics.warnAt(
                            log,
                            getCurrentLine(),
                            -1,
                            SafeResourceLoader.getString(
                                    "POPPING_TOO_FAR", ScriptManager.getResourceBundle()));
                    halt();
                    return ScriptRuntime.VOID_MEMORY;
                }
                return stack.pop();
            case VARIABLE:
                if (!symbolTable.containsKey(from.value())) {
                    ScriptDiagnostics.warnAt(
                            log,
                            getCurrentLine(),
                            -1,
                            SafeResourceLoader.getString(
                                    "UNKNOWN_VARIABLE", ScriptManager.getResourceBundle()),
                            from.value());
                    halt();
                    return ScriptRuntime.VOID_MEMORY;
                }
                return symbolTable.get(from.value());
            default:
                ScriptDiagnostics.warnAt(
                        log,
                        getCurrentLine(),
                        -1,
                        SafeResourceLoader.getString(
                                "UNKNOWN_MEMORY_AREA", ScriptManager.getResourceBundle()),
                        from.area().toString());
                halt();
                return ScriptRuntime.VOID_MEMORY;
        }
    }

    /**
     * Execute a move instruction.
     *
     * @param i The instruction to execute.
     */
    private void move(@NonNull Instruction i) {
        MemoryItem memory = loadValue(i.firstLocation());
        storeValue(memory, i.targetLocation());
    }

    /**
     * Negate the sign of a character.
     *
     * @param i The instruction.
     */
    private void negateChar(Instruction i) {
        final MemLocation firstLocation = i.firstLocation();
        final MemoryItem firstItem = loadValue(firstLocation);

        if (fatalError) {
            return;
        }
        checkType(firstLocation, Type.Base.CHAR);
        if (fatalError) {
            return;
        }

        if (!ScriptRuntime.isIntegral(firstItem.value())) {
            valueTypeMismatch(Type.Base.CHAR);
            return;
        }

        final char firstNumber = (char) ScriptRuntime.toInt(firstItem.value());

        MemoryItem result = new MemoryItem(Character.class, (char) -firstNumber);

        storeValue(result, i.targetLocation());
    }

    /**
     * Negate the sign of a double.
     *
     * @param i The instruction.
     */
    private void negateDouble(Instruction i) {
        final MemLocation firstLocation = i.firstLocation();
        final MemoryItem firstItem = loadValue(firstLocation);

        if (fatalError) {
            return;
        }
        checkType(firstLocation, Type.Base.DOUBLE);
        if (fatalError) {
            return;
        }

        if (!ScriptRuntime.isNumeric(firstItem.value())) {
            valueTypeMismatch(Type.Base.DOUBLE);
            return;
        }

        final double firstNumber = ScriptRuntime.toDouble(firstItem.value());

        MemoryItem result = new MemoryItem(Double.class, -firstNumber);

        storeValue(result, i.targetLocation());
    }

    /**
     * Negate the sign of an integer.
     *
     * @param i The instruction.
     */
    private void negateInt(Instruction i) {
        final MemLocation firstLocation = i.firstLocation();
        final MemoryItem firstItem = loadValue(firstLocation);

        if (fatalError) {
            return;
        }
        checkType(firstLocation, Type.Base.INT);
        if (fatalError) {
            return;
        }

        final Object value = firstItem.value();
        if (value instanceof Double doub) {
            // Unknown types are assumed to be integers, but might turn out to be doubles
            storeValue(new MemoryItem(Double.class, -doub), i.targetLocation());
            return;
        }
        if (!ScriptRuntime.isIntegral(value)) {
            valueTypeMismatch(Type.Base.INT);
            return;
        }

        MemoryItem result = new MemoryItem(Integer.class, -ScriptRuntime.toInt(value));

        storeValue(result, i.targetLocation());
    }

    /**
     * A logical not.
     *
     * @param i The instruction we are executing.
     */
    private void not(Instruction i) {
        final MemLocation firstLocation = i.firstLocation();
        final MemoryItem firstItem = loadValue(firstLocation);

        if (fatalError) {
            return;
        }
        checkType(firstLocation, Type.Base.BOOLEAN);
        if (fatalError) {
            return;
        }

        boolean value = (Boolean) firstItem.value();

        MemoryItem result = new MemoryItem(Boolean.class, !value);

        storeValue(result, i.targetLocation());
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
        if (objectLocation == MemArea.IMMEDIATE
                && "breakpoint".equals(methodName)
                && numParams == 0) {
            // Reserved method name, a breakpoint written in the script
            hitBreakpoint(programCounter);
            return true;
        }
        if (objectLocation == MemArea.IMMEDIATE && "yield".equals(methodName) && numParams < 2) {
            // Reserved method name
            if (numParams == 1) {
                Object tag = parameters.get(0).value();
                if (!(tag instanceof String)) {
                    ScriptDiagnostics.warnAt(
                            log,
                            getCurrentLine(),
                            -1,
                            SafeResourceLoader.getString(
                                    "YIELD_PARAMETER", ScriptManager.getResourceBundle()),
                            methodName);
                    halt();
                    return true;
                }
                if (yieldHandler != null) {
                    yieldHandler.onYield(this, (String) tag);
                } else {
                    ScriptManager.yieldScript(this, (String) tag);
                }
            } else if (yieldHandler != null) {
                yieldHandler.onYield(this, null);
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
        MemoryItem result = new MemoryItem(Boolean.class, operation.test(lastComparison));
        storeValue(result, instruction.targetLocation());
    }

    /** Execute one instruction. */
    public synchronized void step() {
        if (fatalError || (programCounter < 0) || (programCounter >= instructions.size())) {
            // Stop executing
            return;
        }
        final Instruction original = patchedInstructions.get(programCounter);
        if (original != null) {
            if (resumingFromBreakpoint) {
                // Continue past the breakpoint we stopped at
                resumingFromBreakpoint = false;
                execute(original);
            } else {
                resumingFromBreakpoint = true;
                hitBreakpoint(programCounter);
            }
            return;
        }
        resumingFromBreakpoint = false;
        execute(instructions.get(programCounter));
    }

    /**
     * Store a value in the specified memory location. May halt the program if something goes wrong.
     *
     * @param item The item to store.
     * @param location The location to store the item in.
     */
    private void storeValue(MemoryItem originalItem, MemLocation location) {
        MemoryItem item = originalItem;
        switch (location.area()) {
            case STACK:
                stack.push(item);
                break;
            case VARIABLE:
                final String variable = (String) location.value();
                final Class<?> declaredType = declaredTypes.get(variable);
                if (declaredType != null) {
                    item = ScriptRuntime.widen(declaredType, item);
                }
                if (declaredType != null && !ScriptRuntime.fitsType(declaredType, item.value())) {
                    ScriptDiagnostics.warnAt(
                            log,
                            getCurrentLine(),
                            -1,
                            SafeResourceLoader.getString(
                                    "VARIABLE_TYPE_MISMATCH", ScriptManager.getResourceBundle()),
                            item.value() == null ? "null" : item.value().getClass().getSimpleName(),
                            variable,
                            declaredType.getSimpleName());
                    halt();
                    break;
                }
                symbolTable.put(variable, item);
                break;
            case IMMEDIATE:
            default:
                ScriptDiagnostics.warnAt(
                        log,
                        getCurrentLine(),
                        -1,
                        SafeResourceLoader.getString(
                                "INVALID_MEMORY_LOCATION", ScriptManager.getResourceBundle()),
                        location.area());
                halt();
                break;
        }
    }
}
