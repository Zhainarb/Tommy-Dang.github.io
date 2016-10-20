package main;

import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

@SuppressWarnings("serial")
public class MainRenameImages extends PApplet {
	 public final static int CONNECTED = 0,  DENSE = 1,
	    		CONVEX = 2, SKINNY = 3, COUNT = 4, VERSYMMETRY = 5,  HORSYMMETRY = 6,  MSTLENGTH = 7,  NUMALPHA = 8, UNIFORMALPHA = 9;
	public static String[] scagNames = {"Connected", "Dense", "Convex", "Skinny", "Data Level", "Vertical", "Horizontal", "MST Length", "Number Apha", "Uniform Apha"};

	public static int nS =10;
	public static Color[] scagColors = new Color[nS];

	public PFont metaBold = loadFont("Arial-BoldMT-18.vlw");
	public static int size = 40;//160;
	public static ArrayList<PImage> imColor = new ArrayList<PImage>();
	
	public static float[][][] scagB;
	public static float[][][] scagH;
	public static float[][][] scagS;
	public static int step =0;
	public static Integrator iP = new Integrator(0);
	public static float percent = 0f;
	public static String message = "";
	public static String message2 = "";
	public static int count =0;
	public static PFont f;
	public PFont font2 = loadFont("Arial-BoldMT-18.vlw");
	public static ArrayList<String> files;
	
	public static int nI;
	public static PImage curIm = null;
	public static float[] meanH = null;
	public static float[] meanS = null;
	public static float[] meanB = null;
	public static float[] varH = null;
	public static float[] varS = null;
	public static float[] varB = null;
	public static float[][] disA1ll = null;
	
	 
	 
	public static void main(String args[]) {
		PApplet.main(new String[] { MainRenameImages.class.getName() });
	}

	public void setup() {
		size(1280, 750);
		background(Color.WHITE.getRGB());
		stroke(255);
		frameRate(12);
		scagColors[0] = new Color(110, 150, 190); // bluish
		scagColors[1] = new Color(200, 180, 100);// yellow
		scagColors[2] = new Color(215, 112, 112); // red
		scagColors[3] = new Color(100, 200, 200); // cyan
		scagColors[4] = new Color(128,128,8);// flesh colored
		scagColors[5] = Color.pink;
		scagColors[6] = Color.ORANGE;// orange
		scagColors[7] = new Color(176, 140, 181);// purplish
		scagColors[8] = Color.MAGENTA;
		scagColors[9] = Color.BLUE;
		textFont(font2,14);
		f = createFont("Courier",40,true);
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent evt) {
				mouseWheel(evt);
			}
		});

		///********************
		 String imgType = ".png";
		 String pathSAVE = "/Users/TuanDang/Desktop/RenamedData2000/";
		 String path = "Path to the image dataset";
		 
		// Read test image datasets
			 path = "/Users/TuanDang/Desktop/Skytree/SETI/data/";
			 files = new ArrayList<String>();
			 nI = 2000;
			 for (int i=0; i<nI;i++){ 
				files.add(path+"img-"+(i)+imgType); 
			 }	
			 // Copy color Images
			 for (int i=0; i<nI;i++){ 
				PImage im1 = loadImage(files.get(i)); 
			 	imColor.add(im1);
				System.out.println(i+"	"+files.get(i));
				if (i<10){
					im1.save(pathSAVE+"img-000"+(i)+imgType);
				}
				else if (i<100){
					im1.save(pathSAVE+"img-00"+(i)+imgType);
				}
				else if (i<1000){
					im1.save(pathSAVE+"img-0"+(i)+imgType);
				}	
				else{
					im1.save(pathSAVE+"img-"+(i)+imgType);
				}
			 }
		 
		System.out.println("Done rename images");
			
	}

	// This function returns all the files in a directory as an array of Strings
	public  ArrayList<String> listFileNames(String dir, String imgType) {
		File file = new File(dir);
		ArrayList<String> a = new ArrayList<String>();
		if (file.isDirectory()) { // Do
			String names[] = file.list();
			for (int i = 0; i < names.length; i++) {
				ArrayList<String> b = listFileNames(dir + "/" + names[i], imgType);
				for (int j = 0; j < b.size(); j++) {
					//String sss = b.get(j);
					//if (!isIn(sss, a), imgType){
					//if (!sss.contains("download4")){
						a.add(b.get(j));
					//}	
				}
			}
		} else if (dir.endsWith("jpg") || dir.endsWith("JPG") || dir.endsWith("png")) {
			a.add(dir);
			message2 = dir;
		}
		return a;
	}
	public  boolean isIn(String str, ArrayList<String> a , String imgType) {
		String[] str1 = str.split("/");
		String str2 = str1[str1.length - 1].split(imgType)[0];
		for (int i = 0; i < a.size(); i++) {
			String[] str3 = a.get(i).split("/");
			String str4 = str3[str3.length - 1].split(imgType)[0];
			if (str2.contains(str4) || str4.contains(str2))
				return true;
		}
		return false;
	}	

	public void draw() {
		textFont(font2,14);
		this.background(255,255,255);
		count+=10;
		if (count==1000)
			count=0;
	}
	

	public void keyPressed() {
	}

	public void mouseMoved() {
	}

	public void mousePressed() {
	}

	public void mouseReleased() {
	}

	public void mouseDragged() {
	}

	public void mouseClicked() {
	}

	public void mouseWheel(MouseWheelEvent e) {
	}
}