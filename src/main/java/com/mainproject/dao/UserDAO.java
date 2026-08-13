package com.mainproject.dao;

import java.util.HashMap;
import java.util.Map;

import com.google.cloud.firestore.Firestore;
import com.mainproject.controller.config.FirebaseConfig;

public class UserDAO {

    private Firestore db;

    public UserDAO() {
        db = FirebaseConfig.getFirestore();
    }

    public boolean saveUser(
            String uid,
            String fullName,
            String email,
            String role) {

        try {

            Map<String, Object> userData =
                    new HashMap<>();

            userData.put("fullName", fullName);
            userData.put("email", email);
            userData.put("role", role);

            db.collection("users")
                    .document(uid)
                    .set(userData)
                    .get();

            System.out.println(
                    "User saved successfully in Firestore"
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}