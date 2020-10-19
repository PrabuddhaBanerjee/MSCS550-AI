/**
 *Copyright (c) Prabuddha Banerjee
 *
 */
package javaai.aann;

import java.util.HashMap;
import java.util.Map;
/**
 * The ML3 program extends the ML2 class. This program is to test performance
 * of NNS, initially program reads iris.csv and then tests and classifies the
 * values and prints the accuracy of artificial ann.
 * @author Prabuddha Banerjee
 * @version 1.0
 * @since   2020-09-09
 */
public class ML3 extends ML2{

    //final value to idealize the training in percentage
    public final static double TRAINING_PERCENT = 0.80;

        /**
         * This is the main method which instantiates Hashmap and makes use of
         * load method. Further, it tests the accuracy in for loop and prints the
         * result.
         * @param args Unused.
         * @return Nothing.
         */
    public static void main(String[] args){

        //Instantiate Hashmap to store ideal values
        Map<Measure,Species> ideals = new HashMap<>();

        //loads the ideals
        load(ideals);

        //additional test case to containing random valid value with result
        //known as "null"
        measures.add(new Measure());

        //To Calculate the testing start index
        int testingStart = (int) (ideals.size() * TRAINING_PERCENT);

        //Instantiate NNSmap
        NNSMap nns = new NNSMap();

        //Training of nns
        for(int k=0; k < testingStart; k++)
            nns.put(measures.get(k), flowers.get(k));

        // Instruments for performance analysis
        int tried = 0;
        int missed = 0;

        //For populating the header format
        String heading = String.format( "%-3s %-24s %10s %10s",
                "#","Measure","Ideal","Actual");

        //Print Header of each column
        System.out.println(heading);

        //Testing Driver Loop
        for(int k = testingStart; k < measures.size(); k++) {

            //Fetch a test measure
            Measure test = measures.get(k);

            //Increment number of tries
            tried++;

            //tests through ideals
            Species ideal = ideals.get(test);

            //tests through actuals
            Species actual = nns.get(test);

            //compares actual and ideal value
//            System.out.println("!!!!!!"+actual+" "+ideal);
            if (actual==ideal) {

                //outputs the result for accurate tests by formatting string
                String result =  String.format("%-3s %-24s %10s %10s",
                        tried+".", test, ideal, actual);
                System.out.println(result);
            } else {

                //outputs the result for discrepancy tests by formatting string
                String resultMiss = String.format("%-3s %-24s %10s %10s %3s",
                        tried+".", test, ideal, actual, "MISSED!");
                System.out.println(resultMiss);

                //incrementing missed count
                missed++;
            }
        }

        //calculate the accuracy by count of tries and misses
        double accuracy =  ((tried - missed) / (double) tried) * 100;

        //Populate the value of accuracy with count of tried and correct tries.
        System.out.println("accuracy: " + (tried - missed) + " out of " +
                tried + " or " + Math.round(accuracy) + "%");
    }
}
