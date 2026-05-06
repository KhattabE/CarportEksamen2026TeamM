package app.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Quote {
    private int quoteId;
    private int sellerId;
    private BigDecimal totalPrice;
    private String status;
    private String sellerComment;
    private LocalDateTime validUntil;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime rejectedAt;

    public Quote(int quoteId, int sellerId, BigDecimal totalPrice, String status, String sellerComment, LocalDateTime validUntil, LocalDateTime createdAt, LocalDateTime sentAt, LocalDateTime acceptedAt, LocalDateTime rejectedAt) {
        this.quoteId = quoteId;
        this.sellerId = sellerId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.sellerComment = sellerComment;
        this.validUntil = validUntil;
        this.createdAt = createdAt;
        this.sentAt = sentAt;
        this.acceptedAt = acceptedAt;
        this.rejectedAt = rejectedAt;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getSellerComment() {
        return sellerComment;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public LocalDateTime getRejectedAt() {
        return rejectedAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
}
