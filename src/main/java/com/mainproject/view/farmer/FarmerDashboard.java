package com.mainproject.view.farmer;

import java.util.HashMap;
import java.util.Map;

import com.mainproject.model.Product;
import com.mainproject.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

public class FarmerDashboard {

    private Scene scene;
    private StackPane contentArea;
    private final Map<String, ToggleButton> navMap = new HashMap<>();
    private ToggleGroup navGroup;
    private final User user;

    private static final String MAIN_BG = "#E9F7EF";
    private static final String PANEL_BG = "#D4EFDF";
    private static final String PRIMARY_GREEN = "#117864";
    private static final String PRIMARY_TEXT = "#1B2631";
    private static final String BORDER_COLOR = "#A2D9CE";
    private static final String ACCENT_YELLOW = "#F1C40F";

    public FarmerDashboard(User user) {
        this.user = user;
        System.out.println("Opening Farmer Dashboard...");
        System.out.println("Farmer Dashboard opened for: " + user.getEmail());
        System.out.println("Farmer Name: " + user.getFullName());
        System.out.println("Farmer Role: " + user.getRole());
    }

    public Scene getScene() {
        if (scene == null)
            scene = getFarmerDashboardScene();
        return scene;
    }

    public Scene getFarmerDashboardScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + MAIN_BG + ";");
        root.setTop(createTopBar());
        root.setLeft(createSidebar());

        contentArea = new StackPane();
        contentArea.setPadding(new Insets(18, 22, 20, 22));
        root.setCenter(contentArea);

        navigateTo("Dashboard");
        scene = new Scene(root, 1400, 1000);
        return scene;
    }


    private Node loadIcon(String resourcePath, double size) {
        try {
            var stream = getClass().getResourceAsStream(resourcePath);
            if (stream != null) {
                Image img = new Image(stream);
                ImageView iv = new ImageView(img);
                iv.setFitWidth(size);
                iv.setFitHeight(size);
                iv.setPreserveRatio(true);
                iv.setSmooth(true);
                return iv;
            }
        } catch (Exception ignored) {
        }

        Label fallback = new Label("?");
        fallback.setPrefSize(size, size);
        fallback.setAlignment(Pos.CENTER);
        fallback.setStyle(
                "-fx-font-size: " + (size * 0.55) + "px;" +
                        "-fx-text-fill: " + PRIMARY_TEXT + ";" +
                        "-fx-background-color: " + PANEL_BG + ";" +
                        "-fx-background-radius: 4px;");
        System.out.println("[FarmerDashboard] Icon not found: " + resourcePath);
        return fallback;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 25, 10, 20));
        topBar.setPrefHeight(80);
        topBar.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-width: 0 0 1.5px 0;");

        ImageView logo = new ImageView();
        try {
            Image image = new Image(
                    getClass().getResourceAsStream("/assets/icons/AgriLinklogo.png"));
            logo.setImage(image);
            logo.setFitWidth(260);
            logo.setFitHeight(65);
            logo.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Logo not found.");
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Node bellIcon = loadIcon("/assets/icons/notification2.png", 24);

        Circle notificationDot = new Circle(5, Color.web(ACCENT_YELLOW));
        notificationDot.setTranslateX(8);
        notificationDot.setTranslateY(-8);

        StackPane notification = new StackPane(bellIcon, notificationDot);
        notification.setPrefSize(40, 40);
        notification.setStyle("-fx-cursor: hand;");
        notification.setOnMouseClicked(e -> navigateTo("Notifications"));

        HBox userBox = new HBox(10);
        userBox.setAlignment(Pos.CENTER);
        userBox.setStyle("-fx-cursor: hand;");

        Circle avatarCircle = new Circle(20, Color.web(PANEL_BG));
        Node farmerAvatarIcon = loadIcon("/assets/icons/farmer_avatar.png", 28);

        StackPane avatar = new StackPane(avatarCircle, farmerAvatarIcon);
        avatar.setPrefSize(40, 40);

        Label farmerName = new Label(user.getFullName() + " ▼");
        farmerName.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + PRIMARY_TEXT + ";");

        userBox.getChildren().addAll(avatar, farmerName);
        userBox.setOnMouseClicked(e -> navigateTo("Profile"));

        topBar.getChildren().addAll(logo, spacer, notification, userBox);
        return topBar;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(5);
        sidebar.setPrefWidth(235);
        sidebar.setPadding(new Insets(15, 10, 15, 10));
        sidebar.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-width: 0 1.5px 0 0;");

        HBox farmerInfo = new HBox(10);
        farmerInfo.setAlignment(Pos.CENTER_LEFT);
        farmerInfo.setPadding(new Insets(5, 10, 18, 10));

        Circle farmerCircle = new Circle(22, Color.web(PANEL_BG));
        Node sidebarAvatarIcon = loadIcon("/assets/icons/avatar.png", 32);
        StackPane farmerAvatar = new StackPane(farmerCircle, sidebarAvatarIcon);
        farmerAvatar.setPrefSize(44, 44);

        VBox farmerDetails = new VBox(2);

        Label name = new Label(user.getFullName());
        name.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + PRIMARY_TEXT + ";");

        Label role = new Label(user.getRole());
        role.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");

        farmerDetails.getChildren().addAll(name, role);
        farmerInfo.getChildren().addAll(farmerAvatar, farmerDetails);
        sidebar.getChildren().add(farmerInfo);

        navGroup = new ToggleGroup();

        addNavigation(sidebar, "Dashboard", "/assets/icons/homeicon2.png");

        addNavigation(sidebar, "Products", "/assets/icons/producticon.png");

        addNavigation(sidebar, "Orders", "/assets/icons/ordericon.png");

        addNavigation(sidebar, "Marketplace", "/assets/icons/marketplaceicon2.png");

        addNavigation(sidebar, "Equipment Rental", "/assets/icons/rentalicon.png");

        addNavigation(sidebar, "Crop Prices", "/assets/icons/cropicon.png");

        addNavigation(sidebar, "Weather", "/assets/icons/weathericon.png");

        addNavigation(sidebar, "AI Recommendations", "/assets/icons/aifeatureicon.png");

        addNavigation(sidebar, "Notifications", "/assets/icons/notificationicon.png");

        addNavigation(sidebar, "Profile", "/assets/icons/profileicon.png");

        addNavigation(sidebar, "Settings", "/assets/icons/settingicon.png");

        addNavigation(sidebar, "Logout", "/assets/icons/logouticon.png");

        return sidebar;
    }

    private void addNavigation(VBox sidebar, String title, String iconPath) {
        ToggleButton button = createNavigationButton(title, iconPath);
        navMap.put(title, button);
        sidebar.getChildren().add(button);
    }

    private ToggleButton createNavigationButton(String title, String iconPath) {

        Node icon = loadIcon(iconPath, 20);

        Label text = new Label(title);
        text.setStyle("-fx-font-size: 13px; -fx-text-fill: " + PRIMARY_TEXT + ";");

        HBox content = new HBox(12, icon, text);
        content.setAlignment(Pos.CENTER_LEFT);

        ToggleButton button = new ToggleButton();
        button.setGraphic(content);
        button.setToggleGroup(navGroup);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(42);
        button.setPadding(new Insets(0, 10, 0, 14));
        button.setFocusTraversable(false);

        Runnable updateStyle = () -> {
            if (button.isSelected()) {
                button.setStyle(
                        "-fx-background-color: " + PRIMARY_GREEN + ";" +
                                "-fx-background-radius: 9px;" +
                                "-fx-cursor: hand;");
                text.setStyle(
                        "-fx-font-size: 13px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: white;");

                if (icon instanceof ImageView iv) {
                    javafx.scene.effect.ColorAdjust whiten = new javafx.scene.effect.ColorAdjust();
                    whiten.setBrightness(1.0);
                    iv.setEffect(whiten);
                }
            } else {
                button.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-background-radius: 9px;" +
                                "-fx-cursor: hand;");
                text.setStyle(
                        "-fx-font-size: 13px;" +
                                "-fx-text-fill: " + PRIMARY_TEXT + ";");

                if (icon instanceof ImageView iv) {
                    iv.setEffect(null);
                }
            }
        };

        button.selectedProperty().addListener((obs, oldVal, newVal) -> updateStyle.run());
        updateStyle.run();

        button.setOnMouseEntered(e -> {
            if (!button.isSelected()) {
                button.setStyle(
                        "-fx-background-color: " + PANEL_BG + ";" +
                                "-fx-background-radius: 9px;" +
                                "-fx-cursor: hand;");
            }
        });

        button.setOnMouseExited(e -> {
            if (!button.isSelected()) {
                button.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-background-radius: 9px;" +
                                "-fx-cursor: hand;");
            }
        });

        button.setOnAction(e -> navigateTo(title));
        return button;
    }

    public void navigateTo(String page) {
        if (navMap.containsKey(page))
            navMap.get(page).setSelected(true);

        Node viewNode;

        switch (page) {

            case "Dashboard":
                viewNode = new DashboardOverview(this).getView();
                break;

            case "Products":
                viewNode = new Products(this, user.getEmail()).getView();
                break;

            case "AddProduct":
                viewNode = new AddProduct(this, user.getEmail()).getView();
                break;

            case "Orders":
                viewNode = new Orders().getView();
                break;

            case "Marketplace":
                viewNode = new MarketPlace(this).getView();
                break;

            case "Equipment Rental":
                viewNode = new EquipmentRental().getView();
                break;

            case "Crop Prices":
                viewNode = new CropPrices(this).getView();
                break;

            case "Weather":
                viewNode = new Weather().getView();
                break;

            case "AI Recommendations":
                viewNode = new AiRecommendations().getView();
                break;

            case "Notifications":
                viewNode = new Notifications(this).getView();
                break;

            case "Profile":
                viewNode = new Profile().getView();
                break;

            case "Settings":
                viewNode = new Settings().getView();
                break;

            case "Logout":
                handleLogout();
                return;

            default:
                viewNode = new DashboardOverview(this).getView();
                break;
        }

        if (contentArea != null)
            contentArea.getChildren().setAll(viewNode);
    }

    public void navigateToEditProduct(Product product) {
        if (product == null) {
            System.out.println("Cannot edit product.");
            return;
        }
        System.out.println("Opening Edit Product...");
        System.out.println("Product ID: " + product.getProductId());
        System.out.println("Product Name: " + product.getName());

        EditProduct editProduct = new EditProduct(this, product);
        contentArea.getChildren().setAll(editProduct.getView());
    }

    private void handleLogout() {
        System.out.println("Farmer logged out: " + user.getEmail());
        if (scene != null && scene.getWindow() != null)
            scene.getWindow().hide();
    }

    public User getUser() {
        return user;
    }

    public String getFarmerEmail() {
        return user.getEmail();
    }

    public String getFarmerName() {
        return user.getFullName();
    }

    public String getFarmerRole() {
        return user.getRole();
    }

    public StackPane getContentArea() {
        return contentArea;
    }
}