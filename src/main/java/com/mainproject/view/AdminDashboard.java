package com.mainproject.view;

import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;

import javafx.scene.paint.Color;

import javafx.scene.text.Text;

//durgesh code
public class AdminDashboard {

    private Scene adminDashboardScene;

    Scene getAdminDashboardScene() {

        Text text = new Text("Admin Dashboard");
        text.setStyle("-fx-font-size : 20px ");

        BorderPane root = new BorderPane(text);

        adminDashboardScene = new Scene(root, 1400, 1000);
        adminDashboardScene.setFill(Color.WHITE);
        return adminDashboardScene;
    }

}