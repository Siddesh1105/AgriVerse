package com.mainproject.dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.mainproject.config.FirebaseConfig;
import com.mainproject.model.BuyerCartItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BuyerCartDAO {

    private static final String COLLECTION = "buyerCartItems";

    private final Firestore db;

    public BuyerCartDAO() {
        db = FirebaseConfig.getFirestore();
    }

    private String createDocumentId(String buyerEmail, String productId) {
        return buyerEmail
                .replace(".", "_")
                .replace("@", "_")
                .replace("/", "_")
                + "_" + productId;
    }

    public boolean addToCart(BuyerCartItem item) {

        try {
            if (item == null ||
                    item.getBuyerEmail() == null ||
                    item.getBuyerEmail().trim().isEmpty() ||
                    item.getProductId() == null ||
                    item.getProductId().trim().isEmpty()) {
                return false;
            }

            if (item.getQuantity() <= 0) {
                item.setQuantity(1);
            }

            String documentId = createDocumentId(
                    item.getBuyerEmail().trim(),
                    item.getProductId().trim()
            );

            DocumentReference document =
                    db.collection(COLLECTION).document(documentId);

            item.setCartItemId(documentId);
            item.calculateTotal();
            item.setUpdatedAt(new Date());

            document.set(item).get();

            return true;

        } catch (Exception e) {
            System.out.println("Error adding product to buyer cart:");
            e.printStackTrace();
            return false;
        }
    }

    public List<BuyerCartItem> getCartItems(String buyerEmail) {

        List<BuyerCartItem> items = new ArrayList<>();

        try {
            if (buyerEmail == null || buyerEmail.trim().isEmpty()) {
                return items;
            }

            QuerySnapshot snapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo("buyerEmail", buyerEmail.trim())
                            .get()
                            .get();

            for (QueryDocumentSnapshot document : snapshot.getDocuments()) {

                BuyerCartItem item =
                        document.toObject(BuyerCartItem.class);

                if (item != null) {
                    item.setCartItemId(document.getId());
                    item.calculateTotal();
                    items.add(item);
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading buyer cart:");
            e.printStackTrace();
        }

        return items;
    }

    public boolean updateQuantity(
            String cartItemId,
            double quantity) {

        try {
            if (cartItemId == null ||
                    cartItemId.trim().isEmpty() ||
                    quantity <= 0) {
                return false;
            }

            DocumentReference document =
                    db.collection(COLLECTION).document(cartItemId);

            DocumentSnapshot snapshot =
                    document.get().get();

            if (!snapshot.exists()) {
                return false;
            }

            BuyerCartItem item =
                    snapshot.toObject(BuyerCartItem.class);

            if (item == null) {
                return false;
            }

            item.setQuantity(quantity);
            item.calculateTotal();
            item.setUpdatedAt(new Date());

            document.set(item).get();

            return true;

        } catch (Exception e) {
            System.out.println("Error updating buyer cart quantity:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFromCart(String cartItemId) {

        try {
            if (cartItemId == null ||
                    cartItemId.trim().isEmpty()) {
                return false;
            }

            db.collection(COLLECTION)
                    .document(cartItemId)
                    .delete()
                    .get();

            return true;

        } catch (Exception e) {
            System.out.println("Error removing buyer cart item:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearCart(String buyerEmail) {

        try {
            List<BuyerCartItem> items = getCartItems(buyerEmail);

            for (BuyerCartItem item : items) {
                removeFromCart(item.getCartItemId());
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error clearing buyer cart:");
            e.printStackTrace();
            return false;
        }
    }
}
