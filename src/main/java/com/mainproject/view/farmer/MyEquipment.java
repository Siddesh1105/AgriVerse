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
import javafx.scene.control.TextArea;
import javafx.scene.control.CheckBox;
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

public class MyEquipment {

    private static final String GREEN = "#117864";
    private static final String GREEN_DARK = "#0E6655";
    private static final String BORDER = "#A2D9CE";
    private static final String TEXT = "#1B2631";
    private static final String SECONDARY = "#566573";
    private static final String BG = "#F4FBF7";

    private final EquipmentDAO equipmentDAO = new EquipmentDAO();

    private final String farmerEmail;
    private final String farmerName;

    // Runnable navigation
    private final Runnable openAddEquipment;
    private final Runnable backToEquipmentRental;

    private GridPane equipmentGrid;

    private TextField searchField;

    private ComboBox<String> categoryCombo;

    private ComboBox<String> locationCombo;

    private final List<Equipment> equipmentList = new ArrayList<>();

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public MyEquipment(
            String farmerEmail,
            String farmerName,
            Runnable openAddEquipment,
            Runnable backToEquipmentRental) {

        this.farmerEmail = farmerEmail;
        this.farmerName = farmerName;

        this.openAddEquipment = openAddEquipment;
        this.backToEquipmentRental = backToEquipmentRental;

        loadMyEquipment();
    }

    // =========================================================
    // GET VIEW
    // =========================================================

