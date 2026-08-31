package com.mainproject.view.farmer;

import com.mainproject.controller.FarmerEarningsController;
import com.mainproject.controller.EquipmentRentalController;
import com.mainproject.controller.ProductOrderController;
import com.mainproject.model.ProductOrder;
import com.mainproject.model.EquipmentRental;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Read-only farmer earnings page. Existing payment and rental flows are untouched. */
public class Earnings {
    private final String farmerEmail;
    private final FarmerEarningsController earningsController = new FarmerEarningsController();
    private final EquipmentRentalController rentalController = new EquipmentRentalController();
    private final ProductOrderController productOrderController = new ProductOrderController();

    public Earnings(String farmerEmail) { this.farmerEmail = farmerEmail; }

    public Node getView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color:#F8FAFC;");

        Label title = new Label("💰 My Earnings");
        title.setStyle("-fx-font-size:28px;-fx-font-weight:bold;-fx-text-fill:#0F172A;");
        Label subtitle = new Label("Your confirmed earnings from paid product sales and equipment rentals.");
        subtitle.setStyle("-fx-text-fill:#64748B;-fx-font-size:14px;");

        double productTotal = earningsController.getProductEarnings(farmerEmail);
        double rentalTotal = earningsController.getRentalEarnings(farmerEmail);
        double total = earningsController.getTotalEarnings(farmerEmail);
        int productCount = earningsController.getPaidProductCount(farmerEmail);
        int rentalCount = earningsController.getPaidRentalCount(farmerEmail);

        HBox cards = new HBox(18,
                card("Total Earnings", money(total)),
                card("Product Sales", money(productTotal)),
                card("Equipment Rentals", money(rentalTotal)));
        HBox counts = new HBox(18,
                card("Paid Product Orders", String.valueOf(productCount)),
                card("Paid Rentals", String.valueOf(rentalCount)));

        Label productTitle = new Label("Recent Paid Product Sales");
        productTitle.setStyle("-fx-font-size:19px;-fx-font-weight:bold;-fx-text-fill:#0F172A;");
        VBox productList = new VBox(10);
        for (ProductOrder order : productOrderController.getOrdersByFarmer(farmerEmail)) {
            if (order != null && "paid".equalsIgnoreCase(order.getPaymentStatus())) {
                VBox row = transactionRow("🛒 " + safe(order.getProductName()),
                        "Buyer: " + safe(order.getBuyerName()) + "   •   Amount: " + money(order.getTotalAmount()) + "   •   " + safe(order.getPaymentMethod()));
                productList.getChildren().add(row);
            }
        }
        if (productList.getChildren().isEmpty()) {
            Label empty = new Label("No paid product sales yet."); empty.setStyle("-fx-text-fill:#64748B;"); productList.getChildren().add(empty);
        }

        Label recentTitle = new Label("Recent Paid Rental Transactions");
        recentTitle.setStyle("-fx-font-size:19px;-fx-font-weight:bold;-fx-text-fill:#0F172A;");
        VBox list = new VBox(10);
        for (EquipmentRental rental : rentalController.getRentalsByFarmer(farmerEmail)) {
            if (rental != null && "paid".equalsIgnoreCase(rental.getPaymentStatus())) {
                list.getChildren().add(transactionRow("🚜 " + safe(rental.getEquipmentName()),
                        "Renter: " + safe(rental.getBuyerName()) + "   •   Payment: " + money(rental.getTotalAmount()) + "   •   " + safe(rental.getPaymentMethod())));
            }
        }
        if (list.getChildren().isEmpty()) {
            Label empty = new Label("No paid rental earnings yet."); empty.setStyle("-fx-text-fill:#64748B;"); list.getChildren().add(empty);
        }
        root.getChildren().addAll(title, subtitle, cards, counts, productTitle, productList, recentTitle, list);
        ScrollPane scroll = new ScrollPane(root); scroll.setFitToWidth(true); scroll.setStyle("-fx-background:#F8FAFC;-fx-background-color:#F8FAFC;");
        return scroll;
    }

    private VBox transactionRow(String nameText, String detailsText) {
        VBox row = new VBox(5); row.setPadding(new Insets(14));
        row.setStyle("-fx-background-color:white;-fx-background-radius:10;-fx-border-color:#E2E8F0;-fx-border-radius:10;");
        Label name = new Label(nameText); name.setStyle("-fx-font-weight:bold;-fx-font-size:15px;");
        Label details = new Label(detailsText); details.setStyle("-fx-text-fill:#475569;");
        row.getChildren().addAll(name, details); return row;
    }

    private String money(double value) { return "₹" + String.format("%.2f", value); }

    private VBox card(String heading, String value) {
        VBox card = new VBox(8); card.setPadding(new Insets(20)); card.setPrefWidth(260);
        card.setStyle("-fx-background-color:white;-fx-background-radius:12;-fx-border-color:#E2E8F0;-fx-border-radius:12;");
        Label h = new Label(heading); h.setStyle("-fx-text-fill:#64748B;-fx-font-size:13px;");
        Label v = new Label(value); v.setStyle("-fx-font-size:25px;-fx-font-weight:bold;-fx-text-fill:#16A34A;");
        card.getChildren().addAll(h,v); return card;
    }
    private String safe(String s) { return s == null || s.isBlank() ? "-" : s; }
}
