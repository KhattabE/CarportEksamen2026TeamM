package app.services;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculatedMaterialLineTest {

    // Tests that one calculated material line calculates its total price correctly
    @Test
    void testGetLineTotal() {
        CalculatedMaterialLine line = new CalculatedMaterialLine(1, 600, 2, new BigDecimal("149.95"), "Sternbrædder", false);

        assertEquals(1, line.getMaterialId());
        assertEquals(600, line.getLengthCm());
        assertEquals(2, line.getQuantity());
        assertEquals(new BigDecimal("149.95"), line.getUnitPrice());
        assertEquals("stk.", line.getUnit());
        assertEquals("Sternbrædder", line.getUsageDescription());
        assertEquals(0, new BigDecimal("299.90").compareTo(line.getLineTotal()));
    }
}