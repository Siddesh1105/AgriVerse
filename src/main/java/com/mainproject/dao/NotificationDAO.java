package com.mainproject.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.mainproject.model.Notification;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationDAO {

    private static final String COLLECTION =
            "notifications";

    // =====================================================
    // ADD NOTIFICATION
    // =====================================================

    public boolean addNotification(
            Notification notification) {

        try {

            if (notification == null) {

                System.out.println(
                        "Notification is null.");

                return false;
            }

            if (notification.getUserEmail() == null
                    || notification.getUserEmail()
                    .trim()
                    .isEmpty()) {

                System.out.println(
                        "Notification user email is missing.");

                return false;
            }

            Firestore db =
                    FirestoreClient.getFirestore();

            DocumentReference document =
                    db.collection(COLLECTION)
                            .document();

            notification.setNotificationId(
                    document.getId());

            if (notification.getCreatedAt() == null) {

                notification.setCreatedAt(
                        new Date());
            }

            if (notification.getTitle() == null) {

                notification.setTitle(
                        "Notification");
            }

            if (notification.getMessage() == null) {

                notification.setMessage("");
            }

            if (notification.getType() == null) {

                notification.setType(
                        "System");
            }

            document.set(notification)
                    .get(
                            10,
                            TimeUnit.SECONDS);

            System.out.println(
                    "====================================");

            System.out.println(
                    "Notification saved successfully");

            System.out.println(
                    "Notification ID: "
                            + document.getId());

            System.out.println(
                    "User Email: "
                            + notification.getUserEmail());

            System.out.println(
                    "Title: "
                            + notification.getTitle());

            System.out.println(
                    "====================================");

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error saving notification:");

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET USER NOTIFICATIONS
    // =====================================================

    public List<Notification> getNotificationsByUser(
            String userEmail) {

        List<Notification> list =
                new ArrayList<>();

        try {

            if (userEmail == null
                    || userEmail.trim().isEmpty()) {

                return list;
            }

            Firestore db =
                    FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "userEmail",
                                    userEmail.trim())
                            .get();

            QuerySnapshot snapshot =
                    future.get(
                            10,
                            TimeUnit.SECONDS);

            for (
                    QueryDocumentSnapshot document
                    : snapshot.getDocuments()) {

                Notification notification =
                        document.toObject(
                                Notification.class);

                if (notification != null) {

                    notification.setNotificationId(
                            document.getId());

                    list.add(notification);
                }
            }

            // Newest notification first
            list.sort(
                    Comparator.comparing(
                            Notification::getCreatedAt,
                            Comparator.nullsLast(
                                    Comparator.reverseOrder())));

            System.out.println(
                    "Notifications loaded: "
                            + list.size());

        } catch (Exception e) {

            System.out.println(
                    "Error loading notifications:");

            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // GET FARMER NOTIFICATIONS
    // =====================================================

    public List<Notification> getFarmerNotifications(
            String farmerEmail) {

        return getNotificationsByUser(
                farmerEmail);
    }

    // =====================================================
    // GET UNREAD NOTIFICATIONS
    // =====================================================

    public List<Notification> getUnreadNotifications(
            String userEmail) {

        List<Notification> list =
                new ArrayList<>();

        try {

            if (userEmail == null
                    || userEmail.trim().isEmpty()) {

                return list;
            }

            Firestore db =
                    FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "userEmail",
                                    userEmail.trim())
                            .whereEqualTo(
                                    "read",
                                    false)
                            .get();

            QuerySnapshot snapshot =
                    future.get(
                            10,
                            TimeUnit.SECONDS);

            for (
                    QueryDocumentSnapshot document
                    : snapshot.getDocuments()) {

                Notification notification =
                        document.toObject(
                                Notification.class);

                if (notification != null) {

                    notification.setNotificationId(
                            document.getId());

                    list.add(notification);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error loading unread notifications:");

            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // GET UNREAD COUNT
    // =====================================================

    public int getUnreadCount(
            String userEmail) {

        return getUnreadNotifications(
                userEmail).size();
    }

    // =====================================================
    // MARK AS READ
    // =====================================================

    public boolean markAsRead(
            String notificationId) {

        try {

            if (notificationId == null
                    || notificationId.trim().isEmpty()) {

                return false;
            }

            Firestore db =
                    FirestoreClient.getFirestore();

            db.collection(COLLECTION)
                    .document(notificationId)
                    .update(
                            "read",
                            true)
                    .get(
                            10,
                            TimeUnit.SECONDS);

            System.out.println(
                    "Notification marked as read: "
                            + notificationId);

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error marking notification as read:");

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // MARK ALL AS READ
    // =====================================================

    public boolean markAllAsRead(
            String userEmail) {

        try {

            List<Notification> notifications =
                    getUnreadNotifications(
                            userEmail);

            Firestore db =
                    FirestoreClient.getFirestore();

            for (
                    Notification notification
                    : notifications) {

                if (notification.getNotificationId()
                        != null) {

                    db.collection(COLLECTION)
                            .document(
                                    notification
                                            .getNotificationId())
                            .update(
                                    "read",
                                    true)
                            .get(
                                    10,
                                    TimeUnit.SECONDS);
                }
            }

            System.out.println(
                    "All notifications marked as read.");

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error marking all notifications:");

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // DELETE SINGLE NOTIFICATION
    // =====================================================

    public boolean deleteNotification(
            String notificationId) {

        try {

            if (notificationId == null
                    || notificationId.trim().isEmpty()) {

                System.out.println(
                        "Notification ID is empty.");

                return false;
            }

            Firestore db =
                    FirestoreClient.getFirestore();

            db.collection(COLLECTION)
                    .document(notificationId)
                    .delete()
                    .get(
                            10,
                            TimeUnit.SECONDS);

            System.out.println(
                    "Notification deleted successfully: "
                            + notificationId);

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error deleting notification:");

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // DELETE ALL USER NOTIFICATIONS
    // =====================================================

    public boolean deleteAllNotifications(
            String userEmail) {

        try {

            List<Notification> notifications =
                    getNotificationsByUser(
                            userEmail);

            Firestore db =
                    FirestoreClient.getFirestore();

            for (
                    Notification notification
                    : notifications) {

                if (notification.getNotificationId()
                        != null) {

                    db.collection(COLLECTION)
                            .document(
                                    notification
                                            .getNotificationId())
                            .delete()
                            .get(
                                    10,
                                    TimeUnit.SECONDS);
                }
            }

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // DELETE ALL
    // Alias used by Notifications.java
    // =====================================================

    public boolean deleteAll(
            String farmerEmail) {

        return deleteAllNotifications(
                farmerEmail);
    }
}