package com.mainproject.view.admin;

import com.mainproject.dao.OrderDAO;
import com.mainproject.model.Order;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Admin payment management screen.
 * Payments are read directly from the existing orders collection; no fake
 * transactions are created and no existing buyer/farmer functionality is changed.
 */
public class PaymentManagement {

    private final Stage stage;
    private final AdminDashboard dashboard;
    private final OrderDAO orders = new OrderDAO();

    private TableView<Row> table;
    private final ObservableList<Row> data = FXCollections.observableArrayList();
    private TextField search;

    public PaymentManagement(Stage stage, AdminDashboard dashboard) {
        this.stage = stage;
        this.dashboard = dashboard;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + AdminCommon.BG + ";");
        root.setLeft(AdminCommon.sidebar(stage, dashboard, "Payment Management"));
        root.setTop(AdminCommon.topBar(
                "Payment Management",
                () -> AdminCommon.collapse(root),
                this::load
        ));
        root.setCenter(content());
        stage.getScene().setRoot(root);
        load();
    }

    private VBox content() {
        VBox content = new VBox(12);
        content.setPadding(new Insets(20));

        search = new TextField();
        search.setPromptText("Search order, buyer, payment method...");
        search.textProperty().addListener((obs, oldValue, newValue) -> filter());

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No payment records found."));

        TableColumn<Row, String> orderId = new TableColumn<>("Order ID");
        orderId.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getOrderId()));

        TableColumn<Row, String> buyer = new TableColumn<>("Buyer");
        buyer.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getBuyer()));

        TableColumn<Row, String> amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getAmount()));

        TableColumn<Row, String> method = new TableColumn<>("Method");
        method.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getMethod()));

        TableColumn<Row, String> status = new TableColumn<>("Status");
        status.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getStatus()));

        TableColumn<Row, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDate()));

        table.getColumns().addAll(orderId, buyer, amount, method, status, date);
        VBox.setVgrow(table, Priority.ALWAYS);

        Label note = new Label(
                "Payments are derived from the existing orders collection so no fake transactions are shown."
        );
        note.setStyle("-fx-text-fill:#777;");

        VBox card = new VBox(10, search, table, note);
        card.setPadding(new Insets(18));
        AdminCommon.card(card);
        VBox.setVgrow(card, Priority.ALWAYS);

        content.getChildren().add(card);
        VBox.setVgrow(content, Priority.ALWAYS);
        return content;
    }

    private void load() {
        data.clear();
        try {
            List<Order> orderList = orders.getAllOrders();
            for (Order order : orderList) {
                if (order != null) {
                    data.add(new Row(order));
                }
            }
        } catch (Exception e) {
            AdminCommon.error("Payment Load Failed", e.getMessage());
        }
        filter();
    }

    private void filter() {
        if (table == null || search == null) {
            return;
        }

        String query = search.getText() == null
                ? ""
                : search.getText().trim().toLowerCase(Locale.ROOT);

        ObservableList<Row> filtered = FXCollections.observableArrayList();
        for (Row row : data) {
            if (query.isEmpty()
                    || row.getOrderId().toLowerCase(Locale.ROOT).contains(query)
                    || row.getBuyer().toLowerCase(Locale.ROOT).contains(query)
                    || row.getMethod().toLowerCase(Locale.ROOT).contains(query)
                    || row.getStatus().toLowerCase(Locale.ROOT).contains(query)) {
                filtered.add(row);
            }
        }
        table.setItems(filtered);
    }

    /** Public row model with explicit getters for safe JavaFX table access. */
    public static class Row {
        private final String orderId;
        private final String buyer;
        private final String amount;
        private final String method;
        private final String status;
        private final String date;

        Row(Order order) {
            orderId = value(order.getOrderId(), "-");
            buyer = value(order.getBuyerName(), value(order.getBuyerEmail(), "-"));
            amount = "₹" + new DecimalFormat("0.00").format(order.getTotalAmount());
            method = value(order.getPaymentMethod(), "Not selected");
            status = value(order.getPaymentStatus(), "Pending");

            Date paymentDate = order.getPaymentDate();
            Date displayDate = paymentDate != null ? paymentDate : order.getOrderDate();
            date = displayDate == null
                    ? "-"
                    : new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(displayDate);
        }

        private static String value(String text, String fallback) {
            return text == null || text.trim().isEmpty() ? fallback : text.trim();
        }

        public String getOrderId() { return orderId; }
        public String getBuyer() { return buyer; }
        public String getAmount() { return amount; }
        public String getMethod() { return method; }
        public String getStatus() { return status; }
        public String getDate() { return date; }
    }
}
