package com.mainproject.view.buyer;

import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class FarmerProfile {

    private final BuyerDashboard mainController;

    public FarmerProfile(BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25, 35, 25, 35));

        // Farmer Details Card
        VBox profileCard = new VBox(10);
        profileCard.setPadding(new Insets(20));
        profileCard.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 10;");

        Label name = new Label("👨‍🌾 Ramesh Patil ✔ Verified Farmer");
        name.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #166534;");
        Label meta = new Label("Location: Nashik, Maharashtra | Rating: ★ 4.8 (256 reviews) | 128 Products");
        Label bio = new Label("We grow organic vegetables with sustainable drip irrigation and zero pesticides.");

        HBox btnBar = new HBox(10);
        Button btnMsg = new Button("Message Farmer");
        btnMsg.setOnAction(e -> mainController.setView(new ChatWithFarmer(mainController).getView()));
        Button btnFollow = new Button("Follow");
        btnFollow.setStyle("-fx-background-color: #166534; -fx-text-fill: white;");

        btnBar.getChildren().addAll(btnMsg, btnFollow);
        profileCard.getChildren().addAll(name, meta, bio, btnBar);

        // Listed products
        Label lblList = new Label("Products by Ramesh Patil:");
        lblList.setStyle("-fx-font-weight: bold;");

        HBox prods = new HBox(15);
        prods.getChildren().addAll(
            createMiniProductCard("Tomato", "₹28/kg"),
            createMiniProductCard("Onion", "₹22/kg"),
            createMiniProductCard("Capsicum", "₹40/kg")
        );

        root.getChildren().addAll(profileCard, lblList, prods);
        LanguageManager.apply(root);
        return root;
    }

    private VBox createMiniProductCard(String name, String price) {
        VBox c = new VBox(5);
        c.setPadding(new Insets(12));
        c.setPrefWidth(150);
        c.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");
        c.getChildren().addAll(new Label(name), new Label(price));
        return c;
    }
}