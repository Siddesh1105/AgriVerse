package com.mainproject.view.farmer;


import com.mainproject.controller.EquipmentController;
import com.mainproject.controller.CartController;
import com.mainproject.model.Equipment;
import com.mainproject.model.CartItem;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class EquipmentRental {

        // =========================================================
        // COLORS
        // =========================================================

        private static final String GREEN = "#117864";
        private static final String GREEN_DARK = "#0E6655";
        private static final String BORDER = "#A2D9CE";
        private static final String TEXT = "#1B2631";
        private static final String SECONDARY = "#566573";

        // =========================================================
        // DAO
        // =========================================================

        private final EquipmentController equipmentController = new EquipmentController();

        private final CartController cartController = new CartController();

        // =========================================================
        // DATA
        // =========================================================

        private final List<Equipment> equipmentList = new ArrayList<>();

        // =========================================================
        // CURRENT LOGGED-IN FARMER
        // =========================================================

        private final String farmerEmail;
        private final String farmerName;

        private final Runnable openAddEquipment;

        // =========================================================
        // UI
        // =========================================================

        private GridPane equipmentGrid;

        private TextField searchField;

        private ComboBox<String> categoryCombo;

        private ComboBox<String> locationCombo;

        // =========================================================
        // CONSTRUCTOR
        // =========================================================

        public EquipmentRental(
                        String farmerEmail,
                        String farmerName,
                        Runnable openAddEquipment) {

                this.farmerEmail = farmerEmail;
                this.farmerName = farmerName;
                this.openAddEquipment = openAddEquipment;

                loadEquipment();
        }

        // =========================================================
        // MAIN VIEW
        // =========================================================

        public Node getView() {

                VBox root = new VBox(18);

                root.setPadding(
                                new Insets(20));

                root.setStyle(
                                "-fx-background-color:#F4FBF7;");

                // =====================================================
                // TITLE
                // =====================================================

                Label title = new Label("Equipment Rental");

                title.setStyle(
                                "-fx-font-size:28px;" +
                                                "-fx-font-weight:800;" +
                                                "-fx-text-fill:" + TEXT + ";");

                Label subtitle = new Label(
                                "Rent equipment for your farm or list your equipment.");

                subtitle.setStyle(
                                "-fx-font-size:14px;" +
                                                "-fx-text-fill:" + SECONDARY + ";");

                VBox titleBox = new VBox(
                                4,
                                title,
                                subtitle);

                HBox.setHgrow(
                                titleBox,
                                Priority.ALWAYS);

                // =====================================================
                // ADD EQUIPMENT BUTTON
                // =====================================================

                Button addButton = new Button("+ Add Equipment");

                addButton.setPrefHeight(42);

                addButton.setPrefWidth(155);

                addButton.setStyle(
                                primaryStyle());

                addButton.setOnMouseEntered(
                                e -> addButton.setStyle(
                                                primaryHoverStyle()));

                addButton.setOnMouseExited(
                                e -> addButton.setStyle(
                                                primaryStyle()));

                addButton.setOnAction(
                                e -> {

                                        if (openAddEquipment != null) {

                                                openAddEquipment.run();
                                        }
                                });

                // =====================================================
                // HEADER
                // =====================================================

                HBox header = new HBox(
                                titleBox,
                                addButton);

                header.setAlignment(
                                Pos.CENTER_LEFT);

                // =====================================================
                // SEARCH FIELD
                // =====================================================

                searchField = new TextField();

                searchField.setPromptText(
                                "Search equipment...");

                searchField.setPrefHeight(42);

                searchField.setStyle(
                                inputStyle());

                HBox.setHgrow(
                                searchField,
                                Priority.ALWAYS);

                searchField.textProperty()
                                .addListener(
                                                (obs, oldValue, newValue) -> filterEquipment());

                // =====================================================
                // CATEGORY FILTER
                // =====================================================

                categoryCombo = new ComboBox<>();

                categoryCombo
                                .getItems()
                                .add(
                                                "All Categories");

                addCategories();

                categoryCombo.setValue(
                                "All Categories");

                categoryCombo.setPrefHeight(42);

                categoryCombo.setPrefWidth(165);

                categoryCombo.setStyle(
                                comboStyle());

                categoryCombo.setOnAction(
                                e -> filterEquipment());

                // =====================================================
                // LOCATION FILTER
                // =====================================================

                locationCombo = new ComboBox<>();

                locationCombo
                                .getItems()
                                .add(
                                                "All Locations");

                addLocations();

                locationCombo.setValue(
                                "All Locations");

                locationCombo.setPrefHeight(42);

                locationCombo.setPrefWidth(150);

                locationCombo.setStyle(
                                comboStyle());

                locationCombo.setOnAction(
                                e -> filterEquipment());

                // =====================================================
                // SEARCH BUTTON
                // =====================================================

                Button searchButton = new Button("Search");

                searchButton.setPrefWidth(90);

                searchButton.setPrefHeight(42);

                searchButton.setStyle(
                                primaryStyle());

                searchButton.setOnAction(
                                e -> filterEquipment());

                // =====================================================
                // FILTER BAR
                // =====================================================

                HBox filters = new HBox(
                                10,
                                searchField,
                                categoryCombo,
                                locationCombo,
                                searchButton);

                filters.setAlignment(
                                Pos.CENTER_LEFT);

                // =====================================================
                // EQUIPMENT GRID
                // =====================================================

                equipmentGrid = new GridPane();

                equipmentGrid.setHgap(16);

                equipmentGrid.setVgap(16);

                equipmentGrid.setPadding(
                                new Insets(
                                                4,
                                                0,
                                                20,
                                                0));

                // Four equal columns
                for (int i = 0; i < 4; i++) {

                        ColumnConstraints column = new ColumnConstraints();

                        column.setPercentWidth(25);

                        column.setHgrow(
                                        Priority.ALWAYS);

                        equipmentGrid
                                        .getColumnConstraints()
                                        .add(column);
                }

                // =====================================================
                // BUILD INITIAL CARDS
                // =====================================================

                buildCards(
                                equipmentList);

                // =====================================================
                // SCROLL PANE
                // =====================================================

                ScrollPane scroll = new ScrollPane(
                                equipmentGrid);

                scroll.setFitToWidth(true);

                scroll.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scroll.setStyle(
                                "-fx-background-color:transparent;" +
                                                "-fx-background:transparent;" +
                                                "-fx-border-color:transparent;");

                VBox.setVgrow(
                                scroll,
                                Priority.ALWAYS);

                // =====================================================
                // ROOT
                // =====================================================

                root.getChildren().addAll(
                                header,
                                filters,
                                scroll);


                return root;
        }

        // =========================================================
        // LOAD EQUIPMENT
        // =========================================================
        //
        // IMPORTANT:
        //
        // Show ONLY equipment belonging to OTHER farmers.
        //
        // The current logged-in farmer's equipment is excluded
        // using ownerEmail.
        //
        // =========================================================

        private void loadEquipment() {

                try {

                        equipmentList.clear();

                        // Get all equipment from Firestore
                        List<Equipment> data = equipmentController.getAllEquipment();

                        if (data != null) {

                                for (Equipment equipment : data) {

                                        String ownerEmail = safe(
                                                        equipment.getOwnerEmail(),
                                                        "");

                                        /*
                                         * IMPORTANT FILTER
                                         *
                                         * If ownerEmail is the same as the
                                         * logged-in farmer's email, DO NOT
                                         * show that equipment.
                                         *
                                         * Otherwise, add it to the rental list.
                                         */

                                        if (!ownerEmail.isEmpty()
                                                        && farmerEmail != null
                                                        && ownerEmail.equalsIgnoreCase(
                                                                        farmerEmail.trim())) {

                                                // This is MY equipment.
                                                // Do NOT show it here.
                                                continue;
                                        }

                                        // Equipment belongs to another farmer
                                        equipmentList.add(
                                                        equipment);
                                }
                        }

                        System.out.println(
                                        "Other farmers' equipment loaded: "
                                                        + equipmentList.size());

                } catch (Exception e) {

                        System.out.println(
                                        "Error loading other farmers' equipment:");

                        e.printStackTrace();
                }
        }

        // =========================================================
        // ADD CATEGORIES
        // =========================================================

        private void addCategories() {

                for (Equipment e : equipmentList) {

                        String value = safe(
                                        e.getCategory(),
                                        "");

                        if (!value.isEmpty()
                                        && !categoryCombo
                                                        .getItems()
                                                        .contains(value)) {

                                categoryCombo
                                                .getItems()
                                                .add(value);
                        }
                }
        }

        // =========================================================
        // ADD LOCATIONS
        // =========================================================

        private void addLocations() {

                for (Equipment e : equipmentList) {

                        String value = safe(
                                        e.getLocation(),
                                        "");

                        if (!value.isEmpty()
                                        && !locationCombo
                                                        .getItems()
                                                        .contains(value)) {

                                locationCombo
                                                .getItems()
                                                .add(value);
                        }
                }
        }

        // =========================================================
        // BUILD EQUIPMENT CARDS
        // =========================================================

        private void buildCards(
                        List<Equipment> list) {

                equipmentGrid
                                .getChildren()
                                .clear();

                // =====================================================
                // EMPTY STATE
                // =====================================================

                if (list == null
                                || list.isEmpty()) {

                        VBox empty = new VBox(8);

                        empty.setAlignment(
                                        Pos.CENTER);

                        empty.setPadding(
                                        new Insets(50));

                        Label icon = new Label("🚜");

                        icon.setStyle(
                                        "-fx-font-size:40px;");

                        Label message = new Label(
                                        "No equipment found.");

                        message.setStyle(
                                        "-fx-font-size:16px;" +
                                                        "-fx-font-weight:bold;" +
                                                        "-fx-text-fill:" +
                                                        SECONDARY +
                                                        ";");

                        empty.getChildren().addAll(
                                        icon,
                                        message);

                        equipmentGrid.add(
                                        empty,
                                        0,
                                        0,
                                        4,
                                        1);

                        return;
                }

                // =====================================================
                // FOUR CARDS PER ROW
                // =====================================================

                int column = 0;

                int row = 0;

                for (Equipment equipment : list) {

                        VBox card = createCard(
                                        equipment);

                        equipmentGrid.add(
                                        card,
                                        column,
                                        row);

                        column++;

                        if (column == 4) {

                                column = 0;

                                row++;
                        }
                }
        }

        // =========================================================
        // CREATE EQUIPMENT CARD
        // =========================================================

        private VBox createCard(
                        Equipment equipment) {

                VBox card = new VBox(8);

                card.setPrefWidth(260);

                card.setMinWidth(0);

                card.setMaxWidth(
                                Double.MAX_VALUE);

                card.setPrefHeight(310);

                card.setPadding(
                                new Insets(12));

                card.setStyle(
                                cardStyle());

                // =====================================================
                // IMAGE BOX
                // =====================================================

                StackPane imageBox = new StackPane();

                imageBox.setPrefHeight(125);

                imageBox.setMinHeight(125);

                imageBox.setMaxHeight(125);

                imageBox.setStyle(
                                "-fx-background-color:#F1FAF6;" +
                                                "-fx-background-radius:10px;");

                String imageUrl = safe(
                                equipment.getImageUrl(),
                                "");

                if (!imageUrl.isEmpty()) {

                        try {

                                Image image = new Image(
                                                imageUrl,
                                                240,
                                                120,
                                                true,
                                                true,
                                                true);

                                ImageView imageView = new ImageView(
                                                image);

                                imageView.setFitWidth(240);

                                imageView.setFitHeight(120);

                                imageView.setPreserveRatio(
                                                true);

                                imageBox
                                                .getChildren()
                                                .add(
                                                                imageView);

                        } catch (Exception ex) {

                                addPlaceholder(
                                                imageBox);
                        }

                } else {

                        addPlaceholder(
                                        imageBox);
                }

                // =====================================================
                // NAME
                // =====================================================

                Label name = new Label(
                                safe(
                                                equipment.getName(),
                                                "Equipment"));

                name.setStyle(
                                "-fx-font-size:17px;" +
                                                "-fx-font-weight:800;" +
                                                "-fx-text-fill:" +
                                                TEXT +
                                                ";");

                // =====================================================
                // CATEGORY
                // =====================================================

                Label category = new Label(
                                safe(
                                                equipment.getCategory(),
                                                "Agricultural Equipment"));

                category.setStyle(
                                "-fx-font-size:12px;" +
                                                "-fx-text-fill:" +
                                                SECONDARY +
                                                ";");

                // =====================================================
                // PRICE
                // =====================================================

                Label price = new Label(
                                "₹"
                                                + formatPrice(
                                                                equipment.getPrice())
                                                + " / day");

                price.setStyle(
                                "-fx-font-size:16px;" +
                                                "-fx-font-weight:800;" +
                                                "-fx-text-fill:" +
                                                GREEN +
                                                ";");

                // =====================================================
                // AVAILABILITY
                // =====================================================

                Label availability = new Label(
                                equipment.isAvailable()
                                                ? "● Available"
                                                : "● Not Available");

                availability.setStyle(
                                equipment.isAvailable()

                                                ? "-fx-text-fill:#117864;" +
                                                                "-fx-font-size:12px;" +
                                                                "-fx-font-weight:bold;"

                                                : "-fx-text-fill:#C0392B;" +
                                                                "-fx-font-size:12px;" +
                                                                "-fx-font-weight:bold;");

                // =====================================================
                // LOCATION
                // =====================================================

                Label location = new Label(
                                "● "
                                                + safe(
                                                                equipment.getLocation(),
                                                                "Unknown"));

                location.setStyle(
                                "-fx-font-size:12px;" +
                                                "-fx-text-fill:" +
                                                SECONDARY +
                                                ";");

                HBox.setHgrow(
                                location,
                                Priority.ALWAYS);

                // =====================================================
                // ADD TO CART BUTTON
                // =====================================================

                Button addToCart = new Button(
                                "Add to Cart");

                addToCart.setPrefHeight(32);

                addToCart.setStyle(
                                primaryStyle());

                addToCart.setOnMouseEntered(
                                e -> addToCart.setStyle(
                                                primaryHoverStyle()));

                addToCart.setOnMouseExited(
                                e -> addToCart.setStyle(
                                                primaryStyle()));

                addToCart.setDisable(!equipment.isAvailable());

                addToCart.setOnAction(
                                e -> addEquipmentToCart(
                                                equipment));

                // =====================================================
                // VIEW DETAILS BUTTON
                // =====================================================

                Button details = new Button(
                                "View Details");

                details.setPrefHeight(32);

                details.setStyle(
                                outlineStyle());

                details.setOnMouseEntered(
                                e -> details.setStyle(
                                                outlineHoverStyle()));

                details.setOnMouseExited(
                                e -> details.setStyle(
                                                outlineStyle()));

                details.setOnAction(
                                e -> showDetails(
                                                equipment));

                // =====================================================
                // CARD BOTTOM
                // =====================================================

                HBox bottom = new HBox(
                                8,
                                location,
                                addToCart,
                                details);

                bottom.setAlignment(
                                Pos.CENTER_LEFT);

                // =====================================================
                // ADD CARD CONTENT
                // =====================================================

                card.getChildren().addAll(
                                imageBox,
                                name,
                                category,
                                price,
                                availability,
                                bottom);

                // =====================================================
                // CARD HOVER
                // =====================================================

                card.setOnMouseEntered(
                                e -> card.setStyle(
                                                cardHoverStyle()));

                card.setOnMouseExited(
                                e -> card.setStyle(
                                                cardStyle()));

                return card;
        }

        // =========================================================
        // PLACEHOLDER IMAGE
        // =========================================================

        private void addPlaceholder(
                        StackPane box) {

                Label label = new Label("🚜");

                label.setStyle(
                                "-fx-font-size:38px;");

                box.getChildren()
                                .add(label);
        }

        // =========================================================
        // FILTER EQUIPMENT
        // =========================================================

        private void filterEquipment() {

                String search = searchField
                                .getText()
                                .trim()
                                .toLowerCase();

                String category = categoryCombo.getValue();

                String location = locationCombo.getValue();

                List<Equipment> filtered = new ArrayList<>();

                for (Equipment e : equipmentList) {

                        String name = safe(
                                        e.getName(),
                                        "").toLowerCase();

                        String cat = safe(
                                        e.getCategory(),
                                        "").toLowerCase();

                        String loc = safe(
                                        e.getLocation(),
                                        "").toLowerCase();

                        // =================================================
                        // SEARCH MATCH
                        // =================================================

                        boolean searchMatch = search.isEmpty()

                                        || name.contains(
                                                        search)

                                        || cat.contains(
                                                        search)

                                        || loc.contains(
                                                        search);

                        // =================================================
                        // CATEGORY MATCH
                        // =================================================

                        boolean categoryMatch = category == null

                                        || category.equals(
                                                        "All Categories")

                                        || cat.equals(
                                                        category.toLowerCase());

                        // =================================================
                        // LOCATION MATCH
                        // =================================================

                        boolean locationMatch = location == null

                                        || location.equals(
                                                        "All Locations")

                                        || loc.equals(
                                                        location.toLowerCase());

                        // =================================================
                        // ADD MATCHING EQUIPMENT
                        // =================================================

                        if (searchMatch
                                        && categoryMatch
                                        && locationMatch) {

                                filtered.add(e);
                        }
                }

                buildCards(
                                filtered);
        }

        // =========================================================
        // ADD EQUIPMENT TO CART
        // =========================================================

        private void addEquipmentToCart(
                        Equipment equipment) {

                try {

                        if (equipment == null) {
                                showAlert(
                                                Alert.AlertType.ERROR,
                                                "Error",
                                                "Equipment information is missing.");
                                return;
                        }

                        if (!equipment.isAvailable()) {
                                showAlert(
                                                Alert.AlertType.WARNING,
                                                "Equipment Unavailable",
                                                "This equipment is currently not available.");
                                return;
                        }

                        if (farmerEmail == null
                                        || farmerEmail.trim().isEmpty()) {
                                showAlert(
                                                Alert.AlertType.ERROR,
                                                "Login Required",
                                                "Unable to identify the logged-in farmer.");
                                return;
                        }

                        // A farmer cannot rent their own equipment.
                        if (equipment.getOwnerEmail() != null
                                        && equipment.getOwnerEmail().equalsIgnoreCase(
                                                        farmerEmail.trim())) {
                                showAlert(
                                                Alert.AlertType.WARNING,
                                                "Cannot Rent",
                                                "You cannot rent your own equipment.");
                                return;
                        }

                        CartItem cartItem = new CartItem(
                                        farmerEmail,
                                        equipment);

                        boolean added = cartController.addToCart(
                                        cartItem);

                        if (added) {
                                showAlert(
                                                Alert.AlertType.INFORMATION,
                                                "Added to Cart",
                                                equipment.getName()
                                                                + " has been added to your rental cart.");
                        } else {
                                showAlert(
                                                Alert.AlertType.ERROR,
                                                "Cart Error",
                                                "Unable to add equipment to cart.");
                        }

                } catch (Exception e) {
                        System.out.println(
                                        "Error adding equipment to cart:");
                        e.printStackTrace();

                        showAlert(
                                        Alert.AlertType.ERROR,
                                        "Error",
                                        "Something went wrong while adding equipment to cart.");
                }
        }

        // =========================================================
        // SHOW ALERT
        // =========================================================

        private void showAlert(
                        Alert.AlertType type,
                        String title,
                        String message) {

                Alert alert = new Alert(type);

                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }

        // =========================================================
        // SHOW EQUIPMENT DETAILS
        // =========================================================

        private void showDetails(
                        Equipment equipment) {

                Alert alert = new Alert(
                                Alert.AlertType.INFORMATION);

                alert.setTitle(
                                "Equipment Details");

                alert.setHeaderText(
                                safe(
                                                equipment.getName(),
                                                "Equipment"));

                alert.setContentText(

                                "Category: "
                                                + safe(
                                                                equipment.getCategory(),
                                                                "-")

                                                + "\n\nPrice: ₹"
                                                + formatPrice(
                                                                equipment.getPrice())
                                                + " / day"

                                                + "\n\nLocation: "
                                                + safe(
                                                                equipment.getLocation(),
                                                                "-")

                                                + "\n\nOwner: "
                                                + safe(
                                                                equipment.getOwnerName(),
                                                                "-")

                                                + "\n\nContact: "
                                                + safe(
                                                                equipment.getOwnerEmail(),
                                                                "-")

                                                + "\n\nAvailability: "
                                                + (equipment.isAvailable()
                                                                ? "Available"
                                                                : "Not Available")

                                                + "\n\nDescription: "
                                                + safe(
                                                                equipment.getDescription(),
                                                                "-"));

                alert.showAndWait();
        }

        // =========================================================
        // SAFE STRING
        // =========================================================

        private String safe(
                        String value,
                        String fallback) {

                return value == null
                                || value.trim().isEmpty()
                                                ? fallback
                                                : value;
        }

        // =========================================================
        // FORMAT PRICE
        // =========================================================

        private String formatPrice(
                        double value) {

                if (value == (long) value) {

                        return String.format(
                                        "%d",
                                        (long) value);
                }

                return String.format(
                                "%.2f",
                                value);
        }

        // =========================================================
        // INPUT STYLE
        // =========================================================

        private String inputStyle() {

                return "-fx-background-color:white;" +
                                "-fx-border-color:" +
                                BORDER +
                                ";" +
                                "-fx-border-radius:9px;" +
                                "-fx-background-radius:9px;" +
                                "-fx-padding:0 13px;";
        }

        // =========================================================
        // COMBO BOX STYLE
        // =========================================================

        private String comboStyle() {

                return "-fx-background-color:white;" +
                                "-fx-border-color:" +
                                BORDER +
                                ";" +
                                "-fx-border-radius:9px;" +
                                "-fx-background-radius:9px;";
        }

        // =========================================================
        // PRIMARY BUTTON STYLE
        // =========================================================

        private String primaryStyle() {

                return "-fx-background-color:" +
                                GREEN +
                                ";" +
                                "-fx-text-fill:white;" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-radius:9px;" +
                                "-fx-cursor:hand;";
        }

        // =========================================================
        // PRIMARY BUTTON HOVER
        // =========================================================

        private String primaryHoverStyle() {

                return "-fx-background-color:" +
                                GREEN_DARK +
                                ";" +
                                "-fx-text-fill:white;" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-radius:9px;" +
                                "-fx-cursor:hand;";
        }

        // =========================================================
        // OUTLINE BUTTON
        // =========================================================

        private String outlineStyle() {

                return "-fx-background-color:transparent;" +
                                "-fx-text-fill:" +
                                GREEN +
                                ";" +
                                "-fx-font-weight:bold;" +
                                "-fx-border-color:" +
                                BORDER +
                                ";" +
                                "-fx-border-radius:7px;" +
                                "-fx-background-radius:7px;" +
                                "-fx-cursor:hand;";
        }

        // =========================================================
        // OUTLINE BUTTON HOVER
        // =========================================================

        private String outlineHoverStyle() {

                return "-fx-background-color:" +
                                GREEN +
                                ";" +
                                "-fx-text-fill:white;" +
                                "-fx-font-weight:bold;" +
                                "-fx-border-color:" +
                                GREEN +
                                ";" +
                                "-fx-border-radius:7px;" +
                                "-fx-background-radius:7px;" +
                                "-fx-cursor:hand;";
        }

        // =========================================================
        // CARD STYLE
        // =========================================================

        private String cardStyle() {

                return "-fx-background-color:white;" +
                                "-fx-background-radius:14px;" +
                                "-fx-border-color:" +
                                BORDER +
                                ";" +
                                "-fx-border-radius:14px;" +
                                "-fx-effect:dropshadow(" +
                                "gaussian," +
                                "rgba(20,80,65,0.08)," +
                                "10," +
                                "0.15," +
                                "0," +
                                "3" +
                                ");";
        }

        // =========================================================
        // CARD HOVER STYLE
        // =========================================================

        private String cardHoverStyle() {

                return "-fx-background-color:white;" +
                                "-fx-background-radius:14px;" +
                                "-fx-border-color:" +
                                GREEN +
                                ";" +
                                "-fx-border-radius:14px;" +
                                "-fx-effect:dropshadow(" +
                                "gaussian," +
                                "rgba(17,120,100,0.18)," +
                                "16," +
                                "0.2," +
                                "0," +
                                "4" +
                                ");" +
                                "-fx-cursor:hand;";
        }
}