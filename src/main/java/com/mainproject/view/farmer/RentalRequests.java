package com.mainproject.view.farmer;

import com.mainproject.controller.EquipmentRentalController;
import com.mainproject.view.common.ReviewDialog;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RentalRequests {

    // =========================================================
    // VARIABLES
    // =========================================================

    private final String farmerEmail;

    private final EquipmentRentalController rentalController;

    private VBox requestsContainer;

    private Label totalLabel;
    private Label pendingLabel;
    private Label acceptedLabel;
    private Label rejectedLabel;

    // =========================================================
    // COLORS
    // =========================================================

    private static final String PRIMARY_GREEN = "#117864";

    private static final String DARK_TEXT = "#1B2631";

    private static final String LIGHT_BG = "#F8FFFB";

    private static final String BORDER = "#D5E8E1";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public RentalRequests(String farmerEmail) {

        this.farmerEmail = farmerEmail;

        this.rentalController =
                new EquipmentRentalController();
    }

    // =========================================================
    // GET VIEW
    // =========================================================

    public Node getView() {

        BorderPane root = new BorderPane();

        root.setPadding(
                new Insets(20)
        );

        root.setStyle(
                "-fx-background-color: "
                        + LIGHT_BG
                        + ";"
        );

        // =====================================================
        // HEADER
        // =====================================================

        VBox topSection = new VBox(15);

        Label title = new Label(
                "🚜 Equipment Rental Requests"
        );

        title.setStyle(
                "-fx-font-size: 26px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + DARK_TEXT
                        + ";"
        );

        Label subtitle = new Label(
                "Manage rental requests received for your equipment."
        );

        subtitle.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-text-fill: #64748B;"
        );

        Button refreshButton =
                new Button("↻ Refresh");

        refreshButton.setStyle(
                "-fx-background-color: "
                        + PRIMARY_GREEN
                        + ";"
                        + "-fx-text-fill: white;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-radius: 8px;"
                        + "-fx-padding: 9 18;"
                        + "-fx-cursor: hand;"
        );

        refreshButton.setOnAction(
                e -> loadRentalRequests()
        );

        HBox titleRow = new HBox();

        VBox titleBox = new VBox(
                5,
                title,
                subtitle
        );

        HBox.setHgrow(
                titleBox,
                Priority.ALWAYS
        );

        titleRow.setAlignment(
                Pos.CENTER_LEFT
        );

        titleRow.getChildren().addAll(
                titleBox,
                refreshButton
        );

        // =====================================================
        // STATISTICS
        // =====================================================

        totalLabel = createStatValue("0");
        pendingLabel = createStatValue("0");
        acceptedLabel = createStatValue("0");
        rejectedLabel = createStatValue("0");

        HBox stats = new HBox(15);

        stats.getChildren().addAll(

                createStatCard(
                        "Total Requests",
                        totalLabel,
                        "#E8F5E9"
                ),

                createStatCard(
                        "Pending",
                        pendingLabel,
                        "#FFF8E1"
                ),

                createStatCard(
                        "Accepted",
                        acceptedLabel,
                        "#E8F5E9"
                ),

                createStatCard(
                        "Rejected",
                        rejectedLabel,
                        "#FFEBEE"
                )
        );

        for (Node node : stats.getChildren()) {

            HBox.setHgrow(
                    node,
                    Priority.ALWAYS
            );
        }

        topSection.getChildren().addAll(
                titleRow,
                stats
        );

        root.setTop(
                topSection
        );

        // =====================================================
        // REQUEST CONTAINER
        // =====================================================

        requestsContainer = new VBox(15);

        requestsContainer.setPadding(
                new Insets(20, 0, 20, 0)
        );

        ScrollPane scrollPane =
                new ScrollPane(
                        requestsContainer
                );

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-background: transparent;"
        );

        root.setCenter(
                scrollPane
        );

        // =====================================================
        // LOAD DATA
        // =====================================================

        loadRentalRequests();

        return root;
    }

    // =========================================================
    // LOAD RENTAL REQUESTS
    // =========================================================

    private void loadRentalRequests() {

        requestsContainer.getChildren().clear();

        Label loading = new Label(
                "Loading rental requests..."
        );

        loading.setStyle(
                "-fx-font-size: 15px;"
                        + "-fx-text-fill: #64748B;"
        );

        requestsContainer.getChildren().add(
                loading
        );

        Thread thread = new Thread(() -> {

            List<com.mainproject.model.EquipmentRental> rentals =
                    rentalController.getRentalsByFarmer(
                            farmerEmail
                    );

            Platform.runLater(() -> {

                requestsContainer.getChildren().clear();

                updateStatistics(
                        rentals
                );

                if (rentals == null
                        || rentals.isEmpty()) {

                    showEmptyState();

                    return;
                }

                for (
                        com.mainproject.model.EquipmentRental rental
                        : rentals
                ) {

                    requestsContainer.getChildren().add(
                            createRentalCard(
                                    rental
                            )
                    );
                }
            });

        });

        thread.setDaemon(true);

        thread.start();
    }

    // =========================================================
    // UPDATE STATISTICS
    // =========================================================

    private void updateStatistics(
            List<com.mainproject.model.EquipmentRental> rentals) {

        int total = 0;
        int pending = 0;
        int accepted = 0;
        int rejected = 0;

        if (rentals != null) {

            total = rentals.size();

            for (
                    com.mainproject.model.EquipmentRental rental
                    : rentals
            ) {

                String status =
                        rental.getStatus();

                if (status == null) {
                    continue;
                }

                if (status.equalsIgnoreCase("pending")) {

                    pending++;

                } else if (
                        status.equalsIgnoreCase("accepted")
                ) {

                    accepted++;

                } else if (
                        status.equalsIgnoreCase("rejected")
                ) {

                    rejected++;
                }
            }
        }

        totalLabel.setText(
                String.valueOf(total)
        );

        pendingLabel.setText(
                String.valueOf(pending)
        );

        acceptedLabel.setText(
                String.valueOf(accepted)
        );

        rejectedLabel.setText(
                String.valueOf(rejected)
        );
    }

    // =========================================================
    // CREATE RENTAL CARD
    // =========================================================

    private VBox createRentalCard(
            com.mainproject.model.EquipmentRental rental) {

        VBox card = new VBox(12);

        card.setPadding(
                new Insets(18)
        );

        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 14px;"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 14px;"
        );

        // =====================================================
        // HEADER
        // =====================================================

        Label equipmentName =
                new Label(
                        "🚜 "
                                + safe(
                                rental.getEquipmentName()
                        )
                );

        equipmentName.setStyle(
                "-fx-font-size: 19px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + DARK_TEXT
                        + ";"
        );

        Label statusBadge =
                createStatusBadge(
                        rental.getStatus()
                );

        HBox header = new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        header.getChildren().addAll(
                equipmentName,
                spacer,
                statusBadge
        );

        // =====================================================
        // BUYER INFORMATION
        // =====================================================

        Label buyerTitle =
                createSectionTitle(
                        "Buyer Information"
                );

        Label buyerName =
                createInfoLabel(
                        "👤 Buyer: "
                                + safe(
                                rental.getBuyerName()
                        )
                );

        Label buyerEmail =
                createInfoLabel(
                        "✉ Email: "
                                + safe(
                                rental.getBuyerEmail()
                        )
                );

        // =====================================================
        // RENTAL INFORMATION
        // =====================================================

        Label rentalTitle =
                createSectionTitle(
                        "Rental Details"
                );

        Label startDate =
                createInfoLabel(
                        "📅 Start Date: "
                                + formatDate(
                                rental.getStartDate()
                        )
                );

        Label endDate =
                createInfoLabel(
                        "📅 End Date: "
                                + formatDate(
                                rental.getEndDate()
                        )
                );

        Label days =
                createInfoLabel(
                        "🗓 Number of Days: "
                                + rental.getNumberOfDays()
                );

        // =====================================================
        // PRICE
        // =====================================================

        Label pricePerDay =
                new Label(
                        "₹"
                                + formatAmount(
                                rental.getPricePerDay()
                        )
                                + " / day"
                );

        pricePerDay.setStyle(
                "-fx-font-size: 15px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + PRIMARY_GREEN
                        + ";"
        );

        Label totalAmount =
                new Label(
                        "Total Amount: ₹"
                                + formatAmount(
                                rental.getTotalAmount()
                        )
                );

        totalAmount.setStyle(
                "-fx-font-size: 19px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + PRIMARY_GREEN
                        + ";"
        );

        HBox priceRow =
                new HBox();

        priceRow.setAlignment(
                Pos.CENTER_LEFT
        );

        Region priceSpacer =
                new Region();

        HBox.setHgrow(
                priceSpacer,
                Priority.ALWAYS
        );

        priceRow.getChildren().addAll(
                pricePerDay,
                priceSpacer,
                totalAmount
        );

        // =====================================================
        // ACTION BUTTONS
        // =====================================================

        HBox actions =
                new HBox(10);

        actions.setAlignment(
                Pos.CENTER_RIGHT
        );

        String status =
                rental.getStatus();

        if (status != null
                && status.equalsIgnoreCase(
                        "pending"
                )) {

            Button rejectButton =
                    new Button(
                            "✕ Reject"
                    );

            rejectButton.setStyle(
                    "-fx-background-color: #FEE2E2;"
                            + "-fx-text-fill: #B91C1C;"
                            + "-fx-font-weight: bold;"
                            + "-fx-background-radius: 8px;"
                            + "-fx-padding: 9 18;"
                            + "-fx-cursor: hand;"
            );

            Button acceptButton =
                    new Button(
                            "✓ Accept Request"
                    );

            acceptButton.setStyle(
                    "-fx-background-color: "
                            + PRIMARY_GREEN
                            + ";"
                            + "-fx-text-fill: white;"
                            + "-fx-font-weight: bold;"
                            + "-fx-background-radius: 8px;"
                            + "-fx-padding: 9 18;"
                            + "-fx-cursor: hand;"
            );

            rejectButton.setOnAction(
                    e -> updateRentalStatus(
                            rental,
                            "rejected"
                    )
            );

            acceptButton.setOnAction(
                    e -> updateRentalStatus(
                            rental,
                            "accepted"
                    )
            );

            actions.getChildren().addAll(
                    rejectButton,
                    acceptButton
            );

        } else {

            Label statusMessage =
                    new Label(
                            "Request "
                                    + safe(
                                    rental.getStatus()
                            )
                    );

            statusMessage.setStyle(
                    "-fx-font-size: 13px;"
                            + "-fx-font-weight: bold;"
                            + "-fx-text-fill: #64748B;"
            );

            actions.getChildren().add(
                    statusMessage
            );

            if (status != null && status.equalsIgnoreCase("completed")) {
                Button reviewBuyer = new Button("⭐ Review Renter");
                reviewBuyer.setStyle(
                        "-fx-background-color: " + PRIMARY_GREEN + ";" +
                        "-fx-text-fill:white;" +
                        "-fx-font-weight:bold;" +
                        "-fx-padding:9 18;" +
                        "-fx-background-radius:8;" +
                        "-fx-cursor:hand;"
                );
                reviewBuyer.setOnAction(e -> ReviewDialog.show(
                        farmerEmail, farmerEmail, "FARMER",
                        safe(rental.getBuyerEmail()), safe(rental.getBuyerName()), "BUYER",
                        safe(rental.getRentalId()), "EQUIPMENT_RENTAL"
                ));
                actions.getChildren().add(reviewBuyer);
            }
        }

        // =====================================================
        // ADD TO CARD
        // =====================================================

        card.getChildren().addAll(

                header,

                new Separator(),

                buyerTitle,

                buyerName,

                buyerEmail,

                new Separator(),

                rentalTitle,

                startDate,

                endDate,

                days,

                new Separator(),

                priceRow,

                actions
        );

        return card;
    }

    // =========================================================
    // UPDATE RENTAL STATUS
    // =========================================================

    private void updateRentalStatus(
            com.mainproject.model.EquipmentRental rental,
            String status) {

        Thread thread = new Thread(() -> {

            boolean updated =
                    rentalController.updateRentalStatus(
                            rental.getRentalId(),
                            status
                    );

            Platform.runLater(() -> {

                if (updated) {

                    loadRentalRequests();

                } else {

                    System.out.println(
                            "Failed to update rental status."
                    );
                }
            });

        });

        thread.setDaemon(true);

        thread.start();
    }

    // =========================================================
    // EMPTY STATE
    // =========================================================

    private void showEmptyState() {

        VBox emptyBox =
                new VBox(10);

        emptyBox.setAlignment(
                Pos.CENTER
        );

        emptyBox.setPadding(
                new Insets(80)
        );

        Label icon =
                new Label("🚜");

        icon.setStyle(
                "-fx-font-size: 50px;"
        );

        Label title =
                new Label(
                        "No Rental Requests Yet"
                );

        title.setStyle(
                "-fx-font-size: 20px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + DARK_TEXT
                        + ";"
        );

        Label description =
                new Label(
                        "Rental requests from buyers will appear here."
                );

        description.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-text-fill: #64748B;"
        );

        emptyBox.getChildren().addAll(
                icon,
                title,
                description
        );

        requestsContainer.getChildren().add(
                emptyBox
        );
    }

    // =========================================================
    // STATUS BADGE
    // =========================================================

    private Label createStatusBadge(
            String status) {

        String value =
                status == null
                        ? "pending"
                        : status.toLowerCase();

        Label badge =
                new Label(
                        value.toUpperCase()
                );

        String style;

        switch (value) {

            case "accepted":

                style =
                        "-fx-background-color: #DCFCE7;"
                                + "-fx-text-fill: #15803D;";

                break;

            case "rejected":

                style =
                        "-fx-background-color: #FEE2E2;"
                                + "-fx-text-fill: #B91C1C;";

                break;

            case "completed":

                style =
                        "-fx-background-color: #DBEAFE;"
                                + "-fx-text-fill: #1D4ED8;";

                break;

            default:

                style =
                        "-fx-background-color: #FEF3C7;"
                                + "-fx-text-fill: #B45309;";

                break;
        }

        badge.setStyle(
                style
                        + "-fx-font-size: 11px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-background-radius: 20px;"
                        + "-fx-padding: 6 12;"
        );

        return badge;
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private VBox createStatCard(
            String title,
            Label value,
            String background) {

        VBox card =
                new VBox(8);

        card.setPadding(
                new Insets(16)
        );

        card.setPrefWidth(200);

        card.setStyle(
                "-fx-background-color: "
                        + background
                        + ";"
                        + "-fx-background-radius: 12px;"
                        + "-fx-border-color: "
                        + BORDER
                        + ";"
                        + "-fx-border-radius: 12px;"
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-text-fill: #64748B;"
        );

        card.getChildren().addAll(
                titleLabel,
                value
        );

        return card;
    }

    private Label createStatValue(
            String value) {

        Label label =
                new Label(value);

        label.setStyle(
                "-fx-font-size: 25px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + DARK_TEXT
                        + ";"
        );

        return label;
    }

    private Label createSectionTitle(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + DARK_TEXT
                        + ";"
        );

        return label;
    }

    private Label createInfoLabel(
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-text-fill: #475569;"
        );

        return label;
    }

    // =========================================================
    // DATE FORMAT
    // =========================================================

    private String formatDate(
            Date date) {

        if (date == null) {
            return "-";
        }

        SimpleDateFormat formatter =
                new SimpleDateFormat(
                        "dd MMM yyyy"
                );

        return formatter.format(
                date
        );
    }

    // =========================================================
    // AMOUNT FORMAT
    // =========================================================

    private String formatAmount(
            double amount) {

        if (amount == Math.floor(amount)) {

            return String.valueOf(
                    (long) amount
            );
        }

        return String.format(
                "%.2f",
                amount
        );
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }
}