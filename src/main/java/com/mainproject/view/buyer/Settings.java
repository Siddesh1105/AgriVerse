package com.mainproject.view.buyer;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Settings {

    private final BuyerDashboard mainController;

    public Settings (BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25, 40, 25, 40));

        Label title = new Label("Settings & Preferences ⚙️");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        String[] options = {
            "👤 Account Settings (Name, Email, Password)",
            "📍 Saved Delivery Addresses",
            "💳 Payment Methods & UPI",
            "🔔 Notification Preferences",
            "🔒 Privacy & Security Settings",
            "🌐 App Language (English / हिन्दी / मराठी)",
            "❓ Help & Support"
        };

        VBox optsBox = new VBox(8);
        for (String opt : options) {
            Button btn = new Button(opt);
            btn.setMaxWidth(600);
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-alignment: center-left; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");
            optsBox.getChildren().add(btn);
        }

        root.getChildren().addAll(title, optsBox);
        return root;
    }
}