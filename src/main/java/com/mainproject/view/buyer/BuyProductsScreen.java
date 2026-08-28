package com.mainproject.view.buyer;

import com.mainproject.controller.ProductController;
import com.mainproject.controller.ProductOrderController;
import com.mainproject.model.Product;
import com.mainproject.model.ProductOrder;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class BuyProductsScreen {

    private final String buyerEmail;
    private final String buyerName;

    private final ProductController productController;
    private final ProductOrderController orderController;

    public BuyProductsScreen(
            String buyerEmail,
            String buyerName) {

        this.buyerEmail = buyerEmail;
        this.buyerName = buyerName;

        productController = new ProductController();
        orderController = new ProductOrderController();
    }


    public void show() {

        Stage stage = new Stage();

        stage.setTitle("Buy Products");

        VBox root = new VBox(15);

        root.setPadding(new Insets(20));

        root.setStyle(
                "-fx-background-color: #F8FAFC;"
        );

        // =====================================================
        // HEADER
        // =====================================================

        Label title =
                new Label("🛒 Buy Fresh Products");

        title.setStyle(
                "-fx-font-size: 26px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #166534;"
        );

        Label subtitle =
                new Label(
                        "Browse fresh products directly from farmers"
                );

        subtitle.setStyle(
                "-fx-text-fill: #64748B;"
        );

        // =====================================================
        // SEARCH
        // =====================================================

        TextField searchField =
                new TextField();

        searchField.setPromptText(
                "🔍 Search products..."
        );

        searchField.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-padding: 10;"
        );

        VBox productsBox =
                new VBox(15);

        productsBox.setPadding(
                new Insets(10)
        );

        ScrollPane scrollPane =
                new ScrollPane(productsBox);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background: transparent;"
        );

        // =====================================================
        // LOAD PRODUCTS
        // =====================================================

        loadProducts(
                productsBox,
                ""
        );

        searchField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) ->
                                loadProducts(
                                        productsBox,
                                        newValue
                                )
                );

        root.getChildren().addAll(
                title,
                subtitle,
                searchField,
                scrollPane
        );

        stage.setScene(
                new Scene(root, 850, 650)
        );

        stage.show();
    }


    // =====================================================
    // LOAD PRODUCTS
    // =====================================================

    private void loadProducts(
            VBox productsBox,
            String searchText) {

        productsBox.getChildren().clear();

        List<Product> products =
                productController.getAllProducts();

        boolean found = false;

        for (Product product : products) {

            // Only active products with stock

            if (product.getStatus() == null
                    || !product.getStatus()
                    .equalsIgnoreCase("active")) {

                continue;
            }

            if (product.getStock() <= 0) {
                continue;
            }

            // Search

            if (searchText != null
                    && !searchText.trim().isEmpty()) {

                String search =
                        searchText.toLowerCase();

                boolean matches =
                        safe(product.getName())
                                .toLowerCase()
                                .contains(search)

                                ||

                                safe(product.getCategory())
                                        .toLowerCase()
                                        .contains(search)

                                ||

                                safe(product.getVariety())
                                        .toLowerCase()
                                        .contains(search);

                if (!matches) {
                    continue;
                }
            }

            found = true;

            productsBox.getChildren()
                    .add(
                            createProductCard(product)
                    );
        }

        if (!found) {

            Label empty =
                    new Label(
                            "No products found."
                    );

            empty.setStyle(
                    "-fx-font-size: 16px;"
                            + "-fx-text-fill: #64748B;"
            );

            productsBox.getChildren()
                    .add(empty);
        }
    }


    // =====================================================
    // PRODUCT CARD
    // =====================================================

    private VBox createProductCard(
            Product product) {

        VBox card =
                new VBox(10);

        card.setPadding(
                new Insets(18)
        );

        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 12;"
                        + "-fx-border-color: #E2E8F0;"
                        + "-fx-border-radius: 12;"
        );

        Label name =
                new Label(
                        "🥬 " + safe(product.getName())
                );

        name.setStyle(
                "-fx-font-size: 20px;"
                        + "-fx-font-weight: bold;"
        );

        Label category =
                new Label(
                        "Category: "
                                + safe(product.getCategory())
                );

        Label variety =
                new Label(
                        "Variety: "
                                + safe(product.getVariety())
                );

        Label stock =
                new Label(
                        "Available: "
                                + product.getStock()
                                + " "
                                + safe(product.getUnit())
                );

        Label price =
                new Label(
                        "₹"
                                + String.format(
                                "%.2f",
                                product.getPrice()
                        )
                                + " per "
                                + safe(product.getUnit())
                );

        price.setStyle(
                "-fx-font-size: 17px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #16A34A;"
        );

        TextField quantityField =
                new TextField();

        quantityField.setPromptText(
                "Enter quantity"
        );

        quantityField.setMaxWidth(180);

        Button orderButton =
                new Button("🛒 Place Order");

        orderButton.setStyle(
                "-fx-background-color: #16A34A;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 8 16;"
                        + "-fx-background-radius: 8;"
        );

        orderButton.setOnAction(e ->
                placeOrder(
                        product,
                        quantityField
                )
        );

        HBox actionBox =
                new HBox(
                        12,
                        quantityField,
                        orderButton
                );

        actionBox.setAlignment(
                Pos.CENTER_LEFT
        );

        card.getChildren().addAll(
                name,
                category,
                variety,
                stock,
                price,
                actionBox
        );

        return card;
    }


    // =====================================================
    // PLACE ORDER
    // =====================================================

    private void placeOrder(
            Product product,
            TextField quantityField) {

        try {

            double quantity =
                    Double.parseDouble(
                            quantityField.getText()
                                    .trim()
                    );

            if (quantity <= 0) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Invalid Quantity",
                        "Please enter a valid quantity."
                );

                return;
            }

            if (quantity > product.getStock()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Insufficient Stock",
                        "Only "
                                + product.getStock()
                                + " "
                                + product.getUnit()
                                + " is available."
                );

                return;
            }

            ProductOrder order =
                    new ProductOrder();

            order.setProductId(
                    product.getProductId()
            );

            order.setBuyerEmail(
                    buyerEmail
            );

            order.setBuyerName(
                    buyerName
            );

            order.setQuantity(
                    quantity
            );

            boolean success =
                    orderController.createOrder(
                            order
                    );

            if (success) {

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Order Placed",
                        "Your order has been placed successfully!\n\n"
                                + "Product: "
                                + product.getName()
                                + "\nQuantity: "
                                + quantity
                                + " "
                                + product.getUnit()
                                + "\nTotal: ₹"
                                + String.format(
                                "%.2f",
                                product.getPrice()
                                        * quantity
                        )
                                + "\n\nWaiting for farmer approval."
                );

                quantityField.clear();

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Order Failed",
                        "Unable to place order."
                );
            }

        } catch (NumberFormatException ex) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Invalid Quantity",
                    "Please enter a valid number."
            );
        }
    }


    private String safe(String value) {

        return value == null
                ? "N/A"
                : value;
    }


    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert =
                new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}