package com.passwordmanager.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectorFactory {
    // This is the database URL we will connect to
    public static String url = "jdbc:oracle:thin:@localhost:1521:XE"; 
    // Database username
    public static String user = "revanth";
    // Database password
    public static String password = "temp001";

    // This method creates and returns a database connection
    public static Connection getConnection() {
        try {
            // Load the Oracle JDBC driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Connect to the database using the URL, username, and password
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            // If connection fails, stop the program and show the error
            throw new RuntimeException("Unable to get Oracle Connection", e);
        }
    }
}
