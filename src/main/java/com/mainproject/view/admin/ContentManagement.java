
// package com.mainproject.view.admin;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.chart.PieChart;
// import javafx.scene.control.*;
// import javafx.scene.layout.*;
// import javafx.scene.paint.Color;
// import javafx.scene.text.Font;
// import javafx.scene.text.FontWeight;
// import javafx.stage.Stage;

// import java.util.Optional;

// /**
//  * ContentManagement.java
//  *
//  * Builds the "Content Management" admin screen shown in the mock-up:
//  * left sidebar, top header, six summary cards, a tab bar, a filter bar,
//  * the content table, a right hand info panel and a recent activity strip.
//  *
//  * Every button on the screen is wired to a small handler method so the
//  * screen actually responds when clicked, instead of being static UI.
//  */
// public class ContentManagement {

//     private final Stage stage;
//     private final ObservableList<ContentItem> allContent = FXCollections.observableArrayList();
//     private final ObservableList<ContentItem> tableData = FXCollections.observableArrayList();

//     private TableView<ContentItem> table;
//     private TextField searchField;
//     private ComboBox<String> typeFilter;
//     private ComboBox<String> statusFilter;
//     private Label paginationLabel;
//     private Label totalContentValue;

//     private int currentPage = 1;
//     private final int totalPages = 16;

//     private static final String GREEN = "#1f6f43";
//     private static final String BG = "#f4f6f8";

//     public ContentManagement(Stage stage) {
//         this.stage = stage;
//         loadSampleData();
//     }

//     // ---------- top level layout ----------

//     public BorderPane getView() {
//         BorderPane root = new BorderPane();
//         root.setStyle("-fx-background-color: " + BG + ";");

//         root.setLeft(buildSidebar());
//         root.setTop(buildHeader());

//         VBox center = new VBox(16);
//         center.setPadding(new Insets(20));
//         center.getChildren().addAll(
//                 buildPageTitleBar(),
//                 buildStatsRow(),
//                 buildMainBody(),
//                 buildRecentActivity()
//         );

//         ScrollPane scrollPane = new ScrollPane(center);
//         scrollPane.setFitToWidth(true);
//         scrollPane.setStyle("-fx-background-color: transparent;");
//         root.setCenter(scrollPane);

//         return root;
//     }

//     // ---------- sidebar ----------

//     private VBox buildSidebar() {
//         VBox sidebar = new VBox();
//         sidebar.setPrefWidth(210);
//         sidebar.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-width: 0 1 0 0;");

//         HBox logoBox = new HBox(8);
//         logoBox.setPadding(new Insets(20, 16, 20, 16));
//         logoBox.setAlignment(Pos.CENTER_LEFT);
//         logoBox.setCursor(javafx.scene.Cursor.HAND);
//         Label leaf = new Label("\uD83C\uDF3F");
//         Label brand = new Label("AgriLink");
//         brand.setFont(Font.font("System", FontWeight.BOLD, 20));
//         brand.setTextFill(Color.web(GREEN));
//         VBox brandBox = new VBox(brand, subLabel("Admin Dashboard"));
//         logoBox.getChildren().addAll(leaf, brandBox);
//         logoBox.setOnMouseClicked(e -> goToAdminDashboard());

//         VBox nav = new VBox(2);
//         nav.setPadding(new Insets(4, 8, 4, 8));

//         String[] items = {
//                 "Dashboard", "User Management", "Farmer Verification", "Product Management",
//                 "Order Management", "Live Marketplace", "Equipment Management", "Analytics & Reports",
//                 "Crop Price Management", "Payment Management", "Feedback & Reviews", "Content Management"
//         };
//         for (String item : items) {
//             boolean active = item.equals("Content Management");
//             nav.getChildren().add(buildNavItem(item, active));
//         }

//         VBox subNav = new VBox(1);
//         subNav.setPadding(new Insets(0, 8, 0, 24));
//         String[] subItems = {"All Content", "Pages", "Banners", "Announcements", "FAQs", "Resources",
//                 "Policies", "Notification Templates"};
//         for (String s : subItems) {
//             subNav.getChildren().add(buildSubNavItem(s, s.equals("All Content")));
//         }

