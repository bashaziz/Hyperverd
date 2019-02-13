//package Grey;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

   public class BufferedImageBuilder {
    
   private static final int DEFAULT_IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;
   
   
   public static Image byteArrayToImage(byte[] inByteArray)
    {
         try{
            return  ImageIO.read(new ByteArrayInputStream(inByteArray));
        }
        catch(Exception ex)
        {
            ProjectNew.Log("", ProjectNew.ActivityType.ERROR, ex.toString(), ProjectNew.getDelayTime());
            System.out.println(ex);
            return null;
        }
        
   }
    
        public static Image byteArrayToPhoto(byte[] inByteArray)
        {
            try{
            return  ImageIO.read(new ByteArrayInputStream(inByteArray));
            }
            catch(Exception ex)
            {
                ProjectNew.Log("", ProjectNew.ActivityType.ERROR, ex.toString(), ProjectNew.getDelayTime());
                return null;
            }
        }
        
        public BufferedImage bufferImage(Image image) {
            return bufferImage(image, DEFAULT_IMAGE_TYPE);
        }
    
        public BufferedImage bufferImage(Image image, int type) {
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
            Graphics2D g = bufferedImage.createGraphics();
           g.drawImage(image, null, null);
            waitForImage(bufferedImage);
            return bufferedImage;
        }
    
    private void waitForImage(BufferedImage bufferedImage) {
            final ImageLoadStatus imageLoadStatus = new ImageLoadStatus();
            bufferedImage.getHeight(new ImageObserver() {
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    if (infoflags == ALLBITS) {
                        imageLoadStatus.heightDone = true;
                        return true;
                    }
                    return false;
                }
            });
            bufferedImage.getWidth(new ImageObserver() {
               public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    if (infoflags == ALLBITS) {
                        imageLoadStatus.widthDone = true;
                        return true;
                    }
                    return false;
               }
            });
            while (!imageLoadStatus.widthDone && !imageLoadStatus.heightDone) {
             try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
   
               }
            }
        }
    
       // This method returns a buffered image with the contents of an image
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }
        image = new ImageIcon(image).getImage();
    
        boolean hasAlpha = hasAlpha(image);
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
    
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
        }
    
        if (bimage == null) {
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
    
        Graphics g = bimage.createGraphics();
    
        g.drawImage(image, 0, 0, null);
        g.dispose();
    
        return bimage;
    }
      public static boolean hasAlpha(Image image) {
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage)image;
            return bimage.getColorModel().hasAlpha();
        }

         PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }


    
        class ImageLoadStatus {
    
            public boolean widthDone = false;
            public boolean heightDone = false;
        }
    
   }
