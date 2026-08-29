package com.mainproject.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.mainproject.model.CartItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CartDAO {

    private static final String COLLECTION =
            "cartItems";

    // =====================================================
    // ADD TO CART
    // =====================================================

    public boolean addToCart(
            CartItem item) {

        try {

            if (item == null) {

                System.out.println(
                        "Cart item is null.");

                return false;
            }

            if (item.getFarmerEmail() == null
                    || item.getFarmerEmail()
                    .trim()
                    .isEmpty()) {

                System.out.println(
                        "Farmer email is empty.");

                return false;
            }

            Firestore db =
                    FirestoreClient.getFirestore();

            /*
             * Use equipment ID + farmer email
             * so the same equipment does not
             * create duplicate cart records.
             */

            String documentId =
                    item.getFarmerEmail()
                            .replace(".", "_")
                            .replace("@", "_")
                            + "_"
                            + item.getEquipmentId();

            DocumentReference document =
                    db.collection(COLLECTION)
                            .document(documentId);

            item.setCartItemId(
                    documentId);

            item.setRentalDays(
                    item.getRentalDays() <= 0
                            ? 1
                            : item.getRentalDays());

            item.calculateTotal();

            item.setUpdatedAt(
                    new Date());

            document.set(item)
                    .get(
                            10,
                            TimeUnit.SECONDS);

            System.out.println(
                    "================================");

            System.out.println(
                    "Cart item saved successfully.");

            System.out.println(
                    "Cart ID: "
                            + documentId);

            System.out.println(
                    "Equipment: "
                            + item.getEquipmentName());

            System.out.println(
                    "Rental Days: "
                            + item.getRentalDays());

            System.out.println(
                    "Price Per Day: ₹"
                            + item.getPricePerDay());

            System.out.println(
                    "Total Price: ₹"
                            + item.getTotalPrice());

            System.out.println(
                    "================================");

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error adding item to cart:");

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET CART BY FARMER
    // =====================================================

    public List<CartItem> getCartItems(
            String farmerEmail) {

        List<CartItem> list =
                new ArrayList<>();

        try {

            if (farmerEmail == null
                    || farmerEmail
                    .trim()
                    .isEmpty()) {

                return list;
            }

            Firestore db =
                    FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "farmerEmail",
                                    farmerEmail)
                            .get();

            QuerySnapshot snapshot =
                    future.get(
                            10,
                            TimeUnit.SECONDS);

            for (
                    QueryDocumentSnapshot document
                    : snapshot.getDocuments()) {

                CartItem item =
                        document.toObject(
                                CartItem.class);

                if (item != null) {

                    item.setCartItemId(
                            document.getId());

                    /*
                     * Recalculate to make sure
                     * the displayed total is correct.
                     */

                    item.calculateTotal();

                    list.add(item);
                }
            }

            System.out.println(
                    "Cart items loaded: "
                            + list.size());

        } catch (Exception e) {

            System.out.println(
                    "Error loading cart:");

            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // UPDATE RENTAL DAYS
    // =====================================================

    public boolean updateRentalDays(
            String cartItemId,
            int rentalDays) {

        try {

            if (cartItemId == null
                    || cartItemId
                    .trim()
                    .isEmpty()) {

                return false;
            }

            if (rentalDays < 1) {

                rentalDays = 1;
            }

            Firestore db =
                    FirestoreClient.getFirestore();

            DocumentReference document =
                    db.collection(COLLECTION)
                            .document(cartItemId);

            CartItem item =
                    document
                            .get()
                            .get(
                                    10,
                                    TimeUnit.SECONDS)
                            .toObject(
                                    CartItem.class);

            if (item == null) {

                return false;
            }

            item.setRentalDays(
                    rentalDays);

            item.calculateTotal();

            item.setUpdatedAt(
                    new Date());

            document.set(item)
                    .get(
                            10,
                            TimeUnit.SECONDS);

            System.out.println(
                    "Rental days updated.");

            System.out.println(
                    "Rental Days: "
                            + rentalDays);

            System.out.println(
                    "Total: ₹"
                            + item.getTotalPrice());

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating rental days:");

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // REMOVE FROM CART
    // =====================================================

    public boolean removeFromCart(
            String cartItemId) {

        try {

            if (cartItemId == null
                    || cartItemId
                    .trim()
                    .isEmpty()) {

                return false;
            }

            Firestore db =
                    FirestoreClient.getFirestore();

            db.collection(COLLECTION)
                    .document(cartItemId)
                    .delete()
                    .get(
                            10,
                            TimeUnit.SECONDS);

            System.out.println(
                    "Cart item removed.");

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error removing cart item:");

            e.printStackTrace();

            return false;
        }
    }
}