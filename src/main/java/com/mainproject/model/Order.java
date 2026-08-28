package com.mainproject.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {

    private String orderId;

    // =====================================================
    // BUYER DETAILS
    // =====================================================

    private String buyerEmail;
    private String buyerName;

    // =====================================================
    // ORDER ITEMS
    // =====================================================

    private List<OrderItem> items = new ArrayList<>();

    // =====================================================
    // DELIVERY
    // =====================================================

    private String deliveryAddress;

    // =====================================================
    // PAYMENT
    // =====================================================

    private String paymentMethod;

    // =====================================================
    // AMOUNTS
    // =====================================================

    private double subtotal;
    private double deliveryCharge;
    private double totalAmount;

    // Overall order status
    private String status;

    private Date orderDate;

    // =====================================================
    // EMPTY CONSTRUCTOR
    // =====================================================

    public Order() {

        items = new ArrayList<>();
    }

    // =====================================================
    // CALCULATE TOTALS
    // =====================================================

    public void calculateTotals() {

        subtotal = 0;

        if (items != null) {

            for (OrderItem item : items) {

                if (item == null) {
                    continue;
                }

                item.calculateTotal();

                subtotal +=
                        item.getTotalPrice();

                // Default item status
                if (item.getStatus() == null ||
                        item.getStatus().trim().isEmpty()) {

                    item.setStatus("Pending");
                }
            }
        }

        deliveryCharge =
                subtotal > 0 ? 40 : 0;

        totalAmount =
                subtotal + deliveryCharge;
    }

    // =====================================================
    // ORDER ID
    // =====================================================

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    // =====================================================
    // BUYER EMAIL
    // =====================================================

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    // =====================================================
    // BUYER NAME
    // =====================================================

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    // =====================================================
    // ITEMS
    // =====================================================

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {

        this.items =
                items == null
                        ? new ArrayList<>()
                        : items;
    }

    // =====================================================
    // DELIVERY ADDRESS
    // =====================================================

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(
            String deliveryAddress) {

        this.deliveryAddress =
                deliveryAddress;
    }

    // =====================================================
    // PAYMENT METHOD
    // =====================================================

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(
            String paymentMethod) {

        this.paymentMethod =
                paymentMethod;
    }

    // =====================================================
    // SUBTOTAL
    // =====================================================

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    // =====================================================
    // DELIVERY CHARGE
    // =====================================================

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(
            double deliveryCharge) {

        this.deliveryCharge =
                deliveryCharge;
    }

    // =====================================================
    // TOTAL AMOUNT
    // =====================================================

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(
            double totalAmount) {

        this.totalAmount =
                totalAmount;
    }

    // =====================================================
    // ORDER STATUS
    // =====================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // =====================================================
    // ORDER DATE
    // =====================================================

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}