package main;
import java.awt.Color;
import java.text.DecimalFormat;

import processing.core.PApplet;
import processing.core.PFont;

import static main.MainSETI_3_6.scagNames;
public class PopupOrder{
	public int b = -100;
	public PApplet parent;
	public float x = 800;
	public int y = 0;
	public int w1 = 100;
	public int w = 200;
	public int h;
	public int itemH = 20;
	public Color cGray  = new Color(240,240,240);
	public static int s=-1;
	//public Slider slider;

	public PopupOrder(PApplet parent_){
		parent = parent_;
	//	slider =  new Slider(parent_,5);
	}
	
	public void draw(float x_){
		x = x_;
		checkBrushing();
		if (b>=-1){
			parent.fill(160);
			parent.stroke(0);
			h=scagNames.length*itemH+40;
			parent.rect(x, y-2, w,h);
			// Max number of relations
			for (int i=-1;i<scagNames.length;i++){
				if (i==s){
					parent.noStroke();
					parent.fill(0);
					parent.rect(x+10,y+itemH*(i+1)+5,w-25,itemH+1);
					parent.fill(255,255,0);
				}
				else if (i==b){
					parent.fill(255);
				}
				else{
					parent.fill(0);
				}
				if (i==-1){
					parent.textAlign(PApplet.LEFT);
					parent.text("None",x+30,y+itemH*(i+2));
				}
				else{	
					parent.textAlign(PApplet.LEFT);
					parent.text(scagNames[i],x+30,y+itemH*(i+2));
				}
			}	
			
			//if (items[s].equals("Similarity")) 
			//	slider.draw(x+130, y+itemH*items.length-14);
			
		}
		else{
			parent.fill(125);
			parent.rect(x,y-1,w1,24);
			parent.fill(0);
			parent.textAlign(PApplet.CENTER);
			parent.text("Order by",x+w1/2,y+16);
		}	
	}
	
	 public void mouseClicked() {
		if (b<scagNames.length){
			s = b;
			
		}
	}
	 
	public void checkBrushing() {
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (b==-100){
			if (x<mX && mX<x+w1 && y<=mY && mY<=itemH+5){
				b =100;
				return;
			}	
		}
		else{
			for (int i=-1; i<scagNames.length; i++){
				if (x<=mX && mX<=x+w && y+itemH*(i+1)<=mY && mY<=y+itemH*(i+2)+6){
					b =i;
					return;
				}	
			}
			if (x<=mX && mX<=x+w && y<=mY && mY<=y+h)
				return;
		}
		b =-100;
	}
	
	
	
}