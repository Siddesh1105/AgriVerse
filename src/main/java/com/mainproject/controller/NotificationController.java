package com.mainproject.controller;

import java.util.*;
import com.mainproject.model.Notification;
import com.mainproject.dao.NotificationDAO;

/** Controller layer for Notification. */
public class NotificationController {

    private final NotificationDAO notificationDAO;

    public NotificationController() {
        this.notificationDAO = new NotificationDAO();
    }

    public boolean addNotification(
            Notification notification) {
        return notificationDAO.addNotification(notification);
    }

    public List<Notification> getNotificationsByUser(
            String userEmail) {
        return notificationDAO.getNotificationsByUser(userEmail);
    }

    public List<Notification> getFarmerNotifications(
            String farmerEmail) {
        return notificationDAO.getFarmerNotifications(farmerEmail);
    }

    public List<Notification> getUnreadNotifications(
            String userEmail) {
        return notificationDAO.getUnreadNotifications(userEmail);
    }

    public int getUnreadCount(
            String userEmail) {
        return notificationDAO.getUnreadCount(userEmail);
    }

    public boolean markAsRead(
            String notificationId) {
        return notificationDAO.markAsRead(notificationId);
    }

    public boolean markAllAsRead(
            String userEmail) {
        return notificationDAO.markAllAsRead(userEmail);
    }

    public boolean deleteNotification(
            String notificationId) {
        return notificationDAO.deleteNotification(notificationId);
    }

    public boolean deleteAllNotifications(
            String userEmail) {
        return notificationDAO.deleteAllNotifications(userEmail);
    }

    public boolean deleteAll(
            String farmerEmail) {
        return notificationDAO.deleteAll(farmerEmail);
    }

    public List<Notification> getAllNotifications() {
        return notificationDAO.getAllNotifications();
    }

}