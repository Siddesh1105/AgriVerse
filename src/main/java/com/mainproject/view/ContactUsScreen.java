package com.mainproject.view;

import com.mainproject.util.ResponsiveLayout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ContactUsScreen {

    private static final String DARK_GREEN  = "#1B5E20";
    private static final String MID_GREEN   = "#2E7D32";
    private static final String BG_MINT     = "#EAF6EA";
    private static final String TEXT_GRAY   = "#666666";

    public void start(Stage stage) {
        Scene scene = buildScene(stage);
        stage.setTitle("AgriLink \u2013 Contact Us");
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
                buildContactBody()
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

        // Active underline for "Contact Us"
        contactLink.setStyle(
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
            case "features" -> { FeaturesScreen fs = new FeaturesScreen(); fs.start(stage); }
            case "contact"  -> { /* already here */ }
        }
    }

    // ─────────────────────────────────────────────
    // CONTACT BODY: 3-column layout
    // ─────────────────────────────────────────────
    private HBox buildContactBody() {
        // Column 1: Contact info
        VBox contactInfo = buildContactInfoColumn();
        contactInfo.setMaxWidth(320);

        // Column 2: Message form
        VBox messageForm = buildMessageFormColumn();
        HBox.setHgrow(messageForm, Priority.ALWAYS);
        messageForm.setMaxWidth(500);

        // Column 3: Illustration
        StackPane illustration = buildIllustration();

        HBox body = new HBox(60, contactInfo, messageForm, illustration);
        body.setAlignment(Pos.TOP_CENTER);
        body.setPadding(new Insets(50, 80, 60, 80));
        body.setStyle("-fx-background-color: white;");
        return body;
    }

    // ─────────────────────────────────────────────
    // CONTACT INFO COLUMN (left)
    // ─────────────────────────────────────────────
    private VBox buildContactInfoColumn() {
        Text heading = new Text("Contact Us");
        heading.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: #111111;");

        Region accentBar = new Region();
        accentBar.setPrefSize(50, 4);
        accentBar.setStyle("-fx-background-color: " + MID_GREEN + "; -fx-background-radius: 2px;");

        Text subtitle = new Text(
            "We\u2019d love to hear from you! Whether you have a question, " +
            "feedback, or need support, feel free to reach out to us.");
        subtitle.setStyle("-fx-font-size: 13px; -fx-fill: " + TEXT_GRAY + "; -fx-line-spacing: 4px;");
        subtitle.setWrappingWidth(300);

        VBox infoRows = new VBox(20,
                contactRow("\uD83D\uDCDE", "Phone",         "+91 9876543210"),
                contactRow("\uD83D\uDCE7", "Email",         "support@agrilink.com"),
                contactRow("\uD83D\uDCCD", "Address",
                           "AgriLink Solutions Pvt. Ltd.,\nNashik, Maharashtra, India \u2013 422001"),
                contactRow("\uD83D\uDD52", "Working Hours", "Mon \u2013 Sat: 9:00 AM \u2013 6:00 PM"));

        VBox column = new VBox(24, heading, accentBar, subtitle, infoRows);
        column.setAlignment(Pos.TOP_LEFT);
        return column;
    }

    private HBox contactRow(String icon, String title, String detail) {
        StackPane iconPane = new StackPane();
        iconPane.setPrefSize(42, 42);
        iconPane.setStyle("-fx-background-color: #E8F5E9; -fx-background-radius: 50%;");
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 16px;");
        iconPane.getChildren().add(iconText);

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #111111;");

        Text detailText = new Text(detail);
        detailText.setStyle("-fx-font-size: 12px; -fx-fill: " + TEXT_GRAY + ";");
        detailText.setWrappingWidth(220);

        VBox textBlock = new VBox(3, titleText, detailText);
        textBlock.setAlignment(Pos.TOP_LEFT);

        HBox row = new HBox(14, iconPane, textBlock);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    // ─────────────────────────────────────────────
    // MESSAGE FORM COLUMN (center)
    // ─────────────────────────────────────────────
    private VBox buildMessageFormColumn() {
        Text formTitle = new Text("Send Us a Message");
        formTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #111111;");

        Region accentBar = new Region();
        accentBar.setPrefSize(40, 3);
        accentBar.setStyle("-fx-background-color: " + MID_GREEN + "; -fx-background-radius: 2px;");

        // Name & Email row
        TextField nameField   = styledTextField("Your Name");
        TextField emailField  = styledTextField("Your Email");
        HBox nameEmailRow = new HBox(16, nameField, emailField);
        HBox.setHgrow(nameField,  Priority.ALWAYS);
        HBox.setHgrow(emailField, Priority.ALWAYS);
        nameField.setMaxWidth(Double.MAX_VALUE);
        emailField.setMaxWidth(Double.MAX_VALUE);

        TextField subjectField = styledTextField("Subject");
        subjectField.setMaxWidth(Double.MAX_VALUE);

        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Your Message");
        messageArea.setPrefRowCount(5);
        messageArea.setStyle(
            "-fx-background-color: #F9F9F9;" +
            "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-border-radius: 8px;" +
            "-fx-background-radius: 8px; -fx-font-size: 13px;" +
            "-fx-text-fill: #333333; -fx-prompt-text-fill: #AAAAAA;");
        messageArea.setMaxWidth(Double.MAX_VALUE);

        Button sendBtn = new Button("Send Message  \u2192");
        sendBtn.setStyle(
            "-fx-background-color: " + MID_GREEN + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px; -fx-font-weight: bold;" +
            "-fx-background-radius: 25px;" +
            "-fx-padding: 12 28 12 28;");
        sendBtn.setOnMouseEntered(e -> sendBtn.setStyle(
            "-fx-background-color: " + DARK_GREEN + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px; -fx-font-weight: bold;" +
            "-fx-background-radius: 25px;" +
            "-fx-padding: 12 28 12 28;" +
            "-fx-cursor: hand;"));
        sendBtn.setOnMouseExited(e -> sendBtn.setStyle(
            "-fx-background-color: " + MID_GREEN + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px; -fx-font-weight: bold;" +
            "-fx-background-radius: 25px;" +
            "-fx-padding: 12 28 12 28;"));
        sendBtn.setOnAction(e -> {

            showAlert("Message Sent!", "Thank you! We will get back to you soon.");
        });

        VBox form = new VBox(16,
                formTitle,
                accentBar,
                nameEmailRow,
                subjectField,
                messageArea,
                sendBtn);
        form.setAlignment(Pos.TOP_LEFT);
        form.setPadding(new Insets(28));
        form.setStyle(
            "-fx-background-color: #FAFCFA; -fx-background-radius: 18px;" +
            "-fx-border-color: #E8F5E9; -fx-border-width: 1; -fx-border-radius: 18px;");

        DropShadow s = new DropShadow();
        s.setRadius(16);
        s.setColor(Color.rgb(0, 0, 0, 0.06));
        form.setEffect(s);

        return form;
    }

    private TextField styledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(
            "-fx-background-color: #F9F9F9;" +
            "-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-border-radius: 8px;" +
            "-fx-background-radius: 8px; -fx-font-size: 13px;" +
            "-fx-text-fill: #333333; -fx-prompt-text-fill: #AAAAAA;" +
            "-fx-padding: 10 14 10 14;");
        return tf;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ─────────────────────────────────────────────
    // ILLUSTRATION (right column)
    // ─────────────────────────────────────────────
    private StackPane buildIllustration() {
        ImageView img = new ImageView("file:src/main/resources/assets/icons/contact_farmer.png");
        img.setFitWidth(260);
        img.setFitHeight(260);
        img.setPreserveRatio(true);

        // Fallback: colored circle if image not found
        StackPane pane = new StackPane(img);
        pane.setPrefSize(280, 280);
        pane.setStyle(
            "-fx-background-color: #E8F5E9; -fx-background-radius: 24px;");

        DropShadow s = new DropShadow();
        s.setRadius(16);
        s.setColor(Color.rgb(0, 0, 0, 0.08));
        pane.setEffect(s);
        return pane;
    }
}
