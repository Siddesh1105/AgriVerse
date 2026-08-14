package com.mainproject.view;

import com.mainproject.model.User;

import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BuyerDashboard {

    private Scene buyerDashboardScene;

    public BuyerDashboard(User user) {
        //TODO Auto-generated constructor stub
    }

    Scene getBuyerDashboardScene() {

        Text text = new Text("Buyer Dashboard");
        text.setStyle("-fx-font-size : 20px ; ");
        BorderPane root = new BorderPane(text);

        buyerDashboardScene = new Scene(root, 1400, 1000);
        buyerDashboardScene.setFill(Color.WHITE);
        return buyerDashboardScene;
    }

    public Scene getScene() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getScene'");
    }

}