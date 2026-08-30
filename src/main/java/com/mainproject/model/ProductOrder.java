package com.mainproject.model;

import java.util.Date;

public class ProductOrder {

    private String orderId;

    // =====================================================
    // PRODUCT DETAILS
    // =====================================================

    private String productId;
    private String productName;
    private String productImageUrl;
    private String category;
    private String unit;

    // =====================================================
    // FARMER DETAILS
    // =====================================================

    private String farmerEmail;

    // =====================================================
    // BUYER DETAILS
    // =====================================================

    private String buyerEmail;
    private String buyerName;

    // =====================================================
    // ORDER DETAILS
    // =====================================================

    private double quantity;
    private double pricePerUnit;
    private double totalAmount;

    // pending / accepted / rejected / shipped / delivered
    private String status;

    private String paymentStatus; // pending / paid / failed
    private String paymentId;
    private String razorpayOrderId;
    private String paymentMethod;
    private Date paymentDate;

    private Date createdAt;

    // =====================================================
    // EMPTY CONSTRUCTOR
    // Required by Firestore
    // =====================================================

    public ProductOrder() {
    }

    // =====================================================
    // ORDER ID
    // =====================================================

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    // =====================================================
    // PRODUCT ID
    // =====================================================

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    // =====================================================
    // PRODUCT NAME
    // =====================================================

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // =====================================================
    // PRODUCT IMAGE
    // =====================================================

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    // =====================================================
    // CATEGORY
    // =====================================================

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // =====================================================
    // UNIT
    // =====================================================

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // =====================================================
    // FARMER EMAIL
    // =====================================================

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
    }

    // =====================================================
    // BUYER EMAIL
    // =====================================================

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    // =====================================================
    // BUYER NAME
    // =====================================================

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    // =====================================================
    // QUANTITY
    // =====================================================

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    // =====================================================
    // PRICE PER UNIT
    // =====================================================

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    // =====================================================
    // TOTAL AMOUNT
    // =====================================================

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // =====================================================
    // STATUS
    // =====================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    // =====================================================
    // CREATED AT
    // =====================================================

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}