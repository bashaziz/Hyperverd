//package Grey;

import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.*;
import javax.swing.JDialog;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.*; 
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.sql.DriverManager;

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

import java.util.logging.Level;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import javax.media.*;
import javax.media.format.*;
import javax.media.util.*;
import javax.media.control.*;
import javax.media.protocol.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.GroupLayout;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.Statement;

public class ProjectNew extends JFrame
{ 
  public static final String labelAddStaff = "Add Staff";

  public static final String labelAddHardware = "Add Hardware";

  public static final String labelViewReport = "View Report";

  public static final String labelAddStaffHardware = "Assign \nExtra Hardware to\n Staff";

  public static final String appTitle = "Grey";
  
  public static final String nobarcodePlaceHolder = "Please scan the barcode of an already registered device.";

  public static final String scanText = "Click the above Text Field before scanning any barcode!";
  
  public static final String statusSelectDirection = "Please select your Check-in destination!";
  
  public static final String statusSelectDestination = "Please select your Check-out destination!";
  
  public static final String statusUpdatedDatabase = "Updated the database";

  public JPanel statusBarPanel;

  public JPanel scanPanel;

  public JLabel statusBar;

  public JTextField scanAreaTextField;
  
  public Timer timer = null;

  public TimerTask timerTask;
  
  public JMenuBar menuBar;
  
  public JLabel ownerLabel;
  
  public JLabel brandLabel;
  
  public JLabel barcodeLabel;
  
  public JLabel serialLabel;
  
  public JButton signInButton;
  
  public JButton signOutButton;
  
  public JButton hardwareButton;
  
  public JButton staffButton;
  
  public JButton reportButton;
  
  public JButton otherButton;

  public JComboBox scannedHardwareDirectionDropDown;
  
  public JComboBox scannedHardwareDestinationDropDown;

  public static final String scannedHardwareDirectionLabel = "Check-in Destination : ";
  
  public static final String scannedHardwareDestinationLabel = "Check-out Destination : ";
  
  public static final String errCouldNotConnectToDb = "Error: could not connect to the database";

  public static final String statusErrReadDatabase = "Error: could not read from the database";
  
  private JPanel mainPhotoPanel;
  
  private JLabel lblPhoto;
  
  ConnectionHelper helper = new ConnectionHelper();

  public Connection dbconnection = null;
  
  //public TimerTask  timerTask2;

    private javax.swing.JProgressBar progressBar;
    private javax.swing.JScrollPane sPaneActivityLogger;
    private javax.swing.JLabel statusAnimationLabel;
    private static javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    public static javax.swing.JTextArea txtAActivityLogger;
    static boolean running = false;
    static String msgs = "";
	private javax.swing.JMenuItem miSound;
	public static String actType = "";
  
    public static void Log(String user, ActivityType activityType, String msg, int delay)
    {  
        String fullMsg = "";
        
        if(activityType == ActivityType.ERROR)
            actType = "ERROR";
        
        if(activityType == ActivityType.NORMAL)
            actType = "NORMAL";
        
        if(activityType == ActivityType.WARNING)
            actType = "WARNING";
			
        if(activityType == ActivityType.CONNECTED)
            actType = "LINK ESTABLISHED!";
        
        fullMsg = Calendar.getInstance().getTime().toString() + " : "+ user + " : " + actType +  " : " + msg + '\n';
        ProjectNew.updateLog(fullMsg, delay);
    }
    
   public static enum ActivityType
    {
        NORMAL, ERROR, WARNING, CONNECTED;
    }
	
  public void Delay(TimerTask timerTask, long delay) 
  {
	Delay(timerTask, 600);
  }
	
