package app.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProfileOrderTest {

    // Tests that profile order stores order, price and carport data correctly
    @Test
    void testProfileOrderConstructorAndGetters() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime paidAt = LocalDateTime.now();

        ProfileOrder order = new ProfileOrder(1, 5, new BigDecimal("5774.85"), "IN_PROGRESS", "PAID", createdAt, paidAt, 300, 600, 230, true);

        assertEquals(1, order.getOrderId());
        assertEquals(5, order.getQuoteId());
        assertEquals(new BigDecimal("5774.85"), order.getTotalPrice());
        assertEquals("IN_PROGRESS", order.getOrderStatus());
        assertEquals("PAID", order.getPaymentStatus());
        assertEquals(createdAt, order.getCreatedAt());
        assertEquals(paidAt, order.getPaidAt());
        assertEquals(300, order.getWidthCm());
        assertEquals(600, order.getLengthCm());
        assertEquals(230, order.getHeightCm());
        assertTrue(order.isHasShed());
    }
}