package com.mainproject.view.farmer;



import java.util.ArrayList;
import java.util.List;

import com.mainproject.controller.ProductController;
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

public class MarketPlace {

    // =====================================================
    // VARIABLES
    // =====================================================

    private final FarmerDashboard navigator;

    private final ProductController productController;

    private final VBox productList = new VBox(12);

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

    public MarketPlace(
            FarmerDashboard navigator) {

        this.navigator = navigator;

        this.productController = new ProductController();
    }

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root = new VBox(16);

        root.setPadding(
                new Insets(10));

        // =================================================
        // HEADER
        // =================================================

        HBox top = new HBox();

        top.setAlignment(
                Pos.CENTER_LEFT);

        VBox titles = new VBox(3);

        Label pageTitle = new Label(
                "Marketplace");

        pageTitle.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: 800;" +
                        "-fx-text-fill: #1B2631;");

        Label sub = new Label(
                "Explore farm products from farmers on AgriLink.");

        sub.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #566573;");

        titles.getChildren().addAll(
                pageTitle,
                sub);

        Region headerSpacer = new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS);

        Button addNewBtn = new Button(
                "+ Add New Product");

        addNewBtn.setStyle(
                "-fx-background-color: #117864;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 8 16;" +
                        "-fx-cursor: hand;");

        addNewBtn.setOnAction(
                e -> navigator.navigateTo(
                        "AddProduct"));

        top.getChildren().addAll(
                titles,
                headerSpacer,
                addNewBtn);

        // =================================================
        // SEARCH + FILTER
        // =================================================

        HBox filterRow = new HBox(10);

        filterRow.setAlignment(
                Pos.CENTER_LEFT);

        searchField = new TextField();

        searchField.setPromptText(
                "Search products...");

        searchField.setPrefWidth(
                270);

        searchField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-padding: 8 12;");

        searchField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) -> refreshProducts());

        // =================================================
        // FILTER GROUP
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

        // =================================================
        // FILTER ACTIONS
        // =================================================

        allButton.setOnAction(
                e -> {

                    selectedFilter = "All";

                    refreshProducts();
                });

        activeButton.setOnAction(
                e -> {

                    selectedFilter = "Active";

                    refreshProducts();
                });

        inactiveButton.setOnAction(
                e -> {

                    selectedFilter = "Inactive";

                    refreshProducts();
                });

        soldOutButton.setOnAction(
                e -> {

                    selectedFilter = "Sold Out";

                    refreshProducts();
                });

        filterRow.getChildren().addAll(
                searchField,
                allButton,
                activeButton,
                inactiveButton,
                soldOutButton);

        // =================================================
        // PRODUCT LIST
        // =================================================

        productList.setPadding(
                new Insets(
                        2,
                        0,
                        20,
                        0));

        // =================================================
        // LOAD FROM FIRESTORE
        // =================================================

        loadProducts();

        // =================================================
        // ADD EVERYTHING
        // =================================================

        root.getChildren().addAll(
                top,
                filterRow,
                productList);

        // =================================================
        // SCROLL
        // =================================================

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

        updateFilterStyle(
                button,
                false);

        button.selectedProperty()
                .addListener(
                        (obs, oldValue, selected) -> updateFilterStyle(
                                button,
                                selected));

        return button;
    }

    // =====================================================
    // FILTER CSS
    // =====================================================

    private void updateFilterStyle(
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
                    "-fx-background-color: #FFFFFF;" +
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
    // LOAD PRODUCTS FROM FIRESTORE
    // =====================================================

    private void loadProducts() {

        productList.getChildren()
                .clear();

        allProducts.clear();

        try {

            System.out.println(
                    "Loading marketplace products...");

            List<Product> products = productController.getAllProducts();

            if (products == null
                    ||
                    products.isEmpty()) {

                showEmptyMessage(
                        "No products available in marketplace.");

                return;
            }

            allProducts.addAll(
                    products);

            System.out.println(
                    "Marketplace products: "
                            + allProducts.size());

            refreshProducts();

        } catch (Exception e) {

            e.printStackTrace();

            showEmptyMessage(
                    "Unable to load products.");
        }
    }

    // =====================================================
    // REFRESH PRODUCTS
    // =====================================================

    private void refreshProducts() {

        productList.getChildren()
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

            // =============================================
            // SEARCH
            // =============================================

            if (!searchText.isEmpty()) {

                String name = safe(
                        product.getName()).toLowerCase();

                String category = safe(
                        product.getCategory()).toLowerCase();

                String farmer = safe(
                        product.getFarmerEmail()).toLowerCase();

                if (!name.contains(
                        searchText)
                        &&
                        !category.contains(
                                searchText)
                        &&
                        !farmer.contains(
                                searchText)) {

                    continue;
                }
            }

            // =============================================
            // FILTER
            // =============================================

            if (!matchesFilter(
                    product)) {

                continue;
            }

            productList.getChildren()
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
         * Stock has priority.
         *
         * If stock is 0 or less,
         * product is always Sold Out.
         */

        if (product.getStock() <= 0) {

            return "Sold Out";
        }

        String status = safe(
                product.getStatus());

        if (status.isEmpty()) {

            return "Active";
        }

        if ("Inactive".equalsIgnoreCase(
                status)) {

            return "Inactive";
        }

        if ("Sold Out".equalsIgnoreCase(
                status)) {

            return "Sold Out";
        }

        return "Active";
    }

    // =====================================================
    // PRODUCT CARD
    // =====================================================

    private HBox createProductCard(
            Product product) {

        HBox row = new HBox(15);

        row.setAlignment(
                Pos.CENTER_LEFT);

        row.setPadding(
                new Insets(
                        14,
                        18,
                        14,
                        18));

        row.setMaxWidth(
                Double.MAX_VALUE);

        row.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 12px;");

        // =================================================
        // IMAGE
        // =================================================

        StackPane imageBox = new StackPane();

        imageBox.setPrefSize(
                75,
                75);

        imageBox.setMinSize(
                75,
                75);

        imageBox.setMaxSize(
                75,
                75);

        imageBox.setStyle(
                "-fx-background-color: #E9F7EF;" +
                        "-fx-background-radius: 10px;");

        String imageUrl = safe(
                product.getImageUrl());

        if (!imageUrl.isEmpty()) {

            try {

                Image image = new Image(
                        imageUrl,
                        70,
                        70,
                        true,
                        true,
                        true);

                ImageView imageView = new ImageView(
                        image);

                imageView.setFitWidth(
                        70);

                imageView.setFitHeight(
                        70);

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

        Label nameLbl = new Label(
                safe(
                        product.getName()));

        nameLbl.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1B2631;");

        Label categoryLbl = new Label(
                safe(
                        product.getCategory()));

        categoryLbl.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-text-fill: #566573;");

        Label priceLbl = new Label(
                "₹"
                        + formatNumber(
                                product.getPrice())
                        + " / "
                        + safe(
                                product.getUnit()));

        priceLbl.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #117864;" +
                        "-fx-font-weight: bold;");

        info.getChildren().addAll(
                nameLbl,
                categoryLbl,
                priceLbl);

        // =================================================
        // SPACER
        // =================================================

        Region sp = new Region();

        HBox.setHgrow(
                sp,
                Priority.ALWAYS);

        // =================================================
        // FARMER
        // =================================================

        Label farmerLbl = new Label(
                "Farmer: "
                        + safe(
                                product
                                        .getFarmerEmail()));

        farmerLbl.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #566573;");

        // =================================================
        // STOCK
        // =================================================

        Label stockLbl = new Label(
                "Available: "
                        + formatNumber(
                                product.getStock())
                        + " "
                        + safe(
                                product.getUnit()));

        stockLbl.setStyle(
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
        // VIEW BUTTON
        // =================================================

        Button viewBtn = new Button(
                "View");

        viewBtn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #A2D9CE;" +
                        "-fx-border-radius: 6px;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-text-fill: #1B2631;" +
                        "-fx-padding: 6 12;" +
                        "-fx-cursor: hand;");

        viewBtn.setOnAction(
                e -> showProductDetails(
                        product));

        row.getChildren().addAll(
                imageBox,
                info,
                sp,
                farmerLbl,
                stockLbl,
                statusBadge,
                viewBtn);

        return row;
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
                            "-fx-padding: 4 10;" +
                            "-fx-background-radius: 12px;");

        } else if ("Sold Out".equalsIgnoreCase(
                status)) {

            badge.setStyle(
                    "-fx-background-color: #FADBD8;" +
                            "-fx-text-fill: #C0392B;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 4 10;" +
                            "-fx-background-radius: 12px;");

        } else {

            badge.setStyle(
                    "-fx-background-color: #EAECEE;" +
                            "-fx-text-fill: #566573;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 4 10;" +
                            "-fx-background-radius: 12px;");
        }

        return badge;
    }

    // =====================================================
    // VIEW PRODUCT DETAILS
    // =====================================================

    private void showProductDetails(
            Product product) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);

        alert.setTitle(
                "Product Details");

        alert.setHeaderText(
                safe(
                        product.getName()));

        alert.setContentText(
                "Category: "
                        + safe(
                                product.getCategory())

                        + "\n\nPrice: ₹"
                        + formatNumber(
                                product.getPrice())
                        + " / "
                        + safe(
                                product.getUnit())

                        + "\n\nAvailable Stock: "
                        + formatNumber(
                                product.getStock())
                        + " "
                        + safe(
                                product.getUnit())

                        + "\n\nVariety: "
                        + safe(
                                product.getVariety())

                        + "\n\nFarmer: "
                        + safe(
                                product.getFarmerEmail())

                        + "\n\nHarvest Date: "
                        + safe(
                                product.getHarvestDate())

                        + "\n\nDescription:\n"
                        + safe(
                                product.getDescription()));

        alert.showAndWait();
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

        productList.getChildren()
                .add(
                        empty);
    }

    // =====================================================
    // SAFE STRING
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
}