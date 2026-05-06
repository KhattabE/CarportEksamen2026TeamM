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
}
