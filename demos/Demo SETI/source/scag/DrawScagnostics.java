package scag;

import java.util.Iterator;

import processing.core.PApplet;

public class DrawScagnostics{
	public double[] scagnostics = null;
	public  Triangulation dt;

	// public static ArrayList[][] MST;
	// public static Triangulation[][] DT;
	//public static double[][] areaAlpha = null;
	// public static double[][] periAlpha = null;
	public static int[] totalCount = null;// Used in computing Leaders
	
	public  DrawScagnostics(){
		
	}
	public static void init(int nI) {
		/* MST = new ArrayList[nI][numThred];
		 for (int i=0; i<nI;i++){
			 for (int t=0; t<numThred;t++){
				 MST[i][t] =  new ArrayList();
			 }
		 }
		 DT = new Triangulation[nI][numThred];
		 totalCount = new int[nI];
		 areaAlpha = new double[nI][numThred];
		 periAlpha = new double[nI][numThred]; */
	}
	
		
	public void computeScagnosticsOnFileData(int fileID, int nS, boolean[][] data) {
		scagnostics = new double[nS];
		dt = new Triangulation();
		double[] mt = dt.compute(data,fileID);
		for (int m = 0; m < nS; m++) {
			scagnostics[m] = mt[m];
		}
		totalCount[fileID] = dt.totalCount;
			
	//	MST[fileID][thred] =  dt.mstEdges;
	//	DT[fileID][thred] =dt;
	//	areaAlpha[fileID][thred] = dt.alphaArea;
	//	periAlpha[fileID][thred] = dt.alphaPerimeter;
	}
	
	
	
	public void drawMST(PApplet p , int fileID, float xoff, float yoff, float sss) {
		
		p.stroke(0,200,0);
		for (int i = 0; i < dt.mstEdges.size(); i++) {
			Edge e = (Edge) dt.mstEdges.get(i);
			float x1 = xoff + e.p1.y*sss/1000;
			float x2 = xoff + e.p2.y*sss/1000;
			float y1 = yoff + e.p1.x*sss/1000;
			float y2 = yoff + e.p2.x*sss/1000;
			p.line(x1, y1, x2, y2);
		}
	}
	
	
	public void drawAlpha(PApplet p , int fileID, float xoff,
			float yoff, float sss) {
		
	    // DRAW triangle forming ALPHA SHAPE
		p.noStroke();
		p.fill(0,180,180);
		Iterator tri = dt.triangles.iterator();
	    p.curveTightness(1); 
		while (tri.hasNext()) {
            Triangle triangle = (Triangle) tri.next();
            if (triangle.onComplex) {
                Edge e1 = triangle.anEdge;
                Edge e2 = triangle.anEdge.nextE;
                Edge e3 = triangle.anEdge.nextE.nextE;
                
                float x1 = xoff + (e1.p1.y)*sss/1000;
                float x2 = xoff + (e2.p1.y)*sss/1000;
                float x3 = xoff + (e3.p1.y)*sss/1000;
                float y1 = yoff + (e1.p1.x)*sss/1000;
                float y2 = yoff + (e2.p1.x)*sss/1000;
                float y3 = yoff + (e3.p1.x)*sss/1000;
                
                p.beginShape();
                p.curveVertex(x1, y1);
                p.curveVertex(x1, y1);
                p.curveVertex(x2, y2);
                p.curveVertex(x3, y3);
                p.curveVertex(x1, y1); 
                p.curveVertex(x1, y1); 
                p.endShape();
           }
        }
	}
	
	// DRAW HULL
	public void drawConvexHull(PApplet p , int fileID, float xoff,
		float yoff, float sss) {
	 	p.stroke(200,0,0);
		Edge e = dt.hullStart;
		p.smooth();
		p.fill(255,200,200);
		p.beginShape();
		
		float xx2=0;
		float yy2=0;
		if (e !=null){
	        do {
	        	float x1 = xoff + (e.p1.y)*sss/1000;
    			float x2 = xoff + (e.p2.y)*sss/1000;
    			float y1 = yoff + (e.p1.x)*sss/1000;
    			float y2 = yoff + (e.p2.x)*sss/1000;
    			 p.curveVertex(x1,y1);
    			 xx2 =x2;
    			 yy2=y2;
    			p.line(x1, y1, x2, y2);
	            e = e.nextH;
	        } while (!e.isEqual(dt.hullStart));
		}
         p.curveVertex(xx2,yy2);
         p.curveVertex(xx2,yy2);
         p.endShape();
	}
	
}
