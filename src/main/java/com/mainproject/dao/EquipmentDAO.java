package com.mainproject.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.mainproject.model.Equipment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EquipmentDAO {

    private static final String COLLECTION =
            "equipment";

    // =========================================================
    // ADD EQUIPMENT
    // =========================================================

    public boolean addEquipment(
            Equipment equipment) {

        try {

            if (equipment == null) {

                System.out.println(
                        "Equipment is null."
                );

                return false;
            }

            Firestore db =
                    FirestoreClient.getFirestore();

            DocumentReference document =
                    db.collection(COLLECTION)
                            .document();

            equipment.setEquipmentId(
                    document.getId()
            );

            if (equipment.getCreatedAt() == null) {

                equipment.setCreatedAt(
                        new java.util.Date()
                );
            }

            document.set(equipment)
                    .get(
                            10,
                            TimeUnit.SECONDS
                    );

            System.out.println(
                    "Equipment saved successfully!"
            );

            System.out.println(
                    "Equipment ID: "
                            + document.getId()
            );

            System.out.println(
                    "Equipment Name: "
                            + equipment.getName()
            );

            System.out.println(
                    "Image URL: "
                            + equipment.getImageUrl()
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error saving equipment:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // GET ALL AVAILABLE EQUIPMENT
    // =========================================================

    public List<Equipment> getAllEquipment() {

        List<Equipment> list =
                new ArrayList<>();

        try {

            Firestore db =
                    FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "available",
                                    true
                            )
                            .get();

            QuerySnapshot snapshot =
                    future.get(
                            10,
                            TimeUnit.SECONDS
                    );

            for (
                    QueryDocumentSnapshot document
                    : snapshot.getDocuments()
            ) {

                Equipment equipment =
                        document.toObject(
                                Equipment.class
                        );

                equipment.setEquipmentId(
                        document.getId()
                );

                list.add(equipment);
            }

            System.out.println(
                    "Equipment loaded: "
                            + list.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error loading equipment:"
            );

            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // GET EQUIPMENT BY ID
    // =========================================================

    public Equipment getEquipmentById(
            String equipmentId) {

        try {

            if (equipmentId == null
                    || equipmentId.trim().isEmpty()) {

                return null;
            }

            Firestore db =
                    FirestoreClient.getFirestore();

            DocumentSnapshot document =
                    db.collection(COLLECTION)
                            .document(equipmentId)
                            .get()
                            .get(
                                    10,
                                    TimeUnit.SECONDS
                            );

            if (!document.exists()) {

                return null;
            }

            Equipment equipment =
                    document.toObject(
                            Equipment.class
                    );

            if (equipment != null) {

                equipment.setEquipmentId(
                        document.getId()
                );
            }

            return equipment;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // GET EQUIPMENT BY OWNER
    // =========================================================

    public List<Equipment> getEquipmentByOwner(
            String ownerEmail) {

        List<Equipment> list =
                new ArrayList<>();

        try {

            Firestore db =
                    FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "ownerEmail",
                                    ownerEmail
                            )
                            .get();

            QuerySnapshot snapshot =
                    future.get(
                            10,
                            TimeUnit.SECONDS
                    );

            for (
                    QueryDocumentSnapshot document
                    : snapshot.getDocuments()
            ) {

                Equipment equipment =
                        document.toObject(
                                Equipment.class
                        );

                equipment.setEquipmentId(
                        document.getId()
                );

                list.add(equipment);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // UPDATE EQUIPMENT
    // =========================================================

    public boolean updateEquipment(
            Equipment equipment) {

        try {

            if (equipment == null
                    || equipment.getEquipmentId() == null) {

                return false;
            }

            Firestore db =
                    FirestoreClient.getFirestore();

            db.collection(COLLECTION)
                    .document(
                            equipment.getEquipmentId()
                    )
                    .set(equipment)
                    .get(
                            10,
                            TimeUnit.SECONDS
                    );

            System.out.println(
                    "Equipment updated successfully!"
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // DELETE EQUIPMENT
    // =========================================================

    public boolean deleteEquipment(
            String equipmentId) {

        try {

            if (equipmentId == null
                    || equipmentId.trim().isEmpty()) {

                return false;
            }

            Firestore db =
                    FirestoreClient.getFirestore();

            db.collection(COLLECTION)
                    .document(equipmentId)
                    .delete()
                    .get(
                            10,
                            TimeUnit.SECONDS
                    );

            System.out.println(
                    "Equipment deleted successfully!"
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}