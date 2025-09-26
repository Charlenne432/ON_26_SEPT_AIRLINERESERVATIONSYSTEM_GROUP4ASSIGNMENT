//NAMES OF GROUP 4
//1.MUHAYIMPUNDU CHARLENNE  RGNO.223003942
//2.NISHIMWE SEZERANO DELICE RGNO.223010486
//3.MUKAMA UYISENGA LEA RGNO.223018803

package com.panel;

import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import java.sql.PreparedStatement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.util.DB;

public class BookingPanel extends JPanel implements ActionListener {
	private JTextField idTxt, titleTxt, dateTxt, valueTxt, notesTxt, flightIdTxt;
	private JComboBox<String> statusCmb;
	private JButton addBtn, updateBtn, deleteBtn, loadBtn;
	private JTable table;
	private DefaultTableModel model;

	public BookingPanel(){

		setLayout(null);

		// Fields
		idTxt = new JTextField();
		titleTxt = new JTextField();
		dateTxt = new JTextField();
		valueTxt = new JTextField();
		notesTxt = new JTextField();
		flightIdTxt = new JTextField();

		// Status ComboBox
		statusCmb = new JComboBox<>(new String[]{"Confirmed", "Pending", "Cancelled", "Completed"});

		// Buttons
		addBtn = new JButton("Add");
		updateBtn = new JButton("Update");
		deleteBtn = new JButton("Delete");
		loadBtn = new JButton("Load");

		// Table
		String[] labels = {"BookingID", "Title", "Date", "Status", "Value", "Notes", "FlightID"};
		model = new DefaultTableModel(labels, 0);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(20, 280, 750, 200);
		add(sp);

		// Layout fields
		int y = 20;
		addField("BookingID", idTxt, y); y += 30;
		addField("Title", titleTxt, y); y += 30;
		addField("Date", dateTxt, y); y += 30;
		addField("Status", statusCmb, y); y += 30;
		addField("Value", valueTxt, y); y += 30;
		addField("Notes", notesTxt, y); y += 30;
		addField("FlightID", flightIdTxt, y); y += 30;

		// Buttons
		addButtons();

		// Add table row click listener to populate fields
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					idTxt.setText(model.getValueAt(row, 0).toString());
					titleTxt.setText(model.getValueAt(row, 1).toString());
					dateTxt.setText(model.getValueAt(row, 2).toString());
					statusCmb.setSelectedItem(model.getValueAt(row, 3).toString());
					valueTxt.setText(model.getValueAt(row, 4).toString());
					notesTxt.setText(model.getValueAt(row, 5).toString());
					flightIdTxt.setText(model.getValueAt(row, 6).toString());
				}
			}
		});
	}

	private void addField(String lbl, JComponent txt, int y) {
		JLabel l = new JLabel(lbl);
		l.setBounds(20, y, 80, 25);
		txt.setBounds(100, y, 150, 25);
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

	private void clearFields() {
		idTxt.setText("");
		titleTxt.setText("");
		dateTxt.setText("");
		statusCmb.setSelectedIndex(0);
		valueTxt.setText("");
		notesTxt.setText("");
		flightIdTxt.setText("");


	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try (Connection con = DB.getConnection()) {
			if (e.getSource() == addBtn) {
				// Check if FlightID exists (optional validation)
				if (!flightIdTxt.getText().isEmpty()) {
					String checkFlightSql = "SELECT * FROM flight WHERE FlightID=?";
					PreparedStatement checkFlightPs = con.prepareStatement(checkFlightSql);
					checkFlightPs.setInt(1, Integer.parseInt(flightIdTxt.getText()));
					ResultSet flightRs = checkFlightPs.executeQuery();

					if (!flightRs.next()) {
						JOptionPane.showMessageDialog(this,
								"⚠ Flight ID does not exist!",
								"Invalid Flight ID",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
				}

				String sql = "INSERT INTO booking(Title, Date, Status, Value, Notes, FlightID) VALUES(?,?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, titleTxt.getText());
				ps.setString(2, dateTxt.getText());
				ps.setString(3, statusCmb.getSelectedItem().toString());
				ps.setDouble(4, Double.parseDouble(valueTxt.getText()));
				ps.setString(5, notesTxt.getText());
				if (flightIdTxt.getText().isEmpty()) {
					ps.setNull(6, Types.INTEGER);
				} else {
					ps.setInt(6, Integer.parseInt(flightIdTxt.getText()));
				}
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "✅ Booking added successfully!");
				clearFields();
				loadBookings(con);

			} else if (e.getSource() == updateBtn) {
				if (idTxt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(this, "⚠ Please select a booking to update!");
					return;
				}

				// Check if FlightID exists (optional validation)
				if (!flightIdTxt.getText().isEmpty()) {
					String checkFlightSql = "SELECT * FROM flight WHERE FlightID=?";
					PreparedStatement checkFlightPs = con.prepareStatement(checkFlightSql);
					checkFlightPs.setInt(1, Integer.parseInt(flightIdTxt.getText()));
					ResultSet flightRs = checkFlightPs.executeQuery();

					if (!flightRs.next()) {
						JOptionPane.showMessageDialog(this,
								"⚠ Flight ID does not exist!",
								"Invalid Flight ID",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
				}

				String sql = "UPDATE booking SET Title=?, Date=?, Status=?, Value=?, Notes=?, FlightID=? WHERE BookingID=?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, titleTxt.getText());
				ps.setString(2, dateTxt.getText());
				ps.setString(3, statusCmb.getSelectedItem().toString());
				ps.setDouble(4, Double.parseDouble(valueTxt.getText()));
				ps.setString(5, notesTxt.getText());
				if (flightIdTxt.getText().isEmpty()) {
					ps.setNull(6, Types.INTEGER);
				} else {
					ps.setInt(6, Integer.parseInt(flightIdTxt.getText()));
				}
				ps.setInt(7, Integer.parseInt(idTxt.getText()));
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "✅ Booking updated successfully!");
				clearFields();
				loadBookings(con);

			} else if (e.getSource() == deleteBtn) {
				if (idTxt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(this, "⚠ Please select a booking to delete!");
					return;
				}

				int confirm = JOptionPane.showConfirmDialog(this, 
						"Are you sure you want to delete this booking?", 
						"Confirm Delete", 
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					String sql = "DELETE FROM booking WHERE BookingID=?";
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setInt(1, Integer.parseInt(idTxt.getText()));
					ps.executeUpdate();
					JOptionPane.showMessageDialog(this, "✅ Booking deleted successfully!");
					clearFields();
					loadBookings(con);
				}

			} else if (e.getSource() == loadBtn) {
				loadBookings(con);
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "⚠ Invalid number format! Please check Value and FlightID fields.");
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
		}
	}

	private void loadBookings(Connection con) throws SQLException {
		model.setRowCount(0);
		String sql = "SELECT * FROM booking ORDER BY BookingID";
		ResultSet rs = con.createStatement().executeQuery(sql);
		while (rs.next()) {
			model.addRow(new Object[]{
					rs.getInt("BookingID"),
					rs.getString("Title"),
					rs.getString("Date"),
					rs.getString("Status"),
					rs.getDouble("Value"),
					rs.getString("Notes"),
					rs.getObject("FlightID") // Using getObject to handle potential NULL values
			});
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(null); {
			JFrame frame = new JFrame("Booking Management Panel");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(800, 600);
			frame.setLocationRelativeTo(null);
			frame.add(new BookingPanel());
			frame.setVisible(true);
		};


	}


}


