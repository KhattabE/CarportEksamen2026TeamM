package app.entities;

import java.time.LocalDateTime;

public class Carport {
    private int widthCm;
    private int lengthCm;
    private int heightCm;
    private boolean hasShed;
    private int shedWidthCm;
    private int shedLengthCm;
    private String roofType;

    public Carport(int widthCm, int lengthCm, int heightCm, boolean hasShed, int shedWidthCm, int shedLengthCm, String roofType) {
        this.widthCm = widthCm;
        this.lengthCm = lengthCm;
        this.heightCm = heightCm;
        this.hasShed = hasShed;
        this.shedWidthCm = shedWidthCm;
        this.shedLengthCm = shedLengthCm;
        this.roofType = roofType;
    }


    public int getWidthCm() {
        return widthCm;
    }

    public int getLengthCm() {
        return lengthCm;
    }

    public int getHeightCm() {
        return heightCm;
    }

    public boolean isHasShed() {
        return hasShed;
    }

    public int getShedWidthCm() {
        return shedWidthCm;
    }

    public int getShedLengthCm() {
        return shedLengthCm;
    }

    public String getRoofType() {
        return roofType;
    }

    public void setWidthCm(int widthCm) {
        this.widthCm = widthCm;
    }

    public void setLengthCm(int lengthCm) {
        this.lengthCm = lengthCm;
    }

    public void setHeightCm(int heightCm) {
        this.heightCm = heightCm;
    }

    public void setHasShed(boolean hasShed) {
        this.hasShed = hasShed;
    }

    public void setShedWidthCm(int shedWidthCm) {
        this.shedWidthCm = shedWidthCm;
    }

    public void setShedLengthCm(int shedLengthCm) {
        this.shedLengthCm = shedLengthCm;
    }

    public void setRoofType(String roofType) {
        this.roofType = roofType;
    }
}