  public static int getDelayTime() {
	return GreyStatusConfig.findDelayTime();
  }
	public ProjectNew()
	{
		
		PropertiesIO.loadProperties();

		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(constructScanArea(), BorderLayout.CENTER);

		add(constructHomeButtons(), BorderLayout.WEST);

		getContentPane().add(constructStatusBar(), BorderLayout.SOUTH);

		setTitle(appTitle);
		setJMenuBar(createMenuBar());
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/ac-icon.jpg")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		int xSize = ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 50);
		int ySize = ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 40);
		setSize(xSize, ySize);
		setMinimumSize(new Dimension(900, 650));
		//setState(Frame.MAXIMIZED_HORIZ);
		setVisible(true);
		setLocationRelativeTo(null);
		//setResizable(false);
		GreyStatusConfig.takeEffect();
		soundEnabled = false;
		setSoundMenu();
		//
		///
		////
		/////
		Log("", ActivityType.NORMAL, "Welcome!!!", getDelayTime());
		addWindowListener(new WindowAdapter() {
			public void windowOpened( WindowEvent e) {
				scanAreaTextField.requestFocus();
			}
			});
		try
		{
			if(helper.getConnection() != null)
			{
				Log("", ActivityType.CONNECTED, "Connected to Database Server!!!", getDelayTime());
			}
		}
		catch (Exception xcp)
		{
			Log("", ActivityType.ERROR, "Could not establish connection to Database Server! Check Connection Settings!", getDelayTime());
			xcp.printStackTrace();
		}
	}

	public JMenuBar createMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("File");
		JMenuItem menuItem1 = new JMenuItem("View Daily Report");
		menuItem1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new DailyReportTable();
					}
				});
			}
		});
		JMenuItem menuItemD = new JMenuItem("View Generel Report");
		menuItemD.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new ReportTable();

					}
				});
			}
		});
		JMenuItem menuItem2 = new JMenuItem("View the Report of a Device");
		menuItem2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new DeviceReportTable();
					}
				});
			}
		});
		JMenuItem menuItemA = new JMenuItem("View all Devices");
		menuItemA.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new AllHardware();
					}
				});
			}
		});
		JMenuItem menuItemB = new JMenuItem("View all Staff");
		menuItemB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new AllStaff().setVisible(true);
					}
				});
			}
		});
		JMenuItem menuItemC = new JMenuItem("View all Staff with their Devices");
		menuItemC.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new StaffHardware();
					}
				});
			}
		});
		miSound = new JMenuItem();
		miSound.setName("miSound");
		miSound.setText("Sound: On");
        miSound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                toggleSound();
            }
        });		
		JMenuItem menuItem3 = new JMenuItem("Exit");
		menuItem3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		menu1.add(menuItem1);
		menu1.add(menuItem2);
		menu1.add(menuItemD);
		menu1.add(menuItemA);
		menu1.add(menuItemB);
		menu1.add(menuItemC);
		menu1.add(miSound);
		menu1.add(menuItem3);
		JMenu menu2 = new JMenu("Edit");
		JMenuItem menuItem4 = new JMenuItem("Enroll a new Staff");
		menuItem4.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new NewAddStaff().setVisible(true);
					}
				});
			}
		});
		JMenuItem menuItem5 = new JMenuItem("Register a new Device");
		menuItem5.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new AddHardware().setVisible(true);
					}
				});
			}
		});
		JMenuItem menuItem6 = new JMenuItem("Assign an extra hardware to a Staff");
		menuItem6.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new AddStaffHardware().setVisible(true);
					}
				});
			}
		});
		JMenuItem menuItem7 = new JMenuItem("Update a Staff's Photo");
		menuItem7.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new NewUpdateStaff().setVisible(true);
						Log("", ActivityType.NORMAL, "To Update a Staffs photo, Type their Staff ID and hit enter!!!", getDelayTime());
					}
				});
			}
		});
		menu2.add(menuItem4);
		menu2.add(menuItem5);
		menu2.add(menuItem6);
		menu2.add(menuItem7);
		JMenu menu4 = new JMenu("Settings");
		JMenuItem newM = new JMenuItem("Connection Configuration");
		newM.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new GreyConnConfig().setVisible(true);
						Log("", ActivityType.WARNING, "Do not Temper with the database connection settings!!!", getDelayTime());
						Log("", ActivityType.WARNING, "Only edit if the IT Dept. has made changes to its hardware servers!!!", getDelayTime());
					}
				});
			}
		});
		JMenuItem newC = new JMenuItem("Status Bar Settings");
		newC.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						}
						catch (Exception xcp) {
						}
						new GreyStatusConfig();
						Log("", ActivityType.WARNING, "Increase Delay time for slow computers!", getDelayTime());
					}
				});
			}
		});
		JMenu menu5 = new JMenu("Help");
		JMenuItem newerM = new JMenuItem("About Grey");
		newerM.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new AboutGrey().setVisible(true);
					}
				});
			}
		});
		menu4.add(newM);
		menu4.add(newC);
		menu5.add(newerM);
		menuBar.add(menu1);
		menuBar.add(menu2);
		menuBar.add(menu4);
		menuBar.add(menu5);
		return menuBar;
	}
	
	public JPanel constructScanArea()
	{
		scanPanel = new JPanel();
		scanPanel.setLayout(new BorderLayout());
		//scanPanel.setPreferredSize(new Dimension(630, 400));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		scanAreaTextField = new JTextField(88);
		JButton xButton = new JButton("X");
		xButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				ownerLabel.setText("-");
				brandLabel.setText("-");
				barcodeLabel.setText("-");
				serialLabel.setText("-");
				lblPhoto.setIcon(null);
				signInButton.setEnabled(false);
				signOutButton.setEnabled(false);
				scanAreaTextField.requestFocus();
			}
		});
		JPanel devicePanel = new JPanel();
		devicePanel.setBorder(BorderFactory.createTitledBorder("Device Information"));
		devicePanel.setPreferredSize(new Dimension(950, 610));
		devicePanel.setLayout(new BorderLayout());
		JPanel nPanel = new JPanel();
		JPanel sPanel = new JPanel();
		nPanel.setPreferredSize(new Dimension(10, 200));
		mainPhotoPanel = new JPanel();
		lblPhoto = new JLabel();
		JPanel ownerPanel = new JPanel();
		ownerPanel.setPreferredSize(new Dimension(950, 30));
		ownerLabel = new JLabel("-");
		ownerPanel.add(ownerLabel);
		JPanel brandPanel = new JPanel();
		brandPanel.setPreferredSize(new Dimension(950, 30));
		brandLabel = new JLabel("-");
		brandPanel.add(brandLabel);
		JPanel barcodePanel = new JPanel();
		barcodePanel.setPreferredSize(new Dimension(950, 30));
		barcodeLabel = new JLabel("-");
		barcodePanel.add(barcodeLabel);
		JPanel serialPanel = new JPanel();
		serialPanel.setPreferredSize(new Dimension(950, 30));
		serialLabel = new JLabel("-");
		serialPanel.add(serialLabel);
		Font font = new Font("Courier", Font.BOLD, 14);
		ownerLabel.setFont(font);
		brandLabel.setFont(font);
		barcodeLabel.setFont(font);
		serialLabel.setFont(font);

		JPanel signInPanel = new JPanel();
		signInPanel.setPreferredSize(new Dimension(950, 30));
		signInButton = new JButton("Check-in");
		signInButton.setEnabled(false);
		signInPanel.add(signInButton);
		signInButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Direction();
				Log("", ActivityType.NORMAL, "Please select Staff Check-in Destination!!!", getDelayTime());
			}
		});
		JPanel signOutPanel = new JPanel();
		signOutPanel.setPreferredSize(new Dimension(950, 30));
		signOutButton = new JButton("Check-out");
		signOutButton.setEnabled(false);
		signOutPanel.add(signOutButton);
		signOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				new Destination();
				Log("", ActivityType.NORMAL, "Please select staff Check-out Destination!!!", getDelayTime());
			}
		});

        mainPhotoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Photo")); // NOI18N
        mainPhotoPanel.setName("mainPhotoPanel"); // NOI18N
		mainPhotoPanel.setSize(320, 240);
        lblPhoto.setText(""); // NOI18N
        lblPhoto.setName("lblPhoto"); // NOI18N

        javax.swing.GroupLayout mainPhotoPanelLayout = new javax.swing.GroupLayout(mainPhotoPanel);
        mainPhotoPanel.setLayout(mainPhotoPanelLayout);
        mainPhotoPanelLayout.setHorizontalGroup(
            mainPhotoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPhotoPanelLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(lblPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        mainPhotoPanelLayout.setVerticalGroup(
            mainPhotoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPhotoPanelLayout.createSequentialGroup()
                .addComponent(lblPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );
		
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(nPanel);
        nPanel.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup())
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(20, 20, 20))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(mainPhotoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mainPhotoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGap(56, 56, 56))
        );
		devicePanel.add(nPanel, BorderLayout.NORTH);
		sPanel.add(serialPanel);
		sPanel.add(barcodePanel);
		sPanel.add(brandPanel);
		sPanel.add(ownerPanel);
		sPanel.add(signInPanel);
		sPanel.add(signOutPanel);
		devicePanel.add(sPanel, BorderLayout.CENTER);
		mainPanel.add(scanAreaTextField, BorderLayout.CENTER);
		mainPanel.add(xButton, BorderLayout.EAST);
		scanPanel.add(mainPanel, BorderLayout.NORTH);
		scanPanel.add(devicePanel, BorderLayout.CENTER);
		scanAreaTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e)
			{
				//changed();
			}
			public void removeUpdate(DocumentEvent e)
			{
				//changed();
			}
			public void insertUpdate(DocumentEvent e)
			{
				changed();
			}
			public void changed()
			{
				if (scanAreaTextField.getText().length() == 0)
				{
					Log("", ActivityType.ERROR, nobarcodePlaceHolder, getDelayTime());
				}
				else
				{
					timerTask = new TimerTask() {

						@Override
						public void run() 
						{
							viewHardwareDetails();
							scanAreaTextField.setText(null);
						}
					};
					if (timer == null)
					{	
						timer = new Timer();
						timer.schedule(timerTask, 600);
					}
					else
					{
						timer.cancel();
						timer = new Timer();
						timer.schedule(timerTask, 600);
					}
				}
			}
		});
		return scanPanel;
	}

	public void viewHardwareDetails()
	  {
		serialLabel.setText("-");
		ownerLabel.setText("-");
		barcodeLabel.setText("-");
		brandLabel.setText("-");
		lblPhoto.setIcon(null);
		try
		{
		  Connection dbconnection = helper.getConnection();
		  CallableStatement cs = dbconnection.prepareCall("{call getEquipmentStaff(?)}");
		  cs.setString(1, scanAreaTextField.getText());
		  ResultSet result = cs.executeQuery();
		  while(result.next())
		  {
			  byte[] pLabel = result.getBytes("Image");
			  if (pLabel != null)
			  {
				lblPhoto.setIcon(new javax.swing.ImageIcon(result.getBytes("Image")));
			  }
			  if (pLabel == null)
			  {
				
			  }
			  serialLabel.setText(String.format("Serial Number : %s", result.getString("SerialNumber")));
			  barcodeLabel.setText(String.format("Barcode : %s", result.getString("Barcode")));
			  brandLabel.setText(String.format("Brand : %s", result.getString("Brand")));
			  ownerLabel.setText(String.format("In Possession Of : %s", result.getString("Staff In Possession")));
			  Log("", ActivityType.NORMAL, "Device Identity Confirmed!!! ", getDelayTime());
		  }
		  if (ownerLabel.getText() != "-" && ownerLabel.getText() != null)
		  {
			signInButton.setEnabled(true);
			signOutButton.setEnabled(true);
		  }
		  if (cs.executeUpdate() == 0)
		  {
			Log("", ActivityType.ERROR, "Device Identity Unconfirmed!!!", getDelayTime());
			JOptionPane.showMessageDialog( null, "This Device hasn't been registered in the Database!", "Error", JOptionPane.ERROR_MESSAGE);
			scanAreaTextField.requestFocus();
		  }
		}
		catch (SQLException e)
		{
		  dbconnection = null;
		  Log("", ActivityType.ERROR, "Could not establish connection to the Database Server! Tried 1 time(s) : Giving up...", getDelayTime());
		  e.printStackTrace();
		}
		catch (Exception xcp)
		{
			xcp.printStackTrace();
		}
	  }

    public JPanel constructStatusBar()
    {
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        sPaneActivityLogger = new javax.swing.JScrollPane();
        txtAActivityLogger = new javax.swing.JTextArea();
		txtAActivityLogger.setEditable(false);
        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        sPaneActivityLogger.setName("sPaneActivityLogger"); // NOI18N
        txtAActivityLogger.setRows(5);
        txtAActivityLogger.setName("txtAActivityLogger"); // NOI18N
        sPaneActivityLogger.setViewportView(txtAActivityLogger);
		
        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 380, Short.MAX_VALUE)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap(244, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(statusPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(sPaneActivityLogger, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(statusMessageLabel)
                            .addComponent(statusAnimationLabel))
                        .addGap(3, 3, 3))
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(statusPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(sPaneActivityLogger, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addContainerGap()))
		);
	return statusPanel;
	}


  public JPanel constructHomeButtons()
  {
	JPanel mainPanel = new JPanel();
    JPanel topPanel = new JPanel();
	mainPanel.setLayout(new BorderLayout());
	topPanel.setBorder(BorderFactory.createTitledBorder("Menu"));
    topPanel.setPreferredSize(new Dimension(240, 150));
    GridBagLayout layout = new GridBagLayout();
    topPanel.setLayout(layout);
	
	JButton hardwareButton = new JButton("Register Device");
	JButton staffButton = new JButton("Enroll Staff");
	JButton reportButton = new JButton("Daily Report");
	JButton otherButton = new JButton("Assign Extra Hardware To Staff");
	
    GridBagConstraints c = new GridBagConstraints();
    c.ipadx = 5;
    c.ipady = 5;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0;
    c.weighty = 0.0;
    c.gridx = 0;
    c.weightx = 1.0;
    c.gridx = 1;
    c.gridy = 0;
    topPanel.add(hardwareButton, c);
    c.gridy = 1;
    topPanel.add(staffButton, c);
    c.gridy = 2;
    topPanel.add(reportButton, c);
    c.gridy = 3;
    topPanel.add(otherButton, c);
    c.gridy = 4;
    c.weightx = 0;
	
		hardwareButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new AddHardware().setVisible(true);
					}
				});
			}
		});
		
		staffButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new NewAddStaff().setVisible(true);
					}
				});
			}
		});
		
		reportButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new DailyReportTable();
					}
				});
			}
		});

		otherButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new AddStaffHardware().setVisible(true);
					}
				});
			}
		});
	mainPanel.add(topPanel, BorderLayout.NORTH);
    return mainPanel;
  }

	public static boolean soundEnabled = true;
	
    public void toggleSound() 
	{
        soundEnabled = !soundEnabled;
        setSoundMenu();

    }

    public void setSoundMenu()
	{
        if(soundEnabled)
		{
            miSound.setText("Sound: On");
        } 
		else
		{
			if(miSound.getText() != null)
			{
				miSound.setText("Sound: Off");
			}
        }
    }

    public static void playSound(String sound)
	{
       if(soundEnabled)
	   {
            try
			{
                /*if(sound.equals("error"))
				{
                    new AePlayWave("Sound\\c826.wav").start();
                }*/
                if(sound.equals("display"))
				{
                    new AePlayWave( "Sound\\c826.wav" ).start();
                }
                /*if(sound.equals("warning"))
				{
                    new AePlayWave( "Sound\\c826.wav" ).start();
                }
                if(sound.equals("connected"))
				{
                    new AePlayWave( "Sound\\c826.wav" ).start();
                }*/
            } 
			catch(Exception e)
			{
				Log("", ActivityType.ERROR, "Sound error!!!", getDelayTime());
                e.printStackTrace();
            }
       }
    }

    public static void updateLog(final String msg, final int delay) {
        //scroll the text area to the bottom or  the viewer misses the show (Bumm)
        txtAActivityLogger.setCaretPosition(txtAActivityLogger.getDocument().getLength());
		/*if (actType == "NORMAL")
		{*/
			playSound("display");
		/*}
		if (actType == "WARNING")
		{
			playSound("warning");
		}
		if (actType == "ERROR")
		{
			playSound("error");
		}
		if (actType == "CONNECTED")
		{
			playSound("connected");
		}*/
        if(running){
            msgs = msgs + "\r" + msg;

        } else{
            running = true;
            SwingUtilities.invokeLater(new Runnable(){

                public void run(){
                    new Thread(){
                        public void run(){

                            char[] chars = new char[msg.length()];
                            chars = msg.toCharArray();
                            for(char c : chars){
                                txtAActivityLogger.append(Character.toString(c));
                                //txtAActivityLogger.repaint();
                                try{
                                    this.sleep(delay);
                                } catch(Exception e){
                                    System.out.println(e);
                                }
                            }
                            running = false;
                            if(!msgs.equals("")){
                                String showMsg = msgs;
                                msgs = "";
                                updateLog(showMsg, getDelayTime());
                            }

                        }
                    }.start();


                }
            }
            );
            txtAActivityLogger.setCaretPosition(txtAActivityLogger.getDocument().getLength());
        }
    }