//         VBox bottomNav = new VBox(2);
//         bottomNav.setPadding(new Insets(8, 8, 16, 8));
//         String[] bottomItems = {"Notifications", "Reports & Complaints", "System & Data Management",
//                 "Audit Logs", "Settings", "Logout"};
//         for (String item : bottomItems) {
//             bottomNav.getChildren().add(buildNavItem(item, false));
//         }

//         Region spacer = new Region();
//         VBox.setVgrow(spacer, Priority.ALWAYS);

//         sidebar.getChildren().addAll(logoBox, nav, subNav, spacer, bottomNav);
//         return sidebar;
//     }

//     private Label subLabel(String text) {
//         Label l = new Label(text);
//         l.setFont(Font.font(10));
//         l.setTextFill(Color.web("#888888"));
//         return l;
//     }

//     private HBox buildNavItem(String text, boolean active) {
//         HBox box = new HBox(10);
//         box.setPadding(new Insets(8, 12, 8, 12));
//         box.setAlignment(Pos.CENTER_LEFT);
//         box.setCursor(javafx.scene.Cursor.HAND);
//         Label label = new Label(text);
//         label.setFont(Font.font(13));
//         if (active) {
//             box.setStyle("-fx-background-color: " + GREEN + "; -fx-background-radius: 6;");
//             label.setTextFill(Color.WHITE);
//         } else {
//             label.setTextFill(Color.web("#333333"));
//         }
//         box.getChildren().add(label);
//         box.setOnMouseClicked(e -> onSidebarItemClicked(text));
//         return box;
//     }

//     private HBox buildSubNavItem(String text, boolean active) {
//         HBox box = new HBox();
//         box.setPadding(new Insets(6, 12, 6, 12));
//         box.setCursor(javafx.scene.Cursor.HAND);
//         Label label = new Label(text);
//         label.setFont(Font.font(12));
//         label.setTextFill(active ? Color.web(GREEN) : Color.web("#555555"));
//         if (active) label.setStyle("-fx-font-weight: bold;");
//         box.getChildren().add(label);
//         box.setOnMouseClicked(e -> onSubNavItemClicked(text));
//         return box;
//     }

//     // sidebar handlers

//     private void onSidebarItemClicked(String item) {
//         if (item.equals("Dashboard")) {
//             goToAdminDashboard();
//         } else if (item.equals("Content Management")) {
//             // already here, do nothing
//         } else if (item.equals("Logout")) {
//             confirmLogout();
//         } else {
//             infoAlert(item, item + " module is not built in this demo yet.");
//         }
//     }

//     private void onSubNavItemClicked(String tabName) {
//         selectTab(tabName);
//     }

//     private void goToAdminDashboard() {
//         AdminDashboard dashboard = new AdminDashboard(stage);
//         Scene scene = new Scene(dashboard.getView(), 1536, 1000);
//         stage.setScene(scene);
//         stage.setTitle("AgriLink Admin - Dashboard");
//     }

//     private void confirmLogout() {
//         Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
//                 "Are you sure you want to logout?", ButtonType.YES, ButtonType.NO);
//         alert.setHeaderText(null);
//         alert.setTitle("Logout");
//         Optional<ButtonType> result = alert.showAndWait();
//         if (result.isPresent() && result.get() == ButtonType.YES) {
//             stage.close();
//         }
//     }

//     // ---------- header ----------

//     private HBox buildHeader() {
//         HBox header = new HBox(16);
//         header.setPadding(new Insets(14, 24, 14, 24));
//         header.setAlignment(Pos.CENTER_LEFT);
//         header.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-width: 0 0 1 0;");

//         Button menuButton = new Button("\u2630");
//         menuButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16px;");
//         menuButton.setOnAction(e -> infoAlert("Menu", "Sidebar collapse toggled."));

//         TextField globalSearch = new TextField();
//         globalSearch.setPromptText("Search content, pages, banners...");
//         globalSearch.setPrefWidth(400);
//         globalSearch.setOnAction(e -> infoAlert("Search", "Searching for: " + globalSearch.getText()));

//         Region spacer = new Region();
//         HBox.setHgrow(spacer, Priority.ALWAYS);

