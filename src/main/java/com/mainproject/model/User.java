package com.mainproject.model;

public class User {

    private String uid;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String gender;
    private String role;
    private String profileImageUrl;

    // Farmer verification fields
    private String verificationStatus;
    private String rejectionReason;

    // =====================================================
    // EMPTY CONSTRUCTOR
    // Required by Firestore
    // =====================================================

    public User() {
    }

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public User(
            String uid,
            String fullName,
            String email,
            String mobileNumber,
            String gender,
            String role) {

        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
        this.role = role;

        // Every new farmer starts as Pending.
        if ("Farmer".equalsIgnoreCase(role)) {
            this.verificationStatus = "Pending";
        }
    }

    // =====================================================
    // UID
    // =====================================================

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    // =====================================================
    // FULL NAME
    // =====================================================

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // =====================================================
    // EMAIL
    // =====================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // =====================================================
    // MOBILE
    // =====================================================

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    // =====================================================
    // GENDER
    // =====================================================

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // =====================================================
    // ROLE
    // =====================================================

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // =====================================================
    // PROFILE IMAGE
    // =====================================================

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    // =====================================================
    // VERIFICATION STATUS
    // =====================================================

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(
            String verificationStatus) {

        this.verificationStatus = verificationStatus;
    }

    // =====================================================
    // REJECTION REASON
    // =====================================================

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(
            String rejectionReason) {

        this.rejectionReason = rejectionReason;
    }

    // =====================================================
    // TO STRING
    // =====================================================

    @Override
    public String toString() {

        return "User{" +
                "uid='" + uid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", role='" + role + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", verificationStatus='" +
                verificationStatus + '\'' +
                ", rejectionReason='" +
                rejectionReason + '\'' +
                '}';
    }
}