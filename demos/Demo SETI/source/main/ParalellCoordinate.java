package main;
import java.awt.Color;
import java.util.ArrayList;

import processing.core.PApplet;
import static main.MainSETI_3_6.scagB;
import static main.MainSETI_3_6.paralellCoordinate;
import static main.MainSETI_3_6.nI;

public class ParalellCoordinate{
	public int pair =-1;
	public float x,y;
	public float w; 
	public float l =-1;
	public float u =-1;
	public float lV =-1;
	public float uV =-1;
	
	public static Color c1  = new Color(130,130,80);
	public static Color c2  = new Color(160,80,80);
	
	public int bSlider = -1;
	public int sSlider = -1;
	public int gap = 1;
	public String text;
	
	int id;
	public static ArrayList<Integer> b=  new ArrayList<Integer>();
	float min = Float.POSITIVE_INFINITY;
	float max = Float.NEGATIVE_INFINITY;
	
	public static int numSec =  100;
	public ArrayList<Integer>[] count = new ArrayList[numSec+1];
	public static int maxCount = 1;
	public static float scale = 0;
	
	// This is for the word count
	public int numWords = 10; 
	public static int bPoint = -1;
	
	// Image 
	public static Integrator xLeft = new Integrator(1280+300, 0.5f, 0.15f);
	
	
	public ParalellCoordinate(int id_,  float size_, String text_){
		id = id_;
		l = 0;
		u = 300;
		w= size_;
		text = text_;
		min = 0;
		max = 1;
		update();
		updateCount();
	}
		
	public void update(){
		lV = l/w;
		uV = u/w;
	}
	public void updateCount() {
		for(int i=0;i<numSec+1;i++){
			count[i] = new ArrayList<Integer>();
		}
		for(int i=0;i<b.size();i++){
			int index = b.get(i);
			int loc = (int)  (numSec*(scagB[index][id]-min)/(max-min));
			count[loc].add(index);
		}
		
		for(int i=0;i<numSec+1;i++){
			if (count[i].size()>maxCount)
				maxCount = count[i].size();
		}
		scale = 100f/maxCount;
	}
	
	public void draw(PApplet parent, float x_, float y_){
		x= x_;
		y= y_;
		if (main.MainSETI_3_6.filterButton.s)
			xLeft.target(parent.width-350);
		else
			xLeft.target(parent.width+300);
		
		checkBrushingSlider(parent);
		float xx1 = xLeft.value+l;
		float xx2 = xLeft.value+u;
		float x = xLeft.value;
		
		parent.textSize(12);
		parent.stroke(255);
		parent.strokeWeight(0.1f);
		parent.line(x, y, x+w, y);
		
		
		//Lower range
		if (sSlider==0){
			c1= Color.WHITE;
		}	
		else if (bSlider==0){
			c1= Color.PINK;
		}	
		else{
			c1 = new Color(125,125,125);
		}
		parent.fill(c1.getRGB());
		parent.triangle(xx1, y, xx1-10, y-6, xx1-10, y+6);
		
		//Upper range
		if (sSlider==1){
			c2= Color.WHITE;
		}	
		else if (bSlider==1){
			c2= Color.PINK;
		}	
		else{
			c2 = new Color(125,125,125);
		}
		parent.fill(c2.getRGB());
		parent.triangle(xx2, y, xx2+10, y-6, xx2+10, y+6);
		
		parent.fill(255,255,255);
		parent.textAlign(PApplet.RIGHT);
		parent.textSize(14);
		parent.text(text,x-18,y+5);
		
		
		// Draw distribution
		Color color = Color.GREEN;
			
		
		int kSatisfied = b.size();
		int numPoints = 200;
		if (kSatisfied> numPoints){
			// Draw chart on top of bar
			for (int i=-1;i<2;i=i+2){
				parent.beginShape();
				float xG = x;
				float yG = y;
				parent.curveVertex(xG, yG);
				parent.curveVertex(xG, yG);
				for (int l =0; l<=numSec;l++){
					xG = x + l*w/numSec;
					yG = y + i*count[l].size()*scale;
					parent.curveVertex(xG, yG);
				}
				parent.curveVertex(x+w, y);
				parent.curveVertex(x+w, y);
				parent.stroke(new Color(60, 60, 60, 250).getRGB());
				parent.strokeWeight(1);
				parent.fill(color.getRGB());
				parent.endShape();
			}
		}
		else{
			parent.stroke(0, 0, 0);
			parent.strokeWeight(.5f);
			float step = w/numSec;
			for (int l =0; l<=numSec;l++){
				for (int i=0; i<count[l].size();i++){
					float xG = x + l*step+step-1;
					float yG = y-(count[l].size()*step/2f) + (i+1)*step;
					if (PApplet.dist(xG, yG, parent.mouseX, parent.mouseY)<=step){
						if (bPoint<0)
							bPoint =count[l].get(i);
					}
					parent.fill(color.getRGB());
					if (count[l].get(i)==bPoint){
						parent.fill(Color.RED.getRGB());
						parent.ellipse(xG, yG-step/2, step+1, step+1);
					}
					else{
						parent.ellipse(xG, yG-step/2, step, step);
					}
				}
			}
		}
	}
	
	
	public void checkBrushingSlider(PApplet parent) {
		float xx1 = xLeft.value+l;
		float xx2 = xLeft.value+u;
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		
		if (xx1-20<mX && mX < xx1+1 && y-10<mY && mY<y+10){
			bSlider =0;
			return;
		}	
		else if (xx2-1<mX && mX < xx2+20 && y-10<mY && mY<y+10){
			bSlider =1; 
			return;
		}	
		bSlider =-1;
	}
	
	public void checkSelectedSlider1() {
		sSlider = bSlider;
	}
	public void checkSelectedSlider2() {
		sSlider = -1;
	}
	public int checkSelectedSlider3(PApplet parent) {
		if (sSlider==0){
			l += (parent.mouseX - parent.pmouseX);
			if (l>=u) l=u-1;
			if (l<0)  l=0;
		}	
		else if (sSlider==1){
			u += (parent.mouseX - parent.pmouseX);
			if (u<=l) u=l+1;
			if (u>w)  u=w;
		}	
		
		
		// update sliders
		checkall();
		return sSlider;
	}
	
	
	public static void checkall() {
		b =  new ArrayList<Integer>();
		for (int i=0;i<nI;i++){
			boolean isNOTsatified = false;
			for (int id=0;id<paralellCoordinate.length;id++){
				float min = paralellCoordinate[id].min;
				float max = paralellCoordinate[id].max;
				float low = (min+paralellCoordinate[id].lV*(max-min));
				float high = (min+paralellCoordinate[id].uV*(max-min));
				if (scagB[i][id]<low || scagB[i][id]>high)
					isNOTsatified = true;
			}	
			if (!isNOTsatified){
				b.add(i);
			}
		}
		for (int s=0;s<paralellCoordinate.length;s++){
			paralellCoordinate[s].update();
			paralellCoordinate[s].updateCount();
		}
	}
}