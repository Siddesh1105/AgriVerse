package com.mainproject.controller;

import com.mainproject.dao.OrderDAO;
import com.mainproject.model.Order;

import java.util.List;

public class OrderController {

    private final OrderDAO orderDAO;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public OrderController() {

        orderDAO = new OrderDAO();
    }

    // =====================================================
    // PLACE ORDER
    // =====================================================

    public String placeOrder(Order order) {

        return orderDAO.placeOrder(order);
    }

    // =====================================================
    // COMPLETE VERIFIED RAZORPAY PAYMENT
    // =====================================================

    public boolean completePayment(
            String orderId,
            String paymentId,
            String razorpayOrderId,
            String paymentMethod) {

        return orderDAO.completePayment(
                orderId,
                paymentId,
                razorpayOrderId,
                paymentMethod
        );
    }

    // =====================================================
    // GET BUYER ORDERS
    // =====================================================

    public List<Order> getBuyerOrders(
            String buyerEmail) {

        return orderDAO.getBuyerOrders(
                buyerEmail
        );
    }

    // =====================================================
    // GET FARMER ORDERS
    // =====================================================

    public List<Order> getFarmerOrders(
            String farmerEmail) {

        return orderDAO.getFarmerOrders(
                farmerEmail
        );
    }

    // =====================================================
    // GET ORDER BY ID
    // =====================================================

    public Order getOrderById(
            String orderId) {

        return orderDAO.getOrderById(
                orderId
        );
    }

    // =====================================================
    // UPDATE OVERALL ORDER STATUS
    // =====================================================

    public boolean updateStatus(
            String orderId,
            String status) {

        return orderDAO.updateStatus(
                orderId,
                status
        );
    }

    // =====================================================
    // UPDATE INDIVIDUAL PRODUCT STATUS
    // =====================================================

    public boolean updateOrderItemStatus(
            String orderId,
            String productId,
            String farmerEmail,
            String status) {

        return orderDAO.updateOrderItemStatus(
                orderId,
                productId,
                farmerEmail,
                status
        );
    }
}