package com.mainproject.view.admin;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

/**
 * Live Marketplace screen for the AgriLink admin panel.
 * Same pattern as the other screens - builds itself onto the shared Stage
 * and knows how to jump to every other screen that already exists.
 */
public class LiveMarketplace {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";
    private static final DateTimeFormatter STARTED_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");

    private final Stage stage;
    private final AdminDashboard dashboard;
    private final Random random = new Random();

    private BorderPane rootLayout;
    private TableView<LiveStream> table;
    private Label resultsLabel;
    private Button activeTabButton;

    private ObservableList<LiveStream> allStreams;
    private FilteredList<LiveStream> filteredStreams;

    // filter key of the active tab: "Live Now", "Upcoming", "Ended" or "Reported"
    private String currentStatusFilter = "Live Now";

    public LiveMarketplace(Stage stage, AdminDashboard dashboard) {
        this.stage = stage;
        this.dashboard = dashboard;
    }

    public void show() {
        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + BG + ";");
        rootLayout.setLeft(buildSidebar());
        rootLayout.setTop(buildTopBar());
        rootLayout.setCenter(buildContent());
        stage.getScene().setRoot(rootLayout);
    }

    // ------------------------------------------------------------------
    // Sidebar
    // ------------------------------------------------------------------

    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(230);
        sidebar.setStyle("-fx-background-color: " + GREEN_DARK + ";");

        VBox logoBox = new VBox(2);
        logoBox.setPadding(new Insets(22, 15, 22, 20));
        Label logo = new Label("\uD83C\uDF3F  AgriLink");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        Label subtitle = new Label("Admin Dashboard");
        subtitle.setTextFill(Color.web("#c8e6c9"));
        subtitle.setFont(Font.font("Segoe UI", 12));
        logoBox.getChildren().addAll(logo, subtitle);

        String[] navItems = {
                "Dashboard", "User Management", "Farmer Verification",
                "Product Management", "Order Management", "Live Marketplace",
                "Equipment Management", "Analytics & Reports", "Crop Price Management",
                "AI & Smart Tools", "Notifications", "Content Management",
                "Feedback & Reviews", "Reports & Complaints", "Payment Management",
                "Audit Logs", "System & Data Management"
        };

        VBox navBox = new VBox(1);
        for (String item : navItems) {
            Button navButton = new Button(item);
            navButton.setPrefWidth(228);
            navButton.setAlignment(Pos.CENTER_LEFT);
            navButton.setPadding(new Insets(11, 20, 11, 20));
            navButton.setFont(Font.font("Segoe UI", 13));

            if (item.equals("Live Marketplace")) {
                styleActiveNav(navButton);
            } else {
                styleInactiveNav(navButton);
            }

            navButton.setOnAction(e -> handleNavClick(item));
            navBox.getChildren().add(navButton);
        }

        ScrollPane scrollPane = new ScrollPane(navBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: " + GREEN_DARK + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        sidebar.getChildren().addAll(logoBox, scrollPane);
        return sidebar;
    }

    // grows by one branch every time a new screen gets built - same pattern as the
    // other screens
    private void handleNavClick(String pageName) {
        if (pageName.equals("Live Marketplace")) {
            return; // already on this screen
        }
        if (pageName.equals("Dashboard")) {
            dashboard.showDashboard();
            return;
        }
        if (pageName.equals("User Management")) {
            new UserManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Farmer Verification")) {
            new FarmerVerification(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Product Management")) {
            new ProductManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Order Management")) {
            new OrderManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Equipment Management")) {
            new EquipmentManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Analytics & Reports")) {
            new AnalyticsReports(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Notifications")) {
            new NotificationManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Content Management")) {
            new ContentManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Feedback & Reviews")) {
            new FeedbackReviews(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Reports & Complaints")) {
            new ReportsComplaints(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Payment Management")) {
            new PaymentManagement(stage, dashboard).show();
            return;
        }
        if (pageName.equals("Audit Logs")) {
            new AuditLogs(stage, dashboard).show();
            return;
        }
        if (pageName.equals("System & Data Management")) {
            new SystemDataManage(stage, dashboard).show();
            return;
        }
        showInfoAlert(pageName, "This section hasn't been built yet in this demo.");
    }

    private void styleActiveNav(Button b) {
        b.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 0;");
    }

    private void styleInactiveNav(Button b) {
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: #d7e4d9; -fx-background-radius: 0;");
    }

    private void toggleSidebar() {
        Node sidebar = rootLayout.getLeft();
        if (sidebar != null) {
            sidebar.setVisible(!sidebar.isVisible());
            sidebar.setManaged(sidebar.isVisible());
        }
    }

    // ------------------------------------------------------------------
    // Top bar
    // ------------------------------------------------------------------

    private HBox buildTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(16, 25, 16, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #eaeaea; -fx-border-width: 0 0 1 0;");

        Button menuButton = new Button("\u2630");
        menuButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16;");
        menuButton.setOnAction(e -> toggleSidebar());

        Label title = new Label("Live Marketplace");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        topBar.getChildren().addAll(menuButton, title);
        return topBar;
    }

    // ------------------------------------------------------------------
    // Center content
    // ------------------------------------------------------------------

    private VBox buildContent() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(20));

        HBox tabsBar = buildTabsBar();

        VBox tableCard = new VBox(12);
        tableCard.setPadding(new Insets(18));
        tableCard.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        table = buildStreamsTable();
        loadSampleStreams();

        resultsLabel = new Label();
        updateResultsLabel();

        tableCard.getChildren().addAll(table, resultsLabel);
        content.getChildren().addAll(tabsBar, tableCard);
        return content;
    }

    private HBox buildTabsBar() {
        HBox bar = new HBox(8);
        bar.setPadding(new Insets(10, 15, 10, 15));
        bar.setStyle("-fx-background-color: white; -fx-background-radius: 10;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 6, 0, 0, 1);");

        // {filter key, label shown on the button}
        String[][] tabs = {
                { "Live Now", "Live Now (18)" },
                { "Upcoming", "Upcoming (8)" },
                { "Ended", "Ended (120)" },
                { "Reported", "Reported (7)" }
        };

        for (String[] tab : tabs) {
            String filterKey = tab[0];
            Button tabButton = new Button(tab[1]);
            tabButton.setPadding(new Insets(8, 18, 8, 18));
            tabButton.setFont(Font.font("Segoe UI", 13));

            if (filterKey.equals(currentStatusFilter)) {
                activeTabButton = tabButton;
                styleActiveTab(tabButton);
            } else {
                styleInactiveTab(tabButton);
            }

            tabButton.setOnAction(e -> handleTabClick(filterKey, tabButton));
            bar.getChildren().add(tabButton);
        }
        return bar;
    }

    private void handleTabClick(String filterKey, Button clickedTab) {
        currentStatusFilter = filterKey;
        styleInactiveTab(activeTabButton);
        styleActiveTab(clickedTab);
        activeTabButton = clickedTab;
        applyFilters();
    }

    private void styleActiveTab(Button b) {
        b.setStyle("-fx-background-color: #eaf6ec; -fx-text-fill: " + GREEN + ";"
                + "-fx-background-radius: 8; -fx-font-weight: bold;");
    }

    private void styleInactiveTab(Button b) {
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: #666666; -fx-background-radius: 8;");
    }

    // ---- table ----

    private TableView<LiveStream> buildStreamsTable() {
        TableView<LiveStream> tv = new TableView<>();
        tv.setPrefHeight(340);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.setPlaceholder(new Label("No streams found for this tab."));

        TableColumn<LiveStream, String> farmerCol = new TableColumn<>("Farmer");
        farmerCol.setCellValueFactory(new PropertyValueFactory<>("farmerName"));
        farmerCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String farmerName, boolean empty) {
                super.updateItem(farmerName, empty);
                if (empty || farmerName == null || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                LiveStream stream = (LiveStream) getTableRow().getItem();

                Label liveBadge = new Label(
                        stream.getStatus().equals("Live Now") ? "LIVE" : stream.getStatus().toUpperCase());
                liveBadge.setStyle("-fx-background-color: #c62828; -fx-text-fill: white; -fx-font-size: 9;"
                        + "-fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 2 5 2 5;");

                Label thumbnail = new Label("\uD83C\uDF3E");
                thumbnail.setMinSize(42, 42);
                thumbnail.setMaxSize(42, 42);
                thumbnail.setAlignment(Pos.CENTER);
                thumbnail.setStyle("-fx-background-color: #eaf6ec; -fx-background-radius: 8; -fx-font-size: 18;");

                Label nameLabel = new Label(farmerName);
                nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

                HBox box = new HBox(8, liveBadge, thumbnail, nameLabel);
                box.setAlignment(Pos.CENTER_LEFT);
                setGraphic(box);
                setText(null);
            }
        });

        TableColumn<LiveStream, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String title, boolean empty) {
                super.updateItem(title, empty);
                if (empty || title == null) {
                    setText(null);
                } else {
                    setText(title);
                    setStyle("-fx-font-weight: bold;");
                }
            }
        });

        TableColumn<LiveStream, Number> viewersCol = new TableColumn<>("Viewers");
        viewersCol.setCellValueFactory(new PropertyValueFactory<>("viewers"));

        TableColumn<LiveStream, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<LiveStream, String> startedCol = new TableColumn<>("Started On");
        startedCol.setCellValueFactory(new PropertyValueFactory<>("startedOnDisplay"));

        TableColumn<LiveStream, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> actionCell());

        tv.getColumns().addAll(farmerCol, titleCol, viewersCol, locationCol, startedCol, actionCol);
        return tv;
    }

    private TableCell<LiveStream, Void> actionCell() {
        return new TableCell<>() {
            private final Button monitorButton = new Button("Monitor");
            private final Button endButton = new Button("End");
            private final HBox box = new HBox(8, monitorButton, endButton);

            {
                monitorButton.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white;"
                        + "-fx-background-radius: 6; -fx-font-size: 11;");
                endButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #c62828;"
                        + "-fx-border-color: #c62828; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-size: 11;");
                monitorButton.setOnAction(e -> handleMonitor(getTableRow().getItem()));
                endButton.setOnAction(e -> handleEndStream(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }
                LiveStream stream = (LiveStream) getTableRow().getItem();
                boolean isLive = stream.getStatus().equals("Live Now");
                monitorButton.setDisable(!isLive);
                endButton.setDisable(!isLive);
                setGraphic(box);
            }
        };
    }

    private void updateResultsLabel() {
        resultsLabel.setText("Showing " + filteredStreams.size() + " streams");
        resultsLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12;");
    }

    // ------------------------------------------------------------------
    // Data loading + filtering
    // ------------------------------------------------------------------

    private void loadSampleStreams() {
        allStreams = FXCollections.observableArrayList(
                new LiveStream("STR1001", "Ramesh Patil", "Fresh Tomato Harvest", 126, "Nashik, MH",
                        LocalDateTime.of(2025, 5, 29, 10, 30), "Live Now"),
                new LiveStream("STR1002", "Mahesh Jadhav", "Potato Farming Live", 84, "Pune, MH",
                        LocalDateTime.of(2025, 5, 29, 10, 15), "Live Now"),
                new LiveStream("STR1003", "Suresh Yadav", "Onion Harvesting", 62, "Solapur, MH",
                        LocalDateTime.of(2025, 5, 29, 9, 45), "Live Now"),
                new LiveStream("STR1004", "Anita Deshmukh", "Mango Farm Live", 98, "Nagpur, MH",
                        LocalDateTime.of(2025, 5, 29, 9, 30), "Live Now"),
                new LiveStream("STR1005", "Vikram Singh", "Wheat Harvesting", 45, "Amravati, MH",
                        LocalDateTime.of(2025, 5, 29, 9, 10), "Live Now"));

        filteredStreams = new FilteredList<>(allStreams, stream -> true);
        filteredStreams.addListener((ListChangeListener<LiveStream>) change -> updateResultsLabel());

        applyFilters();
        table.setItems(filteredStreams);
    }

    private void applyFilters() {
        filteredStreams.setPredicate(stream -> stream.getStatus().equals(currentStatusFilter));
    }

    // ------------------------------------------------------------------
    // Button actions: monitor / end
    // ------------------------------------------------------------------

    private void handleMonitor(LiveStream stream) {
        if (stream == null) {
            return;
        }
        // pretend a few more people just joined while the admin is watching
        int newViewers = 5 + random.nextInt(11);
        stream.setViewers(stream.getViewers() + newViewers);
        table.refresh();

        String message = "Now monitoring " + stream.getFarmerName() + "'s stream \"" + stream.getTitle() + "\"\n"
                + "Location: " + stream.getLocation() + "\n"
                + "Viewers right now: " + stream.getViewers();
        showInfoAlert("Monitoring Stream", message);
    }

    private void handleEndStream(LiveStream stream) {
        if (stream == null) {
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("End Stream");
        confirm.setHeaderText(null);
        confirm.setContentText("End " + stream.getFarmerName() + "'s live stream \"" + stream.getTitle() + "\"?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stream.setStatus("Ended");
            applyFilters();
            showInfoAlert("Stream Ended", "The stream has been ended and moved out of Live Now.");
        }
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ------------------------------------------------------------------
    // Simple data model for a table row
    // ------------------------------------------------------------------

    public static class LiveStream {
        private final String id;
        private final String farmerName;
        private final String title;
        private int viewers;
        private final String location;
        private final LocalDateTime startedOn;
        private String status;

        public LiveStream(String id, String farmerName, String title, int viewers,
                String location, LocalDateTime startedOn, String status) {
            this.id = id;
            this.farmerName = farmerName;
            this.title = title;
            this.viewers = viewers;
            this.location = location;
            this.startedOn = startedOn;
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public String getFarmerName() {
            return farmerName;
        }

        public String getTitle() {
            return title;
        }

        public int getViewers() {
            return viewers;
        }

        public void setViewers(int viewers) {
            this.viewers = viewers;
        }

        public String getLocation() {
            return location;
        }

        public LocalDateTime getStartedOn() {
            return startedOn;
        }

        public String getStartedOnDisplay() {
            return startedOn.format(STARTED_FORMAT);
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}