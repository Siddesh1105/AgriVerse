package com.mainproject.view.buyer;

import com.mainproject.util.LanguageManager;

import com.mainproject.model.User;

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


public class BuyerDashboard {

    private final User currentUser;

    private Scene scene;
    private BorderPane mainLayout;

    private String currentPage = "Dashboard";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BuyerDashboard(User user) {
        this.currentUser = user;

        System.out.println("====================================");
        System.out.println("Opening Buyer Dashboard");

        if (user != null) {
            System.out.println("Buyer Name  : " + user.getFullName());
            System.out.println("Buyer Email : " + user.getEmail());
            System.out.println("Buyer Role  : " + user.getRole());
        }

        System.out.println("====================================");
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
            return null;
        }

        return currentUser.getEmail();
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
                    "-fx-background-color: #F8FAFC;"
            );

            // =================================================
            // SIDEBAR
            // =================================================

            mainLayout.setLeft(
                    createSidebar()
            );

            // =================================================
            // DEFAULT PAGE
            // =================================================

            setView(
                    new DashboardOverview(this)
                            .getView()
            );

            scene = new Scene(
                    mainLayout,
                    1400,
                    900
            );
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

        if (mainLayout == null) {
            return;
        }

        mainLayout.setCenter(viewNode);
    }

    public void setCurrentPage(String page) {
        currentPage = page;
    }

    public void refreshLanguage() {
        if (mainLayout == null) return;
        mainLayout.setLeft(createSidebar());
        switch (currentPage) {
            case "Marketplace": setView(new LiveMarketplace(this).getView()); break;
            case "My Orders": setView(new MyOrders(this).getView()); break;
            case "Wishlist": setView(new Wishlist(this).getView()); break;
            case "Farmers": setView(new FarmerProfile(this).getView()); break;
            case "Search & Rent": setView(new SearchFilter(this).getView()); break;
            case "Crop Prices": setView(new CropPrices(this).getView()); break;
            case "AI Recommendations": setView(new AiRecommendations(this).getView()); break;
            case "Messages": setView(new ChatWithFarmer(this).getView()); break;
            case "Notifications": setView(new Notification(this).getView()); break;
            case "Voice Assistant": setView(new VoiceAssistant(this).getView()); break;
            case "Settings": setView(new Settings(this, new Runnable() { @Override public void run() { setCurrentPage("Settings"); refreshLanguage(); } }).getView()); break;
            default: currentPage = "Dashboard"; setView(new DashboardOverview(this).getView()); break;
        }
    }

    // =====================================================
    // SIDEBAR
    // =====================================================

    private VBox createSidebar() {

        VBox sidebar = new VBox(8);

        sidebar.setPrefWidth(240);

        sidebar.setPadding(
                new Insets(
                        20,
                        12,
                        20,
                        12
                )
        );

        sidebar.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-border-color: #E2E8F0;" +
                "-fx-border-width: 0 1 0 0;"
        );

        // =================================================
        // BRAND
        // =================================================

        Label brandLabel =
                new Label("🌿 AgriLink");

        brandLabel.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #166534;"
        );

        brandLabel.setPadding(
                new Insets(
                        0,
                        0,
                        15,
                        10
                )
        );

        // =================================================
        // NAVIGATION
        // =================================================

        VBox navItems = new VBox(4);

        navItems.getChildren().addAll(

                createNavButton(
                        "📊 Dashboard",
                        () -> setView(
                                new DashboardOverview(this)
                                        .getView()
                        )
                ),

                createNavButton(
                        "🔴 Live Now",
                        () -> setView(
                                new LiveViewerScreen(this)
                                        .getView()
                        )
                ),

                createNavButton(
                        "🏪 Marketplace",
                        () -> setView(
                                new LiveMarketplace(this)
                                        .getView()
                        )
                ),

                createNavButton(
                        "📦 My Orders",
                        () -> setView(
                                new MyOrders(this)
                                        .getView()
                        )
                ),

                createNavButton(
                        "❤️ Wishlist",
                        () -> setView(
                                new Wishlist(this)
                                        .getView()
                        )
                ),

                createNavButton(
                        "👨‍🌾 Farmers",
                        () -> setView(
                                new FarmerProfile(this)
                                        .getView()
                        )
                ),

                createNavButton(
                        "🚜 Search & Rent",
                        () -> setView(
                                new SearchFilter(this)
                                        .getView()
                        )
                ),

                createNavButton(
                        "📈 Crop Prices",
                        () -> setView(
                                new CropPrices(this)
                                        .getView()
                        )
                ),

                createNavButton(
                        "✨ AI Recommendations",
                        () -> setView(
                                new AiRecommendations(this)
                                        .getView()
                        )
                ),

                createNavButton(
                        "💬 Messages",
                        () -> setView(
                                new ChatWithFarmer(this)
                                        .getView()
                        )
                ),

                createNavButton(
                        "🔔 Notifications",
                        () -> setView(
                                new Notification(this)
                                        .getView()
                        )
                ),

                createNavButton(
                        "🎙️ Voice Assistant",
                        () -> setView(
                                new VoiceAssistant(this)
                                        .getView()
                        )
                ),

                createNavButton(
                        "⚙️ Settings",
                        () -> setView(
                                new Settings(this, new Runnable() { @Override public void run() { setCurrentPage("Settings"); refreshLanguage(); } })
                                        .getView()
                        )
                )
        );

        // =================================================
        // NAVIGATION SCROLL
        // =================================================

        ScrollPane navScroll =
                new ScrollPane(navItems);

        navScroll.setFitToWidth(true);

        navScroll.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;" +
                "-fx-border-color: transparent;"
        );

        VBox.setVgrow(
                navScroll,
                Priority.ALWAYS
        );

        // =================================================
        // LOGOUT
        // =================================================

        Button btnLogout =
                new Button("🚪 Logout");

        btnLogout.setMaxWidth(
                Double.MAX_VALUE
        );

        btnLogout.setStyle(
                "-fx-background-color: #FEE2E2;" +
                "-fx-text-fill: #DC2626;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        // Keep your existing logout logic here
        btnLogout.setOnAction(e -> {

            System.out.println(
                    "Buyer logout clicked."
            );

            // Add your existing logout code here.
        });

        sidebar.getChildren().addAll(
                brandLabel,
                navScroll,
                btnLogout
        );

        LanguageManager.apply(sidebar);
        return sidebar;
    }

    // =====================================================
    // NAVIGATION BUTTON
    // =====================================================

    private Button createNavButton(
            String title,
            Runnable action) {

        Button btn =
                new Button(title);

        btn.setMaxWidth(
                Double.MAX_VALUE
        );

        btn.setAlignment(
                Pos.CENTER_LEFT
        );

        btn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #1E293B;" +
                "-fx-font-size: 13px;" +
                "-fx-padding: 10 14;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        btn.setOnMouseEntered(e ->
                btn.setStyle(
                        "-fx-background-color: #F1F5F9;" +
                        "-fx-text-fill: #166534;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10 14;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
                )
        );

        btn.setOnMouseExited(e ->
                btn.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #1E293B;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10 14;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
                )
        );

        btn.setOnAction(
                e -> action.run()
        );

        return btn;
    }
}