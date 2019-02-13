//package Grey;

import java.awt.Desktop.Action;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import java.awt.event.*;
import java.awt.*;

public class GreyConnConfig extends javax.swing.JDialog 
{
    
    public static final String DB_HOST = "host";
    public static final String DB_PORT = "port";
    public static final String DB_PASSWORD = "password";
    public static final String DB_USERNAME = "username";
    public static final String DB_DATABASE = "database";
    
    public GreyConnConfig() 
	{
        initComponents();
        
        PropertiesIO.loadProperties();
        
        if(PropertiesIO.readValue(DB_HOST) == null)
		{
            PropertiesIO.writeProperty(DB_HOST, "");
        }else
		{
            txtHostName.setText(PropertiesIO.readValue(DB_HOST));
        }
        
        if(PropertiesIO.readValue(DB_PORT) == null)
		{
            PropertiesIO.writeProperty(DB_PORT, "");
        }else
		{
            txtPortNumber.setText(PropertiesIO.readValue(DB_PORT));
        }
        
        if(PropertiesIO.readValue(DB_PASSWORD) == null)
		{
            PropertiesIO.writeProperty(DB_PASSWORD, "");
        }else
		{
            txtPassword.setText(PropertiesIO.readValue(DB_PASSWORD));
        }
        
        if(PropertiesIO.readValue(DB_USERNAME) == null)
		{
            PropertiesIO.writeProperty(DB_USERNAME, "");
        }else
		{
            txtUsername.setText(PropertiesIO.readValue(DB_USERNAME));
        }
        
        if(PropertiesIO.readValue(DB_DATABASE) == null)
		{
            PropertiesIO.writeProperty(DB_DATABASE, "");
        }else
		{
            txtDatabase.setText(PropertiesIO.readValue(DB_DATABASE));
        }
        
    }
	
    public String getHostName(){
        return PropertiesIO.readValue(DB_HOST);
    }
    
    public void setHostName(String hostName){
        PropertiesIO.writeProperty(DB_HOST, hostName);
    }
    
    public String getPortNumber(){
        return PropertiesIO.readValue(DB_PORT);
    }
    
    public void setPortNumber(String portNumber){
        PropertiesIO.writeProperty(DB_PORT, portNumber);
    }
    
    public String getPassword(){
        return PropertiesIO.readValue(DB_PASSWORD);
    }
    
    public void setPassword(String password){
        PropertiesIO.writeProperty(DB_PASSWORD, password);
    }
    
    public String getUsername(){
        return PropertiesIO.readValue(DB_USERNAME);
    }
    
    public void setUsername(String password){
        PropertiesIO.writeProperty(DB_USERNAME, password);
    }
    
    public String getDatabase(){
        return PropertiesIO.readValue(DB_DATABASE);
    }
    
    public void setDatabase(String database){
        PropertiesIO.writeProperty(DB_DATABASE, database);
    }
    
    private void initComponents() 
	{

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtHostName = new javax.swing.JTextField();
        txtPortNumber = new javax.swing.JTextField();
        txtUsername = new javax.swing.JTextField();
        txtDatabase = new javax.swing.JTextField();
        btnClose = new javax.swing.JButton();
        txtTestConn = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        depthButtonGlass1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Connection Configuration"); 
        setName("frmConfigBox");
		setSize(300, 400);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/ac-icon.jpg")));
		setLocationRelativeTo(null);
        setResizable(false);
		
        jLabel1.setText("Host");
        jLabel1.setName("jLabel1");

        jLabel2.setText("PortNumber");
        jLabel2.setName("jLabel2");

        jLabel3.setText("Username");
        jLabel3.setName("jLabel3");

        jLabel4.setText("Password");
        jLabel4.setName("jLabel4");

        jLabel5.setText("Database");
        jLabel5.setName("jLabel5");

        txtHostName.setText("HostName");
        txtHostName.setName("txtHostName");

        txtPortNumber.setText("PortNumber");
        txtPortNumber.setName("txtPortNumber");

        txtUsername.setText("Username");
        txtUsername.setName("txtUsername");

        txtDatabase.setText("Database");
        txtDatabase.setName("txtDatabase");

        btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
			}
		});
        btnClose.setText("Close"); 
        btnClose.setName("btnClose"); 

        txtTestConn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				testConn();
			}
		});
        txtTestConn.setText("Test Connection"); 
        txtTestConn.setName("txtTestConn"); 

        txtPassword.setText("Password");
        txtPassword.setName("txtPassword");

        depthButtonGlass1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				saveConfiguration();
			}
		});
        depthButtonGlass1.setText("Save");
        depthButtonGlass1.setName("depthButtonGlass1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(depthButtonGlass1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnClose)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTestConn))
                    .addComponent(txtHostName, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPortNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtDatabase, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtUsername, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                        .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtHostName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtPortNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnClose)
                        .addComponent(txtTestConn))
                    .addComponent(depthButtonGlass1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }

    public void saveConfiguration() {
        if(txtHostName.getText().trim().equalsIgnoreCase("") == false)
            setHostName(txtHostName.getText());

        if(txtPortNumber.getText().trim().equalsIgnoreCase("") == false)
            setPortNumber(txtPortNumber.getText());

        if(txtUsername.getText().trim().equalsIgnoreCase("") == false)
            setUsername(txtUsername.getText());

        if(txtPassword.getText().trim().equalsIgnoreCase("") == false)
            setPassword(txtPassword.getText());

        if(txtDatabase.getText().trim().equalsIgnoreCase("") == false)
            setDatabase(txtDatabase.getText());

    }

    public void testConn() {
         try
        {
			if(testConnection())
			{
					String msg = "Connection successful.";
						JOptionPane.showMessageDialog(null,msg,"Database Connection",JOptionPane.INFORMATION_MESSAGE);
						ProjectNew.Log("", ProjectNew.ActivityType.CONNECTED, "Connected to Database Server!!!", ProjectNew.getDelayTime());
			}else
			{
				String msg = "Connection failed";
				JOptionPane.showMessageDialog(null,msg,"Database Connection",JOptionPane.ERROR_MESSAGE);
			}
        }
		catch(Exception ex)
		{
            ProjectNew.Log("", ProjectNew.ActivityType.ERROR, ex.toString(), ProjectNew.getDelayTime());
        }
    }

    public void close() {
        this.dispose();
    }
	
     public boolean testConnection(){
        return new ConnectionHelper().testConnection();
    }
 
    private javax.swing.JButton btnClose;
    private javax.swing.JButton depthButtonGlass1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txtDatabase;
    private javax.swing.JTextField txtHostName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPortNumber;
    private javax.swing.JButton txtTestConn;
    private javax.swing.JTextField txtUsername;
    
}
