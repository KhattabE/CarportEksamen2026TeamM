package app.services;

import app.entities.QuoteMaterialLine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CarportCalculationResult {

    private List<CalculatedMaterialLine> materialLines;
    private BigDecimal totalPrice;

    public CarportCalculationResult(List<CalculatedMaterialLine> materialLines) {
        this.materialLines = materialLines;
        this.totalPrice = calculateTotalPrice();
    }

    // Adds all material line prices together
    private BigDecimal calculateTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;

        for (CalculatedMaterialLine line : materialLines) {
            total = total.add(line.getLineTotal());
        }

        return total;
    }

    public List<CalculatedMaterialLine> getMaterialLines() {
        return materialLines;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    // Converts the calculated material lines into database-ready quote material lines
    public List<QuoteMaterialLine> createQuoteMaterialLines(int quoteId) {
        List<QuoteMaterialLine> quoteMaterialLines = new ArrayList<>();

        for (CalculatedMaterialLine line : materialLines) {
            QuoteMaterialLine quoteMaterialLine = new QuoteMaterialLine(0, quoteId, line.getMaterialId(), line.getLengthCm(), BigDecimal.valueOf(line.getQuantity()), line.getUnitPrice(), line.getUsageDescription());

            quoteMaterialLines.add(quoteMaterialLine);
        }

        return quoteMaterialLines;
    }
}