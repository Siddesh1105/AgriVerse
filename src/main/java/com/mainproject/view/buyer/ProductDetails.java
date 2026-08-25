package com.mainproject.view.buyer;

import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ProductDetails {

    private final BuyerDashboard mainController;

    public ProductDetails(BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25, 35, 25, 35));
        root.setStyle("-fx-background-color: #FFFFFF;");

        Button btnBack = new Button("← Back to Marketplace");
        btnBack.setStyle("-fx-background-color: transparent; -fx-text-fill: #166534; -fx-cursor: hand; -fx-font-weight: bold;");
        btnBack.setOnAction(e -> mainController.setView(new LiveMarketplace(mainController).getView()));

        HBox content = new HBox(30);

        // Product Image Mockup
        StackPane imageMock = new StackPane();
        imageMock.setPrefSize(400, 350);
        imageMock.setStyle("-fx-background-color: #F1F5F9; -fx-border-color: #CBD5E1; -fx-border-radius: 12; -fx-background-radius: 12;");
        Label lblImg = new Label("🍅\nFresh Tomato Image Mockup");
        lblImg.setStyle("-fx-font-size: 16px; -fx-text-alignment: center; -fx-text-fill: #64748B;");
        imageMock.getChildren().add(lblImg);

        VBox info = new VBox(12);
        Label title = new Label("Fresh Organic Tomato");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label price = new Label("₹28 / kg  ★ 4.6 (128 Reviews)");
        price.setStyle("-fx-font-size: 18px; -fx-text-fill: #166534; -fx-font-weight: bold;");

        Label farmer = new Label("Farmer: Ramesh Patil (Nashik, Maharashtra) ✔ Verified");
        farmer.setStyle("-fx-text-fill: #64748B;");

        Label desc = new Label("Description:\nDirectly harvested organic grade-A tomatoes. Carefully packed and quality inspected for freshness.");
        desc.setWrapText(true);

        HBox actionBtns = new HBox(12);
        Button btnAdd = new Button("Add to Cart");
        btnAdd.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #166534; -fx-text-fill: #166534; -fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
        btnAdd.setOnAction(e -> mainController.setView(new ShoppingCart(mainController).getView()));

        Button btnBuy = new Button("Buy Now");
        btnBuy.setStyle("-fx-background-color: #166534; -fx-text-fill: white; -fx-padding: 10 24; -fx-background-radius: 8; -fx-cursor: hand;");
        btnBuy.setOnAction(e -> mainController.setView(new Checkout(mainController).getView()));

        actionBtns.getChildren().addAll(btnAdd, btnBuy);

        info.getChildren().addAll(title, price, farmer, desc, actionBtns);

        content.getChildren().addAll(imageMock, info);
        root.getChildren().addAll(btnBack, content);
        LanguageManager.apply(root);
        return root;
    }
}