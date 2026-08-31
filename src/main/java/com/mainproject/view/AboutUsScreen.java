package com.mainproject.view;

import com.mainproject.util.ResponsiveLayout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AboutUsScreen {

    private static final String DARK_GREEN  = "#1B5E20";
    private static final String MID_GREEN   = "#2E7D32";
    private static final String BG_MINT     = "#EAF6EA";
    private static final String TEXT_GRAY   = "#666666";

    public void start(Stage stage) {
        Scene scene = buildScene(stage);
        stage.setTitle("AgriLink \u2013 About Us");
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
                buildAboutTopSection(),
                buildStatsBar(),
                buildMissionVisionSection(),
                buildValuesSection()
        );

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + BG_MINT + "; -fx-background-color: transparent;");
        return ResponsiveLayout.createScene(scroll);
    }

    // ─────────────────────────────────────────────
    // SHARED HEADER
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

        // Active underline for "About Us"
        aboutLink.setStyle(
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
            case "about"    -> { /* already here */ }
            case "features" -> { FeaturesScreen fs = new FeaturesScreen(); fs.start(stage); }
            case "contact"  -> { ContactUsScreen cs = new ContactUsScreen(); cs.start(stage); }
        }
    }

    // ─────────────────────────────────────────────
    // ABOUT TOP: headline + image
    // ─────────────────────────────────────────────
    private HBox buildAboutTopSection() {
        Text heading = new Text("About Us");
        heading.setStyle("-fx-font-size: 38px; -fx-font-weight: bold; -fx-fill: #111111;");

        Region accentBar = new Region();
        accentBar.setPrefSize(50, 4);
        accentBar.setStyle("-fx-background-color: " + MID_GREEN + "; -fx-background-radius: 2px;");

        Text para1 = new Text(
            "AgriLink is a digital platform that connects farmers and buyers, " +
            "promotes fair trade, and supports the agriculture community " +
            "with smart tools and real-time information.");
        para1.setStyle("-fx-font-size: 13px; -fx-fill: " + TEXT_GRAY + "; -fx-line-spacing: 4px;");
        para1.setWrappingWidth(420);

        Text para2 = new Text(
            "Our mission is to empower farmers, bring transparency to the " +
            "agricultural market, and build a stronger agricultural ecosystem for a ");
        para2.setStyle("-fx-font-size: 13px; -fx-fill: " + TEXT_GRAY + ";");

        Text boldBetter = new Text("better tomorrow.");
        boldBetter.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #111111;");

        javafx.scene.text.TextFlow para2Flow = new javafx.scene.text.TextFlow(para2, boldBetter);
        para2Flow.setMaxWidth(420);

        VBox leftContent = new VBox(16, heading, accentBar, para1, para2Flow);
        leftContent.setAlignment(Pos.TOP_LEFT);
        leftContent.setMaxWidth(460);

        ImageView farmerImg = new ImageView("file:src/main/resources/assets/icons/homepage_farmer.jpg");
        farmerImg.setFitWidth(420);
        farmerImg.setFitHeight(280);
        farmerImg.setPreserveRatio(false);

        Rectangle clip = new Rectangle(420, 280);
        clip.setArcWidth(24);
        clip.setArcHeight(24);
        farmerImg.setClip(clip);

        DropShadow imgShadow = new DropShadow();
        imgShadow.setRadius(20);
        imgShadow.setColor(Color.rgb(0, 0, 0, 0.15));

        StackPane imgPane = new StackPane(farmerImg);
        imgPane.setEffect(imgShadow);

        HBox section = new HBox(60, leftContent, imgPane);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(50, 80, 40, 80));
        section.setStyle("-fx-background-color: white;");
        return section;
    }

    // ─────────────────────────────────────────────
    // STATS BAR
    // ─────────────────────────────────────────────
    private HBox buildStatsBar() {
        HBox stats = new HBox(0,
                statItem("\uD83D\uDC65", "10,000+", "Happy Users"),
                divider(),
                statItem("\uD83C\uDF3E", "5,000+",  "Farmers"),
                divider(),
                statItem("\uD83D\uDED2", "2,000+",  "Buyers"),
                divider(),
                statItem("\uD83C\uDF31", "25,000+", "Products Listed"));
        stats.setAlignment(Pos.CENTER);
        HBox.setHgrow(stats, Priority.ALWAYS);

        HBox outer = new HBox(stats);
        outer.setAlignment(Pos.CENTER);
        outer.setStyle("-fx-background-color: #F0F8F0;");
        return outer;
    }

    private HBox statItem(String icon, String number, String label) {
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 26px;");

        Text numberText = new Text(number);
        numberText.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: " + DARK_GREEN + ";");

        Text labelText = new Text(label);
        labelText.setStyle("-fx-font-size: 12px; -fx-fill: " + TEXT_GRAY + ";");

        VBox textBlock = new VBox(2, numberText, labelText);
        textBlock.setAlignment(Pos.CENTER_LEFT);

        HBox item = new HBox(14, iconText, textBlock);
        item.setAlignment(Pos.CENTER);
        item.setPadding(new Insets(28, 60, 28, 60));
        HBox.setHgrow(item, Priority.ALWAYS);
        return item;
    }

    private Region divider() {
        Region r = new Region();
        r.setPrefWidth(1);
        r.setPrefHeight(60);
        r.setStyle("-fx-background-color: #C8E6C9;");
        return r;
    }

    // ─────────────────────────────────────────────
    // MISSION & VISION
    // ─────────────────────────────────────────────
    private HBox buildMissionVisionSection() {
        VBox mission = mvCard(
            "\uD83C\uDFAF",
            "Our Mission",
            "To empower farmers and buyers by providing a transparent, " +
            "reliable, and efficient marketplace for agricultural products and services.");

        VBox vision = mvCard(
            "\uD83D\uDC41",
            "Our Vision",
            "To become the most trusted agricultural platform " +
            "that drives growth, sustainability, and prosperity for all.");

        HBox row = new HBox(30, mission, vision);
        row.setAlignment(Pos.CENTER);
        HBox.setHgrow(mission, Priority.ALWAYS);
        HBox.setHgrow(vision, Priority.ALWAYS);
        mission.setMaxWidth(Double.MAX_VALUE);
        vision.setMaxWidth(Double.MAX_VALUE);

        HBox outer = new HBox(row);
        outer.setPadding(new Insets(40, 80, 20, 80));
        outer.setStyle("-fx-background-color: white;");
        HBox.setHgrow(row, Priority.ALWAYS);
        row.setMaxWidth(Double.MAX_VALUE);
        return outer;
    }

    private VBox mvCard(String icon, String title, String desc) {
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 26px;");

        StackPane iconPane = new StackPane(iconText);
        iconPane.setPrefSize(52, 52);
        iconPane.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 50%;");

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #111111;");

        Text descText = new Text(desc);
        descText.setStyle("-fx-font-size: 13px; -fx-fill: " + TEXT_GRAY + "; -fx-line-spacing: 4px;");
        descText.setWrappingWidth(380);

        HBox content = new HBox(16, iconPane, new VBox(6, titleText, descText));
        content.setAlignment(Pos.TOP_LEFT);

        VBox card = new VBox(content);
        card.setPadding(new Insets(24));
        card.setStyle(
            "-fx-background-color: #F9FBF9; -fx-background-radius: 14px;" +
            "-fx-border-color: #E8F5E9; -fx-border-width: 1; -fx-border-radius: 14px;");
        DropShadow s = new DropShadow();
        s.setRadius(8);
        s.setColor(Color.rgb(0, 0, 0, 0.05));
        card.setEffect(s);
        return card;
    }

    // ─────────────────────────────────────────────
    // VALUES
    // ─────────────────────────────────────────────
    private VBox buildValuesSection() {
        Text heading = new Text("Our Values");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #111111;");

        HBox cards = new HBox(20,
                valueCard("\uD83D\uDEE1\uFE0F", "Trust & Transparency",
                          "We believe in honest and transparent dealings."),
                valueCard("\uD83C\uDF1F", "Quality First",
                          "We ensure the best quality products and services."),
                valueCard("\uD83D\uDC68\u200D\uD83C\uDF3E", "Farmer Empowerment",
                          "We support farmers with tools and knowledge."),
                valueCard("\uD83C\uDF0D", "Sustainability",
                          "We promote sustainable farming and green future."));
        cards.setAlignment(Pos.CENTER_LEFT);

        VBox section = new VBox(24, heading, cards);
        section.setPadding(new Insets(30, 80, 50, 80));
        section.setStyle("-fx-background-color: white;");
        return section;
    }

    private VBox valueCard(String icon, String title, String desc) {
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 22px;");

        StackPane iconBg = new StackPane(iconText);
        iconBg.setPrefSize(50, 50);
        iconBg.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 50%;");

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #111111;");

        Text descText = new Text(desc);
        descText.setStyle("-fx-font-size: 11px; -fx-fill: " + TEXT_GRAY + ";");
        descText.setWrappingWidth(160);

        VBox card = new VBox(12, iconBg, titleText, descText);
        card.setPadding(new Insets(20, 16, 20, 16));
        card.setPrefWidth(200);
        card.setStyle(
            "-fx-background-color: #F9FBF9; -fx-background-radius: 14px;" +
            "-fx-border-color: #E8F5E9; -fx-border-width: 1; -fx-border-radius: 14px;");
        DropShadow s = new DropShadow();
        s.setRadius(8);
        s.setColor(Color.rgb(0, 0, 0, 0.05));
        card.setEffect(s);
        return card;
    }
}