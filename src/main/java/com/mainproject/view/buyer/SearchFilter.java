package com.mainproject.view.buyer;

import com.mainproject.model.Product;

import javafx.collections.FXCollections;
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

        root.setPadding(
                new Insets(20)
        );

        // =====================================================
        // FILTER SIDEBAR
        // =====================================================

        VBox filters =
                new VBox(15);

        filters.setPrefWidth(220);

        filters.setPadding(
                new Insets(15)
        );

        filters.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-border-color: #E2E8F0;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;"
        );

        Label categoryLabel =
                new Label("Category:");

        ComboBox<String> category =
                new ComboBox<>(
                        FXCollections.observableArrayList(
                                "All Categories",
                                "Vegetables",
                                "Grains",
                                "Fruits"
                        )
                );

        category.setValue(
                "All Categories"
        );

        Label priceLabel =
                new Label("Max Price (₹):");

        Slider priceSlider =
                new Slider(
                        10,
                        200,
                        50
                );

        Label locationLabel =
                new Label("Location:");

        TextField location =
                new TextField(
                        "Nashik, Maharashtra"
                );

        Button applyFilters =
                new Button("Apply Filters");

        filters.getChildren().addAll(
                categoryLabel,
                category,
                priceLabel,
                priceSlider,
                locationLabel,
                location,
                applyFilters
        );

        // =====================================================
        // RESULTS
        // =====================================================

        VBox resultsBox =
                new VBox(15);

        resultsBox.setPadding(
                new Insets(0, 0, 0, 15)
        );

        TextField searchBar =
                new TextField();

        searchBar.setPromptText(
                "🔍 Search products, crops, farmers..."
        );

        searchBar.setPrefHeight(38);

        // =====================================================
        // PRODUCT GRID
        // =====================================================

        GridPane grid =
                new GridPane();

        grid.setHgap(15);
        grid.setVgap(15);

        // =====================================================
        // PRODUCT 1
        // =====================================================

        grid.add(
                createResultCard(
                        "Fresh Tomato",
                        "Ramesh Patil",
                        "₹28/kg",
                        "4.6"
                ),
                0,
                0
        );

        // =====================================================
        // PRODUCT 2
        // =====================================================

        grid.add(
                createResultCard(
                        "Organic Wheat",
                        "Green Valley Farm",
                        "₹26/kg",
                        "4.7"
                ),
                1,
                0
        );

        // =====================================================
        // PRODUCT 3
        // =====================================================

        grid.add(
                createResultCard(
                        "Onion",
                        "Mahesh Farm",
                        "₹22/kg",
                        "4.5"
                ),
                0,
                1
        );

        // =====================================================
        // PRODUCT 4
        // =====================================================

        grid.add(
                createResultCard(
                        "Potato",
                        "Patel Farm",
                        "₹20/kg",
                        "4.4"
                ),
                1,
                1
        );

        resultsBox.getChildren().addAll(
                searchBar,
                grid
        );

        // =====================================================
        // APPLY FILTERS
        // =====================================================

        applyFilters.setOnAction(e -> {

            String selectedCategory =
                    category.getValue();

            double maxPrice =
                    priceSlider.getValue();

            String selectedLocation =
                    location.getText();

            System.out.println(
                    "Category: "
                            + selectedCategory
            );

            System.out.println(
                    "Max Price: ₹"
                            + maxPrice
            );

            System.out.println(
                    "Location: "
                            + selectedLocation
            );
        });

        // =====================================================
        // SEARCH
        // =====================================================

        searchBar.textProperty()
                .addListener(
                        (observable,
                         oldValue,
                         newValue) -> {

                            System.out.println(
                                    "Searching: "
                                            + newValue
                            );
                        }
                );

        // =====================================================
        // SET ROOT
        // =====================================================

        root.setLeft(filters);

        root.setCenter(resultsBox);

        // =====================================================
        // LANGUAGE
        // =====================================================


        return root;
    }

    // =====================================================
    // CREATE RESULT CARD
    // =====================================================

    private VBox createResultCard(
            String crop,
            String farmer,
            String price,
            String rating) {

        VBox card =
                new VBox(6);

        card.setPadding(
                new Insets(12)
        );

        card.setPrefWidth(240);

        card.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-border-color: #E2E8F0;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );

        // =====================================================
        // PRODUCT NAME
        // =====================================================

        Label title =
                new Label(crop);

        title.setStyle(
                "-fx-font-weight: bold;"
        );

        // =====================================================
        // FARMER
        // =====================================================

        Label seller =
                new Label(farmer);

        seller.setStyle(
                "-fx-text-fill: #64748B;" +
                "-fx-font-size: 11px;"
        );

        // =====================================================
        // PRICE + RATING
        // =====================================================

        Label rate =
                new Label(
                        price
                                + " ★ "
                                + rating
                );

        rate.setStyle(
                "-fx-text-fill: #166534;" +
                "-fx-font-weight: bold;"
        );

        // =====================================================
        // VIEW PRODUCT
        // =====================================================

        Button btnView =
                new Button(
                        "View Product"
                );

        btnView.setMaxWidth(
                Double.MAX_VALUE
        );

        btnView.setStyle(
                "-fx-background-color:#166534;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:6;" +
                "-fx-padding:8;" +
                "-fx-cursor:hand;"
        );

        btnView.setOnAction(e -> {

            /*
             * Create Product model for the
             * selected product.
             */

            Product product =
                    new Product();

            product.setName(
                    crop
            );

            product.setFarmerEmail(
                    farmer
            );

            /*
             * Convert price text:
             *
             * ₹28/kg
             *      ↓
             * 28
             */

            try {

                String numericPrice =
                        price.replace(
                                "₹",
                                ""
                        ).replace(
                                "/kg",
                                ""
                        ).trim();

                product.setPrice(
                        Double.parseDouble(
                                numericPrice
                        )
                );

            } catch (Exception ex) {

                product.setPrice(
                        0
                );
            }

            /*
             * Default values for the
             * static products.
             */

            product.setUnit(
                    "kg"
            );

            product.setStatus(
                    "Available"
            );

            /*
             * IMPORTANT:
             *
             * ProductDetails requires:
             *
             * BuyerDashboard
             * +
             * Product
             */

            mainController.setView(
                    new ProductDetails(
                            mainController,
                            product
                    ).getView()
            );
        });

        card.getChildren().addAll(
                title,
                seller,
                rate,
                btnView
        );

        return card;
    }
}