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
import com.util.*;

public class FlightPanel extends JPanel implements ActionListener {
	private JTextField idTxt, flightNumberTxt, nameTxt, descriptionTxt, originTxt, destinationTxt, departureTimeTxt, arrivalTimeTxt, createdAtTxt;
	private JComboBox<String> statusCmb;
	private JButton addBtn, updateBtn, deleteBtn, loadBtn;
	private JTable table;
	private DefaultTableModel model;

	public FlightPanel(){

		setLayout(null);

		// Fields
		idTxt = new JTextField();
		flightNumberTxt = new JTextField();
		nameTxt = new JTextField();
		descriptionTxt = new JTextField();
		originTxt = new JTextField();
		destinationTxt = new JTextField();
		departureTimeTxt = new JTextField();
		arrivalTimeTxt = new JTextField();
		createdAtTxt = new JTextField();

		// Status ComboBox
		statusCmb = new JComboBox<>(new String[]{"Scheduled", "Boarding", "Departed", "Arrived", "Delayed", "Cancelled"});

		// Buttons
		addBtn = new JButton("Add");
		updateBtn = new JButton("Update");
		deleteBtn = new JButton("Delete");
		loadBtn = new JButton("Load");

		// Table
		String[] labels = {"FlightID", "FlightNumber", "Name", "Description", "Origin", "Destination", "DepartureTime", "ArrivalTime", "Status", "CreatedAt"};
		model = new DefaultTableModel(labels, 0);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(20, 320, 950, 200);
		add(sp);

		// Layout fields in two columns
		int y = 20;
		// Left column
		addField("FlightID", idTxt, 20, y); y += 30;
		addField("Flight Number", flightNumberTxt, 20, y); y += 30;
		addField("Name", nameTxt, 20, y); y += 30;
		addField("Description", descriptionTxt, 20, y); y += 30;
		addField("Origin", originTxt, 20, y); y += 30;

		// Right column
		y = 20;
		addField("Destination", destinationTxt, 400, y); y += 30;
		addField("Departure Time", departureTimeTxt, 400, y); y += 30;
		addField("Arrival Time", arrivalTimeTxt, 400, y); y += 30;
		addField("Status", statusCmb, 400, y); y += 30;
		addField("Created At", createdAtTxt, 400, y); y += 30;

		// Buttons
		addButtons();

		// Add table row click listener to populate fields
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					idTxt.setText(model.getValueAt(row, 0).toString());
					flightNumberTxt.setText(model.getValueAt(row, 1).toString());
					nameTxt.setText(model.getValueAt(row, 2).toString());
					descriptionTxt.setText(model.getValueAt(row, 3).toString());
					originTxt.setText(model.getValueAt(row, 4).toString());
					destinationTxt.setText(model.getValueAt(row, 5).toString());
					departureTimeTxt.setText(model.getValueAt(row, 6).toString());
					arrivalTimeTxt.setText(model.getValueAt(row, 7).toString());
					statusCmb.setSelectedItem(model.getValueAt(row, 8).toString());
					createdAtTxt.setText(model.getValueAt(row, 9).toString());
				}
			}
		});
	}

	private void addField(String lbl, JComponent component, int x, int y) {
		JLabel label = new JLabel(lbl);
		label.setBounds(x, y, 120, 25);
		component.setBounds(x + 125, y, 150, 25);
		add(label);
		add(component);
	}

	private void addButtons() {
		addBtn.setBounds(750, 20, 100, 30);
		updateBtn.setBounds(750, 60, 100, 30);
		deleteBtn.setBounds(750, 100, 100, 30);
		loadBtn.setBounds(750, 140, 100, 30);

		add(addBtn);
		add(updateBtn);
		add(deleteBtn);
		add(loadBtn);

		addBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		loadBtn.addActionListener(this);
	}

	private void clearFields() {
		idTxt.setText("");
		flightNumberTxt.setText("");
		nameTxt.setText("");
		descriptionTxt.setText("");
		originTxt.setText("");
		destinationTxt.setText("");
		departureTimeTxt.setText("");
		arrivalTimeTxt.setText("");
		statusCmb.setSelectedIndex(0);
		createdAtTxt.setText("");



	}
	@Override
	public void actionPerformed(ActionEvent e) {
		 try (Connection con = DB.getConnection()) {
	            if (e.getSource() == addBtn) {
	                // Check for duplicate flight numbers
	                String checkSql = "SELECT * FROM flight WHERE FlightNumber=?";
	                PreparedStatement checkPs = con.prepareStatement(checkSql);
	                checkPs.setString(1, flightNumberTxt.getText());
	                ResultSet rs = checkPs.executeQuery();

	                if (rs.next()) {
	                    JOptionPane.showMessageDialog(this,
	                            "⚠ Flight Number already exists!",
	                            "Duplicate Error",
	                            JOptionPane.WARNING_MESSAGE);
	                } else {
	                    String sql = "INSERT INTO flight(FlightNumber, Name, Description, Origin, Destination, DepartureTime, ArrivalTime, Status, CreatedAt) VALUES(?,?,?,?,?,?,?,?,?)";
	                    PreparedStatement ps = con.prepareStatement(sql);
	                    ps.setString(1, flightNumberTxt.getText());
	                    ps.setString(2, nameTxt.getText());
	                    ps.setString(3, descriptionTxt.getText());
	                    ps.setString(4, originTxt.getText());
	                    ps.setString(5, destinationTxt.getText());
	                    ps.setString(6, departureTimeTxt.getText());
	                    ps.setString(7, arrivalTimeTxt.getText());
	                    ps.setString(8, statusCmb.getSelectedItem().toString());
	                    ps.setString(9, createdAtTxt.getText());
	                    ps.executeUpdate();
	                    JOptionPane.showMessageDialog(this, "✅ Flight added successfully!");
	                    clearFields();
	                    loadFlights(con);
	                }
	            } else if (e.getSource() == updateBtn) {
	                if (idTxt.getText().isEmpty()) {
	                    JOptionPane.showMessageDialog(this, "⚠ Please select a flight to update!");
	                    return;
	                }
	                String sql = "UPDATE flight SET FlightNumber=?, Name=?, Description=?, Origin=?, Destination=?, DepartureTime=?, ArrivalTime=?, Status=?, CreatedAt=? WHERE FlightID=?";
	                PreparedStatement ps = con.prepareStatement(sql);
	                ps.setString(1, flightNumberTxt.getText());
	                ps.setString(2, nameTxt.getText());
	                ps.setString(3, descriptionTxt.getText());
	                ps.setString(4, originTxt.getText());
	                ps.setString(5, destinationTxt.getText());
	                ps.setString(6, departureTimeTxt.getText());
	                ps.setString(7, arrivalTimeTxt.getText());
	                ps.setString(8, statusCmb.getSelectedItem().toString());
	                ps.setString(9, createdAtTxt.getText());
	                ps.setInt(10, Integer.parseInt(idTxt.getText()));
	                ps.executeUpdate();
	                JOptionPane.showMessageDialog(this, "✅ Flight updated successfully!");
	                clearFields();
	                loadFlights(con);
	            } else if (e.getSource() == deleteBtn) {
	                if (idTxt.getText().isEmpty()) {
	                    JOptionPane.showMessageDialog(this, "⚠ Please select a flight to delete!");
	                    return;
	                }
	                int confirm = JOptionPane.showConfirmDialog(this, 
	                    "Are you sure you want to delete this flight?", 
	                    "Confirm Delete", 
	                    JOptionPane.YES_NO_OPTION);
	                if (confirm == JOptionPane.YES_OPTION) {
	                    String sql = "DELETE FROM flight WHERE FlightID=?";
	                    PreparedStatement ps = con.prepareStatement(sql);
	                    ps.setInt(1, Integer.parseInt(idTxt.getText()));
	                    ps.executeUpdate();
	                    JOptionPane.showMessageDialog(this, "✅ Flight deleted successfully!");
	                    clearFields();
	                    loadFlights(con);
	                }
	            } else if (e.getSource() == loadBtn) {
	                loadFlights(con);
	            }
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(this, "⚠ Invalid number format!");
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
	        }
	    }

	    private void loadFlights(Connection con) throws SQLException {
	        model.setRowCount(0);
	        String sql = "SELECT * FROM flight ORDER BY FlightID";
	        ResultSet rs = con.createStatement().executeQuery(sql);
	        while (rs.next()) {
	            model.addRow(new Object[]{
	                    rs.getInt("FlightID"),
	                    rs.getString("FlightNumber"),
	                    rs.getString("Name"),
	                    rs.getString("Description"),
	                    rs.getString("Origin"),
	                    rs.getString("Destination"),
	                    rs.getString("DepartureTime"),
	                    rs.getString("ArrivalTime"),
	                    rs.getString("Status"),
	                    rs.getString("CreatedAt")
	            });
	        }
	    }

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(null);{
	            JFrame frame = new JFrame("Flight Management Panel");
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            frame.setSize(1000, 600);
	            frame.setLocationRelativeTo(null);
	            frame.add(new FlightPanel());
	            frame.setVisible(true);
	        };

		
	
	}

}

