package project.models;

import project.enums.Status;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Order implements Serializable {
    private static final long serialVersionUID = 16L;

    private int orderId;
    private String description;
    private Status status;
    private LocalDateTime createdAt;

    public Order() {}

    public Order(int orderId, String description, Status status) {
        this.orderId = orderId;
        this.description = description;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + orderId +
                ", description='" + description + "'" +
                ", status=" + status +
                ", createdAt=" + createdAt +
                "}";
    }
}