class Direction extends JDialog
  {  
	public Direction()
	{
		
		getContentPane().add(constructMain(), BorderLayout.CENTER);

		setTitle("Select Direction");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/ac-icon.jpg")));
		pack();
		setMinimumSize(getSize());
		setSize(250, 150);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		try
		{
			if(helper.getConnection() != null)
			selectDirectionCall();
			scannedHardwareDirectionDropDown.setSelectedItem(null);		
		}
		catch (Exception xcp)
		{
			Log("", ActivityType.ERROR, "Could not establish connection to the Database Server! Tried 1 time(s) : Giving up...", getDelayTime());
			xcp.printStackTrace();
		}

	}

  private void selectDirectionCall()
  {
    try
    {
	  Connection dbconnection = helper.getConnection();
	  CallableStatement cs = dbconnection.prepareCall("{call getDirectionType()}");
	  ResultSet result = cs.executeQuery();
	  
      while (result.next())
      {
        scannedHardwareDirectionDropDown.addItem( String.format("%s", result.getString("Direction_ID")));
      }
    }
    catch (Exception e)
    {
	  Log("", ActivityType.ERROR, "Could not establish connection to the Database Server! Tried 1 time(s) : Giving up...", getDelayTime());
      e.printStackTrace();
    }
  }

private boolean signIn()
  {
    try
    {
	  Connection dbconnection = helper.getConnection();
	  CallableStatement cs = dbconnection.prepareCall("{call logEquipment(?,?,?,?,?)}");
	  cs.setString(1, ownerLabel.getText().split(" ")[4]);
	  cs.setString(2, ownerLabel.getText().split(" ")[5]);
	  cs.setString(3, ownerLabel.getText().split(" ")[6]);
	  cs.setString(4, serialLabel.getText().split(" ")[3]);
	  cs.setObject(5, scannedHardwareDirectionDropDown.getSelectedItem());
	  if (cs.executeUpdate() >= 1)
	  {
		Log("", ActivityType.NORMAL, "Check-in Successfull!!!", getDelayTime());
		setVisible(false);
		signInButton.setEnabled(false);
		signOutButton.setEnabled(false);
		ownerLabel.setText("-");
		brandLabel.setText("-");
		barcodeLabel.setText("-");
		serialLabel.setText("-");
		lblPhoto.setIcon(null);
		scanAreaTextField.requestFocus();
	  }
    }
    catch (Exception e)
    {
	  Log("", ActivityType.ERROR, "Could not establish connection to the Database Server! Tried 1 time(s) : Giving up...", getDelayTime());
      e.printStackTrace();
    }
	return true;
  }

	private JPanel constructMain()
	{
    JPanel topPanel = new JPanel();
    topPanel.setPreferredSize(new Dimension(10, 50));
    GridBagLayout layout = new GridBagLayout();
    topPanel.setLayout(layout);
	scannedHardwareDirectionDropDown = new JComboBox();
    JButton okButton = new JButton("Ok");

	okButton.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (scannedHardwareDirectionDropDown.getSelectedItem() == null)
			{
			  Toolkit.getDefaultToolkit().beep();
			  return;
			}
			else
			  {
				signIn();
				/*int count = 0;
				if (DailyReportTable.row != 0)
				{
					if (DailyReportTable.reportTable.getValueAt(count, 6) != "N/A" || DailyReportTable.reportTable.getValueAt(count, 6) != null)
					{
						signIn();
						count++;
					}
					if (DailyReportTable.reportTable.getValueAt(count, 6) == "N/A" || DailyReportTable.reportTable.getValueAt(count, 6) == null)
					{
						Log("", ActivityType.ERROR, "You have already signed in!!!", getDelayTime());
						setVisible(false);
						signInButton.setEnabled(false);
					}
				}
				if (DailyReportTable.row == 0)
				{
					signIn();
				}*/
			  }
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
    topPanel.add(new JLabel(scannedHardwareDirectionLabel, JLabel.RIGHT), c);
    c.weightx = 1.0;
    c.gridx = 1;
    c.gridy = 0;
    topPanel.add(scannedHardwareDirectionDropDown, c);
    c.gridy = 1;
    c.weightx = 0;
    topPanel.add(okButton, c);

    return topPanel;
	}
  }

