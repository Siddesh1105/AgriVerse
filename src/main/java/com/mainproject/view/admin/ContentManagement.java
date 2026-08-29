package com.mainproject.view.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Optional;

/**
 * Content Management screen for the AgriLink admin app.
 *
 * Same green sidebar / top bar shell as the other screens so it feels like
 * one app. Center content lets the admin publish homepage banners /
 * announcements and manage a list of static content blocks (About Us, FAQs,
 * Terms & Conditions, etc.) - the kind of thing a "Content Management"
 * section in an admin panel typically covers.
 *
 * Pattern follows the other screen classes (NotificationManagement,
 * UserManagement, ProductManagement...): built with the shared Stage and a
 * reference back to AdminDashboard so the sidebar can jump between screens.
 */
public class ContentManagement {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";

    private final Stage primaryStage;
    private final AdminDashboard dashboard;

    private BorderPane rootLayout;
    private Button activeNavButton;

    private ComboBox<String> pageBox;
    private TextField titleField;
    private TextArea bodyArea;
    private TableView<ContentRow> table;
    private ObservableList<ContentRow> contentBlocks;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public ContentManagement(Stage primaryStage, AdminDashboard dashboard) {
        this.primaryStage = primaryStage;
        this.dashboard = dashboard;
    }

    // entry point called from AdminDashboard's sidebar click
    public void show() {
        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + BG + ";");

        rootLayout.setLeft(buildSidebar());
        rootLayout.setTop(buildTopBar());
        rootLayout.setCenter(buildContent());

