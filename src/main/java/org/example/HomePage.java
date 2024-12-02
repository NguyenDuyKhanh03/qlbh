package org.example;

import oracle.jdbc.OracleTypes;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;

public class HomePage extends JFrame {
    private JPanel panel2;
    private JTable table1;
    private JComboBox comboBox1;
    private JButton loadDataButton;
    private JButton btn_Logout;
    private JButton btnSupplier;
    private JButton btnPro;
    private JButton btnSale;
    private JButton getKeyButton;
    private JButton giaiMaButton;
    private JButton btnCate;
    private JTextField edtKey;
    private JButton btnRole;
    private JButton btnLoadTable;
    private JButton policyButton;
    private JButton btnAudit;
    public static String user;
    Connection con = DatabaseConnection.getConnection();
    public static String key="";
    public static int selectRow;
    public static int count1 = 1;

    public HomePage() {
        setTitle("Home Page");
        setContentPane(panel2);
        // Set the content pane to the panel
        setContentPane(panel2);

        // Set the default size and close operation
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        setData("CLOTHES");
        System.out.println("Owner: " + user);
        loadDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox1.getSelectedItem()!=null){
                    String tableName = comboBox1.getSelectedItem().toString();
                    try {
                        String sql = "SELECT * FROM " + "CLOTHES."+tableName;
                        PreparedStatement stmt = con.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery();

                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        String[] columnNames = new String[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            columnNames[i - 1] = rsmd.getColumnName(i);
                        }
                        // Đổ dữ liệu vào bảng từ ResultSet
                        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
                        while (rs.next()) {
                            Object[] rowData = new Object[columnCount];
                            for (int i = 1; i <= columnCount; i++) {
                                rowData[i - 1] = rs.getObject(i);
                            }
                            model.addRow(rowData);
                        }
                        table1.setModel(model);

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        btn_Logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql="{CALL SYS.get_sid_serial(?, ?)}";
                CallableStatement stmt = null;
                try {
                    stmt= con.prepareCall(sql);
                    stmt.setString(1, user);
                    stmt.registerOutParameter(2, OracleTypes.CURSOR);
                    stmt.execute();
                    ResultSet rs = (ResultSet) stmt.getObject(2);
                    while(rs.next()){
                        System.out.println("SID: " + rs.getString("SID") + " SERIAL: " + rs.getString("SERIAL#"));
                        String sql2="{CALL logout(?, ?)}";

                        con=DatabaseManager.getConnection();

                        CallableStatement stmt2 = con.prepareCall(sql2);
                        stmt2.setString(1, rs.getString("SID"));
                        stmt2.setString(2, rs.getString("SERIAL#"));
                        stmt2.execute();
                        System.out.println(user);
                        DatabaseManager.isLogout(user.toLowerCase());
                        count1--;
                    }
                    rs.close();
                    con.close();
                    stmt.close();

                    DatabaseConnection.USER="sys as sysdba";
                    DatabaseConnection.PASSWORD="vbv";

                    dispose();
                    new Login(null);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        btnSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Supplier.user=user;
                JFrame supplierFrame = new Supplier();
                supplierFrame.setVisible(true);
                supplierFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                supplierFrame.setSize(600, 500);
            }
        });

        giaiMaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!key.equals("")){
                    if(selectRow!=-1) {
                        DefaultTableModel model = (DefaultTableModel) table1.getModel();
                        byte[] decodedKey = Base64.getDecoder().decode(key);
                        SecretKey originalKey = new SecretKeySpec(decodedKey, "AES");

                        String info= null;
                        try {
                            info = AES.decrypt("AES", (String) model.getValueAt(selectRow, 2), originalKey);
                            model.setValueAt(info, selectRow, 2);

                            String name= Multiplication.decryptWithMultiplication((String) model.getValueAt(selectRow, 1), Integer.parseInt(edtKey.getText()));
                            model.setValueAt(name, selectRow, 1);
                            System.out.println("Name: " + name);
                        } catch (NoSuchPaddingException ex) {
                            throw new RuntimeException(ex);
                        } catch (NoSuchAlgorithmException ex) {
                            throw new RuntimeException(ex);
                        } catch (InvalidKeyException ex) {
                            throw new RuntimeException(ex);
                        } catch (BadPaddingException ex) {
                            throw new RuntimeException(ex);
                        } catch (IllegalBlockSizeException ex) {
                            throw new RuntimeException(ex);
                        }
                        model.setValueAt(info, selectRow, 2);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Please load key first");
                }

            }
        });
        getKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectRow!=-1){
                    loadKey();

                }
                else{
                    JOptionPane.showMessageDialog(null, "Please select a row");
                }
            }
        });
        btnCate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame categoryFrame = new Category();
                dispose();
                categoryFrame.setVisible(true);
            }
        });
        btnPro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame productFrame = new Product();
                dispose();
                productFrame.setVisible(true);
            }
        });
        btnRole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame roleFrame = new Role();
                dispose();
                roleFrame.setVisible(true);
            }
        });
        btnLoadTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setData("CLOTHES");
            }
        });
        policyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame policyFrame = new Policy();
                dispose();
                policyFrame.setVisible(true);
            }
        });
        btnAudit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame auditFrame = new Audit();
                dispose();
                auditFrame.setVisible(true);
            }
        });
        btnSale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame saleFrame = new Sale();
                dispose();
                saleFrame.setVisible(true);
            }
        });
        Timer[] timer = new Timer[1];
        timer[0]=new Timer(7000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!DatabaseManager.isCheckLogin(user)){
                    timer[0].stop();

                    if(count1>0){
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                dispose();
                                new Login(null);
                                count1--;
                            }
                        });
                    }
                    System.out.println("Timer stopped.");
                }
            }
        });
        timer[0].start();
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public void loadKey() {
        selectRow= table1.getSelectedRow();
        if(selectRow!=-1) {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            int id = ((BigDecimal) model.getValueAt(selectRow, 0)).intValue();
            System.out.println("ID: " + id);
            try {
                String sqlKey=" SELECT name FROM CLOTHES.KHANHNE WHERE USERNAME=?";
                PreparedStatement preparedStatement=con.prepareStatement(sqlKey);
                preparedStatement.setInt(1, id);
                ResultSet rs=preparedStatement.executeQuery();
                if(rs.next()){
                    key=rs.getString("name");
                    System.out.println("Key: " + key);
                    JOptionPane.showMessageDialog(null, "Key loaded successfully");
                }
                else{
                    JOptionPane.showMessageDialog(null, "Key not found");
                }

            }catch (SQLException e){
                key="";
                JOptionPane.showMessageDialog(null, "Error");
//                e.printStackTrace();
            }
        }
    }
}
