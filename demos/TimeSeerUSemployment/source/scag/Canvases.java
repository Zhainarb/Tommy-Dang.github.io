package scag;
/*
 * Canvases
 *
 * Leland Wilkinson (SPSS Inc.) and Anushka Anand (University of Illinois at Chicago)
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose without fee is hereby granted, provided that this entire notice
 * is included in all copies of any software which is or includes a copy
 * or modification of this software and in all copies of the supporting
 * documentation for such software.
 * THIS SOFTWARE IS BEING PROVIDED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTY.  IN PARTICULAR, THE AUTHORS MAKE NO
 * REPRESENTATION OR WARRANTY OF ANY KIND CONCERNING THE MERCHANTABILITY
 * OF THIS SOFTWARE OR ITS FITNESS FOR ANY PARTICULAR PURPOSE.
 */

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class Display {

	public Display(){}	//do nothing

	public Rectangle getDisplayBounds(int posX, int posY){
        //-----Position the frame based on screen size.
        int w =0 ,h = 0;
		int sloc, padding = 20;
		Rectangle ret = new Rectangle();
	   //---get number of screens
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice[] gs = ge.getScreenDevices();	//---length = number of screens
//		System.out.println("length = number of screens: " + gs.length);

		//even number of screens imply starting x,y coord will be top of some screen
		//odd number of screens imply starting x,y coord will be middle of some screen
		if (Scagnostics.sx %2 ==0){	//even # horizontal screens
			if (Scagnostics.sy %2 ==0){	//even # vertical screens
				sloc = Scagnostics.sx * Scagnostics.sx/2 * posX + Scagnostics.sy/2 * posY;
	    		 ret.x = gs[sloc].getDefaultConfiguration().getBounds().x;
	    		 ret.y = gs[sloc].getDefaultConfiguration().getBounds().y;
				//get height
				for (int i=sloc; i<(Scagnostics.sy*Scagnostics.sx); i+=Scagnostics.sx)	//(sloc+(Scagnostics.sy/2*Scagnostics.sx))
					h += gs[i].getDefaultConfiguration().getBounds().height;
				ret.height = h;
			}
			else{	//odd # of vertical screens
				sloc = Scagnostics.sx * (int)Math.floor(Scagnostics.sy/2.0) * posX + (int)(Scagnostics.sx/2)* posY;
	    		 ret.x = gs[sloc].getDefaultConfiguration().getBounds().x;
	    		 ret.y = gs[sloc].getDefaultConfiguration().getBounds().y + (gs[sloc].getDefaultConfiguration().getBounds().height/2) * posX ;	//middle of screen if 10,11
				//get height
				for (int i=sloc; i<(Scagnostics.sy*Scagnostics.sx); i+=Scagnostics.sx){
					if (i == sloc && posX==1){
						h += gs[i].getDefaultConfiguration().getBounds().height/2;
						continue;
					}
					if ( (i+Scagnostics.sx >=Scagnostics.sx*Scagnostics.sy) && posX==0){
						h += gs[i].getDefaultConfiguration().getBounds().height/2;
						continue;
					}
					h += gs[i].getDefaultConfiguration().getBounds().height;
				}
				ret.height = h;
			}
			//get width
			for (int i=sloc; i<(sloc+Scagnostics.sx/2); i++)
				w += gs[i].getDefaultConfiguration().getBounds().width;
			System.out.println("setting width for even screens: " + w);
			ret.width = w;
		} 	
		else{ //odd number of horizontal screens
			if (Scagnostics.sy %2 ==0){	//even # vertical screens
				sloc = Scagnostics.sx * (int)Scagnostics.sy/2 * posX + (int)Math.floor(Scagnostics.sx/2.0) * posY;
	    		 ret.x = gs[sloc].getDefaultConfiguration().getBounds().x;
	    		 ret.y = gs[sloc].getDefaultConfiguration().getBounds().y + (gs[sloc].getDefaultConfiguration().getBounds().height/2) * posX ;
				//get height
				for (int i=sloc; i<(Scagnostics.sy*Scagnostics.sx); i+=Scagnostics.sx)
					h += gs[i].getDefaultConfiguration().getBounds().height;
				ret.height = h;
			}
			else{	//odd # of vertical screens
				sloc = Scagnostics.sx * (int)Math.floor(Scagnostics.sy/2.0) * posX + (int)Math.floor(Scagnostics.sx/2.0) * posY;
	    		 ret.x = gs[sloc].getDefaultConfiguration().getBounds().x + (gs[sloc].getDefaultConfiguration().getBounds().width/2) * posY ;	//middle of screen if 01,11;
	    		 ret.y = gs[sloc].getDefaultConfiguration().getBounds().y + (gs[sloc].getDefaultConfiguration().getBounds().height/2) * posX ;	//middle of screen if 10,11
				//get height
				for (int i=sloc; i<(Scagnostics.sy*Scagnostics.sx); i+=Scagnostics.sx){
					if (i == sloc && posX==1){
						h += gs[i].getDefaultConfiguration().getBounds().height/2;
						continue;
					}
					if ( (i+Scagnostics.sx >=Scagnostics.sx*Scagnostics.sy) && posX==0){
						h += gs[i].getDefaultConfiguration().getBounds().height/2;
						continue;
					}
					h += gs[i].getDefaultConfiguration().getBounds().height;
				}
				ret.height = h;
			}
			//get width
			for (int i=sloc; i<(sloc+Math.ceil(Scagnostics.sx/2.0)); i++){
				if (i == sloc && posY==1){
					w += gs[i].getDefaultConfiguration().getBounds().width/2;
					continue;
				}
				if ( ( (i+1) >=(sloc+(int)Math.ceil(Scagnostics.sx/2.0)) ) && posY==0){
					w += gs[i].getDefaultConfiguration().getBounds().width/2;
					continue;
				}
				w += gs[i].getDefaultConfiguration().getBounds().width;
			}//end for
			ret.width = w;
		}
//		System.out.println("Ret: X: " + ret.x + " Y: " + ret.y + " width: " + ret.width + " ht: " + ret.height);
		return ret;
		 //----remove taskbar size
//		 gcBounds.height -= Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration()).top;
//		 gcBounds.height -= Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration()).bottom;
	    //-----
		
	}
}
class GraphFrame extends Frame {
    public GraphFrame(String title, int posX, int posY) {
        setTitle(title);
        setBackground(Color.white);

        Display t = new Display();
		Rectangle gcBounds = t.getDisplayBounds(posX, posY);
		 setBounds(gcBounds.x, gcBounds.y, gcBounds.width, gcBounds.height);
//        setBounds(10, 10, size, size);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        show();
    }

}

