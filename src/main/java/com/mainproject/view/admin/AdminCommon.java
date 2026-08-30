package com.mainproject.view.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/** Shared UI helpers for admin screens. */
final class AdminCommon {
    static final String GREEN = "#1f7a3d";
    static final String GREEN_DARK = "#14532d";
    static final String BG = "#f4f6f5";
    static final String CARD = "-fx-background-color:white;-fx-background-radius:12;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.08),8,0,0,2);";

    private AdminCommon() {}

    static VBox sidebar(Stage stage, AdminDashboard dashboard, String active) {
        VBox side = new VBox();
        side.setPrefWidth(230);
        side.setStyle("-fx-background-color:" + GREEN_DARK + ";");
        VBox logo = new VBox(2);
        logo.setPadding(new Insets(22,15,22,20));
        Label l = new Label("🌿  AgriLink"); l.setTextFill(Color.WHITE); l.setFont(Font.font("Segoe UI", FontWeight.BOLD,20));
        Label s = new Label("Admin Dashboard"); s.setTextFill(Color.web("#c8e6c9"));
        logo.getChildren().addAll(l,s);
        String[] items={"Dashboard","User Management","Farmer Verification","Product Management","Order Management","Live Marketplace","Equipment Management","Analytics & Reports","Crop Price Management","AI & Smart Tools","Notifications","Content Management","Feedback & Reviews","Reports & Complaints","Payment Management","Audit Logs","SystemDataManage"};
        VBox nav = new VBox(1);
        for(String item:items){
            Button b=new Button(item); b.setPrefWidth(228); b.setAlignment(Pos.CENTER_LEFT); b.setPadding(new Insets(11,20,11,20)); b.setFont(Font.font("Segoe UI",13));
            if(item.equals(active)) active(b); else inactive(b);
            b.setOnAction(e -> dashboard.navigateToPage(item)); nav.getChildren().add(b);
        }
        ScrollPane sp=new ScrollPane(nav); sp.setFitToWidth(true); sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); sp.setStyle("-fx-background-color:transparent;-fx-background:"+GREEN_DARK+";"); VBox.setVgrow(sp,Priority.ALWAYS);
        side.getChildren().addAll(logo,sp); return side;
    }
    static HBox topBar(String title, Runnable toggle, Runnable refresh) {
        HBox bar=new HBox(15); bar.setPadding(new Insets(14,25,14,20)); bar.setAlignment(Pos.CENTER_LEFT); bar.setStyle("-fx-background-color:white;-fx-border-color:#eaeaea;-fx-border-width:0 0 1 0;");
        Button menu=new Button("☰"); menu.setStyle("-fx-background-color:transparent;-fx-font-size:16;"); menu.setOnAction(e->toggle.run());
        Label t=new Label(title); t.setFont(Font.font("Segoe UI",FontWeight.BOLD,18)); Region sp=new Region(); HBox.setHgrow(sp,Priority.ALWAYS);
        Button r=new Button("↻ Refresh"); r.setStyle("-fx-background-color:"+GREEN+";-fx-text-fill:white;-fx-background-radius:6;"); r.setOnAction(e->refresh.run());
        bar.getChildren().addAll(menu,t,sp,r); return bar;
    }
    static void active(Button b){b.setStyle("-fx-background-color:"+GREEN+";-fx-text-fill:white;-fx-background-radius:0;");}
    static void inactive(Button b){b.setStyle("-fx-background-color:transparent;-fx-text-fill:#d7e4d9;-fx-background-radius:0;");}
    static void card(Region n){n.setStyle(CARD);}
    static void info(String title,String msg){Alert a=new Alert(Alert.AlertType.INFORMATION);a.setTitle(title);a.setHeaderText(null);a.setContentText(msg);a.showAndWait();}
    static void error(String title,String msg){Alert a=new Alert(Alert.AlertType.ERROR);a.setTitle(title);a.setHeaderText(null);a.setContentText(msg);a.showAndWait();}
    static boolean confirm(String title,String msg){Alert a=new Alert(Alert.AlertType.CONFIRMATION);a.setTitle(title);a.setHeaderText(null);a.setContentText(msg);return a.showAndWait().orElse(ButtonType.CANCEL)==ButtonType.OK;}
    static void collapse(BorderPane root){Node n=root.getLeft();if(n!=null){boolean v=n.isVisible();n.setVisible(!v);n.setManaged(!v);}}
    static String s(Object v){return v==null?"":String.valueOf(v);}
}
