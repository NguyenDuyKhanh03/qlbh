package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Sale extends JFrame{
    private JTextField edtId;
    private JTextField edtQuantity;
    private JComboBox comboBox1;
    private JPanel panel1;
    private JButton createButton;
    private Map<String,Integer> products=new HashMap<>();

    public Sale(){
        setTitle("Sale");
        setContentPane(panel1);
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        loadProduct();
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql="insert into SALES (SALE_ID,PRODUCT_ID,QUANTITY_SOLD, SALE_DATE) values (?,?,?,?)";
                try{
                    PreparedStatement ps=DatabaseConnection.getConnection().prepareStatement(sql);
                    ps.setInt(1, Integer.parseInt(edtId.getText()));
                    ps.setInt(2, products.get(comboBox1.getSelectedItem().toString()));
                    ps.setInt(3, Integer.parseInt(edtQuantity.getText()));



                    LocalDate date=LocalDate.now();
                    ps.setDate(4, java.sql.Date.valueOf(date));
                    ps.execute();
                    JOptionPane.showMessageDialog(null, "Sale successfully");
                }catch (Exception throwables){
                    JOptionPane.showMessageDialog(null, throwables.getMessage());
                    throwables.printStackTrace();
                }
            }
        });
    }

    public void loadProduct(){
        String sql="select PRODUCT_ID,PRODUCT_NAME from PRODUCTS";
        try {
            PreparedStatement ps=DatabaseConnection.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                products.put(rs.getString("PRODUCT_NAME"),rs.getInt("PRODUCT_ID"));
                comboBox1.addItem(rs.getString("PRODUCT_NAME"));
            }
        } catch (Exception throwables) {
            JOptionPane.showMessageDialog(null, throwables.getMessage());
        }
    }
}
