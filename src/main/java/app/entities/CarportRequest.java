package app.entities;

import java.time.LocalDateTime;

public class CarportRequest {
    private int requestId;
    private int userId;
    private Carport carport;
    private String status;
    private String customerComment;
    private LocalDateTime createdAt;

    public CarportRequest(int requestId, int userId, Carport carport, String status, String customerComment, LocalDateTime createdAt) {
        this.requestId = requestId;
        this.userId = userId;
        this.carport = carport;
        this.status = status;
        this.customerComment = customerComment;
        this.createdAt = createdAt;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getUserId() {
        return userId;
    }

    public Carport getCarport() {
        return carport;
    }

    public String getStatus() {
        return status;
    }

    public String getCustomerComment() {
        return customerComment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
