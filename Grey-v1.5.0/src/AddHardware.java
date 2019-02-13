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

public class AddHardware extends JFrame
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

  //ADD HARDWARE THINGS......
  private static final String hardwareTitle = "Register a Device";

  private static final String hardwareTypeLabel = "* Type : ";

  private static final String hardwareBrandLabel = "* Brand : ";

  private static final String hardwareModelLabel = "* Model : ";

  private static final String hardwareSerialNumLabel = "* S/N : ";

  private static final String hardwareBarcodeLabel = "* Barcode : ";

  private static final String labelHardwareSubmit = "Submit";

  private JComboBox hardwareTypeDropDown;

  private JComboBox hardwareBrandDropDown;

  private JTextField hardwareModelTextField;

  private JTextField hardwareSerialNumTextField;

  private JTextField hardwareBarcodeTextField;
  
  private Connection dbconnection = null;
  
  private JPanel statusBarPanel;

  private JPanel scanPanel;

  private JLabel statusBar;
  
  ConnectionHelper helper = new ConnectionHelper();

  
	public AddHardware()
	{

		getContentPane().add(constructMain(), BorderLayout.NORTH);

		setTitle(hardwareTitle);
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
			addHardwareCall();
			hardwareTypeDropDown.setSelectedItem(null);
			hardwareBrandDropDown.setSelectedItem(null);
			hardwareModelTextField.setText(null);
			hardwareSerialNumTextField.setText(null);
			hardwareBarcodeTextField.setText(null);
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

  private void sleep(long millis)
  {
    try
    {
      Thread.sleep(300);
    }
    catch (InterruptedException e)
    {

    }
  }

  private void newHardwareSend()
  {
	if (hardwareModelTextField.getText().equals("") || hardwareSerialNumTextField.getText().equals("") || hardwareBarcodeTextField.getText().equals("") || hardwareTypeDropDown.getSelectedItem() == null || hardwareBrandDropDown.getSelectedItem() == null)
	{
	  Toolkit.getDefaultToolkit().beep();
	  ProjectNew.Log("", ProjectNew.ActivityType.WARNING, statusErrHardwareRequiredFields, ProjectNew.getDelayTime());	  
	  return;
	}
    try
    {
	  Connection dbconnection = helper.getConnection();
	  CallableStatement cs = dbconnection.prepareCall("{call addEquipment(?,?,?,?,?)}");
	  
	  cs.setString(1, hardwareSerialNumTextField.getText());
	  cs.setObject(2, hardwareBrandDropDown.getSelectedItem().toString().split("-")[0]);
      cs.setString(3, hardwareModelTextField.getText());   
      cs.setString(4, hardwareBarcodeTextField.getText());
	  cs.setObject(5, hardwareTypeDropDown.getSelectedItem().toString().split("-")[0]);
	  
	  if (cs.executeUpdate() >= 1)
	  {
		ProjectNew.Log("", ProjectNew.ActivityType.NORMAL, "Device has successfully been registered!!!", ProjectNew.getDelayTime());		
		hardwareTypeDropDown.setSelectedItem(null);
		hardwareBrandDropDown.setSelectedItem(null);
		hardwareModelTextField.setText(null);
		hardwareSerialNumTextField.setText(null);
		hardwareBarcodeTextField.setText(null);
		setVisible(false);
		
		
		return;
	  }
    }
	catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException d)
	{
		d.printStackTrace();
		ProjectNew.Log( "", ProjectNew.ActivityType.ERROR, "A hardware with the same details already exists!!", ProjectNew.getDelayTime());
		hardwareTypeDropDown.setSelectedItem(null);
		hardwareBrandDropDown.setSelectedItem(null);
		hardwareModelTextField.setText(null);
		hardwareSerialNumTextField.setText(null);
		hardwareBarcodeTextField.setText(null);
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

  private void addHardwareCall()
  {
    try
    {
	  Connection dbconnection = helper.getConnection();
	  CallableStatement cs = dbconnection.prepareCall("{call getEquipmentType()}");
	  ResultSet result = cs.executeQuery();
	  
	  CallableStatement cs1 = dbconnection.prepareCall("{call getBrandType}");
	  ResultSet result1 = cs1.executeQuery();

      int count = 0;
      while (result.next())
      {
        hardwareTypeDropDown.addItem( String.format("%s - %s", result.getString("Type_ID"), result.getString("Description")));
        count++;
      }

      while (result1.next())
      {
        hardwareBrandDropDown.addItem( String.format("%s - %s", result1.getString("Brand_ID"), result1.getString("Description")));
        count++;
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

    hardwareTypeDropDown = new JComboBox();
    hardwareBrandDropDown = new JComboBox();
    hardwareModelTextField = new JTextField(50);
    hardwareSerialNumTextField = new JTextField(50);
    hardwareBarcodeTextField = new JTextField(50);


    JButton hardwareSubmitButton = new JButton(labelHardwareSubmit);

	hardwareSubmitButton.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent ae)
		{
			newHardwareSend();
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
    topPanel.add(new JLabel(hardwareTypeLabel, JLabel.RIGHT), c);
    c.gridy = 1;
    topPanel.add(new JLabel(hardwareBrandLabel, JLabel.RIGHT), c);
    c.gridy = 2;
    topPanel.add(new JLabel(hardwareModelLabel, JLabel.RIGHT), c);
    c.gridy = 3;
    topPanel.add(new JLabel(hardwareSerialNumLabel, JLabel.RIGHT), c);
    c.gridy = 4;
    topPanel.add(new JLabel(hardwareBarcodeLabel, JLabel.RIGHT), c);
    c.weightx = 1.0;
    c.gridx = 1;
    c.gridy = 0;
    topPanel.add(hardwareTypeDropDown, c);
    c.gridy = 1;
    topPanel.add(hardwareBrandDropDown, c);
    c.gridy = 2;
    topPanel.add(hardwareModelTextField, c);
    c.gridy = 3;
    topPanel.add(hardwareSerialNumTextField, c);
    c.gridy = 4;
    topPanel.add(hardwareBarcodeTextField, c);
    c.gridy = 5;
    c.weightx = 0;
    topPanel.add(hardwareSubmitButton, c);

    return topPanel;
	}
  }