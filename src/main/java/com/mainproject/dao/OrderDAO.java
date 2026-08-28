package com.mainproject.dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import com.mainproject.config.FirebaseConfig;
import com.mainproject.model.Order;
import com.mainproject.model.OrderItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class OrderDAO {

    private static final String COLLECTION =
            "orders";

    private final Firestore db;

    public OrderDAO() {

        db = FirebaseConfig.getFirestore();
    }

    // =====================================================
    // PLACE ORDER
    // =====================================================

    public String placeOrder(
            Order order) {

        try {

            if (order == null) {
                return null;
            }

            if (order.getBuyerEmail() == null ||
                    order.getBuyerEmail()
                            .trim()
                            .isEmpty()) {

                return null;
            }

            if (order.getItems() == null ||
                    order.getItems().isEmpty()) {

                return null;
            }

            // Calculate totals
            order.calculateTotals();

            // Overall order status
            order.setStatus("Pending");

            // Date
            order.setOrderDate(
                    new Date()
            );

            // Create Firestore document
            DocumentReference document =
                    db.collection(COLLECTION)
                            .document();

            order.setOrderId(
                    document.getId()
            );

            // Save
            document.set(order)
                    .get();

            System.out.println(
                    "Order created successfully: "
                            + order.getOrderId()
            );

            return order.getOrderId();

        } catch (Exception e) {

            System.out.println(
                    "Error placing order:"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // GET BUYER ORDERS
    // =====================================================

    public List<Order> getBuyerOrders(
            String buyerEmail) {

        List<Order> orders =
                new ArrayList<>();

        try {

            if (buyerEmail == null ||
                    buyerEmail.trim().isEmpty()) {

                return orders;
            }

            QuerySnapshot snapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "buyerEmail",
                                    buyerEmail.trim()
                            )
                            .get()
                            .get();

            for (QueryDocumentSnapshot document
                    : snapshot.getDocuments()) {

                Order order =
                        document.toObject(
                                Order.class
                        );

                if (order != null) {

                    order.setOrderId(
                            document.getId()
                    );

                    orders.add(order);
                }
            }

            sortOrders(orders);

        } catch (Exception e) {

            System.out.println(
                    "Error loading buyer orders:"
            );

            e.printStackTrace();
        }

        return orders;
    }

    // =====================================================
    // GET FARMER ORDERS
    // =====================================================

    public List<Order> getFarmerOrders(
            String farmerEmail) {

        List<Order> farmerOrders =
                new ArrayList<>();

        try {

            if (farmerEmail == null ||
                    farmerEmail.trim().isEmpty()) {

                return farmerOrders;
            }

            QuerySnapshot snapshot =
                    db.collection(COLLECTION)
                            .get()
                            .get();

            for (QueryDocumentSnapshot document
                    : snapshot.getDocuments()) {

                Order order =
                        document.toObject(
                                Order.class
                        );

                if (order == null ||
                        order.getItems() == null) {

                    continue;
                }

                order.setOrderId(
                        document.getId()
                );

                boolean found =
                        false;

                for (OrderItem item
                        : order.getItems()) {

                    if (item == null) {
                        continue;
                    }

                    if (item.getFarmerEmail() != null &&
                            item.getFarmerEmail()
                                    .equalsIgnoreCase(
                                            farmerEmail.trim()
                                    )) {

                        found = true;
                        break;
                    }
                }

                if (found) {

                    farmerOrders.add(order);
                }
            }

            sortOrders(farmerOrders);

        } catch (Exception e) {

            System.out.println(
                    "Error loading farmer orders:"
            );

            e.printStackTrace();
        }

        return farmerOrders;
    }

    // =====================================================
    // GET ORDER BY ID
    // =====================================================

    public Order getOrderById(
            String orderId) {

        try {

            if (orderId == null ||
                    orderId.trim().isEmpty()) {

                return null;
            }

            DocumentSnapshot snapshot =
                    db.collection(COLLECTION)
                            .document(orderId)
                            .get()
                            .get();

            if (!snapshot.exists()) {

                return null;
            }

            Order order =
                    snapshot.toObject(
                            Order.class
                    );

            if (order != null) {

                order.setOrderId(
                        snapshot.getId()
                );
            }

            return order;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // UPDATE OVERALL ORDER STATUS
    // =====================================================

    public boolean updateStatus(
            String orderId,
            String status) {

        try {

            db.collection(COLLECTION)
                    .document(orderId)
                    .update(
                            "status",
                            status
                    )
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // UPDATE INDIVIDUAL ITEM STATUS
    // =====================================================

    public boolean updateOrderItemStatus(
            String orderId,
            String productId,
            String farmerEmail,
            String status) {

        try {

            DocumentReference reference =
                    db.collection(COLLECTION)
                            .document(orderId);

            DocumentSnapshot snapshot =
                    reference.get()
                            .get();

            if (!snapshot.exists()) {

                return false;
            }

            Order order =
                    snapshot.toObject(
                            Order.class
                    );

            if (order == null ||
                    order.getItems() == null) {

                return false;
            }

            boolean updated =
                    false;

            for (OrderItem item
                    : order.getItems()) {

                if (item == null) {
                    continue;
                }

                boolean productMatches =
                        item.getProductId() != null &&
                                item.getProductId()
                                        .equals(productId);

                boolean farmerMatches =
                        item.getFarmerEmail() != null &&
                                item.getFarmerEmail()
                                        .equalsIgnoreCase(
                                                farmerEmail
                                        );

                if (productMatches &&
                        farmerMatches) {

                    item.setStatus(status);

                    updated = true;
                    break;
                }
            }

            if (!updated) {

                return false;
            }

            // Update complete items list
            reference.update(
                    "items",
                    order.getItems()
            ).get();

            updateOverallStatus(
                    order
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating order item status:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // UPDATE OVERALL STATUS AUTOMATICALLY
    // =====================================================

    private void updateOverallStatus(
            Order order) {

        try {

            if (order == null ||
                    order.getItems() == null ||
                    order.getItems().isEmpty()) {

                return;
            }

            boolean allCompleted =
                    true;

            boolean hasProcessing =
                    false;

            boolean hasAccepted =
                    false;

            boolean hasPending =
                    false;

            for (OrderItem item
                    : order.getItems()) {

                if (item == null) {
                    continue;
                }

                String status =
                        item.getStatus() == null
                                ? "Pending"
                                : item.getStatus();

                if (!status.equalsIgnoreCase(
                        "Completed")) {

                    allCompleted = false;
                }

                if (status.equalsIgnoreCase(
                        "Processing")) {

                    hasProcessing = true;
                }

                if (status.equalsIgnoreCase(
                        "Accepted")) {

                    hasAccepted = true;
                }

                if (status.equalsIgnoreCase(
                        "Pending")) {

                    hasPending = true;
                }
            }

            String overallStatus;

            if (allCompleted) {

                overallStatus =
                        "Completed";

            } else if (hasProcessing) {

                overallStatus =
                        "Processing";

            } else if (hasAccepted) {

                overallStatus =
                        "Accepted";

            } else if (hasPending) {

                overallStatus =
                        "Pending";

            } else {

                overallStatus =
                        "Updated";
            }

            db.collection(COLLECTION)
                    .document(
                            order.getOrderId()
                    )
                    .update(
                            "status",
                            overallStatus
                    )
                    .get();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // SORT ORDERS
    // =====================================================

    private void sortOrders(
            List<Order> orders) {

        orders.sort(
                Comparator.comparing(
                        Order::getOrderDate,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()
                        )
                )
        );
    }
}