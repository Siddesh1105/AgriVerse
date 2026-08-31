package com.mainproject.view.farmer;

import com.mainproject.controller.EquipmentRentalController;
import com.mainproject.model.EquipmentRental;
import com.mainproject.view.buyer.PaymentScreen;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.util.List;

/** Shows rental requests made by the logged-in farmer and allows payment after acceptance. */
public class MyRentalRequests {
    private final String farmerEmail;
    private final EquipmentRentalController controller = new EquipmentRentalController();
    private VBox list;

    public MyRentalRequests(String farmerEmail) { this.farmerEmail = farmerEmail; }

    public Node getView() {
        VBox root = new VBox(16); root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color:#F8FFFB;");
        HBox header = new HBox(); header.setAlignment(Pos.CENTER_LEFT);
        VBox titles = new VBox(4);
        Label title = new Label("🚜 My Rental Requests"); title.setStyle("-fx-font-size:26px;-fx-font-weight:bold;-fx-text-fill:#1B2631;");
        Label sub = new Label("Track equipment you requested from other farmers and pay after acceptance."); sub.setStyle("-fx-text-fill:#64748B;");
        titles.getChildren().addAll(title, sub);
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        Button refresh = new Button("↻ Refresh"); refresh.setStyle(buttonStyle()); refresh.setOnAction(e -> load());
        header.getChildren().addAll(titles, spacer, refresh);
        list = new VBox(12); list.setPadding(new Insets(4,0,12,0));
        ScrollPane scroll = new ScrollPane(list); scroll.setFitToWidth(true); scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color:transparent;-fx-background:transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);
        root.getChildren().addAll(header, scroll);
        load(); return root;
    }

    private void load() {
        if (list == null) return;
        list.getChildren().setAll(new Label("Loading your rental requests..."));
        Thread t = new Thread(() -> {
            List<EquipmentRental> rentals = controller.getRentalsByBuyer(farmerEmail);
            Platform.runLater(() -> render(rentals));
        }); t.setDaemon(true); t.start();
    }

    private void render(List<EquipmentRental> rentals) {
        list.getChildren().clear();
        if (rentals == null || rentals.isEmpty()) {
            Label empty = new Label("No equipment rental requests found."); empty.setStyle("-fx-font-size:16px;-fx-text-fill:#64748B;"); list.getChildren().add(empty); return;
        }
        for (EquipmentRental r : rentals) list.getChildren().add(card(r));
    }

    private Node card(EquipmentRental r) {
        VBox card = new VBox(10); card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color:white;-fx-background-radius:12;-fx-border-color:#D5E8E1;-fx-border-radius:12;");
        HBox top = new HBox(10); top.setAlignment(Pos.CENTER_LEFT);
        VBox names = new VBox(4);
        Label equipment = new Label("🚜 " + safe(r.getEquipmentName(), "Equipment")); equipment.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        Label owner = new Label("Owner: " + safe(r.getEquipmentOwnerName(), safe(r.getEquipmentOwnerEmail(), "Farmer"))); owner.setStyle("-fx-text-fill:#64748B;");
        names.getChildren().addAll(equipment, owner);
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        String status = safe(r.getStatus(), "pending").toUpperCase();
        Label badge = new Label(status); badge.setStyle(badgeStyle(r.getStatus()));
        top.getChildren().addAll(names, spacer, badge);
        String dates = "Rental: " + date(r.getStartDate()) + " → " + date(r.getEndDate()) + "   |   " + Math.max(1,r.getNumberOfDays()) + " day(s)";
        Label details = new Label(dates); details.setStyle("-fx-text-fill:#475569;");
        Label amount = new Label("Total Amount: ₹" + String.format("%.2f", r.getTotalAmount())); amount.setStyle("-fx-font-size:16px;-fx-font-weight:bold;-fx-text-fill:#117864;");
        card.getChildren().addAll(top, details, amount);
        String payment = safe(r.getPaymentStatus(), "pending");
        if ("accepted".equalsIgnoreCase(r.getStatus()) && !"paid".equalsIgnoreCase(payment)) {
            Label msg = new Label("🎉 Accepted! Complete payment to activate this equipment rental."); msg.setStyle("-fx-text-fill:#166534;");
            Button pay = new Button("💳 Pay Now ₹" + String.format("%.2f", r.getTotalAmount())); pay.setStyle(buttonStyle());
            pay.setOnAction(e -> { PaymentScreen screen = new PaymentScreen(r); screen.setOnPaymentSuccess(this::load); screen.show(); });
            card.getChildren().addAll(msg, pay);
        } else if ("paid".equalsIgnoreCase(payment)) {
            Label paid = new Label("✓ Payment Successful — " + safe(r.getPaymentMethod(), "RAZORPAY")); paid.setStyle("-fx-text-fill:#16A34A;-fx-font-weight:bold;"); card.getChildren().add(paid);
        }
        return card;
    }

    private String date(java.util.Date d){ return d==null?"-":new SimpleDateFormat("dd MMM yyyy").format(d); }
    private String safe(String v,String fallback){ return v==null||v.trim().isEmpty()?fallback:v.trim(); }
    private String buttonStyle(){ return "-fx-background-color:#117864;-fx-text-fill:white;-fx-font-weight:bold;-fx-padding:10 18;-fx-background-radius:8;-fx-cursor:hand;"; }
    private String badgeStyle(String s){ String v=safe(s,"pending").toLowerCase(); String c="accepted".equals(v)||"active".equals(v)?"#DCFCE7;-fx-text-fill:#15803D;":"rejected".equals(v)?"#FEE2E2;-fx-text-fill:#B91C1C;":"#FEF3C7;-fx-text-fill:#B45309;"; return "-fx-background-color:"+c+"-fx-font-weight:bold;-fx-padding:6 12;-fx-background-radius:20;"; }
}
