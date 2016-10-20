package main;

import processing.core.*;
import static main.MainSETI_3_6.selectedImage;
import static main.MainSETI_3_6.locals;
import static main.MainSETI_3_6.dissLeaders;

public class Ball {
	int id;
	public PApplet parent;
	public PImage img = null;
	public static int brushingBall=-1;
	public static int s=-1;
	
	float x,dX,pushX, pullX;
	float y,dY,pushY, pullY;
	public static float r = 30;
	public Integrator iR = new Integrator(r,0.2f,0.2f);
	
	public Ball(int id_, PApplet pa) {
		id = id_;
		parent = pa;
		x = parent.random(parent.width);
		y = parent.random(parent.height-50);
	}
	
	void updateRadius() {
		float radius = r+PApplet.pow(locals[id].size(),0.35f);
		iR.target(radius); 
	}
		
	void checkBrushing() {
		if (BBP.x+x-iR.value+12<parent.mouseX && parent.mouseX<BBP.x+x+iR.value-12 && BBP.y+y-iR.value+12< parent.mouseY && parent.mouseY< BBP.y+y+iR.value-12)
			brushingBall = id;
	}
		
	public void setImg(PImage img_) {
		img = img_;
	}
	
	public static Ball[] append(Ball t[], int id, PApplet parent) {
		Ball temp[] = new Ball[t.length + 1];
		System.arraycopy(t, 0, temp, 0, t.length);
		temp[t.length] = new Ball(id, parent);
		return temp;
	}
	
	void spring(Ball ball2, int index1, int index2, int i1, int i2) {
		float val = dissLeaders[i1][i2]-BBP.slider.val;
		
		float deltax = x-ball2.x;
		float deltay = y-ball2.y;
		float d = PApplet.sqrt(PApplet.pow(deltax, 2) + PApplet.pow(deltay, 2));
		float theta = PApplet.atan2(deltay, deltax);
		if(val<0){
			float R = ball2.iR.value;
			float r1 = iR.value;
			float dis = (d - R-r1)/30;
			if (dis>0){
				pullX -= dis* PApplet.cos(theta);
				pullY -= dis* PApplet.sin(theta);
			}
		
		}
		else{
			if (d>0){ 
				pushX +=  val* PApplet.cos(theta)/(PApplet.sqrt(d)*10); 
				pushY +=  val* PApplet.sin(theta)/(PApplet.sqrt(d)*10); 
			}
		}
		if (Float.isNaN(pullX))
			pullX =0;
		if (Float.isNaN(pullY))
			pullY =0;
		if (Float.isNaN(pushX))
			pushX =0;
		if (Float.isNaN(pushY))
			pushY =0;
		
	}
	
	
	void springChildParent(int indexParent) {
		float deltax = x-MainSETI_3_6.balls[indexParent].x;
		float deltay = y-MainSETI_3_6.balls[indexParent].y;
		float d = PApplet.sqrt(PApplet.pow(deltax, 2) + PApplet.pow(deltay, 2));
		float theta = PApplet.atan2(deltay, deltax);
		
		int count =locals[indexParent].size();
		if (count>500)
			count=500;
		
		float R = 80+MainSETI_3_6.balls[indexParent].iR.value+5*PApplet.sqrt(count);
		float dis = (d - R)/10;
		pullX -= dis* PApplet.cos(theta);
		pullY -= dis* PApplet.sin(theta);
	}
	
	
	/*
	
	void springPull(Ball ball2, int i1, int i2) {
			float deltax = x-ball2.x;
			float deltay = y-ball2.y;
			float d = PApplet.sqrt(PApplet.pow(deltax, 2) + PApplet.pow(deltay, 2));
			float theta = PApplet.atan2(deltay, deltax);
			float dis = (d - r*2)/100;
			if (dis>0){
				pullX -= dis* PApplet.cos(theta);
				pullY -= dis* PApplet.sin(theta);
			}
		
	}
	
	void springPush(Ball ball2, int i1, int i2) {
		float deltax = x-ball2.x;
		float deltay = y-ball2.y;
		float d = PApplet.sqrt(PApplet.pow(deltax, 2) + PApplet.pow(deltay, 2));
		float theta = PApplet.atan2(deltay, deltax);
		float dis = d*0.03f;
		if (dis>0){
			pushX += PApplet.cos(theta)/dis;
			pushY += PApplet.sin(theta)/dis;
		}
	}
	*/
	
	
	
