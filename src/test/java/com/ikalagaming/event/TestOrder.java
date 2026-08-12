package com.ikalagaming.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Order} class.
 *
 * @author Ches Burks
 */
class TestOrder {

    /** Test that the indices of Orders ordered the way we expect.. */
    @Test
    void testRelativeOrder() {
        Assertions.assertTrue(Order.EARLIEST.getIndex() < Order.EARLY.getIndex());
        Assertions.assertTrue(Order.EARLY.getIndex() < Order.DEFAULT.getIndex());
        Assertions.assertTrue(Order.DEFAULT.getIndex() < Order.LATE.getIndex());
        Assertions.assertTrue(Order.LATE.getIndex() < Order.LATEST.getIndex());
        Assertions.assertTrue(Order.LATEST.getIndex() < Order.MONITOR.getIndex());
    }

    /**
     * Test that {@link Order#fromIndex(int)} returns the same value we get from {@link
     * Order#getIndex()}.
     */
    @Test
    void testOrderConversion() {
        for (Order order : Order.values()) {
            Assertions.assertSame(order, Order.fromIndex(order.getIndex()));
        }
    }
}
