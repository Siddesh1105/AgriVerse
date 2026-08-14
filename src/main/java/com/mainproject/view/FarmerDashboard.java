package com.mainproject.view;

import java.util.HashMap;
import java.util.Map;
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

    // Theme Colors
    private static final String MAIN_BG = "#E9F7EF";
    private static final String PANEL_BG = "#D4EFDF";
    private static final String PRIMARY_GREEN = "#117864";
    private static final String ACCENT_YELLOW = "#F1C40F";
    private static final String PRIMARY_TEXT = "#1B2631";
    private static final String BORDER_COLOR = "#A2D9CE";

    public Scene getFarmerDashboardScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + MAIN_BG + ";");


        root.setTop(createTopBar());

        root.setLeft(createSidebar());


        contentArea = new StackPane();
        contentArea.setPadding(new Insets(15, 20, 20, 20));
        root.setCenter(contentArea);

        navigateTo("Dashboard");

        scene = new Scene(root, 1400, 1000);
        return scene;
    }

    private HBox createTopBar() {
        HBox top = new HBox(15);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(12, 25, 12, 20));
        top.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 0 0 1.5px 0;");

        HBox brand = new HBox(8);
        brand.setAlignment(Pos.CENTER_LEFT);
        brand.setPrefWidth(220);

        ImageView agrilogo = new ImageView("file:src/main/resources/assets/icons/AgriLinklogo.png");
        agrilogo.setFitWidth(200);
        agrilogo.setFitHeight(50);

        
        brand.getChildren().addAll(agrilogo);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        StackPane bell = new StackPane();
        bell.setStyle("-fx-cursor: hand;");
        Node bellIcon = loadIconNode("/assets/icons/notificationicon.png", "🔔", 18);
        Circle yellowDot = new Circle(4.5, Color.web(ACCENT_YELLOW));
        yellowDot.setTranslateX(6);
        yellowDot.setTranslateY(-6);
        bell.getChildren().addAll(bellIcon, yellowDot);
        bell.setOnMouseClicked(e -> navigateTo("Notifications"));

    
        HBox userBox = new HBox(8);
        userBox.setAlignment(Pos.CENTER);
        userBox.setStyle("-fx-cursor: hand;");
        Circle avatar = new Circle(15, Color.web(PANEL_BG));

        Label avatarText = new Label("👨‍🌾");
        StackPane avatarStack = new StackPane(avatar, avatarText);
        Label userName = new Label("Rajesh Patil ▼");
        userName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY_TEXT + ";");
        userBox.getChildren().addAll(avatarStack, userName);
        userBox.setOnMouseClicked(e -> navigateTo("Profile"));

        top.getChildren().addAll(brand, spacer, bell, userBox);
        return top;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(4);
        sidebar.setPadding(new Insets(12, 10, 15, 10));
        sidebar.setPrefWidth(235);
        sidebar.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 0 1.5px 0 0;");


        HBox farmerHeader = new HBox(10);
        farmerHeader.setAlignment(Pos.CENTER_LEFT);
        farmerHeader.setPadding(new Insets(5, 10, 15, 10));
        Circle farmerPic = new Circle(18, Color.web(PANEL_BG));
        Label picLabel = new Label("👨‍🌾");
        StackPane picStack = new StackPane(farmerPic, picLabel);

        VBox farmerText = new VBox(2);
        Label fName = new Label("Rajesh Patil");
        fName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY_TEXT + ";");
        Label fRole = new Label("Farmer");
        fRole.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748B;");
        farmerText.getChildren().addAll(fName, fRole);
        farmerHeader.getChildren().addAll(picStack, farmerText);
        sidebar.getChildren().add(farmerHeader);

        navGroup = new ToggleGroup();

        String[][] menuItems = {
            {"/assets/icons/homeicon2.png",         "Dashboard",           "🏠"},
            {"/assets/icons/producticon.png",       "Products",            "📦"},
            {"/assets/icons/ordericon.png",         "Orders",              "📋"},
            {"/assets/icons/marketplaceicon2.png",  "Marketplace",         "🏪"},
            {"/assets/icons/rentalicon.png",        "Equipment Rental",    "🚜"},
            {"/assets/icons/cropicon.png",          "Crop Prices",         "📈"},
            {"/assets/icons/weathericon.png",       "Weather",             "⛅"},
            {"/assets/icons/aifeatureicon2.png",    "AI Recommendations",  "✨"},
            {"/assets/icons/notificationicon.png",  "Notifications",       "🔔"},
            {"/assets/icons/profileicon.png",       "Profile",             "👤"},
            {"/assets/icons/settingsicon.png",      "Settings",            "⚙️"},
            {"/assets/icons/logouticon.png",        "Logout",              "⏻"}
        };

        for (String[] item : menuItems) {
            String iconPath = item[0];
            String title = item[1];
            String fallbackEmoji = item[2];

            ToggleButton btn = createNavButton(iconPath, title, fallbackEmoji);
            navMap.put(title, btn);
            sidebar.getChildren().add(btn);
        }

        navMap.get("Dashboard").setSelected(true);
        return sidebar;
    }

    private ToggleButton createNavButton(String iconPath, String text, String fallbackEmoji) {
        Node iconNode = loadIconNode(iconPath, fallbackEmoji, 18);
        Label textLbl = new Label(text);

        HBox box = new HBox(12, iconNode, textLbl);
        box.setAlignment(Pos.CENTER_LEFT);

        ToggleButton btn = new ToggleButton();
        btn.setGraphic(box);
        btn.setToggleGroup(navGroup);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(40);
        btn.setPadding(new Insets(0, 0, 0, 14));

        Runnable updateStyle = () -> {
            if (btn.isSelected()) {
                btn.setStyle("-fx-background-color: " + PRIMARY_GREEN + "; -fx-background-radius: 8px; -fx-cursor: hand;");
                textLbl.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
            } else {
                btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8px; -fx-cursor: hand;");
                textLbl.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + PRIMARY_TEXT + ";");
            }
        };

        btn.selectedProperty().addListener((obs, oldV, newV) -> updateStyle.run());
        updateStyle.run();

        btn.setOnMouseEntered(e -> {
            if (!btn.isSelected()) {
                btn.setStyle("-fx-background-color: " + PANEL_BG + "; -fx-background-radius: 8px; -fx-cursor: hand;");
            }
        });
        btn.setOnMouseExited(e -> {
            if (!btn.isSelected()) {
                btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 8px; -fx-cursor: hand;");
            }
        });

        btn.setOnAction(e -> navigateTo(text));
        return btn;
    }

    private Node loadIconNode(String imagePath, String fallbackEmoji, double size) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                var stream = getClass().getResourceAsStream(imagePath);
                if (stream != null) {
                    ImageView iv = new ImageView(new Image(stream));
                    iv.setFitWidth(size);
                    iv.setFitHeight(size);
                    iv.setPreserveRatio(true);
                    iv.setSmooth(true);
                    return iv;
                }
            } catch (Exception ignored) {
                
            }
        }

        // Fallback Emoji Label
        Label fallback = new Label(fallbackEmoji);
        fallback.setStyle("-fx-font-size: " + (int) size + "px;");
        fallback.setAlignment(Pos.CENTER);
        return fallback;
    }

    public void navigateTo(String page) {
        if (navMap.containsKey(page) && !navMap.get(page).isSelected()) {
            navMap.get(page).setSelected(true);
        }

        Node viewNode;
        switch (page) {
            case "Dashboard":
                viewNode = new DashboardOverview(this).getView();
                break;
            case "Marketplace":
                viewNode = new MarketPlace(this).getView();
                break;
            case "Products":
                viewNode = new Products ().getView();
                break;
            case "Equipment Rental":
                viewNode = new EquipmentRental().getView();
                break;
            case "Crop Prices":
                viewNode = new CropPrices().getView();
                break;
            case "Weather":
                viewNode = new Weather().getView();
                break;
            case "AI Recommendations":
                viewNode = new AiRecommendations().getView();
                break;
            case "Notifications":
                viewNode = new Notifications().getView();
                break;
            case "Orders":
                viewNode = new Orders().getView();
                break;
            case "Profile":
                viewNode = new Profile().getView();
                break;
            case "Settings":
                viewNode = new Settings().getView();
                break;
            case "Logout":
                Logout.show(contentArea, () -> navigateTo("Dashboard"));
                return;
            case "AddProduct":
                viewNode = new AddProduct(this).getView();
                break;
            default:
                viewNode = new DashboardOverview(this).getView();
                break;
        }

        contentArea.getChildren().setAll(viewNode);
    }
}