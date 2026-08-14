package com.mainproject.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Notifications {

    public Node getView() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(10));

        VBox titles = new VBox(2);
        Label title = new Label("Notifications");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");
        Label sub = new Label("Stay updated with your farm activities.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        titles.getChildren().addAll(title, sub);

        // Filter Tabs
        HBox tabs = new HBox(8);
        tabs.getChildren().addAll(
            createChip("All", true),
            createChip("Orders", false),
            createChip("System", false),
            createChip("Weather", false),
            createChip("Market", false)
        );

        // Notifications List
        VBox list = new VBox(10);
        list.getChildren().addAll(
            createNotifItem("🛍️", "New order received for Tomato (50 kg)", "2 min ago"),
            createNotifItem("⚠️", "Your product Onion is low in stock.", "1 hour ago"),
            createNotifItem("🌧️", "Heavy rainfall expected tomorrow.", "3 hours ago"),
            createNotifItem("💰", "Payment of ₹2,500 received for Order #ORD123", "5 hours ago"),
            createNotifItem("📈", "Market price for Wheat increased by 3%.", "Yesterday")
        );

        root.getChildren().addAll(titles, tabs, list);
        return new ScrollPane(root);
    }

    private Button createChip(String text, boolean active) {
        Button b = new Button(text);
        if (active) {
            b.setStyle("-fx-background-color: #117864; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20px; -fx-padding: 4 14; -fx-cursor: hand;");
        } else {
            b.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #A2D9CE; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-text-fill: #1B2631; -fx-padding: 4 14; -fx-cursor: hand;");
        }
        return b;
    }

    private HBox createNotifItem(String icon, String message, String time) {
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(14));
        box.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #A2D9CE;");

        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size: 20px;");

        Label msg = new Label(message);
        msg.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1B2631;");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label t = new Label(time);
        t.setStyle("-fx-font-size: 12px; -fx-text-fill: #566573;");

        box.getChildren().addAll(ic, msg, sp, t);
        return box;
    }
}
