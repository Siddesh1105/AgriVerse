package com.mainproject.view;

import com.mainproject.util.ResponsiveLayout;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
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

    // =====================================================
    // COLORS
    // =====================================================

    private static final String DARK_GREEN = "#1B5E20";
    private static final String MID_GREEN = "#2E7D32";
    private static final String LIGHT_GREEN = "#66BB6A";
    private static final String BG_MINT = "#EAF6EA";
    private static final String TEXT_GRAY = "#666666";
    private static final String DARK_TEXT = "#1F2937";

    @Override
    public void start(Stage myStage) {

        LoginScreen.Homestage = myStage;

        Scene homeScene = buildHomeScene(myStage);

        myStage.setTitle("AgriLink");
        ResponsiveLayout.prepareStage(myStage);

        myStage.setScene(homeScene);
        myStage.show();
    }

    // =====================================================
    // ROOT LAYOUT
    // =====================================================

    private Scene buildHomeScene(Stage stage) {

        VBox root = new VBox();
        root.setStyle(
                "-fx-background-color: " + BG_MINT + ";"
        );

        root.getChildren().addAll(
                buildHeader(stage),
                buildHeroSection(stage),
                buildWhyChooseSection(),
                buildStatsBar()
        );

        ScrollPane scrollPane = new ScrollPane(root);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                "-fx-background: " + BG_MINT + ";" +
                "-fx-background-color: " + BG_MINT + ";" +
                "-fx-border-color: transparent;"
        );

        // No fixed resolution
        Scene scene = ResponsiveLayout.createScene(scrollPane);

        return scene;
    }

    // =====================================================
    // HEADER
    // =====================================================

    private HBox buildHeader(Stage stage) {

        ImageView leafIcon = new ImageView(
                "file:src/main/resources/assets/icons/leaf_logo.png"
        );

        leafIcon.setFitWidth(38);
        leafIcon.setFitHeight(38);
        leafIcon.setPreserveRatio(true);

        Text brand = new Text("AgriLink");

        brand.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: " + DARK_GREEN + ";"
        );

        Text tagline = new Text(
                "Connecting Farmers, Buyers\nand Agriculture"
        );

        tagline.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-fill: " + TEXT_GRAY + ";"
        );

        VBox brandBlock = new VBox(2, brand, tagline);

        HBox logoBox = new HBox(
                10,
                leafIcon,
                brandBlock
        );

        logoBox.setAlignment(Pos.CENTER_LEFT);

        // =================================================
        // NAVIGATION
        // =================================================

        HBox nav = new HBox(
                28,
                navLink("Home", stage, "home"),
                navLink("About Us", stage, "about"),
                navLink("Features", stage, "features"),
                navLink("Contact Us", stage, "contact")
        );

        nav.setAlignment(Pos.CENTER);

        ComboBox<String> langBox = new ComboBox<>();

        langBox.getItems().addAll(
                "English",
                "Hindi",
                "Marathi"
        );

        langBox.setValue("English");

        langBox.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #B7D6B7;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );

        Region spacerLeft = new Region();
        Region spacerRight = new Region();

        HBox.setHgrow(
                spacerLeft,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                spacerRight,
                Priority.ALWAYS
        );

        HBox header = new HBox(
                20,
                logoBox,
                spacerLeft,
                nav,
                spacerRight,
                langBox
        );

        header.setAlignment(Pos.CENTER);

        header.setPadding(
                new Insets(18, 5, 18, 5)
        );

        header.setMaxWidth(Double.MAX_VALUE);

        header.setStyle(
                "-fx-background-color: " + BG_MINT + ";" +
                "-fx-border-color: transparent transparent #D5E8D5 transparent;" +
                "-fx-border-width: 0 0 1 0;"
        );

        return header;
    }

    // =====================================================
    // NAVIGATION LINK
    // =====================================================

    private Hyperlink navLink(
            String label,
            Stage stage,
            String target) {

        Hyperlink link = new Hyperlink(label);

        link.setStyle(
                "-fx-text-fill: #263238;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-border-width: 0;" +
                "-fx-padding: 5 8 5 8;"
        );

        link.setOnMouseEntered(e ->
                link.setStyle(
                        "-fx-text-fill: " + MID_GREEN + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-width: 0;" +
                        "-fx-padding: 5 8 5 8;"
                )
        );

        link.setOnMouseExited(e ->
                link.setStyle(
                        "-fx-text-fill: #263238;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-width: 0;" +
                        "-fx-padding: 5 8 5 8;"
                )
        );

        link.setOnAction(
                e -> navigate(stage, target)
        );

        return link;
    }

    // =====================================================
    // NAVIGATION
    // =====================================================

    private void navigate(
            Stage stage,
            String target) {

        switch (target) {

            case "home" -> {
                // Already on Home Screen
            }

            case "about" -> {
                AboutUsScreen aboutUsScreen =
                        new AboutUsScreen();

                aboutUsScreen.start(stage);
            }

            case "features" -> {
                FeaturesScreen featuresScreen =
                        new FeaturesScreen();

                featuresScreen.start(stage);
            }

            case "contact" -> {
                ContactUsScreen contactUsScreen =
                        new ContactUsScreen();

                contactUsScreen.start(stage);
            }
        }
    }

    // =====================================================
    // HERO SECTION
    // =====================================================

    private HBox buildHeroSection(Stage stage) {

        // =================================================
        // LEFT CONTENT
        // =================================================

        Text line1 = new Text("Fresh Produce.");

        line1.setStyle(
                "-fx-font-size: 44px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: " + DARK_GREEN + ";"
        );

        Text line2 = new Text("Fair Prices.");

        line2.setStyle(
                "-fx-font-size: 44px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: " + LIGHT_GREEN + ";"
        );

        Text line3 = new Text("Stronger Future.");

        line3.setStyle(
                "-fx-font-size: 44px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: " + DARK_TEXT + ";"
        );

        VBox headline = new VBox(
                0,
                line1,
                line2,
                line3
        );

        Text subtitle = new Text(
                "AgriLink is your one-stop platform for buying and selling " +
                "agricultural products, exploring market prices, renting " +
                "equipment, and getting smart farming insights."
        );

        subtitle.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-fill: " + TEXT_GRAY + ";" +
                "-fx-line-spacing: 4px;"
        );

        subtitle.setWrappingWidth(500);

        // =================================================
        // GET STARTED BUTTON
        // =================================================

        Button getStartedBtn = new Button(
                "Get Started   →"
        );

        getStartedBtn.setStyle(
                "-fx-background-color: " + MID_GREEN + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25px;" +
                "-fx-padding: 12 28 12 28;" +
                "-fx-cursor: hand;"
        );

        getStartedBtn.setOnAction(e -> {

            LoginScreen loginScreen =
                    new LoginScreen();

            loginScreen.start(
                    LoginScreen.Homestage
            );
        });

        // =================================================
        // LEARN MORE BUTTON
        // =================================================

        Button learnMoreBtn =
                new Button("Learn More");

        learnMoreBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + MID_GREEN + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: " + MID_GREEN + ";" +
                "-fx-border-radius: 25px;" +
                "-fx-background-radius: 25px;" +
                "-fx-border-width: 1.5px;" +
                "-fx-padding: 11 28 11 28;" +
                "-fx-cursor: hand;"
        );

        learnMoreBtn.setOnAction(
                e -> navigate(stage, "features")
        );

        HBox buttonRow = new HBox(
                15,
                getStartedBtn,
                learnMoreBtn
        );

        buttonRow.setAlignment(Pos.CENTER_LEFT);

        HBox trustRow = buildTrustRow();

        VBox leftContent = new VBox(
                20,
                headline,
                subtitle,
                buttonRow,
                trustRow
        );

        leftContent.setAlignment(Pos.CENTER_LEFT);

        leftContent.setPrefWidth(520);
        leftContent.setMaxWidth(560);

        HBox.setHgrow(
                leftContent,
                Priority.ALWAYS
        );

        // =================================================
        // RIGHT CONTENT
        // =================================================

        StackPane rightContent =
                buildHeroImageWithOverlay();

        rightContent.setMaxWidth(650);

        HBox.setHgrow(
                rightContent,
                Priority.ALWAYS
        );

        // =================================================
        // HERO CONTAINER
        // =================================================

        HBox hero = new HBox(
                50,
                leftContent,
                rightContent
        );

        hero.setAlignment(Pos.CENTER);

        hero.setPadding(
                new Insets(45, 5, 55, 5)
        );

        hero.setMaxWidth(1300);

        HBox outer = new HBox(hero);

        outer.setAlignment(Pos.CENTER);

        outer.setPadding(
                new Insets(0, 30, 0, 30)
        );

        return outer;
    }

    // =====================================================
    // TRUST ROW
    // =====================================================

    private HBox buildTrustRow() {

        HBox avatarStack = new HBox(-8);

        String[] colors = {
                "#A5D6A7",
                "#81C784",
                "#66BB6A",
                "#4CAF50"
        };

        for (String color : colors) {

            StackPane avatar = new StackPane();

            avatar.setPrefSize(34, 34);

            avatar.setStyle(
                    "-fx-background-color: " + color + ";" +
                    "-fx-background-radius: 50%;" +
                    "-fx-border-color: white;" +
                    "-fx-border-width: 2px;" +
                    "-fx-border-radius: 50%;"
            );

            avatarStack.getChildren().add(avatar);
        }

        Text countBadge =
                new Text("10K+");

        countBadge.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: " + DARK_GREEN + ";"
        );

        Text trustText = new Text(
                "Trusted by 10,000+\nfarmers and buyers"
        );

        trustText.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-fill: " + TEXT_GRAY + ";"
        );

        HBox row = new HBox(
                12,
                avatarStack,
                countBadge,
                trustText
        );

        row.setAlignment(Pos.CENTER_LEFT);

        return row;
    }

    // =====================================================
    // HERO IMAGE
    // =====================================================

    private StackPane buildHeroImageWithOverlay() {

        ImageView farmerImage = new ImageView(
                "file:src/main/resources/assets/icons/farmerhomepage.png"
        );

        // Responsive image sizing
        farmerImage.setFitWidth(560);
        farmerImage.setFitHeight(460);

        farmerImage.setPreserveRatio(false);

        Rectangle clip =
                new Rectangle(560, 460);

        clip.setArcWidth(45);
        clip.setArcHeight(45);

        farmerImage.setClip(clip);

        StackPane imageContainer =
                new StackPane(farmerImage);

        imageContainer.setMaxWidth(560);
        imageContainer.setPrefWidth(560);

        DropShadow shadow = new DropShadow();

        shadow.setRadius(18);

        shadow.setColor(
                Color.rgb(0, 0, 0, 0.12)
        );

        imageContainer.setEffect(shadow);

        return imageContainer;
    }

    // =====================================================
    // WHY CHOOSE SECTION
    // =====================================================

    private VBox buildWhyChooseSection() {

        Text title =
                new Text("Why Choose AgriLink?");

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: " + DARK_TEXT + ";"
        );

        Text subtitle = new Text(
                "Everything you need for a better agricultural experience"
        );

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-fill: " + TEXT_GRAY + ";"
        );

        VBox titleBox = new VBox(
                8,
                title,
                subtitle
        );

        titleBox.setAlignment(Pos.CENTER);

        // FlowPane automatically wraps cards
        // on smaller resolutions
        FlowPane cards = new FlowPane();

        cards.setHgap(20);
        cards.setVgap(20);

        cards.setAlignment(Pos.CENTER);

        cards.getChildren().addAll(

                whyCard(
                        "✓",
                        "Verified Users",
                        "Trusted farmers and buyers"
                ),

                whyCard(
                        "🛡",
                        "Secure & Reliable",
                        "Safe transactions and data protection"
                ),

                whyCard(
                        "🌱",
                        "Best Quality",
                        "Fresh and high-quality agricultural products"
                ),

                whyCard(
                        "₹",
                        "Fair Prices",
                        "Transparent pricing for everyone"
                ),

                whyCard(
                        "🎧",
                        "24/7 Support",
                        "We're here to help you anytime"
                )
        );

        VBox section = new VBox(
                30,
                titleBox,
                cards
        );

        section.setAlignment(Pos.CENTER);

        section.setPadding(
                new Insets(55, 30, 55, 30)
        );

        return section;
    }

    // =====================================================
    // WHY CARD
    // =====================================================

    private VBox whyCard(
            String icon,
            String title,
            String desc) {

        Text iconText = new Text(icon);

        iconText.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-fill: " + MID_GREEN + ";"
        );

        Text titleText = new Text(title);

        titleText.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: " + DARK_TEXT + ";"
        );

        Text descText = new Text(desc);

        descText.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-fill: " + TEXT_GRAY + ";"
        );

        descText.setWrappingWidth(180);

        descText.setTextAlignment(
                TextAlignment.CENTER
        );

        VBox card = new VBox(
                12,
                iconText,
                titleText,
                descText
        );

        card.setAlignment(Pos.CENTER);

        card.setPadding(
                new Insets(25, 18, 25, 18)
        );

        card.setPrefWidth(210);
        card.setMinWidth(190);
        card.setMinHeight(175);

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 16px;" +
                "-fx-border-color: #E2EEE2;" +
                "-fx-border-radius: 16px;"
        );

        DropShadow shadow =
                new DropShadow();

        shadow.setRadius(12);

        shadow.setColor(
                Color.rgb(0, 0, 0, 0.07)
        );

        card.setEffect(shadow);

        return card;
    }

    // =====================================================
    // STATS BAR
    // =====================================================

    private HBox buildStatsBar() {

        FlowPane statsFlow = new FlowPane();

        statsFlow.setHgap(0);
        statsFlow.setVgap(10);

        statsFlow.setAlignment(Pos.CENTER);

        statsFlow.getChildren().addAll(

                statItem("👥", "5000+", "Farmers"),
                statItem("👤", "10000+", "Buyers"),
                statItem("📦", "25000+", "Products"),
                statItem("📍", "100+", "Cities Covered")
        );

        statsFlow.setMaxWidth(1100);

        statsFlow.setPadding(
                new Insets(10)
        );

        statsFlow.setStyle(
                "-fx-background-color: #DCEEDC;" +
                "-fx-background-radius: 18px;"
        );

        HBox outer = new HBox(statsFlow);

        outer.setAlignment(Pos.CENTER);

        outer.setPadding(
                new Insets(0, 30, 55, 30)
        );

        return outer;
    }

    // =====================================================
    // STAT ITEM
    // =====================================================

    private HBox statItem(
            String icon,
            String number,
            String label) {

        Text iconText = new Text(icon);

        iconText.setStyle(
                "-fx-font-size: 22px;"
        );

        Text numberText = new Text(number);

        numberText.setStyle(
                "-fx-font-size: 21px;" +
                "-fx-font-weight: bold;" +
                "-fx-fill: " + DARK_TEXT + ";"
        );

        Text labelText = new Text(label);

        labelText.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-fill: " + TEXT_GRAY + ";"
        );

        VBox textBlock = new VBox(
                2,
                numberText,
                labelText
        );

        HBox item = new HBox(
                10,
                iconText,
                textBlock
        );

        item.setAlignment(Pos.CENTER);

        item.setPrefWidth(230);

        item.setPadding(
                new Insets(20, 25, 20, 25)
        );

        return item;
    }
}