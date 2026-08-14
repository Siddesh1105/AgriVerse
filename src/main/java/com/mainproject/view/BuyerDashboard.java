package com.mainproject.view;

import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BuyerDashboard {

    private Scene buyerDashboardScene;

    Scene getBuyerDashboardScene() {

        Text text = new Text("Buyer Dashboard");
        text.setStyle("-fx-font-size : 20px ; ");
        BorderPane root = new BorderPane(text);

        buyerDashboardScene = new Scene(root, 1400, 1000);
        buyerDashboardScene.setFill(Color.WHITE);
        return buyerDashboardScene;
    }

}