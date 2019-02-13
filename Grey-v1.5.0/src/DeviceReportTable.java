import javax.swing.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.sql.Connection;
import java.sql.Statement;

public class DeviceReportTable extends JFrame
{
	private Connection dbconnection = null;	  
	public JPanel scanPanel;
	public JLabel statusBar;	 
    private JPanel contentPane;
    private JLabel gradesLabel;
    private javax.swing.JTable reportTable;
	private DefaultTableModel reportModel;
    private JScrollPane scroll;
    private JCheckBox showPrintDialogBox;
    private JCheckBox interactiveBox;
    private JCheckBox fitWidthBox;
    private JButton printButton;
	private JTextField newTextField;
	private JButton go;
	public static int row = 0;
    protected JCheckBox headerBox;
    protected JCheckBox footerBox;
    protected JTextField headerField;
    protected JTextField footerField;
	public ResultSet result1;
	public String text;
	ConnectionHelper helper = new ConnectionHelper();
	
	public DeviceReportTable()
	{
		
		getContentPane().add(constructMain(), BorderLayout.CENTER);
		
		getContentPane().add(constructStatusBar(), BorderLayout.SOUTH);

		setTitle("Type in S/N of Device!");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/ac-icon.jpg")));
		pack();
		setMinimumSize(getSize());
		setSize(250, 100);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		newTextField.setText(null);
	}

	private JPanel constructMain()
	{
    JPanel topPanel = new JPanel();
    topPanel.setPreferredSize(new Dimension(10, 50));
    GridBagLayout layout = new GridBagLayout();
    topPanel.setLayout(layout);
	newTextField = new JTextField();

	newTextField.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (newTextField.getText() == null)
			{
			  Toolkit.getDefaultToolkit().beep();
			  setStatusMessage("Type in the S/N of your device");
			  return;
			}
			else
			  {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new ViewDeviceReportTable().setVisible(true);
						setVisible(false);
					}
				});
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
    topPanel.add(new JLabel("S/N :", JLabel.RIGHT), c);
    c.weightx = 1.0;
    c.gridx = 1;
    c.gridy = 0;
    topPanel.add(newTextField, c);
    c.gridy = 1;
    c.weightx = 0;

    return topPanel;
	}
    private void setStatusMessage(String msg)
    {
      statusBar.setText(" " + msg);
    }

    private JLabel constructStatusBar()
    {
      statusBar = new JLabel();
      statusBar.setPreferredSize(new Dimension(10, 23));
      setStatusMessage("Type in the S/N of your device");
      return statusBar;
    }


class ViewDeviceReportTable extends JFrame {

    public ViewDeviceReportTable() {
        super("VIEW REPORT");
		String text = newTextField.getText();
    try
    {
	  dbconnection = helper.getConnection();
	  CallableStatement cs = dbconnection.prepareCall("{call getEquipmentReport(?)}");
	  cs.setString(1, text);
	  result1 = cs.executeQuery();
	  result1.last();
	  row = result1.getRow();	
	  result1.beforeFirst();
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
        gradesLabel = new JLabel("Device Report");
        gradesLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		String col[] = {"Staff ID",
						"Full Name",
						"S/N of Device",
						"IN",
						"IN(Date)",
						"IN(Time)",
						"OUT",
						"OUT(Date)",
						"OUT(Time)"};
						
	  reportModel = new DefaultTableModel(col, row)
	  {
	  
	  };		
		reportTable = new JTable(reportModel);
        reportTable.setFillsViewportHeight(true);
        reportTable.setRowHeight(24);

        scroll = new JScrollPane(reportTable);
	int count = 0;
    try
    {
	  dbconnection = helper.getConnection();
	  CallableStatement cs = dbconnection.prepareCall("{call getEquipmentReport(?)}");
	  cs.setString(1, text);
	  result1 = cs.executeQuery();
      while (result1.next())
      {			
		reportTable.setValueAt( result1.getString("Staff_ID"), count, 0);
		reportTable.setValueAt( String.format("%s %s", result1.getString("FirstName"), result1.getString("SurName")), count, 1);
		reportTable.setValueAt( String.format("%s ", result1.getString("SerialNumber")), count, 2 );
		reportTable.setValueAt( String.format("%s", result1.getString("Direction_ID")), count, 3 );
		reportTable.setValueAt( String.format("%s", result1.getDate("LoginTime")), count, 4 );
		reportTable.setValueAt( String.format("%s", result1.getTime("LoginTime")), count, 5 );
		reportTable.setValueAt( String.format("%s", result1.getString("Destination_ID")), count, 6 );
		reportTable.setValueAt( String.format("%s", result1.getDate("LogoutTime")), count, 7 );
		reportTable.setValueAt( String.format("%s", result1.getTime("LogoutTime")), count, 8 );
		count++;
      }
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
        String tooltipText;

        tooltipText = "Include a page header";
        headerBox = new JCheckBox("Header:", true);
        headerBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                headerField.setEnabled(headerBox.isSelected());
            }
        });
        headerBox.setToolTipText(tooltipText);
        tooltipText = "Page Header (Use {0} to include page number)";
        headerField = new JTextField("Device Report");
        headerField.setToolTipText(tooltipText);

