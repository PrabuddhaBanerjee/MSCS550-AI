/**
 *Copyright (c) Prabuddha Banerjee
 *
 */
package javaai.aann;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The ML1 program extends the BaseML class. This program is for loading data in memomry from iris.csv file.
 * This is done for classification purpose and test the data by Hashmap.
 * @author Prabuddha Banerjee
 * @version 1.0
 * @since   2021-08-19
 */
public class ML1 extends BaseML {

    /*
     * Loads the data from iris.csv file into inherited variables measures and flowers,
     * thereafter stores it in target field which is a Hashmap.
     * @param target: This is only parameter to load method, used to store value/ data.
     * @return: nothing
     */
    public static void load(Map<Measure, Species> target) {

        /** Loads iris.csv file data*/
        BaseML.load();

        /** Loads values into target field*/
        for(int k=0; k < measures.size(); k++){
            target.put(measures.get(k), flowers.get(k));
        }
    }

    /**
     * This is the main method which instantiates map and makes use of load method.
     * Further, there is a for loop which prints the value of measures present in both list and Hashmap.
     * @param args Unused.
     * @return Nothing.
     */
    public static void main(String[] args){

        /** Instantiate measure to speices map */
        Map<Measure, Species> map = new HashMap<>();

        /** Populate map */
        load(map);

        /** List of measures for test case */
        List<Measure> tests = new ArrayList<>(Arrays.asList(measures.get(93), measures.get(69), measures.get(149),
                measures.get(1), measures.get(120)));

        /** examine and print the classifications */
        for(Measure measure: tests) {
            System.out.println(map.get(measure) + " " + measure);
        }
    }
}
