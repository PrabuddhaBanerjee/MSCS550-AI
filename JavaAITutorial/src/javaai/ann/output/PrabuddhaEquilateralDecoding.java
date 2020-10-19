/*
 Copyright (c) Prabuddha Banerjee
 *
 */
package javaai.ann.output;

import javaai.util.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
/**
 * This class tests equilateral decoding tolerance for the iris data set.
 * @author Prabuddha Banerjee
 */
public class PrabuddhaEquilateralDecoding {

    /** Number of tests to run */
    public final static int NUM_TESTS = 100;

    /** Tolerance as a percent, e.g., 1.0 == 1% */
    public final static double PERTURBANCE = 50.0;

    /** Copies the values from running EquilateralEncoding */
    static double ideals[][] = {
            {-0.8660, -0.5000},   // Viginica
            { 0.8660, -0.5000},   // Setosa
            { 0.0000,  1.0000}    // Versicolor
    };

    /** Specie names -- order MUST correspond to measures */
    static final List<String> species =
            new ArrayList<>(Arrays.asList("viginica", "setosa", "versicolor"));

    /**
     * Launch point for program.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        EquilateralEncoding.load();

        Random ran = new Random(0);

        int success = 0;

        //For populating the header format
        String heading = String.format( "%-4s %10s %18s %1s %10s %18s",
                "#","ideal","Encoding","|","actual","Encoding");

        //Print Header of each column
        System.out.println(heading);

        for(int n=0; n < NUM_TESTS; n++) {
            // Pick a species randomly
            int idealIndex = ran.nextInt(ideals.length);

            // Get a random encoding from ideals using idealIndex.
            double[] encodings = ideals[idealIndex];

            // Create a new array of activations perturbed by the tolerance divided by 100.
            double[] activations = new double[encodings.length];
            for(int k=0; k < encodings.length; k++) {
                double epsilon = 1 + ran.nextGaussian() * PERTURBANCE / 100.0;
                activations[k] = encodings[k] * epsilon;
            }

            // Decode these perturbed activations.
            int actualIndex = EquilateralEncoding.eq.decode(activations);

            // If the predicted index equals the actual index, updated success count.
            if (idealIndex == actualIndex) {
                success ++;
                //outputs the result by formatting string
                String result =  String.format("%-4s %10s %18s %1s %10s %18s",
                        (n+1)+".", species.get(actualIndex), Helper.asString(activations), "|",species.get(actualIndex),
                        Helper.asString(encodings));
                System.out.println(result);
            }
            else {
                //outputs the result by formatting string in case of misses
                String resultMiss = String.format("%-4s %10s %18s %1s %10s %18s %3s",
                        (n+1)+".", species.get(actualIndex), Helper.asString(activations), "|",species.get(actualIndex),
                        Helper.asString(encodings), "MISSED!");
                System.out.println(resultMiss);
            }

        }

        double rate = (double)success / NUM_TESTS;

        System.out.printf("accuracy = %d of %d or %4.2f%% perturbance = %5.2f%%\n",success, NUM_TESTS, rate*100, PERTURBANCE);
    }


}

