package com.mainproject.view.buyer;

import com.mainproject.model.User;
import com.mainproject.util.ResponsiveLayout;
import com.mainproject.view.LoginScreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BuyerDashboard {

    private final User currentUser;

    private Scene scene;
    private BorderPane mainLayout;

    private String currentPage = "Dashboard";
    private String buyerEmail;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BuyerDashboard(User user) {

        this.currentUser = user;

        System.out.println("====================================");
        System.out.println("Opening Buyer Dashboard");

        if (user != null) {

            System.out.println(
                    "Buyer Name  : " + user.getFullName()
            );

            System.out.println(
                    "Buyer Email : " + user.getEmail()
            );

            System.out.println(
                    "Buyer Role  : " + user.getRole()
            );
        }

        System.out.println("====================================");
    }
    // =====================================================
// GET LOGGED-IN BUYER EMAIL
// =====================================================

public String getUserEmail1() {
    return buyerEmail;
}

    // =====================================================
    // GET CURRENT USER
    // =====================================================

    public User getCurrentUser() {
        return currentUser;
    }

    // =====================================================
    // GET BUYER EMAIL
    // =====================================================

    public String getBuyerEmail() {

        if (currentUser == null) {
            return "";
        }

        String email = currentUser.getEmail();

        return email == null ? "" : email;
    }

    // =====================================================
    // GET USER EMAIL
    // FIX FOR NOTIFICATION.JAVA
    // =====================================================

    public String getUserEmail() {
        return getBuyerEmail();
    }

    // =====================================================
    // GET BUYER NAME
    // =====================================================

    public String getBuyerName() {

        if (currentUser == null) {
            return "Buyer";
        }

        String name = currentUser.getFullName();

        if (name == null || name.trim().isEmpty()) {
            return "Buyer";
        }

        return name;
    }

    // =====================================================
    // GET SCENE
    // =====================================================

    public Scene getScene() {

        if (scene == null) {

            mainLayout = new BorderPane();

            mainLayout.setStyle(
                    "-fx-background-color:#F8FAFC;"
            );

            mainLayout.setLeft(
                    createSidebar()
            );

            scene = ResponsiveLayout.createScene(mainLayout);

            navigateTo("Dashboard");
        }

        return scene;
    }

    // =====================================================
    // BUYER DASHBOARD SCENE
    // =====================================================

    public Scene getBuyerDashboardScene() {
        return getScene();
    }

    // =====================================================
    // CHANGE CENTER VIEW
    // =====================================================

    public void setView(Node viewNode) {

        if (mainLayout == null || viewNode == null) {
            return;
        }

        mainLayout.setCenter(ResponsiveLayout.scrollPage(viewNode));
    }

    // =====================================================
    // CURRENT PAGE
    // =====================================================

    public void setCurrentPage(String page) {

        if (page != null && !page.trim().isEmpty()) {
            currentPage = page;
        }
    }

    public String getCurrentPage() {
        return currentPage;
    }

    // =====================================================
    // NAVIGATION
    // =====================================================

    public void navigateTo(String page) {

        if (page == null || page.trim().isEmpty()) {
            page = "Dashboard";
        }

        currentPage = page;

        try {

            Node viewNode;

            switch (page) {

                // =============================================
                // DASHBOARD
                // =============================================

                case "Dashboard":

                    viewNode =
                            new DashboardOverview(this)
                                    .getView();

                    break;

                // =============================================
                // LIVE NOW
                // =============================================

                

                // =============================================
                // MARKETPLACE
                // =============================================

                case "Marketplace":

                    viewNode =
                            new LiveMarketplace(this)
                                    .getView();

                    break;

                // =============================================
                // MY ORDERS
                // =============================================

                case "My Orders":

                    viewNode =
                            new MyOrders(this)
                                    .getView();

                    break;

                // =============================================
                // WISHLIST
                // =============================================

                case "Wishlist":

                    viewNode =
                            new Wishlist(this)
                                    .getView();

                    break;

                // =============================================
                // FARMERS
                // =============================================

                

                // =============================================
                // SEARCH & RENT
                // =============================================

                case "Search & Rent":

                    viewNode =
                            new SearchAndRent(this)
                                    .getView();

                    break;

                // =============================================
                // MY RENTAL REQUESTS
                // =============================================

                case "My Rental Requests":

                    viewNode =
                            new MyRentalRequests(this)
                                    .getView();

                    break;

                // =============================================
                // CROP PRICES
                // =============================================

                case "Crop Prices":

                    viewNode =
                            new CropPrices(this)
                                    .getView();

                    break;

                // =============================================
                // AI RECOMMENDATIONS
                // =============================================

                case "AI Recommendations":

                    viewNode =
                            new AiRecommendations(this)
                                    .getView();

                    break;

                // =============================================
                // MESSAGES
                // =============================================

                case "Messages":

                    viewNode =
                            new FarmerChatList(this)
                                    .getView();

                    break;

                // =============================================
                // NOTIFICATIONS
                // =============================================

                case "Notifications":

                    viewNode =
                            new Notification(this)
                                    .getView();

                    break;

                // =============================================
                // VOICE ASSISTANT
                // =============================================

                case "Voice Assistant":

                    viewNode =
                            new VoiceAssistant(this)
                                    .getView();

                    break;

                // =============================================
                // SETTINGS
                // =============================================

                case "Settings":

                    viewNode =
                            new Settings(
                                    this,
                                    new Runnable() {

                                        @Override
                                        public void run() {

                                            setCurrentPage(
                                                    "Settings"
                                            );

                                            refreshLanguage();
                                        }
                                    }
                            ).getView();

                    break;

                // =============================================
                // DEFAULT
                // =============================================

                default:

                    currentPage = "Dashboard";

                    viewNode =
                            new DashboardOverview(this)
                                    .getView();

                    break;
            }

            // =============================================
            // SET CENTER VIEW
            // =============================================

            setView(viewNode);

            // =============================================
            // REFRESH SIDEBAR
            // =============================================

            if (mainLayout != null) {

                mainLayout.setLeft(
                        createSidebar()
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Error opening page: " + page
            );

            e.printStackTrace();

            System.out.println(
                    "===================================="
            );

            showErrorPage(
                    "Unable to open " + page,
                    "Please check the page class and try again."
            );
        }
    }

    // =====================================================
    // REFRESH LANGUAGE
    // =====================================================

    public void refreshLanguage() {

        if (mainLayout == null) {
            return;
        }

        String page = currentPage;

        mainLayout.setLeft(
                createSidebar()
        );

        navigateTo(page);
    }

    // =====================================================
    // ERROR PAGE
    // =====================================================

    private void showErrorPage(
            String titleText,
            String messageText) {

        VBox root = new VBox(12);

        root.setPadding(
                new Insets(40)
        );

        root.setAlignment(
                Pos.CENTER
        );

        Label title =
                new Label(titleText);

        title.setStyle(
                "-fx-font-size:22px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#B91C1C;"
        );

        Label message =
                new Label(messageText);

        message.setStyle(
                "-fx-font-size:14px;" +
                "-fx-text-fill:#64748B;"
        );

        Button retry =
                new Button("← Back to Dashboard");

        retry.setStyle(
                "-fx-background-color:#166534;" +
                "-fx-text-fill:white;" +
                "-fx-font-weight:bold;" +
                "-fx-padding:10 18;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );

        retry.setOnAction(
                e -> navigateTo("Dashboard")
        );

        root.getChildren().addAll(
                title,
                message,
                retry
        );

        setView(root);
    }

    // =====================================================
    // SIDEBAR
    // =====================================================

    private VBox createSidebar() {

        VBox sidebar =
                new VBox(10);

        sidebar.setPrefWidth(260);
        sidebar.setMinWidth(260);
        sidebar.setMaxWidth(260);

        sidebar.setPadding(
                new Insets(
                        20,
                        12,
                        18,
                        12
                )
        );

        sidebar.setStyle(
                "-fx-background-color:#FFFFFF;" +
                "-fx-border-color:#E2E8F0;" +
                "-fx-border-width:0 1 0 0;"
        );

        // =================================================
        // BRAND
        // =================================================

        Label brandLabel =
                new Label("🌿 AgriVerse");

        brandLabel.setStyle(
                "-fx-font-size:25px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#166534;"
        );

        brandLabel.setPadding(
                new Insets(
                        4,
                        8,
                        12,
                        8
                )
        );

        Label accountType =
                new Label("BUYER PORTAL");

        accountType.setStyle(
                "-fx-font-size:10px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#94A3B8;" +
                "-fx-padding:0 8 8 8;"
        );

        // =================================================
        // USER CARD
        // =================================================

        VBox userCard =
                new VBox(3);

        userCard.setPadding(
                new Insets(12)
        );

        userCard.setStyle(
                "-fx-background-color:#F0FDF4;" +
                "-fx-background-radius:10;" +
                "-fx-border-color:#DCFCE7;" +
                "-fx-border-radius:10;"
        );

        Label welcome =
                new Label("Welcome back,");

        welcome.setStyle(
                "-fx-font-size:11px;" +
                "-fx-text-fill:#64748B;"
        );

        Label buyerName =
                new Label(getBuyerName());

        buyerName.setStyle(
                "-fx-font-size:14px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#166534;"
        );

        userCard.getChildren().addAll(
                welcome,
                buyerName
        );

        // =================================================
        // NAVIGATION ITEMS
        // =================================================

        VBox navItems =
                new VBox(4);

        // =================================================
        // OVERVIEW
        // =================================================

        addSectionLabel(
                navItems,
                "OVERVIEW"
        );

        navItems.getChildren().addAll(

                createNavButton(
                        "📊",
                        "Dashboard"
                ),

               

                createNavButton(
                        "🏪",
                        "Marketplace"
                ),

                createNavButton(
                        "📦",
                        "My Orders"
                ),

                createNavButton(
                        "❤️",
                        "Wishlist"
                )
        );

        // =================================================
        // EXPLORE
        // =================================================

        addSectionLabel(
                navItems,
                "EXPLORE"
        );

        navItems.getChildren().addAll(

               

                createNavButton(
                        "🚜",
                        "Search & Rent"
                ),

                createNavButton(
                        "📋",
                        "My Rental Requests"
                ),

                createNavButton(
                        "📈",
                        "Crop Prices"
                ),

                createNavButton(
                        "✨",
                        "AI Recommendations"
                )
        );

        // =================================================
        // ACCOUNT
        // =================================================

        addSectionLabel(
                navItems,
                "ACCOUNT"
        );

        navItems.getChildren().addAll(

                createNavButton(
                        "💬",
                        "Messages"
                ),

                createNavButton(
                        "🔔",
                        "Notifications"
                ),

                createNavButton(
                        "🎙️",
                        "Voice Assistant"
                ),

                createNavButton(
                        "⚙️",
                        "Settings"
                )
        );

        // =================================================
        // NAVIGATION SCROLL
        // =================================================

        ScrollPane navScroll =
                new ScrollPane(navItems);

        navScroll.setFitToWidth(true);

        navScroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        navScroll.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-background:transparent;" +
                "-fx-border-color:transparent;" +
                "-fx-padding:0;"
        );

        VBox.setVgrow(
                navScroll,
                Priority.ALWAYS
        );

        // =================================================
        // LOGOUT
        // =================================================

        Button btnLogout =
                new Button("🚪   Logout");

        btnLogout.setMaxWidth(
                Double.MAX_VALUE
        );

        btnLogout.setAlignment(
                Pos.CENTER
        );

        applyLogoutStyle(
                btnLogout,
                false
        );

        btnLogout.setOnMouseEntered(
                e -> applyLogoutStyle(
                        btnLogout,
                        true
                )
        );

        btnLogout.setOnMouseExited(
                e -> applyLogoutStyle(
                        btnLogout,
                        false
                )
        );

        btnLogout.setOnAction(e -> {

            System.out.println(
                    "Buyer logout clicked."
            );

            // Navigate back to the login screen without closing
            // the JavaFX application window.
            if (LoginScreen.Homestage != null) {
                LoginScreen.logoutToLogin();
            } else if (scene != null && scene.getWindow() instanceof Stage) {
                new LoginScreen().start((Stage) scene.getWindow());
            }
        });

        // =================================================
        // ADD ALL
        // =================================================

        sidebar.getChildren().addAll(
                brandLabel,
                accountType,
                userCard,
                navScroll,
                btnLogout
        );


        return sidebar;
    }

    // =====================================================
    // SECTION LABEL
    // =====================================================

    private void addSectionLabel(
            VBox container,
            String text) {

        Label label =
                new Label(text);

        label.setStyle(
                "-fx-font-size:10px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#94A3B8;" +
                "-fx-padding:12 10 5 10;"
        );

        container.getChildren().add(label);
    }

    // =====================================================
    // NAVIGATION BUTTON
    // =====================================================

    private Button createNavButton(
            String icon,
            String title) {

        Button btn =
                new Button(
                        icon + "   " + title
                );

        btn.setMaxWidth(
                Double.MAX_VALUE
        );

        btn.setAlignment(
                Pos.CENTER_LEFT
        );

        boolean active =
                title.equalsIgnoreCase(
                        currentPage
                );

        applyNavButtonStyle(
                btn,
                active
        );

        btn.setOnMouseEntered(e -> {

            if (!title.equalsIgnoreCase(currentPage)) {

                applyNavHoverStyle(
                        btn
                );
            }
        });

        btn.setOnMouseExited(e -> {

            if (!title.equalsIgnoreCase(currentPage)) {

                applyNavButtonStyle(
                        btn,
                        false
                );
            }
        });

        btn.setOnAction(
                e -> navigateTo(title)
        );

        return btn;
    }

    // =====================================================
    // NAV BUTTON STYLE
    // =====================================================

    private void applyNavButtonStyle(
            Button button,
            boolean active) {

        if (active) {

            button.setStyle(
                    "-fx-background-color:#DCFCE7;" +
                    "-fx-text-fill:#166534;" +
                    "-fx-font-size:13px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:10 14;" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;"
            );

        } else {

            button.setStyle(
                    "-fx-background-color:transparent;" +
                    "-fx-text-fill:#334155;" +
                    "-fx-font-size:13px;" +
                    "-fx-padding:10 14;" +
                    "-fx-background-radius:8;" +
                    "-fx-cursor:hand;"
            );
        }
    }

    // =====================================================
    // NAV HOVER STYLE
    // =====================================================

    private void applyNavHoverStyle(
            Button button) {

        button.setStyle(
                "-fx-background-color:#F1F5F9;" +
                "-fx-text-fill:#166534;" +
                "-fx-font-size:13px;" +
                "-fx-padding:10 14;" +
                "-fx-background-radius:8;" +
                "-fx-cursor:hand;"
        );
    }

    // =====================================================
    // LOGOUT STYLE
    // =====================================================

    private void applyLogoutStyle(
            Button button,
            boolean hover) {

        if (hover) {

            button.setStyle(
                    "-fx-background-color:#FEE2E2;" +
                    "-fx-text-fill:#B91C1C;" +
                    "-fx-font-size:13px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:11 14;" +
                    "-fx-background-radius:9;" +
                    "-fx-cursor:hand;"
            );

        } else {

            button.setStyle(
                    "-fx-background-color:#FEF2F2;" +
                    "-fx-text-fill:#DC2626;" +
                    "-fx-font-size:13px;" +
                    "-fx-font-weight:bold;" +
                    "-fx-padding:11 14;" +
                    "-fx-background-radius:9;" +
                    "-fx-cursor:hand;"
            );
        }
    }
}