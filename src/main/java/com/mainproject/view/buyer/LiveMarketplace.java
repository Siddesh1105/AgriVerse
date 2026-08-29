package com.mainproject.view.buyer;

import com.mainproject.controller.ProductController;
import com.mainproject.controller.BuyerCartController;
import com.mainproject.controller.WishlistController;

import com.mainproject.model.Product;
import com.mainproject.model.BuyerCartItem;
import com.mainproject.model.WishlistItem;

import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LiveMarketplace {

    private final BuyerDashboard navigator;

    // =====================================================
    // CONTROLLERS
    // =====================================================

    private final ProductController productController =
            new ProductController();

    private final BuyerCartController cartController =
            new BuyerCartController();

    private final WishlistController wishlistController =
            new WishlistController();

    // =====================================================
    // PRODUCTS
    // =====================================================

    private final List<Product> products =
            new ArrayList<>();

    private final VBox grid =
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
                new VBox(18);

        root.setPadding(
                new Insets(25)
        );

        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label("Marketplace");

        title.setStyle(
                "-fx-font-size:26px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        Label subtitle =
                new Label(
                        "Explore fresh products directly from farmers."
                );

        subtitle.setStyle(
                "-fx-text-fill:#64748B;"
        );

        // =================================================
        // SEARCH
        // =================================================

        searchField =
                new TextField();

        searchField.setPromptText(
                "Search products, categories or farmers..."
        );

        searchField.setPrefHeight(
                46
        );

        searchField.textProperty().addListener(
                (obs, oldValue, newValue) ->
                        filterProducts(newValue)
        );

        // =================================================
        // HEADER
        // =================================================

        HBox header =
                new HBox();

        VBox heading =
                new VBox(4);

        heading.getChildren().addAll(
                title,
                subtitle
        );

        HBox.setHgrow(
                heading,
                Priority.ALWAYS
        );

        // =================================================
        // CART BUTTON
        // =================================================

        Button cartButton =
                new Button("🛒 My Cart");

        cartButton.setStyle(
                "-fx-background-color:#117864;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:10 18;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        cartButton.setOnAction(
                e ->
                        navigator.setView(
                                new ShoppingCart(
                                        navigator
                                ).getView()
                        )
        );

        header.getChildren().addAll(
                heading,
                cartButton
        );

        // =================================================
        // PRODUCT SCROLL
        // =================================================

        ScrollPane scroll =
                new ScrollPane(
                        grid
                );

        scroll.setFitToWidth(
                true
        );

        scroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;"
        );

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS
        );

        // =================================================
        // ROOT
        // =================================================

        root.getChildren().addAll(
                header,
                searchField,
                scroll
        );

        // =================================================
        // LOAD PRODUCTS
        // =================================================

        loadProducts();

        // =================================================
        // LANGUAGE
        // =================================================

        LanguageManager.apply(
                root
        );

        return root;
    }

    // =====================================================
    // LOAD PRODUCTS
    // =====================================================

    private void loadProducts() {

        products.clear();

        List<Product> loaded =
                productController.getAllProducts();

        if (loaded != null) {

            for (Product product : loaded) {

                if (product == null) {
                    continue;
                }

                String status =
                        product.getStatus();

                /*
                 * Only show products that:
                 *
                 * 1. Have stock
                 * 2. Are Active / Available
                 */

                if (product.getStock() > 0 &&
                        (
                                status == null ||
                                status.equalsIgnoreCase(
                                        "active"
                                ) ||
                                status.equalsIgnoreCase(
                                        "available"
                                )
                        )) {

                    products.add(
                            product
                    );
                }
            }
        }

        renderProducts(
                products
        );
    }

    // =====================================================
    // SEARCH / FILTER PRODUCTS
    // =====================================================

    private void filterProducts(
            String text) {

        String query =
                text == null
                        ? ""
                        : text.trim()
                        .toLowerCase();

        if (query.isEmpty()) {

            renderProducts(
                    products
            );

            return;
        }

        List<Product> filtered =
                new ArrayList<>();

        for (Product product : products) {

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

            String variety =
                    safe(
                            product.getVariety()
                    ).toLowerCase();

            if (name.contains(query) ||
                    category.contains(query) ||
                    farmer.contains(query) ||
                    variety.contains(query)) {

                filtered.add(
                        product
                );
            }
        }

        renderProducts(
                filtered
        );
    }

    // =====================================================
    // RENDER PRODUCTS
    // =====================================================

    private void renderProducts(
            List<Product> list) {

        grid.getChildren().clear();

        if (list == null ||
                list.isEmpty()) {

            Label empty =
                    new Label(
                            "No products found."
                    );

            empty.setStyle(
                    "-fx-font-size:16px;" +
                    "-fx-text-fill:#64748B;"
            );

            grid.getChildren().add(
                    empty
            );

            return;
        }

        GridPane productGrid =
                new GridPane();

        productGrid.setHgap(
                18
        );

        productGrid.setVgap(
                18
        );

        int column = 0;
        int row = 0;

        for (Product product : list) {

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

            if (column == 4) {

                column = 0;
                row++;
            }
        }

        grid.getChildren().add(
                productGrid
        );
    }

    // =====================================================
    // CREATE PRODUCT CARD
    // =====================================================

    private VBox createProductCard(
            Product product) {

        VBox card =
                new VBox(9);

        card.setPrefWidth(
                300
        );

        card.setPadding(
                new Insets(12)
        );

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#A2D9CE;" +
                "-fx-border-radius:14;" +
                "-fx-background-radius:14;"
        );

        // =================================================
        // PRODUCT IMAGE
        // =================================================

        StackPane imageBox =
                new StackPane();

        imageBox.setPrefHeight(
                165
        );

        imageBox.setStyle(
                "-fx-background-color:#E9F7EF;" +
                "-fx-background-radius:12;"
        );

        ImageView imageView =
                createImage(
                        product.getImageUrl()
                );

        if (imageView != null) {

            imageView.setFitWidth(
                    270
            );

            imageView.setFitHeight(
                    160
            );

            imageView.setPreserveRatio(
                    true
            );

            imageBox.getChildren().add(
                    imageView
            );

        } else {

            Label imageLabel =
                    new Label("🌱");

            imageLabel.setStyle(
                    "-fx-font-size:55px;"
            );

            imageBox.getChildren().add(
                    imageLabel
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
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;"
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
                "-fx-text-fill:#64748B;"
        );

        // =================================================
        // PRICE
        // =================================================

        Label price =
                new Label(
                        "₹"
                                + format(
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
                                + format(
                                product.getStock()
                        )
                                + " "
                                + safe(
                                product.getUnit()
                        )
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
                "-fx-text-fill:#64748B;" +
                "-fx-font-size:12px;"
        );

        // =================================================
        // BUTTONS
        // =================================================

        HBox buttons =
                new HBox(8);

        // -------------------------------------------------
        // VIEW BUTTON
        // -------------------------------------------------

        Button view =
                new Button(
                        "View"
                );

        view.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#A2D9CE;" +
                "-fx-text-fill:#117864;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        // -------------------------------------------------
        // ADD TO CART BUTTON
        // -------------------------------------------------

        Button add =
                new Button(
                        "Add to Cart"
                );

        add.setStyle(
                "-fx-background-color:#117864;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        // -------------------------------------------------
        // WISHLIST BUTTON
        // -------------------------------------------------

        Button wishlist =
                new Button(
                        "♡"
                );

        wishlist.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#A2D9CE;" +
                "-fx-text-fill:#117864;" +
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        // =================================================
        // VIEW PRODUCT
        // =================================================

        view.setOnAction(
                e ->
                        navigator.setView(
                                new ProductDetails(
                                        navigator,
                                        product
                                ).getView()
                        )
        );

        // =================================================
        // ADD TO CART
        // =================================================

        add.setOnAction(
                e ->
                        addProductToCart(
                                product
                        )
        );

        // =================================================
        // ADD TO WISHLIST
        // =================================================

        wishlist.setOnAction(
                e ->
                        addProductToWishlist(
                                product,
                                wishlist
                        )
        );

        // =================================================
        // BUTTONS
        // =================================================

        buttons.getChildren().addAll(
                view,
                add,
                wishlist
        );

        // =================================================
        // CARD CONTENT
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
    // ADD PRODUCT TO CART
    // =====================================================

    private void addProductToCart(
            Product product) {

        if (product == null) {
            return;
        }

        BuyerCartItem item =
                new BuyerCartItem(
                        null,
                        navigator.getBuyerEmail(),
                        product.getProductId(),
                        product.getName(),
                        product.getFarmerEmail(),
                        product.getUnit(),
                        product.getPrice(),
                        1,
                        product.getImageUrl()
                );

        boolean success =
                cartController.addToCart(
                        item
                );

        Alert alert =
                new Alert(
                        success
                                ? Alert.AlertType.INFORMATION
                                : Alert.AlertType.ERROR
                );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                success
                        ? product.getName()
                                + " added to cart."
                        : "Unable to add product to cart."
        );

        alert.showAndWait();
    }

    // =====================================================
    // ADD PRODUCT TO WISHLIST
    // =====================================================

    private void addProductToWishlist(
            Product product,
            Button wishlistButton) {

        if (product == null) {
            return;
        }

        String buyerEmail =
                navigator.getBuyerEmail();

        // =================================================
        // CHECK ALREADY IN WISHLIST
        // =================================================

        boolean alreadyExists =
                wishlistController.isInWishlist(
                        buyerEmail,
                        product.getProductId()
                );

        if (alreadyExists) {

            Alert alert =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alert.setHeaderText(
                    null
            );

            alert.setContentText(
                    product.getName()
                            + " is already in your wishlist."
            );

            alert.showAndWait();

            return;
        }

        // =================================================
        // CREATE WISHLIST MODEL
        // =================================================

        WishlistItem item =
                new WishlistItem(
                        buyerEmail,
                        product
                );

        // =================================================
        // CONTROLLER
        // =================================================

        boolean success =
                wishlistController.addToWishlist(
                        item
                );

        // =================================================
        // RESULT
        // =================================================

        Alert alert =
                new Alert(
                        success
                                ? Alert.AlertType.INFORMATION
                                : Alert.AlertType.ERROR
                );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                success
                        ? product.getName()
                                + " added to wishlist."
                        : "Unable to add product to wishlist."
        );

        alert.showAndWait();

        // =================================================
        // CHANGE HEART
        // =================================================

        if (success) {

            wishlistButton.setText(
                    "♥"
            );

            wishlistButton.setStyle(
                    "-fx-background-color:#FEE2E2;" +
                    "-fx-border-color:#FCA5A5;" +
                    "-fx-text-fill:#DC2626;" +
                    "-fx-font-size:18px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;"
            );
        }
    }

    // =====================================================
    // CREATE IMAGE
    // =====================================================

    private ImageView createImage(
            String url) {

        try {

            if (url == null ||
                    url.trim().isEmpty()) {

                return null;
            }

            Image image =
                    new Image(
                            new URL(
                                    url
                            ).openStream()
                    );

            if (image.isError()) {

                return null;
            }

            return new ImageView(
                    image
            );

        } catch (Exception e) {

            return null;
        }
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

    private String format(
            double value) {

        if (value == Math.rint(value)) {

            return String.valueOf(
                    (long) value
            );
        }

        return String.format(
                "%.2f",
                value
        );
    }
}