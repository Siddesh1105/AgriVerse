package com.mainproject.view.buyer;

import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SearchFilter {

    private final BuyerDashboard mainController;

    public SearchFilter(BuyerDashboard controller) {
        this.mainController = controller;
    }

    public Node getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Filter Sidebar (Left)
        VBox filters = new VBox(15);
        filters.setPrefWidth(220);
        filters.setPadding(new Insets(15));
        filters.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 10; -fx-background-radius: 10;");

        filters.getChildren().addAll(
            new Label("Category:"),
            new ComboBox<>(javafx.collections.FXCollections.observableArrayList("All Categories", "Vegetables", "Grains", "Fruits")),
            new Label("Max Price (₹):"),
            new Slider(10, 200, 50),
            new Label("Location:"),
            new TextField("Nashik, Maharashtra"),
            new Button("Apply Filters")
        );

        // Results Grid (Center)
        VBox resultsBox = new VBox(15);
        resultsBox.setPadding(new Insets(0, 0, 0, 15));

        TextField searchBar = new TextField();
        searchBar.setPromptText("🔍 Search products, crops, farmers...");
        searchBar.setPrefHeight(38);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        grid.add(createResultCard("Fresh Tomato", "Ramesh Patil", "₹28/kg", "4.6"), 0, 0);
        grid.add(createResultCard("Organic Wheat", "Green Valley Farm", "₹26/kg", "4.7"), 1, 0);
        grid.add(createResultCard("Onion", "Mahesh Farm", "₹22/kg", "4.5"), 0, 1);
        grid.add(createResultCard("Potato", "Patel Farm", "₹20/kg", "4.4"), 1, 1);

        resultsBox.getChildren().addAll(searchBar, grid);

        root.setLeft(filters);
        root.setCenter(resultsBox);
        LanguageManager.apply(root);
        return root;
    }

    private VBox createResultCard(String crop, String farmer, String price, String rating) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(12));
        card.setPrefWidth(240);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");

        Label title = new Label(crop);
        title.setStyle("-fx-font-weight: bold;");
        Label seller = new Label(farmer);
        seller.setStyle("-fx-text-fill: #64748B; -fx-font-size: 11px;");
        Label rate = new Label(price + " ★ " + rating);
        rate.setStyle("-fx-text-fill: #166534; -fx-font-weight: bold;");

        Button btnView = new Button("View Product");
        btnView.setOnAction(e -> mainController.setView(new ProductDetails(mainController).getView()));

        card.getChildren().addAll(title, seller, rate, btnView);
        return card;
    }
}