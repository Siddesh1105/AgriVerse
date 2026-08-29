package com.mainproject.view.admin;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * User Management screen for the AgriLink admin panel.
 * Not an Application itself - AdminDashboard owns the Stage and launches
 * this screen by calling show(), and this screen hands control back to
 * AdminDashboard through the "dashboard" reference when its own Dashboard
 * nav button is clicked.
 */
public class UserManagement {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";
    private static final int PAGE_SIZE = 7;
    private static final int TOTAL_PAGES = 1780;

    private final Stage stage;
    private final AdminDashboard dashboard;

    private BorderPane rootLayout;
    private TableView<User> table;
    private TextField searchField;
    private Label resultsLabel;
    private Button activeTabButton;
    private List<Button> pageButtons;

    private ObservableList<User> allUsers;
    private FilteredList<User> filteredUsers;
    private SortedList<User> sortedUsers;

    private String currentRoleFilter = "All Users";
    private int currentPage = 1;
    private int totalUsersCount = 12450;

    public UserManagement(Stage stage, AdminDashboard dashboard) {
        this.stage = stage;
        this.dashboard = dashboard;
    }

    // builds the whole screen and puts it on the shared stage
    public void show() {
        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + BG + ";");
        rootLayout.setLeft(buildSidebar());
        rootLayout.setTop(buildTopBar());
        rootLayout.setCenter(buildContent());
        stage.getScene().setRoot(rootLayout);
    }

    // ------------------------------------------------------------------
    // Sidebar (same nav list as the dashboard, "User Management" active)
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

            if (item.equals("User Management")) {
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

    // this is the important bit that connects the two screens together
    private void handleNavClick(String pageName) {
        if (pageName.equals("User Management")) {
            return; // already on this screen
        }
        if (pageName.equals("Dashboard")) {
            dashboard.showDashboard();
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
        if (pageName.equals("Live Marketplace")) {
            new LiveMarketplace(stage, dashboard).show();
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
    // Top bar: search, sort dropdown, Add User button
    // ------------------------------------------------------------------

    private HBox buildTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(16, 25, 16, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #eaeaea; -fx-border-width: 0 0 1 0;");

        Button menuButton = new Button("\u2630");
        menuButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16;");
        menuButton.setOnAction(e -> toggleSidebar());

        searchField = new TextField();
        searchField.setPromptText("Search by name, email, phone...");
        searchField.setPrefWidth(320);
        searchField.setStyle("-fx-background-color: transparent;");
        searchField.textProperty().addListener((obs, oldText, newText) -> applyFilters());

        ComboBox<String> sortBox = new ComboBox<>(FXCollections.observableArrayList(
                "Newest First", "Oldest First", "Name (A-Z)", "Name (Z-A)"));
        sortBox.setValue("Newest First");
        sortBox.setStyle("-fx-background-color: transparent;");
        sortBox.setOnAction(e -> handleSortChange(sortBox.getValue()));

        HBox searchBox = new HBox(8, new Label("\uD83D\uDD0D"), searchField, sortBox);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(6, 15, 6, 15));
        searchBox.setStyle("-fx-background-color: #f3f4f6; -fx-background-radius: 20;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addUserButton = new Button("+  Add User");
        addUserButton.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white;"
                + "-fx-background-radius: 8; -fx-font-weight: bold;");
        addUserButton.setPadding(new Insets(10, 18, 10, 18));
        addUserButton.setOnAction(e -> handleAddUser());

        topBar.getChildren().addAll(menuButton, searchBox, spacer, addUserButton);
        return topBar;
    }

    // ------------------------------------------------------------------
    // Center content: tabs + table card
    // ------------------------------------------------------------------

    private VBox buildContent() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(20));

        HBox tabsBar = buildTabsBar();

        VBox tableCard = new VBox(12);
        tableCard.setPadding(new Insets(18));
        tableCard.setStyle("-fx-background-color: white; -fx-background-radius: 12;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        table = buildUserTable();
        loadSampleUsers();
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

        String[] tabs = { "All Users", "Farmers", "Buyers", "Admins" };
        for (String tabName : tabs) {
            Button tabButton = new Button(tabName);
            tabButton.setPadding(new Insets(8, 18, 8, 18));
            tabButton.setFont(Font.font("Segoe UI", 13));

            if (tabName.equals("All Users")) {
                activeTabButton = tabButton;
                styleActiveTab(tabButton);
            } else {
                styleInactiveTab(tabButton);
            }

            tabButton.setOnAction(e -> handleTabClick(tabName, tabButton));
            bar.getChildren().add(tabButton);
        }
        return bar;
    }

    private void handleTabClick(String tabName, Button clickedTab) {
        currentRoleFilter = tabName;
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

    private TableView<User> buildUserTable() {
        TableView<User> tv = new TableView<>();
        tv.setPrefHeight(320);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.setPlaceholder(new Label("No users found for this filter."));

        TableColumn<User, String> idCol = new TableColumn<>("User ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setCellFactory(col -> badgeCell(this::roleBadgeStyle));

        TableColumn<User, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> badgeCell(this::statusBadgeStyle));

        TableColumn<User, String> joinedCol = new TableColumn<>("Joined On");
        joinedCol.setCellValueFactory(new PropertyValueFactory<>("joinedOn"));

        TableColumn<User, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> actionCell());

        tv.getColumns().addAll(idCol, nameCol, emailCol, phoneCol, roleCol, statusCol, joinedCol, actionCol);
        return tv;
    }

    private TableCell<User, String> badgeCell(Function<String, String> styleLookup) {
        return new TableCell<>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Label badge = new Label(value);
                badge.setPadding(new Insets(3, 10, 3, 10));
                badge.setStyle("-fx-background-radius: 12; -fx-font-size: 11; " + styleLookup.apply(value));
                setGraphic(badge);
                setText(null);
            }
        };
    }

    private String roleBadgeStyle(String role) {
        switch (role) {
            case "Farmer":
                return "-fx-background-color: #e3f2fd; -fx-text-fill: #1565c0;";
            case "Buyer":
                return "-fx-background-color: #f3e5f5; -fx-text-fill: #6a1b9a;";
            case "Admin":
                return "-fx-background-color: #fff8e1; -fx-text-fill: #f57f17;";
            default:
                return "-fx-background-color: #eeeeee; -fx-text-fill: #333333;";
        }
    }

    private String statusBadgeStyle(String status) {
        return status.equals("Active")
                ? "-fx-background-color: #e6f4ea; -fx-text-fill: #2e7d32;"
                : "-fx-background-color: #fdecea; -fx-text-fill: #c62828;";
    }

    private TableCell<User, Void> actionCell() {
        return new TableCell<>() {
            private final Button viewButton = new Button("\uD83D\uDC41");
            private final Button deleteButton = new Button("\uD83D\uDDD1");
            private final HBox box = new HBox(10, viewButton, deleteButton);

            {
                viewButton.setStyle("-fx-background-color: transparent; -fx-font-size: 13;");
                deleteButton.setStyle("-fx-background-color: transparent; -fx-font-size: 13; -fx-text-fill: #c62828;");
                viewButton.setOnAction(e -> handleViewUser(getTableRow().getItem()));
                deleteButton.setOnAction(e -> handleDeleteUser(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
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
        prevButton.setOnAction(e -> handlePrevPage());

        pageButtons = new ArrayList<>();
        HBox pageNumberBox = new HBox(6);
        for (int i = 1; i <= 5; i++) {
            final int pageNumber = i;
            Button pageButton = new Button(String.valueOf(pageNumber));
            pageButton.setOnAction(e -> handlePageClick(pageNumber));
            pageButtons.add(pageButton);
            pageNumberBox.getChildren().add(pageButton);
        }

        Label ellipsis = new Label("...");
        ellipsis.setPadding(new Insets(0, 4, 0, 4));

        Button lastPageButton = new Button(String.valueOf(TOTAL_PAGES));
        lastPageButton.setOnAction(e -> handlePageClick(TOTAL_PAGES));
        pageButtons.add(lastPageButton);

        Button nextButton = new Button(">");
        nextButton.setOnAction(e -> handleNextPage());

        highlightPageButton(currentPage);

        HBox rightSide = new HBox(6, prevButton, pageNumberBox, ellipsis, lastPageButton, nextButton);
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

    // recalculates the "Showing X to Y of Z users" text. For "All Users" it uses
    // the
    // page/total math against the mock 12,450 total; for a role tab it just
    // reflects
    // how many rows matched, since there's no real per-role total in this demo.
    private void updateResultsLabel() {
        int shown = filteredUsers.size();
        if (currentRoleFilter.equals("All Users")) {
            int start = shown == 0 ? 0 : (currentPage - 1) * PAGE_SIZE + 1;
            int end = Math.min(currentPage * PAGE_SIZE, totalUsersCount);
            resultsLabel.setText("Showing " + start + " to " + end + " of "
                    + String.format("%,d", totalUsersCount) + " users");
        } else {
            int end = shown;
            int start = shown == 0 ? 0 : 1;
            resultsLabel.setText("Showing " + start + " to " + end + " of " + shown + " users");
        }
    }

    // ------------------------------------------------------------------
    // Data loading + filtering + sorting
    // ------------------------------------------------------------------

    private void loadSampleUsers() {
        allUsers = FXCollections.observableArrayList(
                new User("USR1001", "Rajesh Patil", "rajesh@gmail.com", "9876543210", "Farmer", "Active",
                        LocalDate.of(2025, 5, 20)),
                new User("USR1002", "Suresh Yadav", "suresh@gmail.com", "8765432109", "Farmer", "Active",
                        LocalDate.of(2025, 5, 18)),
                new User("USR1003", "Green Mart", "greenmart@gmail.com", "9123456780", "Buyer", "Active",
                        LocalDate.of(2025, 5, 15)),
                new User("USR1004", "Fresh Store", "freshstore@gmail.com", "9234567891", "Buyer", "Active",
                        LocalDate.of(2025, 5, 14)),
                new User("USR1005", "Anita Deshmukh", "anita@gmail.com", "9012345678", "Buyer", "Inactive",
                        LocalDate.of(2025, 5, 10)),
                new User("USR1006", "Vikram Singh", "vikram@gmail.com", "9988776655", "Farmer", "Active",
                        LocalDate.of(2025, 5, 9)),
                new User("USR1007", "Organic Basket", "organic@gmail.com", "8877665544", "Buyer", "Active",
                        LocalDate.of(2025, 5, 8)));

        filteredUsers = new FilteredList<>(allUsers, user -> true);
        filteredUsers.addListener((ListChangeListener<User>) change -> updateResultsLabel());

        sortedUsers = new SortedList<>(filteredUsers, Comparator.comparing(User::getJoinedDate).reversed());
        table.setItems(sortedUsers);
    }

    private void applyFilters() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        filteredUsers.setPredicate(user -> {
            boolean matchesRole = currentRoleFilter.equals("All Users")
                    || user.getRole().equalsIgnoreCase(roleForTab(currentRoleFilter));
            boolean matchesSearch = query.isEmpty()
                    || user.getName().toLowerCase().contains(query)
                    || user.getEmail().toLowerCase().contains(query)
                    || user.getPhone().contains(query);
            return matchesRole && matchesSearch;
        });
        currentPage = 1;
    }

    private String roleForTab(String tabLabel) {
        switch (tabLabel) {
            case "Farmers":
                return "Farmer";
            case "Buyers":
                return "Buyer";
            case "Admins":
                return "Admin";
            default:
                return "";
        }
    }

    private void handleSortChange(String option) {
        switch (option) {
            case "Newest First":
                sortedUsers.setComparator(Comparator.comparing(User::getJoinedDate).reversed());
                break;
            case "Oldest First":
                sortedUsers.setComparator(Comparator.comparing(User::getJoinedDate));
                break;
            case "Name (A-Z)":
                sortedUsers.setComparator(Comparator.comparing(User::getName));
                break;
            case "Name (Z-A)":
                sortedUsers.setComparator(Comparator.comparing(User::getName).reversed());
                break;
            default:
                break;
        }
    }

    // ------------------------------------------------------------------
    // Button actions: add / view / delete
    // ------------------------------------------------------------------

    private void handleAddUser() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        dialog.setHeaderText(null);

        ButtonType addButtonType = new ButtonType("Add User", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("Full name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email address");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone number");
        ComboBox<String> roleBox = new ComboBox<>(FXCollections.observableArrayList("Farmer", "Buyer", "Admin"));
        roleBox.setValue("Farmer");
        ComboBox<String> statusBox = new ComboBox<>(FXCollections.observableArrayList("Active", "Inactive"));
        statusBox.setValue("Active");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Email:"), emailField);
        grid.addRow(2, new Label("Phone:"), phoneField);
        grid.addRow(3, new Label("Role:"), roleBox);
        grid.addRow(4, new Label("Status:"), statusBox);
        dialog.getDialogPane().setContent(grid);

        // block the dialog from closing until the required fields are filled in
        Button addButtonNode = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButtonNode.addEventFilter(ActionEvent.ACTION, event -> {
            if (nameField.getText().isBlank() || emailField.getText().isBlank() || phoneField.getText().isBlank()) {
                showInfoAlert("Missing Information", "Please fill in name, email and phone before adding the user.");
                event.consume();
            }
        });

        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButtonType) {
                String newId = "USR" + (1000 + allUsers.size() + 1);
                return new User(newId, nameField.getText().trim(), emailField.getText().trim(),
                        phoneField.getText().trim(), roleBox.getValue(), statusBox.getValue(), LocalDate.now());
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        result.ifPresent(user -> {
            allUsers.add(0, user);
            totalUsersCount++;
            updateResultsLabel();
            showInfoAlert("User Added", user.getName() + " was added successfully.");
        });
    }

    private void handleViewUser(User user) {
        if (user == null) {
            return;
        }
        String details = "User ID: " + user.getId()
                + "\nName: " + user.getName()
                + "\nEmail: " + user.getEmail()
                + "\nPhone: " + user.getPhone()
                + "\nRole: " + user.getRole()
                + "\nStatus: " + user.getStatus()
                + "\nJoined On: " + user.getJoinedOn();
        showInfoAlert("User Details", details);
    }

    private void handleDeleteUser(User user) {
        if (user == null) {
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete User");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete " + user.getName() + " (" + user.getId() + ")? This cannot be undone.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            allUsers.remove(user);
            totalUsersCount--;
            updateResultsLabel();
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

    public static class User {
        private final String id;
        private final String name;
        private final String email;
        private final String phone;
        private final String role;
        private final String status;
        private final LocalDate joinedDate;

        public User(String id, String name, String email, String phone, String role, String status,
                LocalDate joinedDate) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.role = role;
            this.status = status;
            this.joinedDate = joinedDate;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getRole() {
            return role;
        }

        public String getStatus() {
            return status;
        }

        public LocalDate getJoinedDate() {
            return joinedDate;
        }

        public String getJoinedOn() {
            return joinedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        }
    }
}