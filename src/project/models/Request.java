package project.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Request implements Serializable {
    private static final long serialVersionUID = 17L;

    private String description;
    private boolean pendingApproval;
    private LocalDateTime createdAt;

    public Request() {}

    public Request(String description) {
        this.description = description;
        this.pendingApproval = true;
        this.createdAt = LocalDateTime.now();
    }

    public void approve() {
        this.pendingApproval = false;
        System.out.println("Request approved: " + description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(description, request.description) &&
                Objects.equals(createdAt, request.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, createdAt);
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isPendingApproval() { return pendingApproval; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "Request{" +
                "description='" + description + "'" +
                ", pending=" + pendingApproval +
                ", createdAt=" + createdAt +
                "}";
    }
}