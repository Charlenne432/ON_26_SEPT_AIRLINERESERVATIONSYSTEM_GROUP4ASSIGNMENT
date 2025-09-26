//NAMES OF GROUP 4
//1.MUHAYIMPUNDU CHARLENNE  RGNO.223003942
//2.NISHIMWE SEZERANO DELICE RGNO.223010486
//3.MUKAMA UYISENGA LEA RGNO.223018803

package com.panel;

import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.util.DB;

public class PaymentPanel  extends JPanel implements ActionListener {
	 private JTextField idTxt, refNoTxt, amountTxt, dateTxt, bookingIdTxt;
	    private JComboBox<String> methodCmb, statusCmb;
	    private JButton addBtn, updateBtn, deleteBtn, loadBtn;
	    private JTable table;
	    private DefaultTableModel model;


       public PaymentPanel(){
    	   setLayout(null);

           // Fields
           idTxt = new JTextField();
           refNoTxt = new JTextField();
           amountTxt = new JTextField();
           dateTxt = new JTextField();
           bookingIdTxt = new JTextField();

           // Dropdowns
           methodCmb = new JComboBox<>(new String[]{"Credit Card", "Mobile Money", "Cash", "Bank Transfer"});
           statusCmb = new JComboBox<>(new String[]{"Paid", "Pending", "Failed"});

           // Table
           String[] labels = {"PaymentID", "ReferenceNo", "Amount", "Date", "Method", "Status", "BookingID"};
           model = new DefaultTableModel(labels, 0);
           table = new JTable(model);
           JScrollPane sp = new JScrollPane(table);
           sp.setBounds(20, 280, 750, 200);
           add(sp);

           // Layout fields
           int y = 20;
           addField("PaymentID", idTxt, y); y += 30;
           addField("ReferenceNo", refNoTxt, y); y += 30;
           addField("Amount", amountTxt, y); y += 30;
           addField("Date (YYYY-MM-DD HH:MM:SS)", dateTxt, y); y += 30;
           addField("Method", methodCmb, y); y += 30;
           addField("Status", statusCmb, y); y += 30;
           addField("BookingID", bookingIdTxt, y); y += 30;

           // Buttons
           addButtons();
       }

       private void addField(String lbl, JComponent txt, int y) {
           JLabel l = new JLabel(lbl);
           l.setBounds(20, y, 160, 25);
           txt.setBounds(190, y, 150, 25);
           add(l);
           add(txt);
       }

       private void addButtons() {
           addBtn = new JButton("Add");
           updateBtn = new JButton("Update");
           deleteBtn = new JButton("Delete");
           loadBtn = new JButton("Load");

           addBtn.setBounds(380, 20, 100, 30);
           updateBtn.setBounds(380, 60, 100, 30);
           deleteBtn.setBounds(380, 100, 100, 30);
           loadBtn.setBounds(380, 140, 100, 30);

           add(addBtn);
           add(updateBtn);
           add(deleteBtn);
           add(loadBtn);

           addBtn.addActionListener(this);
           updateBtn.addActionListener(this);
           deleteBtn.addActionListener(this);
           loadBtn.addActionListener(this);

       }
	@Override
	public void actionPerformed(ActionEvent e) {
		try (Connection con = DB.getConnection()) {
            if (e.getSource() == addBtn) {
                String sql = "INSERT INTO payment(ReferenceNo, Amount, Date, Method, Status, BookingID) VALUES(?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, refNoTxt.getText());
                ps.setDouble(2, Double.parseDouble(amountTxt.getText()));
                ps.setString(3, dateTxt.getText());
                ps.setString(4, methodCmb.getSelectedItem().toString());
                ps.setString(5, statusCmb.getSelectedItem().toString());
                ps.setInt(6, Integer.parseInt(bookingIdTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Payment added successfully!");
            } else if (e.getSource() == updateBtn) {
                String sql = "UPDATE payment SET ReferenceNo=?, Amount=?, Date=?, Method=?, Status=?, BookingID=? WHERE PaymentID=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, refNoTxt.getText());
                ps.setDouble(2, Double.parseDouble(amountTxt.getText()));
                ps.setString(3, dateTxt.getText());
                ps.setString(4, methodCmb.getSelectedItem().toString());
                ps.setString(5, statusCmb.getSelectedItem().toString());
                ps.setInt(6, Integer.parseInt(bookingIdTxt.getText()));
                ps.setInt(7, Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Payment updated successfully!");
            } else if (e.getSource() == deleteBtn) {
                String sql = "DELETE FROM payment WHERE PaymentID=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Payment deleted successfully!");
            } else if (e.getSource() == loadBtn) {
                model.setRowCount(0);
                String sql = "SELECT * FROM payment";
                ResultSet rs = con.createStatement().executeQuery(sql);
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("PaymentID"),
                            rs.getString("ReferenceNo"),
                            rs.getDouble("Amount"),
                            rs.getString("Date"),
                            rs.getString("Method"),
                            rs.getString("Status"),
                            rs.getInt("BookingID"),
                    });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Payment Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(820, 600);
        frame.add(new PaymentPanel());
        frame.setVisible(true);
    
	
	}



	}


