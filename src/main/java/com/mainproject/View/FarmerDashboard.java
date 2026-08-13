package com.mainproject.View;

import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class FarmerDashboard {

    private Scene farmerDashboardScene;
    private StackPane Screens;

    public Scene getFarmerDashboardScene() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f1efef;");

        Screens = new StackPane();
        Screens.setPadding(new Insets(20));

        showDashboardContent();


         ImageView agrilogo = new ImageView("file:src/main/resources/assets/icons/AgriLinklogo.png");
        agrilogo.setFitWidth(200);
        agrilogo.setFitHeight(50);

        HBox logoBox = new HBox(8, agrilogo);
        logoBox.setAlignment(Pos.TOP_LEFT);
        logoBox.setPadding(new Insets(20, 20, 25, 20));
          root.getChildren().addAll(logoBox);

        VBox sidebar = createSidebarNavigation(selectedPage -> {
            switch (selectedPage) {
                case "Dashboard":
                    showDashboardContent();
                    break;
                case "Profile":
                    showProfileContent();
                    break;
                case "Products":
                    showProductContent();
                    break;
                case "Orders":
                    showOrdersContent();
                    break;
                case "Equipment Rental":
                    showRentalContent();
                    break;
                case "Marketplace":
                    showMarketPlaceContent();
                    break;
                case "Crop Prices":
                    showCropPricesContent();
                    break;
                case "Feedback":
                    showfeedbackContent();
                    break;
                case "AI Features":
                    showAIFeatureContent();
                    break;
                case "Notifications":
                    showNotificationContent();
                    break;
                

                case "Logout":
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Logout Confirmation");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to log out?");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            LoginScreen loginScreen = new LoginScreen();
                            LoginScreen.switchScene(loginScreen.getHomePageScene());
                        }
                    });
                    break;

                  

            }
        });

        root.setLeft(sidebar);
        root.setCenter(Screens);

        farmerDashboardScene = new Scene(root, 1600, 1000);
        farmerDashboardScene.setFill(Color.WHITE);
        return farmerDashboardScene;
    }

    private VBox createSidebarNavigation(Consumer<String> onNavigate) {
        VBox sidebar = new VBox(6);
        sidebar.setPadding(new Insets(15, 12, 15, 12));
        sidebar.setPrefWidth(240);
        sidebar.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E5E5; -fx-border-width: 0 1px 0 0;");

        ToggleGroup group = new ToggleGroup();

        String[][] items = {
                { "/assets/icons/homeicon2.png", "Dashboard" },
                { "/assets/icons/profileicon.png", "Profile" },
                { "/assets/icons/producticon.png", "Products" },
                { "/assets/icons/ordericon.png", "Orders" },
                { "/assets/icons/rentalicon.png", "Equipment Rental" },
                { "/assets/icons/marketplaceicon2.png", "Marketplace" },
                { "/assets/icons/cropicon.png", "Crop Prices" },
                { "/assets/icons/feedbackicon.png", "Feedback" },
                { "/assets/icons/aifeatureicon2.png", "AI Features" },
                { "/assets/icons/notificationicon.png", "Notifications" },
                { "/assets/icons/logouticon.png", "Logout" }
        };

        for (int i = 0; i < items.length; i++) {
            ToggleButton navItem = createNavItem(items[i][0], items[i][1], group, onNavigate);
            if (i == 0) {
                navItem.setSelected(true);
            }
            sidebar.getChildren().add(navItem);
        }

        return sidebar;
    }

    private ToggleButton createNavItem(String iconPath, String text, ToggleGroup group, Consumer<String> onNavigate) {

        ImageView iconView = new ImageView();
        iconView.setFitWidth(18);
        iconView.setFitHeight(18);
        iconView.setPreserveRatio(true);

        Label label = new Label(text);

        HBox content = new HBox(12, iconView, label);
        content.setAlignment(Pos.CENTER_LEFT);

        ToggleButton btn = new ToggleButton();
        btn.setGraphic(content);
        btn.setToggleGroup(group);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(42);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 0, 0, 16));

        btn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                btn.setStyle("-fx-background-color: #2E7D32; -fx-background-radius: 10px; -fx-cursor: hand;");
                label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
            } else {
                btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 20px; -fx-cursor: hand;");
                label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");
            }
        });

        if (btn.isSelected()) {
            btn.setStyle("-fx-background-color: #2E7D32; -fx-background-radius: 10px; -fx-cursor: hand;");
            label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 20px; -fx-cursor: hand;");
            label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        }

        btn.setOnMouseEntered(e -> {
            if (!btn.isSelected()) {
                btn.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 20px; -fx-cursor: hand;");
            }
        });
        btn.setOnMouseExited(e -> {
            if (!btn.isSelected()) {
                btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 20px; -fx-cursor: hand;");
            }
        });

        btn.setOnAction(e -> {
            if (onNavigate != null) {
                onNavigate.accept(text);
            }
        });

        return btn;
    }

    private void showDashboardContent() {
        Text title = new Text("Welcome to Farmer Dashboard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox layout = new VBox(title);
        layout.setAlignment(Pos.TOP_LEFT);
        Screens.getChildren().setAll(layout);
    }

    private void showProfileContent() {
        Text title = new Text("Welcome to Profile Dashboard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox layout = new VBox(title);
        layout.setAlignment(Pos.TOP_LEFT);
        Screens.getChildren().setAll(layout);

    }

    private void showProductContent() {
        Text title = new Text("Welcome to Product Dashboard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox layout = new VBox(title);
        layout.setAlignment(Pos.TOP_LEFT);
        Screens.getChildren().setAll(layout);

    }

    private void showOrdersContent() {
        Text title = new Text("Welcome to Order Dashboard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox layout = new VBox(title);
        layout.setAlignment(Pos.TOP_LEFT);
        Screens.getChildren().setAll(layout);

    }

    private void showRentalContent() {
        Text title = new Text("Welcome to Equipment rental Dashboard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox layout = new VBox(title);
        layout.setAlignment(Pos.TOP_LEFT);
        Screens.getChildren().setAll(layout);

    }

    private void showMarketPlaceContent() {
        Text title = new Text("Welcome to Market Place Dashboard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox layout = new VBox(title);
        layout.setAlignment(Pos.TOP_LEFT);
        Screens.getChildren().setAll(layout);

    }

    private void showCropPricesContent() {
        Text title = new Text("Welcome to Crop Prices Dashboard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox layout = new VBox(title);
        layout.setAlignment(Pos.TOP_LEFT);
        Screens.getChildren().setAll(layout);

    }

    private void showAIFeatureContent() {
        Text title = new Text("Welcome to AI feature Dashboard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox layout = new VBox(title);
        layout.setAlignment(Pos.TOP_LEFT);
        Screens.getChildren().setAll(layout);

    }

    private void showfeedbackContent() {
        Text title = new Text("Welcome to Feedback Dashboard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox layout = new VBox(title);
        layout.setAlignment(Pos.TOP_LEFT);
        Screens.getChildren().setAll(layout);

    }

    private void showNotificationContent() {
        Text title = new Text("Welcome to Notification Dashboard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        VBox layout = new VBox(title);
        layout.setAlignment(Pos.TOP_LEFT);
        Screens.getChildren().setAll(layout);

    }

}