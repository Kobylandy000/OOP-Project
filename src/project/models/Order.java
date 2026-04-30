package project.models;

import project.enums.Status;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private String description;
    private Status status;

    public Order(int orderId, String description, Status status) {
        this.orderId = orderId;
        this.description = description;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isNew() {
        return this.status == Status.PENDING;
    }

    public static List<Order> filterOrders(List<Order> orders, Status status) {
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == status) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }

    public static List<Order> getAcceptedNotDoneOrders(List<Order> orders) {
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == Status.ACCEPTED) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }

    public static List<Order> getDoneOrders(List<Order> orders) {
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == Status.COMPLETED) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }
}