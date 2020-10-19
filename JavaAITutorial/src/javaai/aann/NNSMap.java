/**
 *Copyright (c) Prabuddha Banerjee
 *
 */
package javaai.aann;

import java.util.HashMap;
import java.util.Set;

/**
 * The NNSMap program extends the HashMap class. This program is for finding nearest neighbour species,
 * for a given Measure
 * @author Prabuddha Banerjee
 * @version 1.0
 * @since   2020-08-31
 */
public class NNSMap extends HashMap<Measure, Species>{

    /**
     * This is the get method which predicts the nearest species for any given measure
     * @param dest : This is only parameter to find the nearest species of measure
     * @return prediction.
     */
    @Override
    public Species get(Object dest){
        //Check for dest to cast of measure and avoid cast exception
        if (!(dest instanceof Measure)){
            return null;
        }

        // Starting minimum distance -- the maximum possible value!
        double misDist = Double.MAX_VALUE;

        // Arbitrarily choose a nearest measure
        Set<Measure> keys = this.keySet();
        Measure nearest  = (Measure) keys.toArray()[0];

        // Search each measure in the hashmap
        for(Measure src:keys){
            // Pass 1: get the distance from this src to dest measure
            double dist = getDistance(src, (Measure)dest);

            // If we’re closer than before, update the nearest
            if(dist < misDist){
                misDist = dist;
                nearest = src;
            }
        }

        // Pass 2: get the species prediction for the nearest one
        Species prediction = super.get(nearest);
        return prediction;

    }

    /**
     * This is the getDistance method which calculates the Euclidean distance metric for
     * any given src and dest
     * @param src : This parameter is for finding the distance upto dest
     * @param dest : This parameter is for finding the distance from src
     * @return dist2.
     */
    private double getDistance(Measure src, Measure dest) {

        // This is the accumulator
        double dist2 = 0;

        // The sepal & petal values are in a 4D array.
        for(int k = 0; k< src.values.length; k++){

            // Get the difference or delta
            double delta = src.values[k] - dest.values[k];

            // Sum the square differences
            dist2 += (delta * delta);
        }

        // The metric is the sum of square differences.
        // The square root of dist2, is the Euclidean distance.
        return dist2;
    }
}
