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

public class MarketPlace {

    private final FarmerDashboard navigator;

    public MarketPlace(FarmerDashboard navigator) {
        this.navigator = navigator;
    }

    public Node getView() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(10));

        // Header
        HBox top = new HBox();
        top.setAlignment(Pos.CENTER_LEFT);
        VBox titles = new VBox(2);
        Label pageTitle = new Label("Marketplace");
        pageTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");
        Label sub = new Label("List your products or explore market opportunities.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        titles.getChildren().addAll(pageTitle, sub);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Button addNewBtn = new Button("+ Add New Product");
        addNewBtn.setStyle("-fx-background-color: #117864; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 8 16; -fx-cursor: hand;");
        addNewBtn.setOnAction(e -> navigator.navigateTo("AddProduct"));
        top.getChildren().addAll(titles, sp, addNewBtn);

        // Filter Chips (All, Active, Inactive, Sold Out)
        HBox filterRow = new HBox(8);
        filterRow.getChildren().addAll(
            createChip("All", true),
            createChip("Active", false),
            createChip("Inactive", false),
            createChip("Sold Out", false)
        );

        // Products List
        VBox list = new VBox(12);
        list.getChildren().addAll(
            createProductCard("🍅", "Tomato", "₹20 / kg", "Available: 230 kg", "Active"),
            createProductCard("🧅", "Onion", "₹18 / kg", "Available: 150 kg", "Active"),
            createProductCard("🥔", "Potato", "₹16 / kg", "Available: 300 kg", "Active"),
            createProductCard("🌾", "Wheat", "₹25 / kg", "Available: 500 kg", "Inactive")
        );

        root.getChildren().addAll(top, filterRow, list);
        return new ScrollPane(root);
    }

    private Button createChip(String text, boolean active) {
        Button chip = new Button(text);
        if (active) {
            chip.setStyle("-fx-background-color: #117864; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20px; -fx-padding: 4 14; -fx-cursor: hand;");
        } else {
            chip.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #A2D9CE; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-text-fill: #1B2631; -fx-padding: 4 14; -fx-cursor: hand;");
        }
        return chip;
    }

    private HBox createProductCard(String icon, String name, String price, String stock, String status) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #A2D9CE; -fx-border-radius: 12px;");

        Label iconLbl = new Label(icon);
        iconLbl.setStyle("-fx-font-size: 28px;");

        VBox info = new VBox(2);
        Label nameLbl = new Label(name);
        nameLbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1B2631;");
        Label priceLbl = new Label(price);
        priceLbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #117864; -fx-font-weight: bold;");
        info.getChildren().addAll(nameLbl, priceLbl);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label stockLbl = new Label(stock);
        stockLbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");

        Label statusBadge = new Label(status);
        if ("Active".equalsIgnoreCase(status)) {
            statusBadge.setStyle("-fx-background-color: #D4EFDF; -fx-text-fill: #117864; -fx-font-weight: bold; -fx-padding: 4 10; -fx-background-radius: 12px;");
        } else {
            statusBadge.setStyle("-fx-background-color: #FADBD8; -fx-text-fill: #C0392B; -fx-font-weight: bold; -fx-padding: 4 10; -fx-background-radius: 12px;");
        }

        Button editBtn = new Button("✏️ Edit");
        editBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #A2D9CE; -fx-border-radius: 6px; -fx-text-fill: #1B2631; -fx-cursor: hand;");

        row.getChildren().addAll(iconLbl, info, sp, stockLbl, statusBadge, editBtn);
        return row;
    }
}
