package com.mainproject.controller;

import com.mainproject.dao.OrderDAO;
import com.mainproject.model.BuyerCartItem;
import com.mainproject.model.Order;

import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    private final OrderDAO orderDAO;

    private final BuyerCartController cartController;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public DashboardController() {

        orderDAO = new OrderDAO();

        cartController =
                new BuyerCartController();
    }


    // =====================================================
    // GET ALL BUYER ORDERS
    // =====================================================

    public List<Order> getBuyerOrders(
            String buyerEmail) {

        if (buyerEmail == null ||
                buyerEmail.trim().isEmpty()) {

            return new ArrayList<>();
        }

        return orderDAO.getBuyerOrders(
                buyerEmail.trim()
        );
    }


    // =====================================================
    // TOTAL ORDERS
    // =====================================================

    public int getTotalOrders(
            String buyerEmail) {

        return getBuyerOrders(
                buyerEmail
        ).size();
    }


    // =====================================================
    // PENDING / ACTIVE ORDERS
    // =====================================================

    public int getPendingOrders(
            String buyerEmail) {

        int count = 0;

        List<Order> orders =
                getBuyerOrders(buyerEmail);

        for (Order order : orders) {

            if (order == null) {
                continue;
            }

            String status =
                    order.getStatus();

            if (status == null ||
                    status.trim().isEmpty()) {

                count++;

                continue;
            }

            if (status.equalsIgnoreCase("Pending") ||
                    status.equalsIgnoreCase("Accepted") ||
                    status.equalsIgnoreCase("Processing") ||
                    status.equalsIgnoreCase("Updated")) {

                count++;
            }
        }

        return count;
    }


    // =====================================================
    // COMPLETED ORDERS
    // =====================================================

    public int getCompletedOrders(
            String buyerEmail) {

        int count = 0;

        List<Order> orders =
                getBuyerOrders(buyerEmail);

        for (Order order : orders) {

            if (order == null) {
                continue;
            }

            String status =
                    order.getStatus();

            if (status != null &&
                    (status.equalsIgnoreCase("Completed") ||
                            status.equalsIgnoreCase("Delivered"))) {

                count++;
            }
        }

        return count;
    }


    // =====================================================
    // RECENT ORDERS
    // =====================================================

    public List<Order> getRecentOrders(
            String buyerEmail,
            int limit) {

        List<Order> allOrders =
                getBuyerOrders(buyerEmail);

        List<Order> recentOrders =
                new ArrayList<>();

        if (limit <= 0) {
            return recentOrders;
        }

        int max =
                Math.min(
                        limit,
                        allOrders.size()
                );

        for (int i = 0;
             i < max;
             i++) {

            recentOrders.add(
                    allOrders.get(i)
            );
        }

        return recentOrders;
    }


    // =====================================================
    // GET CART ITEMS
    // =====================================================

    public List<BuyerCartItem> getCartItems(
            String buyerEmail) {

        if (buyerEmail == null ||
                buyerEmail.trim().isEmpty()) {

            return new ArrayList<>();
        }

        List<BuyerCartItem> items =
                cartController.getCartItems(
                        buyerEmail.trim()
                );

        return items == null
                ? new ArrayList<>()
                : items;
    }


    // =====================================================
    // GET CART ITEM COUNT
    // =====================================================

    public int getCartItemCount(
            String buyerEmail) {

        return getCartItems(
                buyerEmail
        ).size();
    }


    // =====================================================
    // GET TOTAL CART QUANTITY
    // =====================================================

    public int getTotalCartQuantity(
            String buyerEmail) {

        int totalQuantity = 0;

        List<BuyerCartItem> items =
                getCartItems(buyerEmail);

        for (BuyerCartItem item : items) {

            if (item == null) {
                continue;
            }

            totalQuantity +=
                    Math.max(
                            1,
                            (int) Math.round(
                                    item.getQuantity()
                            )
                    );
        }

        return totalQuantity;
    }


    // =====================================================
    // GET CART TOTAL
    // =====================================================

    public double getCartTotal(
            String buyerEmail) {

        double total = 0;

        List<BuyerCartItem> items =
                getCartItems(buyerEmail);

        for (BuyerCartItem item : items) {

            if (item == null) {
                continue;
            }

            total += item.getTotalPrice();
        }

        return total;
    }
}