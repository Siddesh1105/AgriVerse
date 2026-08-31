package com.mainproject;

import com.mainproject.view.HomeScreen;
import com.mainproject.view.admin.AdminLoginScreen;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
      //  Application.launch(HomeScreen.class,args);
        Application.launch(AdminLoginScreen.class, args);
    }
}
