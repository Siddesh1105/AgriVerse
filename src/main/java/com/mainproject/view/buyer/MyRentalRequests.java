package com.mainproject.view.buyer;

import com.mainproject.controller.EquipmentRentalController;
import com.mainproject.model.EquipmentRental;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyRentalRequests {

    private final BuyerDashboard dashboard;

    private final EquipmentRentalController rentalController;

    // Used for refreshing rental requests after payment
    private VBox rentalContainer;


    public MyRentalRequests(BuyerDashboard dashboard) {

        this.dashboard = dashboard;

        this.rentalController =
                new EquipmentRentalController();
    }


    // =====================================================
    // GET VIEW
    // =====================================================

    public Node getView() {

        VBox root = new VBox(20);

        root.setPadding(
                new Insets(30)
        );

        root.setStyle(
                "-fx-background-color: #F8FAFC;"
        );


        // =================================================
        // HEADER
        // =================================================

        VBox header = new VBox(5);

        Label title =
                new Label("📋 My Rental Requests");

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1E293B;"
        );


        Label subtitle =
                new Label(
                        "Track all your equipment rental requests."
                );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #64748B;"
        );


        header.getChildren().addAll(
                title,
                subtitle
        );


        // =================================================
        // CONTENT
        // =================================================

        rentalContainer =
                new VBox(15);

        rentalContainer.setPadding(
                new Insets(5)
        );


        loadRentalRequests(
                rentalContainer
        );


        ScrollPane scrollPane =
                new ScrollPane(
                        rentalContainer
                );

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-border-color: transparent;"
        );


        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );


        root.getChildren().addAll(
                header,
                scrollPane
        );


        return root;
    }


    // =====================================================
    // LOAD RENTAL REQUESTS
    // =====================================================

    private void loadRentalRequests(
            VBox container) {

        try {

            String buyerEmail =
                    dashboard.getBuyerEmail();


            if (buyerEmail == null
                    || buyerEmail.trim().isEmpty()) {

                showEmptyMessage(
                        container,
                        "Buyer email not found."
                );

                return;
            }


            List<EquipmentRental> rentals =
                    rentalController.getRentalsByBuyer(
                            buyerEmail
                    );


            if (rentals == null
                    || rentals.isEmpty()) {

                showEmptyMessage(
                        container,
                        "You have not sent any rental requests yet."
                );

                return;
            }


            for (EquipmentRental rental : rentals) {

                container.getChildren().add(
                        createRentalCard(rental)
                );
            }


        } catch (Exception e) {

            e.printStackTrace();

            showEmptyMessage(
                    container,
                    "Unable to load rental requests."
            );
        }
    }


    // =====================================================
    // REFRESH RENTAL REQUESTS
    // =====================================================

    private void refreshRentalRequests() {

        if (rentalContainer == null) {
            return;
        }

        rentalContainer.getChildren().clear();

        loadRentalRequests(
                rentalContainer
        );
    }


    // =====================================================
    // RENTAL CARD
    // =====================================================

    private Node createRentalCard(
            EquipmentRental rental) {

        VBox card =
                new VBox(12);

        card.setPadding(
                new Insets(20)
        );

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #E2E8F0;" +
                "-fx-border-radius: 12;"
        );


        // =================================================
        // TOP ROW
        // =================================================

        HBox topRow =
                new HBox();

        topRow.setAlignment(
                Pos.CENTER_LEFT
        );


        VBox equipmentInfo =
                new VBox(5);


        Label equipmentName =
                new Label(
                        safe(
                                rental.getEquipmentName()
                        )
                );

        equipmentName.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1E293B;"
        );


        Label owner =
                new Label(
                        "Owner: "
                                + safe(
                                rental.getEquipmentOwnerName()
                        )
                );

        owner.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #64748B;"
        );


        equipmentInfo.getChildren().addAll(
                equipmentName,
                owner
        );


        HBox.setHgrow(
                equipmentInfo,
                Priority.ALWAYS
        );


        // =================================================
        // STATUS
        // =================================================

        String rentalStatus =
                rental.getStatus() == null
                        ? "pending"
                        : rental.getStatus().toLowerCase();


        Label status =
                new Label(
                        rentalStatus.toUpperCase()
                );


        applyStatusStyle(
                status,
                rentalStatus
        );


        topRow.getChildren().addAll(
                equipmentInfo,
                status
        );


        // =================================================
        // DETAILS
        // =================================================

        HBox detailsRow =
                new HBox(40);

        detailsRow.setPadding(
                new Insets(
                        10,
                        0,
                        0,
                        0
                )
        );


        VBox startBox =
                createDetailBox(
                        "Start Date",
                        formatDate(
                                rental.getStartDate()
                        )
                );


        VBox endBox =
                createDetailBox(
                        "End Date",
                        formatDate(
                                rental.getEndDate()
                        )
                );


        VBox daysBox =
                createDetailBox(
                        "Duration",
                        rental.getNumberOfDays()
                                + " Days"
                );


        VBox amountBox =
                createDetailBox(
                        "Total Amount",
                        "₹"
                                + String.format(
                                "%.2f",
                                rental.getTotalAmount()
                        )
                );


        detailsRow.getChildren().addAll(
                startBox,
                endBox,
                daysBox,
                amountBox
        );


        // =================================================
        // PRICE
        // =================================================

        Label price =
                new Label(
                        "Price per day: ₹"
                                + String.format(
                                "%.2f",
                                rental.getPricePerDay()
                        )
                );

        price.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #475569;"
        );


        card.getChildren().addAll(
                topRow,
                detailsRow,
                price
        );


        // =================================================
        // PAYMENT SECTION
        // =================================================

        String paymentStatus =
                rental.getPaymentStatus() == null
                        || rental.getPaymentStatus()
                        .trim()
                        .isEmpty()

                        ? "pending"

                        : rental.getPaymentStatus()
                        .toLowerCase();


        // =================================================
        // SHOW PAY NOW BUTTON
        // =================================================

        if (rentalStatus.equals("accepted")
                && !paymentStatus.equals("paid")) {

            VBox paymentBox =
                    new VBox(8);

            paymentBox.setPadding(
                    new Insets(
                            10,
                            0,
                            0,
                            0
                    )
            );


            Label paymentMessage =
                    new Label(
                            "🎉 Your rental request has been accepted! Complete payment to activate your rental."
                    );

            paymentMessage.setWrapText(true);

            paymentMessage.setStyle(
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: #166534;"
            );


            Button payButton =
                    new Button(
                            "💳 Pay Now ₹"
                                    + String.format(
                                    "%.2f",
                                    rental.getTotalAmount()
                            )
                    );


            payButton.setStyle(
                    "-fx-background-color: #16A34A;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 10 18;" +
                    "-fx-background-radius: 8;" +
                    "-fx-cursor: hand;"
            );


            payButton.setOnAction(e -> {

                PaymentScreen paymentScreen =
                        new PaymentScreen(
                                rental
                        );


                paymentScreen.setOnPaymentSuccess(() -> {

                    // Refresh data after successful payment
                    refreshRentalRequests();
                });


                paymentScreen.show();
            });


            paymentBox.getChildren().addAll(
                    paymentMessage,
                    payButton
            );


            card.getChildren().add(
                    paymentBox
            );
        }


        // =================================================
        // PAYMENT SUCCESS
        // =================================================

        if (paymentStatus.equals("paid")) {

            VBox paymentSuccessBox =
                    new VBox(5);


            paymentSuccessBox.setPadding(
                    new Insets(
                            10,
                            0,
                            0,
                            0
                    )
            );


            Label paymentSuccess =
                    new Label(
                            "✓ Payment Successful"
                    );

            paymentSuccess.setStyle(
                    "-fx-text-fill: #16A34A;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;"
            );


            String method =
                    safe(
                            rental.getPaymentMethod()
                    );


            Label paymentMethod =
                    new Label(
                            "Payment Method: "
                                    + method
                    );

            paymentMethod.setStyle(
                    "-fx-font-size: 12px;" +
                    "-fx-text-fill: #64748B;"
            );


            paymentSuccessBox.getChildren().addAll(
                    paymentSuccess,
                    paymentMethod
            );


            card.getChildren().add(
                    paymentSuccessBox
            );
        }


        // =================================================
        // REJECTED MESSAGE
        // =================================================

        if (rentalStatus.equals("rejected")) {

            Label rejectedMessage =
                    new Label(
                            "✕ This rental request was rejected by the equipment owner."
                    );

            rejectedMessage.setStyle(
                    "-fx-text-fill: #DC2626;" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;"
            );


            card.getChildren().add(
                    rejectedMessage
            );
        }


        // =================================================
        // ACTIVE RENTAL MESSAGE
        // =================================================

        if (rentalStatus.equals("active")) {

            Label activeMessage =
                    new Label(
                            "🚜 Your equipment rental is currently ACTIVE."
                    );

            activeMessage.setStyle(
                    "-fx-text-fill: #16A34A;" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;"
            );


            card.getChildren().add(
                    activeMessage
            );
        }


        // =================================================
        // COMPLETED RENTAL MESSAGE
        // =================================================

        if (rentalStatus.equals("completed")) {

            Label completedMessage =
                    new Label(
                            "✓ This equipment rental has been completed."
                    );

            completedMessage.setStyle(
                    "-fx-text-fill: #1D4ED8;" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;"
            );


            card.getChildren().add(
                    completedMessage
            );
        }


        return card;
    }


    // =====================================================
    // DETAIL BOX
    // =====================================================

    private VBox createDetailBox(
            String labelText,
            String valueText) {

        VBox box =
                new VBox(4);


        Label label =
                new Label(
                        labelText
                );

        label.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-text-fill: #94A3B8;"
        );


        Label value =
                new Label(
                        valueText
                );

        value.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #334155;"
        );


        box.getChildren().addAll(
                label,
                value
        );


        return box;
    }


    // =====================================================
    // STATUS STYLE
    // =====================================================

    private void applyStatusStyle(
            Label status,
            String rentalStatus) {

        String value =
                rentalStatus == null
                        ? ""
                        : rentalStatus.toLowerCase();


        switch (value) {


            case "accepted":

                status.setStyle(
                        "-fx-background-color: #DCFCE7;" +
                        "-fx-text-fill: #166534;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 7 14;" +
                        "-fx-background-radius: 20;"
                );

                break;


            case "active":

                status.setStyle(
                        "-fx-background-color: #DCFCE7;" +
                        "-fx-text-fill: #15803D;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 7 14;" +
                        "-fx-background-radius: 20;"
                );

                break;


            case "rejected":

                status.setStyle(
                        "-fx-background-color: #FEE2E2;" +
                        "-fx-text-fill: #DC2626;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 7 14;" +
                        "-fx-background-radius: 20;"
                );

                break;


            case "completed":

                status.setStyle(
                        "-fx-background-color: #DBEAFE;" +
                        "-fx-text-fill: #1D4ED8;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 7 14;" +
                        "-fx-background-radius: 20;"
                );

                break;


            case "pending":

                status.setStyle(
                        "-fx-background-color: #FEF3C7;" +
                        "-fx-text-fill: #92400E;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 7 14;" +
                        "-fx-background-radius: 20;"
                );

                break;


            default:

                status.setStyle(
                        "-fx-background-color: #F1F5F9;" +
                        "-fx-text-fill: #475569;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 7 14;" +
                        "-fx-background-radius: 20;"
                );

                break;
        }
    }


    // =====================================================
    // EMPTY MESSAGE
    // =====================================================

    private void showEmptyMessage(
            VBox container,
            String message) {

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


        Label text =
                new Label(
                        message
                );

        text.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-text-fill: #64748B;"
        );


        emptyBox.getChildren().addAll(
                icon,
                text
        );


        container.getChildren().add(
                emptyBox
        );
    }


    // =====================================================
    // FORMAT DATE
    // =====================================================

    private String formatDate(
            java.util.Date date) {

        if (date == null) {

            return "Not available";
        }


        SimpleDateFormat format =
                new SimpleDateFormat(
                        "dd MMM yyyy"
                );


        return format.format(date);
    }


    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safe(
            String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return "Not available";
        }


        return value;
    }
}