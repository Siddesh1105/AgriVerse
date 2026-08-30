package com.mainproject.view.admin;

import com.mainproject.controller.UserController;
import com.mainproject.model.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FarmerVerification {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String BG = "#f4f6f5";

    private static final int PAGE_SIZE = 5;

    private final Stage stage;
    private final AdminDashboard dashboard;

    private final UserController userController;

    private BorderPane rootLayout;

    private TableView<FarmerApplication> table;

    private Label resultsLabel;

    private Label pendingCountLabel;
    private Label approvedCountLabel;
    private Label rejectedCountLabel;
    private Label informationCountLabel;

    private Button activeTabButton;

    private HBox pageNumberBox;

    private List<Button> pageButtons =
            new ArrayList<>();

    private ObservableList<FarmerApplication>
            allApplications =
            FXCollections.observableArrayList();

    private FilteredList<FarmerApplication>
            filteredApplications;

    private String currentStatusFilter =
            "Pending";

    private int currentPage = 1;

    private TextField searchField;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public FarmerVerification(
            Stage stage,
            AdminDashboard dashboard) {

        this.stage = stage;
        this.dashboard = dashboard;

        this.userController =
                new UserController();
    }

    // ============================================================
    // SHOW
    // ============================================================

    public void show() {

        rootLayout =
                new BorderPane();

        rootLayout.setStyle(
                "-fx-background-color: " + BG + ";"
        );

        rootLayout.setLeft(
                buildSidebar()
        );

        rootLayout.setTop(
                buildTopBar()
        );

        rootLayout.setCenter(
                buildContent()
        );

        stage.getScene().setRoot(
                rootLayout
        );

        loadFarmersFromFirebase();
    }

    // ============================================================
    // SIDEBAR
    // ============================================================

    private VBox buildSidebar() {
        // Use the shared admin sidebar so removed modules cannot
        // reappear when navigating away from the dashboard.
        return AdminCommon.sidebar(stage, dashboard, "Farmer Verification");
    }

    // ============================================================
    // NAVIGATION
    // ============================================================

    private void handleNavClick(
            String pageName) {

        if (pageName == null) {
            return;
        }

        dashboard.navigateToPage(
                pageName
        );
    }

    // ============================================================
    // NAV STYLE
    // ============================================================

    private void styleActiveNav(
            Button button) {

        button.setStyle(
                "-fx-background-color: " +
                        GREEN +
                        ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 0;"
        );
    }

    private void styleInactiveNav(
            Button button) {

        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #d7e4d9;" +
                        "-fx-background-radius: 0;"
        );
    }

    // ============================================================
    // SIDEBAR TOGGLE
    // ============================================================

    private void toggleSidebar() {

        Node sidebar =
                rootLayout.getLeft();

        if (sidebar != null) {

            boolean visible =
                    sidebar.isVisible();

            sidebar.setVisible(
                    !visible
            );

            sidebar.setManaged(
                    !visible
            );
        }
    }

    // ============================================================
    // TOP BAR
    // ============================================================

    private HBox buildTopBar() {

        HBox topBar =
                new HBox(15);

        topBar.setPadding(
                new Insets(
                        16,
                        25,
                        16,
                        20
                )
        );

        topBar.setAlignment(
                Pos.CENTER_LEFT
        );

        topBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #eaeaea;" +
                        "-fx-border-width: 0 0 1 0;"
        );

        Button menuButton =
                new Button("\u2630");

        menuButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-font-size: 16;"
        );

        menuButton.setOnAction(
                e ->
                        toggleSidebar()
        );

        Label title =
                new Label(
                        "Farmer Verification"
                );

        title.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        18
                )
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        searchField =
                new TextField();

        searchField.setPromptText(
                "Search farmers..."
        );

        searchField.setPrefWidth(
                250
        );

        searchField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) ->
                                applyFilters()
                );

        Button refreshButton =
                new Button(
                        "\u21BB Refresh"
                );

        refreshButton.setStyle(
                "-fx-background-color: " +
                        GREEN +
                        ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 6;"
        );

        refreshButton.setOnAction(
                e ->
                        loadFarmersFromFirebase()
        );

        topBar.getChildren().addAll(
                menuButton,
                title,
                spacer,
                searchField,
                refreshButton
        );

        return topBar;
    }

    // ============================================================
    // CONTENT
    // ============================================================

    private VBox buildContent() {

        VBox content =
                new VBox(18);

        content.setPadding(
                new Insets(20)
        );

        HBox tabsBar =
                buildTabsBar();

        VBox tableCard =
                new VBox(12);

        tableCard.setPadding(
                new Insets(18)
        );

        tableCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(" +
                        "gaussian, rgba(0,0,0,0.08)," +
                        "8,0,0,2);"
        );

        table =
                buildApplicationsTable();

        HBox paginationBar =
                buildPaginationBar();

        tableCard.getChildren().addAll(
                table,
                paginationBar
        );

        content.getChildren().addAll(
                tabsBar,
                tableCard
        );

        return content;
    }

    // ============================================================
    // TABS
    // ============================================================

    private HBox buildTabsBar() {

        HBox bar =
                new HBox(8);

        bar.setPadding(
                new Insets(
                        10,
                        15,
                        10,
                        15
                )
        );

        bar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(" +
                        "gaussian, rgba(0,0,0,0.06)," +
                        "6,0,0,1);"
        );

        Button pending =
                createTab(
                        "Pending",
                        "Pending"
                );

        Button approved =
                createTab(
                        "Approved",
                        "Approved"
                );

        Button rejected =
                createTab(
                        "Rejected",
                        "Rejected"
                );

        Button information =
                createTab(
                        "Information Required",
                        "Information Required"
                );

        activeTabButton =
                pending;

        styleActiveTab(
                pending
        );

        bar.getChildren().addAll(
                pending,
                approved,
                rejected,
                information
        );

        pendingCountLabel =
                new Label();

        approvedCountLabel =
                new Label();

        rejectedCountLabel =
                new Label();

        informationCountLabel =
                new Label();

        updateTabCounts();

        return bar;
    }

    private Button createTab(
            String status,
            String text) {

        Button button =
                new Button(text);

        button.setPadding(
                new Insets(
                        8,
                        18,
                        8,
                        18
                )
        );

        button.setFont(
                Font.font(
                        "Segoe UI",
                        13
                )
        );

        styleInactiveTab(
                button
        );

        button.setOnAction(
                e -> {

                    currentStatusFilter =
                            status;

                    currentPage = 1;

                    styleInactiveTab(
                            activeTabButton
                    );

                    styleActiveTab(
                            button
                    );

                    activeTabButton =
                            button;

                    applyFilters();
                }
        );

        return button;
    }

    private void styleActiveTab(
            Button button) {

        button.setStyle(
                "-fx-background-color: #eaf6ec;" +
                        "-fx-text-fill: " +
                        GREEN +
                        ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-font-weight: bold;"
        );
    }

    private void styleInactiveTab(
            Button button) {

        if (button == null) {
            return;
        }

        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #666666;" +
                        "-fx-background-radius: 8;"
        );
    }

    // ============================================================
    // TABLE
    // ============================================================

    private TableView<FarmerApplication>
    buildApplicationsTable() {

        TableView<FarmerApplication> tv =
                new TableView<>();

        tv.setPrefHeight(
                420
        );

        tv.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        tv.setPlaceholder(
                new Label(
                        "No farmer applications found."
                )
        );

        TableColumn<
                FarmerApplication,
                String> farmerCol =
                new TableColumn<>(
                        "Farmer"
                );

        farmerCol.setCellValueFactory(
                new PropertyValueFactory<>(
                        "name"
                )
        );

        TableColumn<
                FarmerApplication,
                String> emailCol =
                new TableColumn<>(
                        "Email"
                );

        emailCol.setCellValueFactory(
                new PropertyValueFactory<>(
                        "email"
                )
        );

        TableColumn<
                FarmerApplication,
                String> mobileCol =
                new TableColumn<>(
                        "Mobile"
                );

        mobileCol.setCellValueFactory(
                new PropertyValueFactory<>(
                        "mobileNumber"
                )
        );

        TableColumn<
                FarmerApplication,
                String> genderCol =
                new TableColumn<>(
                        "Gender"
                );

        genderCol.setCellValueFactory(
                new PropertyValueFactory<>(
                        "gender"
                )
        );

        TableColumn<
                FarmerApplication,
                String> statusCol =
                new TableColumn<>(
                        "Status"
                );

        statusCol.setCellValueFactory(
                new PropertyValueFactory<>(
                        "status"
                )
        );

        statusCol.setCellFactory(
                col ->
                        new TableCell<
                                FarmerApplication,
                                String>() {

                            @Override
                            protected void updateItem(
                                    String status,
                                    boolean empty) {

                                super.updateItem(
                                        status,
                                        empty
                                );

                                if (empty ||
                                        status == null) {

                                    setGraphic(null);
                                    setText(null);

                                    return;
                                }

                                Label badge =
                                        new Label(
                                                status
                                        );

                                badge.setPadding(
                                        new Insets(
                                                4,
                                                10,
                                                4,
                                                10
                                        )
                                );

                                badge.setStyle(
                                        statusStyle(
                                                status
                                        )
                                );

                                setGraphic(
                                        badge
                                );

                                setText(null);
                            }
                        }
        );

        TableColumn<
                FarmerApplication,
                Void> actionCol =
                new TableColumn<>(
                        "Action"
                );

        actionCol.setCellFactory(
                col ->
                        createActionCell()
        );

        tv.getColumns().addAll(
                farmerCol,
                emailCol,
                mobileCol,
                genderCol,
                statusCol,
                actionCol
        );

        return tv;
    }

    // ============================================================
    // ACTION CELL
    // ============================================================

    private TableCell<
            FarmerApplication,
            Void> createActionCell() {

        return new TableCell<>() {

            private final Button approve =
                    new Button("Approve");

            private final Button reject =
                    new Button("Reject");

            private final Button info =
                    new Button("Request Info");

            private final Button view =
                    new Button("View");

            private final HBox box =
                    new HBox(
                            6,
                            view,
                            approve,
                            reject
                    );

            {
                approve.setStyle(
                        "-fx-background-color: " +
                                GREEN +
                                ";" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 6;" +
                                "-fx-font-size: 11;"
                );

                reject.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #c62828;" +
                                "-fx-border-color: #c62828;" +
                                "-fx-border-radius: 6;" +
                                "-fx-background-radius: 6;" +
                                "-fx-font-size: 11;"
                );

                info.setStyle(
                        "-fx-background-color: #fff3e0;" +
                                "-fx-text-fill: #ef6c00;" +
                                "-fx-background-radius: 6;" +
                                "-fx-font-size: 11;"
                );

                view.setStyle(
                        "-fx-background-color: #eeeeee;" +
                                "-fx-text-fill: #333333;" +
                                "-fx-background-radius: 6;" +
                                "-fx-font-size: 11;"
                );

                approve.setOnAction(
                        e ->
                                handleApprove(
                                        getTableRow().getItem()
                                )
                );

                reject.setOnAction(
                        e ->
                                handleReject(
                                        getTableRow().getItem()
                                )
                );

                info.setOnAction(
                        e ->
                                handleRequestInformation(
                                        getTableRow().getItem()
                                )
                );

                view.setOnAction(
                        e ->
                                showFarmerDetails(
                                        getTableRow().getItem()
                                )
                );
            }

            @Override
            protected void updateItem(
                    Void item,
                    boolean empty) {

                super.updateItem(
                        item,
                        empty
                );

                if (empty ||
                        getTableRow() == null ||
                        getTableRow().getItem() == null) {

                    setGraphic(null);

                    return;
                }

                FarmerApplication app =
                        getTableRow().getItem();

                boolean decided =
                        "Approved".equals(
                                app.getStatus()
                        )
                        ||
                        "Rejected".equals(
                                app.getStatus()
                        );

                approve.setDisable(
                        decided
                );

                reject.setDisable(
                        decided
                );
                info.setDisable(
                        decided
                );

                setGraphic(box);
            }
        };
    }

    // ============================================================
    // LOAD FIREBASE
    // ============================================================

    private void loadFarmersFromFirebase() {

        allApplications.clear();

        try {

            List<User> farmers =
                    userController.getAllFarmers();

            for (User user : farmers) {

                if (user == null) {
                    continue;
                }

                String status =
                        user.getVerificationStatus();

                if (status == null ||
                        status.trim().isEmpty()) {

                    status = "Pending";
                }

                FarmerApplication application =
                        new FarmerApplication(
                                user.getUid(),
                                user.getFullName(),
                                user.getEmail(),
                                user.getMobileNumber(),
                                user.getGender(),
                                status,
                                user.getRejectionReason()
                        );

                allApplications.add(
                        application
                );
            }

            filteredApplications =
                    new FilteredList<>(
                            allApplications
                    );

            applyFilters();

            updateTabCounts();

        } catch (Exception e) {

            showErrorAlert(
                    "Unable to load farmers",
                    "Could not load farmer data from Firebase.\n\n"
                            + e.getMessage()
            );
        }
    }

    // ============================================================
    // FILTER
    // ============================================================

    private void applyFilters() {

        if (filteredApplications == null) {
            return;
        }

        String search =
                searchField == null
                        ? ""
                        : searchField
                        .getText()
                        .trim()
                        .toLowerCase();

        filteredApplications.setPredicate(
                farmer -> {

                    if (farmer == null) {
                        return false;
                    }

                    boolean statusMatches =
                            currentStatusFilter.equals(
                                    farmer.getStatus()
                            );

                    if (!statusMatches) {
                        return false;
                    }

                    if (search.isEmpty()) {
                        return true;
                    }

                    return safe(
                            farmer.getName()
                    )
                            .toLowerCase()
                            .contains(search)

                            ||

                            safe(
                                    farmer.getEmail()
                            )
                                    .toLowerCase()
                                    .contains(search)

                            ||

                            safe(
                                    farmer.getMobileNumber()
                            )
                                    .toLowerCase()
                                    .contains(search);
                }
        );

        updateTableForPage();
    }

    // ============================================================
    // PAGINATION
    // ============================================================

    private HBox buildPaginationBar() {

        HBox bar =
                new HBox(10);

        bar.setAlignment(
                Pos.CENTER_LEFT
        );

        bar.setPadding(
                new Insets(
                        15,
                        0,
                        0,
                        0
                )
        );

        resultsLabel =
                new Label();

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button previous =
                new Button("<");

        previous.setOnAction(
                e -> {

                    if (currentPage > 1) {

                        currentPage--;

                        updateTableForPage();
                    }
                }
        );

        pageButtons =
                new ArrayList<>();

        pageNumberBox =
                new HBox(6);

        Button next =
                new Button(">");

        next.setOnAction(
                e -> {

                    int totalPages =
                            getTotalPages();

                    if (currentPage <
                            totalPages) {

                        currentPage++;

                        updateTableForPage();
                    }
                }
        );

        bar.getChildren().addAll(
                resultsLabel,
                spacer,
                previous,
                pageNumberBox,
                next
        );

        return bar;
    }

    private int getTotalPages() {

        if (filteredApplications == null ||
                filteredApplications.isEmpty()) {

            return 1;
        }

        return Math.max(
                1,
                (int) Math.ceil(
                        filteredApplications.size()
                                /
                                (double) PAGE_SIZE
                )
        );
    }

    private void updateTableForPage() {

        if (table == null ||
                filteredApplications == null) {

            return;
        }

        int total =
                filteredApplications.size();

        int totalPages =
                getTotalPages();

        if (currentPage >
                totalPages) {

            currentPage =
                    totalPages;
        }

        int from =
                Math.min(
                        (currentPage - 1)
                                * PAGE_SIZE,
                        total
                );

        int to =
                Math.min(
                        from + PAGE_SIZE,
                        total
                );

        ObservableList<
                FarmerApplication> pageData =
                FXCollections.observableArrayList();

        for (int i = from;
             i < to;
             i++) {

            pageData.add(
                    filteredApplications.get(i)
            );
        }

        table.setItems(
                pageData
        );

        updateResultsLabel(
                total,
                from,
                to
        );

        updatePageButtons(
                totalPages
        );
    }

    private void updateResultsLabel(
            int total,
            int from,
            int to) {

        if (total == 0) {

            resultsLabel.setText(
                    "Showing 0 of 0 applications"
            );

            return;
        }

        resultsLabel.setText(
                "Showing "
                        + (from + 1)
                        + " to "
                        + to
                        + " of "
                        + total
                        + " applications"
        );
    }

    private void updatePageButtons(
            int totalPages) {

        if (pageNumberBox == null) {
            return;
        }

        pageNumberBox.getChildren().clear();
        pageButtons.clear();

        int maxVisible = 7;
        int start = Math.max(1, currentPage - 3);
        int end = Math.min(totalPages, start + maxVisible - 1);
        if (end - start + 1 < maxVisible) {
            start = Math.max(1, end - maxVisible + 1);
        }

        for (int i = start; i <= end; i++) {
            final int page = i;
            Button button = new Button(String.valueOf(i));
            button.setOnAction(e -> { currentPage = page; updateTableForPage(); });
            if (i == currentPage) {
                button.setStyle("-fx-background-color:" + GREEN + ";-fx-text-fill:white;-fx-background-radius:6;");
            } else {
                button.setStyle("-fx-background-color:transparent;-fx-text-fill:#555;-fx-background-radius:6;");
            }
            pageButtons.add(button);
            pageNumberBox.getChildren().add(button);
        }
    }

    // ============================================================
    // TAB COUNTS
    // ============================================================

    private void updateTabCounts() {

        int pending = 0;
        int approved = 0;
        int rejected = 0;
        int information = 0;

        for (FarmerApplication app :
                allApplications) {

            switch (
                    safe(
                            app.getStatus()
                    )
            ) {

                case "Approved":
                    approved++;
                    break;

                case "Rejected":
                    rejected++;
                    break;

                case "Information Required":
                    information++;
                    break;

                default:
                    pending++;
                    break;
            }
        }

        if (pendingCountLabel != null) { pendingCountLabel.setText(String.valueOf(pending)); }
        if (approvedCountLabel != null) { approvedCountLabel.setText(String.valueOf(approved)); }
        if (rejectedCountLabel != null) { rejectedCountLabel.setText(String.valueOf(rejected)); }
        if (informationCountLabel != null) { informationCountLabel.setText(String.valueOf(information)); }

        // Keep the visible tab labels dynamic.
        updateVisibleTabLabel("Pending", pending);
        updateVisibleTabLabel("Approved", approved);
        updateVisibleTabLabel("Rejected", rejected);
        updateVisibleTabLabel("Information Required", information);
    }

    private void updateVisibleTabLabel(String status, int count) {
        if (activeTabButton == null) {
            return;
        }
        // The current active button is not enough to reach all tab buttons, so
        // find them through the content's tab bar.
        if (rootLayout == null || rootLayout.getCenter() == null) {
            return;
        }
        updateTabButtonTexts(rootLayout.getCenter(), status, count);
    }

    private void updateTabButtonTexts(Node node, String status, int count) {
        if (node instanceof Button) {
            Button b = (Button) node;
            if (b.getText().startsWith(status)) {
                b.setText(status + " (" + count + ")");
            }
        }
        if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                updateTabButtonTexts(child, status, count);
            }
        }
    }

    // ============================================================
    // APPROVE
    // ============================================================

    private void handleApprove(
            FarmerApplication application) {

        if (application == null) {
            return;
        }

        Alert confirm =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirm.setTitle(
                "Approve Farmer"
        );

        confirm.setHeaderText(
                null
        );

        confirm.setContentText(
                "Approve "
                        + application.getName()
                        + " as a verified farmer?"
        );

        Optional<ButtonType> result =
                confirm.showAndWait();

        if (result.isEmpty() ||
                result.get() != ButtonType.OK) {

            return;
        }

        boolean success =
                userController
                        .updateFarmerVerification(
                                application.getEmail(),
                                "Approved",
                                ""
                        );

        if (success) {

            application.setStatus(
                    "Approved"
            );

            application.setRejectionReason(
                    ""
            );

            updateTabCounts();

            applyFilters();

            showInfoAlert(
                    "Application Approved",
                    application.getName()
                            + " is now a verified farmer."
            );

        } else {

            showErrorAlert(
                    "Approval Failed",
                    "The farmer could not be approved."
            );
        }
    }

    // ============================================================
    // REJECT
    // ============================================================

    private void handleReject(
            FarmerApplication application) {

        if (application == null) {
            return;
        }

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle(
                "Reject Farmer"
        );

        dialog.setHeaderText(
                "Reject "
                        + application.getName()
                        + "'s application"
        );

        dialog.setContentText(
                "Reason:"
        );

        Optional<String> result =
                dialog.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        String reason =
                result.get().trim();

        if (reason.isEmpty()) {

            showErrorAlert(
                    "Reason Required",
                    "Please enter a reason for rejection."
            );

            return;
        }

        boolean success =
                userController
                        .updateFarmerVerification(
                                application.getEmail(),
                                "Rejected",
                                reason
                        );

        if (success) {

            application.setStatus(
                    "Rejected"
            );

            application.setRejectionReason(
                    reason
            );

            updateTabCounts();

            applyFilters();

            showInfoAlert(
                    "Application Rejected",
                    application.getName()
                            + "'s application was rejected."
            );

        } else {

            showErrorAlert(
                    "Rejection Failed",
                    "The rejection could not be saved."
            );
        }
    }

    private void handleRequestInformation(
            FarmerApplication application) {

        if (application == null) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Request Information");
        dialog.setHeaderText("Request additional information from " + application.getName());
        dialog.setContentText("What information is required?");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty() || result.get().trim().isEmpty()) {
            return;
        }

        String reason = result.get().trim();
        boolean success = userController.updateFarmerVerification(
                application.getEmail(),
                "Information Required",
                reason
        );

        if (success) {
            application.setStatus("Information Required");
            application.setRejectionReason(reason);
            loadFarmersFromFirebase();
            showInfoAlert("Information Requested",
                    "The farmer has been moved to Information Required.");
        } else {
            showErrorAlert("Update Failed",
                    "The request could not be saved to Firebase.");
        }
    }

    // ============================================================
    // VIEW DETAILS
    // ============================================================

    private void showFarmerDetails(
            FarmerApplication application) {

        if (application == null) {
            return;
        }

        String message =
                "Farmer Name: "
                        + safe(application.getName())

                        + "\n\nEmail: "
                        + safe(application.getEmail())

                        + "\n\nMobile: "
                        + safe(application.getMobileNumber())

                        + "\n\nGender: "
                        + safe(application.getGender())

                        + "\n\nFirebase UID: "
                        + safe(application.getUid())

                        + "\n\nVerification Status: "
                        + safe(application.getStatus());

        if (!safe(
                application.getRejectionReason()
        ).isEmpty()) {

            message +=
                    "\n\nRejection Reason: "
                            + application
                            .getRejectionReason();
        }

        showInfoAlert(
                "Farmer Details",
                message
        );
    }

    // ============================================================
    // STATUS STYLE
    // ============================================================

    private String statusStyle(
            String status) {

        switch (status) {

            case "Approved":

                return
                        "-fx-background-color: #e6f4ea;" +
                                "-fx-text-fill: #2e7d32;" +
                                "-fx-background-radius: 12;";

            case "Rejected":

                return
                        "-fx-background-color: #ffebee;" +
                                "-fx-text-fill: #c62828;" +
                                "-fx-background-radius: 12;";

            case "Information Required":

                return
                        "-fx-background-color: #fff3e0;" +
                                "-fx-text-fill: #ef6c00;" +
                                "-fx-background-radius: 12;";

            default:

                return
                        "-fx-background-color: #e3f2fd;" +
                                "-fx-text-fill: #1565c0;" +
                                "-fx-background-radius: 12;";
        }
    }

    // ============================================================
    // ALERTS
    // ============================================================

    private void showInfoAlert(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showErrorAlert(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // ============================================================
    // SAFE STRING
    // ============================================================

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }

    // ============================================================
    // FARMER APPLICATION MODEL
    // ============================================================

    public static class FarmerApplication {

        private final String uid;
        private final String name;
        private final String email;
        private final String mobileNumber;
        private final String gender;

        private String status;
        private String rejectionReason;

        public FarmerApplication(
                String uid,
                String name,
                String email,
                String mobileNumber,
                String gender,
                String status,
                String rejectionReason) {

            this.uid = uid;
            this.name = name;
            this.email = email;
            this.mobileNumber = mobileNumber;
            this.gender = gender;
            this.status = status;
            this.rejectionReason =
                    rejectionReason;
        }

        public String getUid() {
            return uid;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public String getGender() {
            return gender;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(
                String status) {

            this.status = status;
        }

        public String getRejectionReason() {
            return rejectionReason;
        }

        public void setRejectionReason(
                String rejectionReason) {

            this.rejectionReason =
                    rejectionReason;
        }
    }
}