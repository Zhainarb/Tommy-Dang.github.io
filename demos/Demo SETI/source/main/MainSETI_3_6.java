package main;

import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import scag.DrawScagnostics;

@SuppressWarnings("serial")
public class MainSETI_3_6 extends PApplet {
	public final static int PIXELLEVEL = 0, SPARSE = 1, STRIATED= 2, CLUMPY = 3, MONOTONIC = 4, DENSE=5, STRINGY=6, SKINNY=7; 
	public static String[] scagNames = {"Pixel Level", "Closeness", "Striated",  "Clumpy", "Monotonic","Dense","Stringy","Skinny"};
	public static int nS =scagNames.length;
	
	public PFont metaBold = loadFont("Arial-BoldMT-18.vlw");
	public static float[][] scagB;
	public static float[] signalStrength;
	
	public static ThreadLoader1 loader11;
	public static Thread thread11;
	public static ThreadLoader1 loader12;
	public static Thread thread12;
	
	public static Thread thread2;
	public static Integrator iP = new Integrator(0);
	public static int count =0;
	public PFont font2 = loadFont("Arial-BoldMT-18.vlw");
	public PopupHelp popupHelp =  new PopupHelp(this);
	
	
	public static float[] threds = {0.31f,1};
	// falsePositive=251	falseNegative=533
	
	public static float[] disSelected = null;
	public static Ball[] balls = null;
	public static int numDis10 =1000;
	@SuppressWarnings("rawtypes")
	public static ArrayList[] dis10Array = new ArrayList[numDis10];
	
	//Draw Rosement chart
	public Integrator[][] iB= new Integrator[2][nS];
	public static int step =0;
	public static PImage sImB ;
	public static PImage bImB ;
	
	public static Color[] scagColors = new Color[nS];
	public static PApplet ppp;
	
	public static BBP bbp;
	
	public static int size = 70;
	
	public PopupOption option= new PopupOption(this);
	public CheckBox c1 = new CheckBox(this, 5, 5,"Clusters",1,0);
	public CheckBox c3 = new CheckBox(this, 5, 25,"Signal Strength",-1,1);
	public CheckBox c4 = new CheckBox(this, 5, 45,"Similar images",-1,1);
	
	public static int[] selectedImage = {0,1};//{835,528};
	public static int currentIndex = 0;
	
	
	public static int nI;
	public static ArrayList<String> files;
	
	public static int maxPixels = 0;
	
//	public static int[] mostSimilarToSelected; // Not used
//	public static float[] disSimilarToSelected;
	public static int brushingPlot=-1;
	
	public static ArrayList<Integer>[] histo;
	public static float plotSize =18;
	public static float plotX =0;
	
	public static ParalellCoordinate[] paralellCoordinate;
	public static Button filterButton;
	public static Button signalButton;
	public static Button triangulationButton;
	public static PopupOrder popupOrder;
	
	public static long t1=0,t2=0;
	public static int loadImg = -100;
	public static PImage loadingImg;
	public static int minPoints = 6; // min data points to compute scagnostics
	
	// Lensing into signal strength
	public static Integrator[] iX;
	public static boolean isLensing=true;
	public PImage logoImage =  this.loadImage("pics/sm-skytree-logo.png");
	public static double maxDense = 0;
	
	// Leader algorithm
	public static ArrayList<Integer>[] locals = null;
	public static boolean[] isLeaders = null;
	public static int[] myLeader = null;
	public static float cut = 0f; 
	public static float[][] dissLeaders = null; 
	public static int numLeaders = 0; 
	// New
	public static ArrayList<Integer> leaderList = null;

	// Draw signal strength
	public static int bI =-1;
	
	// Draw similar images
	public static int maxSimilar = 1000;
	public static Integrator[] xI, yI;
	public static  ArrayList<Integer> similarList = new ArrayList<Integer>();
	public static PImage[] similarImages;
	public static int numBin = nS*50;
	
	
	// Error message
	public static String errorMessage ="";
	
	
	public static void main(String args[]) {
		PApplet.main(new String[] { MainSETI_3_6.class.getName() });
	}