//         Button bell = badgeButton("\uD83D\uDD14", "12");
//         bell.setOnAction(e -> infoAlert("Notifications", "You have 12 unread notifications."));

//         Button mail = badgeButton("\u2709", "5");
//         mail.setOnAction(e -> infoAlert("Messages", "You have 5 unread messages."));

//         HBox profile = new HBox(8);
//         profile.setAlignment(Pos.CENTER_LEFT);
//         profile.setCursor(javafx.scene.Cursor.HAND);
//         Label avatar = new Label("\uD83D\uDC64");
//         VBox profileText = new VBox(new Label("Super Admin"), subLabel("Administrator"));
//         profile.getChildren().addAll(avatar, profileText);
//         profile.setOnMouseClicked(e -> infoAlert("Profile", "Open profile menu for Super Admin."));

//         header.getChildren().addAll(menuButton, globalSearch, spacer, bell, mail, profile);
//         return header;
//     }

//     private Button badgeButton(String icon, String count) {
//         Button b = new Button(icon + " (" + count + ")");
//         b.setStyle("-fx-background-color: transparent; -fx-font-size: 13px;");
//         return b;
//     }

//     // ---------- page title + add content ----------

//     private HBox buildPageTitleBar() {
//         HBox bar = new HBox();
//         bar.setAlignment(Pos.CENTER_LEFT);

//         VBox titleBox = new VBox(2);
//         Label title = new Label("Content Management");
//         title.setFont(Font.font("System", FontWeight.BOLD, 24));
//         Label subtitle = subLabel("Manage all content, pages, banners and announcements");
//         subtitle.setFont(Font.font(13));
//         titleBox.getChildren().addAll(title, subtitle);

//         Region spacer = new Region();
//         HBox.setHgrow(spacer, Priority.ALWAYS);

//         MenuButton addButton = new MenuButton("+ Add New Content");
//         addButton.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-font-weight: bold;");
//         MenuItem addPage = new MenuItem("Add Page");
//         MenuItem addBanner = new MenuItem("Add Banner");
//         MenuItem addAnnouncement = new MenuItem("Add Announcement");
//         addPage.setOnAction(e -> addNewContent("Page"));
//         addBanner.setOnAction(e -> addNewContent("Banner"));
//         addAnnouncement.setOnAction(e -> addNewContent("Announcement"));
//         addButton.getItems().addAll(addPage, addBanner, addAnnouncement);

//         bar.getChildren().addAll(titleBox, spacer, addButton);
//         return bar;
//     }

//     private void addNewContent(String type) {
//         TextInputDialog dialog = new TextInputDialog();
//         dialog.setTitle("Add New " + type);
//         dialog.setHeaderText("Create a new " + type.toLowerCase());
//         dialog.setContentText("Title:");
//         Optional<String> result = dialog.showAndWait();
//         result.ifPresent(titleText -> {
//             if (!titleText.isBlank()) {
//                 ContentItem item = new ContentItem(titleText, "New " + type.toLowerCase() + " entry",
//                         type, "General", "Draft", "Today", "Super Admin");
//                 allContent.add(0, item);
//                 applyFilters();
//                 totalContentValue.setText(String.valueOf(allContent.size()));
//                 infoAlert("Content Created", type + " \"" + titleText + "\" was added as a draft.");
//             }
//         });
//     }

//     // ---------- stat cards ----------

//     private HBox buildStatsRow() {
//         HBox row = new HBox(14);

//         totalContentValue = new Label(String.valueOf(allContent.size()));
//         VBox totalCard = statCard("\uD83D\uDCC4", "Total Content", totalContentValue, "+12 this month", "#e8f3ee");
//         VBox pagesCard = statCard("\uD83D\uDCC4", "Pages", new Label("18"), "+2 this month", "#e8f3ee");
//         VBox bannersCard = statCard("\uD83D\uDDBC", "Banners", new Label("16"), "+3 this month", "#e8f3ee");
//         VBox announcementsCard = statCard("\uD83D\uDCE2", "Announcements", new Label("10"), "+1 this month", "#fdf1de");
//         VBox faqsCard = statCard("\u2753", "FAQs", new Label("24"), "+4 this month", "#e9f0fb");
//         VBox resourcesCard = statCard("\uD83D\uDCD8", "Resources", new Label("30"), "+6 this month", "#e9f0fb");

