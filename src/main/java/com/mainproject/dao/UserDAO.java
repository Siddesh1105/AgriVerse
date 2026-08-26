package com.mainproject.dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.mainproject.config.FirebaseConfig;
import com.mainproject.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserDAO {

    private Firestore db;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public UserDAO() {

        db = FirebaseConfig.getFirestore();
    }

    // =====================================================
    // SAVE USER
    // =====================================================

    public boolean saveUser(User user) {

        try {

            if (user == null) {

                System.out.println(
                        "Cannot save null user."
                );

                return false;
            }

            Map<String, Object> userData =
                    new HashMap<>();

            userData.put(
                    "uid",
                    user.getUid()
            );

            userData.put(
                    "fullName",
                    user.getFullName()
            );

            userData.put(
                    "email",
                    user.getEmail()
            );

            userData.put(
                    "mobileNumber",
                    user.getMobileNumber()
            );

            userData.put(
                    "gender",
                    user.getGender()
            );

            userData.put(
                    "role",
                    user.getRole()
            );

            userData.put(
                    "profileImageUrl",
                    user.getProfileImageUrl()
            );

            // Email is document ID
            db.collection("users")
                    .document(user.getEmail())
                    .set(userData)
                    .get();

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "User saved successfully in Firestore"
            );

            System.out.println(
                    "Firebase UID: "
                            + user.getUid()
            );

            System.out.println(
                    "User Name: "
                            + user.getFullName()
            );

            System.out.println(
                    "User Email: "
                            + user.getEmail()
            );

            System.out.println(
                    "Mobile Number: "
                            + user.getMobileNumber()
            );

            System.out.println(
                    "Gender: "
                            + user.getGender()
            );

            System.out.println(
                    "Role: "
                            + user.getRole()
            );

            System.out.println(
                    "Profile Image: "
                            + user.getProfileImageUrl()
            );

            System.out.println(
                    "===================================="
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error saving user to Firestore:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET USER BY EMAIL
    // =====================================================

    public User getUserByEmail(String email) {

        try {

            if (email == null
                    || email.trim().isEmpty()) {

                System.out.println(
                        "Email is empty."
                );

                return null;
            }

            email = email.trim();

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
                        "===================================="
                );

                System.out.println(
                        "User found: " + email
                );

                if (user != null) {

                    System.out.println(
                            "User UID: "
                                    + user.getUid()
                    );

                    System.out.println(
                            "User Name: "
                                    + user.getFullName()
                    );

                    System.out.println(
                            "User Email: "
                                    + user.getEmail()
                    );

                    System.out.println(
                            "User Mobile: "
                                    + user.getMobileNumber()
                    );

                    System.out.println(
                            "User Gender: "
                                    + user.getGender()
                    );

                    System.out.println(
                            "User Role: "
                                    + user.getRole()
                    );

                    System.out.println(
                            "Profile Image: "
                                    + user.getProfileImageUrl()
                    );
                }

                System.out.println(
                        "===================================="
                );

                return user;
            }

            System.out.println(
                    "User not found: "
                            + email
            );

            return null;

        } catch (Exception e) {

            System.out.println(
                    "Error getting user from Firestore:"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // UPDATE PROFILE
    // Only name and mobile are changed
    // =====================================================

    public boolean updateProfile(User user) {

        try {

            if (user == null) {

                return false;
            }

            if (user.getEmail() == null
                    || user.getEmail()
                            .trim()
                            .isEmpty()) {

                System.out.println(
                        "Cannot update profile: email is empty."
                );

                return false;
            }

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "fullName",
                    user.getFullName()
            );

            updates.put(
                    "mobileNumber",
                    user.getMobileNumber()
            );

            db.collection("users")
                    .document(
                            user.getEmail()
                    )
                    .update(updates)
                    .get();

            System.out.println(
                    "Profile updated successfully!"
            );

            System.out.println(
                    "Name: "
                            + user.getFullName()
            );

            System.out.println(
                    "Mobile: "
                            + user.getMobileNumber()
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating profile:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // UPDATE PROFILE IMAGE
    // =====================================================

    public boolean updateProfileImage(
            String email,
            String imageUrl) {

        try {

            if (email == null
                    || email.trim().isEmpty()) {

                System.out.println(
                        "Cannot update image: email is empty."
                );

                return false;
            }

            if (imageUrl == null
                    || imageUrl.trim().isEmpty()) {

                System.out.println(
                        "Cannot update image: URL is empty."
                );

                return false;
            }

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "profileImageUrl",
                    imageUrl
            );

            db.collection("users")
                    .document(email.trim())
                    .update(updates)
                    .get();

            System.out.println(
                    "Profile image URL saved to Firestore."
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error saving profile image:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // CHECK USER EXISTS
    // =====================================================

    public boolean userExists(String email) {

        try {

            if (email == null
                    || email.trim().isEmpty()) {

                return false;
            }

            DocumentSnapshot document =
                    db.collection("users")
                            .document(email.trim())
                            .get()
                            .get();

            return document.exists();

        } catch (Exception e) {

            System.out.println(
                    "Error checking user:"
            );

            e.printStackTrace();

            return false;
        }
    }
}