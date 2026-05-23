package app.services;

import app.entities.QuoteMaterialLine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CarportCalculationResult {

    private static final BigDecimal MARKUP_FACTOR = new BigDecimal("1.25");

    private List<CalculatedMaterialLine> materialLines;
    private BigDecimal totalPrice;

    public CarportCalculationResult(List<CalculatedMaterialLine> materialLines) {
        this.materialLines = materialLines;
        this.totalPrice = calculateTotalPrice();
    }

    private BigDecimal calculateTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;

        for (CalculatedMaterialLine line : materialLines) {
            total = total.add(line.getLineTotal());
        }

        return total.multiply(MARKUP_FACTOR).setScale(2, RoundingMode.HALF_UP);
    }

    public List<CalculatedMaterialLine> getMaterialLines() {
        return materialLines;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public List<QuoteMaterialLine> createQuoteMaterialLines(int quoteId) {
        List<QuoteMaterialLine> quoteMaterialLines = new ArrayList<>();

        for (CalculatedMaterialLine line : materialLines) {
            QuoteMaterialLine quoteMaterialLine = new QuoteMaterialLine(0, quoteId, line.getMaterialId(), line.getLengthCm(), BigDecimal.valueOf(line.getQuantity()), line.getUnitPrice(), line.getUnit(), line.getUsageDescription());

            quoteMaterialLines.add(quoteMaterialLine);
        }

        return quoteMaterialLines;
    }
}