        tooltipText = "Include a page footer";
        footerBox = new JCheckBox("Footer:", true);
        footerBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                footerField.setEnabled(footerBox.isSelected());
            }
        });
        footerBox.setToolTipText(tooltipText);
        tooltipText = "Page Footer (Use {0} to Include Page Number)";
        footerField = new JTextField("Page {0}");
        footerField.setToolTipText(tooltipText);

        tooltipText = "Show the Print Dialog Before Printing";
        showPrintDialogBox = new JCheckBox("Show print dialog", true);
        showPrintDialogBox.setToolTipText(tooltipText);
        showPrintDialogBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!showPrintDialogBox.isSelected()) {
                    JOptionPane.showMessageDialog(
                        ViewDeviceReportTable.this,
                        "If the Print Dialog is not shown,"
                            + " the default printer is used.",
                        "Printing Message",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        
        tooltipText = "Keep the GUI Responsive and Show a Status Dialog During Printing";
        interactiveBox = new JCheckBox("Interactive (Show status dialog)", true);
        interactiveBox.setToolTipText(tooltipText);
        interactiveBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!interactiveBox.isSelected()) {
                    JOptionPane.showMessageDialog(
                        ViewDeviceReportTable.this,
                        "If non-interactive, the GUI is fully blocked"
                            + " during printing.",
                        "Printing Message",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        tooltipText = "Shrink the Table to Fit the Entire Width on a Page";
        fitWidthBox = new JCheckBox("Fit width to printed page", true);
        fitWidthBox.setToolTipText(tooltipText);

        tooltipText = "Print the Table";
        printButton = new JButton("Print");
        printButton.setToolTipText(tooltipText);
        
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                printreportTable();
            }
        });

        contentPane = new JPanel();
        addComponentsToContentPane();
		setContentPane(contentPane);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/ac-icon.jpg")));
        setSize(750, 650);
		count = 0;
		while (reportTable.getValueAt(count, 6) == null || reportTable.getValueAt(count, 6) == "null")
		{
			reportTable.setValueAt("N/A", count, 6);
			reportTable.setValueAt("N/A", count, 7);
			reportTable.setValueAt("N/A", count, 8);
			count++;
		}
		if (row != 0)
		{
			setVisible(true);
		}
		if (row == 0)
		{	
			setVisible(true);
			setVisible(false);
			ProjectNew.Log("", ProjectNew.ActivityType.ERROR, "This device has never been Checked-in!!!", ProjectNew.getDelayTime());
		}
    }

    private void addComponentsToContentPane() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Printing"));

        GroupLayout bottomPanelLayout = new GroupLayout(bottomPanel);
        bottomPanel.setLayout(bottomPanelLayout);
        bottomPanelLayout.setHorizontalGroup(
            bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(headerBox)
                    .addComponent(footerBox))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(footerField)
                    .addComponent(headerField, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addComponent(fitWidthBox)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(printButton))
                    .addGroup(bottomPanelLayout.createSequentialGroup()
                        .addComponent(showPrintDialogBox)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(interactiveBox)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bottomPanelLayout.setVerticalGroup(
            bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addGroup(bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(headerBox)
                    .addComponent(headerField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(interactiveBox)
                    .addComponent(showPrintDialogBox))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(footerBox)
                    .addComponent(footerField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(fitWidthBox)
                    .addComponent(printButton))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                    .addComponent(gradesLabel)
                    .addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gradesLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }

    private void printreportTable() {

        MessageFormat header = null;
        
        if (headerBox.isSelected()) {
            header = new MessageFormat(headerField.getText());
        }

        MessageFormat footer = null;
        
        if (footerBox.isSelected()) {
            footer = new MessageFormat(footerField.getText());
        }

        boolean fitWidth = fitWidthBox.isSelected();
        boolean showPrintDialog = showPrintDialogBox.isSelected();
        boolean interactive = interactiveBox.isSelected();

        JTable.PrintMode mode = fitWidth ? JTable.PrintMode.FIT_WIDTH
                                         : JTable.PrintMode.NORMAL;

        try {
            boolean complete = reportTable.print(mode, header, footer,
                                                 showPrintDialog, null,
                                                 interactive, null);

            if (complete) {
                JOptionPane.showMessageDialog(this,
                                              "Printing Complete",
                                              "Printing Result",
                                              JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                                              "Printing Cancelled",
                                              "Printing Result",
                                              JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(this,
                                          "Printing Failed: " + pe.getMessage(),
                                          "Printing Result",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }
}
}