package com.mainproject.view.buyer;

import com.mainproject.controller.BuyerCartController;
import com.mainproject.model.BuyerCartItem;
import com.mainproject.model.Product;
import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;

public class ProductDetails {

    private final BuyerDashboard navigator;
    private final Product product;

    private final BuyerCartController cartController =
            new BuyerCartController();

    public ProductDetails(
            BuyerDashboard navigator,
            Product product) {

        this.navigator = navigator;
        this.product = product;
    }

    public Node getView() {

        VBox root = new VBox(20);
        root.setPadding(new Insets(25));

        Button back =
                new Button("← Back to Marketplace");

        back.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-text-fill:#117864;" +
                "-fx-font-weight:bold;" +
                "-fx-cursor:hand;"
        );

        back.setOnAction(e ->
                navigator.setView(
                        new LiveMarketplace(navigator).getView()
                )
        );

        HBox content = new HBox(30);
        content.setAlignment(Pos.TOP_LEFT);

        StackPane imageBox = new StackPane();
        imageBox.setPrefSize(420, 350);
        imageBox.setStyle(
                "-fx-background-color:#E9F7EF;" +
                "-fx-background-radius:14;"
        );

        ImageView image =
                createImage(product.getImageUrl());

        if (image != null) {
            image.setFitWidth(390);
            image.setFitHeight(330);
            image.setPreserveRatio(true);
            imageBox.getChildren().add(image);
        } else {
            Label placeholder =
                    new Label("🌱");
            placeholder.setStyle("-fx-font-size:80px;");
            imageBox.getChildren().add(placeholder);
        }

        VBox info = new VBox(12);
        info.setPrefWidth(520);

        Label name =
                new Label(product.getName());
        name.setStyle(
                "-fx-font-size:28px;" +
                "-fx-font-weight:bold;"
        );

        Label price =
                new Label(
                        "₹" + format(product.getPrice())
                                + " / " + safe(product.getUnit())
                );
        price.setStyle(
                "-fx-font-size:20px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#117864;"
        );

        Label category =
                new Label("Category: " + safe(product.getCategory()));

        Label variety =
                new Label("Variety: " + safe(product.getVariety()));

        Label stock =
                new Label(
                        "Available: "
                                + format(product.getStock())
                                + " "
                                + safe(product.getUnit())
                );

        Label farmer =
                new Label(
                        "Farmer: "
                                + safe(product.getFarmerEmail())
                );

        Label description =
                new Label(
                        "Description:\n"
                                + safe(product.getDescription())
                );
        description.setWrapText(true);

        Spinner<Integer> quantity =
                new Spinner<>(
                        1,
                        Math.max(
                                1,
                                (int) Math.floor(product.getStock())
                        ),
                        1
                );

        quantity.setPrefWidth(110);

        HBox actions = new HBox(10);

        Button add =
                new Button("Add to Cart");

        Button buy =
                new Button("Buy Now");

        add.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#117864;" +
                "-fx-text-fill:#117864;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:10 20;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        buy.setStyle(
                "-fx-background-color:#117864;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:10 24;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        add.setOnAction(e ->
                addToCart(
                        quantity.getValue()
                )
        );

        buy.setOnAction(e -> {

            if (addToCart(quantity.getValue())) {
                navigator.setView(
                        new ShoppingCart(navigator).getView()
                );
            }
        });

        actions.getChildren().addAll(add, buy);

        info.getChildren().addAll(
                name,
                price,
                category,
                variety,
                stock,
                farmer,
                description,
                new Label("Quantity"),
                quantity,
                actions
        );

        content.getChildren().addAll(
                imageBox,
                info
        );

        root.getChildren().addAll(
                back,
                content
        );

        LanguageManager.apply(root);
        return root;
    }

    private boolean addToCart(int quantity) {

        BuyerCartItem item =
                new BuyerCartItem(
                        null,
                        navigator.getBuyerEmail(),
                        product.getProductId(),
                        product.getName(),
                        product.getFarmerEmail(),
                        product.getUnit(),
                        product.getPrice(),
                        quantity,
                        product.getImageUrl()
                );

        boolean success =
                cartController.addToCart(item);

        if (!success) {
            Alert alert =
                    new Alert(Alert.AlertType.ERROR);

            alert.setHeaderText(null);
            alert.setContentText(
                    "Unable to add product to cart."
            );
            alert.showAndWait();
        }

        return success;
    }

    private ImageView createImage(String url) {

        try {
            if (url == null || url.trim().isEmpty()) {
                return null;
            }

            Image image =
                    new Image(
                            new URL(url).openStream()
                    );

            if (image.isError()) {
                return null;
            }

            return new ImageView(image);

        } catch (Exception e) {
            return null;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String format(double value) {
        if (value == Math.rint(value)) {
            return String.valueOf((long) value);
        }

        return String.format("%.2f", value);
    }
}
