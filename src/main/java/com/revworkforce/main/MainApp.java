package com.revworkforce.main;

import com.revworkforce.model.Employee;
import com.revworkforce.service.AuthService;
import com.revworkforce.ui.MainMenu;

public class MainApp {
    public static void main(String[] args) {
        System.setErr(System.out);
        MainMenu.start();
    }
}