    public Node getView() {

        VBox root = new VBox(18);

        root.setPadding(
                new Insets(20));

        root.setStyle(
                "-fx-background-color:" + BG + ";");

        // =====================================================
        // HEADER
        // =====================================================

        Label title = new Label("My Equipment");

        title.setStyle(
                "-fx-font-size:28px;" +
                        "-fx-font-weight:800;" +
                        "-fx-text-fill:" + TEXT + ";");

        Label subtitle = new Label(
                "Manage the equipment you have listed for rental.");

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
        // ADD EQUIPMENT
        // =====================================================

        Button addButton = new Button("+ Add Equipment");

        addButton.setPrefHeight(42);

        addButton.setPrefWidth(160);

        addButton.setStyle(
                primaryStyle());

        addButton.setOnMouseEntered(
                e -> addButton.setStyle(
                        primaryHoverStyle()));

        addButton.setOnMouseExited(
                e -> addButton.setStyle(
                        primaryStyle()));

        // Runnable navigation
        addButton.setOnAction(
                e -> {

                    if (openAddEquipment != null) {

                        openAddEquipment.run();
                    }
                });

        // =====================================================
        // BACK BUTTON
        // =====================================================

        Button rentalButton = new Button("Equipment Rental");

        rentalButton.setPrefHeight(42);

        rentalButton.setStyle(
                outlineStyle());

        rentalButton.setOnMouseEntered(
                e -> rentalButton.setStyle(
                        outlineHoverStyle()));

        rentalButton.setOnMouseExited(
                e -> rentalButton.setStyle(
                        outlineStyle()));

        // Runnable navigation
        rentalButton.setOnAction(
                e -> {

                    if (backToEquipmentRental != null) {

                        backToEquipmentRental.run();
                    }
                });

        HBox headerButtons = new HBox(
                10,
                rentalButton,
                addButton);

        headerButtons.setAlignment(
                Pos.CENTER_RIGHT);

        HBox header = new HBox(
                15,
                titleBox,
                headerButtons);

        header.setAlignment(
                Pos.CENTER_LEFT);

        // =====================================================
        // SEARCH
        // =====================================================

        searchField = new TextField();

        searchField.setPromptText(
                "Search my equipment...");

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
        // CATEGORY
        // =====================================================

        categoryCombo = new ComboBox<>();

        categoryCombo
                .getItems()
                .add("All Categories");

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
        // LOCATION
        // =====================================================

        locationCombo = new ComboBox<>();

        locationCombo
                .getItems()
                .add("All Locations");

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

        HBox filters = new HBox(
                10,
                searchField,
                categoryCombo,
                locationCombo,
                searchButton);

        filters.setAlignment(
                Pos.CENTER_LEFT);

        // =====================================================
        // GRID
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

        // Four columns
        for (int i = 0; i < 4; i++) {

            ColumnConstraints column = new ColumnConstraints();

            column.setPercentWidth(25);

            column.setHgrow(
                    Priority.ALWAYS);

            equipmentGrid
                    .getColumnConstraints()
                    .add(column);
        }

        buildCards(
                equipmentList);

        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane scroll = new ScrollPane(
                equipmentGrid);

        scroll.setFitToWidth(
                true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setStyle(
                "-fx-background-color:transparent;" +
                        "-fx-background:transparent;" +
                        "-fx-border-color:transparent;");

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        root.getChildren().addAll(
                header,
                filters,
                scroll);

        return root;
    }

    // =========================================================
    // LOAD MY EQUIPMENT
    // =========================================================

    private void loadMyEquipment() {

        try {

            equipmentList.clear();

            if (farmerEmail == null ||
                    farmerEmail.trim().isEmpty()) {

                System.out.println(
                        "Farmer email is empty.");

                return;
            }

            List<Equipment> data = equipmentDAO.getEquipmentByOwner(
                    farmerEmail);

            if (data != null) {

                equipmentList.addAll(
                        data);
            }

            System.out.println(
                    "My Equipment loaded: "
                            + equipmentList.size());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================================================
    // CATEGORIES
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
    // LOCATIONS
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
    // BUILD CARDS
    // =========================================================

    private void buildCards(
            List<Equipment> list) {

        equipmentGrid
                .getChildren()
                .clear();

        if (list == null ||
                list.isEmpty()) {

            VBox empty = new VBox(10);

            empty.setAlignment(
                    Pos.CENTER);

            empty.setPadding(
                    new Insets(60));

            Label icon = new Label("🚜");

            icon.setStyle(
                    "-fx-font-size:45px;");

            Label message = new Label(
                    "You have not added any equipment yet.");

            message.setStyle(
                    "-fx-font-size:16px;" +
                            "-fx-font-weight:bold;" +
                            "-fx-text-fill:" +
                            SECONDARY + ";");

            Button add = new Button(
                    "+ Add Equipment");

            add.setStyle(
                    primaryStyle());

            add.setOnAction(
                    e -> {

                        if (openAddEquipment != null) {

                            openAddEquipment.run();
                        }
                    });

            empty.getChildren().addAll(
                    icon,
                    message,
                    add);

            equipmentGrid.add(
                    empty,
                    0,
                    0,
                    4,
                    1);

            return;
        }

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
    // CARD
    // =========================================================

    private VBox createCard(
            Equipment equipment) {

        VBox card = new VBox(8);

        card.setPrefWidth(260);

        card.setMinWidth(0);

        card.setMaxWidth(
                Double.MAX_VALUE);

        card.setPrefHeight(350);

        card.setPadding(
                new Insets(12));

        card.setStyle(
                cardStyle());

        // Image
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

                imageView.setPreserveRatio(true);

                imageBox
                        .getChildren()
                        .add(imageView);

            } catch (Exception ex) {

                addPlaceholder(
                        imageBox);
            }

        } else {

            addPlaceholder(
                    imageBox);
        }

        // Name
        Label name = new Label(
                safe(
                        equipment.getName(),
                        "Equipment"));

        name.setStyle(
                "-fx-font-size:17px;" +
                        "-fx-font-weight:800;" +
                        "-fx-text-fill:" +
                        TEXT + ";");

        // Category
        Label category = new Label(
                safe(
                        equipment.getCategory(),
                        "Agricultural Equipment"));

        category.setStyle(
                "-fx-font-size:12px;" +
                        "-fx-text-fill:" +
                        SECONDARY + ";");

        // Price
        Label price = new Label(
                "₹"
                        + formatPrice(
                                equipment.getPrice())
                        + " / day");

        price.setStyle(
                "-fx-font-size:16px;" +
                        "-fx-font-weight:800;" +
                        "-fx-text-fill:" +
                        GREEN + ";");

        // Availability
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

        // Location
        Label location = new Label(
                "● "
                        + safe(
                                equipment.getLocation(),
                                "Unknown"));

        location.setStyle(
                "-fx-font-size:12px;" +
                        "-fx-text-fill:" +
                        SECONDARY + ";");

        // Edit
        Button edit = new Button("Edit");

        edit.setPrefHeight(30);

        edit.setStyle(
                outlineStyle());

        edit.setOnAction(
                e -> showEditDialog(
                        equipment));

        // Delete
        Button delete = new Button("Delete");

        delete.setPrefHeight(30);

        delete.setStyle(
                deleteStyle());

        delete.setOnAction(
                e -> deleteEquipment(
                        equipment));

        HBox buttons = new HBox(
                7,
                edit,
                delete);

        buttons.setAlignment(
                Pos.CENTER_LEFT);

        card.getChildren().addAll(
                imageBox,
                name,
                category,
                price,
                availability,
                location,
                buttons);

        card.setOnMouseEntered(
                e -> card.setStyle(
                        cardHoverStyle()));

        card.setOnMouseExited(
                e -> card.setStyle(
                        cardStyle()));

        return card;
    }

    // =========================================================
    // EDIT
    // =========================================================

    private void showEditDialog(
            Equipment equipment) {

        VBox form = new VBox(10);

        form.setPadding(
                new Insets(10));

        TextField nameField = new TextField(
                safe(
                        equipment.getName(),
                        ""));

        TextField priceField = new TextField(
                String.valueOf(
                        equipment.getPrice()));

        TextField locationField = new TextField(
                safe(
                        equipment.getLocation(),
                        ""));

        ComboBox<String> categoryBox = new ComboBox<>();

        categoryBox.getItems().addAll(
                "Tractor",
                "Tillage",
                "Harvesting",
                "Sprayer",
                "Seeding",
                "Cultivator",
                "Other");

        categoryBox.setValue(
                safe(
                        equipment.getCategory(),
                        "Other"));

        TextArea descriptionField = new TextArea(
                safe(
                        equipment.getDescription(),
                        ""));

        descriptionField.setPrefRowCount(4);

        descriptionField.setWrapText(true);

        CheckBox availableBox = new CheckBox(
                "Available for rental");

        availableBox.setSelected(
                equipment.isAvailable());

        form.getChildren().addAll(
                new Label("Equipment Name"),
                nameField,
                new Label("Category"),
                categoryBox,
                new Label("Rental Price / Day"),
                priceField,
                new Label("Location"),
                locationField,
                new Label("Description"),
                descriptionField,
                availableBox);

        Alert dialog = new Alert(
                Alert.AlertType.CONFIRMATION);

        dialog.setTitle(
                "Edit Equipment");

        dialog.setHeaderText(
                "Update Equipment Details");

        dialog.getDialogPane()
                .setContent(form);

        dialog.showAndWait()
                .ifPresent(
                        result -> {

                            if (result == javafx.scene.control.ButtonType.OK) {

                                try {

                                    equipment.setName(
                                            nameField
                                                    .getText()
                                                    .trim());

                                    equipment.setCategory(
                                            categoryBox
                                                    .getValue());

                                    equipment.setPrice(
                                            Double.parseDouble(
                                                    priceField
                                                            .getText()
                                                            .trim()));

                                    equipment.setLocation(
                                            locationField
                                                    .getText()
                                                    .trim());

                                    equipment.setDescription(
                                            descriptionField
                                                    .getText()
                                                    .trim());

                                    equipment.setAvailable(
                                            availableBox
                                                    .isSelected());

                                    boolean updated = equipmentDAO
                                            .updateEquipment(
                                                    equipment);

                                    if (updated) {

                                        showSuccess(
                                                "Equipment updated successfully.");

                                        refresh();

                                    } else {

                                        showError(
                                                "Could not update equipment.");
                                    }

                                } catch (Exception ex) {

                                    showError(
                                            "Invalid equipment details.");
                                }
                            }
                        });
    }

    // =========================================================
    // DELETE
    // =========================================================

    private void deleteEquipment(
            Equipment equipment) {

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION);

        confirm.setTitle(
                "Delete Equipment");

        confirm.setHeaderText(
                "Delete "
                        + safe(
                                equipment.getName(),
                                "Equipment")
                        + "?");

        confirm.setContentText(
                "This equipment will be removed from Firestore.");

        confirm.showAndWait()
                .ifPresent(
                        result -> {

                            if (result == javafx.scene.control.ButtonType.OK) {

                                boolean deleted = equipmentDAO
                                        .deleteEquipment(
                                                equipment
                                                        .getEquipmentId());

                                if (deleted) {

                                    showSuccess(
                                            "Equipment deleted successfully.");

                                    refresh();

                                } else {

                                    showError(
                                            "Could not delete equipment.");
                                }
                            }
                        });
    }

    // =========================================================
    // REFRESH
    // =========================================================

    public void refresh() {

        loadMyEquipment();

        if (equipmentGrid != null) {

            buildCards(
                    equipmentList);
        }
    }

    // =========================================================
    // FILTER
    // =========================================================

    private void filterEquipment() {

        if (searchField == null) {
            return;
        }

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

            boolean searchMatch = search.isEmpty()
                    || name.contains(search)
                    || cat.contains(search)
                    || loc.contains(search);

            boolean categoryMatch = category == null
                    || category.equals(
                            "All Categories")
                    || cat.equals(
                            category.toLowerCase());

            boolean locationMatch = location == null
                    || location.equals(
                            "All Locations")
                    || loc.equals(
                            location.toLowerCase());

            if (searchMatch &&
                    categoryMatch &&
                    locationMatch) {

                filtered.add(e);
            }
        }

        buildCards(
                filtered);
    }

