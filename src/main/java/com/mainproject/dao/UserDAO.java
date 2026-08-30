package com.mainproject.dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import com.mainproject.config.FirebaseConfig;
import com.mainproject.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {

    private final Firestore db;

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

            if (user.getEmail() == null ||
                    user.getEmail().trim().isEmpty()) {

                System.out.println(
                        "Cannot save user: email is empty."
                );

                return false;
            }

            String email =
                    user.getEmail().trim();

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
                    email
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

            // =================================================
            // LOCATION
            // =================================================

            userData.put(
                    "city",
                    user.getCity() == null
                            ? ""
                            : user.getCity().trim()
            );

            userData.put(
                    "district",
                    user.getDistrict() == null
                            ? ""
                            : user.getDistrict().trim()
            );

            userData.put(
                    "state",
                    user.getState() == null
                            ? ""
                            : user.getState().trim()
            );

            // =================================================
            // SAVE TO FIRESTORE
            // EMAIL = DOCUMENT ID
            // =================================================

            db.collection("users")
                    .document(email)
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
                            + email
            );

            System.out.println(
                    "User Role: "
                            + user.getRole()
            );

            System.out.println(
                    "City: "
                            + user.getCity()
            );

            System.out.println(
                    "District: "
                            + user.getDistrict()
            );

            System.out.println(
                    "State: "
                            + user.getState()
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

    public User getUserByEmail(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

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

            if (!document.exists()) {

                System.out.println(
                        "User not found: " + email
                );

                return null;
            }

            User user =
                    document.toObject(
                            User.class
                    );

            if (user != null) {

                // Always restore email from document ID
                user.setEmail(
                        document.getId()
                );

                System.out.println(
                        "===================================="
                );

                System.out.println(
                        "User found: "
                                + user.getEmail()
                );

                System.out.println(
                        "User Name: "
                                + user.getFullName()
                );

                System.out.println(
                        "User Role: "
                                + user.getRole()
                );

                System.out.println(
                        "City: "
                                + user.getCity()
                );

                System.out.println(
                        "District: "
                                + user.getDistrict()
                );

                System.out.println(
                        "State: "
                                + user.getState()
                );

                System.out.println(
                        "===================================="
                );
            }

            return user;

        } catch (Exception e) {

            System.out.println(
                    "Error getting user from Firestore:"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // GET ALL USERS
    // =====================================================

    public List<User> getAllUsers() {

        List<User> users =
                new ArrayList<>();

        try {

            List<QueryDocumentSnapshot> documents =
                    db.collection("users")
                            .get()
                            .get()
                            .getDocuments();

            for (QueryDocumentSnapshot document : documents) {

                User user =
                        document.toObject(
                                User.class
                        );

                if (user == null) {
                    continue;
                }

                // Restore email from Firestore document ID
                user.setEmail(
                        document.getId()
                );

                users.add(user);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error getting all users:"
            );

            e.printStackTrace();
        }

        return users;
    }

    // =====================================================
    // GET USERS BY ROLE
    // =====================================================

    public List<User> getUsersByRole(
            String role) {

        List<User> users =
                new ArrayList<>();

        try {

            if (role == null ||
                    role.trim().isEmpty()) {

                return users;
            }

            List<QueryDocumentSnapshot> documents =
                    db.collection("users")
                            .whereEqualTo(
                                    "role",
                                    role.trim()
                            )
                            .get()
                            .get()
                            .getDocuments();

            for (QueryDocumentSnapshot document : documents) {

                User user =
                        document.toObject(
                                User.class
                        );

                if (user == null) {
                    continue;
                }

                // Restore email from Firestore document ID
                user.setEmail(
                        document.getId()
                );

                users.add(user);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error getting users by role:"
            );

            e.printStackTrace();
        }

        return users;
    }

    // =====================================================
    // GET ALL FARMERS
    // =====================================================

    public List<User> getAllFarmers() {

        return getUsersByRole(
                "Farmer"
        );
    }

    // =====================================================
    // GET ALL BUYERS
    // =====================================================

    public List<User> getAllBuyers() {

        return getUsersByRole(
                "Buyer"
        );
    }

    // =====================================================
    // UPDATE PROFILE
    // =====================================================

    public boolean updateProfile(
            User user) {

        try {

            if (user == null) {

                System.out.println(
                        "Cannot update null user."
                );

                return false;
            }

            if (user.getEmail() == null ||
                    user.getEmail().trim().isEmpty()) {

                System.out.println(
                        "Cannot update profile: email is empty."
                );

                return false;
            }

            Map<String, Object> updates =
                    new HashMap<>();

            // =================================================
            // PROFILE INFORMATION
            // =================================================

            updates.put(
                    "fullName",
                    user.getFullName()
            );

            updates.put(
                    "mobileNumber",
                    user.getMobileNumber()
            );

            updates.put(
                    "gender",
                    user.getGender()
            );

            // =================================================
            // LOCATION
            // =================================================

            updates.put(
                    "city",
                    user.getCity() == null
                            ? ""
                            : user.getCity().trim()
            );

            updates.put(
                    "district",
                    user.getDistrict() == null
                            ? ""
                            : user.getDistrict().trim()
            );

            updates.put(
                    "state",
                    user.getState() == null
                            ? ""
                            : user.getState().trim()
            );

            db.collection("users")
                    .document(
                            user.getEmail().trim()
                    )
                    .update(updates)
                    .get();

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Profile updated successfully!"
            );

            System.out.println(
                    "New City: "
                            + user.getCity()
            );

            System.out.println(
                    "New District: "
                            + user.getDistrict()
            );

            System.out.println(
                    "New State: "
                            + user.getState()
            );

            System.out.println(
                    "===================================="
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

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }

            if (imageUrl == null ||
                    imageUrl.trim().isEmpty()) {

                return false;
            }

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "profileImageUrl",
                    imageUrl.trim()
            );

            db.collection("users")
                    .document(email.trim())
                    .update(updates)
                    .get();

            System.out.println(
                    "Profile image updated successfully!"
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating profile image:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // UPDATE USER LOCATION
    // =====================================================

    public boolean updateLocation(
            String email,
            String state,
            String district,
            String city) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                System.out.println(
                        "Cannot update location: email is empty."
                );

                return false;
            }

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "state",
                    state == null
                            ? ""
                            : state.trim()
            );

            updates.put(
                    "district",
                    district == null
                            ? ""
                            : district.trim()
            );

            updates.put(
                    "city",
                    city == null
                            ? ""
                            : city.trim()
            );

            db.collection("users")
                    .document(email.trim())
                    .update(updates)
                    .get();

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "User location updated successfully!"
            );

            System.out.println(
                    "City: " + city
            );

            System.out.println(
                    "District: " + district
            );

            System.out.println(
                    "State: " + state
            );

            System.out.println(
                    "===================================="
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating user location:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // UPDATE FARMER VERIFICATION (ADMIN)
    // =====================================================

    public boolean updateFarmerVerification(String email, String verificationStatus, String rejectionReason) {
        try {
            if (email == null || email.trim().isEmpty()) return false;
            java.util.Map<String, Object> updates = new java.util.HashMap<>();
            updates.put("verificationStatus", verificationStatus == null ? "Pending" : verificationStatus);
            updates.put("rejectionReason", rejectionReason == null ? "" : rejectionReason);
            db.collection("users").document(email.trim()).update(updates).get();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // =====================================================
    // CHECK USER EXISTS
    // =====================================================

    public boolean userExists(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

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

    // =====================================================
    // GET FARMERS EXCLUDING CURRENT USER
    // =====================================================

    public List<User> getFarmersExcept(
            String email) {

        List<User> farmers =
                getAllFarmers();

        if (email == null ||
                email.trim().isEmpty()) {

            return farmers;
        }

        String currentEmail =
                email.trim();

        farmers.removeIf(
                user ->
                        user != null &&
                                user.getEmail() != null &&
                                user.getEmail()
                                        .equalsIgnoreCase(
                                                currentEmail
                                        )
        );

        return farmers;
    }
}