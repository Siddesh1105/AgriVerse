package com.mainproject.view.buyer;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LiveMarketplace {

    private final BuyerDashboard mainController;

    public LiveMarketplace(BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25, 30, 25, 30));

        Label title = new Label("Live Farmers 🔴 Streaming Now");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1E293B;");

        // Category Filter Tabs
        HBox tabs = new HBox(8);
        String[] cats = {"All", "Vegetables", "Fruits", "Grains", "Others"};
        for (String cat : cats) {
            Button btn = new Button(cat);
            btn.setStyle(cat.equals("All")
                ? "-fx-background-color: #166534; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 6 16;"
                : "-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 20; -fx-padding: 6 16;");
            tabs.getChildren().add(btn);
        }

        // Marketplace Grid (3 columns)
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        grid.add(createFarmerStreamCard("Ramesh Patil", "Tomato", "₹28/kg", "324 viewers"), 0, 0);
        grid.add(createFarmerStreamCard("Suresh Farm", "Mango", "₹120/kg", "176 viewers"), 1, 0);
        grid.add(createFarmerStreamCard("Mahesh Farm", "Onion", "₹22/kg", "96 viewers"), 2, 0);
        grid.add(createFarmerStreamCard("Green Valley", "Chilli", "₹45/kg", "82 viewers"), 0, 1);
        grid.add(createFarmerStreamCard("Patel Farm", "Cabbage", "₹18/kg", "64 viewers"), 1, 1);
        grid.add(createFarmerStreamCard("Organic Farm", "Potato", "₹20/kg", "58 viewers"), 2, 1);

        root.getChildren().addAll(title, tabs, grid);

        ScrollPane sp = new ScrollPane(root);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return sp;
    }

    private VBox createFarmerStreamCard(String farmer, String crop, String price, String viewers) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setPrefWidth(300);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label badge = new Label("🔴 LIVE • " + viewers);
        badge.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626; -fx-padding: 3 8; -fx-background-radius: 4; -fx-font-size: 11px;");

        Label lblName = new Label(farmer);
        lblName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label lblCrop = new Label("Harvest: " + crop + " | Rate: " + price);
        lblCrop.setStyle("-fx-text-fill: #64748B;");

        Button btnWatch = new Button("Watch Stream & Buy");
        btnWatch.setMaxWidth(Double.MAX_VALUE);
        btnWatch.setStyle("-fx-background-color: #166534; -fx-text-fill: white; -fx-padding: 8; -fx-background-radius: 6; -fx-cursor: hand;");
        btnWatch.setOnAction(e -> mainController.setView(new LiveViewerScreen(mainController).getView()));

        card.getChildren().addAll(badge, lblName, lblCrop, btnWatch);
        return card;
    }
}