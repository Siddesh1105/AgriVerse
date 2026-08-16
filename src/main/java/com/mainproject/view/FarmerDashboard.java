package com.mainproject.view;

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

public class FarmerDashboard {

    // =====================================================
    // VARIABLES
    // =====================================================

    private Scene scene;

    private StackPane contentArea;

    private final Map<String, ToggleButton> navMap =
            new HashMap<>();

    private ToggleGroup navGroup;

    private final User user;

    // =====================================================
    // COLORS
    // =====================================================

    private static final String MAIN_BG =
            "#E9F7EF";

    private static final String PANEL_BG =
            "#D4EFDF";

    private static final String PRIMARY_GREEN =
            "#117864";

    private static final String PRIMARY_TEXT =
            "#1B2631";

    private static final String BORDER_COLOR =
            "#A2D9CE";

    private static final String ACCENT_YELLOW =
            "#F1C40F";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public FarmerDashboard(User user) {

        this.user = user;

        System.out.println(
                "Opening Farmer Dashboard..."
        );

        System.out.println(
                "Farmer Dashboard opened for: "
                        + user.getEmail()
        );

        System.out.println(
                "Farmer Name: "
                        + user.getFullName()
        );

        System.out.println(
                "Farmer Role: "
                        + user.getRole()
        );
    }

    // =====================================================
    // GET SCENE
    // =====================================================

    public Scene getScene() {

        if (scene == null) {

            scene =
                    getFarmerDashboardScene();
        }

        return scene;
    }

    // =====================================================
    // CREATE DASHBOARD SCENE
    // =====================================================

    public Scene getFarmerDashboardScene() {

        Rectangle2D screen =
                Screen.getPrimary()
                        .getVisualBounds();

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color: "
                        + MAIN_BG
                        + ";"
        );

        // =================================================
        // TOP BAR
        // =================================================

        root.setTop(
                createTopBar()
        );

        // =================================================
        // SIDEBAR
        // =================================================

        root.setLeft(
                createSidebar()
        );

        // =================================================
        // CONTENT AREA
        // =================================================

        contentArea =
                new StackPane();

        contentArea.setPadding(
                new Insets(
                        18,
                        22,
                        20,
                        22
                )
        );

        root.setCenter(
                contentArea
        );

        // =================================================
        // DEFAULT PAGE
        // =================================================

        navigateTo(
                "Dashboard"
        );

        // =================================================
        // SCENE
        // =================================================

        scene =
                new Scene(
                        root,
                        screen.getWidth(),
                        screen.getHeight()
                );

