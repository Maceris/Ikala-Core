package com.ikalagaming.scripting.interpreter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * Tests the memory location type checks.
 *
 * @author Ches Burks
 */
class TestMemLocation {

    /**
     * Each location should report exactly the type it was created with, whether it holds an
     * immediate value or refers to the stack.
     *
     * @param location The location to check.
     * @param type The type it was created with.
     */
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource
    void types(MemLocation location, Class<?> type) {
        Assertions.assertEquals(type == Boolean.class, location.isBoolean());
        Assertions.assertEquals(type == Character.class, location.isChar());
        Assertions.assertEquals(type == Double.class, location.isDouble());
        Assertions.assertEquals(type == Integer.class, location.isInt());
        Assertions.assertEquals(type == String.class, location.isString());
    }

    static Stream<Arguments> types() {
        final Object[][] values = {
            {Boolean.class, false}, {Character.class, 'a'}, {Double.class, 4.3d},
            {Integer.class, 9}, {String.class, "test"}, {Object.class, new Object()}
        };
        return Stream.of(values)
                .flatMap(
                        value -> {
                            final Class<?> type = (Class<?>) value[0];
                            return Stream.of(
                                    Arguments.of(
                                            new MemLocation(MemArea.IMMEDIATE, type, value[1]),
                                            type),
                                    Arguments.of(new MemLocation(MemArea.STACK, type), type));
                        });
    }
}
