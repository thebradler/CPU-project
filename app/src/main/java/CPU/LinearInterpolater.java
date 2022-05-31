package CPU;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class LinearInterpolater {
	 
	/**
	 * Performs piece-wise linear interpolation given temps,
	 * stepsize, and CPU core
	 * 
	 * @param inputTemps file to read temps from
	 * @param stepSize time between temp readings in seconds
	 * @param core CPU core to analyze
	 * 
	 * @return list of linear interpolation polynomials
	 * 
	 * @throws IOException necessary for file readers
	 */
	public static ArrayList<String> Interpolater(File inputTemps,
            int stepSize, int core) throws IOException {
		
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(inputTemps));
		int tempsSize = TemperatureParser.parseRawTemps(br, stepSize).size();

		ArrayList<String> lines = new ArrayList<String>();
				
		for (int i=0; i < tempsSize - 1; i++){
			
			BufferedReader br1 = new BufferedReader(new FileReader(inputTemps));
			double x0 = TemperatureParser.parseRawTemps(br1, stepSize).get(i).step;
			
			BufferedReader br2 = new BufferedReader(new FileReader(inputTemps));	
			double x1 = TemperatureParser.parseRawTemps(br2, stepSize).get(i+1).step;
			
			BufferedReader br3= new BufferedReader(new FileReader(inputTemps));
			double y0 = TemperatureParser.parseRawTemps(br3, stepSize).get(i).readings[core];
			
			BufferedReader br4= new BufferedReader(new FileReader(inputTemps));
			double y1 = TemperatureParser.parseRawTemps(br4, stepSize).get(i+1).readings[core];	
			
			double secondTerm = (y1-y0)/(x1-x0);

			String line = new String("");
			line = (x0 + " <= x < " + x1 + "; y_" + i + "= " + round(y1-(secondTerm*x0), 4)+ 
					" + " +round(secondTerm, 4)+ "x; interpolation\n" );
			lines.add(line);
			
			br1.close();
			br2.close();
			br3.close();
			br4.close();
		}
		
		return lines;
	}
	
	/**
     * takes a double and rounds it to specified decimal places 
     * From: https://www.baeldung.com/java-round-decimal-number
     *
     * @param value a double to be rounded
     * @param places number of decimal places to round to
     *
     * @return rounded double
     * 
     * @throws IllegalArgumentException if argument is invalid
     */
	static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
}

