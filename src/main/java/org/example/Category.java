package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.sql.CallableStatement;
import java.sql.Connection;

public class Category extends JFrame{
    private JTextField edtId;
    private JTextField edtName;
    private JButton insertButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton button4;
    private JButton button1;
    private JPanel panel1;

    public Category() {
        setTitle("Home Page");
        setContentPane(panel1);
        // Set the content pane to the panel
        setContentPane(panel1);
        Connection con=DatabaseConnection.getConnection();
        // Set the default size and close operation
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        button1.addComponentListener(new ComponentAdapter() {
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new HomePage();
                dispose();
                frame.setVisible(true);
            }
        });
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(edtId.getText().equals("") || edtName.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Please fill all fields");
                    return;
                }
                try {
                    String sql = "CALL clothes.P_insert_cate(?, ?)";
                    CallableStatement statement=con.prepareCall(sql);
                    statement.setInt(1,Integer.parseInt(edtId.getText()));
                    statement.setString(2,edtName.getText());
                    statement.execute();
                    JOptionPane.showMessageDialog(null, "Insert successfully");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Insert failed");
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(edtId.getText().equals("") || edtName.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Please fill all fields");
                    return;
                }
                try {
                    String sql = "CALL clothes.P_update_cate(?, ?)";
                    CallableStatement statement=con.prepareCall(sql);
                    statement.setInt(1,Integer.parseInt(edtId.getText()));
                    statement.setString(2,edtName.getText());
                    statement.execute();
                    JOptionPane.showMessageDialog(null, "Update successfully");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Update failed");
                }
            }
        });
    }

}
