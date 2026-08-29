package com.mainproject.dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.mainproject.config.FirebaseConfig;
import com.mainproject.model.BuyerSettings;

public class BuyerSettingsDAO {

    private final Firestore db;

    public BuyerSettingsDAO() {
        db = FirebaseConfig.getFirestore();
    }

    // =====================================================
    // SAVE SETTINGS
    // =====================================================

    public boolean saveSettings(BuyerSettings settings) {

        try {

            db.collection("buyerSettings")
                    .document(settings.getUserEmail())
                    .set(settings)
                    .get();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error saving buyer settings: "
                            + e.getMessage());

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET SETTINGS
    // =====================================================

    public BuyerSettings getSettings(String userEmail) {

        try {

            DocumentSnapshot document = db
                    .collection("buyerSettings")
                    .document(userEmail)
                    .get()
                    .get();

            if (document.exists()) {

                BuyerSettings settings =
                        document.toObject(BuyerSettings.class);

                return settings;
            }

        } catch (Exception e) {

            System.out.println(
                    "Error getting buyer settings: "
                            + e.getMessage());

            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // CREATE DEFAULT SETTINGS
    // =====================================================

    public BuyerSettings getOrCreateSettings(
            String userEmail) {

        BuyerSettings settings =
                getSettings(userEmail);

        if (settings == null) {

            settings =
                    new BuyerSettings(userEmail);

            saveSettings(settings);
        }

        return settings;
    }
}