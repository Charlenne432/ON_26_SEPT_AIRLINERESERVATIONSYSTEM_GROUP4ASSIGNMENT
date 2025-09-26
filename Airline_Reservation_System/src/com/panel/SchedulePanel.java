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

public class SchedulePanel  extends JPanel implements ActionListener{
    private JTextField idTxt, nameTxt, typeTxt, startDateTxt, endDateTxt, statusTxt, flightIdTxt;
    private JButton addBtn, updateBtn, deleteBtn, loadBtn;
    private JTable table;
    private DefaultTableModel model;

 public SchedulePanel(){
	   setLayout(null);

       // Fields
       idTxt = new JTextField();
       nameTxt = new JTextField();
       typeTxt = new JTextField();
       startDateTxt = new JTextField();
       endDateTxt = new JTextField();
       statusTxt = new JTextField();
       flightIdTxt = new JTextField();

       // Table
       String[] labels = {"ScheduleID", "Name", "Type", "StartDate", "EndDate", "Status", "FlightID"};
       model = new DefaultTableModel(labels, 0);
       table = new JTable(model);
       JScrollPane sp = new JScrollPane(table);
       sp.setBounds(20, 280, 750, 200);
       add(sp);

       // Layout fields
       int y = 20;
       addField("ScheduleID", idTxt, y); y += 30;
       addField("Name", nameTxt, y); y += 30;
       addField("Type", typeTxt, y); y += 30;
       addField("StartDate (YYYY-MM-DD)", startDateTxt, y); y += 30;
       addField("EndDate (YYYY-MM-DD)", endDateTxt, y); y += 30;
       addField("Status", statusTxt, y); y += 30;
       addField("FlightID", flightIdTxt, y); y += 30;

       // Buttons
       addButtons();
   }

   private void addField(String lbl, JComponent txt, int y) {
       JLabel l = new JLabel(lbl);
       l.setBounds(20, y, 150, 25);
       txt.setBounds(180, y, 150, 25);
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
            String sql = "INSERT INTO schedule(Name, Type, StartDate, EndDate, Status, FlightID) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nameTxt.getText());
            ps.setString(2, typeTxt.getText());
            ps.setString(3, startDateTxt.getText());
            ps.setString(4, endDateTxt.getText());
            ps.setString(5, statusTxt.getText());
            ps.setInt(6, Integer.parseInt(flightIdTxt.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Schedule added successfully!");
        } else if (e.getSource() == updateBtn) {
            String sql = "UPDATE schedule SET Name=?, Type=?, StartDate=?, EndDate=?, Status=?, FlightID=? WHERE ScheduleID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nameTxt.getText());
            ps.setString(2, typeTxt.getText());
            ps.setString(3, startDateTxt.getText());
            ps.setString(4, endDateTxt.getText());
            ps.setString(5, statusTxt.getText());
            ps.setInt(6, Integer.parseInt(flightIdTxt.getText()));
            ps.setInt(7, Integer.parseInt(idTxt.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Schedule updated successfully!");
        } else if (e.getSource() == deleteBtn) {
            String sql = "DELETE FROM schedule WHERE ScheduleID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(idTxt.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Schedule deleted successfully!");
        } else if (e.getSource() == loadBtn) {
            model.setRowCount(0);
            String sql = "SELECT * FROM schedule";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ScheduleID"),
                        rs.getString("Name"),
                        rs.getString("Type"),
                        rs.getString("StartDate"),
                        rs.getString("EndDate"),
                        rs.getString("Status"),
                        rs.getInt("FlightID")
                });
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}

public static void main(String[] args) {
    JFrame frame = new JFrame("Schedule Panel");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(820, 600);
    frame.add(new SchedulePanel());
    frame.setVisible(true);
}

	
}
