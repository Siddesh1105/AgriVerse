package com.mainproject.controller;

import com.mainproject.dao.BuyerSettingsDAO;
import com.mainproject.model.BuyerSettings;

public class BuyerSettingsController {

    private final BuyerSettingsDAO buyerSettingsDAO;

    public BuyerSettingsController() {

        buyerSettingsDAO =
                new BuyerSettingsDAO();
    }

    // =====================================================
    // SAVE SETTINGS
    // =====================================================

    public boolean saveSettings(
            BuyerSettings settings) {

        return buyerSettingsDAO
                .saveSettings(settings);
    }

    // =====================================================
    // GET SETTINGS
    // =====================================================

    public BuyerSettings getSettings(
            String userEmail) {

        return buyerSettingsDAO
                .getSettings(userEmail);
    }

    // =====================================================
    // GET OR CREATE SETTINGS
    // =====================================================

    public BuyerSettings getOrCreateSettings(
            String userEmail) {

        return buyerSettingsDAO
                .getOrCreateSettings(userEmail);
    }
}