package com.mainproject.model;

public class WeatherRecommendation {

    private String category;
    private String title;
    private String description;
    private String buttonText;

    // =====================================================
    // EMPTY CONSTRUCTOR
    // =====================================================

    public WeatherRecommendation() {
    }

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public WeatherRecommendation(
            String category,
            String title,
            String description,
            String buttonText) {

        this.category = category;
        this.title = title;
        this.description = description;
        this.buttonText = buttonText;
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
    // TITLE
    // =====================================================

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // =====================================================
    // DESCRIPTION
    // =====================================================

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // =====================================================
    // BUTTON TEXT
    // =====================================================

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }
}