package com.mainproject.view.farmer;

import com.mainproject.util.ResponsiveLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

import com.mainproject.model.Product;
import com.mainproject.model.User;
import com.mainproject.model.CartItem;
import com.mainproject.model.EquipmentRental;
import com.mainproject.controller.CartController;
import com.mainproject.controller.EquipmentRentalController;
import com.mainproject.view.LoginScreen;
import com.mainproject.controller.NotificationController;
import com.google.cloud.firestore.ListenerRegistration;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * ============================================================
 * FARMER DASHBOARD
 * ============================================================
 */
public class FarmerDashboard {

    // =========================================================
    // VARIABLES
    // =========================================================

    private Scene scene;

    private StackPane contentArea;

    private String currentPage = "Dashboard";

    private final Map<String, ToggleButton> navMap = new HashMap<>();

    private ToggleGroup navGroup;

    private final User user;

    private BorderPane root;

    // Notification badge references (notification functionality only)
    private Label notificationBadge;
    private ListenerRegistration notificationListener;
    private final NotificationController notificationController = new NotificationController();

    /**
     * Logged-in farmer email.
     */
    private final String farmerEmail;

    // =========================================================
    // COLORS
    // =========================================================

    private static final String MAIN_BG = "#E9F7EF";

    private static final String PANEL_BG = "#D4EFDF";

    private static final String PRIMARY_GREEN = "#117864";

    private static final String PRIMARY_TEXT = "#1B2631";

    private static final String BORDER_COLOR = "#A2D9CE";

    private static final String ACCENT_YELLOW = "#F1C40F";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public FarmerDashboard(User user) {

        if (user == null) {

            throw new IllegalArgumentException(
                    "User cannot be null.");
        }

        this.user = user;

        /*
         * Store logged-in user's email.
         */
        this.farmerEmail = user.getEmail();

        System.out.println(
                "====================================");

        System.out.println(
                "Opening Farmer Dashboard...");

        System.out.println(
                "Farmer Name: "
                        + user.getFullName());

        System.out.println(
                "Farmer Email: "
                        + farmerEmail);

        System.out.println(
                "Farmer Role: "
                        + user.getRole());

        System.out.println(
                "====================================");
    }

    // =========================================================
    // GET SCENE
    // =========================================================

    public Scene getScene() {

        if (scene == null) {

            scene = getFarmerDashboardScene();
        }

        return scene;
    }

    // =========================================================
    // CREATE DASHBOARD SCENE
    // =========================================================

    public Scene getFarmerDashboardScene() {

        Rectangle2D screen = Screen.getPrimary()
                .getVisualBounds();

        root = new BorderPane();

        root.setStyle(
                "-fx-background-color: "
                        + MAIN_BG
                        + ";");

        // =====================================================
        // TOP BAR
        // =====================================================

        root.setTop(
                createTopBar());

        // =====================================================
        // SIDEBAR
        // =====================================================

        root.setLeft(
                createSidebar());

        // =====================================================
        // CONTENT AREA
        // =====================================================

        contentArea = new StackPane();

        contentArea.setPadding(
                new Insets(
                        18,
                        22,
                        20,
                        22));

        root.setCenter(
                contentArea);

        // =====================================================
        // DEFAULT PAGE
        // =====================================================

        navigateTo(
                "Dashboard");

        // =====================================================
        // SCENE
        // =====================================================

        scene = ResponsiveLayout.createScene(root);

        return scene;
    }

    // =========================================================
    // TOP BAR
    // =========================================================

