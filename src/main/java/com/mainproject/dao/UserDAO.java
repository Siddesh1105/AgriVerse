package com.mainproject.dao;

import java.util.HashMap;
import java.util.Map;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.mainproject.config.FirebaseConfig;

public class UserDAO {

    private Firestore db;

    public UserDAO() {
        db = FirebaseConfig.getFirestore();
    }

    // =====================================================
    // SAVE USER
    // =====================================================

    public boolean saveUser(
            String uid,
            String fullName,
            String email,
            String role) {

        try {

            Map<String, Object> userData =new HashMap<>();

            userData.put("fullName",fullName);

            userData.put( "email",email);

            userData.put("role",role);
            db.collection("users")
                    .document(email)
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

    public String getRole(String email) {
        try {

            DocumentSnapshot document =
                    db.collection("users")
                            .document(email)
                            .get()
                            .get();

            if (document.exists()) {

                String role =document.getString("role");
                System.out.println("Role found: " + role);
                return role;
            }
            System.out.println("User not found: " + email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}