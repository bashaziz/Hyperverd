//package Grey;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class AboutGrey extends JFrame
{
	private JPanel areaPanel;
	
	private JTextArea area;
	
	private String storeAllString = "- Software Name : Grey.\n- Description : Device Logging Software.\n- Software Version : v1.4.0.\n- Created-by : Quanteq Technology Services.\n- Authors :  Bashir Aliyu & Nazir Aliyu.\n- Copyright © 2014, All rights reserved.\n- Build Time : March 17 2014 13:01:16";
	
	public AboutGrey()
	{
		getContentPane().setLayout(new BorderLayout());
		
		areaPanel = new JPanel();
		areaPanel.setPreferredSize(new Dimension(60, 60));
		areaPanel.setBorder(BorderFactory.createTitledBorder("About Grey"));
		area = new JTextArea(10, 45);
		JScrollPane pane = new JScrollPane(area);
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		
		setTitle("About Grey");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/ac-icon.jpg")));
		setSize(500, 200);
		setLocationRelativeTo(null);
		areaPanel.add(area, BorderLayout.CENTER);
		add(areaPanel);
		viewAboutGrey();
	}
	
	private void viewAboutGrey()
	{
	  area.append(storeAllString);
	}
}