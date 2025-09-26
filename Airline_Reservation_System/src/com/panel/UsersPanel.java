//NAMES OF GROUP 4
//1.MUHAYIMPUNDU CHARLENNE  RGNO.223003942
//2.NISHIMWE SEZERANO DELICE RGNO.223010486
//3.MUKAMA UYISENGA LEA RGNO.223018803

package com.panel;

import com.util.DB;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UsersPanel extends JPanel implements ActionListener {

	private JTextField idTxt, nameTxt,roleTxt,emailTxt,CreatedAtTxt;
	private JPasswordField passTxt;
	private JButton addBtn, updateBtn, deleteBtn, loadBtn;
	private JTable table;
	private DefaultTableModel model;
	JComboBox<String> roleCmb = new JComboBox<>(new String[]{"admin", "passenger"});


	public UsersPanel() {
		setLayout(null);

		// Fields
		idTxt = new JTextField();
		nameTxt = new JTextField();
		passTxt = new JPasswordField();
		roleTxt = new JTextField();
		emailTxt = new JTextField();
		CreatedAtTxt = new JTextField();
          
		// Buttons
		addBtn = new JButton("Add");
		updateBtn = new JButton("Update");
		deleteBtn = new JButton("Delete");
		loadBtn = new JButton("Load");

		// Table
		String[] labels = {"ID", "Username", "PasswordHash", "Role","Email","CreatedAt"};
		model = new DefaultTableModel(labels, 0);
		table = new JTable(model);
		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(20, 250, 750, 200);
		add(sp);

		// Layout fields
		int y = 20;
		addField("ID", idTxt, y); y += 30;
		addField("Username", nameTxt, y); y += 30;
		addField("PasswordHash", passTxt, y); y += 30;
		addField("Role", roleTxt, y); y += 30;
		addField("Email", emailTxt, y); y += 30;
		addField("CreatedAt", CreatedAtTxt, y); y += 30;

		// Buttons
		addButtons();
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

	@Override
	public void actionPerformed(ActionEvent e) {
		try (Connection con = DB.getConnection()) {
			if (e.getSource() == addBtn) {
				// Check for duplicates
				String checkSql = "SELECT * FROM users WHERE username=?";
				PreparedStatement checkPs = con.prepareStatement(checkSql);
				checkPs.setString(1, nameTxt.getText());
				ResultSet rs = checkPs.executeQuery();

				if (rs.next()) {
					JOptionPane.showMessageDialog(this,
							"⚠ Username already exists!",
							"Duplicate Error",
							JOptionPane.WARNING_MESSAGE);
				} else {
					String sql = "INSERT INTO `users`(username, passwordHash,role,email,CreatedAt) VALUES(?,?,?,?,?)";
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setString(1, nameTxt.getText());
					ps.setString(2, new String(passTxt.getPassword()));
					ps.setString(3, roleTxt.getText());
					ps.setString(4, emailTxt.getText());
					ps.setString(5, CreatedAtTxt.getText());
					ps.executeUpdate();
					JOptionPane.showMessageDialog(this, " User added successfully!");
				}
			} else if (e.getSource() == updateBtn) {
				String sql = "UPDATE users SET username=?, passwordHash=?, role=?,email=?,CreatedAt=?, WHERE userid=?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, nameTxt.getText());
				ps.setString(2, new String(passTxt.getPassword()));
				ps.setString(3, roleTxt.getText());
				ps.setString(4, emailTxt.getText());
				ps.setString(5, CreatedAtTxt.getText());
				ps.setInt(6, Integer.parseInt(idTxt.getText()));
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, " User updated successfully!");
			} else if (e.getSource() == deleteBtn) {
				String sql = "DELETE FROM users WHERE userID=?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(idTxt.getText()));
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, " User deleted successfully!");
			} else if (e.getSource() == loadBtn) {
				model.setRowCount(0);
				String sql = "SELECT * FROM users";
				ResultSet rs = con.createStatement().executeQuery(sql);
				while (rs.next()) {
					model.addRow(new Object[]{
							rs.getInt("userID"),
							rs.getString("Username"),
							rs.getString("PasswordHash"),
							rs.getString("role"),
							rs.getString("email"),
							rs.getString("CreatedAt"),
					});
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, " Error: " + ex.getMessage());
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Users Panel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.add(new UsersPanel());
		frame.setVisible(true);
	}
}
