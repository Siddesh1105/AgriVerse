package com.mainproject.view.farmer;

import com.mainproject.util.LanguageManager;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AiRecommendations {

    public Node getView() {
        VBox root = new VBox(18);
        root.setPadding(new Insets(10));

        VBox titles = new VBox(2);
        Label title = new Label("AI Recommendations");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");
        Label sub = new Label("Smart insights and suggestions for your farm.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        titles.getChildren().addAll(title, sub);

        // Top 2 Big Cards
        HBox row = new HBox(16);

        // Card 1: Irrigation
        VBox card1 = new VBox(10);
        card1.setPadding(new Insets(20));
        card1.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14px; -fx-border-color: #A2D9CE;");
        HBox.setHgrow(card1, Priority.ALWAYS);

        Label tag1 = new Label("Suggested Action");
        tag1.setStyle("-fx-font-size: 12px; -fx-text-fill: #566573;");
        Label h1 = new Label("Increase irrigation");
        h1.setStyle("-fx-font-size: 18px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");
        Label p1 = new Label("Soil moisture is low in your farm. Increase irrigation for better crop yield.");
        p1.setWrapText(true);
        p1.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        Button btn1 = new Button("View Details");
        btn1.setStyle("-fx-background-color: #117864; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 7 16; -fx-cursor: hand;");
        card1.getChildren().addAll(tag1, h1, p1, btn1);

        // Card 2: Planting Time
        VBox card2 = new VBox(10);
        card2.setPadding(new Insets(20));
        card2.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 14px; -fx-border-color: #A2D9CE;");
        HBox.setHgrow(card2, Priority.ALWAYS);

        Label tag2 = new Label("Best Time to Plant");
        tag2.setStyle("-fx-font-size: 12px; -fx-text-fill: #566573;");
        Label h2 = new Label("Tomato");
        h2.setStyle("-fx-font-size: 18px; -fx-font-weight: 800; -fx-text-fill: #117864;");
        Label p2 = new Label("Next 3 days are ideal for planting with current soil temperatures.");
        p2.setWrapText(true);
        p2.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        Button btn2 = new Button("View Calendar");
        btn2.setStyle("-fx-background-color: transparent; -fx-border-color: #117864; -fx-text-fill: #117864; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 7 16; -fx-cursor: hand;");
        card2.getChildren().addAll(tag2, h2, p2, btn2);

        row.getChildren().addAll(card1, card2);

        // Other Recommendations Box
        VBox other = new VBox(12);
        other.setPadding(new Insets(18));
        other.setStyle("-fx-background-color: #D4EFDF; -fx-background-radius: 14px; -fx-border-color: #A2D9CE;");

        Label otherTitle = new Label("Other Recommendations");
        otherTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: #117864;");

        other.getChildren().addAll(
            otherTitle,
            createRecItem("🌱 Use organic fertilizer for better soil health."),
            createRecItem("🌧️ Expected rainfall increase next week — postpone pesticide spraying."),
            createRecItem("📈 Market price for Tomato may increase due to festive demand.")
        );

        root.getChildren().addAll(titles, row, other);
        return new ScrollPane(root);
    }

    private Label createRecItem(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 13px; -fx-text-fill: #1B2631;");
        return l;
    }
}