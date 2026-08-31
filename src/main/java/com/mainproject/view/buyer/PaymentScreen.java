package com.mainproject.view.buyer;

import com.mainproject.controller.EquipmentRentalController;
import com.mainproject.controller.InvoiceController;
import com.mainproject.model.EquipmentRental;
import com.mainproject.payment.RazorpayPaymentResult;
import com.mainproject.payment.RazorpayPaymentService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PaymentScreen {
    private final EquipmentRental rental;
    private final EquipmentRentalController rentalController = new EquipmentRentalController();
    private Runnable onPaymentSuccess;

    public PaymentScreen(EquipmentRental rental) { this.rental = rental; }
    public void setOnPaymentSuccess(Runnable onPaymentSuccess) { this.onPaymentSuccess = onPaymentSuccess; }

    public void show() {
        if (rental == null || rental.getRentalId() == null || rental.getRentalId().isBlank()) {
            alert(Alert.AlertType.ERROR, "Payment Error", "Rental details are unavailable.");
            return;
        }
        if ("paid".equalsIgnoreCase(rental.getPaymentStatus())) {
            alert(Alert.AlertType.INFORMATION, "Already Paid", "This rental has already been paid.");
            return;
        }
        if (rental.getTotalAmount() <= 0) {
            alert(Alert.AlertType.ERROR, "Payment Error", "Payment amount must be greater than zero.");
            return;
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Razorpay Payment");
        VBox root = new VBox(16); root.setPadding(new Insets(28)); root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:#F8FAFC;");
        Label title = new Label("💳 Pay securely with Razorpay"); title.setStyle("-fx-font-size:22px;-fx-font-weight:bold;");
        Label details = new Label("🚜 " + safe(rental.getEquipmentName()) + "\nAmount: ₹" + String.format("%.2f", rental.getTotalAmount()));
        details.setStyle("-fx-font-size:16px;"); details.setAlignment(Pos.CENTER); details.setWrapText(true);
        Label info = new Label("Razorpay Checkout will open in your browser. UPI, cards and net banking are available there.");
        info.setWrapText(true); info.setStyle("-fx-text-fill:#64748B;");
        Button pay = new Button("Pay ₹" + String.format("%.2f", rental.getTotalAmount()));
        pay.setStyle("-fx-background-color:#16A34A;-fx-text-fill:white;-fx-font-weight:bold;-fx-padding:12 28;-fx-background-radius:8;");
        pay.setOnAction(e -> {
            pay.setDisable(true); pay.setText("Opening Razorpay...");
            new RazorpayPaymentService().pay(rental.getTotalAmount(), "rental_" + rental.getRentalId(), "Equipment rental: " + safe(rental.getEquipmentName()))
                    .thenAccept(result -> Platform.runLater(() -> handleResult(result, pay, stage)));
        });
        root.getChildren().addAll(title, details, info, pay);
        stage.setScene(new Scene(root, 480, 330)); stage.showAndWait();
    }

    private void handleResult(RazorpayPaymentResult result, Button pay, Stage stage) {
        if (result.isSuccess() && rentalController.completePayment(rental.getRentalId(), result.getPaymentId(), result.getRazorpayOrderId(), "RAZORPAY")) {
            new InvoiceController().sendRentalInvoiceAsync(rental);
            alert(Alert.AlertType.INFORMATION, "Payment Successful", "Your verified Razorpay payment was recorded successfully. Your invoice is being sent to your email.");
            if (onPaymentSuccess != null) onPaymentSuccess.run();
            stage.close();
        } else {
            pay.setDisable(false); pay.setText("Retry Payment");
            alert(Alert.AlertType.ERROR, "Payment Failed", result.getError() == null ? "Unable to save the verified payment." : result.getError());
        }
    }
    private String safe(String s){ return s == null ? "Equipment" : s; }
    private void alert(Alert.AlertType t,String title,String msg){ Alert a=new Alert(t,msg);a.setTitle(title);a.setHeaderText(null);a.showAndWait(); }
}
