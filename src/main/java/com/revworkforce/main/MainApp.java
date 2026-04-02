package com.revworkforce.main;

import com.revworkforce.util.DBConnection;

public class MainApp {
    public static void main(String[] args) {
        DBConnection.getConnection();
    }
}