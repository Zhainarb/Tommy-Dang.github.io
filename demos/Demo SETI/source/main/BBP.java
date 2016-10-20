package main;

import java.util.ArrayList;

import processing.core.*;
import static main.MainSETI_3_6.*;
import static main.Ball.*;

public class BBP{
	public static PFont font;

	public static float f = 0.01f; // Friccion
	
	public static boolean halt = false;
	public static boolean show_picColor = true;
	public static int show_picHSB = 0;
	public static boolean show_pull = true;
	public static boolean show_push = true;
	public static boolean show_scag = false;
	public static boolean alphaShape = false;
	public static boolean convexHull = false;
	public static boolean showMST = false;
	public static boolean collide = false;
	public static boolean firstTime = true;
	public static boolean showDiff = false;
		
	public PApplet parent;
	public static int sVar =33;
	public static SliderBBP slider;
	public static float x = 0f;
	public static float y = 0f;
	public static int countInBBP = 0;
	
	public BBP(PApplet p) {
		parent =p;
		font = parent.loadFont("Arial-BoldMT-18.vlw");
		slider = new SliderBBP(parent, 400, 4, "Dissimilarity", 10f,170);
	}
	
	public void draw() {
		parent.noStroke();
		for (int i = 0; i < balls.length; i++) {
			balls[i].iR.update();
		}
			
		float yy2 = parent.height/2;
		float xx2= PApplet.min(ParalellCoordinate.xLeft.value-100, parent.width);
		xx2= PApplet.min(DrawTriangulation.xLeft2.value-100, xx2);
		
		if (PopupOrder.s>=0){
			parent.stroke(0,150);
			parent.strokeWeight(2);
			parent.line(35, yy2, xx2-50, yy2);
			
			parent.textSize(16);
			parent.fill(0);
			parent.text("0",20,yy2+6);
			parent.text("1",xx2-38,yy2+6);
			parent.textAlign(PApplet.CENTER);
			parent.text(main.MainSETI_3_6.scagNames[PopupOrder.s],xx2-60,yy2-20);
			
			parent.noStroke();
			parent.fill(0,150);
			parent.triangle(xx2-50, yy2-10, xx2-50, yy2+10, xx2-40, yy2);
			
		}
		
		for (int i = 0; i < leaderList.size(); i++) {
			int index = leaderList.get(i);
			// Filtering
			if (main.MainSETI_3_6.filterButton.s){
				if (isInArrayList(index, ParalellCoordinate.b)<0) continue;
			}
			
			
			if (balls[index] == null) continue;
			balls[index].pullX =0;
			balls[index].pullY =0;
			balls[index].pushX =0;
			balls[index].pushY =0;
			balls[index].dX =0;
			balls[index].dY =0;
			float fx = 0;
			float fy = 0;
			if (!halt){
				if (PopupOrder.s>=0){
					float wLeader =xx2-150-170;
					fx = (150+wLeader*(scagB[index][PopupOrder.s])-balls[index].x);
					fy = (parent.height*0.5f-balls[index].y);
					
					fx /=10;
					fy /=40;
					
				}
				else{
					for (int j = 0; j < leaderList.size(); j++) {
						int index2 = leaderList.get(j);
						balls[index].spring(balls[index2],index,index2,i,j);
					}
				}
				
				
				for (int i2 = 0; i2 < leaderList.size(); i2++) {
					int index2 = leaderList.get(i2);
					balls[index].collide(index2);
				}
			}
			
			balls[index].updatePosition(fx,fy);
			balls[index].bounce();
			if (brushingBall<0){
				balls[index].display(255);
			}
			else{
				if (index==brushingBall){
					balls[index].display(255);
				}	
				else{
					balls[index].display(45);
				}
				
			}
		}	
		
		// Display children
		if (brushingBall>=0){
			int maxBall = 500;
			for (int j = 0; j < locals[brushingBall].size() && j<maxBall; j++) {
				int indexChild = locals[brushingBall].get(j);
				balls[indexChild].pullX =0;
				balls[indexChild].pullY =0;
				balls[indexChild].pushX =0;
				balls[indexChild].pushY =0;
				balls[indexChild].dX =0;
				balls[indexChild].dY =0;
				
				if (!halt){
					balls[indexChild].springChildParent(brushingBall);
					balls[indexChild].collideChildParent(brushingBall);
					for (int k = 0; k < locals[brushingBall].size() && k<maxBall; k++) {
						if (k==j) continue;
						int indexChild2 = locals[brushingBall].get(k);
						balls[indexChild].collide(indexChild2);
					}
						
					balls[indexChild].updatePosition(0,0);
					
				}
				balls[indexChild].display(255);
			}
		}
		
		if (PopupOrder.s==-1){
			slider.draw();
		}
		if (countInBBP==balls.length)
			countInBBP =0;
		countInBBP++;
	}
	