        return scene;
    }

    // =====================================================
    // TOP BAR
    // =====================================================

    private HBox createTopBar() {

        HBox topBar =
                new HBox(15);

        topBar.setAlignment(
                Pos.CENTER_LEFT
        );

        topBar.setPadding(
                new Insets(
                        10,
                        25,
                        10,
                        20
                )
        );

        topBar.setPrefHeight(
                80
        );

        topBar.setStyle(
                "-fx-background-color: #FFFFFF;"
                        + "-fx-border-color: "
                        + BORDER_COLOR
                        + ";"
                        + "-fx-border-width: 0 0 1.5px 0;"
        );

        // =================================================
        // LOGO
        // =================================================

        ImageView logo =
                new ImageView();

        try {

            Image image =
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/assets/icons/AgriLinklogo.png"
                                    )
                    );

            logo.setImage(
                    image
            );

            logo.setFitWidth(
                    260
            );

            logo.setFitHeight(
                    65
            );

            logo.setPreserveRatio(
                    true
            );

        } catch (Exception e) {

            System.out.println(
                    "Logo not found."
            );
        }

        // =================================================
        // SPACER
        // =================================================

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        // =================================================
        // NOTIFICATION
        // =================================================

        StackPane notification =
                new StackPane();

        notification.setPrefSize(
                40,
                40
        );

        notification.setStyle(
                "-fx-cursor: hand;"
        );

        Label bell =
                new Label(
                        "🔔"
                );

        bell.setStyle(
                "-fx-font-size: 20px;"
        );

        Circle notificationDot =
                new Circle(
                        5,
                        Color.web(
                                ACCENT_YELLOW
                        )
                );

        notificationDot.setTranslateX(
                8
        );

        notificationDot.setTranslateY(
                -8
        );

        notification.getChildren().addAll(
                bell,
                notificationDot
        );

        notification.setOnMouseClicked(
                e -> navigateTo(
                        "Notifications"
                )
        );

        // =================================================
        // USER PROFILE
        // =================================================

        HBox userBox =
                new HBox(10);

        userBox.setAlignment(
                Pos.CENTER
        );

        userBox.setStyle(
                "-fx-cursor: hand;"
        );

        Circle avatarCircle =
                new Circle(
                        20,
                        Color.web(
                                PANEL_BG
                        )
                );

        Label farmerIcon =
                new Label(
                        "👨‍🌾"
                );

        StackPane avatar =
                new StackPane(
                        avatarCircle,
                        farmerIcon
                );

        Label farmerName =
                new Label(
                        user.getFullName()
                                + " ▼"
                );

        farmerName.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + PRIMARY_TEXT
                        + ";"
        );

        userBox.getChildren().addAll(
                avatar,
                farmerName
        );

        userBox.setOnMouseClicked(
                e -> navigateTo(
                        "Profile"
                )
        );

        topBar.getChildren().addAll(
                logo,
                spacer,
                notification,
                userBox
        );

        return topBar;
    }

    // =====================================================
    // SIDEBAR
    // =====================================================

    private VBox createSidebar() {

        VBox sidebar =
                new VBox(5);

        sidebar.setPrefWidth(
                235
        );

        sidebar.setPadding(
                new Insets(
                        15,
                        10,
                        15,
                        10
                )
        );

        sidebar.setStyle(
                "-fx-background-color: #FFFFFF;"
                        + "-fx-border-color: "
                        + BORDER_COLOR
                        + ";"
                        + "-fx-border-width: 0 1.5px 0 0;"
        );

        // =================================================
        // FARMER INFORMATION
        // =================================================

        HBox farmerInfo =
                new HBox(10);

        farmerInfo.setAlignment(
                Pos.CENTER_LEFT
        );

        farmerInfo.setPadding(
                new Insets(
                        5,
                        10,
                        18,
                        10
                )
        );

        Circle farmerCircle =
                new Circle(
                        22,
                        Color.web(
                                PANEL_BG
                        )
                );

        Label farmerEmoji =
                new Label(
                        "👨‍🌾"
                );

        StackPane farmerAvatar =
                new StackPane(
                        farmerCircle,
                        farmerEmoji
                );

        VBox farmerDetails =
                new VBox(2);

        Label name =
                new Label(
                        user.getFullName()
                );

        name.setStyle(
                "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: "
                        + PRIMARY_TEXT
                        + ";"
        );

        Label role =
                new Label(
                        user.getRole()
                );

        role.setStyle(
                "-fx-font-size: 12px;"
                        + "-fx-text-fill: #64748B;"
        );

        farmerDetails.getChildren().addAll(
                name,
                role
        );

        farmerInfo.getChildren().addAll(
                farmerAvatar,
                farmerDetails
        );

        sidebar.getChildren().add(
                farmerInfo
        );

        // =================================================
        // NAVIGATION
        // =================================================

        navGroup =
                new ToggleGroup();

        addNavigation(
                sidebar,
                "Dashboard",
                "🏠"
        );

        addNavigation(
                sidebar,
                "Products",
                "🎁"
        );

        addNavigation(
                sidebar,
                "Orders",
                "📦"
        );

        addNavigation(
                sidebar,
                "Marketplace",
                "🏪"
        );

        addNavigation(
                sidebar,
                "Equipment Rental",
                "🚜"
        );

        addNavigation(
                sidebar,
                "Crop Prices",
                "📈"
        );

        addNavigation(
                sidebar,
                "Weather",
                "🌤"
        );

        addNavigation(
                sidebar,
                "AI Recommendations",
                "✨"
        );

        addNavigation(
                sidebar,
                "Notifications",
                "🔔"
        );

        addNavigation(
                sidebar,
                "Profile",
                "👤"
        );

        addNavigation(
                sidebar,
                "Settings",
                "⚙"
        );

        addNavigation(
                sidebar,
                "Logout",
                "🚪"
        );

        return sidebar;
    }

    // =====================================================
    // ADD NAVIGATION BUTTON
    // =====================================================

    private void addNavigation(
            VBox sidebar,
            String title,
            String emoji) {

        ToggleButton button =
                createNavigationButton(
                        title,
                        emoji
                );

        navMap.put(
                title,
                button
        );

        sidebar.getChildren().add(
                button
        );
    }

    // =====================================================
    // CREATE NAVIGATION BUTTON
    // =====================================================

    private ToggleButton createNavigationButton(
            String title,
            String emoji) {

        Label icon =
                new Label(
                        emoji
                );

        icon.setStyle(
                "-fx-font-size: 18px;"
        );

        Label text =
                new Label(
                        title
                );

        text.setStyle(
                "-fx-font-size: 13px;"
                        + "-fx-text-fill: "
                        + PRIMARY_TEXT
                        + ";"
        );

        HBox content =
                new HBox(
                        12,
                        icon,
                        text
                );

        content.setAlignment(
                Pos.CENTER_LEFT
        );

        ToggleButton button =
                new ToggleButton();

        button.setGraphic(
                content
        );

        button.setToggleGroup(
                navGroup
        );

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        button.setPrefHeight(
                42
        );

        button.setPadding(
                new Insets(
                        0,
                        10,
                        0,
                        14
                )
        );

        button.setFocusTraversable(
                false
        );

        // =================================================
        // STYLE UPDATE
        // =================================================

        Runnable updateStyle =
                () -> {

                    if (
                            button.isSelected()
                    ) {

                        button.setStyle(
                                "-fx-background-color: "
                                        + PRIMARY_GREEN
                                        + ";"
                                        + "-fx-background-radius: 9px;"
                                        + "-fx-cursor: hand;"
                        );

                        text.setStyle(
                                "-fx-font-size: 13px;"
                                        + "-fx-font-weight: bold;"
                                        + "-fx-text-fill: white;"
                        );

                    } else {

                        button.setStyle(
                                "-fx-background-color: transparent;"
                                        + "-fx-background-radius: 9px;"
                                        + "-fx-cursor: hand;"
                        );

                        text.setStyle(
                                "-fx-font-size: 13px;"
                                        + "-fx-text-fill: "
                                        + PRIMARY_TEXT
                                        + ";"
                        );
                    }
                };

        button.selectedProperty()
                .addListener(
                        (obs, oldValue, newValue) ->
                                updateStyle.run()
                );

        updateStyle.run();

        // =================================================
        // HOVER
        // =================================================

        button.setOnMouseEntered(
                e -> {

                    if (
                            !button.isSelected()
                    ) {

                        button.setStyle(
                                "-fx-background-color: "
                                        + PANEL_BG
                                        + ";"
                                        + "-fx-background-radius: 9px;"
                                        + "-fx-cursor: hand;"
                        );
                    }
                }
        );

        button.setOnMouseExited(
                e -> {

                    if (
                            !button.isSelected()
                    ) {

                        button.setStyle(
                                "-fx-background-color: transparent;"
                                        + "-fx-background-radius: 9px;"
                                        + "-fx-cursor: hand;"
                        );
                    }
                }
        );

        button.setOnAction(
                e -> navigateTo(
                        title
                )
        );

        return button;
    }

    // =====================================================
    // NAVIGATION
    // =====================================================

    public void navigateTo(String page) {

        // =================================================
        // UPDATE SELECTED SIDEBAR BUTTON
        // =================================================

        if (
                navMap.containsKey(page)
        ) {

            navMap.get(page)
                    .setSelected(
                            true
                    );
        }

        // =================================================
        // PAGE NODE
        // =================================================

        Node viewNode;

        // =================================================
        // SWITCH
        // =================================================

        switch (page) {

            // =============================================
            // DASHBOARD
            // =============================================

            case "Dashboard":

                viewNode =
                        new DashboardOverview(
                                this
                        ).getView();

                break;

            // =============================================
            // PRODUCTS
            // =============================================

            case "Products":

                viewNode =
                        new Products(
                                this,
                                user.getEmail()
                        ).getView();

                break;

            // =============================================
            // ADD PRODUCT
            // =============================================

            case "AddProduct":

                viewNode =
                        new AddProduct(
                                this,
                                user.getEmail()
                        ).getView();

                break;

            // =============================================
            // ORDERS
            // =============================================

            case "Orders":

                viewNode =
                        new Orders()
                                .getView();

                break;

            // =============================================
            // MARKETPLACE
            // =============================================

            case "Marketplace":

                viewNode =
                        new MarketPlace(
                                this
                        ).getView();

                break;

            // =============================================
            // EQUIPMENT RENTAL
            // =============================================

            case "Equipment Rental":

                viewNode =
                        new EquipmentRental()
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
            // WEATHER
            // =============================================

            case "Weather":

                viewNode =
                        new Weather()
                                .getView();

                break;

            // =============================================
            // AI RECOMMENDATIONS
            // =============================================

            case "AI Recommendations":

                viewNode =
                        new AiRecommendations()
                                .getView();

                break;

            // =============================================
            // NOTIFICATIONS
            // =============================================

            case "Notifications":

                viewNode =
                        new Notifications(this)
                                .getView();

                break;

            // =============================================
            // PROFILE
            // =============================================

            case "Profile":

                viewNode =
                        new Profile()
                                .getView();

                break;

            // =============================================
            // SETTINGS
            // =============================================

            case "Settings":

                viewNode =
                        new Settings()
                                .getView();

                break;

            // =============================================
            // LOGOUT
            // =============================================

            case "Logout":

                handleLogout();

                return;

            // =============================================
            // DEFAULT
            // =============================================

            default:

                viewNode =
                        new DashboardOverview(
                                this
                        ).getView();

                break;
        }

        // =================================================
        // DISPLAY VIEW
        // =================================================

        if (
                contentArea != null
        ) {

            contentArea
                    .getChildren()
                    .setAll(
                            viewNode
                    );
        }
    }

    // =====================================================
    // EDIT PRODUCT NAVIGATION
    // =====================================================

    public void navigateToEditProduct(
            Product product) {

        if (
                product == null
        ) {

            System.out.println(
                    "Cannot edit product."
            );

            return;
        }

        System.out.println(
                "Opening Edit Product..."
        );

        System.out.println(
                "Product ID: "
                        + product.getProductId()
        );

        System.out.println(
                "Product Name: "
                        + product.getName()
        );

        EditProduct editProduct =new EditProduct( this,product);

        Node editView =editProduct.getView();

        contentArea
                .getChildren()
                .setAll(
                        editView
                );
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    private void handleLogout() {

        System.out.println(
                "Farmer logged out: "
                        + user.getEmail()
        );

        /*
         * If you already have a LoginScreen instance,
         * put your login navigation code here.
         *
         * For now, this closes the dashboard window.
         */

        if (
                scene != null
                        &&
                scene.getWindow() != null
        ) {

            scene.getWindow()
                    .hide();
        }
    }

    // =====================================================
    // GET USER
    // =====================================================

    public User getUser() {

        return user;
    }

    // =====================================================
    // GET FARMER EMAIL
    // =====================================================

    public String getFarmerEmail() {

        return user.getEmail();
    }

    // =====================================================
    // GET FARMER NAME
    // =====================================================

    public String getFarmerName() {

        return user.getFullName();
    }

    // =====================================================
    // GET FARMER ROLE
    // =====================================================

    public String getFarmerRole() {

        return user.getRole();
    }

    // =====================================================
    // GET CONTENT AREA
    // =====================================================

    public StackPane getContentArea() {

        return contentArea;
    }
}