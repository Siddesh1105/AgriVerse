package com.mainproject.view.buyer;

import com.mainproject.controller.ProductOrderController;
import com.mainproject.model.ProductOrder;
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

public class ProductPaymentScreen {
    private final ProductOrder order;
    private final ProductOrderController controller = new ProductOrderController();
    public ProductPaymentScreen(ProductOrder order) { this.order = order; }

    public void show() {
        if (order == null || order.getOrderId() == null || order.getOrderId().isBlank()) {
            alert(Alert.AlertType.ERROR, "Payment Error", "Order details are unavailable.");
            return;
        }
        if ("paid".equalsIgnoreCase(order.getPaymentStatus())) {
            alert(Alert.AlertType.INFORMATION, "Already Paid", "This order has already been paid.");
            return;
        }
        if (order.getTotalAmount() <= 0) {
            alert(Alert.AlertType.ERROR, "Payment Error", "Payment amount must be greater than zero.");
            return;
        }
        Stage stage=new Stage(); stage.initModality(Modality.APPLICATION_MODAL); stage.setTitle("Razorpay Product Payment");
        VBox root=new VBox(16); root.setPadding(new Insets(28)); root.setAlignment(Pos.CENTER); root.setStyle("-fx-background-color:#F8FAFC;");
        Label title=new Label("💳 Pay for your order"); title.setStyle("-fx-font-size:22px;-fx-font-weight:bold;");
        Label details=new Label("🛒 " + safe(order.getProductName()) + "\nAmount: ₹" + String.format("%.2f",order.getTotalAmount())); details.setStyle("-fx-font-size:16px;");
        Label info=new Label("Secure Razorpay Checkout will open in your browser."); info.setStyle("-fx-text-fill:#64748B;");
        Button pay=new Button("Pay ₹"+String.format("%.2f",order.getTotalAmount())); pay.setStyle("-fx-background-color:#16A34A;-fx-text-fill:white;-fx-font-weight:bold;-fx-padding:12 28;-fx-background-radius:8;");
        pay.setOnAction(e->{ pay.setDisable(true); pay.setText("Opening Razorpay..."); new RazorpayPaymentService().pay(order.getTotalAmount(),"product_"+order.getOrderId(),"Product order: "+safe(order.getProductName())).thenAccept(r->Platform.runLater(()->handle(r,pay,stage))); });
        root.getChildren().addAll(title,details,info,pay); stage.setScene(new Scene(root,460,300)); stage.showAndWait();
    }
    private void handle(RazorpayPaymentResult r,Button pay,Stage stage){
        if(r.isSuccess() && controller.completePayment(order.getOrderId(),r.getPaymentId(),r.getRazorpayOrderId(),"RAZORPAY")){ alert(Alert.AlertType.INFORMATION,"Payment Successful","Your verified payment was recorded successfully."); stage.close(); }
        else { pay.setDisable(false); pay.setText("Retry Payment"); alert(Alert.AlertType.ERROR,"Payment Failed",r.getError()==null?"Unable to save payment.":r.getError()); }
    }
    private String safe(String s){return s==null?"Product":s;}
    private void alert(Alert.AlertType t,String title,String msg){Alert a=new Alert(t,msg);a.setTitle(title);a.setHeaderText(null);a.showAndWait();}
}
