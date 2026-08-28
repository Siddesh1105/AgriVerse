package com.mainproject.model;

import java.util.Date;

public class Notification {

    private String notificationId;
    private String userEmail;
    private String title;
    private String message;
    private String type;
    private boolean read;
    private Date createdAt;

    // =====================================================
    // EMPTY CONSTRUCTOR
    // Required by Firestore
    // =====================================================

    public Notification() {
    }

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Notification(
            String userEmail,
            String title,
            String message,
            String type) {

        this.userEmail = userEmail;
        this.title = title;
        this.message = message;
        this.type = type;
        this.read = false;
        this.createdAt = new Date();
    }

    // =====================================================
    // NOTIFICATION ID
    // =====================================================

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    // =====================================================
    // USER EMAIL
    // =====================================================

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    // =====================================================
    // TITLE
    // =====================================================

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // =====================================================
    // MESSAGE
    // =====================================================

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // =====================================================
    // TYPE
    // =====================================================

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // =====================================================
    // READ
    // =====================================================

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    // =====================================================
    // CREATED AT
    // =====================================================

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // =====================================================
    // TIMESTAMP
    // =====================================================

    public String getTimestamp() {

        if (createdAt == null) {
            return "";
        }

        return createdAt.toString();
    }

    // =====================================================
    // TO STRING
    // =====================================================

    @Override
    public String toString() {

        return "Notification{" +
                "notificationId='" + notificationId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", read=" + read +
                ", createdAt=" + createdAt +
                '}';
    }
}