//         for (VBox card : new VBox[]{totalCard, pagesCard, bannersCard, announcementsCard, faqsCard, resourcesCard}) {
//             HBox.setHgrow(card, Priority.ALWAYS);
//         }

//         row.getChildren().addAll(totalCard, pagesCard, bannersCard, announcementsCard, faqsCard, resourcesCard);
//         return row;
//     }

//     private VBox statCard(String icon, String label, Label valueLabel, String delta, String iconBg) {
//         VBox card = new VBox(6);
//         card.setPadding(new Insets(16));
//         card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
//                 "-fx-border-color: #eef0f2; -fx-border-radius: 10;");
//         card.setCursor(javafx.scene.Cursor.HAND);

//         Label iconLabel = new Label(icon);
//         iconLabel.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 8; -fx-padding: 6 10;");

//         Label textLabel = new Label(label);
//         textLabel.setFont(Font.font(12));
//         textLabel.setTextFill(Color.web("#666666"));

//         valueLabel.setFont(Font.font("System", FontWeight.BOLD, 22));

//         Label deltaLabel = new Label(delta);
//         deltaLabel.setFont(Font.font(11));
//         deltaLabel.setTextFill(Color.web("#2e9e5b"));

//         card.getChildren().addAll(iconLabel, textLabel, valueLabel, deltaLabel);
//         card.setOnMouseClicked(e -> infoAlert(label, "Showing details for: " + label));
//         return card;
//     }

//     // ---------- main body: tabs + filters + table on the left, info panel on the right ----------

//     private HBox buildMainBody() {
//         HBox body = new HBox(16);

//         VBox left = new VBox(0);
//         left.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
//                 "-fx-border-color: #eef0f2; -fx-border-radius: 10;");
//         left.setPadding(new Insets(16));
//         left.getChildren().addAll(buildTabsBar(), buildFilterBar(), buildTable(), buildPagination());
//         HBox.setHgrow(left, Priority.ALWAYS);

//         VBox right = buildRightPanel();
//         right.setPrefWidth(300);

//         body.getChildren().addAll(left, right);
//         return body;
//     }

//     private HBox tabsBarRef;
//     private final String[] tabNames = {"All Content", "Pages", "Banners", "Announcements", "FAQs",
//             "Resources", "Policies", "Notification Templates"};
//     private String selectedTab = "All Content";

//     private HBox buildTabsBar() {
//         tabsBarRef = new HBox(20);
//         tabsBarRef.setPadding(new Insets(0, 0, 12, 0));
//         for (String name : tabNames) {
//             Label tabLabel = new Label(name);
//             tabLabel.setFont(Font.font(13));
//             tabLabel.setCursor(javafx.scene.Cursor.HAND);
//             styleTabLabel(tabLabel, name.equals(selectedTab));
//             tabLabel.setOnMouseClicked(e -> selectTab(name));
//             tabsBarRef.getChildren().add(tabLabel);
//         }
//         return tabsBarRef;
//     }

//     private void styleTabLabel(Label label, boolean active) {
//         if (active) {
//             label.setTextFill(Color.web(GREEN));
//             label.setStyle("-fx-font-weight: bold; -fx-border-color: " + GREEN + "; -fx-border-width: 0 0 2 0; -fx-padding: 0 0 8 0;");
//         } else {
//             label.setTextFill(Color.web("#555555"));
//             label.setStyle("-fx-padding: 0 0 8 0;");
//         }
//     }

//     private void selectTab(String name) {
//         selectedTab = name;
//         for (var node : tabsBarRef.getChildren()) {
//             Label l = (Label) node;
//             styleTabLabel(l, l.getText().equals(name));
//         }
//         applyFilters();
//     }

//     private HBox buildFilterBar() {
//         HBox bar = new HBox(10);
//         bar.setPadding(new Insets(10, 0, 10, 0));
//         bar.setAlignment(Pos.CENTER_LEFT);

//         searchField = new TextField();
//         searchField.setPromptText("Search content by title or type...");
//         searchField.setPrefWidth(260);
//         searchField.textProperty().addListener((obs, oldV, newV) -> applyFilters());

