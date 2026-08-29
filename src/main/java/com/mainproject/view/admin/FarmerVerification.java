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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Farmer Verification screen for the AgriLink admin panel.
 * Built the same way as UserManagement and ProductManagement: not an
 * Application, just a screen builder that swaps itself onto the shared
 * Stage, and knows how to jump to the other screens that already exist.
 */
public class FarmerVerification {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";
    private static final int PAGE_SIZE = 5;
    private static final int TOTAL_PAGES = 3;

    private final Stage stage;
    private final AdminDashboard dashboard;

    private BorderPane rootLayout;
    private TableView<FarmerApplication> table;
    private Label resultsLabel;
    private Button activeTabButton;
    private List<Button> pageButtons;

    private ObservableList<FarmerApplication> allApplications;
    private FilteredList<FarmerApplication> filteredApplications;

    // filter key used by the active tab: "Pending", "Approved", "Rejected" or "Information Required"
    private String currentStatusFilter = "Pending";
    private int currentPage = 1;
    private int pendingTotalCount = 15;

    public FarmerVerification(Stage stage, AdminDashboard dashboard) {
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
                "Feedback & Reviews", "Reports & Complaints", "Payment Management"
        };

        VBox navBox = new VBox(1);
        for (String item : navItems) {
            Button navButton = new Button(item);
            navButton.setPrefWidth(228);
            navButton.setAlignment(Pos.CENTER_LEFT);
            navButton.setPadding(new Insets(11, 20, 11, 20));
            navButton.setFont(Font.font("Segoe UI", 13));

            if (item.equals("Farmer Verification")) {
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

    // grows by one branch every time a new screen gets built - same pattern as the other screens
    private void handleNavClick(String pageName) {
        if (pageName.equals("Dashboard")) {
            dashboard.showDashboard();
        } else if (pageName.equals("User Management")) {
            new UserManagement(stage, dashboard).show();
        } else if (pageName.equals("Product Management")) {
            new ProductManagement(stage, dashboard).show();
        } else if (pageName.equals("Farmer Verification")) {
            // already here
        } else {
            showInfoAlert(pageName, "This section isn't built in this demo.");
        }
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
    // Top bar - just the hamburger and a page title, nothing else was in the screenshot
    // ------------------------------------------------------------------

    private HBox buildTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(16, 25, 16, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #eaeaea; -fx-border-width: 0 0 1 0;");

        Button menuButton = new Button("\u2630");
        menuButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16;");
        menuButton.setOnAction(e -> toggleSidebar());

        Label title = new Label("Farmer Verification");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        topBar.getChildren().addAll(menuButton, title);
        return topBar;
    }

    // ------------------------------------------------------------------
    // Center content: status tabs + table card
    // ------------------------------------------------------------------

    private VBox buildContent() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(20));

        HBox tabsBar = buildTabsBar();

        VBox tableCard = new VBox(12);
        tableCard.setPadding(new Insets(18));
        tableCard.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        table = buildApplicationsTable();
        loadSampleApplications();
        HBox paginationBar = buildPaginationBar();

        tableCard.getChildren().addAll(table, paginationBar);
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
                {"Pending", "Pending (15)"},
                {"Approved", "Approved (5,236)"},
                {"Rejected", "Rejected (146)"},
                {"Information Required", "Information Required (23)"}
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
        currentPage = 1;
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

    private TableView<FarmerApplication> buildApplicationsTable() {
        TableView<FarmerApplication> tv = new TableView<>();
        tv.setPrefHeight(340);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.setPlaceholder(new Label("No applications found for this tab."));

        TableColumn<FarmerApplication, String> farmerCol = new TableColumn<>("Farmer");
        farmerCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        farmerCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty || name == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Label avatar = new Label(name.substring(0, 1).toUpperCase());
                avatar.setMinSize(32, 32);
                avatar.setMaxSize(32, 32);
                avatar.setAlignment(Pos.CENTER);
                avatar.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white;"
                        + "-fx-background-radius: 16; -fx-font-weight: bold;");
                Label nameLabel = new Label(name);
                nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
                HBox box = new HBox(10, avatar, nameLabel);
                box.setAlignment(Pos.CENTER_LEFT);
                setGraphic(box);
                setText(null);
            }
        });

        TableColumn<FarmerApplication, String> farmCol = new TableColumn<>("Farm Details");
        farmCol.setCellValueFactory(new PropertyValueFactory<>("farmDetails"));

        TableColumn<FarmerApplication, String> docsCol = new TableColumn<>("Documents");
        docsCol.setCellValueFactory(new PropertyValueFactory<>("documents"));

        TableColumn<FarmerApplication, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<FarmerApplication, String> appliedCol = new TableColumn<>("Applied On");
        appliedCol.setCellValueFactory(new PropertyValueFactory<>("appliedOn"));

        TableColumn<FarmerApplication, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> actionCell());

        tv.getColumns().addAll(farmerCol, farmCol, docsCol, locationCol, appliedCol, actionCol);
        return tv;
    }

    private TableCell<FarmerApplication, Void> actionCell() {
        return new TableCell<>() {
            private final Button approveButton = new Button("Approve");
            private final Button rejectButton = new Button("Reject");
            private final HBox box = new HBox(8, approveButton, rejectButton);

            {
                approveButton.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white;"
                        + "-fx-background-radius: 6; -fx-font-size: 11;");
                rejectButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #c62828;"
                        + "-fx-border-color: #c62828; -fx-border-radius: 6; -fx-background-radius: 6; -fx-font-size: 11;");
                approveButton.setOnAction(e -> handleApprove(getTableRow().getItem()));
                rejectButton.setOnAction(e -> handleReject(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }
                FarmerApplication application = (FarmerApplication) getTableRow().getItem();
                boolean alreadyDecided = !application.getStatus().equals("Pending")
                        && !application.getStatus().equals("Information Required");
                approveButton.setDisable(alreadyDecided);
                rejectButton.setDisable(alreadyDecided);
                setGraphic(box);
            }
        };
    }

    // ---- pagination footer ----

    private HBox buildPaginationBar() {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(15, 0, 0, 0));

        resultsLabel = new Label();
        updateResultsLabel();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button prevButton = new Button("<");
        prevButton.setStyle("-fx-background-color: transparent;");
        prevButton.setOnAction(e -> handlePrevPage());

        pageButtons = new ArrayList<>();
        HBox pageNumberBox = new HBox(6);
        for (int i = 1; i <= TOTAL_PAGES; i++) {
            final int pageNumber = i;
            Button pageButton = new Button(String.valueOf(pageNumber));
            pageButton.setOnAction(e -> handlePageClick(pageNumber));
            pageButtons.add(pageButton);
            pageNumberBox.getChildren().add(pageButton);
        }

        Button nextButton = new Button(">");
        nextButton.setStyle("-fx-background-color: transparent;");
        nextButton.setOnAction(e -> handleNextPage());

        highlightPageButton(currentPage);

        HBox rightSide = new HBox(6, prevButton, pageNumberBox, nextButton);
        rightSide.setAlignment(Pos.CENTER_LEFT);

        bar.getChildren().addAll(resultsLabel, spacer, rightSide);
        return bar;
    }

    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            highlightPageButton(currentPage);
            updateResultsLabel();
        }
    }

    private void handleNextPage() {
        if (currentPage < TOTAL_PAGES) {
            currentPage++;
            highlightPageButton(currentPage);
            updateResultsLabel();
        }
    }

    private void handlePageClick(int page) {
        currentPage = page;
        highlightPageButton(page);
        updateResultsLabel();
    }

    private void highlightPageButton(int page) {
        for (Button b : pageButtons) {
            if (b.getText().equals(String.valueOf(page))) {
                b.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 6;");
            } else {
                b.setStyle("-fx-background-color: transparent; -fx-text-fill: #555555; -fx-background-radius: 6;");
            }
        }
    }

    // for the Pending tab this uses the mock total of 15 applications; the other tabs
    // just reflect how many sample rows actually matched, since there's no bigger
    // dataset behind them in this demo
    private void updateResultsLabel() {
        int shown = filteredApplications.size();
        if (currentStatusFilter.equals("Pending")) {
            int start = shown == 0 ? 0 : (currentPage - 1) * PAGE_SIZE + 1;
            int end = Math.min(currentPage * PAGE_SIZE, pendingTotalCount);
            resultsLabel.setText("Showing " + start + " to " + end + " of " + pendingTotalCount + " applications");
        } else {
            int start = shown == 0 ? 0 : 1;
            resultsLabel.setText("Showing " + start + " to " + shown + " of " + shown + " applications");
        }
    }

    // ------------------------------------------------------------------
    // Data loading + filtering
    // ------------------------------------------------------------------

    private void loadSampleApplications() {
        allApplications = FXCollections.observableArrayList(
                new FarmerApplication("APP1001", "Ramesh Patil", "5 Acre", "ID Proof, Land Doc, Photo", "Nashik, MH", LocalDate.of(2025, 5, 20), "Pending"),
                new FarmerApplication("APP1002", "Mahesh Jadhav", "3 Acre", "ID Proof, Land Doc, Photo", "Pune, MH", LocalDate.of(2025, 5, 19), "Pending"),
                new FarmerApplication("APP1003", "Suresh Yadav", "8 Acre", "ID Proof, Land Doc, Photo", "Solapur, MH", LocalDate.of(2025, 5, 18), "Pending"),
                new FarmerApplication("APP1004", "Anita Deshmukh", "2 Acre", "ID Proof, Land Doc", "Nagpur, MH", LocalDate.of(2025, 5, 17), "Pending"),
                new FarmerApplication("APP1005", "Vikram Singh", "6 Acre", "ID Proof, Land Doc, Photo", "Amravati, MH", LocalDate.of(2025, 5, 16), "Pending")
        );

        filteredApplications = new FilteredList<>(allApplications, application -> true);
        filteredApplications.addListener((ListChangeListener<FarmerApplication>) change -> updateResultsLabel());

        applyFilters();
        table.setItems(filteredApplications);
    }

    private void applyFilters() {
        filteredApplications.setPredicate(application -> application.getStatus().equals(currentStatusFilter));
    }

    // ------------------------------------------------------------------
    // Button actions: approve / reject
    // ------------------------------------------------------------------

    private void handleApprove(FarmerApplication application) {
        if (application == null) {
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Approve Application");
        confirm.setHeaderText(null);
        confirm.setContentText("Approve " + application.getName() + "'s farmer application?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            application.setStatus("Approved");
            pendingTotalCount--;
            applyFilters();
            showInfoAlert("Application Approved", application.getName() + " is now a verified farmer.");
        }
    }

    private void handleReject(FarmerApplication application) {
        if (application == null) {
            return;
        }
        TextInputDialog reasonDialog = new TextInputDialog();
        reasonDialog.setTitle("Reject Application");
        reasonDialog.setHeaderText(null);
        reasonDialog.setContentText("Reason for rejecting " + application.getName() + " (optional):");

        Optional<String> result = reasonDialog.showAndWait();
        if (result.isPresent()) {
            application.setStatus("Rejected");
            pendingTotalCount--;
            applyFilters();
            showInfoAlert("Application Rejected", application.getName() + "'s application was rejected.");
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

    public static class FarmerApplication {
        private final String id;
        private final String name;
        private final String farmDetails;
        private final String documents;
        private final String location;
        private final LocalDate appliedDate;
        private String status;

        public FarmerApplication(String id, String name, String farmDetails, String documents,
                                  String location, LocalDate appliedDate, String status) {
            this.id = id;
            this.name = name;
            this.farmDetails = farmDetails;
            this.documents = documents;
            this.location = location;
            this.appliedDate = appliedDate;
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getFarmDetails() {
            return farmDetails;
        }

        public String getDocuments() {
            return documents;
        }

        public String getLocation() {
            return location;
        }

        public LocalDate getAppliedDate() {
            return appliedDate;
        }

        public String getAppliedOn() {
            return appliedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}