	void collide(int i2) {
		float X = MainSETI_3_6.balls[i2].x;
		float Y = MainSETI_3_6.balls[i2].y;
		float R = MainSETI_3_6.balls[i2].iR.value;
		float deltax = X - x;
		float deltay = Y - y;
		float d = PApplet.sqrt(PApplet.pow(deltax, 2) + PApplet.pow(deltay, 2));
		float r1 = iR.value;
		float dD = r1 + R - d;
		if (d < r1 + R && d > 0) {
			float theta = PApplet.atan2(deltay, deltax);
			dX += -dD * PApplet.cos(theta) * R / (r1 + R);
			dY += -dD * PApplet.sin(theta) * R / (r1 + R);
		}
			
	}
	
	void collideChildParent(int i2) {
		float X = MainSETI_3_6.balls[i2].x;
		float Y = MainSETI_3_6.balls[i2].y;
		float R = MainSETI_3_6.balls[i2].iR.value;
		float deltax = X - x;
		float deltay = Y - y;
		float d = PApplet.sqrt(PApplet.pow(deltax, 2) + PApplet.pow(deltay, 2));
		float r1 = iR.value;
		float dD = r1 + R - d;
		if (d < r1 + R && d > 0) {
			float theta = PApplet.atan2(deltay, deltax);
			dX += -dD * PApplet.cos(theta) * R / (r1 + R);
			dY += -dD * PApplet.sin(theta) * R / (r1 + R);
		}
	}
	
	void bounce() {
		float rrr = iR.value;
		if (x<rrr) x=rrr;
		else if (x>parent.width-rrr) x=parent.width-rrr;
		if (x>ParalellCoordinate.xLeft.value-110-rrr) x=ParalellCoordinate.xLeft.value-110-rrr;
		if (x>DrawTriangulation.xLeft2.value-110-rrr) x=DrawTriangulation.xLeft2.value-110-rrr;
		
		if (y<rrr+25) y=rrr+25;
		else if (y>parent.height-rrr) y=parent.height-rrr;
	}
	
	void updatePosition(float fx, float fy) {
		x += (dX*0.5f+(pullX+pushX)+fx);
		y += (dY*0.5f+(pullY+pushY)+fy);
	}
	
	void display(int opacity) {
		float diameter = iR.value*2-12; 
		float xx = BBP.x + x;
		float yy = BBP.y + y;
		
		
		if (opacity>200 && main.MainSETI_3_6.signalButton.s){
			float v = main.MainSETI_3_6.signalStrength[id];
			parent.fill(10,10,10+v*245);
			parent.noStroke();
			parent.ellipse(xx, yy, diameter,diameter);
		}
		
		float size = diameter/1.41422f;
		parent.tint(255, opacity);
		if (img==null) {
			PImage im1 = parent.loadImage(main.MainSETI_3_6.files.get(id));
			setImg(im1);
		//	parent.rect(xx, yy,size,size);
		}	
		else
			parent.image(img, xx-size/2, yy-size/2,size,size);
		parent.strokeWeight(1);
		
		
		if (opacity>=199){
			if (id==selectedImage[0]){
				parent.noFill();
				parent.stroke(255,0,0);
				parent.rect( xx-size/2, yy-size/2,size,size);
			}
			else if (id==selectedImage[1]){
				parent.noFill();
				parent.stroke(255,100,0);
				parent.rect( xx-size/2, yy-size/2,size,size);
			}	
		}
		
		if (id==brushingBall){
			parent.fill(255,0,0);
			parent.text(id,xx,yy);
		}
	}
}
