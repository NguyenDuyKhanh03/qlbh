package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Register extends JFrame {
    private JTextField edtUsername;
    private JTextField edtPass;
    private JButton btnRegister;
    private JPanel panel1;

    public Register() {
        setTitle("Đăng kí");
        setContentPane(panel1);
        setPreferredSize(new Dimension(450, 500));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!edtUsername.getText().isEmpty() && !edtPass.getText().isEmpty()) {
                    Connection conn = DatabaseConnection.getConnection();
                    CallableStatement stmt = null;
                    if(conn != null) {
                        String sql = "{CALL CreateUser(?, ?, ?)}";
                        try {
                            stmt=conn.prepareCall(sql);
                            stmt.setString(1, edtUsername.getText());
                            stmt.setString(2, edtPass.getText());
                            stmt.setInt(3, 3);

                            stmt.execute();
                            System.out.println("User " + edtUsername.getText() + " created successfully.");

                            Register.this.dispose();
                            new Login(null);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            }
        });
    }
}
