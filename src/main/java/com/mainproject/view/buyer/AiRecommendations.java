
package com.mainproject.view.buyer;

import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AiRecommendations {

    private final BuyerDashboard mainController;

    public AiRecommendations(BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25, 30, 25, 30));

        Label title = new Label("AI Recommendations & Market Predictions 🤖");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox cards = new HBox(15);
        cards.getChildren().addAll(
            createAiCard("Best Price for Tomato", "Ramesh Patil", "₹28 / kg", "98% Price Match"),
            createAiCard("Best Price for Wheat", "Green Valley Farm", "₹26 / kg", "Lowest Market Rate"),
            createAiCard("Best Quality Onion", "Mahesh Farm", "₹22 / kg", "Grade A Verified")
        );

        HBox promptBar = new HBox(10);
        TextField tfPrompt = new TextField();
        tfPrompt.setPromptText("Ask AgriAI (e.g. 'Find cheapest fresh wheat in Pune')");
        HBox.setHgrow(tfPrompt, Priority.ALWAYS);

        Button btnAsk = new Button("Ask AI");
        btnAsk.setStyle("-fx-background-color: #166534; -fx-text-fill: white;");

        promptBar.getChildren().addAll(tfPrompt, btnAsk);

        root.getChildren().addAll(title, cards, promptBar);
        LanguageManager.apply(root);
        return root;
    }

    private VBox createAiCard(String category, String farmer, String price, String tag) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setPrefWidth(280);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 10;");

        Label lblCat = new Label(category);
        lblCat.setStyle("-fx-font-weight: bold; -fx-text-fill: #166534;");
        Label lblFarm = new Label(farmer);
        Label lblPrice = new Label(price + " (" + tag + ")");

        card.getChildren().addAll(lblCat, lblFarm, lblPrice);
        return card;
    }
}