//package Grey;


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

public class PhotoCapture1 extends javax.swing.JDialog 
{
  
  public PhotoCapture1( java.awt.Frame parent, boolean modal) 
  {
       super(parent, modal);
        initComponents();
       
        
    }
	
    public PhotoCapture1(NewUpdateStaff pForm1) 
	{
      
        initComponents();
        this.pForm1 = pForm1;
        
        startCamera();
        
    }
	
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        panelPhoto = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setName("Form");
        jButton1.setText("Capture");
        jButton1.setName("btnCapture"); 
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        panelPhoto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        panelPhoto.setSize(320, 240);
        panelPhoto.setName("panelPhoto"); 
        panelPhoto.setPreferredSize(new java.awt.Dimension(320, 240));
        panelPhoto.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                onComponetHidden(evt);
            }
        });

        javax.swing.GroupLayout panelPhotoLayout = new javax.swing.GroupLayout(panelPhoto);
        panelPhoto.setLayout(panelPhotoLayout);
        panelPhotoLayout.setHorizontalGroup(
            panelPhotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );
        panelPhotoLayout.setVerticalGroup(
            panelPhotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 236, Short.MAX_VALUE)
        );

        jButton2.setText("Close"); 
        jButton2.setName("btnClose"); 
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 322, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)))
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(14, 14, 14))
        );

        pack();
    }

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {
     
        captureImage();
    }

    private void onComponetHidden(java.awt.event.ComponentEvent evt) {
       
        this.dispose();
          player.close();
          ProjectNew.Log("", ProjectNew.ActivityType.NORMAL,"Closing Camera", ProjectNew.getDelayTime());
           
    }

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) 
	{   
         comp = null;
        ProjectNew.Log("", ProjectNew.ActivityType.NORMAL,"Closing Camera", ProjectNew.getDelayTime());
         player.close();
         this.dispose();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) 
	{
         comp = null;
        ProjectNew.Log("", ProjectNew.ActivityType.NORMAL,"Closing Camera", ProjectNew.getDelayTime());
         player.close();
         this.dispose();
    }
     public void captureImage()
     {
            
            FrameGrabbingControl fgc = (FrameGrabbingControl)
            player.getControl("javax.media.control.FrameGrabbingControl");
            buf = fgc.grabFrame();

            btoi = new
            BufferToImage((VideoFormat)buf.getFormat());
            img = btoi.createImage(buf).getScaledInstance(320, 240, Image.SCALE_SMOOTH);;
            
			 pForm1.captureImage(img);
             ProjectNew.Log("", ProjectNew.ActivityType.NORMAL, "Image Successfully Captured!", ProjectNew.getDelayTime());               
        }
    
    
    
    
     public void startCamera()
     {
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
               panelPhoto.add(comp);
               comp.setSize(320,240);
            }
          
        }
        catch (Exception ex)
        {
            ProjectNew.Log("", ProjectNew.ActivityType.ERROR, "Could not activate camera, please check device", ProjectNew.getDelayTime());
            ProjectNew.Log("", ProjectNew.ActivityType.ERROR, ex.toString(), ProjectNew.getDelayTime());
            JOptionPane.showMessageDialog(null, "Could not activate camera, please check device.\n" + ex.getMessage(), "Enroll", JOptionPane.ERROR_MESSAGE);
        }     
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
    
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel panelPhoto;
   
	NewUpdateStaff pForm1;
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
}
