package project.models;

import project.users.Employee;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Message implements Serializable {
    private static final long serialVersionUID = 12L;

    private Employee sender;
    private Employee receiver;
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;

    public Message() {}

    public Message(Employee sender, Employee receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(sender, message.sender) &&
                Objects.equals(receiver, message.receiver) &&
                Objects.equals(timestamp, message.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, timestamp);
    }

    public Employee getSender() { return sender; }
    public Employee getReceiver() { return receiver; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isRead() { return isRead; }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + sender.getFullName() +
                ", to=" + receiver.getFullName() +
                ", content='" + content + "'" +
                ", time=" + timestamp +
                ", isRead=" + isRead +
                "}";
    }
}