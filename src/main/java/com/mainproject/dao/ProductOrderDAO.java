package com.mainproject.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import com.mainproject.model.ProductOrder;

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

            document.set(order)
                    .get(10, TimeUnit.SECONDS);

            System.out.println(
                    "Product order created successfully!"
            );

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
}