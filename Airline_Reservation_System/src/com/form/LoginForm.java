//NAMES OF GROUP 4
//1.MUHAYIMPUNDU CHARLENNE  RGNO.223003942
//2.NISHIMWE SEZERANO DELICE RGNO.223010486
//3.MUKAMA UYISENGA LEA RGNO.223018803

package com.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

import com.util.DB;

public class LoginForm extends JFrame implements ActionListener {
	JTextField userTxt=new JTextField("Enter Username");
	JPasswordField passTxt=new JPasswordField("password");
	JButton loginbtn=new JButton("Login");
	JButton cancelbtn=new JButton("Cancel");
//	constructor
	public LoginForm(){
		setTitle("Login form=");
		setBounds(100,100,500,400);
		setLayout(null);
		userTxt.setBounds(50,30,120,25);
		passTxt.setBounds(50,70,120,25);
		loginbtn.setBounds(50,120,100,25);
		cancelbtn.setBounds(170,120,100,25);
		
		add(userTxt);add(passTxt);add(loginbtn);add(cancelbtn);
		loginbtn.addActionListener(this);
		cancelbtn.addActionListener(this);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}
	
	public void actionPerformed(ActionEvent e) {
		try(Connection con= DB.getConnection()) {
            String sql="Select * FROM users WHERE username =? AND passwordHash=?";
            PreparedStatement ps=con.prepareCall(sql);
            ps.setString(1, userTxt.getText());
            ps.setString(2, new String(passTxt.getPassword()));
            ResultSet rs=ps.executeQuery();
            if(rs.next()) {
            	String role=rs.getString("role");
            	dispose();
            	new ARS(role, rs.getInt("userID"));
            }else {
            	JOptionPane.showMessageDialog(this, "Invalid Login");
            }
            
            
		} catch(Exception ex) { ex.printStackTrace();}
		
	}

}
