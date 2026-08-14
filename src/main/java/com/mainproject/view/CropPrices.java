package com.mainproject.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CropPrices {

    public Node getView() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(10));

        // Header with Mandi Selector
        HBox top = new HBox();
        top.setAlignment(Pos.CENTER_LEFT);

        VBox titles = new VBox(2);
        Label title = new Label("Crop Prices");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");
        Label sub = new Label("Check latest market prices for different crops.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        titles.getChildren().addAll(title, sub);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        ComboBox<String> marketBox = new ComboBox<>();
        marketBox.getItems().addAll("Nashik Market", "Pune APMC", "Mumbai Market");
        marketBox.setValue("Nashik Market");
        marketBox.setStyle("-fx-background-radius: 8px; -fx-border-color: #A2D9CE;");

        top.getChildren().addAll(titles, sp, marketBox);

        // Price Table
        VBox table = new VBox();
        table.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #A2D9CE; -fx-border-radius: 12px;");

        table.getChildren().add(createRow("Crop", "Min Price", "Max Price", "Modal Price", "Trend", true));
        table.getChildren().add(createRow("🍅 Tomato", "₹15 / kg", "₹22 / kg", "₹18 / kg", "+5%", false));
        table.getChildren().add(createRow("🧅 Onion", "₹12 / kg", "₹16 / kg", "₹15 / kg", "+2%", false));
        table.getChildren().add(createRow("🥔 Potato", "₹10 / kg", "₹16 / kg", "₹13 / kg", "-1%", false));
        table.getChildren().add(createRow("🌾 Wheat", "₹22 / kg", "₹29 / kg", "₹25 / kg", "+3%", false));
        table.getChildren().add(createRow("🥬 Cabbage", "₹8 / kg", "₹12 / kg", "₹10 / kg", "+1%", false));
        table.getChildren().add(createRow("🌶️ Chilli", "₹40 / kg", "₹60 / kg", "₹50 / kg", "+4%", false));

        root.getChildren().addAll(top, table);
        return new ScrollPane(root);
    }

    private HBox createRow(String crop, String min, String max, String modal, String trend, boolean isHeader) {
        HBox row = new HBox();
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);

        Label cLbl = new Label(crop);
        cLbl.setPrefWidth(150);
        Label minLbl = new Label(min);
        minLbl.setPrefWidth(120);
        Label maxLbl = new Label(max);
        maxLbl.setPrefWidth(120);
        Label modLbl = new Label(modal);
        modLbl.setPrefWidth(120);
        Label trndLbl = new Label(trend);
        trndLbl.setPrefWidth(100);

        if (isHeader) {
            String hStyle = "-fx-font-size: 13px; -fx-font-weight: 800; -fx-text-fill: #1B2631;";
            cLbl.setStyle(hStyle);
            minLbl.setStyle(hStyle);
            maxLbl.setStyle(hStyle);
            modLbl.setStyle(hStyle);
            trndLbl.setStyle(hStyle);
            row.setStyle("-fx-background-color: #D4EFDF; -fx-background-radius: 12px 12px 0 0; -fx-border-color: #A2D9CE; -fx-border-width: 0 0 1 0;");
        } else {
            String bStyle = "-fx-font-size: 13px; -fx-text-fill: #1B2631;";
            cLbl.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1B2631;");
            minLbl.setStyle(bStyle);
            maxLbl.setStyle(bStyle);
            modLbl.setStyle(bStyle);

            if (trend.startsWith("+")) {
                trndLbl.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #117864;");
            } else {
                trndLbl.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #C0392B;");
            }
            row.setStyle("-fx-border-color: #EBF5FB; -fx-border-width: 0 0 1 0;");
        }

        row.getChildren().addAll(cLbl, minLbl, maxLbl, modLbl, trndLbl);
        return row;
    }
}