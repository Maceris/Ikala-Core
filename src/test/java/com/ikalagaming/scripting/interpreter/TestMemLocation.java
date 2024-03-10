package com.ikalagaming.scripting.interpreter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests the functionality of the memory location record.
 *
 * @author Ches Burks
 */
class TestMemLocation {

    /** Checks the type checking convenience methods. */
    @Test
    public void testTypes() {
        MemLocation location;
        location = new MemLocation(MemArea.IMMEDIATE, Boolean.class, false);
        Assertions.assertTrue(location.isBoolean());
        Assertions.assertFalse(location.isChar());
        Assertions.assertFalse(location.isDouble());
        Assertions.assertFalse(location.isInt());
        Assertions.assertFalse(location.isString());
        location = new MemLocation(MemArea.IMMEDIATE, Character.class, 'a');
        Assertions.assertFalse(location.isBoolean());
        Assertions.assertTrue(location.isChar());
        Assertions.assertFalse(location.isDouble());
        Assertions.assertFalse(location.isInt());
        Assertions.assertFalse(location.isString());
        location = new MemLocation(MemArea.IMMEDIATE, Double.class, 4.3d);
        Assertions.assertFalse(location.isBoolean());
        Assertions.assertFalse(location.isChar());
        Assertions.assertTrue(location.isDouble());
        Assertions.assertFalse(location.isInt());
        Assertions.assertFalse(location.isString());
        location = new MemLocation(MemArea.IMMEDIATE, Integer.class, 9);
        Assertions.assertFalse(location.isBoolean());
        Assertions.assertFalse(location.isChar());
        Assertions.assertFalse(location.isDouble());
        Assertions.assertTrue(location.isInt());
        Assertions.assertFalse(location.isString());
        location = new MemLocation(MemArea.IMMEDIATE, String.class, "test");
        Assertions.assertFalse(location.isBoolean());
        Assertions.assertFalse(location.isChar());
        Assertions.assertFalse(location.isDouble());
        Assertions.assertFalse(location.isInt());
        Assertions.assertTrue(location.isString());

        location = new MemLocation(MemArea.STACK, Boolean.class);
        Assertions.assertTrue(location.isBoolean());
        Assertions.assertFalse(location.isChar());
        Assertions.assertFalse(location.isDouble());
        Assertions.assertFalse(location.isInt());
        Assertions.assertFalse(location.isString());
        location = new MemLocation(MemArea.STACK, Character.class);
        Assertions.assertFalse(location.isBoolean());
        Assertions.assertTrue(location.isChar());
        Assertions.assertFalse(location.isDouble());
        Assertions.assertFalse(location.isInt());
        Assertions.assertFalse(location.isString());
        location = new MemLocation(MemArea.STACK, Double.class);
        Assertions.assertFalse(location.isBoolean());
        Assertions.assertFalse(location.isChar());
        Assertions.assertTrue(location.isDouble());
        Assertions.assertFalse(location.isInt());
        Assertions.assertFalse(location.isString());
        location = new MemLocation(MemArea.STACK, Integer.class);
        Assertions.assertFalse(location.isBoolean());
        Assertions.assertFalse(location.isChar());
        Assertions.assertFalse(location.isDouble());
        Assertions.assertTrue(location.isInt());
        Assertions.assertFalse(location.isString());
        location = new MemLocation(MemArea.STACK, String.class);
        Assertions.assertFalse(location.isBoolean());
        Assertions.assertFalse(location.isChar());
        Assertions.assertFalse(location.isDouble());
        Assertions.assertFalse(location.isInt());
        Assertions.assertTrue(location.isString());
    }
}