    // =========================================================
    // PLACEHOLDER
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
    // SUCCESS
    // =========================================================

    private void showSuccess(
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);

        alert.setTitle(
                "My Equipment");

        alert.setHeaderText(
                "Success");

        alert.setContentText(
                message);

        alert.showAndWait();
    }

    // =========================================================
    // ERROR
    // =========================================================

    private void showError(
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.ERROR);

        alert.setTitle(
                "My Equipment");

        alert.setHeaderText(
                "Something went wrong");

        alert.setContentText(
                message);

        alert.showAndWait();
    }

    // =========================================================
    // SAFE
    // =========================================================

    private String safe(
            String value,
            String fallback) {

        return value == null ||
                value.trim().isEmpty()
                        ? fallback
                        : value;
    }

    // =========================================================
    // PRICE
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
    // STYLES
    // =========================================================

    private String inputStyle() {

        return "-fx-background-color:white;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius:9px;" +
                "-fx-background-radius:9px;" +
                "-fx-padding:0 13px;";
    }

    private String comboStyle() {

        return "-fx-background-color:white;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius:9px;" +
                "-fx-background-radius:9px;";
    }

    private String primaryStyle() {

        return "-fx-background-color:" + GREEN + ";" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:9px;" +
                "-fx-cursor:hand;";
    }

    private String primaryHoverStyle() {

        return "-fx-background-color:" + GREEN_DARK + ";" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:9px;" +
                "-fx-cursor:hand;";
    }

    private String outlineStyle() {

        return "-fx-background-color:transparent;" +
                "-fx-text-fill:" + GREEN + ";" +
                "-fx-font-weight:bold;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius:7px;" +
                "-fx-background-radius:7px;" +
                "-fx-cursor:hand;";
    }

    private String outlineHoverStyle() {

        return "-fx-background-color:" + GREEN + ";" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-border-color:" + GREEN + ";" +
                "-fx-border-radius:7px;" +
                "-fx-background-radius:7px;" +
                "-fx-cursor:hand;";
    }

    private String deleteStyle() {

        return "-fx-background-color:#FDEDEC;" +
                "-fx-text-fill:#C0392B;" +
                "-fx-font-weight:bold;" +
                "-fx-border-color:#E6B0AA;" +
                "-fx-border-radius:7px;" +
                "-fx-background-radius:7px;" +
                "-fx-cursor:hand;";
    }

    private String cardStyle() {

        return "-fx-background-color:white;" +
                "-fx-background-radius:14px;" +
                "-fx-border-color:" + BORDER + ";" +
                "-fx-border-radius:14px;" +
                "-fx-effect:dropshadow(gaussian," +
                "rgba(20,80,65,0.08),10,0.15,0,3);";
    }

    private String cardHoverStyle() {

        return "-fx-background-color:white;" +
                "-fx-background-radius:14px;" +
                "-fx-border-color:" + GREEN + ";" +
                "-fx-border-radius:14px;" +
                "-fx-effect:dropshadow(gaussian," +
                "rgba(17,120,100,0.18),16,0.2,0,4);" +
                "-fx-cursor:hand;";
    }
}