//         typeFilter = new ComboBox<>(FXCollections.observableArrayList(
//                 "All Types", "Page", "Banner", "Announcement", "FAQ", "Resource"));
//         typeFilter.setValue("All Types");
//         typeFilter.setOnAction(e -> applyFilters());

//         statusFilter = new ComboBox<>(FXCollections.observableArrayList(
//                 "All Status", "Published", "Draft", "Scheduled", "Unpublished", "Archived"));
//         statusFilter.setValue("All Status");
//         statusFilter.setOnAction(e -> applyFilters());

//         Button dateButton = new Button("\uD83D\uDCC5 01/05/2025 - 29/05/2025");
//         dateButton.setOnAction(e -> infoAlert("Date Range", "Open a date range picker here."));

//         Button filterButton = new Button("\u25BE Filter");
//         filterButton.setOnAction(e -> applyFilters());

//         bar.getChildren().addAll(searchField, typeFilter, statusFilter, dateButton, filterButton);
//         return bar;
//     }

//     private TableView<ContentItem> buildTable() {
//         table = new TableView<>();
//         table.setItems(tableData);
//         table.setPrefHeight(360);
//         table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

//         TableColumn<ContentItem, String> titleCol = new TableColumn<>("Title");
//         titleCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle()));

//         TableColumn<ContentItem, String> typeCol = new TableColumn<>("Type");
//         typeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));

//         TableColumn<ContentItem, String> categoryCol = new TableColumn<>("Category");
//         categoryCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory()));

//         TableColumn<ContentItem, String> statusCol = new TableColumn<>("Status");
//         statusCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));

//         TableColumn<ContentItem, String> updatedCol = new TableColumn<>("Last Updated");
//         updatedCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLastUpdated()));

//         TableColumn<ContentItem, String> byCol = new TableColumn<>("Updated By");
//         byCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUpdatedBy()));

//         TableColumn<ContentItem, Void> actionsCol = new TableColumn<>("Actions");
//         actionsCol.setCellFactory(col -> new ActionsCell());

//         table.getColumns().addAll(titleCol, typeCol, categoryCol, statusCol, updatedCol, byCol, actionsCol);
//         applyFilters();
//         return table;
//     }

//     /** Table cell showing View / Edit / Duplicate / Delete buttons for each row. */
//     private class ActionsCell extends TableCell<ContentItem, Void> {
//         private final Button view = new Button("\uD83D\uDC41");
//         private final Button edit = new Button("\u270F");
//         private final Button duplicate = new Button("\u29C9");
//         private final Button delete = new Button("\uD83D\uDDD1");
//         private final HBox box = new HBox(6, view, edit, duplicate, delete);

//         ActionsCell() {
//             for (Button b : new Button[]{view, edit, duplicate, delete}) {
//                 b.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
//             }
//             view.setOnAction(e -> viewItem(getCurrentItem()));
//             edit.setOnAction(e -> editItem(getCurrentItem()));
//             duplicate.setOnAction(e -> duplicateItem(getCurrentItem()));
//             delete.setOnAction(e -> deleteItem(getCurrentItem()));
//         }

//         private ContentItem getCurrentItem() {
//             return getTableView().getItems().get(getIndex());
//         }

//         @Override
//         protected void updateItem(Void item, boolean empty) {
//             super.updateItem(item, empty);
//             setGraphic(empty ? null : box);
//         }
//     }

//     private void viewItem(ContentItem item) {
//         infoAlert(item.getTitle(), item.getDescription() + "\n\nType: " + item.getType() +
//                 "\nStatus: " + item.getStatus() + "\nUpdated by: " + item.getUpdatedBy());
//     }

//     private void editItem(ContentItem item) {
//         ChoiceDialog<String> dialog = new ChoiceDialog<>(item.getStatus(),
//                 "Published", "Draft", "Scheduled", "Unpublished", "Archived");
//         dialog.setTitle("Edit Content");
//         dialog.setHeaderText("Editing: " + item.getTitle());
//         dialog.setContentText("Status:");
//         Optional<String> result = dialog.showAndWait();
//         result.ifPresent(newStatus -> {
//             item.setStatus(newStatus);
//             table.refresh();
//             infoAlert("Saved", item.getTitle() + " status updated to " + newStatus + ".");
//         });
//     }