        primaryStage.getScene().setRoot(rootLayout);
    }

    // ------------------------------------------------------------------
    // Sidebar (mirrors AdminDashboard so navigation feels seamless)
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
            Button navButton = buildNavButton(item);
            navBox.getChildren().add(navButton);
            if (item.equals("Content Management")) {
                activeNavButton = navButton;
                styleActiveButton(navButton);
            }
        }

        ScrollPane scrollPane = new ScrollPane(navBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: " + GREEN_DARK + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        sidebar.getChildren().addAll(logoBox, scrollPane);
        return sidebar;
    }

    private Button buildNavButton(String label) {
        Button button = new Button(label);
        button.setPrefWidth(228);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(11, 20, 11, 20));
        button.setFont(Font.font("Segoe UI", 13));
        styleInactiveButton(button);
        button.setOnAction(e -> handleNavClick(label));
        return button;
    }

    private void styleActiveButton(Button button) {
        button.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 0;");
    }

    private void styleInactiveButton(Button button) {
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #d7e4d9; -fx-background-radius: 0;");
    }

    // routes sidebar clicks - every screen that has been built hands off to
    // its own class, the two not-yet-built items just say so
    private void handleNavClick(String pageName) {
        if (pageName.equals("Content Management")) {
            return; // already on this screen
        }
        if (pageName.equals("Dashboard")) {
            dashboard.showDashboard();
            return;
        }
        if (pageName.equals("User Management")) {
            new UserManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Farmer Verification")) {
            new FarmerVerification(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Product Management")) {
            new ProductManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Order Management")) {
            new OrderManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Live Marketplace")) {
            new LiveMarketplace(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Equipment Management")) {
            new EquipmentManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Analytics & Reports")) {
            new AnalyticsReports(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Notifications")) {
            new NotificationManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Feedback & Reviews")) {
            new FeedbackReviews(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Reports & Complaints")) {
            new ReportsComplaints(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Payment Management")) {
            new PaymentManagement(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("Audit Logs")) {
            new AuditLogs(primaryStage, dashboard).show();
            return;
        }
        if (pageName.equals("System & Data Management")) {
            new SystemDataManage(primaryStage, dashboard).show();
            return;
        }
        showInfoAlert(pageName, "This section hasn't been built yet in this demo.");
    }

    // ------------------------------------------------------------------
    // Top bar (same look/behaviour as the other screens')
    // ------------------------------------------------------------------

    private HBox buildTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(14, 25, 14, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #eaeaea; -fx-border-width: 0 0 1 0;");

        Button menuButton = new Button("\u2630");
        menuButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16;");
        menuButton.setOnAction(e -> toggleSidebar());

        TextField searchField = new TextField();
        searchField.setPromptText("Search by users, orders, products...");
        searchField.setPrefWidth(420);
        searchField.setStyle("-fx-background-color: transparent;");
        searchField.setOnAction(e -> handleSearch(searchField.getText()));

        HBox searchBox = new HBox(8, new Label("\uD83D\uDD0D"), searchField);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(6, 15, 6, 15));
        searchBox.setStyle("-fx-background-color: #f3f4f6; -fx-background-radius: 20;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button bellButton = new Button("\uD83D\uDD14");
        bellButton.setStyle("-fx-background-color: transparent; -fx-font-size: 15;");
        bellButton.setOnAction(
                e -> showInfoAlert("Notifications", "You have 42 pending approvals and 3 new orders today."));

        Button messageButton = new Button("\uD83D\uDCAC");
        messageButton.setStyle("-fx-background-color: transparent; -fx-font-size: 15;");
        messageButton.setOnAction(e -> showInfoAlert("Messages", "No new messages right now."));

        MenuButton profileMenu = new MenuButton("Super Admin");
        profileMenu.setStyle("-fx-background-color: transparent;");
        MenuItem profileItem = new MenuItem("My Profile");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem logoutItem = new MenuItem("Logout");
        profileItem.setOnAction(e -> showInfoAlert("Profile", "Opening admin profile page."));
        settingsItem.setOnAction(e -> showInfoAlert("Settings", "Opening settings page."));
        logoutItem.setOnAction(e -> handleLogout());
        profileMenu.getItems().addAll(profileItem, settingsItem, logoutItem);

        topBar.getChildren().addAll(menuButton, searchBox, spacer, bellButton, messageButton, profileMenu);
        return topBar;
    }

    private void toggleSidebar() {
        Node sidebar = rootLayout.getLeft();
        if (sidebar != null) {
            sidebar.setVisible(!sidebar.isVisible());
            sidebar.setManaged(sidebar.isVisible());
        }
    }

    private void handleSearch(String query) {
        if (query == null || query.isBlank()) {
            showInfoAlert("Search", "Type something first, then press Enter.");
        } else {
            showInfoAlert("Search Results", "Searching for: \"" + query + "\"");
        }
    }

    private void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Logout");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            showInfoAlert("Logged Out", "You have been logged out.");
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
    // Center content: publish form + content blocks table
    // ------------------------------------------------------------------

    private ScrollPane buildContent() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(20));

        Label heading = new Label("Content Management");
        heading.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));

        HBox row = new HBox(20);
        VBox publishPanel = buildPublishPanel();
        VBox listPanel = buildContentListPanel();
        HBox.setHgrow(listPanel, Priority.ALWAYS);
        row.getChildren().addAll(publishPanel, listPanel);

        view.getChildren().addAll(heading, row);

        ScrollPane scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: " + BG + "; -fx-background: " + BG + ";");
        return scrollPane;
    }

    private VBox buildPublishPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setPrefWidth(320);
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        Label formTitle = new Label("Publish Content");
        formTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        Label pageLbl = new Label("Section");
        pageBox = new ComboBox<>(FXCollections.observableArrayList(
                "Homepage Banner", "About Us", "FAQs", "Terms & Conditions", "Privacy Policy", "Announcement"));
        pageBox.setValue("Homepage Banner");
        pageBox.setMaxWidth(Double.MAX_VALUE);

        Label titleLbl = new Label("Title");
        titleField = new TextField();
        titleField.setPromptText("e.g. Monsoon Season Offer");

        Label bodyLbl = new Label("Content");
        bodyArea = new TextArea();
        bodyArea.setPromptText("Type the content text here...");
        bodyArea.setPrefHeight(120);
        bodyArea.setWrapText(true);

        Button publishBtn = new Button("Publish");
        publishBtn.setMaxWidth(Double.MAX_VALUE);
        publishBtn.setStyle("-fx-background-color:" + GREEN + "; -fx-text-fill:white; -fx-font-weight:bold;"
                + " -fx-padding:10; -fx-background-radius:6;");
        publishBtn.setOnAction(e -> handlePublish());

        Button clearBtn = new Button("Clear");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setStyle(
                "-fx-background-color: #f3f4f6; -fx-text-fill: #333; -fx-padding:8; -fx-background-radius:6;");
        clearBtn.setOnAction(e -> clearForm());

        panel.getChildren().addAll(formTitle, pageLbl, pageBox, titleLbl, titleField,
                bodyLbl, bodyArea, publishBtn, clearBtn);
        return panel;
    }

    // validates the form, inserts a new row at the top of the table and
    // resets the form - stands in for an actual save-to-server call
    private void handlePublish() {
        String title = titleField.getText();
        if (title == null || title.isBlank()) {
            showInfoAlert("Title Required", "Please enter a title before publishing.");
            return;
        }
        String body = bodyArea.getText();
        if (body == null || body.isBlank()) {
            showInfoAlert("Content Required", "Please type some content before publishing.");
            return;
        }

        String section = pageBox.getValue();
        String today = LocalDate.now().format(DATE_FMT);

        ContentRow newRow = new ContentRow(title, section, today, "Published");
        contentBlocks.add(0, newRow);

        showInfoAlert("Content Published", "\"" + title + "\" was published under " + section + ".");
        clearForm();
    }

    private void clearForm() {
        pageBox.setValue("Homepage Banner");
        titleField.clear();
        bodyArea.clear();
    }

    private VBox buildContentListPanel() {
        VBox panel = new VBox(12);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Published Content");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Hyperlink viewAll = new Hyperlink("View All");
        viewAll.setOnAction(e -> showInfoAlert("All Content", "Opening the full content library."));
        header.getChildren().addAll(title, spacer, viewAll);

        table = buildContentTable();

        panel.getChildren().addAll(header, table);
        return panel;
    }

    private TableView<ContentRow> buildContentTable() {
        TableView<ContentRow> tableView = new TableView<>();
        tableView.setPrefHeight(320);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ContentRow, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<ContentRow, String> sectionCol = new TableColumn<>("Section");
        sectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));

        TableColumn<ContentRow, String> updatedCol = new TableColumn<>("Last Updated");
        updatedCol.setCellValueFactory(new PropertyValueFactory<>("updatedOn"));

        TableColumn<ContentRow, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<ContentRow, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Label badge = new Label(status);
                badge.setPadding(new Insets(3, 10, 3, 10));
                String style = status.equals("Published")
                        ? "-fx-background-color:#E8F5E9; -fx-text-fill:" + GREEN + ";"
                        : "-fx-background-color:#fff3e0; -fx-text-fill:#ef6c00;";
                badge.setStyle(style + " -fx-background-radius:10; -fx-font-size:11;");
                setGraphic(badge);
                setText(null);
            }
        });

        tableView.getColumns().addAll(titleCol, sectionCol, updatedCol, statusCol);

        contentBlocks = loadContentBlocks();
        tableView.setItems(contentBlocks);

        // clicking a row shows the full details, and offers an unpublish option
        tableView.setRowFactory(tv -> {
            TableRow<ContentRow> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (!row.isEmpty()) {
                    showContentDetails(row.getItem());
                }
            });
            return row;
        });

        return tableView;
    }

    private ObservableList<ContentRow> loadContentBlocks() {
        ObservableList<ContentRow> rows = FXCollections.observableArrayList();
        rows.add(new ContentRow("Monsoon Season Offer", "Homepage Banner", "29 May 2025", "Published"));
        rows.add(new ContentRow("How Farmer Verification Works", "FAQs", "27 May 2025", "Published"));
        rows.add(new ContentRow("About AgriLink", "About Us", "20 May 2025", "Published"));
        rows.add(new ContentRow("Updated Terms & Conditions", "Terms & Conditions", "15 May 2025", "Draft"));
        rows.add(new ContentRow("Privacy Policy 2025", "Privacy Policy", "10 May 2025", "Published"));
        return rows;
    }

    private void showContentDetails(ContentRow row) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Content Details");
        alert.setHeaderText(row.getTitle());
        alert.setContentText("Section: " + row.getSection()
                + "\nLast Updated: " + row.getUpdatedOn()
                + "\nStatus: " + row.getStatus()
                + "\n\nUnpublish this content?");
        alert.getButtonTypes().setAll(new ButtonType("Unpublish"), ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().getText().equals("Unpublish")) {
            int index = contentBlocks.indexOf(row);
            if (index >= 0) {
                String today = LocalDate.now().format(DATE_FMT);
                contentBlocks.set(index, new ContentRow(row.getTitle(), row.getSection(), today, "Draft"));
            }
            showInfoAlert("Unpublished", "\"" + row.getTitle() + "\" was moved back to Draft.");
        }
    }

    // ------------------------------------------------------------------
    // Simple data model for a table row
    // ------------------------------------------------------------------

    public static class ContentRow {
        private final String title;
        private final String section;
        private final String updatedOn;
        private final String status;

        public ContentRow(String title, String section, String updatedOn, String status) {
            this.title = title;
            this.section = section;
            this.updatedOn = updatedOn;
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public String getSection() {
            return section;
        }

        public String getUpdatedOn() {
            return updatedOn;
        }

        public String getStatus() {
            return status;
        }
    }
}
