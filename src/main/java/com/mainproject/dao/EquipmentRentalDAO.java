package com.mainproject.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.mainproject.model.EquipmentRental;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EquipmentRentalDAO {

    private static final String COLLECTION = "equipmentRentals";

    public boolean createRental(EquipmentRental rental) {
        try {
            if (rental == null) return false;

            Firestore db = FirestoreClient.getFirestore();
            DocumentReference document = db.collection(COLLECTION).document();

            rental.setRentalId(document.getId());

            if (rental.getStatus() == null || rental.getStatus().trim().isEmpty()) {
                rental.setStatus("pending");
            }

            if (rental.getPaymentStatus() == null || rental.getPaymentStatus().trim().isEmpty()) {
                rental.setPaymentStatus("pending");
            }

            if (rental.getCreatedAt() == null) {
                rental.setCreatedAt(new Date());
            }

            document.set(rental).get(10, TimeUnit.SECONDS);
            System.out.println("Rental request created successfully!");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public EquipmentRental getRentalById(String rentalId) {
        try {
            if (isEmpty(rentalId)) return null;

            Firestore db = FirestoreClient.getFirestore();
            DocumentSnapshot document = db.collection(COLLECTION)
                    .document(rentalId)
                    .get()
                    .get(10, TimeUnit.SECONDS);

            if (!document.exists()) return null;

            EquipmentRental rental = document.toObject(EquipmentRental.class);
            if (rental != null) rental.setRentalId(document.getId());
            return rental;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EquipmentRental> getRentalsByFarmer(String farmerEmail) {
        List<EquipmentRental> list = new ArrayList<>();

        try {
            Firestore db = FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION)
                    .whereEqualTo("equipmentOwnerEmail", farmerEmail)
                    .get();

            QuerySnapshot snapshot = future.get(10, TimeUnit.SECONDS);

            for (QueryDocumentSnapshot document : snapshot.getDocuments()) {
                EquipmentRental rental = document.toObject(EquipmentRental.class);
                rental.setRentalId(document.getId());
                list.add(rental);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<EquipmentRental> getRentalsByBuyer(String buyerEmail) {
        List<EquipmentRental> list = new ArrayList<>();

        try {
            Firestore db = FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> future = db.collection(COLLECTION)
                    .whereEqualTo("buyerEmail", buyerEmail)
                    .get();

            QuerySnapshot snapshot = future.get(10, TimeUnit.SECONDS);

            for (QueryDocumentSnapshot document : snapshot.getDocuments()) {
                EquipmentRental rental = document.toObject(EquipmentRental.class);
                rental.setRentalId(document.getId());
                list.add(rental);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateRentalStatus(String rentalId, String status) {
        try {
            if (isEmpty(rentalId) || isEmpty(status)) return false;

            Firestore db = FirestoreClient.getFirestore();

            db.collection(COLLECTION)
                    .document(rentalId)
                    .update("status", status)
                    .get(10, TimeUnit.SECONDS);

            System.out.println("Rental status updated: " + status);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Demo payment: records payment in Firestore.
    public boolean completePayment(String rentalId, String paymentMethod) {
        try {
            if (isEmpty(rentalId) || isEmpty(paymentMethod)) return false;

            Firestore db = FirestoreClient.getFirestore();

            String paymentId = "AGRI-" + System.currentTimeMillis();

            Map<String, Object> updates = new HashMap<>();
            updates.put("paymentStatus", "paid");
            updates.put("paymentId", paymentId);
            updates.put("paymentMethod", paymentMethod);
            updates.put("paymentDate", new Date());
            updates.put("status", "active");

            db.collection(COLLECTION)
                    .document(rentalId)
                    .update(updates)
                    .get(10, TimeUnit.SECONDS);

            System.out.println("Payment completed successfully!");
            System.out.println("Payment ID: " + paymentId);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRental(String rentalId) {
        try {
            if (isEmpty(rentalId)) return false;

            Firestore db = FirestoreClient.getFirestore();

            db.collection(COLLECTION)
                    .document(rentalId)
                    .delete()
                    .get(10, TimeUnit.SECONDS);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
