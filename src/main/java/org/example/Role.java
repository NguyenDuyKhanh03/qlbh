package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Role extends JFrame{
    private JComboBox comboBox1;
    private JButton btnGrant;
    private JButton btnRevoke;
    private JPanel panel1;
    private JComboBox comboBox2;
    private JButton btnGrantGroupRole;
    private JButton btnRevokeGroupRole;
    private JButton btnGrantPro;
    private JButton btnRevokePro;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JComboBox comboBox6;


    private JTable table1;
    private JComboBox comboBox7;
    private JButton btnHome;
    Connection con = DatabaseConnection.getConnection();

    public Role() {
        setTitle("Home Page");
        setContentPane(panel1);
        // Set the content pane to the panel
        setContentPane(panel1);

        // Set the default size and close operation
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setData("CLOTHES");
        setData1();
        setData3();
        setData4();
        comboBox4.addItem("SELECT");
        comboBox4.addItem("INSERT");
        comboBox4.addItem("UPDATE");
        comboBox4.addItem("DELETE");


        comboBox3.addItem("admin");
        comboBox3.addItem("nv");

        setData5();

        setData6();
        btnGrant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox1.getSelectedItem()==null || comboBox4.getSelectedItem()==null || comboBox5.getSelectedItem()==null){
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ thông tin cần grant role");
                    return;
                }
                else {
                    String table=comboBox1.getSelectedItem().toString();
                    String role=comboBox4.getSelectedItem().toString();
                    String user=comboBox5.getSelectedItem().toString();

                    try {
                        CallableStatement stmt = con.prepareCall("{call CLOTHES.pro_grant_role(?,?,?)}");
                        stmt.setString(1, table);
                        stmt.setString(2, user);
                        stmt.setString(3, role);
                        stmt.execute();
                        JOptionPane.showMessageDialog(null, "Grant successfully");
                        setData5();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Grant failed");
                    }
                }
            }
        });
        btnRevoke.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox1.getSelectedItem()==null || comboBox4.getSelectedItem()==null || comboBox5.getSelectedItem()==null){
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ thông tin cần revoke role");
                    return;
                }
                else {
                    String table=comboBox1.getSelectedItem().toString();
                    String role=comboBox4.getSelectedItem().toString();
                    String user=comboBox5.getSelectedItem().toString();

                    try {
                        CallableStatement stmt = con.prepareCall("{call CLOTHES.pro_revoke_role(?,?,?)}");
                        stmt.setString(1, table);
                        stmt.setString(2, user);
                        stmt.setString(3, role);
                        stmt.execute();
                        JOptionPane.showMessageDialog(null, "Revoke successfully");
                        setData5();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Revoke failed");
                    }
                }
            }
        });

        btnGrantGroupRole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox2.getSelectedItem()==null || comboBox6.getSelectedItem()==null){
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ thông tin cần revoke role");
                    return;
                }
                else{
                    String user=comboBox6.getSelectedItem().toString();
                    String pro=comboBox2.getSelectedItem().toString();

                    try {
                        CallableStatement stmt = con.prepareCall("{call CLOTHES.pro_grant_exec_pro(?,?)}");
                        stmt.setString(1, pro);
                        stmt.setString(2, user);
                        stmt.execute();
                        JOptionPane.showMessageDialog(null, "Grant successfully");
                        setData5();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Grant failed");
                    }
                }
            }
        });
        btnRevokeGroupRole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox2.getSelectedItem()==null || comboBox6.getSelectedItem()==null){
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ thông tin cần revoke role");
                    return;
                }
                else{
                    String user=comboBox6.getSelectedItem().toString();
                    String pro=comboBox2.getSelectedItem().toString();

                    try {
                        CallableStatement stmt = con.prepareCall("{call CLOTHES.pro_revoke_exec_pro(?,?)}");
                        stmt.setString(1, pro);
                        stmt.setString(2, user);
                        stmt.execute();
                        JOptionPane.showMessageDialog(null, "Revoke successfully");
                        setData5();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Revoke failed");
                    }
                }
            }

        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int row=table1.rowAtPoint(e.getPoint());
                if(row>=0){
                    table1.setRowSelectionInterval(row, row);

                    String user = table1.getModel().getValueAt(row, 0).toString();
                    String privilege = table1.getModel().getValueAt(row, 1).toString();
                    String table = table1.getModel().getValueAt(row, 3).toString();

                    int choice=JOptionPane.showConfirmDialog(null,
                            "Bạn có muốn xóa quyền "+privilege+" của user "+user+" không?",
                            "Vui lòng chọn!",
                            JOptionPane.YES_NO_OPTION);

                    if(choice==JOptionPane.YES_OPTION){
                        try {
                            if(privilege!="SELECT" || privilege!="INSERT" || privilege!="UPDATE" || privilege!="DELETE"){
                                CallableStatement stmt = con.prepareCall("{call CLOTHES.pro_revoke_role(?,?,?)}");
                                stmt.setString(1, table);
                                stmt.setString(2, user);
                                stmt.setString(3, privilege);
                                stmt.execute();
                                JOptionPane.showMessageDialog(null, "Revoke successfully");
                                setData5();
                            }
                            else{
                                CallableStatement stmt = con.prepareCall("{call CLOTHES.pro_revoke_exec_pro(?,?)}");
                                stmt.setString(1, table);
                                stmt.setString(2, user);
                                stmt.execute();
                                JOptionPane.showMessageDialog(null, "Revoke successfully");
                                setData5();
                            }

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Revoke failed");
                        }
                    }
                }
            }


        });
        btnGrantPro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox3.getSelectedItem()==null){
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ thông tin cần grant role");
                    return;
                }
                else{

                    String sql="GRANT "+comboBox3.getSelectedItem()+ " TO "+comboBox7.getSelectedItem();
                    PreparedStatement stmt = null;
                    try {
                        stmt = con.prepareStatement(sql);
                        stmt.execute();
                        JOptionPane.showMessageDialog(null, "Grant successfully");
                        setData5();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Grant failed");
                    }
                }

            }
        });
        btnHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new HomePage();
                dispose();
                frame.setVisible(true);
            }
        });
        btnRevokePro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox3.getSelectedItem()==null){
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ thông tin cần grant role");
                    return;
                }
                else{

                    String sql="REVOKE "+comboBox3.getSelectedItem()+ " FROM "+comboBox7.getSelectedItem();
                    PreparedStatement stmt = null;
                    try {
                        stmt = con.prepareStatement(sql);
                        stmt.execute();
                        JOptionPane.showMessageDialog(null, "Grant successfully");
                        setData5();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Grant failed");
                    }
                }
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
            e.printStackTrace();
        }
    }
    public void setData1() {
        PreparedStatement stmt = null;
        try {
            String sql = "SELECT OBJECT_NAME FROM USER_PROCEDURES";
            stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            comboBox3.removeAllItems();
            // Đổ dữ liệu vào bảng từ ResultSet
            while (rs.next()) {
                String proName = rs.getString("object_name");
                comboBox2.addItem(proName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setData3() {
        PreparedStatement stmt = null;
        try {
            String sql = "select username from dba_users";
            stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            comboBox5.removeAllItems();
            // Đổ dữ liệu vào bảng từ ResultSet
            while (rs.next()) {
                String proName = rs.getString("username");
                comboBox5.addItem(proName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void setData4() {
        PreparedStatement stmt = null;
        try {
            String sql = "select username from dba_users";
            stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            comboBox6.removeAllItems();

            while (rs.next()) {
                String proName = rs.getString("username");
                comboBox6.addItem(proName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setData5() {
        PreparedStatement stmt = null;
        try {
            String sql = "SELECT grantee, privilege, owner, table_name FROM dba_tab_privs WHERE owner = 'CLOTHES'";
            stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();

            int countColumn=rsmd.getColumnCount();
            String[] columnNames = new String[countColumn];
            for (int i=0;i<countColumn;i++){
                columnNames[i]=rsmd.getColumnName(i+1);
            }
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            // Đổ dữ liệu vào bảng từ ResultSet
            while (rs.next()) {
                Object[] rowData = new Object[countColumn];
                for (int i=0;i<countColumn;i++){
                    rowData[i]=rs.getObject(i+1);
                }
                model.addRow(rowData);
            }

            table1.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setData6() {
        PreparedStatement stmt = null;
        try {
            String sql = "select username from dba_users";
            stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            comboBox7.removeAllItems();
            // Đổ dữ liệu vào bảng từ ResultSet
            while (rs.next()) {
                String proName = rs.getString("username");
                comboBox7.addItem(proName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