class ChildFrame extends Frame {
	//*****************************************************  
    public ChildFrame(String title, int size, int posX, int posY) {
	//*****************************************************  
        setTitle(title);
        setBackground(Color.white);
//        setBounds(10, 10, wt, ht);

        Display t = new Display();
		Rectangle gcBounds = t.getDisplayBounds(posX, posY);

		if (size == -1)
			 setBounds(gcBounds.x, gcBounds.y, gcBounds.width, gcBounds.height);
		else
			 setBounds(gcBounds.x, gcBounds.y, size*3, size);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                setVisible(false);
                dispose();
            }
        });

   //     show();
    }

}

class SplomCanvas extends Canvas {
    Frame frame;
    int numRows, numCols, numVars;
    double[][] data;
    String[] labels;
    boolean measure;
    int[] sortOrder = null;
    boolean[] isScagnosticOutlier = null;
    int highlightedRow = -1;

    public SplomCanvas(Frame frame, double[][] data, String[] labels, int[] sortOrder,
                       boolean[] outliers, boolean measure) {
        this.frame = frame;
        this.data = data;
        this.labels = labels;
        this.sortOrder = sortOrder;
        this.isScagnosticOutlier = outliers;
        this.measure = measure;
        numCols = data.length;
        numRows = data[0].length;
        numVars = numCols;
    }

    private void drawFrame(Graphics g, int xloc, int yloc, int size) {
        if (measure) {
            g.setColor(Color.black);
            g.drawRect(xloc, yloc, size, size);
        } else {
            Color c = Color.getHSBColor(.1f, .03f, .9f);
            g.setColor(c);
            g.fillRect(xloc, yloc, size, size);
        }
    }

