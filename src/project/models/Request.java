package project.models;

public class Request {
    private String description;
    private boolean pendingApproval;

    public Request(String description, boolean pendingApproval) {
        this.description = description;
        this.pendingApproval = pendingApproval;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPendingApproval() {
        return pendingApproval;
    }

    public void setPendingApproval(boolean pendingApproval) {
        this.pendingApproval = pendingApproval;
    }
}