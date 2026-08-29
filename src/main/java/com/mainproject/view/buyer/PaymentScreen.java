package com.mainproject.view.buyer;

import com.mainproject.controller.EquipmentRentalController;
import com.mainproject.model.EquipmentRental;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PaymentScreen {

    private final EquipmentRental rental;
    private final EquipmentRentalController rentalController;
    private Runnable onPaymentSuccess;

    public PaymentScreen(EquipmentRental rental) {
        this.rental = rental;
        this.rentalController = new EquipmentRentalController();
    }

    public void setOnPaymentSuccess(Runnable onPaymentSuccess) {
        this.onPaymentSuccess = onPaymentSuccess;
    }

    public void show() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("AgriVerse Payment");

        VBox root = new VBox(18);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #F8FAFC;");

        Label title = new Label("💳 Complete Payment");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1E293B;");

        VBox details = new VBox(10);
        details.setPadding(new Insets(20));
        details.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                + "-fx-border-color: #E2E8F0; -fx-border-radius: 12;");

        Label equipment = new Label("🚜 " + safe(rental.getEquipmentName()));
        equipment.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label owner = new Label("Owner: " + safe(rental.getEquipmentOwnerName()));
        Label duration = new Label("Rental Duration: " + rental.getNumberOfDays() + " Days");

        Label amount = new Label("Total Amount: ₹" + String.format("%.2f", rental.getTotalAmount()));
        amount.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #16A34A;");

        details.getChildren().addAll(equipment, owner, duration, amount);

        Label methodTitle = new Label("Select Payment Method");
        methodTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ToggleGroup paymentGroup = new ToggleGroup();

        RadioButton upi = new RadioButton("📱 UPI");
        RadioButton card = new RadioButton("💳 Card");
        RadioButton banking = new RadioButton("🏦 Net Banking");

        upi.setToggleGroup(paymentGroup);
        card.setToggleGroup(paymentGroup);
        banking.setToggleGroup(paymentGroup);
        upi.setSelected(true);

        Button payButton = new Button("💳 Pay ₹" + String.format("%.2f", rental.getTotalAmount()));
        payButton.setMaxWidth(Double.MAX_VALUE);
        payButton.setStyle("-fx-background-color: #16A34A; -fx-text-fill: white; "
                + "-fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 12 20; "
                + "-fx-background-radius: 8; -fx-cursor: hand;");

        payButton.setOnAction(e -> {
            RadioButton selected = (RadioButton) paymentGroup.getSelectedToggle();

            String paymentMethod = "UPI";
            if (selected != null) {
                if (selected.getText().contains("Card")) {
                    paymentMethod = "CARD";
                } else if (selected.getText().contains("Banking")) {
                    paymentMethod = "NET_BANKING";
                }
            }

            payButton.setDisable(true);
            payButton.setText("Processing Payment...");

            boolean success = rentalController.completePayment(
                    rental.getRentalId(),
                    paymentMethod);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION,
                        "Payment Successful",
                        "Payment completed successfully!\n\n"
                                + "Amount: ₹" + String.format("%.2f", rental.getTotalAmount())
                                + "\nMethod: " + paymentMethod
                                + "\n\nYour rental is now ACTIVE.");

                if (onPaymentSuccess != null) {
                    onPaymentSuccess.run();
                }

                stage.close();

            } else {
                payButton.setDisable(false);
                payButton.setText("💳 Pay ₹" + String.format("%.2f", rental.getTotalAmount()));

                showAlert(Alert.AlertType.ERROR,
                        "Payment Failed",
                        "Unable to process payment. Please try again.");
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #334155; "
                + "-fx-font-size: 14px; -fx-padding: 10 20; "
                + "-fx-background-radius: 8; -fx-cursor: hand;");
        cancelButton.setOnAction(e -> stage.close());

        HBox buttonBox = new HBox(10, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        root.getChildren().addAll(
                title, details, methodTitle,
                upi, card, banking,
                payButton, buttonBox);

        stage.setScene(new Scene(root, 500, 500));
        stage.showAndWait();
    }

    private String safe(String value) {
        return value == null ? "N/A" : value;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
