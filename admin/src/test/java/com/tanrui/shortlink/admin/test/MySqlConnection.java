package com.tanrui.shortlink.admin.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/shortlink?allowPublicKeyRetrieval=true&useSSL=false";
        String user = "root";
        String password = "niit1234";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