    private HBox createTopBar() {

        HBox topBar = new HBox(15);

        topBar.setAlignment(
                Pos.CENTER_LEFT);

        topBar.setPadding(
                new Insets(
                        10,
                        25,
                        10,
                        20));

        topBar.setPrefHeight(
                80);

        topBar.setStyle(
                "-fx-background-color: #FFFFFF;"
                        + "-fx-border-color: "
                        + BORDER_COLOR
                        + ";"
                        + "-fx-border-width: "
                        + "0 0 1.5px 0;");

        // =====================================================
        // LOGO
        // =====================================================

        ImageView logo = new ImageView();

        try {

            Image image = new Image(
                    getClass()
                            .getResourceAsStream(
                                    "/assets/icons/AgriLinklogo.png"));

            if (!image.isError()) {

                logo.setImage(
                        image);

                logo.setFitWidth(
                        260);

                logo.setFitHeight(
                        65);

                logo.setPreserveRatio(
                        true);
            }

        } catch (Exception e) {

            System.out.println(
                    "AgriLink logo not found.");
        }

        // =====================================================
        // SPACER
        // =====================================================

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        // =====================================================
        // NOTIFICATION
        // =====================================================

        StackPane notification = new StackPane();

        notification.setPrefSize(
                40,
                40);

        notification.setStyle(
                "-fx-cursor: hand;");

        Label bell = new Label("🔔");

        bell.setStyle(
                "-fx-font-size: 20px;");

        notificationBadge = new Label("0");

        notificationBadge.setMinSize(18, 18);
        notificationBadge.setAlignment(Pos.CENTER);
        notificationBadge.setTranslateX(10);
        notificationBadge.setTranslateY(-10);
        notificationBadge.setStyle(
                "-fx-background-color: " + ACCENT_YELLOW + ";" +
                "-fx-text-fill: #1B2631;" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10px;");

        notificationBadge.setVisible(false);
        notificationBadge.setManaged(false);

        notification.getChildren()
                .addAll(
                        bell,
                        notificationBadge);

        startNotificationListener();

        notification.setOnMouseClicked(
                e -> navigateTo(
                        "Notifications"));

        // =====================================================
        // USER PROFILE
        // =====================================================

        HBox userBox = new HBox(10);

        userBox.setAlignment(
                Pos.CENTER);

        userBox.setStyle(
                "-fx-cursor: hand;");

        StackPane avatar = createProfileAvatar(
                20);

        String displayName = safe(
                user.getFullName());

        if (displayName.isEmpty()) {

            displayName = "Farmer";
        }

        Label farmerName = new Label(
                displayName
                        + " ▼");

        farmerName.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + PRIMARY_TEXT
                        + ";");

        userBox.getChildren()
                .addAll(
                        avatar,
                        farmerName);

        userBox.setOnMouseClicked(
                e -> navigateTo(
                        "Profile"));

        // =====================================================
        // ADD TO TOP BAR
        // =====================================================

        topBar.getChildren()
                .addAll(
                        logo,
                        spacer,
                        notification,
                        userBox);

        return topBar;
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private VBox createSidebar() {

        VBox sidebar = new VBox(5);

        sidebar.setPrefWidth(
                235);

        sidebar.setPadding(
                new Insets(
                        15,
                        10,
                        15,
                        10));

        sidebar.setStyle(
                "-fx-background-color: #FFFFFF;"
                        + "-fx-border-color: "
                        + BORDER_COLOR
                        + ";"
                        + "-fx-border-width: "
                        + "0 1.5px 0 0;");

        // =====================================================
        // FARMER INFORMATION
        // =====================================================

        HBox farmerInfo = new HBox(10);

        farmerInfo.setAlignment(
                Pos.CENTER_LEFT);

        farmerInfo.setPadding(
                new Insets(
                        5,
                        10,
                        18,
                        10));

        StackPane farmerAvatar = createProfileAvatar(
                22);

        VBox farmerDetails = new VBox(2);

        String nameValue = safe(
                user.getFullName());

        if (nameValue.isEmpty()) {

            nameValue = "Farmer";
        }

        Label name = new Label(
                nameValue);

        name.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + PRIMARY_TEXT
                        + ";");

        String roleValue = safe(
                user.getRole());

        if (roleValue.isEmpty()) {

            roleValue = "Farmer";
        }

        Label role = new Label(
                roleValue);

        role.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-text-fill: #64748B;");

        farmerDetails.getChildren()
                .addAll(
                        name,
                        role);

        farmerInfo.getChildren()
                .addAll(
                        farmerAvatar,
                        farmerDetails);

        sidebar.getChildren()
                .add(
                        farmerInfo);

        // =====================================================
        // NAVIGATION GROUP
        // =====================================================

        navGroup = new ToggleGroup();

        // =====================================================
        // DASHBOARD
        // =====================================================

        addNavigation(
                sidebar,
                "Dashboard",
                "🏠");

        // =====================================================
        // PRODUCTS
        // =====================================================

        addNavigation(
                sidebar,
                "Products",
                "🎁");

        // =====================================================
        // ORDERS
        // =====================================================

        addNavigation(
                sidebar,
                "Orders",
                "📦");

        // =====================================================
        // EQUIPMENT RENTAL
        // =====================================================

        addNavigation(
                sidebar,
                "Equipment Rental",
                "🚜");

        // =====================================================
        // RENTAL REQUESTS - NEW
        // =====================================================

        addNavigation(
                sidebar,
                "Rental Requests",
                "📩");

        // =====================================================
        // MY CART
        // =====================================================

        addNavigation(
                sidebar,
                "My Cart",
                "🛒");

        // =====================================================
        // MY EQUIPMENT
        // =====================================================

        addNavigation(
                sidebar,
                "My Equipment",
                "⚙");

        // =====================================================
        // MARKETPLACE
        // =====================================================

        addNavigation(
                sidebar,
                "Marketplace",
                "🏪");

        // =====================================================
        // WEATHER
        // =====================================================

        addNavigation(
                sidebar,
                "Weather",
                "🌤");

        // =====================================================
        // AI RECOMMENDATIONS
        // =====================================================

        addNavigation(
                sidebar,
                "AI Recommendations",
                "✨");

        // =====================================================
        // MESSAGES
        // =====================================================

        addNavigation(
                sidebar,
                "Messages",
                "💬");

        // =====================================================
        // NOTIFICATIONS
        // =====================================================

        addNavigation(
                sidebar,
                "Notifications",
                "🔔");

        // =====================================================
        // PROFILE
        // =====================================================

        addNavigation(
                sidebar,
                "Profile",
                "👤");

        // =====================================================
        // SETTINGS
        // =====================================================

        addNavigation(
                sidebar,
                "Settings",
                "⚙");

        // =====================================================
        // LOGOUT
        // =====================================================

        addNavigation(
                sidebar,
                "Logout",
                "🚪");

        return sidebar;
    }