    private int getCellSize() {
        return (9 * Math.min(frame.getHeight(), frame.getWidth())) / (10 * numVars);
    }

    public void paint(Graphics g) {
        int size = getCellSize();
        int border = size / 20;
        int sb = size + border;
/*
        int[] colors = new int[]{3, 3, 4, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 2, 3, 3, 3, 2, 2, 1, 1, 1, 1, 1, 4,
                                 2, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 4, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 4, 3, 3, 3,
                                 3, 3, 2, 3, 3, 3, 3, 3, 4, 3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 4, 2, 2, 2, 2, 2, 2, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                                 3, 2, 2, 2, 2, 2, 2, 2, 3};
*/
        Rectangle r = getBounds();
        r.height = size;
        r.width = size;
        int symbolSize = 2;
        if (measure)
            symbolSize = 3;
        Color symbolColor = Color.black;
        if (measure)
            symbolColor = Color.gray;
        Color highlightColor = Color.red;
        for (int row = 0; row < numVars; row++) {
            int yloc = row * sb;
            for (int col = 0; col <= row; col++) {
                int xloc = col * sb;
                int icol = col;
                int irow = row;
                if (sortOrder != null) {
                    icol = sortOrder[col];
                    irow = sortOrder[row];
                }
                drawFrame(g, xloc, yloc, size);
                if (row == col) {
                    Font font = new Font("Verdana", Font.PLAIN, r.width / 7);
                    g.setFont(font);
                    g.setColor(Color.black);
                    g.drawString(labels[icol], (xloc + size / 2 - font.getSize() * labels[icol].length() / 3),
                            (yloc + size / 2 + border));
                } else {
                    int colr;
                    if (irow > icol)
                        colr = ((irow) * (irow - 1)) / 2 + icol;
                    else
                        colr = ((icol) * (icol - 1)) / 2 + irow;
                    for (int i = 0; i < numRows; i++) {
                        g.setColor(symbolColor);
                        if (isScagnosticOutlier != null) {
                            if (isScagnosticOutlier[colr])
                                g.setColor(Color.red);
                        }
/*
                        if (colors[colr] == 1) g.setColor(Color.gray);
                        if (colors[colr] == 2) g.setColor(Color.blue);
                        if (colors[colr] == 3) g.setColor(Color.green);
                        if (colors[colr] == 4) g.setColor(Color.red);
*/
                        int ix = xloc + border + (int) (.9 * data[icol][i] * size);
                        int iy = yloc + size - border - (int) (.9 * data[irow][i] * size);
                        g.fillRect(ix, iy, symbolSize, symbolSize);
                    }
                    if (highlightedRow >= 0) {
                        g.setColor(highlightColor);
                        int ix = xloc + border + (int) (.9 * data[icol][highlightedRow] * size);
                        int iy = yloc + size - border - (int) (.9 * data[irow][highlightedRow] * size);
                        g.fillRect(ix, iy, 2 * symbolSize, 2 * symbolSize);
                    }
                }
            }
        }
    }

    public boolean mouseDown(Event evt, int x, int y) {
        if (!measure)
            return false;

        int fuzz = 3;
        int size = getCellSize();
        int border = size / 20;
        int sb = size + border;
        int irow = -1;
        int icol = -1;
        int xloc = 0;
        int yloc = 0;
        for (int row = 1; row < numVars; row++) {
            int yl = row * sb;
            for (int col = 0; col < row; col++) {
                int xl = col * sb;
                if (x > xl && x < xl + sb && y > yl && y < yl + sb) {
                    irow = row;
                    icol = col;
                    xloc = xl;
                    yloc = yl;
                    break;
                }
            }
            if (irow >= 0) break;
        }
        if (irow < 0) {
            highlightedRow = -1;
            return false;
        }

        int numCells = (int) (1 + Math.sqrt(8 * numRows + 1)) / 2;   // numRows is a Triangular number n(n-1)/2
        int k = 0;
        for (int i = 1; i < numCells; i++) {
            for (int j = 0; j < i; j++) {
                int ix = xloc + border + (int) (.9 * data[icol][k] * size);
                int iy = yloc + size - border - (int) (.9 * data[irow][k] * size);
                if (Math.abs(ix - x) < fuzz && Math.abs(iy - y) < fuzz) {
        			//*****************************************************                	
                    drawScatterplot(labels, j, i, k);
        			//*****************************************************                	                    
                    highlightedRow = k;
                    return true;
                }
                k++;
            }
        }
        highlightedRow = -1;
        return false;
    }

