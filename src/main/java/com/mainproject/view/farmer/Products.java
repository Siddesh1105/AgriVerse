package com.mainproject.view.farmer;


import java.util.ArrayList;
import java.util.List;

import com.mainproject.dao.ProductDAO;
import com.mainproject.model.Product;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Products {

    // =====================================================
    // VARIABLES
    // =====================================================

    private final FarmerDashboard navigator;

    private final String farmerEmail;

    private final ProductDAO productDAO;

    private final VBox productContainer = new VBox(12);

    private final List<Product> allProducts = new ArrayList<>();

    private TextField searchField;

    private ToggleButton allButton;
    private ToggleButton activeButton;
    private ToggleButton inactiveButton;
    private ToggleButton soldOutButton;

    private String selectedFilter = "All";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Products(FarmerDashboard navigator,String farmerEmail) {
        this.navigator = navigator;
        this.farmerEmail = farmerEmail;
        this.productDAO = new ProductDAO();
    }

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root = new VBox(18);

        root.setPadding(
                new Insets(10));

        // =================================================
        // HEADER
        // =================================================

        HBox topBar = new HBox();

        topBar.setAlignment(Pos.CENTER_LEFT);
        VBox titles = new VBox(3);
        Label title = new Label("Products Management");
        title.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: 800;" +
                        "-fx-text-fill: #1B2631;");

        Label sub = new Label(
                "Manage your listed farm produce, " +
                        "update prices and stock.");

        sub.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #566573;");

        titles.getChildren().addAll(
                title,
                sub);

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Button addProductBtn = new Button(
                "+ Add New Product");

        addProductBtn.setStyle(
                "-fx-background-color: #117864;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 8 16;" +
                        "-fx-cursor: hand;");

        addProductBtn.setOnAction(
                e -> navigator.navigateTo(
                        "AddProduct"));

        topBar.getChildren().addAll(
                titles,
                spacer,
                addProductBtn);

        // =================================================
        // FILTER BAR
        // =================================================

        HBox filterBar = new HBox(10);

        filterBar.setAlignment(
                Pos.CENTER_LEFT);

        searchField = new TextField();

        searchField.setPromptText(
                "Search products...");

        searchField.setPrefWidth(
                260);

        searchField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-padding: 7 12;");

        searchField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) -> refreshProductCards());

        // =================================================
        // FILTER BUTTONS
        // =================================================

        ToggleGroup filterGroup = new ToggleGroup();

        allButton = createFilterButton(
                "All",
                filterGroup);

        activeButton = createFilterButton(
                "Active",
                filterGroup);

        inactiveButton = createFilterButton(
                "Inactive",
                filterGroup);

        soldOutButton = createFilterButton(
                "Sold Out",
                filterGroup);

        allButton.setSelected(
                true);

        allButton.setOnAction(
                e -> {

                    selectedFilter = "All";

                    refreshFilterStyles();

                    refreshProductCards();
                });

        activeButton.setOnAction(
                e -> {

                    selectedFilter = "Active";

                    refreshFilterStyles();

                    refreshProductCards();
                });

        inactiveButton.setOnAction(
                e -> {

                    selectedFilter = "Inactive";

                    refreshFilterStyles();

                    refreshProductCards();
                });

        soldOutButton.setOnAction(
                e -> {

                    selectedFilter = "Sold Out";

                    refreshFilterStyles();

                    refreshProductCards();
                });

        filterBar.getChildren().addAll(
                searchField,
                allButton,
                activeButton,
                inactiveButton,
                soldOutButton);

        // =================================================
        // PRODUCT CONTAINER
        // =================================================

        productContainer.setPadding(
                new Insets(2, 0, 10, 0));

        // =================================================
        // LOAD PRODUCTS
        // =================================================

        loadProducts();

        root.getChildren().addAll(
                topBar,
                filterBar,
                productContainer);

        ScrollPane scroll = new ScrollPane(
                root);

        scroll.setFitToWidth(
                true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;");

        return scroll;
    }

    // =====================================================
    // CREATE FILTER BUTTON
    // =====================================================

    private ToggleButton createFilterButton(
            String text,
            ToggleGroup group) {

        ToggleButton button = new ToggleButton(
                text);

        button.setToggleGroup(
                group);

        button.setPrefHeight(
                34);

        button.setPadding(
                new Insets(
                        5,
                        16,
                        5,
                        16));

        button.setFocusTraversable(
                false);

        updateFilterButtonStyle(
                button,
                false);

        button.selectedProperty()
                .addListener(
                        (obs, oldValue, newValue) -> updateFilterButtonStyle(
                                button,
                                newValue));

        return button;
    }

    // =====================================================
    // FILTER BUTTON STYLE
    // =====================================================

    private void updateFilterButtonStyle(
            ToggleButton button,
            boolean selected) {

        if (selected) {

            button.setStyle(
                    "-fx-background-color: #117864;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 20px;" +
                            "-fx-border-color: #117864;" +
                            "-fx-border-radius: 20px;" +
                            "-fx-padding: 5 16;" +
                            "-fx-cursor: hand;");

        } else {

            button.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-text-fill: #1B2631;" +
                            "-fx-font-weight: normal;" +
                            "-fx-background-radius: 20px;" +
                            "-fx-border-color: #A2D9CE;" +
                            "-fx-border-radius: 20px;" +
                            "-fx-padding: 5 16;" +
                            "-fx-cursor: hand;");
        }
    }

    // =====================================================
    // REFRESH FILTER STYLES
    // =====================================================

    private void refreshFilterStyles() {

        updateFilterButtonStyle(
                allButton,
                allButton.isSelected());

        updateFilterButtonStyle(
                activeButton,
                activeButton.isSelected());

        updateFilterButtonStyle(
                inactiveButton,
                inactiveButton.isSelected());

        updateFilterButtonStyle(
                soldOutButton,
                soldOutButton.isSelected());
    }

    // =====================================================
    // LOAD PRODUCTS
    // =====================================================

    private void loadProducts() {

        productContainer.getChildren()
                .clear();

        allProducts.clear();

        if (farmerEmail == null
                ||
                farmerEmail.trim().isEmpty()) {

            showEmptyMessage(
                    "Farmer email not available.");

            return;
        }

        try {

            List<Product> products = productDAO.getFarmerProducts(
                    farmerEmail);

            if (products != null
                    &&
                    !products.isEmpty()) {

                allProducts.addAll(
                        products);

                refreshProductCards();

            } else {

                showEmptyMessage(
                        "You have not added any products yet.");
            }

        } catch (Exception e) {

            e.printStackTrace();

            showEmptyMessage(
                    "Unable to load products.");
        }
    }

    // =====================================================
    // REFRESH PRODUCT CARDS
    // =====================================================

    private void refreshProductCards() {

        if (productContainer == null) {
            return;
        }

        productContainer.getChildren()
                .clear();

        String searchText = searchField == null
                ? ""
                : searchField
                        .getText()
                        .trim()
                        .toLowerCase();

        int count = 0;

        for (Product product : allProducts) {

            if (product == null) {
                continue;
            }

            // ---------------------------------------------
            // SEARCH
            // ---------------------------------------------

            if (!searchText.isEmpty()) {

                String name = safe(
                        product.getName()).toLowerCase();

                String category = safe(
                        product.getCategory()).toLowerCase();

                if (!name.contains(
                        searchText)
                        &&
                        !category.contains(
                                searchText)) {

                    continue;
                }
            }

            // ---------------------------------------------
            // FILTER
            // ---------------------------------------------

            if (!matchesFilter(
                    product)) {

                continue;
            }

            productContainer.getChildren()
                    .add(
                            createProductCard(
                                    product));

            count++;
        }

        if (count == 0) {

            showEmptyMessage(
                    "No products found.");
        }
    }

    // =====================================================
    // FILTER LOGIC
    // =====================================================

    private boolean matchesFilter(
            Product product) {

        if ("All".equals(
                selectedFilter)) {

            return true;
        }

        String status = getDisplayStatus(
                product);

        return status.equalsIgnoreCase(
                selectedFilter);
    }

    // =====================================================
    // GET DISPLAY STATUS
    // =====================================================

    private String getDisplayStatus(
            Product product) {

        /*
         * Sold Out has priority.
         * This prevents an old "Active" status
         * from showing when stock is 0.
         */

        if (product.getStock() <= 0) {

            return "Sold Out";
        }

        String status = safe(
                product.getStatus());

        if (status.isEmpty()) {

            return "Active";
        }

        if ("Sold Out".equalsIgnoreCase(
                status)) {

            return "Sold Out";
        }

        if ("Inactive".equalsIgnoreCase(
                status)) {

            return "Inactive";
        }

        return "Active";
    }

    // =====================================================
    // CREATE PRODUCT CARD
    // =====================================================

    private HBox createProductCard(
            Product product) {

        HBox card = new HBox(15);

        card.setAlignment(
                Pos.CENTER_LEFT);

        card.setPadding(
                new Insets(
                        14,
                        18,
                        14,
                        18));

        card.setMaxWidth(
                Double.MAX_VALUE);

        card.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 12px;");

        // =================================================
        // IMAGE
        // =================================================

        StackPane imageBox = new StackPane();

        imageBox.setPrefSize(
                70,
                70);

        imageBox.setMinSize(
                70,
                70);

        imageBox.setMaxSize(
                70,
                70);

        imageBox.setStyle(
                "-fx-background-color: #E9F7EF;" +
                        "-fx-background-radius: 10px;");

        String imageUrl = safe(product.getImageUrl());

        if (!imageUrl.isEmpty()) {

            try {

                Image image = new Image(
                        imageUrl,
                        65,
                        65,
                        true,
                        true,
                        true);

                ImageView imageView = new ImageView(
                        image);

                imageView.setFitWidth(
                        65);

                imageView.setFitHeight(
                        65);

                imageView.setPreserveRatio(
                        true);

                imageBox.getChildren()
                        .add(
                                imageView);

            } catch (Exception e) {

                addImagePlaceholder(
                        imageBox);
            }

        } else {

            addImagePlaceholder(
                    imageBox);
        }

        // =================================================
        // PRODUCT INFO
        // =================================================

        VBox info = new VBox(3);

        Label nameLabel = new Label(
                safe(
                        product.getName()));

        nameLabel.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1B2631;");

        String category = safe(
                product.getCategory());

        Label categoryLabel = new Label(
                category
                        .isEmpty()
                                ? "Product"
                                : category);

        categoryLabel.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-text-fill: #566573;");

        Label priceLabel = new Label(
                "₹"
                        + formatNumber(
                                product.getPrice())
                        + " / "
                        + safe(
                                product.getUnit()));

        priceLabel.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #117864;");

        info.getChildren().addAll(
                nameLabel,
                categoryLabel,
                priceLabel);

        // =================================================
        // SPACER
        // =================================================

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        // =================================================
        // STOCK
        // =================================================

        Label stockLabel = new Label(
                "Stock: "
                        + formatNumber(
                                product.getStock())
                        + " "
                        + safe(
                                product.getUnit()));

        stockLabel.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #566573;");

        // =================================================
        // STATUS
        // =================================================

        String status = getDisplayStatus(
                product);

        Label statusBadge = createStatusBadge(
                status);

        // =================================================
        // EDIT BUTTON
        // =================================================

        Button editBtn = new Button(
                "✏ Edit");

        editBtn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 6px;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-padding: 6 10;" +
                        "-fx-cursor: hand;");

        editBtn.setOnAction(
                e -> navigator.navigateToEditProduct(
                        product));

        // =================================================
        // DELETE BUTTON
        // =================================================

        Button deleteBtn = new Button(
                "🗑");

        deleteBtn.setStyle(
                "-fx-background-color: #FDEDEC;" +
                        "-fx-text-fill: #C0392B;" +
                        "-fx-border-color: #F5B7B1;" +
                        "-fx-border-radius: 6px;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-padding: 6 9;" +
                        "-fx-cursor: hand;");

        deleteBtn.setOnAction(
                e -> deleteProduct(
                        product));

        card.getChildren().addAll(
                imageBox,
                info,
                spacer,
                stockLabel,
                statusBadge,
                editBtn,
                deleteBtn);

        return card;
    }

    // =====================================================
    // STATUS BADGE
    // =====================================================

    private Label createStatusBadge(
            String status) {

        Label badge = new Label(
                status);

        if ("Active".equalsIgnoreCase(
                status)) {

            badge.setStyle(
                    "-fx-background-color: #D4EFDF;" +
                            "-fx-text-fill: #117864;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 5 10;" +
                            "-fx-background-radius: 12px;");

        } else if ("Sold Out".equalsIgnoreCase(
                status)) {

            badge.setStyle(
                    "-fx-background-color: #FADBD8;" +
                            "-fx-text-fill: #C0392B;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 5 10;" +
                            "-fx-background-radius: 12px;");

        } else {

            badge.setStyle(
                    "-fx-background-color: #EAECEE;" +
                            "-fx-text-fill: #566573;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 5 10;" +
                            "-fx-background-radius: 12px;");
        }

        return badge;
    }

    // =====================================================
    // DELETE PRODUCT
    // =====================================================

    private void deleteProduct(
            Product product) {

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION);

        confirm.setTitle(
                "Delete Product");

        confirm.setHeaderText(
                "Delete "
                        + safe(
                                product.getName())
                        + "?");

        confirm.setContentText(
                "This product will be removed from Firestore.");

        confirm.showAndWait()
                .ifPresent(
                        result -> {

                            if (result == javafx.scene.control.ButtonType.OK) {

                                boolean deleted = productDAO
                                        .deleteProduct(
                                                product
                                                        .getProductId());

                                if (deleted) {

                                    allProducts.remove(
                                            product);

                                    refreshProductCards();

                                    showInfo(
                                            "Product deleted successfully.");

                                } else {

                                    showError(
                                            "Unable to delete product.");
                                }
                            }
                        });
    }

    // =====================================================
    // IMAGE PLACEHOLDER
    // =====================================================

    private void addImagePlaceholder(
            StackPane imageBox) {

        Label placeholder = new Label(
                "🌱");

        placeholder.setStyle(
                "-fx-font-size: 25px;");

        imageBox.getChildren()
                .add(
                        placeholder);
    }

    // =====================================================
    // EMPTY MESSAGE
    // =====================================================

    private void showEmptyMessage(
            String message) {

        Label empty = new Label(
                message);

        empty.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #566573;" +
                        "-fx-padding: 30px;");

        productContainer.getChildren()
                .add(
                        empty);
    }

    // =====================================================
    // STRING SAFE
    // =====================================================

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }

    // =====================================================
    // FORMAT NUMBER
    // =====================================================

    private String formatNumber(
            double value) {

        if (value == Math.floor(value)) {

            return String.format(
                    "%.0f",
                    value);
        }

        return String.format(
                "%.2f",
                value);
    }

    // =====================================================
    // INFO ALERT
    // =====================================================

    private void showInfo(
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);

        alert.setTitle(
                "Products");

        alert.setHeaderText(
                null);

        alert.setContentText(
                message);

        alert.showAndWait();
    }

    // =====================================================
    // ERROR ALERT
    // =====================================================

    private void showError(
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.ERROR);

        alert.setTitle(
                "Products");

        alert.setHeaderText(
                "Error");

        alert.setContentText(
                message);

        alert.showAndWait();
    }
}