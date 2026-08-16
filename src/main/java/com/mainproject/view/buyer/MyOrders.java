package com.mainproject.view.buyer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MyOrders {

    private final BuyerDashboard mainController;

    public MyOrders (BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25, 30, 25, 30));

        Label title = new Label("My Orders History 📦");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox ordersList = new VBox(10);
        ordersList.getChildren().addAll(
            createOrderCard("Order #1024", "Tomato (20 kg)", "₹600", "Delivered", "15 May 2025"),
            createOrderCard("Order #1023", "Wheat (50 kg)", "₹1,250", "Processing", "16 May 2025"),
            createOrderCard("Order #1022", "Potato (30 kg)", "₹600", "Shipped", "14 May 2025"),
            createOrderCard("Order #1021", "Onion (20 kg)", "₹440", "Delivered", "10 May 2025")
        );

        root.getChildren().addAll(title, ordersList);

        ScrollPane sp = new ScrollPane(root);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return sp;
    }

    private HBox createOrderCard(String id, String item, String price, String status, String date) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");

        Label lblInfo = new Label(id + " • " + date + "\n" + item);
        HBox.setHgrow(lblInfo, Priority.ALWAYS);

        Label lblStatus = new Label(price + "\n" + status);
        lblStatus.setStyle("-fx-text-fill: #166534; -fx-font-weight: bold;");

        card.getChildren().addAll(lblInfo, lblStatus);
        return card;
    }
}