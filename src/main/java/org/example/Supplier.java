package org.example;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;

public class Supplier extends JFrame {
    private JPanel panel1;
    private JTextField edtId;
    private JTextField edtName;
    private JTextField edtInfo;
    private JButton insertButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton button1;
    private JTable table1;
    private JTextField edtKey;
    public static String user;
    private boolean isUpdatingOrDeleting = false;

    Connection con = DatabaseConnection.getConnection();
    public Supplier() {
        setTitle("Vận chuyển");
        setContentPane(panel1);
        setPreferredSize(new Dimension(600, 500));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        loadData();

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = "INSERT INTO SUPPLIERS VALUES(?, ?, ?)";
                String sql1="INSERT INTO KHANHNE VALUES(?,?)";
                SecretKey key = null;
                try {
                    key = AES.generateKey();
                    String encrypted = AES.encrypt("AES", edtInfo.getText(), key);

                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setInt(1, Integer.parseInt(edtId.getText()));
                    String encryptedName=Multiplication.encryptWithMultiplication(edtName.getText(), Integer.parseInt(edtKey.getText()));
                    stmt.setString(2, encryptedName);
                    stmt.setString(3, encrypted);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Insert successfully");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (NoSuchPaddingException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalBlockSizeException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                } catch (BadPaddingException ex) {
                    throw new RuntimeException(ex);
                } catch (InvalidKeyException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    String encrypted = Base64.getEncoder().encodeToString(key.getEncoded());
                    PreparedStatement stmt1 = con.prepareStatement(sql1);
                    stmt1.setInt(1, Integer.parseInt(edtId.getText()));
                    stmt1.setString(2, encrypted);
                    stmt1.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Insert successfully");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JFrame homeFrame = new HomePage();
                homeFrame.setSize(600, 600);
                homeFrame.setVisible(true);
            }
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row=table1.getSelectedRow();
                if(row!=-1){
                    edtId.setText(table1.getValueAt(row, 0).toString());
                    edtName.setText(table1.getValueAt(row, 1).toString());
                    edtInfo.setText(table1.getValueAt(row, 2).toString());
                    edtId.setEnabled(false);
                }
            }
        });
        table1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(isUpdatingOrDeleting) {
                    edtId.setText("");
                    edtName.setText("");
                    edtInfo.setText("");
                }
            }

        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isUpdatingOrDeleting = true;
                String sql = "UPDATE SUPPLIERS SET SUPPLIER_NAME=?, CONTACT_INFO=? WHERE SUPPLIER_ID=?";
                try{
                    String key=loadKey();
                    SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
                    String encrypted = AES.encrypt("AES", edtInfo.getText(), secretKey);
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setString(1, edtName.getText());
                    stmt.setString(2, encrypted);
                    stmt.setInt(3, Integer.parseInt(edtId.getText()));
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Update successfully");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                loadData();
                edtId.setEnabled(true);
                isUpdatingOrDeleting = false;
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isUpdatingOrDeleting = true;
                String sql = "DELETE FROM SUPPLIERS WHERE SUPPLIER_ID=?";
                try {
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setInt(1, Integer.parseInt(edtId.getText()));
                    stmt.executeUpdate();

                    String sqlKey = "DELETE FROM KHANHNE WHERE USERNAME=?";
                    PreparedStatement preparedStatement = con.prepareStatement(sqlKey);
                    preparedStatement.setInt(1, Integer.parseInt(edtId.getText()));
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Delete successfully");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                loadData();
                edtId.setEnabled(true);
                isUpdatingOrDeleting = false;

            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private void loadData() {
        String sql="SELECT * FROM SUPPLIERS";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int col = rsmd.getColumnCount();
            String[] colName = new String[col];

            for (int i = 0; i < col; i++) {
                colName[i] = rsmd.getColumnName(i + 1);
            }
            DefaultTableModel model = new DefaultTableModel(colName, 0);

            while (rs.next()) {
                Object[] row = new Object[col];
                for (int i = 0; i < col; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                model.addRow(row);
            }
            table1.setModel(model);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadKey() {
        int selectRow = table1.getSelectedRow();
        String key = "";
        if (selectRow != -1) {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            int id = ((BigDecimal) model.getValueAt(selectRow, 0)).intValue();
            System.out.println("ID: " + id);
            try {
                String sqlKey = "SELECT name FROM KHANHNE WHERE USERNAME=?";
                PreparedStatement preparedStatement = con.prepareStatement(sqlKey);
                preparedStatement.setInt(1, id);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    key = rs.getString("name");
                    System.out.println("Key: " + key);
                    JOptionPane.showMessageDialog(null, "Key loaded successfully");
                } else {
                    JOptionPane.showMessageDialog(null, "Key not found");
                }
                return key;

            } catch (SQLException e) {

                JOptionPane.showMessageDialog(null, "Error");

            }
        }
        return key;
    }


}
