package com.mainproject.view.buyer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class VoiceAssistant {

    private final BuyerDashboard mainController;

    public VoiceAssistant (BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        Label greeting = new Label("Hello Rahul! How can I help you? 🎙️");
        greeting.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Button btnMic = new Button("🎙️");
        btnMic.setStyle("-fx-font-size: 32px; -fx-background-color: #166534; -fx-text-fill: white; -fx-pref-width: 90px; -fx-pref-height: 90px; -fx-background-radius: 50; -fx-cursor: hand;");
        btnMic.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Listening to your voice prompt...");
            alert.showAndWait();
        });

        Label hints = new Label("Try saying:\n• 'Show me live farmers'\n• 'Find tomato under 30 rupees'\n• 'Track my order'");
        hints.setStyle("-fx-text-fill: #64748B; -fx-text-alignment: center; -fx-font-size: 14px;");

        root.getChildren().addAll(greeting, btnMic, hints);
        return root;
    }
}
