package CPU;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


//https://www.sanfoundry.com/java-program-gaussian-elimination-algorithm/

public class LeastSquaresApproximator {

	/**
	 * Performs least squares approximation given input temps,
	 * core, and step size
	 * 
	 * @param inputTemps file to read temps from
	 * @param stepSize time between readings
	 * @param core CPU core to be approximated
	 * 
	 * @return polynomial least squares approximation
	 * 
	 * @throws IOException necessary for file readers
	 */
	public static String Approximator(File inputTemps, int stepSize, int core) 
			throws IOException {
		
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(inputTemps));
		int tempsSize = TemperatureParser.parseRawTemps(br, stepSize).size();
		
		double[][] x = new double[tempsSize][2];
		double[][] xT = new double[2][tempsSize];
		double[] y = new double[tempsSize];
		long[][] xTx = new long[2][2];
		long[] xTy = new long[2]; 
		
		for (int i = 0; i < tempsSize; i++) {
			BufferedReader br1 = new BufferedReader(new FileReader(inputTemps));
			
			BufferedReader br2 = new BufferedReader(new FileReader(inputTemps));

			x[i][0] = 1;
			x[i][1] = TemperatureParser.parseRawTemps(br1, stepSize).get(i).step;
			
			xT[0][i] = 1;
			xT[1][i] = x[i][1];

			y[i] = TemperatureParser.parseRawTemps(br2, stepSize).get(i).readings[core];
			
			br1.close();
			br2.close();
		}
		
		for (int k = 0; k < 2; k++) {
			for (int j = 0; j < 2; j++) {
				for (int i = 0; i < tempsSize; i++) {
					xTx[k][j] += xT[k][i]*x[i][j];
				}
			}
		}
		
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < tempsSize; i++) {
				xTy[j] += xT[j][i]*y[i];
			}
		}		
		
		BufferedReader br3 = new BufferedReader(new FileReader(inputTemps));
		int lastStep = TemperatureParser.parseRawTemps(br3, stepSize).get(tempsSize-1).step;
		br3.close();
		
		solve(xTx, xTy);
		
	    return ("\n0 <= x < " + lastStep + "; y = " + LinearInterpolater.round(getSolution()[0], 4) 
	    	+ " + " + LinearInterpolater.round(getSolution()[1], 4) + "x; least-squares");
		   
	}
	/**
	 * Solution to Gaussian elimination of matrix
	 */
	private static double[] solution; 
	
	/**
	 * Accessor for solution
	 * @return solution
	 */
	private static double[] getSolution() {
		return solution;
	}
	
	/**
	 * Setter function for solution
	 * @param solution to store
	 */
	private static void setSolution(double[] sol) {
		solution = sol;
	}
	
	/**
	 * Performs Gaussian elimination on augmented matrix
	 * Source: https://www.sanfoundry.com/java-program-gaussian-elimination-algorithm/
	 *
	 * @param xTx 2 dimensional array of longs
	 * @param xTy array of longs 
	 */
	public static void solve(long[][] xTx, long[] xTy) {
	        int N = xTy.length;
	        for (int k = 0; k < N; k++) 
	        {
	            /** find pivot row **/
	            int max = k;
	            for (int i = k + 1; i < N; i++) 
	                if (Math.abs(xTx[i][k]) > Math.abs(xTx[max][k])) 
	                    max = i;
	 
	            /** swap row in A matrix **/    
	            long[] temp = xTx[k]; 
	            xTx[k] = xTx[max]; 
	            xTx[max] = temp;
	 
	            /** swap corresponding values in constants matrix **/
	            long t = xTy[k]; 
	            xTy[k] = xTy[max]; 
	            xTy[max] = t;
	 
	            /** pivot within A and B **/
	            for (int i = k + 1; i < N; i++) 
	            {
	                double factor = xTx[i][k] / xTx[k][k];
	                xTy[i] -= factor * xTy[k];
	                for (int j = k; j < N; j++) 
	                    xTx[i][j] -= factor * xTx[k][j];
	            }
	        }
	 
	        /** back substitution **/
	        double[] locSolution = new double[N];
	        setSolution(locSolution);
	        for (int i = N - 1; i >= 0; i--) 
	        {
	            double sum = 0.0;
	            for (int j = i + 1; j < N; j++) 
	                sum += xTx[i][j] * solution[j];
	            solution[i] = (xTy[i] - sum) / xTx[i][i];
	        }        
	    }
	
	
}
