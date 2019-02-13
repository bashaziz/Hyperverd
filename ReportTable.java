//package Grey;

import javax.swing.*;
import javax.swing.table.*;
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

public class ReportTable extends JFrame {

  private Connection dbconnection = null;
  
  private ResultSet result2;
  
    private JPanel contentPane;
    private JLabel gradesLabel;
    private JTable reportTable;
	private DefaultTableModel reportModel;
    private JScrollPane scroll;
    private JCheckBox showPrintDialogBox;
    private JCheckBox interactiveBox;
    private JCheckBox fitWidthBox;
    private JButton printButton;

    protected JCheckBox headerBox;
    protected JCheckBox footerBox;
    protected JTextField headerField;
    protected JTextField footerField;
	ConnectionHelper helper = new ConnectionHelper();
	
  private void viewReport()
  {
    try
    {
	  Connection dbconnection = helper.getConnection();
	  CallableStatement cs = dbconnection.prepareCall("{call getMovementLog()}");
	  ResultSet result = cs.executeQuery();
	  int count = 0;  
      while (result.next())
      {	        
		reportTable.setValueAt( result.getString("Staff_ID"), count, 0);
		reportTable.setValueAt( String.format("%s %s", result.getString("FirstName"), result.getString("SurName")), count, 1);
		reportTable.setValueAt( result.getString("SerialNumber"), count, 2 );
		reportTable.setValueAt( result.getString("Direction_ID"), count, 3 );
		reportTable.setValueAt( result.getDate("LoginTime"), count, 4 );
		reportTable.setValueAt( String.format("%s", result.getTime("LoginTime")), count, 5 );
		reportTable.setValueAt( result.getString("Destination_ID"), count, 6 );
		reportTable.setValueAt( result.getDate("LogoutTime"), count, 7 );
		reportTable.setValueAt( String.format("%s", result.getTime("LogoutTime")), count, 8 );
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
  }
  
    public ReportTable() {
        super("VIEW REPORT");

		int row = 0;
		try
		{
			Connection dbconnection = helper.getConnection();
			CallableStatement cs = dbconnection.prepareCall("{call getMovementLog()}");
			ResultSet result1 = cs.executeQuery();
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
		
        gradesLabel = new JLabel("General Report");
        gradesLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		String col[] = {"Staff ID", "Full Name", "S/N of Device", "IN", "IN(Date)", "IN(Time)", "OUT", "OUT(Date)", "OUT(Time)"};
        reportModel = new DefaultTableModel(col, row) 
		{
			public boolean isCellEditable(int rowIndex, int columnIndex)
			{
				return false;
			}
		};
		reportTable = new JTable(reportModel);
        reportTable.setFillsViewportHeight(true);
        reportTable.setRowHeight(24);

        scroll = new JScrollPane(reportTable);

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
        headerField = new JTextField("General Report");
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
                        ReportTable.this,
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
                        ReportTable.this,
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
        setSize(750, 650);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/ac-icon.jpg")));
        try
		{
			if (helper.getConnection() != null)
				viewReport();
				int count = 0;
				while ( reportTable.getValueAt(count, 6) == null )
				{
					reportTable.setValueAt("N/A", count, 6);
					reportTable.setValueAt("N/A", count, 7);
					reportTable.setValueAt("N/A", count, 8);
					count++;
				}
				if (row == 0)
				{
					ProjectNew.Log("", ProjectNew.ActivityType.NORMAL, "No Device has been Checked-in!!!", ProjectNew.getDelayTime());
					setVisible(false);
				}
				if (row != 0)
				{
					setVisible(true);
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

    private void printreportTable() 
	{
        MessageFormat header = null;
        if (headerBox.isSelected()) 
		{
            header = new MessageFormat(headerField.getText());
        }
        MessageFormat footer = null;
        if (footerBox.isSelected()) 
		{
            footer = new MessageFormat(footerField.getText());
        }

        boolean fitWidth = fitWidthBox.isSelected();
        boolean showPrintDialog = showPrintDialogBox.isSelected();
        boolean interactive = interactiveBox.isSelected();

        JTable.PrintMode mode = fitWidth ? JTable.PrintMode.FIT_WIDTH
                                         : JTable.PrintMode.NORMAL;

        try 
		{
            boolean complete = reportTable.print(mode, header, footer,
                                                 showPrintDialog, null,
                                                 interactive, null);

            if (complete) 
			{
                JOptionPane.showMessageDialog(this,
                                              "Printing Complete",
                                              "Printing Result",
                                              JOptionPane.INFORMATION_MESSAGE);
            } else 
			{
                JOptionPane.showMessageDialog(this,
                                              "Printing Cancelled",
                                              "Printing Result",
                                              JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException pe) 
		{
            JOptionPane.showMessageDialog(this,
                                          "Printing Failed: " + pe.getMessage(),
                                          "Printing Result",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }
}