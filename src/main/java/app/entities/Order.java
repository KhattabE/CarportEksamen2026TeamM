package app.entities;

import java.time.LocalDateTime;

public class Order {
    private int orderId;
    private int quoteId;
    private String orderStatus;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    public Order(int orderId, int quoteId, String orderStatus, String paymentStatus, LocalDateTime createdAt, LocalDateTime paidAt) {
        this.orderId = orderId;
        this.quoteId = quoteId;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}
