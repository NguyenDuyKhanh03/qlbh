package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;

public class Policy extends JFrame{
    private JPanel panel1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton btnCreatePolicy;
    private JButton btnDeletePolicy;
    private JButton loadButton;
    private JTable table1;
    private JTable table2;
    private JButton btnLoadAuditTrain;
    private JButton button1;
    Connection con=DatabaseConnection.getConnection();

    public Policy() {
        setTitle("Policy");
        setContentPane(panel1);
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        setData("CLOTHES");

        comboBox2.addItem("SELECT");
        comboBox2.addItem("INSERT");
        comboBox2.addItem("UPDATE");
        comboBox2.addItem("DELETE");

        btnCreatePolicy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = comboBox1.getSelectedItem().toString();
                if(tableName!=null){
                    String sql="{call CLOTHES.p_create_policy(?,?)}";

                    try {
                        PreparedStatement cs = con.prepareCall(sql);
                        cs.setString(1, tableName);
                        cs.setString(2, Objects.requireNonNull(comboBox2.getSelectedItem()).toString().toUpperCase());
                        cs.execute();
                        JOptionPane.showMessageDialog(null, "Create policy successfully");
                        loadPolicy();
                    } catch (SQLException throwables) {
                        JOptionPane.showMessageDialog(null, throwables.getMessage());
                    }
                }
            }
        });
        btnDeletePolicy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = comboBox1.getSelectedItem().toString();
                if(tableName!=null){
                    String sql="{call CLOTHES.p_delete_policy(?,?)}";

                    try {
                        PreparedStatement cs = con.prepareCall(sql);
                        cs.setString(1, tableName);
                        cs.setString(2, Objects.requireNonNull(comboBox2.getSelectedItem()).toString().toUpperCase());
                        cs.execute();
                        JOptionPane.showMessageDialog(null, "Delete policy successfully");
                        loadPolicy();
                    } catch (SQLException throwables) {
                        JOptionPane.showMessageDialog(null, throwables.getMessage());
                    }
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPolicy();
            }
        });
        btnLoadAuditTrain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAuditTRain();
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

        Timer timer=new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
            comboBox1.removeAllItems();
            // Đổ dữ liệu vào bảng từ ResultSet
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                comboBox1.addItem(tableName);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void loadPolicy(){
        String sql = "select * from dba_audit_policies ";
        try {
            PreparedStatement cs = con.prepareCall(sql);
            ResultSet rs = cs.executeQuery();
            ResultSetMetaData mt=rs.getMetaData();
            String[] title=new String[3];

            for(int i=0;i<3;i++){
                title[i]=mt.getColumnName(i+1);
            }
            DefaultTableModel model=new DefaultTableModel(title,0);
            while (rs.next()){
                Object[] row=new Object[mt.getColumnCount()];
                String objectSchema=rs.getString("object_schema");
                String objectName=rs.getString("object_name");
                String policyName=rs.getString("policy_name");
                row[0]=objectSchema;
                row[1]=objectName;
                row[2]=policyName;
                model.addRow(row);
            }
            table1.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAuditTRain(){
        String sql="select SESSION_ID , timestamp, object_name, sql_text,extended_timestamp from dba_fga_audit_trail";
        try {
            PreparedStatement cs = con.prepareCall(sql);
            ResultSet rs = cs.executeQuery();
            ResultSetMetaData mt=rs.getMetaData();
            String[] title=new String[5];

            for(int i=0;i<5;i++){
                title[i]=mt.getColumnName(i+1);
            }
            DefaultTableModel model=new DefaultTableModel(title,0);
            while (rs.next()){
                Object[] row=new Object[mt.getColumnCount()];
                String sessionID=rs.getString("SESSION_ID");
                String timestamp=rs.getString("timestamp");
                String objectName=rs.getString("object_name");
                String sqlText=rs.getString("sql_text");
                String extendedTimestamp=rs.getString("extended_timestamp");
                row[0]=sessionID;
                row[1]=timestamp;
                row[2]=objectName;
                row[3]=sqlText;
                row[4]=extendedTimestamp;
                model.addRow(row);
            }
            table2.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