    public boolean mouseUp(Event evt, int x, int y) {
        if (!measure)
            return false;
        repaint();
        return true;
    }

   	//***************************************************** size was 325
    private void drawScatterplot(String[] labels, int j, int i, int k) {
//       Frame f = new ChildFrame("x: " + labels[j] + " y: " + labels[i], 325, 900);
        Frame f = new ChildFrame("x: " + labels[j] + " y: " + labels[i], 350, 0,0);	//position 0	
        //*****************************************************
        Binner b = new Binner();
        BinnedData bdata = b.binHex(Scagnostics.data[j], Scagnostics.data[i], BinnedData.BINS);
        Triangulation dt = new Triangulation();
        dt.compute(bdata, true);
        //*****************************************************
        ScatterplotCanvas sc = new ScatterplotCanvas(f, dt, k);
        //*****************************************************
        f.add(sc);
        sc.repaint();
        f.show();
    }

}

class TestCanvas extends Canvas{

	Panel pan;

	public TestCanvas(Panel p){
			this.pan=p;
		setSize(50,50);
		setBackground(Color.BLACK);
		System.out.println("Construct: " + pan.getHeight() + " " + pan.getWidth() );
	}	

	public void paint(Graphics g){
		System.out.println("Paint: " + pan.getHeight() + " " + pan.getWidth() );
		g.drawRect(10,10,10,10);
	}

}

class RankCanvas extends Canvas{
	//*****************************************************  frame->panel
	Panel panel;
	//*****************************************************  
    int numRows, numCols, numVars, numPlots, numMeasures, numCells;
    double[][] data;
    String[] labels;
    int[][] sortOrder = null;
    double[][] measures;
    int highlightedRow = -1;
	//*****************************************************  
	double limit = 0.1;	//---cutoff for rank to be displayed
	//*****************************************************  

	//*****************************************************  frame->panel 
    public RankCanvas(Panel panl, double[][] data, String[] labels, int[][] sortOrder, double[][] measures) {
		this.panel = panl;
		setBounds(4,21, panel.getWidth(),panel.getHeight());
        Rectangle r = getBounds();
//		System.out.println("CONST: Bounds: " + r.x + " " + r.y + " " + r.width + "  " + r.height);
	//*****************************************************  
        this.data = data;
        this.labels = labels;
        this.sortOrder = sortOrder;
        this.measures = measures;
        numCols = data.length;
        numRows = data[0].length;
        numVars = numCols;
        numCells = numVars * (numVars - 1) / 2;
        numPlots = 10;
        numMeasures = 9;
    }

    private void drawFrame(Graphics g, int xloc, int yloc, int size) {
        g.setColor(Color.BLUE);
        g.drawRect(xloc, yloc, size, size);
    }

    private int getCellSize() {
		//*****************************************************  frame->panel
        return (7 * Math.min(panel.getHeight(), panel.getWidth())) / (10 * numPlots);
		//*****************************************************  
    }

