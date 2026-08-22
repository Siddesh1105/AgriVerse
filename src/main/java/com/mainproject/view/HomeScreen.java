package com.mainproject.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class HomeScreen extends Application {

    private static final String DARK_GREEN = "#1B5E20";
    private static final String MID_GREEN = "#2E7D32";
    private static final String LIGHT_GREEN = "#66BB6A";
    private static final String BG_MINT = "#EAF6EA";
    private static final String TEXT_GRAY = "#666666";

    @Override
    public void start(Stage primaryStage) throws Exception {
    

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + BG_MINT + "; -fx-background-color: transparent;");

        return new Scene(scrollPane, 1400, 1000);
    }

    // =====================================================
    // HEADER (nav links now navigate to other screens)
    // =====================================================
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

        HBox nav = new HBox(30,
                navLink("Home", stage, "home"),
                navLink("About Us", stage, "about"),
                navLink("Features", stage, "features"),
                navLink("Contact Us", stage, "contact"));
        nav.setAlignment(Pos.CENTER);

        ComboBox<String> langBox = new ComboBox<>();
        langBox.getItems().addAll("English", "Hindi", "Marathi");
        langBox.setValue("English");

        Region spacerLeft = new Region();
        Region spacerRight = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        HBox header = new HBox(20, logoBox, spacerLeft, nav, spacerRight, langBox);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 50, 20, 50));
        header.setStyle("-fx-background-color: " + BG_MINT + ";");

        return header;
    }

    /**
     * Creates a navigation hyperlink that routes to the given target screen.
     *
     * @param label  display text
     * @param stage  the primary Stage to replace the scene on
     * @param target one of "home" | "about" | "features" | "contact"
     */
    private Hyperlink navLink(String label, Stage stage, String target) {
        Hyperlink link = new Hyperlink(label);
        link.setStyle("-fx-text-fill: #222222; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-width: 0;");
        link.setOnAction(e -> navigate(stage, target));
        return link;
    }

    private void navigate(Stage stage, String target) {
        switch (target) {
            case "home" -> {
                /* already on home */ }
            case "about" -> {
                AboutUsScreen as = new AboutUsScreen();
                as.start(stage);
            }
            case "features" -> {
                FeaturesScreen fs = new FeaturesScreen();
                fs.start(stage);
            }
            case "contact" -> {
                ContactUsScreen cs = new ContactUsScreen();
                cs.start(stage);
            }
        }
    }

    // =====================================================
    // HERO SECTION
    // =====================================================
    private HBox buildHeroSection(Stage stage) {

        Text line1 = new Text("Fresh Produce.");
        line1.setStyle("-fx-font-size: 44px; -fx-font-weight: bold; -fx-fill: " + DARK_GREEN + ";");

        Text line2 = new Text("Fair Prices.");
        line2.setStyle("-fx-font-size: 44px; -fx-font-weight: bold; -fx-fill: " + LIGHT_GREEN + ";");

        Text line3 = new Text("Stronger Future.");
        line3.setStyle("-fx-font-size: 44px; -fx-font-weight: bold; -fx-fill: #111111;");

        VBox headline = new VBox(0, line1, line2, line3);

        Text subtitle = new Text(
                "AgriLink is your one-stop platform for buying and selling " +
                        "agricultural products, exploring market prices, renting " +
                        "equipment, and getting smart farming insights.");
        subtitle.setStyle("-fx-font-size: 14px; -fx-fill: " + TEXT_GRAY + "; -fx-line-spacing: 4px;");
        subtitle.setWrappingWidth(480);

        // GET STARTED -> opens LoginScreen
        Button getStartedBtn = new Button("Get Started   \u2192");
        getStartedBtn.setStyle(
                "-fx-background-color: " + MID_GREEN + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 12 28 12 28;");
        getStartedBtn.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.start(LoginScreen.Homestage);
        });

        Button learnMoreBtn = new Button("Learn More");
        learnMoreBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + MID_GREEN + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: " + MID_GREEN + ";" +
                        "-fx-border-radius: 25px;" +
                        "-fx-border-width: 1.5px;" +
                        "-fx-padding: 11 28 11 28;");
        // "Learn More" scrolls to features or navigates to FeaturesScreen
        learnMoreBtn.setOnAction(e -> navigate(stage, "features"));

        HBox buttonRow = new HBox(15, getStartedBtn, learnMoreBtn);
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        HBox trustRow = buildTrustRow();

        VBox leftContent = new VBox(20, headline, subtitle, buttonRow, trustRow);
        leftContent.setAlignment(Pos.CENTER_LEFT);
        leftContent.setMaxWidth(520);

        StackPane rightContent = buildHeroImageWithOverlay();
        HBox.setHgrow(rightContent, Priority.ALWAYS);

        HBox hero = new HBox(40, leftContent, rightContent);
        hero.setAlignment(Pos.CENTER);
        hero.setPadding(new Insets(20, 50, 60, 50));

        return hero;
    }

    private HBox buildTrustRow() {

        HBox avatarStack = new HBox(-8);
        String[] colors = { "#A5D6A7", "#81C784", "#66BB6A", "#4CAF50" };
        for (String c : colors) {
            StackPane avatar = new StackPane();
            avatar.setPrefSize(34, 34);
            avatar.setStyle(
                    "-fx-background-color: " + c + ";" +
                            "-fx-background-radius: 50%;" +
                            "-fx-border-color: white;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 50%;");
            avatarStack.getChildren().add(avatar);
        }

        Text countBadge = new Text("10K+");
        countBadge.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-fill: " + DARK_GREEN + ";");

        Text trustText = new Text("Trusted by 10,000+\nfarmers and buyers");
        trustText.setStyle("-fx-font-size: 12px; -fx-fill: " + TEXT_GRAY + ";");

        HBox row = new HBox(12, avatarStack, countBadge, trustText);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private StackPane buildHeroImageWithOverlay() {

        ImageView farmerImage = new ImageView("file:src/main/resources/assets/icons/farmerhomepage.png");
        farmerImage.setFitWidth(560);
        farmerImage.setFitHeight(480);
        farmerImage.setPreserveRatio(false);

        Rectangle clip = new Rectangle(560, 480);
        clip.setArcWidth(80);
        clip.setArcHeight(80);
        farmerImage.setClip(clip);

        VBox overlayCard = new VBox(18);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(20);
        shadow.setColor(Color.rgb(0, 0, 0, 0.15));
        overlayCard.setEffect(shadow);

        StackPane stack = new StackPane(farmerImage, overlayCard);
        StackPane.setAlignment(overlayCard, Pos.CENTER_RIGHT);
        StackPane.setMargin(overlayCard, new Insets(0, -40, 0, 0));

        return stack;
    }

    private VBox buildWhyChooseSection() {

        Text title = new Text("\u2014  Why Choose AgriLink?  \u2014");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-fill: #111111;");

        HBox titleRow = new HBox(title);
        titleRow.setAlignment(Pos.CENTER);

        HBox cards = new HBox(20,
                whyCard("\u2705", "Verified Users", "Trusted farmers and buyers"),
                whyCard("\uD83D\uDEE1", "Secure & Reliable", "Safe transactions and data protection"),
                whyCard("\uD83C\uDF31", "Best Quality", "Fresh and high-quality agricultural products"),
                whyCard("\u20B9", "Fair Prices", "Transparent pricing for everyone"),
                whyCard("\uD83C\uDFA7", "24/7 Support", "We're here to help you anytime"));
        cards.setAlignment(Pos.CENTER);

        VBox section = new VBox(30, titleRow, cards);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(20, 50, 40, 50));

        return section;
    }

    private VBox whyCard(String icon, String title, String desc) {

        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 26px; -fx-fill: " + MID_GREEN + ";");

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #111111;");

        Text descText = new Text(desc);
        descText.setStyle("-fx-font-size: 11px; -fx-fill: " + TEXT_GRAY + ";");
        descText.setWrappingWidth(160);
        descText.setTextAlignment(TextAlignment.CENTER);

        VBox card = new VBox(10, iconText, titleText, descText);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(24, 16, 24, 16));
        card.setPrefWidth(200);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 14px;");

        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setColor(Color.rgb(0, 0, 0, 0.06));
        card.setEffect(shadow);

        return card;
    }

    private HBox buildStatsBar() {

        HBox statsRow = new HBox(0,
                statItem("\uD83D\uDC65", "5000+", "Farmers"),
                divider(),
                statItem("\uD83D\uDC64", "10000+", "Buyers"),
                divider(),
                statItem("\uD83D\uDCE6", "25000+", "Products"),
                divider(),
                statItem("\uD83D\uDCCD", "100+", "Cities Covered"));

        statsRow.setAlignment(Pos.CENTER);
        statsRow.setMaxWidth(1100);
        statsRow.setStyle("-fx-background-color: #DCEEDC; -fx-background-radius: 16px;");

        HBox outer = new HBox(statsRow);
        outer.setAlignment(Pos.CENTER);
        outer.setPadding(new Insets(0, 50, 50, 50));

        return outer;
    }

    private HBox statItem(String icon, String number, String label) {

        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 20px; -fx-fill: " + MID_GREEN + ";");

        Text numberText = new Text(number);
        numberText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #111111;");

        Text labelText = new Text(label);
        labelText.setStyle("-fx-font-size: 12px; -fx-fill: " + TEXT_GRAY + ";");

        VBox textBlock = new VBox(0, numberText, labelText);

        HBox item = new HBox(10, iconText, textBlock);
        item.setAlignment(Pos.CENTER);
        item.setPadding(new Insets(20, 40, 20, 40));

        return item;
    }

    private Region divider() {
        Region r = new Region();
        r.setPrefWidth(1);
        r.setPrefHeight(50);
        r.setStyle("-fx-background-color: #B7D6B7;");
        return r;
    }
}
