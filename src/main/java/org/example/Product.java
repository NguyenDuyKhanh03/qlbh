package org.example;

import oracle.jdbc.OracleTypes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Product extends JFrame {
    private JPanel panel1;
    private JTextField edtId;
    private JTextField edtName;
    private JButton insertButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton button1;
    private JComboBox cbCate;
    private JTextField edtDes;
    private JTextField edtPrice;
    private JTextField edtQuantity;
    Connection con=DatabaseConnection.getConnection();
    private Map<String, Integer> categoryMap = new HashMap<>();

    public Product() {
        setTitle("Home Page");
        setContentPane(panel1);
        // Set the default size and close operation
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        loadCate();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame home = new HomePage();
                home.setVisible(true);
                dispose();
            }
        });
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cbCate.getSelectedItem()!=null){
                    String sql = "CALL CLOTHES.P_insert_pro(?,?,?,?,?,?)";
                    try {
                        CallableStatement cs = con.prepareCall(sql);
                        cs.setInt(1, Integer.parseInt(edtId.getText()));
                        cs.setString(2, edtName.getText());
                        cs.setString(3, edtDes.getText());
                        cs.setInt(4, Integer.parseInt(edtPrice.getText()));
                        cs.setInt(5, Integer.parseInt(edtQuantity.getText()));

                        String cate=cbCate.getSelectedItem().toString();
                        cs.setInt(6, categoryMap.get(cate));

                        cs.execute();
                        JOptionPane.showMessageDialog(null, "Insert success");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Insert failed");
                    }
                }
            }
        });
    }

    private void loadCate(){
        String sql = "CALL CLOTHES.P_select_cate(?)";
        try {
            CallableStatement cs = con.prepareCall(sql);
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            cs.execute();
            ResultSet rs = (ResultSet) cs.getObject(1);
            while (rs.next()){
                cbCate.addItem(rs.getString(2));
                categoryMap.put(rs.getString(2), rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