//     private void duplicateItem(ContentItem item) {
//         ContentItem copy = new ContentItem(item.getTitle() + " (Copy)", item.getDescription(),
//                 item.getType(), item.getCategory(), "Draft", "Today", "Super Admin");
//         allContent.add(copy);
//         applyFilters();
//         totalContentValue.setText(String.valueOf(allContent.size()));
//         infoAlert("Duplicated", "Created a copy of \"" + item.getTitle() + "\".");
//     }

//     private void deleteItem(ContentItem item) {
//         Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
//                 "Delete \"" + item.getTitle() + "\"? This cannot be undone.", ButtonType.YES, ButtonType.NO);
//         alert.setHeaderText(null);
//         alert.setTitle("Delete Content");
//         Optional<ButtonType> result = alert.showAndWait();
//         if (result.isPresent() && result.get() == ButtonType.YES) {
//             allContent.remove(item);
//             applyFilters();
//             totalContentValue.setText(String.valueOf(allContent.size()));
//         }
//     }

//     private HBox buildPagination() {
//         HBox bar = new HBox(8);
//         bar.setPadding(new Insets(12, 0, 0, 0));
//         bar.setAlignment(Pos.CENTER_LEFT);

//         paginationLabel = new Label("Showing 1 to 8 of " + allContent.size() + " contents");
//         paginationLabel.setFont(Font.font(12));
//         paginationLabel.setTextFill(Color.web("#666666"));

//         Region spacer = new Region();
//         HBox.setHgrow(spacer, Priority.ALWAYS);

//         Button prev = new Button("\u2039");
//         prev.setOnAction(e -> goToPage(currentPage - 1));

//         HBox pageNumbers = new HBox(4);
//         for (int i = 1; i <= 5; i++) {
//             pageNumbers.getChildren().add(pageNumberButton(i));
//         }
//         Label dots = new Label("...");
//         pageNumbers.getChildren().add(dots);
//         pageNumbers.getChildren().add(pageNumberButton(totalPages));

//         Button next = new Button("\u203A");
//         next.setOnAction(e -> goToPage(currentPage + 1));

//         bar.getChildren().addAll(paginationLabel, spacer, prev, pageNumbers, next);
//         return bar;
//     }

//     private Button pageNumberButton(int number) {
//         Button b = new Button(String.valueOf(number));
//         if (number == currentPage) {
//             b.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; -fx-background-radius: 4;");
//         }
//         b.setOnAction(e -> goToPage(number));
//         return b;
//     }

//     private void goToPage(int page) {
//         if (page < 1 || page > totalPages) return;
//         currentPage = page;
//         paginationLabel.setText("Showing page " + currentPage + " of " + totalPages + " (" + allContent.size() + " contents)");
//     }

//     // ---------- filtering ----------

//     private void applyFilters() {
//         String search = searchField == null ? "" : searchField.getText().toLowerCase().trim();
//         String type = typeFilter == null ? "All Types" : typeFilter.getValue();
//         String status = statusFilter == null ? "All Status" : statusFilter.getValue();

//         tableData.clear();
//         for (ContentItem item : allContent) {
//             boolean matchesTab = selectedTab.equals("All Content") ||
//                     (selectedTab.equals("Pages") && item.getType().equals("Page")) ||
//                     (selectedTab.equals("Banners") && item.getType().equals("Banner")) ||
//                     (selectedTab.equals("Announcements") && item.getType().equals("Announcement")) ||
//                     (selectedTab.equals("FAQs") && item.getType().equals("FAQ")) ||
//                     (selectedTab.equals("Resources") && item.getType().equals("Resource")) ||
//                     (selectedTab.equals("Policies") && item.getType().equals("Policy")) ||
//                     (selectedTab.equals("Notification Templates") && item.getType().equals("Template"));

//             boolean matchesSearch = search.isEmpty() || item.getTitle().toLowerCase().contains(search);
//             boolean matchesType = type.equals("All Types") || item.getType().equals(type);
//             boolean matchesStatus = status.equals("All Status") || item.getStatus().equals(status);

