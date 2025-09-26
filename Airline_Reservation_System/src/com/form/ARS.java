//NAMES OF GROUP 4
//1.MUHAYIMPUNDU CHARLENNE  RGNO.223003942
//2.NISHIMWE SEZERANO DELICE RGNO.223010486
//3.MUKAMA UYISENGA LEA RGNO.223018803

package com.form;

import java.awt.BorderLayout;

import javax.swing.*;

import com.panel.BookingPanel;
import com.panel.FlightPanel;
import com.panel.PaymentPanel;
import com.panel.SchedulePanel;
import com.panel.TicketPanel;
import com.panel.UsersPanel;


public class ARS  extends JFrame {
	JTabbedPane tabs=new JTabbedPane();
//	constructor
public ARS(String role, int userid) {
	setTitle("Airline Reservation System");
	setSize(900,600);
	setLayout(new BorderLayout());
	if(role.equalsIgnoreCase("admin")) {
		tabs.add("Users ",new UsersPanel());
		tabs.add("Ticket ",new TicketPanel());
		tabs.add("Schedule ",new SchedulePanel());
		tabs.add("Payment ",new PaymentPanel());
		tabs.add("Flight ",new FlightPanel());
		tabs.add("Booking ",new BookingPanel());
		
		
	}else if(role.equalsIgnoreCase("admin")) {
		   
	    tabs.add("Schedule", new SchedulePanel());
	    tabs.add("Flight", new FlightPanel());
	    tabs.add("Booking", new BookingPanel());
	} else if(role.equalsIgnoreCase("passenger")) {
	    
	    tabs.add("Ticket", new TicketPanel());
	    tabs.add("Payment", new PaymentPanel());
	}
	add(tabs,BorderLayout.CENTER);
	setVisible(true);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
}
	


	

}
