package main;
import java.awt.Color;

import processing.core.PApplet;


class Button  {
  float x, y, w, h;
  private Color bg = Color.WHITE;
  public boolean s = false;
  public boolean b = false;
  public Color color = Color.WHITE;
  int count =0;
  public String name = "";
  
  
  Button(String s,float w, float h, Color c) {
    this.w = w;
    this.h = h;
    color = c;
    name = s;
  } 
  
  
  public boolean mouseOver(PApplet parent) {
	  	int mX = parent.mouseX;
	  	int mY = parent.mouseY;
		if (mX > x && mX < x + w && mY > y && mY < y + h){
			b =true;
			return b;
		}	
		b =false;
		return b;
	}
  
  
  public boolean mouseClicked() {
	  if (b){
			count=0;
			s = !s;
			return true;
		}	
		return false;
   }
  
  public void draw(PApplet parent,float x_, float y_) {
	  x=x_;
	  y=y_;
	parent.stroke(80);
	parent.strokeWeight(1);
	if (s)
		bg = color;
	else if (b)
		bg = Color.PINK;
	else	
		bg = new Color(125,125,125);
	
    parent.fill(bg.getRGB());
    parent.rect(x,y-2,w,h); 
    
    parent.fill(0);
    parent.textSize(13);
    parent.textAlign(PApplet.CENTER);
    parent.text(name,x+w/2,y+16);
    
    count++;
    if (count==10000)
    	count=200;
  }
  
}