package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class Login extends JDialog {
    private JPanel panel1;
    private JTextField edtUsername;
    private JTextField edtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    Connection con = DatabaseConnection.getConnection();
    public String username;
    public Login(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(panel1);
        setPreferredSize(new Dimension(500, 400));
        pack();
//        setMaximumSize(new Dimension(600, 600));
        setVisible(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);

        btnRegister.addActionListener(e -> {
            dispose();
            JFrame homeFrame = new Register();
            homeFrame.setSize(500, 500);
            homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
        btnLogin.addActionListener(e -> {
            String encryptedPassword="";
            try {
                CallableStatement stmt = null;
                String sql = "{? = call caesar_encrypt(?,?)}";
                stmt=con.prepareCall(sql);
                stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
                stmt.setString(2, edtPassword.getText());
                stmt.setInt(3, 3);
                stmt.execute();

                encryptedPassword = stmt.getString(1).toLowerCase();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            DatabaseConnection.USER = edtUsername.getText();
            DatabaseConnection.PASSWORD = encryptedPassword;
            Connection conn = DatabaseConnection.getConnection();

            if(conn != null) {


                username=edtUsername.getText();
                HomePage.user=edtUsername.getText().toUpperCase();

                JFrame homeFrame = new HomePage();

                homeFrame.setSize(600, 600);
                homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                homeFrame.setVisible(true);
                dispose();
            }
            else{
                JOptionPane.showMessageDialog(null, "Login failed");
                edtUsername.setText("");
                edtPassword.setText("");
            }


        });
    }

    public static void main(String[] args) {
        Login login = new Login(null);
    }
}
