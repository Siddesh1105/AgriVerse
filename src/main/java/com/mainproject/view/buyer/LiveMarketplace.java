package com.mainproject.view.buyer;

import com.mainproject.util.LanguageManager;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class LiveMarketplace {

    private final BuyerDashboard navigator;

    private final ProductController productController =
            new ProductController();

    private final List<Product> allProducts =
            new ArrayList<>();

    private final VBox productList =
            new VBox(12);

    private TextField searchField;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public LiveMarketplace(
            BuyerDashboard navigator) {

        this.navigator = navigator;
    }


    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root =
                new VBox(16);

        root.setPadding(
                new Insets(20)
        );


        // =================================================
        // HEADER
        // =================================================

        HBox header =
                new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT
        );


        VBox titles =
                new VBox(3);


        Label title =
                new Label(
                        "Marketplace"
                );

        title.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:800;" +
                "-fx-text-fill:#1B2631;"
        );


        Label subtitle =
                new Label(
                        "Explore fresh products directly from farmers."
                );

        subtitle.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#566573;"
        );


        titles.getChildren().addAll(
                title,
                subtitle
        );


        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        // =================================================
        // CART BUTTON
        // =================================================

        Button cartButton =
                new Button(
                        "🛒 My Cart"
                );

        cartButton.setStyle(
                "-fx-background-color:#117864;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8px;" +
                "-fx-padding:8 16;" +
                "-fx-cursor:hand;"
        );


        cartButton.setOnAction(
                e -> navigator.setView(
                        new ShoppingCart(
                                navigator
                        ).getView()
                )
        );


        header.getChildren().addAll(
                titles,
                spacer,
                cartButton
        );


        // =================================================
        // SEARCH
        // =================================================

        HBox searchRow =
                new HBox(10);

        searchRow.setAlignment(
                Pos.CENTER_LEFT
        );


        searchField =
                new TextField();

        searchField.setPromptText(
                "Search products, categories or farmers..."
        );

        searchField.setPrefHeight(
                42
        );

        searchField.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:8px;" +
                "-fx-border-color:#A2D9CE;" +
                "-fx-border-radius:8px;" +
                "-fx-padding:8 12;"
        );


        HBox.setHgrow(
                searchField,
                Priority.ALWAYS
        );


        searchField.textProperty()
                .addListener(
                        (obs, oldValue, newValue)
                                -> refreshProducts()
                );


        searchRow.getChildren().add(
                searchField
        );


        // =================================================
        // PRODUCT GRID
        // =================================================

        GridPane grid =
                new GridPane();

        grid.setHgap(16);

        grid.setVgap(16);

        grid.setPadding(
                new Insets(
                        5,
                        0,
                        20,
                        0
                )
        );


        // =================================================
        // LOAD PRODUCTS
        // =================================================

        loadProducts();


        // =================================================
        // ROOT
        // =================================================

        root.getChildren().addAll(
                header,
                searchRow,
                grid
        );


        // =================================================
        // STORE GRID REFERENCE
        // =================================================

        this.productGrid =
                grid;


        refreshProducts();


        // =================================================
        // SCROLL
        // =================================================

        ScrollPane scroll =
                new ScrollPane(
                        root
                );

        scroll.setFitToWidth(
                true
        );

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;"
        );


        return scroll;
    }


    private GridPane productGrid;


    // =====================================================
    // LOAD PRODUCTS FROM FIRESTORE
    // =====================================================

    private void loadProducts() {

        allProducts.clear();

        try {

            System.out.println(
                    "Loading buyer marketplace products..."
            );


            List<Product> products =
                    productController.getAllProducts();


            if (products != null) {

                allProducts.addAll(
                        products
                );
            }


            System.out.println(
                    "Buyer marketplace products loaded: "
                            + allProducts.size()
            );


        } catch (Exception e) {

            System.out.println(
                    "Error loading marketplace products:"
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // REFRESH PRODUCTS
    // =====================================================

    private void refreshProducts() {

        if (productGrid == null) {
            return;
        }


        productGrid.getChildren()
                .clear();


        String search =
                searchField == null
                        ? ""
                        : searchField
                                .getText()
                                .trim()
                                .toLowerCase();


        int column = 0;

        int row = 0;

        int count = 0;


        for (Product product :
                allProducts) {


            if (product == null) {
                continue;
            }


            // =================================================
            // ONLY AVAILABLE PRODUCTS
            // =================================================

            if (product.getStock() <= 0) {
                continue;
            }


            String status =
                    safe(
                            product.getStatus()
                    );


            if (!status.isEmpty()
                    && status.equalsIgnoreCase(
                            "Inactive"
                    )) {

                continue;
            }


            // =================================================
            // SEARCH
            // =================================================

            if (!search.isEmpty()) {

                String name =
                        safe(
                                product.getName()
                        ).toLowerCase();


                String category =
                        safe(
                                product.getCategory()
                        ).toLowerCase();


                String farmer =
                        safe(
                                product.getFarmerEmail()
                        ).toLowerCase();


                if (!name.contains(search)
                        && !category.contains(search)
                        && !farmer.contains(search)) {

                    continue;
                }
            }


            // =================================================
            // CREATE CARD
            // =================================================

            VBox card =
                    createProductCard(
                            product
                    );


            productGrid.add(
                    card,
                    column,
                    row
            );


            column++;

            count++;


            if (column == 4) {

                column = 0;

                row++;
            }
        }


        // =================================================
        // EMPTY
        // =================================================

        if (count == 0) {

            Label empty =
                    new Label(
                            "No products available."
                    );

            empty.setStyle(
                    "-fx-font-size:15px;" +
                    "-fx-text-fill:#566573;" +
                    "-fx-padding:40px;"
            );


            productGrid.add(
                    empty,
                    0,
                    0,
                    4,
                    1
            );
        }
    }


    // =====================================================
    // PRODUCT CARD
    // =====================================================

    private VBox createProductCard(
            Product product) {

        VBox card =
                new VBox(9);

        card.setPadding(
                new Insets(12)
        );

        card.setPrefWidth(
                250
        );

        card.setPrefHeight(
                330
        );


        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:12px;" +
                "-fx-border-color:#A2D9CE;" +
                "-fx-border-radius:12px;" +
                "-fx-effect:dropshadow(" +
                "gaussian," +
                "rgba(0,0,0,0.08)," +
                "8,0,0,2);"
        );


        // =================================================
        // IMAGE
        // =================================================

        StackPane imageBox =
                new StackPane();

        imageBox.setPrefHeight(
                135
        );

        imageBox.setStyle(
                "-fx-background-color:#E9F7EF;" +
                "-fx-background-radius:10px;"
        );


        String imageUrl =
                safe(
                        product.getImageUrl()
                );


        if (!imageUrl.isEmpty()) {

            try {

                Image image =
                        new Image(
                                imageUrl,
                                220,
                                130,
                                true,
                                true,
                                true
                        );


                ImageView imageView =
                        new ImageView(
                                image
                        );


                imageView.setFitWidth(
                        220
                );

                imageView.setFitHeight(
                        130
                );

                imageView.setPreserveRatio(
                        true
                );


                imageBox.getChildren()
                        .add(
                                imageView
                        );


            } catch (Exception e) {

                addPlaceholder(
                        imageBox
                );
            }

        } else {

            addPlaceholder(
                    imageBox
            );
        }


        // =================================================
        // PRODUCT NAME
        // =================================================

        Label name =
                new Label(
                        safe(
                                product.getName()
                        )
                );

        name.setStyle(
                "-fx-font-size:16px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1B2631;"
        );


        // =================================================
        // CATEGORY
        // =================================================

        Label category =
                new Label(
                        safe(
                                product.getCategory()
                        )
                );

        category.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#566573;"
        );


        // =================================================
        // PRICE
        // =================================================

        Label price =
                new Label(
                        "₹"
                                + formatNumber(
                                        product.getPrice()
                                )
                                + " / "
                                + safe(
                                        product.getUnit()
                                )
                );

        price.setStyle(
                "-fx-font-size:17px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#117864;"
        );


        // =================================================
        // STOCK
        // =================================================

        Label stock =
                new Label(
                        "Available: "
                                + formatNumber(
                                        product.getStock()
                                )
                                + " "
                                + safe(
                                        product.getUnit()
                                )
                );

        stock.setStyle(
                "-fx-font-size:12px;" +
                "-fx-text-fill:#566573;"
        );


        // =================================================
        // FARMER
        // =================================================

        Label farmer =
                new Label(
                        "Farmer: "
                                + safe(
                                        product.getFarmerEmail()
                                )
                );

        farmer.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#64748B;"
        );

        farmer.setWrapText(
                true
        );


        // =================================================
        // BUTTONS
        // =================================================

        HBox buttons =
                new HBox(8);

        HBox.setHgrow(
                buttons,
                Priority.ALWAYS
        );


        Button view =
                new Button(
                        "View"
                );

        view.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#A2D9CE;" +
                "-fx-border-radius:7px;" +
                "-fx-background-radius:7px;" +
                "-fx-text-fill:#117864;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );


        view.setOnAction(
                e -> showProductDetails(
                        product
                )
        );


        Button cart =
                new Button(
                        "Add to Cart"
                );

        cart.setStyle(
                "-fx-background-color:#117864;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7px;" +
                "-fx-padding:7 12;" +
                "-fx-cursor:hand;"
        );


        cart.setOnAction(
                e -> addToCart(
                        product
                )
        );


        HBox.setHgrow(
                cart,
                Priority.ALWAYS
        );


        buttons.getChildren().addAll(
                view,
                cart
        );


        // =================================================
        // CARD
        // =================================================

        card.getChildren().addAll(
                imageBox,
                name,
                category,
                price,
                stock,
                farmer,
                buttons
        );


        return card;
    }


    // =====================================================
    // ADD TO CART
    // =====================================================

    private void addToCart(
            Product product) {

        /*
         * We will connect your existing CartController here.
         *
         * For now this confirms the selected product.
         */

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        alert.setTitle(
                "Add to Cart"
        );


        alert.setHeaderText(
                product.getName()
        );


        alert.setContentText(
                "Product selected successfully."
                        + "\n\nPrice: ₹"
                        + formatNumber(
                                product.getPrice()
                        )
                        + " / "
                        + safe(
                                product.getUnit()
                        )
        );


        alert.showAndWait();
    }


    // =====================================================
    // PRODUCT DETAILS
    // =====================================================

    private void showProductDetails(
            Product product) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        alert.setTitle(
                "Product Details"
        );


        alert.setHeaderText(
                safe(
                        product.getName()
                )
        );


        alert.setContentText(

                "Category: "
                        + safe(
                                product.getCategory()
                        )

                        + "\n\nPrice: ₹"
                        + formatNumber(
                                product.getPrice()
                        )
                        + " / "
                        + safe(
                                product.getUnit()
                        )

                        + "\n\nAvailable Stock: "
                        + formatNumber(
                                product.getStock()
                        )
                        + " "
                        + safe(
                                product.getUnit()
                        )

                        + "\n\nFarmer: "
                        + safe(
                                product.getFarmerEmail()
                        )

                        + "\n\nVariety: "
                        + safe(
                                product.getVariety()
                        )

                        + "\n\nHarvest Date: "
                        + safe(
                                product.getHarvestDate()
                        )

                        + "\n\nDescription:\n"
                        + safe(
                                product.getDescription()
                        )
        );


        alert.showAndWait();
    }


    // =====================================================
    // IMAGE PLACEHOLDER
    // =====================================================

    private void addPlaceholder(
            StackPane imageBox) {

        Label placeholder =
                new Label("🌱");

        placeholder.setStyle(
                "-fx-font-size:35px;"
        );


        imageBox.getChildren()
                .add(
                        placeholder
                );
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
                    value
            );
        }


        return String.format(
                "%.2f",
                value
        );
    }
}