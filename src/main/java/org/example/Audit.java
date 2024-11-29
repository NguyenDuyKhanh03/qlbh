package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Audit extends JFrame{
    private JComboBox cb1;
    private JButton btnCreateAudit;
    private JPanel panel1;
    private JButton btnDeleteAudit;
    private JComboBox comboBox2;
    private JTable table1;
    private JButton btnLoad;
    private JTable table2;
    private JButton button1;
    Connection con=DatabaseConnection.getConnection();

    public Audit(){
        setTitle("Audit");
        setContentPane(panel1);
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);



        setData("CLOTHES");

        cb1.addItem("SELECT");
        cb1.addItem("INSERT");
        cb1.addItem("UPDATE");
        cb1.addItem("DELETE");

        loadAudit();
        btnCreateAudit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String statementType= cb1.getSelectedItem().toString();
                String tableName= comboBox2.getSelectedItem().toString();
                if(statementType!=null){
                    String sql="AUDIT "+statementType+" ON CLOTHES."+tableName+" BY ACCESS";

                    try {
                        PreparedStatement cs = con.prepareCall(sql);
                        cs.execute();
                        JOptionPane.showMessageDialog(null, "Create audit successfully");
                        loadAudit();
                    } catch (Exception throwables) {
                        JOptionPane.showMessageDialog(null, throwables.getMessage());
                    }
                }
            }
        });
        btnDeleteAudit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String statementType= cb1.getSelectedItem().toString();
                String tableName= comboBox2.getSelectedItem().toString();
                if(statementType!=null){
                    String sql="NOAUDIT "+statementType+" ON CLOTHES."+tableName;

                    try {
                        PreparedStatement cs = con.prepareCall(sql);
                        cs.execute();
                        JOptionPane.showMessageDialog(null, "Delete audit successfully");
                        loadAudit();
                    } catch (Exception throwables) {
                        JOptionPane.showMessageDialog(null, throwables.getMessage());
                    }
                }
            }
        });
        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAudit1();
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame home = new HomePage();
                home.setVisible(true);
                dispose();
            }
        });
    }
    public void setData(String owner) {
        PreparedStatement stmt = null;
        try {
            String sql = "SELECT table_name FROM all_tables WHERE owner = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, owner);
            ResultSet rs = stmt.executeQuery();
            cb1.removeAllItems();
            // Đổ dữ liệu vào bảng từ ResultSet
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                comboBox2.addItem(tableName);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void loadAudit(){
        String sql = "SELECT object_name,object_type FROM DBA_OBJ_AUDIT_OPTS where owner = 'CLOTHES'";
        try {
            PreparedStatement cs = con.prepareCall(sql);
            ResultSet rs = cs.executeQuery();
            ResultSetMetaData mt=rs.getMetaData();
            String[] title=new String[mt.getColumnCount()];

            for (int i = 0; i < mt.getColumnCount(); i++) {
                title[i]=mt.getColumnName(i+1);
            }

            DefaultTableModel model = new DefaultTableModel(title, 0);
            while (rs.next()) {
                Object[] row = new Object[mt.getColumnCount()];
                for (int i = 0; i < mt.getColumnCount(); i++) {
                    row[i] = rs.getObject(i + 1);
                }
                model.addRow(row);
            }
            table1.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAudit1(){
        String sql = "SELECT USERNAME, TIMESTAMP ,OBJ_NAME,ACTION_NAME,SQL_TEXT FROM DBA_AUDIT_TRAIL";
        try {
            PreparedStatement cs = con.prepareCall(sql);
            ResultSet rs = cs.executeQuery();
            ResultSetMetaData mt=rs.getMetaData();
            String[] title=new String[mt.getColumnCount()];

            for (int i = 0; i < mt.getColumnCount(); i++) {
                title[i]=mt.getColumnName(i+1);
            }

            DefaultTableModel model = new DefaultTableModel(title, 0);
            while (rs.next()) {
                Object[] row = new Object[mt.getColumnCount()];
                for (int i = 0; i < mt.getColumnCount(); i++) {
                    row[i] = rs.getObject(i + 1);
                }
                model.addRow(row);
            }
            table2.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
