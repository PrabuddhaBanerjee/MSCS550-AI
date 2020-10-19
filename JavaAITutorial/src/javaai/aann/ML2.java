/**
 *Copyright (c) Prabuddha Banerjee
 *
 */
package javaai.aann;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The ML2 program extends the ML1 class. This program is for finding and printing nearest neighbour,
 * as well as finding the species to which it corresponds.
 * @author Prabuddha Banerjee
 * @version 1.0
 * @since   2020-08-31
 */
public class ML2 extends ML1{

    /**
     * This is the main method which instantiates nnsmap and makes use of load method.
     * Further, there is a for loop which prints the value of measures.
     * @param args Unused.
     * @return Nothing.
     */
    public static void main(String[] args){
        /** Instantiate measure to speices map */
        NNSMap map = new NNSMap();

        /** Populate map */
        load(map);

        /** List of measures for test case */
        List<Measure> tests =
                new ArrayList<>(Arrays.asList(new Measure(5.1, 3.5, 1.4, 0.2),
                        new Measure(5.1,3.6,1.5,0.4),
                        new Measure(0.0,0.0, 0.0, 0.0),
                        new Measure(100.0,100.0, 100.0, 100.0),
                        new Measure(-1.0, -1.0, -1.0, -1.0)));

        /** examine and print the classifications */
        for(Measure measure: tests)
            System.out.println(map.get(measure)+" "+measure);
    }
}
