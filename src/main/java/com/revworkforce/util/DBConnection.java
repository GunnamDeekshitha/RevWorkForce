package com.revworkforce.util;

import com.revworkforce.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBConnection {

    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());

    private static final String URL      = "jdbc:mysql://localhost:3306/revworkforce";
    private static final String USER     = "root";
    private static final String PASSWORD = "Deekshu7494";

    private DBConnection() {

    }

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.fine("Database connected successfully");
            return con;
        } catch (SQLException e) {
            logger.severe("Database connection failed: " + e.getMessage());
            throw new DatabaseException("Unable to connect to the database. Please try again later.", e);
        }
    }
}
