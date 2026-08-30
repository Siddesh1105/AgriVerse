package com.mainproject.view.admin;

/**
 * Simple data holder for one row of the Content Management table
 * (a Page, Banner, Announcement, FAQ or Resource).
 */
public class ContentItem {

    private String title;
    private String description;
    private String type;
    private String category;
    private String status;
    private String lastUpdated;
    private String updatedBy;

    public ContentItem(String title, String description, String type, String category,
                        String status, String lastUpdated, String updatedBy) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.category = category;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.updatedBy = updatedBy;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public String getLastUpdated() { return lastUpdated; }
    public String getUpdatedBy() { return updatedBy; }

    public void setStatus(String status) { this.status = status; }
}

