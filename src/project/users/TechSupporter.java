package project.users;

import project.enums.Status;
import project.models.Order;

import java.util.ArrayList;
import java.util.List;

public class TechSupporter extends Employee {
    private static final long serialVersionUID = 19L;

    private List<Order> orders;

    public TechSupporter() {
        super();
        this.orders = new ArrayList<>();
    }

    public TechSupporter(String fullName, String email, String password,
                         int id, String position) {
        super(fullName, email, password, id, position);
        this.orders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        if (!orders.contains(order)) {
            orders.add(order);
            System.out.println("Order added: " + order.getOrderId());
        }
    }

    public void acceptOrder(Order order) {
        if (orders.contains(order)) {
            order.setStatus(Status.ACCEPTED);
            System.out.println("Order accepted: " + order.getOrderId());
        } else {
            System.out.println("Order not found.");
        }
    }

    public void rejectOrder(Order order) {
        if (orders.contains(order)) {
            order.setStatus(Status.REJECTED);
            System.out.println("Order rejected: " + order.getOrderId());
        } else {
            System.out.println("Order not found.");
        }
    }

    public void completeOrder(Order order) {
        if (orders.contains(order)) {
            order.setStatus(Status.COMPLETED);
            System.out.println("Order completed: " + order.getOrderId());
        } else {
            System.out.println("Order not found.");
        }
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }

    public List<Order> viewOrder(Status status) {
        List<Order> filtered = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == status) filtered.add(order);
        }
        return filtered;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    @Override
    public String toString() {
        return super.toString() + ", orders=" + orders.size();
    }
}