class Destination extends JDialog
  { 
	public Destination()
	{
		
		getContentPane().add(constructMain(), BorderLayout.CENTER);
		
		setTitle("Select Destination");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/ac-icon.jpg")));
		pack();
		setMinimumSize(getSize());
		setSize(250, 150);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);

		try
		{
			if(helper.getConnection() != null)
			selectDestinationCall();
			scannedHardwareDestinationDropDown.setSelectedItem(null);		
		}
		catch (Exception xcp)
		{
			Log("", ActivityType.ERROR, "Could not establish connection to the Database Server! Tried 1 time(s) : Giving up...", getDelayTime());
			xcp.printStackTrace();
		}

	}

  private void selectDestinationCall()
  {
    try
    {
	  Connection dbconnection = helper.getConnection();
	  CallableStatement cs = dbconnection.prepareCall("{call getDestination()}");
	  ResultSet result = cs.executeQuery();
	  
      while (result.next())
      {
        scannedHardwareDestinationDropDown.addItem( String.format("%s", result.getString("Destination_ID")));
      }
    }
    catch (Exception e)
    {
	  Log("", ActivityType.ERROR, "Could not establish connection to the Database Server! Tried 1 time(s) : Giving up...", getDelayTime());
	  dbconnection = null;
      e.printStackTrace();
    }
  }