	public void setup() {
		size(1440, 900);
		frameRate(12);
		if (frame != null) {
		    frame.setResizable(true);
		 }
		
		smooth();
		textFont(font2,14);
		ppp = this;
		
		scagColors[0] = new Color(141, 211, 199);
		scagColors[1] = new Color(200, 200, 100);
		scagColors[2] = new Color(190, 186, 218); 
		scagColors[3] = new Color(251, 128, 114); 
		scagColors[4] = new Color(128, 177, 211); 
		scagColors[5] = new Color(222, 185, 200); 
		scagColors[6] = new Color(253, 180, 98);
		scagColors[7] = new Color(179, 222, 105);
			for (int j = 0; j < nS; j++) {
				DrawTriangulation.iP1[j] = new Integrator(0, 0.2f, 0.5f);
				DrawTriangulation.iP2[j] = new Integrator(0, 0.2f, 0.5f);
				DrawTriangulation.iP3[j] = new Integrator(0, 0.2f, 0.5f);
			}	
		
		filterButton = new Button("Filter by",  100, 25, new Color(0,255,0));
		triangulationButton  = new Button("Triangulation",  100, 25, new Color(255,255,0));
		signalButton = new Button("Show Signal", 100, 25, new Color(50,50,255));
		popupOrder  = new PopupOrder(this);
		
		for (int j = 0; j < nS; j++) {
			iB[0][j] = new Integrator(0,0.2f,0.5f);
			iB[1][j] = new Integrator(0,0.2f,0.5f);
		}
	
		
		
		
		
		loader11 = new ThreadLoader1(this);
		thread11 = new Thread(loader11);
		thread11.start();
		
		//loader12 = new ThreadLoader1(this, nI/2,nI);
		//thread12 = new Thread(loader12);
		//thread12.start();
		
		
		
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent evt) {
				mouseWheel(evt);
			}
		});

	}

	

	public void draw() {
		textFont(font2,14);
		this.background(100,100,100);
		this.strokeWeight(1);
		
		if (step>=3){
			String help = "";
			if (c1.s>=0){
				help = "Move the mouse over a image to see images in its cluster \n" +
						"Click an image to the computation details in Triangulation option \n" +
						"Press '+' to increase image sizes \n" +
						"Press '-' to decrease image sizes \n" +
						"Press 'f' to Frezee images \n" +
						"Press 'd' to reDistrinute images \n" +
						"Scroll mouse to move left or right \n"+
						"Scroll mouse and press a key to move up or down \n";
				if (bbp!=null && balls!=null){
					bbp.draw();
					
					// Draw background
					this.noStroke();
					this.fill(0);
					this.rect(ParalellCoordinate.xLeft.value-100, 0, this.width-ParalellCoordinate.xLeft.value+100, this.height);
					
					// Draw Polylines
					//System.out.println(ParalellCoordinate.xLeft.value);
					if (ParalellCoordinate.xLeft.value>0){
						for (int i=0;i<ParalellCoordinate.b.size();i++){
							int index = ParalellCoordinate.b.get(i);
							stroke(255,100);
							strokeWeight(1);
							
							for (int id=0;id<paralellCoordinate.length-1;id++){
								float x4 = ParalellCoordinate.xLeft.value+paralellCoordinate[id].w*scagB[index][id];
								float y4 = paralellCoordinate[id].y;
								float x5 = ParalellCoordinate.xLeft.value+paralellCoordinate[id+1].w*scagB[index][id+1];
								float y5 = paralellCoordinate[id+1].y;
								this.line(x4, y4, x5, y5);		
							}
							
							if (i>=500) {
								this.textSize(14);
								this.textAlign(PApplet.LEFT);
								this.fill(150);
								if (filterButton.s)
									this.text("Drawing 500/"+ParalellCoordinate.b.size()+" polylines", this.width-250, 55);
								break;
							}
						}	
						
						// Draw paralell coordinate
						ParalellCoordinate.bPoint =-1;
						float gap = this.height/(paralellCoordinate.length+0.5f);
						for (int i=0;i<paralellCoordinate.length;i++){
							paralellCoordinate[i].draw(this, this.width-400, gap*(i+1));
						}
					}
					
					// Draw Trangulation
					if (triangulationButton.s){
						this.tint(255,255);
						DrawTriangulation.draw(this, selectedImage[0], selectedImage[1]);
					}
					
					signalButton.draw(this,this.width-200,0);
					triangulationButton.draw(this,this.width-300,0);
					filterButton.draw(this,this.width-400,0);
					popupOrder.draw(this.width-500);
				}
				
				ParalellCoordinate.xLeft.update();
				DrawTriangulation.xLeft2.update();
			}
			else if (c3.s>=0){
				help = "Press 's' to start/stop lensing \n" +
						"You can select two different images to view \n \t\t their details in Triangulation option";
				
				this.tint(255,255);
				brushingPlot=-1;
				float xx = 40;
				float yy = this.height-120;
				float ww = this.width-60;
				float hh = this.height-170;
				
				// Y axis label
				this.fill(0,0,200);
				this.textSize(20);
				this.textAlign(PApplet.LEFT);
				translate(xx-20,yy-200);
				rotate((float) (-PI/2.));
				text("Signal Strength",0,0);
				rotate((float) (PI/2.));
				translate(-(xx-20),-(yy-200));
				this.textSize(14);
				
				
				// Sort files by id
				 float step = ww/(1.6f*nI);
				 if (triangulationButton.s){
					 step = ww/(3.2f*nI);
				 }
				 float xx1=xx;
		         float yy1=yy;
		         int numLabel =6;
		         int imageStep = nI/numLabel;
		         
		         
		         // Compute lensing positions
		 		 float ssstep = (this.width-xx)/nI;
		 		 
		 		if (isLensing) 
		 			bI = (int) ((mouseX-xx)/ssstep);
		         int nDLense =2;
		         for (int i =0; i <nI;i++){
		            if (!isLensing) break;
		        	if (bI-nDLense<=i && i<=bI+nDLense)
						iX[i].target(size);
					else if (bI-nDLense-4<i && i<bI-nDLense)
						iX[i].target((i-(bI-nDLense-4))*size/5);
					else if (bI+nDLense<i && i<bI+nDLense+4)
						iX[i].target((bI+nDLense+4-i)*size/5);
					else 
						iX[i].target(step);
					
					iX[i].update();
				 } 
		         
		         // Draw line graph
		 		 float xx2 =xx;
		 		 float yy2 = yy;
		 		 this.stroke(0,0,200);
	             
		 		 int maxI = 5000;
		 		 int skip = nI/maxI;
		 		 if (skip==0) skip=1;
		 		 for (int i =0; i <nI;i++){
            	      if (i>0)
		            	  xx2 += iX[i].value/2+iX[i-1].value/2;
		              else
		            	  xx2 += iX[i].value;
		              float hh2 = signalStrength[i]*hh;
		              yy2 = yy-hh2;
		              if (i>0 && (i%skip==0 || (bI-1000<=i && i<=bI+1000)))
			                this.line(xx1, yy1, xx2, yy2);
	             	  xx1=xx2;
				      yy1=yy2;
				 }
				
			     // Draw lensed images
		 		 xx1 =xx;
		 		 yy1 =yy;
		 	 	 xx2 =xx;
		 	 	for (int i =0; i <nI;i++){
			          if (i>0)
		            	  xx2 += iX[i].value/2+iX[i-1].value/2;
		              else
		            	  xx2 += iX[i].value;
		              float hh2 = signalStrength[i]*hh;
		              yy2 = yy-hh2;
		              if (iX[i].value>5){
		            	  PImage im1 = loadImage(files.get(i));
						  this.image(im1, xx2-iX[i].value/2, yy2-iX[i].value/2,iX[i].value-1,iX[i].value-1);
				    	  if (xx2-iX[i].value/2<mouseX && mouseX<xx2+iX[i].value/2 &&
				    			  yy2-iX[i].value/2< mouseY && mouseY< yy2+iX[i].value/2){
				    		  brushingPlot = i;
				    		  this.noFill();
			            	  this.stroke(255,255,0);
			            	  this.rect(xx2-iX[i].value/2, yy2-iX[i].value/2, iX[i].value-1, iX[i].value-1);
			            	  this.fill(255,255,0);
				              this.textAlign(PApplet.CENTER);
				              String[] str = files.get(i).split("/");
				              String name = str[str.length-1];
					    	  this.text(name,xx2,yy2+iX[i].value/2+15);
				          }	  
		              }
				      else{
				    	  if (i%imageStep==0){
					        this.fill(0);
					    	  this.textAlign(PApplet.CENTER);
					    	  String[] str = files.get(i).split("/");
				              String name = str[str.length-1];
					    	 this.text(name,xx2,yy+20);
				    	  }
				      }
		              if (i==selectedImage[0]){
		            	  this.noFill();
		            	  this.stroke(255,0,0);
		            	  if (iX[i].value>5){
		            		  this.rect(xx2-iX[i].value/2, yy2-iX[i].value/2, iX[i].value-1, iX[i].value-1);
		            		  this.fill(255,0,0);
				              this.textAlign(PApplet.CENTER);
				              String[] str = files.get(i).split("/");
				              String name = str[str.length-1];
					    	 this.text(name,xx2,yy2+iX[i].value/2+15);
		            	  }	  
		            	  else 
		            		  this.rect(xx2-2, yy2-2, 4, 4);
		            	
		              }
		              if (i==selectedImage[1]){
		            	  this.noFill();
		            	  this.stroke(255,100,0);
		            	  if (iX[i].value>5){
		            		  this.rect(xx2-iX[i].value/2, yy2-iX[i].value/2, iX[i].value-1, iX[i].value-1);
		            		  this.fill(255,100,0);
				              this.textAlign(PApplet.CENTER);
				              String[] str = files.get(i).split("/");
				              String name = str[str.length-1];
					    	  this.text(name,xx2,yy2+iX[i].value/2+15);
		            	  }	  
		            	  else 
		            		  this.rect(xx2-2, yy2-2, 4, 4);
		            	  
		              }
		              xx1=xx2;
				      yy1=yy2;
		         }

		 	 	// Draw Trangulation
				if (triangulationButton.s){
					this.tint(255,255);
					DrawTriangulation.draw(this, selectedImage[0], selectedImage[1]);
				}
				triangulationButton.draw(this,this.width-300,0);
				DrawTriangulation.xLeft2.update();
			}
			else if (c4.s>=0){
				help = "Move the mouse over a image to see details in Triangulation option \n" +
						"Click an image to see other similar images \n" +
						"Press '+' to Zoom In \n" +
						"Press '-' to Zoom Out \n" +
						"Scroll mouse to move left or right \n";
				this.tint(255,255);
				brushingPlot=-1;
				
				this.fill(0);
				this.textSize(15);
				 String[] str = files.get(selectedImage[0]).split("/");
	             String name = str[str.length-1];
	             this.textAlign(PApplet.LEFT);
		    	 this.text("Displaying "+maxSimilar+" most similar images of the selected image: "+name, 10, 70);
				
				for (int j = 0; j < similarList.size(); j++) {
					int i = similarList.get(j);
					xI[i].update();
					yI[i].update();
					this.image(similarImages[j], xI[i].value, yI[i].value,plotSize-1f,plotSize-1f);
					if (xI[i].value<=mouseX && mouseX<= xI[i].value+plotSize &&
							yI[i].value<=mouseY && mouseY<= yI[i].value+plotSize)
						brushingPlot = i;
				}
				
				// Draw selected image
				/*this.noStroke();
				this.fill(0,0,0,150);
				this.rect(0,45,180,330);
				
				PImage im1 = loadImage(files.get(selectedImage[0]));
				this.image(im1, 20, 80,120,120);*/
				this.noFill();
				this.stroke(255,0,0);
				this.strokeWeight(2);
				this.rect(xI[selectedImage[0]].value-1, yI[selectedImage[0]].value-1,plotSize,plotSize);
				
				/*this.noFill();
				this.stroke(255,0,0);
				this.strokeWeight(2);
				this.rect(20,80,120,120);*/
				this.textSize(15);
				this.fill(255,0,0);
				this.textAlign(PApplet.LEFT);
				this.text("Selected Image", xI[selectedImage[0]].value,yI[selectedImage[0]].value+plotSize+14);
				
				if (brushingPlot>=0){
					this.noFill();
					this.stroke(255,100,0);
					this.strokeWeight(2);
					this.rect(xI[brushingPlot].value-1, yI[brushingPlot].value-1,plotSize,plotSize);
					
					/*
					// Draw brushing image
					PImage im2 = loadImage(files.get(brushingPlot));
					this.image(im2, 20, 240,120,120);
					this.noFill();
					this.stroke(255,255,0);
					this.strokeWeight(2);
					this.rect(20,240,120,120);
					this.textSize(15);
					this.fill(255,255,0);
					this.text("Brushing Image", 22,232);*/
				}	
				// Draw Trangulation
				if (triangulationButton.s){
					this.tint(255,255);
					DrawTriangulation.draw(this, selectedImage[0], brushingPlot);
				}
				triangulationButton.draw(this,this.width-300,0);
				DrawTriangulation.xLeft2.update();
			}
		 	popupHelp.draw(help, this.width-100);
			option.draw(c1, c3, c4, this.width-600);
		}
		else{
			float val = ((count*5)%256)/255f;
			float xx = 100;
			float yy=100;
			
			this.textAlign(PApplet.LEFT);
			Color c = ColorScales.getColor(val, "rainbow", 1);
			this.fill(c.getRGB());
			this.textSize(20);
			this.text("Please wait ...", xx, yy);
			//Error message
			this.fill(200,0,0);
			this.textSize(16);
			this.text(errorMessage, 100,600);
	
			// draw current image
			if (loadingImg!=null){
				this.fill(0);
				this.textSize(16);
				this.text(files.get(loadImg), xx, yy+30);
				this.image(loadingImg, xx+50, yy+40, size*2, size*2);
			
				 DecimalFormat df = new DecimalFormat("#.#");
				// draw loading time
				 float timeScale = 4;
				yy += size*2+150;
				this.fill(0);
				this.textSize(16);
				this.text("Image "+(loadImg+1)+" of "+nI, xx+50, yy-80);
				
				float second1 = (float)t1/1000;
				this.text("Loading time in seconds: "+df.format(second1), xx, yy);
				this.fill(0);
				this.noStroke(); 
				val = (float) (loadImg)/(nI);
				Color c2 = ColorScales.getColor(val, "rainbow", 1);
				this.fill(c2.getRGB());
				this.rect(xx, yy+10, second1*timeScale, 30);
				
				this.fill(0);
				this.textSize(14);
				this.textAlign(PApplet.CENTER);
				for (int i=0; i<=second1; i=i+10){
					this.text(i+"", xx+i*timeScale, yy+30);
				}
				
				// draw feature computation time
				yy += 100;
				this.fill(0);
				this.textSize(16);
				this.textAlign(PApplet.LEFT);
				
				float second2 = (float)t2/1000;
				this.text("Computing features of images in seconds: "+df.format(second2), xx, yy);
				this.fill(0);
				this.noStroke(); 
				val = (float) (loadImg)/(nI);
				float rate= second2/second1;
				if (rate>1) rate=1;
				Color c3 = ColorScales.getColor(val*rate, "rainbow", 1);
				this.fill(c3.getRGB());
				this.rect(xx, yy+10, second2*timeScale, 30);
				
				this.fill(0);
				this.textSize(14);
				this.textAlign(PApplet.CENTER);
				for (int i=0; i<=second2; i=i+10){
					this.text(i+"", xx+i*timeScale, yy+30);
				}
			}
		}
		if (c1.bMode >= 0) {
			if (c1.s>=0) {
				c3.s = -1;
				c4.s = -1;
			}
		}
		else if (c3.bMode >= 0) {
			if (c3.s>=0) {
				c1.s = -1;
				c4.s = -1;
			}
		}
		else if (c4.bMode >= 0) {
			if (c4.s>=0) {
				c1.s = -1;
				c3.s = -1;
			}
		}
		
		this.image(logoImage, 2, 3, logoImage.width/2.2f, logoImage.height/2.2f);
		
		count++;
		if (count==1000)
			count=0;
	}
	
	private static Map<Integer, Float> sortByComparator2(Map<Integer, Float> unsortMap) {
		// Convert Map to List
		List<Map.Entry<Integer, Float>> list = 
			new LinkedList<Map.Entry<Integer, Float>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, Float>>() {
			public int compare(Map.Entry<Integer, Float> o1,
                                           Map.Entry<Integer, Float> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
 
		// Convert sorted map back to a Map
		Map<Integer, Float> sortedMap = new LinkedHashMap<Integer, Float>();
		for (Iterator<Map.Entry<Integer, Float>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, Float> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
		
	public void drawDescriptor(int id, int selected, float rr2, float yy2) {
		if (scagB==null)
			return;
		if (id<0) 
			return;
		if (id<balls.length && balls[id].img!=null)
			this.image(balls[id].img,10,yy2-10);

			textAlign(PApplet.LEFT);
			strokeWeight(1);
			
			//Brightness
			/*
			float lastAng = -PApplet.PI/2;
			float xx3 = step*60+200;
			ellipse(xx3, yy2,3,3);
			for (int i = 0; i < nS; i++){
				float v = scagB[id][step][i]*rr2;
				iB[selected][step][i].target(v);
				iB[selected][step][i].update();
				Color color =scagColors[i];
				fill(color.getRGB());
				float alpha = PApplet.PI*2/nS;
				arc(xx3,yy2, iB[selected][step][i].value, iB[selected][step][i].value, lastAng, lastAng+alpha);
				lastAng += alpha;  
			}*/
	}

	public void keyPressed() {
		if (c1.s>=0){
			if (bbp!=null)
				bbp.keyPressed();
		}
		else if (c3.s>=0){
			if (key=='s' || key=='S'){
				isLensing = !isLensing;
			}
		}
		else if (c4.s>=0){
			if (key=='+'){
				plotSize ++;
				computeSimilarHistogram();
			}
			else if (key=='-'){
				plotSize --;
				if (plotSize<5)
					plotSize=5;
				computeSimilarHistogram();
			}
		}
	}

	public void mouseMoved() {
		if (bbp!=null && option.b<0)
			bbp.mouseMoved();
		if (step>=3 && c1.s>=0 && bbp!=null){
			filterButton.mouseOver(this);
			triangulationButton.mouseOver(this);
			signalButton.mouseOver(this);
		}
		if (step>=3 && (c3.s>=0 || c4.s>=0)){
			triangulationButton.mouseOver(this);
		}
			
		
	}

	public void mousePressed() {
		if (bbp!=null && paralellCoordinate!=null){
			bbp.mousePressed();
			for (int i=0;i<paralellCoordinate.length;i++){
				paralellCoordinate[i].checkSelectedSlider1();
			}
		}
	}

	public void mouseReleased() {
		if (bbp!=null && paralellCoordinate!=null){
			bbp.mouseReleased();
			for (int i=0;i<paralellCoordinate.length;i++){
				paralellCoordinate[i].checkSelectedSlider2();
			}
		}	
	}

	public void mouseDragged() {
		if (bbp!=null){
			bbp.mouseDragged();
			
			ParalellCoordinate.maxCount=0;
			for (int i=0;i<paralellCoordinate.length;i++){
				if (paralellCoordinate[i].bSlider>=0)
					paralellCoordinate[i].checkSelectedSlider3(this);
			}
		}
		
	}

	public void mouseClicked() {
		if (option.b>=0){
			c1.checkSelected();
			c3.checkSelected();
			c4.checkSelected();
			if(c4.s>=0){
				computeSimilarHistogram();
			}
			return;
		}
		else if (popupOrder.b>=-1){
			popupOrder.mouseClicked();
		}
		else if(filterButton.b){
			filterButton.mouseClicked();
			if (filterButton.s)
				ParalellCoordinate.xLeft.target(this.width-350);
			else
				ParalellCoordinate.xLeft.target(this.width+300);
			
		}
		else if(signalButton.b){
			signalButton.mouseClicked();
		}
		else if(triangulationButton.b){
			triangulationButton.mouseClicked();
			if(triangulationButton.s){
				DrawTriangulation.xLeft2.target(this.width-350);
			}
			else{
				DrawTriangulation.xLeft2.target(this.width+300);
			}
		}
		else if (c1.s>=0 && bbp!=null){
			bbp.mouseClicked();
		}
		else  if (c3.s>=0 && brushingPlot>=0){
			selectedImage[currentIndex] = brushingPlot;
			currentIndex++;
			if (currentIndex==2)
				currentIndex=0;
		}
		else if (c4.s>=0){
			if (brushingPlot>=0){
				selectedImage[0]= brushingPlot;
				computeSimilarHistogram();
			}
		}
	}

	public void mouseWheel(MouseWheelEvent e) {
		int delta = e.getWheelRotation();
		if (c4.s>=0){
			plotX +=delta*2f;
			updateSimilarImageLocation();
		}
		else{
			if (keyPressed){
				BBP.y +=delta;
			}
			else{
				BBP.x +=delta;
			}
		}
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
					a.add(b.get(j));	
				}
				
			}
		} else if (dir.endsWith(imgType)) {
			a.add(dir);
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
	
	class ThreadLoader1 implements Runnable {
		public PApplet parent;
		
		public ThreadLoader1(PApplet p) {
			parent = p;
		}

		public void run() { 
			try {
				String path = "/Users/TuanDang/Desktop/Skytree/SETI/image8Selected70x70";
				 //String path = "/Users/TuanDang/Desktop/Skytree/SETI/image70x70";
				 //String path = "./imageSelected70x70";
				 //String path = "./image70x70";
				 String imgType = ".png";
				 files = listFileNames(path, imgType); 
				 nI =files.size();
				 println("Number of images: "+nI);
			
				 scagB = new float[nI][nS];
				 DrawScagnostics.totalCount = new int[nI];
				 
				 // Save Color Images
				t1 = 0;
				t2 = 0;
				for (int f = 0; f < nI; f++) {
					long t11 = System.currentTimeMillis();
					
					loadingImg = loadImage(files.get(f));
					if (loadingImg==null){	
						errorMessage = "Problem at "+files.get(f);
						loadingImg = new PImage(70,70);
						continue;
					}
					//if (DrawScagnostics.totalCount[f]>=8){
					// Save images
					//String[] names = files.get(f).split("/");
					//String name = names[names.length-1];
					if (loadingImg.width!=size || loadingImg.height!=size){ // resize if it is not 70x70 imaged
						loadingImg.resize(size, size);
						//loadingImg.save("./image8Selected"+size+"x"+size+"/"+name);
					}
					//}
					
					/*
					 * int tries=0;
					while (im1==null){
						im1 = loadImage(files.get(f));
					//	parent.textSize(13);
					//	parent.textAlign(PApplet.LEFT);
					//	parent.text("Stuck at reading image = "+files.get(f), 200, 500);
						tries++;
						if (tries>0)
							im1 = new PImage(70,70);
					}*/
					
					loadImg = f;
					long t12 = System.currentTimeMillis();
					t1 += t12-t11;
					
					long t21 = System.currentTimeMillis();
					boolean[][] data = main.HSB.getDataB(loadingImg);
					DrawScagnostics scagnostics= new DrawScagnostics();
					scagnostics.computeScagnosticsOnFileData(f,nS, data);
					
					
					
					for (int i = 0; i < nS; i++) {
						if (Double.isNaN(scagnostics.scagnostics[i])){
							scagB[f][i] = 0;
							System.out.println("NaN file="+f+" feature="+scagNames[i]);
						}
						else if (i==DENSE){  // DENSE is processed differently
							scagB[f][i] = (float) scagnostics.scagnostics[i];
							if (scagB[f][i]>maxDense)
								maxDense = scagB[f][i];
						}
						else if (scagnostics.scagnostics[i]>1)
							scagB[f][i] = 1;
						else if (scagnostics.scagnostics[i]<0)
							scagB[f][i] = 0;
						else{
							scagB[f][i] = (float) scagnostics.scagnostics[i];
						}
					}
					// Max Pixels
					if (DrawScagnostics.totalCount[f]>maxPixels){
						maxPixels = DrawScagnostics.totalCount[f];
					}
		
					long t22 = System.currentTimeMillis();
					t2 += t22-t21;
				}
				System.out.println("******* Computation time = "+(t1+t2)/1000f);
				
				// Standardized
				 for (int f = 0; f < nI; f++) {
					 scagB[f][PIXELLEVEL] *= ((size)*(size)/( (float) maxPixels));
					 scagB[f][DENSE] /= maxDense;
					 scagB[f][DENSE] = PApplet.pow(scagB[f][DENSE], 0.5f);
				}	 
				 
				System.out.println("Finished Computing Scagnostics");
				step++;
				
				
				// Compute Leader algorithm
				buildLeaders();
				System.out.println("Finished Leader");
				
				
				// Compute Dissimilarity
				bbp = new BBP(parent);
				balls = new Ball[nI];
				for (int i = 0; i < nI; i++) {
					balls[i] = new Ball(i,parent); 
					balls[i].updateRadius();
				}
				
				// Draw similarity distributions
				xI =  new Integrator[nI]; 
				yI =  new Integrator[nI]; 
				for (int i = 0; i < nI; i++) {
					xI[i] = new Integrator(0, 0.2f, 0.5f);
					yI[i] = new Integrator(0, 0.2f, 0.5f);
				}
				iX = new Integrator[nI];
				for (int i = 0; i < nI; i++) {
					iX[i] = new Integrator(0, 0.2f, 0.5f);
				}	
				//computeSimilarHistogram();
				
				paralellCoordinate = new ParalellCoordinate[scagNames.length];
				for (int i = 0; i<scagNames.length;i++){
					paralellCoordinate[i] = new ParalellCoordinate(i, 300, scagNames[i]);
				}
				ParalellCoordinate.checkall();
				
				
				System.out.println("Finished ParalellCoordinate");
				step=3;
				
				// Writing the output
				
				String[] output = new String[nI+1];
				output[0] = "FileID"+"\t" +"path"+"\t"+"Signal Strength"+"\t";
					for (int s = 0; s < nS; s++) {
						output[0]+=scagNames[s]+"\t";
					}
				
				signalStrength = new float[nI];
				float max = 0;
				for (int i = 0; i < nI; i++) {
					for (int s = 0; s<scagNames.length;s++){
						signalStrength[i] += scagB[i][s];
					}
					
					if (signalStrength[i]>max)
						max = signalStrength[i];
				}
				for (int i = 0; i < nI; i++) {
					signalStrength[i] /= max;
					
					String str = i+"\t"+files.get(i)+"\t"+signalStrength[i]+"\t";
						for (int s = 0; s < nS; s++) {
							str+=scagB[i][s]+"\t";
						}	
					output[i+1] = str;
				}
				parent.saveStrings("./output.txt", output);
				System.out.println("Fisnish Signal Strength");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void buildLeaders() {
		leaderList = new ArrayList<Integer>();
		isLeaders = new boolean[nI];
		myLeader = new int[nI];
		locals = new ArrayList[nI];
		for (int i = 0; i < nI; i++) {
			locals[i] = new ArrayList<Integer>();
		}
		cut = 1.5f;
		
		// First Round
		for (int i = 0; i < nI; i++) {
			int foundParent = -1;
			float minDisToParent = cut;
			for (int j = 0; j < leaderList.size(); j++) {
				int pa = leaderList.get(j);
				float dis = computeDis(i,pa);
				if (dis < minDisToParent){
					foundParent = pa;
					myLeader[i] = pa;
					minDisToParent = dis;
				}
			}
			if (foundParent<0){
				isLeaders[i] = true;
				leaderList.add(i);
			}
			else{
				locals[foundParent].add(i);
			}
		}
		
		// Second round
		for (int i = 0; i < nI; i++) {
			locals[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < nI; i++) {
			if (isLeaders[i]) continue;
			int foundParent = -1;
			float minDisToParent = cut;
			for (int j = 0; j < leaderList.size(); j++) {
				int pa = leaderList.get(j);
				float dis = computeDis(i,pa);
				if (dis < minDisToParent){
					foundParent = pa;
					myLeader[i] = pa;
					minDisToParent = dis;
				}
			}
			if (foundParent<0){
				System.out.println("SOMETHING WRONG: "+i);
			}
			else{
				locals[foundParent].add(i);
			}
		}
		
		System.out.println();
		System.out.println("numLeaders of "+count+":	"+leaderList.size()+" Cut:"+cut);
		for (int i = 0; i < leaderList.size(); i++) {
			int index = leaderList.get(i);
			System.out.println("	"+i+"	index="+index+"	locals="+locals[index].size());
		}
		System.out.println();
			
		// Compute Dissimilarity of Center
		numLeaders = leaderList.size();
		dissLeaders = new float[numLeaders][numLeaders];
		for (int c1 = 0; c1 < numLeaders; c1++) {
			for (int c2 = 0; c2 < numLeaders; c2++) {
				int index1 = leaderList.get(c1);
				int index2 = leaderList.get(c2);
				dissLeaders[c1][c2]=computeDis(index1,index2);
				
				// control the similarity of low pixels images
				if (dissLeaders[c1][c2]<cut)
					dissLeaders[c1][c2] = cut;
			}
		}	
	}
	public static float computeDis(int f1, int f2) {
		float sum = 0;
		for (int sc = 0; sc < nS; sc++) {
			if (sc == PIXELLEVEL)
				sum += 5*PApplet.abs(scagB[f1][sc] - scagB[f2][sc]);
			else if (sc == DENSE)
				sum += 4*PApplet.abs(scagB[f1][sc] - scagB[f2][sc]);
			else if (sc == MONOTONIC)
				sum += 3*PApplet.abs(scagB[f1][sc] - scagB[f2][sc]);
			else if (sc == STRINGY)
				sum += 2*PApplet.abs(scagB[f1][sc] - scagB[f2][sc]);
			else 
				sum += PApplet.abs(scagB[f1][sc] - scagB[f2][sc]);
		}
		return sum;
	}
	
	
	public void computeSimilarHistogram() {
		Map<Integer, Float> unsortMap = new HashMap<Integer, Float>();
		for (int i=0;i<nI;i++){
			unsortMap.put(i, computeDis(selectedImage[0],i));
		}
		Map<Integer, Float> sortedMap = sortByComparator2(unsortMap);
		
		
		int count=0;
		similarList = new ArrayList<Integer>();
		for (Map.Entry<Integer, Float> entry : sortedMap.entrySet()) {
			if (count>=maxSimilar) break;
			similarList.add(entry.getKey());
			count++;
		}
		
		similarImages = new PImage[maxSimilar];
		for (int j = 0; j < maxSimilar; j++) {
			similarImages[j] = loadImage(files.get(similarList.get(j)));
		}	
		
		
		histo = new ArrayList[numBin];
		for (int k=0;k<numBin;k++){
			histo[k] = new ArrayList<Integer>();
		}
		for (int j = 0; j < maxSimilar; j++) {
			int i = similarList.get(j);
			int index = (int) (computeDis(selectedImage[0],i)*numBin/nS);
			histo[index].add(i);
		}
		
		for (int k = 0; k < numBin; k++) {
			float xx = plotX+k*plotSize;
			float yy = this.height-100;
			for (int l = 0; l < histo[k].size(); l++) {
				int index = histo[k].get(l);
				float yy2 = yy - l*plotSize;
				xI[index].target(xx);
				yI[index].target(yy2);
			}
		}
		updateSimilarImageLocation();
	}
	public void updateSimilarImageLocation() {
		for (int k = 0; k < numBin; k++) {
			float xx = plotX+k*plotSize;
			float yy = this.height-50;
			for (int l = 0; l < histo[k].size(); l++) {
				int index = histo[k].get(l);
				float yy2 = yy - l*plotSize;
				xI[index].target(xx);
				yI[index].target(yy2);
			}
		}
	}
}