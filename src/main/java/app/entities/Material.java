package app.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Material {
    private int materialId;
    private String name;
    private String description;
    private String category;
    private String unit;
    private BigDecimal pricePerUnit;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Material(int materialId, String name, String description, String category, String unit, BigDecimal pricePerUnit, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.materialId = materialId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getMaterialId() {
        return materialId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }
}
