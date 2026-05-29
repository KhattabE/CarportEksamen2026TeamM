package app.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    // Tests that an order stores quote id, status, payment status and dates correctly
    @Test
    void testOrderConstructorAndGetters() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime paidAt = LocalDateTime.now();

        Order order = new Order(1, 10, "IN_PROGRESS", "PAID", createdAt, paidAt);

        assertEquals(1, order.getOrderId());
        assertEquals(10, order.getQuoteId());
        assertEquals("IN_PROGRESS", order.getOrderStatus());
        assertEquals("PAID", order.getPaymentStatus());
        assertEquals(createdAt, order.getCreatedAt());
        assertEquals(paidAt, order.getPaidAt());
    }
}