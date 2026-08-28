package com.mainproject.view.buyer;

import com.mainproject.controller.BuyerCartController;
import com.mainproject.controller.WishlistController;
import com.mainproject.model.BuyerCartItem;
import com.mainproject.model.WishlistItem;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.List;

public class Wishlist {

    private final BuyerDashboard mainController;

    // =====================================================
    // CONTROLLERS
    // =====================================================

    private final WishlistController wishlistController =
            new WishlistController();

    private final BuyerCartController cartController =
            new BuyerCartController();

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Wishlist(
            BuyerDashboard controller) {

        this.mainController = controller;
    }

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root =
                new VBox(20);

        root.setPadding(
                new Insets(25, 30, 25, 30)
        );

        root.setStyle(
                "-fx-background-color:#F8FAFC;"
        );

        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label();

        title.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        // =================================================
        // LOAD WISHLIST
        // =================================================

        List<WishlistItem> wishlistItems =
                wishlistController.getWishlist(
                        mainController.getBuyerEmail()
                );

        int itemCount =
                wishlistItems == null
                        ? 0
                        : wishlistItems.size();

        title.setText(
                "My Wishlist ♥ (" +
                        itemCount +
                        " Items)"
        );

        // =================================================
        // GRID
        // =================================================

        GridPane grid =
                new GridPane();

        grid.setHgap(18);
        grid.setVgap(18);

        // =================================================
        // EMPTY WISHLIST
        // =================================================

        if (wishlistItems == null ||
                wishlistItems.isEmpty()) {

            VBox emptyBox =
                    new VBox(12);

            emptyBox.setAlignment(
                    Pos.CENTER
            );

            emptyBox.setPadding(
                    new Insets(40)
            );

            Label emptyIcon =
                    new Label("♡");

            emptyIcon.setStyle(
                    "-fx-font-size:50px;" +
                    "-fx-text-fill:#94A3B8;"
            );

            Label emptyText =
                    new Label(
                            "Your wishlist is empty."
                    );

            emptyText.setStyle(
                    "-fx-font-size:16px;" +
                    "-fx-text-fill:#64748B;"
            );

            Button browse =
                    new Button(
                            "Browse Marketplace"
                    );

            browse.setStyle(
                    "-fx-background-color:#117864;" +
                    "-fx-text-fill:white;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:10 18;" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;"
            );

            browse.setOnAction(
                    e ->
                            mainController.setView(
                                    new LiveMarketplace(
                                            mainController
                                    ).getView()
                            )
            );

            emptyBox.getChildren().addAll(
                    emptyIcon,
                    emptyText,
                    browse
            );

            root.getChildren().addAll(
                    title,
                    emptyBox
            );

        } else {

            // =============================================
            // ADD WISHLIST ITEMS
            // =============================================

            int column = 0;
            int row = 0;

            for (
                    WishlistItem item :
                    wishlistItems) {

                VBox card =
                        createWishlistCard(
                                item
                        );

                grid.add(
                        card,
                        column,
                        row
                );

                column++;

                if (column == 3) {

                    column = 0;
                    row++;
                }
            }

            root.getChildren().addAll(
                    title,
                    grid
            );
        }

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

        scroll.setFitToHeight(
                true
        );

        scroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;"
        );

        // =================================================
        // LANGUAGE
        // =================================================

   

        return scroll;
    }

    // =====================================================
    // CREATE WISHLIST CARD
    // =====================================================

    private VBox createWishlistCard(
            WishlistItem item) {

        VBox card =
                new VBox(10);

        card.setPrefWidth(
                310
        );

        card.setPadding(
                new Insets(14)
        );

        card.setStyle(
                "-fx-background-color:#FFFFFF;" +
                "-fx-border-color:#A2D9CE;" +
                "-fx-border-radius:12;" +
                "-fx-background-radius:12;"
        );

        // =================================================
        // IMAGE
        // =================================================

        StackPane imageBox =
                new StackPane();

        imageBox.setPrefHeight(
                160
        );

        imageBox.setStyle(
                "-fx-background-color:#E9F7EF;" +
                "-fx-background-radius:10;"
        );

        ImageView imageView =
                createImage(
                        item.getImageUrl()
                );

        if (imageView != null) {

            imageView.setFitWidth(
                    280
            );

            imageView.setFitHeight(
                    155
            );

            imageView.setPreserveRatio(
                    true
            );

            imageBox.getChildren().add(
                    imageView
            );

        } else {

            Label placeholder =
                    new Label("🌱");

            placeholder.setStyle(
                    "-fx-font-size:50px;"
            );

            imageBox.getChildren().add(
                    placeholder
            );
        }

        // =================================================
        // PRODUCT NAME
        // =================================================

        Label productName =
                new Label(
                        safe(
                                item.getProductName()
                        )
                );

        productName.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        // =================================================
        // CATEGORY
        // =================================================

        Label category =
                new Label(
                        "Category: "
                                + safe(
                                item.getCategory()
                        )
                );

        category.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#64748B;"
        );

        // =================================================
        // PRICE
        // =================================================

        Label price =
                new Label(
                        "₹"
                                + format(
                                item.getPrice()
                        )
                                + " / "
                                + safe(
                                item.getUnit()
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
                                item.getStock()
                        )
                                + " "
                                + safe(
                                item.getUnit()
                        )
                );

        stock.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#475569;"
        );

        // =================================================
        // FARMER
        // =================================================

        Label farmer =
                new Label(
                        "Farmer: "
                                + safe(
                                item.getFarmerEmail()
                        )
                );

        farmer.setStyle(
                "-fx-font-size:12px;" +
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

        // -------------------------------------------------
        // VIEW
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

        /*
         * We don't directly navigate to ProductDetails
         * here because WishlistItem is a separate model
         * and does not contain every Product field.
         *
         * We can add full Product loading later through
         * ProductController -> ProductDAO.
         */

        view.setOnAction(
                e -> {

                    Alert alert =
                            new Alert(
                                    Alert.AlertType.INFORMATION
                            );

                    alert.setHeaderText(
                            item.getProductName()
                    );

                    alert.setContentText(
                            "Price: ₹"
                                    + format(
                                    item.getPrice()
                            )
                                    + " / "
                                    + safe(
                                    item.getUnit()
                            )
                                    + "\n"
                                    + "Category: "
                                    + safe(
                                    item.getCategory()
                            )
                    );

                    alert.showAndWait();
                }
        );

        // -------------------------------------------------
        // ADD TO CART
        // -------------------------------------------------

        Button addToCart =
                new Button(
                        "Add to Cart"
                );

        addToCart.setStyle(
                "-fx-background-color:#117864;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        addToCart.setOnAction(
                e ->
                        addWishlistItemToCart(
                                item
                        )
        );

        // -------------------------------------------------
        // REMOVE
        // -------------------------------------------------

        Button remove =
                new Button(
                        "Remove"
                );

        remove.setStyle(
                "-fx-background-color:#FEE2E2;" +
                "-fx-text-fill:#DC2626;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        remove.setOnAction(
                e ->
                        removeWishlistItem(
                                item
                        )
        );

        buttons.getChildren().addAll(
                view,
                addToCart,
                remove
        );

        // =================================================
        // CARD
        // =================================================

        card.getChildren().addAll(
                imageBox,
                productName,
                category,
                price,
                stock,
                farmer,
                buttons
        );

        return card;
    }

    // =====================================================
    // ADD WISHLIST ITEM TO CART
    // =====================================================

    private void addWishlistItemToCart(
            WishlistItem item) {

        if (item == null) {
            return;
        }

        BuyerCartItem cartItem =
                new BuyerCartItem(
                        null,
                        mainController.getBuyerEmail(),
                        item.getProductId(),
                        item.getProductName(),
                        item.getFarmerEmail(),
                        item.getUnit(),
                        item.getPrice(),
                        1,
                        item.getImageUrl()
                );

        boolean success =
                cartController.addToCart(
                        cartItem
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
                        ? item.getProductName()
                                + " added to cart."
                        : "Unable to add product to cart."
        );

        alert.showAndWait();
    }

    // =====================================================
    // REMOVE WISHLIST ITEM
    // =====================================================

    private void removeWishlistItem(
            WishlistItem item) {

        if (item == null) {
            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setHeaderText(
                "Remove from Wishlist"
        );

        confirmation.setContentText(
                "Remove "
                        + item.getProductName()
                        + " from your wishlist?"
        );

        ButtonType result =
                confirmation.showAndWait()
                        .orElse(
                                ButtonType.CANCEL
                        );

        if (result !=
                ButtonType.OK) {

            return;
        }

        boolean success =
                wishlistController
                        .removeFromWishlist(
                                item.getWishlistId()
                        );

        if (success) {

            /*
             * Reload the Wishlist page
             * after successful deletion.
             */

            mainController.setView(
                    new Wishlist(
                            mainController
                    ).getView()
            );

        } else {

            Alert alert =
                    new Alert(
                            Alert.AlertType.ERROR
                    );

            alert.setHeaderText(
                    null
            );

            alert.setContentText(
                    "Unable to remove item."
            );

            alert.showAndWait();
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

            System.out.println(
                    "Unable to load wishlist image:"
            );

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
    // FORMAT
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