    // =========================================================
    // ADD NAVIGATION BUTTON
    // =========================================================

    private void addNavigation(
            VBox sidebar,
            String title,
            String emoji) {

        ToggleButton button = createNavigationButton(
                title,
                emoji);

        navMap.put(
                title,
                button);

        sidebar.getChildren()
                .add(
                        button);
    }

    // =========================================================
    // CREATE NAVIGATION BUTTON
    // =========================================================

    private ToggleButton createNavigationButton(
            String title,
            String emoji) {

        Label icon = new Label(
                emoji);

        icon.setStyle(
                "-fx-font-size: 18px;");

        Label text = new Label(
                title);

        text.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-text-fill: "
                        + PRIMARY_TEXT
                        + ";");

        HBox content = new HBox(
                12,
                icon,
                text);

        content.setAlignment(
                Pos.CENTER_LEFT);

        ToggleButton button = new ToggleButton();

        button.setGraphic(
                content);

        button.setToggleGroup(
                navGroup);

        button.setMaxWidth(
                Double.MAX_VALUE);

        button.setPrefHeight(
                42);

        button.setPadding(
                new Insets(
                        0,
                        10,
                        0,
                        14));

        button.setFocusTraversable(
                false);

        Runnable updateStyle = () -> {

            if (button.isSelected()) {

                button.setStyle(
                        "-fx-background-color: "
                                + PRIMARY_GREEN
                                + ";"
                                + "-fx-background-radius: 9px;"
                                + "-fx-cursor: hand;");

                text.setStyle(
                        "-fx-font-size: 13px;"
                                + "-fx-font-weight: bold;"
                                + "-fx-text-fill: white;");

            } else {

                button.setStyle(
                        "-fx-background-color: transparent;"
                                + "-fx-background-radius: 9px;"
                                + "-fx-cursor: hand;");

                text.setStyle(
                        "-fx-font-size: 13px;"
                                + "-fx-text-fill: "
                                + PRIMARY_TEXT
                                + ";");
            }
        };

        button.selectedProperty()
                .addListener(
                        (obs,
                         oldValue,
                         newValue) -> updateStyle.run());

        updateStyle.run();

        // =====================================================
        // HOVER
        // =====================================================

        button.setOnMouseEntered(
                e -> {

                    if (!button.isSelected()) {

                        button.setStyle(
                                "-fx-background-color: "
                                        + PANEL_BG
                                        + ";"
                                        + "-fx-background-radius: 9px;"
                                        + "-fx-cursor: hand;");
                    }
                });

        button.setOnMouseExited(
                e -> {

                    if (!button.isSelected()) {

                        button.setStyle(
                                "-fx-background-color: transparent;"
                                        + "-fx-background-radius: 9px;"
                                        + "-fx-cursor: hand;");
                    }
                });

        // =====================================================
        // NAVIGATION
        // =====================================================

