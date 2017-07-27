package org.xz.utils;

import java.awt.Rectangle;  
import java.awt.image.BufferedImage;  
import java.io.*;  
import java.util.Iterator;  
import javax.imageio.ImageIO;  
import javax.imageio.ImageReadParam;  
import javax.imageio.ImageReader;  
import javax.imageio.stream.ImageInputStream;  
  
public class ImageUtils {  
    public static void cutJPG(InputStream input, OutputStream out, int x,  
            int y, int width, int height) throws IOException {  
        ImageInputStream imageStream = null;  
        try {  
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("jpg");  
            ImageReader reader = readers.next();  
            imageStream = ImageIO.createImageInputStream(input);  
            reader.setInput(imageStream, true);  
            ImageReadParam param = reader.getDefaultReadParam();  
              
            //System.out.println(reader.getWidth(0));  
            //System.out.println(reader.getHeight(0));  
            Rectangle rect = new Rectangle(x, y, width, height);  
            param.setSourceRegion(rect);  
            BufferedImage bi = reader.read(0, param);  
            ImageIO.write(bi, "jpg", out);  
        } finally {  
            imageStream.close();  
        }  
    }  
    
    public static void cutJPG(InputStream input, OutputStream out) throws IOException{
    	int x=Integer.parseInt(ConstUtils.prop.getProperty(ConstUtils.IMG_CUT_X));
    	int y=Integer.parseInt(ConstUtils.prop.getProperty(ConstUtils.IMG_CUT_Y));
    	int w=Integer.parseInt(ConstUtils.prop.getProperty(ConstUtils.IMG_CUT_W));
    	int h=Integer.parseInt(ConstUtils.prop.getProperty(ConstUtils.IMG_CUT_H));
    	
    	ImageUtils.cutJPG(input, out, x, y, w, h);
    }    
      
    public static void cutPNG(InputStream input, OutputStream out, int x,  
            int y, int width, int height) throws IOException {  
        ImageInputStream imageStream = null;  
        try {  
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("png");  
            ImageReader reader = readers.next();  
            imageStream = ImageIO.createImageInputStream(input);  
            reader.setInput(imageStream, true);  
            ImageReadParam param = reader.getDefaultReadParam();  
              
            //System.out.println(reader.getWidth(0));  
            //System.out.println(reader.getHeight(0));  
              
            Rectangle rect = new Rectangle(x, y, width, height);  
            param.setSourceRegion(rect);  
            BufferedImage bi = reader.read(0, param);  
            ImageIO.write(bi, "png", out);  
        } finally {  
            imageStream.close();  
        }  
    }  

      
    public static void cutImage(InputStream input, OutputStream out, String type,int x,  
            int y, int width, int height) throws IOException {  
        ImageInputStream imageStream = null;  
        try {  
            String imageType=(null==type||"".equals(type))?"jpg":type;  
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(imageType);  
            ImageReader reader = readers.next();  
            imageStream = ImageIO.createImageInputStream(input);  
            reader.setInput(imageStream, true);  
            ImageReadParam param = reader.getDefaultReadParam();  
            Rectangle rect = new Rectangle(x, y, width, height);  
            param.setSourceRegion(rect);  
            BufferedImage bi = reader.read(0, param);  
            ImageIO.write(bi, imageType, out);  
        } finally {  
            imageStream.close();  
        }  
    }  
  
      
    public static void main(String[] args) throws Exception {  
        ImageUtils.cutJPG(new FileInputStream("c:\\test.JPG"),  
                  new FileOutputStream("c:\\test2.jpg"), 0,0,200,100);  
          
        ImageUtils.cutPNG(new FileInputStream("c:\\1.png"),  
                new FileOutputStream("c:\\test3.png"), 0,0,50,40);  
    }  
}
