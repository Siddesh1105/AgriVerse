package com.mainproject.view.farmer;

import com.mainproject.dao.EquipmentDAO;
import com.mainproject.model.Equipment;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class EquipmentRental {

    private final String farmerEmail;
    private final String farmerName;

    private final EquipmentDAO equipmentDAO = new EquipmentDAO();

    private final List<Equipment> equipmentList = new ArrayList<>();

    private VBox equipmentContainer;

    private TextField searchField;

    private ComboBox<String> categoryCombo;

    private ComboBox<String> locationCombo;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public EquipmentRental(
            String farmerEmail,
            String farmerName) {

        this.farmerEmail = farmerEmail;

        this.farmerName = farmerName;

        loadEquipment();
    }

    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root = new VBox(18);

        root.setPadding(
                new Insets(20));

        root.setStyle(
                "-fx-background-color:#F4FBF7;");

        // =================================================
        // HEADER
        // =================================================

        HBox header = new HBox(15);

        header.setAlignment(
                Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);

        Label title = new Label(
                "Equipment Rental");

        title.setStyle(
                "-fx-font-size:30px;"
                        + "-fx-font-weight:800;"
                        + "-fx-text-fill:#17202A;");

        Label subtitle = new Label(
                "Rent equipment for your farm or list your equipment.");

        subtitle.setStyle(
                "-fx-font-size:15px;"
                        + "-fx-text-fill:#566573;");

        titleBox.getChildren()
                .addAll(
                        title,
                        subtitle);

        HBox.setHgrow(
                titleBox,
                Priority.ALWAYS);

        // =================================================
        // ADD EQUIPMENT BUTTON
        // =================================================

        Button addEquipmentButton = new Button(
                "+ Add Equipment");

        addEquipmentButton.setPrefHeight(
                48);

        addEquipmentButton.setPrefWidth(
                200);

        addEquipmentButton.setStyle(
                "-fx-background-color:#117864;"
                        + "-fx-text-fill:white;"
                        + "-fx-font-size:14px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:10px;"
                        + "-fx-cursor:hand;");

        /*
         * Keep your existing Add Equipment navigation here
         * if you already have it connected.
         */

        addEquipmentButton.setOnAction(
                e -> {

                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Add Equipment",
                            "Connect this button to your Add Equipment screen.");
                });

        header.getChildren()
                .addAll(
                        titleBox,
                        addEquipmentButton);

        // =================================================
        // SEARCH
        // =================================================

        HBox searchBox = new HBox(12);

        searchBox.setAlignment(
                Pos.CENTER_LEFT);

        searchField = new TextField();

        searchField.setPromptText(
                "Search equipment...");

        searchField.setPrefHeight(
                52);

        searchField.setStyle(
                "-fx-background-color:white;"
                        + "-fx-border-color:#A2D9CE;"
                        + "-fx-border-radius:9px;"
                        + "-fx-background-radius:9px;"
                        + "-fx-padding:0 15px;"
                        + "-fx-font-size:14px;");

        HBox.setHgrow(
                searchField,
                Priority.ALWAYS);

        // =================================================
        // CATEGORY
        // =================================================

        categoryCombo = new ComboBox<>();

        categoryCombo.setPrefHeight(
                52);

        categoryCombo.setPrefWidth(
                215);

        categoryCombo
                .getItems()
                .add(
                        "All Categories");

        categoryCombo
                .setValue(
                        "All Categories");

        categoryCombo.setStyle(
                "-fx-background-color:white;"
                        + "-fx-border-color:#A2D9CE;"
                        + "-fx-border-radius:9px;"
                        + "-fx-background-radius:9px;");

        // =================================================
        // LOCATION
        // =================================================

        locationCombo = new ComboBox<>();

        locationCombo.setPrefHeight(
                52);

        locationCombo.setPrefWidth(
                215);

        locationCombo
                .getItems()
                .add(
                        "All Locations");

        locationCombo
                .setValue(
                        "All Locations");

        locationCombo.setStyle(
                "-fx-background-color:white;"
                        + "-fx-border-color:#A2D9CE;"
                        + "-fx-border-radius:9px;"
                        + "-fx-background-radius:9px;");

        // =================================================
        // SEARCH BUTTON
        // =================================================

        Button searchButton = new Button(
                "Search");

        searchButton.setPrefHeight(
                52);

        searchButton.setPrefWidth(
                120);

        searchButton.setStyle(
                "-fx-background-color:#117864;"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:9px;"
                        + "-fx-cursor:hand;");

        searchButton.setOnAction(
                e -> filterEquipment());

        searchBox.getChildren()
                .addAll(
                        searchField,
                        categoryCombo,
                        locationCombo,
                        searchButton);

        // =================================================
        // EQUIPMENT CONTAINER
        // =================================================

        equipmentContainer = new VBox(20);

        equipmentContainer.setPadding(
                new Insets(5));

        ScrollPane scrollPane = new ScrollPane(
                equipmentContainer);

        scrollPane.setFitToWidth(
                true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setStyle(
                "-fx-background-color:transparent;"
                        + "-fx-border-color:transparent;");

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS);

        // =================================================
        // ROOT
        // =================================================

        root.getChildren()
                .addAll(
                        header,
                        searchBox,
                        scrollPane);

        populateCategories();
        populateLocations();
        displayEquipment(
                equipmentList);

        return root;
    }

    // =====================================================
    // LOAD EQUIPMENT
    // =====================================================

    private void loadEquipment() {

        equipmentList.clear();

        try {

            List<Equipment> loaded = equipmentDAO.getAllEquipment();

            if (loaded != null) {

                equipmentList.addAll(
                        loaded);
            }

            System.out.println(
                    "Equipment loaded: "
                            + equipmentList.size());

        } catch (Exception e) {

            System.out.println(
                    "Error loading equipment:");

            e.printStackTrace();
        }
    }

    // =====================================================
    // POPULATE CATEGORIES
    // =====================================================

    private void populateCategories() {

        if (categoryCombo == null) {

            return;
        }

        categoryCombo
                .getItems()
                .clear();

        categoryCombo
                .getItems()
                .add(
                        "All Categories");

        for (Equipment equipment : equipmentList) {

            String category = equipment.getCategory();

            if (category != null
                    && !category.trim().isEmpty()
                    && !categoryCombo
                            .getItems()
                            .contains(category)) {

                categoryCombo
                        .getItems()
                        .add(category);
            }
        }

        categoryCombo
                .setValue(
                        "All Categories");
    }

    // =====================================================
    // POPULATE LOCATIONS
    // =====================================================

    private void populateLocations() {

        if (locationCombo == null) {

            return;
        }

        locationCombo
                .getItems()
                .clear();

        locationCombo
                .getItems()
                .add(
                        "All Locations");

        for (Equipment equipment : equipmentList) {

            String location = equipment.getLocation();

            if (location != null
                    && !location.trim().isEmpty()
                    && !locationCombo
                            .getItems()
                            .contains(location)) {

                locationCombo
                        .getItems()
                        .add(location);
            }
        }

        locationCombo
                .setValue(
                        "All Locations");
    }

    // =====================================================
    // DISPLAY EQUIPMENT
    // =====================================================

    private void displayEquipment(
            List<Equipment> list) {

        if (equipmentContainer == null) {

            return;
        }

        equipmentContainer
                .getChildren()
                .clear();

        if (list == null
                || list.isEmpty()) {

            Label empty = new Label(
                    "No equipment available.");

            empty.setStyle(
                    "-fx-font-size:18px;"
                            + "-fx-font-weight:bold;"
                            + "-fx-text-fill:#566573;");

            equipmentContainer
                    .getChildren()
                    .add(empty);

            return;
        }

        /*
         * 4 cards horizontally.
         */

        GridPane grid = new GridPane();

        grid.setHgap(16);
        grid.setVgap(16);

        grid.setPadding(
                new Insets(5));

        for (int i = 0; i < list.size(); i++) {

            Equipment equipment = list.get(i);

            VBox card = createEquipmentCard(
                    equipment);

            int column = i % 4;

            int row = i / 4;

            grid.add(
                    card,
                    column,
                    row);

            GridPane.setHgrow(
                    card,
                    Priority.ALWAYS);
        }

        equipmentContainer
                .getChildren()
                .add(grid);
    }

    // =====================================================
    // CREATE EQUIPMENT CARD
    // =====================================================

    private VBox createEquipmentCard(
            Equipment equipment) {

        VBox card = new VBox(9);

        card.setPrefWidth(245);
        card.setMinWidth(225);
        card.setMaxWidth(255);
        card.setPadding(
                new Insets(14));

        card.setStyle(
                "-fx-background-color:white;"
                        + "-fx-border-color:#A2D9CE;"
                        + "-fx-border-radius:14px;"
                        + "-fx-background-radius:14px;"
                        + "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.08),8,0,0,3);");

        // =================================================
        // IMAGE
        // =================================================

        VBox imageBox = new VBox();

        imageBox.setAlignment(
                Pos.CENTER);

        imageBox.setPrefHeight(120);

        imageBox.setStyle(
                "-fx-background-color:#F1FAF6;"
                        + "-fx-background-radius:10px;");

        String imageUrl = equipment.getImageUrl();

        if (imageUrl != null
                && !imageUrl.trim().isEmpty()) {

            try {

                Image image = new Image(
                        imageUrl,
                        205,
                        110,
                        true,
                        true,
                        true);

                ImageView imageView = new ImageView(
                        image);

                imageView.setFitWidth(205);
                imageView.setFitHeight(110);

                imageView.setPreserveRatio(
                        true);

                imageBox.getChildren()
                        .add(imageView);

            } catch (Exception e) {

                addImagePlaceholder(
                        imageBox);
            }

        } else {

            addImagePlaceholder(
                    imageBox);
        }

        // =================================================
        // NAME
        // =================================================

        Label name = new Label(
                safe(
                        equipment.getName(),
                        "Equipment"));

        name.setStyle(
                "-fx-font-size:18px;"
                        + "-fx-font-weight:800;"
                        + "-fx-text-fill:#17202A;");

        // =================================================
        // CATEGORY
        // =================================================

        Label category = new Label(
                safe(
                        equipment.getCategory(),
                        "Equipment"));

        category.setStyle(
                "-fx-font-size:14px;"
                        + "-fx-text-fill:#566573;");

        // =================================================
        // PRICE
        // =================================================

        Label price = new Label(
                "₹"
                        + formatPrice(
                                equipment.getPrice())
                        + " / day");

        price.setStyle(
                "-fx-font-size:17px;"
                        + "-fx-font-weight:bold;"
                        + "-fx-text-fill:#117864;");

        // =================================================
        // AVAILABLE
        // =================================================

        Label available = new Label();

        if (equipment.isAvailable()) {

            available.setText(
                    "● Available");

            available.setStyle(
                    "-fx-font-size:14px;"
                            + "-fx-font-weight:bold;"
                            + "-fx-text-fill:#117864;");

        } else {

            available.setText(
                    "● Not Available");

            available.setStyle(
                    "-fx-font-size:14px;"
                            + "-fx-font-weight:bold;"
                            + "-fx-text-fill:#C0392B;");
        }

        // =================================================
        // LOCATION
        // =================================================

        Label location = new Label(
                "● "
                        + safe(
                                equipment.getLocation(),
                                "-"));

        location.setStyle(
                "-fx-font-size:14px;"
                        + "-fx-text-fill:#566573;");

        // =================================================
        // BUTTONS
        // =================================================

        Button viewButton = new Button(
                "View Details");

        viewButton.setPrefHeight(
                40);

        viewButton.setStyle(
                "-fx-background-color:white;"
                        + "-fx-text-fill:#117864;"
                        + "-fx-font-weight:bold;"
                        + "-fx-border-color:#A2D9CE;"
                        + "-fx-border-radius:8px;"
                        + "-fx-background-radius:8px;"
                        + "-fx-cursor:hand;");

        viewButton.setOnAction(
                e -> showEquipmentDetails(
                        equipment));

        Button addToCartButton = new Button(
                "Add to Cart");

        addToCartButton.setPrefHeight(
                40);

        addToCartButton.setStyle(
                "-fx-background-color:#117864;"
                        + "-fx-text-fill:white;"
                        + "-fx-font-weight:bold;"
                        + "-fx-background-radius:8px;"
                        + "-fx-cursor:hand;");

        addToCartButton.setOnAction(
                e -> addEquipmentToCart(
                        equipment));

        HBox buttons = new HBox(8);

        buttons.setAlignment(
                Pos.CENTER_LEFT);

        buttons.getChildren()
                .addAll(
                        viewButton,
                        addToCartButton);

        // =================================================
        // CARD
        // =================================================

        card.getChildren()
                .addAll(
                        imageBox,
                        name,
                        category,
                        price,
                        available,
                        location,
                        buttons);

        return card;
    }

    // =====================================================
    // ADD EQUIPMENT TO CART
    // =====================================================

    private void addEquipmentToCart(
            Equipment equipment) {

        if (equipment == null) {

            return;
        }

        if (!equipment.isAvailable()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Not Available",
                    "This equipment is currently unavailable.");

            return;
        }

        if (farmerEmail == null
                || farmerEmail.trim().isEmpty()) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Login Required",
                    "Farmer email is not available.");

            return;
        }

        /*
         * This uses the CartDAO.
         *
         * Rental days automatically starts
         * at 1.
         */

        com.mainproject.dao.CartDAO cartDAO = new com.mainproject.dao.CartDAO();

        com.mainproject.model.CartItem cartItem = new com.mainproject.model.CartItem(
                farmerEmail,
                equipment);

        boolean success = cartDAO.addToCart(
                cartItem);

        if (success) {

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Added to Cart",
                    equipment.getName()
                            + " has been added to your cart.");

        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Cart Error",
                    "Unable to add equipment to cart.");
        }
    }

    // =====================================================
    // FILTER
    // =====================================================

    private void filterEquipment() {

        String search = searchField
                .getText()
                .trim()
                .toLowerCase();

        String selectedCategory = categoryCombo
                .getValue();

        String selectedLocation = locationCombo
                .getValue();

        List<Equipment> filtered = new ArrayList<>();

        for (Equipment equipment : equipmentList) {

            boolean matchesSearch = search.isEmpty()
                    || safe(
                            equipment.getName(),
                            "")
                            .toLowerCase()
                            .contains(search)
                    || safe(
                            equipment.getCategory(),
                            "")
                            .toLowerCase()
                            .contains(search);

            boolean matchesCategory = selectedCategory == null
                    || selectedCategory
                            .equals("All Categories")
                    || selectedCategory
                            .equals(
                                    equipment.getCategory());

            boolean matchesLocation = selectedLocation == null
                    || selectedLocation
                            .equals("All Locations")
                    || selectedLocation
                            .equals(
                                    equipment.getLocation());

            if (matchesSearch
                    && matchesCategory
                    && matchesLocation) {

                filtered.add(
                        equipment);
            }
        }

        displayEquipment(
                filtered);
    }

    // =====================================================
    // VIEW DETAILS
    // =====================================================

    private void showEquipmentDetails(
            Equipment equipment) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);

        alert.setTitle(
                "Equipment Details");

        alert.setHeaderText(
                equipment.getName());

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
                        + "\n\nDescription: "
                        + safe(
                                equipment.getDescription(),
                                "No description available.")
                        + "\n\nOwner: "
                        + safe(
                                equipment.getOwnerName(),
                                "-"));

        alert.showAndWait();
    }

    // =====================================================
    // IMAGE PLACEHOLDER
    // =====================================================

    private void addImagePlaceholder(
            VBox imageBox) {

        Label label = new Label("🚜");

        label.setStyle(
                "-fx-font-size:45px;");

        imageBox.getChildren()
                .add(label);
    }

    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safe(
            String value,
            String fallback) {

        return value == null
                || value.trim().isEmpty()
                        ? fallback
                        : value;
    }

    // =====================================================
    // FORMAT PRICE
    // =====================================================

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

    // =====================================================
    // ALERT
    // =====================================================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);

        alert.setTitle(
                title);

        alert.setHeaderText(
                null);

        alert.setContentText(
                message);

        alert.showAndWait();
    }
}