	public int isIn(int num, int[] a) {
		for (int i=0;i<a.length;i++){
			if (a[i]==num)
				return i;
		}
		return -1;
	}
	
	public int isInArrayList(int num, ArrayList<Integer> a) {
		for (int i=0;i<a.size();i++){
			int index = a.get(i);
			if (index==num)
				return i;
		}
		return -1;
	}
	
	void newKP(int id) {
		Ball[] tempBall = Ball.append(balls, id, parent);
		balls = tempBall;
	}
	
	public void mouseClicked() {
		Ball.s = Ball.brushingBall;
		if (Ball.s>=0){
			main.MainSETI_3_6.selectedImage[main.MainSETI_3_6.currentIndex] = Ball.s;
			System.out.println(main.MainSETI_3_6.currentIndex+"selectedImage[main.MainSETI_2_5.currentIndex]="+selectedImage[main.MainSETI_3_6.currentIndex]);
			main.MainSETI_3_6.currentIndex++;
			if (main.MainSETI_3_6.currentIndex==2)
				main.MainSETI_3_6.currentIndex=0;
		}	
	}
	
	public void mouseMoved() {
		brushingBall = -1;
		for (int i = 0; i < leaderList.size(); i++) {
			int index = leaderList.get(i);
			//if (isInArrayList(index, ParalellCoordinate.b)>=0)
				balls[index].checkBrushing();
		}
		
	}
	public void mouseDragged() {
		if (brushingBall>=0){
			balls[brushingBall].x = parent.mouseX;
			balls[brushingBall].y = parent.mouseY;
		}
	}
	
	public void mousePressed() {
		slider.checkSelectedSlider1();
	}	 
	
	public void mouseReleased() {
		slider.checkSelectedSlider2();
	}
	
	public void keyPressed() {
		if (parent.key == '0') { 
			x =0;
			y = 0;
		}
		if (parent.key == '-') { 
			Ball.r --;
			if (Ball.r<5)
				Ball.r =5;
			for (int i = 0; i < nI; i++) {
				balls[i].updateRadius();
			}	
		}
		if (parent.key == '+') { 
			Ball.r ++;	
			for (int i = 0; i < nI; i++) {
				balls[i].updateRadius();
			}	
		}
		if (parent.key == 'd' || parent.key == 'D') { 
			for (int i = 0; i < balls.length; i++) {
				balls[i].x = parent.random(parent.width);
				balls[i].y = parent.random(parent.height);
			}
		}	
		if (parent.key == 'p') { 
				show_pull = !show_pull;
		}
		if (parent.key == '[') { 
			show_push = !show_push;
		}
		if (parent.key == '1' || parent.key == '2' || parent.key == '3' || parent.key == '4' || parent.key == '5' ||
				parent.key == '6' || parent.key == '7' || parent.key == '8' || parent.key == '9' || parent.key == '0') { 
			//Ball.step = Integer.parseInt(parent.key+""); 
		}
		if (parent.key == 'c') { 
			show_picColor = !show_picColor;
			show_picHSB = 0;
		}
		if (parent.key == 'b') {
			show_picHSB++;
			if (show_picHSB==5) show_picHSB = 1;
			show_picColor = false;
		}
		if (parent.key == 'o') { // 
			show_scag = !show_scag;
			show_picColor = false;
		}
		if (parent.key == 'f' || parent.key == 'F') { // HALT
			halt = !halt;
		}
		else if (parent.key == 'a') { // shaking
			alphaShape = !alphaShape;
			//show_picColor = false;
			//show_scag = false;
		}
		else if (parent.key == 'h') { // shaking
			convexHull = !convexHull;
		//	show_picColor = false;
		//	show_scag = false;
		}
		else if (parent.key == 'm') { // shaking
			showMST = !showMST;
		//	show_picColor = false;
		//	show_scag = false;
		}
		else if (parent.key == 'c') { 
			collide = !collide;
		}
		else if (parent.key == 'e') { 
			showDiff = !showDiff;
		}
	}	
}
