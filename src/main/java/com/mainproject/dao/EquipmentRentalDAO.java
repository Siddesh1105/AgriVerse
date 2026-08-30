package com.mainproject.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.mainproject.model.EquipmentRental;
import com.mainproject.model.Notification;

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

            // Notify the equipment owner (farmer) about the new request.
            if (rental.getEquipmentOwnerEmail() != null
                    && !rental.getEquipmentOwnerEmail().trim().isEmpty()) {

                String buyer = rental.getBuyerName();
                if (buyer == null || buyer.trim().isEmpty()) buyer = "A buyer";

                String equipment = rental.getEquipmentName();
                if (equipment == null || equipment.trim().isEmpty()) equipment = "your equipment";

                new NotificationDAO().addNotification(
                        new Notification(
                                rental.getEquipmentOwnerEmail().trim(),
                                "New Equipment Request",
                                buyer + " requested to rent " + equipment + ".",
                                "EQUIPMENT"
                        )
                );
            }

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

            // Read the rental first so the correct buyer can be notified.
            DocumentReference rentalRef = db.collection(COLLECTION)
                    .document(rentalId);

            DocumentSnapshot snapshot = rentalRef
                    .get()
                    .get(10, TimeUnit.SECONDS);

            if (!snapshot.exists()) return false;

            EquipmentRental rental = snapshot.toObject(EquipmentRental.class);
            if (rental != null) rental.setRentalId(snapshot.getId());

            String normalizedStatus = status.trim().toLowerCase();

            rentalRef
                    .update("status", normalizedStatus)
                    .get(10, TimeUnit.SECONDS);

            // Notify the buyer when the farmer accepts or rejects the request.
            if (rental != null
                    && rental.getBuyerEmail() != null
                    && !rental.getBuyerEmail().trim().isEmpty()) {

                String equipment = rental.getEquipmentName();
                if (equipment == null || equipment.trim().isEmpty()) {
                    equipment = "your equipment rental request";
                }

                Notification notification = null;

                if ("accepted".equals(normalizedStatus)) {
                    notification = new Notification(
                            rental.getBuyerEmail().trim(),
                            "Equipment Rental Request Accepted",
                            "Your rental request for " + equipment
                                    + " has been accepted by the equipment owner.",
                            "EQUIPMENT_ACCEPTED"
                    );
                } else if ("rejected".equals(normalizedStatus)) {
                    notification = new Notification(
                            rental.getBuyerEmail().trim(),
                            "Equipment Rental Request Rejected",
                            "Your rental request for " + equipment
                                    + " has been rejected by the equipment owner.",
                            "EQUIPMENT_REJECTED"
                    );
                }

                if (notification != null) {
                    new NotificationDAO().addNotification(notification);
                }
            }

            System.out.println("Rental status updated: " + normalizedStatus);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =====================================================
    // COMPLETE VERIFIED RAZORPAY PAYMENT
    // =====================================================
    public boolean completePayment(String rentalId, String paymentId, String razorpayOrderId, String paymentMethod) {
        try {
            if (isEmpty(rentalId) || isEmpty(paymentId)) return false;
            EquipmentRental rental = getRentalById(rentalId);
            if (rental == null) return false;
            if ("paid".equalsIgnoreCase(rental.getPaymentStatus())) return true;

            Firestore db = FirestoreClient.getFirestore();
            Map<String, Object> updates = new HashMap<>();
            updates.put("paymentStatus", "paid");
            updates.put("paymentId", paymentId.trim());
            updates.put("razorpayOrderId", razorpayOrderId == null ? "" : razorpayOrderId.trim());
            updates.put("paymentMethod", paymentMethod == null || paymentMethod.trim().isEmpty() ? "RAZORPAY" : paymentMethod.trim());
            updates.put("paymentDate", new Date());
            updates.put("status", "active");
            db.collection(COLLECTION).document(rentalId).update(updates).get(10, TimeUnit.SECONDS);

            if (!isEmpty(rental.getEquipmentOwnerEmail())) {
                new NotificationDAO().addNotification(new Notification(
                        rental.getEquipmentOwnerEmail().trim(),
                        "Rental Payment Received",
                        "Payment of ₹" + String.format("%.2f", rental.getTotalAmount())
                                + " was received for " + (isEmpty(rental.getEquipmentName()) ? "your equipment" : rental.getEquipmentName()) + ".",
                        "RENTAL_PAYMENT"
                ));
            }
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean completePayment(String rentalId, String paymentId, String paymentMethod) {
        return completePayment(rentalId, paymentId, null, paymentMethod);
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
