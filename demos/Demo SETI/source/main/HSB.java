package main;

import java.awt.Color;

import processing.core.PImage;
import static main.MainSETI_3_6.size;
import static main.MainSETI_3_6.threds;


public class HSB {
	public static boolean[][] getDataB(PImage im) {
		 int[] px = im.pixels;
		
		 float thred1 = threds[0];
		 float thred2 = threds[1];
		 boolean[][] data = new boolean[size][size];
		 for (int i=0;i<size;i++){ 
			 for (int j=0;j<size;j++){
				 int index = i*size+j;
				 Color c = new Color(px[index]);
				 float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
				 if (thred1<=hsb[2] && hsb[2]<=thred2){
				 	data[i][j] = true;
			 	 }
			 }	 
		 }
		 return data;
	}
	
	public static float getBrightness(PImage im) {
		 int[] px = im.pixels;
		 float sum=0;
		 for (int i=0;i<size;i++){ 
			 for (int j=0;j<size;j++){
				 int index = i*size+j;
				 Color c = new Color(px[index]);
				 float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
				 sum += hsb[2];
			 }	 
		 }
		 return sum;
	}
	
	
	
	
	
}
