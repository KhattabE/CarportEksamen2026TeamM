package app.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Quote {
    private int quoteId;
    private int requestId;
    private int sellerId;
    private BigDecimal totalPrice;
    private String status;
    private String sellerComment;
    private LocalDate validUntil;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime rejectedAt;
    private String previewImage;

    public Quote(int quoteId, int requestId, int sellerId, BigDecimal totalPrice, String status, String sellerComment, LocalDate validUntil, LocalDateTime createdAt, LocalDateTime sentAt, LocalDateTime acceptedAt, LocalDateTime rejectedAt) {
        this.quoteId = quoteId;
        this.requestId = requestId;
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

    //This constructor is used for the QuoteMapper method, since some of the attributes is not needed
    public Quote(int requestId, int sellerId, BigDecimal totalPrice, String status, String sellerComment, LocalDate validUntil) {
        this.requestId = requestId;
        this.sellerId = sellerId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.sellerComment = sellerComment;
        this.validUntil = validUntil;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public int getRequestId() {
        return requestId;
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

    public LocalDate getValidUntil() {
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

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }
}