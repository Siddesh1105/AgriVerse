package com.mainproject;

import com.mainproject.view.HomeScreen;
import com.mainproject.view.admin.AdminDashboard;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
       // Application.launch(HomeScreen.class, args);
       Application.launch(AdminDashboard.class, args);
        System.out.println("hiii");
    }
}
