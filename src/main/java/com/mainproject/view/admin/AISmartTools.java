package com.mainproject.view.admin;

import com.mainproject.controller.AIChatController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AISmartTools {
    private final Stage stage; private final AdminDashboard dashboard; private final AIChatController ai=new AIChatController(); private BorderPane root; private TextArea chat; private TextField input;
    public AISmartTools(Stage stage,AdminDashboard dashboard){this.stage=stage;this.dashboard=dashboard;}
    public void show(){root=new BorderPane();root.setStyle("-fx-background-color:"+AdminCommon.BG+";");root.setLeft(AdminCommon.sidebar(stage,dashboard,"AI & Smart Tools"));root.setTop(AdminCommon.topBar("AI & Smart Tools",()->AdminCommon.collapse(root),()->{}));root.setCenter(content());stage.getScene().setRoot(root);}
    private VBox content(){VBox box=new VBox(12);box.setPadding(new Insets(20));Label h=new Label("AgriLink AI Assistant");h.setStyle("-fx-font-size:24;-fx-font-weight:bold;");chat=new TextArea();chat.setEditable(false);chat.setWrapText(true);chat.setPrefRowCount(20);input=new TextField();input.setPromptText("Ask about crops, farming, irrigation, soil, markets...");Button send=new Button("Ask AI");send.setStyle("-fx-background-color:"+AdminCommon.GREEN+";-fx-text-fill:white;");send.setOnAction(e->ask());input.setOnAction(e->ask());HBox row=new HBox(8,input,send);HBox.setHgrow(input,Priority.ALWAYS);VBox card=new VBox(10,h,chat,row);card.setPadding(new Insets(18));AdminCommon.card(card);box.getChildren().add(card);return box;}
    private void ask(){String q=input.getText().trim();if(q.isEmpty())return;chat.appendText("You: "+q+"\n");input.clear();new Thread(()->{String a=ai.getResponse(q);Platform.runLater(()->chat.appendText("AI: "+a+"\n\n"));}).start();}
}