private boolean signOut()
  {
    try
    {
	  Connection dbconnection = helper.getConnection();
	  CallableStatement cs = dbconnection.prepareCall("{call logEquipmentOut(?,?)}");
	  cs.setString(1, serialLabel.getText().split(" ")[3]);
	  cs.setObject(2, scannedHardwareDestinationDropDown.getSelectedItem());
	  if (cs.executeUpdate() >= 1)
	  {
		Log("", ActivityType.NORMAL, "Check-out Successful!!! BYE!", getDelayTime());
		setVisible(false);
		signInButton.setEnabled(false);
		signOutButton.setEnabled(false);
		ownerLabel.setText("-");
		brandLabel.setText("-");
		barcodeLabel.setText("-");
		serialLabel.setText("-");
		lblPhoto.setIcon(null);
		scanAreaTextField.requestFocus();
	  }
	  else
	  {
		Log("", ActivityType.ERROR, "This Device has not been checked in!", getDelayTime());
		Log("", ActivityType.ERROR, "Please Check-in before you Check-out", getDelayTime());
		setVisible(false);
		signOutButton.setEnabled(false);
	  }
    }
    catch (Exception e)
    {
	  Log("", ActivityType.ERROR, "Could not establish connection to the Database Server! Tried 1 time(s) : Giving up...", getDelayTime());
	  dbconnection = null;
      e.printStackTrace();
    }
	return true;
  }

	private JPanel constructMain()
	{
    JPanel topPanel = new JPanel();
    topPanel.setPreferredSize(new Dimension(10, 50));
    GridBagLayout layout = new GridBagLayout();
    topPanel.setLayout(layout);
	scannedHardwareDestinationDropDown = new JComboBox();
    JButton okButton = new JButton("Ok");

	okButton.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (scannedHardwareDestinationDropDown.getSelectedItem() == null)
			{
			  Toolkit.getDefaultToolkit().beep();
			  return;
			}
			else
			  {
				signOut();
			  }
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
    topPanel.add(new JLabel(scannedHardwareDestinationLabel, JLabel.RIGHT), c);
    c.weightx = 1.0;
    c.gridx = 1;
    c.gridy = 0;
    topPanel.add(scannedHardwareDestinationDropDown, c);
    c.gridy = 1;
    c.weightx = 0;
    topPanel.add(okButton, c);

    return topPanel;
	}
}
}