//             if (matchesTab && matchesSearch && matchesType && matchesStatus) {
//                 tableData.add(item);
//             }
//         }
//         if (paginationLabel != null) {
//             paginationLabel.setText("Showing 1 to " + tableData.size() + " of " + allContent.size() + " contents");
//         }
//     }

//     // ---------- right panel ----------

//     private VBox buildRightPanel() {
//         VBox panel = new VBox(16);

//         panel.getChildren().addAll(buildOverviewCard(), buildStatusCard(), buildQuickActionsCard());
//         return panel;
//     }

//     private VBox buildOverviewCard() {
//         VBox card = panelCard("Content Overview");

//         PieChart chart = new PieChart();
//         chart.getData().add(new PieChart.Data("Pages (14.1%)", 18));
//         chart.getData().add(new PieChart.Data("Banners (12.5%)", 16));
//         chart.getData().add(new PieChart.Data("Announcements (7.8%)", 10));
//         chart.getData().add(new PieChart.Data("FAQs (18.8%)", 24));
//         chart.getData().add(new PieChart.Data("Resources (23.4%)", 30));
//         chart.getData().add(new PieChart.Data("Others (23.4%)", 30));
//         chart.setLegendVisible(true);
//         chart.setLabelsVisible(false);
//         chart.setPrefHeight(220);
//         chart.setOnMouseClicked(e -> infoAlert("Content Overview", "Content breakdown by category."));

//         card.getChildren().add(chart);
//         return card;
//     }

//     private VBox buildStatusCard() {
//         VBox card = panelCard("Content Status");
//         String[][] rows = {
//                 {"Published", "78", "#2e9e5b"},
//                 {"Draft", "20", "#e0a536"},
//                 {"Scheduled", "15", "#3b82f6"},
//                 {"Unpublished", "10", "#9ca3af"},
//                 {"Archived", "5", "#ef4444"}
//         };
//         for (String[] row : rows) {
//             HBox line = new HBox(8);
//             line.setAlignment(Pos.CENTER_LEFT);
//             line.setPadding(new Insets(6, 0, 6, 0));
//             line.setCursor(javafx.scene.Cursor.HAND);

//             Label dot = new Label("\u25CF");
//             dot.setTextFill(Color.web(row[2]));

//             Label name = new Label(row[0]);
//             name.setFont(Font.font(13));

//             Region spacer = new Region();
//             HBox.setHgrow(spacer, Priority.ALWAYS);

//             Label count = new Label(row[1]);
//             count.setFont(Font.font("System", FontWeight.BOLD, 13));

//             line.getChildren().addAll(dot, name, spacer, count);
//             line.setOnMouseClicked(e -> {
//                 statusFilter.setValue(row[0].equals("Published") || row[0].equals("Draft")
//                         || row[0].equals("Scheduled") || row[0].equals("Unpublished")
//                         || row[0].equals("Archived") ? row[0] : "All Status");
//                 applyFilters();
//             });
//             card.getChildren().add(line);
//         }
//         return card;
//     }

//     private VBox buildQuickActionsCard() {
//         VBox card = panelCard("Quick Actions");
//         card.getChildren().add(quickActionButton("\uD83D\uDCC4 Add New Page", "#e8f3ee", () -> addNewContent("Page")));
//         card.getChildren().add(quickActionButton("\uD83D\uDDBC Add New Banner", "#e9f0fb", () -> addNewContent("Banner")));
//         card.getChildren().add(quickActionButton("\uD83D\uDCE2 Create Announcement", "#fdf1de", () -> addNewContent("Announcement")));
//         card.getChildren().add(quickActionButton("\u2753 Add FAQ", "#f2e9fb", () -> addNewContent("FAQ")));
//         return card;
//     }

//     private Button quickActionButton(String text, String bg, Runnable action) {
//         Button b = new Button(text);
//         b.setMaxWidth(Double.MAX_VALUE);
//         b.setAlignment(Pos.CENTER_LEFT);
//         b.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 8; -fx-padding: 10; -fx-font-weight: bold;");
//         b.setOnAction(e -> action.run());
//         VBox.setMargin(b, new Insets(4, 0, 4, 0));
//         return b;
//     }

