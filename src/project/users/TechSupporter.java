package project.users;

import project.enums.Status;
import project.models.Order;

import java.util.ArrayList;
import java.util.List;

public class TechSupporter extends Employee {
    private List<Order> orders = new ArrayList<>();

    public List<Order> viewOrder(Status status) {
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus().equals(status)) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }

    public void acceptOrder(Order order) {
        if (orders.contains(order)) {
            order.setStatus(Status.ACCEPTED);
            System.out.println("Order " + order.getOrderId() + " accepted.");
        } else {
            System.out.println("Order not found.");
        }
    }

    public void rejectOrder(Order order) {
        if (orders.contains(order)) {
            order.setStatus(Status.REJECTED);
            System.out.println("Order " + order.getOrderId() + " rejected.");
        } else {
            System.out.println("Order not found.");
        }
    }

    public void addOrder(Order order) {
        if (!orders.contains(order)) {
            orders.add(order);
            System.out.println("Order " + order.getOrderId() + " added.");
        } else {
            System.out.println("Order " + order.getOrderId() + " already exists.");
        }
    }

    public void removeOrder(Order order) {
        if (orders.contains(order)) {
            orders.remove(order);
            System.out.println("Order " + order.getOrderId() + " removed.");
        } else {
            System.out.println("Order not found.");
        }
    }
}