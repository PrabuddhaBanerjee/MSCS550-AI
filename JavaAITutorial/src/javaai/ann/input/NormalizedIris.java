/**
 *Copyright (c) Prabuddha Banerjee
 *
 */
package javaai.ann.input;

import javaai.util.Helper;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * This class normalizes the real-world iris data and gives its report.
 * @author Prabuddha Banerjee
 * @version 1.0
 * @since   2021-08-21
 */

public class NormalizedIris extends RealWorld {
    /** The high range index */
    public final static int HI = 1;

    /** The low range index */
    public final static int LO = 0;

    /** Normalized data stored here; reals are instantiated by RealWorld. */
    protected static HashMap<String, List<Double>> normals = new HashMap<>();

    /**
     * Launches the program.
     * @param args Command line arguments -- not used
     */
    public static void main(String[] args) {
        // Load the iris data into reals
        load();

        // Get the titles of each column
        Set<String> titles = reals.keySet();

        // Normalize each column by title
        for(String title: titles) {
            // Get the column data for this title
            List<Double> list = reals.get(title);

            // Get range for this column using elementary form of unsupervised learning
            double[] range = getRange(list);

            // Summarize range for this column for its title
            System.out.printf("%-12s: %6.2f - %5.2f\n", title, range[LO], range[HI]);

            // Creating a NormalizedField instance using the hi-lo range.
            // For reference see Encog's SimpleNormalize.java.
            NormalizedField norm = new NormalizedField(NormalizationAction.Normalize,
                    null,range[HI],range[LO],1,-1);

            // List will contain normalized iris data for this column.
            List<Double> normalized = new ArrayList<>();

            // Normalizing each element of reals and add it to normalized.

            for(int i = 0; i< reals.get(title).size(); i++){
                double val = norm.normalize(reals.get(title).get(i));
                normalized.add(val);
            }


            // Add normalized data to the normals for this title.
            normals.put(title, normalized);
        }

        // Write rest of the report

        // This is the column header
        System.out.printf("%3s ","#");

        for(String key: titles)
            System.out.printf("%15s ",key);

        System.out.println();

        // Now write the row by row data -- it should line up right-justified
        int size =  Helper.getRowCount();

        for(int k=0; k < size; k++) {
            System.out.printf("%3d ",k);

            for(String key: titles) {
                Double real = reals.get(key).get(k);

                Double normal = normals.get(key).get(k);

                System.out.printf("%6.2f => %5.2f ",real,normal);
            }

            System.out.println();
        }

    }

    /**
     * Gets hi-lo range using an elementary form of unsupervised learning.
     * @param list List
     * @return 2-tuple of doubles for low and high range
     */
    protected static double[] getRange(List<Double> list) {
        // Initial low and high values
        double[] range = {Double.MAX_VALUE, -Double.MAX_VALUE};

        // Go through each value in the list
        for(Double value: list) {
            // Assign if value greater than range[HI], update range[HI].
            if( value > range[HI])
                range[HI] = value;

            // Assign if value less than range[LO], update range[LO].
            if( range[LO] > value)
                range[LO] = value;
        }

        return range;
    }
}