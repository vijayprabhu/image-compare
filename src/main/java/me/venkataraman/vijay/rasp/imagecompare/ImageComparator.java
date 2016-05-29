package me.venkataraman.vijay.rasp.imagecompare;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

public class ImageComparator {
	
	public static double compareImages(byte[] byteArrayImage1, byte[] byteArrayImage2) {
		double percentDifferent = 0;
		
		BufferedImage imageOne = null;
		BufferedImage imageTwo = null;
		
		try {
			
			imageOne = ImageIO.read(new ByteArrayInputStream(byteArrayImage1));
			imageTwo = ImageIO.read(new ByteArrayInputStream(byteArrayImage2));
			
			int width1 = imageOne.getWidth(null);
		    int width2 = imageTwo.getWidth(null);
		    int height1 = imageOne.getHeight(null);
		    int height2 = imageTwo.getHeight(null);
		    if ((width1 != width2) || (height1 != height2)) {
		      System.err.println("Error: Images dimensions mismatch");
		      System.exit(1);
		    }
		    long diff = 0;
		    for (int y = 0; y < height1; y++) {
		      for (int x = 0; x < width1; x++) {
		        int rgb1 = imageOne.getRGB(x, y);
		        int rgb2 = imageTwo.getRGB(x, y);
		        int r1 = (rgb1 >> 16) & 0xff;
		        int g1 = (rgb1 >>  8) & 0xff;
		        int b1 = (rgb1      ) & 0xff;
		        int r2 = (rgb2 >> 16) & 0xff;
		        int g2 = (rgb2 >>  8) & 0xff;
		        int b2 = (rgb2      ) & 0xff;
		        diff += Math.abs(r1 - r2);
		        diff += Math.abs(g1 - g2);
		        diff += Math.abs(b1 - b2);
		      }
		    }
		    double n = width1 * height1 * 3;
		    double p = diff / n / 255.0;
		    percentDifferent = p * 100.0;
			
		}catch (Exception e) {

			percentDifferent = -1;
		}
		
		return percentDifferent;
	}
}
