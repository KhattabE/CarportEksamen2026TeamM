package app.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProfileOrder {

    private int orderId;
    private int quoteId;
    private BigDecimal totalPrice;
    private String orderStatus;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    private int widthCm;
    private int lengthCm;
    private int heightCm;
    private boolean hasShed;
    private String previewImage;

    public ProfileOrder(int orderId, int quoteId, BigDecimal totalPrice, String orderStatus, String paymentStatus, LocalDateTime createdAt, LocalDateTime paidAt, int widthCm, int lengthCm, int heightCm, boolean hasShed) {
        this.orderId = orderId;
        this.quoteId = quoteId;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.widthCm = widthCm;
        this.lengthCm = lengthCm;
        this.heightCm = heightCm;
        this.hasShed = hasShed;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
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

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }
}