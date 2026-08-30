package com.mainproject;
import com.mainproject.view.HomeScreen;
import javafx.application.Application;
import com.mainproject.view.admin.AdminDashboard;
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        //Application.launch(HomeScreen.class, args);
        Application.launch(AdminDashboard.class, args);

    }
}
