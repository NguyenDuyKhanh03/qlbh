package org.example;

import java.sql.*;

public class DatabaseManager {
    public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    public static String USER = "sys as sysdba";
    public static String PASSWORD = "vbv";

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

    public static boolean isCheckLogin(String username){
        Connection conn = DatabaseManager.getConnection();
        String sql = "select * from user_status where username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username.toLowerCase());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                if(rs.getString("status").equals("ACTIVE")){
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void isLogin(String username) {
        Connection conn = DatabaseManager.getConnection();
        String sql = "update user_status set status = 'ACTIVE' where username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void isLogout(String username) {
        Connection conn = DatabaseManager.getConnection();
        String sql = "update user_status set status = 'ISACTIVE' where username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
