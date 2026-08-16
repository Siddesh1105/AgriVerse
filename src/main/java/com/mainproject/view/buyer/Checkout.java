package com.mainproject.view.buyer;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Checkout {

    private final BuyerDashboard mainController;

    public Checkout(BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25, 40, 25, 40));

        Label title = new Label("Checkout & Confirm Order 💳");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox panels = new HBox(25);

        // Address & Payment Section
        VBox leftForm = new VBox(12);
        leftForm.setPadding(new Insets(20));
        leftForm.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 10;");
        HBox.setHgrow(leftForm, Priority.ALWAYS);

        leftForm.getChildren().addAll(
            new Label("Delivery Address:"),
            new TextField("123 Farm Road, Nashik, Maharashtra - 422001"),
            new Label("Select Payment Mode:"),
            new RadioButton("Cash on Delivery (COD)"),
            new RadioButton("UPI / QR Code Scan"),
            new RadioButton("Debit / Credit Card")
        );

        // Confirmation Section
        VBox rightConfirm = new VBox(15);
        rightConfirm.setPrefWidth(300);
        rightConfirm.setPadding(new Insets(20));
        rightConfirm.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 10;");

        Label total = new Label("Total Payable: ₹630");
        total.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #166534;");

        Button btnPlace = new Button("Place Order");
        btnPlace.setMaxWidth(Double.MAX_VALUE);
        btnPlace.setStyle("-fx-background-color: #166534; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 8; -fx-cursor: hand;");
        btnPlace.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully! (Order ID #1025)");
            alert.showAndWait();
            mainController.setView(new MyOrders (mainController).getView());
        });

        rightConfirm.getChildren().addAll(total, btnPlace);

        panels.getChildren().addAll(leftForm, rightConfirm);
        root.getChildren().addAll(title, panels);
        return root;
    }
}