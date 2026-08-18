package com.mainproject;
import com.mainproject.view.AdminDashboard;
import com.mainproject.view.LoginScreen;
import javafx.application.Application;
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Application.launch(LoginScreen.class, args);
        //Application.launch(AdminDashboard.class, args);

    }
}
