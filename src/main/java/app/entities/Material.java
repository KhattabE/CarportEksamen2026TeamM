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
    private boolean isActive; // hedder "active" i databasen, skal vi skifte navnet til "isActive" i stedet?
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Material(int materialId, String name, String description, String category, String unit, BigDecimal pricePerUnit, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.materialId = materialId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getMaterialId() {
        return materialId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
