package com.mainproject.view.buyer;

import com.mainproject.controller.EquipmentController;
import com.mainproject.model.Equipment;
import com.mainproject.util.LanguageManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class SearchAndRent {

    private final BuyerDashboard mainController;

    private final EquipmentController equipmentController =
            new EquipmentController();

    private GridPane equipmentGrid;

    private TextField searchField;
    private ComboBox<String> categoryBox;
    private Slider priceSlider;
    private TextField locationField;
    private ComboBox<String> availabilityBox;

    private List<Equipment> allEquipment =
            new ArrayList<>();

    public SearchAndRent(BuyerDashboard controller) {
        this.mainController = controller;
    }

    // =====================================================
    // MAIN VIEW
    // =====================================================

    public Node getView() {

        BorderPane root = new BorderPane();

        root.setPadding(new Insets(25));

        root.setStyle(
                "-fx-background-color:#F8FAFC;"
        );

        // =====================================================
        // FILTER PANEL
        // =====================================================

        VBox filterPanel =
                createFilterPanel();

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox mainContent =
                new VBox(18);

        mainContent.setPadding(
                new Insets(0, 0, 0, 25)
        );

        Label title =
                new Label(
                        "🚜 Search & Rent Farm Equipment"
                );

        title.setStyle(
                "-fx-font-size:24px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1F2937;"
        );

        Label subtitle =
                new Label(
                        "Find tractors, harvesters and agricultural equipment near you."
                );

        subtitle.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#64748B;"
        );

        VBox header =
                new VBox(5);

        header.getChildren().addAll(
                title,
                subtitle
        );

        // =====================================================
        // SEARCH FIELD
        // =====================================================

        searchField =
                new TextField();

        searchField.setPromptText(
                "🔍 Search tractors, equipment, tools..."
        );

        searchField.setPrefHeight(45);

        searchField.setStyle(
                "-fx-font-size:14px;" +
                "-fx-background-radius:8;" +
                "-fx-border-radius:8;" +
                "-fx-border-color:#CBD5E1;" +
                "-fx-background-color:white;"
        );

        searchField.textProperty().addListener(
                (obs, oldValue, newValue) ->
                        applyFilters()
        );

        // =====================================================
        // EQUIPMENT GRID
        // =====================================================

        equipmentGrid =
                new GridPane();

        equipmentGrid.setHgap(18);
        equipmentGrid.setVgap(18);

        equipmentGrid.setPadding(
                new Insets(5)
        );

        ScrollPane scrollPane =
                new ScrollPane(equipmentGrid);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;"
        );

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        mainContent.getChildren().addAll(
                header,
                searchField,
                scrollPane
        );

        root.setLeft(filterPanel);
        root.setCenter(mainContent);

        // =====================================================
        // LOAD FIRESTORE EQUIPMENT
        // =====================================================

        loadEquipment();

        LanguageManager.apply(root);

        return root;
    }

    // =====================================================
    // LOAD EQUIPMENT
    // =====================================================

    private void loadEquipment() {

        try {

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Loading equipment from Firestore..."
            );

            allEquipment =
                    equipmentController.getAllEquipment();

            if (allEquipment == null) {

                allEquipment =
                        new ArrayList<>();
            }

            System.out.println(
                    "Equipment found: "
                            + allEquipment.size()
            );

            System.out.println(
                    "===================================="
            );

            displayEquipment(
                    allEquipment
            );

        } catch (Exception e) {

            System.out.println(
                    "Error loading equipment:"
            );

            e.printStackTrace();

            showError(
                    "Unable to load equipment from Firestore."
            );
        }
    }

    // =====================================================
    // DISPLAY EQUIPMENT
    // =====================================================

    private void displayEquipment(
            List<Equipment> equipmentList) {

        equipmentGrid.getChildren().clear();

        if (equipmentList == null ||
                equipmentList.isEmpty()) {

            Label emptyLabel =
                    new Label(
                            "🚜 No equipment available right now."
                    );

            emptyLabel.setStyle(
                    "-fx-font-size:16px;" +
                    "-fx-text-fill:#64748B;" +
                    "-fx-padding:30;"
            );

            equipmentGrid.add(
                    emptyLabel,
                    0,
                    0
            );

            return;
        }

        int column = 0;
        int row = 0;

        for (Equipment equipment :
                equipmentList) {

            if (equipment == null) {
                continue;
            }

            VBox card =
                    createEquipmentCard(
                            equipment
                    );

            equipmentGrid.add(
                    card,
                    column,
                    row
            );

            column++;

            if (column == 2) {

                column = 0;
                row++;
            }
        }
    }

    // =====================================================
    // FILTER PANEL
    // =====================================================

    private VBox createFilterPanel() {

        VBox panel =
                new VBox(18);

        panel.setPrefWidth(280);

        panel.setPadding(
                new Insets(20)
        );

        panel.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:12;" +
                "-fx-background-radius:12;"
        );

        Label filterTitle =
                new Label(
                        "🔎 Search Filters"
                );

        filterTitle.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1F2937;"
        );

        // =====================================================
        // CATEGORY
        // =====================================================

        Label categoryLabel =
                new Label(
                        "Equipment Category"
                );

        categoryBox =
                new ComboBox<>();

        categoryBox.getItems().addAll(
                "All Equipment",
                "Tractors",
                "Harvesters",
                "Power Tillers",
                "Water Pumps",
                "Seed Machines",
                "Sprayers",
                "Other Equipment"
        );

        categoryBox.setValue(
                "All Equipment"
        );

        categoryBox.setMaxWidth(
                Double.MAX_VALUE
        );

        categoryBox.setOnAction(
                e -> applyFilters()
        );

        // =====================================================
        // PRICE
        // =====================================================

        Label priceLabel =
                new Label(
                        "Maximum Rent Per Day (₹)"
                );

        priceSlider =
                new Slider(
                        0,
                        10000,
                        10000
                );

        priceSlider.setShowTickLabels(true);

        priceSlider.setShowTickMarks(true);

        Label priceValue =
                new Label(
                        "Up to ₹10000 / day"
                );

        priceSlider.valueProperty().addListener(
                (obs, oldValue, newValue) -> {

                    priceValue.setText(
                            "Up to ₹"
                                    + newValue.intValue()
                                    + " / day"
                    );

                    applyFilters();
                }
        );

        // =====================================================
        // LOCATION
        // =====================================================

        Label locationLabel =
                new Label(
                        "Location"
                );

        locationField =
                new TextField();

        locationField.setPromptText(
                "Enter city or district"
        );

        locationField.textProperty().addListener(
                (obs, oldValue, newValue) ->
                        applyFilters()
        );

        // =====================================================
        // AVAILABILITY
        // =====================================================

        Label availabilityLabel =
                new Label(
                        "Availability"
                );

        availabilityBox =
                new ComboBox<>();

        availabilityBox.getItems().addAll(
                "All",
                "Available Now"
        );

        availabilityBox.setValue(
                "Available Now"
        );

        availabilityBox.setMaxWidth(
                Double.MAX_VALUE
        );

        availabilityBox.setOnAction(
                e -> applyFilters()
        );

        // =====================================================
        // APPLY BUTTON
        // =====================================================

        Button applyButton =
                new Button(
                        "🔍 Apply Filters"
                );

        applyButton.setMaxWidth(
                Double.MAX_VALUE
        );

        applyButton.setPrefHeight(42);

        applyButton.setStyle(
                "-fx-background-color:#166534;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;"
        );

        applyButton.setOnAction(
                e -> applyFilters()
        );

        // =====================================================
        // RESET BUTTON
        // =====================================================

        Button resetButton =
                new Button(
                        "Reset Filters"
                );

        resetButton.setMaxWidth(
                Double.MAX_VALUE
        );

        resetButton.setStyle(
                "-fx-background-color:#F1F5F9;" +
                "-fx-text-fill:#475569;" +
                "-fx-background-radius:8;"
        );

        resetButton.setOnAction(e -> {

            searchField.clear();

            categoryBox.setValue(
                    "All Equipment"
            );

            priceSlider.setValue(
                    10000
            );

            locationField.clear();

            availabilityBox.setValue(
                    "Available Now"
            );

            applyFilters();
        });

        panel.getChildren().addAll(

                filterTitle,

                new Separator(),

                categoryLabel,
                categoryBox,

                priceLabel,
                priceSlider,
                priceValue,

                locationLabel,
                locationField,

                availabilityLabel,
                availabilityBox,

                applyButton,
                resetButton
        );

        return panel;
    }

    // =====================================================
    // APPLY FILTERS
    // =====================================================

    private void applyFilters() {

        if (allEquipment == null ||
                searchField == null ||
                categoryBox == null ||
                locationField == null ||
                priceSlider == null ||
                availabilityBox == null) {

            return;
        }

        String searchText =
                searchField.getText()
                        .trim()
                        .toLowerCase();

        String category =
                categoryBox.getValue();

        String location =
                locationField.getText()
                        .trim()
                        .toLowerCase();

        double maxPrice =
                priceSlider.getValue();

        String availability =
                availabilityBox.getValue();

        List<Equipment> filtered =
                new ArrayList<>();

        for (Equipment equipment :
                allEquipment) {

            if (equipment == null) {
                continue;
            }

            // =================================================
            // SEARCH
            // =================================================

            boolean matchesSearch =

                    searchText.isEmpty()

                            ||

                    contains(
                            equipment.getName(),
                            searchText
                    )

                            ||

                    contains(
                            equipment.getDescription(),
                            searchText
                    )

                            ||

                    contains(
                            equipment.getCategory(),
                            searchText
                    )

                            ||

                    contains(
                            equipment.getLocation(),
                            searchText
                    );

            if (!matchesSearch) {
                continue;
            }

            // =================================================
            // CATEGORY
            // =================================================

            boolean matchesCategory =

                    category == null

                            ||

                    category.equals(
                            "All Equipment"
                    )

                            ||

                    contains(
                            equipment.getCategory(),
                            category.toLowerCase()
                    );

            if (!matchesCategory) {
                continue;
            }

            // =================================================
            // LOCATION
            // =================================================

            boolean matchesLocation =

                    location.isEmpty()

                            ||

                    contains(
                            equipment.getLocation(),
                            location
                    );

            if (!matchesLocation) {
                continue;
            }

            // =================================================
            // PRICE
            // =================================================

            if (equipment.getPrice() > maxPrice) {
                continue;
            }

            // =================================================
            // AVAILABILITY
            // =================================================

            if ("Available Now".equals(
                    availability
            )) {

                if (!equipment.isAvailable()) {
                    continue;
                }
            }

            filtered.add(
                    equipment
            );
        }

        displayEquipment(
                filtered
        );
    }

    // =====================================================
    // CREATE EQUIPMENT CARD
    // =====================================================

    private VBox createEquipmentCard(
            Equipment equipment) {

        VBox card =
                new VBox(10);

        card.setPrefWidth(330);

        card.setPadding(
                new Insets(18)
        );

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-radius:12;" +
                "-fx-background-radius:12;"
        );

        // =====================================================
        // ICON
        // =====================================================

        Label equipmentIcon =
                new Label(
                        getEquipmentIcon(
                                equipment.getCategory()
                        )
                );

        equipmentIcon.setStyle(
                "-fx-font-size:35px;"
        );

        // =====================================================
        // NAME
        // =====================================================

        Label name =
                new Label(
                        safe(
                                equipment.getName()
                        )
                );

        name.setStyle(
                "-fx-font-size:17px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#1F2937;"
        );

        name.setWrapText(true);

        // =====================================================
        // STATUS
        // =====================================================

        Label status =
                new Label(
                        equipment.isAvailable()
                                ? "● Available"
                                : "● Not Available"
                );

        status.setStyle(

                equipment.isAvailable()

                        ?

                        "-fx-text-fill:#15803D;" +
                        "-fx-font-weight:bold;" +
                        "-fx-font-size:12px;"

                        :

                        "-fx-text-fill:#DC2626;" +
                        "-fx-font-weight:bold;" +
                        "-fx-font-size:12px;"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        HBox top =
                new HBox(
                        8,
                        equipmentIcon,
                        name,
                        spacer,
                        status
                );

        top.setAlignment(
                Pos.CENTER_LEFT
        );

        // =====================================================
        // DESCRIPTION
        // =====================================================

        Label description =
                new Label(
                        safe(
                                equipment.getDescription()
                        )
                );

        description.setWrapText(true);

        description.setStyle(
                "-fx-text-fill:#64748B;" +
                "-fx-font-size:13px;"
        );

        // =====================================================
        // OWNER
        // =====================================================

        Label owner =
                new Label(
                        "👨‍🌾 Owner: "
                                + safe(
                                equipment.getOwnerName()
                        )
                );

        owner.setStyle(
                "-fx-text-fill:#475569;"
        );

        // =====================================================
        // LOCATION
        // =====================================================

        Label location =
                new Label(
                        "📍 "
                                + safe(
                                equipment.getLocation()
                        )
                );

        location.setStyle(
                "-fx-text-fill:#64748B;"
        );

        // =====================================================
        // CATEGORY
        // =====================================================

        Label category =
                new Label(
                        "Category: "
                                + safe(
                                equipment.getCategory()
                        )
                );

        category.setStyle(
                "-fx-text-fill:#64748B;" +
                "-fx-font-size:12px;"
        );

        // =====================================================
        // PRICE
        // =====================================================

        Label price =
                new Label(
                        "₹"
                                + format(
                                equipment.getPrice()
                        )
                                + " / day"
                );

        price.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#166534;"
        );

        // =====================================================
        // VIEW DETAILS BUTTON
        // =====================================================

        Button viewButton =
                new Button(
                        "View Details"
                );

        viewButton.setPrefHeight(40);

        viewButton.setMaxWidth(
                Double.MAX_VALUE
        );

        viewButton.setStyle(
                "-fx-background-color:#F1F5F9;" +
                "-fx-text-fill:#334155;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;"
        );

        viewButton.setOnAction(e ->
                showEquipmentDetails(
                        equipment
                )
        );

        // =====================================================
        // RENT NOW BUTTON
        // =====================================================

        Button rentButton =
                new Button(
                        "🚜 Rent Now"
                );

        rentButton.setPrefHeight(42);

        rentButton.setMaxWidth(
                Double.MAX_VALUE
        );

        rentButton.setStyle(
                "-fx-background-color:#166534;" +
                "-fx-text-fill:white;" +
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-background-radius:8;"
        );

        // Disable if equipment is unavailable

        rentButton.setDisable(
                !equipment.isAvailable()
        );

        // =====================================================
        // OPEN EQUIPMENT RENTAL FORM
        // =====================================================

        rentButton.setOnAction(e -> {

            try {

                System.out.println(
                        "===================================="
                );

                System.out.println(
                        "Opening rental form..."
                );

                System.out.println(
                        "Equipment: "
                                + equipment.getName()
                );

                System.out.println(
                        "Equipment ID: "
                                + equipment.getEquipmentId()
                );

                System.out.println(
                        "Owner: "
                                + equipment.getOwnerName()
                );

                System.out.println(
                        "===================================="
                );

                mainController.setView(

                        new EquipmentRentalForm(
                                mainController,
                                equipment
                        ).getView()
                );

            } catch (Exception ex) {

                System.out.println(
                        "Error opening rental form:"
                );

                ex.printStackTrace();

                showError(
                        "Unable to open the rental form."
                );
            }
        });

        // =====================================================
        // ADD COMPONENTS
        // =====================================================

        card.getChildren().addAll(
                top,
                description,
                owner,
                location,
                category,
                new Separator(),
                price,
                viewButton,
                rentButton
        );

        return card;
    }

    // =====================================================
    // VIEW EQUIPMENT DETAILS
    // =====================================================

    private void showEquipmentDetails(
            Equipment equipment) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Equipment Details"
        );

        alert.setHeaderText(
                safe(
                        equipment.getName()
                )
        );

        alert.setContentText(

                "Description: "
                        + safe(
                        equipment.getDescription()
                )

                        +

                        "\n\nCategory: "
                        + safe(
                        equipment.getCategory()
                )

                        +

                        "\nLocation: "
                        + safe(
                        equipment.getLocation()
                )

                        +

                        "\nOwner: "
                        + safe(
                        equipment.getOwnerName()
                )

                        +

                        "\nPrice: ₹"
                        + format(
                        equipment.getPrice()
                )
                        + " / day"

                        +

                        "\nStatus: "
                        +

                        (
                                equipment.isAvailable()
                                        ? "Available"
                                        : "Not Available"
                        )
        );

        alert.showAndWait();
    }

    // =====================================================
    // EQUIPMENT ICON
    // =====================================================

    private String getEquipmentIcon(
            String category) {

        if (category == null) {
            return "🚜";
        }

        String value =
                category.toLowerCase();

        if (value.contains("tractor")) {
            return "🚜";
        }

        if (value.contains("harvester")) {
            return "🌾";
        }

        if (value.contains("pump")) {
            return "💧";
        }

        if (value.contains("seed")) {
            return "🌱";
        }

        if (value.contains("sprayer")) {
            return "💦";
        }

        if (value.contains("tiller")) {
            return "🚜";
        }

        return "🛠";
    }

    // =====================================================
    // HELPER METHODS
    // =====================================================

    private boolean contains(
            String value,
            String search) {

        if (value == null ||
                search == null) {

            return false;
        }

        return value
                .toLowerCase()
                .contains(
                        search.toLowerCase()
                );
    }

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }

    private String format(
            double value) {

        if (value == Math.rint(value)) {

            return String.valueOf(
                    (long) value
            );
        }

        return String.format(
                "%.2f",
                value
        );
    }

    // =====================================================
    // ERROR MESSAGE
    // =====================================================

    private void showError(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                "Error"
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}