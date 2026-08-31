package com.mainproject.view.buyer;

import com.mainproject.controller.OrderController;
import com.mainproject.controller.InvoiceController;
import com.mainproject.model.Order;
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

/** Payment screen for cart/checkout product orders. */
public class OrderPaymentScreen {

    private final Order order;
    private final OrderController orderController = new OrderController();
    private final InvoiceController invoiceController = new InvoiceController();
    private Runnable onPaymentSuccess;
    private Runnable onPaymentFailed;

    public OrderPaymentScreen(Order order) {
        this.order = order;
    }

    public void setOnPaymentSuccess(Runnable onPaymentSuccess) {
        this.onPaymentSuccess = onPaymentSuccess;
    }

    public void setOnPaymentFailed(Runnable onPaymentFailed) {
        this.onPaymentFailed = onPaymentFailed;
    }

    public void show() {
        if (order == null || isBlank(order.getOrderId())) {
            alert(Alert.AlertType.ERROR, "Payment Error", "Order details are unavailable.");
            runFailed();
            return;
        }

        if (order.getTotalAmount() <= 0) {
            alert(Alert.AlertType.ERROR, "Payment Error", "Payment amount must be greater than zero.");
            runFailed();
            return;
        }

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Razorpay Product Payment");

        VBox root = new VBox(16);
        root.setPadding(new Insets(28));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:#F8FAFC;");

        Label title = new Label("💳 Complete Product Payment");
        title.setStyle("-fx-font-size:22px;-fx-font-weight:bold;");

        Label details = new Label(
                "Order ID: " + order.getOrderId()
                        + "\nItems: " + (order.getItems() == null ? 0 : order.getItems().size())
                        + "\nAmount: ₹" + String.format("%.2f", order.getTotalAmount())
        );
        details.setStyle("-fx-font-size:16px;");
        details.setAlignment(Pos.CENTER);

        Label info = new Label(
                "Secure Razorpay Checkout will open in your browser. "
                        + "UPI, cards and net banking are available there."
        );
        info.setWrapText(true);
        info.setStyle("-fx-text-fill:#64748B;");

        Button pay = new Button("Pay ₹" + String.format("%.2f", order.getTotalAmount()));
        pay.setStyle("-fx-background-color:#117864;-fx-text-fill:white;"
                + "-fx-font-weight:bold;-fx-padding:12 28;-fx-background-radius:8;");

        pay.setOnAction(e -> {
            pay.setDisable(true);
            pay.setText("Opening Razorpay...");

            new RazorpayPaymentService()
                    .pay(
                            order.getTotalAmount(),
                            "order_" + order.getOrderId(),
                            "AgriVerse product order " + order.getOrderId()
                    )
                    .thenAccept(result -> Platform.runLater(
                            () -> handleResult(result, pay, stage)
                    ));
        });

        stage.setOnCloseRequest(e -> runFailed());
        root.getChildren().addAll(title, details, info, pay);
        stage.setScene(new Scene(root, 520, 350));
        stage.showAndWait();
    }

    private void handleResult(
            RazorpayPaymentResult result,
            Button pay,
            Stage stage) {

        if (result != null && result.isSuccess()
                && orderController.completePayment(
                        order.getOrderId(),
                        result.getPaymentId(),
                        result.getRazorpayOrderId(),
                        "RAZORPAY"
                )) {

            alert(Alert.AlertType.INFORMATION,
                    "Payment Successful",
                    "Your verified Razorpay payment was recorded successfully. Your invoice will be sent to your email.");

            invoiceController.sendOrderInvoiceAsync(order);

            if (onPaymentSuccess != null) {
                onPaymentSuccess.run();
            }
            onPaymentFailed = null;
            stage.close();
            return;
        }

        pay.setDisable(false);
        pay.setText("Retry Payment");
        String error = result == null ? "Unable to complete payment." : result.getError();
        alert(Alert.AlertType.ERROR,
                "Payment Failed",
                error == null ? "Unable to save the verified payment." : error);
        runFailed();
    }

    private void runFailed() {
        if (onPaymentFailed != null) {
            Runnable callback = onPaymentFailed;
            onPaymentFailed = null;
            callback.run();
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void alert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
