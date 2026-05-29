package app.services;

import app.entities.QuoteMaterialLine;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarportCalculationResultTest {

    // Tests that total price is calculated from all material lines including markup
    @Test
    void testTotalPriceIsCalculatedCorrectly() {
        List<CalculatedMaterialLine> lines = new ArrayList<>();

        lines.add(new CalculatedMaterialLine(1, 600, 2, new BigDecimal("149.95"), "Sternbrædder", false));
        lines.add(new CalculatedMaterialLine(2, 300, 1, new BigDecimal("89.95"), "Sidebræt", false));

        CarportCalculationResult result = new CarportCalculationResult(lines);

        assertEquals(2, result.getMaterialLines().size());
        assertEquals(0, new BigDecimal("487.31").compareTo(result.getTotalPrice()));
    }

    // Tests that calculated lines can be converted to QuoteMaterialLine objects
    @Test
    void testCreateQuoteMaterialLines() {
        List<CalculatedMaterialLine> lines = new ArrayList<>();

        lines.add(new CalculatedMaterialLine(1, 600, 2, new BigDecimal("149.95"), "Sternbrædder", false));

        CarportCalculationResult result = new CarportCalculationResult(lines);
        List<QuoteMaterialLine> quoteMaterialLines = result.createQuoteMaterialLines(10);

        assertEquals(1, quoteMaterialLines.size());
        assertEquals(10, quoteMaterialLines.get(0).getQuoteId());
        assertEquals(1, quoteMaterialLines.get(0).getMaterialId());
        assertEquals(600, quoteMaterialLines.get(0).getLengthCm());
        assertEquals(new BigDecimal("2"), quoteMaterialLines.get(0).getQuantity());
        assertEquals(new BigDecimal("149.95"), quoteMaterialLines.get(0).getUnitPrice());
        assertEquals("stk.", quoteMaterialLines.get(0).getUnit());
        assertEquals("Sternbrædder", quoteMaterialLines.get(0).getUsageDescription());
    }
}