package main;

import static main.MainSETI_3_6.size;
import static main.MainSETI_3_6.nS;
import static main.MainSETI_3_6.scagColors;
import static main.MainSETI_3_6.selectedImage;
import static main.MainSETI_3_6.scagB;
import static main.MainSETI_3_6.files;

import java.awt.Color;

import main.Integrator;
import processing.core.PApplet;
import processing.core.PImage;
import scag.DrawScagnostics;

public class DrawTriangulation {
	public static Integrator[] iP1 = new Integrator[nS];
	public static Integrator[] iP2 = new Integrator[nS];
	public static Integrator[] iP3 = new Integrator[nS];

	public static Integrator xLeft2 = new Integrator(1280+300, 0.5f, 0.15f);

	public static void draw(PApplet p, int im1, int im2) {
		// p.background(0, 0, 0);
		float x = xLeft2.value;
		float y = 80;
		
		p.fill(255);
		p.stroke(0);
		p.strokeWeight(1);
		p.rect(x-100, -1, 450, p.height+1);
		drawTriangulation(p, im1, x, y, iP1, 0, new Color(255,0,0));
		if (im2>=0)
			drawTriangulation(p, im2, x+230, y, iP2,1, new Color(255,100,0));
	}
	
	
	public static void drawTriangulation(PApplet p, int imID, float x, float y, Integrator[] iP, int index, Color color) {
		p.fill(0);
		
		float x2 = x;
		float y2 = y;
		float s2 = 100;
		float g2 = 12;
		
		float xOroginal = x2;
		String[] pieces = files.get(imID).split("/");
		p.textSize(14);
		p.textAlign(PApplet.LEFT);
		p.text(pieces[pieces.length-1], xOroginal-30, y2-10);
		p.textAlign(PApplet.RIGHT);
		p.text("ID="+imID, xOroginal-7, y2+s2/2+5);
		if (imID>=0){
			PImage im = p.loadImage(files.get(imID));
			p.image(im, xOroginal, y2, s2, s2);
			p.noFill();
			p.strokeWeight(2);
			p.stroke(color.getRGB());
			p.rect(xOroginal, y2, s2, s2);
			p.strokeWeight(1);			
		}	
			
		y2=195;
		x2 = x;
		p.stroke(0);
		p.noFill();
		p.rect(x2-1, y2-1, s2+1, s2+1);
		p.noSmooth();
		
		// Get data image
		
		PImage im1 = p.loadImage(files.get(imID));
		
		 
		boolean[][] data = main.HSB.getDataB(im1);
		
		PImage im = new PImage(size,size);
		int count = 0;
		 for (int i=0;i<size;i++){ 
			 for (int j=0;j<size;j++){
				 int ind = i*size+j;
				 if (data[i][j]){
					 im.pixels[ind] = Color.YELLOW.getRGB();
					 count++;
				 }	 
				 else 
					 im.pixels[ind] = new Color(50,50,50).getRGB();
			 }	 
		 }
		DrawScagnostics scagnostics= new DrawScagnostics();
		scagnostics.computeScagnosticsOnFileData(imID,nS, data);
			
		p.image(im, x2,y2, s2, s2);
		p.fill(220,220,0);
		p.text("Data poitns", x-7, y2+s2/2+5);
		p.text(count, x-7, y2+s2/2+20);
		
		
		y2+=s2+g2;
		p.stroke(0);
		p.fill(230, 230, 230);
		p.rect(x2, y2, s2, s2);
		scagnostics.drawMST(p, imID, x2, y2, s2);
		p.fill(0,200,0);
		p.text("MST", x-7, y2+s2/2+5);
		
		y2+=s2+g2;
		p.stroke(0);
		p.fill(230, 230, 230);
		p.rect(x2, y2, s2, s2);
		scagnostics.drawAlpha(p, imID, x2, y2, s2);
		p.fill(0,180,200);
		p.text("Alpha shape", x-7, y2+s2/2+5);
		
		y2+=s2+g2;
		p.stroke(0);
		p.fill(230, 230, 230);
		p.rect(x2, y2, s2, s2);
		scagnostics.drawConvexHull(p, imID, x2, y2, s2);
		p.fill(200,0,0);
		p.text("Convex Hull", x-7, y2+s2/2+5);
		
		
		y2+=s2+g2;
		p.noStroke();
		drawScag(p, imID, s2, x2, y2, iP, index);
	}

	public static void drawScag(PApplet p, int id, float s, float x, float y,Integrator[] iP, int index) {
		p.textAlign(PApplet.RIGHT);
		p.strokeWeight(1);
		for (int i = 0; i < nS; i++) {
			float v = scagB[id][i] * s;
			iP[i].target(v);
			iP[i].update();
			Color color = scagColors[i];
			p.fill(color.getRGB());
			float y2 = y + i * s*0.9f/ nS;
			float x2 = x;
			p.rect(x2, y2, iP[i].value, s*0.9f/nS-1);

			p.text(MainSETI_3_6.scagNames[i], x2 - 10, y2 + 10);
		}
	}
}