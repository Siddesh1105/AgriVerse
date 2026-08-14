package com.mainproject.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class EquipmentRental {

    public Node getView() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(10));

        // Title
        VBox header = new VBox(2);
        Label title = new Label("Equipment Rental");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #1B2631;");
        Label sub = new Label("Rent equipment for your farm or list your equipment.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #566573;");
        header.getChildren().addAll(title, sub);

        // Search & Filters Bar
        HBox filterBar = new HBox(10);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchBox = new TextField();
        searchBox.setPromptText("Search equipment...");
        searchBox.setStyle("-fx-background-radius: 8px; -fx-border-color: #A2D9CE; -fx-border-radius: 8px; -fx-padding: 7 10;");
        HBox.setHgrow(searchBox, Priority.ALWAYS);

        ComboBox<String> catBox = new ComboBox<>();
        catBox.getItems().addAll("All Categories", "Tractors", "Harvesters", "Tillage");
        catBox.setValue("All Categories");
        catBox.setStyle("-fx-background-radius: 8px; -fx-border-color: #A2D9CE; -fx-border-radius: 8px;");

        ComboBox<String> locBox = new ComboBox<>();
        locBox.getItems().addAll("Location", "Nashik", "Pune", "Nagpur");
        locBox.setValue("Location");
        locBox.setStyle("-fx-background-radius: 8px; -fx-border-color: #A2D9CE; -fx-border-radius: 8px;");

        Button searchBtn = new Button("Search");
        searchBtn.setStyle("-fx-background-color: #117864; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 7 16; -fx-cursor: hand;");

        filterBar.getChildren().addAll(searchBox, catBox, locBox, searchBtn);

        // 2x3 Grid of Equipment Cards
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        grid.add(createEquipCard("🚜", "Tractor", "₹1,500 / day", "Nashik"), 0, 0);
        grid.add(createEquipCard("⚙️", "Rotavator", "₹800 / day", "Nashik"), 1, 0);
        grid.add(createEquipCard("🌾", "Harvester", "₹3,000 / day", "Nashik"), 2, 0);

        grid.add(createEquipCard("💧", "Sprayer", "₹930 / day", "Nashik"), 0, 1);
        grid.add(createEquipCard("🛠️", "Cultivator", "₹700 / day", "Nashik"), 1, 1);
        grid.add(createEquipCard("🌱", "Seed Drill", "₹900 / day", "Nashik"), 2, 1);

        root.getChildren().addAll(header, filterBar, grid);
        return new ScrollPane(root);
    }

    private VBox createEquipCard(String icon, String title, String rate, String loc) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12px; -fx-border-color: #A2D9CE; -fx-border-radius: 12px;");
        card.setPrefWidth(260);

        Label iconLbl = new Label(icon);
        iconLbl.setStyle("-fx-font-size: 40px;");
        iconLbl.setAlignment(Pos.CENTER);

        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1B2631;");

        Label rateLbl = new Label(rate);
        rateLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: 800; -fx-text-fill: #117864;");

        HBox bot = new HBox();
        Label locLbl = new Label(loc);
        locLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #566573;");

        Button viewBtn = new Button("View Details");
        viewBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #117864; -fx-font-weight: bold; -fx-cursor: hand;");

        bot.getChildren().addAll(locLbl, viewBtn);

        card.getChildren().addAll(iconLbl, titleLbl, rateLbl, bot);
        return card;
    }
}
