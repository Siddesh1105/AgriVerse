package com.mainproject.view.buyer;

import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Notification {

    private final BuyerDashboard mainController;

    public Notification(BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25, 30, 25, 30));

        Label title = new Label("Recent Notifications 🔔");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox list = new VBox(10);
        list.getChildren().addAll(
            createNotifItem("Order #1024 has been delivered.", "Just now"),
            createNotifItem("Ramesh Patil is live now streaming Tomato harvest!", "10 min ago"),
            createNotifItem("Price drop alert: Tomato price is now ₹28/kg in Nashik.", "1 hour ago"),
            createNotifItem("Special Offer: Get 10% off on all organic grains.", "1 day ago")
        );

        root.getChildren().addAll(title, list);
        LanguageManager.apply(root);
        return root;
    }

    private HBox createNotifItem(String text, String time) {
        HBox row = new HBox(15);
        row.setPadding(new Insets(12));
        row.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");

        Label lbl = new Label("🔔 " + text);
        HBox.setHgrow(lbl, Priority.ALWAYS);

        Label lblTime = new Label(time);
        lblTime.setStyle("-fx-text-fill: #94A3B8;");

        row.getChildren().addAll(lbl, lblTime);
        return row;
    }
}