package com.mainproject.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Products {

    private FarmerDashboard navigator;
    private final VBox productContainer = new VBox(12);

    // 1. Constructor with FarmerDashboard (Fixes the error!)
    public Products(FarmerDashboard navigator) {
        this.navigator = navigator;
    }

    // 2. Default No-arg Constructor
    public Products() {
    }

    public Node getView() {
        VBox root = new VBox(18);
        root.setPadding(new Insets(10));

        // Header Bar
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);

        VBox titles = new VBox(2);
        Label title = new Label("Products Management");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");
        Label sub = new Label("Manage your listed farm produce, update prices and stock.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        titles.getChildren().addAll(title, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addProductBtn = new Button("+ Add New Product");
        addProductBtn.setStyle("-fx-background-color: #117864; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 8 16; -fx-cursor: hand;");
        addProductBtn.setOnAction(e -> {
            if (navigator != null) {
                navigator.navigateTo("AddProduct");
            }
        });

        topBar.getChildren().addAll(titles, spacer, addProductBtn);

        // Filter Bar
        HBox filterBar = new HBox(10);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");
        searchField.setStyle("-fx-background-radius: 8px; -fx-border-color: #A2D9CE; -fx-border-radius: 8px; -fx-padding: 7 12;");
        searchField.setPrefWidth(260);

        HBox chips = new HBox(8);
        chips.getChildren().addAll(
            createFilterChip("All", true),
            createFilterChip("Active", false),
            createFilterChip("Inactive", false),
            createFilterChip("Sold Out", false)
        );

        filterBar.getChildren().addAll(searchField, chips);

        // Product Cards
        productContainer.getChildren().addAll(
            createProductCard("🍅", "Tomato", "₹20 / kg", "Available: 230 kg", "Active"),
            createProductCard("🧅", "Onion", "₹18 / kg", "Available: 150 kg", "Active"),
            createProductCard("🥔", "Potato", "₹16 / kg", "Available: 300 kg", "Active"),
            createProductCard("🌾", "Wheat", "₹25 / kg", "Available: 500 kg", "Inactive")
        );

        root.getChildren().addAll(topBar, filterBar, productContainer);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scroll;
    }

    private HBox createProductCard(String icon, String name, String price, String stock, String status) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(14, 18, 14, 18));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #A2D9CE; -fx-border-radius: 12px;");

        Label iconLbl = new Label(icon);
        iconLbl.setStyle("-fx-font-size: 30px;");

        VBox info = new VBox(2);
        Label nameLbl = new Label(name);
        nameLbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1B2631;");
        Label priceLbl = new Label(price);
        priceLbl.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #117864;");
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
        editBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #A2D9CE; -fx-border-radius: 6px; -fx-cursor: hand;");
        editBtn.setOnAction(e -> {
            if (navigator != null) {
                navigator.navigateTo("AddProduct");
            }
        });

        card.getChildren().addAll(iconLbl, info, sp, stockLbl, statusBadge, editBtn);
        return card;
    }

    private Button createFilterChip(String text, boolean active) {
        Button b = new Button(text);
        if (active) {
            b.setStyle("-fx-background-color: #117864; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20px; -fx-padding: 4 14; -fx-cursor: hand;");
        } else {
            b.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #A2D9CE; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-text-fill: #1B2631; -fx-padding: 4 14; -fx-cursor: hand;");
        }
        return b;
    }
}