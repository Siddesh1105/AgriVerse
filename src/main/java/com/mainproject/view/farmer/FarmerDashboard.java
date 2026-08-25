package com.mainproject.view.farmer;

import com.mainproject.util.LanguageManager;


import java.util.HashMap;
import java.util.Map;

import com.mainproject.model.Product;
import com.mainproject.model.User;

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

/**
 * ============================================================
 * FARMER DASHBOARD
 * ============================================================
 *
 * Main dashboard for logged-in farmers.
 *
 * Handles:
 *
 * - Top bar
 * - Farmer profile
 * - Sidebar navigation
 * - Dashboard
 * - Products
 * - Add Product
 * - Orders
 * - Marketplace
 * - Equipment Rental
 * - My Cart
 * - My Equipment
 * - Add Equipment
 * - Crop Prices
 * - Weather
 * - AI Recommendations
 * - Notifications
 * - Profile
 * - Settings
 * - Logout
 *
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

                BorderPane root = new BorderPane();

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

                scene = new Scene(
                                root,
                                screen.getWidth(),
                                screen.getHeight());

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

                Circle notificationDot = new Circle(
                                5,
                                Color.web(
                                                ACCENT_YELLOW));

                notificationDot.setTranslateX(
                                8);

                notificationDot.setTranslateY(
                                -8);

                notification.getChildren()
                                .addAll(
                                                bell,
                                                notificationDot);

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
                // CROP PRICES
                // =====================================================

                

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

                LanguageManager.apply(sidebar);
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

                // =====================================================
                // STYLE UPDATE
                // =====================================================

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

                // =====================================================
                // PAGE NODE
                // =====================================================

                Node viewNode;

                // =====================================================
                // SWITCH
                // =====================================================

                switch (page) {

                        // =================================================
                        // DASHBOARD
                        // =================================================

                        case "Dashboard":

                                /*
                                 * IMPORTANT:
                                 *
                                 * DashboardOverview should receive
                                 * only the FarmerDashboard navigator.
                                 *
                                 * The dashboard can get the email using:
                                 *
                                 * navigator.getFarmerEmail()
                                 */

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

                                viewNode = new Orders()
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

                                viewNode = new EquipmentRental(
                                                farmerEmail,
                                                safe(
                                                                user.getFullName()), null)
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

                                                // Continue Shopping
                                                () -> navigateTo(
                                                                "Equipment Rental"),

                                                // Checkout
                                                () -> {

                                                        System.out.println(
                                                                        "Checkout clicked.");

                                                        System.out.println(
                                                                        "Razorpay payment will be integrated later.");
                                                })
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

                                                // Open Add Equipment
                                                () -> navigateTo(
                                                                "AddEquipment"),

                                                // Back to Equipment Rental
                                                () -> navigateTo(
                                                                "Equipment Rental"))
                                                .getView();

                                break;

                        // =================================================
                        // ADD EQUIPMENT
                        // =================================================

                        case "AddEquipment":

                                /*
                                 * FIX:
                                 *
                                 * Added missing AddEquipment navigation.
                                 */

                                viewNode = new AddEquipment(
                                                farmerEmail,
                                                safe(
                                                                user.getFullName()),

                                                // After successful save
                                                () -> navigateTo(
                                                                "Equipment Rental"))
                                                .getView();

                                break;

                        // =================================================
                        // CROP PRICES
                        // =================================================

                        // =================================================
                        // WEATHER
                        // =================================================

                        case "Weather":

                                viewNode = new Weather()
                                                .getView();

                                break;

                        // =================================================
                        // AI RECOMMENDATIONS
                        // =================================================

                        case "AI Recommendations":

                                viewNode = new AiRecommendations()
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
                                                        viewNode);
                }
        }

        // =========================================================
        // REFRESH AFTER LANGUAGE CHANGE
        // =========================================================

        public void refreshLanguage() {

                if (root != null) {
                        root.setLeft(createSidebar());
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

                if (scene != null
                                && scene.getWindow() != null) {

                        scene.getWindow()
                                        .hide();
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
        // ONLY CHANGES THE PROFILE ICON
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

                                        ImageView profileImage = new ImageView(image);

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

                // Keep the existing farmer icon
                // only when no valid profile image exists.
                Label farmerIcon = new Label("👨‍🌾");

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
}