package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    public static String USER = "sys as sysdba";
    public static String PASSWORD = "sys";

    public static Connection getConnection() {
        try {
            // Tải driver JDBC
            Class.forName("oracle.jdbc.driver.OracleDriver");
            if(USER.toLowerCase().equals("sys"))
                USER+=" as sysdba";
            // Kết nối đến cơ sở dữ liệu
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
    }



}
