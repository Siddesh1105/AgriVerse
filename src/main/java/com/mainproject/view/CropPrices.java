package com.mainproject.view;

import com.mainproject.controller.CropPriceController;
import com.mainproject.model.CropPrice;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CropPrices {

    private final FarmerDashboard navigator;

    private final CropPriceController controller =
            new CropPriceController();

    private final ComboBox<String> stateBox =
            new ComboBox<>();

    private final ComboBox<String> districtBox =
            new ComboBox<>();

    private final ComboBox<String> marketBox =
            new ComboBox<>();

    private final TableView<CropPrice> table =
            new TableView<>();

    private final Label messageLabel =
            new Label();

    public CropPrices(FarmerDashboard navigator) {
        this.navigator = navigator;
    }

    public Node getView() {

        VBox root = new VBox(16);
        root.setPadding(new Insets(10));

        // =====================================================
        // HEADER
        // =====================================================

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titles = new VBox(3);

        Label title = new Label("Crop Prices");

        title.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-font-weight: 800;" +
                "-fx-text-fill: #1B2631;"
        );

        Label sub = new Label(
                "Check latest mandi prices for different crops."
        );

        sub.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #566573;"
        );

        titles.getChildren().addAll(
                title,
                sub
        );

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        // =====================================================
        // STATE COMBOBOX
        // =====================================================

        stateBox.setPromptText("Select State");
        stateBox.setPrefWidth(160);

        stateBox.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8px;" +
                "-fx-border-color: #A2D9CE;" +
                "-fx-border-radius: 8px;"
        );

        // =====================================================
        // DISTRICT COMBOBOX
        // =====================================================

        districtBox.setPromptText("Select District");
        districtBox.setPrefWidth(160);
        districtBox.setDisable(true);

        districtBox.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8px;" +
                "-fx-border-color: #A2D9CE;" +
                "-fx-border-radius: 8px;"
        );

        // =====================================================
        // MARKET COMBOBOX
        // =====================================================

        marketBox.setPromptText("Select Market");
        marketBox.setPrefWidth(170);
        marketBox.setDisable(true);

        marketBox.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8px;" +
                "-fx-border-color: #A2D9CE;" +
                "-fx-border-radius: 8px;"
        );

        // =====================================================
        // REFRESH BUTTON
        // =====================================================

        Button refreshButton = new Button("⟳ Refresh");

        refreshButton.setStyle(
                "-fx-background-color: #117864;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8px;" +
                "-fx-padding: 8 15;" +
                "-fx-cursor: hand;"
        );

        // =====================================================
        // SELECTOR ROW
        // =====================================================

        HBox selectors = new HBox(10);

        selectors.setAlignment(
                Pos.CENTER_RIGHT
        );

        selectors.getChildren().addAll(
                stateBox,
                districtBox,
                marketBox,
                refreshButton
        );

        header.getChildren().addAll(
                titles,
                spacer,
                selectors
        );

        // =====================================================
        // MESSAGE LABEL
        // =====================================================

        messageLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #566573;"
        );

        messageLabel.setText(
                "Loading states..."
        );

        // =====================================================
        // CROP COLUMN
        // =====================================================

        TableColumn<CropPrice, String> cropColumn =
                new TableColumn<>("Crop");

        cropColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                data.getValue().getCommodity()
                        )
        );

        cropColumn.setPrefWidth(180);

        // =====================================================
        // MIN PRICE
        // =====================================================

        TableColumn<CropPrice, String> minColumn =
                new TableColumn<>("Min Price");

        minColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                "₹" +
                                data.getValue().getMinPrice()
                        )
        );

        minColumn.setPrefWidth(130);

        // =====================================================
        // MAX PRICE
        // =====================================================

        TableColumn<CropPrice, String> maxColumn =
                new TableColumn<>("Max Price");

        maxColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                "₹" +
                                data.getValue().getMaxPrice()
                        )
        );

        maxColumn.setPrefWidth(130);

        // =====================================================
        // MODAL PRICE
        // =====================================================

        TableColumn<CropPrice, String> modalColumn =
                new TableColumn<>("Modal Price");

        modalColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                "₹" +
                                data.getValue().getModalPrice()
                        )
        );

        modalColumn.setPrefWidth(140);

        // =====================================================
        // MARKET
        // =====================================================

        TableColumn<CropPrice, String> marketColumn =
                new TableColumn<>("Market");

        marketColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                data.getValue().getMarket()
                        )
        );

        marketColumn.setPrefWidth(160);

        // =====================================================
        // VARIETY
        // =====================================================

        TableColumn<CropPrice, String> varietyColumn =
                new TableColumn<>("Variety");

        varietyColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                data.getValue().getVariety()
                        )
        );

        varietyColumn.setPrefWidth(150);

        // =====================================================
        // DATE
        // =====================================================

        TableColumn<CropPrice, String> dateColumn =
                new TableColumn<>("Date");

        dateColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                data.getValue().getArrivalDate()
                        )
        );

        dateColumn.setPrefWidth(130);

        // =====================================================
        // ADD COLUMNS
        // =====================================================

        table.getColumns().addAll(
                cropColumn,
                minColumn,
                maxColumn,
                modalColumn,
                marketColumn,
                varietyColumn,
                dateColumn
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setPrefHeight(520);

        table.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #A2D9CE;" +
                "-fx-border-radius: 12px;"
        );

        // =====================================================
        // STATE SELECTION
        // =====================================================

        stateBox.setOnAction(e -> {

            String state =
                    stateBox.getValue();

            if (
                    state == null ||
                    state.trim().isEmpty()
            ) {
                return;
            }

            districtBox.getItems().clear();
            marketBox.getItems().clear();

            districtBox.setDisable(true);
            marketBox.setDisable(true);

            table.getItems().clear();

            messageLabel.setText(
                    "Loading districts..."
            );

            Thread thread = new Thread(() -> {

                try {

                    var districts =
                            controller.getDistricts(
                                    state
                            );

                    Platform.runLater(() -> {

                        districtBox.getItems().setAll(
                                districts
                        );

                        districtBox.setDisable(
                                false
                        );

                        messageLabel.setText(
                                districts.size()
                                        + " districts found."
                        );
                    });

                } catch (Exception ex) {

                    ex.printStackTrace();

                    Platform.runLater(() -> {

                        messageLabel.setText(
                                "Unable to load districts."
                        );
                    });
                }

            });

            thread.setDaemon(true);
            thread.start();
        });

        // =====================================================
        // DISTRICT SELECTION
        // =====================================================

        districtBox.setOnAction(e -> {

            String state =
                    stateBox.getValue();

            String district =
                    districtBox.getValue();

            if (
                    state == null ||
                    district == null ||
                    district.trim().isEmpty()
            ) {
                return;
            }

            marketBox.getItems().clear();
            marketBox.setDisable(true);

            table.getItems().clear();

            messageLabel.setText(
                    "Loading markets..."
            );

            Thread thread = new Thread(() -> {

                try {

                    var markets =
                            controller.getMarkets(
                                    state,
                                    district
                            );

                    Platform.runLater(() -> {

                        marketBox.getItems().setAll(
                                markets
                        );

                        marketBox.setDisable(
                                false
                        );

                        messageLabel.setText(
                                markets.size()
                                        + " markets found."
                        );
                    });

                } catch (Exception ex) {

                    ex.printStackTrace();

                    Platform.runLater(() -> {

                        messageLabel.setText(
                                "Unable to load markets."
                        );
                    });
                }

            });

            thread.setDaemon(true);
            thread.start();
        });

        // =====================================================
        // MARKET SELECTION
        // =====================================================

        marketBox.setOnAction(e -> {

            String state =
                    stateBox.getValue();

            String district =
                    districtBox.getValue();

            String market =
                    marketBox.getValue();

            if (
                    state == null ||
                    district == null ||
                    market == null ||
                    market.trim().isEmpty()
            ) {
                return;
            }

            loadPrices(
                    state,
                    district,
                    market
            );
        });

        // =====================================================
        // REFRESH BUTTON
        // =====================================================

        refreshButton.setOnAction(e -> {

            String state =
                    stateBox.getValue();

            String district =
                    districtBox.getValue();

            String market =
                    marketBox.getValue();

            // -----------------------------------------------
            // If market selected → refresh prices
            // -----------------------------------------------

            if (
                    state != null &&
                    district != null &&
                    market != null
            ) {

                loadPrices(
                        state,
                        district,
                        market
                );

                return;
            }

            // -----------------------------------------------
            // If district selected → refresh markets
            // -----------------------------------------------

            if (
                    state != null &&
                    district != null
            ) {

                loadMarkets(
                        state,
                        district
                );

                return;
            }

            // -----------------------------------------------
            // If only state selected → refresh districts
            // -----------------------------------------------

            if (
                    state != null
            ) {

                loadDistricts(
                        state
                );

                return;
            }

            // -----------------------------------------------
            // Nothing selected → load states
            // -----------------------------------------------

            loadStates();
        });

        // =====================================================
        // ROOT
        // =====================================================

        root.getChildren().addAll(
                header,
                messageLabel,
                table
        );

        ScrollPane scroll =
                new ScrollPane(root);

        scroll.setFitToWidth(true);

        scroll.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;"
        );

        // =====================================================
        // LOAD STATES
        // =====================================================

        loadStates();

        return scroll;
    }

    // =========================================================
    // LOAD STATES
    // =========================================================

    private void loadStates() {

        messageLabel.setText(
                "Loading states..."
        );

        Thread thread = new Thread(() -> {

            try {

                var states =
                        controller.getStates();

                Platform.runLater(() -> {

                    stateBox.getItems().setAll(
                            states
                    );

                    stateBox.setDisable(false);

                    messageLabel.setText(
                            states.size()
                                    + " states available."
                    );
                });

            } catch (Exception ex) {

                ex.printStackTrace();

                Platform.runLater(() -> {

                    messageLabel.setText(
                            "Unable to load states."
                    );
                });
            }

        });

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // LOAD DISTRICTS
    // =========================================================

    private void loadDistricts(
            String state) {

        districtBox.getItems().clear();
        marketBox.getItems().clear();

        districtBox.setDisable(true);
        marketBox.setDisable(true);

        table.getItems().clear();

        messageLabel.setText(
                "Loading districts..."
        );

        Thread thread = new Thread(() -> {

            try {

                var districts =
                        controller.getDistricts(
                                state
                        );

                Platform.runLater(() -> {

                    districtBox.getItems().setAll(
                            districts
                    );

                    districtBox.setDisable(false);

                    messageLabel.setText(
                            districts.size()
                                    + " districts found."
                    );
                });

            } catch (Exception ex) {

                ex.printStackTrace();

                Platform.runLater(() -> {

                    messageLabel.setText(
                            "Unable to load districts."
                    );
                });
            }

        });

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // LOAD MARKETS
    // =========================================================

    private void loadMarkets(
            String state,
            String district) {

        marketBox.getItems().clear();
        marketBox.setDisable(true);

        table.getItems().clear();

        messageLabel.setText(
                "Loading markets..."
        );

        Thread thread = new Thread(() -> {

            try {

                var markets =
                        controller.getMarkets(
                                state,
                                district
                        );

                Platform.runLater(() -> {

                    marketBox.getItems().setAll(
                            markets
                    );

                    marketBox.setDisable(false);

                    messageLabel.setText(
                            markets.size()
                                    + " markets found."
                    );
                });

            } catch (Exception ex) {

                ex.printStackTrace();

                Platform.runLater(() -> {

                    messageLabel.setText(
                            "Unable to load markets."
                    );
                });
            }

        });

        thread.setDaemon(true);
        thread.start();
    }

    // =========================================================
    // LOAD CROP PRICES
    // =========================================================

    private void loadPrices(
            String state,
            String district,
            String market) {

        table.getItems().clear();

        messageLabel.setText(
                "Loading latest mandi prices..."
        );

        Thread thread = new Thread(() -> {

            try {

                var prices =
                        controller.getCropPrices(
                                state,
                                district,
                                market
                        );

                Platform.runLater(() -> {

                    table.setItems(
                            FXCollections.observableArrayList(
                                    prices
                            )
                    );

                    if (
                            prices.isEmpty()
                    ) {

                        messageLabel.setText(
                                "No crop prices found for "
                                        + market
                                        + "."
                        );

                    } else {

                        messageLabel.setText(
                                prices.size()
                                        + " crop price records found."
                        );
                    }
                });

            } catch (Exception ex) {

                ex.printStackTrace();

                Platform.runLater(() -> {

                    messageLabel.setText(
                            "Unable to load crop prices."
                    );
                });
            }

        });

        thread.setDaemon(true);
        thread.start();
    }
}