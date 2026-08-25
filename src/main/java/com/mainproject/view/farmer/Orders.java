package com.mainproject.view.farmer;

import com.mainproject.util.LanguageManager;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Orders {

    public Node getView() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(10));

        VBox titles = new VBox(2);
        Label title = new Label("Orders");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");
        Label sub = new Label("Manage your customer orders and deliveries.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        titles.getChildren().addAll(title, sub);

        // Filter Tabs
        HBox tabs = new HBox(8);
        tabs.getChildren().addAll(
            createChip("All Orders", true),
            createChip("Pending", false),
            createChip("Completed", false),
            createChip("Cancelled", false)
        );

        // Orders Table
        VBox table = new VBox();
        table.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #A2D9CE;");

        table.getChildren().add(createOrderRow("Order ID", "Product", "Amount", "Status", "Date", true));
        table.getChildren().add(createOrderRow("#ORD124", "Tomato (50 kg)", "₹1,000", "Pending", "May 12, 2025", false));
        table.getChildren().add(createOrderRow("#ORD123", "Onion (100 kg)", "₹1,800", "Completed", "May 10, 2025", false));
        table.getChildren().add(createOrderRow("#ORD122", "Potato (75 kg)", "₹1,125", "Completed", "May 8, 2025", false));
        table.getChildren().add(createOrderRow("#ORD121", "Wheat (200 kg)", "₹4,800", "Cancelled", "May 5, 2025", false));

        root.getChildren().addAll(titles, tabs, table);
        return new ScrollPane(root);
    }

    private Button createChip(String text, boolean active) {
        Button b = new Button(text);
        if (active) {
            b.setStyle("-fx-background-color: #117864; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20px; -fx-padding: 4 14; -fx-cursor: hand;");
        } else {
            b.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #A2D9CE; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-text-fill: #1B2631; -fx-padding: 4 14; -fx-cursor: hand;");
        }
        return b;
    }

    private HBox createOrderRow(String id, String prod, String amt, String status, String date, boolean isHeader) {
        HBox row = new HBox();
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);

        Label idLbl = new Label(id);
        idLbl.setPrefWidth(120);
        Label prodLbl = new Label(prod);
        prodLbl.setPrefWidth(160);
        Label amtLbl = new Label(amt);
        amtLbl.setPrefWidth(120);
        Label statLbl = new Label(status);
        statLbl.setPrefWidth(120);
        Label dateLbl = new Label(date);
        dateLbl.setPrefWidth(130);

        if (isHeader) {
            String hStyle = "-fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: #1B2631;";
            idLbl.setStyle(hStyle);
            prodLbl.setStyle(hStyle);
            amtLbl.setStyle(hStyle);
            statLbl.setStyle(hStyle);
            dateLbl.setStyle(hStyle);
            row.setStyle("-fx-background-color: #D4EFDF; -fx-background-radius: 12px 12px 0 0; -fx-border-color: #A2D9CE; -fx-border-width: 0 0 1 0;");
            row.getChildren().addAll(idLbl, prodLbl, amtLbl, statLbl, dateLbl);
        } else {
            idLbl.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1B2631;");
            prodLbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #1B2631;");
            amtLbl.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #117864;");
            dateLbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");

            if ("Pending".equalsIgnoreCase(status)) {
                statLbl.setStyle("-fx-background-color: #FCF3CF; -fx-text-fill: #B7950B; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 10px;");
            } else if ("Completed".equalsIgnoreCase(status)) {
                statLbl.setStyle("-fx-background-color: #D4EFDF; -fx-text-fill: #117864; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 10px;");
            } else {
                statLbl.setStyle("-fx-background-color: #FADBD8; -fx-text-fill: #C0392B; -fx-font-weight: bold; -fx-padding: 3 8; -fx-background-radius: 10px;");
            }

            Button viewBtn = new Button("View");
            viewBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #A2D9CE; -fx-border-radius: 6px; -fx-cursor: hand;");

            row.setStyle("-fx-border-color: #EBF5FB; -fx-border-width: 0 0 1 0;");
            row.getChildren().addAll(idLbl, prodLbl, amtLbl, statLbl, dateLbl, viewBtn);
        }

        return row;
    }
}