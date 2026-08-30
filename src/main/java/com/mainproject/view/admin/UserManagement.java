package com.mainproject.view.admin;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.mainproject.config.FirebaseConfig;

import javafx.application.Platform;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class UserManagement {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";

    private static final int PAGE_SIZE = 7;

    private final Stage stage;
    private final AdminDashboard dashboard;

    private final Firestore db;
    private final FirebaseAuth firebaseAuth;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private BorderPane rootLayout;

    private TableView<User> table;

    private TextField searchField;

    private Label resultsLabel;

    private Label loadingLabel;

    private Button activeTabButton;

    private ComboBox<String> sortBox;

    private Button previousButton;
    private Button nextButton;

    private HBox pageNumberBox;

    private final ObservableList<User> allUsers = FXCollections.observableArrayList();

    private final ObservableList<User> displayedUsers = FXCollections.observableArrayList();

    private String currentRoleFilter = "All Users";

    private int currentPage = 1;

    private int totalPages = 1;

    private Comparator<User> currentComparator = Comparator.comparing(
            User::getJoinedDateSafe,
            Comparator.nullsLast(Comparator.reverseOrder()));

    public UserManagement(
            Stage stage,
            AdminDashboard dashboard) {

        this.stage = stage;
        this.dashboard = dashboard;

        this.db = FirebaseConfig.getFirestore();

        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    // ============================================================
    // SHOW SCREEN
    // ============================================================

    public void show() {

        rootLayout = new BorderPane();

        rootLayout.setStyle(
                "-fx-background-color: " + BG + ";");

        rootLayout.setLeft(
                buildSidebar());

        rootLayout.setTop(
                buildTopBar());

        rootLayout.setCenter(
                buildContent());

        stage.getScene().setRoot(rootLayout);

        loadUsersFromFirestore();
    }

    // ============================================================
    // SIDEBAR
    // ============================================================

    private VBox buildSidebar() {
        // Use the shared admin sidebar so every admin screen shows
        // the same current navigation options.
        return AdminCommon.sidebar(stage, dashboard, "User Management");
    }

    private void handleNavClick(
            String pageName) {
        if (pageName == null) {
            return;
        }

        // All admin navigation is handled centrally by AdminDashboard.
        // This allows direct navigation from any admin page to any other page.
        dashboard.navigateToPage(pageName);
    }

    private void styleActiveNav(Button b) {

        b.setStyle(
                "-fx-background-color: " +
                        GREEN +
                        "; -fx-text-fill: white;" +
                        "-fx-background-radius: 0;");
    }

    private void styleInactiveNav(Button b) {

        b.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #d7e4d9;" +
                        "-fx-background-radius: 0;");
    }

    private void toggleSidebar() {

        Node sidebar = rootLayout.getLeft();

        if (sidebar != null) {

            boolean visible = !sidebar.isVisible();

            sidebar.setVisible(visible);

            sidebar.setManaged(visible);
        }
    }

    // ============================================================
    // TOP BAR
    // ============================================================

    private HBox buildTopBar() {

        HBox topBar = new HBox(15);

        topBar.setPadding(
                new Insets(
                        16,
                        25,
                        16,
                        20));

        topBar.setAlignment(
                Pos.CENTER_LEFT);

        topBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #eaeaea;" +
                        "-fx-border-width: 0 0 1 0;");

        Button menuButton = new Button("\u2630");

        menuButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-font-size: 16;");

        menuButton.setOnAction(
                e -> toggleSidebar());

        searchField = new TextField();

        searchField.setPromptText(
                "Search by name, email, phone...");

        searchField.setPrefWidth(300);

        searchField.setStyle(
                "-fx-background-color: transparent;");

        searchField.textProperty().addListener(
                (obs, oldText, newText) -> {

                    currentPage = 1;

                    refreshTable();
                });

        sortBox = new ComboBox<>(
                FXCollections.observableArrayList(
                        "Newest First",
                        "Oldest First",
                        "Name (A-Z)",
                        "Name (Z-A)"));

        sortBox.setValue(
                "Newest First");

        sortBox.setStyle(
                "-fx-background-color: transparent;");

        sortBox.setOnAction(
                e -> handleSortChange(
                        sortBox.getValue()));

        Button refreshButton = new Button("\u21BB");

        refreshButton.setTooltip(
                new Tooltip("Refresh users"));

        refreshButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-font-size: 16;");

        refreshButton.setOnAction(
                e -> loadUsersFromFirestore());

        HBox searchBox = new HBox(
                8,
                new Label("\uD83D\uDD0D"),
                searchField,
                sortBox,
                refreshButton);

        searchBox.setAlignment(
                Pos.CENTER_LEFT);

        searchBox.setPadding(
                new Insets(
                        6,
                        15,
                        6,
                        15));

        searchBox.setStyle(
                "-fx-background-color: #f3f4f6;" +
                        "-fx-background-radius: 20;");

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Button addUserButton = new Button("+  Add User");

        addUserButton.setStyle(
                "-fx-background-color: " +
                        GREEN +
                        "; -fx-text-fill: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-font-weight: bold;");

        addUserButton.setPadding(
                new Insets(
                        10,
                        18,
                        10,
                        18));

        addUserButton.setOnAction(
                e -> handleAddUser());

        topBar.getChildren().addAll(
                menuButton,
                searchBox,
                spacer,
                addUserButton);

        return topBar;
    }

    // ============================================================
    // CONTENT
    // ============================================================

    private VBox buildContent() {

        VBox content = new VBox(18);

        content.setPadding(
                new Insets(20));

        HBox tabsBar = buildTabsBar();

        VBox tableCard = new VBox(12);

        tableCard.setPadding(
                new Insets(18));

        tableCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(" +
                        "gaussian, rgba(0,0,0,0.08)," +
                        "8,0,0,2);");

        loadingLabel = new Label();

        loadingLabel.setStyle(
                "-fx-text-fill: #777;");

        table = buildUserTable();

        table.setItems(
                displayedUsers);

        HBox paginationBar = buildPaginationBar();

        tableCard.getChildren().addAll(
                loadingLabel,
                table,
                paginationBar);

        content.getChildren().addAll(
                tabsBar,
                tableCard);

        return content;
    }

    // ============================================================
    // TABS
    // ============================================================

    private HBox buildTabsBar() {

        HBox bar = new HBox(8);

        bar.setPadding(
                new Insets(
                        10,
                        15,
                        10,
                        15));

        bar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(" +
                        "gaussian, rgba(0,0,0,0.06)," +
                        "6,0,0,1);");

        String[] tabs = {

                "All Users",
                "Farmers",
                "Buyers",
                "Admins"
        };

        for (String tabName : tabs) {

            Button tabButton = new Button(tabName);

            tabButton.setPadding(
                    new Insets(
                            8,
                            18,
                            8,
                            18));

            tabButton.setFont(
                    Font.font(
                            "Segoe UI",
                            13));

            if (tabName.equals(
                    "All Users")) {

                activeTabButton = tabButton;

                styleActiveTab(
                        tabButton);

            } else {

                styleInactiveTab(
                        tabButton);
            }

            tabButton.setOnAction(
                    e -> handleTabClick(
                            tabName,
                            tabButton));

            bar.getChildren().add(
                    tabButton);
        }

        return bar;
    }

    private void handleTabClick(
            String tabName,
            Button clickedTab) {

        currentRoleFilter = tabName;

        if (activeTabButton != null) {

            styleInactiveTab(
                    activeTabButton);
        }

        styleActiveTab(
                clickedTab);

        activeTabButton = clickedTab;

        currentPage = 1;

        refreshTable();
    }

    private void styleActiveTab(Button b) {

        b.setStyle(
                "-fx-background-color: #eaf6ec;" +
                        "-fx-text-fill: " +
                        GREEN +
                        ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-font-weight: bold;");
    }

    private void styleInactiveTab(Button b) {

        b.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #666666;" +
                        "-fx-background-radius: 8;");
    }

    // ============================================================
    // TABLE
    // ============================================================

    private TableView<User> buildUserTable() {

        TableView<User> tv = new TableView<>();

        tv.setPrefHeight(400);

        tv.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        tv.setPlaceholder(
                new Label(
                        "No users found."));

        TableColumn<User, String> idCol = new TableColumn<>("User ID");

        idCol.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        TableColumn<User, String> nameCol = new TableColumn<>("Name");

        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");

        emailCol.setCellValueFactory(
                new PropertyValueFactory<>("email"));

        TableColumn<User, String> phoneCol = new TableColumn<>("Phone");

        phoneCol.setCellValueFactory(
                new PropertyValueFactory<>("phone"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");

        roleCol.setCellValueFactory(
                new PropertyValueFactory<>("role"));

        roleCol.setCellFactory(
                col -> badgeCell(
                        this::roleBadgeStyle));

        TableColumn<User, String> statusCol = new TableColumn<>("Status");

        statusCol.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        statusCol.setCellFactory(
                col -> badgeCell(
                        this::statusBadgeStyle));

        TableColumn<User, String> joinedCol = new TableColumn<>("Joined On");

        joinedCol.setCellValueFactory(
                new PropertyValueFactory<>("joinedOn"));

        TableColumn<User, Void> actionCol = new TableColumn<>("Action");

        actionCol.setCellFactory(
                col -> actionCell());

        tv.getColumns().addAll(
                idCol,
                nameCol,
                emailCol,
                phoneCol,
                roleCol,
                statusCol,
                joinedCol,
                actionCol);

        return tv;
    }

    private TableCell<User, String> badgeCell(
            Function<String, String> styleLookup) {

        return new TableCell<>() {

            @Override
            protected void updateItem(
                    String value,
                    boolean empty) {

                super.updateItem(
                        value,
                        empty);

                if (empty ||
                        value == null) {

                    setGraphic(null);

                    setText(null);

                    return;
                }

                Label badge = new Label(value);

                badge.setPadding(
                        new Insets(
                                3,
                                10,
                                3,
                                10));

                badge.setStyle(
                        "-fx-background-radius: 12;" +
                                "-fx-font-size: 11;" +
                                styleLookup.apply(value));

                setGraphic(badge);

                setText(null);
            }
        };
    }

    private String roleBadgeStyle(
            String role) {

        if ("Farmer".equalsIgnoreCase(role)) {

            return "-fx-background-color: #e3f2fd;" +
                    "-fx-text-fill: #1565c0;";

        } else if ("Buyer".equalsIgnoreCase(role)) {

            return "-fx-background-color: #f3e5f5;" +
                    "-fx-text-fill: #6a1b9a;";

        } else if ("Admin".equalsIgnoreCase(role)) {

            return "-fx-background-color: #fff8e1;" +
                    "-fx-text-fill: #f57f17;";
        }

        return "-fx-background-color: #eeeeee;" +
                "-fx-text-fill: #333333;";
    }

    private String statusBadgeStyle(
            String status) {

        if ("Active".equalsIgnoreCase(status)) {

            return "-fx-background-color: #e6f4ea;" +
                    "-fx-text-fill: #2e7d32;";
        }

        return "-fx-background-color: #fdecea;" +
                "-fx-text-fill: #c62828;";
    }

    // ============================================================
    // ACTION COLUMN
    // ============================================================

    private TableCell<User, Void> actionCell() {

        return new TableCell<>() {

            private final Button viewButton = new Button("\uD83D\uDC41");

            private final Button statusButton = new Button("\u21C5");

            private final Button deleteButton = new Button("\uD83D\uDDD1");

            private final HBox box = new HBox(
                    7,
                    viewButton,
                    statusButton,
                    deleteButton);

            {

                viewButton.setTooltip(
                        new Tooltip("View user"));

                statusButton.setTooltip(
                        new Tooltip(
                                "Activate / Deactivate"));

                deleteButton.setTooltip(
                        new Tooltip("Delete user"));

                viewButton.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-font-size: 13;");

                statusButton.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-font-size: 13;");

                deleteButton.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-font-size: 13;" +
                                "-fx-text-fill: #c62828;");

                viewButton.setOnAction(
                        e -> {

                            User user = getTableRow().getItem();

                            handleViewUser(user);
                        });

                statusButton.setOnAction(
                        e -> {

                            User user = getTableRow().getItem();

                            handleToggleStatus(user);
                        });

                deleteButton.setOnAction(
                        e -> {

                            User user = getTableRow().getItem();

                            handleDeleteUser(user);
                        });

                box.setAlignment(
                        Pos.CENTER_LEFT);
            }

            @Override
            protected void updateItem(
                    Void item,
                    boolean empty) {

                super.updateItem(
                        item,
                        empty);

                if (empty) {

                    setGraphic(null);

                } else {

                    User user = getTableRow().getItem();

                    if (user != null) {

                        statusButton.setText(
                                "Active".equalsIgnoreCase(
                                        user.getStatus())
                                                ? "\u23FB"
                                                : "\u25B6");
                    }

                    setGraphic(box);
                }
            }
        };
    }

    // ============================================================
    // PAGINATION
    // ============================================================

    private HBox buildPaginationBar() {

        HBox bar = new HBox(10);

        bar.setAlignment(
                Pos.CENTER_LEFT);

        bar.setPadding(
                new Insets(
                        15,
                        0,
                        0,
                        0));

        resultsLabel = new Label(
                "Showing 0 to 0 of 0 users");

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        previousButton = new Button("<");

        previousButton.setOnAction(
                e -> goToPreviousPage());

        nextButton = new Button(">");

        nextButton.setOnAction(
                e -> goToNextPage());

        pageNumberBox = new HBox(6);

        HBox rightSide = new HBox(
                6,
                previousButton,
                pageNumberBox,
                nextButton);

        rightSide.setAlignment(
                Pos.CENTER_LEFT);

        bar.getChildren().addAll(
                resultsLabel,
                spacer,
                rightSide);

        return bar;
    }

    private void rebuildPagination() {

        pageNumberBox.getChildren().clear();

        totalPages = Math.max(
                1,
                (int) Math.ceil(
                        (double) getFilteredUsers().size()
                                /
                                PAGE_SIZE));

        if (currentPage > totalPages) {

            currentPage = totalPages;
        }

        previousButton.setDisable(
                currentPage <= 1);

        nextButton.setDisable(
                currentPage >= totalPages);

        int startPage = Math.max(
                1,
                currentPage - 2);

        int endPage = Math.min(
                totalPages,
                startPage + 4);

        if (endPage - startPage < 4) {

            startPage = Math.max(
                    1,
                    endPage - 4);
        }

        for (int i = startPage; i <= endPage; i++) {

            final int page = i;

            Button button = new Button(
                    String.valueOf(page));

            button.setOnAction(
                    e -> {

                        currentPage = page;

                        refreshTable();
                    });

            if (page == currentPage) {

                button.setStyle(
                        "-fx-background-color: " +
                                GREEN +
                                ";" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 6;");

            } else {

                button.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #555555;" +
                                "-fx-background-radius: 6;");
            }

            pageNumberBox.getChildren().add(
                    button);
        }

        updateResultsLabel();
    }

    private void goToPreviousPage() {

        if (currentPage > 1) {

            currentPage--;

            refreshTable();
        }
    }

    private void goToNextPage() {

        if (currentPage < totalPages) {

            currentPage++;

            refreshTable();
        }
    }

    // ============================================================
    // FIRESTORE LOADING
    // ============================================================

    private void loadUsersFromFirestore() {

        loadingLabel.setText(
                "Loading users...");

        executor.submit(() -> {

            try {

                List<User> users = new ArrayList<>();

                List<QueryDocumentSnapshot> documents = db.collection("users")
                        .get()
                        .get()
                        .getDocuments();

                for (DocumentSnapshot document : documents) {

                    if (!document.exists()) {
                        continue;
                    }

                    String email = document.getId();

                    String uid = getString(
                            document,
                            "uid");

                    String name = getString(
                            document,
                            "fullName");

                    String phone = getString(
                            document,
                            "mobileNumber");

                    String role = getString(
                            document,
                            "role");

                    String status = getString(
                            document,
                            "status");

                    if (status == null ||
                            status.isBlank()) {

                        status = "Active";
                    }

                    LocalDate joinedDate = getCreatedDate(
                            document);

                    users.add(
                            new User(
                                    uid,
                                    name,
                                    email,
                                    phone,
                                    role,
                                    status,
                                    joinedDate));
                }

                final List<User> loadedUsers = users;

                Platform.runLater(() -> {

                    allUsers.setAll(
                            loadedUsers);

                    currentPage = 1;

                    loadingLabel.setText(
                            "Loaded " +
                                    loadedUsers.size() +
                                    " users");

                    refreshTable();
                });

            } catch (Exception e) {

                e.printStackTrace();

                Platform.runLater(() -> {

                    loadingLabel.setText(
                            "Failed to load users.");

                    showErrorAlert(
                            "Database Error",
                            "Unable to load users from Firestore.\n\n"
                                    + e.getMessage());
                });
            }
        });
    }

    private String getString(
            DocumentSnapshot document,
            String field) {

        String value = document.getString(field);

        if (value == null) {

            return "";
        }

        return value;
    }

    private LocalDate getCreatedDate(
            DocumentSnapshot document) {

        try {

            Timestamp timestamp = document.getTimestamp(
                    "createdAt");

            if (timestamp != null) {

                return timestamp
                        .toDate()
                        .toInstant()
                        .atZone(
                                ZoneId.systemDefault())
                        .toLocalDate();
            }

        } catch (Exception ignored) {
        }

        return null;
    }

    // ============================================================
    // FILTER + SEARCH + SORT
    // ============================================================

    private List<User> getFilteredUsers() {

        String query = searchField == null
                ? ""
                : searchField.getText()
                        .trim()
                        .toLowerCase();

        List<User> result = new ArrayList<>();

        for (User user : allUsers) {

            if (user == null) {
                continue;
            }

            boolean roleMatch;

            if (currentRoleFilter.equals(
                    "All Users")) {

                roleMatch = true;

            } else {

                roleMatch = user.getRole()
                        .equalsIgnoreCase(
                                roleForTab(
                                        currentRoleFilter));
            }

            if (!roleMatch) {
                continue;
            }

            boolean searchMatch = query.isEmpty()
                    ||
                    safe(user.getName())
                            .toLowerCase()
                            .contains(query)
                    ||
                    safe(user.getEmail())
                            .toLowerCase()
                            .contains(query)
                    ||
                    safe(user.getPhone())
                            .toLowerCase()
                            .contains(query);

            if (searchMatch) {

                result.add(user);
            }
        }

        result.sort(
                currentComparator);

        return result;
    }

    private String roleForTab(
            String tabLabel) {

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

    private String safe(String value) {

        return value == null
                ? ""
                : value;
    }

    private void handleSortChange(
            String option) {

        switch (option) {

            case "Newest First":

                currentComparator = Comparator.comparing(
                        User::getJoinedDateSafe,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()));

                break;

            case "Oldest First":

                currentComparator = Comparator.comparing(
                        User::getJoinedDateSafe,
                        Comparator.nullsLast(
                                Comparator.naturalOrder()));

                break;

            case "Name (A-Z)":

                currentComparator = Comparator.<User, String>comparing(
                        user -> safe(user.getName()).toLowerCase());

                break;

            case "Name (Z-A)":

                currentComparator = Comparator.<User, String>comparing(
                        user -> safe(user.getName()).toLowerCase())
                        .reversed();

                break;
        }

        currentPage = 1;

        refreshTable();
    }

    // ============================================================
    // REFRESH TABLE
    // ============================================================

    private void refreshTable() {

        if (table == null) {
            return;
        }

        List<User> filtered = getFilteredUsers();

        int total = filtered.size();

        totalPages = Math.max(
                1,
                (int) Math.ceil(
                        (double) total /
                                PAGE_SIZE));

        if (currentPage > totalPages) {

            currentPage = totalPages;
        }

        int fromIndex = (currentPage - 1)
                *
                PAGE_SIZE;

        int toIndex = Math.min(
                fromIndex + PAGE_SIZE,
                total);

        if (fromIndex > total) {

            fromIndex = 0;
            toIndex = Math.min(
                    PAGE_SIZE,
                    total);
        }

        List<User> pageUsers = filtered.subList(
                fromIndex,
                toIndex);

        displayedUsers.setAll(
                pageUsers);

        rebuildPagination();

        updateResultsLabel();
    }

    private void updateResultsLabel() {

        if (resultsLabel == null) {
            return;
        }

        int total = getFilteredUsers().size();

        if (total == 0) {

            resultsLabel.setText(
                    "Showing 0 to 0 of 0 users");

            return;
        }

        int start = ((currentPage - 1)
                * PAGE_SIZE) + 1;

        int end = Math.min(
                currentPage * PAGE_SIZE,
                total);

        resultsLabel.setText(
                "Showing "
                        + start
                        + " to "
                        + end
                        + " of "
                        + String.format(
                                "%,d",
                                total)
                        + " users");
    }

    // ============================================================
    // ADD USER
    // ============================================================

    private void handleAddUser() {

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle(
                "Add New User");

        dialog.setHeaderText(
                "Create a new AgriLink user");

        ButtonType addButton = new ButtonType(
                "Add User",
                ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        addButton,
                        ButtonType.CANCEL);

        TextField nameField = new TextField();

        nameField.setPromptText(
                "Full name");

        TextField emailField = new TextField();

        emailField.setPromptText(
                "Email address");

        TextField phoneField = new TextField();

        phoneField.setPromptText(
                "Phone number");

        PasswordField passwordField = new PasswordField();

        passwordField.setPromptText(
                "Password");

        ComboBox<String> roleBox = new ComboBox<>(
                FXCollections.observableArrayList(
                        "Farmer",
                        "Buyer",
                        "Admin"));

        roleBox.setValue(
                "Farmer");

        ComboBox<String> statusBox = new ComboBox<>(
                FXCollections.observableArrayList(
                        "Active",
                        "Inactive"));

        statusBox.setValue(
                "Active");

        ComboBox<String> genderBox = new ComboBox<>(
                FXCollections.observableArrayList(
                        "Male",
                        "Female",
                        "Other"));

        genderBox.setValue(
                "Other");

        GridPane grid = new GridPane();

        grid.setHgap(12);

        grid.setVgap(12);

        grid.setPadding(
                new Insets(15));

        grid.addRow(
                0,
                new Label("Name:"),
                nameField);

        grid.addRow(
                1,
                new Label("Email:"),
                emailField);

        grid.addRow(
                2,
                new Label("Phone:"),
                phoneField);

        grid.addRow(
                3,
                new Label("Password:"),
                passwordField);

        grid.addRow(
                4,
                new Label("Gender:"),
                genderBox);

        grid.addRow(
                5,
                new Label("Role:"),
                roleBox);

        grid.addRow(
                6,
                new Label("Status:"),
                statusBox);

        dialog.getDialogPane()
                .setContent(grid);

        Button addButtonNode = (Button) dialog.getDialogPane()
                .lookupButton(
                        addButton);

        addButtonNode.setDisable(true);

        Runnable validate = () -> {

            boolean valid = !nameField.getText()
                    .trim()
                    .isEmpty()
                    &&
                    !emailField.getText()
                            .trim()
                            .isEmpty()
                    &&
                    !phoneField.getText()
                            .trim()
                            .isEmpty()
                    &&
                    !passwordField.getText()
                            .isEmpty()
                    &&
                    passwordField.getText()
                            .length() >= 6;

            addButtonNode.setDisable(
                    !valid);
        };

        nameField.textProperty()
                .addListener(
                        (obs, o, n) -> validate.run());

        emailField.textProperty()
                .addListener(
                        (obs, o, n) -> validate.run());

        phoneField.textProperty()
                .addListener(
                        (obs, o, n) -> validate.run());

        passwordField.textProperty()
                .addListener(
                        (obs, o, n) -> validate.run());

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isEmpty()
                ||
                result.get() != addButton) {

            return;
        }

        String name = nameField.getText().trim();

        String email = emailField.getText()
                .trim()
                .toLowerCase();

        String phone = phoneField.getText().trim();

        String password = passwordField.getText();

        String gender = genderBox.getValue();

        String role = roleBox.getValue();

        String status = statusBox.getValue();

        createUser(
                name,
                email,
                phone,
                password,
                gender,
                role,
                status);
    }

    private void createUser(
            String name,
            String email,
            String phone,
            String password,
            String gender,
            String role,
            String status) {

        loadingLabel.setText(
                "Creating user...");

        executor.submit(() -> {

            try {

                // ------------------------------------------------
                // Check Firestore first
                // ------------------------------------------------

                DocumentSnapshot existing = db.collection("users")
                        .document(email)
                        .get()
                        .get();

                if (existing.exists()) {

                    Platform.runLater(() -> showErrorAlert(
                            "User Exists",
                            "A user with this email already exists."));

                    return;
                }

                // ------------------------------------------------
                // Create Firebase Authentication user
                // ------------------------------------------------

                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                        .setEmail(email)
                        .setPassword(password)
                        .setDisplayName(name)
                        .setDisabled(
                                "Inactive".equalsIgnoreCase(
                                        status));

                UserRecord firebaseUser = firebaseAuth
                        .createUser(request);

                String uid = firebaseUser.getUid();

                // ------------------------------------------------
                // Save Firestore document
                // ------------------------------------------------

                java.util.Map<String, Object> data = new java.util.HashMap<>();

                data.put(
                        "uid",
                        uid);

                data.put(
                        "fullName",
                        name);

                data.put(
                        "email",
                        email);

                data.put(
                        "mobileNumber",
                        phone);

                data.put(
                        "gender",
                        gender);

                data.put(
                        "role",
                        role);

                data.put(
                        "status",
                        status);

                data.put(
                        "createdAt",
                        Timestamp.now());

                data.put(
                        "profileImageUrl",
                        "");

                db.collection("users")
                        .document(email)
                        .set(data)
                        .get();

                Platform.runLater(() -> {

                    loadingLabel.setText(
                            "User created successfully.");

                    showInfoAlert(
                            "User Added",
                            name +
                                    " has been added successfully.");

                    loadUsersFromFirestore();
                });

            } catch (Exception e) {

                e.printStackTrace();

                Platform.runLater(() -> {

                    loadingLabel.setText("");

                    showErrorAlert(
                            "Unable to Add User",
                            e.getMessage());
                });
            }
        });
    }

    // ============================================================
    // VIEW USER
    // ============================================================

    private void handleViewUser(
            User user) {

        if (user == null) {
            return;
        }

        String details = "User ID: "
                + user.getId()
                + "\n\nName: "
                + user.getName()
                + "\n\nEmail: "
                + user.getEmail()
                + "\n\nPhone: "
                + user.getPhone()
                + "\n\nRole: "
                + user.getRole()
                + "\n\nStatus: "
                + user.getStatus()
                + "\n\nJoined On: "
                + user.getJoinedOn();

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);

        alert.setTitle(
                "User Details");

        alert.setHeaderText(
                user.getName());

        alert.setContentText(
                details);

        alert.showAndWait();
    }

    // ============================================================
    // ACTIVATE / DEACTIVATE
    // ============================================================

    private void handleToggleStatus(
            User user) {

        if (user == null) {
            return;
        }

        boolean currentlyActive = "Active".equalsIgnoreCase(
                user.getStatus());

        String newStatus = currentlyActive
                ? "Inactive"
                : "Active";

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION);

        confirm.setTitle(
                "Change User Status");

        confirm.setHeaderText(
                null);

        confirm.setContentText(
                "Change "
                        + user.getName()
                        + " to "
                        + newStatus
                        + "?");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isEmpty()
                ||
                result.get() != ButtonType.OK) {

            return;
        }

        updateUserStatus(
                user,
                newStatus);
    }

    private void updateUserStatus(
            User user,
            String newStatus) {

        loadingLabel.setText(
                "Updating user status...");

        executor.submit(() -> {

            try {

                java.util.Map<String, Object> updates = new java.util.HashMap<>();

                updates.put(
                        "status",
                        newStatus);

                db.collection("users")
                        .document(
                                user.getEmail())
                        .update(updates)
                        .get();

                // Also disable/enable Firebase Authentication.
                if (user.getUid() != null
                        &&
                        !user.getUid().isBlank()) {

                    UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(
                            user.getUid())
                            .setDisabled(
                                    "Inactive".equalsIgnoreCase(
                                            newStatus));

                    firebaseAuth.updateUser(
                            request);
                }

                Platform.runLater(() -> {

                    loadingLabel.setText(
                            "Status updated.");

                    showInfoAlert(
                            "Status Updated",
                            user.getName()
                                    + " is now "
                                    + newStatus
                                    + ".");

                    loadUsersFromFirestore();
                });

            } catch (Exception e) {

                e.printStackTrace();

                Platform.runLater(() -> {

                    showErrorAlert(
                            "Status Update Failed",
                            e.getMessage());
                });
            }
        });
    }

    // ============================================================
    // DELETE USER
    // ============================================================

    private void handleDeleteUser(
            User user) {

        if (user == null) {
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION);

        confirm.setTitle(
                "Delete User");

        confirm.setHeaderText(
                "Delete User?");

        confirm.setContentText(
                "Are you sure you want to delete:\n\n"
                        + user.getName()
                        + "\n"
                        + user.getEmail()
                        + "\n\n"
                        + "This action cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isEmpty()
                ||
                result.get() != ButtonType.OK) {

            return;
        }

        deleteUser(
                user);
    }

    private void deleteUser(
            User user) {

        loadingLabel.setText(
                "Deleting user...");

        executor.submit(() -> {

            try {

                // ------------------------------------------------
                // Delete Firestore document
                // ------------------------------------------------

                db.collection("users")
                        .document(
                                user.getEmail())
                        .delete()
                        .get();

                // ------------------------------------------------
                // Delete Firebase Authentication account
                // ------------------------------------------------

                if (user.getUid() != null
                        &&
                        !user.getUid().isBlank()) {

                    try {

                        firebaseAuth.deleteUser(
                                user.getUid());

                    } catch (Exception authException) {

                        System.out.println(
                                "Firebase Auth delete failed: "
                                        +
                                        authException.getMessage());
                    }
                }

                Platform.runLater(() -> {

                    loadingLabel.setText(
                            "User deleted.");

                    showInfoAlert(
                            "User Deleted",
                            user.getName()
                                    + " was deleted successfully.");

                    loadUsersFromFirestore();
                });

            } catch (Exception e) {

                e.printStackTrace();

                Platform.runLater(() -> {

                    showErrorAlert(
                            "Delete Failed",
                            e.getMessage());
                });
            }
        });
    }

    // ============================================================
    // ALERTS
    // ============================================================

    private void showInfoAlert(
            String title,
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION);

        alert.setTitle(
                title);

        alert.setHeaderText(
                null);

        alert.setContentText(
                message);

        alert.showAndWait();
    }

    private void showErrorAlert(
            String title,
            String message) {

        Alert alert = new Alert(
                Alert.AlertType.ERROR);

        alert.setTitle(
                title);

        alert.setHeaderText(
                null);

        alert.setContentText(
                message == null
                        ? "Unknown error."
                        : message);

        alert.showAndWait();
    }

    // ============================================================
    // USER TABLE MODEL
    // ============================================================

    public static class User {

        private final String id;

        private final String name;

        private final String email;

        private final String phone;

        private final String role;

        private final String status;

        private final LocalDate joinedDate;

        public User(
                String uid,
                String name,
                String email,
                String phone,
                String role,
                String status,
                LocalDate joinedDate) {

            this.id = uid == null || uid.isBlank()
                    ? email
                    : uid;

            this.name = name == null
                    ? ""
                    : name;

            this.email = email == null
                    ? ""
                    : email;

            this.phone = phone == null
                    ? ""
                    : phone;

            this.role = role == null
                    ? ""
                    : role;

            this.status = status == null
                    ? "Active"
                    : status;

            this.joinedDate = joinedDate;
        }

        public String getId() {

            return id;
        }

        public String getUid() {

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

        public LocalDate getJoinedDateSafe() {

            return joinedDate;
        }

        public String getJoinedOn() {

            if (joinedDate == null) {

                return "—";
            }

            return joinedDate.format(
                    DateTimeFormatter.ofPattern(
                            "dd MMM yyyy"));
        }
    }
}