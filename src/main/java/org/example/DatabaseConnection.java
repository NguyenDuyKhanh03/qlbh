package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    public static String USER = "sys as sysdba";
    public static String PASSWORD = "vbv";

    public static Connection getConnection() {
        try {
            // Tải driver JDBC
            Class.forName("oracle.jdbc.driver.OracleDriver");
            if (USER.toLowerCase().equals("sys"))
                USER += " as sysdba";
            // Kết nối đến cơ sở dữ liệu
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            if(28000==e.getErrorCode()){
                JOptionPane.showMessageDialog(null, "Tài khoản của bạn đã bị khóa. Vui lòng thử lại sau.");
            }
            else if(1017==e.getErrorCode()){
                JOptionPane.showMessageDialog(null, "Sai tên đăng nhập hoặc mật khẩu. Vui lòng thử lại.");
            }
            else{
                JOptionPane.showMessageDialog(null, "Vui lòng đăng nhập lại.");
            }
            return null;
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }



}
