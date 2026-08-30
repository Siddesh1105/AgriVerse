package com.mainproject.model;

public class User {

    private String uid;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String gender;
    private String role;
    private String profileImageUrl;
    private String verificationStatus;
    private String rejectionReason;

    // =====================================================
    // FARMER LOCATION
    // =====================================================

    private String city;
    private String district;
    private String state;

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
    // MOBILE NUMBER
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


    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    // =====================================================
    // CITY
    // =====================================================

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // =====================================================
    // DISTRICT
    // =====================================================

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    // =====================================================
    // STATE
    // =====================================================

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}