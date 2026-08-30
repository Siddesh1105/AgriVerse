package com.mainproject.view;

import com.mainproject.util.ResponsiveLayout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class FeaturesScreen {

    private static final String DARK_GREEN  = "#1B5E20";
    private static final String MID_GREEN   = "#2E7D32";
    private static final String BG_MINT     = "#EAF6EA";
    private static final String TEXT_GRAY   = "#666666";

    public void start(Stage stage) {
        Scene scene = buildScene(stage);
        stage.setTitle("AgriLink \u2013 Features");
        stage.setScene(scene);
        stage.show();
    }

    // ─────────────────────────────────────────────
    // ROOT
    // ─────────────────────────────────────────────
    private Scene buildScene(Stage stage) {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + BG_MINT + ";");
        root.getChildren().addAll(
                buildHeader(stage),
                buildFeaturesContent(stage)
        );

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + BG_MINT + "; -fx-background-color: transparent;");
        return ResponsiveLayout.createScene(scroll);
    }

    // ─────────────────────────────────────────────
    // HEADER
    // ─────────────────────────────────────────────
    private HBox buildHeader(Stage stage) {
        ImageView leafIcon = new ImageView("file:src/main/resources/assets/icons/leaf_logo.png");
        leafIcon.setFitWidth(36);
        leafIcon.setFitHeight(36);
        leafIcon.setPreserveRatio(true);

        Text brand = new Text("AgriLink");
        brand.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: " + DARK_GREEN + ";");

        Text tagline = new Text("Connecting Farmers, Buyers\nand Agriculture");
        tagline.setStyle("-fx-font-size: 11px; -fx-fill: " + TEXT_GRAY + ";");

        VBox brandBlock = new VBox(2, brand, tagline);
        HBox logoBox = new HBox(10, leafIcon, brandBlock);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        Hyperlink homeLink    = navLink("Home",       stage, "home");
        Hyperlink aboutLink   = navLink("About Us",   stage, "about");
        Hyperlink featLink    = navLink("Features",   stage, "features");
        Hyperlink contactLink = navLink("Contact Us", stage, "contact");

        // Active underline for "Features"
        featLink.setStyle(
            "-fx-text-fill: " + MID_GREEN + ";" +
            "-fx-font-size: 14px; -fx-font-weight: bold;" +
            "-fx-border-width: 0 0 2.5 0;" +
            "-fx-border-color: " + MID_GREEN + ";" +
            "-fx-padding: 0 0 4 0;");

        HBox nav = new HBox(30, homeLink, aboutLink, featLink, contactLink);
        nav.setAlignment(Pos.CENTER);

        ComboBox<String> langBox = new ComboBox<>();
        langBox.getItems().addAll("English", "Hindi", "Marathi");
        langBox.setValue("English");

        Region spacerL = new Region();
        Region spacerR = new Region();
        HBox.setHgrow(spacerL, Priority.ALWAYS);
        HBox.setHgrow(spacerR, Priority.ALWAYS);

        HBox header = new HBox(20, logoBox, spacerL, nav, spacerR, langBox);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 50, 20, 50));
        header.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0;");
        return header;
    }

    private Hyperlink navLink(String label, Stage stage, String target) {
        Hyperlink link = new Hyperlink(label);
        link.setStyle("-fx-text-fill: #222222; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-width: 0;");
        link.setOnAction(e -> navigate(stage, target));
        return link;
    }

    private void navigate(Stage stage, String target) {
        switch (target) {
            case "home"     -> { HomeScreen hs = new HomeScreen(); hs.start(stage); }
            case "about"    -> { AboutUsScreen as = new AboutUsScreen(); as.start(stage); }
            case "features" -> { /* already here */ }
            case "contact"  -> { ContactUsScreen cs = new ContactUsScreen(); cs.start(stage); }
        }
    }

    // ─────────────────────────────────────────────
    // FEATURES CONTENT
    // ─────────────────────────────────────────────
    private VBox buildFeaturesContent(Stage stage) {
        // Page title
        Text title = new Text("Features");
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: #111111;");

        Text subtitle = new Text(
            "Powerful tools and features to simplify agriculture\nand empower everyone in the ecosystem.");
        subtitle.setStyle("-fx-font-size: 14px; -fx-fill: " + TEXT_GRAY + "; -fx-text-alignment: center;");
        subtitle.setTextAlignment(TextAlignment.CENTER);

        VBox titleBlock = new VBox(12, title, subtitle);
        titleBlock.setAlignment(Pos.CENTER);

        // Row 1: 5 cards
        HBox row1 = new HBox(20,
                featureCard("\uD83D\uDED2", "#E8F5E9", "Buy & Sell",
                            "Buy and sell fresh agricultural produce directly with ease."),
                featureCard("\uD83D\uDCE1", "#FCE4EC", "Live Market",
                            "Watch farmers live and buy products in real-time from your home."),
                featureCard("\uD83D\uDE9C", "#FFF8E1", "Equipment Rental",
                            "Rent agricultural equipment easily and affordably."),
                featureCard("\uD83E\uDD16", "#E3F2FD", "AI Insights",
                            "Get AI-powered recommendations for better farming decisions."),
                featureCard("\uD83D\uDCC8", "#F3E5F5", "Crop Prices",
                            "Check real-time crop prices in your local markets."));
        row1.setAlignment(Pos.CENTER);

        // Row 2: 5 cards
        HBox row2 = new HBox(20,
                featureCard("\u26C5", "#E0F7FA", "Weather Updates",
                            "Get accurate weather updates and forecasts for better planning."),
                featureCard("\uD83D\uDCE6", "#FBE9E7", "Orders Management",
                            "Manage your orders, track deliveries, and view history."),
                featureCard("\uD83D\uDD12", "#E8F5E9", "Secure Payments",
                            "Enjoy secure and transparent payments for all transactions."),
                featureCard("\uD83C\uDF10", "#EDE7F6", "Multi Language",
                            "Use AgriLink in your preferred language with ease."),
                featureCard("\uD83C\uDFA7", "#FFF3E0", "24/7 Support",
                            "We are here to help you anytime, anywhere."));
        row2.setAlignment(Pos.CENTER);

        VBox grid = new VBox(20, row1, row2);
        grid.setAlignment(Pos.CENTER);

        // CTA Banner
        HBox ctaBanner = buildCtaBanner(stage);

        VBox content = new VBox(40, titleBlock, grid, ctaBanner);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(50, 80, 60, 80));
        content.setStyle("-fx-background-color: white;");
        return content;
    }

    private VBox featureCard(String emoji, String bgColor, String title, String desc) {
        StackPane iconPane = new StackPane();
        iconPane.setPrefSize(60, 60);
        iconPane.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 50%;");

        Text iconText = new Text(emoji);
        iconText.setStyle("-fx-font-size: 26px;");
        iconPane.getChildren().add(iconText);

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #111111;");
        titleText.setTextAlignment(TextAlignment.CENTER);

        Text descText = new Text(desc);
        descText.setStyle("-fx-font-size: 11px; -fx-fill: " + TEXT_GRAY + ";");
        descText.setWrappingWidth(160);
        descText.setTextAlignment(TextAlignment.CENTER);

        VBox card = new VBox(12, iconPane, titleText, descText);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(28, 16, 28, 16));
        card.setPrefWidth(190);
        card.setMinWidth(160);
        card.setStyle(
            "-fx-background-color: white; -fx-background-radius: 16px;" +
            "-fx-border-color: #F0F0F0; -fx-border-width: 1; -fx-border-radius: 16px;");

        DropShadow s = new DropShadow();
        s.setRadius(10);
        s.setColor(Color.rgb(0, 0, 0, 0.06));
        card.setEffect(s);

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: #F9FFF9; -fx-background-radius: 16px;" +
            "-fx-border-color: #A5D6A7; -fx-border-width: 1.5; -fx-border-radius: 16px;" +
            "-fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: white; -fx-background-radius: 16px;" +
            "-fx-border-color: #F0F0F0; -fx-border-width: 1; -fx-border-radius: 16px;"));

        return card;
    }

    // ─────────────────────────────────────────────
    // CTA BANNER
    // ─────────────────────────────────────────────
    private HBox buildCtaBanner(Stage stage) {
        ImageView leafIcon = new ImageView("file:src/main/resources/assets/icons/leaf_logo.png");
        leafIcon.setFitWidth(40);
        leafIcon.setFitHeight(40);
        leafIcon.setPreserveRatio(true);

        Text bannerTitle = new Text("All-in-One Platform for Agriculture");
        bannerTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: white;");

        Text bannerSub = new Text("Everything you need to grow, sell, and succeed.");
        bannerSub.setStyle("-fx-font-size: 13px; -fx-fill: rgba(255,255,255,0.85);");

        VBox textBlock = new VBox(4, bannerTitle, bannerSub);
        textBlock.setAlignment(Pos.CENTER_LEFT);

        Button getStartedBtn = new Button("Get Started  \u279C");
        getStartedBtn.setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: " + MID_GREEN + ";" +
            "-fx-font-size: 14px; -fx-font-weight: bold;" +
            "-fx-background-radius: 25px;" +
            "-fx-padding: 12 28 12 28;");
        getStartedBtn.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.start(LoginScreen.Homestage);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox banner = new HBox(20, leafIcon, textBlock, spacer, getStartedBtn);
        banner.setAlignment(Pos.CENTER);
        banner.setPadding(new Insets(28, 40, 28, 40));
        banner.setStyle(
            "-fx-background-color: " + MID_GREEN + ";" +
            "-fx-background-radius: 16px;");

        DropShadow s = new DropShadow();
        s.setRadius(16);
        s.setColor(Color.rgb(46, 125, 50, 0.3));
        banner.setEffect(s);

        return banner;
    }
}