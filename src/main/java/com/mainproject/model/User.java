package com.mainproject.model;

public class User {

    private String uid;
    private String fullName;
    private String email;
    private String role;

    // Required by Firestore
    public User() {
    }

    // Constructor
    public User(
            String uid,
            String fullName,
            String email,
            String role) {

        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    // =========================
    // UID
    // =========================

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    // =========================
    // FULL NAME
    // =========================

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // =========================
    // EMAIL
    // =========================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // =========================
    // ROLE
    // =========================

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {

        return "User{" +
                "uid='" + uid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}