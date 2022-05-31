package CPU;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

public class TemperatureParser{


    /**
     * From: https://git.cs.odu.edu/tkennedy/cs417-lecture-examples/-/blob/master/SemesterProject-CPU-Temps
     * /java/src/main/java/edu/odu/cs/cs417/TemperatureParser.java
     * 
     * A pair of values where:
     * <ul>
     *   <li> _step_ attribute represents the time at which the reading was
     *        taken.
     *   </li>
     *   <li> _readings_ is a vector with _n_ temperature readings,
     *        where _n_ is the number of CPU Cores.
     *   </li>
     * </ul>
     */
	
	
	 public static void main(String[] args) throws IOException {
		    if (args.length == 0) {
		         System.out.println("Need file input");
		       
		    } else {
		    	
		    	//write output to files
		    	File inputFile = new File(args[0]);
		    	File file0 = new File("CPU-core-0.txt");
				File file1 = new File("CPU-core-1.txt");
		    	File file2 = new File("CPU-core-2.txt");
		    	File file3 = new File("CPU-core-3.txt");
		    	
		    	FileWriter fw0 = new FileWriter(file0);
		    	FileWriter fw1 = new FileWriter(file1);
		    	FileWriter fw2 = new FileWriter(file2);
		    	FileWriter fw3 = new FileWriter(file3);
		    	
		    	PrintWriter pw0 = new PrintWriter(fw0);
		    	PrintWriter pw1 = new PrintWriter(fw1);
		    	PrintWriter pw2 = new PrintWriter(fw2);
		    	PrintWriter pw3 = new PrintWriter(fw3);

		    	pw0.print(LinearInterpolater.Interpolater(inputFile, 30, 0));
		    	pw0.print(LeastSquaresApproximator.Approximator(inputFile, 30, 0));
		    	pw0.close();
		    	
		    	pw1.print(LinearInterpolater.Interpolater(inputFile, 30, 1));
		    	pw1.print(LeastSquaresApproximator.Approximator(inputFile, 30, 1));
		    	pw1.close();
		    	
		    	pw2.print(LinearInterpolater.Interpolater(inputFile, 30, 2));
		    	pw2.print(LeastSquaresApproximator.Approximator(inputFile, 30, 2));
		    	pw2.close();
		    	
		    	pw3.print(LinearInterpolater.Interpolater(inputFile, 30, 2));
		    	pw3.print(LeastSquaresApproximator.Approximator(inputFile, 30, 0));
		    	pw3.close();
		    }
     }
	
    public static class CoreTempReading
    {
    	/**
    	 * Core we want
    	 */
    	public int core;
    	
        /**
         * Time-step at which the readings were measured.
         */
        public int step;

        /**
         * Temperature readings (one per CPU core).
         */
        public double[] readings;

        /**
         * Create a reading for the specified time-step.
         *
         * @param time time-step at which the readings were measured
         * @param theReadings temperature readings (one per core)
         */
        public CoreTempReading(int time, double[] theReadings)
        {
            this.step = time;
            this.readings = theReadings;
        }

        /**
         * Generate a printable form of the temperature reading in the
         * form (time, [core_0, core_1, ... core_{n-1}]).
         *
         * @return the string representation
         */
    }

    /**
     * Take an input file and parse all core temps. Assume a step size of
     * 30 seconds.
     *
     * @param inputTemps an input file
     *
     * @return a vector of 2-tuples (pairs) containing time step and core
     *         temperature readings
     */
    public static List<CoreTempReading> parseRawTemps(BufferedReader inputTemps)
    {
        return parseRawTemps(inputTemps, 30);
    }

    /**
     * Take an input file and time-step size and parse all core temps.
     *
     * @param inputTemps an input file
     * @param stepSize time-step in seconds
     *
     * @return a vector of 2-tuples (pairs) containing time step and core
     *         temperature readings
     */
    public static List<CoreTempReading> parseRawTemps(BufferedReader inputTemps,
                                        int stepSize)
    {
        String[][] rawLines = inputTemps.lines()
                                        .map(s -> s.split("([^0-9]*\\s)|([^0-9]*$)"))
                                        .toArray(String[][]::new);

        List<CoreTempReading> allReadings = new Vector<>(rawLines.length);

        int step = 0;
        for (String[] line : rawLines) {
            double[] tempReadings = new double[line.length];

            for (int i = 0; i < tempReadings.length; i++) {
                tempReadings[i] = Double.parseDouble(line[i]);
            }
            
            allReadings.add(new CoreTempReading(step, tempReadings));

            step += stepSize;
        }

        return allReadings;
    }
}

