package com.mainproject.model;

import java.util.Date;

public class Equipment {

    private String equipmentId;
    private String name;
    private double price;
    private String location;
    private String category;
    private String description;
    private boolean available;

    private String ownerEmail;
    private String ownerName;

    // Cloudinary image URL
    private String imageUrl;

    private Date createdAt;

    // =========================================================
    // EMPTY CONSTRUCTOR
    // Required by Firestore
    // =========================================================

    public Equipment() {
    }

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Equipment(
            String name,
            double price,
            String location,
            String category,
            String description,
            boolean available,
            String ownerEmail,
            String ownerName,
            String imageUrl) {

        this.name = name;
        this.price = price;
        this.location = location;
        this.category = category;
        this.description = description;
        this.available = available;
        this.ownerEmail = ownerEmail;
        this.ownerName = ownerName;
        this.imageUrl = imageUrl;
        this.createdAt = new Date();
    }

    // =========================================================
    // EQUIPMENT ID
    // =========================================================

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    // =========================================================
    // NAME
    // =========================================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // =========================================================
    // PRICE
    // =========================================================

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // =========================================================
    // LOCATION
    // =========================================================

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // =========================================================
    // CATEGORY
    // =========================================================

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // =========================================================
    // DESCRIPTION
    // =========================================================

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // =========================================================
    // AVAILABLE
    // =========================================================

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }


    public String getStatus() { return available ? "Approved" : "Unavailable"; }
    public void setStatus(String status) {
        this.available = status != null && (status.equalsIgnoreCase("Approved") || status.equalsIgnoreCase("Available"));
    }

    // =========================================================
    // OWNER EMAIL
    // =========================================================

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    // =========================================================
    // OWNER NAME
    // =========================================================

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    // =========================================================
    // IMAGE URL
    // =========================================================

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // =========================================================
    // CREATED AT
    // =========================================================

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}