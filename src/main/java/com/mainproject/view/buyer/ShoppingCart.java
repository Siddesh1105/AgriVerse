package com.mainproject.view.buyer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ShoppingCart {

    private final BuyerDashboard mainController;

    public ShoppingCart(BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));

        VBox itemsBox = new VBox(12);
        itemsBox.getChildren().addAll(
            new Label("🛒 My Cart (3 Items)"),
            createCartRow("Fresh Tomato", "Ramesh Patil", "10 kg", "₹280"),
            createCartRow("Onion", "Mahesh Farm", "5 kg", "₹110"),
            createCartRow("Potato", "Patel Farm", "10 kg", "₹200")
        );

        // Summary Card
        VBox summary = new VBox(12);
        summary.setPrefWidth(280);
        summary.setPadding(new Insets(20));
        summary.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label lblSum = new Label("Order Summary");
        lblSum.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label subtotal = new Label("Subtotal: ₹590");
        Label delivery = new Label("Delivery: ₹40");
        Label total = new Label("Total: ₹630");
        total.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #166534;");

        Button btnProceed = new Button("Proceed to Checkout");
        btnProceed.setMaxWidth(Double.MAX_VALUE);
        btnProceed.setStyle("-fx-background-color: #166534; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 8; -fx-cursor: hand;");
        btnProceed.setOnAction(e -> mainController.setView(new Checkout(mainController).getView()));

        summary.getChildren().addAll(lblSum, subtotal, delivery, new Separator(), total, btnProceed);

        root.setCenter(itemsBox);
        root.setRight(summary);
        BorderPane.setMargin(summary, new Insets(0, 0, 0, 20));
        return root;
    }

    private HBox createCartRow(String crop, String farmer, String qty, String total) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12));
        row.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");

        Label details = new Label(crop + " (" + farmer + ")\nQuantity: " + qty);
        HBox.setHgrow(details, Priority.ALWAYS);

        Label price = new Label(total);
        price.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        row.getChildren().addAll(details, price);
        return row;
    }
}