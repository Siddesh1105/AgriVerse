package com.mainproject.view.buyer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CropPrices{

    private final BuyerDashboard mainController;

    public CropPrices(BuyerDashboard controller) {
        this.mainController = controller;
    }

    public static class PriceEntry {
        public SimpleStringProperty crop, min, max, avg, trend;
        public PriceEntry(String c, String mi, String ma, String a, String t) {
            crop = new SimpleStringProperty(c);
            min = new SimpleStringProperty(mi);
            max = new SimpleStringProperty(ma);
            avg = new SimpleStringProperty(a);
            trend = new SimpleStringProperty(t);
        }
    }

    public Node getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25, 30, 25, 30));

        Label title = new Label("Live Mandi / APMC Crop Daily Rates 📈");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<PriceEntry> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<PriceEntry, String> colCrop = new TableColumn<>("Crop Name");
        colCrop.setCellValueFactory(c -> c.getValue().crop);

        TableColumn<PriceEntry, String> colMin = new TableColumn<>("Min Price");
        colMin.setCellValueFactory(c -> c.getValue().min);

        TableColumn<PriceEntry, String> colMax = new TableColumn<>("Max Price");
        colMax.setCellValueFactory(c -> c.getValue().max);

        TableColumn<PriceEntry, String> colAvg = new TableColumn<>("Modal Price");
        colAvg.setCellValueFactory(c -> c.getValue().avg);

        TableColumn<PriceEntry, String> colTrend = new TableColumn<>("Trend");
        colTrend.setCellValueFactory(c -> c.getValue().trend);

        table.getColumns().addAll(colCrop, colMin, colMax, colAvg, colTrend);

        ObservableList<PriceEntry> data = FXCollections.observableArrayList(
            new PriceEntry("Tomato", "₹25 / kg", "₹32 / kg", "₹28 / kg", "▲ Up"),
            new PriceEntry("Wheat", "₹24 / kg", "₹28 / kg", "₹26 / kg", "▼ Down"),
            new PriceEntry("Onion", "₹18 / kg", "₹24 / kg", "₹22 / kg", "▲ Up"),
            new PriceEntry("Potato", "₹16 / kg", "₹22 / kg", "₹20 / kg", "▲ Up"),
            new PriceEntry("Cabbage", "₹12 / kg", "₹18 / kg", "₹15 / kg", "▼ Down")
        );
        table.setItems(data);

        root.getChildren().addAll(title, table);
        return root;
    }
}