//     private VBox panelCard(String title) {
//         VBox card = new VBox(10);
//         card.setPadding(new Insets(16));
//         card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
//                 "-fx-border-color: #eef0f2; -fx-border-radius: 10;");
//         Label heading = new Label(title);
//         heading.setFont(Font.font("System", FontWeight.BOLD, 15));
//         card.getChildren().add(heading);
//         return card;
//     }

//     // ---------- recent activity ----------

//     private HBox buildRecentActivity() {
//         VBox wrapper = new VBox(10);
//         Label heading = new Label("Recent Activity");
//         heading.setFont(Font.font("System", FontWeight.BOLD, 16));

//         HBox strip = new HBox(14);
//         String[][] activities = {
//                 {"Page Updated", "About Us page was updated", "29 May 2025, 10:30 AM", "Super Admin"},
//                 {"Banner Published", "Fresh Harvest Week Banner", "29 May 2025, 09:15 AM", "Content Manager"},
//                 {"Announcement Scheduled", "Upcoming Maintenance", "28 May 2025, 04:45 PM", "Super Admin"},
//                 {"FAQ Added", "New FAQ was added", "28 May 2025, 11:20 AM", "Content Editor"},
//                 {"Resource Published", "Organic Farming Guide", "27 May 2025, 03:10 PM", "Content Manager"}
//         };
//         for (String[] a : activities) {
//             strip.getChildren().add(activityCard(a[0], a[1], a[2], a[3]));
//         }

//         wrapper.getChildren().addAll(heading, strip);
//         return new HBox(wrapper);
//     }

//     private VBox activityCard(String title, String desc, String time, String by) {
//         VBox card = new VBox(4);
//         card.setPadding(new Insets(12));
//         card.setPrefWidth(220);
//         card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
//                 "-fx-border-color: #eef0f2; -fx-border-radius: 10;");
//         card.setCursor(javafx.scene.Cursor.HAND);

//         Label t = new Label(title);
//         t.setFont(Font.font("System", FontWeight.BOLD, 12));
//         Label d = new Label(desc);
//         d.setFont(Font.font(11));
//         d.setWrapText(true);
//         Label ti = new Label(time);
//         ti.setFont(Font.font(10));
//         ti.setTextFill(Color.web("#888888"));
//         Label b = new Label(by);
//         b.setFont(Font.font(10));
//         b.setTextFill(Color.web("#888888"));

//         card.getChildren().addAll(t, d, ti, b);
//         card.setOnMouseClicked(e -> infoAlert(title, desc + "\n" + time + "\n" + by));
//         return card;
//     }

//     // ---------- sample data ----------

//     private void loadSampleData() {
//         allContent.addAll(
//                 new ContentItem("Fresh Harvest Week Banner", "Limited time offer for fresh products",
//                         "Banner", "Promotion", "Published", "29 May 2025", "Super Admin"),
//                 new ContentItem("About Us", "Information about AgriLink platform",
//                         "Page", "General", "Published", "28 May 2025", "Super Admin"),
//                 new ContentItem("Upcoming Maintenance", "System maintenance announcement",
//                         "Announcement", "System", "Scheduled", "28 May 2025", "Super Admin"),
//                 new ContentItem("How to Sell Live on AgriLink", "Guide for live selling",
//                         "Resource", "Guide", "Published", "27 May 2025", "Content Manager"),
//                 new ContentItem("Frequently Asked Questions", "Common questions and answers",
//                         "FAQ", "Help", "Published", "27 May 2025", "Super Admin"),
//                 new ContentItem("Summer Sale - 20% Off", "Special discount on all equipment",
//                         "Banner", "Promotion", "Draft", "26 May 2025", "Content Editor"),
//                 new ContentItem("Terms & Conditions", "Platform terms and conditions",
//                         "Page", "Policy", "Published", "25 May 2025", "Super Admin"),
//                 new ContentItem("Organic Farming Guide", "Complete guide for organic farming",
//                         "Resource", "Guide", "Published", "25 May 2025", "Content Manager")
//         );
//     }

//     // ---------- helpers ----------

//     private void infoAlert(String title, String message) {
//         Alert alert = new Alert(Alert.AlertType.INFORMATION);
//         alert.setTitle(title);
//         alert.setHeaderText(null);
//         alert.setContentText(message);
//         alert.showAndWait();
//     }
// }