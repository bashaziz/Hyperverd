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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.List;
import javax.imageio.ImageIO;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;

import java.text.SimpleDateFormat;

import javax.swing.filechooser.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
//import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.GroupLayout;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.Statement;

public class NewAddStaff extends JFrame
  {
  private static final String dbHost = "localhost";

  private static final String dbName = "qdl";

  private static final String dbUsername = "root";

  private static final String dbPassword = "password";

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

  //ADD STAFF THINGS....
  private static final String staffTitle = "Enroll Staff";

  private static final String labelStaffSubmit = "Submit";

  private static final String staffIdLabel = "* Staff ID : ";

  private static final String staffFirstNameLabel = "* First Name : ";

  private static final String staffLastNameLabel = "* Last Name : ";

  private static final String staffPracticeLabel = "* Practice : ";

  private static final String staffSexLabel = "* Sex : ";
  
  private static final String staffDeviceLabel = "* Device : ";
  
  private JComboBox staffPracticeDropDown;

  private JComboBox staffDeviceDropDown;

  private JTextField staffIdTextField;

  private JTextField staffFirstNameTextField;

  private JTextField staffLastNameTextField;

  private JComboBox staffSexDropDown;
  
  private Connection dbconnection = null;
  
  private JPanel statusBarPanel;

  private JPanel scanPanel;

  private JLabel statusBar;
  
  private JPanel mainPhotoPanel;
  
  private JLabel lblPhoto;
  
  private JButton btnCapturePhoto;
  
  private JButton btnUploadPhoto;
  
  private JButton staffSubmitButton;
  
  private byte[] photo;
  
  public String staffId = "";
  
  public CallableStatement cps;
  
    public static Player player = null;
    public CaptureDeviceInfo di = null;
    public MediaLocator ml = null;
    public Buffer buf = null;
    public Image img = null;
    public VideoFormat vf = null;
    public BufferToImage btoi = null;
    public ImagePanel imgpanel = null;
    public int captureMode = 0;
    public   Component comp= null;
    public Image photoImageTemp = null;
    int validationCounter = 0;
	
	private String filename;
	private byte[] person_image;
	private FileInputStream fis;
	private String path;
	
	BufferedImageBuilder BufferedImageBuilder = new BufferedImageBuilder();
	ConnectionHelper helper = new ConnectionHelper();

  public NewAddStaff()
	{
	
		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(constructMain(), BorderLayout.NORTH);

		setTitle(staffTitle);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/ac-icon.jpg")));
		pack();
		//setMinimumSize(getSize());
		setSize(600, 500);
		setResizable(false);
		//setLocationRelativeTo(null);
		
		try
		{
			if(helper.getConnection() != null)
			addStaffCall();
			staffIdTextField.setText(null);
			staffFirstNameTextField.setText(null);
			staffLastNameTextField.setText(null);
			staffSexDropDown.setSelectedItem(null);
			staffPracticeDropDown.setSelectedItem(null);
			staffDeviceDropDown.setSelectedItem(null);
			lblPhoto.setIcon(null);		
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

    public byte[] photoToByteArray(Image inImage) throws Exception {

            BufferedImage inBufferedImage = null;
            inBufferedImage = BufferedImageBuilder.toBufferedImage(inImage);

            RenderedImage inRenderedImage = (RenderedImage) inBufferedImage;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(inRenderedImage, "png", baos);

            if (baos != null) {

                return baos.toByteArray();

            } else {

                return null;
            }
    }
	
	
    public void preparePhoto(boolean isNew)
	{
        //prepare this participant for a trip to the moon
        if(isNew){
            //participant = new Participant();
        }

            byte[] ba = null;
            try{
            ImageIcon image = (ImageIcon)lblPhoto.getIcon();
            
            
                ba = photoToByteArray(image.getImage());
            } catch(Exception e){
                //System.out.println("set photo: " + e);

            }
            setPhoto(ba);
        

    }
	
    public void setPhoto(byte[] photo) 
	{
        this.photo = photo;
    }

    public byte[] getPhoto() {
        return photo;
    }

	
	
  private void newStaffSend()
  {
	if (staffIdTextField.getText().equals("") || staffFirstNameTextField.getText().equals("") || staffLastNameTextField.getText().equals("") || staffSexDropDown.getSelectedItem() == null || staffPracticeDropDown.getSelectedItem() == null || staffDeviceDropDown.getSelectedItem() == null || lblPhoto.getIcon() == null)
	{
	  Toolkit.getDefaultToolkit().beep();
	  ProjectNew.Log("", ProjectNew.ActivityType.WARNING, statusErrStaffRequiredFields, ProjectNew.getDelayTime());
	}

    try
    {
	  Connection dbconnection = helper.getConnection();
	  CallableStatement cs = dbconnection.prepareCall("{call addStaff(?,?,?,?,?,?)}");
      cs.setString(1, staffIdTextField.getText());
      cs.setString(2, staffFirstNameTextField.getText());
      cs.setString(3, staffLastNameTextField.getText());
      cs.setObject(4, staffSexDropDown.getSelectedItem());
      cs.setObject(5, staffPracticeDropDown.getSelectedItem().toString().split(" ")[0]);
	  cs.setBytes(6, getPhoto());
	  
	  CallableStatement cbs = dbconnection.prepareCall("{call addStaffEquipment(?, ?)}");
	  cbs.setString(1, staffIdTextField.getText());
	  cbs.setObject(2, staffDeviceDropDown.getSelectedItem().toString().split(" ")[3]);
	  
	  if (cs.executeUpdate() >= 1 && cbs.executeUpdate() >=1)
		{
			ProjectNew.Log("", ProjectNew.ActivityType.ERROR, "Staff has successfully been registered!!!", ProjectNew.getDelayTime());
			
			
			staffIdTextField.setText(null);
			staffFirstNameTextField.setText(null);
			staffLastNameTextField.setText(null);
			staffSexDropDown.setSelectedItem(null);
			staffPracticeDropDown.setSelectedItem(null);
			staffDeviceDropDown.setSelectedItem(null);
			lblPhoto.setIcon(null);
			setVisible(false);
			
			return;
		}
    }
	catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException d)
	{
		d.printStackTrace();
		ProjectNew.Log("", ProjectNew.ActivityType.ERROR, "A Staff with the same details already exists!!!", ProjectNew.getDelayTime());
		staffIdTextField.setText(null);
		staffFirstNameTextField.setText(null);
		staffLastNameTextField.setText(null);
		staffSexDropDown.setSelectedItem(null);
		staffPracticeDropDown.setSelectedItem(null);
		staffDeviceDropDown.setSelectedItem(null);
		lblPhoto.setIcon(null);
	}
    catch (SQLException e)
    {
	  ProjectNew.Log("", ProjectNew.ActivityType.ERROR, "Could not establish connection to the Database Server! Tried 1 time(s) : Giving up...", ProjectNew.getDelayTime());
      e.printStackTrace();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private void addStaffCall()
  {
    try
    {
	  Connection dbconnection = helper.getConnection();
	  CallableStatement cs4 = dbconnection.prepareCall("{call getGender()}");
	  ResultSet result4 = cs4.executeQuery();
	  
	  CallableStatement cs5 = dbconnection.prepareCall("{call getSerialNumber()}");
	  ResultSet result5 = cs5.executeQuery();
	  
	  CallableStatement cs6 = dbconnection.prepareCall("{call getPractice()}");
	  ResultSet result6 = cs6.executeQuery();
	  
      while (result4.next())
      {
        staffSexDropDown.addItem( String.format("%s", result4.getString("sex")));
      }
	  while (result5.next())
	  {
		staffDeviceDropDown.addItem(String.format("%s %s %s %s", result5.getString("Type_ID"), result5.getString("Brand_ID"), result5.getString("Model"), result5.getString("SerialNumber")));
	  }
	  while (result6.next())
	  {
        staffPracticeDropDown.addItem( String.format("%s - %s", result6.getString("Practice_ID"), result6.getString("Description")));
	  }
    }
    catch (SQLException e)
    {
      ProjectNew.Log("", ProjectNew.ActivityType.ERROR, "Could not establish connection to the Database Server! Tried 1 time(s) : Giving up...", ProjectNew.getDelayTime());
      e.printStackTrace();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

	private JPanel constructMain()
	{
		JPanel mPanel = new JPanel();
		mPanel.setLayout(new BorderLayout());
		JPanel nPanel = new JPanel();
		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(10, 220));
		nPanel.setPreferredSize(new Dimension(10, 250));
		mainPhotoPanel = new JPanel();
		lblPhoto = new JLabel();
		btnCapturePhoto = new JButton("Capture");
		btnUploadPhoto = new JButton("Upload");
        btnUploadPhoto.addMouseListener(new MouseListener() {
    	    public void mousePressed(MouseEvent e) {
	            //btnCapturePhoto.setForeground(new Color(0xffff00));
	    }

            public void mouseReleased(MouseEvent e) {}

            public void mouseEntered(MouseEvent e) {
	            //btnCapturePhoto.setForeground(new Color(0xffffff));
	    }

            public void mouseExited(MouseEvent e) {
		        //btnCapturePhoto.setForeground(new Color(0x000000));
	    }

            public void mouseClicked(MouseEvent evt) 
			{
                startPhotoUpload();
			}
        });
        btnCapturePhoto.setActionCommand("photo");
        btnCapturePhoto.addMouseListener(new MouseListener() {
    	    public void mousePressed(MouseEvent e) {
	            //btnCapturePhoto.setForeground(new Color(0xffff00));
	    }

            public void mouseReleased(MouseEvent e) {}

            public void mouseEntered(MouseEvent e) {
	            //btnCapturePhoto.setForeground(new Color(0xffffff));
	    }

            public void mouseExited(MouseEvent e) {
		        //btnCapturePhoto.setForeground(new Color(0x000000));
	    }

            public void mouseClicked(MouseEvent evt) 
			{
                btnCapturePhotoMouseClicked(evt);
			}
        });
        //btnCapturePhoto.setColor1(new Color(0x96d8fa));
        //btnCapturePhoto.setColor2(new Color(0x6accf7));
        //btnCapturePhoto.setColor3(new Color(0x5faffe));
        //btnCapturePhoto.setColor4(new Color(0x63d8fc));
	
        mainPhotoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Photo")); // NOI18N
        mainPhotoPanel.setName("mainPhotoPanel"); // NOI18N

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
                                    .addComponent(btnCapturePhoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnUploadPhoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addComponent(btnCapturePhoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUploadPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56))
        );

        //getAccessibleContext().setAccessibleName(resourceMap.getString("Form.AccessibleContext.accessibleName")); // NOI18N

    GridBagLayout layout1 = new GridBagLayout();
    topPanel.setLayout(layout1);

	staffIdTextField = new JTextField(50);	
    staffFirstNameTextField = new JTextField(50);
    staffLastNameTextField = new JTextField(50);
    staffSexDropDown = new JComboBox();
    staffPracticeDropDown = new JComboBox();
	staffDeviceDropDown = new JComboBox();

    JButton staffSubmitButton = new JButton(labelStaffSubmit);

	staffSubmitButton.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			preparePhoto(true);
			newStaffSend();
		}
	});

    GridBagConstraints c = new GridBagConstraints();
    c.ipadx = 5;
    c.ipady = 5;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 6;
    c.weighty = 4.0;
    c.gridx = 0;
    c.gridy = 0;
    topPanel.add(new JLabel(staffIdLabel, JLabel.RIGHT), c);
    c.gridy = 1;
    topPanel.add(new JLabel(staffFirstNameLabel, JLabel.RIGHT), c);
    c.gridy = 2;
    topPanel.add(new JLabel(staffLastNameLabel, JLabel.RIGHT), c);
    c.gridy = 3;
    topPanel.add(new JLabel(staffSexLabel, JLabel.RIGHT), c);
    c.gridy = 4;
    topPanel.add(new JLabel(staffPracticeLabel, JLabel.RIGHT), c);
	c.gridy = 5;
	topPanel.add(new JLabel(staffDeviceLabel, JLabel.RIGHT), c);
    c.weightx = 8.0;
    c.gridx = 2;
    c.gridy = 0;
    topPanel.add(staffIdTextField, c);
    c.gridy = 1;
    topPanel.add(staffFirstNameTextField, c);
    c.gridy = 2;
    topPanel.add(staffLastNameTextField, c);
    c.gridy = 3;
    topPanel.add(staffSexDropDown, c);
    c.gridy = 4;
    topPanel.add(staffPracticeDropDown, c);
    c.gridy = 5;
	topPanel.add(staffDeviceDropDown, c);
	c.gridy = 6;
    topPanel.add(staffSubmitButton, c);

	mPanel.add(nPanel, BorderLayout.NORTH);
	mPanel.add(topPanel, BorderLayout.SOUTH);
	//pack();
    return mPanel;
	}
	
    private void btnCapturePhotoMouseClicked(java.awt.event.MouseEvent evt) 
	{
		PhotoCapture pc = new PhotoCapture(this);
		pc.setVisible(true);
    }

    public void startCamera()
    {
       
    }

    public void startPhotoUpload() 
    {                                         
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter fnef = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "bmp", "png");
        chooser.setFileFilter(fnef);
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        path = f.getPath();
        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(190, 158, Image.SCALE_SMOOTH);;
        lblPhoto.setIcon(new ImageIcon(scaled));
    }
	
    class ImagePanel extends Panel
    {
        public Image myimg = null;

        public ImagePanel()
        {
            setLayout(null);
            setSize(200, 150);
        }

        public void setImage(Image img)
        {
            this.myimg = img;
            repaint();
        }

        public void paint(Graphics g)
        {
            if (myimg != null)
            {
                g.drawImage(myimg, 0, 0, this);
            }
        }
    }

    public void startPhotoCamera() {
        ProjectNew.Log("", ProjectNew.ActivityType.NORMAL, "Starting Camera...", ProjectNew.getDelayTime());
        imgpanel = new ImagePanel();

        String str1 = "vfw:Logitech USB Video Camera:0";
        String str2 = "vfw:Microsoft WDM Image Capture (Win32):0";
        di = CaptureDeviceManager.getDevice(str1);
        ml = new MediaLocator("vfw://0");

        try
        {
            player = Manager.createRealizedPlayer(ml);
            player.start();

            if ((comp = player.getVisualComponent()) != null)
            {
               mainPhotoPanel.add(comp);
               comp.setSize(mainPhotoPanel.getWidth(),mainPhotoPanel.getHeight());
            }

        }
        catch (Exception ex)
        {
			ProjectNew.Log("", ProjectNew.ActivityType.ERROR, "Camera Error!!!", ProjectNew.getDelayTime());
            //System.out.println(ex.toString());


        }


    }

    public void captureImage(Image capturedImg) {

        // show the image

        photoImageTemp = capturedImg;

        int imageWidth = (int) (photoImageTemp.getWidth(null) * 0.6);
        int imageHeight = (int) (photoImageTemp.getHeight(null)* 0.6);

        lblPhoto.setIcon(new ImageIcon(capturedImg.getScaledInstance(imageWidth,imageHeight,1)));

        //updateDrawableRect(lblCapturePhoto.getWidth(), lblCapturePhoto.getHeight());
        lblPhoto.repaint();

    }
  }
