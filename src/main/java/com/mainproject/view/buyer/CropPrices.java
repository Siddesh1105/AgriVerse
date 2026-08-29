package com.mainproject.view.buyer;

import com.mainproject.controller.CropPriceController;
import com.mainproject.model.CropPrice;
import com.mainproject.util.LanguageManager;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CropPrices {

    private final BuyerDashboard mainController;

    private final CropPriceController cropPriceController =
            new CropPriceController();

    private final ObservableList<CropPrice> tableData =
            FXCollections.observableArrayList();

    private TableView<CropPrice> table;

    private ComboBox<String> stateCombo;
    private ComboBox<String> districtCombo;
    private ComboBox<String> marketCombo;

    private TextField searchField;

    private Button refreshButton;
    private Button clearButton;

    private ProgressIndicator loadingIndicator;
    private Label statusLabel;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public CropPrices(BuyerDashboard controller) {
        this.mainController = controller;
    }

    // =====================================================
    // MAIN VIEW
    // =====================================================

    public Node getView() {

        BorderPane root = new BorderPane();

        root.setPadding(new Insets(25, 30, 25, 30));

        root.setStyle(
                "-fx-background-color: #F8FAFC;"
        );

        VBox content = new VBox(18);

        // =================================================
        // HEADER
        // =================================================

        VBox header = createHeader();

        // =================================================
        // FILTER CARD
        // =================================================

        VBox filterCard = createFilterCard();

        // =================================================
        // TABLE CARD
        // =================================================

        VBox tableCard = createTableCard();

        VBox.setVgrow(tableCard, Priority.ALWAYS);

        content.getChildren().addAll(
                header,
                filterCard,
                tableCard
        );

        root.setCenter(content);

        // Load states automatically
        loadStates();

        LanguageManager.apply(root);

        return root;
    }

    // =====================================================
    // HEADER
    // =====================================================

    private VBox createHeader() {

        VBox header = new VBox(5);

        HBox titleRow = new HBox(12);

        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label("📈");

        icon.setStyle(
                "-fx-font-size: 26px;"
        );

        Label title =
                new Label("Live Mandi / APMC Crop Prices");

        title.setStyle(
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1E293B;"
        );

        titleRow.getChildren().addAll(
                icon,
                title
        );

        Label subtitle = new Label(
                "Check the latest crop prices from mandi markets across India"
        );

        subtitle.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #64748B;"
        );

        header.getChildren().addAll(
                titleRow,
                subtitle
        );

        return header;
    }

    // =====================================================
    // FILTER CARD
    // =====================================================

    private VBox createFilterCard() {

        VBox card = new VBox(14);

        card.setPadding(new Insets(18));

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E2E8F0;" +
                "-fx-border-radius: 12;"
        );

        Label filterTitle = new Label("🔎 Search & Filter");

        filterTitle.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #334155;"
        );

        // =============================================
        // COMBO BOXES
        // =============================================

        stateCombo = new ComboBox<>();

        stateCombo.setPromptText("Select State");

        stateCombo.setPrefWidth(220);

        districtCombo = new ComboBox<>();

        districtCombo.setPromptText("Select District");

        districtCombo.setPrefWidth(220);

        districtCombo.setDisable(true);

        marketCombo = new ComboBox<>();

        marketCombo.setPromptText("Select Market");

        marketCombo.setPrefWidth(220);

        marketCombo.setDisable(true);

        searchField = new TextField();

        searchField.setPromptText("Search crop (Tomato, Onion...)");

        searchField.setPrefWidth(230);

        // =============================================
        // BUTTONS
        // =============================================

        refreshButton = new Button("🔄 Refresh Prices");

        refreshButton.setStyle(
                "-fx-background-color: #166534;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 16;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        clearButton = new Button("Clear");

        clearButton.setStyle(
                "-fx-background-color: #F1F5F9;" +
                "-fx-text-fill: #334155;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 16;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        // =============================================
        // LOADING
        // =============================================

        loadingIndicator = new ProgressIndicator();

        loadingIndicator.setPrefSize(22, 22);

        loadingIndicator.setVisible(false);

        loadingIndicator.setManaged(false);

        statusLabel = new Label("Select a state to view crop prices");

        statusLabel.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #64748B;"
        );

        // =============================================
        // FILTER ROW
        // =============================================

        HBox filters = new HBox(12);

        filters.setAlignment(Pos.CENTER_LEFT);

        filters.getChildren().addAll(
                stateCombo,
                districtCombo,
                marketCombo,
                searchField,
                refreshButton,
                clearButton,
                loadingIndicator
        );

        HBox.setHgrow(searchField, Priority.ALWAYS);

        // =============================================
        // EVENTS
        // =============================================

        stateCombo.setOnAction(e -> {

            String state = stateCombo.getValue();

            districtCombo.getItems().clear();
            marketCombo.getItems().clear();

            districtCombo.setValue(null);
            marketCombo.setValue(null);

            marketCombo.setDisable(true);

            tableData.clear();

            if (state != null && !state.trim().isEmpty()) {

                districtCombo.setDisable(false);

                loadDistricts(state);

            } else {

                districtCombo.setDisable(true);

                statusLabel.setText(
                        "Select a state to view crop prices"
                );
            }
        });

        districtCombo.setOnAction(e -> {

            String state = stateCombo.getValue();
            String district = districtCombo.getValue();

            marketCombo.getItems().clear();

            marketCombo.setValue(null);

            tableData.clear();

            if (state != null &&
                    district != null &&
                    !district.trim().isEmpty()) {

                marketCombo.setDisable(false);

                loadMarkets(state, district);

            } else {

                marketCombo.setDisable(true);
            }
        });

        marketCombo.setOnAction(e -> {

            loadCropPrices();
        });

        refreshButton.setOnAction(e -> {

            loadCropPrices();
        });

        clearButton.setOnAction(e -> {

            clearFilters();
        });

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> filterTable()
        );

        card.getChildren().addAll(
                filterTitle,
                filters,
                statusLabel
        );

        return card;
    }

    // =====================================================
    // TABLE CARD
    // =====================================================

    private VBox createTableCard() {

        VBox card = new VBox(12);

        card.setPadding(new Insets(18));

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E2E8F0;" +
                "-fx-border-radius: 12;"
        );

        HBox tableHeader = new HBox();

        tableHeader.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("🌾 Available Crop Prices");

        title.setStyle(
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #334155;"
        );

        Label priceInfo = new Label(
                "Prices are provided by the official mandi data source"
        );

        priceInfo.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #94A3B8;"
        );

        Region spacer = new Region();

        HBox.setHgrow(spacer, Priority.ALWAYS);

        tableHeader.getChildren().addAll(
                title,
                spacer,
                priceInfo
        );

        // =============================================
        // TABLE
        // =============================================

        table = new TableView<>();

        table.setItems(tableData);

        table.setPlaceholder(
                new Label("No crop price data available")
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setStyle(
                "-fx-background-color: transparent;"
        );

        // =============================================
        // COLUMNS
        // =============================================

        TableColumn<CropPrice, String> commodityColumn =
                new TableColumn<>("Crop");

        commodityColumn.setCellValueFactory(
                new PropertyValueFactory<>("commodity")
        );

        commodityColumn.setPrefWidth(160);

        TableColumn<CropPrice, String> varietyColumn =
                new TableColumn<>("Variety");

        varietyColumn.setCellValueFactory(
                new PropertyValueFactory<>("variety")
        );

        varietyColumn.setPrefWidth(140);

        TableColumn<CropPrice, String> marketColumn =
                new TableColumn<>("Market");

        marketColumn.setCellValueFactory(
                new PropertyValueFactory<>("market")
        );

        marketColumn.setPrefWidth(170);

        TableColumn<CropPrice, String> minColumn =
                new TableColumn<>("Min Price");

        minColumn.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        formatPrice(
                                cell.getValue().getMinPrice()
                        )
                )
        );

        minColumn.setPrefWidth(120);

        TableColumn<CropPrice, String> maxColumn =
                new TableColumn<>("Max Price");

        maxColumn.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        formatPrice(
                                cell.getValue().getMaxPrice()
                        )
                )
        );

        maxColumn.setPrefWidth(120);

        TableColumn<CropPrice, String> modalColumn =
                new TableColumn<>("Modal Price");

        modalColumn.setCellValueFactory(
                cell -> new SimpleStringProperty(
                        formatPrice(
                                cell.getValue().getModalPrice()
                        )
                )
        );

        modalColumn.setPrefWidth(130);

        TableColumn<CropPrice, String> dateColumn =
                new TableColumn<>("Arrival Date");

        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("arrivalDate")
        );

        dateColumn.setPrefWidth(130);

        table.getColumns().addAll(
                commodityColumn,
                varietyColumn,
                marketColumn,
                minColumn,
                maxColumn,
                modalColumn,
                dateColumn
        );

        // =============================================
        // ROW STYLE
        // =============================================

        table.setRowFactory(tv -> {

            TableRow<CropPrice> row =
                    new TableRow<>();

            row.setStyle(
                    "-fx-font-size: 13px;"
            );

            return row;
        });

        VBox.setVgrow(table, Priority.ALWAYS);

        card.getChildren().addAll(
                tableHeader,
                table
        );

        return card;
    }

    // =====================================================
    // LOAD STATES
    // =====================================================

    private void loadStates() {

        setLoading(true);

        statusLabel.setText(
                "Loading available states..."
        );

        Task<List<String>> task = new Task<>() {

            @Override
            protected List<String> call() {

                return cropPriceController.getStates();
            }
        };

        task.setOnSucceeded(e -> {

            List<String> states = task.getValue();

            if (states != null) {

                states.sort(
                        String.CASE_INSENSITIVE_ORDER
                );

                stateCombo.getItems().setAll(states);
            }

            setLoading(false);

            statusLabel.setText(
                    "Select State → District → Market"
            );
        });

        task.setOnFailed(e -> {

            setLoading(false);

            statusLabel.setText(
                    "Unable to load states. Check your internet connection."
            );

            showError(
                    "Unable to load states",
                    task.getException()
            );
        });

        startTask(task);
    }

    // =====================================================
    // LOAD DISTRICTS
    // =====================================================

    private void loadDistricts(String state) {

        setLoading(true);

        statusLabel.setText(
                "Loading districts..."
        );

        Task<List<String>> task = new Task<>() {

            @Override
            protected List<String> call() {

                return cropPriceController.getDistricts(state);
            }
        };

        task.setOnSucceeded(e -> {

            List<String> districts = task.getValue();

            if (districts != null) {

                districts.sort(
                        String.CASE_INSENSITIVE_ORDER
                );

                districtCombo.getItems().setAll(districts);
            }

            setLoading(false);

            statusLabel.setText(
                    "Select a district"
            );
        });

        task.setOnFailed(e -> {

            setLoading(false);

            statusLabel.setText(
                    "Unable to load districts"
            );

            showError(
                    "Unable to load districts",
                    task.getException()
            );
        });

        startTask(task);
    }

    // =====================================================
    // LOAD MARKETS
    // =====================================================

    private void loadMarkets(
            String state,
            String district) {

        setLoading(true);

        statusLabel.setText(
                "Loading markets..."
        );

        Task<List<String>> task = new Task<>() {

            @Override
            protected List<String> call() {

                return cropPriceController.getMarkets(
                        state,
                        district
                );
            }
        };

        task.setOnSucceeded(e -> {

            List<String> markets = task.getValue();

            if (markets != null) {

                markets.sort(
                        String.CASE_INSENSITIVE_ORDER
                );

                marketCombo.getItems().setAll(markets);
            }

            setLoading(false);

            statusLabel.setText(
                    "Select a market to view prices"
            );
        });

        task.setOnFailed(e -> {

            setLoading(false);

            statusLabel.setText(
                    "Unable to load markets"
            );

            showError(
                    "Unable to load markets",
                    task.getException()
            );
        });

        startTask(task);
    }

    // =====================================================
    // LOAD CROP PRICES
    // =====================================================

    private void loadCropPrices() {

        String state = stateCombo.getValue();
        String district = districtCombo.getValue();
        String market = marketCombo.getValue();

        if (state == null || state.trim().isEmpty()) {

            statusLabel.setText(
                    "Please select a state first"
            );

            return;
        }

        setLoading(true);

        statusLabel.setText(
                "Loading latest crop prices..."
        );

        Task<List<CropPrice>> task = new Task<>() {

            @Override
            protected List<CropPrice> call() {

                return cropPriceController.getCropPrices(
                        state,
                        district,
                        market
                );
            }
        };

        task.setOnSucceeded(e -> {

            List<CropPrice> prices = task.getValue();

            tableData.clear();

            if (prices != null) {

                prices.sort(
                        Comparator.comparing(
                                CropPrice::getCommodity,
                                Comparator.nullsLast(
                                        String.CASE_INSENSITIVE_ORDER
                                )
                        )
                );

                tableData.addAll(prices);
            }

            setLoading(false);

            filterTable();

            if (tableData.isEmpty()) {

                statusLabel.setText(
                        "No crop prices found for the selected filters"
                );

            } else {

                statusLabel.setText(
                        tableData.size()
                                + " crop price records loaded successfully"
                );
            }
        });

        task.setOnFailed(e -> {

            setLoading(false);

            tableData.clear();

            statusLabel.setText(
                    "Unable to load crop prices"
            );

            showError(
                    "Unable to load crop prices",
                    task.getException()
            );
        });

        startTask(task);
    }

    // =====================================================
    // SEARCH FILTER
    // =====================================================

    private void filterTable() {

        if (table == null) {
            return;
        }

        String search = searchField.getText();

        if (search == null ||
                search.trim().isEmpty()) {

            table.setItems(tableData);

            return;
        }

        String keyword =
                search.trim().toLowerCase();

        ObservableList<CropPrice> filtered =
                FXCollections.observableArrayList();

        for (CropPrice price : tableData) {

            String commodity =
                    safeLower(
                            price.getCommodity()
                    );

            String variety =
                    safeLower(
                            price.getVariety()
                    );

            String market =
                    safeLower(
                            price.getMarket()
                    );

            if (commodity.contains(keyword)
                    || variety.contains(keyword)
                    || market.contains(keyword)) {

                filtered.add(price);
            }
        }

        table.setItems(filtered);
    }

    // =====================================================
    // CLEAR FILTERS
    // =====================================================

    private void clearFilters() {

        stateCombo.setValue(null);

        districtCombo.getItems().clear();
        districtCombo.setValue(null);
        districtCombo.setDisable(true);

        marketCombo.getItems().clear();
        marketCombo.setValue(null);
        marketCombo.setDisable(true);

        searchField.clear();

        tableData.clear();

        table.setItems(tableData);

        statusLabel.setText(
                "Filters cleared. Select a state to continue."
        );
    }

    // =====================================================
    // FORMAT PRICE
    // =====================================================

    private String formatPrice(String price) {

        if (price == null ||
                price.trim().isEmpty() ||
                price.equals("0")) {

            return "N/A";
        }

        // Government mandi data is generally reported per quintal
        return "₹" + price + " / quintal";
    }

    // =====================================================
    // SAFE LOWERCASE
    // =====================================================

    private String safeLower(String value) {

        if (value == null) {
            return "";
        }

        return value.toLowerCase();
    }

    // =====================================================
    // LOADING STATE
    // =====================================================

    private void setLoading(boolean loading) {

        if (loadingIndicator != null) {

            loadingIndicator.setVisible(loading);

            loadingIndicator.setManaged(loading);
        }

        if (refreshButton != null) {

            refreshButton.setDisable(loading);
        }
    }

    // =====================================================
    // START BACKGROUND TASK
    // =====================================================

    private void startTask(Task<?> task) {

        Thread thread = new Thread(task);

        thread.setDaemon(true);

        thread.start();
    }

    // =====================================================
    // ERROR HANDLER
    // =====================================================

    private void showError(
            String message,
            Throwable exception) {

        System.out.println(
                "================================="
        );

        System.out.println(message);

        if (exception != null) {

            exception.printStackTrace();
        }

        System.out.println(
                "================================="
        );
    }
}