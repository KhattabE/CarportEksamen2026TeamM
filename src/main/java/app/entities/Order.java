package app.entities;

import java.time.LocalDateTime;

public class Order {
    private int order_id;
    private int quote_id;
    private String order_status;
    private String payment_status;
    private LocalDateTime created_at;
    private LocalDateTime paid_at;

    public Order(int order_id, int quote_id, String order_status, String payment_status, LocalDateTime created_at, LocalDateTime paid_at) {
        this.order_id = order_id;
        this.quote_id = quote_id;
        this.order_status = order_status;
        this.payment_status = payment_status;
        this.created_at = created_at;
        this.paid_at = paid_at;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(int quote_id) {
        this.quote_id = quote_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getPaid_at() {
        return paid_at;
    }

    public void setPaid_at(LocalDateTime paid_at) {
        this.paid_at = paid_at;
    }
}
