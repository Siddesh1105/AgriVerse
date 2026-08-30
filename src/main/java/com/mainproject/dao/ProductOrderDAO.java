package com.mainproject.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import com.mainproject.model.ProductOrder;
import com.mainproject.model.Notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProductOrderDAO {

    private static final String COLLECTION = "productOrders";

    // =====================================================
    // CREATE ORDER
    // =====================================================

    public boolean createOrder(ProductOrder order) {

        try {

            if (order == null) {
                return false;
            }

            Firestore db = FirestoreClient.getFirestore();

            DocumentReference document =
                    db.collection(COLLECTION).document();

            order.setOrderId(document.getId());

            if (order.getCreatedAt() == null) {
                order.setCreatedAt(new Date());
            }

            if (order.getStatus() == null
                    || order.getStatus().trim().isEmpty()) {

                order.setStatus("pending");
            }

            if (order.getPaymentStatus() == null || order.getPaymentStatus().trim().isEmpty()) {
                order.setPaymentStatus("pending");
            }

            document.set(order)
                    .get(10, TimeUnit.SECONDS);

            // Create notification for the product owner (farmer).
            if (order.getFarmerEmail() != null && !order.getFarmerEmail().trim().isEmpty()) {
                String buyer = order.getBuyerName();
                if (buyer == null || buyer.trim().isEmpty()) buyer = "A buyer";

                new NotificationDAO().addNotification(
                        new Notification(
                                order.getFarmerEmail().trim(),
                                "New Order Received",
                                buyer + " placed an order for "
                                        + order.getQuantity() + " "
                                        + safe(order.getUnit()) + " of "
                                        + safe(order.getProductName()) + ".",
                                "ORDER"
                        )
                );
            }

            System.out.println("Product order created successfully!");
            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error creating product order:"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // GET ORDER BY ID
    // =====================================================

    public ProductOrder getOrderById(String orderId) {

        try {

            Firestore db = FirestoreClient.getFirestore();

            DocumentSnapshot document =
                    db.collection(COLLECTION)
                            .document(orderId)
                            .get()
                            .get(10, TimeUnit.SECONDS);

            if (!document.exists()) {
                return null;
            }

            ProductOrder order =
                    document.toObject(ProductOrder.class);

            if (order != null) {
                order.setOrderId(document.getId());
            }

            return order;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }


    // =====================================================
    // GET ORDERS FOR FARMER
    // =====================================================

    public List<ProductOrder> getOrdersByFarmer(
            String farmerEmail) {

        List<ProductOrder> list =
                new ArrayList<>();

        try {

            Firestore db = FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "farmerEmail",
                                    farmerEmail
                            )
                            .get();

            QuerySnapshot snapshot =
                    future.get(10, TimeUnit.SECONDS);

            for (QueryDocumentSnapshot document
                    : snapshot.getDocuments()) {

                ProductOrder order =
                        document.toObject(
                                ProductOrder.class
                        );

                order.setOrderId(document.getId());

                list.add(order);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }


    // =====================================================
    // GET ORDERS FOR BUYER
    // =====================================================

    public List<ProductOrder> getOrdersByBuyer(
            String buyerEmail) {

        List<ProductOrder> list =
                new ArrayList<>();

        try {

            Firestore db = FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "buyerEmail",
                                    buyerEmail
                            )
                            .get();

            QuerySnapshot snapshot =
                    future.get(10, TimeUnit.SECONDS);

            for (QueryDocumentSnapshot document
                    : snapshot.getDocuments()) {

                ProductOrder order =
                        document.toObject(
                                ProductOrder.class
                        );

                order.setOrderId(document.getId());

                list.add(order);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }


    // =====================================================
    // UPDATE ORDER STATUS
    // =====================================================

    public boolean updateOrderStatus(
            String orderId,
            String status) {

        try {

            Firestore db =
                    FirestoreClient.getFirestore();

            db.collection(COLLECTION)
                    .document(orderId)
                    .update(
                            "status",
                            status
                    )
                    .get(10, TimeUnit.SECONDS);

            System.out.println(
                    "Order status updated: "
                            + status
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }



    // =====================================================
    // COMPLETE VERIFIED RAZORPAY PAYMENT
    // =====================================================
    public boolean completePayment(String orderId, String paymentId, String razorpayOrderId, String paymentMethod) {
        try {
            ProductOrder order = getOrderById(orderId);
            if (order == null || paymentId == null || paymentId.trim().isEmpty()) return false;
            if ("paid".equalsIgnoreCase(order.getPaymentStatus())) return true;

            Firestore db = FirestoreClient.getFirestore();
            java.util.Map<String, Object> updates = new java.util.HashMap<>();
            updates.put("paymentStatus", "paid");
            updates.put("paymentId", paymentId.trim());
            updates.put("razorpayOrderId", razorpayOrderId == null ? "" : razorpayOrderId.trim());
            updates.put("paymentMethod", paymentMethod == null || paymentMethod.trim().isEmpty() ? "RAZORPAY" : paymentMethod.trim());
            updates.put("paymentDate", new Date());
            db.collection(COLLECTION).document(orderId).update(updates).get(10, TimeUnit.SECONDS);

            if (order.getFarmerEmail() != null && !order.getFarmerEmail().trim().isEmpty()) {
                new NotificationDAO().addNotification(new Notification(
                        order.getFarmerEmail().trim(),
                        "Product Payment Received",
                        "Payment of ₹" + String.format("%.2f", order.getTotalAmount())
                                + " was received for " + safe(order.getProductName()) + ".",
                        "ORDER_PAYMENT"
                ));
            }
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean completePayment(String orderId, String paymentId, String paymentMethod) {
        return completePayment(orderId, paymentId, null, paymentMethod);
    }

    // =====================================================
    // DELETE ORDER
    // =====================================================

    public boolean deleteOrder(String orderId) {

        try {

            Firestore db =
                    FirestoreClient.getFirestore();

            db.collection(COLLECTION)
                    .document(orderId)
                    .delete()
                    .get(10, TimeUnit.SECONDS);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
    private String safe(String value) {
        return value == null || value.trim().isEmpty() ? "product" : value.trim();
    }

}