    public void paint(Graphics g) {
        int size = getCellSize();
        int border = size / 10;
        int sb = 4 + size + border;
		setBounds(4,21, panel.getWidth(),panel.getHeight());
        Rectangle r = getBounds();
//		System.out.println("PAINT Bounds: " + r.x + " " + r.y + " " + r.width + "  " + r.height);
        r.height = size;
        r.width = size;
        int symbolSize = 2;
        Color symbolColor = Color.BLACK;
        int k = 0;
        int[][] indices = new int[2][numVars * (numVars - 1) / 2];
        for (int i = 1; i < numVars; i++) {
            for (int j = 0; j < i; j++) {
                indices[0][k] = j;
                indices[1][k] = i;
                k++;
            }
        }
        int origin = 30;
        for (int j = 0; j < numMeasures; j++) {
            int xloc = origin + j * (15 + sb);
            Font font = new Font("Verdana", Font.BOLD, r.width / 5);
            g.setFont(font);
            g.setColor(Color.black);
            g.drawString(labels[j], (xloc + size / 2 - font.getSize() * labels[j].length() / 3),
                    origin - 10);
        }
        for (int m = 0; m < Math.min(numPlots, numCells); m++) {
            int yloc = origin + m * sb;
            for (int j = 0; j < numMeasures; j++) {
                int xloc = origin + j * (15 + sb);
                int index = sortOrder[j][numCells - m - 1];
                double measure = measures[j][index];
		//*****************************************************  
                if (measure < limit)
		//*****************************************************  
                    continue;
                int icol = indices[0][index];
                int irow = indices[1][index];
                int ix = 0;
                int iy = 0;
                drawFrame(g, xloc, yloc, size);
                for (int i = 0; i < numRows; i++) {
                    g.setColor(symbolColor);
                    ix = xloc + border + (int) (.9 * data[icol][i] * size);
                    iy = yloc + size - border - (int) (.9 * data[irow][i] * size);
                    g.fillRect(ix, iy, symbolSize, symbolSize);
                }
                g.setColor(Color.red);
                ix = xloc + (size * icol / numVars);
                iy = yloc - 5;
                g.fillRect(ix, iy, 2, 5);
                ix = xloc + (size * irow / numVars);
                g.fillRect(ix, iy, 2, 5);
            }
        }
    }

	//*****************************************************  
	public void setLimit(double lim) {	//---limit for cutoff of ranks
        this.limit = lim;
		repaint();
    }
	//*****************************************************  
}

class ScatterplotCanvas extends Canvas {
    Frame frame;
    Triangulation dt;
	//*****************************************************  
	int index; 

    public ScatterplotCanvas(Frame frame, Triangulation dt, int ind) {
		this.index = ind;
	//*****************************************************  
        this.frame = frame;
        this.dt = dt;
    }

    public void paint(Graphics g) {
		//*****************************************************  
		//---divide the display into three sections
       int size = 8 * Math.min(frame.getHeight(), frame.getWidth()/3) / 10;
		//*****************************************************
        Renderers.drawComplex(dt.nodes, dt.triangles, g, Color.getHSBColor(.1f, .03f, .9f), 20, 20, size, size, 0, 1000);
        Renderers.drawPoints(dt, g, Color.black, 20, 20, size, size, 0, 1000);
        Renderers.drawMST(dt, g, Color.green, 20, 20, size, size, 0, 1000);
//        Renderers.drawHull(g, Color.BLACK, 10, 10, size, size, 0, 1000);
//        Renderers.drawShape(g, Color.red, 10, 10, size, size, 0, 1000);
//        g.setColor(Color.BLACK);
//        g.drawRect(4, 4, 100*size/96, 100*size/96);

		//***************************************************** 
		//---add text for the key
		Font font = new Font("Verdana", Font.BOLD, frame.getWidth()/65);		//chose 65 based on what fit and looked legible 
        g.setFont(font);
		g.setColor(Color.BLUE);
		g.drawString("MEASURES", size+65, 3*frame.getWidth()/65);
		g.drawString("HISTOGRAM", size+65, 4*frame.getWidth()/65);
		g.drawString("KEY", size+70, 10*frame.getWidth()/65);
		g.setColor(Color.RED);
		g.drawString("Measures of", size+65, 12*frame.getWidth()/65);
		g.drawString("plot on left", size+65, 13*frame.getWidth()/65);
		g.setColor(Color.BLACK);
		g.drawString("Average of", size+65, 15*frame.getWidth()/65);
		g.drawString("measures of", size+65, 16*frame.getWidth()/65);
		g.drawString("other plots", size+65, 17*frame.getWidth()/65);
		Renderers.drawHistogram(g, index, frame.getWidth()/3 + 110 ,20 , size,  frame.getWidth(),  0, 1000);
		//*****************************************************  

    }
}


