// package com.mainproject.view.buyer;

// import com.mainproject.model.User;

// import javafx.scene.Scene;

// import javafx.scene.layout.BorderPane;

// import javafx.scene.paint.Color;
// import javafx.scene.text.Text;

// public class BuyerDashboard {

//     private Scene buyerDashboardScene;

//     public BuyerDashboard(User user) {

//     }

//     Scene getBuyerDashboardScene() {

//         Text text = new Text("Buyer Dashboard");
//         text.setStyle("-fx-font-size : 20px ; ");
//         BorderPane root = new BorderPane(text);

//         buyerDashboardScene = new Scene(root, 1400, 1000);
//         buyerDashboardScene.setFill(Color.WHITE);
//         return buyerDashboardScene;
//     }

//     public Scene getScene() {

//         throw new UnsupportedOperationException("Unimplemented method 'getScene'");
//     }

// }

package com.mainproject.view.buyer;

import com.mainproject.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;


public class BuyerDashboard {

    private User currentUser;
    private Scene scene;
    private BorderPane mainLayout;

    public BuyerDashboard(User user) {
        this.currentUser = user;
    }

    public Scene getScene() {
        if (scene == null) {
            mainLayout = new BorderPane();
            mainLayout.setStyle("-fx-background-color: #F8FAFC;");

            // Left Sidebar
            mainLayout.setLeft(createSidebar());

            // Default initial view: Screen 1 (Dashboard Overview)
            setView(new DashboardOverview(this).getView());

            scene = new Scene(mainLayout, 1400, 1000);
        }
        return scene;
    }

    public Scene getBuyerDashboardScene() {
        return getScene();
    }

    public void setView(Node viewNode) {
        mainLayout.setCenter(viewNode);
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(240);
        sidebar.setPadding(new Insets(20, 12, 20, 12));
        sidebar.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-width: 0 1 0 0;");

        // Brand Logo
        Label brandLabel = new Label("🌿 AgriLink");
        brandLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #166534;");
        brandLabel.setPadding(new Insets(0, 0, 15, 10));

        // Navigation Items
        VBox navItems = new VBox(4);
        navItems.getChildren().addAll(
            createNavButton("📊 Dashboard", () -> setView(new DashboardOverview(this).getView())),
            createNavButton("🔴 Live Now", () -> setView(new LiveViewerScreen(this).getView())),
            createNavButton("🏪 Marketplace", () -> setView(new LiveMarketplace(this).getView())),
            createNavButton("📦 My Orders", () -> setView(new MyOrders(this).getView())),
            createNavButton("❤️ Wishlist", () -> setView(new Wishlist(this).getView())),
            createNavButton("👨‍🌾 Farmers", () -> setView(new FarmerProfile(this).getView())),
            createNavButton("🚜 Search & Rent", () -> setView(new SearchFilter(this).getView())),
            createNavButton("📈 Crop Prices", () -> setView(new CropPrices(this).getView())),
            createNavButton("✨ AI Recommendations", () -> setView(new AiRecommendations(this).getView())),
            createNavButton("💬 Messages", () -> setView(new ChatWithFarmer(this).getView())),
            createNavButton("🔔 Notifications", () -> setView(new Notification(this).getView())),
            createNavButton("🎙️ Voice Assistant", () -> setView(new VoiceAssistant(this).getView())),
            createNavButton("⚙️ Settings", () -> setView(new Settings(this).getView()))
        );

        ScrollPane navScroll = new ScrollPane(navItems);
        navScroll.setFitToWidth(true);
        navScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        VBox.setVgrow(navScroll, Priority.ALWAYS);

        Button btnLogout = new Button("🚪 Logout");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 8; -fx-cursor: hand;");

        sidebar.getChildren().addAll(brandLabel, navScroll, btnLogout);
        return sidebar;
    }

    private Button createNavButton(String title, Runnable action) {
        Button btn = new Button(title);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #1E293B; -fx-font-size: 13px; -fx-padding: 10 14; -fx-background-radius: 8; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #166534; -fx-font-size: 13px; -fx-padding: 10 14; -fx-background-radius: 8; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #1E293B; -fx-font-size: 13px; -fx-padding: 10 14; -fx-background-radius: 8; -fx-cursor: hand;"));
        btn.setOnAction(e -> action.run());
        return btn;
    }
}