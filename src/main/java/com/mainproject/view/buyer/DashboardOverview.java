package com.mainproject.view.buyer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class DashboardOverview {

    private final BuyerDashboard mainController;

    public DashboardOverview(BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25, 30, 25, 30));

        // Header greeting & top bar
        BorderPane header = new BorderPane();
        Label lblGreeting = new Label("Good Morning, Buyer 👋\nWelcome back to AgriLink");
        lblGreeting.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1E293B;");

        HBox topActions = new HBox(12);
        topActions.setAlignment(Pos.CENTER_RIGHT);
        Button btnCart = new Button("🛒 Cart (3)");
        btnCart.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 8; -fx-padding: 8 14; -fx-cursor: hand;");
        btnCart.setOnAction(e -> mainController.setView(new ShoppingCart(mainController).getView()));

        Label lblUser = new Label("👤 Rahul Sharma (Buyer)");
        lblUser.setStyle("-fx-font-weight: bold; -fx-text-fill: #166534;");
        topActions.getChildren().addAll(btnCart, lblUser);

        header.setLeft(lblGreeting);
        header.setRight(topActions);
        root.getChildren().add(header);

        // 4 Summary Metric Cards
        HBox metrics = new HBox(15);
        metrics.getChildren().addAll(
            createMetricCard("Total Orders", "24", "View all orders", "#166534"),
            createMetricCard("Pending Orders", "6", "Awaiting delivery", "#CA8A04"),
            createMetricCard("Completed Orders", "18", "View history", "#22C55E"),
            createMetricCard("Cart Items", "3", "View cart", "#2563EB")
        );
        root.getChildren().add(metrics);

        // Middle Row: Live Farmers & Recent Orders
        HBox midRow = new HBox(20);
        VBox liveFarmersBox = createLiveFarmersSection();
        VBox recentOrdersBox = createRecentOrdersSection();
        HBox.setHgrow(liveFarmersBox, Priority.ALWAYS);
        HBox.setHgrow(recentOrdersBox, Priority.ALWAYS);
        midRow.getChildren().addAll(liveFarmersBox, recentOrdersBox);
        root.getChildren().add(midRow);

        // Bottom Row: Recommendations & AI Promo
        HBox bottomRow = new HBox(20);
        VBox recsBox = createRecommendationsSection();
        VBox aiBox = createAiPromoSection();
        HBox.setHgrow(recsBox, Priority.ALWAYS);
        HBox.setHgrow(aiBox, Priority.ALWAYS);
        bottomRow.getChildren().addAll(recsBox, aiBox);
        root.getChildren().add(bottomRow);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scrollPane;
    }

    private VBox createMetricCard(String title, String value, String subtitle, String colorHex) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 10; -fx-background-radius: 10;");
        HBox.setHgrow(card, Priority.ALWAYS);

        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-text-fill: #64748B; -fx-font-size: 12px;");
        Label lblVal = new Label(value);
        lblVal.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + colorHex + ";");
        Label lblSub = new Label(subtitle);
        lblSub.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 11px;");

        card.getChildren().addAll(lblTitle, lblVal, lblSub);
        return card;
    }

    private VBox createLiveFarmersSection() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 10; -fx-background-radius: 10;");

        BorderPane titleBar = new BorderPane();
        titleBar.setLeft(new Label("Live Farmers 🔴"));
        Hyperlink hlViewAll = new Hyperlink("View All");
        hlViewAll.setOnAction(e -> mainController.setView(new LiveMarketplace(mainController).getView()));
        titleBar.setRight(hlViewAll);

        HBox cards = new HBox(10);
        cards.getChildren().addAll(
            createMiniStreamCard("Ramesh Patil", "Tomato", "324 viewers"),
            createMiniStreamCard("Suresh Farm", "Mango", "176 viewers"),
            createMiniStreamCard("Mahesh Farm", "Onion", "96 viewers")
        );

        box.getChildren().addAll(titleBar, cards);
        return box;
    }

    private VBox createMiniStreamCard(String farmer, String crop, String viewers) {
        VBox card = new VBox(4);
        card.setPadding(new Insets(8));
        card.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-radius: 8; -fx-background-radius: 8;");
        Label name = new Label(farmer);
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        Label details = new Label(crop + " • " + viewers);
        details.setStyle("-fx-text-fill: #64748B; -fx-font-size: 11px;");
        card.getChildren().addAll(name, details);
        return card;
    }

    private VBox createRecentOrdersSection() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 10; -fx-background-radius: 10;");

        BorderPane titleBar = new BorderPane();
        titleBar.setLeft(new Label("Recent Orders 📦"));
        Hyperlink hlViewAll = new Hyperlink("View All");
        hlViewAll.setOnAction(e -> mainController.setView(new MyOrders (mainController).getView()));
        titleBar.setRight(hlViewAll);

        VBox orderList = new VBox(6);
        orderList.getChildren().addAll(
            createOrderItemRow("Order #1024", "Tomato (20 kg)", "₹600", "Delivered", "#166534"),
            createOrderItemRow("Order #1023", "Wheat (50 kg)", "₹1,250", "Processing", "#CA8A04"),
            createOrderItemRow("Order #1022", "Potato (30 kg)", "₹480", "Delivered", "#166534")
        );

        box.getChildren().addAll(titleBar, orderList);
        return box;
    }

    private HBox createOrderItemRow(String id, String desc, String price, String status, String color) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        Label lblId = new Label(id + " - " + desc);
        lblId.setStyle("-fx-font-size: 12px;");
        HBox.setHgrow(lblId, Priority.ALWAYS);

        Label lblPriceStatus = new Label(price + " [" + status + "]");
        lblPriceStatus.setStyle("-fx-font-weight: bold; -fx-text-fill: " + color + "; -fx-font-size: 12px;");

        row.getChildren().addAll(lblId, lblPriceStatus);
        return row;
    }

    private VBox createRecommendationsSection() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label lbl = new Label("Recommended for You");
        lbl.setStyle("-fx-font-weight: bold;");

        HBox items = new HBox(10);
        items.getChildren().addAll(
            createProductBadge("Fresh Tomato", "₹28/kg", "4.6"),
            createProductBadge("Organic Wheat", "₹26/kg", "4.7"),
            createProductBadge("Alphonso Mango", "₹120/kg", "4.8")
        );
        box.getChildren().addAll(lbl, items);
        return box;
    }

    private VBox createProductBadge(String name, String price, String rating) {
        VBox badge = new VBox(3);
        badge.setPadding(new Insets(8));
        badge.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");
        badge.getChildren().addAll(
            new Label(name),
            new Label(price + " ★ " + rating)
        );
        return badge;
    }

    private VBox createAiPromoSection() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label title = new Label("🤖 AI Smart Recommendations");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label desc = new Label("Find the best verified prices & seasonal crop trends near you.");
        desc.setStyle("-fx-text-fill: #64748B; -fx-font-size: 12px;");
        desc.setWrapText(true);

        Button btnTry = new Button("Try Smart Assistant");
        btnTry.setStyle("-fx-background-color: #166534; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 8; -fx-cursor: hand;");
        btnTry.setOnAction(e -> mainController.setView(new AiRecommendations(mainController).getView()));

        box.getChildren().addAll(title, desc, btnTry);
        return box;
    }
}