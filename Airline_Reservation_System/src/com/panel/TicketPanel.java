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

public class TicketPanel extends JPanel implements ActionListener{
	private JTextField idTxt, categoryTxt, detailTxt, ownerTxt, locationTxt, createdAtTxt, scheduleIdTxt;
	private JButton addBtn, updateBtn, deleteBtn, loadBtn;
	private JTable table;
	private DefaultTableModel model;
	
	public TicketPanel(){
        setLayout(null);

        // Fields
        idTxt = new JTextField();
        categoryTxt = new JTextField();
        detailTxt = new JTextField();
        ownerTxt = new JTextField();
        locationTxt = new JTextField();
        createdAtTxt = new JTextField();
        scheduleIdTxt = new JTextField();

        // Buttons
        addBtn = new JButton("Add");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        loadBtn = new JButton("Load");

        // Table
        String[] labels = {"TicketID", "Category", "Detail", "Owner", "Location", "CreatedAt", "ScheduleID"};
        model = new DefaultTableModel(labels, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 300, 750, 200);
        add(sp);

        // Layout fields
        int y = 20;
        addField("TicketID", idTxt, y); y += 30;
        addField("Category", categoryTxt, y); y += 30;
        addField("Detail", detailTxt, y); y += 30;
        addField("Owner", ownerTxt, y); y += 30;
        addField("Location", locationTxt, y); y += 30;
        addField("CreatedAt", createdAtTxt, y); y += 30;
        addField("ScheduleID", scheduleIdTxt, y); y += 30;

        // Buttons
        addButtons();
    }

    private void addField(String lbl, JComponent txt, int y) {
        JLabel l = new JLabel(lbl);
        l.setBounds(20, y, 80, 25);
        txt.setBounds(120, y, 150, 25);
        add(l);
        add(txt);
    }

    private void addButtons() {
        addBtn.setBounds(300, 20, 100, 30);
        updateBtn.setBounds(300, 60, 100, 30);
        deleteBtn.setBounds(300, 100, 100, 30);
        loadBtn.setBounds(300, 140, 100, 30);

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
                String sql = "INSERT INTO ticket(Category, Detail, Owner, Location, CreatedAt, ScheduleID) VALUES(?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, categoryTxt.getText());
                ps.setString(2, detailTxt.getText());
                ps.setString(3, ownerTxt.getText());
                ps.setString(4, locationTxt.getText());
                ps.setString(5, createdAtTxt.getText());
                ps.setInt(6, Integer.parseInt(scheduleIdTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Ticket added successfully!");

            } else if (e.getSource() == updateBtn) {
                String sql = "UPDATE ticket SET Category=?, Detail=?, Owner=?, Location=?, CreatedAt=?, ScheduleID=? WHERE TicketID=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, categoryTxt.getText());
                ps.setString(2, detailTxt.getText());
                ps.setString(3, ownerTxt.getText());
                ps.setString(4, locationTxt.getText());
                ps.setString(5, createdAtTxt.getText());
                ps.setInt(6, Integer.parseInt(scheduleIdTxt.getText()));
                ps.setInt(7, Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Ticket updated successfully!");

            } else if (e.getSource() == deleteBtn) {
                String sql = "DELETE FROM ticket WHERE TicketID=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(idTxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Ticket deleted successfully!");

            } else if (e.getSource() == loadBtn) {
                model.setRowCount(0);
                String sql = "SELECT * FROM ticket";
                ResultSet rs = con.createStatement().executeQuery(sql);
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("TicketID"),
                        rs.getString("Category"),
                        rs.getString("Detail"),
                        rs.getString("Owner"),
                        rs.getString("Location"),
                        rs.getString("CreatedAt"),
                        rs.getInt("ScheduleID")
                    });
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ticket Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new TicketPanel());
        frame.setVisible(true);
    }

	}






