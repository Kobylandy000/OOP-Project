package project.models;

import project.users.Employee;
import project.users.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Message {
    private Employee sender;
    private Employee receiver;
    private String content;
    private LocalDate timeStamp;
    private boolean isRead;



    public Message(Employee sender, Employee receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timeStamp = LocalDate.now();
        this.isRead = false;
    }


    public void sendMessage() {

        receiver.receiveMessage(this);

        System.out.println("Message sent successfully from " + sender.getFullName() + " to " + receiver.getFullName());
    }

    public void markAsRead() {
        this.isRead = true;
        System.out.println("Message from " + sender.getFullName() + " has been marked as read.");
    }

    public String getContent() {
        return content;
    }

    public User getSenderInfo() {
        return sender;
    }


    public User getReceiverInfo() {
        return receiver;
    }

    public LocalDate getTimeStamp() {
        return timeStamp;
    }

    public boolean isRead() {
        return isRead;
    }


    @Override
    public String toString() {
        return "Message{" +
                "Sender=" + sender.getFullName() +
                ", Receiver=" + receiver.getFullName() +
                ", Content='" + content + '\'' +
                ", TimeStamp=" + timeStamp +
                ", IsRead=" + isRead +
                '}';
    }
}
