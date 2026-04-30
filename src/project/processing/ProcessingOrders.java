package project.processing;

import project.models.Order;

import java.util.List;


public abstract class ProcessingOrders {
    protected List<Order> orders;

    public ProcessingOrders(List<Order> orders) {
        this.orders = orders;
    }

    public final void processOrder() {
        filterOrders();
        validateOrders();
        executeOrders();
        finalizeProcessing();
    }

    protected abstract void filterOrders();
    protected void validateOrders() {
        System.out.println("Validating orders...");
    }
    protected abstract void executeOrders();
    protected void finalizeProcessing() {
        System.out.println("Finalizing order processing...");
    }
}