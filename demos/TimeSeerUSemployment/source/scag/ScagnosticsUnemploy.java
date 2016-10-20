package scag;

import java.io.*;
import java.util.HashMap;

import processing.core.PApplet;

public class ScagnosticsUnemploy extends PApplet {
	public static int nS = 49;
	public static int nI = 13;
	public static int nM = 22 * 12;
	public static double[][][] data = new double[nM][nI+1][nS];
	public static double[][][] data2 = new double[nM][4][nS];

	private static int numScagnostics = Triangulation.numScagnostics;
	public static double[][][] scagnostics = null;
	public static String[] scagnosticsLabels = Triangulation.scagnosticsLabels;
	public static int sx, sy;
	public static HashMap<String,String> hm = new HashMap<String,String>();
	public static HashMap<String,Integer> hi = new HashMap<String,Integer>();
	
	public static String line1 =""; 
	
	public static void main(String argv[]) {
		getIndustry();
		getData();
		
		computeEmpRate();
		writeEmpRate();
		
		// Write data
		normalizeMinMax();
		writeStandardizedData();
		
		// Write Scagnostics
		computeScagnosticsOnFileData();
		for (int m = 0; m < nM; m++) {
			writeOut(m);
		}	
		
	}

	private static void getIndustry() {
		ScagnosticsUnemploy w = new ScagnosticsUnemploy();
		String[] lines = w.loadStrings("IndustryFiltered.txt");
		for (int k = 0; k < lines.length; k++) {
			String[] p = lines[k].split("\t");
			hm.put(p[0], p[1]);
			hi.put(p[0], k);
		}
	}	
	private static void getData() {
		ScagnosticsUnemploy w = new ScagnosticsUnemploy();
		String[] lines = w.loadStrings("employ1.txt");
		line1 = lines[0];
		int state = -1;
		String pS ="";
		for (int k = 1; k < lines.length; k++) {
			String[] p = lines[k].split(",");
			
			if (!p[0].substring(0, 5).equals(pS)){
				state++;
				pS = p[0].substring(0, 5);
			}
			String code = p[0].substring(10, 18);
			System.out.println(code+" = "+lines[k]);
			
			int ins = hi.get(code);
			System.out.println("Industry: "+ins);
			int m = 0;
			for (int k2 = 0; k2 < p.length; k2++) {
				if (k2 % 13 == 0)
					continue; // Remove annual data

				String str = p[k2].trim();
				if (str.contains("(P)")) {
					str = str.replace("(P)", "");
				} else if (str.equals(""))
					str ="0";
				
				data[m][ins][state] = Double.parseDouble(str);
				m++;
			}
		}
		
		lines = w.loadStrings("unemploy.txt");
		for (int k = 1; k < lines.length; k++) {
			int s = (k - 1) / 4;
			int ins = (k - 1) % 4;
			String[] p = lines[k].split("\t");
			int m = 0;
			for (int k2 = 1; k2 < p.length; k2++) {
				if (k2 % 13 == 0)
					continue; // Remove annual data

				String str = p[k2].trim();
				if (p[k2].contains("(P)")) {
					str = p[k2].replace("(P)", "");
				} else if (str.equals(""))
					break;
				data2[m][ins][s] = Double.parseDouble(str);
				
				if (ins==0){
					data[m][nI][s] = Double.parseDouble(str);
					data[m][nI][s] = 100 -data[m][nI][s];
				}
				m++;
			}
		}
	}

	private static void computeScagnosticsOnFileData() {
		int numCells = nI * (nI + 1) / 2;
		scagnostics = new double[nM][numScagnostics][numCells];
		for (int m = 0; m < nM; m++) {
			int k = 0;
			for (int i = 1; i <= nI; i++) {
				for (int j = 0; j < i; j++) {
						Binner b = new Binner();
						BinnedData bdata = b.binHex(data[m][j], data[m][i],
								BinnedData.BINS);
						Triangulation dt = new Triangulation();
						double[] mt = dt.compute(bdata, false);
						if (mt == null)
							continue;
						for (int scag = 0; scag < numScagnostics; scag++) {
							if (Double.isNaN(mt[scag]))
								mt[scag] = 0;
							scagnostics[m][scag][k] = mt[scag];
						}
						k++;
					
				}
			}
		}
	}

	private static void writeStandardizedData() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("src/employ2.txt"));
            out.write(line1);
            out.newLine();
                for (int s = 0; s < nS; s++) {
	                for (int i = 0; i <= nI; i++) {
	                	for (int m = 0; m < nM; m++) {
	                    	 out.write(data[m][i][s] + "\t");
	  	                }
	                    out.newLine();
	            	}
	            }
            out.close();
        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error writing file", "Alert",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
	
	private static void writeOut(int m) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("src/Output/scagnostics"+m+".txt"));
            for (int i = 0; i < scagnosticsLabels.length; i++) {
                out.write(scagnosticsLabels[i] + " ");
            }
            out.newLine();
            
            for (int j = 0; j < scagnostics[m][0].length; j++) {
                for (int i = 0; i < scagnostics[m].length; i++) {
                    out.write(scagnostics[m][i][j] + " ");
                }
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error writing file", "Alert",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
	
	private static void computeEmpRate() {
		// Compute Employment Rate
		for (int m = 0; m <	nM; m++) {
			for (int i = 0; i < nI; i++) {
				for (int s = 0; s < nS; s++){
					data[m][i][s] = (data[m][i][s]*1000*100/data2[m][3][s]);	
				}	
			}
		}
		for (int m = nM-1; m >=0; m--) {
			for (int i = nI; i <= nI; i++) {
				for (int s = 0; s < nS; s++) {
						if (m==0)
							data[m][i][s]=0;
						else
							data[m][i][s] = data[m][i][s]-data[m-1][i][s];
					
				}
			}
		}
	}
	private static void writeEmpRate() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("src/employRate.txt"));
            out.write(line1);
            out.newLine();
                for (int s = 0; s < nS; s++) {
	                for (int i = 0; i <= nI; i++) {
	                    for (int m = 0; m < nM; m++) {
	                    	 out.write(data[m][i][s] + "\t");
	  	                }
	                    out.newLine();
	            	}
	            }
            out.close();
        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error writing file", "Alert",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
	
	private static void normalizeMinMax() {
		double[][] dataMin = new double[nM][nI+1];
		double[][] dataMax = new double[nM][nI+1];
		for (int m = 0; m < nM; m++) {
			for (int i = 0; i <= nI; i++) {
				dataMin[m][i] = Double.POSITIVE_INFINITY;
				dataMax[m][i] = Double.NEGATIVE_INFINITY;
				for (int s = 0; s < nS; s++) {
					if (data[m][i][s] < dataMin[m][i])
						dataMin[m][i] = data[m][i][s];
					if (data[m][i][s] > dataMax[m][i])
						dataMax[m][i] = data[m][i][s];
				}
			}
		}
		// Normalize
		for (int m = 0; m < nM; m++) {
			for (int i = 0; i <= nI; i++) {
				//if (m%12==0)
				//	System.out.println();
				for (int s = 0; s < nS; s++) {
					//if (m%12==0 && (data[m][i][s]==dataMax[m][i] || data[m][i][s]==dataMin[m][i]))
					//	System.out.println(s+"  "+data[m][i][s] +"  "+dataMax[m][i]);
					
					data[m][i][s] = (data[m][i][s] - dataMin[m][i])
							/ (dataMax[m][i] - dataMin[m][i]);
						
				}
			}
		}
	}

}