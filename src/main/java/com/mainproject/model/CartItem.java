package com.mainproject.model;

import java.util.Date;

public class CartItem {

    private String cartItemId;

    private String farmerEmail;

    private String equipmentId;
    private String equipmentName;
    private String category;
    private String location;

    private double pricePerDay;

    private int rentalDays;

    private double totalPrice;

    private String imageUrl;

    private String ownerEmail;
    private String ownerName;

    private Date createdAt;
    private Date updatedAt;

    // =====================================================
    // EMPTY CONSTRUCTOR
    // Required by Firestore
    // =====================================================

    public CartItem() {
    }

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public CartItem(
            String farmerEmail,
            Equipment equipment) {

        this.farmerEmail = farmerEmail;

        if (equipment != null) {

            this.equipmentId =
                    equipment.getEquipmentId();

            this.equipmentName =
                    equipment.getName();

            this.category =
                    equipment.getCategory();

            this.location =
                    equipment.getLocation();

            this.pricePerDay =
                    equipment.getPrice();

            this.rentalDays = 1;

            this.totalPrice =
                    pricePerDay * rentalDays;

            this.imageUrl =
                    equipment.getImageUrl();

            this.ownerEmail =
                    equipment.getOwnerEmail();

            this.ownerName =
                    equipment.getOwnerName();
        }

        this.createdAt =
                new Date();

        this.updatedAt =
                new Date();
    }

    // =====================================================
    // CART ITEM ID
    // =====================================================

    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(
            String cartItemId) {

        this.cartItemId =
                cartItemId;
    }

    // =====================================================
    // FARMER EMAIL
    // =====================================================

    public String getFarmerEmail() {
        return farmerEmail;
    }

    public void setFarmerEmail(
            String farmerEmail) {

        this.farmerEmail =
                farmerEmail;
    }

    // =====================================================
    // EQUIPMENT ID
    // =====================================================

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(
            String equipmentId) {

        this.equipmentId =
                equipmentId;
    }

    // =====================================================
    // EQUIPMENT NAME
    // =====================================================

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(
            String equipmentName) {

        this.equipmentName =
                equipmentName;
    }

    // =====================================================
    // CATEGORY
    // =====================================================

    public String getCategory() {
        return category;
    }

    public void setCategory(
            String category) {

        this.category =
                category;
    }

    // =====================================================
    // LOCATION
    // =====================================================

    public String getLocation() {
        return location;
    }

    public void setLocation(
            String location) {

        this.location =
                location;
    }

    // =====================================================
    // PRICE PER DAY
    // =====================================================

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(
            double pricePerDay) {

        this.pricePerDay =
                pricePerDay;
    }

    // =====================================================
    // RENTAL DAYS
    // =====================================================

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(
            int rentalDays) {

        this.rentalDays =
                rentalDays;

        calculateTotal();
    }

    // =====================================================
    // TOTAL PRICE
    // =====================================================

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(
            double totalPrice) {

        this.totalPrice =
                totalPrice;
    }

    // =====================================================
    // IMAGE URL
    // =====================================================

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(
            String imageUrl) {

        this.imageUrl =
                imageUrl;
    }

    // =====================================================
    // OWNER EMAIL
    // =====================================================

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(
            String ownerEmail) {

        this.ownerEmail =
                ownerEmail;
    }

    // =====================================================
    // OWNER NAME
    // =====================================================

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(
            String ownerName) {

        this.ownerName =
                ownerName;
    }

    // =====================================================
    // CREATED AT
    // =====================================================

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            Date createdAt) {

        this.createdAt =
                createdAt;
    }

    // =====================================================
    // UPDATED AT
    // =====================================================

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
            Date updatedAt) {

        this.updatedAt =
                updatedAt;
    }

    // =====================================================
    // CALCULATE TOTAL
    // =====================================================

    public void calculateTotal() {

        this.totalPrice =
                this.pricePerDay
                        * this.rentalDays;
    }
}