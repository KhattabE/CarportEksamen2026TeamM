package app.services;

import java.math.BigDecimal;

public class CalculatedMaterialLine {

    private int materialId;
    private int lengthCm;
    private int quantity;
    private BigDecimal unitPrice;
    private String usageDescription;

    public CalculatedMaterialLine(int materialId, int lengthCm, int quantity, BigDecimal unitPrice, String usageDescription) {
        this.materialId = materialId;
        this.lengthCm = lengthCm;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.usageDescription = usageDescription;
    }

    public int getMaterialId() {
        return materialId;
    }

    public int getLengthCm() {
        return lengthCm;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public String getUsageDescription() {
        return usageDescription;
    }

    // Calculates the price for this one material line
    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}