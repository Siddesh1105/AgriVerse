package com.mainproject.model;

public class BuyerSettings {

    private String userEmail;

    // Delivery Address
    private String fullAddress;
    private String city;
    private String district;
    private String state;
    private String pincode;

    // Payment
    private String paymentMethod;
    private String upiId;

    // Notifications
    private boolean orderNotifications;
    private boolean priceAlerts;
    private boolean offerNotifications;

    // Privacy & Security
    private boolean profileVisible;
    private boolean loginAlerts;

    public BuyerSettings() {
    }

    public BuyerSettings(String userEmail) {

        this.userEmail = userEmail;

        // Default values
        this.orderNotifications = true;
        this.priceAlerts = true;
        this.offerNotifications = true;

        this.profileVisible = true;
        this.loginAlerts = true;

        this.paymentMethod = "Cash on Delivery";
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
    // ADDRESS
    // =====================================================

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    // =====================================================
    // PAYMENT
    // =====================================================

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    // =====================================================
    // NOTIFICATIONS
    // =====================================================

    public boolean isOrderNotifications() {
        return orderNotifications;
    }

    public void setOrderNotifications(boolean orderNotifications) {
        this.orderNotifications = orderNotifications;
    }

    public boolean isPriceAlerts() {
        return priceAlerts;
    }

    public void setPriceAlerts(boolean priceAlerts) {
        this.priceAlerts = priceAlerts;
    }

    public boolean isOfferNotifications() {
        return offerNotifications;
    }

    public void setOfferNotifications(boolean offerNotifications) {
        this.offerNotifications = offerNotifications;
    }

    // =====================================================
    // PRIVACY
    // =====================================================

    public boolean isProfileVisible() {
        return profileVisible;
    }

    public void setProfileVisible(boolean profileVisible) {
        this.profileVisible = profileVisible;
    }

    public boolean isLoginAlerts() {
        return loginAlerts;
    }

    public void setLoginAlerts(boolean loginAlerts) {
        this.loginAlerts = loginAlerts;
    }
}