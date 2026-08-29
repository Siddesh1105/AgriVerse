package com.mainproject.view.buyer;

import com.mainproject.controller.EquipmentRentalController;
import com.mainproject.model.Equipment;
import com.mainproject.model.EquipmentRental;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class EquipmentRentalForm {

    private final BuyerDashboard mainController;
    private final Equipment equipment;

    private final EquipmentRentalController rentalController =
            new EquipmentRentalController();

    public EquipmentRentalForm(
            BuyerDashboard mainController,
            Equipment equipment) {

        this.mainController = mainController;
        this.equipment = equipment;
    }


    public Node getView() {

        VBox root = new VBox(20);

        root.setPadding(
                new Insets(30)
        );

        // =====================================
        // TITLE
        // =====================================

        Label title =
                new Label(
                        "🚜 Rent Farm Equipment"
                );

        title.setStyle(
                "-fx-font-size: 26px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #1F2937;"
        );


        Label subtitle =
                new Label(
                        "Complete the rental request for "
                                + equipment.getName()
                );

        subtitle.setStyle(
                "-fx-font-size: 15px;"
                        + "-fx-text-fill: #64748B;"
        );


        // =====================================
        // EQUIPMENT INFORMATION
        // =====================================

        VBox equipmentCard =
                new VBox(10);

        equipmentCard.setPadding(
                new Insets(20)
        );

        equipmentCard.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 12;"
                        + "-fx-border-color: #E2E8F0;"
                        + "-fx-border-radius: 12;"
        );


        Label equipmentName =
                new Label(
                        "🚜 " + equipment.getName()
                );

        equipmentName.setStyle(
                "-fx-font-size: 20px;"
                        + "-fx-font-weight: bold;"
        );


        Label owner =
                new Label(
                        "Owner: "
                                + equipment.getOwnerName()
                );


        Label location =
                new Label(
                        "📍 Location: "
                                + equipment.getLocation()
                );


        Label price =
                new Label(
                        "₹"
                                + String.format(
                                "%.0f",
                                equipment.getPrice()
                        )
                                + " / day"
                );

        price.setStyle(
                "-fx-font-size: 18px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #166534;"
        );


        equipmentCard.getChildren().addAll(
                equipmentName,
                owner,
                location,
                price
        );


        // =====================================
        // DATE PICKERS
        // =====================================

        Label startLabel =
                new Label("Rental Start Date");

        DatePicker startDate =
                new DatePicker();

        startDate.setValue(
                LocalDate.now()
        );


        Label endLabel =
                new Label("Rental End Date");

        DatePicker endDate =
                new DatePicker();

        endDate.setValue(
                LocalDate.now().plusDays(1)
        );


        // =====================================
        // DAYS AND TOTAL
        // =====================================

        Label daysLabel =
                new Label();

        Label totalLabel =
                new Label();

        daysLabel.setStyle(
                "-fx-font-size: 16px;"
                        + "-fx-font-weight: bold;"
        );

        totalLabel.setStyle(
                "-fx-font-size: 20px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #166534;"
        );


        Runnable calculateTotal = () -> {

            if (startDate.getValue() == null
                    || endDate.getValue() == null) {

                return;
            }

            if (endDate.getValue()
                    .isBefore(
                            startDate.getValue()
                    )) {

                daysLabel.setText(
                        "Invalid date selection"
                );

                totalLabel.setText("");

                return;
            }

            long days =
                    java.time.temporal.ChronoUnit.DAYS
                            .between(
                                    startDate.getValue(),
                                    endDate.getValue()
                            )
                            + 1;

            double total =
                    days * equipment.getPrice();

            daysLabel.setText(
                    "Number of Days: "
                            + days
            );

            totalLabel.setText(
                    "Total Amount: ₹"
                            + String.format(
                            "%.0f",
                            total
                    )
            );
        };


        startDate.valueProperty()
                .addListener(
                        (obs, oldValue, newValue)
                                -> calculateTotal.run()
                );

        endDate.valueProperty()
                .addListener(
                        (obs, oldValue, newValue)
                                -> calculateTotal.run()
                );

        calculateTotal.run();


        // =====================================
        // SUBMIT BUTTON
        // =====================================

        Button submitButton =
                new Button(
                        "🚜 Send Rental Request"
                );

        submitButton.setMaxWidth(
                Double.MAX_VALUE
        );

        submitButton.setStyle(
                "-fx-background-color: #166534;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 16px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 12;"
                        + "-fx-background-radius: 8;"
        );


        submitButton.setOnAction(e -> {

            if (startDate.getValue() == null
                    || endDate.getValue() == null) {

                showAlert(
                        "Please select rental dates."
                );

                return;
            }

            if (endDate.getValue()
                    .isBefore(
                            startDate.getValue()
                    )) {

                showAlert(
                        "End date cannot be before start date."
                );

                return;
            }


            long days =
                    java.time.temporal.ChronoUnit.DAYS
                            .between(
                                    startDate.getValue(),
                                    endDate.getValue()
                            )
                            + 1;


            double totalAmount =
                    days * equipment.getPrice();


            // =====================================
            // CREATE RENTAL OBJECT
            // =====================================

            EquipmentRental rental =
                    new EquipmentRental();


            rental.setEquipmentId(
                    equipment.getEquipmentId()
            );

            rental.setEquipmentName(
                    equipment.getName()
            );

            rental.setEquipmentOwnerEmail(
                    equipment.getOwnerEmail()
            );

            rental.setEquipmentOwnerName(
                    equipment.getOwnerName()
            );


            /*
             * IMPORTANT:
             *
             * Replace these two values with your
             * logged-in buyer details.
             */

            rental.setBuyerEmail(
                    mainController.getBuyerEmail()
            );

            rental.setBuyerName(
                    mainController.getBuyerName()
            );


            rental.setStartDate(
                    Date.from(
                            startDate.getValue()
                                    .atStartOfDay(
                                            ZoneId.systemDefault()
                                    )
                                    .toInstant()
                    )
            );


            rental.setEndDate(
                    Date.from(
                            endDate.getValue()
                                    .atStartOfDay(
                                            ZoneId.systemDefault()
                                    )
                                    .toInstant()
                    )
            );


            rental.setNumberOfDays(
                    (int) days
            );

            rental.setPricePerDay(
                    equipment.getPrice()
            );

            rental.setTotalAmount(
                    totalAmount
            );

            rental.setStatus(
                    "pending"
            );


            rental.setCreatedAt(
                    new Date()
            );


            // =====================================
            // SAVE TO FIRESTORE
            // =====================================

            boolean success =
                    rentalController.createRental(
                            rental
                    );


            if (success) {

                Alert alert =
                        new Alert(
                                Alert.AlertType.INFORMATION
                        );

                alert.setTitle(
                        "Rental Request Sent"
                );

                alert.setHeaderText(
                        "Request Sent Successfully!"
                );

                alert.setContentText(
                        "Your rental request for "
                                + equipment.getName()
                                + " has been sent to "
                                + equipment.getOwnerName()
                                + "."
                );

                alert.showAndWait();


                // Go back to Search & Rent
                mainController.setView(
                        new SearchAndRent(
                                mainController
                        ).getView()
                );

            } else {

                showAlert(
                        "Failed to send rental request."
                );
            }
        });


        Button backButton =
                new Button(
                        "← Back"
                );

        backButton.setOnAction(e ->
                mainController.setView(
                        new SearchAndRent(
                                mainController
                        ).getView()
                )
        );


        HBox buttons =
                new HBox(
                        10,
                        backButton,
                        submitButton
                );


        root.getChildren().addAll(
                title,
                subtitle,
                equipmentCard,
                startLabel,
                startDate,
                endLabel,
                endDate,
                daysLabel,
                totalLabel,
                buttons
        );


        ScrollPane scrollPane =
                new ScrollPane(root);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color: transparent;"
        );

        return scrollPane;
    }


    // =====================================
    // ALERT
    // =====================================

    private void showAlert(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alert.setTitle("Warning");

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}