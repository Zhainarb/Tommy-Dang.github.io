package main;
import java.awt.Color;

import processing.core.PApplet;
import processing.core.PFont;

public class PopupHelp{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int b = -1;
	public PApplet parent;
	public int y = -1;
	public int w = 100;
	public int h = 24;
	public float x2 = 100;
	public int w2 = 580;
	public int h2 = 220;
	public int itemNum = 9;
	public Color cGray  = new Color(240,240,240);
	public PopupHelp(PApplet parent_){
		parent = parent_;
	}
	public int count =0;
	
	
	public void draw(String s, float x_){
		x2 = x_;
		checkBrushing();
		parent.textSize(13);
		parent.stroke(80);
		parent.strokeWeight(1);
		parent.fill(125);
		parent.rect(x2, y, w, h);
		
		parent.textAlign(PApplet.LEFT);
		parent.fill(0);
		parent.text("Help?",x2+27,y+16);
	
		if (b>=0){
			parent.textSize(13);
			parent.fill(60,60,60,248);
			parent.strokeWeight(1f);
			parent.stroke(150,150,150,100);
			parent.rect(x2-w2+100, y+24, w2, h2);
			parent.textSize(14);
			parent.textAlign(PApplet.LEFT);
			parent.fill(255);
			parent.text(s, x2-w2+100+20, y+50);
		}
		
		count++;
	    if (count==10000)
	    	count=200;
	}
	
	 
	public void checkBrushing() {
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (x2<mX && mX<x2+w && y<mY && mY<h){
			b =100;
			return;
		}	
		
		b =-1;
	}
	
}