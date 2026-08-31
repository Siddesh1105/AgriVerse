package com.mainproject.view.admin;

import com.mainproject.controller.AuthController;
import com.mainproject.controller.UserController;
import com.mainproject.model.User;
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

/** Admin login screen. Keeps the supplied login UI and uses existing Firebase authentication. */
public class AdminLoginScreen extends Application {
    private static final String ADMIN_EMAIL = "admin@agriverse.com";
    private static final String GREEN = "#1f7a3d";
    private static final String GREEN_DARK = "#14532d";
    private static final String PAGE_BG = "#eef1f4";
    private static final String LEFT_PANEL_IMAGE = "file:src/main/resources/assets/icons/Adminlogin.png";

    private final AuthController authController = new AuthController();
    private final UserController userController = new UserController();
    private Stage primaryStage;
    private TextField emailField;
    private PasswordField passwordField;
    private TextField passwordVisibleField;
    private Button toggleVisibilityButton;
    private CheckBox rememberMeCheckBox;
    private boolean passwordVisible = false;

    @Override public void start(Stage stage) {
        primaryStage = stage;
        stage.setTitle("AgriVerse Admin - Login");
        stage.setScene(new Scene(buildRoot(), 1360, 860));
        stage.show();
    }

    private StackPane buildRoot() {
        StackPane page = new StackPane(); page.setStyle("-fx-background-color: " + PAGE_BG + ";"); page.setPadding(new Insets(40));
        HBox card = new HBox(); card.setMaxWidth(1280); card.setMaxHeight(780);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 25, 0, 0, 10);");
        Region left = buildLeftPanel(); VBox right = buildRightPanel();
        HBox.setHgrow(left, Priority.ALWAYS); HBox.setHgrow(right, Priority.ALWAYS); left.setPrefWidth(640); right.setPrefWidth(640);
        Rectangle clip = new Rectangle(1280,780); clip.setArcWidth(40); clip.setArcHeight(40); card.setClip(clip);
        card.getChildren().addAll(left,right); page.getChildren().add(card); return page;
    }

    private Region buildLeftPanel() {
        StackPane panel = new StackPane(); panel.setStyle("-fx-background-color: " + GREEN_DARK + ";");
        try {
            Image image = new Image(LEFT_PANEL_IMAGE, true);
            ImageView view = new ImageView(image); view.setPreserveRatio(false); view.fitWidthProperty().bind(panel.widthProperty()); view.fitHeightProperty().bind(panel.heightProperty());
            panel.getChildren().add(view);
        } catch (Exception ignored) { }
        VBox fallback = new VBox(18); fallback.setAlignment(Pos.CENTER); fallback.setPadding(new Insets(50));
        Label logo=new Label("🌿 AgriVerse"); logo.setTextFill(Color.WHITE); logo.setFont(Font.font("Segoe UI",FontWeight.BOLD,38));
        Label title=new Label("Admin Dashboard"); title.setTextFill(Color.WHITE); title.setFont(Font.font("Segoe UI",FontWeight.BOLD,28));
        Label sub=new Label("Securely manage your AgriVerse platform"); sub.setTextFill(Color.web("#c8e6c9")); sub.setFont(Font.font("Segoe UI",16)); fallback.getChildren().addAll(logo,title,sub);
        panel.getChildren().add(fallback); return panel;
    }

    private VBox buildRightPanel() {
        VBox panel=new VBox(16); panel.setAlignment(Pos.CENTER); panel.setPadding(new Insets(50,70,40,70)); panel.setStyle("-fx-background-color:white;");
        Label shield=new Label("🔒"); shield.setFont(Font.font(26)); shield.setTextFill(Color.WHITE); shield.setStyle("-fx-background-color:"+GREEN+"; -fx-background-radius:50; -fx-padding:14;"); shield.setMinSize(56,56); shield.setAlignment(Pos.CENTER);
        Label title=new Label("Admin Login"); title.setFont(Font.font("Segoe UI",FontWeight.BOLD,28));
        Label subtitle=new Label("Welcome back! Please login to continue."); subtitle.setFont(Font.font("Segoe UI",13)); subtitle.setTextFill(Color.web("#777777"));
        VBox header=new VBox(6,title,subtitle); header.setAlignment(Pos.CENTER); header.setPadding(new Insets(0,0,20,0));
        VBox form=new VBox(14,buildEmailField(),buildPasswordField(),buildRememberRow(),buildLoginButton()); form.setMaxWidth(420);
        panel.getChildren().addAll(shield,header,form,buildFooter()); return panel;
    }

    private VBox buildEmailField(){ Label l=new Label("Email Address"); l.setFont(Font.font("Segoe UI",FontWeight.SEMI_BOLD,12)); emailField=new TextField(); emailField.setPromptText("Enter your email address"); emailField.setPrefHeight(42); return new VBox(6,l,fieldWrapper("👤",emailField)); }
    private VBox buildPasswordField(){ Label l=new Label("Password"); l.setFont(Font.font("Segoe UI",FontWeight.SEMI_BOLD,12)); passwordField=new PasswordField(); passwordField.setPromptText("Enter your password"); passwordField.setPrefHeight(42); passwordField.setStyle("-fx-background-color:transparent;-fx-border-color:transparent;"); passwordField.setOnAction(e->handleLogin()); passwordVisibleField=new TextField(); passwordVisibleField.setPromptText("Enter your password"); passwordVisibleField.setStyle("-fx-background-color:transparent;-fx-border-color:transparent;"); passwordVisibleField.setManaged(false); passwordVisibleField.setVisible(false); passwordVisibleField.setOnAction(e->handleLogin()); passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty()); StackPane stack=new StackPane(passwordField,passwordVisibleField); toggleVisibilityButton=new Button("Show"); toggleVisibilityButton.setStyle("-fx-background-color:transparent;-fx-text-fill:#666666;-fx-font-size:11px;"); toggleVisibilityButton.setOnAction(e->togglePasswordVisibility()); HBox w=fieldWrapper("🔒",stack); w.getChildren().add(toggleVisibilityButton); return new VBox(6,l,w); }
    private HBox fieldWrapper(String icon, javafx.scene.Node field){ HBox w=new HBox(8); w.setAlignment(Pos.CENTER_LEFT); w.setPadding(new Insets(0,12,0,12)); w.setStyle("-fx-background-color:white;-fx-border-color:#d8dee2;-fx-border-radius:8;-fx-background-radius:8;"); Label i=new Label(icon); HBox.setHgrow(field,Priority.ALWAYS); w.getChildren().addAll(i,field); return w; }
    private void togglePasswordVisibility(){ passwordVisible=!passwordVisible; passwordField.setVisible(!passwordVisible); passwordField.setManaged(!passwordVisible); passwordVisibleField.setVisible(passwordVisible); passwordVisibleField.setManaged(passwordVisible); toggleVisibilityButton.setText(passwordVisible?"Hide":"Show"); }
    private HBox buildRememberRow(){ HBox row=new HBox(); row.setAlignment(Pos.CENTER_LEFT); rememberMeCheckBox=new CheckBox("Remember me"); rememberMeCheckBox.setSelected(true); Region s=new Region(); HBox.setHgrow(s,Priority.ALWAYS); Hyperlink forgot=new Hyperlink("Forgot Password?"); forgot.setTextFill(Color.web(GREEN)); forgot.setOnAction(e->handleForgotPassword()); row.getChildren().addAll(rememberMeCheckBox,s,forgot); return row; }
    private Button buildLoginButton(){ Button b=new Button("→  Login to Dashboard"); b.setMaxWidth(Double.MAX_VALUE); b.setPrefHeight(46); b.setStyle("-fx-background-color:"+GREEN+";-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:14px;-fx-background-radius:8;"); b.setOnAction(e->handleLogin()); return b; }
    private VBox buildFooter(){ VBox f=new VBox(6); f.setAlignment(Pos.CENTER); f.setPadding(new Insets(24,0,0,0)); Label secure=new Label("🔒 Secure admin access."); secure.setTextFill(Color.web("#888888")); Label copy=new Label("AgriVerse Admin Panel © 2026 All rights reserved."); copy.setTextFill(Color.web("#999999")); f.getChildren().addAll(secure,copy); return f; }

    private void handleLogin(){
        String email=emailField.getText()==null?"":emailField.getText().trim(); String password=passwordField.getText()==null?"":passwordField.getText();
        if(email.isEmpty()||password.isEmpty()){ showAlert(Alert.AlertType.WARNING,"Missing details","Please enter both your email address and password."); return; }
        if(!ADMIN_EMAIL.equalsIgnoreCase(email)){ showAlert(Alert.AlertType.ERROR,"Access denied","Only the AgriVerse administrator account can access this dashboard."); return; }
        boolean ok=authController.signIn(email,password); if(!ok){ showAlert(Alert.AlertType.ERROR,"Login failed","Invalid admin email or password."); return; }
        User user=userController.getUserByEmail(email); if(user==null){ showAlert(Alert.AlertType.ERROR,"Admin setup required","Admin details were not found in Firestore."); return; }
        if(user.getRole()==null || !"Admin".equalsIgnoreCase(user.getRole().trim())){ showAlert(Alert.AlertType.ERROR,"Access denied","This account does not have the Admin role."); return; }
        navigateToDashboard();
    }
    private void navigateToDashboard(){ try { new AdminDashboard().start(primaryStage); } catch(Exception e){ e.printStackTrace(); showAlert(Alert.AlertType.ERROR,"Dashboard error","Unable to open the Admin Dashboard: "+e.getMessage()); } }
    private void handleForgotPassword(){ TextInputDialog d=new TextInputDialog(emailField.getText()); d.setTitle("Forgot Password"); d.setHeaderText("Reset your password"); d.setContentText("Enter your admin email address:"); d.showAndWait().ifPresent(email->{ if(!ADMIN_EMAIL.equalsIgnoreCase(email.trim())){ showAlert(Alert.AlertType.ERROR,"Invalid account","Only the administrator email can be reset here."); return; } boolean sent=authController.resetPassword(email.trim()); showAlert(sent?Alert.AlertType.INFORMATION:Alert.AlertType.ERROR,sent?"Reset link sent":"Reset failed",sent?"Password reset email has been sent.":"Unable to send password reset email."); }); }
    private void showAlert(Alert.AlertType type,String title,String message){ Alert a=new Alert(type); a.setTitle(title); a.setHeaderText(null); a.setContentText(message); a.showAndWait(); }
}
