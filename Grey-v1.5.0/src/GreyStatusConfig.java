import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import java.util.*;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class GreyStatusConfig extends JDialog
{
	public static JPanel mainPanel;
	
	public static final String colorLabel = " Background Color :";
	
	public static final String color1Label = " Foreground Color :";
	
	public static final String Delay = " Delay Time(ms):";
	
	public static final String Save = "Save New Setttings";
	
	public static final String Default = "Default Settings";
	
	public static JTextField txtDelay;
	
	public static JComboBox cmbColor;
	
	public static JComboBox cmbColor1;
	
	public static JButton saveButton;
	
	public static JButton defaultButton;
	
	public static String BG_COLOR = "background";
	
	public static String FG_COLOR = "foreground";
	
	public static String INT_DELAY = "delay";
	
	public static int dlt;
	
	Object[] bColors = 
	{
		"BLACK", 
		"DARK BLUE", 
		"GREY",
	};
	
	Object[] fColors = 
	{
		"LIGHT GREEN",
		"YELLOW", 
		"CYAN",
	};
	
	public static void refresh() {
		if (PropertiesIO.readValue(BG_COLOR) == null) {
			PropertiesIO.writeProperty(BG_COLOR, "");
		}
		else {
			cmbColor.setSelectedItem(PropertiesIO.readValue(BG_COLOR));
		}
		
		if (PropertiesIO.readValue(FG_COLOR) == null) {
			PropertiesIO.writeProperty(FG_COLOR, "");
		}
		else {
			cmbColor1.setSelectedItem(PropertiesIO.readValue(FG_COLOR));
		}
		
		if (PropertiesIO.readValue(INT_DELAY) == null) {
			PropertiesIO.writeProperty(INT_DELAY, "");
		}
		else {
			txtDelay.setText(PropertiesIO.readValue(INT_DELAY));
		}
	}
	public GreyStatusConfig()
	{
		PropertiesIO.loadProperties();
		
		getContentPane().setLayout(new BorderLayout());	
		getContentPane().add(constructMain(), BorderLayout.CENTER);	
		setTitle("Status Bar Settings");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/ac-icon.jpg")));
		setSize(400, 150);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		refresh();
	}

	public static final String getBackgroundColor() {
		return PropertiesIO.readValue(BG_COLOR);
	}
		
	public void setBackgroundColor(String backgroundcolor)
	{
		PropertiesIO.writeProperty(BG_COLOR, backgroundcolor);
	}
		
	public static final String getForegroundColor() {
		return PropertiesIO.readValue(FG_COLOR);
	}
		
	public void setForegroundColor(String foregroundcolor) {
		PropertiesIO.writeProperty(FG_COLOR, foregroundcolor);
	}
		
	public static final String getDelay() {
		return PropertiesIO.readValue(INT_DELAY);
	}
		
	public void setDelayTime(String delaytime) {
		PropertiesIO.writeProperty(INT_DELAY, delaytime);
	}
	
	public static int findDelayTime() {
		String dl = getDelay();
		int dlt = Integer.parseInt(dl);
		return dlt;
	}
	
	public JPanel constructMain()
	{
		mainPanel = new JPanel();
		txtDelay = new JTextField(10);
		cmbColor = new JComboBox(bColors);// Background color
		cmbColor1 = new JComboBox(fColors);// Forground Color

		saveButton = new JButton(Save);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveConfiguration();
			}
		});
		defaultButton = new JButton(Default);
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				saveDefaultConfiguration();
			}
		});
		GridBagLayout layout = new GridBagLayout();
		mainPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.ipady = 5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(new JLabel(colorLabel, JLabel.LEFT), c);
		c.gridy = 1;
		mainPanel.add(new JLabel(color1Label, JLabel.LEFT), c);
		c.gridy = 2;
		mainPanel.add(new JLabel(Delay, JLabel.LEFT), c);
		c.gridy = 3;
		mainPanel.add(defaultButton, c);
		
		c.weightx = 1;
		c.gridx = 1;
		c.gridy = 0;
		mainPanel.add(cmbColor, c);
		c.gridy = 1;
		mainPanel.add(cmbColor1, c);
		c.gridy = 2;
		mainPanel.add(txtDelay, c);
		c.gridy = 3;
		mainPanel.add(saveButton, c);
		
		
		
		return mainPanel;
	}
	
/// UI Done
/// Functions : After Iftar!!!!
	
	public boolean saveConfiguration() {			
		int dlt = Integer.parseInt(txtDelay.getText());
		if ( dlt > 10 && dlt < 100 ) {
			if (cmbColor.getSelectedItem().toString().trim().equalsIgnoreCase("") == false)
				setBackgroundColor(cmbColor.getSelectedItem().toString());
				
			if (cmbColor1.getSelectedItem().toString().trim().equalsIgnoreCase("") == false)
				setForegroundColor(cmbColor1.getSelectedItem().toString());
				
			if (txtDelay.getText().trim().equalsIgnoreCase("") == false)
				setDelayTime(txtDelay.getText());
			takeEffect();
			ProjectNew.Log("", ProjectNew.ActivityType.NORMAL, "New Status Bar settings saved and applied!", ProjectNew.getDelayTime());
		}	
		else
			ProjectNew.Log("", ProjectNew.ActivityType.WARNING, "Delay time cannot go below 10 milli seconds or exceed 100 milli seconds!", ProjectNew.getDelayTime());
		return true;
	}
	
	public boolean saveDefaultConfiguration() {
		setBackgroundColor("DARK BLUE");
		setForegroundColor("CYAN");
		setDelayTime("20");
		takeEffect();
		refresh();
		ProjectNew.Log("", ProjectNew.ActivityType.NORMAL, "Status Bar settings has been reverted to Default Settings!", ProjectNew.getDelayTime());
		return true;
	}
	
	public static boolean takeEffect()
	{
		String bg = getBackgroundColor();
		String fg = getForegroundColor();

		if (bg.equals("DARK BLUE"))
			ProjectNew.txtAActivityLogger.setBackground(new Color(336699)); // NOI18N
		if (bg.equals("BLACK"))
			ProjectNew.txtAActivityLogger.setBackground(new Color(0, 0, 0));
		if (bg.equals("GREY"))
			ProjectNew.txtAActivityLogger.setBackground(new Color(64, 64, 64));
		if (fg.equals("LIGHT GREEN"))
			ProjectNew.txtAActivityLogger.setForeground(new Color(102, 255, 102)); // NOI18N
		if (fg.equals("YELLOW"))
			ProjectNew.txtAActivityLogger.setForeground(new Color(204, 204, 0));
		if (fg.equals("CYAN"))
			ProjectNew.txtAActivityLogger.setForeground(new Color(0, 255, 255));
		return true;
	}
		
	public static void main (String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				catch (Exception xcp) {}
				new GreyStatusConfig().setVisible(true);
			}
		});
	}
}