package main;
import java.awt.Color;

import processing.core.PApplet;
import processing.core.PFont;

public class PopupOption{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int b = -1;
	public PApplet parent;
	public int y = -1;
	public int w = 100;
	public int h = 24;
	public float x2 = 0;
	public int w2 = 200;
	public int h2 = 90;
	public int itemNum = 9;
	public Color cGray  = new Color(240,240,240);
	public PopupOption(PApplet parent_){
		parent = parent_;
	}
	public int count =0;
	
	
	public void draw(CheckBox c1, CheckBox c3, CheckBox c4, float x_){
		x2 = x_;
		checkBrushing();
		if (b>=0){
			parent.textSize(13);
			parent.fill(80);
			parent.strokeWeight(1f);
			parent.stroke(150,150,150,100);
			parent.rect(x2, y-2, w2, h2);
			c1.draw(x2+10);
			c3.draw(x2+10);
			c4.draw(x2+10);
		}
		else{
			parent.textSize(13);
			parent.stroke(80);
			parent.strokeWeight(1);
			parent.fill(125);
			parent.rect(x2, y, w, h);
			
			parent.textAlign(PApplet.CENTER);
			parent.fill(0);
			parent.text("Menu",x2+w/2,y+16);
		}	
		count++;
	    if (count==10000)
	    	count=200;
	}
	
	 
	public void checkBrushing() {
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (b==-1){
			if (x2<mX && mX<x2+w && y<mY && mY<h){
				b =100;
				return;
			}	
		}
		else{
			if (x2<mX && mX<x2+w2 && y<mY && mY<h2){
				return;
			}	
			
		}
		b =-1;
	}
	
}