package com.mainproject.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class BuyerDashboard {

    private static final String GREEN = "#2E7D32";
    private static final String BG = "#f1efef";
    private static final String BORDER = "#e5e5e5";
    private static final String MUTED = "#888888";

    private Scene buyerDashboardScene;

    Scene getBuyerDashboardScene() {

        VBox sidebar = buildSidebar();
        VBox content = buildContent();

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + BG + "; -fx-background-color: " + BG + "; -fx-border-width: 0;");

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(scroll);
        root.setStyle("-fx-background-color: " + BG + ";");

        buyerDashboardScene = new Scene(root, 1600, 1000);
        buyerDashboardScene.setFill(Color.WHITE);
        return buyerDashboardScene;
    }

    private VBox buildSidebar() {

        Text logoIcon = new Text("🌿");
        logoIcon.setStyle("-fx-font-size: 22px;");
        Text logoText = new Text("AgriLink");
        logoText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: " + GREEN + ";");
        HBox logoBox = new HBox(8, logoIcon, logoText);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setPadding(new Insets(20, 20, 25, 20));

        String[][] items = {
                { "🏠", "Dashboard" },
                { "🛒", "Marketplace" },
                { "🧾", "Orders" },
                { "⭐", "Reviews" },
                { "👨‍🌾", "Farmers" },
                { "🛍️", "Cart" },
                { "🔔", "Notifications" },
                { "👤", "Profile" }
        };

        ToggleGroup group = new ToggleGroup();
        VBox navBox = new VBox(4);
        navBox.setPadding(new Insets(0, 10, 0, 10));

        for (int i = 0; i < items.length; i++) {
            ToggleButton navItem = createNavItem(items[i][0], items[i][1], group);
            if (i == 0) {
                navItem.setSelected(true);
            }
            navBox.getChildren().add(navItem);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        ToggleButton logoutItem = createNavItem("🚪", "Logout", null);
        logoutItem.setOnAction(e -> LoginScreen.logoutToLogin());
        VBox logoutBox = new VBox(logoutItem);
        logoutBox.setPadding(new Insets(0, 10, 20, 10));

        VBox sidebar = new VBox(logoBox, navBox, spacer, logoutBox);
        sidebar.setPrefWidth(230);
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: " + BORDER + "; -fx-border-width: 0 1 0 0;");
        return sidebar;
    }

    private ToggleButton createNavItem(String icon, String label, ToggleGroup group) {
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 15px;");
        Text labelText = new Text(label);
        labelText.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        HBox inner = new HBox(10, iconText, labelText);
        inner.setAlignment(Pos.CENTER_LEFT);

        ToggleButton item = new ToggleButton();
        item.setGraphic(inner);
        item.setMaxWidth(Double.MAX_VALUE);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPrefHeight(40);
        if (group != null) {
            item.setToggleGroup(group);
        }

        item.setStyle(navStyle(false));
        item.selectedProperty().addListener((obs, was, isSel) -> item.setStyle(navStyle(isSel)));

        return item;
    }

    private String navStyle(boolean active) {
        if (active) {
            return "-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 8px; "
                    + "-fx-border-width: 0;";
        }
        return "-fx-background-color: transparent; -fx-text-fill: #444444; -fx-background-radius: 8px; "
                + "-fx-border-width: 0;";
    }

    private VBox buildContent() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(30, 40, 40, 40));

        content.getChildren().addAll(
                buildHeader(),
                buildStatCards(),
                buildRecommendedSection());

        return content;
    }

    private VBox buildHeader() {
        Text title = new Text("Buyer Dashboard");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Circle avatar = new Circle(16, Color.web(GREEN));
        Text initials = new Text("MK");
        initials.setStyle("-fx-fill: white; -fx-font-size: 11px; -fx-font-weight: bold;");
        StackPane avatarPane = new StackPane(avatar, initials);

        Text userName = new Text("Mahesh Kumar");
        userName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        HBox userBox = new HBox(8, avatarPane, userName);
        userBox.setAlignment(Pos.CENTER);

        HBox headerRow = new HBox(20, title, spacer, userBox);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Text welcome = new Text("Welcome back, Mahesh Kumar");
        welcome.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        Text subtitle = new Text("Find the best quality products from trusted farmers.");
        subtitle.setStyle("-fx-font-size: 12px; -fx-fill: " + MUTED + ";");

        return new VBox(4, headerRow, welcome, subtitle);
    }

    private HBox buildStatCards() {
        HBox row = new HBox(20,
                createStatCard("🛒", "Orders Placed", "15", "This month"),
                createStatCard("🚚", "Orders Delivered", "10", "This month"),
                createStatCard("💰", "Total Spent", "₹45,230", "This month"),
                createStatCard("❤️", "Wishlist", "8", "Items"));
        for (int i = 0; i < row.getChildren().size(); i++) {
            HBox.setHgrow(row.getChildren().get(i), Priority.ALWAYS);
        }
        return row;
    }

    private VBox createStatCard(String icon, String label, String value, String sub) {
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 20px;");

        Text labelText = new Text(label);
        labelText.setStyle("-fx-font-size: 12px; -fx-fill: " + MUTED + "; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(labelText, spacer, iconText);
        top.setAlignment(Pos.CENTER_LEFT);

        Text valueText = new Text(value);
        valueText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Text subText = new Text(sub);
        subText.setStyle("-fx-font-size: 11px; -fx-fill: " + MUTED + ";");

        VBox card = new VBox(10, top, valueText, subText);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12px; "
                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12px;");
        card.setPrefWidth(250);
        return card;
    }

    private VBox buildRecommendedSection() {
        Text heading = new Text("Recommended for You");
        heading.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox productsRow = new HBox(18,
                createProductCard("🍅", "Fresh Tomatoes", "₹20 / kg", "Pune, Maharashtra", "Add to Cart"),
                createProductCard("🧅", "Onions", "₹18 / kg", "Nashik, Maharashtra", "Add to Cart"),
                createProductCard("🥔", "Potatoes", "₹15 / kg", "Solapur, Maharashtra", "Add to Cart"),
                createProductCard("🫛", "Green Chillies", "₹40 / kg", "Pune, Maharashtra", "Add Details"));

        for (int i = 0; i < productsRow.getChildren().size(); i++) {
            HBox.setHgrow(productsRow.getChildren().get(i), Priority.ALWAYS);
        }

        VBox panel = new VBox(16, heading, productsRow);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 12px; "
                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 12px;");
        return panel;
    }

    private VBox createProductCard(String emoji, String name, String price, String location, String actionLabel) {
        Text imagePlaceholder = new Text(emoji);
        imagePlaceholder.setStyle("-fx-font-size: 46px;");
        StackPane imageBox = new StackPane(imagePlaceholder);
        imageBox.setPrefSize(Double.MAX_VALUE, 100);
        imageBox.setStyle("-fx-background-color: #f4f4f4; -fx-background-radius: 10px;");

        Text nameText = new Text(name);
        nameText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Text priceText = new Text(price);
        priceText.setStyle("-fx-font-size: 13px; -fx-fill: " + GREEN + "; -fx-font-weight: bold;");

        Text locationText = new Text("📍 " + location);
        locationText.setStyle("-fx-font-size: 11px; -fx-fill: " + MUTED + ";");
        locationText.setTextAlignment(TextAlignment.LEFT);

        Button actionBtn = new Button(actionLabel);
        actionBtn.setMaxWidth(Double.MAX_VALUE);
        actionBtn.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-background-radius: 8px; -fx-font-size: 12px;");

        VBox card = new VBox(8, imageBox, nameText, priceText, locationText, actionBtn);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10px; "
                + "-fx-border-color: " + BORDER + "; -fx-border-radius: 10px;");
        card.setPrefWidth(220);
        return card;
    }
}