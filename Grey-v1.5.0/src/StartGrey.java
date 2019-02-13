//package Grey;

import javax.swing.*;

public class StartGrey
{
	
  public static void main (String[] args)
  {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIManager.put("swing.boldMetal", false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
						}
						new ProjectNew().setVisible(true);
					}
				});
  }

}