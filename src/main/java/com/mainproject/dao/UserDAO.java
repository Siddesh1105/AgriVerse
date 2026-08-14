package com.mainproject.dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.mainproject.config.FirebaseConfig;
import com.mainproject.model.User;

public class UserDAO {

    private Firestore db;

    public UserDAO() {

        db = FirebaseConfig.getFirestore();
    }

    // =====================================================
    // SAVE USER
    // =====================================================

    public boolean saveUser(User user) {

        try {

            db.collection("users")
                    .document(user.getEmail())
                    .set(user)
                    .get();

            System.out.println(
                    "User saved successfully in Firestore"
            );

            System.out.println(
                    "User Email: " + user.getEmail()
            );

            System.out.println(
                    "User Role: " + user.getRole()
            );

            System.out.println(
                    "Firebase UID: " + user.getUid()
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET USER BY EMAIL
    // =====================================================

    public User getUserByEmail(String email) {

        try {

            DocumentSnapshot document =
                    db.collection("users")
                            .document(email)
                            .get()
                            .get();

            if (document.exists()) {

                User user =
                        document.toObject(
                                User.class
                        );

                System.out.println(
                        "User found: " + email
                );

                if (user != null) {

                    System.out.println(
                            "User Role: "
                                    + user.getRole()
                    );
                }

                return user;
            }

            System.out.println(
                    "User not found: " + email
            );

            return null;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // CHECK USER EXISTS
    // =====================================================

    public boolean userExists(String email) {

        try {

            DocumentSnapshot document =
                    db.collection("users")
                            .document(email)
                            .get()
                            .get();

            return document.exists();

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}