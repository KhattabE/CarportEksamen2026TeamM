package app.entities;

import java.math.BigDecimal;

public class QuoteMaterialLine {
    private int quoteLineId;
    private int quoteId;
    private int materialId;
    private int lengthCm;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String unit;
    private String usageDescription;

    public QuoteMaterialLine(int quoteLineId, int quoteId, int materialId, int lengthCm, BigDecimal quantity, BigDecimal unitPrice, String unit, String usageDescription) {
        this.quoteLineId = quoteLineId;
        this.quoteId = quoteId;
        this.materialId = materialId;
        this.lengthCm = lengthCm;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unit = unit;
        this.usageDescription = usageDescription;
    }

    public int getQuoteLineId() {
        return quoteLineId;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public int getLengthCm() {
        return lengthCm;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public String getUnit() {
        return unit;
    }

    public String getUsageDescription() {
        return usageDescription;
    }

}
