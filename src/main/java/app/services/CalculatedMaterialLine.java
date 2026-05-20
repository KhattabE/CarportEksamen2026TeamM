package app.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatedMaterialLine {

    private int materialId;
    private int lengthCm;
    private int quantity;
    private BigDecimal unitPrice;
    private String usageDescription;
    private boolean pricePerMeter;

    public CalculatedMaterialLine(int materialId, int lengthCm, int quantity, BigDecimal unitPrice, String usageDescription, boolean pricePerMeter) {
        this.materialId = materialId;
        this.lengthCm = lengthCm;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.usageDescription = usageDescription;
        this.pricePerMeter = pricePerMeter;
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

    public BigDecimal getLineTotal() {
        BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(quantity));

        if (pricePerMeter) {
            BigDecimal lengthMeters = BigDecimal.valueOf(lengthCm).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

            total = total.multiply(lengthMeters);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
