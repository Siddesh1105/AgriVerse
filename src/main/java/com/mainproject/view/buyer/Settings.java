package com.mainproject.view.buyer;

import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Settings {

    private final BuyerDashboard mainController;
    private final Runnable languageChanged;

    public Settings(BuyerDashboard controller) {
        this(controller, null);
    }

    public Settings(
            BuyerDashboard controller,
            Runnable languageChanged) {
        this.mainController = controller;
        this.languageChanged = languageChanged;
    }

    public Node getView() {

        VBox root = new VBox(15);
        root.setPadding(new Insets(25, 40, 25, 40));

        Label title = new Label("Settings & Preferences ⚙️");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label languageTitle = new Label("Language");
        languageTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        Label languageDescription = new Label(
                "Choose your preferred application language.");
        languageDescription.setStyle(
                "-fx-text-fill: #64748B; -fx-font-size: 12px;");

        ComboBox<String> language = new ComboBox<>();
        language.getItems().addAll("English", "Marathi");
        language.setValue(LanguageManager.getLanguage());
        language.setPrefWidth(300);
        language.setOnAction(e -> {
            LanguageManager.setLanguage(language.getValue());
            if (languageChanged != null) {
                languageChanged.run();
            }
        });

        String[] options = {
            "👤 Account Settings (Name, Email, Password)",
            "📍 Saved Delivery Addresses",
            "💳 Payment Methods & UPI",
            "🔔 Notification Preferences",
            "🔒 Privacy & Security Settings",
            "🌐 App Language (English / मराठी)",
            "❓ Help & Support"
        };

        VBox optsBox = new VBox(8);
        for (String opt : options) {
            Button btn = new Button(opt);
            btn.setMaxWidth(600);
            btn.setStyle(
                    "-fx-background-color: #FFFFFF;" +
                    "-fx-border-color: #E2E8F0;" +
                    "-fx-alignment: center-left;" +
                    "-fx-padding: 12;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;");
            optsBox.getChildren().add(btn);
        }

        root.getChildren().addAll(
                title,
                optsBox,
                languageTitle,
                languageDescription,
                language);

        LanguageManager.apply(root);
        return root;
    }
}
