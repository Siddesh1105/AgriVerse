package com.mainproject.view.buyer;

import com.mainproject.controller.ProductController;
import com.mainproject.model.Product;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FarmerProfile {

    private final BuyerDashboard mainController;

    private final ProductController productController =
            new ProductController();

    /*
     * Selected farmer information.
     */
    private String farmerEmail;
    private String farmerName;

    // =====================================================
    // EXISTING CONSTRUCTOR
    // Keeps sidebar navigation working
    // =====================================================

    public FarmerProfile(
            BuyerDashboard controller) {

        this.mainController = controller;

        this.farmerName = "Farmer";
        this.farmerEmail = null;
    }

    // =====================================================
    // FARMER PROFILE CONSTRUCTOR
    // Used when buyer opens a specific farmer
    // =====================================================

    public FarmerProfile(
            BuyerDashboard controller,
            String farmerEmail,
            String farmerName) {

        this.mainController = controller;

        this.farmerEmail =
                farmerEmail == null
                        ? null
                        : farmerEmail.trim();

        this.farmerName =
                farmerName == null ||
                        farmerName.trim().isEmpty()
                        ? "Farmer"
                        : farmerName.trim();

        System.out.println(
                "===================================="
        );

        System.out.println(
                "Opening Farmer Profile"
        );

        System.out.println(
                "Farmer Name: "
                        + this.farmerName
        );

        System.out.println(
                "Farmer Email: "
                        + this.farmerEmail
        );

        System.out.println(
                "===================================="
        );
    }

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root =
                new VBox(20);

        root.setPadding(
                new Insets(
                        25,
                        35,
                        25,
                        35
                )
        );

        root.setStyle(
                "-fx-background-color:#F8FAFC;"
        );

        // =================================================
        // LOAD FARMER PRODUCTS
        // =================================================

        List<Product> farmerProducts =
                getFarmerProducts();

        // =================================================
        // PROFILE CARD
        // =================================================

        VBox profileCard =
                new VBox(10);

        profileCard.setPadding(
                new Insets(20)
        );

        profileCard.setStyle(
                "-fx-background-color:#FFFFFF;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;"
        );

        // =================================================
        // FARMER NAME
        // =================================================

        String displayName =
                farmerName == null ||
                        farmerName.trim().isEmpty()
                        ? "Farmer"
                        : farmerName.trim();

        Label name =
                new Label(
                        "👨‍🌾 "
                                + displayName
                                + " ✔ Verified Farmer"
                );

        name.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#166534;"
        );

        // =================================================
        // FARMER INFORMATION
        // =================================================

        Label meta =
                new Label();

        meta.setStyle(
                "-fx-font-size:13px;" +
                "-fx-text-fill:#64748B;"
        );

        if (farmerEmail != null &&
                !farmerEmail.trim().isEmpty()) {

            meta.setText(
                    "📧 "
                            + farmerEmail
                            + " | "
                            + farmerProducts.size()
                            + " Products"
            );

        } else {

            meta.setText(
                    farmerProducts.size()
                            + " Products"
            );
        }

        // =================================================
        // BIO
        // =================================================

        Label bio =
                new Label(
                        "Products and crops listed by this farmer."
                );

        bio.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#334155;"
        );

        bio.setWrapText(
                true
        );

        // =================================================
        // BUTTONS
        // =================================================

        HBox btnBar =
                new HBox(10);

        Button btnMsg =
                new Button(
                        "Message Farmer"
                );

        btnMsg.setStyle(
                "-fx-background-color:#FFFFFF;" +
                "-fx-border-color:#CBD5E1;" +
                "-fx-text-fill:#1E293B;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        btnMsg.setOnAction(
                e ->
                        mainController.setView(
                                new ChatWithFarmer(
                                        mainController, farmerName, farmerEmail
                                ).getView()
                        )
        );

        Button btnFollow =
                new Button(
                        "Follow"
                );

        btnFollow.setStyle(
                "-fx-background-color:#166534;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        btnBar.getChildren().addAll(
                btnMsg,
                btnFollow
        );

        profileCard.getChildren().addAll(
                name,
                meta,
                bio,
                btnBar
        );

        // =================================================
        // PRODUCT TITLE
        // =================================================

        Label lblList =
                new Label(
                        "Products by "
                                + displayName
                                + ":"
                );

        lblList.setStyle(
                "-fx-font-size:16px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
        );

        // =================================================
        // PRODUCT GRID
        // =================================================

        GridPane productGrid =
                new GridPane();

        productGrid.setHgap(15);

        productGrid.setVgap(15);

        // =================================================
        // NO PRODUCTS
        // =================================================

        if (farmerProducts.isEmpty()) {

            VBox emptyBox =
                    new VBox(10);

            emptyBox.setAlignment(
                    Pos.CENTER
            );

            emptyBox.setPadding(
                    new Insets(30)
            );

            Label emptyIcon =
                    new Label(
                            "🌱"
                    );

            emptyIcon.setStyle(
                    "-fx-font-size:40px;"
            );

            Label empty =
                    new Label(
                            "No products found for this farmer."
                    );

            empty.setStyle(
                    "-fx-font-size:15px;" +
                    "-fx-text-fill:#64748B;"
            );

            emptyBox.getChildren().addAll(
                    emptyIcon,
                    empty
            );

            productGrid.add(
                    emptyBox,
                    0,
                    0
            );

        } else {

            int column = 0;

            int row = 0;

            for (Product product :
                    farmerProducts) {

                if (product == null) {
                    continue;
                }

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
        }

        // =================================================
        // ADD EVERYTHING
        // =================================================

        root.getChildren().addAll(
                profileCard,
                lblList,
                productGrid
        );

        // =================================================
        // LANGUAGE
        // =================================================
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

    // =====================================================
    // GET FARMER PRODUCTS
    // =====================================================

    private List<Product> getFarmerProducts() {

        List<Product> result =
                new ArrayList<>();

        try {

            // -------------------------------------------------
            // VALIDATE FARMER EMAIL
            // -------------------------------------------------

            if (farmerEmail == null ||
                    farmerEmail.trim().isEmpty()) {

                System.out.println(
                        "===================================="
                );

                System.out.println(
                        "FarmerProfile: Farmer email is EMPTY."
                );

                System.out.println(
                        "Cannot load farmer products."
                );

                System.out.println(
                        "===================================="
                );

                return result;
            }

            String email =
                    farmerEmail.trim();

            // -------------------------------------------------
            // DEBUG
            // -------------------------------------------------

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "FarmerProfile - Loading Products"
            );

            System.out.println(
                    "Farmer Name: "
                            + farmerName
            );

            System.out.println(
                    "Farmer Email: "
                            + email
            );

            // -------------------------------------------------
            // DIRECT FARMER PRODUCT QUERY
            // -------------------------------------------------

            result =
                    productController
                            .getFarmerProducts(
                                    email
                            );

            // -------------------------------------------------
            // NULL SAFETY
            // -------------------------------------------------

            if (result == null) {

                result =
                        new ArrayList<>();
            }

            // -------------------------------------------------
            // DEBUG RESULTS
            // -------------------------------------------------

            System.out.println(
                    "Products Found: "
                            + result.size()
            );

            for (Product product :
                    result) {

                if (product != null) {

                    System.out.println(
                            "Product: "
                                    + product.getName()
                                    + " | Farmer Email: "
                                    + product.getFarmerEmail()
                    );
                }
            }

            System.out.println(
                    "===================================="
            );

        } catch (Exception e) {

            System.out.println(
                    "Error loading farmer products:"
            );

            e.printStackTrace();
        }

        return result;
    }

    // =====================================================
    // PRODUCT CARD
    // =====================================================

    private VBox createProductCard(
            Product product) {

        VBox card =
                new VBox(8);

        card.setPrefWidth(
                210
        );

        card.setPadding(
                new Insets(12)
        );

        card.setStyle(
                "-fx-background-color:#FFFFFF;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;"
        );

        // =================================================
        // IMAGE
        // =================================================

        StackPane imageBox =
                new StackPane();

        imageBox.setPrefHeight(
                110
        );

        imageBox.setStyle(
                "-fx-background-color:#E9F7EF;" +
                "-fx-background-radius:8;"
        );

        ImageView imageView =
                createImage(
                        product.getImageUrl()
                );

        if (imageView != null) {

            imageView.setFitWidth(
                    190
            );

            imageView.setFitHeight(
                    105
            );

            imageView.setPreserveRatio(
                    true
            );

            imageBox.getChildren().add(
                    imageView
            );

        } else {

            Label placeholder =
                    new Label(
                            "🌱"
                    );

            placeholder.setStyle(
                    "-fx-font-size:40px;"
            );

            imageBox.getChildren().add(
                    placeholder
            );
        }

        // =================================================
        // NAME
        // =================================================

        Label productName =
                new Label(
                        safe(
                                product.getName()
                        )
                );

        productName.setStyle(
                "-fx-font-size:15px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1E293B;"
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
                "-fx-font-size:15px;" +
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

        stock.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#64748B;"
        );

        // =================================================
        // VIEW PRODUCT
        // =================================================

        Button view =
                new Button(
                        "View Product"
                );

        view.setMaxWidth(
                Double.MAX_VALUE
        );

        view.setStyle(
                "-fx-background-color:#117864;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:7;" +
                "-fx-cursor:hand;"
        );

        view.setOnAction(
                e ->
                        mainController.setView(
                                new ProductDetails(
                                        mainController,
                                        product
                                ).getView()
                        )
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
                view
        );

        return card;
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
                                    url.trim()
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
                    "Unable to load product image."
            );

            return null;
        }
    }

    // =====================================================
    // SAFE
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