//package Grey;

import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.Connection;
import java.sql.Statement;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;

import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.Statement;

public class AddStaffHardware extends JFrame
  {
  private static final String dbHost = "localhost";

  private static final String dbName = "qdl";

  private static final String dbUsername = "root";

  private static final String dbPassword = "";

  private static final String appTitle = "Device Movement Logger";

  private static final String statusReady = "Ready";

  private static final String statusFoundHardware = "Hardware Found.";

  private static final String statusHardwareDetails = "Please insert hardware details!!!";

  private static final String statusStaffDetails = "Please insert staff details!!!";

  private static final String statusListing = "Reading database...";

  private static final String statusSending = "Sending to database...";

  private static final String statusNumHardware = "Number of devices logged in : %d";

  private static final String errCouldNotConnectToDb = "Error: could not connect to the database";

  private static final String statusErrReadDatabase = "Error: could not read from the database";

  private static final String statusErrUpdateDatabase = "Error: could not update the database";

  private static final String statusErrStaffRequiredFields = "Error: please fill up all required fields";
  
  private static final String statusErrStaffHardwareRequiredFields = "Error: Select your staff Id and at least 1 device!";

  private static final String statusErrHardwareRequiredFields = "Error: please fill up all required fields";

  private static final String statusUpdatedDatabase = "Updated the database";
  
  private static final String statusSelectDirection = "Please select your sign in destination!";
  
  private static final String statusSelectDestination = "Please select your sign out destination!";

  private static final String nobarcodePlaceHolder = "Please scan the barcode of an already registered device.";

  private static final String scanText = "Click the above Text Field before scanning any barcode.";
  
  private static final String staffHardwareTitle = "Assign Extra Hardware to Staff";

  private static final String labelStaffHardwareSubmit = "Submit";

  private static final String staffHardwareIdLabel = "Staff : ";
  
  private static final String staffHardwareDevice2Label = "Second Device : ";
  
  private static final String staffHardwareDevice3Label = "Third Device : ";

  private JComboBox staffHardwareIdDropDown;
  
  private JComboBox staffHardwareOptional1DeviceDropDown;

  private JComboBox staffHardwareOptional2DeviceDropDown;
  
  private Connection dbconnection = null;
  
  private JPanel statusBarPanel;

  private JLabel statusBar;
  
  ConnectionHelper helper = new ConnectionHelper();
  
	public AddStaffHardware()
	{

		getContentPane().add(constructMain(), BorderLayout.NORTH);
		setTitle(staffHardwareTitle);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/ac-icon.jpg")));
		pack();
		setMinimumSize(getSize());
		setSize(400, 300);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);

		try
		{
			if(helper.getConnection() != null)
			addStaffHardwareCall();
			staffHardwareIdDropDown.setSelectedItem(null);
			staffHardwareOptional1DeviceDropDown.setSelectedItem(null);
			staffHardwareOptional2DeviceDropDown.setSelectedItem(null);
		}
		catch (SQLException xcp)
		{
			ProjectNew.Log("", ProjectNew.ActivityType.ERROR, "Could not establish connection to the Database Server! Tried 1 time(s) : Giving up...", ProjectNew.getDelayTime());
			xcp.printStackTrace();
		}
		catch (Exception xcp)
		{
			xcp.printStackTrace();
		}
		
	}

  private void newStaffHardwareSend()
  {
	if (staffHardwareIdDropDown.getSelectedItem() == null || staffHardwareOptional1DeviceDropDown.getSelectedItem() == null)
	{
		Toolkit.getDefaultToolkit().beep();
		ProjectNew.Log("", ProjectNew.ActivityType.WARNING, statusErrStaffHardwareRequiredFields, ProjectNew.getDelayTime());
		return;
	}
    try
    {	  
		  Connection dbconnection = helper.getConnection();
		  CallableStatement cs2 = dbconnection.prepareCall("{call addStaffEquipment(?,?)}");
		  cs2.setObject(1, staffHardwareIdDropDown.getSelectedItem().toString().split(" ")[0]);
		  cs2.setObject(2, staffHardwareOptional1DeviceDropDown.getSelectedItem().toString().split(" ")[3]);
		  
		  CallableStatement cs3 = dbconnection.prepareCall("{call addStaffEquipment(?,?)}");

		if (!(staffHardwareOptional2DeviceDropDown.getSelectedItem() == null))
		{	  
			cs3.setObject(1, staffHardwareIdDropDown.getSelectedItem().toString().split(" ")[0]);
			cs3.setObject(2, staffHardwareOptional2DeviceDropDown.getSelectedItem().toString().split(" ")[3]);
			
			if (cs3.executeUpdate() >= 1)
			{
				setVisible(false);
			}
		}
		if (cs2.executeUpdate() >= 1 || cs3.executeUpdate() >= 1)
		{
			ProjectNew.Log( "", ProjectNew.ActivityType.NORMAL, "Hardware has successfully been assigned to the staff!", ProjectNew.getDelayTime());
			staffHardwareIdDropDown.setSelectedItem(null);
			staffHardwareOptional1DeviceDropDown.setSelectedItem(null);
			staffHardwareOptional2DeviceDropDown.setSelectedItem(null);
			setVisible(false);
				
			return;
		}
    }
	catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException d)
	{
		d.printStackTrace();
		ProjectNew.Log( "", ProjectNew.ActivityType.ERROR, "This device has already been assigned to another staff", ProjectNew.getDelayTime());
	}
	catch (SQLException xcp)
	{
		ProjectNew.Log("", ProjectNew.ActivityType.ERROR, "Could not establish connection to the Database Server! Tried 1 time(s) : Giving up...", ProjectNew.getDelayTime());
		xcp.printStackTrace();
	}

    catch (Exception e)
    {
      e.printStackTrace();
	  ProjectNew.Log( "", ProjectNew.ActivityType.ERROR, "This staff already has 3 devices!!!!", ProjectNew.getDelayTime());
      staffHardwareIdDropDown.setSelectedItem(null);
	  staffHardwareOptional1DeviceDropDown.setSelectedItem(null);
	  staffHardwareOptional2DeviceDropDown.setSelectedItem(null);
    }
  }

  private void addStaffHardwareCall()
  {
    try
    {
	  Connection dbconnection = helper.getConnection();
	  CallableStatement cs5 = dbconnection.prepareCall("{call getSerialNumber()}");
	  ResultSet result5 = cs5.executeQuery();
	  
	  CallableStatement cs6 = dbconnection.prepareCall("{call getStaffId()}");
	  ResultSet result6 = cs6.executeQuery();

      while (result5.next())
      {
        staffHardwareOptional1DeviceDropDown.addItem(String.format("%s %s %s %s", result5.getString("Type_ID"), result5.getString("Brand_ID"), result5.getString("Model"), result5.getString("SerialNumber")));
        staffHardwareOptional2DeviceDropDown.addItem(String.format("%s %s %s %s", result5.getString("Type_ID"), result5.getString("Brand_ID"), result5.getString("Model"), result5.getString("SerialNumber")));
	  }
	  while (result6.next())
	  {
        staffHardwareIdDropDown.addItem( String.format("%s %s %s", result6.getString("Staff_ID"), result6.getString("FirstName"), result6.getString("SurName")));
	  }
    }
	catch (SQLException xcp)
	{
		ProjectNew.Log("", ProjectNew.ActivityType.ERROR, "Could not establish connection to the Database Server! Tried 1 time(s) : Giving up...", ProjectNew.getDelayTime());
		xcp.printStackTrace();
	}

    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

	private JPanel constructMain()
	{
    JPanel topPanel = new JPanel();
    topPanel.setPreferredSize(new Dimension(10, 200));
    GridBagLayout layout = new GridBagLayout();
    topPanel.setLayout(layout);

    staffHardwareIdDropDown = new JComboBox();
    staffHardwareOptional1DeviceDropDown = new JComboBox();
    staffHardwareOptional2DeviceDropDown = new JComboBox();

    JButton submitButton = new JButton(labelStaffHardwareSubmit);

	submitButton.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			newStaffHardwareSend();
		}
	});

    GridBagConstraints c = new GridBagConstraints();
    c.ipadx = 5;
    c.ipady = 5;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0;
    c.weighty = 1.0;
    c.gridx = 0;
    c.gridy = 0;
    topPanel.add(new JLabel(staffHardwareIdLabel, JLabel.RIGHT), c);
    c.gridy = 1;
    topPanel.add(new JLabel(staffHardwareDevice2Label, JLabel.RIGHT), c);
    c.gridy = 2;
    topPanel.add(new JLabel(staffHardwareDevice3Label, JLabel.RIGHT), c);
    c.weightx = 1.0;
    c.gridx = 2;
    c.gridy = 0;
    topPanel.add(staffHardwareIdDropDown, c);
    c.gridy = 1;
    topPanel.add(staffHardwareOptional1DeviceDropDown, c);
    c.gridy = 2;
    topPanel.add(staffHardwareOptional2DeviceDropDown, c);
    c.gridy = 3;
    c.weightx = 0;
    topPanel.add(submitButton, c);

    return topPanel;
	}
  }