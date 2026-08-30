package com.mainproject.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.mainproject.config.FirebaseConfig;
import com.mainproject.model.User;

public class UserDAO {

    private final Firestore db;

    public UserDAO() {
        this.db = FirebaseConfig.getFirestore();
    }

    // =====================================================
    // SAVE USER
    // =====================================================

    public boolean saveUser(User user) {

        try {

            if (user == null) {
                return false;
            }

            if (user.getEmail() == null ||
                    user.getEmail().trim().isEmpty()) {

                return false;
            }

            if (user.getUid() == null ||
                    user.getUid().trim().isEmpty()) {

                user.setUid(
                        user.getEmail().trim()
                );
            }

            /*
             * Every new Farmer is Pending unless a
             * verification status has already been set.
             */
            if ("Farmer".equalsIgnoreCase(
                    user.getRole())) {

                if (user.getVerificationStatus() == null ||
                        user.getVerificationStatus()
                                .trim()
                                .isEmpty()) {

                    user.setVerificationStatus(
                            "Pending"
                    );
                }

                if (user.getRejectionReason() == null) {
                    user.setRejectionReason("");
                }
            }

            DocumentReference ref =
                    db.collection("users")
                            .document(
                                    user.getEmail().trim()
                            );

            ref.set(user).get();

            System.out.println(
                    "User saved successfully: "
                            + user.getEmail()
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error saving user: "
                            + e.getMessage()
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

                return null;
            }

            DocumentSnapshot snapshot =
                    db.collection("users")
                            .document(
                                    email.trim()
                            )
                            .get()
                            .get();

            if (snapshot == null ||
                    !snapshot.exists()) {

                return null;
            }

            return snapshot.toObject(
                    User.class
            );

        } catch (Exception e) {

            System.out.println(
                    "Error fetching user by email: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // UPDATE PROFILE
    // =====================================================

    public boolean updateProfile(
            User user) {

        try {

            if (user == null ||
                    user.getEmail() == null ||
                    user.getEmail()
                            .trim()
                            .isEmpty()) {

                return false;
            }

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "uid",
                    user.getUid()
            );

            updates.put(
                    "fullName",
                    user.getFullName()
            );

            updates.put(
                    "email",
                    user.getEmail()
            );

            updates.put(
                    "mobileNumber",
                    user.getMobileNumber()
            );

            updates.put(
                    "gender",
                    user.getGender()
            );

            updates.put(
                    "role",
                    user.getRole()
            );

            updates.put(
                    "profileImageUrl",
                    user.getProfileImageUrl() == null
                            ? ""
                            : user.getProfileImageUrl()
            );

            /*
             * Do NOT overwrite an existing verification
             * status with Pending when a user updates
             * their profile.
             */
            if ("Farmer".equalsIgnoreCase(
                    user.getRole())) {

                if (user.getVerificationStatus() != null &&
                        !user.getVerificationStatus()
                                .trim()
                                .isEmpty()) {

                    updates.put(
                            "verificationStatus",
                            user.getVerificationStatus()
                    );
                }

                updates.put(
                        "rejectionReason",
                        user.getRejectionReason() == null
                                ? ""
                                : user.getRejectionReason()
                );
            }

            db.collection("users")
                    .document(
                            user.getEmail().trim()
                    )
                    .update(updates)
                    .get();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating profile: "
                            + e.getMessage()
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

            db.collection("users")
                    .document(
                            email.trim()
                    )
                    .update(
                            "profileImageUrl",
                            imageUrl == null
                                    ? ""
                                    : imageUrl
                    )
                    .get();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating profile image: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // USER EXISTS
    // =====================================================

    public boolean userExists(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }

            return db.collection("users")
                    .document(
                            email.trim()
                    )
                    .get()
                    .get()
                    .exists();

        } catch (Exception e) {

            System.out.println(
                    "Error checking user existence: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET ALL USERS
    // =====================================================

    public List<User> getAllUsers() {

        try {

            QuerySnapshot snapshot =
                    db.collection("users")
                            .get()
                            .get();

            List<User> users =
                    new ArrayList<>();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                User user =
                        document.toObject(
                                User.class
                        );

                if (user != null) {
                    users.add(user);
                }
            }

            return users;

        } catch (Exception e) {

            System.out.println(
                    "Error fetching all users: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    // =====================================================
    // GET ALL FARMERS
    // =====================================================

    public List<User> getAllFarmers() {

        List<User> allUsers =
                getAllUsers();

        List<User> farmers =
                new ArrayList<>();

        for (User user : allUsers) {

            if (user != null &&
                    "Farmer".equalsIgnoreCase(
                            user.getRole())) {

                /*
                 * Existing farmers created before the
                 * verification system was added may not
                 * have verificationStatus in Firestore.
                 *
                 * Treat them as Pending.
                 */
                if (user.getVerificationStatus() == null ||
                        user.getVerificationStatus()
                                .trim()
                                .isEmpty()) {

                    user.setVerificationStatus(
                            "Pending"
                    );
                }

                if (user.getRejectionReason() == null) {
                    user.setRejectionReason("");
                }

                farmers.add(user);
            }
        }

        return farmers;
    }

    // =====================================================
    // UPDATE FARMER VERIFICATION
    // =====================================================

    public boolean updateFarmerVerification(
            String email,
            String status,
            String rejectionReason) {

        try {

            // ---------------------------------------------
            // VALIDATE EMAIL
            // ---------------------------------------------

            if (email == null ||
                    email.trim().isEmpty()) {

                System.out.println(
                        "Verification update failed:"
                                + " email is empty."
                );

                return false;
            }

            // ---------------------------------------------
            // VALIDATE STATUS
            // ---------------------------------------------

            if (status == null ||
                    status.trim().isEmpty()) {

                System.out.println(
                        "Verification update failed:"
                                + " status is empty."
                );

                return false;
            }

            String cleanEmail =
                    email.trim();

            String cleanStatus =
                    status.trim();

            String cleanReason =
                    rejectionReason == null
                            ? ""
                            : rejectionReason.trim();

            // ---------------------------------------------
            // GET FARMER DOCUMENT
            // ---------------------------------------------

            DocumentReference farmerRef =
                    db.collection("users")
                            .document(
                                    cleanEmail
                            );

            DocumentSnapshot existing =
                    farmerRef
                            .get()
                            .get();

            if (existing == null ||
                    !existing.exists()) {

                System.out.println(
                        "Verification update failed:"
                                + " farmer document does not exist."
                );

                return false;
            }

            // ---------------------------------------------
            // MAKE SURE THIS IS A FARMER
            // ---------------------------------------------

            String role =
                    existing.getString("role");

            if (role == null ||
                    !"Farmer".equalsIgnoreCase(
                            role.trim()
                    )) {

                System.out.println(
                        "Verification update failed:"
                                + " user is not a Farmer."
                );

                return false;
            }

            // ---------------------------------------------
            // PREPARE UPDATE
            // ---------------------------------------------

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "verificationStatus",
                    cleanStatus
            );

            updates.put(
                    "rejectionReason",
                    cleanReason
            );

            // ---------------------------------------------
            // UPDATE FIRESTORE
            // ---------------------------------------------

            farmerRef
                    .update(updates)
                    .get();

            // ---------------------------------------------
            // READ AGAIN FROM FIRESTORE
            // ---------------------------------------------

            DocumentSnapshot savedSnapshot =
                    farmerRef
                            .get()
                            .get();

            if (savedSnapshot == null ||
                    !savedSnapshot.exists()) {

                System.out.println(
                        "Verification update failed:"
                                + " document could not be read after update."
                );

                return false;
            }

            String savedStatus =
                    savedSnapshot.getString(
                            "verificationStatus"
                    );

            String savedReason =
                    savedSnapshot.getString(
                            "rejectionReason"
                    );

            // ---------------------------------------------
            // CONSOLE DEBUG
            // ---------------------------------------------

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "       FARMER VERIFICATION UPDATE"
            );

            System.out.println(
                    "Email: "
                            + cleanEmail
            );

            System.out.println(
                    "Requested Status: "
                            + cleanStatus
            );

            System.out.println(
                    "Saved Status: "
                            + savedStatus
            );

            System.out.println(
                    "Saved Reason: "
                            + savedReason
            );

            System.out.println(
                    "=========================================="
            );

            // ---------------------------------------------
            // VERIFY STATUS
            // ---------------------------------------------

            if (savedStatus == null ||
                    !cleanStatus.equalsIgnoreCase(
                            savedStatus.trim()
                    )) {

                System.out.println(
                        "ERROR: Firestore status verification failed."
                );

                return false;
            }

            return true;

        } catch (Exception e) {

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "ERROR UPDATING FARMER VERIFICATION"
            );

            System.out.println(
                    e.getMessage()
            );

            System.out.println(
                    "=========================================="
            );

            e.printStackTrace();

            return false;
        }
    }
}