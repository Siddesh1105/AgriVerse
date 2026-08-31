

package com.mainproject.view.admin;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * AdminLogin.java
 *
 * Landing screen shown before AdminDashboard. Split screen: the
 * AgriLink branding photo on the left (logo, tagline, "Admin
 * Dashboard" title and the four feature badges are all baked into
 * that image) and the actual login form on the right.
 *
 * This class is the real application entry point - it launches first,
 * and "Login to Dashboard" hands the same Stage over to AdminDashboard.
 *
 * Java 17 / JavaFX 21.
 */
public class AdminLogin extends Application {

    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String PAGE_BG = "#eef1f4";

    // Path to the branding image used on the left panel. Drop the
    // supplied PNG in this location in the project's resources folder,
    // the same way LoginScreen.java loads its own login image.
    private static final String LEFT_PANEL_IMAGE = "file:src/main/resources/assets/icons/Adminlogin.png";

    private Stage primaryStage;

    private TextField emailField;
    private PasswordField passwordField;
    private TextField passwordVisibleField;
    private Button toggleVisibilityButton;
    private CheckBox rememberMeCheckBox;

    private boolean passwordVisible = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        Scene scene = new Scene(buildRoot(), 1360, 860);
        stage.setTitle("AgriLink Admin - Login");
        stage.setScene(scene);
        stage.show();
    }

    // ---------- layout ----------

    private StackPane buildRoot() {
        StackPane page = new StackPane();
        page.setStyle("-fx-background-color: " + PAGE_BG + ";");
        page.setPadding(new Insets(40));

        HBox card = new HBox();
        card.setMaxWidth(1280);
        card.setMaxHeight(780);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 25, 0, 0, 10);");

        Region leftPanel = buildLeftPanel();
        VBox rightPanel = buildRightPanel();

        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
        leftPanel.setPrefWidth(640);
        rightPanel.setPrefWidth(640);

        // clip so the left panel's square corners don't poke out of the rounded card
        Rectangle clip = new Rectangle(1280, 780);
        clip.setArcWidth(40);
        clip.setArcHeight(40);
        card.setClip(clip);

        card.getChildren().addAll(leftPanel, rightPanel);
        page.getChildren().add(card);
        return page;
    }

    // ---------- left branding panel ----------

    /**
     * The left panel is now just the supplied branding photo - it
     * already contains the leaf logo, "AgriLink" wordmark, tagline,
     * "Admin Dashboard" title/subtitle and the four feature badges, so
     * there is nothing else to lay out here. The ImageView is bound to
     * the panel's own size so it always fills the left half of the
     * card, however the window is resized.
     */
    private Region buildLeftPanel() {
        StackPane panel = new StackPane();
        panel.setStyle("-fx-background-color: " + GREEN_DARK + ";"); // fallback while the image loads

        ImageView brandingImage = new ImageView(new Image(LEFT_PANEL_IMAGE, true));
        brandingImage.setPreserveRatio(false);
        brandingImage.fitWidthProperty().bind(panel.widthProperty());
        brandingImage.fitHeightProperty().bind(panel.heightProperty());

        panel.getChildren().add(brandingImage);
        return panel;
    }

    // ---------- right login form ----------

    private VBox buildRightPanel() {
        VBox panel = new VBox(16);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(50, 70, 40, 70));
        panel.setStyle("-fx-background-color: white;");

        Label shieldIcon = new Label("\uD83D\uDD12");
        shieldIcon.setFont(Font.font(26));
        shieldIcon.setTextFill(Color.WHITE);
        shieldIcon.setStyle("-fx-background-color: " + GREEN + "; -fx-background-radius: 50; -fx-padding: 14;");
        shieldIcon.setMinSize(56, 56);
        shieldIcon.setAlignment(Pos.CENTER);

        Label title = new Label("Admin Login");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        Label subtitle = new Label("Welcome back! Please login to continue.");
        subtitle.setFont(Font.font("Segoe UI", 13));
        subtitle.setTextFill(Color.web("#777777"));

        VBox emailBlock = buildEmailField();
        VBox passwordBlock = buildPasswordField();
        HBox rememberRow = buildRememberRow();
        Button loginButton = buildLoginButton();
        HBox divider = buildOrDivider();

        VBox footer = buildFooter();

        VBox formBlock = new VBox(14, emailBlock, passwordBlock, rememberRow, loginButton, divider);
        formBlock.setMaxWidth(420);

        VBox headerBlock = new VBox(6, title, subtitle);
        headerBlock.setAlignment(Pos.CENTER);
        headerBlock.setPadding(new Insets(0, 0, 20, 0));

        panel.getChildren().addAll(shieldIcon, headerBlock, formBlock, footer);
        return panel;
    }

    private VBox buildEmailField() {
        Label label = new Label("Email Address");
        label.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));

        emailField = new TextField();
        emailField.setPromptText("Enter your email address");
        emailField.setPrefHeight(42);

        HBox wrapper = fieldWrapper("\uD83D\uDC64", emailField);

        VBox box = new VBox(6, label, wrapper);
        return box;
    }

    private VBox buildPasswordField() {
        Label label = new Label("Password");
        label.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(42);
        passwordField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        passwordField.setOnAction(e -> handleLogin());

        passwordVisibleField = new TextField();
        passwordVisibleField.setPromptText("Enter your password");
        passwordVisibleField.setPrefHeight(42);
        passwordVisibleField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        passwordVisibleField.setManaged(false);
        passwordVisibleField.setVisible(false);
        passwordVisibleField.setOnAction(e -> handleLogin());
        // keep both fields showing the same text no matter which one is active
        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());

        StackPane fieldStack = new StackPane(passwordField, passwordVisibleField);

        toggleVisibilityButton = new Button("Show");
        toggleVisibilityButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #666666; -fx-font-size: 11px;");
        toggleVisibilityButton.setOnAction(e -> togglePasswordVisibility());

        HBox wrapper = fieldWrapper("\uD83D\uDD12", fieldStack);
        wrapper.getChildren().add(toggleVisibilityButton);

        VBox box = new VBox(6, label, wrapper);
        return box;
    }

    /** Small helper that puts a leading icon in front of any field-like node, inside a bordered box. */
    private HBox fieldWrapper(String icon, javafx.scene.Node field) {
        HBox wrapper = new HBox(8);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        wrapper.setPadding(new Insets(0, 12, 0, 12));
        wrapper.setStyle("-fx-background-color: white; -fx-border-color: #d8dee2; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label iconLabel = new Label(icon);
        iconLabel.setTextFill(Color.web("#888888"));

        HBox.setHgrow(field, Priority.ALWAYS);
        if (field instanceof Region region) {
            region.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        }

        wrapper.getChildren().addAll(iconLabel, field);
        return wrapper;
    }

    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        passwordField.setVisible(!passwordVisible);
        passwordField.setManaged(!passwordVisible);
        passwordVisibleField.setVisible(passwordVisible);
        passwordVisibleField.setManaged(passwordVisible);
        toggleVisibilityButton.setText(passwordVisible ? "Hide" : "Show");
    }

    private HBox buildRememberRow() {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);

        rememberMeCheckBox = new CheckBox("Remember me");
        rememberMeCheckBox.setSelected(true);
        rememberMeCheckBox.setFont(Font.font("Segoe UI", 12));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Hyperlink forgotPassword = new Hyperlink("Forgot Password?");
        forgotPassword.setFont(Font.font("Segoe UI", 12));
        forgotPassword.setTextFill(Color.web(GREEN));
        forgotPassword.setOnAction(e -> handleForgotPassword());

        row.getChildren().addAll(rememberMeCheckBox, spacer, forgotPassword);
        return row;
    }

    private Button buildLoginButton() {
        Button button = new Button("\u2192  Login to Dashboard");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(46);
        button.setStyle("-fx-background-color: " + GREEN + "; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 8;");
        button.setOnAction(e -> handleLogin());
        return button;
    }

    private HBox buildOrDivider() {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER);
        Separator left = new Separator();
        Separator right = new Separator();
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        Label or = new Label("OR");
        or.setFont(Font.font("Segoe UI", 11));
        or.setTextFill(Color.web("#999999"));
        row.getChildren().addAll(left, or, right);
        return row;
    }

    private Button buildGoogleButton() {
        Button button = new Button("G  Login with Google");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(46);
        button.setStyle("-fx-background-color: white; -fx-text-fill: #333333; -fx-font-weight: bold; " +
                "-fx-font-size: 13px; -fx-border-color: #d8dee2; -fx-border-radius: 8; -fx-background-radius: 8;");
        button.setOnAction(e -> handleGoogleLogin());
        return button;
    }

    private VBox buildFooter() {
        VBox footer = new VBox(6);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(24, 0, 0, 0));

        Label secureNote = new Label("\uD83D\uDD12 Secure admin access. All activities are monitored.");
        secureNote.setFont(Font.font("Segoe UI", 11));
        secureNote.setTextFill(Color.web("#888888"));

        Label copyright = new Label("AgriLink Admin Panel \u00A9 2025 All rights reserved.");
        copyright.setFont(Font.font("Segoe UI", 11));
        copyright.setTextFill(Color.web("#999999"));

        footer.getChildren().addAll(secureNote, copyright);
        return footer;
    }

    // ---------- actions ----------

    private void handleLogin() {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing details", "Please enter both your email address and password.");
            return;
        }

        if (!authenticate(email, password)) {
            showAlert(Alert.AlertType.ERROR, "Login failed", "The email or password you entered is incorrect.");
            return;
        }

        navigateToDashboard();
    }

    /**
     * Placeholder authentication check. Replace this with a real call
     * against your user/admin DAO (e.g. checking a hashed password in
     * Firestore or the database) - it currently just accepts any
     * non-empty email/password so the UI flow can be demoed end to end.
     */
    private boolean authenticate(String email, String password) {
        return !email.isBlank() && !password.isBlank();
    }

    private void navigateToDashboard() {
        AdminDashboard adminDashboard = new AdminDashboard();
        adminDashboard.showDashboard(primaryStage);
    }

    private void handleForgotPassword() {
        TextInputDialog dialog = new TextInputDialog(emailField.getText());
        dialog.setTitle("Forgot Password");
        dialog.setHeaderText("Reset your password");
        dialog.setContentText("Enter your email address:");
        dialog.showAndWait().ifPresent(email ->
                showAlert(Alert.AlertType.INFORMATION, "Reset link sent",
                        "If an account exists for " + email + ", a password reset link has been sent."));
    }

    private void handleGoogleLogin() {
        showAlert(Alert.AlertType.INFORMATION, "Login with Google",
                "Google sign-in isn't wired up to a provider yet in this demo.");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}