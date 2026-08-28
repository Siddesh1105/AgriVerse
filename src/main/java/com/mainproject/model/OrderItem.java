package com.mainproject.model;

public class OrderItem {

    private String productId;
    private String productName;

    // Farmer who owns this product
    private String farmerEmail;

    private String unit;

    private double price;
    private double quantity;
    private double totalPrice;

    // Pending / Accepted / Rejected / Processing / Completed
    private String status;

    // =====================================================
    // EMPTY CONSTRUCTOR
    // Required by Firestore
    // =====================================================

    public OrderItem() {
    }

    // =====================================================
    // CALCULATE TOTAL
    // =====================================================

    public void calculateTotal() {

        totalPrice = price * quantity;
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
    // FARMER EMAIL
    // =====================================================

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(String farmerEmail) {
        this.farmerEmail = farmerEmail;
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
    // PRICE
    // =====================================================

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
    // TOTAL PRICE
    // =====================================================

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
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
}