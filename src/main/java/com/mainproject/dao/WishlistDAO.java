package com.mainproject.dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.mainproject.config.FirebaseConfig;
import com.mainproject.model.WishlistItem;

import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {

    private static final String COLLECTION =
            "wishlistItems";

    private final Firestore db;

    public WishlistDAO() {
        db = FirebaseConfig.getFirestore();
    }

    // =====================================================
    // ADD TO WISHLIST
    // =====================================================

    public boolean addToWishlist(
            WishlistItem item) {

        try {

            if (item == null) {
                return false;
            }

            if (item.getBuyerEmail() == null ||
                    item.getBuyerEmail()
                            .trim()
                            .isEmpty()) {

                System.out.println(
                        "Buyer email is empty."
                );

                return false;
            }

            if (item.getProductId() == null ||
                    item.getProductId()
                            .trim()
                            .isEmpty()) {

                System.out.println(
                        "Product ID is empty."
                );

                return false;
            }

            /*
             * One buyer + one product
             * = one wishlist record.
             *
             * This prevents duplicate
             * wishlist entries.
             */

            String documentId =
                    item.getBuyerEmail()
                            .replace(".", "_")
                            .replace("@", "_")
                            + "_"
                            + item.getProductId();

            DocumentReference document =
                    db.collection(COLLECTION)
                            .document(documentId);

            item.setWishlistId(
                    documentId
            );

            document.set(item)
                    .get();

            System.out.println(
                    "Product added to wishlist: "
                            + item.getProductName()
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error adding to wishlist:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET BUYER WISHLIST
    // =====================================================

    public List<WishlistItem> getWishlist(
            String buyerEmail) {

        List<WishlistItem> list =
                new ArrayList<>();

        try {

            if (buyerEmail == null ||
                    buyerEmail.trim().isEmpty()) {

                return list;
            }

            QuerySnapshot snapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "buyerEmail",
                                    buyerEmail.trim()
                            )
                            .get()
                            .get();

            for (
                    QueryDocumentSnapshot document
                    : snapshot.getDocuments()) {

                WishlistItem item =
                        document.toObject(
                                WishlistItem.class
                        );

                if (item != null) {

                    item.setWishlistId(
                            document.getId()
                    );

                    list.add(item);
                }
            }

            System.out.println(
                    "Wishlist items loaded: "
                            + list.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error loading wishlist:"
            );

            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // CHECK IF PRODUCT IS IN WISHLIST
    // =====================================================

    public boolean isInWishlist(
            String buyerEmail,
            String productId) {

        try {

            if (buyerEmail == null ||
                    buyerEmail.trim().isEmpty() ||
                    productId == null ||
                    productId.trim().isEmpty()) {

                return false;
            }

            String documentId =
                    buyerEmail.trim()
                            .replace(".", "_")
                            .replace("@", "_")
                            + "_"
                            + productId;

            return db.collection(COLLECTION)
                    .document(documentId)
                    .get()
                    .get()
                    .exists();

        } catch (Exception e) {

            System.out.println(
                    "Error checking wishlist:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // REMOVE FROM WISHLIST
    // =====================================================

    public boolean removeFromWishlist(
            String wishlistId) {

        try {

            if (wishlistId == null ||
                    wishlistId.trim().isEmpty()) {

                return false;
            }

            db.collection(COLLECTION)
                    .document(wishlistId)
                    .delete()
                    .get();

            System.out.println(
                    "Wishlist item removed."
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error removing wishlist item:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // REMOVE PRODUCT FOR BUYER
    // =====================================================

    public boolean removeProductFromWishlist(
            String buyerEmail,
            String productId) {

        try {

            if (buyerEmail == null ||
                    buyerEmail.trim().isEmpty() ||
                    productId == null ||
                    productId.trim().isEmpty()) {

                return false;
            }

            String documentId =
                    buyerEmail.trim()
                            .replace(".", "_")
                            .replace("@", "_")
                            + "_"
                            + productId;

            db.collection(COLLECTION)
                    .document(documentId)
                    .delete()
                    .get();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error removing product from wishlist:"
            );

            e.printStackTrace();

            return false;
        }
    }
}