        button.setOnAction(
                e -> navigateTo(
                        title));

        return button;
    }

    // =========================================================
    // NAVIGATION
    // =========================================================

    public void navigateTo(
            String page) {

        currentPage = page;

        System.out.println(
                "Navigating to: "
                        + page);

        // =====================================================
        // UPDATE SELECTED SIDEBAR BUTTON
        // =====================================================

        if (navMap.containsKey(page)) {

            navMap.get(page)
                    .setSelected(
                            true);
        }

        Node viewNode;

        // =====================================================
        // SWITCH
        // =====================================================

        switch (page) {

            // =================================================
            // DASHBOARD
            // =================================================

            case "Dashboard":

                viewNode = new DashboardOverview(
                        this)
                        .getView();

                break;

            // =================================================
            // PRODUCTS
            // =================================================

            case "Products":

                viewNode = new Products(
                        this,
                        farmerEmail)
                        .getView();

                break;

            // =================================================
            // ADD PRODUCT
            // =================================================

            case "AddProduct":

                viewNode = new AddProduct(
                        this,
                        farmerEmail)
                        .getView();

                break;

            // =================================================
            // ORDERS
            // =================================================

            case "Orders":

                viewNode = new Orders(farmerEmail)
                        .getView();

                break;

            // =================================================
            // MARKETPLACE
            // =================================================

            case "Marketplace":

                viewNode = new CropPrices(
                        this)
                        .getView();

                break;

            // =================================================
            // EQUIPMENT RENTAL
            // =================================================

            case "Equipment Rental":

                viewNode = new com.mainproject.view.farmer.EquipmentRental(
                        farmerEmail,
                        safe(
                                user.getFullName()),
                        () -> navigateTo(
                                "AddEquipment"))
                        .getView();

                break;

            // =================================================
            // RENTAL REQUESTS - NEW
            // =================================================

            case "Rental Requests":

                viewNode = new RentalRequests(
                        farmerEmail)
                        .getView();

                break;

            // =================================================
            // MY CART
            // =================================================

            case "My Cart":

                viewNode = new Cart(
                        farmerEmail,
                        safe(
                                user.getFullName()),

                        () -> navigateTo(
                                "Equipment Rental"),

                        () -> processEquipmentCheckout())
                        .getView();

                break;

            // =================================================
            // MY EQUIPMENT
            // =================================================

            case "My Equipment":

                viewNode = new MyEquipment(
                        farmerEmail,
                        safe(
                                user.getFullName()),

                        () -> navigateTo(
                                "AddEquipment"),

                        () -> navigateTo(
                                "Equipment Rental"))
                        .getView();

                break;

            // =================================================
            // ADD EQUIPMENT
            // =================================================

            case "AddEquipment":

                viewNode = new AddEquipment(
                        farmerEmail,
                        safe(
                                user.getFullName()),

                        () -> navigateTo(
                                "Equipment Rental"))
                        .getView();

                break;

            // =================================================
            // WEATHER
            // =================================================

            case "Weather":

                viewNode = new Weather(farmerEmail)
                        .getView();

                break;

            // =================================================
            // AI RECOMMENDATIONS
            // =================================================

            case "AI Recommendations":

                viewNode = new AiRecommendations(farmerEmail)
                        .getView();

                break;

            // =================================================
            // MESSAGES
            // =================================================

            case "Messages":

                viewNode = new BuyerChatList(
                        this)
                        .getView();

                break;

            // =================================================
            // NOTIFICATIONS
            // =================================================

            case "Notifications":

                viewNode = new Notifications(
                        this,
                        farmerEmail)
                        .getView();

                break;

            // =================================================
            // PROFILE
            // =================================================

            case "Profile":

                System.out.println(
                        "Opening Profile for: "
                                + farmerEmail);

                viewNode = new Profile(
                        farmerEmail)
                        .getView();

                break;

            // =================================================
            // SETTINGS
            // =================================================

            case "Settings":

                viewNode = new Settings(
                        farmerEmail,
                        new Runnable() {

                            @Override
                            public void run() {

                                refreshLanguage();
                            }
                        })
                        .getView();

                break;

            // =================================================
            // LOGOUT
            // =================================================

            case "Logout":

                handleLogout();

                return;

            // =================================================
            // DEFAULT
            // =================================================

            default:

                System.out.println(
                        "Unknown page: "
                                + page);

                viewNode = new DashboardOverview(
                        this)
                        .getView();

                break;
        }

        // =====================================================
        // DISPLAY VIEW
        // =====================================================

        if (contentArea != null) {

            contentArea
                    .getChildren()
                    .setAll(
                            ResponsiveLayout.scrollPage(viewNode));
        }
    }

    // =========================================================
    // REFRESH AFTER LANGUAGE CHANGE
    // =========================================================

    public void refreshLanguage() {

        if (root != null) {

            root.setLeft(
                    createSidebar());
        }

        String page = currentPage == null
                ? "Dashboard"
                : currentPage;

        navigateTo(page);
    }

    // =========================================================
    // EQUIPMENT RENTAL NAVIGATION
    // =========================================================

    public void navigateToEquipmentRental() {

        navigateTo(
                "Equipment Rental");
    }

    // =========================================================
    // RENTAL REQUESTS NAVIGATION - NEW
    // =========================================================

    public void navigateToRentalRequests() {

        navigateTo(
                "Rental Requests");
    }

    // =========================================================
    // ADD EQUIPMENT NAVIGATION
    // =========================================================

    public void navigateToAddEquipment() {

        navigateTo(
                "AddEquipment");
    }

    // =========================================================
    // PRODUCTS NAVIGATION
    // =========================================================

    public void navigateToProducts() {

        navigateTo(
                "Products");
    }

    // =========================================================
    // MARKETPLACE NAVIGATION
    // =========================================================

    public void navigateToMarketplace() {

        navigateTo(
                "Marketplace");
    }

    // =========================================================
    // CROP PRICES NAVIGATION
    // =========================================================

    public void navigateToCropPrices() {

        navigateTo(
                "Crop Prices");
    }

    // =========================================================
    // EDIT PRODUCT NAVIGATION
    // =========================================================

    public void navigateToEditProduct(
            Product product) {

        if (product == null) {

            System.out.println(
                    "Cannot edit product.");

            return;
        }

        System.out.println(
                "====================================");

        System.out.println(
                "Opening Edit Product...");

        System.out.println(
                "Product ID: "
                        + product.getProductId());

        System.out.println(
                "Product Name: "
                        + product.getName());

        System.out.println(
                "====================================");

        EditProduct editProduct = new EditProduct(
                this,
                product);

        Node editView = editProduct.getView();

        if (contentArea != null) {

            contentArea
                    .getChildren()
                    .setAll(
                            editView);
        }
    }

    // =========================================================
    // EQUIPMENT CART CHECKOUT
    // =========================================================

    private void processEquipmentCheckout() {

        List<CartItem> items = new CartController()
                .getCartItems(farmerEmail);

        if (items == null || items.isEmpty()) {
            showDashboardAlert(
                    javafx.scene.control.Alert.AlertType.WARNING,
                    "Empty Cart",
                    "Your equipment rental cart is empty.");
            return;
        }

        EquipmentRentalController rentalController =
                new EquipmentRentalController();
        CartController cartController = new CartController();

        int successful = 0;
        int failed = 0;

        for (CartItem item : items) {

            EquipmentRental rental = new EquipmentRental();

            rental.setEquipmentId(item.getEquipmentId());
            rental.setEquipmentName(item.getEquipmentName());
            rental.setEquipmentOwnerEmail(item.getOwnerEmail());
            rental.setEquipmentOwnerName(item.getOwnerName());

            // The logged-in farmer is the person requesting this rental.
            rental.setBuyerEmail(farmerEmail);
            rental.setBuyerName(safe(user.getFullName()).isEmpty()
                    ? "Farmer"
                    : safe(user.getFullName()));

            int days = Math.max(1, item.getRentalDays());
            rental.setNumberOfDays(days);
            rental.setPricePerDay(item.getPricePerDay());
            rental.setTotalAmount(item.getTotalPrice());

            Date startDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_YEAR, days);

            rental.setStartDate(startDate);
            rental.setEndDate(calendar.getTime());
            rental.setStatus("pending");
            rental.setPaymentStatus("pending");
            rental.setCreatedAt(new Date());

            boolean created = rentalController.createRental(rental);

            if (created) {
                successful++;
                cartController.removeFromCart(item.getCartItemId());
            } else {
                failed++;
            }
        }

        if (successful > 0) {
            String message = successful
                    + " equipment rental request(s) submitted successfully.";

            if (failed > 0) {
                message += " " + failed
                        + " request(s) could not be submitted and remain in your cart.";
            }

            showDashboardAlert(
                    javafx.scene.control.Alert.AlertType.INFORMATION,
                    "Rental Requests Submitted",
                    message);

            navigateTo("Equipment Rental");

        } else {
            showDashboardAlert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Checkout Failed",
                    "Unable to submit the equipment rental request(s). Please try again.");
        }
    }

    private void showDashboardAlert(
            javafx.scene.control.Alert.AlertType type,
            String title,
            String message) {

        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void handleLogout() {

        System.out.println(
                "====================================");

        System.out.println(
                "Farmer logged out: "
                        + farmerEmail);

        System.out.println(
                "====================================");

        // Return to the login screen instead of closing the application.
        if (LoginScreen.Homestage != null) {
            LoginScreen.logoutToLogin();
        } else if (scene != null && scene.getWindow() instanceof Stage) {
            new LoginScreen().start((Stage) scene.getWindow());
        }
    }


    // =========================================================
    // OPEN CHAT WITH BUYER
    // =========================================================

    public void navigateToChat(
            String buyerName,
            String buyerEmail) {

        currentPage = "Messages";

        Node chatView = new ChatWithBuyer(
                this,
                buyerName,
                buyerEmail)
                .getView();

        if (contentArea != null) {
            contentArea.getChildren().setAll(chatView);
        }
    }

    // =========================================================
    // GET USER
    // =========================================================

    public User getUser() {

        return user;
    }

    // =========================================================
    // GET FARMER EMAIL
    // =========================================================

    public String getFarmerEmail() {

        return farmerEmail;
    }

    // =========================================================
    // GET FARMER NAME
    // =========================================================

    public String getFarmerName() {

        return user.getFullName();
    }

    // =========================================================
    // GET FARMER ROLE
    // =========================================================

    public String getFarmerRole() {

        return user.getRole();
    }

    // =========================================================
    // GET CONTENT AREA
    // =========================================================

    public StackPane getContentArea() {

        return contentArea;
    }

    // =========================================================
    // PROFILE AVATAR
    // =========================================================

    private StackPane createProfileAvatar(
            double radius) {

        Circle background = new Circle(
                radius,
                Color.web(PANEL_BG));

        StackPane avatar = new StackPane(
                background);

        String imageUrl = null;

        try {

            imageUrl = user.getProfileImageUrl();

        } catch (Exception e) {

            System.out.println(
                    "Profile image URL not available.");
        }

        if (imageUrl != null
                && !imageUrl.trim().isEmpty()) {

            try {

                Image image = new Image(
                        imageUrl.trim(),
                        radius * 2,
                        radius * 2,
                        true,
                        true,
                        true);

                if (!image.isError()) {

                    ImageView profileImage = new ImageView(
                            image);

                    profileImage.setFitWidth(
                            radius * 2);

                    profileImage.setFitHeight(
                            radius * 2);

                    profileImage.setPreserveRatio(
                            true);

                    Circle clip = new Circle(
                            radius,
                            radius,
                            radius);

                    profileImage.setClip(
                            clip);

                    avatar.getChildren()
                            .add(
                                    profileImage);

                    return avatar;
                }

            } catch (Exception e) {

                System.out.println(
                        "Unable to load profile image.");
            }
        }

        Label farmerIcon = new Label(
                "👨‍🌾");

        avatar.getChildren()
                .add(
                        farmerIcon);

        return avatar;
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safe(
            String value) {

        if (value == null) {

            return "";
        }

        return value.trim();
    }
    // =========================================================
    // REAL-TIME NOTIFICATION BADGE
    // =========================================================

    private void startNotificationListener() {

        if (farmerEmail == null || farmerEmail.trim().isEmpty()) {
            return;
        }

        if (notificationListener != null) {
            notificationListener.remove();
        }

        notificationListener = notificationController.listenForNotifications(
                farmerEmail,
                notifications -> {
                    int unread = 0;

                    if (notifications != null) {
                        for (com.mainproject.model.Notification notification : notifications) {
                            if (notification != null && !notification.isRead()) {
                                unread++;
                            }
                        }
                    }

                    final int unreadCount = unread;

                    javafx.application.Platform.runLater(() -> {
                        if (notificationBadge != null) {
                            notificationBadge.setText(
                                    unreadCount > 99 ? "99+" : String.valueOf(unreadCount));
                            notificationBadge.setVisible(unreadCount > 0);
                        }
                    });
                }
        );
    }

}